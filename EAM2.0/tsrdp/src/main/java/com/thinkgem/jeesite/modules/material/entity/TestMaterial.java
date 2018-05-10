/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.material.entity;

import com.tiansu.eam.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

/*import com.thinkgem.jeesite.common.persistence.DataEntity;*/

/**
 * 物料信息Entity
 * @author caoh
 * @version 2017-07-19
 */
public class TestMaterial extends DataEntity<TestMaterial> {

	private static final long serialVersionUID = 1L;

	private String materialid;		// 编码
	private String materialcode;		// 编码
	private String materialname;		// 名称
	private Integer materialnumber;		// 数量
	private String materialunit;		// 单位
	private float materialcost;		// 价格
	private String materialstatus;		// 有效状态
	private String materiallevel;		// 物资等级
	private String materialtype;		// 物资类别

	private String materialemp;			// 物资责任人
	private String materialphone;		// 物资责任人电话
	private String materialmail;		// 物资责任人邮箱
	private String materialdate;		//物资入库日期
	private String pstid; 				//流程实例id

	public TestMaterial() {
		super();
	}

	public TestMaterial(String materialid){
		super(materialid);
	}

	public String getPstid() {
		return pstid;
	}

	public void setPstid(String pstid) {
		this.pstid = pstid;
	}

	public String getMaterialid() {
		return materialid;
	}

	public void setMaterialid(String materialid) {
		this.materialid = materialid;
	}

	@Length(min=0, max=255, message="编码长度必须介于 0 和 255 之间")
	public String getMaterialcode() {
		return materialcode;
	}

	public void setMaterialcode(String materialcode) {
		this.materialcode = materialcode;
	}

	@Length(min=0, max=255, message="名称长度必须介于 0 和 255 之间")
	public String getMaterialname() {
		return materialname;
	}

	public void setMaterialname(String materialname) {
		this.materialname = materialname;
	}

	public Integer getMaterialnumber() {
		return materialnumber;
	}

	public void setMaterialnumber(Integer materialnumber) {
		this.materialnumber = materialnumber;
	}

	@Length(min=0, max=255, message="单位长度必须介于 0 和 255 之间")
	public String getMaterialunit() {
		return materialunit;
	}

	public void setMaterialunit(String materialunit) {
		this.materialunit = materialunit;
	}

	public float getMaterialcost() {
		return materialcost;
	}

	public void setMaterialcost(float materialcost) {
		this.materialcost = materialcost;
	}

	@Length(min=0, max=11, message="有效状态长度必须介于 0 和 11 之间")
	public String getMaterialstatus() {
		return materialstatus;
	}

	public void setMaterialstatus(String materialstatus) {
		this.materialstatus = materialstatus;
	}

	@Length(min=0, max=255, message="物资等级长度必须介于 0 和 255 之间")
	public String getMateriallevel() {
		return materiallevel;
	}

	public void setMateriallevel(String materiallevel) {
		this.materiallevel = materiallevel;
	}

	@Length(min=0, max=255, message="物资类别长度必须介于 0 和 255 之间")
	public String getMaterialtype() {
		return materialtype;
	}

	public void setMaterialtype(String materialtype) {
		this.materialtype = materialtype;
	}

	public String getMaterialemp() {
		return materialemp;
	}

	public void setMaterialemp(String materialemp) {
		this.materialemp = materialemp;
	}

	public String getMaterialphone() {
		return materialphone;
	}

	public void setMaterialphone(String materialphone) {
		this.materialphone = materialphone;
	}

	public String getMaterialmail() {
		return materialmail;
	}

	public void setMaterialmail(String materialmail) {
		this.materialmail = materialmail;
	}

	public String getMaterialdate() {
		return materialdate;
	}

	public void setMaterialdate(String materialdate) {
		this.materialdate = materialdate;
	}

}