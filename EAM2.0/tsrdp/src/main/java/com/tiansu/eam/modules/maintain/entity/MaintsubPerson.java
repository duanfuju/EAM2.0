package com.tiansu.eam.modules.maintain.entity;

/**
 * @author zhangdf
 * @description
 * @create 2017-10-17 18:59
 **/
public class MaintsubPerson {
    public String id;
    public String project_id;
    public String emp_id;
    public String emp_hour;
    public String emp_price;
    public String emp_skills;
    public String emp_remark;

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

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getEmp_hour() {
        return emp_hour;
    }

    public void setEmp_hour(String emp_hour) {
        this.emp_hour = emp_hour;
    }

    public String getEmp_price() {
        return emp_price;
    }

    public void setEmp_price(String emp_price) {
        this.emp_price = emp_price;
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
