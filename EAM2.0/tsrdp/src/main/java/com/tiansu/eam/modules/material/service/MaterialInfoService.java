/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.modules.material.service;

import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.modules.material.dao.MaterialInfoDao;
import com.tiansu.eam.modules.material.entity.MaterialInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/*import com.thinkgem.jeesite.common.service.CrudService;*/

/**
 * 物料信息Service
 * @author caoh
 * @version 2017-08-21
 */
@Service
@Transactional(readOnly = true)
public class MaterialInfoService extends CrudService<MaterialInfoDao, MaterialInfo> {

	@Autowired
	private MaterialInfoDao mterialInfoDao;

	public MaterialInfo get(String id) {
		return super.get(id);
	}

	public Map getEdit(String id){
		return mterialInfoDao.getEdit(id);

	}

	public Map getDetail(String id){
		return mterialInfoDao.getDetail(id);

	}

	public Map dataTablePage(MaterialInfo materialInfo) {
		return super.dataTablePage(materialInfo);

	}

	public Map dataTablePageMap(Map map) {
		return super.dataTablePageMap(map);

	}
	
	@Transactional(readOnly = false)
	public void insert(MaterialInfo materialInfo) {

		materialInfo.preInsert();
		//生成主键
		materialInfo.setId_key(IdGen.uuid());
		materialInfo.setCreate_by(materialInfo.getCreateBy().getLoginname());
		materialInfo.setCreate_time(materialInfo.getCreateDate());
		materialInfo.setUpdate_by(materialInfo.getCreateBy().getLoginname());
		materialInfo.setUpdate_time(materialInfo.getCreateDate());
		mterialInfoDao.insert(materialInfo);

	}

	@Transactional(readOnly = false)
	public void update(MaterialInfo materialInfo) {

		materialInfo.preUpdate();
		materialInfo.setUpdate_by(materialInfo.getUpdateBy().getLoginname());
		materialInfo.setUpdate_time(materialInfo.getUpdateDate());
		mterialInfoDao.update(materialInfo);

	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		mterialInfoDao.delete(id);
	}

	public List<Map> supplierSelect(){
		return mterialInfoDao.supplierSelect();
	}


	/**
	 * @creator duanfuju
	 * @createtime 2017/9/26 10:41
	 * @description:
	 *  批量插入
	 * @param materialInfo
	 * @return
	 */
	public int insertBatch(List<MaterialInfo> materialInfo){
		return mterialInfoDao.insertBatch(materialInfo);
	}

	/**
	 * @creator duanfuju
	 * @createtime 2017/9/26 14:36
	 * @description:
	 * 	导出查询
	 * @param map
	 * @return
	 */
	public List<Map> findListByMap(Map map){
		return mterialInfoDao.findListByMap(map);
	}
}