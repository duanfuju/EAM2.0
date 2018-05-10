package com.tiansu.eam.modules.sys.service;

import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.sys.dao.EamButtonDao;
import com.tiansu.eam.modules.sys.entity.Button;
import com.tiansu.eam.modules.sys.entity.ButtonInfo;
import com.tiansu.eam.modules.sys.entity.User;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tiansu on 2017/7/20.
 */
@Service
@Transactional(readOnly = true)
public class EamButtonService extends CrudService<EamButtonDao, Button> {

    @Autowired
    private EamButtonDao buttonDao;
    List<Button> buttonList = null;

    /**
    *@Create
    *@Description :
    *@Param :  对buttonno split
    *@author : suven
    *@Date : 18:35 2017/11/15
    */
    public List<Map<String, Object>> getButtons(List<Map<String, Object>> blist){
        if(blist.size()>0){
        for(Map<String, Object> bmap:blist){
            bmap.put("buttonno",((String)bmap.get("buttonno")).split("_")[1]);
        }
        }
       return blist;
    }
    public List<Button> findList(){
        buttonList = buttonDao.findList(new Button());

        return buttonList;
    }
    List<ButtonInfo> buttonInfoList = null;
    public List<ButtonInfo> findRelatedList(){
        buttonInfoList = buttonDao.findRelatedList(new ButtonInfo());
        return buttonInfoList;
    }

//获取对应角色菜单的按钮
    public List<Map<String, Object>> getButtonByRoleandMenu(){

        HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String id=request.getParameter("id");
        User user=UserUtils.getUser();//获取当前角色
       if(id !=null && !"".equals(id) && user!=null){
               String loginname=user.getLoginname();
           Map<String,Object> map=new HashMap<String,Object>();
           map.put("id",id);
           map.put("loginname",loginname);
          List<Map<String, Object>> buttons= buttonDao.getButtonByRoleandMenu(map);
          buttons=getButtons(buttons);
          //String b= JSON.toJSONString(buttons);
           return buttons;
       }
        return null;
    }
}
