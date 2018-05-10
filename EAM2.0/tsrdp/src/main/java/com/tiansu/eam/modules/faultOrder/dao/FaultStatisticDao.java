package com.tiansu.eam.modules.faultOrder.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.faultOrder.entity.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wangjl
 * @description
 * @create 2017-08-21 8:39
 **/
@MyBatisDao
public interface FaultStatisticDao extends CrudDao<FaultOrder> {


    /**
     * 统计某段期间各个部门产生的故障工单数
     * @param map
     * @return
     */
    public List<Map> getOccupancyByDept(Map map);

    /**
     * 统计某段期间各专业产生的工单各个状态下的总数
     * @param map
     * @return
     */
    public List<Map> getOrderCountsByStatus(Map map);


    /**
     * 统计各工单某个周期内增减情况
     * @param map
     * @return
     */
    public List<Map> getOrderCountsByPeriod(Map map);

    /*
    * 获取设备类别树*/
    public List<Map> getDeviceTree();
    /*
    * 获取人员树*/
    public List<Map> getPersonTree();
    /**
     * 统计某段期间某设备或人员耗材数
     * @param map
     * @return
     */
    public List<Map> getCostsByDev(Map map);

    public List<Map> getCostsByDevCategory(Map map);

    public List<Map> getoolDetByEmp(Map map);
    public List<Map> getoolDetByEmpcategory(Map map);
    /*通过上级部门查找部门*/
    public List<Map> getDeptByno(String pid);

    /*设备或类别查询总消耗*/
    public Map queryTotalCost(Map map);
    public Map queryTotalCostBycagory(Map map);
    /**
     * 统计某段期间人员绩效
     * @param map
     * @return
     */
    public List<Map> getOrderPerformanceByEmp(Map map);

    /**
     * 统计某段期间工单来源占比
     * @param map
     * @return
     */
    public List<Map> getOrderCountsBySource(Map map);

    public int getOrderCount(Map map);

    /**
     * 查询统计级别下设备大类
     * @param level
     * @return
     */
    List<Map> getDevCatalogStaticLevel(String level);

    /**
     * /**
     * 统计工单类别下设备所属大类及其数量
     * @param paramMap
     * @return
     */
    List<Map> getDevCatalogOrderCount(Map paramMap);

    /**
     * 统计完成率:
     * @param paramMap
     * @return
     */
    List<Map> countFinishOrdersByUser(Map paramMap);
    List<Map> countAcceptOrdersByUser(Map paramMap);

    /**
     * 统计及时率:
     * @param paramMap
     * @return
     */
    List<Map> countTimelyOrdersByUser(Map paramMap);

    /**
     * 统计工时:
     * @param paramMap
     * @return
     */
    List<Map> countWorkHoursByUser(Map paramMap);

    /**
     * 统计评分:
     * @param paramMap
     * @return
     */
    List<Map> countEvaluateScoreByUser(Map paramMap);

}
