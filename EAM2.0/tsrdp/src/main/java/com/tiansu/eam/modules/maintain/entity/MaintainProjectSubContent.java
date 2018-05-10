package com.tiansu.eam.modules.maintain.entity;/**
 * Created by suven on 2017/11/2.
 */

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * 保养月计划表（保养内容）
 *
 * @author suven suven
 * @create 2017/11/2
 */
public class MaintainProjectSubContent  extends DataEntity<MaintainProjectSubContent> {
        private String id;
        private String project_id;
        private String maintain_code;
        private String maintain_content;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

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
