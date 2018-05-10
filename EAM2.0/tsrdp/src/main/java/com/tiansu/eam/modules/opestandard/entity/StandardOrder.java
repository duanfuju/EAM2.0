package com.tiansu.eam.modules.opestandard.entity;

import com.tiansu.eam.common.persistence.DataEntity;
import com.tiansu.eam.modules.sys.entity.User;

import java.util.Date;

/**
 * @author zhangww
 * @description
 * @create 2017-10-09 10:46
 **/
public class StandardOrder extends DataEntity<StandardOrder>{
    private String pstid;//流程记录字段
    private String id_key;
    private String order_code;
    private String order_content;
    private String order_priority;
    private String order_totalhours;
    private String order_work;
    private String order_desc;
    private String order_remark;
    private String order_status;
    private String order_major;
    private User approve_by;
    private String approve_reason;
    private Date approve_time;
    private String approve_status;
    private User create_by;
    private Date create_time;
    private User update_by;
    private Date update_time;

    public String getPstid() {
        return pstid;
    }

    public void setPstid(String pstid) {
        this.pstid = pstid;
    }

    public String getId_key() {
        return id_key;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getOrder_content() {
        return order_content;
    }

    public void setOrder_content(String order_content) {
        this.order_content = order_content;
    }

    public String getOrder_priority() {
        return order_priority;
    }

    public void setOrder_priority(String order_priority) {
        this.order_priority = order_priority;
    }

    public String getOrder_totalhours() {
        return order_totalhours;
    }

    public void setOrder_totalhours(String order_totalhours) {
        this.order_totalhours = order_totalhours;
    }

    public String getOrder_work() {
        return order_work;
    }

    public void setOrder_work(String order_work) {
        this.order_work = order_work;
    }

    public String getOrder_desc() {
        return order_desc;
    }

    public void setOrder_desc(String order_desc) {
        this.order_desc = order_desc;
    }

    public String getOrder_remark() {
        return order_remark;
    }

    public void setOrder_remark(String order_remark) {
        this.order_remark = order_remark;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public User getApprove_by() {
        return approve_by;
    }

    public void setApprove_by(User approve_by) {
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

    public String getApprove_status() {
        return approve_status;
    }

    public void setApprove_status(String approve_status) {
        this.approve_status = approve_status;
    }

    public User getCreate_by() {
        return create_by;
    }

    public void setCreate_by(User create_by) {
        this.create_by = create_by;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public User getUpdate_by() {
        return update_by;
    }

    public void setUpdate_by(User update_by) {
        this.update_by = update_by;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public String getOrder_major() {
        return order_major;
    }

    public void setOrder_major(String order_major) {
        this.order_major = order_major;
    }
}
