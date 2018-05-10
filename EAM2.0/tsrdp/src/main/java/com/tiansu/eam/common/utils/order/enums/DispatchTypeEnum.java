package com.tiansu.eam.common.utils.order.enums;

/**
 * @author wangjl
 * @description 派单方式,与工单类型共同决定具体执行哪种派单方式
 * @create 2017-09-05 11:00
 **/

public enum DispatchTypeEnum {
    //手工派单
    ORDER_MANUAL_DISP,
    //自动派单
    ORDER_AUTO_DISP,
    //抢单
    ORDER_GRAB_DISP
}
