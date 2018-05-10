package com.tiansu.eam.modules.maintain.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.act.service.EamProcessService;
import com.tiansu.eam.modules.faultOrder.controller.FaultOrderController;
import com.tiansu.eam.modules.faultOrder.entity.*;
import com.tiansu.eam.modules.maintain.entity.*;
import com.tiansu.eam.modules.maintain.service.MaintainTaskService;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.activiti.engine.FormService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author caoh
 * @description
 * @create 2017-10-30 15:20
 **/
@Controller
@RequestMapping(value = "${adminPath}/eam/maintainTask")
public class MaintainTaskController extends BaseController {
    @Autowired
    private MaintainTaskService maintainTaskService;

    @Autowired
    private EamProcessService eamProcessService;    // 工作进程service

    @Autowired
    FormService formService;

    @Autowired
    private TaskService taskService;

    //反馈表单数据key
    public static final String FEEDBACK_FORM_DATA = "FEEDBACK_FORM_DATA";

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "insert")
    @Transactional
    public String insert(MaintainTask maintainTask,HttpServletRequest request) {


        String userName = UserUtils.getUser().getLoginname();
        //maintainTask.setMaterialemp(userName);
        //生成主键
        String uuid = IdGen.uuid();
        maintainTask.setId_key(uuid);

        String pstid = eamProcessService.startProcessByPdid("maintainFlow","eam_maintain_task",uuid,request);
        if("timeout".equals(pstid)){
            return "timeout";
        }
        maintainTask.setPstid(pstid);
        maintainTaskService.insert(maintainTask);
        return "success";
    }


    /**
     * @creator caoh
     * @createtime 2017-11-2 10:05
     * @description: 获取保养任务关联的保养内容
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "findMaintainProject")
    public List<Map> findMaintainProject(){
        String id = getPara("id");
       return  maintainTaskService.findMaintainProject(id);
    }

    /**
     * @creator caoh
     * @createtime 2017-11-2 10:05
     * @description: 获取反馈页面数据
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getFeedBackDatas")
    public Map getFeedBackDatas(){
        String id = getPara("id");
        return  maintainTaskService.getFeedBackDatas(id);
    }


    /**
     * @creator caoh
     * @createtime 2017-11-01 15:43
     * @description: 保养反馈提交
     * @param request
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "feedBack")
    @Transactional
    public String feedBack(HttpServletRequest request) {
        String param=getPara("param");
        String taskId = getPara("taskid");//工作流任务id
        String userId = UserUtils.getUser().getLoginname();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        // 权限检查-任务的办理人和当前人不一致不能完成任务
        if (!userId.equals(task.getAssignee())) {
            return "noauth";
        }
        //获取报修表单数据
       // FaultOrder faultNotifyOrder = (FaultOrder) taskService.getVariable(taskId, FaultOrderController.NOTIFY_FORM_DATA);
        //将序列化的json字符串转为实体
        MaintainTask maintainTask= JSON.parseObject(param,MaintainTask.class);
        Map formValues = new HashMap();
        formValues.put(MaintainTaskController.FEEDBACK_FORM_DATA,param);


        //faultOrder.setId(faultOrder.getId());
        maintainTask.preUpdate();
        maintainTask.setTask_status(OrderStatusEnum.FINISHED.value());
        maintainTask.setTask_time_finish(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        List<MaintainTool> newMaintainToolList = Lists.newArrayList();
        List<MaintainAttachment> newMaintainSparepartList = Lists.newArrayList();
        List<MaintainEmp> newMaintainPersonList = Lists.newArrayList();
        List<MaintainOther> newMaintainOthersList = Lists.newArrayList();

        List<MaintainTool> maintainToolList=maintainTask.getToolList();//工器具
        for(MaintainTool s:maintainToolList){
            if(s == null || StringUtils.isEmpty(s.getTool_id())){
                continue;
            }
            s.setId(IdGen.uuid());
            newMaintainToolList.add(s);
        }
        List<MaintainAttachment> maintainAttachmentList=maintainTask.getAttachmentList();//备品备件
        for(MaintainAttachment s:maintainAttachmentList){
            if(s == null || StringUtils.isEmpty(s.getAttachment_id())){
                continue;
            }
            s.setId(IdGen.uuid());
            newMaintainSparepartList.add(s);
        }
        List<MaintainEmp> maintainPersonList=maintainTask.getEmpList();//人员工时
        for(MaintainEmp s:maintainPersonList){
            if(s == null || StringUtils.isEmpty(s.getEmp_id())){
                continue;
            }
            s.setId(IdGen.uuid());
            newMaintainPersonList.add(s);
        }
        List<MaintainOther> maintainOtherList=maintainTask.getOtherList();//其他费用
        for(MaintainOther s:maintainOtherList){
            if(s == null || StringUtils.isEmpty(s.getCharge_name())){
                continue;
            }
            s.setId(IdGen.uuid());
            newMaintainOthersList.add(s);
        }
        maintainTask.setToolList(newMaintainToolList);
        maintainTask.setAttachmentList(newMaintainSparepartList);
        maintainTask.setEmpList(newMaintainPersonList);
        maintainTask.setOtherList(newMaintainOthersList);

        formService.submitTaskFormData(taskId, formValues);

        maintainTaskService.update(maintainTask);
        maintainTaskService.insertActualDetail(newMaintainToolList,newMaintainSparepartList,newMaintainPersonList,newMaintainOthersList);

        return "success";
    }
}
