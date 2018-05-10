package com.tiansu.eam.modules.base.entity;

import com.thinkgem.jeesite.common.persistence.TreeEntity;

/**
 * @author wangjl
 * @description 树结构动态生成entity
 * @create 2017-09-16 10:59
 **/
public class BaseTree extends TreeEntity<BaseTree> {

    private static final long serialVersionUID = 1L;
    private BaseTree parent;		// 父级编号
    private String parentIds;		// 所有父级编号
    private String name;		// 名称
    private Integer sort;		// 排序


    public BaseTree() {
        super();
    }

    public BaseTree(String id){
        super(id);
    }

    @Override
    public BaseTree getParent() {
        return parent;
    }

    @Override
    public void setParent(BaseTree parent) {
        this.parent = parent;
    }

    @Override
    public String getParentIds() {
        return parentIds;
    }

    @Override
    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Integer getSort() {
        return sort;
    }

    @Override
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getParentId() {
        return parent != null && parent.getId() != null ? parent.getId() : "0";
    }
}
