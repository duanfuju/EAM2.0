package com.tiansu.eam.modules.faultOrder.service;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.modules.faultOrder.controller.FaultOrderController;
import com.tiansu.eam.modules.faultOrder.dao.FaultOrderDao;
import com.tiansu.eam.modules.faultOrder.entity.FaultOrder;
import com.tiansu.eam.modules.faultOrder.entity.OrderStatusEnum;
import com.tiansu.eam.modules.sys.entity.User;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangjl
 * @description
 * @create 2017-10-13 11:37
 **/

public class FaultOrderGrabDispService implements JavaDelegate {


    /**
     * 实现JavaDelegate接口，使用其中的execute方法 由于要放入流程定义中，所以要实现可序列话接口
     *
     */
    private static final long serialVersionUID = 5593437463482732772L;

    /** 流程变量**/
    private Expression disp_type;
    @Autowired
    FaultOrderService faultOrderService= SpringContextHolder.getBean(FaultOrderService.class);;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
//      获取报修表单数据
        FaultOrder cachedFaultOrder = (FaultOrder) delegateExecution.getVariable(FaultOrderController.NOTIFY_FORM_DATA);
        FaultOrder faultOrder = faultOrderService.get(cachedFaultOrder.getId());
        if (faultOrder == null) {
            faultOrder = cachedFaultOrder;
        }

        FaultOrder newOrder = new FaultOrder();
        newOrder.setId(faultOrder.getId());
        faultOrder.setOrder_status(OrderStatusEnum.PENDING_ACCEPT.value());
        faultOrder.setOrder_dispatcher(new User(null,"系统"));
        faultOrder.setOrder_dispatch_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        faultOrder.preUpdate();
        faultOrderService.update(faultOrder);
    }
}
