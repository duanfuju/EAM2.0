package com.tiansu.eam.modules.faultOrder.entity;

import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;

/**
 * @author wangjl
 * Created by wangjl on 2017/8/9.
 * 故障工单请求审批实体类
 */
public class FaultSwitch extends DataEntity<FaultSwitch> {
    private static final long serialVersionUID = 1L;
    //工单id
    private String order_id;
    //需审批的操作类型 1-转单 2-挂起 3-解挂 4-验收 5-归档
    private String switch_type;
    //申请时间（转单、挂起）
    private Date switch_time;
    //审批时间
    private Date approve_time;
    //解挂时间，（解挂不需要审批，是对挂起操作的更新）
    private Date relieve_time;
    //审批状态 1-待审批 2-同意 3-拒绝
    private String approve_status;
    //接单人（只在转单时存入）
    private String switch_person;
    //申请人
    private String request_person;
    //审批人
    private String approve_person;
    //请求原因
    private String switch_reason;
    //审批理由
    private String approve_info;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getSwitch_type() {
        return switch_type;
    }

    public void setSwitch_type(String switch_type) {
        this.switch_type = switch_type;
    }

    public Date getSwitch_time() {
        return switch_time;
    }

    public void setSwitch_time(Date switch_time) {
        this.switch_time = switch_time;
    }

    public Date getApprove_time() {
        return approve_time;
    }

    public void setApprove_time(Date approve_time) {
        this.approve_time = approve_time;
    }

    public Date getRelieve_time() {
        return relieve_time;
    }

    public void setRelieve_time(Date relieve_time) {
        this.relieve_time = relieve_time;
    }

    public String getApprove_status() {
        return approve_status;
    }

    public void setApprove_status(String approve_status) {
        this.approve_status = approve_status;
    }

    public String getSwitch_person() {
        return switch_person;
    }

    public void setSwitch_person(String switch_person) {
        this.switch_person = switch_person;
    }

    public String getRequest_person() {
        return request_person;
    }

    public void setRequest_person(String request_person) {
        this.request_person = request_person;
    }

    public String getApprove_person() {
        return approve_person;
    }

    public void setApprove_person(String approve_person) {
        this.approve_person = approve_person;
    }

    public String getSwitch_reason() {
        return switch_reason;
    }

    public void setSwitch_reason(String switch_reason) {
        this.switch_reason = switch_reason;
    }

    public String getApprove_info() {
        return approve_info;
    }

    public void setApprove_info(String approve_info) {
        this.approve_info = approve_info;
    }
}

