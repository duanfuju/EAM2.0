package com.tiansu.eam.modules.inspection.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * @author wujh
 * @description 巡检任务其他费用实体类
 * @create 2017-10-13 14:27
 **/
public class InspectiontaskOthers extends DataEntity<InspectiontaskOthers> {

    private static final long serialVersionUID = 1L;

    private String id_key;                 //主键id
    private String inspectiontask_id;      //巡检任务id
    private String otherexpenses;      	 //其他费用事项
    private float otherexpenses_amount;  //其它费用金额
    private String otherexpenses_remark;  //其它费用备注

    public String getId_key() {
        return id_key;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }

    public String getInspectiontask_id() {
        return inspectiontask_id;
    }

    public void setInspectiontask_id(String inspectiontask_id) {
        this.inspectiontask_id = inspectiontask_id;
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
