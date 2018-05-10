package com.tiansu.eam.modules.maintain.service;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.modules.inspection.dao.InspectionTaskDao;
import com.tiansu.eam.modules.inspection.entity.InspectionTaskSwitch;
import com.tiansu.eam.modules.maintain.dao.MaintainTaskDao;
import com.tiansu.eam.modules.maintain.entity.MaintainStatusEnum;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author caoh
 * @description 巡检任务审批操作及结果
 * @create 2017-11-2 17:20
 **/
public class ActMaintainApproval implements TaskListener {
    // 审批结果  1 通过 2退回
    public static final String APPROVE_SUCCESS = "1";
    public static final String APPROVE_FAIL = "2";

    private MaintainTaskDao maintainTaskDao = SpringContextHolder.getBean(MaintainTaskDao.class);
    private Expression approveReason;
    private Expression deptLeaderApproved;
    private Expression pIid;
    private Expression switch_person;
    private Expression first_person;

    public void notify(DelegateTask delegateTask) {
        String approve = (String) deptLeaderApproved.getValue(delegateTask);
        // 获取流程id
        String piid = delegateTask.getProcessInstanceId();
        String appReason = (String)approveReason.getValue(delegateTask);
        String switchPerson = (String) delegateTask.getVariable("switch_person");
        //String switchPerson = (String)switch_person.getValue(delegateTask);


        String approve_by = UserUtils.getUser().getLoginname();
        String now_status = (String)delegateTask.getVariable("task_status");
       // String inspectionTask_id = inspectionTaskDao.getInspectionIdByPIid(piid).get("id").toString();
        //InspectionTaskSwitch inspectionTaskSwitch = inspectionTaskDao.getInspectionTaskSwitch(inspectionTask_id);
        Map<String,Object> map = new HashMap<String,Object>();
        if(approve !=null && !"".equals(approve)){
            if(approve.equals("true")){
               // map.put("switch_result", APPROVE_SUCCESS);
                if(now_status.equals(MaintainStatusEnum.PENDING_CANCEL.value())) {  // 申请撤销
                    map.put("task_status", MaintainStatusEnum.CANCELED.value());
                } else if (now_status.equals(MaintainStatusEnum.PENDING_TRANSFER.value())) {  // 申请转单
                    map.put("task_status", MaintainStatusEnum.PENDING_CONF.value());
                    map.put("task_act_processor_plan", switchPerson);
                } else if (now_status.equals(MaintainStatusEnum.PENDING_HANGUP.value())) {  // 申请挂起
                    map.put("task_status", MaintainStatusEnum.HANGUP.value());
                }
                //设置流程转向
                delegateTask.setVariable("approved","1");
            }else{
               // map.put("switch_result", APPROVE_FAIL);
                if(now_status.equals(MaintainStatusEnum.PENDING_CANCEL.value())) {  // 申请撤销
                    map.put("task_status", MaintainStatusEnum.PENDING_CONF.value());
                } else if (now_status.equals(MaintainStatusEnum.PENDING_TRANSFER.value())) {  // 申请转单
                    map.put("task_status", MaintainStatusEnum.PENDING_PROCESS.value());     // 进行中
                    //如果不同意转单，直接将处理人指派为转单申请人
                    String firstPerson = (String)first_person.getValue(delegateTask);
                    map.put("task_act_processor", firstPerson);
                } else if (now_status.equals(MaintainStatusEnum.PENDING_HANGUP.value())) {  // 申请挂起
                    map.put("task_status", MaintainStatusEnum.PENDING_PROCESS.value());
                }
                //设置流程转向
                delegateTask.setVariable("approved","0");
            }
        }
        //map.put("update_by",approve_by);
        //map.put("approve_reason",appReason);
        map.put("pstid",piid);
        //map.put("inspectiontask_id", inspectionTask_id);
        //String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //map.put("update_time",time);

        //inspectionTaskSwitch = getInspectionTaskSwitch(map);
        //inspectionTaskDao.updateSwitchByInsId(inspectionTaskSwitch);
        maintainTaskDao.updateMaintainTaskByPiid(map);

    }

}
