package com.tiansu.eam.modules.employee.entity;/**
 * @description
 * @author duanfuju
 * @create 2017-09-14 16:16
 **/

import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;

/**
 * @author duanfuju
 * @create 2017-09-14 16:16
 * @desc 人员信息扩展 entity
 **/
public class EamUserExt extends DataEntity<EamUserExt> {

    private String id_key;//主键
    private String loginname;//用户登录名
    private String own_area;//归属区域（关联设备位置表的位置编号loc_id）
    private String duty_area;//责任区域
    private Date birth_date;//出生日期
    private String major;//专业
    private String higher_author;//上级主管
    private Date hiredate;//聘用时间
    private Date expirydate;//终止时间
    private String remark;//备注
    private String status;//状态(在岗，待岗，离职，退休)

    public String getId_key() {
        return id_key;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getOwn_area() {
        return own_area;
    }

    public void setOwn_area(String own_area) {
        this.own_area = own_area;
    }

    public String getDuty_area() {
        return duty_area;
    }

    public void setDuty_area(String duty_area) {
        this.duty_area = duty_area;
    }

    public Date getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getHigher_author() {
        return higher_author;
    }

    public void setHigher_author(String higher_author) {
        this.higher_author = higher_author;
    }

    public Date getHiredate() {
        return hiredate;
    }

    public void setHiredate(Date hiredate) {
        this.hiredate = hiredate;
    }

    public Date getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(Date expirydate) {
        this.expirydate = expirydate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

