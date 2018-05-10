package com.tiansu.eam.modules.maintain.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * @creator caoh
 * @createtime 2017-10-20 9:12
 * @description: 工器具反馈表
 */
public class MaintainTool extends DataEntity<MaintainTool> {

    //任务id
    private String task_id;
    //工器具id
    private String tool_id;
    //工器具数量
    private String tool_num;
    //工器具备注
    private String tool_remark;



    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }
    public String getTool_id() {
        return tool_id;
    }

    public void setTool_id(String tool_id) {
        this.tool_id = tool_id;
    }

    public String getTool_num() {
        return tool_num;
    }

    public void setTool_num(String tool_num) {
        this.tool_num = tool_num;
    }

    public String getTool_remark() {
        return tool_remark;
    }

    public void setTool_remark(String tool_remark) {
        this.tool_remark = tool_remark;
    }



}

