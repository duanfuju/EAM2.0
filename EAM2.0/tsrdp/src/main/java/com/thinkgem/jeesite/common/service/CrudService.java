/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.persistence.Page;

import javax.servlet.http.HttpServletRequest;

/**
 * Service基类
 * @author ThinkGem
 * @version 2014-05-16
 */
@Transactional(readOnly = true)
public abstract class CrudService<D extends CrudDao<T>, T extends DataEntity<T>> extends BaseService {
	
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
	
	/**
	 * 查询列表数据
	 * @param entity
	 * @return
	 */
	public List<T> findList(T entity) {
		return dao.findList(entity);
	}

	/**
	 * 查询列表数据
	 * @param entity
	 * @param request 请求参数
	 * @return
	 */
	public Map dataTablePage(T entity,HttpServletRequest request) {
		Page page = new Page<T>();

		Map map= new HashMap();

		String sta = request.getParameter("start")==null?"0":request.getParameter("start");
		String len = request.getParameter("length")==null?"10":request.getParameter("length");
		String dra = request.getParameter("draw")==null?"1":request.getParameter("draw");
		int start = Integer.parseInt(sta);
		int length = Integer.parseInt(len);
		int draw = Integer.parseInt(dra);

		page.setPageNo(start/length+1);
		page.setPageSize(length);
		entity.setPage(page);
		List<T> list = dao.findList(entity);


		/*if(start+length>=list.size()){
			map.put("data",list.subList(start,list.size()));
		}else{
			map.put("data",list.subList(start,start+length));
		}*/

		map.put("recordsFiltered",page.getCount());
		map.put("recordsTotal",page.getCount());
		map.put("data",list);
		map.put("draw",draw);
		return map;
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
	 * 删除数据
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void delete(T entity) {
		dao.delete(entity);
	}

}
