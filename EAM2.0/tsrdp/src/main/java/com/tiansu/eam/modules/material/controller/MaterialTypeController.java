/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.modules.material.controller;

import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.material.entity.MaterialType;
import com.tiansu.eam.modules.material.service.MaterialTypeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 物料类别Controller
 * @author caoh
 * @version 2017-08-08
 */
@Controller
@RequestMapping(value = "${adminPath}/material/materialType")
public class MaterialTypeController extends BaseController {

	@Autowired
	private MaterialTypeService materialTypeService;


	/**
	 * @creator Douglas
	 * @createtime 2017-8-23 16:46
	 * @description: 打开新增页面
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "addUI")
	public String addUI() {

		return "modules/material/materialTypeFormAdd";

	}

	/**
	 * @creator Douglas
	 * @createtime 2017-8-23 16:47
	 * @description:打开编辑页面
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "editUI")
	public String editUI() {

		return "modules/material/materialTypeFormEdit";
	}

	/**
	 * 获取编辑页面字段数据
	 * @param materialType
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = "editObj")
	public Map editObj(MaterialType materialType) {
		String id = getPara("id");

		return materialTypeService.getEdit(id);
	}

	/**
	 * 获取详情页面字段数据
	 * @param materialType
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = "detailObj")
	public Map detailObj(MaterialType materialType) {
		String id = getPara("id");

		return materialTypeService.getDetail(id);
	}

	/**
	 * @creator Douglas
	 * @createtime 2017-8-23 16:45
	 * @description: 获取列表分页数据
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = {"listData"})
	public Map<String,Object> listData() {

		Map param = getFormMap();
		Map<String,Object> map = materialTypeService.dataTablePageMap(param);

		/*MaterialType materialType = getFormEntity(MaterialType.class);
		Map<String,Object> map = materialTypeService.dataTablePage(materialType);*/

		return map;
	}

	/**
	 * @creator Douglas
	 * @createtime 2017-8-23 16:45
	 * @description: 新增数据
	 * @param materialType
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = "insert")
	public String insert(MaterialType materialType) {

		materialTypeService.insert(materialType);
		materialTypeService.updateMaterialTypeTree();
		return "success";
	}

	/**
	 * @creator Douglas
	 * @createtime 2017-8-23 16:46
	 * @description: 更新数据
	 * @param materialType
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = "update")
	public String update(MaterialType materialType) {

		materialTypeService.update(materialType);
		return "success";
	}

	/**
	 * @creator Douglas
	 * @createtime 2017-8-23 16:46
	 * @description: 删除数据
	 * @return
	 */
	@ResponseBody
	@Transactional(readOnly=false)
	@RequiresPermissions("user")
	@RequestMapping(value = "delete")
	public String delete() {
		String ids[] = getPara("id").split(",");
		for(int i=0;i<ids.length;i++){
			//MaterialType materialType = materialTypeService.get(ids[i]);
			materialTypeService.delete(ids[i]);
		}

		return "success";
	}

	/**
	 * 获取字段权限
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = "getfields")
	public Map getFields(){

		String menuno = getPara("menuno");
		Map<String,Object> result = getFieldsByMenuno(menuno);
		return result;
	}


	/**
	 * 打开详情页面
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "detailUI")
	public String detailUI() {

		return "modules/material/materialTypeFormDetail";
	}

	/**
	 * 获取物料类别下拉树数据
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = "materialTypeTree")
	public List<Map> materialTypeTree() {
		return materialTypeService.getMaterialTypeTree();

	}

}