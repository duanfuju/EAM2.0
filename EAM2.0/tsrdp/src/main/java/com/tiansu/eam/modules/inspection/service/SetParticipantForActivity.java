package com.tiansu.eam.modules.inspection.service;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.modules.employee.service.EamUserExtService;
import com.tiansu.eam.modules.schedual.dao.SchedualDao;
import com.tiansu.eam.modules.schedual.entity.Schedual;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wujh
 * @description 根据之前选择的派单方式，分配给具体的人，并做对应的接单/转单/撤销操作
 * @create 2017-10-16 17:13
 **/
@Service
@Transactional
public class SetParticipantForActivity implements TaskListener {

    // 获取任务实体表的service层
    private InspectionTaskService inspectionTaskService = SpringContextHolder.getBean(InspectionTaskService.class);

    // EAM工作人员相关信息
    private EamUserExtService eamUserExtService = SpringContextHolder.getBean(EamUserExtService.class);

    // 排班表接口
    private SchedualDao schedualDao = SpringContextHolder.getBean(SchedualDao.class);

    // 当前时间
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private String piid;     // 流程id

    @Override
    public void notify(DelegateTask delegateTask) {

        // 获取流程id
        piid = delegateTask.getProcessInstanceId();
        // 获取当前的派单方式
        String disp_type = (String) delegateTask.getVariable("disp_type");
        // 要参与节点的参加者
        List<String> users = new ArrayList<>();

        // 根据流程id获取当前任务
        Map map = inspectionTaskService.getInspectionTaskByPIid(piid);
        // 当前巡检任务状态
        String taskStatus = map.get("task_status").toString();

        if(EAMConsts.TO_BE_CONFIRMED.equals(taskStatus)) {   // 待接单
            // 获取当前节点所参与者，从巡检路线的人员表取，或者新生成任务的计划巡检认来
            if(EAMConsts.MANUAL_DISP_TYPE.equals(disp_type)) {
                // 获取巡检任务责任人
                String loginname = map.get("task_processor_plan").toString();
                users.add(loginname);
            } else if(EAMConsts.AUTO_DISP_TYPE.equals(disp_type)){
                // 根据排班表派单
                users = getAutoDispPartis(map);
            }
        } else if (EAMConsts.BE_ON.equals(taskStatus)){     // 任务状态：1-进行中
            // 执行任务的责任人是当前接单人
            String loginname = map.get("task_processor").toString();
            users.add(loginname);
        } else if(EAMConsts.HANGUPED_STATUS.equals(taskStatus)){     // 任务状态：5-已挂起
            String loginname = map.get("task_processor").toString();
            users.add(loginname);
        }
        if(users != null && users.size() > 0){
            delegateTask.addCandidateUsers(users);     // 给当前节点设置参与人
        }
        delegateTask.setVariable("task_status", taskStatus);
    }

    /**
     * 获取自动派单参与者
     * 自动派单是指在报修派单环节，根据排班表以及责任区域和专业推送到具体的运维人员待办任务清单里。自动派单的具体规则：通过专业、责任区域、排班表3要素进行派单。</span>
     <br>
     <span id="content_right_span2">步骤一，根据报修设备，判断设备归属专业，找到所有该专业的运维人员。（人员管理功能里专业为必填）
     <br>步骤二，根据报修设备位置，匹配该专业下具有此责任区域的运维人员。（人员管理功能里责任区域为必填）。
     <br>步骤三，根据排班表，匹配出当前在班人员。
     <br>步骤四，如果在前三个判断条件中，未找到相关的派单对象，则派给责任人。
     * @return
     */
    public List<String> getAutoDispPartis(Map inspectionTask){
        // Step1:找到当前任务下的设备的归属专业所属责任人
        Map param = new HashMap();
        param.put("pstid", piid);
        List<Map> mapList = inspectionTaskService.getDeviceInfos(param);
        List<String> majorList = new ArrayList<>();
        List<String> devLocList = new ArrayList<>();
        if(mapList != null && mapList.size() > 0) {
            for(int i = 0; i < mapList.size(); i++){
                Map result = mapList.get(i);
                if(!majorList.contains(result.get("dev_major").toString())){
                    majorList.add(result.get("dev_major").toString());
                }
                if(!devLocList.contains(result.get("id").toString())){
                    devLocList.add(result.get("id").toString());
                }
            }
        }
        Map paramMap = new HashMap();
        paramMap.put("majorList",majorList);
        paramMap.put("dutyAreaList",devLocList);
        List<Map> majorEmps = eamUserExtService.findListByMapForInterface(paramMap);

        // Step2: 根据排班表，匹配出当前在班人员。
        List<Schedual> schedualEmps = getCurrentSchedualEmps();

        // Step3: 取前两步数据交集，如果为空，则 获取设备归属负责人
        List<String> resultLst = new ArrayList<>();
        for(Map majorEmp : majorEmps){
            for(Schedual schedualEmp : schedualEmps){
                if(schedualEmp.getSchedual_emp().equals(majorEmp.get("loginname"))){
                    resultLst.add(schedualEmp.getSchedual_emp());
                }
            }
        }
        if(resultLst.size()==0){
            String loginname = inspectionTask.get("task_processor_plan").toString();
            resultLst.add(loginname);
        }
        return resultLst;
    }

    /**
     * 根据排班表，获取当前值班人员
     * @return
     */
    private List<Schedual> getCurrentSchedualEmps() {
        List<Schedual> schedualList = schedualDao.getSchedualsByDate(sdf.format(new Date()));
        return schedualList;
    }

}
