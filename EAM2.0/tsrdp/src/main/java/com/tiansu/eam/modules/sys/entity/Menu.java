/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tiansu.eam.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单Entity
 * @author ThinkGem
 * @version 2013-05-15
 */
public class Menu extends DataEntity<Menu> {

	private static final long serialVersionUID = 1L;
	private Menu parent;    // 父级菜单
	private String parentId; // 父级编号
	private String menuno;   // 菜单编号
	private String subsystemno;  //子系统编号
	private String menuname;    // 名称
	private String link;    // 链接
	//	private String target; 	// 目标（ mainFrame、_blank、_self、_parent、_top）
	private String icon;    // 图标
	private Integer orderId;    // 排序

	private String userLoginName;

	public Menu() {
		super();
		this.orderId = 30;
	}

	public Menu(String id) {
		super(id);
	}

	@JsonBackReference
	@NotNull
	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	@Length(min = 1, max = 2000)
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getMenuno() {
		return menuno;
	}

	public void setMenuno(String menuno) {
		this.menuno = menuno;
	}

	public String getSubsystemno() {
		return subsystemno;
	}

	public void setSubsystemno(String subsystemno) {
		this.subsystemno = subsystemno;
	}

	public String getMenuname() {
		return menuname;
	}

	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	@JsonIgnore
	public static void sortList(List<Menu> list, List<Menu> sourcelist, String parentId, boolean cascade) {
		for (int i = 0; i < sourcelist.size(); i++) {
			Menu e = sourcelist.get(i);
			if (e.getParent() != null && e.getParent().getId() != null
					&& e.getParent().getId().equals(parentId)) {
				list.add(e);
				if (cascade) {
					// 判断是否还有子节点, 有则继续获取子节点
					for (int j = 0; j < sourcelist.size(); j++) {
						Menu child = sourcelist.get(j);
						if (child.getParent() != null && child.getParent().getId() != null
								&& child.getParent().getId().equals(e.getId())) {
							sortList(list, sourcelist, e.getId(), true);
							break;
						}
					}
				}
			}
		}
	}

	@JsonIgnore
	public static String getRootId() {
		return "1";
	}

	public String getUserLoginName() {
		return userLoginName;
	}

	public void setUserLoginName(String userLoginName) {
		this.userLoginName = userLoginName;
	}

	@Override
	public String toString() {
		return menuname;
	}


	public static List<MenuTreeData> convertMenuListToTree(List<Menu> menuList) {
		if (menuList == null) {
			return null;
		}
		List<MenuTreeData> menuTreeList = new ArrayList();
		List<MenuTreeData> resultList = new ArrayList<>();
		for (Menu menu : menuList) {
			MenuTreeData tree = new MenuTreeData(menu.getMenuno(), menu.getParentId(),
					menu.getMenuname(), menu.getLink(), menu.getIcon(), "", menu.getOrderId());
			tree.setParentId(menu.getParentId());
			tree.setChildren(new ArrayList());
			menuTreeList.add(tree);
		}

		for (MenuTreeData tree : menuTreeList) {
			for (MenuTreeData t : menuTreeList) {
				if (t.getParentId().equals(tree.getId())) {
					if (tree.getChildren() == null || tree.getChildren().size() == 0) {
						List<MenuTreeData> myChildrens = new ArrayList();
						myChildrens.add(t);
						tree.setChildren(myChildrens);
					} else {
						tree.getChildren().add(t);
					}
				}
			}
		}


		for (MenuTreeData tree : menuTreeList) {
			if (MenuTreeData.ROOT_PARENT.equals(tree.getParentId())) {
				resultList.add(tree);
			}

		}


		return resultList;
	}

}

	class MenuTreeData<T> {

		public static final String ROOT_PARENT = "0";
		private static final long serialVersionUID = 1L;
		//菜单id
		private String id;
		//上级菜单
		private String parentId;

		//菜单图标
		private String icon;
		//菜单名称
		private String text;
		//菜单链接
		private String href;
		//菜单tip
		private String tip;
		//下级菜单
		private List<T> children;

		private int order;

		public MenuTreeData(String id,String parentId,String text,String href,String icon,String tip,int order) {
			this.id = id;
			this.parentId = parentId;
			this.icon = icon;
			this.text = text;
			this.href = href;
			this.tip = tip;
			this.order = order;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getParentId() {
			return parentId;
		}

		public void setParentId(String parentId) {
			this.parentId = parentId;
		}

		public String getIcon() {
			return icon;
		}

		public void setIcon(String icon) {
			this.icon = icon;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getHref() {
			return href;
		}

		public void setHref(String href) {
			this.href = href;
		}

		public String getTip() {
			return tip;
		}

		public void setTip(String tip) {
			this.tip = tip;
		}

		public List<T> getChildren() {
			return children;
		}

		public void setChildren(List<T> children) {
			this.children = children;
		}

		public int getOrder() {
			return order;
		}

		public void setOrder(int order) {
			this.order = order;
		}

}