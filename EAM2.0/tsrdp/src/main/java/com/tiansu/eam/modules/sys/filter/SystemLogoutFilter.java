package com.tiansu.eam.modules.sys.filter;

import com.thinkgem.jeesite.common.config.Global;
import com.tiansu.eam.modules.sso.BaseSettings;
import com.tiansu.eam.modules.sso.CallLogoutService;
import com.tiansu.eam.modules.sso.ClientCallBack;
import com.tiansu.eam.modules.sso.SSOService;
import com.tiansu.eam.modules.sys.interceptor.AjaxInterceptor;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by wangjl on 2017/7/25.
 * 登出拦截器，用于处理登出操作前的处理
 */
@Service
public  class SystemLogoutFilter extends SSOService {
    private Log logger = LogFactory.getLog(AjaxInterceptor.class);   //日志记录
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        HttpSession session = ((HttpServletRequest)request).getSession();
        if(session.getAttribute("ssoToken") != null){
            SSOService ssoService= ClientCallBack.build(1);
            int success=ssoService.doService((HttpServletRequest) request,(HttpServletResponse)  response);
            if(success==0) {
                logger.info("平台退出失败，返回至管理首页");
                return true;
            }
            ((HttpServletResponse) response).sendRedirect(session.getAttribute(BaseSettings.ssohost).toString());
        }else{
            ((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + Global.getAdminPath() + "/eam/login");
        }

        //在这里执行退出系统前需要清空的数据
        UserUtils.clearCache();


        Subject subject = getSubject(request, response);
        try {
            subject.logout();


        } catch (SessionException ise) {
            ise.printStackTrace();
        }
        // issueRedirect(request, response, redirectUrl);
        //返回false表示不执行后续的过滤器，直接返回跳转到登录页面
        return false;

    }
}
