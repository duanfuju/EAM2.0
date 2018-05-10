package com.tiansu.eam.common.utils.order.orderModel;

import com.tiansu.eam.common.utils.order.enums.DispatchTypeEnum;
import com.tiansu.eam.common.utils.order.enums.OrderTypeEnum;

/**
 * @author wangjl
 * @description 保养工单
 * @create 2017-09-05 11:05
 **/
public class MaintOrder   {


//    @Override
    public OrderTypeEnum getOrderType() {
        return OrderTypeEnum.MAINT_ORDER;
    }

//    @Override
    public DispatchTypeEnum getCurrentDispType() throws RuntimeException {
        return null;
    }

//    @Override
    public DispatchTypeEnum[] getAllDispTypes() {
        return new DispatchTypeEnum[0];
    }


}