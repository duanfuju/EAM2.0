package com.tiansu.eam.common.utils.excel.entity;
import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;

   /**
    * org_employee 实体类
    * Mon Jul 24 19:49:55 CST 2017 dfj
    */ 


public class OrgEmployee extends  DataEntity<OrgEmployee> {
 	private Double EMPID;
	private String EMPCODE;
	private Double OPERATORID;
	private String USERID;
	private String EMPNAME;
	private String REALNAME;
	private String GENDER;
	private Date BIRTHDATE;
	private Double POSITION;
	private String EMPSTATUS;
	private String CARDTYPE;
	private String CARDNO;
	private Date INDATE;
	private Date OUTDATE;
	private String OTEL;
	private String OADDRESS;
	private String OZIPCODE;
	private String OEMAIL;
	private String FAXNO;
	private String MOBILENO;
	private String QQ;
	private String HTEL;
	private String HADDRESS;
	private String HZIPCODE;
	private String PEMAIL;
	private String PARTY;
	private String DEGREE;
	private Integer SORTNO;
	private Double MAJOR;
	private String SPECIALTY;
	private String WORKEXP;
	private Date REGDATE;
	private Date CREATETIME;
	private Date LASTMODYTIME;
	private String ORGIDLIST;
	private Double ORGID;
	private String REMARK;
	private String TENANT_ID;
	private String APP_ID;
	private String WEIBO;
	public void setEMPID(Double EMPID){
		this.EMPID=EMPID;
	}
	public Double getEMPID(){
		return EMPID;
	}
	public void setEMPCODE(String EMPCODE){
		this.EMPCODE=EMPCODE;
	}
	public String getEMPCODE(){
		return EMPCODE;
	}
	public void setOPERATORID(Double OPERATORID){
		this.OPERATORID=OPERATORID;
	}
	public Double getOPERATORID(){
		return OPERATORID;
	}
	public void setUSERID(String USERID){
		this.USERID=USERID;
	}
	public String getUSERID(){
		return USERID;
	}
	public void setEMPNAME(String EMPNAME){
		this.EMPNAME=EMPNAME;
	}
	public String getEMPNAME(){
		return EMPNAME;
	}
	public void setREALNAME(String REALNAME){
		this.REALNAME=REALNAME;
	}
	public String getREALNAME(){
		return REALNAME;
	}
	public void setGENDER(String GENDER){
		this.GENDER=GENDER;
	}
	public String getGENDER(){
		return GENDER;
	}
	public void setBIRTHDATE(Date BIRTHDATE){
		this.BIRTHDATE=BIRTHDATE;
	}
	public Date getBIRTHDATE(){
		return BIRTHDATE;
	}
	public void setPOSITION(Double POSITION){
		this.POSITION=POSITION;
	}
	public Double getPOSITION(){
		return POSITION;
	}
	public void setEMPSTATUS(String EMPSTATUS){
		this.EMPSTATUS=EMPSTATUS;
	}
	public String getEMPSTATUS(){
		return EMPSTATUS;
	}
	public void setCARDTYPE(String CARDTYPE){
		this.CARDTYPE=CARDTYPE;
	}
	public String getCARDTYPE(){
		return CARDTYPE;
	}
	public void setCARDNO(String CARDNO){
		this.CARDNO=CARDNO;
	}
	public String getCARDNO(){
		return CARDNO;
	}
	public void setINDATE(Date INDATE){
		this.INDATE=INDATE;
	}
	public Date getINDATE(){
		return INDATE;
	}
	public void setOUTDATE(Date OUTDATE){
		this.OUTDATE=OUTDATE;
	}
	public Date getOUTDATE(){
		return OUTDATE;
	}
	public void setOTEL(String OTEL){
		this.OTEL=OTEL;
	}
	public String getOTEL(){
		return OTEL;
	}
	public void setOADDRESS(String OADDRESS){
		this.OADDRESS=OADDRESS;
	}
	public String getOADDRESS(){
		return OADDRESS;
	}
	public void setOZIPCODE(String OZIPCODE){
		this.OZIPCODE=OZIPCODE;
	}
	public String getOZIPCODE(){
		return OZIPCODE;
	}
	public void setOEMAIL(String OEMAIL){
		this.OEMAIL=OEMAIL;
	}
	public String getOEMAIL(){
		return OEMAIL;
	}
	public void setFAXNO(String FAXNO){
		this.FAXNO=FAXNO;
	}
	public String getFAXNO(){
		return FAXNO;
	}
	public void setMOBILENO(String MOBILENO){
		this.MOBILENO=MOBILENO;
	}
	public String getMOBILENO(){
		return MOBILENO;
	}
	public void setQQ(String QQ){
		this.QQ=QQ;
	}
	public String getQQ(){
		return QQ;
	}
	public void setHTEL(String HTEL){
		this.HTEL=HTEL;
	}
	public String getHTEL(){
		return HTEL;
	}
	public void setHADDRESS(String HADDRESS){
		this.HADDRESS=HADDRESS;
	}
	public String getHADDRESS(){
		return HADDRESS;
	}
	public void setHZIPCODE(String HZIPCODE){
		this.HZIPCODE=HZIPCODE;
	}
	public String getHZIPCODE(){
		return HZIPCODE;
	}
	public void setPEMAIL(String PEMAIL){
		this.PEMAIL=PEMAIL;
	}
	public String getPEMAIL(){
		return PEMAIL;
	}
	public void setPARTY(String PARTY){
		this.PARTY=PARTY;
	}
	public String getPARTY(){
		return PARTY;
	}
	public void setDEGREE(String DEGREE){
		this.DEGREE=DEGREE;
	}
	public String getDEGREE(){
		return DEGREE;
	}
	public void setSORTNO(Integer SORTNO){
		this.SORTNO=SORTNO;
	}
	public Integer getSORTNO(){
		return SORTNO;
	}
	public void setMAJOR(Double MAJOR){
		this.MAJOR=MAJOR;
	}
	public Double getMAJOR(){
		return MAJOR;
	}
	public void setSPECIALTY(String SPECIALTY){
		this.SPECIALTY=SPECIALTY;
	}
	public String getSPECIALTY(){
		return SPECIALTY;
	}
	public void setWORKEXP(String WORKEXP){
		this.WORKEXP=WORKEXP;
	}
	public String getWORKEXP(){
		return WORKEXP;
	}
	public void setREGDATE(Date REGDATE){
		this.REGDATE=REGDATE;
	}
	public Date getREGDATE(){
		return REGDATE;
	}
	public void setCREATETIME(Date CREATETIME){
		this.CREATETIME=CREATETIME;
	}
	public Date getCREATETIME(){
		return CREATETIME;
	}
	public void setLASTMODYTIME(Date LASTMODYTIME){
		this.LASTMODYTIME=LASTMODYTIME;
	}
	public Date getLASTMODYTIME(){
		return LASTMODYTIME;
	}
	public void setORGIDLIST(String ORGIDLIST){
		this.ORGIDLIST=ORGIDLIST;
	}
	public String getORGIDLIST(){
		return ORGIDLIST;
	}
	public void setORGID(Double ORGID){
		this.ORGID=ORGID;
	}
	public Double getORGID(){
		return ORGID;
	}
	public void setREMARK(String REMARK){
		this.REMARK=REMARK;
	}
	public String getREMARK(){
		return REMARK;
	}
	public void setTENANTID(String TENANT_ID){
		this.TENANT_ID=TENANT_ID;
	}
	public String getTENANTID(){
		return TENANT_ID;
	}
	public void setAPPID(String APP_ID){
		this.APP_ID=APP_ID;
	}
	public String getAPPID(){
		return APP_ID;
	}
	public void setWEIBO(String WEIBO){
		this.WEIBO=WEIBO;
	}
	public String getWEIBO(){
		return WEIBO;
	}
}

