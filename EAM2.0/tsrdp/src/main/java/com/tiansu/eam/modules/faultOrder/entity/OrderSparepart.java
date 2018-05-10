package com.tiansu.eam.modules.faultOrder.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * @creator caoh
 * @createtime 2017-10-20 9:12
 * @description: 备品备件反馈表
 */
public class OrderSparepart extends DataEntity<OrderSparepart> {
    private static final long serialVersionUID = 1L;
    //工单id
    private String order_id;
    //备件id
    private String part_id;
    //备件数量
    private String part_num;
    //备件备注
    private String part_remark;

    public String getPart_id() {
        return part_id;
    }

    public void setPart_id(String part_id) {
        this.part_id = part_id;
    }

    public String getPart_num() {
        return part_num;
    }

    public void setPart_num(String part_num) {
        this.part_num = part_num;
    }

    public String getPart_remark() {
        return part_remark;
    }

    public void setPart_remark(String part_remark) {
        this.part_remark = part_remark;
    }


    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }





}

