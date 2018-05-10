package com.tiansu.eam.interfaces.common;

import java.io.Serializable;

/**
 * @author wangr
 * @description 人员工时
 * @create 2017-10-25 上午 11:27
 */
public class PersonJson implements Serializable {
    private static final long serialVersionUID = 1L;

    /**/
    private String id;

    /*人员id*/
    private String userId;

    /*人员姓名*/
    private String name;

    /*工时*/
    private String hour;

    /*单价*/
    private String price;

    /*备注*/
    private String note;

    private String hourTotal;

    private String skill;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHourTotal() {
        return hourTotal;
    }

    public void setHourTotal(String hourTotal) {
        this.hourTotal = hourTotal;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }
}
