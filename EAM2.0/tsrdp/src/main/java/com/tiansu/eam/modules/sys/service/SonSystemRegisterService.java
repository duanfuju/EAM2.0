package com.tiansu.eam.modules.sys.service;/**
 * Created by suven on 2017/10/11.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.tiansu.eam.modules.sys.dao.EamSystemDao;
import com.tiansu.eam.modules.sys.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 子系统注册
 *
 * @author suven suven
 * @create 2017/10/11
 */
@Service
@Transactional(readOnly = true)
public class SonSystemRegisterService {
    @Autowired
    EamButtonService eamButtonService;
    @Autowired
    EamMenuService menuService;
    @Autowired
    EamSystemDao systemDao;
    /**
    *@Create
    *@Description :获得左右相关数据组装成json字符串
    *@Param :  * @param null
    *@author : suven
    *@Date : 11:04 2017/10/11
    */
    public String getAllRelatedInfoToJsonString(){
        StringBuilder json=new StringBuilder();
        json.append("{\"sys\":");
        List<SystemInfo> systemInfoList =systemDao.findRelatedList(new SystemInfo());
        String sysJson= JSONArray.toJSONString(systemInfoList, filter);
        sysJson=sysJson.substring(1,sysJson.length()-1);
        json.append(sysJson).append(",\"menus\":" );
       List<MenuInfo> menuList=menuService.getRelatedMenuList();
        String menuJson= JSONArray.toJSONString(menuList, filter);
        json.append(menuJson).append(",\"buttons\":" );
       List<ButtonInfo> buttonList= eamButtonService.findRelatedList();
        String buttonJson=JSONArray.toJSONString(buttonList, filter);
        json.append(buttonJson).append("}");


      return json.toString();

    }
    private static ValueFilter filter = new ValueFilter() {
        @Override
        public Object process(Object obj, String s, Object v) {
            if (v == null){
                if(s.equals("orderid")||s.equals("parentid")){
                    return 0;
                }
                return "";}
            return v;
        }
    };


}
