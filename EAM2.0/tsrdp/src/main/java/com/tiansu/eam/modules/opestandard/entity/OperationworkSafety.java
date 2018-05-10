package com.tiansu.eam.modules.opestandard.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * @author wujh
 * @description  标准工作安全措施
 * @create 2017-09-07 11:14
 **/
public class OperationworkSafety extends DataEntity<OperationworkSafety>{

    private static final long serialVersionUID = 1L;

    private String id_key;                 //主键id
    private String operationwork_id;      //标准工作id
    private String safety_code;           //安全措施编码
    private String safety_desc;           //安全措施描述
    private String safety_standard;      //安全措施质检标准
    private String safety_remark;        //安全措施备注

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

    public String getSafety_code() {
        return safety_code;
    }

    public void setSafety_code(String safety_code) {
        this.safety_code = safety_code;
    }

    public String getSafety_desc() {
        return safety_desc;
    }

    public void setSafety_desc(String safety_desc) {
        this.safety_desc = safety_desc;
    }

    public String getSafety_standard() {
        return safety_standard;
    }

    public void setSafety_standard(String safety_standard) {
        this.safety_standard = safety_standard;
    }

    public String getSafety_remark() {
        return safety_remark;
    }

    public void setSafety_remark(String safety_remark) {
        this.safety_remark = safety_remark;
    }
}
