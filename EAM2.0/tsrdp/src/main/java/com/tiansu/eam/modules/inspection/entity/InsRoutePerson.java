package com.tiansu.eam.modules.inspection.entity;

/**
 * @author zhangdf
 * @description
 * @create 2017-10-17 18:59
 **/
public class InsRoutePerson {
    public String id;
    public String inspectionroute_id;
    public String loginname;
    public String person_hours;
    public String person_hourprice;
    public String person_postskill;
    public String person_hourtotal;
    public String person_remark;

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

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getPerson_hours() {
        return person_hours;
    }

    public void setPerson_hours(String person_hours) {
        this.person_hours = person_hours;
    }

    public String getPerson_hourprice() {
        return person_hourprice;
    }

    public void setPerson_hourprice(String person_hourprice) {
        this.person_hourprice = person_hourprice;
    }

    public String getPerson_postskill() {
        return person_postskill;
    }

    public void setPerson_postskill(String person_postskill) {
        this.person_postskill = person_postskill;
    }

    public String getPerson_hourtotal() {
        return person_hourtotal;
    }

    public void setPerson_hourtotal(String person_hourtotal) {
        this.person_hourtotal = person_hourtotal;
    }

    public String getPerson_remark() {
        return person_remark;
    }

    public void setPerson_remark(String person_remark) {
        this.person_remark = person_remark;
    }
}
