
package com.tiansu.eam.modules.maintain.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.maintain.entity.MaintainProject;
import com.tiansu.eam.modules.maintain.entity.MaintainProjectContent;
import com.tiansu.eam.modules.maintain.entity.MaintainProjectDevice;

import java.util.List;
import java.util.Map;

/**
 * @creator duanfuju
 * @createtime 2017/11/2 15:29
 * @description:
 *      保养年计划 Dao
 */
@MyBatisDao
public interface MaintainProjectDao extends CrudDao<MaintainProject> {

    /**
     * @creator duanfuju
     * @createtime 2017/11/14 11:14
     * @description:
     * 获取导出的数据
     * @param map
     * @return
     */
    List<Map> getExportData(Map map);
    /**
     * @creator duanfuju
     * @createtime 2017/11/7 17:31
     * @description:
     *      根据保养设置id获取关联设备
     * @return
     */
    List<Map> getNeedInsertDevicesByProjectIds(Map map);

    /**
     * @creator duanfuju
     * @createtime 2017/11/7 17:31
     * @description:
     *      根据保养设置id获取关联内容
     * @return
     */
    List<Map> getNeedInsertContentsByProjectIds(Map map);
    /**
     * @creator duanfuju
     * @createtime 2017/11/7 17:01
     * @description:
     *      获取保养设置
     * @param map
     * @return
     */
    List<Map> getNeedInsertProject (Map map);
    /**
     * @creator duanfuju
     * @createtime 2017/11/7 11:11
     * @description:
     * 生成功能
     * @param map
     * @return
     */
    int approval(Map map);

    /**
     * @creator duanfuju
     * @createtime 2017/11/14 17:13
     * @description: 
     * 审批（流程启动中）
     * @param map
     * @return
     */
    int approvalsp(Map map);
    /**
     * @creator duanfuju
     * @createtime 2017/11/2 15:29
     * @description: 
     * 列表查询
     * @param map
     * @return
     */
    List<Map> findListByMap(Map map);

    /**
     *@creator duanfuju
     * @createtime 2017/11/2 15:29
     * @description: 
     * 根据id获取
     * @param id
     * @return
     */
    Map findById(String id);

    /**
     * @creator duanfuju
     * @createtime 2017/11/15 8:57
     * @description:
     * 根据流程实例id获取数据
     * @param pstid
     * @return
     */
    Map findByPstid(String pstid);


    /**
     * @creator duanfuju
     * @createtime 2017/11/2 15:48
     * @description:
     *  根据年计划的id获取保养内容
     * @param map
     * @return
     */
    List<Map> findContentListByMap(Map map);

    /**
     * @creator duanfuju
     * @createtime 2017/11/2 15:49
     * @description:
     *  根据年计划的id获取保养设备
     * @param map
     * @return
     */
    List<Map> findDeviceListByMap(Map map);

    /**
     *@creator duanfuju
     * @createtime 2017/11/2 15:29
     * @description: 
     * 根据id删除（逻辑）
     * @param map
     * @return
     */
    int delete(Map map);

    /**
     * @creator duanfuju
     * @createtime 2017/11/2 15:42
     * @description:
     *      删除保养内容（物理）
     * @param map
     * @return
     */
    int deleteContent(Map map);

    /**
     * @creator duanfuju
     * @createtime 2017/11/2 15:43
     * @description:
     *      删除关联设备（物理）
     * @param map
     * @return
     */
    int deleteDevice(Map map);

    /**
     * @creator duanfuju
     * @createtime 2017/11/2 15:45
     * @description:
     *  批量新增保养内容
     * @param maintainProjectContents
     * @return
     */
    int insertBatchContent(List<MaintainProjectContent> maintainProjectContents);

    /**
     * @creator duanfuju
     * @createtime 2017/11/2 15:46
     * @description:
     *  批量新增保养设备
     * @param maintainProjectDevices
     * @return
     */
    int insertBatchDevice(List<MaintainProjectDevice> maintainProjectDevices);

    /**
     * @creator duanfuju
     * @createtime 2017/11/7 16:48
     * @description:
     *      批量新增
     * @param maintainProjects
     * @return
     */
    int  insertBatchProject(List<MaintainProject> maintainProjects);


}
