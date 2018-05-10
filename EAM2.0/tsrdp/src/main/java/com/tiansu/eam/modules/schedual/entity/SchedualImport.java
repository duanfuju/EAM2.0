package com.tiansu.eam.modules.schedual.entity;

import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;

/**
 * @creator duanfuju
 * @createtime 2017/10/12 14:36
 * @description:
 *  用于导入
 */
public class SchedualImport extends DataEntity<SchedualImport> {
    private String order_id;
    private String order_code;//排班编码
    private String order_name;//排班名称
    private String approve_status;//审批状态
    private String remark;//备注
    private String schedual_type_code;//排班类型编码
    private String schedual_type_name;//排班类型名称
    private String schedual_emp;//人员名称
    private Date schedule_date;//排班日期

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getOrder_name() {
        return order_name;
    }

    public void setOrder_name(String order_name) {
        this.order_name = order_name;
    }

    public String getApprove_status() {
        return approve_status;
    }

    public void setApprove_status(String approve_status) {
        this.approve_status = approve_status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSchedual_type_code() {
        return schedual_type_code;
    }

    public void setSchedual_type_code(String schedual_type_code) {
        this.schedual_type_code = schedual_type_code;
    }

    public String getSchedual_type_name() {
        return schedual_type_name;
    }

    public void setSchedual_type_name(String schedual_type_name) {
        this.schedual_type_name = schedual_type_name;
    }

    public String getSchedual_emp() {
        return schedual_emp;
    }

    public void setSchedual_emp(String schedual_emp) {
        this.schedual_emp = schedual_emp;
    }

    public Date getSchedule_date() {
        return schedule_date;
    }

    public void setSchedule_date(Date schedule_date) {
        this.schedule_date = schedule_date;
    }

    @Override
    public String toString() {
        return "SchedualImport{" +
                "order_id='" + order_id + '\'' +
                ", order_code='" + order_code + '\'' +
                ", order_name='" + order_name + '\'' +
                ", approve_status='" + approve_status + '\'' +
                ", remark='" + remark + '\'' +
                ", schedual_type_code='" + schedual_type_code + '\'' +
                ", schedual_type_name='" + schedual_type_name + '\'' +
                ", schedual_emp='" + schedual_emp + '\'' +
                ", schedule_date=" + schedule_date +
                '}';
    }
}

