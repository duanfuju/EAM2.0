/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.modules.act.service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.tiansu.eam.modules.act.entity.ActProcess;
import com.tiansu.eam.modules.act.entity.EamBusinessTask;
import com.tiansu.eam.modules.act.service.ext.ActEamUserEntityService;
import com.tiansu.eam.modules.sys.dao.EamRoleDao;
import com.tiansu.eam.modules.sys.entity.User;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.history.*;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.pvm.ReadOnlyProcessDefinition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.NativeProcessInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipInputStream;

/**
 * 流程相关service
 * 包括流程(定义),流程实例服务相关的内容
 * @author q1210
 * @version 2017-08-07
 */
@Service
@Transactional(readOnly = true)
public class EamProcessService extends EamActBaseService {

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private FormService formService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private TaskService taskService;
	@Autowired
	ActEamUserEntityService actEamUserEntityService;

	@Autowired
	private EamRoleDao eamRoleDao;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * @creator caoh
	 * @createtime 2017-9-20 10:11
	 * @description: 新建流程列表
	 * @param category 类型
	 * @return 返回包含列表的list对象
	 */
	public List<Map> processList(String category) {
		//获取页面请求
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		//只查询最新版本运行中的流程
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
				.latestVersion().orderByProcessDefinitionKey().desc();

		if (StringUtils.isNotEmpty(category)){
			processDefinitionQuery.processDefinitionCategory(category);
		}

		List<ActProcess> list = new ArrayList<>();
		List<ProcessDefinition> procList =processDefinitionQuery.list();

		//定义一个List缓存流程分类
		List<String> categoryList = new ArrayList<>();
		//最终返回结果的对象
		List<Map> definitionList = new ArrayList<>();
		for (Object o:procList) {
			ActProcess proc = new ActProcess();
			if (null != o) {
				ProcessDefinition pd = (ProcessDefinition) o;
				//获取该流程分类
				String proCategory = pd.getCategory();
				//如果List中没有该流程分类，则缓存该分类名，并在返回的结果集中添加一个新类别的集合
				if(categoryList.indexOf(proCategory)==-1){
					categoryList.add(proCategory);
					Map singleCategoryMap = new HashMap();
					singleCategoryMap.put("title",proCategory);
					List<Map> categoryContent = new ArrayList<>();
					//存放当前single循环的流程定义对象
					Map singlePro = new HashMap();
					singlePro.put("name",pd.getName());
					singlePro.put("processDefineId",pd.getId());
					singlePro.put("processDefineKey",pd.getKey());
					categoryContent.add(singlePro);
					singleCategoryMap.put("content",categoryContent);
					definitionList.add(singleCategoryMap);

				}else{
					Map singlePro = new HashMap();
					singlePro.put("name",pd.getName());
					singlePro.put("processDefineId",pd.getId());
					singlePro.put("processDefineKey",pd.getKey());

					Map singleCategoryMap = definitionList.get(categoryList.indexOf(proCategory));
					List<Map> categoryContent = (List)singleCategoryMap.get("content");
					categoryContent.add(singlePro);
					//替换原Map中的content数组
					singleCategoryMap.put("content",categoryContent);
					//替换原来返回结果集中的分类Map
					definitionList.set(categoryList.indexOf(proCategory),singleCategoryMap);

				}

			}
		}

		return definitionList;
	}

	/**
	 * @creator caoh
	 * @createtime 2017-9-20 10:11
	 * @description: 流程定义列表
	 * @param category 类型
	 * @return 返回包含列表的map对象
	 */
	public Map<String,Object> processListPage(String category) {
		//获取页面请求
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		//只查询最新版本运行中的流程
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
				.latestVersion().orderByProcessDefinitionKey().desc();

		if (StringUtils.isNotEmpty(category)){
			processDefinitionQuery.processDefinitionCategory(category);
		}

		List<ActProcess> list = new ArrayList<>();
		List<ProcessDefinition> procList = dataTablePage(processDefinitionQuery,request);

		for (Object o:procList) {
			ActProcess proc = new ActProcess();
			if (null != o) {
				ProcessDefinition pd = (ProcessDefinition) o;

				proc.setProcessDefineId(pd.getId());
				proc.setProcessDefineKey(pd.getKey());
				proc.setVersion(Integer.toString(pd.getVersion()));
				proc.setName(pd.getName());
				proc.setDesc(pd.getDescription());
				proc.setCategory(pd.getCategory());
				proc.setDeploymentId(pd.getDeploymentId());
				proc.setCategory(pd.getCategory());
				proc.setResourceName(pd.getResourceName());
				proc.setDiagramResourceName(pd.getDiagramResourceName());
				if(pd.isSuspended()){
					proc.setIsSuspended(1);
				}
				else{
					proc.setIsSuspended(0);
				}

				list.add(proc);
			}
		}

		Map<String, Object> map= new HashMap();
		map.put("recordsFiltered",processDefinitionQuery.count());
		map.put("recordsTotal",processDefinitionQuery.count());
		map.put("data",list);
		int draw = Integer.parseInt(request.getParameter("draw")==null?"1":request.getParameter("draw"));
		map.put("draw",draw);

		return map;
	}

