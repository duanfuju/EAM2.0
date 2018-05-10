package com.tiansu.eam.interfaces.order.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangr
 * @description 工单列表封装json返回
 * @create 2017-10-17 下午 4:40
 */
public class OrderListJson implements Serializable {
    private static final long serialVersionUID = 1L;

    /*工单id*/
    private String orderId;

    /*任务id*/
    private String taskId;

    /*工单code*/
    private String code;

    /*报修地址*/
    private String location;

    /*报修地址*/
    private String locationName;

    /*报修地址*/
    private String detailLocation;

    /*故障描述 */
    private String bugDescription;

    /*故障现象*/
    private String bugAppearance;

    /*报修人*/
    private String bugLinkMan;

    /*报修人电话*/
    private String bugLinkManPhone;

    /*计划开始时间*/
    private Date planStartTime;

    /*计划结束时间*/
    private Date planFinishTime;

    /*预约、期望时间*/
    private Date expectTime;

    /*服务器时间*/
    private Date nowTime;

    /*派单时间*/
    private Date dispatchTime;

    /*到达时间*/
    private Date arriveTime;

    /*接单时间*/
    private Date takingTime;

    /*工单反馈完成时间*/
    private Date finishTime;

    /*工单生成时间*/
    private Date createTime;

    /*设备名称*/
    private String devName;

    /*对于抢单模式，是否超过规定时间*/
    private int isovertime;

    /*工单等级*/
    private int orderLevel;

    /*工单等级名称*/
    private String orderLevelName;

    /*工单状态*/
    private String orderStatus;

    /*工单状态*/
    private String orderStatusName;

    /*流程相关*/
    private String pstid;

    /*节点key*/
    private String taskDefKey;

    private String timeName;

    private String source;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getDetailLocation() {
        return detailLocation;
    }

    public void setDetailLocation(String detailLocation) {
        this.detailLocation = detailLocation;
    }

    public String getBugDescription() {
        return bugDescription;
    }

    public void setBugDescription(String bugDescription) {
        this.bugDescription = bugDescription;
    }

    public String getBugAppearance() {
        return bugAppearance;
    }

    public void setBugAppearance(String bugAppearance) {
        this.bugAppearance = bugAppearance;
    }

    public String getBugLinkMan() {
        return bugLinkMan;
    }

    public void setBugLinkMan(String bugLinkMan) {
        this.bugLinkMan = bugLinkMan;
    }

    public String getBugLinkManPhone() {
        return bugLinkManPhone;
    }

    public void setBugLinkManPhone(String bugLinkManPhone) {
        this.bugLinkManPhone = bugLinkManPhone;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "CET")
    public Date getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(Date expectTime) {
        this.expectTime = expectTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getNowTime() {
        return nowTime;
    }

    public void setNowTime(Date nowTime) {
        this.nowTime = nowTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(Date dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(Date arriveTime) {
        this.arriveTime = arriveTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getTakingTime() {
        return takingTime;
    }

    public void setTakingTime(Date takingTime) {
        this.takingTime = takingTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public int getIsovertime() {
        return isovertime;
    }

    public void setIsovertime(int isovertime) {
        this.isovertime = isovertime;
    }

    public int getOrderLevel() {
        return orderLevel;
    }

    public void setOrderLevel(int orderLevel) {
        this.orderLevel = orderLevel;
    }

    public String getOrderLevelName() {
        return orderLevelName;
    }

    public void setOrderLevelName(String orderLevelName) {
        this.orderLevelName = orderLevelName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public String getPstid() {
        return pstid;
    }

    public void setPstid(String pstid) {
        this.pstid = pstid;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
