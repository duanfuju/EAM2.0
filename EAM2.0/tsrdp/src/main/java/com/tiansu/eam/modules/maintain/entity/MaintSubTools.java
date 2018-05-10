package com.tiansu.eam.modules.maintain.entity;

/**
 * @author zhangdf
 * @description
 * @create 2017-10-17 18:57
 **/
public class MaintSubTools {
    public String id;
    public String project_id;
    public String tool_id;
    public String tool_num;
    public String tool_remark;

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