	/**
	 * @creator caoh
	 * @createtime 2017-9-22 10:17
	 * @description: 运行中流程实例列表
	 * @param procDefName String
	 * @return 返回包含流程实例列表的map
	 */
	public Map<String,Object> runningList(String procDefName) {
		//获取页面请求
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String sql = "SELECT DISTINCT\n" +
				"\tRES.*,\n" +
				"\tP.KEY_ AS ProcessDefinitionKey,\n" +
				"\tP.ID_ AS ProcessDefinitionId,\n" +
				"\tP.NAME_ AS ProcessDefinitionName,\n" +
				"\tP.VERSION_ AS ProcessDefinitionVersion,\n" +
				"\tP.DEPLOYMENT_ID_ AS DeploymentId\n" +
				"FROM\n" +
				"\tACT_RU_EXECUTION RES\n" +
				"INNER JOIN ACT_RE_PROCDEF P ON RES.PROC_DEF_ID_ = P.ID_\n" +
				"WHERE\n" +
				"\tRES.PARENT_ID_ IS NULL\n"
				//+"\tAND P.NAME_ LIKE '%#{procDefName}%'\n";
				+(procDefName==null?"":"\tAND P.NAME_ LIKE '%"+procDefName+"%'\n");
				//+ "ORDER BY RES.PROC_INST_ID_ DESC";
		NativeProcessInstanceQuery nativeProcessInstanceQuery = runtimeService.createNativeProcessInstanceQuery().sql(sql);
		nativeProcessInstanceQuery.parameter("orderBy","PROC_INST_ID_ DESC");

		ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();

		//processInstanceQuery.orderByProcessDefinitionKey();
		/*if (StringUtils.isNotBlank(procDefKey)){

		}*/


		processInstanceQuery.orderByProcessInstanceId().desc();


		List<EamBusinessTask> list = new ArrayList<>();
		//List<ProcessInstance> procInsList = dataTablePage(processInstanceQuery,request);
		List<ProcessInstance> procInsList = dataTablePage(nativeProcessInstanceQuery,request);
		//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		//将查询出的ProcessInstance列表转化为EamBusinessTask对象数组
		for (Object o:procInsList) {
			EamBusinessTask pb = new EamBusinessTask();
			if (null != o) {
				ProcessInstance instance = (ProcessInstance) o;
				//用于存放当前流程实例下激活的任务
				Map<String, Task> taskMap = new HashMap<String, Task>();
				// 每个Execution的当前活动ID，可能为多个
				Map<String, List<String>> currentActivityMap = new HashMap<String, List<String>>();

				//获取流程定义
				RepositoryServiceImpl repositoryServiceImpl = (RepositoryServiceImpl) repositoryService;
				ReadOnlyProcessDefinition deployedProcessDefinition = repositoryServiceImpl.getDeployedProcessDefinition(instance.getProcessDefinitionId());
				ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) deployedProcessDefinition;
				//获取流程定义下的所有节点
				List<ActivityImpl> activitiList = processDefinition.getActivities();//获得当前任务的所有节点

				//取得该流程实例当前激活的节点（每个流程实例处于激活的节点可能有多个）
				List<String> activeActivityIds = runtimeService.getActiveActivityIds(instance.getId());
				currentActivityMap.put(instance.getId(), activeActivityIds);

				//遍历所有激活的节点，查询这些节点对应的任务
				for (String activityId : activeActivityIds) {

					// 查询处于活动状态的任务
					Task task = taskService.createTaskQuery().taskDefinitionKey(activityId).executionId(instance.getId()).singleResult();

					// 调用活动
					if (task == null) {
						ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
								.superProcessInstanceId(instance.getId()).singleResult();
						if (processInstance != null) {
							task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();
						}
					}
					taskMap.put(activityId, task);
				}

				List<String> activitiIds = currentActivityMap.get(instance.getId());//当前excution对应的多个活动id
				/*for(String id:activitiIds){
					String taskname = taskMap.get(id)==null?"系统任务":taskMap.get(id).getName();
					String taskassignee = taskMap.get(id)==null||taskMap.get(id).getAssignee()==null?"":("|"+taskMap.get(id).getAssignee());
					String taskstatu = "";
					if(taskMap.get(id)!=null){      //人工活动
						taskstatu = taskassignee==""?"未签收":"办理中";
					}else{   //非人工活动
						taskstatu="运行中";
					}
					pb.setCurrentTaskInfo(taskname+"("+taskstatu+taskassignee+")");
				}*/

				for (ActivityImpl activity : activitiList) {
					// 当前节点
					String activityId = activity.getId();
					if (activeActivityIds.contains(activityId)) {

						String taskname = activity.getProperties().get("name").toString();
						String taskassignee = taskMap.get(activityId)==null||taskMap.get(activityId).getAssignee()==null?"":("|"+taskMap.get(activityId).getAssignee());
						String taskstatu = "";
						if(taskMap.get(activityId)!=null){      //人工活动
							taskstatu = taskassignee==""?"未签收":"办理中";
						}else{   //非人工活动
							taskstatu="运行中";
						}
						pb.setCurrentTaskInfo(taskname+"("+taskstatu+taskassignee+")");

						//pb.setCurrentTaskInfo(activity.getProperties().get("name").toString());
						//Task task = taskMap.get(activityId);

						//List users = getTaskCandidate(task.getId());


						//String ass = task.getAssignee();
						//System.out.println(task.getAssignee());
						//pb.setUndoPerson((Task)taskMap.get("activityId"));
					}
				}

				//instance.getId();
				pb.setProcInsId(instance.getProcessInstanceId());
				String pdName = instance.getProcessDefinitionName();

				/*String starter="";
				String starttime="";
				//创建历史的流程变量查询
				HistoricVariableInstance starterInst = historyService.createHistoricVariableInstanceQuery().processInstanceId(instance.getId()).variableName("Starter").singleResult();
				HistoricVariableInstance starttimeInst = historyService.createHistoricVariableInstanceQuery().processInstanceId(instance.getId()).variableName("StartTime").singleResult();
				if(starterInst!=null){
					starter = starterInst.getValue().toString();//流程发起者
				}
				if(starttimeInst!=null){
					starttime = starttimeInst.getValue().toString();//流程发起时间
				}*/

				HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(instance.getId()).singleResult();
				String starter = historicProcessInstance.getStartUserId();//流程发起者
				String starttime = sdf.format(historicProcessInstance.getStartTime());//流程发起时间


				pb.setTitle(pdName+"-"+starter+"-"+starttime);
				pb.setProcDefName(pdName);
				pb.setProcessStarter(starter);
				pb.setProcessInstCreateDate(starttime);
				//pb.setProcDefVersion(instance.getProcessDefinitionVersion());
				pb.setBusinessCode(instance.getBusinessKey());
				pb.setIsSuspended(instance.isSuspended()==true?1:0);

				list.add(pb);
			}
		}

		Map<String, Object> map= new HashMap();
		map.put("recordsFiltered",nativeProcessInstanceQuery.sql("select count(*) from ("+sql+")"+"RES").count());
		map.put("recordsTotal",nativeProcessInstanceQuery.sql("select count(*) from ("+sql+")"+"RES").count());
		map.put("data",list);
		int draw = Integer.parseInt(request.getParameter("draw")==null?"1":request.getParameter("draw"));
		map.put("draw",draw);

