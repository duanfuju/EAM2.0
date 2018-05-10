package com.tiansu.eam.common.utils.excel.entity;

import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;

   /**
    * ts_failure_standard 实体类
    * Mon Jul 24 19:49:55 CST 2017 dfj
    */ 


public class TsFailureStandard extends  DataEntity<TsFailureStandard> {
 	private String id;
	private Integer library_id;
	private String failure_phenomena_code;
	private Integer failure_phenomena_priority;
	private String failure_phenomena_description;
	private String failure_cause_code;
	private Integer failure_cause_serverity;
	private String failure_cause_description;
	private String failure_measures_code;
	private String failure_measures_description;
	private Integer failure_status;
	private Date create_time;
	private Date modify_time;
	private Integer oldid;
	public void setId(String id){
		this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setLibraryId(Integer library_id){
		this.library_id=library_id;
	}
	public Integer getLibraryId(){
		return library_id;
	}
	public void setFailurePhenomenaCode(String failure_phenomena_code){
		this.failure_phenomena_code=failure_phenomena_code;
	}
	public String getFailurePhenomenaCode(){
		return failure_phenomena_code;
	}
	public void setFailurePhenomenaPriority(Integer failure_phenomena_priority){
		this.failure_phenomena_priority=failure_phenomena_priority;
	}
	public Integer getFailurePhenomenaPriority(){
		return failure_phenomena_priority;
	}
	public void setFailurePhenomenaDescription(String failure_phenomena_description){
		this.failure_phenomena_description=failure_phenomena_description;
	}
	public String getFailurePhenomenaDescription(){
		return failure_phenomena_description;
	}
	public void setFailureCauseCode(String failure_cause_code){
		this.failure_cause_code=failure_cause_code;
	}
	public String getFailureCauseCode(){
		return failure_cause_code;
	}
	public void setFailureCauseServerity(Integer failure_cause_serverity){
		this.failure_cause_serverity=failure_cause_serverity;
	}
	public Integer getFailureCauseServerity(){
		return failure_cause_serverity;
	}
	public void setFailureCauseDescription(String failure_cause_description){
		this.failure_cause_description=failure_cause_description;
	}
	public String getFailureCauseDescription(){
		return failure_cause_description;
	}
	public void setFailureMeasuresCode(String failure_measures_code){
		this.failure_measures_code=failure_measures_code;
	}
	public String getFailureMeasuresCode(){
		return failure_measures_code;
	}
	public void setFailureMeasuresDescription(String failure_measures_description){
		this.failure_measures_description=failure_measures_description;
	}
	public String getFailureMeasuresDescription(){
		return failure_measures_description;
	}
	public void setFailureStatus(Integer failure_status){
		this.failure_status=failure_status;
	}
	public Integer getFailureStatus(){
		return failure_status;
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
}

