package com.tiansu.eam.modules.sys.controller;/**
 * Created by suven on 2017/10/24.
 */

import com.thinkgem.jeesite.common.web.BaseController;
import com.tiansu.eam.modules.sys.entity.SystemInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取子系统信息
 *
 * @author suven suven
 * @create 2017/10/24
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/eamsubsystem")
public class EamSubSystemInfoController extends BaseController {

    @RequestMapping(value={"findSubSystems",""})
    @ResponseBody
    public List<SystemInfo> findSubSystems( HttpServletRequest request, HttpServletResponse response, Model model){
        List<SystemInfo> systemInfos=new ArrayList<SystemInfo>();
        HttpSession session=request.getSession();
        String systemnos=(String)session.getAttribute("subsystemnos");
        if(("").equals(systemnos)||systemnos==null){
            return null;
        }
        String[] systems=systemnos.split(",");
        for(int i=0;i<systems.length;i++){
            systemInfos.add((SystemInfo)session.getAttribute(systems[i]));
        }
        return systemInfos;
    }

}
