package com.tiansu.eam.modules.maintain.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * @creator caoh
 * @createtime 2017-10-20 9:12
 * @description: 其他费用反馈表
 */
public class MaintainOther extends DataEntity<MaintainOther> {

    //任务id
    private String task_id;
    //费用名称
    private String charge_name;

    //费用价格
    private String charge_price;
    //费用备注
    private String charge_remark;


    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
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

