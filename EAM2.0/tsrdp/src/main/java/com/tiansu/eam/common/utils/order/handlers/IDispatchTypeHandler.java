package com.tiansu.eam.common.utils.order.handlers;


import com.tiansu.eam.common.utils.order.configuration.DispConfigParam;
import com.tiansu.eam.common.utils.order.orderModel.ContextParam;

/**
 * @author wangjl
 * @description 不同派单方式的处理接口
 * @create 2017-09-05 11:35
 **/
public interface IDispatchTypeHandler {

    public boolean handlerDispEvent(ContextParam contextParam, DispConfigParam paramConfig);
}
