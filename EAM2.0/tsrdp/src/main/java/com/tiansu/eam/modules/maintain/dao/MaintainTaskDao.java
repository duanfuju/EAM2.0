package com.tiansu.eam.modules.maintain.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.inspection.entity.*;
import com.tiansu.eam.modules.maintain.entity.*;

import java.util.List;
import java.util.Map;

/**
 * @author caoh
 * @description 保养任务Dao
 * @create 2017-10-13 10:03
 **/
@MyBatisDao
public interface MaintainTaskDao extends CrudDao<MaintainTask> {
    // 查询单个保养任务中的保养内容
    public List<Map> findMaintainProject(String id);

    //查询反馈页面中的详情数据
    public Map getFeedBackDatas(String id);

    //  派单实际消耗
    public int insertActualTool(List<MaintainTool> list);
    public int insertActualSparepart(List<MaintainAttachment> list);
    public int insertActualPerson(List<MaintainEmp> list);
    public int insertActualOther(List<MaintainOther> list);

    //public String getInspectionIdByPIid(String piid);
    public Map getMaintainTaskByPiid(String piid);

    public int updateMaintainTaskByPiid(Map map);

    public List getTaskMajorByPiid(String piid);

    public List<Map> getScheduleByTaskDate(String dateStr);

    public String getProcessorByDuty(String project_id);

    public int updateMaintainTaskProcessorPlan(Map map);

    //生成的多个保养任务
    public int insertTask(List list);

}
