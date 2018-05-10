/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.modules.material.entity;

import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;


/**
 * 物料信息 Entity
 * @author caoh
 * @version 2017-08-21
 */
public class MaterialInfo extends DataEntity<MaterialInfo> {

	private static final long serialVersionUID = 1L;

	private String id_key;					//主键
	private String material_code;		// 物料编码

	private String material_name;		// 物料名称

	private String 	material_desc;			// 描述
	private String 	material_type;			// 物料类别

	private String 	material_unit;	// 计量单位
	private int 	material_qty;		// 库存数量
	private String 	material_purchasing;		// 采购方式
	private String 	material_cost;		// 标准成本
	private String 	material_price;		// 销售价格
	private String 	material_brand;		// 品牌
	private String 	material_supplier;		// 供应商
	private String 	material_remark;			// 备注
	private String 	material_status;			//状态
	private String  material_level;			//重要程度
	private String material_model;			//规格型号
	private String 	create_by;		// 创建人
	private Date 	create_time;		// 创建时间
	private String 	update_by;		// 更新人
	private Date update_time;		// 更新日期
	private char 	isdelete;		// 删除标志

	public MaterialInfo() {
		super();
	}

	public MaterialInfo(String id){
		super(id);
	}

	public String getId_key() {
		return id_key;
	}

	public void setId_key(String id_key) {
		this.id_key = id_key;
	}

	public String getMaterial_code() {
		return material_code;
	}

	public void setMaterial_code(String material_code) {
		this.material_code = material_code;
	}

	public String getMaterial_name() {
		return material_name;
	}

	public void setMaterial_name(String material_name) {
		this.material_name = material_name;
	}

	public String getMaterial_desc() {
		return material_desc;
	}

	public void setMaterial_desc(String material_desc) {
		this.material_desc = material_desc;
	}

	public String getMaterial_type() {
		return material_type;
	}

	public void setMaterial_type(String material_type) {
		this.material_type = material_type;
	}

	public String getMaterial_unit() {
		return material_unit;
	}

	public void setMaterial_unit(String material_unit) {
		this.material_unit = material_unit;
	}

	public int getMaterial_qty() {
		return material_qty;
	}

	public void setMaterial_qty(int material_qty) {
		this.material_qty = material_qty;
	}

	public String getMaterial_purchasing() {
		return material_purchasing;
	}

	public void setMaterial_purchasing(String material_purchasing) {
		this.material_purchasing = material_purchasing;
	}


	public String getMaterial_brand() {
		return material_brand;
	}

	public void setMaterial_brand(String material_brand) {
		this.material_brand = material_brand;
	}

	public String getMaterial_supplier() {
		return material_supplier;
	}

	public void setMaterial_supplier(String material_supplier) {
		this.material_supplier = material_supplier;
	}

	public String getMaterial_remark() {
		return material_remark;
	}

	public void setMaterial_remark(String material_remark) {
		this.material_remark = material_remark;
	}



	public String getMaterial_level() {
		return material_level;
	}

	public void setMaterial_level(String material_level) {
		this.material_level = material_level;
	}

	public String getMaterial_model() {
		return material_model;
	}

	public void setMaterial_model(String material_model) {
		this.material_model = material_model;
	}

	public String getCreate_by() {
		return create_by;
	}

	public void setCreate_by(String create_by) {
		this.create_by = create_by;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getUpdate_by() {
		return update_by;
	}

	public void setUpdate_by(String update_by) {
		this.update_by = update_by;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public char getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(char isdelete) {
		this.isdelete = isdelete;
	}

	public String getMaterial_cost() {
		return material_cost;
	}

	public void setMaterial_cost(String material_cost) {
		this.material_cost = material_cost;
	}

	public String getMaterial_price() {
		return material_price;
	}

	public void setMaterial_price(String material_price) {
		this.material_price = material_price;
	}

	public String getMaterial_status() {
		return material_status;
	}

	public void setMaterial_status(String material_status) {
		this.material_status = material_status;
	}
}