package com.tiansu.eam.modules.maintain.entity;/**
 * Created by suven on 2017/11/2.
 */

import com.tiansu.eam.common.persistence.DataEntity;

import java.util.List;

/**
 * 保养设置表
 *
 * @author suven suven
 * @create 2017/11/2
 */
public class MaintainProjectInf extends DataEntity<MaintainProjectInf> {
    private String id_key;
    private String project_code;//	保养计划编码
    private String project_name;//	名称
    private String project_mode;//	类型
    private String project_type;//	分类
    private String project_empid;//	保养人
    private String project_stime;//	启动时间
    private String project_period;//保养周期类型
    private String project_cycle;//	保养周期
    private String project_bm;//	保养部门
    private String status;//	状态
    private String maint_check_code;
    private String maint_check_cont;//	保养验收内容
    private String maint_check_mark;//	保养验收备注
    private String create_by;//	创建者
    private String create_time;//	创建时间
    private String update_by;//	更新者
    private String update_time;//	更新时间
    private String isdelete;//	是否删除（0 否 1是）
    private String field1;//	预留字段1
    private String field2;//	预留字段2
    private List<MaintainProjectInfContent> maintainProjectInfContentList;
    private String  dev_id;

    public List<MaintainProjectInfContent> getMaintainProjectInfContentList() {
        return maintainProjectInfContentList;
    }

    public void setMaintainProjectInfContentList(List<MaintainProjectInfContent> maintainProjectInfContentList) {
        this.maintainProjectInfContentList = maintainProjectInfContentList;
    }

    public String getId_key() {
        return id_key;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }

    public String getDev_id() {
        return dev_id;
    }

    public void setDev_id(String dev_id) {
        this.dev_id = dev_id;
    }

    public String getProject_period() {
        return project_period;
    }

    public void setProject_period(String project_period) {
        this.project_period = project_period;
    }
    public String getProject_code() {
        return project_code;
    }

    public void setProject_code(String project_code) {
        this.project_code = project_code;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getProject_mode() {
        return project_mode;
    }

    public void setProject_mode(String project_mode) {
        this.project_mode = project_mode;
    }

    public String getProject_type() {
        return project_type;
    }

    public void setProject_type(String project_type) {
        this.project_type = project_type;
    }

    public String getProject_empid() {
        return project_empid;
    }

    public void setProject_empid(String project_empid) {
        this.project_empid = project_empid;
    }

    public String getProject_stime() {
        return project_stime;
    }

    public void setProject_stime(String project_stime) {
        this.project_stime = project_stime;
    }

    public String getProject_cycle() {
        return project_cycle;
    }

    public void setProject_cycle(String project_cycle) {
        this.project_cycle = project_cycle;
    }

    public String getProject_bm() {
        return project_bm;
    }

    public void setProject_bm(String project_bm) {
        this.project_bm = project_bm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMaint_check_code() {
        return maint_check_code;
    }

    public void setMaint_check_code(String maint_check_code) {
        this.maint_check_code = maint_check_code;
    }

    public String getMaint_check_cont() {
        return maint_check_cont;
    }

    public void setMaint_check_cont(String maint_check_cont) {
        this.maint_check_cont = maint_check_cont;
    }

    public String getMaint_check_mark() {
        return maint_check_mark;
    }

    public void setMaint_check_mark(String maint_check_mark) {
        this.maint_check_mark = maint_check_mark;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_by() {
        return update_by;
    }

    public void setUpdate_by(String update_by) {
        this.update_by = update_by;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }
}
