package com.tiansu.eam.modules.inspection.service;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.modules.inspection.dao.InspectionTaskDao;
import com.tiansu.eam.modules.inspection.entity.InspectionTaskSwitch;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wujh
 * @description 根据列表中显示的按钮做相应的操作
 * @create 2017-10-16 20:10
 **/
@Service
@Transactional
public class ActInspectionTaskActivity implements TaskListener {
    // 获取任务实体表的service层
    private InspectionTaskDao inspectionTaskDao = SpringContextHolder.getBean(InspectionTaskDao.class);

    private Expression result;    //当前任务状态
    private Expression requestReason;   // 申请撤销/转单的原因
    private Expression switch_person;    // 接单人

    @Override
    public void notify(DelegateTask delegateTask) {
        // 获取流程id
        String piid = delegateTask.getProcessInstanceId();

        // 当前流程处理的巡检任务
        Map taskObj = inspectionTaskDao.getInspectionIdByPIid(piid);

        // 转单的接单人
        String switch_person = (String) delegateTask.getVariable("switch_person");
        // 获取当前操作按钮信息
        String transferSelect = (String) delegateTask.getVariable("result");

        String inspectionId = taskObj.get("id").toString();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("inspectiontask_id", inspectionId);
        map.put("pstid",piid);
        String requestReason = (String) delegateTask.getVariable("requestReason");
        map.put("switch_reason",requestReason);
        map.put("update_by",delegateTask.getAssignee());
        map.put("update_time",new Date());
        map.put("flag", transferSelect);
        if(transferSelect.equals(EAMConsts.CANCEL_APPLY)) {     // 申请撤销3
            map.put("switch_status", EAMConsts.INSPECTION_SWITCH_CANCEL);   // switch表中申请撤销
            map.put("task_status", EAMConsts.CANCLE_STATUS);  // 设置任务状态
            //设置流程转向
            delegateTask.setVariable("task_status",EAMConsts.CANCLE_STATUS);
        } else if(transferSelect.equals(EAMConsts.TRANSFER_APPLY)){     // 申请转单2
            if(switch_person != null && !switch_person.equals("")) {
                map.put("switch_person",switch_person);
            }
            map.put("switch_status", EAMConsts.INSPECTION_SWITCH_TRANSFER);   // switch表中申请撤销
            map.put("task_status", EAMConsts.TRANSFER_STATUS);  // 设置任务状态
            //设置流程转向
            delegateTask.setVariable("task_status",EAMConsts.TRANSFER_STATUS);
        } else if(transferSelect.equals(EAMConsts.HANGUP_APPLY)) { // 4
            map.put("switch_status", EAMConsts.INSPECTION_SWITCH_HANGUP);   // switch表中申请挂起
            map.put("task_status", EAMConsts.HANGUP_STATUS);   // switch表中申请挂起
            //设置流程转向
            delegateTask.setVariable("task_status",EAMConsts.HANGUP_STATUS);
        } else if(transferSelect.equals(EAMConsts.ACCEPT_APPLY)){   // 接单 1
            map.put("task_status", EAMConsts.BE_ON);   // 任务状态为进行中
            map.put("task_processor", delegateTask.getAssignee());
            map.put("task_time_begin", new Date());    // 接单任务开始时间
            //设置流程转向
            delegateTask.setVariable("task_status",EAMConsts.BE_ON);
        } else if(transferSelect.equals(EAMConsts.UNHANG_APPLY) || transferSelect.equals(EAMConsts.FEEDBACK_APPLY)) {   // 解挂 6 反馈 5
            map.put("task_status", EAMConsts.BE_ON);   // 任务状态为进行中
            map.put("task_processor", delegateTask.getAssignee());
            //设置流程转向
            delegateTask.setVariable("task_status",EAMConsts.BE_ON);
        }
        if(!transferSelect.equals(EAMConsts.ACCEPT_APPLY) && !transferSelect.equals(EAMConsts.FEEDBACK_APPLY)) {
            InspectionTaskSwitch inspectionTaskSwitch = getInspectionTaskSwitch(delegateTask,map);
            if(transferSelect.equals(EAMConsts.UNHANG_APPLY)) {
                inspectionTaskDao.updateRelieveTimeByInsId(inspectionTaskSwitch);
            } else {
                inspectionTaskDao.insertTaskSwitch(inspectionTaskSwitch);
            }
        }
        inspectionTaskDao.updateAprByPIid(map);
    }

    /**
     * 根据从流程传参，拼接流程任务实例
     * @param map
     * @return
     */
    public InspectionTaskSwitch getInspectionTaskSwitch(DelegateTask delegateTask, Map map){
        InspectionTaskSwitch inspectionTaskSwitch = new InspectionTaskSwitch();
        if(map.get("flag").equals(EAMConsts.UNHANG_APPLY)){
            inspectionTaskSwitch.setInspectiontask_id(map.get("inspectiontask_id").toString());  // 巡检任务id
            inspectionTaskSwitch.setRelieve_time(new Date());   // 设置解挂时间
        } else {
            inspectionTaskSwitch.setId_key(IdGen.uuid());
            inspectionTaskSwitch.setSwitch_status(map.get("switch_status").toString());   // 转换状态：0-转单 1-挂起 2-撤销
            if(map.get("task_status").equals(EAMConsts.TRANSFER_STATUS)){      // 申请转单
                inspectionTaskSwitch.setSwitch_person(map.get("switch_person").toString());
            }
            inspectionTaskSwitch.setSwitch_time(new Date());
            inspectionTaskSwitch.setInspectiontask_id(map.get("inspectiontask_id").toString());
            inspectionTaskSwitch.setSwitch_reason(map.get("switch_reason").toString());
            inspectionTaskSwitch.setApply_person(delegateTask.getAssignee());
        }
        return inspectionTaskSwitch;
    }
}
