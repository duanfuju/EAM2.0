package com.tiansu.eam.common.utils.excel.entity;
import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;

   /**
    * ts_operation_standard 实体类
    * Mon Jul 24 19:49:55 CST 2017 dfj
    */ 


public class TsOperationStandard extends  DataEntity<TsOperationStandard> {
 	private String id;
	private String operation_standard_code;
	private Integer library_id;
	private String operation_standard_description;
	private String operation_standard_explain;
	private String operation_standard_remark;
	private Integer operation_standard_status;
	private Date crate_time;
	private Date modify_time;
	private Integer oldid;
	public void setId(String id){
		this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setOperationStandardCode(String operation_standard_code){
		this.operation_standard_code=operation_standard_code;
	}
	public String getOperationStandardCode(){
		return operation_standard_code;
	}
	public void setLibraryId(Integer library_id){
		this.library_id=library_id;
	}
	public Integer getLibraryId(){
		return library_id;
	}
	public void setOperationStandardDescription(String operation_standard_description){
		this.operation_standard_description=operation_standard_description;
	}
	public String getOperationStandardDescription(){
		return operation_standard_description;
	}
	public void setOperationStandardExplain(String operation_standard_explain){
		this.operation_standard_explain=operation_standard_explain;
	}
	public String getOperationStandardExplain(){
		return operation_standard_explain;
	}
	public void setOperationStandardRemark(String operation_standard_remark){
		this.operation_standard_remark=operation_standard_remark;
	}
	public String getOperationStandardRemark(){
		return operation_standard_remark;
	}
	public void setOperationStandardStatus(Integer operation_standard_status){
		this.operation_standard_status=operation_standard_status;
	}
	public Integer getOperationStandardStatus(){
		return operation_standard_status;
	}
	public void setCrateTime(Date crate_time){
		this.crate_time=crate_time;
	}
	public Date getCrateTime(){
		return crate_time;
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
}

