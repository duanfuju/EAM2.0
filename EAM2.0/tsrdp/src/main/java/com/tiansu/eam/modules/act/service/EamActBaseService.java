package com.tiansu.eam.modules.act.service;

import com.thinkgem.jeesite.common.persistence.Page;
import org.activiti.engine.query.NativeQuery;
import org.activiti.engine.query.Query;
import org.activiti.engine.runtime.ProcessInstance;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by marsart on 2017/8/10.
 */
public class EamActBaseService{
    public List dataTablePage(Query query, HttpServletRequest request) {

        Map map= new HashMap();
        Page<ProcessInstance> page = new Page();
        String sta = request.getParameter("start")==null?"0":request.getParameter("start");
        String len = request.getParameter("length")==null?"10":request.getParameter("length");

        int start = Integer.parseInt(sta);
        int length = Integer.parseInt(len);

        page.setPageNo(start/length+1);
        page.setPageSize(length);
        List list = query.listPage(start, length);

         return list;
    }

    public List dataTablePage(NativeQuery nativeQuery, HttpServletRequest request) {

        Map map= new HashMap();
        Page<ProcessInstance> page = new Page();
        String sta = request.getParameter("start")==null?"0":request.getParameter("start");
        String len = request.getParameter("length")==null?"10":request.getParameter("length");

        int start = Integer.parseInt(sta);
        int length = Integer.parseInt(len);

        page.setPageNo(start/length+1);
        page.setPageSize(length);
        List list = nativeQuery.listPage(start, length);

        return list;
    }

    /**
     * @param str 源字符串
     * @return
     */
    public static String change(String str) {
        if (str != null) {
            StringBuffer sb = new StringBuffer(str);
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            return sb.toString();
        } else {
            return null;
        }
    }

}
