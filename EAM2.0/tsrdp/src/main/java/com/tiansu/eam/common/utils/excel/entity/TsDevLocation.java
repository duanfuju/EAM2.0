package com.tiansu.eam.common.utils.excel.entity;
import com.tiansu.eam.common.persistence.DataEntity;

   /**
    * ts_dev_location 实体类
    * Mon Jul 24 19:49:54 CST 2017 dfj
    */ 


public class TsDevLocation extends  DataEntity<TsDevLocation> {
 	private String id;
	private Integer pid;
	private String location_name;
	private Integer isleaf;
	private String description;
	private String code;
	private String status;
	private String company;
	private String loc_level;
	private String scort;
	private String loc_tel;
	private String loc_dept;
	public void setId(String id){
		this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setPid(Integer pid){
		this.pid=pid;
	}
	public Integer getPid(){
		return pid;
	}
	public void setLocationName(String location_name){
		this.location_name=location_name;
	}
	public String getLocationName(){
		return location_name;
	}
	public void setIsleaf(Integer isleaf){
		this.isleaf=isleaf;
	}
	public Integer getIsleaf(){
		return isleaf;
	}
	public void setDescription(String description){
		this.description=description;
	}
	public String getDescription(){
		return description;
	}
	public void setCode(String code){
		this.code=code;
	}
	public String getCode(){
		return code;
	}
	public void setStatus(String status){
		this.status=status;
	}
	public String getStatus(){
		return status;
	}
	public void setCompany(String company){
		this.company=company;
	}
	public String getCompany(){
		return company;
	}
	public void setLocLevel(String loc_level){
		this.loc_level=loc_level;
	}
	public String getLocLevel(){
		return loc_level;
	}
	public void setScort(String scort){
		this.scort=scort;
	}
	public String getScort(){
		return scort;
	}
	public void setLocTel(String loc_tel){
		this.loc_tel=loc_tel;
	}
	public String getLocTel(){
		return loc_tel;
	}
	public void setLocDept(String loc_dept){
		this.loc_dept=loc_dept;
	}
	public String getLocDept(){
		return loc_dept;
	}
}

