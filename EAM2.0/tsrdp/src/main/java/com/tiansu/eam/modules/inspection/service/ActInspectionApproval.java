package com.tiansu.eam.modules.inspection.service;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.modules.inspection.dao.InspectionTaskDao;
import com.tiansu.eam.modules.inspection.entity.InspectionTaskSwitch;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wujh
 * @description 巡检任务审批操作及结果
 * @create 2017-10-19 17:20
 **/
@Transactional
@Service
public class ActInspectionApproval implements TaskListener {
    // 审批结果  1 通过 2退回
    public static final String APPROVE_SUCCESS = "1";
    public static final String APPROVE_FAIL = "2";
    // 标准工作service
    private InspectionTaskDao inspectionTaskDao = SpringContextHolder.getBean(InspectionTaskDao.class);
    private Expression approveReason;
    private Expression deptLeaderApproved;

    public void notify(DelegateTask delegateTask) {
        String approve = (String) deptLeaderApproved.getValue(delegateTask);
        String piid = delegateTask.getProcessInstanceId();
        String appReason = (String)approveReason.getValue(delegateTask);
        String approve_by = delegateTask.getAssignee();
        String now_status = (String)delegateTask.getVariable("task_status");
        String inspectionTask_id = inspectionTaskDao.getInspectionIdByPIid(piid).get("id").toString();
        InspectionTaskSwitch inspectionTaskSwitch = inspectionTaskDao.getInspectionTaskSwitch(inspectionTask_id);
        Map<String,Object> map = new HashMap<String,Object>();
        if(approve !=null && !"".equals(approve)){
            if(approve.equals("true")){
                map.put("switch_result", APPROVE_SUCCESS);
                if(now_status.equals(EAMConsts.CANCLE_STATUS)) {  // 申请撤销
                    map.put("task_status", EAMConsts.CANCLED_STATUS);
                    map.put("task_time_finish", new Date());
                } else if (now_status.equals(EAMConsts.TRANSFER_STATUS)) {  // 申请转单
                    map.put("task_status", EAMConsts.TO_BE_CONFIRMED);
                    map.put("task_processor_plan", inspectionTaskSwitch.getSwitch_person());
                } else if (now_status.equals(EAMConsts.HANGUP_STATUS)) {  // 申请挂起
                    map.put("task_status", EAMConsts.HANGUPED_STATUS);
                }
                //设置流程转向
                delegateTask.setVariable("approved","1");
            }else{
                map.put("switch_result", APPROVE_FAIL);
                if(now_status.equals(EAMConsts.CANCLE_STATUS)) {  // 申请撤销
                    map.put("task_status", EAMConsts.TO_BE_CONFIRMED);
                } else if (now_status.equals(EAMConsts.TRANSFER_STATUS)) {  // 申请转单
                    map.put("task_status", EAMConsts.BE_ON);     // 进行中
                    map.put("task_processor", inspectionTaskSwitch.getApply_person());
                } else if (now_status.equals(EAMConsts.HANGUP_STATUS)) {  // 申请挂起
                    map.put("task_status", EAMConsts.BE_ON);
                }
                //设置流程转向
                delegateTask.setVariable("approved","0");
            }
        }
        map.put("update_by",approve_by);
        map.put("approve_reason",appReason);
        map.put("pstid",piid);
        map.put("inspectiontask_id", inspectionTask_id);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        map.put("update_time",time);

        inspectionTaskSwitch = getInspectionTaskSwitch(map);
        inspectionTaskDao.updateSwitchByInsId(inspectionTaskSwitch);
        inspectionTaskDao.updateAprByPIid(map);

    }

    /**
     * 根据从流程传参，拼接流程任务实例
     * @param map
     * @return
     */
    public InspectionTaskSwitch getInspectionTaskSwitch(Map map){
        InspectionTaskSwitch inspectionTaskSwitch = new InspectionTaskSwitch();
        inspectionTaskSwitch.setInspectiontask_id(map.get("inspectiontask_id").toString());  // 巡检任务id
        inspectionTaskSwitch.setApprove_time(new Date());  // 审批时间
        inspectionTaskSwitch.setApprove_reason(map.get("approve_reason").toString());
        inspectionTaskSwitch.setApprove_person(map.get("update_by").toString());
        inspectionTaskSwitch.setSwitch_result(map.get("switch_result").toString());
        return inspectionTaskSwitch;
    }
}
