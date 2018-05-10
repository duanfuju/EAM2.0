package com.tiansu.eam.interfaces.inspection.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangr
 * @description 区域详情
 * @create 2017-10-31 上午 9:25
 */
public class AreaDetailJson implements Serializable {
    private static final long serialVersionUID = 1L;

    /*区域id*/
    private String areaId;

    /*区域名称*/
    private String areaName;

    /*巡检区域状态*/
    private String areaStatus;

    private List<SubjectDetailJson> subject;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public List<SubjectDetailJson> getSubject() {
        return subject;
    }

    public void setSubject(List<SubjectDetailJson> subject) {
        this.subject = subject;
    }

    public String getAreaStatus() {
        return areaStatus;
    }

    public void setAreaStatus(String areaStatus) {
        this.areaStatus = areaStatus;
    }
}
