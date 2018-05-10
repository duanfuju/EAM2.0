package com.tiansu.eam.modules.act.service;

import com.tiansu.eam.modules.act.entity.EamBusinessTask;
import com.tiansu.eam.modules.act.entity.EamTableCode;
import com.tiansu.eam.modules.act.utils.ProcessDefCache;
import com.tiansu.eam.modules.sys.dao.SysConfigDao;
import com.tiansu.eam.modules.sys.entity.User;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.NativeExecutionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.NativeTaskQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Creebted by marsart on 2017/7/27.
 * @author q1210
 * 工作流任务相关查询/处理
 */
@Service
@Transactional(readOnly = true)
public class EamActTaskService extends EamActBaseService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    SysConfigDao sysConfigDao;

    private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * @creator Douglas
     * @createtime 2017-9-6 11:36
     * @description: 我的代办任务列表
     * @param taskName 任务名称
     * @return
     */
    public Map<String,Object> todoList(String taskName,String processName,String userId) {
        //获取页面请求
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //获取当前登录用户
        User user = UserUtils.getUser();
        if (user == null || StringUtils.isBlank(user.getLoginname())) {//userId为app传入参数
            user = UserUtils.get(userId);
        }
        //自定义sql查询方式
        NativeTaskQuery nativeTaskQuery = taskService.createNativeTaskQuery();
        //过滤条件-任务名称
        String filterTaskName = "";
        if (StringUtils.isNotBlank(taskName)) {
            filterTaskName += " and RES.NAME_ like #{taskName}";
            nativeTaskQuery.parameter("taskName", "%" + taskName + "%");
        }

        //过滤条件-流程名称
        String filterProcessName = "";
        if (StringUtils.isNotBlank(processName)) {
            filterProcessName += " P.NAME_ like #{processName} and";
            nativeTaskQuery.parameter("processName", "%" + processName + "%");
        }
        // 当前人在候选人或者候选组范围之内
        String sql = "select distinct RES.* from ACT_RU_TASK RES left join ACT_RU_IDENTITYLINK I on I.TASK_ID_ = RES.ID_ " +
                "LEFT JOIN ACT_RE_PROCDEF P ON P.ID_=RES.PROC_DEF_ID_ "+
                "WHERE RES.SUSPENSION_STATE_ = '1' and " +
                filterProcessName +
                " ( RES.ASSIGNEE_ = #{userId}" +
                " or (RES.ASSIGNEE_ is null  and ( I.USER_ID_ = #{userId} or I.GROUP_ID_ IN (select G.roleno from ioms_roleuser G where G.loginname = #{userId} ) )" +
                ") )" + filterTaskName;

        nativeTaskQuery.sql(sql).parameter("userId", user.getLoginname()).parameter("orderBy","CREATE_TIME_ desc");



        // 从5.16版本开始可以使用以下方式
        //TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateOrAssigned(user.getLoginname()).orderByTaskCreateTime().desc();


        List<EamBusinessTask> list = new ArrayList<>();
        List<Task> tasks =dataTablePage(nativeTaskQuery,request);

        //循环将task数组转成EamBusinessTask类型数组
        for (Task task:tasks) {
            EamBusinessTask businessTask = new EamBusinessTask();
            if (null != task) {
                ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                        .processDefinitionId(task.getProcessDefinitionId()).singleResult();

                HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();

                String starter = instance.getStartUserId();
                String starttime = sdf.format(instance.getStartTime());
                String businesskey = instance.getBusinessKey();
                String tbName = businesskey.substring(0,businesskey.indexOf(':'));
                String tbKey = businesskey.substring(businesskey.indexOf(':')+1);
                String businessCode = getBusinessCode(tbName,tbKey);


                businessTask.setTitle(processDefinition.getName()+"-"+starter+"-"+starttime+"-"+businessCode);//请求标题
                businessTask.setProcDefName(processDefinition.getName());//流程名称
                businessTask.setTaskId(task.getId());                   //任务id
                businessTask.setTaskName(task.getName());               //任务名称
                businessTask.setProcInsId(task.getProcessInstanceId()); //流程实例id
                businessTask.setProcDefId(task.getProcessDefinitionId());//流程定义id
                businessTask.setIsSuspended(task.isSuspended()==true?1:0);//任务状态
                businessTask.setTaskCreateDate(sdf.format(task.getCreateTime()));   //任务创建日期
                businessTask.setAssignee(task.getAssignee());           //任务委托人|签收人
                list.add(businessTask);
            }
        }

        Map<String, Object> map= new HashMap();
        map.put("recordsFiltered",nativeTaskQuery.sql("select count(*) from ("+sql+")"+"RES").count());
        map.put("recordsTotal",nativeTaskQuery.sql("select count(*) from ("+sql+")"+"RES").count());
        map.put("data",list);
        int draw = Integer.parseInt(request.getParameter("draw")==null?"1":request.getParameter("draw"));
        map.put("draw",draw);

        return map;

    }

    /**
     * @creator caoh
     * @createtime 2017-9-18 14:41
     * @description: 我的已办任务列表(运行中流程)
     * @param processName
     * @return
     */
    public Map<String,Object> finishedList(String processName,String uName) {
        //获取页面请求
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String userId = UserUtils.getUser().getLoginname();
        if (StringUtils.isBlank(userId)) {  //uName为app传入参数
            userId = uName;
        }
        NativeExecutionQuery nativeExecutionQuery = runtimeService.createNativeExecutionQuery().parameter("userId", userId).parameter("orderBy", "CLAIM_TIME_ desc");

        //过滤条件-流程名称
        String filterProcessName = "";
        if (StringUtils.isNotBlank(processName)) {
            filterProcessName += "AND P.NAME_ like #{processName} ";
            nativeExecutionQuery.parameter("processName", "%" + processName + "%");
        }
        // native query
        String sql = "select RES.*,ART.CLAIM_TIME_ AS CLAIM_TIME_ from ACT_RU_EXECUTION RES left join ACT_HI_TASKINST ART on ART.PROC_INST_ID_ = RES.PROC_INST_ID_ "
                +"LEFT JOIN ACT_RE_PROCDEF P ON P.ID_=RES.PROC_DEF_ID_"
                + " where RES.SUSPENSION_STATE_ = '1' and ART.ASSIGNEE_ = #{userId} and ACT_ID_ is not null and IS_ACTIVE_ = '1' "
                +filterProcessName;


       // nativeExecutionQuery

        List<EamBusinessTask> list = new ArrayList<>();
        List<Execution> executionList =dataTablePage(nativeExecutionQuery.sql(sql),request);

        /**循环将execution数组转成EamBusinessTask类型数组,并设置流程定义和当前处理节点等信息-start**/
        // 查询流程定义对象
        Map<String, ProcessDefinition> definitionMap = new HashMap<String, ProcessDefinition>();

        // 任务的英文-中文对照
        Map<String, Task> taskMap = new HashMap<String, Task>();

        // 每个Execution的当前活动ID，可能为多个
        Map<String, List<String>> currentActivityMap = new HashMap<String, List<String>>();

        for (Execution execution:executionList) {
            EamBusinessTask businessTask = new EamBusinessTask();


            ExecutionEntity executionEntity = (ExecutionEntity) execution;
            String processInstanceId = executionEntity.getProcessInstanceId();
            String processDefinitionId = executionEntity.getProcessDefinitionId();
            //String excutionId = execution.getId();



            // 缓存ProcessDefinition对象到Map集合
            definitionCache(definitionMap, processDefinitionId);

            // 查询当前流程的所有处于活动状态的活动ID，如果并行的活动则会有多个
            List<String> activeActivityIds = runtimeService.getActiveActivityIds(execution.getId());

            currentActivityMap.put(execution.getId(), activeActivityIds);

            for (String activityId : activeActivityIds) {

                // 查询处于活动状态的任务
                Task task = taskService.createTaskQuery().taskDefinitionKey(activityId).executionId(execution.getId()).singleResult();

                // 调用活动
                if (task == null) {
                    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                            .superProcessInstanceId(processInstanceId).singleResult();
                    if (processInstance != null) {
                        task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();
                        definitionCache(definitionMap, processInstance.getProcessDefinitionId());
                    }
                }
                taskMap.put(activityId, task);
            }


            businessTask.setExecutionId(execution.getId());   //设置执行Id
            businessTask.setProcInsId(execution.getProcessInstanceId());//设置流程实例Id
            String processDefid = ((ExecutionEntity) execution).getProcessDefinitionId();//获取流程定义id
            String prcoDefName = definitionMap.get(processDefid).getName();//获取流程定义名称
            businessTask.setProcDefName(prcoDefName); //设置流程定义名称
            businessTask.setProcDefId(processDefid);   //设置流程定义Id

            String excutionid = execution.getId();
           // HistoricProcessInstanceQuery historicProcessInstanceQuery =
            HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery().processInstanceId(execution.getProcessInstanceId()).singleResult();

            String starter = instance.getStartUserId();//流程发起者
            String starttime = sdf.format(instance.getStartTime());//流程发起时间

//            String starter = (String)runtimeService.getVariable(excutionid,"Starter");
//            String starttime = (String)runtimeService.getVariable(excutionid,"StartTime");

            businessTask.setTitle(prcoDefName+"-"+starter+"-"+starttime);//设置请求标题
            businessTask.setProcessStarter(starter);
            /*            if(starter!=null&&starter!=""){
                businessTask.setProcessStarter(starter);//设置发起者
            }*/


            List<String> activitiIds = currentActivityMap.get(excutionid);//当前excution对应的多个活动id
            for(String id:activitiIds){
                String taskname = taskMap.get(id)==null?"系统任务":taskMap.get(id).getName();
                String taskassignee = taskMap.get(id)==null||taskMap.get(id).getAssignee()==null?"":("|"+taskMap.get(id).getAssignee());
                String taskstatu = "";
                if(taskMap.get(id)!=null){      //人工活动
                    taskstatu = taskassignee==""?"未签收":"办理中";
                }else{   //非人工活动
                    taskstatu="运行中";
                }
                businessTask.setCurrentTaskInfo(taskname+"("+taskstatu+taskassignee+")");
            }
            list.add(businessTask);

        }

        Map<String, Object> map= new HashMap();
        map.put("recordsFiltered",nativeExecutionQuery.sql("select count(*) from (" + sql + ") tb").count());
        map.put("recordsTotal",nativeExecutionQuery.sql("select count(*) from (" + sql + ") tb").count());
        map.put("data",list);
        int draw = Integer.parseInt(request.getParameter("draw")==null?"1":request.getParameter("draw"));
        map.put("draw",draw);

        return map;

    }


    /**
     * 查询某用户下所有未完成任务
     * 待完成
     * @return 任务列表
     */
    @Deprecated
    public List<EamBusinessTask> todoListByUser(String userId){
        List<EamBusinessTask> result = new ArrayList<>();
        if(StringUtils.isEmpty(userId)){
            return result;
        }

        return result;
    }

    /**
     * 查询下某类型任务,act可以为空
     * @param processDefKey 指定流程procDefKey
     * @return List<ActTask>
     *//*
    public List<EamBusinessTask> todoList(String processDefKey){

        List<EamBusinessTask> result = new ArrayList<>();
        // =============== 已经签收的任务  ===============
        result.addAll(taskList(processDefKey,0));
        // =============== 待用户签收的任务  ===============
        result.addAll(taskList(processDefKey,1));
        // =============== 待用户组签收的任务===============
        result.addAll(taskList(processDefKey,2));
        Collections.sort(result);
        return result;
    }*/

    /**
     * 用户待完成任务
     * @param processDefKey 指定流程procDefKey
     * @param type 任务查询类型0:已签收任务,1:待签收（签收人）任务，2.待签收（签收组）任务
     * @return 任务列表
     */
    public List<EamBusinessTask> taskList(String processDefKey, int type) {
        String userId = UserUtils.getUser().getLoginname();
        TaskQuery todoTaskQuery;
        List<EamBusinessTask> result = new ArrayList<>();
        switch (type) {
            case 0:
                todoTaskQuery = taskService.createTaskQuery().taskAssignee(userId).
                        active().includeProcessVariables();
                break;
            case 1:
                todoTaskQuery = taskService.createTaskQuery().taskCandidateUser(userId)
                        .active().includeProcessVariables();
                break;
            case 2:
                List<String> groups = UserUtils.getUser().getRoleIdList();
                todoTaskQuery = taskService.createTaskQuery().taskCandidateGroupIn(groups)
                        .active().includeProcessVariables();
                break;
            default:
                return result;
        }

        if (StringUtils.isNotBlank(processDefKey)){
            todoTaskQuery.processDefinitionKey(processDefKey);
        }

        //先关掉时间条件
/*        if (act.getBeginDebte() != null){
            todoTaskQuery.taskCreebtedAfter(act.getBeginDebte());
        }
        if (act.getEndDebte() != null){
            todoTaskQuery.taskCreebtedBefore(act.getEndDebte());
        }*/

        // 查询列表
        List<Task> todoList = todoTaskQuery.list();
        for (Task task : todoList) {
            EamBusinessTask ebt = new EamBusinessTask();
            ebt.setTask(task);
            ebt.setVars(task.getProcessVariables());
            ebt.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));

            if (0 == type) {
                ebt.setStatus("todo");
            }
            else
            {
                ebt.setStatus("claim");
            }
            ebt.setProcDefId(task.getProcessDefinitionId());
            ebt.setProcInsId(task.getProcessInstanceId());
            ebt.setTaskCreateDate(sdf.format(task.getCreateTime()));
            ebt.setTaskDefKey(task.getTaskDefinitionKey());

            ebt.setVars(task.getProcessVariables());
            task.getTaskLocalVariables();

            ebt.setTaskId(task.getId());
            result.add(ebt);
        }

        return result;
    }


    /**
     *
     * @return
     */
    @Deprecated
    public List<EamBusinessTask> historicTaskList(EamBusinessTask act){
        List<EamBusinessTask> result = new ArrayList<>();
        return result;
    }



    /**
     * 返回流程实例路径处理节点（不区分已完成、未完成实例）
     * @param procInsId String 流程实例
     * @return
     */
    @Deprecated
    public List<EamBusinessTask> histoicFlowList(String procInsId){
        List<EamBusinessTask> result = new ArrayList<>();
        try {
            List<HistoricTaskInstance> list =  historyService.createHistoricTaskInstanceQuery().
                    processInstanceId(procInsId).orderByHistoricTaskInstanceEndTime().list();
            EamBusinessTask ebt = null;
            for (HistoricTaskInstance taskInstance: list){
                ebt = new EamBusinessTask();
                ebt.setTaskName(taskInstance.getName());
                ebt.setProcInsId(taskInstance.getProcessInstanceId());
                ebt.setProcDefId(taskInstance.getProcessDefinitionId());
                ebt.setTaskCreateDate(sdf.format(taskInstance.getStartTime()));
                if (null != taskInstance.getEndTime()) {
                    if (StringUtils.equals(taskInstance.getDeleteReason(),"completed")) {
                        ebt.setStatus("finish");
                        ebt.setTaskEndDate(sdf.format(taskInstance.getEndTime()));
                    }
                    else if (StringUtils.equals(taskInstance.getDeleteReason(),"deleted")) {
                        ebt.setStatus("deleted");
                    }
                }
                else {
                    if (null == taskInstance.getClaimTime()) {
                        ebt.setStatus("claim");
                    }
                    else {
                        ebt.setStatus("todo");
                    }
                }
                result.add(ebt);
            }
        }
        catch(ActivitiException e) {

        }

        return result;
    }

    /**
     * 签收任务
     * @param taskId 任务ID
     * @param userId 签收用户ID（用户登录名）
     */
    @Transactional(readOnly = false)
    public void claim(String taskId, String userId){
        taskService.claim(taskId, userId);
    }

	/**
	 * 委派任务(由他人代办)
	 * @param taskId 任务ID
     * @param ownerUserId 委派人
	 * @param userId 被委派人
	 */
    @Transactional(readOnly = false)
	public void delegebteTask(String taskId,String ownerUserId,String userId){
        taskService.setOwner(taskId,ownerUserId);
		taskService.delegateTask(taskId, userId);
	}

	/**
	 * 完成任务
	 * @param taskId 任务ID
	 */
	public void completeTask(String taskId){
		taskService.resolveTask(taskId);
	}



    /**
     * 流程定义对象缓存
     *
     * @param definitionMap
     * @param processDefinitionId
     */
    private void definitionCache(Map<String, ProcessDefinition> definitionMap, String processDefinitionId) {
        if (definitionMap.get(processDefinitionId) == null) {
            ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
            processDefinitionQuery.processDefinitionId(processDefinitionId);
            ProcessDefinition processDefinition = processDefinitionQuery.singleResult();

            // 放入缓存
            definitionMap.put(processDefinitionId, processDefinition);
        }
    }


    /**
     * @creator caoh
     * @createtime 2017-10-10 10:41
     * @description:挂起/激活流程
     * @param state active-请求激活
     * @param processInstanceId 流程实例ID
     * @return
     */
    public void changeState(String state, String processInstanceId) {
        if (StringUtils.equals("active", state)) {
            runtimeService.activateProcessInstanceById(processInstanceId);
        } else {
            runtimeService.suspendProcessInstanceById(processInstanceId);
        }

    }

    /**
     * @creator Douglas
     * @createtime 2017-12-1 11:44
     * @description: 根据流程中的businesskey字段，查询业务数据中的业务编码
     * @param tbName
     * @param tbKey
     * @return
     */
    private String getBusinessCode(String tbName,String tbKey){
        String tbCode="";
        for(EamTableCode code:EamTableCode.values()){
            if(code.getTablename().equals(tbName)){
                tbCode = code.getCodename();
            }
        }
        Map map = new HashMap();
        map.put("tbKey","'"+tbKey+"'");
        map.put("tbName",tbName);
        map.put("tbCode",tbCode);
        if(tbCode==""){
            return "";
        }else {
            String aa = sysConfigDao.getBusinessCode(map);
            return aa;
        }


    }

}
