/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.modules.act.controller;

import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.act.service.EamActModelService;
import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 流程模型相关Controller
 * @author ThinkGem
 * @version 2013-11-03
 */
@Controller
@RequestMapping(value = "${adminPath}/eam/act/model")
public class EamActModelController extends BaseController {

	@Autowired
	private EamActModelService actModelService;

	@Autowired
	RepositoryService repositoryService;

	/**
	 * 流程模型列表
	 */
	//RequiresPermissions("act:model:edit")
	@ResponseBody
	@RequestMapping(value = { "list", "" })
	public Map<String,Object> modelList(HttpServletRequest request){

		Map<String,Object> map = actModelService.modelList();
        return map;
		//return "modules/act/actModelList";
	}

	/**
	 * 创建模型
	 */
//	@RequiresPermissions("act:model:edit")
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(Model model) {
		return "modules/act/actModelCreate";
	}
	
	/**
	 * 创建模型
	 */
//	@RequiresPermissions("act:model:edit")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public void create(String name, String key, String description, String category,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			org.activiti.engine.repository.Model modelData = actModelService.create(name, key, description, category);
			response.sendRedirect(request.getContextPath() + "/act/process-editor/modeler.jsp?modelId=" + modelData.getId());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("创建模型失败：", e);
		}
	}

	/**
	 * 根据Model部署流程
	 */
//	@RequiresPermissions("act:model:edit")
	@RequestMapping(value = "deploy")
	@ResponseBody
	public String deploy() {
		String id = getPara("modelid");
		if (actModelService.deploy(id)){
			return "success";
		}else {
			return "fail";

		}
	}


	/**
	 * @creator Douglas
	 * @createtime 2017-9-1 17:09
	 * @description: 根据上传文件部署流程
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "deployByFile")
	@ResponseBody
	public String deployByFile(@RequestParam(value = "file", required = true) MultipartFile file) {
		Boolean statu = actModelService.deploy(file);
		return statu==true?"success":"fail";
	}
	
	/**
	 * 导出model的xml文件
	 */
//	@RequiresPermissions("act:model:edit")
	@RequestMapping(value = "export")
	public void export(String id, HttpServletResponse response) {
		actModelService.export(id, response);
	}

	/**
	 * 更新Model分类
	 */
//	@RequiresPermissions("act:model:edit")
	@RequestMapping(value = "updateCategory")
	public String updateCategory(String id, String category, RedirectAttributes redirectAttributes) {
		actModelService.updateCategory(id, category);
		redirectAttributes.addFlashAttribute("message", "设置成功，模块ID=" + id);
		return "redirect:" + adminPath + "/act/model";
	}
	
	/**
	 * 删除Model
	 * @return
	 */
//	@RequiresPermissions("act:model:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete() {
		String id = getPara("modelid");
		try{
			actModelService.delete(id);
			return "success";
		}catch (Exception e) {
			return "fail";
		}

		//redirectAttributes.addFlashAttribute("message", "删除成功，模型ID=" + id);
		//return "redirect:" + adminPath + "/act/model";
	}

	/**
	 * @creator caoh
	 * @createtime 2017-12-6 15:23
	 * @description: 删除部署的流程，级联删除流程实例
	 * @param deploymentId 流程部署ID
	 */
	@RequestMapping(value = "deleteDeployment")
	@ResponseBody
	public String deleteProcessDefinition(@RequestParam("deploymentId") String deploymentId) {
		repositoryService.deleteDeployment(deploymentId, true);
		return "success";
	}
}
