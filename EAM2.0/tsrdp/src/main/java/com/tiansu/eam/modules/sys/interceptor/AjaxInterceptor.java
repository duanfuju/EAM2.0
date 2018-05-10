package com.tiansu.eam.modules.sys.interceptor;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.modules.sys.security.EamAuthorizingRealm;
import com.tiansu.eam.modules.sys.security.PrincipalUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjl on 2017/7/29.
 * 拦截ajax请求，如果session丢失立刻返回登录页面
 */
public class AjaxInterceptor implements HandlerInterceptor {

    private EamAuthorizingRealm eamAuthorizingRealm;
    private Log logger = LogFactory.getLog(AjaxInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        // 针对ajax请求
        String requestType = request.getHeader("X-Requested-With");
//        判断是否ajax请求
        if (StringUtils.equals("XMLHttpRequest", requestType)) {
//            判断session是否存在，不再存在，返回false
            try{
                String requestUri = request.getRequestURI();
                for (String white : getWhiteAjax()){
                    if(requestUri.indexOf(white)>0){
                        return true;
                    }
                }
                getEamAuthorizingRealm().checkAccountLogin((PrincipalUser) SecurityUtils.getSubject().getPrincipal());
            }catch (AuthenticationException e){
                // 重定向
                logger.warn("登录超时："+e.getMessage());
                String path = request.getContextPath();
                String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
                String reLoginUrl = "";
                response.setHeader("SESSIONSTATUS", "TIMEOUT");
                response.setHeader("CONTEXTPATH", basePath+reLoginUrl);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                e.printStackTrace();
                return false;
            }


        }

        return true;
    }

    /**
     * 拦截白名单；发现通过html加载js，如附件管理，checkAccountLogin（）会判断错误。在此处加上白名单过滤
     * @return
     */
    private List<String> getWhiteAjax(){
        List<String> whiteList = new ArrayList<>();
        whiteList.add("ckfinder/ckfinder.js");
        return whiteList;
    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    /**
     * 获取系统业务对象
     */
    public EamAuthorizingRealm getEamAuthorizingRealm() {
        if (eamAuthorizingRealm == null) {
            eamAuthorizingRealm = SpringContextHolder.getBean(EamAuthorizingRealm.class);
        }
        return eamAuthorizingRealm;
    }

}
