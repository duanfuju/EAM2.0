package com.tiansu.eam.modules.opestandard.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.opestandard.entity.StandardOrder;

import java.util.List;
import java.util.Map;

/**
 * @author zhangww
 * @description
 * @create 2017-08-31 14:21
 **/
@MyBatisDao
public interface StandardOrderDao extends CrudDao<StandardOrder> {

    int deleteByids(StandardOrder standardOrder);
    Map<String,Object> getByCode(String code);
    Map getLibByPIid(String pstid);
    int updateAprByPIid(Map map);
    List<Map> getOrderwork();
    List<Map> getMajorByWork(Map map);
    List<Map> getDevinfo(Map map);

    List<Map> getAllCodes(Map paramMap);

    /**
     * @creator duanfuju
     * @createtime 2017/10/30 11:45
     * @description:
     *  根据条件获取数据（导出时）
     * @param map
     * @return
     */
    List<Map> findListByMap(Map map);
}
