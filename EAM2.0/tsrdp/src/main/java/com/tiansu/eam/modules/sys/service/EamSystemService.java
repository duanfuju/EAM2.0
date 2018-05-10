/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.modules.sys.service;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.security.Digests;
import com.thinkgem.jeesite.common.security.shiro.session.SessionDAO;
import com.thinkgem.jeesite.common.utils.Encodes;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.common.web.Servlets;
import com.tiansu.eam.modules.device.dao.EamDeviceDao;
import com.tiansu.eam.modules.sys.dao.*;
import com.tiansu.eam.modules.sys.entity.*;
import com.tiansu.eam.modules.sys.security.EamAuthorizingRealm;
import com.tiansu.eam.modules.sys.utils.IPUtil;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.activiti.engine.IdentityService;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.System;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * @author ThinkGem
 * @version 2013-12-05
 */
@Service
@Transactional(readOnly = true)
public class EamSystemService  {
	//salt位数
	public static final int SALT_SIZE = 8;

	@Autowired
	private EamUserDao eamUserDao;
	@Autowired
	private EamRoleDao eamRoleDao;
	@Autowired
	private EamMenuDao eamMenuDao;
	@Autowired
	private SessionDAO sessionDao;
	@Autowired
	private EamAuthorizingRealm systemRealm;
	@Autowired
	private EamDeptDao eamDeptDao;
	@Autowired
	private EamDeviceDao eamDeviceDao;
	@Autowired
	private EamDataScopeDao eamDataScopeDao;

	public SessionDAO getSessionDao() {
		return sessionDao;
	}

	@Autowired
	private IdentityService identityService;

