package com.tiansu.eam.modules.maintain.entity;

import com.tiansu.eam.common.persistence.DataEntity;
import com.tiansu.eam.modules.inspection.entity.*;

import java.util.Date;
import java.util.List;

/**
 * @author caoh
 * @description 保养任务
 * @create 2017-10-26 9:32
 **/
public class MaintainTask extends DataEntity<MaintainTask> {

    private static final long serialVersionUID = 1L;

    private String id_key;    // 主键id
    private String project_id;  // 保养月计划id
    private String task_code;   // 保养任务编码
    private String task_name;   // 保养任务名称
    private String task_type;   // 保养分类

    private String task_mode;   // 保养类型
    private String task_period;   // 保养周期
    private String task_time_plan;   // 保养任务计划时间
    private String task_time_begin;   // 实际开始时间
    private String task_time_finish;   // 实际结束时间
    private String isoverdue;        // 是否逾期
    private String task_device;        // 保养设备
    private String task_status;      //任务状态
    private String task_act_processor;      //实际处理人
    private String task_act_processor_plan;      //计划处理人
    private String task_maintain_code;      //保养内容编码
    private String task_maintain_content;      //保养内容
    private String task_maintain_remark;      //备注
    private String task_fk_flag;      //反馈标识
    private String pstid;      //流程实例id
    private String project_cycle;//周期_

    private String task_result;   //检查结果 0正常 1报修 2异常

    private String task_appearance;   //现象
    private String task_fk_photo;   //反馈照片
    private String task_fk_video;   //反馈视频

    private List<MaintainTool> toolList;  //工器具
    private List<MaintainAttachment> attachmentList;      //备品备件
    private List<MaintainEmp> empList;        //人员工时
    private List<MaintainOther> otherList;  //其他费用

    public String getId_key() {
        return id_key;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getTask_code() {
        return task_code;
    }

    public void setTask_code(String task_code) {
        this.task_code = task_code;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }

    public String getTask_mode() {
        return task_mode;
    }

    public void setTask_mode(String task_mode) {
        this.task_mode = task_mode;
    }

    public String getTask_period() {
        return task_period;
    }

    public void setTask_period(String task_period) {
        this.task_period = task_period;
    }

    public String getTask_time_plan() {
        return task_time_plan;
    }

    public void setTask_time_plan(String task_time_plan) {
        this.task_time_plan = task_time_plan;
    }

    public String getTask_time_begin() {
        return task_time_begin;
    }

    public void setTask_time_begin(String task_time_begin) {
        this.task_time_begin = task_time_begin;
    }

    public String getTask_time_finish() {
        return task_time_finish;
    }

    public void setTask_time_finish(String task_time_finish) {
        this.task_time_finish = task_time_finish;
    }

    public String getIsoverdue() {
        return isoverdue;
    }

    public void setIsoverdue(String isoverdue) {
        this.isoverdue = isoverdue;
    }

    public String getTask_device() {
        return task_device;
    }

    public void setTask_device(String task_device) {
        this.task_device = task_device;
    }

    public String getTask_status() {
        return task_status;
    }

    public void setTask_status(String task_status) {
        this.task_status = task_status;
    }

    public String getTask_act_processor() {
        return task_act_processor;
    }

    public void setTask_act_processor(String task_act_processor) {
        this.task_act_processor = task_act_processor;
    }

    public String getTask_act_processor_plan() {
        return task_act_processor_plan;
    }

    public void setTask_act_processor_plan(String task_act_processor_plan) {
        this.task_act_processor_plan = task_act_processor_plan;
    }

    public String getTask_maintain_code() {
        return task_maintain_code;
    }

    public void setTask_maintain_code(String task_maintain_code) {
        this.task_maintain_code = task_maintain_code;
    }

    public String getTask_maintain_content() {
        return task_maintain_content;
    }

    public void setTask_maintain_content(String task_maintain_content) {
        this.task_maintain_content = task_maintain_content;
    }

    public String getTask_maintain_remark() {
        return task_maintain_remark;
    }

    public void setTask_maintain_remark(String task_maintain_remark) {
        this.task_maintain_remark = task_maintain_remark;
    }

    public String getTask_fk_flag() {
        return task_fk_flag;
    }

    public void setTask_fk_flag(String task_fk_flag) {
        this.task_fk_flag = task_fk_flag;
    }

    public String getPstid() {
        return pstid;
    }

    public void setPstid(String pstid) {
        this.pstid = pstid;
    }

    public String getTask_result() {
        return task_result;
    }

    public void setTask_result(String task_result) {
        this.task_result = task_result;
    }

    public String getTask_appearance() {
        return task_appearance;
    }

    public void setTask_appearance(String task_appearance) {
        this.task_appearance = task_appearance;
    }

    public String getTask_fk_photo() {
        return task_fk_photo;
    }

    public void setTask_fk_photo(String task_fk_photo) {
        this.task_fk_photo = task_fk_photo;
    }

    public String getTask_fk_video() {
        return task_fk_video;
    }

    public void setTask_fk_video(String task_fk_video) {
        this.task_fk_video = task_fk_video;
    }

    public List<MaintainTool> getToolList() {
        return toolList;
    }

    public void setToolList(List<MaintainTool> toolList) {
        this.toolList = toolList;
    }

    public List<MaintainAttachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<MaintainAttachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public List<MaintainEmp> getEmpList() {
        return empList;
    }

    public void setEmpList(List<MaintainEmp> empList) {
        this.empList = empList;
    }

    public List<MaintainOther> getOtherList() {
        return otherList;
    }

    public void setOtherList(List<MaintainOther> otherList) {
        this.otherList = otherList;
    }

    public String getProject_cycle() {
        return project_cycle;
    }

    public void setProject_cycle(String project_cycle) {
        this.project_cycle = project_cycle;
    }
}
