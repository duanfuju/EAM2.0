package com.tiansu.eam.common.utils.excel.entity;
import com.tiansu.eam.common.persistence.DataEntity;

   /**
    * ts_dev_category 实体类
    * Mon Jul 24 19:49:54 CST 2017 dfj
    */ 


public class TsDevCategory extends  DataEntity<TsDevCategory> {
 	private String id;
	private String cat_code;
	private String cat_name;
	private Integer cat_parent;
	private String cat_level;
	private String cat_enable;
	private String cat_description;
	private Integer cat_creater;
	private Integer cat_updater;
	private String cat_company;
	private String scort;
	public void setId(String id){
		this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setCatCode(String cat_code){
		this.cat_code=cat_code;
	}
	public String getCatCode(){
		return cat_code;
	}
	public void setCatName(String cat_name){
		this.cat_name=cat_name;
	}
	public String getCatName(){
		return cat_name;
	}
	public void setCatParent(Integer cat_parent){
		this.cat_parent=cat_parent;
	}
	public Integer getCatParent(){
		return cat_parent;
	}
	public void setCatLevel(String cat_level){
		this.cat_level=cat_level;
	}
	public String getCatLevel(){
		return cat_level;
	}
	public void setCatEnable(String cat_enable){
		this.cat_enable=cat_enable;
	}
	public String getCatEnable(){
		return cat_enable;
	}
	public void setCatDescription(String cat_description){
		this.cat_description=cat_description;
	}
	public String getCatDescription(){
		return cat_description;
	}
	public void setCatCreater(Integer cat_creater){
		this.cat_creater=cat_creater;
	}
	public Integer getCatCreater(){
		return cat_creater;
	}
	public void setCatUpdater(Integer cat_updater){
		this.cat_updater=cat_updater;
	}
	public Integer getCatUpdater(){
		return cat_updater;
	}
	public void setCatCompany(String cat_company){
		this.cat_company=cat_company;
	}
	public String getCatCompany(){
		return cat_company;
	}
	public void setScort(String scort){
		this.scort=scort;
	}
	public String getScort(){
		return scort;
	}
}

