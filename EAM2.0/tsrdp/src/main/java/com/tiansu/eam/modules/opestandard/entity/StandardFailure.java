package com.tiansu.eam.modules.opestandard.entity;

/**
 * @author zhangww
 * @description 缺陷故障库
 * @create 2017-08-31 16:35
 **/
public class StandardFailure {
    private String id;
    private String library_id;
    private String failure_phenomena_code;
    private String failure_phenomena_priority;
    private String failure_phenomena_desc;
    private String failure_cause_code;
    private String failure_cause_serverity;
    private String failure_cause_desc;
    private String failure_measures_code;
    private String failure_measures_desc;

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

    public String getFailure_phenomena_code() {
        return failure_phenomena_code;
    }

    public void setFailure_phenomena_code(String failure_phenomena_code) {
        this.failure_phenomena_code = failure_phenomena_code;
    }

    public String getFailure_phenomena_priority() {
        return failure_phenomena_priority;
    }

    public void setFailure_phenomena_priority(String failure_phenomena_priority) {
        this.failure_phenomena_priority = failure_phenomena_priority;
    }

    public String getFailure_phenomena_desc() {
        return failure_phenomena_desc;
    }

    public void setFailure_phenomena_desc(String failure_phenomena_desc) {
        this.failure_phenomena_desc = failure_phenomena_desc;
    }

    public String getFailure_cause_code() {
        return failure_cause_code;
    }

    public void setFailure_cause_code(String failure_cause_code) {
        this.failure_cause_code = failure_cause_code;
    }

    public String getFailure_cause_serverity() {
        return failure_cause_serverity;
    }

    public void setFailure_cause_serverity(String failure_cause_serverity) {
        this.failure_cause_serverity = failure_cause_serverity;
    }

    public String getFailure_cause_desc() {
        return failure_cause_desc;
    }

    public void setFailure_cause_desc(String failure_cause_desc) {
        this.failure_cause_desc = failure_cause_desc;
    }

    public String getFailure_measures_code() {
        return failure_measures_code;
    }

    public void setFailure_measures_code(String failure_measures_code) {
        this.failure_measures_code = failure_measures_code;
    }

    public String getFailure_measures_desc() {
        return failure_measures_desc;
    }

    public void setFailure_measures_desc(String failure_measures_desc) {
        this.failure_measures_desc = failure_measures_desc;
    }
}
