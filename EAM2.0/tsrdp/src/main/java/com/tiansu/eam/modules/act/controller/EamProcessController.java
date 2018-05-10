/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.modules.act.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.act.service.EamProcessService;
import com.tiansu.eam.modules.act.service.ext.ActEamUserEntityService;
import com.tiansu.eam.modules.act.utils.ActivitiUtil;
import com.tiansu.eam.modules.sys.dao.EamRoleDao;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.*;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.*;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.SubProcessActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.ReadOnlyProcessDefinition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.activiti.engine.identity.User;

/**
 * 流程定义相关Controller
 * @author ThinkGem
 * @version 2013-11-03
 */
@Controller
@RequestMapping(value = "${adminPath}/eam/act/process")
public class EamProcessController extends BaseController {

	@Autowired
	private EamProcessService eamProcessService;

	@Autowired
	RepositoryService repositoryService;

	@Autowired
	FormService formService;

	@Autowired
	IdentityService identityService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	ProcessEngineConfiguration processEngineConfiguration;

	@Autowired
	ActEamUserEntityService actEamUserEntityService;

	@Autowired
	private EamRoleDao eamRoleDao;

	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 新建流程页面
	 */
	//@RequiresPermissions("act:process:edit")
	@ResponseBody
	@RequestMapping(value = {"list", ""})
	public List processList(String category) {

		return eamProcessService.processList(category);


	}

	/**
	 * 流程定义列表
	 */
	//@RequiresPermissions("act:process:edit")
	@ResponseBody
	@RequestMapping(value = {"listPage", ""})
	public Map processListPage(String category) {
		return eamProcessService.processListPage(category);
	}


	/**
	 * @creator caoh
	 * @createtime 2017-8-31 11:37
	 * @description: 打开启动表单
	 * @return 表单url
	 */
	@RequestMapping(value = "startForm")
	public ModelAndView startForm() {
		//return eamProcessService.processList(category);
		String processDefinitionId = getPara("processDefinitionId");
		//formService.getStartFormKey();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		boolean hasStartFormKey = processDefinition.hasStartFormKey();

		// 根据是否有formkey属性判断使用哪个展示层
		String viewName = "modules/act/actTaskStart";
		ModelAndView mav = new ModelAndView();
		String url=null;
		if(hasStartFormKey){
			url = eamProcessService.getStartFormKey(processDefinitionId);
			mav.setViewName(url);
		}else{
			mav.setViewName(viewName);
			StartFormData startFormData = formService.getStartFormData(processDefinitionId);
			mav.addObject("startFormData", startFormData);
			mav.addObject("processDefinition", processDefinition);
		}


		//StartFormData startFormData = formService.getStartFormData(processDefinitionId);
		return mav;
	}

