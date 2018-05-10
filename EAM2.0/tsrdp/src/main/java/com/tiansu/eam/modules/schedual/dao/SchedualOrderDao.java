package com.tiansu.eam.modules.schedual.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.schedual.entity.Schedual;
import com.tiansu.eam.modules.schedual.entity.SchedualImport;
import com.tiansu.eam.modules.schedual.entity.SchedualOrder;

import java.util.List;
import java.util.Map;

/**
 * @author wangjl
 * @description
 * @create 2017-08-21 8:39
 **/
@MyBatisDao
public interface SchedualOrderDao extends CrudDao<SchedualOrder> {

    /**
     * 根据排班表表单获取所有排班记录
     * @param schedualOrder
     * @return
     */
    List<Schedual> findListByOrder(SchedualOrder schedualOrder);

    /**
     * @creator duanfuju
     * @createtime 2017/10/11 8:37
     * @description:
     *      获取导出数据
     * @param map
     * @return
     */
    List<Map> getExportData(Map map);

    /**
     * @creator duanfuju
     * @createtime 2017/10/12 15:32
     * @description:
     *      批量插入
     * @param schedualExports
     * @return
     */
    int insertBatch(List<SchedualImport> schedualExports);
}
