package com.tiansu.eam.modules.sso;

import com.tiansu.eam.modules.sys.interceptor.AjaxInterceptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.web.filter.authc.LogoutFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public  class SSOService extends LogoutFilter{
   public Log logger = LogFactory.getLog(AjaxInterceptor.class);   //日志记录
   public int doService(HttpServletRequest request, HttpServletResponse response){
      return 0;
   };




}
