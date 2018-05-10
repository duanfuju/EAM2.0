/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.common.service;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.tiansu.eam.common.dao.ValidateDBDao;
import com.tiansu.eam.common.persistence.DataEntity;
import com.tiansu.eam.common.persistence.RelatedModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tiansu.eam.modules.sys.utils.UserUtils.getDataScopeDeptIds;

/**
 * Service基类
 * @author ThinkGem
 * @version 2014-05-16
 */
@Transactional(readOnly = true)
public abstract class CrudService<D extends CrudDao<T>, T extends DataEntity<T>> {


	//查询是否需要包含数据权限查询
	private boolean defaultDataScope = false;
	/**
	 * 持久层对象
	 */
	@Autowired
	protected D dao;
	
	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public T get(String id) {
		return dao.get(id);
	}
	
	/**
	 * 获取单条数据
	 * @param entity
	 * @return
	 */
	public T get(T entity) {
		return dao.get(entity);
	}

	@Autowired
	private ValidateDBDao validateDBDao;

	private Logger logger = LoggerFactory.getLogger(CrudService.class);
	/**
	 * 查询列表数据
	 * @param entity 实体查询对象
	 * @return
	 */
	public List<T> findList(T entity) {
		return dao.findList(entity);
	}

	public Map dataTablePage(T entity) {
		return dataTablePage(entity,defaultDataScope);
	}

	public Map dataTablePageMap(Map param) {
		return dataTablePageMap(param,defaultDataScope);
	}

    public Map dataTablePageMapApp(Map param) {
        return dataTablePageMapApp(param,defaultDataScope);
    }

    /**
	 * 查询列表数据
	 * @param entity
	 * @return
	 */
	public Map dataTablePage(T entity,boolean isDataScope) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		Page<T> page = new Page();

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
		page = setOrderBy(page,request);


		entity.setPage(page);
		if(isDataScope){
			//获取所拥有数据权限的部门Id
			String deptIds = getDataScopeDeptIds();
			entity.setDept(deptIds);
		}

