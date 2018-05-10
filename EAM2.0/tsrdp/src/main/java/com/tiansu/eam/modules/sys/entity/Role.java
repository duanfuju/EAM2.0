/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.modules.sys.entity;

import com.google.common.collect.Lists;
import com.tiansu.eam.common.persistence.DataEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 角色entity
 * Created by tiansu on 2017/7/13.
 */
public class Role extends DataEntity<Role> {
	
	private static final long serialVersionUID = 1L;

	private String rolename;          //角色名称
	private String rolenote;          //角色备注
	private String rolecode;          //角色标识
	private User user;	                // 根据用户登录名查询角色列表
	private DataScope dataScope;      //拥有数据权限
	private List<Menu> menuList = Lists.newArrayList(); // 拥有菜单列表
	private List<Button> buttonList = Lists.newArrayList();

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getRolenote() {
		return rolenote;
	}

	public void setRolenote(String rolenote) {
		this.rolenote = rolenote;
	}

	public String getRolecode() {
		return rolecode;
	}

	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}

	public Role() {
		super();
	}

	public Role(String id){
		super(id);
	}

	public Role(User user) {
		this();
		this.user = user;
	}

	public DataScope getDataScope() {
		return dataScope;
	}

	public void setDataScope(DataScope dataScope) {
		this.dataScope = dataScope;
	}

	public List<Button> getButtonList() {
		return buttonList;
	}

	public void setButtonList(List<Button> buttonList) {
		this.buttonList = buttonList;
	}

	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

	public List<String> getMenuIdList() {
		List<String> menuIdList = Lists.newArrayList();
		for (Menu menu : menuList) {
			menuIdList.add(menu.getId());
		}
		return menuIdList;
	}

	public void setMenuIdList(List<String> menuIdList) {
		menuList = Lists.newArrayList();
		for (String menuId : menuIdList) {
			Menu menu = new Menu();
			menu.setId(menuId);
			menuList.add(menu);
		}
	}

	public List<String> getButtonIdList() {
		List<String> buttonIdList = Lists.newArrayList();
		for (Button button : buttonList) {
			buttonIdList.add(button.getButtonno());
		}
		return buttonIdList;
	}

	public void setButtonIdList(List<String> buttonIdList) {
		buttonList = Lists.newArrayList();
		for (String buttonId : buttonIdList) {
			Button button = new Button();
			button.setButtonno(buttonId);
			buttonList.add(button);
		}
	}

	public String getMenuIds() {
		return StringUtils.join(getMenuIdList(), ",");
	}

	public void setMenuIds(String menuIds) {
		menuList = Lists.newArrayList();
		if (menuIds != null){
			String[] ids = StringUtils.split(menuIds, ",");
			setMenuIdList(Lists.newArrayList(ids));
		}
	}

	public String getButtonIds() {
		return StringUtils.join(getButtonIdList(), ",");
	}

	public void setButtonIds(String buttonIds) {
		buttonList = Lists.newArrayList();
		if (buttonIds != null){
			String[] ids = StringUtils.split(buttonIds, ",");
			setButtonIdList(Lists.newArrayList(ids));
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
