/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.modules.material.service;

import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.modules.material.dao.MaterialTypeDao;
import com.tiansu.eam.modules.material.entity.MaterialType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/*import com.thinkgem.jeesite.common.service.CrudService;*/

/**
 * 物料类别Service
 * @author caoh
 * @version 2017-08-08
 */
@Service
@Transactional(readOnly = true)
public class MaterialTypeService extends CrudService<MaterialTypeDao, MaterialType> {

	@Autowired
	private MaterialTypeDao mterialTypeDao;

	public MaterialType get(String id) {
		return super.get(id);
	}

	public Map getEdit(String id){
		return mterialTypeDao.getEdit(id);

	}

	public Map getDetail(String id){
		return mterialTypeDao.getDetail(id);

	}

	public Map dataTablePage(MaterialType mterialType) {
		return super.dataTablePage(mterialType);

	}

	public Map dataTablePageMap(Map map) {
		return super.dataTablePageMap(map);

	}
	
	@Transactional(readOnly = false)
	public void insert(MaterialType materialType) {

		materialType.preInsert();
		//生成主键
		materialType.setId_key(IdGen.uuid());
		//生成树节点id
		materialType.setType_id(IdGen.randowNum());
		materialType.setCreate_by(materialType.getCreateBy().getLoginname());
		materialType.setUpdate_by(materialType.getCreateBy().getLoginname());
		materialType.setCreate_time(materialType.getCreateDate());
		materialType.setUpdate_time(materialType.getCreateDate());
		mterialTypeDao.insert(materialType);

	}

	@Transactional(readOnly = false)
	public void update(MaterialType materialType) {

		materialType.preUpdate();
		materialType.setUpdate_by(materialType.getUpdateBy().getLoginname());
		materialType.setUpdate_time(materialType.getUpdateDate());
		mterialTypeDao.update(materialType);

	}

	public void updateMaterialTypeTree(){
		mterialTypeDao.updateMaterialTypeTree();
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		mterialTypeDao.delete(id);
	}


	/**
	 * 获取物料类别下拉树数据
	 */
	public List<Map> getMaterialTypeTree(){
		return mterialTypeDao.getMaterialTypeTree();
	}

	/**
	 * @creator duanfuju
	 * @createtime 2017/9/26 10:44
	 * @description:
	 *  批量插入
	 * @param materialType
	 * @return
	 */
	public int insertBatch(List<MaterialType> materialType){
		return mterialTypeDao.insertBatch(materialType);
	}

	/**
	 * @creator duanfuju
	 * @createtime 2017/9/26 14:33
	 * @description:
	 * 	导出条件查询
	 * @param map
	 * @return
	 */
	public List<Map> findListByMap(Map map){
		return mterialTypeDao.findListByMap(map);
	}

}