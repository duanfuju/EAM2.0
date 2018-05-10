/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.modules.material.entity;

import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;


/**
 * 物料类别 Entity
 * @author caoh
 * @version 2017-08-08
 */
public class MaterialType extends DataEntity<MaterialType> {

	private static final long serialVersionUID = 1L;

	private String id_key;					//主键
	private String type_id;				// 逻辑主键
	private String type_pid;			// 父级类型

	private String 	type_code;			// 编码
	private String 	type_name;			// 名字

	private String 	type_description;	// 描述
	private String 	type_status;		// 状态
	private String 	create_by;		// 创建人
	private Date 	create_time;		// 创建时间
	private String 	update_by;		// 更新人
	private Date 	update_time;		// 更新日期
	private char 	isdelete;		// 删除标志

	public MaterialType() {
		super();
	}

	public MaterialType(String id){
		super(id);
	}

	public String getId_key() {
		return id_key;
	}

	public void setId_key(String id_key) {
		this.id_key = id_key;
	}

	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public String getType_pid() {
		return type_pid;
	}

	public void setType_pid(String type_pid) {
		this.type_pid = type_pid;
	}

	public String getType_code() {
		return type_code;
	}

	public void setType_code(String type_code) {
		this.type_code = type_code;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getType_description() {
		return type_description;
	}

	public void setType_description(String type_description) {
		this.type_description = type_description;
	}

	public String getType_status() {
		return type_status;
	}

	public void setType_status(String type_status) {
		this.type_status = type_status;
	}

	public String getCreate_by() {
		return create_by;
	}

	public void setCreate_by(String create_by) {
		this.create_by = create_by;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getUpdate_by() {
		return update_by;
	}

	public void setUpdate_by(String update_by) {
		this.update_by = update_by;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public char getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(char isdelete) {
		this.isdelete = isdelete;
	}




}