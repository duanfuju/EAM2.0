package com.tiansu.eam.modules.opestandard.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * @author wujh
 * @description
 * @create 2017-09-07 11:20
 **/
public class OperationworkTools extends DataEntity<OperationworkTools> {

    private static final long serialVersionUID = 1L;

    private String id_key;                 //主键id
    private String operationwork_id;      //标准工作id
    private String material_id;           //工器具id
    private int  tools_num;               //工器具数量
    private String tools_remark;          //工器具备注

    public OperationworkTools() {
    }

    public OperationworkTools(String operationwork_id, String material_id, int tools_num, String tools_remark) {
        this.operationwork_id = operationwork_id;
        this.material_id = material_id;
        this.tools_num = tools_num;
        this.tools_remark = tools_remark;
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

    public String getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(String material_id) {
        this.material_id = material_id;
    }

    public int getTools_num() {
        return tools_num;
    }

    public void setTools_num(int tools_num) {
        this.tools_num = tools_num;
    }

    public String getTools_remark() {
        return tools_remark;
    }

    public void setTools_remark(String tools_remark) {
        this.tools_remark = tools_remark;
    }
}
