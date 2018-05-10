package com.tiansu.eam.interfaces.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tiansu.eam.interfaces.common.OtherChargesJosn;
import com.tiansu.eam.interfaces.common.PersonJson;
import com.tiansu.eam.interfaces.common.Tool_MaterialsJson;
import org.activiti.engine.history.HistoricActivityInstance;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author wangr
 * @description 用于返回我的工单详情
 * @create 2017-10-25 上午 10:15
 */
public class OrderDetailsJson implements Serializable {
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

    /*故障图片*/
    private String bugImg;

    /*故障视频*/
    private String bugVideo;

    /*到达图片*/
    private String arriveImg;

    /*反馈原因*/
    private String reason;

    /*反馈结果 1修好 0未修好*/
    private String orderResult;

    /*反馈图片*/
    private String fkImg;

    /*反馈视频*/
    private String fkVideo;

    /*评分*/
    private String score;

    /*评价内容*/
    private String evaluate;

    /*节点key*/
    private String taskDefKey;

    /*保修来源*/
    private String source;

    /*工器具*/
    private List<Tool_MaterialsJson> tools;

    /*备件*/
    private List<Tool_MaterialsJson> materials;

    /*人员工时*/
    private List<PersonJson> manHour;

    /*其他费用*/
    private List<OtherChargesJosn> other;

    /*历史记录*/
    private List<?> history;

    private String timeName;

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

    public String getBugImg() {
        return bugImg;
    }

    public void setBugImg(String bugImg) {
        this.bugImg = bugImg;
    }

    public String getBugVideo() {
        return bugVideo;
    }

    public void setBugVideo(String bugVideo) {
        this.bugVideo = bugVideo;
    }

    public String getArriveImg() {
        return arriveImg;
    }

    public void setArriveImg(String arriveImg) {
        this.arriveImg = arriveImg;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getOrderResult() {
        return orderResult;
    }

    public void setOrderResult(String orderResult) {
        this.orderResult = orderResult;
    }

    public String getFkImg() {
        return fkImg;
    }

    public void setFkImg(String fkImg) {
        this.fkImg = fkImg;
    }

    public String getFkVideo() {
        return fkVideo;
    }

    public void setFkVideo(String fkVideo) {
        this.fkVideo = fkVideo;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Tool_MaterialsJson> getTools() {
        return tools;
    }

    public void setTools(List<Tool_MaterialsJson> tools) {
        this.tools = tools;
    }

    public List<Tool_MaterialsJson> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Tool_MaterialsJson> materials) {
        this.materials = materials;
    }

    public List<PersonJson> getManHour() {
        return manHour;
    }

    public void setManHour(List<PersonJson> manHour) {
        this.manHour = manHour;
    }

    public List<OtherChargesJosn> getOther() {
        return other;
    }

    public void setOther(List<OtherChargesJosn> other) {
        this.other = other;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public List<?> getHistory() {
        return history;
    }

    public void setHistory(List<?> history) {
        this.history = history;
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
