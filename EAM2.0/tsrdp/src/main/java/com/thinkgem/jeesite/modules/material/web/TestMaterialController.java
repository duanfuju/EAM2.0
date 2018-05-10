/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.material.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.modules.act.service.EamProcessService;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.omg.PortableInterceptor.SUCCESSFUL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.tiansu.eam.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.material.entity.TestMaterial;
import com.thinkgem.jeesite.modules.material.service.TestMaterialService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物料信息Controller
 * @author caoh
 * @version 2017-07-19
 */
@Controller
@RequestMapping(value = "${adminPath}/material/testMaterial")
public class TestMaterialController extends BaseController {

	@Autowired
	private TestMaterialService testMaterialService;

	@Autowired
	private EamProcessService eamProcessService;


	@RequiresPermissions("user")
	@RequestMapping(value = "addUI")
	public String addUI() {

		return "modules/material/testMaterialFormAdd";

	}

	@RequiresPermissions("user")
	@RequestMapping(value = "editUI")
	public String editUI() {

		return "modules/material/testMaterialFormEdit";
	}

	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = "editObj")
	public Map editObj(TestMaterial testMaterial) {
		String id = getPara("id");

		return testMaterialService.getEdit(id);
	}

	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = {"listData"})
	public Map<String,Object> listData() {

		/*TestMaterial testMaterial = getFormEntity(TestMaterial.class);
		Map<String,Object> map = testMaterialService.dataTablePage(testMaterial);*/

		Map param = getFormMap();
		Map<String,Object> map = testMaterialService.dataTablePageMap(param);

		return map;
	}

	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = "insert")
	@Transactional
	public String insert(TestMaterial testMaterial,HttpServletRequest request) {


		String userName = UserUtils.getUser().getLoginname();
		testMaterial.setMaterialemp(userName);
		//生成主键
		String uuid = IdGen.uuid();
		testMaterial.setMaterialid(uuid);

		String pstid = eamProcessService.startProcessByPdid("process_test","test_material",uuid,request);
		if("timeout".equals(pstid)){
			return "timeout";
		}
		testMaterial.setPstid(pstid);
		testMaterialService.insert(testMaterial);
		return "success";
	}

	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = "update")
	public String update(TestMaterial testMaterial) {

		testMaterialService.update(testMaterial);

		return "success";
	}

	@ResponseBody
	@Transactional(readOnly=false)
	@RequiresPermissions("user")
	@RequestMapping(value = "delete")
	public String delete() {
		String ids[] = getPara("id").split(",");
		for(int i=0;i<ids.length;i++){
			TestMaterial testMaterial = testMaterialService.get(ids[i]);
			testMaterialService.delete(testMaterial);
		}

		return "success";
	}

	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = "getfields")
	public Map getFields(){

		String menuno = getPara("menuno");
		//String menuno = "1102";
		Map<String,Object> result = getFieldsByMenuno(menuno);
		return result;
	}

}