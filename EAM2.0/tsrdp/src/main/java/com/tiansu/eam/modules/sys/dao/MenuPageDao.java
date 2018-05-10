package com.tiansu.eam.modules.sys.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.sys.entity.MenuPage;

import java.util.List;

/**
 * Created by zhangdf on 2017/7/20.
 */
@MyBatisDao
public interface MenuPageDao extends CrudDao<MenuPage> {
    public List<MenuPage> findList(MenuPage menuPage);
}
