package com.tiansu.eam.common.utils.order.handlers;

import com.tiansu.eam.common.utils.order.configuration.DispConfigParam;
import com.tiansu.eam.common.utils.order.orderModel.ContextParam;

/**
 * @author wangjl
 * @description 故障工单手工派单处理类
 * @create 2017-09-05 14:13
 **/
public class RepairOrderManualDispHandler implements IDispatchTypeHandler{

    @Override
    public boolean handlerDispEvent(ContextParam contextParam, DispConfigParam paramConfig) {
        return false;
    }
}
