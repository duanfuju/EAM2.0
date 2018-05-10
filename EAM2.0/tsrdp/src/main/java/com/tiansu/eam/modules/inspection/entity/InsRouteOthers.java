package com.tiansu.eam.modules.inspection.entity;

/**
 * @author zhangdf
 * @description
 * @create 2017-10-17 19:00
 **/
public class InsRouteOthers {
    public String id;
    public String inspectionroute_id;
    public String otherexpenses;
    public String otherexpenses_amount;
    public String otherexpenses_remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInspectionroute_id() {
        return inspectionroute_id;
    }

    public void setInspectionroute_id(String inspectionroute_id) {
        this.inspectionroute_id = inspectionroute_id;
    }

    public String getOtherexpenses() {
        return otherexpenses;
    }

    public void setOtherexpenses(String otherexpenses) {
        this.otherexpenses = otherexpenses;
    }

    public String getOtherexpenses_amount() {
        return otherexpenses_amount;
    }

    public void setOtherexpenses_amount(String otherexpenses_amount) {
        this.otherexpenses_amount = otherexpenses_amount;
    }

    public String getOtherexpenses_remark() {
        return otherexpenses_remark;
    }

    public void setOtherexpenses_remark(String otherexpenses_remark) {
        this.otherexpenses_remark = otherexpenses_remark;
    }
}
