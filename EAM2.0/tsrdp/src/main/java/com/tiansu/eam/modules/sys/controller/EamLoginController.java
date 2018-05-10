package com.tiansu.eam.modules.sys.controller;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.security.shiro.session.SessionDAO;
import com.thinkgem.jeesite.common.servlet.ValidateCodeServlet;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.CookieUtils;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.security.FormAuthenticationFilter;
import com.tiansu.eam.modules.sys.security.PrincipalUser;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by wangjl on 2017/7/14.
 *
 *
 *
 登入：
 1、登录发送a/eam/login的post请求，真正的过程由Filter完成。
    eamLoginController中的login（POST请求用于登录失败的处理，GET请求在登出时重定向登录页面时执行 ）
 2、请求经过一系列过滤器，其中AuthenticatingFilter.executeLogin()会执行getSubject().login()。
     继而DefaultSecurityManager.login() 时会根据 UserworkPassworkToken获取认证信息；
     具体认证过程在EamAuthorizingRealm.doGetAuthenticationInfo(AuthenticationToken authcToken)中实现；
 3、认证成功后 DefaultSecurityManager.login() 会将subject信息放入SubjectContext上下文中。
    并跳转到shiro bean的successUrl（见spring-context-shiro.xml中shiroFilter的配置）
 4、shiro会对所有方法做aop切面，AuthorizingMethodInterceptor.invoke()会检查所有的realm的isPermitted,
    从而执行EamAuthorizingRealm.doGetAuthenticationInfo(PrincipalCollection principals)。

 ### 登出:退出过滤器,执行SystemLogoutFilter
 */
@Controller
/*@RequestMapping(value = "${adminPath}/eam")*/
public class EamLoginController extends BaseController {

    @Autowired
    private SessionDAO sessionDAO;

