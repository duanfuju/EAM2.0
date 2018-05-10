package com.tiansu.eam.modules.maintain.entity;

/**
 * @author zhangww
 * @description
 * @create 2017-10-17 18:56
 **/
public class MaintSubProc {
    public String id;
    public String project_id;
    public String procedure_code;
    public String procedure_desc;
    public String procedure_standard;
    public String procedure_remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getProcedure_code() {
        return procedure_code;
    }

    public void setProcedure_code(String procedure_code) {
        this.procedure_code = procedure_code;
    }

    public String getProcedure_desc() {
        return procedure_desc;
    }

    public void setProcedure_desc(String procedure_desc) {
        this.procedure_desc = procedure_desc;
    }

    public String getProcedure_standard() {
        return procedure_standard;
    }

    public void setProcedure_standard(String procedure_standard) {
        this.procedure_standard = procedure_standard;
    }

    public String getProcedure_remark() {
        return procedure_remark;
    }

    public void setProcedure_remark(String procedure_remark) {
        this.procedure_remark = procedure_remark;
    }
}
