package com.tiansu.eam.modules.opestandard.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * @author wujh
 * @description  标准工作工序表实体类
 * @create 2017-09-07 10:46
 * @createtime 2017/9/7 10:47
 **/
public class OperationworkProcedure extends DataEntity<OperationworkProcedure>{

    private static final long serialVersionUID = 1L;

    private String id_key;                 //主键id
    private String operationwork_id;      //标准工作id
    private String procedure_code;        //工序编码
    private String procedure_desc;        //工序描述
    private String procedure_standard;   //工序质检标准
    private String procedure_remark;     //工序备注

    public OperationworkProcedure() {
    }

    public OperationworkProcedure(String operationwork_id, String procedure_code, String procedure_desc, String procedure_standard, String procedure_remark) {
        this.operationwork_id = operationwork_id;
        this.procedure_code = procedure_code;
        this.procedure_desc = procedure_desc;
        this.procedure_standard = procedure_standard;
        this.procedure_remark = procedure_remark;
    }

    public String getId_key() {
        return id_key;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }

    public String getOperationwork_id() {
        return operationwork_id;
    }

    public void setOperationwork_id(String operationwork_id) {
        this.operationwork_id = operationwork_id;
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
