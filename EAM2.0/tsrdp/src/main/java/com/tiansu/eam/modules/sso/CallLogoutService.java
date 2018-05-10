package com.tiansu.eam.modules.sso;

import com.thinkgem.jeesite.common.config.Global;
import com.tiansu.eam.modules.sys.interceptor.AjaxInterceptor;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

public class CallLogoutService extends SSOService {



    @Override
   public int doService(HttpServletRequest request, HttpServletResponse response) {
        int success = 0;
        try {
            // 远程调用认证中心销毁
                String ssohost;
                //獲取session
                HttpSession session = request.getSession();
                //獲取用戶
                String username =(String)session.getAttribute("userCode");
               //獲取主機地址
            if (session.getAttribute(BaseSettings.ssohost) == null) {
                ssohost = Global.getConfig(BaseSettings.ssohostAddress);
                session.setAttribute(BaseSettings.ssohost, ssohost);
            } else
                ssohost = session.getAttribute(BaseSettings.ssohost).toString();
            //獲取sso_token
                String token = session.getAttribute(BaseSettings.token) == null ?
                          StringUtils.EMPTY : session.getAttribute(BaseSettings.token).toString();
               if (token != null  && !token.equals("")) {

                   //拼接請求
                   String requestUrl = ssohost + BaseSettings.logoutApiAddress + "?token=" + token;
                   //獲取請求返回值
                   String content = new HttpClientUtil(requestUrl).get();
                   JSONObject jsonContext = JSONObject.fromObject(content);
                   if ((boolean) jsonContext.get("success")) {
                       success = 1;
                       logger.info("退出用戶" + username + "\r\n" + "请求:" + requestUrl + "\r\n" + "单点登录返回内容：" + content);

                   }
               }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return success;
    }

    private JSONObject httpPost(String url, List<NameValuePair> params) {
        JSONObject jsonResult = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (entity != null) {
            entity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(entity);
        }
        try {
            HttpResponse result = httpclient.execute(httpPost);
            url = URLDecoder.decode(url, "UTF-8");
            /**请求发送成功，并得到响应**/
            if (result.getStatusLine().getStatusCode() == 200) {
                String str = "";
                try {
                    /**读取服务器返回过来的json字符串数据**/
                    str = EntityUtils.toString(result.getEntity());

                    /**把json字符串转换成json对象**/
                    jsonResult = JSONObject.fromObject(str);
                } catch (Exception e) {
                    logger.error("post请求提交失败:" + url, e);
                }
            }
        } catch (IOException e) {
            logger.error("post请求提交失败:" + url, e);


        }
        return jsonResult;
    }

}
