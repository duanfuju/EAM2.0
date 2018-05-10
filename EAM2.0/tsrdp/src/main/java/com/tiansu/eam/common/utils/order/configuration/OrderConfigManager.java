package com.tiansu.eam.common.utils.order.configuration;

import com.tiansu.eam.common.utils.ClassUtil;
import com.tiansu.eam.common.utils.order.enums.DispatchTypeEnum;
import com.tiansu.eam.common.utils.order.enums.OrderTypeEnum;
import com.tiansu.eam.common.utils.order.handlers.configs.OrderConfigFactory;
import com.tiansu.eam.common.utils.order.orderModel.IOrderConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangjl
 * @description 工单派发容器管理类
 *
 * 一级静态配置（容器间类耦合关系的配置（IOrderConfiguration），不依赖于代码外的改变而改变，如handler的新增注册，在启动时注册）;
 * 二级 动态配置：工单派单参数的配置(OrderDispHandlerControl.getConfigParams())，可做缓存，也可实时查询数据库 。
 * 三级 创建工单实例时创建 如上下文变量，工单实例变量
 *
 * @create 2017-09-06 9:29
 **/
public class OrderConfigManager {

    private static Map<OrderTypeEnum,OrderDispControl> orderDispControlMap = new HashMap<>();

    //OrderConfigManager 单例化处理
    private static OrderConfigManager orderConfigManager = null;
    public static OrderConfigManager getInstance() {
        if (orderConfigManager == null) {
            synchronized (OrderConfigManager.class) {
                if (orderConfigManager == null) {
                    orderConfigManager = new OrderConfigManager();
                }
            }
        }
        return orderConfigManager;
    }


    public void initConfig(){
//       创建工单时同时初始化工单容器
        registerHandlerConfig();
    }

    /**
     * //获取所有IOrder的实现类并收集其配置注册进容器中；
     * @throws RuntimeException
     */
    private void registerHandlerConfig() {
        List<Class<IOrderConfiguration>> orders = null;
        try {
            //获取所有继承IOrder的工单类，并收集其注册其派发Handler
            orders = ClassUtil.getAllAssignedClass(IOrderConfiguration.class,false);
            if(orders.size() > 0){
                for(Class<IOrderConfiguration> orderClass : orders){
                    IOrderConfiguration order = orderClass.newInstance();
                    //先做集成注册，之后才能init初始化init方法，不然在init中无法获取容器注册的对象关系
                    registerDispHanlerControl(order);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void registerDispHanlerControl(IOrderConfiguration orderConfiguration) {
        if(!orderDispControlMap.containsKey(orderConfiguration.getOrderType())){
            orderDispControlMap.put(orderConfiguration.getOrderType(),new OrderDispControl());
        }
        //注册EventHandlers

        DispatchTypeEnum[] allDispTypeArray = orderConfiguration.getAllDispTypes();
        for(DispatchTypeEnum dispatchType : allDispTypeArray){
            OrderDispHandlerControl orderDispHandlerControl = OrderConfigFactory.getConfiguration(orderConfiguration.getOrderType(),dispatchType);
            orderDispHandlerControl.initEventHandlers();
            orderDispControlMap.get(orderConfiguration.getOrderType()).registOrderDispControl(dispatchType,orderDispHandlerControl);
        }




    }

    public OrderDispControl getOrderDispControl(OrderTypeEnum ordertype){
        if(orderDispControlMap.containsKey(ordertype)){
            return orderDispControlMap.get(ordertype);
        }
        return null;
    }

}
