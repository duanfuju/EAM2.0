package com.tiansu.eam.modules.act.service;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.material.entity.TestMaterial;
import com.thinkgem.jeesite.modules.material.service.TestMaterialService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**@creator Douglas
 * @createtime 2017-9-15 16:35
 * @description: TaskService获取流程参数
 **/

@Service
@Transactional
public class HelloService implements TaskListener {

    private static final long serialVersionUID = 1L;


    private Expression materialcode;
    private Expression deptApproved;

    TestMaterialService testMaterialService = SpringContextHolder.getBean(TestMaterialService.class);
    //EamTaskCandidateService eamTaskCandidateService = SpringContextHolder.getBean(EamTaskCandidateService.class);

    public void notify(DelegateTask delegateTask) {
        TestMaterial a = new TestMaterial();

        //插入业务数据
        a.setMaterialcode("AAA");
        testMaterialService.insert(a);

        //动态设置当前节点的参与者（在任务创建之前）

        List<String> emps = new ArrayList<>();
        emps.add("dog");
        emps.add("emagine");

        EamTaskCandidateService.setCandidate(delegateTask,emps);
        //查询当前任务的参与者
        Set map = delegateTask.getCandidates();

        //delegateTask.addCandidateUsers(emps);//完成多处理人的指定


        String code = (String) materialcode.getValue(delegateTask);
        //String approve = (String) deptApproved.getValue(delegateTask);
        System.out.println(code);
        //SystemInfo.out.println(approve);

    }

}
