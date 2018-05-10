package com.tiansu.eam.modules.opestandard.controller;

import com.alibaba.fastjson.JSON;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.act.service.EamProcessService;
import com.tiansu.eam.modules.device.service.EamDeviceService;
import com.tiansu.eam.modules.material.service.MaterialInfoService;
import com.tiansu.eam.modules.opestandard.entity.*;
import com.tiansu.eam.modules.opestandard.service.EamOperationWorkService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wujh
 * @description 标准工作及标准工作审批controller
 * @create 2017-09-11 14:08
 **/
@Controller
@RequestMapping(value = "${adminPath}/eam/operationwork")
public class OperationWorkController extends BaseController {

    @Autowired
    private EamOperationWorkService eamOperationWorkService;    // 标准工作service

    @Autowired
    private EamProcessService eamProcessService;    // 工作进程service

    @Autowired
    private MaterialInfoService materialInfoService;    // 物料service

    @Autowired
    private EamDeviceService eamDeviceService;    // 设备service

    /**
     * 获取标准工作列表数据
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "listData")
    public Map<String, Object> listData(){
        Map param = super.getFormMap();
        Map<String, Object> map = eamOperationWorkService.dataTablePageMap(param);
        return map;
    }

    /**
     * 获取所有审批通过的标准工作编码（共系统需要查找标准工作时下拉使用）
     * @modifier wangjl
     * @modifytime 2017/10/20 16:37
     * @modifyDec:
     * @return
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getApprovedWorkCodes")
    public List<Map> getAllApprovedWorks(){
        Map paramMap = new HashMap();
        paramMap.put("approve_status",1);
        List<Map> findListByMap = eamOperationWorkService.getAllCodes(paramMap);
        return findListByMap;
    }
    /**
     * 标准工作新增页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "addUI")
    public String addUI() {
        return "modules/opestandard/operationworkFormAdd";
    }

    /**
     * 标准工作编辑页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "editUI")
    public String editUI(){
        return "modules/opestandard/operationworkFormEdit";
    }

    /**
     * 标准工作详情页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "detailUI")
    public String detailUI(){
        return "modules/opestandard/operationworkFormDetail";
    }

    /**
     * 标准工作新增/修改页面的设备选择页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "DeviceSelectUI")
    public String DeviceSelectUI(){
        return "modules/opestandard/operationworkDeviceSelect";
    }

    /**
     * 根据主键id获取标准信息
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "editObj")
    public Map editObj() {
        String id = getPara("id");
        return eamOperationWorkService.getEdit(id);
    }

    /**
     * @creator wujh
     * @createtime 2017/9/18 19:58
     * @description: 根据标准工作id获取该标准工作下的设备
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getDeviceInfo")
    public Map<String, Object> getDevice(){
        Map result = new HashMap();
        String deviceIds = "";
        String deviceNames = "";
        String deviceMajors = "";

        // 获取前台传参的标准工作id
        String operaworkId = getPara("operationwork_id");
        List<Map<String, Object>> devices = eamOperationWorkService.getDevice(operaworkId);
        if(devices != null && devices.size() != 0){
            for(int i = 0; i < devices.size(); i++) {
                deviceIds += devices.get(i).get("dev_id") + ",";
                deviceNames += devices.get(i).get("dev_name") + ",";
            }
            deviceIds = deviceIds.substring(0, deviceIds.length() - 1);
            deviceNames = deviceNames.substring(0, deviceNames.length() - 1);

            String[] ids_array = deviceIds.split(",");
            Map map = new HashMap();
            map.put("ids", ids_array);
            List<Map> devMajorList = eamOperationWorkService.getDeviceMajors(map);
            if(devMajorList != null && devMajorList.size() > 0) {
                for(int i = 0; i < devMajorList.size(); i++) {
                    deviceMajors += devMajorList.get(i).get("dict_name") + ",";
                }
                deviceMajors = deviceMajors.substring(0, deviceMajors.length() - 1);
            }

            result.put("deviceIds", deviceIds);
            result.put("deviceNames", deviceNames);
            result.put("deviceMajors", deviceMajors);
        }
        return result;
    }

    /**
     * @creator wujh
     * @createtime 2017/9/18 19:58
     * @description: 根据标准工作id获取该标准工作下的工序数据
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getProcedure")
    public List<Map<String, Object>> getProcedure(){
        // 获取前台传参的标准工作id
        String operaworkId = getPara("operationwork_id");
        return eamOperationWorkService.getProcedure(operaworkId);
    }

    /**
     * @creator wujh
     * @createtime 2017/9/18 19:58
     * @description: 根据标准工作id获取该标准工作下的安全措施数据
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getSafety")
    public List<Map<String, Object>> getSafety(){
        // 获取前台传参的标准工作id
        String operaworkId = getPara("operationwork_id");
        return eamOperationWorkService.getSafety(operaworkId);
    }

    /**
     * @creator wujh
     * @createtime 2017/9/18 19:58
     * @description: 根据标准工作id获取该标准工作下的工器具数据
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getTools")
    public List<Map<String, Object>> getToolInfos(){
        // 获取前台传参的标准工作id
        String operaworkId = getPara("operationwork_id");
        return eamOperationWorkService.getWorkTools(operaworkId);
    }

    /**
     * @creator wujh
     * @createtime 2017/9/18 19:58
     * @description: 根据标准工作id获取该标准工作下的备品备件数据
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getSpareparts")
    public List<Map<String, Object>> getSpareparts(){
        // 获取前台传参的标准工作id
        String operaworkId = getPara("operationwork_id");
        return eamOperationWorkService.getSpareparts(operaworkId);
    }

    /**
     * @creator wujh
     * @createtime 2017/9/18 19:58
     * @description: 根据标准工作id获取该标准工作下的人员工时数据
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getPersonhours")
    public List<Map<String, Object>> getPersonhours(){
        // 获取前台传参的标准工作id
        String operaworkId = getPara("operationwork_id");
        return eamOperationWorkService.getPersonhours(operaworkId);
    }

    /**
     * @creator wujh
     * @createtime 2017/9/18 19:58
     * @description: 根据标准工作id获取该标准工作下的其他费用数据
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getOtherexpenses")
    public List<Map<String, Object>> getOtherexpenses(){
        // 获取前台传参的标准工作id
        String operaworkId = getPara("operationwork_id");
        return eamOperationWorkService.getOtherexpenses(operaworkId);
    }

    /**
     * 新增标准工作，插入多张表数据
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "insert")
    @Transactional
    public Map<String, Object> insert(OperationWork operationWork, HttpServletRequest request){
        Map<String,Object> returnMap = new HashMap();
        String param = getPara("param");      //获取入参
        operationWork = JSON.parseObject(param, OperationWork.class);
        if(operationWork == null) {
            returnMap.put("flag",false);
            returnMap.put("msg","参数错误");
            return returnMap;
        } else if(operationWork.getOperationwork_code() != null || !"".equals(operationWork.getOperationwork_code())) {
            int cou = eamOperationWorkService.getBycode(operationWork.getOperationwork_code());
            if(cou > 0){
                returnMap.put("flag",false);
                returnMap.put("msg","编码重复");
                return returnMap;
            }
        }

        //生成主键
        String uuid = IdGen.uuid();
        operationWork.setId_key(uuid);
        String pstid = eamProcessService.startProcessByPdid("operationwork_approval","eam_operation_work",uuid,request);
        if("timeout".equals(pstid)){
            returnMap.put("flag",false);
            returnMap.put("msg","超时");
        } else {
            //设备列表
            List<OperationworkDevice> operationworkDeviceList = operationWork.getDeviceList();
            for(OperationworkDevice device : operationworkDeviceList){
                device.setId(IdGen.uuid());  //给工序表设置主键
                device.setOperationwork_id(uuid);  //给工序表set标准工作外键
            }

            //工序列表
            List<OperationworkProcedure> procedureList = operationWork.getProcedureList();
            for(OperationworkProcedure procedure : procedureList){
                procedure.setId(IdGen.uuid());  //给工序表设置主键
                procedure.setOperationwork_id(uuid);  //给工序表set标准工作外键
            }

            //安全措施列表
            List<OperationworkSafety> safetyList = operationWork.getSafetyList();
            for(OperationworkSafety safety : safetyList) {
                safety.setId(IdGen.uuid());  //给安全措施表设置主键
                safety.setOperationwork_id(uuid);  //给安全措施表set标准工作外键
            }

            // 工器具列表
            List<OperationworkTools> toolsList = operationWork.getToolsList();
            if(toolsList != null && toolsList.size() != 0) {
                for(OperationworkTools tools : toolsList) {
                    if(tools != null && tools.getMaterial_id() != null && !("").equals(tools.getMaterial_id())){
                        Map map = materialInfoService.getDetail(tools.getMaterial_id());
                        // 统计该工器具的库存数量
                        int qty = Integer.parseInt(map.get("material_qty").toString());
                        if(tools.getTools_num() > qty) {
                            returnMap.put("flag",false);
                            returnMap.put("msg","工器具数量超过库存数量" + qty);
                            return returnMap;
                        } else {
                            tools.setId(IdGen.uuid());  //给工器具表设置主键
                            tools.setOperationwork_id(uuid);  //给工器具表set标准工作外键
                        }
                    } else {
                        tools.setId(IdGen.uuid());  //给工器具表设置主键
                        tools.setOperationwork_id(uuid);  //给工器具表set标准工作外键
                    }
                }
            }

            // 备品备件列表
            List<OperationworkSpareparts> sparepartsList = operationWork.getSparepartsList();
            if(sparepartsList != null && sparepartsList.size() != 0) {
                for(OperationworkSpareparts spareparts : sparepartsList) {
                    if(spareparts != null && spareparts.getMaterial_id() != null && !("").equals(spareparts.getMaterial_id())) {
                        Map map = materialInfoService.getDetail(spareparts.getMaterial_id());
                        // 统计该工器具的库存数量
                        int qty = Integer.parseInt(map.get("material_qty").toString());
                        if(spareparts.getSpareparts_num() > qty) {
                            returnMap.put("flag",false);
                            returnMap.put("msg","备品备件数量超过库存数量" + qty);
                            return returnMap;
                        } else {
                            spareparts.setId(IdGen.uuid());  //给备品备件表设置主键
                            spareparts.setOperationwork_id(uuid);  //给备品备件表set标准工作外键
                        }
                    } else {
                        spareparts.setId(IdGen.uuid());  //给备品备件表设置主键
                        spareparts.setOperationwork_id(uuid);  //给备品备件表set标准工作外键
                    }
                }
            }

            // 人员工时列表
            List<OperationworkPerson> personList = operationWork.getPersonList();
            for(OperationworkPerson person : personList) {
                person.setId(IdGen.uuid());  //给人员工时表设置主键
                person.setOperationwork_id(uuid);  //给人员工时表set标准工作外键
            }

            // 其他费用列表
            List<OperationworkOthers> othersList = operationWork.getOthersList();
            for(OperationworkOthers others : othersList) {
                others.setId(IdGen.uuid());  //给其他费用表设置主键
                others.setOperationwork_id(uuid);  //给其他费用表set标准工作外键
            }

            operationWork.setPstid(pstid);
            //分别插入标准工作和标准工作相关联的表数据
            eamOperationWorkService.insert(operationWork);
            eamOperationWorkService.insertDetail(operationworkDeviceList, procedureList, safetyList, toolsList, sparepartsList, personList, othersList);
            returnMap.put("flag",true);
            returnMap.put("msg","操作成功");
        }
        return returnMap;
    }

    /**
     * 修改标准工作，修改多张表数据
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "update")
    @Transactional
    public Map<String, Object> update(OperationWork operationWork){
        Map<String,Object> returnMap = new HashMap();
        String param = getPara("param");      //获取入参
        operationWork = JSON.parseObject(param, OperationWork.class);
        if(operationWork == null) {
            returnMap.put("flag",false);
            returnMap.put("msg","参数错误");
            return returnMap;
        } else {
            //获取标准工作主键
            String uuid = operationWork.getId_key();
            operationWork.setId_key(uuid);
            //设备列表
            List<OperationworkDevice> operationworkDeviceList = operationWork.getDeviceList();
            for(OperationworkDevice device : operationworkDeviceList){
                device.setId(IdGen.uuid());  //给工序表设置主键
                device.setOperationwork_id(uuid);  //给工序表set标准工作外键
            }

            //工序列表
            List<OperationworkProcedure> procedureList = operationWork.getProcedureList();
            for(OperationworkProcedure procedure : procedureList){
                procedure.setId(IdGen.uuid());  //给工序表设置主键
                procedure.setOperationwork_id(uuid);  //给工序表set标准工作外键
            }

            //安全措施列表
            List<OperationworkSafety> safetyList = operationWork.getSafetyList();
            for(OperationworkSafety safety : safetyList) {
                safety.setId(IdGen.uuid());  //给安全措施表设置主键
                safety.setOperationwork_id(uuid);  //给安全措施表set标准工作外键
            }

            // 工器具列表
            List<OperationworkTools> toolsList = operationWork.getToolsList();
            for(OperationworkTools tools : toolsList) {
                if(tools != null && tools.getMaterial_id() != null && !("").equals(tools.getMaterial_id())){
                    Map map = materialInfoService.getDetail(tools.getMaterial_id());
                    // 统计该工器具的库存数量
                    int qty = Integer.parseInt(map.get("material_qty").toString());
                    if(tools.getTools_num() > qty) {
                        returnMap.put("flag",false);
                        returnMap.put("msg","工器具数量超过库存数量" + qty);
                        return returnMap;
                    } else {
                        tools.setId(IdGen.uuid());  //给工器具表设置主键
                        tools.setOperationwork_id(uuid);  //给工器具表set标准工作外键
                    }
                } else {
                    tools.setId(IdGen.uuid());  //给工器具表设置主键
                    tools.setOperationwork_id(uuid);  //给工器具表set标准工作外键
                }
            }

            // 备品备件列表
            List<OperationworkSpareparts> sparepartsList = operationWork.getSparepartsList();
            for(OperationworkSpareparts spareparts : sparepartsList) {
                if(spareparts != null && spareparts.getMaterial_id() != null && !("").equals(spareparts.getMaterial_id())) {
                    Map map = materialInfoService.getDetail(spareparts.getMaterial_id());
                    // 统计该工器具的库存数量
                    int qty = Integer.parseInt(map.get("material_qty").toString());
                    if(spareparts.getSpareparts_num() > qty) {
                        returnMap.put("flag",false);
                        returnMap.put("msg","备品备件数量超过库存数量" + qty);
                        return returnMap;
                    } else {
                        spareparts.setId(IdGen.uuid());  //给备品备件表设置主键
                        spareparts.setOperationwork_id(uuid);  //给备品备件表set标准工作外键
                    }
                } else {
                    spareparts.setId(IdGen.uuid());  //给备品备件表设置主键
                    spareparts.setOperationwork_id(uuid);  //给备品备件表set标准工作外键
                }
            }

            // 人员工时列表
            List<OperationworkPerson> personList = operationWork.getPersonList();
            for(OperationworkPerson person : personList) {
                person.setId(IdGen.uuid());  //给人员工时表设置主键
                person.setOperationwork_id(uuid);  //给人员工时表set标准工作外键
            }

            // 其他费用列表
            List<OperationworkOthers> othersList = operationWork.getOthersList();
            for(OperationworkOthers others : othersList) {
                others.setId(IdGen.uuid());  //给其他费用表设置主键
                others.setOperationwork_id(uuid);  //给其他费用表set标准工作外键
            }

            eamOperationWorkService.deleDetail(uuid);//先物理删除标准工作下的字表
            //分别插入标准工作和标准工作相关联的表数据
            eamOperationWorkService.update(operationWork);
            eamOperationWorkService.insertDetail(operationworkDeviceList, procedureList, safetyList, toolsList, sparepartsList, personList, othersList);
            returnMap.put("flag",true);
            returnMap.put("msg","操作成功");
        }
        return returnMap;
    }

    /**
     * 根据id删除标准工作信息
     * @return
     */
    @ResponseBody
    @Transactional(readOnly=false)
    @RequiresPermissions("user")
    @RequestMapping(value = "delete")
    public Map<String,Object> delete() {
        String[] ids = getPara("id").split(",");
        return eamOperationWorkService.delete(ids);
    }

