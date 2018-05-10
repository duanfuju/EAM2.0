/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.modules.sys.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.sys.entity.Menu;
import com.tiansu.eam.modules.sys.entity.MenuInfo;
import com.tiansu.eam.modules.sys.entity.Role;

import java.util.List;

/**
 * 菜单DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface EamMenuDao extends CrudDao<Menu> {
	/**
	*@Create
	*@Description :获得注册子系统相关信息
	*@Param :  * @param null
	*@author : suven
	*@Date : 15:51 2017/10/11
	*/
	public List<MenuInfo> getRelatedMenuList(MenuInfo menu);

	public List<Menu> getMenuList(Menu menu);

	public List<Menu> findByParentIdsLike(Menu menu);

	public List<Menu> getByUserLoginName(Menu menu);

	public int updateParentIds(Menu menu);

	/**
	 * 根据角色和菜单获取菜单详情信息，包括菜单详情，字段权限，按钮权限
	 * @param role
	 * @param menu
	 * @return
	 */
	public Menu getMenuDetailInfo(Role role,Menu menu);

	public int updateSort(Menu menu);

}
