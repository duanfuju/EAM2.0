/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.material.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.tiansu.eam.common.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
/*import com.thinkgem.jeesite.common.service.CrudService;*/
import com.thinkgem.jeesite.modules.material.entity.TestMaterial;
import com.thinkgem.jeesite.modules.material.dao.TestMaterialDao;

import javax.servlet.http.HttpServletRequest;

/**
 * 物料信息Service
 * @author caoh
 * @version 2017-07-19
 */
@Service
@Transactional(readOnly = true)
public class TestMaterialService extends CrudService<TestMaterialDao, TestMaterial> {

	@Autowired
	private TestMaterialDao testMaterialDao;

	public Map getEdit(String id){
		return testMaterialDao.getEdit(id);

	}

	public TestMaterial get(String id) {
		return super.get(id);
	}

	public Map dataTablePage(TestMaterial testMaterial) {
		return super.dataTablePage(testMaterial);

	}

	public Map dataTablePageMap(Map map) {
		return super.dataTablePageMap(map);

	}
	
	@Transactional(readOnly = false)
	public void insert(TestMaterial testMaterial) {

		testMaterial.preInsert();
		testMaterialDao.insert(testMaterial);

	}

	@Transactional(readOnly = false)
	public void update(TestMaterial testMaterial) {

		testMaterial.preUpdate();
		testMaterialDao.update(testMaterial);

	}
	
	@Transactional(readOnly = false)
	public void delete(TestMaterial testMaterial) {

		super.delete(testMaterial);
	}
	
}