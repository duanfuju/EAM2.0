/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.modules.material.controller;

import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.material.entity.MaterialInfo;
import com.tiansu.eam.modules.material.service.MaterialInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 物料信息Controller
 * @author caoh
 * @version 2017-08-21
 */
@Controller
@RequestMapping(value = "${adminPath}/material/materialInfo")
public class MaterialInfoController extends BaseController {

	@Autowired
	private MaterialInfoService materialInfoService;


	@RequiresPermissions("user")
	@RequestMapping(value = "addUI")
	public String addUI() {

		return "modules/material/materialInfoFormAdd";

	}

	@RequiresPermissions("user")
	@RequestMapping(value = "editUI")
	public String editUI() {

		return "modules/material/materialInfoFormEdit";
	}

	/**
	 * 获取编辑页面字段数据
	 * @param materialInfo
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = "editObj")
	public Map editObj(MaterialInfo materialInfo) {
		String id = getPara("id");

		return materialInfoService.getEdit(id);
	}

	/**
	 * 获取详情页面字段数据
	 * @param materialInfo
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = "detailObj")
	public Map detailObj(MaterialInfo materialInfo) {
		String id = getPara("id");

		return materialInfoService.getDetail(id);
	}

	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = {"listData"})
	public Map<String,Object> listData() {

		Map param = getFormMap();
		Map<String,Object> map = materialInfoService.dataTablePageMap(param);

		/*MaterialType materialType = getFormEntity(MaterialType.class);
		Map<String,Object> map = materialTypeService.dataTablePage(materialType);*/

		return map;
	}

	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = "insert")
	public String insert(MaterialInfo materialInfo) {
		//生成树节点id
		//materialInfo.setType_id(IdGen.randowNum());
		materialInfoService.insert(materialInfo);
		return "success";
	}

	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = "update")
	public String update(MaterialInfo materialInfo) {

		materialInfoService.update(materialInfo);
		return "success";
	}

	@ResponseBody
	@Transactional(readOnly=false)
	@RequiresPermissions("user")
	@RequestMapping(value = "delete")
	public String delete() {
		String ids[] = getPara("id").split(",");
		for(int i=0;i<ids.length;i++){
			//MaterialType materialType = materialTypeService.get(ids[i]);
			materialInfoService.delete(ids[i]);
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

		return "modules/material/materialInfoFormDetail";
	}


	/**
	 * @creator Douglas
	 * @createtime 2017-8-23 14:45
	 * @description: 获取供应商下拉数据
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = "supplierSelect")
	public List<Map> supplierSelect(){
		return materialInfoService.supplierSelect();
	}


}