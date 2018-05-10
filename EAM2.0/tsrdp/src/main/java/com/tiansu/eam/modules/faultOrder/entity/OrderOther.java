package com.tiansu.eam.modules.faultOrder.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * @creator caoh
 * @createtime 2017-10-20 9:12
 * @description: 其他费用反馈表
 */
public class OrderOther extends DataEntity<OrderOther> {
    private static final long serialVersionUID = 1L;
    //工单id
    private String order_id;
    //其他费用名称
    private String charge_name;
    //价格
    private String charge_price;
    //备注
    private String charge_remark;


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


    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }




}

