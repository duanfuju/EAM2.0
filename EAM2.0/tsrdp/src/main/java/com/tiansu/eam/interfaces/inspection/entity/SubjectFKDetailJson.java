package com.tiansu.eam.interfaces.inspection.entity;

import java.io.Serializable;

/**
 * @author wangr
 * @description 巡检项反馈显示
 * @create 2017-11-02 下午 3:52
 */
public class SubjectFKDetailJson implements Serializable {

    private static final long serialVersionUID = 1L;

    /*巡检项id*/
    private String subjectId;

    /*巡检项编码*/
    private String subjectCode;

    /*巡检项名*/
    private String subjectName;

    /*巡检项内容方法*/
    private String subjectContent;

    /*检查值类型*/
    private String subject_valuetype;

    /*单位*/
    private String subject_unit;

    /*值1*/
    private String subject_value1;

    /*值2*/
    private String subject_value2;

    /*值3*/
    private String subject_value3;

    /*上限*/
    private String subject_sx_value;

    /*下限*/
    private String subject_xx_value;

    /*参考值*/
    private String subject_ck_value;

    /*检查值*/
    private String check_value;

    /*是否关闭*/
    private String isclose;

    /*故障现象*/
    private String appearance;

    /*检查结果*/
    private int check_result;

    /*备注*/
    private String remark;

    /*是否提交*/
    private String issubmit;

    private String check_picture;

    private String check_video;

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectContent() {
        return subjectContent;
    }

    public void setSubjectContent(String subjectContent) {
        this.subjectContent = subjectContent;
    }

    public String getSubject_ck_value() {
        return subject_ck_value;
    }

    public void setSubject_ck_value(String subject_ck_value) {
        this.subject_ck_value = subject_ck_value;
    }

    public String getCheck_value() {
        return check_value;
    }

    public void setCheck_value(String check_value) {
        this.check_value = check_value;
    }

    public String getIsclose() {
        return isclose;
    }

    public void setIsclose(String isclose) {
        this.isclose = isclose;
    }

    public String getSubject_valuetype() {
        return subject_valuetype;
    }

    public void setSubject_valuetype(String subject_valuetype) {
        this.subject_valuetype = subject_valuetype;
    }

    public String getSubject_unit() {
        return subject_unit;
    }

    public void setSubject_unit(String subject_unit) {
        this.subject_unit = subject_unit;
    }

    public String getSubject_value1() {
        return subject_value1;
    }

    public void setSubject_value1(String subject_value1) {
        this.subject_value1 = subject_value1;
    }

    public String getSubject_value2() {
        return subject_value2;
    }

    public void setSubject_value2(String subject_value2) {
        this.subject_value2 = subject_value2;
    }

    public String getSubject_value3() {
        return subject_value3;
    }

    public void setSubject_value3(String subject_value3) {
        this.subject_value3 = subject_value3;
    }

    public String getSubject_sx_value() {
        return subject_sx_value;
    }

    public void setSubject_sx_value(String subject_sx_value) {
        this.subject_sx_value = subject_sx_value;
    }

    public String getSubject_xx_value() {
        return subject_xx_value;
    }

    public void setSubject_xx_value(String subject_xx_value) {
        this.subject_xx_value = subject_xx_value;
    }

    public String getAppearance() {
        return appearance;
    }

    public void setAppearance(String appearance) {
        this.appearance = appearance;
    }

    public int getCheck_result() {
        return check_result;
    }

    public void setCheck_result(int check_result) {
        this.check_result = check_result;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIssubmit() {
        return issubmit;
    }

    public void setIssubmit(String issubmit) {
        this.issubmit = issubmit;
    }

    public String getCheck_picture() {
        return check_picture;
    }

    public void setCheck_picture(String check_picture) {
        this.check_picture = check_picture;
    }

    public String getCheck_video() {
        return check_video;
    }

    public void setCheck_video(String check_video) {
        this.check_video = check_video;
    }
}
