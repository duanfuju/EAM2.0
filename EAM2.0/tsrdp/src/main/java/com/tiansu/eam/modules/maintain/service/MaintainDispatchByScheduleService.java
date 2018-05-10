package com.tiansu.eam.modules.maintain.service;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.modules.maintain.dao.MaintainTaskDao;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import java.util.*;

/**
 * @author caoh
 * @description
 * @create 2017-11-08 14:47
 **/
public class MaintainDispatchByScheduleService implements JavaDelegate {

    // 获取任务实体表的dao层
    private MaintainTaskDao maintainTaskDao = SpringContextHolder.getBean(MaintainTaskDao.class);

    /**
     * @creator caoh
     * @createtime 2017-11-8 16:27
     * @description: 根据责任人直接派单
     * @param execution
     * @throws Exception
     */
    @Override
    public void execute(DelegateExecution execution) throws Exception {

        System.out.println("serviceTask已经执行已经执行！");

        String piid = execution.getProcessInstanceId();
        // 根据流程id获取当前任务
        Map map = maintainTaskDao.getMaintainTaskByPiid(piid);


        String processor_plan = getProcessorByDuty(map.get("project_id").toString());

        Map param = new HashMap();
        param.put("processor_plan",processor_plan);
        param.put("pstid",piid);
        maintainTaskDao.updateMaintainTaskProcessorPlan(param);

    }

    private String getProcessorByDuty(String project_id){
        String emp_id = maintainTaskDao.getProcessorByDuty(project_id);
        return emp_id;
    }
}
