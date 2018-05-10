package com.tiansu.eam.modules.sys.controller;

import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.sys.service.EamButtonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangdf on 2017/7/27.
 */
@Controller
@RequestMapping(value = "${adminPath}/eam/button")
public class EamButtonController extends BaseController{
    @Autowired
    EamButtonService eamButtonService;
    @ResponseBody
    @RequestMapping(value = {"getButtonByRole"})
    public List<Map<String, Object>> getButtonByRoleandMenu(){
        return eamButtonService.getButtonByRoleandMenu();
    }
}
