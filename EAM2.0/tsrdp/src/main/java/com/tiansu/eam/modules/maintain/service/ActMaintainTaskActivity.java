package com.tiansu.eam.modules.maintain.service;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.modules.inspection.dao.InspectionTaskDao;
import com.tiansu.eam.modules.inspection.entity.InspectionTaskSwitch;
import com.tiansu.eam.modules.maintain.dao.MaintainTaskDao;
import com.tiansu.eam.modules.maintain.entity.MaintainStatusEnum;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author caoh
 * @description 根据列表中显示的按钮做相应的操作
 * @create 2017-11-02 20:10
 **/
public class ActMaintainTaskActivity implements TaskListener {
    // 获取任务实体表的service层
    private MaintainTaskDao maintainTaskDao = SpringContextHolder.getBean(MaintainTaskDao.class);

    private Expression pIid;   // 当前工作流id
    private Expression result;    //当前任务状态
    private Expression requestReason;   // 申请撤销/转单的原因
    private Expression switch_person;    // 接单人

    @Override
    public void notify(DelegateTask delegateTask) {
        // 获取流程id
        String piid = delegateTask.getProcessInstanceId();

        // 当前流程处理的巡检任务
        String taskId = maintainTaskDao.getMaintainTaskByPiid(piid).get("id").toString();

        // 转单的接单人
        String switch_person = (String) delegateTask.getVariable("switch_person");
        // 获取当前操作按钮信息
        String transferSelect = (String) delegateTask.getVariable("result");
        //请求原因
        String requestReason = (String) delegateTask.getVariable("requestReason");
        // 当前巡检任务状态
        //String taskStatus = taskObj.get("task_status").toString();
        //String inspectionId = taskObj.get("id").toString();
        Map<String,Object> map = new HashMap<String,Object>();
        //map.put("inspectiontask_id", taskId);
        map.put("pstid",piid);

        //map.put("switch_reason",requestReason);
        //map.put("update_by",UserUtils.getUser().getLoginname());
        //map.put("update_time",new Date());

        if(transferSelect.equals(MaintainStatusEnum.PENDING_CANCEL.value())) {     // 申请撤销
            //map.put("switch_status", EAMConsts.INSPECTION_SWITCH_CANCEL);   // switch表中申请撤销
            map.put("task_status", MaintainStatusEnum.PENDING_CANCEL.value());  // 设置任务状态
            //设置流程转向
            delegateTask.setVariable("task_status",MaintainStatusEnum.PENDING_CANCEL.value());

        } else if(transferSelect.equals(MaintainStatusEnum.PENDING_TRANSFER.value())){     // 申请转单
            if(switch_person != null && !switch_person.equals("")) {
                map.put("switch_person",switch_person);
            }
            //map.put("switch_status", EAMConsts.INSPECTION_SWITCH_TRANSFER);
            map.put("task_status", MaintainStatusEnum.PENDING_TRANSFER.value());  // 设置任务状态
            //设置流程转向
            delegateTask.setVariable("task_status",MaintainStatusEnum.PENDING_TRANSFER.value());
            //将当前申请人存入流程数据
            delegateTask.setVariable("first_person",UserUtils.getUser().getLoginname());
        } else if(transferSelect.equals(MaintainStatusEnum.PENDING_HANGUP.value())) {
            //map.put("switch_status", EAMConsts.INSPECTION_SWITCH_HANGUP);   // switch表中申请挂起
            map.put("task_status", MaintainStatusEnum.PENDING_HANGUP.value());   // switch表中申请挂起
            //设置流程转向
            delegateTask.setVariable("task_status",MaintainStatusEnum.PENDING_HANGUP.value());
        } else if(transferSelect.equals(MaintainStatusEnum.PENDING_PROCESS.value())){   // 接单
            map.put("task_status", MaintainStatusEnum.PENDING_PROCESS.value());   // 任务状态为处理中
            map.put("task_act_processor", UserUtils.getUser().getLoginname());
            //设置流程转向
            delegateTask.setVariable("task_status",MaintainStatusEnum.PENDING_PROCESS.value());
        }else if(transferSelect.equals(MaintainStatusEnum.PENDING_UN_HANGUP.value())){  //解挂
            //设置任务状态为处理中
            map.put("task_status", MaintainStatusEnum.PENDING_PROCESS.value());
            delegateTask.setVariable("task_status",MaintainStatusEnum.PENDING_PROCESS.value());
        } else {
            //add by wangr start 2017年11月13日17:38:50
            //确认反馈
            //设置任务状态为处理中
            map.put("task_status", MaintainStatusEnum.PENDING_PROCESS.value());
            delegateTask.setVariable("task_status",MaintainStatusEnum.PENDING_PROCESS.value());
            //add by wangr end 2017年11月13日17:38:50
        }
//        if(!transferSelect.equals(EAMConsts.ACCEPT_APPLY)) {
//            InspectionTaskSwitch inspectionTaskSwitch = getInspectionTaskSwitch(map);
//            //maintainTaskDao.insertTaskSwitch(inspectionTaskSwitch);
//        }

        //根据流程实例id更新task表
        maintainTaskDao.updateMaintainTaskByPiid(map);
    }

}
