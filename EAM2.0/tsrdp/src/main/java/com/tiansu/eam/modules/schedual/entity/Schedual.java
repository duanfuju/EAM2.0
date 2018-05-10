package com.tiansu.eam.modules.schedual.entity;

import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;

/**
 * Created by wangjl on 2017/8/9.
 * 排班信息
 */
public class Schedual extends DataEntity<Schedual> {
    //排班类型（时间为多条，不好查询，只查id和name供页面展示）
    private SchedualType schedual_type;
    //排班人员
    private String schedual_emp;
    //排班日期
    private Date schedule_date;

    //所属排班
    private String order_id;

    public SchedualType getSchedual_type() {
        return schedual_type;
    }

    public void setSchedual_type(SchedualType schedual_type) {
        this.schedual_type = schedual_type;
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

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}

