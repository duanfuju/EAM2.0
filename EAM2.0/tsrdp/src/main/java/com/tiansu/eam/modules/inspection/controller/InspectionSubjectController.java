package com.tiansu.eam.modules.inspection.controller;

import com.alibaba.fastjson.JSON;
import com.thinkgem.jeesite.common.persistence.Page;
import com.tiansu.eam.common.web.BaseController;

import com.tiansu.eam.modules.inspection.entity.InspectionSubject;
import com.tiansu.eam.modules.inspection.entity.InspectionSubjectForInsert;
import com.tiansu.eam.modules.inspection.service.InspectionSubjectService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tiansu.eam.modules.sys.utils.UserUtils.getDataScopeDeptIds;

/**
 * @creator duanfuju
 * @createtime 2017/10/19 10:24
 * @description:
 * 巡检项Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/inspection/inspectionSubject")
public class InspectionSubjectController extends BaseController {

	@Autowired
	private InspectionSubjectService inspectionSubjectService;

	/**
	 *@creator duanfuju
	 * @createtime 2017/10/20 11:31
	 * @description:
	 *  修改前获取单个对象
	 */
	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = "editObj")
	public Map editObj() {
		String id=getPara("id");
		return inspectionSubjectService.findById(id);
	}

	/**
	 *@creator duanfuju
	 * @createtime 2017/10/20 10:39
	 * @description:
	 *  跳转修改页面路径
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "editUI")
	public String editUI() {
		return "modules/inspection/inspectionSubjectFormEdit";
	}

	/**
	 * @creator duanfuju
	 * @createtime 2017/10/20 10:39
	 * @description: 
	 * 		跳转详情页面路径
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "detailUI")
	public String detailUI() {
		return "modules/inspection/inspectionSubjectFormDetail";
	}
	
	
	/**
	 * @creator duanfuju
	 * @createtime 2017/10/20 10:17
	 * @description:
	 * 		删除
	 */
	@ResponseBody
	@Transactional(readOnly=false)
	@RequiresPermissions("user")
	@RequestMapping(value = "delete")
	public String delete() {
		inspectionSubjectService.delete(getPara("id"));
		return "success";
	}
	/**.
	 * @creator duanfuju
	 * @createtime 2017/10/19 10:31
	 * @description:
	 *  新增/更新
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "save")
	@Transactional
	@ResponseBody
	public String save(InspectionSubjectForInsert subjects, HttpServletRequest request) {
		String param = getPara("param");      //获取入参
		subjects = JSON.parseObject(param, InspectionSubjectForInsert.class);
		int n=inspectionSubjectService.save(subjects);
		if(n>0){
			return "success";
		}else{
			return  "error";
		}
	}

	/**
	 * @creator duanfuju
	 * @createtime 2017/10/19 10:26
	 * @description:
	 * 	跳转新增页面路径
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "addUI")
	public String addUI() {
		return "modules/inspection/inspectionSubjectFormAdd";
	}

	/**
	 * @creator duanfuju
	 * @createtime 2017/10/19 10:26
	 * @description:
	 * 		 列表查询
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = {"dataTablePageMap"})
	public Map<String,Object> listData() {
		Map param = getFormMap();
		Map<String,Object> map = inspectionSubjectService.dataTablePageMap(param);
		return map;
	}
	/**
	 * @creator duanfuju
	 * @createtime 2017/10/19 10:26
	 * @description:
	 * 		 列表查询
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = {"inspectionSubjectSelectUI"})
	public Map inspectionSubjectSelectUI() {
		Map param = getFormMap();
		Map<String,Object> map = inspectionSubjectService.inspectionSubjectSelectUI(param);
		return map;
	}
	/**
	 * 新增/修改页面的设备选择页面
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "DeviceSelectUI")
	public String DeviceSelectUI(){
		return "modules/inspection/inspectionSubjectDeviceSelect";
	}


}