	/**
	 * @creator Douglas
	 * @createtime 2017-9-7 14:22
	 * @description: 获取审批节点表单及数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getTaskForm")
	public ModelAndView readTaskForm() throws Exception {
		//String viewName = "chapter6/task-form";
		String taskId = getPara("taskId");
		//根据taskid获取tasDdefinitionKey
		String taskDefKey = taskService.createTaskQuery().taskId(taskId).singleResult().getTaskDefinitionKey();

		String pdId = getPara("pdId");
		String pIid= getPara("pIid");
		String htmlpath="";
		if(pdId.indexOf("library_approvenew") != -1){
			htmlpath = "modules/act/actLibraryApprove";
		} else if(pdId.indexOf("operationwork_approval") != -1){
			htmlpath = "modules/act/actOperaWorkApproval";
		}else if(pdId.indexOf("stand_order") != -1){
			htmlpath = "modules/act/actStandOrderApprove";
		}else if(pdId.indexOf(EAMConsts.FAULT_ORDER_FLOWDEF) != -1 && "order_feedback".equals(taskDefKey)){
			htmlpath = "modules/faultOrder/faultOrderFeedBack";
		}else if(pdId.indexOf(EAMConsts.FAULT_ORDER_FLOWDEF) != -1 && "order_manual_disp".equals(taskDefKey)){
			htmlpath = "modules/faultOrder/faultOrderDisp";
		}else if(pdId.indexOf("inspectionRoute_approve") != -1){
			htmlpath = "modules/act/actInspectionRouteApprove";
		}else if(pdId.indexOf("inspectionRoute_closed") != -1){
			htmlpath = "modules/act/actInspectionRouteClosed";
		}else if(pdId.indexOf("maintMon_approve") != -1){
			htmlpath = "modules/act/actmaintainMonApprove";
		}else if(pdId.indexOf("maintAnnual_approve") != -1){
			htmlpath = "modules/act/actmaintainAnnualApprove";
		}else if(pdId.indexOf("inspectionFlow") != -1 && !"inspectionTask_feedback".equals(taskDefKey)) { // 除了反馈页面都加基本信息
			htmlpath = "modules/act/actTransferSelect";
		} else if(pdId.indexOf("inspectionFlow") != -1 && "inspectionTask_feedback".equals(taskDefKey)){ // 反馈页面单独页面
			htmlpath = "modules/inspection/inspectionTaskFeedBack2";
//
//		else if(pdId.indexOf("inspectionFlow") != -1) {
//			htmlpath = "modules/act/actTransferSelect";
		}else if(pdId.indexOf("GDFK") != -1) { // 除了反馈页面都加基本信息
			htmlpath = "modules/maintain/maintainTaskFeedBack";
		}else if(pdId.indexOf("maintainFlow") != -1 && "taskFeedBack".equals(taskDefKey)) { //保养反馈页面
			htmlpath = "modules/maintain/maintainTaskFeedBack";
		}else{
			htmlpath = "modules/act/actTaskApprove";
		}


		//获取当前任务节点表单
		String taskForm = formService.getTaskFormKey(pdId,taskDefKey);
		//ModelAndView mav = new ModelAndView(taskForm.substring(0,taskForm.indexOf(".jsp")));
		ModelAndView mav = new ModelAndView(htmlpath);
		//获取任务表单内容
		TaskFormData taskFormData = formService.getTaskFormData(taskId);
		Task task = null;

		// 外置表单
		if (taskFormData != null && taskFormData.getFormKey() != null) {
			//Object renderedTaskForm = formService.getRenderedTaskForm(taskId,"myFormEngine");
			Object renderedTaskForm = formService.getRenderedTaskForm(taskId);
			task = taskService.createTaskQuery().taskId(taskId).singleResult();
			mav.addObject("taskFormData", renderedTaskForm);
			mav.addObject("hasFormKey", true);
		} else if (taskFormData != null) { // 动态表单
			mav.addObject("taskFormData", taskFormData);
			task = taskFormData.getTask();
		} else { // 手动创建的任务（包括子任务）
			task = taskService.createTaskQuery().taskId(taskId).singleResult();
			mav.addObject("manualTask", true);
		}

		String processInstanceId = task.getProcessInstanceId();

		mav.addObject("task", task);
		mav.addObject("pIid",pIid);
		mav.addObject("pstid",processInstanceId);

		// 查询历史流程实例
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(pIid).singleResult();
		// 查询流程定义对象
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult();

		mav.addObject("processDefinition",processDefinition);

		mav.addObject("businessKey",historicProcessInstance.getBusinessKey());

		//流程流转详细信息
		String json = eamProcessService.getProcessInstTaskDetail(pIid);

		mav.addObject("activityList",json);

		return mav;
	}


	/**
	 * @creator caoh
	 * @createtime 2017-10-28 16:25
	 * @description: 运行中的实例列表
	 *
	 */
	//@RequiresPermissions("act:process:edit")
	@ResponseBody
	@RequestMapping(value = "running")
	public Map<String,Object> runningList(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String,String> param = getFormMap();
		String procDefName=null;
		if(param.containsKey("processDefName")){
			procDefName = param.get("processDefName").toString();
		}
		//String procDefName = mp.get()
		Map<String,Object> map = eamProcessService.runningList(procDefName);

	    return map;
	}

	/**
	 * @creator caoh
	 * @createtime 2017-11-28 16:25
	 * @description: 我发起的流程
	 *
	 */
	//@RequiresPermissions("act:process:edit")
	@ResponseBody
	@RequestMapping(value = "myRequest")
	public Map<String,Object> myRequest(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String,String> param = getFormMap();
		String procDefName=null;
		if(param.containsKey("processDefName")){
			procDefName = param.get("processDefName").toString();
		}
		//String procDefName = mp.get()
		Map<String,Object> map = eamProcessService.myRequest(procDefName);

		return map;
	}


