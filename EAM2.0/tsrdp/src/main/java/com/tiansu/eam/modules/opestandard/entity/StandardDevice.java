package com.tiansu.eam.modules.opestandard.entity;

/**
 * @author zhangww
 * @description 标准库设备中间表
 * @create 2017-08-31 16:07
 **/
public class StandardDevice {
    private String id;
    private String library_id;//标准库id
    private String device_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibrary_id() {
        return library_id;
    }

    public void setLibrary_id(String library_id) {
        this.library_id = library_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
}
