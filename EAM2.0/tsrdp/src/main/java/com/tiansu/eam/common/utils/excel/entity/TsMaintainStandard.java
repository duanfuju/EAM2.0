package com.tiansu.eam.common.utils.excel.entity;
import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;

   /**
    * ts_maintain_standard 实体类
    * Mon Jul 24 19:49:55 CST 2017 dfj
    */ 


public class TsMaintainStandard extends  DataEntity<TsMaintainStandard> {
 	private String id;
	private String maintain_standard_code;
	private Integer library_id;
	private String maintain_standard_description;
	private String maintain_standard_explain;
	private String maintain_standard_remark;
	private Integer maintain_standard_status;
	private Date create_time;
	private Date modify_time;
	private Integer oldid;
	public void setId(String id){
		this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setMaintainStandardCode(String maintain_standard_code){
		this.maintain_standard_code=maintain_standard_code;
	}
	public String getMaintainStandardCode(){
		return maintain_standard_code;
	}
	public void setLibraryId(Integer library_id){
		this.library_id=library_id;
	}
	public Integer getLibraryId(){
		return library_id;
	}
	public void setMaintainStandardDescription(String maintain_standard_description){
		this.maintain_standard_description=maintain_standard_description;
	}
	public String getMaintainStandardDescription(){
		return maintain_standard_description;
	}
	public void setMaintainStandardExplain(String maintain_standard_explain){
		this.maintain_standard_explain=maintain_standard_explain;
	}
	public String getMaintainStandardExplain(){
		return maintain_standard_explain;
	}
	public void setMaintainStandardRemark(String maintain_standard_remark){
		this.maintain_standard_remark=maintain_standard_remark;
	}
	public String getMaintainStandardRemark(){
		return maintain_standard_remark;
	}
	public void setMaintainStandardStatus(Integer maintain_standard_status){
		this.maintain_standard_status=maintain_standard_status;
	}
	public Integer getMaintainStandardStatus(){
		return maintain_standard_status;
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

