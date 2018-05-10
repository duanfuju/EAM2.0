package com.tiansu.eam.modules.maintain.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.maintain.entity.MaintainProjectInf;
import com.tiansu.eam.modules.maintain.entity.MaintainProjectInfDevice;
import com.tiansu.eam.modules.maintain.entity.MaintainProjectInfForImport;

import java.util.List;
import java.util.Map;

/**
 * Created by suven on 2017/11/2.
 */
@MyBatisDao
public interface MaintainProjectInfDao extends CrudDao<MaintainProjectInf> {

/**
*@Create
*@Description :判断数据是否被引用
*@Param :  * @param null
*@author : suven
*@Date : 19:27 2017/11/21
*/
int queryFromYear(Map m);
int queryFromMon(Map m);
    int deleteByids(Map map);
    Map<String,Object> getByCode(String code);
    int insertMaintCont(List list);
    int insertMaintDev(List list);
    List<String> getDevidsByMaint_id(String id_key);
    List quMaintContent(String maintSetId);
    String getProject_mode(String project_mode);
    String getProject_bm(String project_bm);
    Map getList(String id);
    int delMaintContent(String project_id);
    int delMaintDevice(String project_id);

    List<Map> findListByMap(Map map);

    /**
     * @creator duanfuju
     * @createtime 2017/11/15 15:01
     * @description:
     * 获取导出的数据
     * @param map
     * @return
     */
    List<Map> getExportData(Map map);
    /**
     * @creator duanfuju
     * @createtime 2017/11/15 15:00
     * @description: 
     *  根据年计划的id获取保养内容
     * @param map
     * @return
     */
    List<Map> findContentListByMap(Map map);

    /**
     * @creator duanfuju
     * @createtime 2017/11/15 15:00
     * @description: 
     *  根据年计划的id获取保养设备
     * @param map
     * @return
     */
    List<Map> findDeviceListByMap(Map map);

    /**
     * @creator duanfuju
     * @createtime 2017/11/15 16:21
     * @description:
     *      批量插入
     * @param maintainProjectInfForImport
     * @return
     */
    int insertBatch(List<MaintainProjectInfForImport> maintainProjectInfForImport);

    /**
     * @creator duanfuju
     * @createtime 2017/11/15 16:44
     * @description:
     *      更新单个
     * @param maintainProjectInfForImport
     * @return
     */
    int updateSingle(MaintainProjectInfForImport maintainProjectInfForImport);
}
