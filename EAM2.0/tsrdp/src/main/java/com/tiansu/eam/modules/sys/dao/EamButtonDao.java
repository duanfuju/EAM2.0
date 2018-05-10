package com.tiansu.eam.modules.sys.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.sys.entity.Button;
import com.tiansu.eam.modules.sys.entity.ButtonInfo;
import com.tiansu.eam.modules.sys.entity.User;

import java.util.List;
import java.util.Map;

/**
 * Created by tiansu on 2017/7/20.
 */
@MyBatisDao
public interface EamButtonDao extends CrudDao<Button> {

    /**
     * 获取按钮的所有信息
     * @param button
     * @return
     */
    public List<Button> findList(Button button);
    /**
    *@Create
    *@Description :
    *@Param :  * @param null
    *@author : suven
    *@Date : 15:42 2017/10/11
    */
    public List<ButtonInfo> findRelatedList(ButtonInfo button);

    /**
     * 通过用户登录名获取菜单按钮权限
     * @param  user
     */
    public List<Button> getByLoginname(User user);

    public List<Map<String,Object>>getButtonByRoleandMenu(Map<String,Object> map);


}
