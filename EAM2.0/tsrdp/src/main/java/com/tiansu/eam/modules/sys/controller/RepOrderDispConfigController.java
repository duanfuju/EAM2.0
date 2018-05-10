package com.tiansu.eam.modules.sys.controller;

import com.thinkgem.jeesite.common.web.BaseController;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.modules.sys.entity.SysConfigEntry;
import com.tiansu.eam.modules.sys.service.SysConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangjl
 * @description 工单派单配置controller
 * @create 2017-08-29 16:44
 **/
@Controller
@RequestMapping(value = "${adminPath}/repairOrderDisp")
public class RepOrderDispConfigController extends BaseController {
    @Autowired
    SysConfigService sysConfigService;

    /**
     *
     * 保存工单配置
     * @param request
     * @param response
     */
    @RequestMapping(value={"saveRepairOrderConfig",""})
    public @ResponseBody Map<String,Object> saveRepairOrderConfig( HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> returnMap = new HashMap();
        try {
            String disp_type = getPara("disp_type");
            String max_orders = getPara("max_orders");
            String max_timeout = getPara("max_timeout");
            SysConfigEntry configEntry = new SysConfigEntry();
            if(StringUtils.isNotEmpty(disp_type)){
                configEntry.preInsert();
                configEntry.setConfig_key(EAMConsts.REPAIR_ORDER_DISP_TYPE);
                configEntry.setConfig_value(disp_type);
                sysConfigService.updateByKeyName(configEntry);
            }
            if(StringUtils.isNotEmpty(max_orders)){
                configEntry.preInsert();
                configEntry.setConfig_key(EAMConsts.REPARI_ORDEDR_QRAB_ORDERS);
                configEntry.setConfig_value(max_orders);
                sysConfigService.updateByKeyName(configEntry);
            }
            if(StringUtils.isNotEmpty(max_timeout)){
                configEntry.preInsert();
                configEntry.setConfig_key(EAMConsts.REPARI_ORDEDR_QRAB_TIMEOUT);
                configEntry.setConfig_value(max_timeout);
                sysConfigService.updateByKeyName(configEntry);
            }

            returnMap.put("flag",true);
            returnMap.put("msg","操作成功");
            return returnMap;
        }catch (Exception e){
            e.printStackTrace();
            returnMap.put("flag",false);
            returnMap.put("msg","操作失败["+e.getMessage()+"]");
        }
        return returnMap;
    }

    /**
     *
     * 读取工单配置数据
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping(value={"getRepairOrderConfig",""})
    public Map<String,Object> getRepairOrderConfig( HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> returnMap = new HashMap();
        List<String> configs = new ArrayList();
        try {
            String disp_type = "";
            String max_orders = "";
            String max_timeout = "";

            SysConfigEntry configEntry = sysConfigService.getByKeyName(EAMConsts.REPAIR_ORDER_DISP_TYPE);
            if(configEntry!=null){
                disp_type = configEntry.getConfig_value();
            }
            configEntry = sysConfigService.getByKeyName(EAMConsts.REPARI_ORDEDR_QRAB_ORDERS);
            if(configEntry!=null){
                max_orders = configEntry.getConfig_value();
            }
            configEntry = sysConfigService.getByKeyName(EAMConsts.REPARI_ORDEDR_QRAB_TIMEOUT);
            if(configEntry!=null){
                max_timeout = configEntry.getConfig_value();
            }
            configs.add(disp_type);
            configs.add(max_orders);
            configs.add(max_timeout);

            returnMap.put("flag",true);
            returnMap.put("data",configs);
            returnMap.put("msg","操作成功");
            return returnMap;
        }catch (Exception e){
            e.printStackTrace();
            returnMap.put("flag",false);
            returnMap.put("msg","操作失败["+e.getMessage()+"]");
        }
        return returnMap;
    }


}
