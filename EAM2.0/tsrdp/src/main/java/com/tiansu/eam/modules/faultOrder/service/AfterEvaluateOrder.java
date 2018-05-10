package com.tiansu.eam.modules.faultOrder.service;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.modules.faultOrder.controller.FaultOrderController;
import com.tiansu.eam.modules.faultOrder.dao.FaultOrderDao;
import com.tiansu.eam.modules.faultOrder.entity.FaultOrder;
import com.tiansu.eam.modules.faultOrder.entity.OrderStatusEnum;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;

/**
 * @author wangjl
 * @description 工单评价后动作
 * @create 2017-10-16 10:02
 **/
public class AfterEvaluateOrder implements TaskListener {
    private Expression avgScore;
    private Expression evaluate;
    private Expression opType;

    private static FaultOrderDao faultOrderDao = SpringContextHolder.getBean(FaultOrderDao.class);

    public void notify(DelegateTask delegateTask) {
        //        获取报修表单数据
        FaultOrder faultOrder = (FaultOrder) delegateTask.getVariable(FaultOrderController.NOTIFY_FORM_DATA);
//        delegateTask.getDelegationState()
        String avgScoreStr = (String) avgScore.getValue(delegateTask);
        String evaluateStr = (String) evaluate.getValue(delegateTask);

        FaultOrder newOrder = new FaultOrder();
        if(faultOrder!=null){
            newOrder.setId(faultOrder.getId());
            newOrder.preUpdate();
            newOrder.setOrder_status(OrderStatusEnum.EVALUATEED.value());
            newOrder.setEvaluate_score(avgScoreStr);
            newOrder.setEvaluate_remark(evaluateStr);
        }
        faultOrderDao.update(newOrder);
    }

}
