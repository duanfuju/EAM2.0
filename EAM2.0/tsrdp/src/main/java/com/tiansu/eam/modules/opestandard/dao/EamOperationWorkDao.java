package com.tiansu.eam.modules.opestandard.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.opestandard.entity.*;

import java.util.List;
import java.util.Map;

/**
 * @author wujh
 * @description 标准工作接口
 * @create 2017-09-07 14:02
 **/
@MyBatisDao
public interface EamOperationWorkDao extends CrudDao<OperationWork> {

    /**
     * 查询当前编码在数据库中的数量
     * @param code
     * @return
     */
    Map<String,Object> getByCode(String code);

    /**
     * 根据主键id获取标准工作信息
     * @param id
     * @return
     */
    Map getEdit(String id);

    /**
     * 根据标准工作主键id获取标准工作下的设备
     * @param id
     * @return
     */
    List<Map<String, Object>> getDevice(String id);

    /**
     * 根据标准工作主键id获取标准工作下的工序数据
     * @param id
     * @return
     */
    List<Map<String, Object>> getProcedure(String id);

    /**
     * 根据标准工作主键id获取标准工作下的安全措施数据
     * @param id
     * @return
     */
    List<Map<String, Object>> getSafety(String id);

    /**
     * 根据标准工作主键id获取标准工作下的工器具数据
     * @param id
     * @return
     */
    List<Map<String, Object>> getWorkTools(String id);

    /**
     * 根据标准工作主键id获取标准工作下的备品备件数据
     * @param id
     * @return
     */
    List<Map<String, Object>> getSpareparts(String id);

    /**
     * 根据标准工作主键id获取标准工作下的人员工时数据
     * @param id
     * @return
     */
    List<Map<String, Object>> getPersonhours(String id);

    /**
     * 根据标准工作主键id获取标准工作下的工序数据
     * @param id
     * @return
     */
    List<Map<String, Object>> getOtherexpenses(String id);

    /**
     * 根据实体对象删除符合条件记录
     * @param operationWork
     * @return
     */
    int delete(OperationWork operationWork);

    // 获取工器具和备品备件中的下拉列表数据
    List<Map<String, Object>> getTools();

    /**
     * 插入标准工作关系设备
     * @param deviceList
     * @return
     */
    int insertDevice(List<OperationworkDevice> deviceList);

    /**
     * 插入工序列表数据
     * @param procedureList
     * @return
     */
    int insertProcedure(List<OperationworkProcedure> procedureList);

    /**
     * 插入安全措施列表数据
     * @param safetyList
     * @return
     */
    int insertSafety(List<OperationworkSafety> safetyList);

    /**
     * 插入工器具列表数据
     * @param toolsList
     * @return
     */
    int insertTools(List<OperationworkTools> toolsList);

    /**
     * 插入备品备件列表数据
     * @param sparepartsList
     * @return
     */
    int insertSpareparts(List<OperationworkSpareparts> sparepartsList);

    /**
     * 插入人员工时列表数据
     * @param personList
     * @return
     */
    int insertPersonHours(List<OperationworkPerson> personList);

    /**
     * 插入其他费用列表数据
     * @param othersList
     * @return
     */
    int insertOthers(List<OperationworkOthers> othersList);

    /**
     * 根据标准工作主键删除其关联字表数据
     * @param operationwork_id
     * @return
     */
    int deleteDevice(String operationwork_id);
    int deleteProcedure(String operationwork_id);
    int deleteSafety(String operationwork_id);
    int deleteTools(String operationwork_id);
    int deleteSpareparts(String operationwork_id);
    int deletePersonhours(String operationwork_id);
    int deleteOthers(String operationwork_id);

    /**
     * 导出标准工作多表数据
     * @param map
     * @return
     */
    List<Map> findWorkListByMap(Map map);  //标准工作
    List<Map> findProcedureByMap(Map map);  //标准工作工序
    List<Map> findSafetyByMap(Map map);  //标准工作安全措施
    List<Map> findToolsByMap(Map map);  //标准工作工器具
    List<Map> findSparepartsByMap(Map map);  //标准工作备品备件
    List<Map> findPersonHoursByMap(Map map);  //标准工作人员工时
    List<Map> findOthersByMap(Map map);  //标准工作其他费用

    // 根据工作流id获取标准库的信息
    Map getOperaworkIdByPIid(String pstid);

    // 更新标准工作的相关审批信息
    int updateAprByPIid(Map map);

    // 根据设备id获取设备专业
    List<Map> getDeviceMajors(Map map);

    // 设备类别/设备树
    List<Map> findDevCategoryList(Map map);

    /**
     * @creator duanfuju
     * @createtime 2017/10/20 8:55
     * @description:
     *      获取设备类别和信息树
     * @return
     */
    List<Map> getDevCategoryList();

    List<Map> getAllCodes(Map paramMap);
}
