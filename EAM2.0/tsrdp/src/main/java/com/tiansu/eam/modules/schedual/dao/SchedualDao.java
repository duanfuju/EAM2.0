package com.tiansu.eam.modules.schedual.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.schedual.entity.Schedual;
import com.tiansu.eam.modules.schedual.entity.SchedualImport;

import java.util.List;

/**
 * @author wangjl
 * @description
 * @create 2017-08-21 8:39
 **/
@MyBatisDao
public interface SchedualDao extends CrudDao<Schedual> {

    void batchInsert(List<Schedual> schedualList);

    void deleteByOrderId(String order_id);

    /**
     * @creator duanfuju
     * @createtime 2017/10/12 15:32
     * @description:
     *      批量插入
     * @param schedualExports
     * @return
     */
    int insertBatch(List<SchedualImport> schedualExports);

    List<Schedual> getSchedualsByDate(String dateStr);
}
