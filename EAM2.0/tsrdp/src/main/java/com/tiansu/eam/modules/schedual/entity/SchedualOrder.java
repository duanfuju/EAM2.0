package com.tiansu.eam.modules.schedual.entity;

import com.google.common.collect.Lists;
import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;
import java.util.List;

/**
 * Created by wangjl on 2017/8/9.
 * 排班信息
 */
public class SchedualOrder extends DataEntity<SchedualOrder> {
    private static final long serialVersionUID = 1L;
    //排班单编码
    private String order_code;

    private String order_name;

    private String remark;
    //单据审批状态//0:待提交  1：待审批  2：已审批   3：已驳回
    private String approve_status;

    private String pstid;

    private String approve_by;

    private String approve_reason;

    private Date approve_time;

    private List<Schedual> schedualList = Lists.newArrayList();;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getApprove_status() {
        return approve_status;
    }

    public void setApprove_status(String approve_status) {
        this.approve_status = approve_status;
    }

    public List<Schedual> getSchedualList() {
        return schedualList;
    }

    public void setSchedualList(List<Schedual> schedualList) {
        this.schedualList = schedualList;
    }

    public String getPstid() {
        return pstid;
    }

    public void setPstid(String pstid) {
        this.pstid = pstid;
    }

    public String getApprove_by() {
        return approve_by;
    }

    public void setApprove_by(String approve_by) {
        this.approve_by = approve_by;
    }

    public String getApprove_reason() {
        return approve_reason;
    }

    public void setApprove_reason(String approve_reason) {
        this.approve_reason = approve_reason;
    }

    public Date getApprove_time() {
        return approve_time;
    }

    public void setApprove_time(Date approve_time) {
        this.approve_time = approve_time;
    }
}

