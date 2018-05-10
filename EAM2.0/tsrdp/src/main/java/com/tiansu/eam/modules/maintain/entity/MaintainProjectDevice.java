package com.tiansu.eam.modules.maintain.entity;/**
 * Created by suven on 2017/11/2.
 */

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * @creator duanfuju
 * @createtime 2017/11/2 15:24
 * @description:
 *      保养年计划表（设备关联）
 */
public class MaintainProjectDevice extends DataEntity<MaintainProjectDevice> {
    private String project_id; //保养计划id
    private String dev_id; //设备id

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
