/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.common.utils.excel.fieldtype.RoleListType;
import com.tiansu.eam.common.persistence.DataEntity;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 用户Entity
 * @author ThinkGem
 * @version 2013-12-05
 */
public class User extends DataEntity<User> {

	private static final long serialVersionUID = 1L;
	private Dept company;	// 归属公司
	private Dept office;	// 归属部门

	private String sysno;//序号id
	private String loginname;//登录名
	private String userpwd;//用户密码
	private Boolean status;//用户状态，0禁用
	private String realname;//姓名
	private String loginphone;//手机号码
	private String loginemail;//邮箱
	private String usercallphone;
	private String userqq;//QQ
	private String userwechat;//微信
	private String usernotetext;

	private String userbakphone;	// 电话
	private String userbakemail;	// 手机
	private String userpic; //头像
	private String userjobno;
	private String usernickname;
	private int usersex;
	private String useridentification;
	private String identificationtype;
	private String userdeptno;
	private String isdeptmanager;
	private String isoutsource;

	private String isdeleted;
	private String loginip;




//	private String loginIp;	// 最后登陆IP
//	private Date loginDate;	// 最后登陆日期
//	private String loginFlag;	// 是否允许登陆


//	private String oldLoginIp;	// 上次登陆IP
//	private Date oldLoginDate;	// 上次登陆日期
	
	private Role role;	// 根据角色查询用户条件
	
	private List<Role> roleList = Lists.newArrayList(); // 拥有角色列表

	public User() {
		super();
		this.status = true;
	}
	
	public User(String sysno){
		this.sysno =sysno;
	}

	public User(String sysno, String loginName){
		this.loginname = loginName;
	}

	public User(Role role){
		super();
		this.role = role;
	}

	@Override
	public String getId(){
		return sysno;
	}
	/*@Override
	public void setId(String id){
		this.sysno = id;
	}*/


	public String getSysno() {
		return sysno;
	}

	public void setSysno(String sysno) {
		this.sysno = sysno;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getUserpwd() {
		return userpwd;
	}

	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getUsercallphone() {
		return usercallphone;
	}

	public void setUsercallphone(String usercallphone) {
		this.usercallphone = usercallphone;
	}

	public String getUserqq() {
		return userqq;
	}

	public void setUserqq(String userqq) {
		this.userqq = userqq;
	}

	public String getUserwechat() {
		return userwechat;
	}

	public void setUserwechat(String userwechat) {
		this.userwechat = userwechat;
	}

	public String getUsernotetext() {
		return usernotetext;
	}

	public void setUsernotetext(String usernotetext) {
		this.usernotetext = usernotetext;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getLoginphone() {
		return loginphone;
	}

	public void setLoginphone(String loginphone) {
		this.loginphone = loginphone;
	}

	public String getLoginemail() {
		return loginemail;
	}

	public void setLoginemail(String loginemail) {
		this.loginemail = loginemail;
	}

	public String getUserbakphone() {
		return userbakphone;
	}

	public void setUserbakphone(String userbakphone) {
		this.userbakphone = userbakphone;
	}

	public String getUserbakemail() {
		return userbakemail;
	}

	public void setUserbakemail(String userbakemail) {
		this.userbakemail = userbakemail;
	}

	public String getUserpic() {
		return userpic;
	}

	public void setUserpic(String userpic) {
		this.userpic = userpic;
	}

	public String getUserjobno() {
		return userjobno;
	}

	public void setUserjobno(String userjobno) {
		this.userjobno = userjobno;
	}

	public String getUsernickname() {
		return usernickname;
	}

	public void setUsernickname(String usernickname) {
		this.usernickname = usernickname;
	}

	public int getUsersex() {
		return usersex;
	}

	public void setUsersex(int usersex) {
		this.usersex = usersex;
	}

	public String getUseridentification() {
		return useridentification;
	}

	public void setUseridentification(String useridentification) {
		this.useridentification = useridentification;
	}

	public String getIdentificationtype() {
		return identificationtype;
	}

	public void setIdentificationtype(String identificationtype) {
		this.identificationtype = identificationtype;
	}

	public String getUserdeptno() {
		return userdeptno;
	}

	public void setUserdeptno(String userdeptno) {
		this.userdeptno = userdeptno;
	}

	public String getIsdeptmanager() {
		return isdeptmanager;
	}

	public void setIsdeptmanager(String isdeptmanager) {
		this.isdeptmanager = isdeptmanager;
	}

	public String getIsoutsource() {
		return isoutsource;
	}

	public void setIsoutsource(String isoutsource) {
		this.isoutsource = isoutsource;
	}

	public String getIsdeleted() {
		return isdeleted;
	}

	public void setIsdeleted(String isdeleted) {
		this.isdeleted = isdeleted;
	}

	public String getLoginip() {
		return loginip;
	}

	public void setLoginip(String loginip) {
		this.loginip = loginip;
	}

	/*@SupCol(isUnique="true", isHide="true")
	@ExcelField(title="ID", type=1, align=2, sort=1)
	public String getId() {
		return id;
	}*/

	@JsonIgnore
	@NotNull(message="归属公司不能为空")
	@ExcelField(title="归属公司", align=2, sort=20)
	public Dept getCompany() {
		return company;
	}

	public void setCompany(Dept company) {
		this.company = company;
	}
	
	@JsonIgnore
	@NotNull(message="归属部门不能为空")
	@ExcelField(title="归属部门", align=2, sort=25)
	public Dept getOffice() {
		return office;
	}

	public void setOffice(Dept office) {
		this.office = office;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@JsonIgnore
	@ExcelField(title="拥有角色", align=1, sort=800, fieldType=RoleListType.class)
	public List<Role> getRoleList() {
		return roleList;
	}
	
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	@JsonIgnore
	public List<String> getRoleIdList() {
		List<String> roleIdList = Lists.newArrayList();
		for (Role role : roleList) {
			roleIdList.add(role.getId());
		}
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		roleList = Lists.newArrayList();
		for (String roleId : roleIdList) {
			Role role = new Role();
			role.setId(roleId);
			roleList.add(role);
		}
	}
	
	/**
	 * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
	 */
	public String getRoleNames() {
		return Collections3.extractToString(roleList, "rolename", ",");
	}
	
	public boolean isAdmin(){
		return isAdmin(this.id);
	}
	
	public static boolean isAdmin(String id){
		return id != null && "1".equals(id);
	}
	
	@Override
	public String toString() {
		return id;
	}
}