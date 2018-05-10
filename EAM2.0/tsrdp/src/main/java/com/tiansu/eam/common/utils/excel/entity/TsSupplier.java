package com.tiansu.eam.common.utils.excel.entity;

import com.tiansu.eam.common.persistence.DataEntity;

import java.sql.Timestamp;
import java.util.Date;

   /**
    * ts_supplier 实体类
    * Mon Jul 24 19:49:54 CST 2017 dfj
    */ 


public class TsSupplier extends  DataEntity<TsSupplier> {
 	private String id;
	private String supplier_sp;
	private String supplier_code;
	private String supplier_name;
	private String supplier_shortname;
	private String supplier_type;
	private String supplier_level;
	private String supplier_englishname;
	private String supplier_business_lincense;
	private String supplier_lincense;
	private String supplier_taxrate;
	private String supplier_org;
	private String supplier_bank;
	private String supplier_taxcode;
	private String supplier_president;
	private String supplier_account;
	private String supplier_bank_address;
	private String supplier_regfund;
	private String supplier_linkman;
	private String supplier_fax;
	private String supplier_address;
	private String supplier_phone;
	private String supplier_email;
	private Integer supplier_postcode;
	private Date supplier_business_date;
	private String supplier_credit;
	private String supplier_company;
	private String supplier_enable;
	private String supplier_resource;
	private String supplier_transport;
	private String supplier_remark;
	private Date create_time;
	private Date modify_time;
	private String supplier_xy;
	private Date supplier_business_date_end;
	private String supplier_sp_emp;
	private Timestamp supplier_sp_sptime;
	private String supplier_sp_yj;
	private String supplier_sucCode;
	public void setId(String id){
		this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setSupplierSp(String supplier_sp){
		this.supplier_sp=supplier_sp;
	}
	public String getSupplierSp(){
		return supplier_sp;
	}
	public void setSupplierCode(String supplier_code){
		this.supplier_code=supplier_code;
	}
	public String getSupplierCode(){
		return supplier_code;
	}
	public void setSupplierName(String supplier_name){
		this.supplier_name=supplier_name;
	}
	public String getSupplierName(){
		return supplier_name;
	}
	public void setSupplierShortname(String supplier_shortname){
		this.supplier_shortname=supplier_shortname;
	}
	public String getSupplierShortname(){
		return supplier_shortname;
	}
	public void setSupplierType(String supplier_type){
		this.supplier_type=supplier_type;
	}
	public String getSupplierType(){
		return supplier_type;
	}
	public void setSupplierLevel(String supplier_level){
		this.supplier_level=supplier_level;
	}
	public String getSupplierLevel(){
		return supplier_level;
	}
	public void setSupplierEnglishname(String supplier_englishname){
		this.supplier_englishname=supplier_englishname;
	}
	public String getSupplierEnglishname(){
		return supplier_englishname;
	}
	public void setSupplierBusinessLincense(String supplier_business_lincense){
		this.supplier_business_lincense=supplier_business_lincense;
	}
	public String getSupplierBusinessLincense(){
		return supplier_business_lincense;
	}
	public void setSupplierLincense(String supplier_lincense){
		this.supplier_lincense=supplier_lincense;
	}
	public String getSupplierLincense(){
		return supplier_lincense;
	}
	public void setSupplierTaxrate(String supplier_taxrate){
		this.supplier_taxrate=supplier_taxrate;
	}
	public String getSupplierTaxrate(){
		return supplier_taxrate;
	}
	public void setSupplierOrg(String supplier_org){
		this.supplier_org=supplier_org;
	}
	public String getSupplierOrg(){
		return supplier_org;
	}
	public void setSupplierBank(String supplier_bank){
		this.supplier_bank=supplier_bank;
	}
	public String getSupplierBank(){
		return supplier_bank;
	}
	public void setSupplierTaxcode(String supplier_taxcode){
		this.supplier_taxcode=supplier_taxcode;
	}
	public String getSupplierTaxcode(){
		return supplier_taxcode;
	}
	public void setSupplierPresident(String supplier_president){
		this.supplier_president=supplier_president;
	}
	public String getSupplierPresident(){
		return supplier_president;
	}
	public void setSupplierAccount(String supplier_account){
		this.supplier_account=supplier_account;
	}
	public String getSupplierAccount(){
		return supplier_account;
	}
	public void setSupplierBankAddress(String supplier_bank_address){
		this.supplier_bank_address=supplier_bank_address;
	}
	public String getSupplierBankAddress(){
		return supplier_bank_address;
	}
	public void setSupplierRegfund(String supplier_regfund){
		this.supplier_regfund=supplier_regfund;
	}
	public String getSupplierRegfund(){
		return supplier_regfund;
	}
	public void setSupplierLinkman(String supplier_linkman){
		this.supplier_linkman=supplier_linkman;
	}
	public String getSupplierLinkman(){
		return supplier_linkman;
	}
	public void setSupplierFax(String supplier_fax){
		this.supplier_fax=supplier_fax;
	}
	public String getSupplierFax(){
		return supplier_fax;
	}
	public void setSupplierAddress(String supplier_address){
		this.supplier_address=supplier_address;
	}
	public String getSupplierAddress(){
		return supplier_address;
	}
	public void setSupplierPhone(String supplier_phone){
		this.supplier_phone=supplier_phone;
	}
	public String getSupplierPhone(){
		return supplier_phone;
	}
	public void setSupplierEmail(String supplier_email){
		this.supplier_email=supplier_email;
	}
	public String getSupplierEmail(){
		return supplier_email;
	}
	public void setSupplierPostcode(Integer supplier_postcode){
		this.supplier_postcode=supplier_postcode;
	}
	public Integer getSupplierPostcode(){
		return supplier_postcode;
	}
	public void setSupplierBusinessDate(Date supplier_business_date){
		this.supplier_business_date=supplier_business_date;
	}
	public Date getSupplierBusinessDate(){
		return supplier_business_date;
	}
	public void setSupplierCredit(String supplier_credit){
		this.supplier_credit=supplier_credit;
	}
	public String getSupplierCredit(){
		return supplier_credit;
	}
	public void setSupplierCompany(String supplier_company){
		this.supplier_company=supplier_company;
	}
	public String getSupplierCompany(){
		return supplier_company;
	}
	public void setSupplierEnable(String supplier_enable){
		this.supplier_enable=supplier_enable;
	}
	public String getSupplierEnable(){
		return supplier_enable;
	}
	public void setSupplierResource(String supplier_resource){
		this.supplier_resource=supplier_resource;
	}
	public String getSupplierResource(){
		return supplier_resource;
	}
	public void setSupplierTransport(String supplier_transport){
		this.supplier_transport=supplier_transport;
	}
	public String getSupplierTransport(){
		return supplier_transport;
	}
	public void setSupplierRemark(String supplier_remark){
		this.supplier_remark=supplier_remark;
	}
	public String getSupplierRemark(){
		return supplier_remark;
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
	public void setSupplierXy(String supplier_xy){
		this.supplier_xy=supplier_xy;
	}
	public String getSupplierXy(){
		return supplier_xy;
	}
	public void setSupplierBusinessDateEnd(Date supplier_business_date_end){
		this.supplier_business_date_end=supplier_business_date_end;
	}
	public Date getSupplierBusinessDateEnd(){
		return supplier_business_date_end;
	}
	public void setSupplierSpEmp(String supplier_sp_emp){
		this.supplier_sp_emp=supplier_sp_emp;
	}
	public String getSupplierSpEmp(){
		return supplier_sp_emp;
	}
	public void setSupplierSpSptime(Timestamp supplier_sp_sptime){
		this.supplier_sp_sptime=supplier_sp_sptime;
	}
	public Timestamp getSupplierSpSptime(){
		return supplier_sp_sptime;
	}
	public void setSupplierSpYj(String supplier_sp_yj){
		this.supplier_sp_yj=supplier_sp_yj;
	}
	public String getSupplierSpYj(){
		return supplier_sp_yj;
	}
	public void setSupplierSucCode(String supplier_sucCode){
		this.supplier_sucCode=supplier_sucCode;
	}
	public String getSupplierSucCode(){
		return supplier_sucCode;
	}
}

