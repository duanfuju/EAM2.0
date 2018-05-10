package com.tiansu.eam.modules.opestandard.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * @author wujh
 * @description 标准工作其他费用实体类
 * @create 2017-09-07 11:50
 **/
public class OperationworkOthers extends DataEntity<OperationworkOthers> {

    private static final long serialVersionUID = 1L;

    private String id_key;                 //主键id
    private String operationwork_id;      //标准工作id
    private String otherexpenses;      	 //其他费用事项
    private float otherexpenses_amount;  //其它费用金额
    private String otherexpenses_remark;  //其它费用备注

    public OperationworkOthers() {
    }

    public OperationworkOthers(String operationwork_id, String otherexpenses, float otherexpenses_amount, String otherexpenses_remark) {
        this.operationwork_id = operationwork_id;
        this.otherexpenses = otherexpenses;
        this.otherexpenses_amount = otherexpenses_amount;
        this.otherexpenses_remark = otherexpenses_remark;
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

    public String getOtherexpenses() {
        return otherexpenses;
    }

    public void setOtherexpenses(String otherexpenses) {
        this.otherexpenses = otherexpenses;
    }

    public float getOtherexpenses_amount() {
        return otherexpenses_amount;
    }

    public void setOtherexpenses_amount(float otherexpenses_amount) {
        this.otherexpenses_amount = otherexpenses_amount;
    }

    public String getOtherexpenses_remark() {
        return otherexpenses_remark;
    }

    public void setOtherexpenses_remark(String otherexpenses_remark) {
        this.otherexpenses_remark = otherexpenses_remark;
    }
}
