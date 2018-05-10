package com.tiansu.eam.common.utils.order.orderModel;


import com.tiansu.eam.common.utils.order.enums.DispatchTypeEnum;
import com.tiansu.eam.common.utils.order.enums.OrderTypeEnum;

/**
 * @author wangjl
 * @description 故障工单
 * @create 2017-09-05 11:04
 **/
public class RepairOrder extends AbstractOrder {

    public void init(){
        super.init();
    }

    @Override
    public OrderTypeEnum getOrderType() {
        return OrderTypeEnum.REPAIR_ORDER;
    }

    @Override
    public DispatchTypeEnum getCurrentDispType() throws RuntimeException {
        //TODO 实时查询数据库
        return DispatchTypeEnum.ORDER_GRAB_DISP;
    }

    @Override
    public DispatchTypeEnum[] getAllDispTypes() {
        return new DispatchTypeEnum[]{
                DispatchTypeEnum.ORDER_MANUAL_DISP,
                DispatchTypeEnum.ORDER_AUTO_DISP,
                DispatchTypeEnum.ORDER_GRAB_DISP
        };
    }







}
