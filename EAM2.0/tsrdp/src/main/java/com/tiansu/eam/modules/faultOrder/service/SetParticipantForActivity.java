package com.tiansu.eam.modules.faultOrder.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.modules.device.dao.EamDeviceDao;
import com.tiansu.eam.modules.device.entity.Device;
import com.tiansu.eam.modules.device.service.EamDeviceService;
import com.tiansu.eam.modules.employee.dao.EamUserExtDao;
import com.tiansu.eam.modules.faultOrder.controller.FaultOrderController;
import com.tiansu.eam.modules.faultOrder.dao.FaultOrderDao;
import com.tiansu.eam.modules.faultOrder.entity.FaultOrder;
import com.tiansu.eam.modules.schedual.dao.SchedualDao;
import com.tiansu.eam.modules.schedual.entity.Schedual;
import com.tiansu.eam.modules.sys.dao.EamRoleDao;
import com.tiansu.eam.modules.sys.service.EamRoleService;
import com.tiansu.eam.modules.sys.service.EamSystemService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wangjl
 * @description 根据当前不同步骤 设定参与者
 * @create 2017-10-16 10:02
 **/
public class SetParticipantForActivity implements TaskListener {
    Log logger = LogFactory.getLog(SetParticipantForActivity.class);
    private Expression  activityName;

    private FaultOrderDao faultOrderDao = SpringContextHolder.getBean(FaultOrderDao.class);

    private EamUserExtDao eamUserExtDao = SpringContextHolder.getBean(EamUserExtDao.class);

    private EamDeviceDao eamDeviceDao = SpringContextHolder.getBean(EamDeviceDao.class);

//    private RepositoryService repositoryService = SpringContextHolder.getBean(RepositoryService.class);

    private EamRoleDao eamRoleDao = SpringContextHolder.getBean(EamRoleDao.class);

    private SchedualDao schedualDao = SpringContextHolder.getBean(SchedualDao.class);

    private EamDeviceService deviceService = SpringContextHolder.getBean(EamDeviceService.class);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public void notify(DelegateTask delegateTask) {

        String activityNameStr = activityName.getExpressionText();
        //        获取报修表单数据
        FaultOrder cachedFaultOrder = (FaultOrder) delegateTask.getVariable(FaultOrderController.NOTIFY_FORM_DATA);
        FaultOrder faultOrder = faultOrderDao.get(cachedFaultOrder.getId());
        if(faultOrder == null){
            faultOrder = cachedFaultOrder;
        }
        List<String> participants = new ArrayList<>();
        if("待接单".equals(activityNameStr)){
            String disp_type = (String) delegateTask.getVariable("disp_type");
            logger.debug("派单模式:"+disp_type);
            if(EAMConsts.MANUAL_DISP_TYPE.equals(disp_type)){
                participants = getManualDispOrderPartis(faultOrder);
                if(participants != null && participants.size() > 0){
                    delegateTask.addCandidateUsers(participants);  //指定任务候选用户
                }
            }else if(EAMConsts.AUTO_DISP_TYPE.equals(disp_type)){
                participants = getAutoDispPartis(faultOrder);
                if(participants != null && participants.size() > 0){
                    delegateTask.addCandidateUsers(participants);  //指定任务候选用户
                }else{
                    participants = getYwLeaderRole();
                    delegateTask.addCandidateGroups(participants);
                }
            }else if(EAMConsts.GRAB_DISP_TYPE.equals(disp_type)){
                //抢单
                participants = getQrabDispOrderPartis(faultOrder);
                //获取某人名下未完成且未挂起的单子数，若超过配置项，则不能再接单 delegateTask.setVariable();
//                TODO
                //运维班长转下来的单子 ，不能再转单  delegateTask.setVariable();
                if(participants != null && participants.size() > 0){
                    delegateTask.addCandidateUsers(participants);  //指定任务候选用户
                }else{
                    participants = getYwLeaderRole();
                    delegateTask.addCandidateGroups(participants);
                }
            }
        }else if("待到达".equals(activityNameStr) || "申请挂起".equals(activityNameStr) || "待反馈".equals(activityNameStr)
                || "解挂".equals(activityNameStr) || "处理工单".equals(activityNameStr)){
            participants.add(getActualOrderDealer(faultOrder));
            if(participants != null && participants.size() > 0){
                delegateTask.addCandidateUsers(participants);  //指定任务候选用户
            }
        }else if("待评价".equals(activityNameStr)){
            participants.add(faultOrder.getCreateBy().getLoginname());
            if(participants != null && participants.size() > 0){
                delegateTask.addCandidateUsers(participants);  //指定任务候选用户
            }
        }else {

        }

        logger.debug("参与者:"+delegateTask.getProcessInstanceId()+":"+activityNameStr+"---"+ delegateTask.getCandidates());

        //TODO:设置超级用户处理权限
        List<String> us = new ArrayList<>();
        us.add("admin");
        delegateTask.addCandidateUsers(us);




    }

    /**
     * 获取当前工单实际处理人
     * @return
     */
    public String getActualOrderDealer(FaultOrder faultOrder){
        if(faultOrder!=null && faultOrder.getOrder_solver() != null){
            return faultOrder.getOrder_solver().getLoginname();
        }
        return "";
    }



