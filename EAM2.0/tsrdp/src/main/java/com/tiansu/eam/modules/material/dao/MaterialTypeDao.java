/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.modules.material.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.material.entity.MaterialType;

import java.util.List;
import java.util.Map;

/**
 * 物料类别DAO接口
 * @author caoh
 * @version 2017-08-08
 */
@MyBatisDao
public interface MaterialTypeDao extends CrudDao<MaterialType> {
    public List<Map> findListByMap(Map map);

    public Map<String,Object> getEdit(String id);

    public int insert(MaterialType materialType);

    public int update(MaterialType materialType);

    public int delete(String id);

    public Map getDetail(String id);

    public List<Map> getMaterialTypeTree();

    /**
     * @creator duanfuju
     * @createtime 2017/9/26 10:44
     * @description:
     *  批量插入
     * @param materialType
     * @return
     */
    int insertBatch(List<MaterialType> materialType);

    public int updateMaterialTypeTree();
}