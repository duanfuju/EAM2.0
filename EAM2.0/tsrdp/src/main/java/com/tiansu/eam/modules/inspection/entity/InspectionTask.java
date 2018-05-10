package com.tiansu.eam.modules.inspection.entity;

import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;
import java.util.List;

/**
 * @author wujh
 * @description 巡检任务
 * @create 2017-10-13 9:32
 **/
public class InspectionTask extends DataEntity<InspectionTask> {

    private static final long serialVersionUID = 1L;

    private String id_key;    // 主键id
    private String task_code;  // 巡检任务编码
    private String route_id;  // 巡检路线id
    private InspectionRoute inspectionRoute;   // 巡检任务所属的巡检路线
    private Date task_time_plan_begin;    // 巡检任务预计开始时间
    private Date task_time_plan_finish;   // 巡检任务预计结束时间
    private Date task_time_begin;   // 巡检任务开始时间
    private Date task_time_finish;  // 巡检任务结束时间
    private String isoverdue;        // 是否逾期
    private double task_totalhour;  // 巡检实际消耗总工时
    private double task_totalhour_plan;  // 巡检任务预计消耗总工时
    private String task_status;      //巡检任务状态
    private String task_processor;   //任务实际巡检人
    private String task_processor_plan;   //巡检任务计划接单人
    private String create_by;           // 创建人
    private Date create_time;           // 创建时间
    private String update_by;           // 更新人
    private Date update_time;           // 创建时间
    private String isdelete;            //是否删除

    private String pstid; 				//流程实例id

    public String getId_key() {
        return id_key;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public Date getTask_time_plan_begin() {
        return task_time_plan_begin;
    }

    public void setTask_time_plan_begin(Date task_time_plan_begin) {
        this.task_time_plan_begin = task_time_plan_begin;
    }

    public Date getTask_time_plan_finish() {
        return task_time_plan_finish;
    }

    public void setTask_time_plan_finish(Date task_time_plan_finish) {
        this.task_time_plan_finish = task_time_plan_finish;
    }

    public double getTask_totalhour_plan() {
        return task_totalhour_plan;
    }

    public void setTask_totalhour_plan(double task_totalhour_plan) {
        this.task_totalhour_plan = task_totalhour_plan;
    }

    public Date getTask_time_begin() {
        return task_time_begin;
    }

    public void setTask_time_begin(Date task_time_begin) {
        this.task_time_begin = task_time_begin;
    }

    public Date getTask_time_finish() {
        return task_time_finish;
    }

    public void setTask_time_finish(Date task_time_finish) {
        this.task_time_finish = task_time_finish;
    }

    public String getIsoverdue() {
        return isoverdue;
    }

    public void setIsoverdue(String isoverdue) {
        this.isoverdue = isoverdue;
    }

    public double getTask_totalhour() {
        return task_totalhour;
    }

    public void setTask_totalhour(double task_totalhour) {
        this.task_totalhour = task_totalhour;
    }

    public String getTask_status() {
        return task_status;
    }

    public void setTask_status(String task_status) {
        this.task_status = task_status;
    }

    public String getTask_processor() {
        return task_processor;
    }

    public void setTask_processor(String task_processor) {
        this.task_processor = task_processor;
    }

    public String getTask_processor_plan() {
        return task_processor_plan;
    }

    public void setTask_processor_plan(String task_processor_plan) {
        this.task_processor_plan = task_processor_plan;
    }

    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }

    public InspectionRoute getInspectionRoute() {
        return inspectionRoute;
    }

    public void setInspectionRoute(InspectionRoute inspectionRoute) {
        this.inspectionRoute = inspectionRoute;
    }

    public String getPstid() {
        return pstid;
    }

    public void setPstid(String pstid) {
        this.pstid = pstid;
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

    public String getTask_code() {
        return task_code;
    }

    public void setTask_code(String task_code) {
        this.task_code = task_code;
    }
}
