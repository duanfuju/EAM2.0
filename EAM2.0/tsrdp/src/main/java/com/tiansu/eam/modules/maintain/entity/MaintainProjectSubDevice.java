package com.tiansu.eam.modules.maintain.entity;
/**
 * Created by suven on 2017/11/2.
 */

/**
 * 保养月计划表（设备关联）
 *
 * @author suven suven
 * @create 2017/11/2
 */
public class MaintainProjectSubDevice{
    private String id;
    private String project_id;
    private String dev_id;

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

    public String getDev_id() {
        return dev_id;
    }

    public void setDev_id(String dev_id) {
        this.dev_id = dev_id;
    }
}
