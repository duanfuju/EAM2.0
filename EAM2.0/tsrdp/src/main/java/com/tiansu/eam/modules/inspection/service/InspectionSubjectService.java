/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.modules.inspection.service;

import com.alibaba.fastjson.JSON;
import com.thinkgem.jeesite.common.persistence.Page;
import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.modules.device.entity.DevLocation;
import com.tiansu.eam.modules.employee.entity.EamCustomer;
import com.tiansu.eam.modules.inspection.dao.InspectionSubjectDao;
import com.tiansu.eam.modules.inspection.entity.InspectionSubject;
import com.tiansu.eam.modules.inspection.entity.InspectionSubjectForInsert;
import com.tiansu.eam.modules.opestandard.entity.OperationWork;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.tiansu.eam.modules.sys.utils.UserUtils.getDataScopeDeptIds;

/*import com.thinkgem.jeesite.common.service.CrudService;*/

/**
 * @creator duanfuju
 * @createtime 2017/10/19 10:25
 * @description:
 * 			巡检项Service
 */
@Service
@Transactional(readOnly = true)
public class InspectionSubjectService extends CrudService<InspectionSubjectDao, InspectionSubject> {

	@Autowired
	private InspectionSubjectDao inspectionSubjectDao;


	@Override
	public Page setOrderBy(Page page, HttpServletRequest request){
		Enumeration<String> en = request.getParameterNames();
		while (en.hasMoreElements()) {
			String nms = en.nextElement().toString();
			if(nms.startsWith("order") && nms.endsWith("[column]")){
				String column = request.getParameterValues(nms)[0];
				String columnname = request.getParameterValues("columns["+column+"][data]")[0];
				if("0".equals(columnname)||"1".equals(columnname)){
					return setDefaultOrderBy(page,request);
				}else if(columnname.equals("device_location")){
					columnname="dev_id";
				}
				String orderby = request.getParameterValues("order[0][dir]")[0];
				page.setOrderBy(columnname+" "+orderby);
				break;
			}
		}
		return page;
	}
	public Map inspectionSubjectSelectUI(Map param) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		Page<InspectionSubject> page = new Page();
		Map map= new HashMap();
		//设置分页数据
		String sta = request.getParameter("start")==null?"0":request.getParameter("start");
		String len = request.getParameter("length")==null?"10":request.getParameter("length");
		String dra = request.getParameter("draw")==null?"1":request.getParameter("draw");
		int start = Integer.parseInt(sta);
		int length = Integer.parseInt(len);
		int draw = Integer.parseInt(dra);

		page.setPageNo(start/length+1);
		page.setPageSize(length);

		//获取排序数据
		page = this.setOrderBy(page,request);
		if(false){
			//获取所拥有数据权限的部门Id
			String deptIds = getDataScopeDeptIds();
			param.put("dept",deptIds);
		}
		param.put("page",page);
		List<Map> list = dao.inspectionSubjectSelectUI(param);
		map.put("recordsFiltered",page.getCount());
		map.put("recordsTotal",page.getCount());
		map.put("data",list);
		map.put("draw",draw);
		return map;
	}
	/**
	 * @creator duanfuju
	 * @createtime 2017/10/19 10:36
	 * @description:
	 * 批量插入
	 * @param inspectionSubject
	 * @return
	 */
	public int insertBatch(List<InspectionSubject> inspectionSubject){
		return  inspectionSubjectDao.insertBatch(inspectionSubject);
	}
	/**
	 * @creator duanfuju
	 * @createtime 2017/10/19 10:35
	 * @description:
	 * 	获取所有数据
	 */
	public List<Map> findListByMap(Map map){
		return inspectionSubjectDao.findListByMap(map);
	}


	/**
	 * @creator duanfuju
	 * @createtime 2017/10/19 11:07
	 * @description: 
	 * 根据id获取
	 * @param id
	 * @return
	 */
	public Map findById(String id) {
		return inspectionSubjectDao.findById(id);
	}

	/**
	 * @creator duanfuju
	 * @createtime 2017/10/19 11:07
	 * @description: 
	 * 根据id删除（逻辑）
	 * @param id
	 * @return
	 */
	public int delete(String id){
		Map map = new HashMap();
		String ids[] = id.split(",");
		map.put("ids",ids);
		map.put("updateBy", UserUtils.getUser().getLoginname());
		map.put("updateDate",new Date());
		return  inspectionSubjectDao.delete(map);
	}

	/**
	 * @creator duanfuju
	 * @createtime 2017/10/19 15:22
	 * @description: 
	 * 	新增/更新（先删除后新增）
	 * @param subjects
	 * @return
	 */
	public int save(InspectionSubjectForInsert subjects) {
		int res=-1;

		if(subjects.getId_key()!=null&&subjects.getId_key()!=""){
			delete(subjects.getId_key());
		}
		String[] dev_id=subjects.getDev_id().split(",");//选中的设备
		String subject_status=subjects.getSubject_status();
		List<InspectionSubject> allSubject = new ArrayList<>();//所有的
		List<InspectionSubject> charSubjects =subjects.getCharSubjects();//字符型
		for (int i = 0; i <charSubjects.size() ; i++) {
			InspectionSubject subject=charSubjects.get(i);
			subject.setSubject_valuetype("1");
		}
		List<InspectionSubject> numberSubjects =subjects.getNumberSubjects();//数值型
		for (int i = 0; i <numberSubjects.size() ; i++) {
			InspectionSubject subject=numberSubjects.get(i);
			subject.setSubject_valuetype("0");
		}
		allSubject.addAll(charSubjects);
		allSubject.addAll(numberSubjects);
		for (int i = 0; i <dev_id.length; i++) {
			for (int j = 0; j <allSubject.size() ; j++) {
				InspectionSubject subject=allSubject.get(j);
				if(subject.getSubject_name()!=null&&subject.getSubject_name()!=""){
					subject.setDev_id(dev_id[i]);
					subject.setSubject_status(subject_status);
					subject.preInsert();
					inspectionSubjectDao.insert(subject);
					res=1;
				}
			}
		}
		return res;
	}
}