package com.tiansu.eam.modules.sso;


/**
*@Create
*@Description :单点登入基础信息
*@Param :  * @param null
*@author : suven
*@Date : 19:26 2017/10/17
*/
public class BaseSettings {
    //
    public static final String ssohostip = "ssohostip";
    //token中登陆参数
    public static final String callbackParamKey = "logins";
    //令牌
    public static final String token = "sso_token";
    //回调
    public static final String jsonp4callbackParamKey = "callback";
    public static final String encryptRule = "[\\s*\t\n\r]";
    public static final String splitRule = "\\|";
    //登陆代码
    public static final String cmdcode = "cmdcode";
    //sso主机
    public static final String ssohost = "ssohost";
    //ssoip
    public static final String ssoip = "ssoip";
    //sso主机地址
    public static final String ssohostAddress = "ssohostAddress";
    //登出接口
    public static final String logoutApiAddress = "/api/v1.00/login/sso/logout";
    //验证接口
    public static final String vifyTokenApiAddress = "/api/v1.00/login/verifyToken";
    //获取平台以及个兄弟平台参数接口
    public static final String getNaviInfoApiAddress = "/api/v1.00/menu/getnavinfo";
    //配置文件noFilter属性
    public static final String notFilterPages = "notFilter";
    public static final String ssotokenErrMessage = "登录令牌不合法，请重新登录！";
    public static final String pageEncodingLanguage = "gb2312";
}
