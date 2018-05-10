package com.tiansu.eam.modules.sys.controller;/**
 * Created by suven on 2017/10/11.
 */


import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.sys.service.SonSystemRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 子系統注冊功能，獲得所有的相關屬性
 *
 * @author suven suven
 * @create 2017/10/11
 */
@Controller
@RequestMapping(value = "${adminPath}/eam/register")
public class SonSystemRegisterController extends BaseController {

    @Autowired
    SonSystemRegisterService ssrs;
    /**
    *@Create
    *@Description :查詢所有相關信息，組裝成JSON
    *@Param :  * @param null
    *@author : suven
    *@Date : 10:40 2017/10/11
    */
    @RequestMapping("/getJsonString")
    @ResponseBody
    public String register(){

        return ssrs.getAllRelatedInfoToJsonString();
    }

}