	/**
	 * @creator Douglas
	 * @createtime 2017-9-12 16:50
	 * @description: 查询已结束流程实例
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "finished")
	public Map<String,Object> finishedProcessInstanceList() {
		Map<String,String> param = getFormMap();

		//查询条件-流程名称
		String processName=null;
		if(param.containsKey("processName")){
			processName = param.get("processName").toString();
		}
		Map<String,Object> map = eamProcessService.finishedList(processName);

		return map;

	}


	/**@creator Douglas
	 * @createtime 2017-9-12 18:04
	 * @description:读取运行中流程历史数据
	 * @return
	 */
	@RequestMapping(value = "trace/view")
	public ModelAndView historyDatas() {
		String executionId = getPara("executionId");

		ModelAndView mav = new ModelAndView("modules/act/actTraceprocess");

		// 查询Execution对象
		Execution execution = runtimeService.createExecutionQuery().executionId(executionId).singleResult();

		// 查询所有的历史活动记录
		String processInstanceId = execution.getProcessInstanceId();
		List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();

		// 查询历史流程实例
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();

		// 查询流程有关的变量
		List<HistoricVariableInstance> variableInstances = historyService.createHistoricVariableInstanceQuery()
				.processInstanceId(processInstanceId).list();

		List<HistoricDetail> formProperties = historyService.createHistoricDetailQuery().processInstanceId(processInstanceId).formProperties().list();

		// 查询流程定义对象
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult();

		// 查询运行时流程实例
		ProcessInstance parentProcessInstance = runtimeService.createProcessInstanceQuery()
				.subProcessInstanceId(execution.getProcessInstanceId()).singleResult();

		//流程流转详细信息
		String json = eamProcessService.getProcessInstTaskDetail(historicProcessInstance.getId());
		mav.addObject("activityList",json);

		mav.addObject("parentProcessInstance", parentProcessInstance);
		mav.addObject("historicProcessInstance", historicProcessInstance);
		mav.addObject("variableInstances", variableInstances);
		mav.addObject("activities", activityInstances);
		mav.addObject("formProperties", formProperties);
		mav.addObject("processDefinition", processDefinition);
		mav.addObject("executionId", executionId);

		return mav;
	}


	/**
	 * @creator Douglas
	 * @createtime 2017-9-16 11:37
	 * @description: 只跟踪流程图信息
	 * @return
	 */
	@RequestMapping(value = "trace/flow")
	public ModelAndView folwDatas() {
		String executionId = getPara("executionId");

		ModelAndView mav = new ModelAndView("modules/act/actTraceFlow");

		// 查询Execution对象
		Execution execution = runtimeService.createExecutionQuery().executionId(executionId).singleResult();

		// 查询所有的历史活动记录
		String processInstanceId = execution.getProcessInstanceId();
		List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();

		// 查询历史流程实例
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();

		// 查询流程有关的变量
		List<HistoricVariableInstance> variableInstances = historyService.createHistoricVariableInstanceQuery()
				.processInstanceId(processInstanceId).list();

		List<HistoricDetail> formProperties = historyService.createHistoricDetailQuery().processInstanceId(processInstanceId).formProperties().list();

		// 查询流程定义对象
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult();

		// 查询运行时流程实例
		ProcessInstance parentProcessInstance = runtimeService.createProcessInstanceQuery()
				.subProcessInstanceId(execution.getProcessInstanceId()).singleResult();

		//mav.addObject("parentProcessInstance", parentProcessInstance);
		mav.addObject("historicProcessInstance", historicProcessInstance);
		//mav.addObject("variableInstances", variableInstances);
		//mav.addObject("activities", activityInstances);
		//mav.addObject("formProperties", formProperties);
		mav.addObject("processDefinition", processDefinition);
		mav.addObject("executionId", executionId);

		return mav;
	}


