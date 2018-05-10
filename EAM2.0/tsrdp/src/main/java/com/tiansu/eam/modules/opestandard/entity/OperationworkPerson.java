package com.tiansu.eam.modules.opestandard.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * @author wujh
 * @description   标准工作人员工时实体类
 * @create 2017-09-07 11:45
 **/
public class OperationworkPerson extends DataEntity<OperationworkPerson> {

    private static final long serialVersionUID = 1L;

    private String id_key;                 //主键id
    private String operationwork_id;      //标准工作id
    private String loginname;              //登录人员
    private float person_hours;           //人员工时
    private float person_hourprice;      //额定工时单价
    private String person_postskill;     //人员岗位技能
    private float person_hourtotal;     //额定工时小计
    private String person_remark;        //人员工时备注

    public OperationworkPerson() {
    }

    public OperationworkPerson(String operationwork_id, String loginname, float person_hours, float person_hourprice, String person_postskill, float person_hourtotal, String person_remark) {
        this.operationwork_id = operationwork_id;
        this.loginname = loginname;
        this.person_hours = person_hours;
        this.person_hourprice = person_hourprice;
        this.person_postskill = person_postskill;
        this.person_hourtotal = person_hourtotal;
        this.person_remark = person_remark;
    }

    public String getId_key() {
        return id_key;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }

    public String getOperationwork_id() {
        return operationwork_id;
    }

    public void setOperationwork_id(String operationwork_id) {
        this.operationwork_id = operationwork_id;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public float getPerson_hours() {
        return person_hours;
    }

    public void setPerson_hours(float person_hours) {
        this.person_hours = person_hours;
    }

    public float getPerson_hourprice() {
        return person_hourprice;
    }

    public void setPerson_hourprice(float person_hourprice) {
        this.person_hourprice = person_hourprice;
    }

    public String getPerson_postskill() {
        return person_postskill;
    }

    public void setPerson_postskill(String person_postskill) {
        this.person_postskill = person_postskill;
    }

    public float getPerson_hourtotal() {
        return person_hourtotal;
    }

    public void setPerson_hourtotal(float person_hourtotal) {
        this.person_hourtotal = person_hourtotal;
    }

    public String getPerson_remark() {
        return person_remark;
    }

    public void setPerson_remark(String person_remark) {
        this.person_remark = person_remark;
    }
}
