package com.tiansu.eam.modules.act.test;

import com.tiansu.eam.modules.act.service.EamProcessService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.annotation.Resource;

/**
 * Created by marsart on 2017/8/24.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations = "classpath:jeesite.properties")
@ContextConfiguration(locations = {"classpath:spring-context.xml","classpath:spring-context-activiti.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class ProcessInstanceTest {
    @Resource
    EamProcessService eamProcessService;



    @Test
    public void testWorkFlow ()
    {
        //启动流程，启动成功返回流程实例id,启动失败返回空
        String processInstanceId = eamProcessService.startProcess("","table_name","businessId","工单流程");



    }

}