    /**
     * 获取自动派单参与者
     * 自动派单是指在报修派单环节，由本系统根据已经设定的规则，将报修的工单自动推送到具体的运维人员待办任务清单里。自动派单的具体规则：通过专业、责任区域、排班表3要素进行派单。</span>
     <br>
     <span id="content_right_span2">步骤一，根据报修设备，判断设备归属专业，找到所有该专业的运维人员。（人员管理功能里专业为必填）
     <br>步骤二，根据报修设备位置，匹配该专业下具有此责任区域的运维人员。（人员管理功能里责任区域为必填）。
     <br>步骤三，根据排班表，匹配出当前在班人员。
     <br>步骤四，如果在前三个判断条件中，未找到相关的派单对象，则派给相关专业（报修设备所归属的专业）运维班长和主管，由班长或主管进行转单操作。
     <br>班长或主管进行转单操作：是指针派给运维班长和主管的单子，由班长或主管进行接单人员（可以看到全部运维人员）的选择，从而完成转单操作。
     * @return
     */
    public List<String> getAutoDispPartis(FaultOrder faultOrder){
        //Step1:找到同专业且对应责任区域的运维人员
        Map paramMap = new HashMap();
        String devMajor = deviceService.get(faultOrder.getOrder_device().getId()).getDev_major();
        if(StringUtils.isEmpty(devMajor)){
            return null;
        }
        paramMap.put("major",devMajor);
        paramMap.put("dbName", Global.getConfig("jdbc.type"));
        paramMap.put("duty_area",faultOrder.getNotifier_loc().getId());
        //TODO 责任区域判断
//        1获取设备位置；
//        2获取该位置及其所有上级位置；loc_seq
//        3获取该设备位置及其所有上级位置的责任人（因为责任人中存的是单个位置节点）



        List<Map> majorEmps = eamUserExtDao.findListByMap(paramMap);
        //Step2: 根据排班表，匹配出当前在班人员。
        List<Schedual> schedualEmps = getCurrentSchedualEmps();
//        Step3: 取前两步数据交集，如果为空，则转给运维主管或运维班长
        List<String> resultLst = new ArrayList<>();
        for(Map majorEmp : majorEmps){
            for(Schedual schedualEmp : schedualEmps){
                if(schedualEmp.getSchedual_emp().equals(majorEmp.get("loginname"))){
                    resultLst.add(schedualEmp.getSchedual_emp());
                }
            }
        }
        return resultLst;
    }

    /**
     * 根据排班表，获取当前值班人员
     * @return
     */
    private List<Schedual> getCurrentSchedualEmps() {
        //获取当天值班人员
        List<Schedual> schedualList = schedualDao.getSchedualsByDate(sdf.format(new Date()));
        //TODO 当前时间在值班人员值班时间期间(24小时排班制，跨天排班分成2班)
        return schedualList;
    }

    /**
     * 获取手动派单参与者
     * 手动派单是指在报修派单环节，由派单用户手动选择接单人员进行工单派单；接单人员：是指可供选择的人员，在本系统中为报修设备所归属专业的。
     * @return
     */
    public List<String> getManualDispOrderPartis(FaultOrder faultOrder){
        //获取指定的接单人
        List<String> partis = new ArrayList<>();
        if(!StringUtils.isEmpty(faultOrder.getOrder_receiver())){
            partis.add(faultOrder.getOrder_receiver());
        }
        return partis;
    }

    /**
     * 获取自动抢单参与者
     * 派单+抢单是指报修以后的工单，根据设备归属的专业，匹配出所有该专业的运维人员，该专业的所有人员都可以看到该单据，且可以进行抢单。</span>
     <br>
     <span id="content_right_span4">运维人员抢到的单子只能做接单操作，不能做转单操作。且最多只能接<input style="color:rgb(0,0,255); width:40px;" type="text" id="max_orders" value="5">单，
     除非已完成或已挂起，否则在抢单模式下不可再接单。单据在一定时间内（默认<input style="color:rgb(0,0,255);width:40px;" type="text" value="30" id="max_timeout">秒）
     没有人接的单据，自动派给这个专业的运维主管和运维班长，由运维主管或运维班长进行转单。运维主管或运维班长只能派单，不能拒绝，对于运维主管和运维班长派下来的单子，运维人员（技师）只能接单，不能转单。</span>

     * @return
     */
    public List<String> getQrabDispOrderPartis(FaultOrder faultOrder){
        Map paramMap = new HashMap();
        String devMajor = deviceService.get(faultOrder.getOrder_device().getId()).getDev_major();
        if(StringUtils.isEmpty(devMajor)){
            return null;
        }
        paramMap.put("dbName", Global.getConfig("jdbc.type"));
        paramMap.put("major",devMajor);
        List<Map> majorEmps = eamUserExtDao.findListByMap(paramMap);
        List<String> resultLst = new ArrayList<>();
        for(Map majorEmp : majorEmps){
            resultLst.add((String) majorEmp.get("loginname"));
        }
        return resultLst;
    }

    /**
     * 查询运维班长和运维主管的角色编码
     * @return
     */
    public List<String> getYwLeaderRole(){
        List<String> roles = Lists.newArrayList();
        Map paramMap = new HashMap();
        //运维主管和运维班长为系统预置角色，此处可以写死
        paramMap.put("rolenames","'运维主管','运维班长'");
        List<Map> roleMap = eamRoleDao.findListByMap(paramMap);
        for(Map role : roleMap){
            roles.add((String) role.get("rolecode"));
        }
        return roles;
    }

}
