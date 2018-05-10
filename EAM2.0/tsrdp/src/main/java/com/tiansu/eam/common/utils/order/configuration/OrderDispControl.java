package com.tiansu.eam.common.utils.order.configuration;

import com.tiansu.eam.common.utils.order.enums.DispatchTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangjl
 * @description 工单派发方式配置（拥有者：工单）
 * @create 2017-09-05 17:32
 **/
public class OrderDispControl {
    //某类工单拥有的派发方式及相应处理handler
    private Map<DispatchTypeEnum, OrderDispHandlerControl> orderDispControls = new HashMap();

    //注册该类工单所有派单方式
    protected void registOrderDispControl(DispatchTypeEnum dispatchType,OrderDispHandlerControl orderDispHandlerControl){
        orderDispControls.put(dispatchType,orderDispHandlerControl);
    }

    /**
     * 获取工单派单配置
     * @param dispatchType
     * @return
     */

    public OrderDispHandlerControl getOrderDispControl(DispatchTypeEnum dispatchType){
        return orderDispControls.get(dispatchType);
    }
}
