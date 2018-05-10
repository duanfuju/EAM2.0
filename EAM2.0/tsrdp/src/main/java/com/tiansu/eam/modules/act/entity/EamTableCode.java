package com.tiansu.eam.modules.act.entity;

/**
 * @creator caoh
 * @createtime 2017-12-1 11:31
 * @description:工作流标题关联业务编码枚举类
 **/
public enum  EamTableCode {
    MAINTAIN_TASK("eam_maintain_task","task_code"),
    FAULT_ORDER("eam_fault_order","order_code"),
    INSPECTION_TASK("eam_inspection_task","task_code"),
    OPERATION_WORK("eam_operation_work", "operationwork_code"),
    OPERATION_LIBRARY("eam_operation_library", "library_code"),
    INSPECTION_ROUTE("eam_inspection_route", "route_code"),
    OPERATION_ORDER("eam_operation_orders", "order_code");




    private String tablename ;
    private String codename ;

    private EamTableCode( String tablename , String codename ){
        this.tablename = tablename ;
        this.codename = codename ;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

}
