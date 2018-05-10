package com.tiansu.eam.modules.maintain.entity;/**
 * Created by suven on 2017/11/2.
 */

import com.tiansu.eam.common.persistence.DataEntity;


/**
 * @creator duanfuju
 * @createtime 2017/11/2 15:22
 * @description:
 *      保养年计划表（保养内容）
 */
public class MaintainProjectContent  extends DataEntity<MaintainProjectContent> {

    private String project_id;//保养年计划id
    private String maintain_code;//保养编码
    private String maintain_content;//保养内容

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
