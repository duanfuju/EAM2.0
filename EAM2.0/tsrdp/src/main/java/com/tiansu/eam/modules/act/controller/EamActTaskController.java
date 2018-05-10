/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.modules.act.controller;

import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.act.service.EamActTaskService;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.activiti.engine.FormService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 流程个人任务相关Controller
 * @author ThinkGem
 * @version 2013-11-03
 */
@Controller
@RequestMapping(value = "${adminPath}/eam/act/task")
public class EamActTaskController extends BaseController {

	@Autowired
	private ActTaskService actTaskService;

	@Autowired
	private EamActTaskService eamActTaskService;

	@Autowired
	private TaskService taskService;

	@Autowired
	IdentityService identityService;

	@Autowired
	FormService formService;
	/**
	 * @creator caoh
	 * @createtime 2017-9-18 15:01
	 * @description: 获取待办列表
	 * @return
	 * @modifier fy
	 * @modifytime 2017/11/28 上午 9:40
	 * @modifyDec:添加用户字段在todoList：uName
	 */
	@RequestMapping(value = "todoList")
	@ResponseBody
	public Map<String, Object> todoList() throws Exception {
		Map<String,String> param = getFormMap();
		//查询条件-任务名称
		String taskName=null;
		//查询条件-流程名称
		String processName=null;
		if(param.containsKey("taskName")){
			taskName = param.get("taskName").toString();
		}
		if(param.containsKey("processName")){
			processName = param.get("processName").toString();
		}

		Map<String,Object> map = eamActTaskService.todoList(taskName,processName,"");
		return map;
	}

	/**
	 * @creator caoh
	 * @createtime 2017-9-18 15:01
	 * @description: 获取已办任务列表
	 * @return
	 * @throws Exception
	 * @modifier fy
	 * @modifytime 2017/11/28 上午 9:40
	 * @modifyDec:添加用户字段在finishedList：uName
	 */
	@RequestMapping(value = "historic")
	@ResponseBody
	public Map<String, Object> historicList() throws Exception {
		Map<String,String> param = getFormMap();
		//查询条件-流程名称
		String processName=null;
		if(param.containsKey("processName")){
			processName = param.get("processName").toString();
		}
		Map<String,Object> map = eamActTaskService.finishedList(processName,"");
		return map;

	}


	/**
	 * @creator caoh
	 * @createtime 2017-9-6 16:47
	 * @description: 签收任务
	 */
	@RequestMapping(value = "claim")
	@ResponseBody
	public String claim() {
		String taskId = getPara("taskid");
		String userId = UserUtils.getUser().getLoginname();
		actTaskService.claim(taskId, userId);//签收参数（任务id，签收人）
		return "success";
	}

	/**
	 * @creator caoh
	 * @createtime 2017-9-6 16:47
	 * @description: 反签收任务
	 */
	@RequestMapping(value = "unclaim")
	@ResponseBody
	public String unclaim() {
		String taskId = getPara("taskid");
		// 反签收条件过滤
		List<IdentityLink> links = taskService.getIdentityLinksForTask(taskId);
		for (IdentityLink identityLink : links) {
			// 如果一个任务有相关的候选人、组就可以反签收
			if (StringUtils.equals(IdentityLinkType.CANDIDATE, identityLink.getType())) {
				taskService.claim(taskId, null);
				//redirectAttributes.addFlashAttribute("message", "任务已反签收");
				return "success";
			}
		}
		//redirectAttributes.addFlashAttribute("error", "该任务不允许反签收！");
		return "fail";
	}


	/**
	 * @creator caoh
	 * @createtime 2017-9-8 11:34
	 * @description: 完成当前任务
	 * @return 处理状态：success-成功；noauth-没有权限
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	public String complete(HttpServletRequest request) {
		// 设置当前操作人，对于调用活动可以获取到当前操作人
		String taskId = getPara("taskid");
		String userId = UserUtils.getUser().getLoginname();
		identityService.setAuthenticatedUserId(userId);

		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		// 如果任务的流程定义任务Key为空则认为是手动创建的任务
		if (StringUtils.isBlank(task.getTaskDefinitionKey())) {
			taskService.complete(taskId);
			return "success";
		}

		// 权限检查-任务的办理人和当前人不一致不能完成任务
		if (!userId.equals(task.getAssignee())) {

			return "noauth";
		}

		// 单独处理被委派的任务
		if (task.getDelegationState() == DelegationState.PENDING) {
			taskService.resolveTask(taskId);
			return "success";
		}

		TaskFormData taskFormData = formService.getTaskFormData(taskId);
		String formKey = taskFormData.getFormKey();
		// 从请求中获取表单字段的值
		List<FormProperty> formProperties = taskFormData.getFormProperties();
		Map<String, String> formValues = new HashMap<String, String>();

		if (StringUtils.isNotBlank(formKey)) { // formkey表单
			Map<String, String[]> parameterMap = request.getParameterMap();
			Set<Map.Entry<String, String[]>> entrySet = parameterMap.entrySet();
			for (Map.Entry<String, String[]> entry : entrySet) {
				String key = entry.getKey();
				formValues.put(key, entry.getValue()[0]);
			}
		} else { // 动态表单
			for (FormProperty formProperty : formProperties) {
				if (formProperty.isWritable()) {
					String value = request.getParameter(formProperty.getId());
					formValues.put(formProperty.getId(), value);
				}
			}
		}
		formService.submitTaskFormData(taskId, formValues);
		return "success";
	}
	
	/**
	 * 读取带跟踪的图片
	 */
	@RequestMapping(value = "trace/photo/{procDefId}/{execId}")
	public void tracePhoto(@PathVariable("procDefId") String procDefId, @PathVariable("execId") String execId, HttpServletResponse response) throws Exception {
		InputStream imageStream = actTaskService.tracePhoto(procDefId, execId);
		
		// 输出资源内容到相应对象
		byte[] b = new byte[1024];
		int len;
		while ((len = imageStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
	}
	
	/**
	 * 删除任务
	 * @param taskId 流程实例ID
	 * @param reason 删除原因
	 */
	@RequiresPermissions("act:process:edit")
	@RequestMapping(value = "deleteTask")
	public String deleteTask(String taskId, String reason, RedirectAttributes redirectAttributes) {
		if (StringUtils.isBlank(reason)){
			addMessage(redirectAttributes, "请填写删除原因");
		}else{
			actTaskService.deleteTask(taskId, reason);
			addMessage(redirectAttributes, "删除任务成功，任务ID=" + taskId);
		}
		return "redirect:" + adminPath + "/act/task";
	}
}
