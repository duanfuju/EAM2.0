package com.tiansu.eam.modules.opestandard.entity;

/**
 * @author zhangww
 * @description 运行标准
 * @create 2017-08-31 16:41
 **/
public class StandardOpe {
    private String id;
    private String library_id;
    private String operation_standard_code;
    private String operation_standard_desc;
    private String operation_standard_explain;
    private String operation_standard_remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibrary_id() {
        return library_id;
    }

    public void setLibrary_id(String library_id) {
        this.library_id = library_id;
    }

    public String getOperation_standard_code() {
        return operation_standard_code;
    }

    public void setOperation_standard_code(String operation_standard_code) {
        this.operation_standard_code = operation_standard_code;
    }

    public String getOperation_standard_desc() {
        return operation_standard_desc;
    }

    public void setOperation_standard_desc(String operation_standard_desc) {
        this.operation_standard_desc = operation_standard_desc;
    }

    public String getOperation_standard_explain() {
        return operation_standard_explain;
    }

    public void setOperation_standard_explain(String operation_standard_explain) {
        this.operation_standard_explain = operation_standard_explain;
    }

    public String getOperation_standard_remark() {
        return operation_standard_remark;
    }

    public void setOperation_standard_remark(String operation_standard_remark) {
        this.operation_standard_remark = operation_standard_remark;
    }
}
