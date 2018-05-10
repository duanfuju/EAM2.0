package com.tiansu.eam.modules.maintain.entity;

/**
 * @author zhangdf
 * @description
 * @create 2017-10-17 18:59
 **/
public class MaintSubSpareparts {
    public String id;
    public String project_id;
    public String attachment_id;
    public String  attachment_num;
    public String attachment_remark;

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

    public String getAttachment_id() {
        return attachment_id;
    }

    public void setAttachment_id(String attachment_id) {
        this.attachment_id = attachment_id;
    }

    public String getAttachment_num() {
        return attachment_num;
    }

    public void setAttachment_num(String attachment_num) {
        this.attachment_num = attachment_num;
    }

    public String getAttachment_remark() {
        return attachment_remark;
    }

    public void setAttachment_remark(String attachment_remark) {
        this.attachment_remark = attachment_remark;
    }
}