	/**@creator Douglas
	 * @createtime 2017-9-12 18:04
	 * @description:读取运行中流程历史数据
	 * @return
	 */
	@RequestMapping(value = "trace/finish/view")
	public ModelAndView historyDatasFinished() {
		String processInstanceId = getPara("processInstanceId");

		ModelAndView mav = new ModelAndView("modules/act/actTraceFinishedprocess");

 		List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();

		// 查询历史流程实例
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();

		// 查询流程有关的变量
		List<HistoricVariableInstance> variableInstances = historyService.createHistoricVariableInstanceQuery()
				.processInstanceId(processInstanceId).list();

		List<HistoricDetail> formProperties = historyService.createHistoricDetailQuery().processInstanceId(processInstanceId).formProperties().list();

		// 查询流程定义对象
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult();

		mav.addObject("historicProcessInstance", historicProcessInstance);
		mav.addObject("variableInstances", variableInstances);
		mav.addObject("activities", activityInstances);
		mav.addObject("formProperties", formProperties);
		mav.addObject("processDefinition", processDefinition);

		return mav;
	}




	/**
	 *@creator Douglas
	 * @createtime 2017-9-8 16:40
	 * @description: 读取流程资源
	 * @param processDefinitionId 流程定义ID
	 * @param resourceName        资源名称
	 */
	@RequestMapping(value = "/read-resource")
	public void readResource(@RequestParam("pdid") String processDefinitionId,
							 @RequestParam("resourceName") String resourceName, HttpServletResponse response)
			throws Exception {
		ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
		ProcessDefinition pd = pdq.processDefinitionId(processDefinitionId).singleResult();

		// 通过接口读取
		InputStream resourceAsStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), resourceName);

		// 输出资源内容到相应对象
		byte[] b = new byte[1024];
		int len;
		while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
	}

	/**
	 * 读取流程资源
	 */
	@RequestMapping(value = "/trace/data/auto/{executionId}")
	public void readResource(@PathVariable("executionId") String executionId, HttpServletResponse response)
			throws Exception {
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(executionId).singleResult();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
		List<String> activeActivityIds = runtimeService.getActiveActivityIds(executionId);
		ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
		List<String> highLightedFlows = getEamHighLightedFlows(processDefinition, processInstance.getId());
		InputStream imageStream =diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds, highLightedFlows);

		// 输出资源内容到相应对象
		byte[] b = new byte[1024];
		int len;
		while ((len = imageStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
	}

	/**
	 * 获取流程图高亮
	 * @param processDefinition
	 * @param processInstanceId
	 * @return
	 */
	private List<String> getEamHighLightedFlows(ProcessDefinitionEntity processDefinition, String processInstanceId) {
		List<String> highLightedFlows = new ArrayList<String>();
		List<HistoricActivityInstance> historicActivityInstances = historyService
				.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId)
				.orderByHistoricActivityInstanceStartTime().asc().list();

		List<String> historicActivityInstanceList = new ArrayList<String>();
		for (HistoricActivityInstance hai : historicActivityInstances) {
			historicActivityInstanceList.add(hai.getActivityId());
		}

		// add current activities to list
		List<String> highLightedActivities = runtimeService.getActiveActivityIds(processInstanceId);
		historicActivityInstanceList.addAll(highLightedActivities);

		// activities and their sequence-flows
		for (ActivityImpl activity : processDefinition.getActivities()) {
			int index = historicActivityInstanceList.indexOf(activity.getId());

			if (index >= 0 && index + 1 < historicActivityInstanceList.size()) {
				List<PvmTransition> pvmTransitionList = activity
						.getOutgoingTransitions();
				for (PvmTransition pvmTransition : pvmTransitionList) {
					String destinationFlowId = pvmTransition.getDestination().getId();
					if (destinationFlowId.equals(historicActivityInstanceList.get(index + 1))) {
						highLightedFlows.add(pvmTransition.getId());
					}
				}
			}
		}
		return highLightedFlows;
	}

	/**
	 *@creator Douglas
	 * @createtime 2017-9-8 16:43
	 * @description: 读取跟踪运行中流程数据
	 * @param executionId
	 * @return 返回流程信息
	 * @throws Exception
	 */
	@RequestMapping(value = "/trace/data/{executionId}")
	@ResponseBody
	public List<Map<String, Object>> readActivityDatas(@PathVariable("executionId") String executionId) throws Exception {
		ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(executionId).singleResult();
		List<String> activeActivityIds = runtimeService.getActiveActivityIds(executionId);
		//List<String> activeActivityIds = historyService.
		RepositoryServiceImpl repositoryServiceImpl = (RepositoryServiceImpl) repositoryService;
		ReadOnlyProcessDefinition deployedProcessDefinition = repositoryServiceImpl
				.getDeployedProcessDefinition(executionEntity.getProcessDefinitionId());

		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) deployedProcessDefinition;
		List<ActivityImpl> activitiList = processDefinition.getActivities();//获得当前任务的所有节点

		List<Map<String, Object>> activityInfos = new ArrayList<Map<String, Object>>();
		for (ActivityImpl activity : activitiList) {

			ActivityBehavior activityBehavior = activity.getActivityBehavior();

			boolean currentActiviti = false;
			// 当前节点
			String activityId = activity.getId();
			if (activeActivityIds.contains(activityId)) {
				currentActiviti = true;
			}
			Map<String, Object> activityImageInfo = packageSingleActivitiInfo(activity, executionEntity.getId(), currentActiviti);
			activityInfos.add(activityImageInfo);

			// 处理子流程
			if (activityBehavior instanceof SubProcessActivityBehavior) {
				List<ActivityImpl> innerActivityList = activity.getActivities();
				for (ActivityImpl innerActivity : innerActivityList) {
					String innerActivityId = innerActivity.getId();
					if (activeActivityIds.contains(innerActivityId)) {
						currentActiviti = true;
					} else {
						currentActiviti = false;
					}
					activityImageInfo = packageSingleActivitiInfo(innerActivity, executionEntity.getId(), currentActiviti);
					activityInfos.add(activityImageInfo);
				}
			}

		}

		return activityInfos;
	}


	/**
	 * 设置流程分类
	 */
	//@RequiresPermissions("act:process:edit")
	@RequestMapping(value = "updateCategory")
	public String updateCategory(String procDefId, String category, RedirectAttributes redirectAttributes) {
		eamProcessService.updateCategory(procDefId, category);
		return "redirect:" + adminPath + "/act/process";
	}

	/**
	 * 挂起、激活流程实例
	 */
	//@RequiresPermissions("act:process:edit")
	@RequestMapping(value = "update/{state}")
	public String updateState(@PathVariable("state") String state, String procDefId, RedirectAttributes redirectAttributes) {
		String message = eamProcessService.updateState(state, procDefId);
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:" + adminPath + "/act/process";
	}

	/**@creator caoh
	 * @createtime 2017-9-12 19:17
	 * @description: 转换流程定义为模型
	 * @param processDefinitionId
	 *
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws XMLStreamException
	 */
	@ResponseBody
	@RequestMapping(value = "/convert-to-model/{processDefinitionId}")
	public String convertToModel(@PathVariable("processDefinitionId") String processDefinitionId)
			throws UnsupportedEncodingException, XMLStreamException {

		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
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
		modelData.setCategory(processDefinition.getDeploymentId());

		ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
		modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
		modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
		modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
		modelData.setMetaInfo(modelObjectNode.toString());

		repositoryService.saveModel(modelData);

		repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));
		return "success";


	}
	
	/**
	 * 导出图片文件到硬盘
	 */
	//@RequiresPermissions("act:process:edit")
	@RequestMapping(value = "export/diagrams")
	@ResponseBody
	public List<String> exportDiagrams(@Value("#{APP_PROP['activiti.export.diagram.path']}") String exportDir) throws IOException {
		List<String> files = eamProcessService.exportDiagrams(exportDir);;
		return files;
	}


	
	/**
	 * 删除流程实例
	 * @param procInsId 流程实例ID
	 * @param reason 删除原因
	 */
	//@RequiresPermissions("act:process:edit")
	@RequestMapping(value = "deleteProcIns")
	public String deleteProcIns(String procInsId, String reason, RedirectAttributes redirectAttributes) {
		if (StringUtils.isBlank(reason)){
			addMessage(redirectAttributes, "请填写删除原因");
		}else{
			eamProcessService.deleteProcIns(procInsId, reason);
			addMessage(redirectAttributes, "删除流程实例成功，实例ID=" + procInsId);
		}
		return "redirect:" + adminPath + "/act/process/running/";
	}



	/**
	 *@creator caoh
	 * @createtime 2017-9-8 16:44
	 * @description: 封装输出信息，包括：当前节点的X、Y坐标、变量信息、任务类型、任务描述
	 * @param activity
	 * @param currentActiviti
	 * @return
	 */
	private Map<String, Object> packageSingleActivitiInfo(ActivityImpl activity, String executionId,
														  boolean currentActiviti) throws Exception {
		Map<String, Object> activityInfo = new HashMap<String, Object>();
		activityInfo.put("currentActiviti", currentActiviti);

		// 设置图形的XY坐标以及宽度、高度
		setSizeAndPositonInfo(activity, activityInfo);

		Map<String, Object> vars = new HashMap<String, Object>();
		Map<String, Object> properties = activity.getProperties();
		vars.put("任务类型", ActivitiUtil.getZhActivityType(properties.get("type").toString()));
		vars.put("任务名称", properties.get("name"));

		// 当前节点的task
		if (currentActiviti) {
			setCurrentTaskInfo(executionId, activity.getId(), vars);
		}

		logger.debug("trace variables: {}", vars);
		activityInfo.put("vars", vars);
		return activityInfo;
	}


	/**
	 *@creator caoh
	 * @createtime 2017-9-8 16:44
	 * @description: 获取当前节点信息
	 * @return
	 */
	private void setCurrentTaskInfo(String executionId, String activityId, Map<String, Object> vars) {
		Task currentTask = taskService.createTaskQuery().executionId(executionId)
				.taskDefinitionKey(activityId).singleResult();
		logger.debug("current task for processInstance: {}", ToStringBuilder.reflectionToString(currentTask));

		if (currentTask == null) return;

		String assignee = currentTask.getAssignee();
		if (assignee != null) {
			//org.activiti.engine.identity.User assigneeUser = identityService.createUserQuery().userId(assignee).singleResult();
			org.activiti.engine.identity.User assigneeUser = actEamUserEntityService.findUserById(assignee);//userId(assignee).singleResult();
			String userInfo = assigneeUser.getFirstName() +"/"+ assigneeUser.getId();
			vars.put("当前处理人", userInfo);
			vars.put("创建时间", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(currentTask.getCreateTime()));
		} else {
			vars.put("任务状态", "未签收");
		}

	}

	/**
	 *@creator caoh
	 * @createtime 2017-9-8 16:44
	 * @description: 设置宽度、高度、坐标属性
	 * @param activity
	 * @param activityInfo
	 */
	private void setSizeAndPositonInfo(ActivityImpl activity, Map<String, Object> activityInfo) {
		activityInfo.put("width", activity.getWidth());
		activityInfo.put("height", activity.getHeight());
		activityInfo.put("x", activity.getX());
		activityInfo.put("y", activity.getY());
	}


	/**
	 * @creator caoh
	 * @createtime 2017-10-9 15:14
	 * @description: 打开设置候选启动的页面
	 * @return
	 */
	@RequestMapping(value = "startable")
	public String startableUI() {

		return "modules/act/actStartable";
	}


	/**
	 * @creator caoh
	 * @createtime 2017-10-18 10:22
	 * @description:内置表单提交启动流程
	 * @return
	 */
	@RequestMapping(value = "startProcessInner")
	@ResponseBody
	public String startProcessInner(HttpServletRequest request) {
		String pdKey = getPara("pdKey");
		String pstid = eamProcessService.startProcessByPdid(pdKey,"","",request);

		return pstid;
	}

	/**
	 * @creator caoh
	 * @createtime 2017-12-6 16:02
	 * @description: 删除运行中的流程实例（使归档）
	 * @param processInstanceId 流程实例Id
	 * @return success
	 */
	@RequestMapping(value = "deleteProcessInstance")
	@ResponseBody
	public String delete(@RequestParam("processInstanceId") String processInstanceId) {
		runtimeService.deleteProcessInstance(processInstanceId, "管理员删除");
		return "success";
	}



}
