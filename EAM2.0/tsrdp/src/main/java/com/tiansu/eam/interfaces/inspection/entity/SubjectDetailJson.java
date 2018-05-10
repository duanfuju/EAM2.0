package com.tiansu.eam.interfaces.inspection.entity;

import java.io.Serializable;

/**
 * @author wangr
 * @description 巡检项详情
 * @create 2017-10-31 上午 9:28
 */
public class SubjectDetailJson implements Serializable {

    private static final long serialVersionUID = 1L;

    /*地址名称*/
    private String locationName;

    /*设备id*/
    private String devId;

    /*设备名称*/
    private String devName;

    /*是否巡检 1是  0否*/
    private int isInspection;

    //巡检项名称
    private String subjectName;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public int getIsInspection() {
        return isInspection;
    }

    public void setIsInspection(int isInspection) {
        this.isInspection = isInspection;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
