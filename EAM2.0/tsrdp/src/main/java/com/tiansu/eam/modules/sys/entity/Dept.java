/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.modules.sys.entity;


import com.thinkgem.jeesite.common.persistence.TreeEntity;

/**
 * 部门entity
 * Created by tiansu on 2017/7/13.
 */
public class Dept extends TreeEntity<Dept> {
	private static final long serialVersionUID = 1L;

	private String deptno;       //部门编号

	private String deptname;    //部门名称

	private String deptnote;    //部门备注

	private String deptroles;   //部门授权角色

	private String parentid;      //上级部门编号

	private int depttypeno;      //部门类型

	public Dept() {
		super();
	}

	public Dept(String id) {
		super(id);
	}

	public void setParent(Dept dept){
		this.parent = dept;
	}

	public Dept getParent(){
		return parent;
	}

	public String getDeptno() {
		return deptno;
	}

	public void setDeptno(String deptno) {
		this.deptno = deptno;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getDeptnote() {
		return deptnote;
	}

	public void setDeptnote(String deptnote) {
		this.deptnote = deptnote;
	}

	public String getDeptroles() {
		return deptroles;
	}

	public void setDeptroles(String deptroles) {
		this.deptroles = deptroles;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public int getDepttypeno() {
		return depttypeno;
	}

	public void setDepttypeno(int depttypeno) {
		this.depttypeno = depttypeno;
	}

	@Override
	public String toString() {
		return name;
	}
}