/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.material.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.material.entity.TestMaterial;

import java.util.List;
import java.util.Map;

/**
 * 物料信息DAO接口
 * @author caoh
 * @version 2017-07-19
 */
@MyBatisDao
public interface TestMaterialDao extends CrudDao<TestMaterial> {
    public List<Map> findListByMap(Map map);

    public Map<String,Object> getEdit(String id);

    public int insert(TestMaterial testMaterial);

    public int update(TestMaterial testMaterial);
}