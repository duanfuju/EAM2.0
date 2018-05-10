package com.tiansu.eam.modules.maintain.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * @creator caoh
 * @createtime 2017-10-20 9:12
 * @description: 备品备件反馈表
 */
public class MaintainAttachment extends DataEntity<MaintainAttachment> {

    //任务id
    private String task_id;
    //备品id
    private String attachment_id;
    //备品数量
    private String attachment_num;
    //备品备注
    private String attachment_remark;


    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
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

