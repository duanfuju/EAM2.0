package com.tiansu.eam.modules.maintain.service;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.modules.employee.dao.EamUserExtDao;
import com.tiansu.eam.modules.inspection.service.InspectionTaskService;
import com.tiansu.eam.modules.maintain.dao.MaintainTaskDao;
import com.tiansu.eam.modules.maintain.entity.MaintainStatusEnum;
import com.tiansu.eam.modules.schedual.dao.SchedualDao;
import com.tiansu.eam.modules.schedual.entity.Schedual;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author caoh
 *
 * @description 根据之前选择的派单方式，分配给具体的人，并做对应的接单/转单/撤销操作
 * @create 2017-11-2 17:13
 **/
public class SetParticipantForActivity implements TaskListener {

    // 获取任务实体表的dao层
    private MaintainTaskDao maintainTaskDao = SpringContextHolder.getBean(MaintainTaskDao.class);

    // EAM工作人员相关信息
    private EamUserExtDao eamUserExtDao = SpringContextHolder.getBean(EamUserExtDao.class);

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
        //String disp_type = (String) delegateTask.getVariable("disp_type");
        // 要参与节点的参加者
        List<String> users = new ArrayList<>();
        // 根据流程id获取当前任务
        Map map = maintainTaskDao.getMaintainTaskByPiid(piid);
        // 当前巡检任务状态
        String taskStatus = map.get("task_status").toString();
        // 当前巡检任务期望解决时间
        String task_time_plan = map.get("task_time_plan").toString();

        if(MaintainStatusEnum.PENDING_CONF.value().equals(taskStatus)) {   // 待接单
            String processor_plan = map.get("task_act_processor_plan").toString();
            users= Arrays.asList(processor_plan.split(","));

        } else if (MaintainStatusEnum.PENDING_PROCESS.value().equals(taskStatus)){     // 任务状态：1-进行中
            // 执行任务的责任人是当前接单人
            String loginname = map.get("task_act_processor").toString();
            users.add(loginname);
        } else if(MaintainStatusEnum.HANGUP.value().equals(taskStatus)){     // 任务状态：5-已挂起
            String loginname = map.get("task_act_processor").toString();
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

        return null;
    }

    /**
     * 根据排班表，获取当前值班人员
     * @return
     */
    private List<Map> getCurrentSchedualEmps(String time_plan) {
        List<Map> schedualList = maintainTaskDao.getScheduleByTaskDate(time_plan);
        return schedualList;
    }

}
