package com.tiansu.eam.modules.opestandard.entity;

/**
 * @author zhangww
 * @description 故障检修标准
 * @create 2017-08-31 16:18
 **/
public class StandardFault {
    private String id;
    private String library_id;
    private String fault_standard_code;
    private String fault_standard_desc;
    private String fault_standard_explain;
    private String fault_standard_remark;

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

    public String getFault_standard_code() {
        return fault_standard_code;
    }

    public void setFault_standard_code(String fault_standard_code) {
        this.fault_standard_code = fault_standard_code;
    }

    public String getFault_standard_desc() {
        return fault_standard_desc;
    }

    public void setFault_standard_desc(String fault_standard_desc) {
        this.fault_standard_desc = fault_standard_desc;
    }

    public String getFault_standard_explain() {
        return fault_standard_explain;
    }

    public void setFault_standard_explain(String fault_standard_explain) {
        this.fault_standard_explain = fault_standard_explain;
    }

    public String getFault_standard_remark() {
        return fault_standard_remark;
    }

    public void setFault_standard_remark(String fault_standard_remark) {
        this.fault_standard_remark = fault_standard_remark;
    }
}
