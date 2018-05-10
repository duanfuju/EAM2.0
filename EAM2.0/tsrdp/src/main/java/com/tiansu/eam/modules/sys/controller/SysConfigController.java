package com.tiansu.eam.modules.sys.controller;

import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.sys.entity.SysConfigEntry;
import com.tiansu.eam.modules.sys.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangjl
 * @description
 * @create 2017-08-31 16:58
 **/
@Controller
@RequestMapping(value = "${adminPath}/sysConfig")
public class SysConfigController extends BaseController {

    @Autowired
    SysConfigService sysConfigService;

    @ResponseBody
    @RequestMapping(value = {"getConfigByName"})
    public Map<String,Object> getConfigByName(String config_key){
        Map<String,Object> returnMap = new HashMap();
        if(config_key == null){
            returnMap.put("flag",false);
            returnMap.put("msg","操作失败，参数为空");
            return returnMap;
        }

        try {
            SysConfigEntry result = sysConfigService.getByKeyName(config_key);
            if(result!=null){
                returnMap.put("flag",true);
                returnMap.put("data",result.getConfig_value());
                returnMap.put("msg","操作成功");
            }else{
                returnMap.put("flag",false);
                returnMap.put("msg","查询结果为空");
            }
            return returnMap;
        }catch (Exception e){
            e.printStackTrace();
            returnMap.put("flag",false);
            returnMap.put("msg","操作失败["+e.getMessage()+"]");
        }
        return returnMap;
    }

    @ResponseBody
    @RequestMapping(value = {"updateConfigByName"})
    public Map<String,Object> updateConfigByName(HttpServletRequest request, HttpServletResponse response){
        String configKey = getPara("config_key");
        String configValue = getPara("config_value");
        Map<String,Object> returnMap = new HashMap();
        if(configKey == null){
            returnMap.put("flag",false);
            returnMap.put("msg","操作失败，参数为空");
            return returnMap;
        }
        try {
            SysConfigEntry sysConfig = new SysConfigEntry();
            sysConfig.setConfig_key(configKey);
            sysConfig.setConfig_value(configValue);
            sysConfigService.updateByKeyName(sysConfig);

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



}

