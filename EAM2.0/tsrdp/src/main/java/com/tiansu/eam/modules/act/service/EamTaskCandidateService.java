package com.tiansu.eam.modules.act.service;

import org.activiti.engine.delegate.DelegateTask;

import java.util.List;

/**
 * @creator caoh
 * @createtime 2017-10-10 10:07
 * @description:动态设置流程节点参与者
 */

public class EamTaskCandidateService {

    /**
     * @creator caoh
     * @createtime 2017-10-10 10:45
     * @description: 设置参与用户users
     * @param delegateTask 监听器的DelegateTask对象
     * @param users 用户id数组
     */
    public static void setCandidate(DelegateTask delegateTask,List<String> users) {

        //设置当前节点的参与者（在任务创建之前）
        delegateTask.addCandidateUsers(users);  //指定任务候选用户
        //delegateTask.addCandidateGroups(groups);//指定任务候选组

    }

}
