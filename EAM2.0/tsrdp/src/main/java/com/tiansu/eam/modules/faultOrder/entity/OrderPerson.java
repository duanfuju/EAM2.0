package com.tiansu.eam.modules.faultOrder.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * @creator caoh
 * @createtime 2017-10-20 9:12
 * @description: 人员工时反馈表
 */
public class OrderPerson extends DataEntity<OrderPerson> {
    private static final long serialVersionUID = 1L;
    //工单id
    private String order_id;
    //人员id
    private String emp_id;
    //人员工时
    private String hour;
    //人员费用
    private String charge;
    //人员备注
    private String remark;

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }




}

