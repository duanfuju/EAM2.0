package com.tiansu.eam.modules.inspection.entity;

import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;

/**
 * @author wujh
 * @description 巡检任务流程任务中间表
 * @create 2017-10-17 9:58
 **/
public class InspectionTaskSwitch extends DataEntity<InspectionTaskSwitch> {

    private static final long serialVersionUID = 1L;

    private String id_key;                    //主键id
    private String inspectiontask_id;       //巡检任务主键id
    private String switch_status;           //转换状态
    private Date switch_time;               // 申请转单/挂起/撤销时间
    private String switch_reason;           // 申请原因
    private String apply_person;            // 申请人
    private String switch_person;           // 接单人
    private Date approve_time;              // 审批时间
    private String approve_reason;          // 审批理由
    private String approve_person;          // 审批人
    private Date relieve_time;              // 解挂时间
    private String switch_result;           // 审批结果

    public String getId_key() {
        return id_key;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }

    public String getInspectiontask_id() {
        return inspectiontask_id;
    }

    public void setInspectiontask_id(String inspectiontask_id) {
        this.inspectiontask_id = inspectiontask_id;
    }

    public String getSwitch_status() {
        return switch_status;
    }

    public void setSwitch_status(String switch_status) {
        this.switch_status = switch_status;
    }

    public Date getSwitch_time() {
        return switch_time;
    }

    public void setSwitch_time(Date switch_time) {
        this.switch_time = switch_time;
    }

    public String getSwitch_reason() {
        return switch_reason;
    }

    public void setSwitch_reason(String switch_reason) {
        this.switch_reason = switch_reason;
    }

    public String getApply_person() {
        return apply_person;
    }

    public void setApply_person(String apply_person) {
        this.apply_person = apply_person;
    }

    public String getSwitch_person() {
        return switch_person;
    }

    public void setSwitch_person(String switch_person) {
        this.switch_person = switch_person;
    }

    public Date getApprove_time() {
        return approve_time;
    }

    public void setApprove_time(Date approve_time) {
        this.approve_time = approve_time;
    }

    public String getApprove_reason() {
        return approve_reason;
    }

    public void setApprove_reason(String approve_reason) {
        this.approve_reason = approve_reason;
    }

    public String getApprove_person() {
        return approve_person;
    }

    public void setApprove_person(String approve_person) {
        this.approve_person = approve_person;
    }

    public Date getRelieve_time() {
        return relieve_time;
    }

    public void setRelieve_time(Date relieve_time) {
        this.relieve_time = relieve_time;
    }

    public String getSwitch_result() {
        return switch_result;
    }

    public void setSwitch_result(String switch_result) {
        this.switch_result = switch_result;
    }
}