    /**
     * @creator wujh
     * @createtime 2017/9/18 14:30
     * @description: 获取标准库中下拉选择工器具或备品备件的列表数据
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = {"tools"})
    public List<Map<String, Object>> getTools() {
        List<Map<String, Object>> tools = eamOperationWorkService.getTools();
        return tools;
    }

    /**
     * @creator wujh
     * @createtime 2017/9/23 11:47
     * @description: 根据工作流id获取标准库信息
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getOperaworkIdByPIid")
    public Map getOperaworkIdByPIid(){
        String pIid = getPara("pIid");
        return eamOperationWorkService.getOperaworkIdByPIid(pIid);
    }

    /**
     * 根据选中的设备id获取其专业
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getDeviceMajor")
    public List<Map> getDeviceMajors (){
        Map map = new HashMap();
        String[] ids_array = getPara("deviceIds").toString().split(",");
        map.put("ids", ids_array);
        List<Map> devMajorList = eamOperationWorkService.getDeviceMajors(map);
        return devMajorList;
    }

    /**
     * 获取设备和设备类别树数据
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getDeviceTreeData")
    public List getDeviceTreeData(){
        String res = getPara("operationwork_id");
        Map param = new HashMap();
        param.put("operationwork_id", res);
        List<Map> mapList = eamOperationWorkService.findDevCategoryList(param);
        System.out.println(mapList);

        if (mapList == null) {
            return null;
        }
        List<DeviceTreeData> deviceTreeDataList = new ArrayList();
        List<DeviceTreeData> resultList = new ArrayList<>();

        for(int i = 0; i < mapList.size(); i++){
            String pid;
            Map map = mapList.get(i);
            if(map.containsKey("pid") && map.get("pid") != null && !"".equals(map.get("pid"))) {
                pid = map.get("pid").toString();
            } else {
                pid = "0";
            }
            DeviceTreeData deviceTreeData = new DeviceTreeData(map.get("id").toString(), pid,
                    map.get("name").toString(), map.get("type").toString(), map.get("code").toString());
            deviceTreeData.setParentId(pid);
            deviceTreeData.setChildren(new ArrayList());
            deviceTreeDataList.add(deviceTreeData);
        }

        for (DeviceTreeData tree : deviceTreeDataList) {
            for (DeviceTreeData t : deviceTreeDataList) {
                if (t.getParentId().equals(tree.getId())) {
                    if (tree.getChildren() == null || tree.getChildren().size() == 0) {
                        List<DeviceTreeData> myChildrens = new ArrayList();
                        myChildrens.add(t);
                        tree.setChildren(myChildrens);
                    } else {
                        tree.getChildren().add(t);
                    }
                }
            }
        }

        for (DeviceTreeData tree : deviceTreeDataList) {
            if (DeviceTreeData.ROOT_PARENT.equals(tree.getParentId())) {
                resultList.add(tree);
            }
        }
        return resultList;
    }
    /**
     * 获取设备和设备类别树数据
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getDeviceTreeDataForInspectionSubject")
    public List getDeviceTreeDataForInspectionSubject(){
        List<Map> mapList = eamOperationWorkService.getDevCategoryList();
        if (mapList == null) {
            return null;
        }
        List<DeviceTreeData> deviceTreeDataList = new ArrayList();
        List<DeviceTreeData> resultList = new ArrayList<>();

        for(int i = 0; i < mapList.size(); i++){
            String pid;
            Map map = mapList.get(i);
            if(map.containsKey("pid") && map.get("pid") != null && !"".equals(map.get("pid"))) {
                pid = map.get("pid").toString();
            } else {
                pid = "0";
            }
            DeviceTreeData deviceTreeData = new DeviceTreeData(map.get("id").toString(), pid,
                    map.get("name").toString(), map.get("type").toString(), map.get("code").toString());
            deviceTreeData.setParentId(pid);
            deviceTreeData.setChildren(new ArrayList());
            deviceTreeDataList.add(deviceTreeData);
        }

        for (DeviceTreeData tree : deviceTreeDataList) {
            for (DeviceTreeData t : deviceTreeDataList) {
                if (t.getParentId().equals(tree.getId())) {
                    if (tree.getChildren() == null || tree.getChildren().size() == 0) {
                        List<DeviceTreeData> myChildrens = new ArrayList();
                        myChildrens.add(t);
                        tree.setChildren(myChildrens);
                    } else {
                        tree.getChildren().add(t);
                    }
                }
            }
        }

        for (DeviceTreeData tree : deviceTreeDataList) {
            if (DeviceTreeData.ROOT_PARENT.equals(tree.getParentId())) {
                resultList.add(tree);
            }
        }
        return resultList;
    }
}
