package com.tiansu.eam.modules.sso;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
*@Create
*@Description :请求服务各种操作起始位置
*@Param :  * @param null
*@author : suven
*@Date : 19:03 2017/10/16
*/
public class SessionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    // demo for sso filter
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            final int cmdCode = 3; // vifyServiceHander
            HttpServletRequest req = (HttpServletRequest) request;
            //判断是否过滤
            boolean doFilter = isdoFilter(req);
            if (doFilter) {

                SSOService ssoService = ClientCallBack.build(cmdCode);
                ssoService.doService(req, (HttpServletResponse) response);
                filterChain.doFilter(request, response);
            } else {
                filterChain.doFilter(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * check isneeed filter urlpath
    * author:xiaxing
    *
    * */
    /**
    *@Modify
    *@Description :对请求进行是否存在过滤条件进行判断
    *@Param :  * @param null
    *@author : suven
    *@Date : 19:04 2017/10/16
    */
    private boolean isdoFilter(HttpServletRequest request) {
        boolean doFilter = true;
        // 不过滤的uri
        String[] notFilter =new String[]{};
        notFilter=request.getSession().getServletContext().
                getInitParameter(BaseSettings.notFilterPages).split(",");
        String uri = request.getRequestURI();
        for (String s : notFilter) {
            if (uri.contains(s)) {
                doFilter = false;
                break;
            }
        }
        return doFilter;
    }

    @Override
    public void destroy() {

    }


}
