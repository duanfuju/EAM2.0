package com.tiansu.eam.modules.device.controller;

import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.device.entity.DevSpareParts;
import com.tiansu.eam.modules.device.service.EamDevSparePartsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author tiansu
 * @description
 * @create 2017-09-06 10:34
 **/
@Controller
@RequestMapping(value = "${adminPath}/eam/devSpareParts")
public class EamDevSparePartsController extends BaseController {
    @Autowired
    private EamDevSparePartsService eamDevSparePartsService;

    /**
     * 新增设备信息
     * @param devSpareParts
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "insert")
    public String insert(DevSpareParts devSpareParts){
        eamDevSparePartsService.insert(devSpareParts);
        return "success";
    }

    /**
     * 根据设备查询该设备下的工器具以及备品备件
     * 工器具和备品备件的区分标识是type_flag  0 备品备件 1工器具
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "listData")
    public Map<String, Object> listData(){
        Map param = super.getFormMap();
        Map<String, Object> map = eamDevSparePartsService.dataTablePageMap(param);
        return map;
    }
}
