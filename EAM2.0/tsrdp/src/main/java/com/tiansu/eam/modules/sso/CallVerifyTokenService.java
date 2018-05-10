package com.tiansu.eam.modules.sso;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinkgem.jeesite.common.config.Global;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class CallVerifyTokenService extends SSOService {
   @Override
   public int doService(HttpServletRequest request, HttpServletResponse response) {
       int success = 0;
       try {
           // 从session中获取登录者实体
           Object obj = request.getSession().getAttribute(BaseSettings.token);// 子系统中认证用户的token变量
           // 如果session中不存在登录者实体，则弹出框提示重新登录
           // 设置request和response的字符集，防止乱码
           request.setCharacterEncoding(BaseSettings.pageEncodingLanguage);
           response.setCharacterEncoding(BaseSettings.pageEncodingLanguage);
           PrintWriter out = response.getWriter();
           String hostUrl = Global.getConfig(BaseSettings.ssohostAddress);
           String strBackUrl = "http://" + request.getServerName() //服务器地址
                   + ":"
                   + request.getServerPort()       //端口号
                   + request.getContextPath()      //项目名称
                   + request.getServletPath();

           if (request.getQueryString() != null) {
               strBackUrl += "?" + (request.getQueryString()); //参数
           }
           strBackUrl = EncryptUtil.encrypt64(strBackUrl).replaceAll(BaseSettings.encryptRule, StringUtils.EMPTY);
           String loginPage =
                   hostUrl + "/login.html?logins=" + strBackUrl;
           StringBuilder builder = new StringBuilder();
           if (obj == null) {
               builder.append("<script>window.location='").append(loginPage).append("'</script>");
           } else {
               boolean flag = sendVifyToken(request, hostUrl);
               if (!flag) {
                   builder.append("<script>alert('" + BaseSettings.ssotokenErrMessage + "');window.location='").append(loginPage).append("'</script>");
               }
           }
           out.print(builder.toString());
           success = 1;
       } catch (Exception ex) {
           ex.printStackTrace();
       }
       return success;
   }

   /*
   * 发送token去服务器端验证
   * */
   private boolean sendVifyToken(HttpServletRequest request, String hostUrl) {
       try {
           HttpSession session = request.getSession();
           String token = session.getAttribute(BaseSettings.token) == null ?
                   StringUtils.EMPTY : session.getAttribute(BaseSettings.token).toString();
           HttpClient httpclient = new DefaultHttpClient();
           HttpPost httpPost = new HttpPost(hostUrl + BaseSettings.vifyTokenApiAddress);
           List<NameValuePair> params = new ArrayList<>();
           params.add(new BasicNameValuePair(BaseSettings.token, token));
           UrlEncodedFormEntity entity;
           entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
           entity.setContentType("application/x-www-form-urlencoded");
           httpPost.setEntity(entity);
           HttpResponse httpResponse = httpclient.execute(httpPost);
           String result = inputStreamToString(httpResponse.getEntity().getContent());
           if (StringUtils.isNotEmpty(result)) {
               ObjectMapper mapper = new ObjectMapper();
               ResponseBaseResult responseBaseResult;
               responseBaseResult = mapper.readValue(result, ResponseBaseResult.class);
               if (responseBaseResult != null) {
                   if (responseBaseResult.getResultBody()) {
                       return true;
                   }
               }
               return false;
           }
           return false;
       } catch (IOException e) {
           e.printStackTrace();
           return false;
       } catch (RuntimeException ex) {
           return false;
       }
   }

   private String inputStreamToString(InputStream is) throws IOException {

       String line;
       StringBuilder total = new StringBuilder();
       BufferedReader rd = new BufferedReader(new InputStreamReader(is));
       while ((line = rd.readLine()) != null) {
           total.append(line);
       }
       return total.toString();
   }
}
