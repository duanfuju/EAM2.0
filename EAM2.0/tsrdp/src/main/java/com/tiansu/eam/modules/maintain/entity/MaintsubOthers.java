package com.tiansu.eam.modules.maintain.entity;

/**
 * @author zhangdf
 * @description
 * @create 2017-10-17 19:00
 **/
public class MaintsubOthers {
    public String id;
    public String project_id;
    public String charge_name;
    public String charge_price;
    public String charge_remark;

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

    public String getCharge_name() {
        return charge_name;
    }

    public void setCharge_name(String charge_name) {
        this.charge_name = charge_name;
    }

    public String getCharge_price() {
        return charge_price;
    }

    public void setCharge_price(String charge_price) {
        this.charge_price = charge_price;
    }

    public String getCharge_remark() {
        return charge_remark;
    }

    public void setCharge_remark(String charge_remark) {
        this.charge_remark = charge_remark;
    }
}
