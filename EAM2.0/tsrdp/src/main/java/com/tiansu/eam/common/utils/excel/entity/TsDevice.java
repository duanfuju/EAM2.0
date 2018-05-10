package com.tiansu.eam.common.utils.excel.entity;
import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;

   /**
    * ts_device 实体类
    * Mon Jul 24 19:49:54 CST 2017 dfj
    */ 


public class TsDevice extends  DataEntity<TsDevice> {
 	private String id;
	private String dev_code;
	private String dev_name;
	private Integer dev_location;
	private String dev_level;
	private Date dev_expire_date;
	private String dev_enable;
	private String dev_brand;
	private String dev_model;
	private String dev_serial_number;
	private Integer dev_supplier;
	private Integer dev_maintenancer;
	private Date dev_startdate;
	private Date dev_prodate;
	private String dev_lifespan;
	private String dev_area;
	private String dev_bimid;
	private Integer dev_category;
	private String dev_param_name;
	private String dev_param_val;
	private String dev_org;
	private String dev_org_group;
	private String dev_station;
	private String dev_employee;
	private String dev_qrcode_status;
	private String dev_status;
	private String dev_company;
	private Date dev_buy_time;
	private Date dev_free_time;
	private Date dev_pay_time;
	private Date dev_regect_time;
	private String dev_source;
	private String dev_pic;
	private String dev_major;
	public void setId(String id){
		this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setDevCode(String dev_code){
		this.dev_code=dev_code;
	}
	public String getDevCode(){
		return dev_code;
	}
	public void setDevName(String dev_name){
		this.dev_name=dev_name;
	}
	public String getDevName(){
		return dev_name;
	}
	public void setDevLocation(Integer dev_location){
		this.dev_location=dev_location;
	}
	public Integer getDevLocation(){
		return dev_location;
	}
	public void setDevLevel(String dev_level){
		this.dev_level=dev_level;
	}
	public String getDevLevel(){
		return dev_level;
	}
	public void setDevExpireDate(Date dev_expire_date){
		this.dev_expire_date=dev_expire_date;
	}
	public Date getDevExpireDate(){
		return dev_expire_date;
	}
	public void setDevEnable(String dev_enable){
		this.dev_enable=dev_enable;
	}
	public String getDevEnable(){
		return dev_enable;
	}
	public void setDevBrand(String dev_brand){
		this.dev_brand=dev_brand;
	}
	public String getDevBrand(){
		return dev_brand;
	}
	public void setDevModel(String dev_model){
		this.dev_model=dev_model;
	}
	public String getDevModel(){
		return dev_model;
	}
	public void setDevSerialNumber(String dev_serial_number){
		this.dev_serial_number=dev_serial_number;
	}
	public String getDevSerialNumber(){
		return dev_serial_number;
	}
	public void setDevSupplier(Integer dev_supplier){
		this.dev_supplier=dev_supplier;
	}
	public Integer getDevSupplier(){
		return dev_supplier;
	}
	public void setDevMaintenancer(Integer dev_maintenancer){
		this.dev_maintenancer=dev_maintenancer;
	}
	public Integer getDevMaintenancer(){
		return dev_maintenancer;
	}
	public void setDevStartdate(Date dev_startdate){
		this.dev_startdate=dev_startdate;
	}
	public Date getDevStartdate(){
		return dev_startdate;
	}
	public void setDevProdate(Date dev_prodate){
		this.dev_prodate=dev_prodate;
	}
	public Date getDevProdate(){
		return dev_prodate;
	}
	public void setDevLifespan(String dev_lifespan){
		this.dev_lifespan=dev_lifespan;
	}
	public String getDevLifespan(){
		return dev_lifespan;
	}
	public void setDevArea(String dev_area){
		this.dev_area=dev_area;
	}
	public String getDevArea(){
		return dev_area;
	}
	public void setDevBimid(String dev_bimid){
		this.dev_bimid=dev_bimid;
	}
	public String getDevBimid(){
		return dev_bimid;
	}
	public void setDevCategory(Integer dev_category){
		this.dev_category=dev_category;
	}
	public Integer getDevCategory(){
		return dev_category;
	}
	public void setDevParamName(String dev_param_name){
		this.dev_param_name=dev_param_name;
	}
	public String getDevParamName(){
		return dev_param_name;
	}
	public void setDevParamVal(String dev_param_val){
		this.dev_param_val=dev_param_val;
	}
	public String getDevParamVal(){
		return dev_param_val;
	}
	public void setDevOrg(String dev_org){
		this.dev_org=dev_org;
	}
	public String getDevOrg(){
		return dev_org;
	}
	public void setDevOrgGroup(String dev_org_group){
		this.dev_org_group=dev_org_group;
	}
	public String getDevOrgGroup(){
		return dev_org_group;
	}
	public void setDevStation(String dev_station){
		this.dev_station=dev_station;
	}
	public String getDevStation(){
		return dev_station;
	}
	public void setDevEmployee(String dev_employee){
		this.dev_employee=dev_employee;
	}
	public String getDevEmployee(){
		return dev_employee;
	}
	public void setDevQrcodeStatus(String dev_qrcode_status){
		this.dev_qrcode_status=dev_qrcode_status;
	}
	public String getDevQrcodeStatus(){
		return dev_qrcode_status;
	}
	public void setDevStatus(String dev_status){
		this.dev_status=dev_status;
	}
	public String getDevStatus(){
		return dev_status;
	}
	public void setDevCompany(String dev_company){
		this.dev_company=dev_company;
	}
	public String getDevCompany(){
		return dev_company;
	}
	public void setDevBuyTime(Date dev_buy_time){
		this.dev_buy_time=dev_buy_time;
	}
	public Date getDevBuyTime(){
		return dev_buy_time;
	}
	public void setDevFreeTime(Date dev_free_time){
		this.dev_free_time=dev_free_time;
	}
	public Date getDevFreeTime(){
		return dev_free_time;
	}
	public void setDevPayTime(Date dev_pay_time){
		this.dev_pay_time=dev_pay_time;
	}
	public Date getDevPayTime(){
		return dev_pay_time;
	}
	public void setDevRegectTime(Date dev_regect_time){
		this.dev_regect_time=dev_regect_time;
	}
	public Date getDevRegectTime(){
		return dev_regect_time;
	}
	public void setDevSource(String dev_source){
		this.dev_source=dev_source;
	}
	public String getDevSource(){
		return dev_source;
	}
	public void setDevPic(String dev_pic){
		this.dev_pic=dev_pic;
	}
	public String getDevPic(){
		return dev_pic;
	}
	public void setDevMajor(String dev_major){
		this.dev_major=dev_major;
	}
	public String getDevMajor(){
		return dev_major;
	}
}

