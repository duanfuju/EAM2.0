package com.tiansu.eam.modules.schedual.service;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.modules.schedual.controller.SchedualOrderController;
import com.tiansu.eam.modules.schedual.entity.SchedualOrder;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;

import java.util.Date;

/**
 * @author wangjl
 * @description 排班表审批流程之后，要同步审批信息到业务表操作
 * @create 2017-09-23 14:04
 **/
public class SchedualOrderApproveAfter implements TaskListener {
    SchedualOrderService schedualOrderService = SpringContextHolder.getBean(SchedualOrderService.class);

    private Expression result;
    private Expression remark;

    public void notify(DelegateTask delegateTask) {

        SchedualOrder schedualOrder = (SchedualOrder) delegateTask.getVariable(SchedualOrderController.SCHEDUAL_ORDER_FLOW);
        if(schedualOrder != null){
            schedualOrder.preUpdate();
            String approveStr = (String) result.getValue(delegateTask);
            boolean approved = "1".equals(approveStr)?true:false;
            schedualOrder.setApprove_status(approved?EAMConsts.ORDER_APPROVED:EAMConsts.ORDER_REJECTED);
            schedualOrder.setApprove_by(UserUtils.getUser().getLoginname());
            schedualOrder.setApprove_time(new Date());
            String appReason = (String)remark.getValue(delegateTask);
            schedualOrder.setApprove_reason(appReason);
        }

        schedualOrderService.update(schedualOrder);

    }

}
