/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.modules.sys.utils;

import com.alibaba.fastjson.JSONArray;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.modules.sys.dao.*;
import com.tiansu.eam.modules.sys.entity.*;
import com.tiansu.eam.modules.sys.model.fieldcontrol.PageFieldConfigModel;
import com.tiansu.eam.modules.sys.security.PrincipalUser;
import com.tiansu.eam.modules.sys.service.EamFieldControllService;
import com.tiansu.eam.modules.sys.service.EamSystemService;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 用户工具类
 * @author ThinkGem
 * @version 2013-12-05
 */
public class UserUtils {

	private static EamUserDao eamUserDao = SpringContextHolder.getBean(EamUserDao.class);
	private static EamRoleDao eamRoleDao = SpringContextHolder.getBean(EamRoleDao.class);
	private static EamMenuDao eamMenuDao = SpringContextHolder.getBean(EamMenuDao.class);
	private static EamSystemService eamSystemService = SpringContextHolder.getBean(EamSystemService.class);
	private static EamFieldControllService fieldControllService = SpringContextHolder.getBean(EamFieldControllService.class);

//	private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);
	private static EamDeptDao eamDeptDao = SpringContextHolder.getBean(EamDeptDao.class);
	private static EamButtonDao eamButtonDao = SpringContextHolder.getBean(EamButtonDao.class);

	public static final String USER_CACHE = "userCache";
	public static final String USER_CACHE_ID_ = "id_";
	public static final String USER_CACHE_LOGIN_NAME_ = "ln";
	public static final String USER_CACHE_LIST_BY_DEPT_ID_ = "oid_";

	public static final String CACHE_ROLE_LIST = "roleList";
	public static final String CACHE_MENU_LIST = "menuList";
	public static final String CACHE_AREA_LIST = "areaList";
	public static final String CACHE_DEPT_LIST = "deptList";
	public static final String CACHE_DEPT_ALL_LIST = "deptAllList";
	public static final String CACHE_BUTTON_LIST = "buttonList";
	
	/**
	 * 根据ID获取用户
	 * @param id
	 * @return 取不到返回null
	 */
	public static User get(String id){
		User user = (User)CacheUtils.get(USER_CACHE, USER_CACHE_ID_ + id);
		if (user ==  null){
			user = eamUserDao.get(id);
			if (user == null){
				return null;
			}
			user.setRoleList(eamRoleDao.findListByUser(user));
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
			CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginname(), user);
		}
		return user;
	}
	
	/**
	 * 根据登录名,邮箱名或者手机号 获取用户
	 * @param loginName
	 * @return 取不到返回null
	 */
	public static User getByLoginName(String loginName){
//		User user = (User)CacheUtils.get(USER_CACHE, USER_CACHE_LOGIN_NAME_ + loginName);
		User user = null;
		if (user == null){
			user = eamUserDao.getByLoginName(new User(null, loginName));
			if (user == null){
				return null;
			}
			user.setRoleList(eamRoleDao.findListByUser(user));
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
			CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginname(), user);
		}
		return user;
	}
	
	/**
	 * 清除当前用户缓存
	 */
	public static void clearCache(){
		removeCache(CACHE_ROLE_LIST);
		removeCache(CACHE_MENU_LIST);
		removeCache(CACHE_AREA_LIST);
		removeCache(CACHE_DEPT_LIST);
		removeCache(CACHE_DEPT_ALL_LIST);
		removeCache(CACHE_BUTTON_LIST);
		UserUtils.clearCache(getUser());
	}

	/**
	*@Author :suven
	*@Date :14:48 2017/10/31
	*@Description : *清空所有session
	*/
	public static void initSession(HttpServletRequest request){
		Enumeration em = request.getSession().getAttributeNames();
		while(em.hasMoreElements()){
			request.getSession().removeAttribute(em.nextElement().toString());
		}
	}
	/**
	 * 清除指定用户缓存
	 * @param user
	 */
	public static void clearCache(User user){
		CacheUtils.remove(USER_CACHE, USER_CACHE_ID_ + user.getId());
		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginname());
