package com.tiansu.eam.modules.opestandard.entity;

import java.util.List;

/**
 * @author wujh
 * @description 设备树  设备类别和设备树
 * @create 2017-09-25 15:19
 **/
public class DeviceTreeData<T> {

    public static final String ROOT_PARENT = "0";
    private static final long serialVersionUID = 1L;

    //设备(设备类别)id
    private String id;
    //上级设备（设备类别）
    private String parentId;
    // 设备名称
    private String name;
    // 设备/设备类别编码
    private String code;
    // 类型：设备device  设备类别devCategory
    private String type;
    // 子级
    private List<T> children;

    public DeviceTreeData(String id, String parentId, String name, String type, String code) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.type = type;
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
