package com.tiansu.eam.modules.inspection.entity;

import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;
import java.util.List;

/**
 * @author wujh
 * @description 巡检任务反馈实体类
 * @create 2017-10-24 17:09
 **/
public class InspectiontaskFeedback extends DataEntity<InspectiontaskFeedback> {

    private static final long serialVersionUID = 1L;

    private String id_key;                 // 反馈表主键
    private String inspectiontask_id;    //巡检任务id
    private String subject_id;           // 巡检项id
    private String check_value;          // 检查值
    private Date check_time;             // 检查时间
    private String check_result;         // 检查结果
    private String check_picture;        //检查图片
    private String check_video;          //检查视频
    private String appearance;           //反馈现象
    private String issubmit;             // 是否已提交
    private String isclose;              // 是否已关闭
    private String remark;               // 备注
    private String create_by;            // 创建者
    private Date create_time;            //创建时间
    private String update_by;            // 更新者
    private Date update_time;            // 更新时间

    private String expect_time;            // 期望解决时间
    private String processMode;          // 处理方式

    public InspectiontaskFeedback() {
    }

    public String getId_key() {
        return id_key;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }

    public String getInspectiontask_id() {
        return inspectiontask_id;
    }

    public void setInspectiontask_id(String inspectiontask_id) {
        this.inspectiontask_id = inspectiontask_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getCheck_value() {
        return check_value;
    }

    public void setCheck_value(String check_value) {
        this.check_value = check_value;
    }

    public Date getCheck_time() {
        return check_time;
    }

    public void setCheck_time(Date check_time) {
        this.check_time = check_time;
    }

    public String getCheck_result() {
        return check_result;
    }

    public void setCheck_result(String check_result) {
        this.check_result = check_result;
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

    public String getAppearance() {
        return appearance;
    }

    public void setAppearance(String appearance) {
        this.appearance = appearance;
    }

    public String getIssubmit() {
        return issubmit;
    }

    public void setIssubmit(String issubmit) {
        this.issubmit = issubmit;
    }

    public String getIsclose() {
        return isclose;
    }

    public void setIsclose(String isclose) {
        this.isclose = isclose;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_by() {
        return update_by;
    }

    public void setUpdate_by(String update_by) {
        this.update_by = update_by;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public String getExpect_time() {
        return expect_time;
    }

    public void setExpect_time(String expect_time) {
        this.expect_time = expect_time;
    }

    public String getProcessMode() {
        return processMode;
    }

    public void setProcessMode(String processMode) {
        this.processMode = processMode;
    }
}
