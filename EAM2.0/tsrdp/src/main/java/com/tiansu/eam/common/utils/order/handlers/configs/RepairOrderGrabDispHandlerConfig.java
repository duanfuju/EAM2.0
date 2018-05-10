package com.tiansu.eam.common.utils.order.handlers.configs;


import com.tiansu.eam.common.utils.order.configuration.DispConfigParam;
import com.tiansu.eam.common.utils.order.configuration.OrderDispHandlerControl;
import com.tiansu.eam.common.utils.order.handlers.RepairOrderAutoDispHandler;
import com.tiansu.eam.common.utils.order.handlers.RepairOrderManualDispHandler;

/**
 * @author wangjl
 * @description
 * @create 2017-09-05 15:13
 **/
public class RepairOrderGrabDispHandlerConfig extends OrderDispHandlerControl {
    @Override
    public void initEventHandlers() {
        super.addEventHandler(RepairOrderManualDispHandler.class);
        super.addEventHandler(RepairOrderAutoDispHandler.class);
    }

    @Override
    public DispConfigParam getConfigParams() {
        DispConfigParam p = new DispConfigParam();
        p.addParamValue("configP",System.currentTimeMillis());
        System.out.println(p.getParamValue("configP"));
        return p;
    }


}
