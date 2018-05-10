package com.tiansu.eam.common.utils.excel.entity;
import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;

   /**
    * ts_safety_standard 实体类
    * Mon Jul 24 19:49:55 CST 2017 dfj
    */ 


public class TsSafetyStandard extends  DataEntity<TsSafetyStandard> {
 	private String id;
	private String safety_standard_code;
	private Integer library_id;
	private String safety_standard_description;
	private String safety_standard_explain;
	private String safety_standard_remark;
	private Integer safety_standard_status;
	private Date create_time;
	private Date modify_time;
	private Integer oldid;
	public void setId(String id){
		this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setSafetyStandardCode(String safety_standard_code){
		this.safety_standard_code=safety_standard_code;
	}
	public String getSafetyStandardCode(){
		return safety_standard_code;
	}
	public void setLibraryId(Integer library_id){
		this.library_id=library_id;
	}
	public Integer getLibraryId(){
		return library_id;
	}
	public void setSafetyStandardDescription(String safety_standard_description){
		this.safety_standard_description=safety_standard_description;
	}
	public String getSafetyStandardDescription(){
		return safety_standard_description;
	}
	public void setSafetyStandardExplain(String safety_standard_explain){
		this.safety_standard_explain=safety_standard_explain;
	}
	public String getSafetyStandardExplain(){
		return safety_standard_explain;
	}
	public void setSafetyStandardRemark(String safety_standard_remark){
		this.safety_standard_remark=safety_standard_remark;
	}
	public String getSafetyStandardRemark(){
		return safety_standard_remark;
	}
	public void setSafetyStandardStatus(Integer safety_standard_status){
		this.safety_standard_status=safety_standard_status;
	}
	public Integer getSafetyStandardStatus(){
		return safety_standard_status;
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

