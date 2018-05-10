/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.modules.inspection.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.inspection.entity.InspectionSubject;

import java.util.List;
import java.util.Map;

/**
 * @creator duanfuju
 * @createtime 2017/10/19 11:04
 * @description:
 * 巡检项DAO接口
 */
@MyBatisDao
public interface InspectionSubjectDao extends CrudDao<InspectionSubject> {
    /**
     * @creator duanfuju
     * @createtime 2017/10/19 11:06
     * @description: 
     * 列表查询
     * @param map
     * @return
     */
    List<Map> findListByMap(Map map);

    /**
     * @creator duanfuju
     * @createtime 2017/10/19 11:05
     * @description: 
     * 根据id获取
     * @param id
     * @return
     */
    Map findById(String id);

    /**
     * @creator duanfuju
     * @createtime 2017/10/19 11:05
     * @description: 
     * 根据id删除（逻辑）
     * @param map
     * @return
     */
    int delete(Map map);

    /**
     * @creator duanfuju
     * @createtime 2017/10/19 11:05
     * @description: 
     *批量插入
     * @return
     */
     int insertBatch(List<InspectionSubject> inspectionSubject);

    /**
     * 巡检项被巡检区域选择的时候
     * @return
     */
    List<Map> inspectionSubjectSelectUI(Map param);
}