package com.tiansu.eam.modules.schedual.controller;


import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.schedual.entity.Schedual;
import com.tiansu.eam.modules.schedual.service.SchedualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wangjl
 * @description 排班controller
 * @create 2017-08-18 15:48
 **/
@Controller
@RequestMapping(value = "${adminPath}/schedual")
public class SchedualController extends BaseController {

    @Autowired
    SchedualService schedualService;

    /**
     * 根据排班id获取该排班信息
     * @param schedual
     * @return
     */
    @RequestMapping(value = {"getSchedualForShow"})
    public String getSchedualForShow(Schedual schedual,Model model){
        String schedualId = getPara("id");
        return "modules/schedual/schedualEdit";
    }




}
