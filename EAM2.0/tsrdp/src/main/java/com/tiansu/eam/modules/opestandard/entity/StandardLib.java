package com.tiansu.eam.modules.opestandard.entity;

import com.tiansu.eam.common.persistence.DataEntity;
import com.tiansu.eam.modules.sys.entity.User;

import java.util.Date;
import java.util.List;

/**
 * @author zhangww
 * @description 标准库模版
 * @create 2017-08-31 14:20
 **/
public class StandardLib extends DataEntity<StandardLib>{
    private String pstid;
    private String id_key;
    private String library_code;
    private String library_name;
   // private String library_desc;运维标准库描述
    private String attach_id;//附件
    private User approve_by;//审批人
    private String approve_reason;
    private Date approve_time;
    private String approve_status;//审批状态
    private String library_status;//状态
    private String devicename;
    private String deviceids;
    private List<StandardDevice> standardDevices;//设备
    private List<StandardFailure> standardFailures;//缺陷故障库
    private List<StandardFault> standardFaults;//检修
    private List<StandardMaintain> standardMaintains;//保养标准
    private List<StandardOpe> standardOpes;//运行标准
    private List<StandardPatrol> standardPatrols;//巡检
    private List<StandardSafety> standardSafetys;//安全标准
    public String getId_key() {
        return id_key;
    }

    public String getPstid() {
        return pstid;
    }

    public void setPstid(String pstid) {
        this.pstid = pstid;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }

    public String getLibrary_code() {
        return library_code;
    }

    public void setLibrary_code(String library_code) {
        this.library_code = library_code;
    }

    public String getLibrary_name() {
        return library_name;
    }

    public void setLibrary_name(String library_name) {
        this.library_name = library_name;
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    public String getDeviceids() {
        return deviceids;
    }

    public void setDeviceids(String deviceids) {
        this.deviceids = deviceids;
    }

    public String getAttach_id() {
        return attach_id;
    }

    public void setAttach_id(String attach_id) {
        this.attach_id = attach_id;
    }

    public User getApprove_by() {
        return approve_by;
    }

    public void setApprove_by(User approve_by) {
        this.approve_by = approve_by;
    }

    public String getApprove_reason() {
        return approve_reason;
    }

    public void setApprove_reason(String approve_reason) {
        this.approve_reason = approve_reason;
    }

    public Date getApprove_time() {
        return approve_time;
    }

    public void setApprove_time(Date approve_time) {
        this.approve_time = approve_time;
    }

    public String getApprove_status() {
        return approve_status;
    }

    public void setApprove_status(String approve_status) {
        this.approve_status = approve_status;
    }

    public String getLibrary_status() {
        return library_status;
    }

    public void setLibrary_status(String library_status) {
        this.library_status = library_status;
    }

    public List<StandardDevice> getStandardDevices() {
        return standardDevices;
    }

    public void setStandardDevices(List<StandardDevice> standardDevices) {
        this.standardDevices = standardDevices;
    }

    public List<StandardFailure> getStandardFailures() {
        return standardFailures;
    }

    public void setStandardFailures(List<StandardFailure> standardFailures) {
        this.standardFailures = standardFailures;
    }

    public List<StandardFault> getStandardFaults() {
        return standardFaults;
    }

    public void setStandardFaults(List<StandardFault> standardFaults) {
        this.standardFaults = standardFaults;
    }

    public List<StandardMaintain> getStandardMaintains() {
        return standardMaintains;
    }

    public void setStandardMaintains(List<StandardMaintain> standardMaintains) {
        this.standardMaintains = standardMaintains;
    }

    public List<StandardOpe> getStandardOpes() {
        return standardOpes;
    }

    public void setStandardOpes(List<StandardOpe> standardOpes) {
        this.standardOpes = standardOpes;
    }

    public List<StandardPatrol> getStandardPatrols() {
        return standardPatrols;
    }

    public void setStandardPatrols(List<StandardPatrol> standardPatrols) {
        this.standardPatrols = standardPatrols;
    }

    public List<StandardSafety> getStandardSafetys() {
        return standardSafetys;
    }

    public void setStandardSafetys(List<StandardSafety> standardSafetys) {
        this.standardSafetys = standardSafetys;
    }
}
