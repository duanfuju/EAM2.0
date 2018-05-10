package com.tiansu.eam.modules.act.test;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by marsart on 2017/8/23.
 * 工作流测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations = "classpath:jeesite.properties")
@ContextConfiguration(locations = {"classpath:spring-context.xml","classpath:spring-context-activiti.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public class TaskListTest {
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;

    @Test
    public void testTaskList(){

        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .latestVersion().orderByProcessDefinitionKey().asc().list();

//        List<HistoricTaskInstance> hlist = historyService.createHistoricTaskInstanceQuery().processInstanceId("cf0933fc4890408382adfd8d0670b17f").list();
//
        for ( ProcessDefinition pde : list)
        {
            pde.isSuspended();
            //SystemInfo.out.println("actid:"+hai.getId()+",key:"+hai.getTaskDefinitionKey()+",parent:"+hai.getParentTaskId()+",name:"+hai.getName()+",type:"+hai.getAssignee()+"\n");
        }
    }

    @Test
    public void testEamActModelService() {
        //etService.todoList();
        return;
    }

}
