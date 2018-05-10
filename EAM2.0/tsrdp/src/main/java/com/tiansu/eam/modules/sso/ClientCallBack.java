package com.tiansu.eam.modules.sso;

import com.thinkgem.jeesite.modules.sys.security.UsernamePasswordToken;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
*@Create
*@Description :所有单点登陆集合操作类
*@Param :  * @param null
*@author : suven
*@Date : 19:30 2017/10/17
*/
public class ClientCallBack extends HttpServlet {


    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    /*
    * 编写接受信息
    * */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int defaultCmdCode = -1;
        try {



            response.setContentType("text/plain");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            defaultCmdCode =
                    StringUtils.isNotEmpty(request.getParameter(BaseSettings.cmdcode)) ?
                            Integer.parseInt(request.getParameter(BaseSettings.cmdcode)) : defaultCmdCode;
            if (defaultCmdCode == -1) {
                return;
            }
            SSOService ssoService = build(defaultCmdCode);
            if(ssoService!=null) {
                ssoService.doService(request, response);
                if(defaultCmdCode==0) {
                    boolean success = loginEam(request, response);
                    if(success){

                        ssoService=new  CallGetNaviInfoService();
                        ssoService.doService(request, response);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean loginEam(HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordToken upt = new UsernamePasswordToken();
        HttpSession session = request.getSession();
        String token=(String) session.getAttribute("ssoToken");

        String username=(String)session.getAttribute("userCode");
        try {
            upt.setUsername(username); // 登录用户名
            upt.setPassword(token.toCharArray());
            upt.setParams(upt.toString()); // 单点登录识别参数，see： AuthorizingRealm.assertCredentialsMatch
        } catch (Exception ex){
            ex.printStackTrace();
        }
        try {
            UserUtils.getSubject().login(upt);

        } catch (AuthenticationException ae) {
            if (!ae.getMessage().startsWith("msg:")){
                ae = new AuthenticationException("msg:授权错误，请检查用户配置，若不能解决，请联系管理员。");
            }
            ae.printStackTrace();
        }




        return true;


    }

    /**
    *@Create
    *@Description :根据cmdCode创建SSOService对象
    *@Param :  * @param null
    *@author : suven
    *@Date : 19:31 2017/10/17
    */
    public static SSOService build(int cmdCode) {
        SSOService ssoService = null;
        //判断请求码与属性位置关系
        if (cmdCode == ESSOCmd.ssocallbak.ordinal()) {
            ssoService = new CallBackService();
        } else if (cmdCode == ESSOCmd.logout.ordinal()) {
            ssoService = new CallLogoutService();
        } else if (cmdCode == ESSOCmd.isoutline.ordinal()) {
            ssoService = new CallOutlineService();
        } else if (cmdCode == ESSOCmd.vifyToken.ordinal()) {
            ssoService = new CallVerifyTokenService();
        }
        return ssoService;
    }
}
