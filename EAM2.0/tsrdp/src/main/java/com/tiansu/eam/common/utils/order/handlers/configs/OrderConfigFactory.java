package com.tiansu.eam.common.utils.order.handlers.configs;

import com.tiansu.eam.common.utils.order.configuration.OrderDispHandlerControl;
import com.tiansu.eam.common.utils.order.enums.DispatchTypeEnum;
import com.tiansu.eam.common.utils.order.enums.OrderTypeEnum;


/**
 * @author wangjl
 * @description 工单配置项 工厂
 * @create 2017-09-05 15:10
 **/
public class OrderConfigFactory {
    public static OrderDispHandlerControl getConfiguration(OrderTypeEnum orderType, DispatchTypeEnum dispatchType){
        //目前只有工单派单且为抢单模式时才会有自定义参数
        if(OrderTypeEnum.REPAIR_ORDER == orderType && DispatchTypeEnum.ORDER_GRAB_DISP == dispatchType){
            return new RepairOrderGrabDispHandlerConfig();
        }else{
            //没有配置参数的派单配置handler
            return new DefaultOrderGrabDispHandlerConfig();
        }
    }
}
