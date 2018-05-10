
package com.tiansu.eam.modules.inspection.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.inspection.entity.InspectionArea;
import com.tiansu.eam.modules.inspection.entity.InspectionAreaSubject;

import java.util.List;
import java.util.Map;

/**
 * @creator duanfuju
 * @createtime 2017/10/23 11:19
 * @description: 
 *  巡检区域Dao
 */
@MyBatisDao
public interface InspectionAreaDao extends CrudDao<InspectionArea> {
    /**
     *@creator duanfuju
     * @createtime 2017/10/23 11:20
     * @description: 
     * 列表查询
     * @param map
     * @return
     */
    List<Map> findListByMap(Map map);

    /**
     *@creator duanfuju
     * @createtime 2017/10/23 11:20
     * @description: 
     * 根据id获取
     * @param id
     * @return
     */
    Map findById(String id);

    /**
     *@creator duanfuju
     * @createtime 2017/10/23 11:20
     * @description: 
     * 根据id删除（逻辑）
     * @param map
     * @return
     */
    int delete(Map map);

    /**
     *@creator duanfuju
     * @createtime 2017/10/23 11:20
     * @description:
     *批量插入(巡检区域)
     * @return
     */
    int insertBatch(List<InspectionArea> inspectionAreas);

    /**
     *@creator duanfuju
     * @createtime 2017/10/23 11:20
     * @description:
     *批量插入(巡检区域和巡检项的关联表)
     * @return
     */
    int insertBatchAreaAndSubject(List<InspectionAreaSubject> inspectionAreaSubjects);

    /**
     * @creator duanfuju
     * @createtime 2017/10/23 15:15
     * @description:
     *      根据id删除（物理删除，检区域和巡检项的关联表)）
     * @param map
     * @return
     */
    int deteleBatchAreaAndSubjectByAreaId(Map map);
}
