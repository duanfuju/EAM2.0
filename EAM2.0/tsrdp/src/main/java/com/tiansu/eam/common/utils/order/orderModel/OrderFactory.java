package com.tiansu.eam.common.utils.order.orderModel;

import com.tiansu.eam.common.utils.order.enums.OrderTypeEnum;

/**
 * @author wangjl
 * @description
 * @create 2017-09-05 10:56
 **/
public class OrderFactory {

    /**
     * 创建工单实例
     * @return
     */
    public static IOrder createOrder(OrderTypeEnum ordertype){
        if(OrderTypeEnum.REPAIR_ORDER.equals(ordertype)){
            return new RepairOrder();
        }else if(OrderTypeEnum.MAINT_ORDER.equals(ordertype)){
//            return new MaintOrder();
        }else if(OrderTypeEnum.INSPECT_ORDER.equals(ordertype)){
//            return new InspectOrder();
        }
        return null;
    }


}
