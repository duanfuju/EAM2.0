package com.tiansu.eam.common.utils.excel.model;

import java.util.Map;

public class ConvertDataModel {

	private String id;
	//转换数据集
	private Map<String,Object> convertDataMap;

	private String initClass;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Object> getConvertDataMap() {
		return convertDataMap;
	}

	public void setConvertDataMap(Map<String, Object> convertDataMap) {
		this.convertDataMap = convertDataMap;
	}

	public String getInitClass() {
		return initClass;
	}

	public void setInitClass(String initClass) {
		this.initClass = initClass;
	}
	
	
	
}
