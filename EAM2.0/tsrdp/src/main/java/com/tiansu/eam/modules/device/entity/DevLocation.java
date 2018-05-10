package com.tiansu.eam.modules.device.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * Created by wangjl on 2017/8/9.
 * @modifier duanfuju
 * @modifytime 2017/10/17 10:41
 * @modifyDec:
 */
public class DevLocation extends DataEntity<DevLocation> {

    //主键id    和插件ligerui属性冲突
    private String id_key;

    private String loc_id;
    //位置树节点父id
    private String loc_pid;
    //位置编码
    private String loc_code;
    //位置名称
    private String loc_name;
    //位置描述
    private String loc_desc;
    //位置电话
    private String loc_tel;
    //归属部门
    private String loc_dept;
    //空间功能属性
    private String loc_funcprop;
    //空间面积
    private double loc_area;


    //是否有效
    private String loc_status;

    public String getId_key() {
        return id_key;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }

    public String getLoc_id() {
        return loc_id;
    }

    public void setLoc_id(String loc_id) {
        this.loc_id = loc_id;
    }

    public String getLoc_pid() {
        return loc_pid;
    }

    public void setLoc_pid(String loc_pid) {
        this.loc_pid = loc_pid;
    }

    public String getLoc_code() {
        return loc_code;
    }

    public void setLoc_code(String loc_code) {
        this.loc_code = loc_code;
    }

    public String getLoc_name() {
        return loc_name;
    }

    public void setLoc_name(String loc_name) {
        this.loc_name = loc_name;
    }

    public String getLoc_desc() {
        return loc_desc;
    }

    public void setLoc_desc(String loc_desc) {
        this.loc_desc = loc_desc;
    }

    public String getLoc_tel() {
        return loc_tel;
    }

    public void setLoc_tel(String loc_tel) {
        this.loc_tel = loc_tel;
    }

    public String getLoc_dept() {
        return loc_dept;
    }

    public void setLoc_dept(String loc_dept) {
        this.loc_dept = loc_dept;
    }

    public String getLoc_funcprop() {
        return loc_funcprop;
    }

    public void setLoc_funcprop(String loc_funcprop) {
        this.loc_funcprop = loc_funcprop;
    }

    public double getLoc_area() {
        return loc_area;
    }

    public void setLoc_area(double loc_area) {
        this.loc_area = loc_area;
    }

    public String getLoc_status() {
        return loc_status;
    }

    public void setLoc_status(String loc_status) {
        this.loc_status = loc_status;
    }
}
