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

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangjl
 * @description 工单到达后动作
 * @create 2017-10-16 10:02
 **/
public class AfterArriveOrder implements TaskListener {
    private Expression xFilePath;

    private static FaultOrderDao faultOrderDao = SpringContextHolder.getBean(FaultOrderDao.class);

    public void notify(DelegateTask delegateTask) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        FaultOrder faultOrder = (FaultOrder) delegateTask.getVariable(FaultOrderController.NOTIFY_FORM_DATA);
        FaultOrder newOrder = new FaultOrder();
        if(faultOrder!=null){
            String arriveImg = (String) xFilePath.getValue(delegateTask);
            newOrder.setOrder_status(OrderStatusEnum.PENDING_FINISH.value());
            newOrder.setOrder_arrivetime(sdf.format(new Date()));
            newOrder.setArrive_img(arriveImg);
            newOrder.setId(faultOrder.getId());
            newOrder.preUpdate();
        }
        faultOrderDao.update(newOrder);

    }

}
