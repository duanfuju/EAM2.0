/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.modules.material.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.material.entity.MaterialInfo;

import java.util.List;
import java.util.Map;

/**
 * 物料信息DAO接口
 * @author caoh
 * @version 2017-08-21
 */
@MyBatisDao
public interface MaterialInfoDao extends CrudDao<MaterialInfo> {
    public List<Map> findListByMap(Map map);

    public Map<String,Object> getEdit(String id);

    public int insert(MaterialInfo materialInfo);

    public int update(MaterialInfo materialInfo);

    public int delete(String id);

    public Map getDetail(String id);

    public List<Map> supplierSelect();

    /**
     * @creator duanfuju
     * @createtime 2017/9/26 10:41
     * @description:
     *  批量插入
     * @param materialInfo
     * @return
     */
    int insertBatch(List<MaterialInfo> materialInfo);
}