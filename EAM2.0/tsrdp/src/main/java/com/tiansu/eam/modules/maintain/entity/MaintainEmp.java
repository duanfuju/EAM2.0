package com.tiansu.eam.modules.maintain.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * @creator caoh
 * @createtime 2017-10-20 9:12
 * @description: 人员工时反馈表
 */
public class MaintainEmp extends DataEntity<MaintainEmp> {

    //任务id
    private String task_id;
    //人员id
    private String emp_id;
    //单价
    private String emp_price;
    //人员工时
    private String emp_hour;
    //人员岗位技能
    private String emp_skills;
    //人员备注
    private String emp_remark;

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getEmp_price() {
        return emp_price;
    }

    public void setEmp_price(String emp_price) {
        this.emp_price = emp_price;
    }

    public String getEmp_hour() {
        return emp_hour;
    }

    public void setEmp_hour(String emp_hour) {
        this.emp_hour = emp_hour;
    }

    public String getEmp_skills() {
        return emp_skills;
    }

    public void setEmp_skills(String emp_skills) {
        this.emp_skills = emp_skills;
    }

    public String getEmp_remark() {
        return emp_remark;
    }

    public void setEmp_remark(String emp_remark) {
        this.emp_remark = emp_remark;
    }







}

