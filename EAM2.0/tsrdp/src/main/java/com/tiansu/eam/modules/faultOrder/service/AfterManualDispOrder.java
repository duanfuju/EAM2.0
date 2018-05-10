package com.tiansu.eam.modules.faultOrder.service;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.modules.faultOrder.dao.FaultOrderDao;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

/**
 * @author wangjl
 * @description 工单手动派单后动作
 * @create 2017-10-16 10:02
 **/
public class AfterManualDispOrder implements TaskListener {
    private Expression transfer;
    private Expression reason;


    private static FaultOrderDao faultOrderDao = SpringContextHolder.getBean(FaultOrderDao.class);

    public void notify(DelegateTask delegateTask) {
        ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
;

//        newOrder.setOrder_status(OrderStatusEnum.PENDING_FINISH.value());



    }

}
