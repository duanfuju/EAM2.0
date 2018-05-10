package com.tiansu.eam.common.utils.order.orderModel;

import com.tiansu.eam.common.utils.order.enums.DispatchTypeEnum;
import com.tiansu.eam.common.utils.order.enums.OrderTypeEnum;

/**
 * @author wangjl
 * @description 工单配置信息：在启动时容器收集其实现类，收集其中的派发Handler处理放入容器中
 *  （容器间类耦合关系的配置，不依赖于代码外的改变而改变，如handler的新增注册，在启动时注册）;
 * @create 2017-09-07 15:49
 **/
public interface IOrderConfiguration {
    /** 工单基本属性**/
    //工单类别
    public OrderTypeEnum getOrderType();

    //当前派单方式
    public DispatchTypeEnum getCurrentDispType() throws RuntimeException;

    //当前工单所有派单方式
    public DispatchTypeEnum[] getAllDispTypes();

}
