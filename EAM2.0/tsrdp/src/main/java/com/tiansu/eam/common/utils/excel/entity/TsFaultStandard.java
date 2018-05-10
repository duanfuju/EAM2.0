package com.tiansu.eam.common.utils.excel.entity;
import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;

   /**
    * ts_fault_standard 实体类
    * Mon Jul 24 19:49:55 CST 2017 dfj
    */ 


public class TsFaultStandard extends  DataEntity<TsFaultStandard> {
 	private String id;
	private String fault_standard_code;
	private Integer library_id;
	private String fault_standard_description;
	private String fault_standard_explain;
	private String fault_standard_remark;
	private Integer fault_standard_status;
	private Date create_time;
	private Date modify_time;
	private Integer oldid;
	public void setId(String id){
		this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setFaultStandardCode(String fault_standard_code){
		this.fault_standard_code=fault_standard_code;
	}
	public String getFaultStandardCode(){
		return fault_standard_code;
	}
	public void setLibraryId(Integer library_id){
		this.library_id=library_id;
	}
	public Integer getLibraryId(){
		return library_id;
	}
	public void setFaultStandardDescription(String fault_standard_description){
		this.fault_standard_description=fault_standard_description;
	}
	public String getFaultStandardDescription(){
		return fault_standard_description;
	}
	public void setFaultStandardExplain(String fault_standard_explain){
		this.fault_standard_explain=fault_standard_explain;
	}
	public String getFaultStandardExplain(){
		return fault_standard_explain;
	}
	public void setFaultStandardRemark(String fault_standard_remark){
		this.fault_standard_remark=fault_standard_remark;
	}
	public String getFaultStandardRemark(){
		return fault_standard_remark;
	}
	public void setFaultStandardStatus(Integer fault_standard_status){
		this.fault_standard_status=fault_standard_status;
	}
	public Integer getFaultStandardStatus(){
		return fault_standard_status;
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

