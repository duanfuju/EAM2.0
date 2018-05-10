package com.tiansu.eam.common.utils.order.orderModel;

import com.tiansu.eam.common.utils.order.configuration.OrderConfigManager;
import com.tiansu.eam.common.utils.order.configuration.OrderDispControl;
import com.tiansu.eam.common.utils.order.configuration.OrderDispHandlerControl;
import com.tiansu.eam.common.utils.order.handlers.IDispatchTypeHandler;

import java.util.List;

/**
 * @author wangjl
 * @description
 * @create 2017-09-05 11:25
 **/
public abstract class AbstractOrder implements IOrder,IOrderConfiguration{

    private ContextParam contextParam ;

    @Override
    public void init(){

    }

    /**
     * 获取当前order实例的上下文变量
     * @return
     */
    public ContextParam getContextParam (){
        if(contextParam == null){
            contextParam = new ContextParam();
        }
        return contextParam;
    }

    public List<Class<? extends IDispatchTypeHandler>> getEventHandlers (){
        return getCurrentOrderDispHandlerControl().getEventHandlers();
    }


    private OrderDispControl getOrderDispControl(){
        return OrderConfigManager.getInstance().getOrderDispControl(getOrderType());
    }

    private OrderDispHandlerControl getCurrentOrderDispHandlerControl(){
        return getOrderDispControl().getOrderDispControl(getCurrentDispType());
    }

    @Override
    public void runHandler() {
        OrderDispHandlerControl currentOrderHandlerControl = getCurrentOrderDispHandlerControl();

        List<Class<? extends IDispatchTypeHandler>> dispTypeHandlers = currentOrderHandlerControl.getEventHandlers();
        if(dispTypeHandlers!=null){
            for(Class<? extends IDispatchTypeHandler> dispTypeHandlerCls : dispTypeHandlers){
                try {
                    IDispatchTypeHandler dispTypeHandlerInst = dispTypeHandlerCls.newInstance();
                    dispTypeHandlerInst.handlerDispEvent(getContextParam(),currentOrderHandlerControl.getConfigParams());
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

            }
        }
    }
}