    /**
     * 管理登录
     */
    @RequestMapping(value = "${adminPath}/eam/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
        PrincipalUser principal = UserUtils.getPrincipal();

//		// 默认页签模式
//		String tabmode = CookieUtils.getCookie(request, "tabmode");
//		if (tabmode == null){
//			CookieUtils.setCookie(response, "tabmode", "1");
//		}

        if (logger.isDebugEnabled()){
            logger.debug("login, active session size: {}", sessionDAO.getActiveSessions(false).size());
        }

        // 如果已登录，再次访问主页，则退出原账号。
        if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))){
            CookieUtils.setCookie(response, "LOGINED", "false");
        }

        // 如果已经登录，则跳转到管理首页
        if(principal != null && !principal.isMobileLogin()){
            String redirecturl = "redirect:" + adminPath + "/eam/index";
            return redirecturl;
        }



        return "modules/eamsys/sysLogin";
    }

    /**
     * 登录失败，真正登录的POST请求由Filter完成
     */
    @RequestMapping(value = "${adminPath}/eam/login", method = RequestMethod.POST)
    public String loginFail(HttpServletRequest request, HttpServletResponse response, Model model) {
        PrincipalUser principal = UserUtils.getPrincipal();

        // 如果已经登录，则跳转到管理首页
        if(principal != null){
            return "redirect:" + adminPath;
        }

        String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
        boolean rememberMe = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
        boolean mobile = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_MOBILE_PARAM);
        String exception = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        String message = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);

        if (StringUtils.isBlank(message) || StringUtils.equals(message, "null")){
            message = "用户或密码错误, 请重试.";
        }

        model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_MOBILE_PARAM, mobile);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, exception);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);

        if (logger.isDebugEnabled()){
            logger.debug("login fail, active session size: {}, message: {}, exception: {}",
                    sessionDAO.getActiveSessions(false).size(), message, exception);
        }

        // 非授权异常，登录失败，验证码加1。
        if (!UnauthorizedException.class.getName().equals(exception)){
            model.addAttribute("isValidateCodeLogin", isValidateCodeLogin(username, true, false));
        }

        // 验证失败清空验证码
        request.getSession().setAttribute(ValidateCodeServlet.VALIDATE_CODE, IdGen.uuid());

        // 如果是手机登录，则返回JSON字符串
        if (mobile){
            return renderString(response, model);
        }

        return "modules/eamsys/sysLogin";
    }

    /**
     * 单点登录成功，进入管理首页
     */
    @RequestMapping(value = "/ssologin")
    public String ssoLogin(HttpServletRequest request, HttpServletResponse response) {
        PrincipalUser principal = UserUtils.getPrincipal();

        // 登录成功后，验证码计算器清零
          String username =null;
        if(principal==null){
              username=(String)request.getSession().getAttribute("userCode");
        }else{
              username=principal.getLoginName();
            // 如果是手机登录，则返回JSON字符串
            if (principal.isMobileLogin()){
                if (request.getParameter("login") != null){
                    return renderString(response, principal);
                }
                if (request.getParameter("index") != null){
                    return "modules/sys/sysIndex";
                }
                return "redirect:" + adminPath + "/eam/login";
            }
        }
          isValidateCodeLogin(username, false, true);

        if (logger.isDebugEnabled()){
            logger.debug("show index, active session size: {}", sessionDAO.getActiveSessions(false).size());
        }




//        return "modules/sys/sysIndex";
        return "index";
    }

    /**
     * 登录成功，进入管理首页
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "${adminPath}/eam/index")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        PrincipalUser principal = UserUtils.getPrincipal();

        // 登录成功后，验证码计算器清零
        isValidateCodeLogin(principal.getLoginName(), false, true);

        if (logger.isDebugEnabled()){
            logger.debug("show index, active session size: {}", sessionDAO.getActiveSessions(false).size());
        }

//        // 如果已登录，再次访问主页，则退出原账号。
//        if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))){
//            String logined = CookieUtils.getCookie(request, "LOGINED");
//            if (StringUtils.isBlank(logined) || "false".equals(logined)){
//                CookieUtils.setCookie(response, "LOGINED", "true");
//            }else if (StringUtils.equals(logined, "true")){
//                UserUtils.getSubject().logout();
//                return "redirect:" + adminPath + "/eam/login";
//            }
//        }

        // 如果是手机登录，则返回JSON字符串
        if (principal.isMobileLogin()){
            if (request.getParameter("login") != null){
                return renderString(response, principal);
            }
            if (request.getParameter("index") != null){
                return "modules/sys/sysIndex";
            }
            return "redirect:" + adminPath + "/eam/login";
        }

//        return "modules/sys/sysIndex";
		return "index";
    }

    /**
     * 退出失败，进入管理首页
     */
    @RequestMapping(value = "${adminPath}/logout")
    public String logOutFailIndex(HttpServletRequest request, HttpServletResponse response) {
        PrincipalUser principal = UserUtils.getPrincipal();


        // 如果没有权限，再次访问登陆主页。
        if (principal==null){
          return "redirect:" + adminPath + "/eam/login";

        }else{

            String redirecturl = "redirect:" + adminPath + "/eam/index";
            return redirecturl;
        }




//        // 如果已登录，再次访问主页，则退出原账号。
//        if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))){
//            String logined = CookieUtils.getCookie(request, "LOGINED");
//            if (StringUtils.isBlank(logined) || "false".equals(logined)){
//                CookieUtils.setCookie(response, "LOGINED", "true");
//            }else if (StringUtils.equals(logined, "true")){
//                UserUtils.getSubject().logout();
//                return "redirect:" + adminPath + "/eam/login";
//            }
//        }



//        return "modules/sys/sysIndex";

    }
    /**
     * 获取主题方案
     */
    @RequestMapping(value = "/eam/theme/{theme}")
    public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request, HttpServletResponse response){
        if (StringUtils.isNotBlank(theme)){
            CookieUtils.setCookie(response, "theme", theme);
        }else{
            theme = CookieUtils.getCookie(request, "theme");
        }
        return "redirect:"+request.getParameter("url");
    }

    /**
     * 是否是验证码登录
     * @param useruame 用户名
     * @param isFail 计数加1
     * @param clean 计数清零
     * @return
     */
    @SuppressWarnings("unchecked")
    public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean){
        Map<String, Integer> loginFailMap = (Map<String, Integer>) CacheUtils.get("loginFailMap");
        if (loginFailMap==null){
            loginFailMap = Maps.newHashMap();
            CacheUtils.put("loginFailMap", loginFailMap);
        }
        Integer loginFailNum = loginFailMap.get(useruame);
        if (loginFailNum==null){
            loginFailNum = 0;
        }
        if (isFail){
            loginFailNum++;
            loginFailMap.put(useruame, loginFailNum);
        }
        if (clean){
            loginFailMap.remove(useruame);
        }
        return loginFailNum >= 3;
    }
}
