package com.tiansu.eam.modules.maintain.entity;/**
 * Created by suven on 2017/11/2.
 */

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * 保养设置表（保养内容）
 *
 * @author suven suven
 * @create 2017/11/2
 */
public class MaintainProjectInfContent extends DataEntity<MaintainProjectInfContent> {

    private String project_id;//	保养计划id
    private String maintain_code;//	保养编码
    private String maintain_content;//	maintain_content

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getMaintain_code() {
        return maintain_code;
    }

    public void setMaintain_code(String maintain_code) {
        this.maintain_code = maintain_code;
    }

    public String getMaintain_content() {
        return maintain_content;
    }

    public void setMaintain_content(String maintain_content) {
        this.maintain_content = maintain_content;
    }
}
