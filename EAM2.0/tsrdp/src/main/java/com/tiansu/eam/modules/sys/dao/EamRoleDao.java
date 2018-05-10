/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.modules.sys.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.sys.entity.Role;
import com.tiansu.eam.modules.sys.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 角色DAO接口
 * @author ThinkGem
 * @version 2017-07-20
 */
@MyBatisDao
public interface EamRoleDao extends CrudDao<Role> {

	/**
	 * 根据角色编号获取角色信息
	 * @param role
	 * @return
	 */
	public Role getByName(Role role);

	/**
	 * 根据用户获取角色信息
	 * @param role
	 * @return
	 */
	public List<Role> findListByUser(Role role);

	/**
	 * 根据角色获取数据范围
	 * @param role
	 * @return
	 */
	public Role getDataScopeByName(Role role);

	public List<Role> findListByUser(User user);

	public List<Map> roleSelect();

	public List<String> getUserIdByRole(String roleno);

}
