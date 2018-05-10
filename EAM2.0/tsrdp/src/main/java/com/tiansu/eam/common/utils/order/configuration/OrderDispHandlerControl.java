package com.tiansu.eam.common.utils.order.configuration;

import com.tiansu.eam.common.utils.order.handlers.IDispatchTypeHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjl
 * @description 工单派发方式配置及处理类（拥有者：OrderDispControl）
 * @create 2017-09-05 18:20
 **/
public abstract class OrderDispHandlerControl{

    /**
     * 获取当前派发方式的配置参数
     * @return
     */
    public abstract DispConfigParam getConfigParams();

    /**
     * 添加dispHandler
     */
    public abstract void initEventHandlers();


    //某类工单拥有的派发方式及相应处理handler
    List<Class<? extends IDispatchTypeHandler>> dispatchTypeHandlers = new ArrayList<>();




    public void addEventHandler(Class<? extends IDispatchTypeHandler> eventHandler) {
        dispatchTypeHandlers.add(eventHandler);
    }

    public List<Class<? extends IDispatchTypeHandler>> getEventHandlers() {
        return dispatchTypeHandlers;
    }

}
