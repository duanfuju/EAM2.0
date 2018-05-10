package com.tiansu.eam.modules.maintain.entity;/**
 * Created by suven on 2017/11/2.
 */

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * 保养设置表（设备关联）
 *
 * @author suven suven
 * @create 2017/11/2
 */
public class MaintainProjectInfDevice extends DataEntity<MaintainProjectInfDevice> {
    private String project_id;//	保养计划id
    private String dev_id;//	设备id

    public MaintainProjectInfDevice(String project_id, String dev_id) {
        this.project_id = project_id;
        this.dev_id = dev_id;
    }

    public MaintainProjectInfDevice(String id, String project_id, String dev_id) {
        super(id);
        this.project_id = project_id;
        this.dev_id = dev_id;
    }
    public MaintainProjectInfDevice() {
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
