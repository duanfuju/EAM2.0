package com.tiansu.eam.interfaces.act.service;

import com.thinkgem.jeesite.common.utils.StringUtils;
import com.tiansu.eam.common.EAMConsts;
import org.activiti.engine.EngineServices;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.NativeTaskQuery;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangr
 * @description 流程业务
 * @create 2017-10-23 下午 6:47
 */
@Service
@Transactional(readOnly = true)
public class TaskRestService {
    private Logger logger = LoggerFactory.getLogger(TaskRestService.class.getName());

    @Autowired
    private TaskService taskService;

    @Autowired
    EngineServices processEngine;

    /**
     * @return
     * @creator wangr
     * @createtime 2017-10-24 09:16
     * @description: 我的代办任务列表
     * @modifier wangr
     * @modifytime 2017/11/27 上午 9:40
     * @modifyDec: 更改工单排序  只用报修时间倒序排序
     */
    public Map<String, Object> todoList(Map param) {

        //流程定义key
        String processDefKey = param.get("processDefKey").toString();
        String userId = param.get("userId").toString();
        List<String> status = (List<String>) param.get("status");

        StringBuffer sql = new StringBuffer();
        NativeTaskQuery taskQuery = taskService.createNativeTaskQuery();
        //组装数据返回
        Map<String, Object> map = new HashMap();
        //工单流程
        if (EAMConsts.FAULT_ORDER_FLOWDEF.equals(processDefKey)) {
            //自定义sql
            sql.append(" SELECT DISTINCT RES.*,a.* FROM eam_fault_order a ");
            sql.append(" LEFT JOIN ACT_RU_TASK RES ON RES.PROC_INST_ID_ = a.pstid ");
            sql.append(" LEFT JOIN ACT_RU_IDENTITYLINK I ON I.TASK_ID_ = RES.ID_ ");
            sql.append(" LEFT JOIN ACT_RE_PROCDEF P ON P.ID_=RES.PROC_DEF_ID_ ");
            sql.append(" WHERE a.isdelete != '1' AND RES.SUSPENSION_STATE_ = '1' ");
            sql.append(" AND ( RES.ASSIGNEE_ = #{userId} or (RES.ASSIGNEE_ is null  ");
            sql.append(" and ( I.USER_ID_ = #{userId} or I.GROUP_ID_ IN  ");
            sql.append(" (select G.roleno from ioms_roleuser G where G.loginname = #{userId})))) ");
            sql.append(" AND P.KEY_ = " + "'" + processDefKey + "'");
            sql.append(" AND a.order_status IN ( ");
            sql.append(StringUtils.join(status.toArray(), ","));
            sql.append(" ) ");

            taskQuery.sql(sql.toString()).parameter("userId", userId).parameter(
                    "orderBy", "create_time desc");
            logger.info("工单查询userId：" + userId + " --sql:" + sql.toString());
        } else if (EAMConsts.INSPECTION_FLOWDEF.equals(processDefKey)) {
            //巡检流程
            sql.append(" SELECT DISTINCT RES.*,a.* FROM eam_inspection_task a ");
            sql.append(" LEFT JOIN ACT_RU_TASK RES ON RES.PROC_INST_ID_ = a.pstid ");
            sql.append(" LEFT JOIN ACT_RU_IDENTITYLINK I ON I.TASK_ID_ = RES.ID_ ");
            sql.append(" LEFT JOIN ACT_RE_PROCDEF P ON P.ID_=RES.PROC_DEF_ID_ ");
            sql.append(" WHERE a.isdelete != '1' AND RES.SUSPENSION_STATE_ = '1' ");
            sql.append(" AND ( RES.ASSIGNEE_ = #{userId} or (RES.ASSIGNEE_ is null  ");
            sql.append(" and ( I.USER_ID_ = #{userId} or I.GROUP_ID_ IN  ");
            sql.append(" (select G.roleno from ioms_roleuser G where G.loginname = #{userId})))) ");
            sql.append(" AND P.KEY_ = " + "'" + processDefKey + "'");

            String isoverdue = param.get("isoverdue").toString();
            if ("0".equals(isoverdue)) {
                sql.append(" AND a.task_status IN ( ");
                sql.append(StringUtils.join(status.toArray(), ","));
                sql.append(" ) ");
                sql.append(" AND getdate() BETWEEN a.task_time_plan_begin AND task_time_plan_finish ");
            } else {
                sql.append(" AND a.task_status !='2' ");
                sql.append(" AND getdate() > task_time_plan_finish ");
            }

            taskQuery.sql(sql.toString()).parameter("userId", userId)
                    .parameter("isoverdue", isoverdue)
                    .parameter("orderBy", "task_time_plan_begin desc");
            logger.info("巡检查询userId：" + userId + " --sql:" + sql.toString());
        } else if (EAMConsts.MAINTAIN_FLOW.equals(processDefKey)) {
            sql.append(" SELECT DISTINCT RES.*,a.* FROM eam_maintain_task a ");
            sql.append(" LEFT JOIN ACT_RU_TASK RES ON RES.PROC_INST_ID_ = a.pstid ");
            sql.append(" LEFT JOIN ACT_RU_IDENTITYLINK I ON I.TASK_ID_ = RES.ID_ ");
            sql.append(" LEFT JOIN ACT_RE_PROCDEF P ON P.ID_=RES.PROC_DEF_ID_ ");
            sql.append(" WHERE RES.SUSPENSION_STATE_ = '1' ");
            sql.append(" AND ( RES.ASSIGNEE_ = #{userId} or (RES.ASSIGNEE_ is null  ");
            sql.append(" and ( I.USER_ID_ = #{userId} or I.GROUP_ID_ IN  ");
            sql.append(" (select G.roleno from ioms_roleuser G where G.loginname = #{userId})))) ");
            sql.append(" AND P.KEY_ = " + "'" + processDefKey + "'");
            sql.append(" AND a.task_status IN ( ");
            sql.append(StringUtils.join(status.toArray(), ","));
            sql.append(" ) ");
            sql.append(" AND getdate() > a.task_time_plan ");
            taskQuery.sql(sql.toString()).parameter("userId", userId)
                    .parameter("orderBy", "task_time_plan desc");
            logger.info("保养查询userId：" + userId + " --sql:" + sql.toString());
        }
        if (StringUtils.isBlank(sql.toString())) {
            map.put("success", false);
            return map;
        }
        //设置分页数据
        int start = Integer.parseInt(param.get("start").toString());
        int length = Integer.parseInt(param.get("length").toString());

        //获取流程id
        List<Task> taskList = taskQuery.listPage(start, length);
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < taskList.size(); i++) {
            list.add(taskList.get(i).getProcessInstanceId());
        }