//		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getOldLoginName());
	}
	
	/**
	 * 获取当前用户
	 * @return 取不到返回 new User()
	 */
	public static User getUser(){
		PrincipalUser principal = getPrincipal();
		if (principal!=null){
			User user = get(principal.getId());
			if (user != null){
				return user;
			}
			return new User();
		}
		// 如果没有登录，则返回实例化空的User对象。
		return new User();
	}

	/**
	 * 获取当前用户角色列表
	 * @return
	 */
	public static List<Role> getRoleList(){
		@SuppressWarnings("unchecked")
		List<Role> roleList = (List<Role>)getCache(CACHE_ROLE_LIST);
		if (roleList == null){
			User user = getUser();
			if (user.isAdmin()){
				roleList = eamRoleDao.findAllList(new Role());
			}else{
				roleList = eamRoleDao.findListByUser(user);
			}
			putCache(CACHE_ROLE_LIST, roleList);
		}
		return roleList;
	}
	
	/**
	 * 获取当前用户数据权限部门列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getDataScopeDeptIds(){
		//获取当前登录用户拥有的数据权限部门
		List<Dept> resultDeptList = eamSystemService.getDataScopeDepts();

		String deptIds = "";
		for(Dept dept : resultDeptList){
			deptIds += "'"+dept.getDeptno() +"',";
		}
		if(deptIds.endsWith(",")){
			deptIds = deptIds.substring(0,deptIds.length()-1);
		}
		return deptIds;
	}

	/**
	 * 获取当前用户数据权限人员列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getDataScopeUserIds(){
		String userIds = getUser().getLoginname();
		//获取当前登录用户拥有的数据权限部门
		String  deptIds = getDataScopeDeptIds();
		if(StringUtils.isEmpty(deptIds)){
			return userIds;
		}
//		根据部门查询人员
		User paramUser = new User();
		paramUser.setUserdeptno(deptIds);
		List<User> userList = eamUserDao.findUserByDeptno(paramUser);
		String queryUsers = "";
		if(userList != null){
			for(User u : userList){
				queryUsers += u.getLoginname()+",";
			}
			queryUsers += userIds;
			return queryUsers;
		}else{
			return userIds;
		}

	}

	/**	 * 获取当前用户授权菜单
	 * @return
	 */
	public static List<Menu> getMenuList(){
		@SuppressWarnings("unchecked")
		List<Menu> menuList = (List<Menu>)getCache(CACHE_MENU_LIST);
		if (menuList == null){
			User user = getUser();
			if (user.isAdmin()){
				menuList = eamMenuDao.findAllList(new Menu());
			}else{
				Menu m = new Menu();
				//一体化平台中是讲userlogingnmae当做角色用户关联表外键
				m.setUserLoginName(user.getLoginname());
				menuList = eamMenuDao.getByUserLoginName(m);
			}
			putCache(CACHE_MENU_LIST, menuList);
		}
		return menuList;
	}

	/**
	 * 获取当前用户授权按钮
	 * @return
	 */
	public static List<Button> getButtonList(){
		@SuppressWarnings("unchecked")
		List<Button> buttonList = (List<Button>)getCache(CACHE_BUTTON_LIST);
		if (buttonList == null){
			User user = getUser();
			if (user.isAdmin()){
				buttonList = eamButtonDao.findList(new Button());
			}else{
				buttonList = eamButtonDao.getByLoginname(user.getCurrentUser());
			}
			putCache(CACHE_BUTTON_LIST, buttonList);
		}
		return buttonList;
	}

	/**
	 * 根据菜单id获取当前登录用户字段数据
	 * @param menuno
	 * @return
	 */
	public static List<PageFieldConfigModel> getMenuFieldConfigList(String menuno){
		List<PageFieldConfigModel> menuFieldConnfigList = new ArrayList<>();
		//获取当前用户角色，并根据角色和菜单查询出配置的字段权限；
		List<Role> roleList = UserUtils.getUser().getRoleList();
		//根据不同角色查询不同的配置，并取配置并集
		if(roleList != null){
			//因为菜单配置的字段数固定，所以每个角色配置的字段总数也固定，定义一个临时变量tempMap<字段名称，改字段名对应的配置项>，用于每个角色的配置项去重，最终合并成一个配置项
			Map<String,PageFieldConfigModel> tempMap = new LinkedMap();
			//每个角色
			for(Role role : roleList){
				FieldControl control = fieldControllService.getFieldControl(role.getRolecode(),menuno);
				if(control!=null){
					List<PageFieldConfigModel> fieldConfigList = JSONArray.parseArray((String)control.getContent(),PageFieldConfigModel.class);
					if(fieldConfigList != null){
						for(PageFieldConfigModel config : fieldConfigList){
							PageFieldConfigModel mergeConfig = new PageFieldConfigModel();
							mergeConfig.setFieldName(config.getFieldName());
							mergeConfig.setDisplayName(config.getDisplayName());
							mergeConfig.setGridWidth(config.getGridWidth());
							mergeConfig.setMarginType(config.getMarginType());
							mergeConfig.setValidateType(config.getValidateType());
							if("false".equals(mergeConfig.isShowInGrid()) || mergeConfig.isShowInGrid() == null){
								mergeConfig.setShowInGrid(config.isShowInGrid());
							}
							if("false".equals(mergeConfig.getShowInSearch()) || mergeConfig.getShowInSearch() == null){
								mergeConfig.setShowInSearch(config.getShowInSearch());
							}
							if("false".equals(mergeConfig.getShowInForm()) || mergeConfig.getShowInForm() == null){
								mergeConfig.setShowInForm(config.getShowInForm());
							}
							if("false".equals(mergeConfig.getEditable()) || mergeConfig.getEditable() == null){
								mergeConfig.setEditable(config.getEditable());
							}
							if("false".equals(mergeConfig.getNullable()) || mergeConfig.getNullable() == null){
								mergeConfig.setNullable(config.getNullable());
							}
							//不断覆盖，取最后一个作为合并后的结果
							tempMap.put(mergeConfig.getFieldName(),mergeConfig);
						}
					}
				}
			}

			//合并后的结果都存到了tempMap中，key为配置的字段名，value为改字段名对应的配置项
			Iterator<Map.Entry<String, PageFieldConfigModel>> iterator = tempMap.entrySet().iterator();
			while (iterator.hasNext()){
				Map.Entry entry = iterator.next();
				PageFieldConfigModel pfcm = (PageFieldConfigModel) entry.getValue();
				menuFieldConnfigList.add(pfcm);
			}

		}else {
			return menuFieldConnfigList;
		}



		return menuFieldConnfigList;
	}

	/**
	 * 获取当前用户有权限访问的部门
	 * @return
	 */
	public static List<Dept> getdeptAllList(){
		@SuppressWarnings("unchecked")
		List<Dept> deptList = (List<Dept>)getCache(CACHE_DEPT_ALL_LIST);
		if (deptList == null){
			deptList = eamDeptDao.findAllList(new Dept());
		}
		return deptList;
	}
	
	/**
	 * 获取授权主要对象
	 */
	public static Subject getSubject(){
		return SecurityUtils.getSubject();
	}
	
	/**
	 * 获取当前登录者对象
	 */
	public static PrincipalUser getPrincipal(){
		try{
			Subject subject = SecurityUtils.getSubject();
			PrincipalUser principal = (PrincipalUser)subject.getPrincipal();
			if (principal != null){
				return principal;
			}
//			subject.logout();
		}catch (UnavailableSecurityManagerException e) {
			
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	public static Session getSession(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession(false);
			if (session == null){
				session = subject.getSession();
			}
			if (session != null){
				return session;
			}
//			subject.logout();
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	// ============== User Cache ==============
	
	public static Object getCache(String key) {
		return getCache(key, null);
	}
	
	public static Object getCache(String key, Object defaultValue) {
//		Object obj = getCacheMap().get(key);
		Object obj = getSession().getAttribute(key);
		return obj==null?defaultValue:obj;
	}

	public static void putCache(String key, Object value) {
//		getCacheMap().put(key, value);
		getSession().setAttribute(key, value);
	}

	public static void removeCache(String key) {
//		getCacheMap().remove(key);
		getSession().removeAttribute(key);
	}
	
//	public static Map<String, Object> getCacheMap(){
//		Principal principal = getPrincipal();
//		if(principal!=null){
//			return principal.getCacheMap();
//		}
//		return new HashMap<String, Object>();
//	}
}
