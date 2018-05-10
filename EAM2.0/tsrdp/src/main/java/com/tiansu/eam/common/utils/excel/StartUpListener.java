package com.tiansu.eam.common.utils.excel;

import com.tiansu.eam.common.utils.order.configuration.OrderConfigManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.net.URL;


public class StartUpListener implements ServletContextListener {  
  
	
    public void contextDestroyed(ServletContextEvent context) {  
          
    }  
  
    public void contextInitialized(ServletContextEvent context) {  
    	//String filePath = context.getServletContext().getRealPath("/")+File.separator+"config";

        URL resource =Thread.currentThread().getContextClassLoader().getResource("excelmodel.xml");
        String path = resource.getPath();
        // 上下文初始化执行
        System.out.println("================>[ServletContextListener]自动加载启动开始.");  
    	ExcelConfigParser.loadXmlConfig(path);
        String path1 =Thread.currentThread().getContextClassLoader().getResource("config.properties").getPath();
        SystemProperty.loadProptyConfig(path1);




        //初始化工单派单机制容器；
        OrderConfigManager.getInstance().initConfig();

       /* RepairOrder order = (RepairOrder) OrderFactory.createOrder(OrderTypeEnum.REPAIR_ORDER);
        try {
            order.init();
            order.setS("11");
            order.getContextParam().addParamValue("REPAIR_ORDER","TESTVALUE");
            order.runHandler();
        } catch (Exception e) {
            e.printStackTrace();
        }

        RepairOrder order2 = (RepairOrder) OrderFactory.createOrder(OrderTypeEnum.REPAIR_ORDER);
        try {
            order2.init();
            order2.setS("22");
            order2.getContextParam().addParamValue("33","44");

            order2.runHandler();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        System.out.println("================>[ServletContextListener]自动加载结束.");
    }


} 
	

