package com.tiansu.eam.interfaces.inspection.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangr
 * @description 我的巡检列表返回结果封装json
 * @create 2017-10-30 上午 10:46
 */
public class InspectionListJson implements Serializable {
    private static final long serialVersionUID = 1L;

    /*巡检id*/
    private String inspectionId;

    /*任务id*/
    private String taskId;

    /*路线id*/
    private String routeId;

    /*流程id*/
    private String pstid;

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
    private String routeCode;

    /*周期具体到天*/
    private String routeCycle;

    /*巡检路线名称*/
    private String routeName;

    /*路线对象名称*/
    private String routeObject;

    /*巡检路线分类*/
    private String routeType;

    /*巡检类型*/
    private String routeMode;

    /*状态*/
    private String status;

    /*状态名*/
    private String statusName;

    private String period;

    /*流程节点key*/
    private String taskDefKey;

    private String timeName;

    public String getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(String inspectionId) {
        this.inspectionId = inspectionId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getPstid() {
        return pstid;
    }

    public void setPstid(String pstid) {
        this.pstid = pstid;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getRouteCycle() {
        return routeCycle;
    }

    public void setRouteCycle(String routeCycle) {
        this.routeCycle = routeCycle;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteObject() {
        return routeObject;
    }

    public void setRouteObject(String routeObject) {
        this.routeObject = routeObject;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public String getRouteMode() {
        return routeMode;
    }

    public void setRouteMode(String routeMode) {
        this.routeMode = routeMode;
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

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getTimeName() {
        return timeName;
    }

    public void setTimeName(String timeName) {
        this.timeName = timeName;
    }
}
