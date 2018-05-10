package com.tiansu.eam.modules.opestandard.service;

import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.opestandard.dao.EamOperationWorkDao;
import com.tiansu.eam.modules.opestandard.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tiansu
 * @description
 * @create 2017-09-07 14:21
 **/
@Service
public class EamOperationWorkService extends CrudService<EamOperationWorkDao, OperationWork> {

    @Autowired
    private EamOperationWorkDao eamOperationWorkDao;

    public Map dataTablePageMap(Map map) {
        Map datamap = super.dataTablePageMap(map);
        List<Map> datas=( List<Map>)datamap.get("data");
        StringBuffer device_name = null;
        for(Map data : datas){
            device_name = new StringBuffer();
            if(data.get("id_key") != null){
                List<Map<String, Object>> devlist = eamOperationWorkDao.getDevice((String)data.get("id_key"));
                for(Map devmap : devlist){
                    device_name.append("," + devmap.get("dev_name"));
                }
                if(device_name.indexOf(",") !=-1) {
                    data.put("device_name", device_name.substring(1));
                }
            }
        }
        return datamap;
    }

    /**
     *  创建新的标准工作
     *  新建标准工作，其附带的工序等表也要插入赋值
     * @param operationWork
     */
    public void insert(OperationWork operationWork){
        operationWork.preInsert();
        eamOperationWorkDao.insert(operationWork);
    }

    /**
     * @creator wujh
     * @createtime 2017/9/20 14:43
     * @description: 分别给工序/安全措施/工器具/备品备件/人员工时/其他费用插入新增数据
     * @param procedureList 工序列表
     * @param safetyList  安全措施列表
     * @param toolsList  工器具列表
     * @param sparepartsList  备品备件列表
     * @param personList  人员工时列表
     * @param othersList  其他费用列表
     */
    public void insertDetail(List<OperationworkDevice> deviceList, List<OperationworkProcedure> procedureList, List<OperationworkSafety> safetyList, List<OperationworkTools> toolsList,
                             List<OperationworkSpareparts> sparepartsList, List<OperationworkPerson> personList, List<OperationworkOthers> othersList){
        eamOperationWorkDao.insertDevice(deviceList);
        eamOperationWorkDao.insertProcedure(procedureList);
        eamOperationWorkDao.insertSafety(safetyList);
        eamOperationWorkDao.insertTools(toolsList);
        eamOperationWorkDao.insertSpareparts(sparepartsList);
        eamOperationWorkDao.insertPersonHours(personList);
        eamOperationWorkDao.insertOthers(othersList);
    }

    /**
     * 根据主键id获取要编辑的标准工作内容
     * @param id
     * @return
     */
    public OperationWork get(String id){
        return eamOperationWorkDao.get(id);
    }

    /**
     * 修改标准工作内容
     * @param operationWork
     */
    public void update(OperationWork operationWork){
        operationWork.preUpdate();
        eamOperationWorkDao.update(operationWork);
    }

    /**
     * @creator wujh
     * @createtime 2017/9/21 14:07
     * @description: 根据标准工作主键删除其关联字表数据
     * @param operationwork_id
     */
    public void deleDetail(String operationwork_id){
        eamOperationWorkDao.deleteDevice(operationwork_id);  //删除该标准工作下的设备
        eamOperationWorkDao.deleteProcedure(operationwork_id);  //删除工序
        eamOperationWorkDao.deleteSafety(operationwork_id);  //删除安全措施
        eamOperationWorkDao.deleteTools(operationwork_id);  //删除工器具
        eamOperationWorkDao.deleteSpareparts(operationwork_id);  //删除备品备件
        eamOperationWorkDao.deletePersonhours(operationwork_id);  //删除人员工时
        eamOperationWorkDao.deleteOthers(operationwork_id);  //删除其他事项
    }

    /**
     * 判断当前的标准工作编码是否在库中已经存在
     * @param code
     * @return
     */
    public int getBycode(String code){
        Map<String,Object> map= eamOperationWorkDao.getByCode(code);
        return (int)map.get("cou");
    }

    /**
     * 根据主键id获取标准工作信息
     * @param id
     * @return
     */
    public Map getEdit(String id){
        return eamOperationWorkDao.getEdit(id);
    }

    /**
     * 根据标准工作id获取该标准工作下的设备
     * @param id
     * @return
     */
    public List<Map<String, Object>> getDevice(String id){
        return eamOperationWorkDao.getDevice(id);
    }

    /**
     * @creator wujh
     * @createtime 2017/9/18 20:01
     * @description: 根据标准工作id获取该标准工作下的工序数据
     * @param id
     * @return
     */
    public List<Map<String, Object>> getProcedure(String id) {
        return eamOperationWorkDao.getProcedure(id);
    }

    /**
     * @creator wujh
     * @createtime 2017/9/18 20:01
     * @description: 根据标准工作id获取该标准工作下的安全措施数据
     * @param id
     * @return
     */
    public List<Map<String, Object>> getSafety(String id) {
        return eamOperationWorkDao.getSafety(id);
    }

    /**
     * @creator wujh
     * @createtime 2017/9/18 20:01
     * @description: 根据标准工作id获取该标准工作下的工器具数据
     * @param id
     * @return
     */
    public List<Map<String, Object>> getWorkTools(String id) {
        return eamOperationWorkDao.getWorkTools(id);
    }

