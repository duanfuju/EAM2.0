package com.tiansu.eam.common.utils.excel.entity;
import com.tiansu.eam.common.persistence.DataEntity;

   /**
    * ts_operation_device 实体类
    * Mon Jul 24 19:49:55 CST 2017 dfj
    */ 


public class TsOperationDevice extends  DataEntity<TsOperationDevice> {
 	private String id;
	private Integer operation_id;
	private Integer device_id;
	private Integer oldid;
	public void setId(String id){
		this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setOperationId(Integer operation_id){
		this.operation_id=operation_id;
	}
	public Integer getOperationId(){
		return operation_id;
	}
	public void setDeviceId(Integer device_id){
		this.device_id=device_id;
	}
	public Integer getDeviceId(){
		return device_id;
	}
	public void setOldid(Integer oldid){
		this.oldid=oldid;
	}
	public Integer getOldid(){
		return oldid;
	}
}

