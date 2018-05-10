package com.tiansu.eam.common.utils.excel.entity;

import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;

   /**
    * ts_customer 实体类
    * Mon Jul 24 19:49:54 CST 2017 dfj
    */ 


public class TsCustomer extends  DataEntity<TsCustomer> {
 	private String id;
	private String customer_code;
	private String customer_name;
	private String customer_type;
	private String customer_credit;
	private String customer_enable;
	private String customer_linkman;
	private String customer_phone;
	private String customer_tel;
	private String customer_location;
	private Date customer_begindate;
	private Date customer_enddate;
	private String customer_business_lincense;
	private String customer_taxrate;
	private String customer_org;
	private String customer_bank;
	private String customer_taxcode;
	private String customer_president;
	private String customer_account;
	private String customer_bank_address;
	private String customer_regfund;
	private String customer_fax;
	private String customer_remark;
	private String customer_address;
	public void setId(String id){
		this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setCustomerCode(String customer_code){
		this.customer_code=customer_code;
	}
	public String getCustomerCode(){
		return customer_code;
	}
	public void setCustomerName(String customer_name){
		this.customer_name=customer_name;
	}
	public String getCustomerName(){
		return customer_name;
	}
	public void setCustomerType(String customer_type){
		this.customer_type=customer_type;
	}
	public String getCustomerType(){
		return customer_type;
	}
	public void setCustomerCredit(String customer_credit){
		this.customer_credit=customer_credit;
	}
	public String getCustomerCredit(){
		return customer_credit;
	}
	public void setCustomerEnable(String customer_enable){
		this.customer_enable=customer_enable;
	}
	public String getCustomerEnable(){
		return customer_enable;
	}
	public void setCustomerLinkman(String customer_linkman){
		this.customer_linkman=customer_linkman;
	}
	public String getCustomerLinkman(){
		return customer_linkman;
	}
	public void setCustomerPhone(String customer_phone){
		this.customer_phone=customer_phone;
	}
	public String getCustomerPhone(){
		return customer_phone;
	}
	public void setCustomerTel(String customer_tel){
		this.customer_tel=customer_tel;
	}
	public String getCustomerTel(){
		return customer_tel;
	}
	public void setCustomerLocation(String customer_location){
		this.customer_location=customer_location;
	}
	public String getCustomerLocation(){
		return customer_location;
	}
	public void setCustomerBegindate(Date customer_begindate){
		this.customer_begindate=customer_begindate;
	}
	public Date getCustomerBegindate(){
		return customer_begindate;
	}
	public void setCustomerEnddate(Date customer_enddate){
		this.customer_enddate=customer_enddate;
	}
	public Date getCustomerEnddate(){
		return customer_enddate;
	}
	public void setCustomerBusinessLincense(String customer_business_lincense){
		this.customer_business_lincense=customer_business_lincense;
	}
	public String getCustomerBusinessLincense(){
		return customer_business_lincense;
	}
	public void setCustomerTaxrate(String customer_taxrate){
		this.customer_taxrate=customer_taxrate;
	}
	public String getCustomerTaxrate(){
		return customer_taxrate;
	}
	public void setCustomerOrg(String customer_org){
		this.customer_org=customer_org;
	}
	public String getCustomerOrg(){
		return customer_org;
	}
	public void setCustomerBank(String customer_bank){
		this.customer_bank=customer_bank;
	}
	public String getCustomerBank(){
		return customer_bank;
	}
	public void setCustomerTaxcode(String customer_taxcode){
		this.customer_taxcode=customer_taxcode;
	}
	public String getCustomerTaxcode(){
		return customer_taxcode;
	}
	public void setCustomerPresident(String customer_president){
		this.customer_president=customer_president;
	}
	public String getCustomerPresident(){
		return customer_president;
	}
	public void setCustomerAccount(String customer_account){
		this.customer_account=customer_account;
	}
	public String getCustomerAccount(){
		return customer_account;
	}
	public void setCustomerBankAddress(String customer_bank_address){
		this.customer_bank_address=customer_bank_address;
	}
	public String getCustomerBankAddress(){
		return customer_bank_address;
	}
	public void setCustomerRegfund(String customer_regfund){
		this.customer_regfund=customer_regfund;
	}
	public String getCustomerRegfund(){
		return customer_regfund;
	}
	public void setCustomerFax(String customer_fax){
		this.customer_fax=customer_fax;
	}
	public String getCustomerFax(){
		return customer_fax;
	}
	public void setCustomerRemark(String customer_remark){
		this.customer_remark=customer_remark;
	}
	public String getCustomerRemark(){
		return customer_remark;
	}
	public void setCustomerAddress(String customer_address){
		this.customer_address=customer_address;
	}
	public String getCustomerAddress(){
		return customer_address;
	}
}

