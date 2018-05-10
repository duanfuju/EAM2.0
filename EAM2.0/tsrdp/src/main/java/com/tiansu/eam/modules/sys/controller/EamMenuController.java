package com.tiansu.eam.modules.sys.controller;

import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.sys.entity.Menu;
import com.tiansu.eam.modules.sys.service.EamMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by tiansu on 2017/7/20.
 */

@Controller
@RequestMapping(value = "${adminPath}/eam/menu")
public class EamMenuController extends BaseController {
    @Autowired
    private EamMenuService menuService;

    @ResponseBody
    @RequestMapping(value = {"getMenuList"})
    public List<Menu> getMenuList(@RequestParam(required=false) String extId, HttpServletResponse response) {
        List<Menu> menuList = menuService.getMenuList();
        return menuList;
    }




}
