package com.tiansu.eam.modules.opestandard.entity;

/**
 * @author zhangdf
 * @description
 * @create 2017-08-31 16:33
 **/
public class StandardPatrol {
    private String id;
    private String library_id;
    private String patrol_standard_code;
    private String patrol_standard_desc;
    private String patrol_standard_explain;
    private String patrol_standard_remark;

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

    public String getPatrol_standard_code() {
        return patrol_standard_code;
    }

    public void setPatrol_standard_code(String patrol_standard_code) {
        this.patrol_standard_code = patrol_standard_code;
    }

    public String getPatrol_standard_desc() {
        return patrol_standard_desc;
    }

    public void setPatrol_standard_desc(String patrol_standard_desc) {
        this.patrol_standard_desc = patrol_standard_desc;
    }

    public String getPatrol_standard_explain() {
        return patrol_standard_explain;
    }

    public void setPatrol_standard_explain(String patrol_standard_explain) {
        this.patrol_standard_explain = patrol_standard_explain;
    }

    public String getPatrol_standard_remark() {
        return patrol_standard_remark;
    }

    public void setPatrol_standard_remark(String patrol_standard_remark) {
        this.patrol_standard_remark = patrol_standard_remark;
    }
}
