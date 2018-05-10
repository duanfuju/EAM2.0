package com.tiansu.eam.modules.maintain.entity;

/**
 * @author caoh
 * @description 保养工单状态
 * @create 2017-11-01 16:34
 **/
public enum MaintainStatusEnum {
//    （0待确认 1处理中 2已完成 3申请转单 4申请挂起  5已挂起 6申请撤销 7已撤销 8申请解挂）
    PENDING_CONF("0"), PENDING_PROCESS("1") , PENDING_FINISH("2"),PENDING_TRANSFER("3") ,PENDING_HANGUP("4"), HANGUP("5") ,PENDING_CANCEL("6"),CANCELED("7"),PENDING_UN_HANGUP("8");

    private String value;

    MaintainStatusEnum(String value){
        this.value = value;
    }

    public String value(){
        return value;
    }


}
