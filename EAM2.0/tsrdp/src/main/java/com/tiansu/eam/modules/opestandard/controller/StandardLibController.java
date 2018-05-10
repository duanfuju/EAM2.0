package com.tiansu.eam.modules.opestandard.controller;

import com.alibaba.fastjson.JSON;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.act.service.EamProcessService;
import com.tiansu.eam.modules.opestandard.entity.*;
import com.tiansu.eam.modules.opestandard.service.StandardLibService;
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
 * @author zhangww
 * @description
 * @create 2017-08-31 14:23
 **/
@Controller
@RequestMapping(value = "${adminPath}/opestandard/standardLibrary")
public class StandardLibController extends BaseController{
    @Autowired
    private StandardLibService standardLibService;
    @Autowired
    private EamProcessService eamProcessService;//流程
    /*
    * @creator zhangww
    * @createtime 2017/9/4 14:53
    * @description:查询业务标所有数据
    * */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = {"listData"})
    public Map<String,Object> listData() {

        Map param = getFormMap();
        Map<String,Object> map = standardLibService.dataTablePageMap(param);

        return map;
    }
    /*
    * @creator zhangww
    * @createtime 2017/9/4 14:59
    * @description:新增跳转
    * */
    @RequiresPermissions("user")
    @RequestMapping(value = "addUI")
    public String addUI() {

        return "modules/opestandard/standardLibraryFormAdd";
    }
   /*
   * @creator zhangww
   * @createtime 2017/9/4 15:05
   * @description:编辑跳转
   * */
    @RequiresPermissions("user")
    @RequestMapping(value = "editUI")
    public String editUI() {

        return "modules/opestandard/standardLibraryFormEdit";

    }
    /**
     * 标准新增/修改页面的设备选择页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "DeviceSelectUI")
    public String DeviceSelectUI(){
        return "modules/opestandard/standardLibDeviceSelect";
    }
    /*
    * @creator zhangww
    * @createtime 2017/9/4 15:09
    * @description:新增操作,插入多个表
    * */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "insert")
    @Transactional
    public String insert(StandardLib standardLib,HttpServletRequest request) {
        String param=getPara("param");
        standardLib=JSON.parseObject(param,StandardLib.class);
        if(standardLib.getLibrary_code() !=null || !"".equals(standardLib.getLibrary_code())){
            int cou=standardLibService.getBycode(standardLib.getLibrary_code());//判断编码是否重复
            if(cou>0){
                return "repeat";
            }
        }
        standardLib.preInsert();
        String uuid = standardLib.getId();
        standardLib.setId_key(uuid);
        List<StandardFault> standardFaultList=standardLib.getStandardFaults();//运行标准
        for(StandardFault s:standardFaultList){
            s.setId(IdGen.uuid());
            s.setLibrary_id(uuid);
        }
        List<StandardMaintain> standardMaintainList=standardLib.getStandardMaintains();//保养标准
        for(StandardMaintain s:standardMaintainList){
            s.setId(IdGen.uuid());
            s.setLibrary_id(uuid);
        }
        List<StandardPatrol> standardPatrolList=standardLib.getStandardPatrols();//巡检标准
        for(StandardPatrol s:standardPatrolList){
            s.setId(IdGen.uuid());
            s.setLibrary_id(uuid);
        }
        List<StandardFailure> standardFailures=standardLib.getStandardFailures();//缺陷故障库
        for(StandardFailure s:standardFailures){
            s.setId(IdGen.uuid());
            s.setLibrary_id(uuid);
        }
        List<StandardOpe> standardOpeList=standardLib.getStandardOpes();//运行标准
        for(StandardOpe s:standardOpeList){
            s.setId(IdGen.uuid());
            s.setLibrary_id(uuid);
        }
        List<StandardSafety> standardSafetyList=standardLib.getStandardSafetys();//安全标准
        for(StandardSafety s:standardSafetyList){
            s.setId(IdGen.uuid());
            s.setLibrary_id(uuid);
        }
        List<StandardDevice> standardDeviceList=standardLib.getStandardDevices();
        {
            for(StandardDevice s:standardDeviceList){
                s.setId(IdGen.uuid());
                s.setLibrary_id(uuid);
            }
        }
        String pstid = eamProcessService.startProcessByPdid("library_approvenew","eam_operation_library",uuid,request);
        if("timeout".equals(pstid)){
            return "timeout";
        }
        standardLib.setPstid(pstid);
        //分别插入
        standardLibService.insert(standardLib);//插入主表
        standardLibService.insertDetail(standardFaultList,standardMaintainList,standardPatrolList,standardFailures,standardOpeList,standardSafetyList,standardDeviceList);
        return "success";
    }

    /**
     * 获取编辑页面字段数据
     * @param
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "editObj")
    public StandardLib editObj() {
        String id = getPara("id");
        return standardLibService.getEdit(id);
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "quFault")
    public List<StandardFault> quFault(){
        String id = getPara("id");
        return standardLibService.quFault(id);
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "quMaintain")
    public List<StandardMaintain> quMaintain(){
        String id = getPara("id");
        return standardLibService.quMaintain(id);
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "quPatrol")
    public List<StandardPatrol> quPatrol(){
        String id = getPara("id");
        return standardLibService.quPatrol(id);
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "quFailure")
    public List<StandardFailure> quFailure(){
        String id = getPara("id");
        return standardLibService.quFailure(id);
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "quOpe")
    public List<StandardOpe> quOpe(){
        String id = getPara("id");
        return standardLibService.quOpe(id);
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "quSafe")
    public List<StandardSafety> quSafe(){
        String id = getPara("id");
        return standardLibService.quSafe(id);
    }
    /*
    * @creator zhangww
    * @createtime 2017/9/4 15:10
    * @description:修改操作
    * */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "update")
    @Transactional
    public String update(StandardLib standardLib) {
        String param=getPara("param");
        standardLib=JSON.parseObject(param,StandardLib.class);
        standardLib.preUpdate();
        String uuid = standardLib.getId_key();
        standardLib.setId_key(uuid);
        List<StandardFault> standardFaultList=standardLib.getStandardFaults();//运行标准
        for(StandardFault s:standardFaultList){
            s.setId(IdGen.uuid());
            s.setLibrary_id(uuid);
        }
        List<StandardMaintain> standardMaintainList=standardLib.getStandardMaintains();//保养标准
        for(StandardMaintain s:standardMaintainList){
            s.setId(IdGen.uuid());
            s.setLibrary_id(uuid);
        }
        List<StandardPatrol> standardPatrolList=standardLib.getStandardPatrols();//巡检标准
        for(StandardPatrol s:standardPatrolList){
            s.setId(IdGen.uuid());
            s.setLibrary_id(uuid);
        }
        List<StandardFailure> standardFailures=standardLib.getStandardFailures();//缺陷故障库
        for(StandardFailure s:standardFailures){
            s.setId(IdGen.uuid());
            s.setLibrary_id(uuid);
        }
        List<StandardOpe> standardOpeList=standardLib.getStandardOpes();//运行标准
        for(StandardOpe s:standardOpeList){
            s.setId(IdGen.uuid());
            s.setLibrary_id(uuid);
        }
        List<StandardSafety> standardSafetyList=standardLib.getStandardSafetys();//安全标准
        for(StandardSafety s:standardSafetyList){
            s.setId(IdGen.uuid());
            s.setLibrary_id(uuid);
        }
        List<StandardDevice> standardDeviceList=standardLib.getStandardDevices();
        {
            for(StandardDevice s:standardDeviceList){
                s.setId(IdGen.uuid());
                s.setLibrary_id(uuid);
            }
        }
        standardLibService.deleDetail(uuid);//先物理删除
        //分别插入
        standardLibService.update(standardLib);//插入主表
        standardLibService.insertDetail(standardFaultList,standardMaintainList,standardPatrolList,standardFailures,standardOpeList,standardSafetyList,standardDeviceList);
        return "success";
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "delete")
    @Transactional
    public String delete(){
        //批量删除，单个删除
        String ids = getPara("id");
        StandardLib standardLib=standardLibService.getEdit(ids);
        String status=standardLib.getLibrary_status();
        String approve_status=standardLib.getApprove_status();
        if("1".equals(status)){
            return "nodele";
        }
        if(!"0".equals(approve_status)){
            return "nodelestatus";
        }
        standardLibService.deleteByids(ids);
        return "success";
    }
    /**@creator zhangww
     * @createtime 2017/8/22 9:14
     * @description:详情页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "detailUI")
    public String detailUI() {

        return "modules/opestandard/standardLibraryFormDeta";
    }

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getLibByPIid")
    public Map getLibByPIid(){
        String pIid=getPara("pIid");
        return standardLibService.getLibByPIid(pIid);
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
        List<Map> mapList = standardLibService.findDevCategoryList(param);
        System.out.println(mapList);

        if (mapList == null) {
            return null;
        }
        List<DeviceTreeData> deviceTreeDataList = new ArrayList();
        List<DeviceTreeData> resultList = new ArrayList<>();

        for(int i = 0; i < mapList.size(); i++){
            String pid;
            Map map = mapList.get(i);
            if(map.containsKey("pId") && map.get("pId") != null && !"".equals(map.get("pId"))) {
                pid = map.get("pId").toString();
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