		return map;
	}

	/**
	 * @creator caoh
	 * @createtime 2017-11-28 16:27
	 * @description: 我发起的流程
	 * @param procDefName 流程定义名称
	 * @return
	 */
	public Map<String,Object> myRequest(String procDefName) {
		//获取页面请求
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String userId = UserUtils.getUser().getLoginname();
		/*HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery().startedBy(userId).orderByProcessInstanceStartTime().desc();
		if (StringUtils.isNotBlank(procDefName)){
			historicProcessInstanceQuery.processDefinitionName("%"+procDefName+"%");
		}*/

		String sql = "SELECT DISTINCT\n" +
				"\tRES.*,\n" +
				"\tDEF.KEY_ AS PROC_DEF_KEY_,\n" +
				"\tDEF.NAME_ AS PROC_DEF_NAME_,\n" +
				"\tDEF.VERSION_ AS PROC_DEF_VERSION_,\n" +
				"\tDEF.DEPLOYMENT_ID_ AS DEPLOYMENT_ID_\n" +
				"\tFROM\n" +
				"\tACT_HI_PROCINST RES\n" +
				"\tLEFT OUTER JOIN ACT_RE_PROCDEF DEF ON RES.PROC_DEF_ID_ = DEF.ID_\n" +
				"\tWHERE 1=1\n"+
				"\tAND RES.START_USER_ID_='"+userId+"'\n"+
				(procDefName==null?"":"\tAND DEF.NAME_ LIKE '%"+procDefName+"%'\n");

		NativeHistoricProcessInstanceQuery nativeHistoricProcessInstanceQuery = historyService.createNativeHistoricProcessInstanceQuery().sql(sql);
		nativeHistoricProcessInstanceQuery.parameter("orderBy","START_TIME_ DESC");
		List<EamBusinessTask> list = new ArrayList<>();
		List<HistoricProcessInstance> procInsList = dataTablePage(nativeHistoricProcessInstanceQuery,request);


		//将查询出的ProcessInstance列表转化为EamBusinessTask对象数组
		for (Object o:procInsList) {
			EamBusinessTask pb = new EamBusinessTask();
			if (null != o) {
				HistoricProcessInstance historicProcessInstance = (HistoricProcessInstance) o;
				//流程未结束，则查询该流程所在节点
				if(historicProcessInstance.getEndTime()==null){
					//流程当前节点信息
					String procInfo = "";
					//获取当前流程实例对应的Excution，可能有多个
					ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery().processInstanceId(historicProcessInstance.getId());
					List<ProcessInstance> excutionList = processInstanceQuery.list();
					//循环所有的excution
					for(Object ex:excutionList){
						ProcessInstance instance = (ProcessInstance)ex;
						//用于存放当前流程实例下激活的任务
						Map<String, Task> taskMap = new HashMap<String, Task>();
						// 每个Execution的当前活动ID，可能为多个
						Map<String, List<String>> currentActivityMap = new HashMap<String, List<String>>();

						//获取流程定义
						RepositoryServiceImpl repositoryServiceImpl = (RepositoryServiceImpl) repositoryService;
						ReadOnlyProcessDefinition deployedProcessDefinition = repositoryServiceImpl.getDeployedProcessDefinition(instance.getProcessDefinitionId());
						ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) deployedProcessDefinition;
						//获取流程定义下的所有节点
						List<ActivityImpl> activitiList = processDefinition.getActivities();


						//取得该流程实例当前激活的节点（每个流程实例处于激活的节点可能有多个）
						List<String> activeActivityIds = runtimeService.getActiveActivityIds(instance.getId());
						currentActivityMap.put(instance.getId(), activeActivityIds);

						//遍历所有激活的节点，查询这些节点对应的任务
						for (String activityId : activeActivityIds) {

							// 查询处于活动状态的任务
							Task task = taskService.createTaskQuery().taskDefinitionKey(activityId).executionId(instance.getId()).singleResult();

							// 调用活动
							if (task == null) {
								ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
										.superProcessInstanceId(instance.getId()).singleResult();
								if (processInstance != null) {
									task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();
								}
							}
							taskMap.put(activityId, task);
						}

						List<String> activitiIds = currentActivityMap.get(instance.getId());//当前excution对应的多个活动id



						for (ActivityImpl activity : activitiList) {
							// 当前节点
							String activityId = activity.getId();
							String taskname = "";
							String taskassignee = "";
							String taskstatu = "";
							if (activeActivityIds.contains(activityId)) {

								taskname = activity.getProperties().get("name").toString();
								taskassignee = taskMap.get(activityId)==null||taskMap.get(activityId).getAssignee()==null?"":("|"+taskMap.get(activityId).getAssignee());
								//String taskstatu = "";
								if(taskMap.get(activityId)!=null){      //人工活动
									taskstatu = taskassignee==""?"未签收":"办理中";
								}else{   //非人工活动
									taskstatu="运行中";
								}

								procInfo+=(taskname+"("+taskstatu+taskassignee+")");


							}
						}
						pb.setProcInsId(instance.getId());

					}
					pb.setCurrentTaskInfo(procInfo);


				}else{
					pb.setCurrentTaskInfo("已归档");
					pb.setProcInsId(historicProcessInstance.getId());
				}



				//instance.getId();
				//pb.setProcInsId(instance.getProcessInstanceId());
				String pdName = historicProcessInstance.getProcessDefinitionName();



				//HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(instance.getId()).singleResult();
				String starter = historicProcessInstance.getStartUserId();//流程发起者
				String starttime = sdf.format(historicProcessInstance.getStartTime());//流程发起时间
				String endtime ="";
				if(historicProcessInstance.getEndTime()!=null){
					endtime = sdf.format(historicProcessInstance.getEndTime());//流程发起时间
				}


				pb.setTitle(pdName+"-"+starter+"-"+starttime);
				pb.setProcDefName(pdName);
				pb.setProcessStarter(starter);
				pb.setProcessInstCreateDate(starttime);
				pb.setProcessInstEndeDate(endtime);
				//pb.setProcDefVersion(instance.getProcessDefinitionVersion());
				pb.setBusinessCode(historicProcessInstance.getBusinessKey());
				//pb.setIsSuspended(instance.isSuspended()==true?1:0);

				list.add(pb);
			}
		}

		Map<String, Object> map= new HashMap();
		map.put("recordsFiltered",nativeHistoricProcessInstanceQuery.sql("select count(*) from ("+sql+")"+"RES").count());
		map.put("recordsTotal",nativeHistoricProcessInstanceQuery.sql("select count(*) from ("+sql+")"+"RES").count());
		map.put("data",list);
		int draw = Integer.parseInt(request.getParameter("draw")==null?"1":request.getParameter("draw"));
		map.put("draw",draw);

		return map;
	}


	/**
	 * @creator caoh
	 * @createtime 2017-9-19 14:10
	 * @description:已归档流程实例列表
	 * @param processName String
	 * @return 返回包含流程实例列表的map
	 */
	public Map<String,Object> finishedList(String processName) {
		//获取页面请求
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		//HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery().finished().orderByProcessInstanceStartTime().desc();
		NativeHistoricProcessInstanceQuery nativeHistoricProcessInstanceQuery = historyService.createNativeHistoricProcessInstanceQuery();

		//过滤条件-流程名称
		String filterProcessName = "";
		if (StringUtils.isNotBlank(processName)) {
			filterProcessName += "AND DEF.NAME_ like #{processName} ";
			nativeHistoricProcessInstanceQuery.parameter("processName", "%" + processName + "%");
		}

		String sql = "SELECT DISTINCT\n" +
				"\tRES.*,\n" +
				"\tDEF.KEY_ AS PROC_DEF_KEY_,\n" +
				"\tDEF.NAME_ AS PROC_DEF_NAME_,\n" +
				"\tDEF.VERSION_ AS PROC_DEF_VERSION_,\n" +
				"\tDEF.DEPLOYMENT_ID_ AS DEPLOYMENT_ID_\n" +
				"\tFROM\n" +
				"\tACT_HI_PROCINST RES\n" +
				"\tLEFT OUTER JOIN ACT_RE_PROCDEF DEF ON RES.PROC_DEF_ID_ = DEF.ID_\n" +
				"\tWHERE\n" +
				"\tRES.END_TIME_ IS NOT NULL\n"+
				filterProcessName;
		nativeHistoricProcessInstanceQuery.sql(sql).parameter("orderBy","END_TIME_ DESC");

		List<EamBusinessTask> list = new ArrayList<>();
		List<HistoricProcessInstance> historicProcessInstanceList = dataTablePage(nativeHistoricProcessInstanceQuery,request);
		//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		//将查询出的ProcessInstance列表转化为EamBusinessTask对象数组
		for (Object o:historicProcessInstanceList) {
			EamBusinessTask pb = new EamBusinessTask();
			if (null != o) {

				HistoricProcessInstance instance = (HistoricProcessInstance) o;
				String starter = instance.getStartUserId();
				String starttime = sdf.format(instance.getStartTime());
				String endtime = sdf.format(instance.getEndTime());

				pb.setProcDefId(instance.getProcessDefinitionId());//流程定义Id
				pb.setProcInsId(instance.getId());					//流程实例Id
				pb.setTitle(instance.getProcessDefinitionName()+"-"+starter+"-"+starttime);
				pb.setProcDefName(instance.getProcessDefinitionName());//流程定义名称
				pb.setProcessInstCreateDate(starttime);//流程启动时间
				pb.setProcessInstEndeDate(endtime);//流程结束时间
				pb.setProcessStarter(starter);		//流程启动者
				pb.setProcessFinishReason(instance.getDeleteReason());	//流程结束原因

				list.add(pb);
			}
		}

		Map<String, Object> map= new HashMap();
		map.put("recordsFiltered",nativeHistoricProcessInstanceQuery.sql("select count(*) from ("+sql+")"+"RES").count());
		map.put("recordsTotal",nativeHistoricProcessInstanceQuery.sql("select count(*) from ("+sql+")"+"RES").count());
		map.put("data",list);
		int draw = Integer.parseInt(request.getParameter("draw")==null?"1":request.getParameter("draw"));
		map.put("draw",draw);

		return map;
	}

	/**
	 * @creator Douglas
	 * @createtime 2017-9-19 14:11
	 * @description: 读取资源，通过部署ID
	 * @param procDefId  流程定义ID
	 * @param proInsId 流程实例ID
	 * @param resType 资源类型(xml|image)
	 */
	public InputStream resourceRead(String procDefId, String proInsId, String resType) throws Exception {

		if (StringUtils.isBlank(procDefId)){
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(proInsId).singleResult();
			procDefId = processInstance.getProcessDefinitionId();
		}
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();

		String resourceName = "";
		if (resType.equals("image")) {
			resourceName = processDefinition.getDiagramResourceName();
		} else if (resType.equals("xml")) {
			resourceName = processDefinition.getResourceName();
		}

		InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
		return resourceAsStream;
	}

	/**
	 * 部署流程 - 保存
	 * @param file
	 * @return
	 */
	@Transactional(readOnly = false)
	public String deploy(String exportDir, String category, MultipartFile file) {

		String message = "";

		String fileName = file.getOriginalFilename();

		try {
			InputStream fileInputStream = file.getInputStream();
			Deployment deployment = null;
			String extension = FilenameUtils.getExtension(fileName);
			if (extension.equals("zip") || extension.equals("bar")) {
				ZipInputStream zip = new ZipInputStream(fileInputStream);
				deployment = repositoryService.createDeployment().addZipInputStream(zip).deploy();
			} else if (extension.equals("png")) {
				deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
			} else if (fileName.indexOf("bpmn20.xml") != -1) {
				deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
			} else if (extension.equals("bpmn")) { // bpmn扩展名特殊处理，转换为bpmn20.xml
				String baseName = FilenameUtils.getBaseName(fileName);
				deployment = repositoryService.createDeployment().addInputStream(baseName + ".bpmn20.xml", fileInputStream).deploy();
			} else {
				message = "不支持的文件类型：" + extension;
			}

			List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();

			// 设置流程分类
			for (ProcessDefinition processDefinition : list) {
//					ActUtils.exportDiagramToFile(repositoryService, processDefinition, exportDir);
				repositoryService.setProcessDefinitionCategory(processDefinition.getId(), category);
				message += "部署成功，流程ID=" + processDefinition.getId() + "<br/>";
			}

			if (list.size() == 0){
				message = "部署失败，没有流程。";
			}

		} catch (Exception e) {
			throw new ActivitiException("部署失败！", e);
		}
		return message;
	}

	/**
	 * 设置流程分类
	 */
	@Transactional(readOnly = false)
	public void updateCategory(String procDefId, String category) {
		repositoryService.setProcessDefinitionCategory(procDefId, category);
	}

	/**
	 * 挂起、激活流程
	 */
	@Transactional(readOnly = false)
	public String updateState(String state, String procDefId) {
		if (state.equals("active")) {
			repositoryService.activateProcessDefinitionById(procDefId, true, null);
			return "已激活ID为[" + procDefId + "]的流程定义。";
		} else if (state.equals("suspend")) {
			repositoryService.suspendProcessDefinitionById(procDefId, true, null);
			return "已挂起ID为[" + procDefId + "]的流程定义。";
		}
		return "无操作";
	}

	/**
	 * 将部署的流程转换为模型
	 * @param procDefId
	 * @throws UnsupportedEncodingException
	 * @throws XMLStreamException
	 */
	@Transactional(readOnly = false)
	public org.activiti.engine.repository.Model convertToModel(String procDefId) throws UnsupportedEncodingException, XMLStreamException {

		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
		InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
				processDefinition.getResourceName());
		XMLInputFactory xif = XMLInputFactory.newInstance();
		InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
		XMLStreamReader xtr = xif.createXMLStreamReader(in);
		BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

		BpmnJsonConverter converter = new BpmnJsonConverter();
		ObjectNode modelNode = converter.convertToJson(bpmnModel);
		org.activiti.engine.repository.Model modelData = repositoryService.newModel();
		modelData.setKey(processDefinition.getKey());
		modelData.setName(processDefinition.getResourceName());
		modelData.setCategory(processDefinition.getCategory());//.getDeploymentId());
		modelData.setDeploymentId(processDefinition.getDeploymentId());
		modelData.setVersion(Integer.parseInt(String.valueOf(repositoryService.createModelQuery().modelKey(modelData.getKey()).count()+1)));

		ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
		modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
		modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, modelData.getVersion());
		modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
		modelData.setMetaInfo(modelObjectNode.toString());

		repositoryService.saveModel(modelData);

		repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));

		return modelData;
	}

	/**
	 * 导出图片文件到硬盘
	 */
	public List<String> exportDiagrams(String exportDir) throws IOException {
		List<String> files = new ArrayList<String>();
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();

		for (ProcessDefinition processDefinition : list) {
			String diagramResourceName = processDefinition.getDiagramResourceName();
			String key = processDefinition.getKey();
			int version = processDefinition.getVersion();
			String diagramPath = "";

			InputStream resourceAsStream = repositoryService.getResourceAsStream(
					processDefinition.getDeploymentId(), diagramResourceName);
			byte[] b = new byte[resourceAsStream.available()];

			@SuppressWarnings("unused")
			int len = -1;
			resourceAsStream.read(b, 0, b.length);

			// create file if not exist
			String diagramDir = exportDir + "/" + key + "/" + version;
			File diagramDirFile = new File(diagramDir);
			if (!diagramDirFile.exists()) {
				diagramDirFile.mkdirs();
			}
			diagramPath = diagramDir + "/" + diagramResourceName;
			File file = new File(diagramPath);

			// 文件存在退出
			if (file.exists()) {
				// 文件大小相同时直接返回否则重新创建文件(可能损坏)
				//logger.debug("diagram exist, ignore... : {}", diagramPath);

				files.add(diagramPath);
			} else {
				file.createNewFile();
				//logger.debug("export diagram to : {}", diagramPath);

				// wirte bytes to file
				FileUtils.writeByteArrayToFile(file, b, true);

				files.add(diagramPath);
			}

		}

		return files;
	}

	/**
	 * 删除部署的流程，级联删除流程实例
	 * @param deploymentId 流程部署ID
	 */
	@Transactional(readOnly = false)
	public void deleteDeployment(String deploymentId) {
		repositoryService.deleteDeployment(deploymentId, true);
	}

	/**
	 * 删除部署的流程实例
	 * @param procInsId 流程实例ID
	 * @param deleteReason 删除原因，可为空
	 */
	@Transactional(readOnly = false)
	public void deleteProcIns(String procInsId, String deleteReason) {
		runtimeService.deleteProcessInstance(procInsId, deleteReason);
	}

	/**
	 * 根据processDefineKey获取启动表单地址
	 * @param processDefineId 流程定义id
	 * @return
	 */
	public String getStartFormKey(String processDefineId)
	{
		try {
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
					.processDefinitionId(processDefineId).singleResult();
		    return formService.getStartFormKey(processDefinition.getId());
		}
		catch(ActivitiException ae){
			String warnStr = "getStartFormKey from ["+processDefineId+"] find a exception.";
			logger.warn(warnStr,ae);
			return null;
		}
	}

	/*public String readStartForm(String processDefinitionId,
									  HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();

		com.tiansu.eam.modules.sys.entity.User user = UserUtils.getUser();
		List<Group> groupList = (List<Group>) request.getSession().getAttribute("groups");

		// 权限拦截
		boolean startable = false;
		List<IdentityLink> identityLinks = repositoryService.getIdentityLinksForProcessDefinition(processDefinition.getId());
		if (identityLinks == null || identityLinks.isEmpty()) {
			startable = true;
		} else {
			for (IdentityLink identityLink : identityLinks) {
				if (StringUtils.isNotBlank(identityLink.getUserId()) && identityLink.getUserId().equals(user.getId())) {
					startable = true;
					break;
				}

				if (StringUtils.isNotBlank(identityLink.getGroupId())) {
					for (Group group : groupList) {
						if (group.getName().equals(identityLink.getGroupId())) {
							startable = true;
							break;
						}
					}
				}
			}
		}

		if (!startable) {
			*//*redirectAttributes.addFlashAttribute("error", "您无权启动【" + processDefinition.getName() + "】流程！");
			return new ModelAndView("redirect:/chapter5/process-list-view");*//*
			return "denied";
		}

		boolean hasStartFormKey = processDefinition.hasStartFormKey();

		// 根据是否有formkey属性判断使用哪个展示层
		*//*String viewName = "modules/material/materialInfoFormAdd";
		ModelAndView mav = new ModelAndView(viewName);

		// 判断是否有formkey属性
		if (hasStartFormKey) {
			Object renderedStartForm = formService.getRenderedStartForm(processDefinitionId);
			mav.addObject("startFormData", renderedStartForm);
			mav.addObject("processDefinition", processDefinition);
		} else { // 动态表单字段
			StartFormData startFormData = formService.getStartFormData(processDefinitionId);
			mav.addObject("startFormData", startFormData);
		}
		mav.addObject("hasStartFormKey", hasStartFormKey);
		mav.addObject("processDefinitionId", processDefinitionId);*//*
		return "modules/material/materialInfoFormAdd";
	}*/

	/**
	 * 启动流程
	 * @param procDefKey 流程定义KEY
	 * @param businessTable 业务表表名
	 * @param businessId	业务表编号
	 * @return 流程实例ID
	 */
	@Transactional(readOnly = false)
	public String startProcess(String procDefKey, String businessTable, String businessId) {
		return startProcess(procDefKey, businessTable, businessId, "");
	}

	/**
	 * 启动流程
	 * @param procDefKey 流程定义KEY
	 * @param businessTable 业务表表名
	 * @param businessId	业务表编号
	 * @param title			流程标题，显示在待办任务标题
	 * @return 流程实例ID
	 */
	@Transactional(readOnly = false)
	public String startProcess(String procDefKey, String businessTable, String businessId, String title) {
		Map<String, Object> vars = new HashMap<>();
		return startProcess(procDefKey, businessTable, businessId, title, vars);
	}

	/**
	 * 启动流程
	 * @param procDefKey    流程定义KEY
	 * @param businessTable 业务表表名
	 * @param businessId	业务表编号
	 * @param title	    流程标题，显示在待办任务标题
	 * @param vars		    流程变量
	 * @return 流程实例ID
	 */
	@Transactional(readOnly = false)
	public String startProcess(String procDefKey, String businessTable, String businessId, String title, Map<String, Object> vars) {
		String userId = UserUtils.getUser().getLoginname();//ObjectUtils.toString(UserUtils.getUser().getId())

		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(userId);

		// 设置流程变量
		if (vars == null){
			vars = new HashMap<>();
		}


		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(procDefKey).latestVersion().singleResult();
		// 先读取表单字段在根据表单字段的ID读取请求参数值
		Map<String, String> formValues = new HashMap<String, String>();
		StartFormData formData = formService.getStartFormData(processDefinition.getId());
//		Object renderedStartForm = formService.getRenderedStartForm(processDefinition.getId());
		// 从请求中获取表单字段的值
		List<FormProperty> formProperties = formData.getFormProperties();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		for (FormProperty formProperty : formProperties) {
			String value = request.getParameter(formProperty.getId());
			formValues.put(formProperty.getId(), value);
		}

		// 设置流程标题
		if (org.apache.commons.lang3.StringUtils.isNotBlank(title)){
			vars.put("title", title);
		}

		// 启动流程
		ProcessInstance procIns = runtimeService.startProcessInstanceByKey(procDefKey, businessTable+":"+businessId, vars);

		// 更新业务表流程实例ID
		EamBusinessTask act = new EamBusinessTask();
		act.setBusinessTable(businessTable);// 业务表名
		act.setBusinessId(businessId);	// 业务表ID
		act.setProcInsId(procIns.getId());

		//actDao.updateProcInsIdByBusinessId(act);
		return act.getProcInsId();
	}


	/**
	 * @creator Douglas
	 * @createtime 2017-9-5 10:51
	 * @description: 根据流程定义key启动流程
	 * @param procDefKey
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false)
	public String startProcessByPdid(String procDefKey,String businessTable, String businessId,HttpServletRequest request) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(procDefKey).latestVersion().singleResult();
		boolean hasStartFormKey = processDefinition.hasStartFormKey();
		String pdid = processDefinition.getId();
		Map<String, String> formValues = new HashMap<String, String>();

		if (hasStartFormKey) { // formkey表单
			Map<String, String[]> parameterMap = request.getParameterMap();
			Set<Map.Entry<String, String[]>> entrySet = parameterMap.entrySet();
			for (Map.Entry<String, String[]> entry : entrySet) {
				String key = entry.getKey();
				formValues.put(key, entry.getValue()[0]);
			}
		} else { // 动态表单
			// 先读取表单字段在根据表单字段的ID读取请求参数值
			StartFormData formData = formService.getStartFormData(pdid);

			// 从请求中获取表单字段的值
			List<FormProperty> formProperties = formData.getFormProperties();
			for (FormProperty formProperty : formProperties) {
				String value = request.getParameter(formProperty.getId());
				formValues.put(formProperty.getId(), value);
			}
		}

		// 获取当前登录的用户
		User user = UserUtils.getUser();
		// 用户未登录不能操作，实际应用使用权限框架实现，例如Spring Security、Shiro等
		if (user == null || org.apache.commons.lang3.StringUtils.isBlank(user.getId())) {
			return "timeout";
		}
		//设置流程启动者与启动时间
		identityService.setAuthenticatedUserId(user.getLoginname());


		// 提交表单字段并启动一个新的流程实例
		//ProcessInstance processInstance = runtimeService.startProcessInstanceById(pdid,businessTable+":"+businessId, formValues);
		ProcessInstance processInstance = formService.submitStartFormData(pdid,businessTable+":"+businessId, formValues);
		//logger.debug("start a processinstance: {}", processInstance);
		//redirectAttributes.addFlashAttribute("message", "流程已启动，实例ID：" + processInstance.getId());
		//return "redirect:/chapter5/process-list-view";
		return processInstance.getProcessInstanceId();
	}

	/**
	 * @creator wangr
	 * @createtime 2017/10/13 0013 上午 11:09
	 * @description: 根据流程定义key启动流程  用于APP启动流程 获取用户的方式和web不一样
	 *               维护启动流程的代码麻烦将此方法也一同维护
	 * @param procDefKey
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false)
	public String startAppProcessByPdid(String procDefKey,String businessTable, String businessId,String userId,HttpServletRequest request) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(procDefKey).latestVersion().singleResult();
		boolean hasStartFormKey = processDefinition.hasStartFormKey();
		String pdid = processDefinition.getId();
		Map<String, String> formValues = new HashMap<String, String>();

		if (hasStartFormKey) { // formkey表单
			Map<String, String[]> parameterMap = request.getParameterMap();
			Set<Map.Entry<String, String[]>> entrySet = parameterMap.entrySet();
			for (Map.Entry<String, String[]> entry : entrySet) {
				String key = entry.getKey();
				formValues.put(key, entry.getValue()[0]);
			}
		} else { // 动态表单
			// 先读取表单字段在根据表单字段的ID读取请求参数值
			StartFormData formData = formService.getStartFormData(pdid);

			// 从请求中获取表单字段的值
			List<FormProperty> formProperties = formData.getFormProperties();
			for (FormProperty formProperty : formProperties) {
				String value = request.getParameter(formProperty.getId());
				formValues.put(formProperty.getId(), value);
			}
		}

		// 用户
		if (org.apache.commons.lang3.StringUtils.isBlank(userId)) {
			return "timeout";
		}
		//设置流程启动者
		identityService.setAuthenticatedUserId(userId);

		// 提交表单字段并启动一个新的流程实例
		ProcessInstance processInstance = formService.submitStartFormData(pdid,businessTable+":"+businessId, formValues);
		//logger.debug("start a processinstance: {}", processInstance);
		//redirectAttributes.addFlashAttribute("message", "流程已启动，实例ID：" + processInstance.getId());
		//return "redirect:/chapter5/process-list-view";
		return processInstance.getProcessInstanceId();
	}

	/**
	 * 结束流程
	 *
	 * @param procInstId
	 * @return
	 */
	public String stopProcessInstance(String procInstId) {
		return "unsupport";
	}

	/**
	 * 获取全局流程变量
	 * @param variableName 对象名
	 * @param executionId 流程实例名
	 * @return
	 */
	public Object getProcInsVar(String executionId, String variableName)
	{
		try {
			return runtimeService.getVariable(executionId, variableName);
		}
		catch (ActivitiObjectNotFoundException e){
			logger.warn("GetProcInsVar fail,the process instance is not exist,executionId:["+executionId+"]");
			return null;
		}
	}

	/**
	 * 获取全局流程变量map
	 * @param executionId 流程实例id
	 * @return null或者map
	 */
	public Map<String,Object> getProcInsVars(String executionId)
	{
		try {
			return runtimeService.getVariables(executionId);
		}
		catch (ActivitiObjectNotFoundException e){
			logger.warn("GetProcInsVars fail,the process instance is not exist,executionId:["+executionId+"]");
			return null;
		}
	}

	/**
	 * 设置全局流程变量
	 * @param executionId 流程id
	 * @param varName 变量名
	 * @param value 变量对象
	 * @return 1：成功，0：失败
	 */
	public int setProcInsVar(String executionId, String varName, Object value)
	{
		try {
			runtimeService.setVariable(executionId, varName, value);
			return 1;
		}
		catch (ActivitiObjectNotFoundException e){
			logger.warn("SetProcInsVar fail,the process instance is not exist,executionId:["+executionId+"]");
		}
		return 0;
	}

	/**
	 * 设置全局流程变量map
	 * @param executionId 流程id
	 * @param map 变量map
	 * @return 1：成功，0：失败
	 */
	public int setProcInsVarMap(String executionId, Map<String, Object> map)
	{
		try {
			runtimeService.setVariables(executionId,map);
			return 1;
		}
		catch (ActivitiObjectNotFoundException e){
			logger.warn("SetProcInsVarMap fail,the process instance is not exist,executionId:["+executionId+"]");
		}
		return 0;
	}


	/**
	 * @creator caoh
	 * @createtime 2017-12-1 16:26
	 * @description: 获取流程流转详细信息
	 * @return
	 */
	public String getProcessInstTaskDetail(String processInstanceId){
		// 查询历史流程实例
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		//定义数组返回所有活动记录数据
		List activityList = new ArrayList();
		// 查询所有的历史活动记录
		List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
		/**循环所有活动记录，查询参与人列表，包括任务办理人和未操作者---start*/
		for(HistoricActivityInstance activityInstance:activityInstances){

			Map activityMap = new HashMap();
			//存放任务参与者的List
			List<Map> detailList = new ArrayList<>();
			if("startEvent".equals(activityInstance.getActivityType())){
				activityMap.put("node","开始节点");//设置开始节点名称
			}else{
				activityMap.put("node",activityInstance.getActivityName()==null?"":activityInstance.getActivityName());//设置任务节点名称
			}

			int operatorNum = 0;
			int submit = 0;
			//获取活动的任务id
			String tskId = activityInstance.getTaskId();
			//String assignee = activityInstance.getAssignee()==null?"":activityInstance.getAssignee();
			List<String> distinctusers = new ArrayList();
			List<String> assigneeusers = new ArrayList();
			//如果taskId为null，则该任务可能为开始节点、系统任务、或者网关
			if(tskId==null ){
				if(activityInstance.getActivityType().equals("startEvent")){//开始节点
					activityMap.put("operatorNum","1");//设置任务参与者人数
					activityMap.put("submit","1");//设置任务已提交人数
					Map operatorMap = new HashMap();
					operatorMap.put("operator",historicProcessInstance.getStartUserId()==null?"":historicProcessInstance.getStartUserId());
					operatorMap.put("status","已提交");
					operatorMap.put("accepttime",sdf.format(activityInstance.getStartTime()));
					operatorMap.put("submittime",activityInstance.getEndTime()==null?"":sdf.format(activityInstance.getEndTime()));
					operatorMap.put("duration",activityInstance.getDurationInMillis()==null?"":calculateSeconds(activityInstance.getDurationInMillis()));
					detailList.add(operatorMap);
					activityMap.put("detail",detailList);
					activityList.add(activityMap);
					continue;
				}else if(activityInstance.getActivityType().equals("serviceTask")){
					activityMap.put("operatorNum",0);//设置任务参与者人数
					activityMap.put("submit",0);//设置任务已提交人数
					Map operatorMap = new HashMap();
					operatorMap.put("operator","系统");
					operatorMap.put("status","已运行");
					operatorMap.put("accepttime",sdf.format(activityInstance.getStartTime()));
					operatorMap.put("submittime",activityInstance.getEndTime()==null?"":sdf.format(activityInstance.getEndTime()));
					operatorMap.put("duration",activityInstance.getDurationInMillis()==null?"":calculateSeconds(activityInstance.getDurationInMillis()));
					detailList.add(operatorMap);
					activityMap.put("detail",detailList);
					activityList.add(activityMap);
					continue;
				}else if(activityInstance.getActivityType().equals("exclusiveGateway")){
					continue;
				}

			}else{
				// 读取任务参与人列表

				List<HistoricIdentityLink> identityLinksForTask = historyService.getHistoricIdentityLinksForTask(tskId);
				//identityService.getIdentityLinks(tskId);


				//将重复的候选人筛选出来，并去重

				for(HistoricIdentityLink identity:identityLinksForTask){
					if(identity.getGroupId()!=null){//如果为候选组，查出组内所有用户
						List<String> groupusers = eamRoleDao.getUserIdByRole(identity.getGroupId());
						for(String user:groupusers){//遍历该组，将不重复的用户放入distinctusers
							if(distinctusers.indexOf(user)==-1){
								distinctusers.add(user);
							}
						}
					}else if(identity.getUserId()!=null && identity.getType().equals("candidate")){
						String user = identity.getUserId();
						if(distinctusers.indexOf(user)==-1){
							distinctusers.add(user);
						}
					}else if(identity.getUserId()!=null && identity.getType().equals("assignee")){
						String user = identity.getUserId();
						assigneeusers.add(user);
						if(distinctusers.indexOf(user)==-1){
							distinctusers.add(user);
						}
					}
				}

				for(String user:distinctusers){
					Map operatorMap = new HashMap();
					operatorMap.put("operator",user);
					operatorMap.put("accepttime",sdf.format(activityInstance.getStartTime()));


					if(assigneeusers.indexOf(user)!=-1){
						//已签收但未完成
						if(activityInstance.getEndTime()==null){
							operatorMap.put("status","已签收");
							operatorMap.put("submittime","");
							operatorMap.put("duration","");
						}else{
							operatorMap.put("status","已提交");
							operatorMap.put("submittime",activityInstance.getEndTime()==null?"":sdf.format(activityInstance.getEndTime()));
							operatorMap.put("duration",activityInstance.getDurationInMillis()==null?"":calculateSeconds(activityInstance.getDurationInMillis()));
							submit++;
						}
					}else{
						operatorMap.put("status","未签收");
						operatorMap.put("submittime","");
						operatorMap.put("duration","");
					}
					detailList.add(operatorMap);
				}

				//mav.addObject("identityLinksForTask", identityLinksForTask);

			}

			activityMap.put("operatorNum",distinctusers.size());//设置待办总人数
			activityMap.put("submit",submit);			//设置已提交人数
			activityMap.put("detail",detailList);		//设置任务完成明细

			activityList.add(activityMap);

		}
		/**循环所有活动记录，查询参与人列表，包括任务办理人和未操作者---end*/
		String json = JSON.toJSONString(activityList);
		return json;
	}

	/**
	 * @creator caoh
	 * @createtime 2017-11-30 14:09
	 * @description: 根据毫米数计算天/时/分/秒/毫秒
	 * @param durationInMillis
	 * @return
	 */
	private String calculateSeconds(Long durationInMillis){
		String duration="";
		long millisecond=0,seconds=0,minutes=0,hours=0,days=0;
		if(durationInMillis<1000){		//时间<1s
			duration = durationInMillis+"毫秒";
		}else if(1000<=durationInMillis&&durationInMillis<(60*1000)){		//时间小于1min
			seconds = durationInMillis/1000;
			millisecond = durationInMillis%1000;
			duration = seconds+"秒"+millisecond+"毫秒";
		}else if((60*1000)<=durationInMillis && durationInMillis<(60*60*1000)){//时间小于1hour
			minutes = durationInMillis/(60*1000);
			minutes=(minutes==0?1:minutes);
			seconds = (durationInMillis%(60*1000))/1000;
			millisecond = (durationInMillis%(minutes*60*1000))%1000;
			duration = minutes+"分"+seconds+"秒"+millisecond+"毫秒";
		}else if((60*60*1000)<=durationInMillis && durationInMillis<(24*60*60*1000)){//时间小于1day
			hours = durationInMillis/(60*60*1000);
			hours=(hours==0?1:hours);
			minutes = (durationInMillis%(60*60*1000))/(60*1000);
			minutes=(minutes==0?1:minutes);
			seconds = (durationInMillis%(hours*60*60*1000))%(minutes*60*1000)/1000;
			millisecond = (durationInMillis%(hours*60*60*1000))%(minutes*60*1000)%1000;
			duration = hours+"时"+minutes+"分"+seconds+"秒"+millisecond+"毫秒";
		}else if(durationInMillis>=(24*60*60*1000)){//时间大于1day
			days = durationInMillis/(24*60*60*1000);
			days=(days==0?1:days);
			hours = (durationInMillis%(24*60*60*1000))/(60*60*1000);
			hours=(hours==0?1:hours);
			minutes = durationInMillis%(days*24*60*60*1000)%(hours*60*60*1000)/(60*1000);
			minutes=(minutes==0?1:minutes);
			seconds = durationInMillis%(days*24*60*60*1000)%(hours*60*60*1000)%(minutes*60*1000)/1000;
			millisecond = durationInMillis%(days*24*60*60*1000)%(hours*60*60*1000)%(minutes*60*1000)%1000;
			duration = days+"天"+hours+"时"+minutes+"分"+seconds+"秒"+millisecond+"毫秒";
		}

		return duration;
	}

	/**
	 *
	 *@User   :Test
	 *@date   :2014-6-27 上午09:38:36
	 *@return :Set
	 *@userFor :获得任务中的办理候选人
	 */
	private List getTaskCandidate(String taskId) {
		List users = new ArrayList();
		List identityLinkList = taskService.getIdentityLinksForTask(taskId);
		if (identityLinkList != null && identityLinkList.size() > 0) {
			for (Iterator iterator = identityLinkList.iterator(); iterator
					.hasNext();) {
				IdentityLink identityLink = (IdentityLink) iterator.next();
				if (identityLink.getUserId() != null) {
					UserEntity user = getUser(identityLink.getUserId());
					if (user != null)
						users.add(user);
				}
				if (identityLink.getGroupId() != null) {
					/*// 根据组获得对应人员
					List userList = identityService.createUserQuery()
							.memberOfGroup(identityLink.getGroupId()).list();
					if (userList != null && userList.size() > 0)
						users.addAll(userList);*/
				}
			}

		}
		return users;
	}

	private UserEntity getUser(String userId) {
		UserEntity user =  actEamUserEntityService.findUserById(userId);
		return user;
	}



}
