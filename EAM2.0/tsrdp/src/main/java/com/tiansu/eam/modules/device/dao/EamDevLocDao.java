package com.tiansu.eam.modules.device.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.device.entity.DevLocation;

import java.util.List;
import java.util.Map;

/**
 * @creator duanfuju
 * @createtime 2017/9/4 16:08
 * @description:
 */
@MyBatisDao
public interface EamDevLocDao extends CrudDao<DevLocation> {

    /**
     * @creator duanfuju
     * @createtime 2017/11/23 18:36
     * @description:
     * 根据pid地柜查询
     * @return
     */
    List<Map> getDataByPId(Map map);
    /**
     * @creator duanfuju
     * @createtime 2017/8/31 15:37
     * @description: 
     * 列表查询
     * @param map
     * @return
     */
    List<Map> findListByMap(Map map);
    /**
     * @creator duanfuju
     * @createtime 2017/9/6 8:44
     * @description:
     * 获取设备类别下拉树数据
     */
    List<Map> getDevLocationTree();
    /**
     * @creator duanfuju
     * @createtime 2017/8/31 15:37
     * @description: 
     * 根据id获取
     * @param id
     * @return
     */
    Map findById(String id);

    List<Map> findByLocId(String id);
    /**
     * @creator duanfuju
     * @createtime 2017/8/31 15:37
     * @description: 
     * 根据id删除（逻辑）
     * @param map
     * @return
     */
    int delete(Map map);

    /**
     * @creator duanfuju
     * @createtime 2017/8/31 15:39
     * @description: 
     *批量插入
     * @return
     */
    int insertBatch(List<DevLocation> devLocation);

    public int updateDevLocationTree();
}
