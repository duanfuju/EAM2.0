package com.tiansu.eam.modules.sso;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CallOutlineService extends  SSOService {

    @Override
    public int doService(HttpServletRequest request, HttpServletResponse response) {
        try {
            final String outLine="<script></script>";
            PrintWriter out = response.getWriter();
            out.println(outLine);//返回websocket的嵌入代码供被迫下线通知使用
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