        if (null == list || list.size() == 0) {
            map.put("recordsFiltered", 0);
            map.put("recordsTotal", 0);
            map.put("data", list);
        } else {
            map.put("recordsFiltered", taskQuery.sql("select count(*) from (" + sql + ")" + "RES").count());
            map.put("recordsTotal", taskQuery.sql("select count(*) from (" + sql + ")" + "RES").count());
            map.put("data", list);
            map.put("task", taskList);
        }
        map.put("success", true);
        return map;
    }

    /**
     * 获取任务
     *
     * @param pstid 流程ID
     */
    public Task getTask(String pstid) {
        return taskService.createTaskQuery().processInstanceId(pstid).singleResult();
    }

    /**
     * 获取当前流程的下一个节点
     *
     * @param procInstanceId
     * @return
     */
    public String getNextNode(String procInstanceId) {
        // 1、首先是根据流程ID获取当前任务：
        List<Task> tasks = processEngine.getTaskService().createTaskQuery().processInstanceId(procInstanceId).list();
        String nextId = "";
        for (Task task : tasks) {
            RepositoryService rs = processEngine.getRepositoryService();
            // 2、然后根据当前任务获取当前流程的流程定义，然后根据流程定义获得所有的节点：
            ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) rs)
                    .getDeployedProcessDefinition(task.getProcessDefinitionId());
            List<ActivityImpl> activitiList = def.getActivities(); // rs是指RepositoryService的实例
            // 3、根据任务获取当前流程执行ID，执行实例以及当前流程节点的ID：
            String excId = task.getExecutionId();
            RuntimeService runtimeService = processEngine.getRuntimeService();
            ExecutionEntity execution = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(excId)
                    .singleResult();
            String activitiId = execution.getActivityId();
            // 4、然后循环activitiList
            // 并判断出当前流程所处节点，然后得到当前节点实例，根据节点实例获取所有从当前节点出发的路径，然后根据路径获得下一个节点实例：
            for (ActivityImpl activityImpl : activitiList) {
                String id = activityImpl.getId();
                if (activitiId.equals(id)) {
                    System.out.println("当前任务：" + activityImpl.getProperty("name")); // 输出某个节点的某种属性
                    List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();// 获取从某个节点出来的所有线路
                    for (PvmTransition tr : outTransitions) {
                        PvmActivity ac = tr.getDestination(); // 获取线路的终点节点
                        System.out.println("下一步任务任务：" + ac.getProperty("name"));
                        nextId = ac.getId();
                    }
                    break;
                }
            }
        }
        return nextId;
    }
}
