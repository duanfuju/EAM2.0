package com.tiansu.eam.interfaces.common;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangr
 * @description
 * @create 2017-11-16 下午 2:52
 */
public class History implements Serializable{
    private static final long serialVersionUID = 1L;

    /*节点名称*/
    private String activityName;

    /*节点时间*/
    private Date activityTime;

    /*任务处理人*/
    private String userName;

    /*转给谁*/
    private String switchPerson;

    /*转给谁*/
    private String switchPhone;

    /*手机号码*/
    private String phone;

    /*申请 审批意见*/
    private String reason;

    /*审批结果*/
    private String result;

    /*评分*/
    private String score;

    /*评价*/
    private String evaluate;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(Date activityTime) {
        this.activityTime = activityTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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

    public String getSwitchPerson() {
        return switchPerson;
    }

    public void setSwitchPerson(String switchPerson) {
        this.switchPerson = switchPerson;
    }

    public String getSwitchPhone() {
        return switchPhone;
    }

    public void setSwitchPhone(String switchPhone) {
        this.switchPhone = switchPhone;
    }
}
