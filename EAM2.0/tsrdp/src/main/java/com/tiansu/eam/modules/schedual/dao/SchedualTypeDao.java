package com.tiansu.eam.modules.schedual.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.schedual.entity.SchedualType;
import com.tiansu.eam.modules.schedual.entity.SchedualTypeTime;

import java.util.List;
import java.util.Map;

/**
 * @author wangjl
 * @description
 * @create 2017-08-21 8:39
 **/
@MyBatisDao
public interface SchedualTypeDao extends CrudDao<SchedualType> {


    void deleteByTypeId(String id);

    void batchSaveTime(List<SchedualTypeTime> schedual_time_list);

    /**
     * @creator duanfuju
     * @createtime 2017/10/12 8:59
     * @description:
     *  批量插入
     * @param schedualTypes
     * @return
     */
    int insertBatch(List<SchedualType> schedualTypes);

    List<SchedualTypeTime> getSchedualTimes(List<Map> schedualTypeMap);
}
