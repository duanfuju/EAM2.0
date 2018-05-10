package com.tiansu.eam.common.utils.excel.entity;

import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;

   /**
    * ts_operation_library 实体类
    * Mon Jul 24 19:49:54 CST 2017 dfj
    */ 


public class TsOperationLibrary extends  DataEntity<TsOperationLibrary> {
 	private String id;
	private String library_code;
	private Integer approval_status;
	private String company;
	private Date create_time;
	private Date modify_time;
	private Integer oldid;
	private Double empid;
	private String fujpath;
	public void setId(String id){
		this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setLibraryCode(String library_code){
		this.library_code=library_code;
	}
	public String getLibraryCode(){
		return library_code;
	}
	public void setApprovalStatus(Integer approval_status){
		this.approval_status=approval_status;
	}
	public Integer getApprovalStatus(){
		return approval_status;
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
	public void setOldid(Integer oldid){
		this.oldid=oldid;
	}
	public Integer getOldid(){
		return oldid;
	}
	public void setEmpid(Double empid){
		this.empid=empid;
	}
	public Double getEmpid(){
		return empid;
	}
	public void setFujpath(String fujpath){
		this.fujpath=fujpath;
	}
	public String getFujpath(){
		return fujpath;
	}
}

