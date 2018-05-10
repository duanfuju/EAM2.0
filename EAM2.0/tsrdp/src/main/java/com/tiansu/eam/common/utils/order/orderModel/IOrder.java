package com.tiansu.eam.common.utils.order.orderModel;


/**
 * @author wangjl
 * @description 工单基础功能抽象封装
 * @create 2017-09-05 10:58
 **/
public interface IOrder {

    public void init() throws RuntimeException;

    public void runHandler() throws Exception;

    public ContextParam getContextParam();

}