    /**
     * @creator wujh
     * @createtime 2017/9/18 20:01
     * @description: 根据标准工作id获取该标准工作下的备品备件数据
     * @param id
     * @return
     */
    public List<Map<String, Object>> getSpareparts(String id) {
        return eamOperationWorkDao.getSpareparts(id);
    }

    /**
     * @creator wujh
     * @createtime 2017/9/18 20:01
     * @description: 根据标准工作id获取该标准工作下的工序数据
     * @param id
     * @return
     */
    public List<Map<String, Object>> getPersonhours(String id) {
        return eamOperationWorkDao.getPersonhours(id);
    }

    /**
     * @creator wujh
     * @createtime 2017/9/18 20:01
     * @description: 根据标准工作id获取该标准工作下的其他费用数据
     * @param id
     * @return
     */
    public List<Map<String, Object>> getOtherexpenses(String id) {
        return eamOperationWorkDao.getOtherexpenses(id);
    }

    /**
     * @creator wujh
     * @createtime 2017/9/16 10:50
     * @description: 根据前台传递的id删除标准工作信息
     * @param ids 可以批量删除
     * @return
     */
    public Map<String,Object> delete(String[] ids){
        Map<String,Object> returnMap = new HashMap();
        if(ids == null || ids.length == 0){
            returnMap.put("flag",false);
            returnMap.put("msg","参数错误");
            return returnMap;
        } else {
            for(int i = 0; i < ids.length; i++) {
                // 根据id获取标准工作信息
                OperationWork operationWork = get(ids[i]);
                if("1".equals(operationWork.getOperationwork_status())){
                    returnMap.put("flag",false);
                    returnMap.put("msg","此数据为有效数据不允许删除");
                    return returnMap;
                } else if("1".equals(operationWork.getApprove_status()) || "2".equals(operationWork.getApprove_status())){
                    returnMap.put("flag",false);
                    returnMap.put("msg","审批流程已经完成的标准工作不可编辑");
                    return returnMap;
                } else {
                    operationWork.preUpdate();
                    operationWork.setId_key(ids[i]);
                    eamOperationWorkDao.delete(operationWork);
                    returnMap.put("flag",true);
                    returnMap.put("msg","删除成功");
                }
            }
        }

        return returnMap;
    }

    /**
     * @createtime 2017/9/18 14:34
     * @description: 获取下拉工器具数据
     * @return
     */
    public List<Map<String, Object>> getTools(){
        return eamOperationWorkDao.getTools();
    }

    /**
     * 标准工作导出方法
     * @param map
     * @return
     */
    public List<Map> findWorkListByMap(Map map){
        List<Map> datas=eamOperationWorkDao.findWorkListByMap(map);
        StringBuffer device_name = null;
        StringBuffer device_code = null;
        for(Map data : datas){
            device_name = new StringBuffer();
            device_code = new StringBuffer();
            if(data.get("id_key") != null){
                List<Map<String, Object>> devlist = eamOperationWorkDao.getDevice((String)data.get("id_key"));
                for(Map devmap : devlist){
                    device_name.append("," + devmap.get("dev_name"));
                    device_code.append("," + devmap.get("dev_code"));
                }
                if(device_name.indexOf(",") !=-1) {
                    data.put("dev_name", device_name.substring(1));
                    data.put("dev_code", device_code.substring(1));
                }
            }
        }
        return datas;
    }
    public List<Map> findProcedureByMap(Map map){
        return eamOperationWorkDao.findProcedureByMap(map);
    }
    public List<Map> findSafetyByMap(Map map){
        return eamOperationWorkDao.findSafetyByMap(map);
    }
    public List<Map> findToolsByMap(Map map){
        return eamOperationWorkDao.findToolsByMap(map);
    }
    public List<Map> findSparepartsByMap(Map map){
        return eamOperationWorkDao.findSparepartsByMap(map);
    }
    public List<Map> findPersonHoursByMap(Map map){
        return eamOperationWorkDao.findPersonHoursByMap(map);
    }
    public List<Map> findOthersByMap(Map map){
        return eamOperationWorkDao.findOthersByMap(map);
    }

    /**
     * @creator wujh
     * @createtime 2017/9/23 11:49
     * @description:  根据工作流id获取标准库的信息
     * @param pstid
     * @return
     */
    public Map getOperaworkIdByPIid(String pstid) {
        return eamOperationWorkDao.getOperaworkIdByPIid(pstid);
    }

    /**
     * 修改标准工作的审批相关信息
     * @param map
     */
    public void updateAprByPIid(Map map){
        eamOperationWorkDao.updateAprByPIid(map);
    }

    public List<Map> getDeviceMajors(Map map){
        return eamOperationWorkDao.getDeviceMajors(map);
    }

    /**
     * 查询设备类别/设备树
     * @param map
     * @return
     */
    public List<Map> findDevCategoryList(Map map){
        return eamOperationWorkDao.findDevCategoryList(map);
    }

    /**
     * 查询设备类别/设备树
     * @return
     */
    public List<Map> getDevCategoryList(){
        return eamOperationWorkDao.getDevCategoryList();
    }

    public List<Map> getAllCodes(Map paramMap) {
        return eamOperationWorkDao.getAllCodes(paramMap);
    }
}
