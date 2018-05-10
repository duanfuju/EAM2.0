package com.tiansu.eam.modules.act.entity;

/**
 * Created by marsart on 2017/7/27.
 * @author q1210
 * 简单审批任务
 */
public class ApprovalAct {
    //简单审批任务
    private String comment; 	// 任务意见
    private String flag; 		// 意见状态（涉及引擎的flowcondition设置）

    public  String getComment() {
        return comment;
    }

    public void   setComment(String comment) {
        this.comment = comment;
    }

    public String getFlag() {
        return flag;
    }

    public void   setFlag(String flag) {
        this.flag = flag;
    }

}
