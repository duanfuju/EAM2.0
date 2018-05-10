package com.tiansu.eam.modules.sys.entity;

import java.util.List;

/**
 * @author wujh
 * @description 业务字典树型实体类
 * @create 2017-12-01 9:42
 **/
public class DictTreeData<T> {
    public static final String ROOT_PARENT = "0";
    private static final long serialVersionUID = 1L;

    //业务字典id
    private String id;
    //上级业务字典id
    private String parentId;
    // 业务字典名称
    private String name;
    // 业务字典值
    private String value;
    // 业务字典描述
    private String desc;
    // 子级
    private List<T> children;

    public DictTreeData(String id, String parentId, String name, String value, String desc) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.value = value;
        this.desc = desc;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }
}