		List<T> list = dao.findList(entity);
		map.put("recordsFiltered",page.getCount());
		map.put("recordsTotal",page.getCount());
		map.put("data",list);
		map.put("draw",draw);
		return map;
	}

	/**
	 * 查询列表数据
	 * @param param map查询对象
	 * @param includeDataScope 是否包含数据权限
	 * @return
	 */
	public Map dataTablePageMap(Map param,boolean includeDataScope) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		Page<Map> page = new Page();
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
		page = setOrderBy(page,request);
		if(includeDataScope){
			//获取所拥有数据权限的部门Id
			String deptIds = getDataScopeDeptIds();
			param.put("dept",deptIds);
		}

		param.put("page",page);
		List<Map> list = getPageTableData(param);

		map.put("recordsFiltered",page.getCount());
		map.put("recordsTotal",page.getCount());
		map.put("data",list);
		map.put("draw",draw);
		return map;
	}

	public List<Map> getPageTableData(Map param) {
		return dao.findListByMap(param);
	}

	/**
	 * 查询列表数据
	 * @param param map查询对象
	 * @param includeDataScope 是否包含数据权限
	 * @return
	 */
	public Map dataTablePageMapApp(Map param,boolean includeDataScope) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		Page<Map> page = new Page();
		Map map= new HashMap();


		//设置分页数据
		String sta = request.getParameter("pageNo")==null?"0":request.getParameter("pageNo");
		String len = request.getParameter("length")==null?"10":request.getParameter("length");
		String dra = request.getParameter("draw")==null?"1":request.getParameter("draw");
		int pageNo = Integer.parseInt(sta);
		int length = Integer.parseInt(len);
		int draw = Integer.parseInt(dra);

		page.setPageNo(pageNo);
		page.setPageSize(length);

		//获取排序数据
		page = setOrderBy(page,request);
		if(includeDataScope){
			//获取所拥有数据权限的部门Id
			String deptIds = getDataScopeDeptIds();
			param.put("dept",deptIds);
		}

		param.put("page",page);
		List<Map> list = dao.findListByMap(param);

		map.put("recordsFiltered",page.getCount());
		map.put("recordsTotal",page.getCount());
		map.put("data",list);
		map.put("draw",draw);
		return map;
	}

	/**
	 *
	 * @param page
	 * @param request
	 * @return
	 * @modifier wangjl
	 * @modifytime 2017/9/28 11:07
	 * @modifyDec: 添加排序默认字段，适配第一次查询时按照时间进行排序的问题，
	 * 				若表中没有createtime，updatetime，请重写setDefaultOrderBy方法。或者前台页面中"order": [['-1', "desc"]], -1改为实际列
	 */
	public Page setOrderBy(Page page,HttpServletRequest request){
		Enumeration<String> en = request.getParameterNames();
		while (en.hasMoreElements()) {
			String nms = en.nextElement().toString();
			if(nms.startsWith("order") && nms.endsWith("[column]")){

				String column = request.getParameterValues(nms)[0];
				String columnname = request.getParameterValues("columns["+column+"][data]")[0];
				//modify by wangjl at 20170828
				if("0".equals(columnname)){
					return setDefaultOrderBy(page,request);
				}
				String orderby = request.getParameterValues("order[0][dir]")[0];
				page.setOrderBy(columnname+" "+orderby);
				break;
			}
		}

		return page;
	}


	public Page setDefaultOrderBy(Page page,HttpServletRequest request){
		page.setOrderBy("update_time desc");
		return page;
	}
	
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<T> findPage(Page<T> page, T entity) {
		entity.setPage(page);
		page.setList(dao.findList(entity));
		return page;
	}

	/**
	 * 保存数据（插入或更新）
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void save(T entity) {
		if (entity.getIsNewRecord()){
			entity.preInsert();
			dao.insert(entity);
		}else{
			entity.preUpdate();
			dao.update(entity);
		}
	}
	/**
	 * 新增数据
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void insert(T entity) {
		if(StringUtils.isEmpty(entity.getId())){
			entity.preInsert();
		}
		dao.insert(entity);
	}
	/**
	 * 修改数据
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void update(T entity) {
			entity.preUpdate();
			dao.update(entity);
	}

	/**
	 * 删除数据
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void delete(T entity) {
		dao.delete(entity);
	}

	/**
	 * 判断该数据是否被引用，引用表较多时性能会比较慢，可以将最有可能被引用的表放在list前面检查。如果需要批量检查大量数据，可考虑用临时表
	 * @param relatedModelList 关联表信息
	 * @param dataTable 检查表
	 * @param dataColumn 检查字段
	 * @param dataValue 检查字段值
	 * @return
	 */
	public boolean isRelated(List<RelatedModel> relatedModelList, String dataTable, String dataColumn,String dataValue){
		if(relatedModelList != null && relatedModelList.size() > 0){
			for(RelatedModel model : relatedModelList){
				String sql = buildRelateSql(model,dataTable,dataColumn,dataValue);
				Map<String,String> executeSqlMap = new HashMap<>();
				executeSqlMap.put("executeSql",sql);
				Map<String,Object> result  = validateDBDao.executeSql(executeSqlMap);
				if(result != null){
					int count = (Integer)result.get("count");
					if(count > 0){
						logger.warn("data related! related sql is :"+ sql);
						return  true;
					}
				}

			}
		}

		return false;
	}


	/**
	 * 创建数据关联校验sql
	 * @param model
	 * @param dataTable
	 * @param dataColumn
	 * @return
	 */
	private String buildRelateSql(RelatedModel model, String dataTable, String dataColumn,String dataValue) {
		StringBuffer relateSql = new StringBuffer("select count(*) as count from "+dataTable+", "+model.getRelatedTable()
				+" where " +dataTable+"."+dataColumn +"="+model.getRelatedTable() + "."+ model.getRelatedColumn() +" and "
				+dataTable+"."+dataColumn +"='"+dataValue+"'" );
		//判断当前数据库类型 String dbName = Global.getConfig("jdbc.type");if("oracle".equals(dbName)){
		if(org.apache.commons.lang3.StringUtils.isNotEmpty(model.getWhereCond())){
			relateSql.append(" and "+model.getWhereCond());
		}

		return relateSql.toString();
	}

}
