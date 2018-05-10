package com.tiansu.eam.modules.maintain.entity;/**
 * Created by suven on 2017/11/2.
 */

import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;
import java.util.List;

/**
 * 保养月计划表
 *
 * @author suven suven
 * @create 2017/11/2
 */
public class MaintainProjectSub extends DataEntity<MaintainProjectSub> {
    private String pstid;//流程
    private String id_key;
    private String project_code;//	保养计划编码
    private String  project_name;//	名称
    private String project_mode;//	类型
    private String project_type;//	分类
    private String project_empid;//	保养人
    private String project_sp_empid;//	审批人
    private Date project_sp_time;//	审批时间
    private String  project_cycleyear;//	保养年份
    private String project_cycle;//	保养周期
    private String  project_stime;//	启动时间
    private String  project_dtime;//	结束时间
    private String project_status;//	审批状态
    private String operation_code;//	标准工作编码
    private String  project_isoperation;//	是否引入标准工作
    private String project_bm;//	保养部门
    private String status;//	状态
    private String project_reason;//	退回理由
    private String isdelete;//	是否删除（0 否 1是）
    private String project_period;
    private String project_device;//设备id(,拼接)
    private String project_devname;//设备name
    private String project_producedate ;//月计划的生成月份,2017-11
    private String projectyear_id;//关联年计划
    private String projectset_id;  //关联保养设置

    List<MaintSubProc> maintSubProcs;
    List<MaintSubSafe> maintSubSafes;
    List<MaintSubTools> maintSubTools;
    List<MaintSubSpareparts> maintSubSpareparts;
    List<MaintsubPerson> maintsubPersons;
    List<MaintsubOthers> maintsubOthers;
    public String getPstid() {
        return pstid;
    }

    public void setPstid(String pstid) {
        this.pstid = pstid;
    }

    public String getId_key() {
        return id_key;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
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

    public String getProject_sp_empid() {
        return project_sp_empid;
    }

    public void setProject_sp_empid(String project_sp_empid) {
        this.project_sp_empid = project_sp_empid;
    }

    public String getProject_cycleyear() {
        return project_cycleyear;
    }

    public void setProject_cycleyear(String project_cycleyear) {
        this.project_cycleyear = project_cycleyear;
    }

    public String getProject_cycle() {
        return project_cycle;
    }

    public void setProject_cycle(String project_cycle) {
        this.project_cycle = project_cycle;
    }

    public String getProject_status() {
        return project_status;
    }

    public void setProject_status(String project_status) {
        this.project_status = project_status;
    }

    public String getOperation_code() {
        return operation_code;
    }

    public void setOperation_code(String operation_code) {
        this.operation_code = operation_code;
    }

    public String getProject_isoperation() {
        return project_isoperation;
    }

    public void setProject_isoperation(String project_isoperation) {
        this.project_isoperation = project_isoperation;
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

    public String getProject_reason() {
        return project_reason;
    }

    public void setProject_reason(String project_reason) {
        this.project_reason = project_reason;
    }

    public Date getProject_sp_time() {
        return project_sp_time;
    }

    public void setProject_sp_time(Date project_sp_time) {
        this.project_sp_time = project_sp_time;
    }

    public String getProject_stime() {
        return project_stime;
    }

    public void setProject_stime(String project_stime) {
        this.project_stime = project_stime;
    }

    public String getProject_dtime() {
        return project_dtime;
    }

    public void setProject_dtime(String project_dtime) {
        this.project_dtime = project_dtime;
    }

    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }

    public String getProject_period() {
        return project_period;
    }

    public void setProject_period(String project_period) {
        this.project_period = project_period;
    }

    public String getProject_device() {
        return project_device;
    }

    public void setProject_device(String project_device) {
        this.project_device = project_device;
    }

    public String getProject_devname() {
        return project_devname;
    }

    public void setProject_devname(String project_devname) {
        this.project_devname = project_devname;
    }

    public List<MaintSubProc> getMaintSubProcs() {
        return maintSubProcs;
    }

    public void setMaintSubProcs(List<MaintSubProc> maintSubProcs) {
        this.maintSubProcs = maintSubProcs;
    }

    public List<MaintSubSafe> getMaintSubSafes() {
        return maintSubSafes;
    }

    public void setMaintSubSafes(List<MaintSubSafe> maintSubSafes) {
        this.maintSubSafes = maintSubSafes;
    }

    public List<MaintSubTools> getMaintSubTools() {
        return maintSubTools;
    }

    public void setMaintSubTools(List<MaintSubTools> maintSubTools) {
        this.maintSubTools = maintSubTools;
    }

    public List<MaintSubSpareparts> getMaintSubSpareparts() {
        return maintSubSpareparts;
    }

    public void setMaintSubSpareparts(List<MaintSubSpareparts> maintSubSpareparts) {
        this.maintSubSpareparts = maintSubSpareparts;
    }

    public List<MaintsubPerson> getMaintsubPersons() {
        return maintsubPersons;
    }

    public void setMaintsubPersons(List<MaintsubPerson> maintsubPersons) {
        this.maintsubPersons = maintsubPersons;
    }

    public List<MaintsubOthers> getMaintsubOthers() {
        return maintsubOthers;
    }

    public void setMaintsubOthers(List<MaintsubOthers> maintsubOthers) {
        this.maintsubOthers = maintsubOthers;
    }

    public String getProject_producedate() {
        return project_producedate;
    }

    public void setProject_producedate(String project_producedate) {
        this.project_producedate = project_producedate;
    }

    public String getProjectyear_id() {
        return projectyear_id;
    }

    public void setProjectyear_id(String projectyear_id) {
        this.projectyear_id = projectyear_id;
    }

    public String getProjectset_id() {
        return projectset_id;
    }

    public void setProjectset_id(String projectset_id) {
        this.projectset_id = projectset_id;
    }
}
