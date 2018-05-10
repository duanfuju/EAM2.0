package com.tiansu.eam.modules.sso;

import com.thinkgem.jeesite.common.config.Global;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;


/**
*@Create
*@Description :登陆回调类
*@Param :  * @param null
*@author : suven
*@Date : 19:29 2017/10/17
*/
class CallBackService extends SSOService {

    /**
    *@Create
    *@Description :登陆回调操作
    *@Param :  * @param null
    *@author : suven
    *@Date : 19:29 2017/10/17
    */
    @Override
    public  int doService(HttpServletRequest request, HttpServletResponse response) {
        int success = 0;
        try {
            HttpSession session = request.getSession();
            String token = request.getParameter(BaseSettings.callbackParamKey);
            String ssoip = Global.getConfig(BaseSettings.ssohostip);
            String ssohost = Global.getConfig(BaseSettings.ssohostAddress);
            session.setAttribute(BaseSettings.ssohost,ssohost);
            session.setAttribute(BaseSettings.ssoip, ssoip);
            session.setAttribute("ssoToken",token);
            String username=request.getParameter("loginname");
            session.setAttribute("userCode",username);

            if (StringUtils.isNotEmpty(token)) {
                String info = EncryptUtil.decrypt64(token);
                info = info.replaceAll(BaseSettings.encryptRule, StringUtils.EMPTY);
                String[] aars = info.split(BaseSettings.splitRule);
                if(aars.length>0) {
                    session.setAttribute(BaseSettings.token, aars[0]);
                }

            }
            String sessionToken = session.getAttribute(BaseSettings.token) == null ?
                    StringUtils.EMPTY : session.getAttribute(BaseSettings.token).toString();
            PrintWriter out = response.getWriter();
            String jsonpCallback = request.getParameter(BaseSettings.jsonp4callbackParamKey);//客户端请求参数
            out.println(jsonpCallback + "({\"result\":\"" + sessionToken + "\"})");//返回jsonp格式数据
            out.flush();
            out.close();
            success = 1;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return success;
    }
}
