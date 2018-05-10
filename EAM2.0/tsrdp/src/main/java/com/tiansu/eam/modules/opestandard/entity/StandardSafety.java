package com.tiansu.eam.modules.opestandard.entity;

/**
 * @author zhangdf
 * @description
 * @create 2017-08-31 16:43
 **/
public class StandardSafety {
    private String id;
    private String library_id;
    private String safety_standard_code;
    private String safety_standard_desc;
    private String safety_standard_explain;
    private String safety_standard_remark;

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

    public String getSafety_standard_code() {
        return safety_standard_code;
    }

    public void setSafety_standard_code(String safety_standard_code) {
        this.safety_standard_code = safety_standard_code;
    }

    public String getSafety_standard_desc() {
        return safety_standard_desc;
    }

    public void setSafety_standard_desc(String safety_standard_desc) {
        this.safety_standard_desc = safety_standard_desc;
    }

    public String getSafety_standard_explain() {
        return safety_standard_explain;
    }

    public void setSafety_standard_explain(String safety_standard_explain) {
        this.safety_standard_explain = safety_standard_explain;
    }

    public String getSafety_standard_remark() {
        return safety_standard_remark;
    }

    public void setSafety_standard_remark(String safety_standard_remark) {
        this.safety_standard_remark = safety_standard_remark;
    }
}
