package com.tiansu.eam.modules.maintain.entity;
import com.tiansu.eam.common.persistence.DataEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
/**
 * @creator duanfuju
 * @createtime 2017/11/15 16:09
 * @description:
 * 保养设置表(用于导入)
 */
public class MaintainProjectInfForImport extends DataEntity<MaintainProjectInfForImport> {
    private String id_key;
    private String project_code;//	保养计划编码
    private String project_name;//	名称
    private String project_mode;//	类型
    private String project_type;//	分类
    private String project_empid;//	保养人
    private Date project_stime;//	启动时间
    private String project_period;//保养周期类型
    private String project_cycle;//	保养周期
    private String project_bm;//	保养部门
    private String status;//	状态
    private String maint_check_code;
    private String maint_check_cont;//	保养验收内容
    private String maint_check_mark;//	保养验收备注
    private String dev_code;//设备id
    private String project_content;//保养内容
    List<MaintainProjectInfDevice> maintainProjectInfDevices;
    List<MaintainProjectInfContent> maintainProjectInfContents;

    /**
     * @creator duanfuju
     * @createtime 2017/11/15 17:28
     * @description: 
     * 设置设备数据
     */
    public void setDeviceData(){
        if(dev_code!=null){
            maintainProjectInfDevices =new ArrayList<>();
            if(id.length()>0||(!id.equals(""))||id!=null){
                String[] dev_code_arr=dev_code.split(",");
                for (int i = 0; i <dev_code_arr.length ; i++) {
                    MaintainProjectInfDevice maintainProjectInfDevice = new MaintainProjectInfDevice();
                    maintainProjectInfDevice.setDev_id(dev_code_arr[i]);
                    maintainProjectInfDevice.setProject_id(id);
                    maintainProjectInfDevice.preInsert();
                    maintainProjectInfDevices.add(maintainProjectInfDevice);
                }
            }
        }
    }

    /**
     * @creator duanfuju
     * @createtime 2017/11/15 17:29
     * @description: 
     * 保养内容数据
     */
    public void setContentData(){
        if(project_content!=null){
            maintainProjectInfContents =new ArrayList<>();
            if(id!=null){
                MaintainProjectInfContent maintainProjectInfContent=new MaintainProjectInfContent();
                maintainProjectInfContent.setMaintain_content(project_content);
                maintainProjectInfContent.setProject_id(id);
                maintainProjectInfContent.preInsert();
                maintainProjectInfContents.add(maintainProjectInfContent);
            }
        }
    }

    public String getId_key() {
        return id_key;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }

    public List<MaintainProjectInfDevice> getMaintainProjectInfDevices() {
        return maintainProjectInfDevices;
    }

    public void setMaintainProjectInfDevices(List<MaintainProjectInfDevice> maintainProjectInfDevices) {
        this.maintainProjectInfDevices = maintainProjectInfDevices;
    }

    public List<MaintainProjectInfContent> getMaintainProjectInfContents() {
        return maintainProjectInfContents;
    }

    public void setMaintainProjectInfContents(List<MaintainProjectInfContent> maintainProjectInfContents) {
        this.maintainProjectInfContents = maintainProjectInfContents;
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

    public Date getProject_stime() {
        return project_stime;
    }

    public void setProject_stime(Date project_stime) {
        this.project_stime = project_stime;
    }

    public String getProject_period() {
        return project_period;
    }

    public void setProject_period(String project_period) {
        this.project_period = project_period;
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

    public String getDev_code() {
        return dev_code;
    }

    public void setDev_code(String dev_code) {
        this.dev_code = dev_code;
    }

    public String getProject_content() {
        return project_content;
    }

    public void setProject_content(String project_content) {
        this.project_content = project_content;
    }
}
