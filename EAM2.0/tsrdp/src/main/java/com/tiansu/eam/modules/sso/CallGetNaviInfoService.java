package com.tiansu.eam.modules.sso;/**
 * Created by suven on 2017/10/24.
 */

import com.thinkgem.jeesite.common.config.Global;
import com.tiansu.eam.modules.sys.entity.SystemInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 从平台接口中获得所有子平台信息
 *
 * @author suven suven
 * @create 2017/10/24
 */
public class CallGetNaviInfoService extends SSOService {
    @Override
    public int doService(HttpServletRequest request, HttpServletResponse response){
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
                String requestUrl = ssohost + BaseSettings.getNaviInfoApiAddress + "?token=" + token;
                //獲取請求返回值
                String content = new HttpClientUtil(requestUrl).get();
                JSONObject jsonContext = JSONObject.fromObject(content);
                if (!(boolean) jsonContext.get("success")) {

                    JSONArray resBody=(JSONArray) jsonContext.get("resultBody");
                    String subsystemno="";
                    String ssName="";
                    String subcallbackurl="";
                    String subsystemnos="";
                    for(int i=0;i<resBody.size();i++){
                        JSONObject rb=(JSONObject)resBody.get(i);
                        subsystemno=rb.getString("subsystemno");
                        ssName=rb.getString("subsystemname");
                        subcallbackurl=rb.getString("subsystemlink")+"?subsystemno="+subsystemno;
                        SystemInfo systemInfo=new SystemInfo();
                        systemInfo.setSubsystemno(subsystemno);
                        systemInfo.setSubsystemname(ssName);
                        systemInfo.setSubcallbackurl(subcallbackurl);
                        session.setAttribute(subsystemno,systemInfo);

                            subsystemnos+=subsystemno+",";


                            session.setAttribute("subsystemnos",subsystemnos);

                    }

                    super.logger.info("获得子平台共" + resBody.size() + "个\r\n" + "请求:" + requestUrl + "\r\n" + "单点登录返回内容：" + content);
                    success = 1;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return success;
    };
}
