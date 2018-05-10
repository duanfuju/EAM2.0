package com.tiansu.eam.modules.maintain.service;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.modules.maintain.dao.MaintainTaskDao;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

import java.util.*;

/**
 * @author caoh
 * @description
 * @create 2017-11-08 14:47
 **/
public class MaintainDispatchByDutyService implements JavaDelegate {

    // 获取任务实体表的dao层
    private MaintainTaskDao maintainTaskDao = SpringContextHolder.getBean(MaintainTaskDao.class);

    /**
     * @creator caoh
     * @createtime 2017-11-8 16:27
     * @description: 根据排班表和专业派单，如果派不到人，直接根据责任人派单
     * @param execution
     * @throws Exception
     */
    @Override
    public void execute(DelegateExecution execution) throws Exception {

        System.out.println("serviceTask已经执行已经执行！");

        String piid = execution.getProcessInstanceId();
        // 根据流程id获取当前任务
        Map map = maintainTaskDao.getMaintainTaskByPiid(piid);
        // 当前巡检任务期望解决时间
        String task_time_plan = map.get("task_time_plan").toString();

        //获取当前任务中所有设备的专业，取并集
        List<String> task_majors = maintainTaskDao.getTaskMajorByPiid(piid);
        //查询当前值班人员
        List<Map> scheduals = getCurrentSchedualEmps(task_time_plan);
        //以字符串形式存入task表中
        String processor_plan = "";
        List<String> users = new ArrayList<>();
        for(Map mp:scheduals){
            List<String> emp_major = Arrays.asList(mp.get("emp_major").toString().split(","));
            if(emp_major.containsAll(task_majors)){
                users.add(mp.get("schedual_emp").toString());
                processor_plan += mp.get("schedual_emp").toString()+",";
            }
        }
        //如果根据排班表和专业筛选之后人员为空，则默认派给责任人
        if(users.size()==0){
            processor_plan=getProcessorByDuty(map.get("project_id").toString());
        }
        Map param = new HashMap();
        param.put("processor_plan",processor_plan);
        param.put("pstid",piid);
        maintainTaskDao.updateMaintainTaskProcessorPlan(param);

    }

    /**
     * 根据排班表，获取当前值班人员
     * @return
     */
    private List<Map> getCurrentSchedualEmps(String time_plan) {
        List<Map> schedualList = maintainTaskDao.getScheduleByTaskDate(time_plan);
        return schedualList;
    }

    private String getProcessorByDuty(String project_id){
        String emp_id = maintainTaskDao.getProcessorByDuty(project_id);
        return emp_id;
    }
}
