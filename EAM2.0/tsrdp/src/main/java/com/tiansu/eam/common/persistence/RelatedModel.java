package com.tiansu.eam.common.persistence;

/**
 * @author wangjl
 * @description 数据是否被占用model
 * @create 2017-11-06 11:09
 **/
public class RelatedModel {
    private String relatedTable;

    private String relatedColumn;

    private String whereCond;

    public RelatedModel(String relatedTable,String relatedColumn,String whereCond){
        this.relatedTable = relatedTable;
        this.relatedColumn = relatedColumn;
        this.whereCond = whereCond;
    }

    public String getRelatedTable() {
        return relatedTable;
    }

    public String getRelatedColumn() {
        return relatedColumn;
    }

    public String getWhereCond() {
        return whereCond;
    }
}
