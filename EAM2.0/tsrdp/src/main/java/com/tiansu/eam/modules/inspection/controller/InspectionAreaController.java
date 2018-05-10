
package com.tiansu.eam.modules.inspection.controller;

import com.alibaba.fastjson.JSON;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.inspection.entity.InspectionArea;
import com.tiansu.eam.modules.inspection.service.InspectionAreaService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @creator duanfuju
 * @createtime 2017/10/24 9:34
 * @description:
 * 		巡检区域controller
 */
@Controller
@RequestMapping(value = "${adminPath}/inspection/inspectionArea")
public class InspectionAreaController extends BaseController {

	@Autowired
	private InspectionAreaService inspectionAreaService;

	/**
	 * 标准新增/修改页面的设备选择页面
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "inspectionSubjectSelectUI")
	public String DeviceSelectUI(){
		return "modules/inspection/inspectionSubjectSelectUI";
	}
	/**
	 *@creator duanfuju
	 * @createtime 2017/10/24 9:34
	 * @description: 
	 *  跳转新增页面路径
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "addUI")
	public String addUI(Map<String, Object> map) {
		return "modules/inspection/inspectionAreaFormAdd";
	}

	/**
	 *@creator duanfuju
	 * @createtime 2017/10/24 9:34
	 * @description: 
	 *  跳转修改页面路径
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "editUI")
	public String editUI() {
		return "modules/inspection/inspectionAreaFormEdit";
	}

	/**
	 * @creator duanfuju
	 * @createtime 2017/10/24 9:34
	 * @description: 
	 * 跳转详情页面路径
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "detailUI")
	public String detailUI() {
		return "modules/inspection/inspectionAreaFormDetail";
	}




	/**
	 * @creator duanfuju
	 * @createtime 2017/10/24 9:34
	 * @description: 
	 * 删除
	 */
	@ResponseBody
	@Transactional(readOnly=false)
	@RequiresPermissions("user")
	@RequestMapping(value = "delete")
	public String delete() {
		inspectionAreaService.delete(getPara("id"));
		return "success";
	}

	/**
	 * @creator duanfuju
	 * @createtime 2017/10/24 9:34
	 * @description: 
	 * 修改前获取单个对象
	 */
	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = "editObj")
	public Map editObj() {
		String id=getPara("id");
		return inspectionAreaService.findById(id);
	}


	/**
	 *@creator duanfuju
	 * @createtime 2017/10/24 9:34
	 * @description:  新增
	 */
	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = "save")
	public String save(InspectionArea inspectionAreas, HttpServletRequest request) {
		String param = getPara("param");      //获取入参
		inspectionAreas = JSON.parseObject(param, InspectionArea.class);
		int n=inspectionAreaService.insertAndUpdate(inspectionAreas);
		if(n>0){
			return  "error";
		}
		return "success";
	}


	/**
	 * @creator duanfuju
	 * @createtime 2017/10/24 9:34
	 * @description: 
	 * 列表查询
	 */
	@ResponseBody
	@RequiresPermissions("user")
	@RequestMapping(value = {"dataTablePageMap"})
	public Map<String,Object> dataTablePageMap() {
		Map param = getFormMap();
		Map<String,Object> map = inspectionAreaService.dataTablePageMap(param);
		return map;
	}


}
