package com.tiansu.eam.interfaces.maintain.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangr
 * @description
 * @create 2017-11-06 下午 3:51
 */
public class MaintainListJson implements Serializable {

    private static final long serialVersionUID = 1L;

    /*保养id*/
    private String maintainId;

    /*任务id*/
    private String taskId;

    /*流程id*/
    private String pstid;

    /*所属计划id*/
    private String projectId;

    /*设备*/
    private String devName;

    /*地址*/
    private String locationName;

    /*周期类型*/
    private String cycleType;

    /*是否逾期*/
    private String isoverdue;

    /*计划开始时间*/
    private Date planStartTime;

    /*计划完成时间*/
    private Date planFinishTime;

    /*实际开始时间*/
    private Date startTime;

    /*实际结束时间*/
    private Date finishTime;

    /*路线code*/
    private String maintainCode;

    /*保养名称*/
    private String maintainName;

    /*保养分类*/
    private String maintainType;

    /*保养类型*/
    private String maintainMode;

    /*状态*/
    private String status;

    /*状态名*/
    private String statusName;

    /*流程节点key*/
    private String taskDefKey;

    public String getMaintainId() {
        return maintainId;
    }

    public void setMaintainId(String maintainId) {
        this.maintainId = maintainId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getPstid() {
        return pstid;
    }

    public void setPstid(String pstid) {
        this.pstid = pstid;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    public String getCycleType() {
        return cycleType;
    }

    public void setCycleType(String cycleType) {
        this.cycleType = cycleType;
    }

    public String getIsoverdue() {
        return isoverdue;
    }

    public void setIsoverdue(String isoverdue) {
        this.isoverdue = isoverdue;
    }

    public Date getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(Date planStartTime) {
        this.planStartTime = planStartTime;
    }

    public Date getPlanFinishTime() {
        return planFinishTime;
    }

    public void setPlanFinishTime(Date planFinishTime) {
        this.planFinishTime = planFinishTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getMaintainCode() {
        return maintainCode;
    }

    public void setMaintainCode(String maintainCode) {
        this.maintainCode = maintainCode;
    }

    public String getMaintainName() {
        return maintainName;
    }

    public void setMaintainName(String maintainName) {
        this.maintainName = maintainName;
    }

    public String getMaintainType() {
        return maintainType;
    }

    public void setMaintainType(String maintainType) {
        this.maintainType = maintainType;
    }

    public String getMaintainMode() {
        return maintainMode;
    }

    public void setMaintainMode(String maintainMode) {
        this.maintainMode = maintainMode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }
}
