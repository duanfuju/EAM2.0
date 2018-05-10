package com.tiansu.eam.modules.act.entity;

/**
 * Created by marsart on 2017/8/10.
 * 一般业务
 */
public class EamBusiness {
    //流程信息
    private String procInsId; 	// 流程实例ID 对应ProcessInstance
    private String procDefId; 	// 流程定义ID 对应ProcessDefine
    private String procDefKey; 	// 流程定义Key（流程定义标识）

    private String owner;        //业务归属人
    private String operator;     //当前处理人
    private String taskDefKey;   //当前处理环节
    private String taskId;       //

}
