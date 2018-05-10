package com.tiansu.eam.interfaces.inspection.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author wangr
 * @description
 * @create 2017-11-02 下午 3:28
 */
public class InspectionFKDetailJson implements Serializable {

    private static final long serialVersionUID = 1L;

    /*巡检路线编码*/
    private String routeCode;

    /*路线*/
    private String routeName;

    /*设备*/
    private String devName;

    /*地址*/
    private String locationName;

    /*区域*/
    private String areaName;

    /*图片*/
    private String imgUrl;

    /*视频*/
    private String voideUrl;

    /*计划结束*/
    private Date planStartTime;

    /*计划结束时间*/
    private Date planFinishTime;

    /*巡检任务id*/
    private String inspectionId;

    /*状态*/
    private String status;

    /*巡检项*/
    private List<?> subject;

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVoideUrl() {
        return voideUrl;
    }

    public void setVoideUrl(String voideUrl) {
        this.voideUrl = voideUrl;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(Date planStartTime) {
        this.planStartTime = planStartTime;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getPlanFinishTime() {
        return planFinishTime;
    }

    public void setPlanFinishTime(Date planFinishTime) {
        this.planFinishTime = planFinishTime;
    }

    public String getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(String inspectionId) {
        this.inspectionId = inspectionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<?> getSubject() {
        return subject;
    }

    public void setSubject(List<?> subject) {
        this.subject = subject;
    }


}
