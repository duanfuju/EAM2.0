package com.tiansu.eam.common.utils.excel.model;

import java.util.List;

public class ExcelConfigModel {

	private String id;
	//excel各字段对应数据库字段的bean，在多数据库表导入时可与各数据库表bean反射赋值
	private String modelClass;
	//excel导入类型，用于标志导入的是哪个excel表
	private String uploadType;
	private String exportFileName;
	private String treeColIndex;//树形节点子节点所在列索引
	
	private List<ExcelFieldModel> fields;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModelClass() {
		return modelClass;
	}

	public void setModelClass(String modelClass) {
		this.modelClass = modelClass;
	}

	public String getUploadType() {
		return uploadType;
	}

	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}

	public String getExportFileName() {
		return exportFileName;
	}

	public void setExportFileName(String exportFileName) {
		this.exportFileName = exportFileName;
	}

	public List<ExcelFieldModel> getFields() {
		return fields;
	}

	public void setFields(List<ExcelFieldModel> fields) {
		this.fields = fields;
	}

	public String getTreeColIndex() {
		return treeColIndex;
	}

	public void setTreeColIndex(String treeColIndex) {
		this.treeColIndex = treeColIndex;
	}
}

