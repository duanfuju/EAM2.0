package com.tiansu.eam.modules.inspection.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.inspection.entity.*;
import com.tiansu.eam.modules.opestandard.entity.StandardFailure;

import java.util.List;
import java.util.Map;

/**
 * @author wujh
 * @description 巡检任务接口
 * @create 2017-10-13 10:03
 **/
@MyBatisDao
public interface InspectionTaskDao extends CrudDao<InspectionTask> {
    // 查询巡检任务列表数据
    public List<Map> findListByMap(Map map);

    // 根据id获取巡检任务详情
    public Map<String,Object> getEdit(String id);
    public Map<String,Object> getInspectionTaskByPIid(String pstid);
    public Map<String, Object> getInspectionTaskById(String inspectionTask_id);

    /**
     * 根据巡检任务主键id获取巡检任务下的工序数据
     * @param inspectiontask_id
     * @return
     */
    public List<Map<String, Object>> getProcedure(String inspectiontask_id);

    /**
     * 根据巡检任务主键id获取巡检任务下的安全措施数据
     * @param id
     * @return
     */
    public List<Map<String, Object>> getSafety(String id);

    /**
     * 根据巡检任务主键id获取巡检任务下的工器具数据
     * @param id
     * @return
     */
    public List<Map<String, Object>> getWorkTools(String id);

    /**
     * 根据巡检任务主键id获取巡检任务下的备品备件数据
     * @param id
     * @return
     */
    public List<Map<String, Object>> getSpareparts(String id);

    /**
     * 根据巡检任务主键id获取巡检任务下的人员工时数据
     * @param id
     * @return
     */
    public List<Map<String, Object>> getPersonhours(String id);

    /**
     * 根据巡检任务主键id获取巡检任务下的工序数据
     * @param id
     * @return
     */
    public List<Map<String, Object>> getOtherexpenses(String id);

    /**
     * 插入工序列表数据
     * @param procedureList
     * @return
     */
    public int insertProcedure(List<InspectionTaskProcedure> procedureList);

    /**
     * 插入安全措施列表数据
     * @param safetyList
     * @return
     */
    public int insertSafety(List<InspectiontaskSafety> safetyList);

    /**
     * 插入工器具列表数据
     * @param toolsList
     * @return
     */
    public int insertTools(List<InspectiontaskTools> toolsList);

    /**
     * 插入备品备件列表数据
     * @param sparepartsList
     * @return
     */
    public int insertSpareparts(List<InspectiontaskSpareparts> sparepartsList);

    /**
     * 插入人员工时列表数据
     * @param personList
     * @return
     */
    public int insertPersonHours(List<InspectiontaskPerson> personList);

    /**
     * 插入其他费用列表数据
     * @param othersList
     * @return
     */
    public int insertOthers(List<InspectiontaskOthers> othersList);

    /**
     * 根据巡检任务主键删除其关联字表数据
     * @param inspectionroute_id
     * @return
     */
    public int deleteProcedure(String inspectionroute_id);
    public int deleteSafety(String inspectionroute_id);
    public int deleteTools(String inspectionroute_id);
    public int deleteSpareparts(String inspectionroute_id);
    public int deletePersonhours(String inspectionroute_id);
    public int deleteOthers(String inspectionroute_id);

    // 根据工作流id获取标准库的信息
    public Map getInspectionIdByPIid(String pstid);

    // 批量生成巡检任务
    public int insert(List<InspectionTask> list);

    // 更新巡检任务的相关审批信息
    public int updateAprByPIid(Map map);

    // 更新申请审批信息
    public int updateSwitchByInsId(InspectionTaskSwitch inspectionTaskSwitch);
    public int updateRelieveTimeByInsId(InspectionTaskSwitch inspectionTaskSwitch);

    // 撤销/转单/挂起信息存储
    public int insertTaskSwitch(InspectionTaskSwitch inspectionTaskSwitch);

    // 根据任务id/流程id获取任务下设备的位置区域和设备专业
    public List<Map> getDeviceInfos(Map map);

    // 根据任务id获取到转单的switch表信息
    public InspectionTaskSwitch getInspectionTaskSwitch(String inspectiontask_id);

    // 根据任务流程id获取该任务所覆盖的巡检区域
    public List<Map> getAreaInfoByTaskPstid(String pstid);

    // 根据巡检区域id获取区域下的巡检项/设备信息
    public List<Map> getSubjectInfos(Map map);

    // 插入巡检项反馈信息
    public int insertFeedBackList(List<InspectiontaskFeedback> inspectiontaskFeedbackList);

    // 删除已保存的巡检项反馈信息
    public int deleteFeedBack(Map map);

    /**
     * 根据任务id获取当前任务id已反馈保存的巡检项反馈信息
     * @param map
     * @return
     */
    public List<InspectiontaskFeedback> getFeedBackInfoByTaskId(Map map);

    // 根据设备id获取标准库下的故障标准信息
    public List<StandardFailure> getFailureList(String dev_id);

    /**
     * @creator wangr
     * @createtime 2017/11/3 0003 下午 5:13
     * @description: 巡检任务反馈时 根据 inspectiontask_id 更新表eam_inspectiontask_feedback字段issubmit为1
     * @param map
     * @return
     */
    int updateIssubmit(Map map);

    public List<Map> getPersonInfos(Map map);

    public List<Map> getToolInfos(Map map);

    public List<Map> getSparepartsInfos(Map map);

    public List<Map> getOtherexpenseInfos(Map map);
}
