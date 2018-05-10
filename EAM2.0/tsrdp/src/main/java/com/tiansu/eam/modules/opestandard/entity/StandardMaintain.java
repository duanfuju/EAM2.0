package com.tiansu.eam.modules.opestandard.entity;

/**
 * @author zhangww
 * @description 保养标准
 * @create 2017-08-31 16:26
 **/
public class StandardMaintain {
    private String id;
    private String library_id;
    private String maintain_standard_code;
    private String maintain_standard_desc;
    private String maintain_standard_explain;
    private String maintain_standard_remark;

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

    public String getMaintain_standard_code() {
        return maintain_standard_code;
    }

    public void setMaintain_standard_code(String maintain_standard_code) {
        this.maintain_standard_code = maintain_standard_code;
    }

    public String getMaintain_standard_desc() {
        return maintain_standard_desc;
    }

    public void setMaintain_standard_desc(String maintain_standard_desc) {
        this.maintain_standard_desc = maintain_standard_desc;
    }

    public String getMaintain_standard_explain() {
        return maintain_standard_explain;
    }

    public void setMaintain_standard_explain(String maintain_standard_explain) {
        this.maintain_standard_explain = maintain_standard_explain;
    }

    public String getMaintain_standard_remark() {
        return maintain_standard_remark;
    }

    public void setMaintain_standard_remark(String maintain_standard_remark) {
        this.maintain_standard_remark = maintain_standard_remark;
    }
}
