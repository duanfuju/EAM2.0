package com.tiansu.eam.modules.inspection.service;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * @author wujh
 * @description 巡检任务选择按责任人派单，则直接分配到具体的用户
 * @create 2017-10-16 16:04
 **/
public class ActInspectionManualAfter implements JavaDelegate {
    /**
     * 实现JavaDelegate接口，使用其中的execute方法 由于要放入流程定义中，所以要实现可序列话接口
     *
     */
    private static final long serialVersionUID = 5593437463482732772L;

    // 从

    // 流程变量
    private Expression disp_type;

    //重写委托的提交方法
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

    }

}
