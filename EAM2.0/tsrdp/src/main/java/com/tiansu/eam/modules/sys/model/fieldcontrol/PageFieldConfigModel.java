package com.tiansu.eam.modules.sys.model.fieldcontrol;

/**
 * Created by wangjl on 2017/7/28.
 */
 /**
 * * 字段权限配置类，前台页面模型。字段名需和前台传来的json保持一致
  * （前后台model分离方便json结构的数据转换）
 */
public class PageFieldConfigModel {
    //字段名称
    private String fieldName;
    //展示名称
    private String displayName;
    private String showInGrid;
    private int gridWidth;
    //前台lightui控件只能支持string，int，date，不支持boolean，前台编辑后true变成了“true”，此处设置成string。
    private String marginType;
    private String showInSearch;
    private String showInForm;
    private String nullable;
    private String editable;
    //校验类型，目前支持vint,vfloat,vtel,vmail
    private String validateType;

     public String getFieldName() {
         return fieldName;
     }

     public void setFieldName(String fieldName) {
         this.fieldName = fieldName;
     }

     public String getDisplayName() {
         return displayName;
     }

     public void setDisplayName(String displayName) {
         this.displayName = displayName;
     }

     public String isShowInGrid() {
         return showInGrid;
     }

     public void setShowInGrid(String showInGrid) {
         this.showInGrid = String.valueOf(showInGrid);
     }

     public int getGridWidth() {
         return gridWidth;
     }

     public void setGridWidth(int gridWidth) {
         this.gridWidth = gridWidth;
     }

     public String getMarginType() {
         return marginType;
     }

     public void setMarginType(String marginType) {
         this.marginType = marginType;
     }

     public String getShowInGrid() {
         return showInGrid;
     }

     public String getShowInSearch() {
         return showInSearch;
     }

     public void setShowInSearch(String showInSearch) {
         this.showInSearch = showInSearch;
     }

     public String getShowInForm() {
         return showInForm;
     }

     public void setShowInForm(String showInForm) {
         this.showInForm = showInForm;
     }

     public String getNullable() {
         return nullable;
     }

     public void setNullable(String nullable) {
         this.nullable = nullable;
     }

     public String getEditable() {
         return editable;
     }

     public void setEditable(String editable) {
         this.editable = editable;
     }

     public String getValidateType() {
         return validateType;
     }

     public void setValidateType(String validateType) {
         this.validateType = validateType;
     }
 }
