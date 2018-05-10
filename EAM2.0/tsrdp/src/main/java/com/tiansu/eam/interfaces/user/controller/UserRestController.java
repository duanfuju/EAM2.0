package com.tiansu.eam.interfaces.user.controller;


import com.alibaba.fastjson.JSON;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.interfaces.device.entity.DataUtils;
import com.tiansu.eam.interfaces.resulthelper.APPResponseBody;
import com.tiansu.eam.modules.employee.service.EamUserExtService;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Created by shufq on 2017/10/12.
 * @description 用户相关接口
 */

@Path("/{tenant}/user")
@Controller
public class UserRestController extends BaseController{
    public static EamUserExtService eamUserExtService = SpringContextHolder.getBean(EamUserExtService.class);

    @Path("/get")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public @ResponseBody String getOrgTree(@QueryParam("rolecode") String rolecode,
                                           @Context HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", "*");
        Map param = super.getFormMap();
        param.put("rolecode",rolecode);
        //查询所有部门：
        List<Map> listDept = eamUserExtService.findDept(param);
        for (int i = 0; i < listDept.size(); i++) {
            Map map = listDept.get(i);
            List<Map> listUser = eamUserExtService.findUser(map);
            map.put("user",listUser);
        }
       Map<String, Object> map = new HashMap();
        map.put("data",listDept);
        APPResponseBody responseBody = new APPResponseBody("1", DataUtils.sucCode, DataUtils.sucMsg, map);
        return JSON.toJSONString(responseBody);

    }

}
