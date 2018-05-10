package com.tiansu.eam.common.utils.excel.entity;

import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;

   /**
    * ts_material_type 实体类
    * Mon Jul 24 19:49:54 CST 2017 dfj
    */ 


public class TsMaterialType extends  DataEntity<TsMaterialType> {
 	private String id;
	private String type_code;
	private String type_name;
	private Integer pId;
	private String description;
	private Integer status;
	private String company;
	private Date create_time;
	private Date modify_time;
	public void setId(String id){
		this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setTypeCode(String type_code){
		this.type_code=type_code;
	}
	public String getTypeCode(){
		return type_code;
	}
	public void setTypeName(String type_name){
		this.type_name=type_name;
	}
	public String getTypeName(){
		return type_name;
	}
	public void setPId(Integer pId){
		this.pId=pId;
	}
	public Integer getPId(){
		return pId;
	}
	public void setDescription(String description){
		this.description=description;
	}
	public String getDescription(){
		return description;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public Integer getStatus(){
		return status;
	}
	public void setCompany(String company){
		this.company=company;
	}
	public String getCompany(){
		return company;
	}
	public void setCreateTime(Date create_time){
		this.create_time=create_time;
	}
	public Date getCreateTime(){
		return create_time;
	}
	public void setModifyTime(Date modify_time){
		this.modify_time=modify_time;
	}
	public Date getModifyTime(){
		return modify_time;
	}
}

