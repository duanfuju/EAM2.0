package com.tiansu.eam.interfaces.common;


import java.io.Serializable;


public class ProcedureJson implements Serializable{

    private static final long serialVersionUID = 1L;

    private String id;                 //主键id
    private String inspectiontask_id;      //标准工作id
    private String procedure_code;        //工序编码
    private String procedure_desc;        //工序描述
    private String procedure_standard;   //工序质检标准
    private String procedure_remark;     //工序备注

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInspectiontask_id() {
        return inspectiontask_id;
    }

    public void setInspectiontask_id(String inspectiontask_id) {
        this.inspectiontask_id = inspectiontask_id;
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
