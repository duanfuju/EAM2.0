package com.tiansu.eam.common.utils.excel.entity;

import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;

   /**
    * ts_material 实体类
    * Mon Jul 24 19:49:54 CST 2017 dfj
    */ 


public class TsMaterial extends  DataEntity<TsMaterial> {
 	private String id;
	private String material_code;
	private String material_name;
	private Integer material_type;
	private String material_unit;
	private Integer material_number;
	private Integer material_purchase_methods;
	private float material_cost;
	private float material_price;
	private String material_brand;
	private Integer material_supplier;
	private String material_remark;
	private Integer material_status;
	private Integer material_level;
	private Date create_time;
	private Date modify_time;
	private String material_model;
	public void setId(String id){
		this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setMaterialCode(String material_code){
		this.material_code=material_code;
	}
	public String getMaterialCode(){
		return material_code;
	}
	public void setMaterialName(String material_name){
		this.material_name=material_name;
	}
	public String getMaterialName(){
		return material_name;
	}
	public void setMaterialType(Integer material_type){
		this.material_type=material_type;
	}
	public Integer getMaterialType(){
		return material_type;
	}
	public void setMaterialUnit(String material_unit){
		this.material_unit=material_unit;
	}
	public String getMaterialUnit(){
		return material_unit;
	}
	public void setMaterialNumber(Integer material_number){
		this.material_number=material_number;
	}
	public Integer getMaterialNumber(){
		return material_number;
	}
	public void setMaterialPurchaseMethods(Integer material_purchase_methods){
		this.material_purchase_methods=material_purchase_methods;
	}
	public Integer getMaterialPurchaseMethods(){
		return material_purchase_methods;
	}
	public void setMaterialCost(float material_cost){
		this.material_cost=material_cost;
	}
	public float getMaterialCost(){
		return material_cost;
	}
	public void setMaterialPrice(float material_price){
		this.material_price=material_price;
	}
	public float getMaterialPrice(){
		return material_price;
	}
	public void setMaterialBrand(String material_brand){
		this.material_brand=material_brand;
	}
	public String getMaterialBrand(){
		return material_brand;
	}
	public void setMaterialSupplier(Integer material_supplier){
		this.material_supplier=material_supplier;
	}
	public Integer getMaterialSupplier(){
		return material_supplier;
	}
	public void setMaterialRemark(String material_remark){
		this.material_remark=material_remark;
	}
	public String getMaterialRemark(){
		return material_remark;
	}
	public void setMaterialStatus(Integer material_status){
		this.material_status=material_status;
	}
	public Integer getMaterialStatus(){
		return material_status;
	}
	public void setMaterialLevel(Integer material_level){
		this.material_level=material_level;
	}
	public Integer getMaterialLevel(){
		return material_level;
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
	public void setMaterialModel(String material_model){
		this.material_model=material_model;
	}
	public String getMaterialModel(){
		return material_model;
	}
}