	@Autowired
	private EamUserDao sysUserDao;
	@Autowired
	private MenuPageDao menuPageDao;
	//-- User Service --//


//			// 清除用户缓存
//			UserUtils.clearCache(user);
////			// 清除权限缓存
////			systemRealm.clearAllCachedAuthorizationInfo();
//		}

	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword) {
		String plain = Encodes.unescapeHtml(plainPassword);
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, EAMConsts.HASH_INTERATIONS);
		return Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword);
	}
	
	/**
	 * 验证密码
	 * @param plainPassword 明文密码
	 * @param password 密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		String plain = Encodes.unescapeHtml(plainPassword);
		byte[] salt = Encodes.decodeHex(password.substring(0,16));
		byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, EAMConsts.HASH_INTERATIONS);
		return password.equals(Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword));
	}
	
	/**
	 * 获得活动会话
	 * @return
	 */
	public Collection<Session> getActiveSessions(){
		return sessionDao.getActiveSessions(false);
	}
	
	//-- Role Service --//
	//更新或添加新的数据权限范围
	@Transactional(readOnly = false)
	public boolean saveDataScope(String role_id, String data_scope, String custom_detail) {
		// 先删除角色对应的数据范围信息，然后再添加
		//若之前没有角色范围就
        DataScope dataScope = new DataScope(role_id, data_scope, custom_detail);
		int i = eamDataScopeDao.deleteDataScope(role_id);
		if( i >= 0) {
            int j = eamDataScopeDao.insertDataScopeInfo(dataScope);
            if(j > 0){
                return true;
            }
        }
        return false;
	}




	public User getByLoginName(User user){
		return eamUserDao.getByLoginName(user);
	}



	/**
	 * 获取Key加载信息
	 */
	public static boolean printKeyLoadMessage(){
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n======================================================================\r\n");
		sb.append("\r\n    欢迎使用 "+ Global.getConfig("productName")+"  - Powered By http://www.tiansu-china.com/\r\n");
		sb.append("\r\n======================================================================\r\n");
		System.out.println(sb.toString());
		return true;
	}

	/**
	 * 数据权限处理类
	 * @return
	 */
	public List<Dept> getDataScopeDepts(){
		//根据登录用户查询其所有的数据范围配置
		List<Role> roles = UserUtils.getRoleList();
		List<DataScope> dspList = new ArrayList<>();
		if(roles!=null){
			for(Role r : roles){
				if(r.getDataScope()!=null){
					dspList.add(r.getDataScope());
				}
			}
		}

		List<Dept> dataScopeDeptList = getDeptIdsByScope(dspList);

		//去重
		List<String> tmpIdList = new ArrayList<>();
		List<Dept> resultDeptList = new ArrayList<>();
		for(Dept tmpdept : dataScopeDeptList){
			if(!tmpIdList.contains(tmpdept.getDeptno())){
				tmpIdList.add(tmpdept.getDeptno());
				resultDeptList.add(tmpdept);
			}
		}

		return resultDeptList;
	}

	/**
	 * 	根据数据范围配置并取得所有有权限的部门，若结果为空，则标识权限没有配置或只配置了数据本人可见
	 * @param dspList
	 * @return
	 */
	private List<Dept> getDeptIdsByScope(List<DataScope> dspList) {
		//整合出一个所有的拥有权限的部门
		List<Dept> resultdeptList = new ArrayList<>();
		//登录用户所在部门
		String deptId = UserUtils.getUser().getUserdeptno();
		Dept dept = new Dept();
		dept.setDeptno(deptId);
		dept = eamDeptDao.getDept(dept);
		//查询出所有部门，避免迭代循环查询
		List<Dept> allDepts = eamDeptDao.queryDeptList(new Dept());
		for(DataScope dsp : dspList) {
			String dataScope = dsp.getData_scope();
			if(DataScope.DATA_SCOPE_ALL.equals(dataScope)){
				return allDepts;
			}else if(DataScope.DATA_SCOPE_SELF.equals(dataScope)){
				//自己能看到是基本要求，任何情况下都要满足，不需要再做过滤
				continue;
			}else if(DataScope.DATA_SCOPE_COMPANY_AND_CHILD.equals(dataScope)
					|| DataScope.DATA_SCOPE_OFFICE_AND_CHILD.equals(dataScope)){
				resultdeptList.add(dept);
			}else if(DataScope.DATA_SCOPE_COMPANY.equals(dataScope)
					|| DataScope.DATA_SCOPE_OFFICE.equals(dataScope)) {
				List<Dept> tmpDepts = new ArrayList();
				resultdeptList.add(dept);
				List<Dept> subDepts = subDeptList(allDepts,dept.getDeptno(),tmpDepts);
				resultdeptList.addAll(subDepts);
			}else if(DataScope.DATA_SCOPE_CUSTOM.equals(dataScope)){
				String[] depts = dsp.getCustom_detail().split(",");
				if(depts != null ){
					for(String d : depts){
						Dept cdept  = new Dept();
						cdept.setDeptno(d);
						resultdeptList.add(cdept);
					}
				}

			}

		}

		return resultdeptList;
	}

	/**
	 * 根据当前部门获取所有下级子部门
	 * @param dept
	 * @return
	 */
	public List<Dept> getSubDeptList(Dept dept){
		List<Dept> allDepts = eamDeptDao.queryDeptList(new Dept());
		List<Dept> tmpDepts = new ArrayList();
		List<Dept> subDepts = subDeptList(allDepts,dept.getDeptno(),tmpDepts);
		return subDepts;
	}

	private List<Dept> subDeptList( List<Dept> deptList, String pid,List<Dept> childDept) {
		for (Dept mu : deptList) {
			//遍历出父id等于参数的id，add进子节点集合
			if (mu.getParentid().equals(pid)) {
				//递归遍历下一级
				subDeptList(deptList, mu.getDeptno(), childDept);
				childDept.add(mu);
			}
		}
		return childDept;
	}

	///////////////// Synchronized to the Activiti end //////////////////
	//mememe
	public Page<User> findUser(Page<User> page, User eamUser){
		eamUser.setPage(page);
		List<User> aa=sysUserDao.findList(eamUser);
		page.setList(aa);
		return page;
	}

	public User findUserByname(User eamUser){
		eamUser.setLoginname("e");
		User aa=sysUserDao.getByLoginName(eamUser);
		return  aa;
	}
	public Page<MenuPage> findMenuPage(Page<MenuPage> page, MenuPage menuPage){
		menuPage.setPage(page);
        page.setList(menuPageDao.findList(menuPage));
        return page;
	}

	public void updateUserLoginInfo() {
		UserUtils.getUser().setLoginip(IPUtil.getRemoteAddr(Servlets.getRequest()));
	}


	/**
	 * @creator caoh
	 * @createtime 2017-9-19 16:31
	 * @description:获取人员下拉
	 * @return
	 */
	public List<Map> userSelect(){
		return sysUserDao.userSelect();
	}
	/**
	*@Create
	*@Description :从List<User>中遍历专业是否和相关表中的设备专业相同
	*@Param :  * @param null
	*@author : suven
	*@Date : 17:28 2017/11/28
	*/
    public List getSameMajorBetweenUserAndDevice(List users,String business_key){
		String dev_major=eamDeviceDao.getDevMajorByFaultOrderId(business_key);
		List n_user = new ArrayList();
		String u_majors = null;
		String[] u_major = null;
		if(dev_major!=null&&!("").equals(dev_major)) {

			for (Object user : users) {
				u_majors = (String) (((Map) user).get("major"));
				u_major = u_majors.split(",");
				for (String m : u_major) {
					if (m.equals(dev_major)) {
						n_user.add(user);

					}
				}

			}
		}else{
			n_user=users;
		}
		return n_user;
    }
	/**
	 * @creator caoh
	 * @createtime 2017-9-19 16:31
	 * @description:获取角色下拉
	 * @return
	 */
	public List<Map> roleSelect(){
		return eamRoleDao.roleSelect();
	}

}
