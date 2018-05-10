package com.tiansu.eam.modules.device.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.device.entity.DevCategory;
import com.tiansu.eam.modules.device.entity.DevSpareParts;
import com.tiansu.eam.modules.device.entity.DevTools;
import com.tiansu.eam.modules.device.entity.Device;
import com.tiansu.eam.modules.device.service.EamDeviceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备信息Controller
 * Created by tiansu on 2017/8/8.
 */
@Controller
@RequestMapping(value = "${adminPath}/eam/device")
public class EamDeviceController extends BaseController {

    @Autowired
    private EamDeviceService eamDeviceService;

    /**
     * 获取设备树信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map> treeData(@RequestParam(required=false) String extId, HttpServletResponse response){
        Map map = getFormMap();
        List<Map> treeDatas = eamDeviceService.findDevCategoryList(map);
        return treeDatas;
    }

    /**
     * 获取设备树信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "categoryTreeData")
    public List<Map> deviceTreeData(@RequestParam(required=false) String extId, HttpServletResponse response){
        Map map = getFormMap();
        List<Map> treeDatas = eamDeviceService.findDeviceTreeList(map);
        return treeDatas;
    }

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "listData")
    public Map<String, Object> listData(){
        Map param = super.getFormMap();
        Map<String, Object> map = eamDeviceService.dataTablePageMap(param);
        return map;
    }

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getfields")
    public Map getFields(){
        String menuno = getPara("menuno");
        Map<String, Object> result = getFieldsByMenuno(menuno);
        return result;
    }

    /**
     * 设备新增页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "addUI")
    public String addUI() {
        return "modules/device/deviceFormAdd";
    }

    /**
     * 设备修改页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "editUI")
    public String editUI() {
        return "modules/device/deviceFormEdit";
    }

    /**
     * @creator wujh
     * @createtime 2017/9/5 14:34
     * @description: 设备信息的详情页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "detailUI")
    public String detailUI() {

        return "modules/device/deviceFormDetail";
    }

    /**
     * @creator wujh
     * @createtime 2017/9/28 16:38
     * @description: 备品备件和工器具选择页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "MaterialSelectUI")
    public String MaterialSelectUI(){
        return "modules/device/MaterialSelectUI3";
    }

    /**
     * @creator shufq
     * @createtime 2017/11/25 15:38
     * @description: 备品备件和工器具已选页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "MaterialSelectedUI")
    public String MaterialSelectedUI(){
        return "modules/device/MaterialSelectedUI";
    }

    /**
     * @creator wujh
     * @createtime 2017/9/28 16:38
     * @description: 二维码打印预览页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "qrCodePrintUI")
    public String qrCodePrintUI(){
        return "modules/device/qrCodePrintUI";
    }

    /**
     * 根据主键id获取设备信息
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "editObj")
    public Map editObj() {
        String id = getPara("id");
        return eamDeviceService.getEdit(id);
    }

    /**
     * 新增设备信息
     * @param device
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "insert")
    public Map<String,Object> insert(Device device){
        Map<String,Object> returnMap = new HashMap();
        String param = getPara("param");      //获取入参
        device = JSON.parseObject(param, Device.class);
        if(device == null) {
            returnMap.put("flag",false);
            returnMap.put("msg","参数错误");
            return returnMap;
        } else if(device.getDev_code() != null || ("").equals(device.getDev_code())) {
            int cou = eamDeviceService.getBycode(device.getDev_code());
            if(cou > 0){
                returnMap.put("flag",false);
                returnMap.put("msg","编码重复");
                return returnMap;
            }

            //生成主键
            String uuid = IdGen.uuid();
            device.setId_key(uuid);

            // 备品备件列表
            List<DevSpareParts> parts = new ArrayList<>();
            List<DevSpareParts> sparePartsList = device.getSparePartsList();
            for(DevSpareParts devSpareParts : sparePartsList){
                if(devSpareParts != null && devSpareParts.getMaterial_id() != null && !"".equals(devSpareParts.getMaterial_id())){
                    devSpareParts.setId(IdGen.uuid());  //给备品备件表设置主键
                    devSpareParts.setDev_id(uuid);  //给备品备件表set设备信息外键
                    parts.add(devSpareParts);
                }
            }

            // 工器具列表
            List<DevTools> tools = new ArrayList<>();
            List<DevTools> devToolsList = device.getToolsList();
            for(DevTools devTools : devToolsList){
                if(devTools != null && devTools.getMaterial_id() != null && !"".equals(devTools.getMaterial_id())) {
                    devTools.setId(IdGen.uuid());  //给备品备件表设置主键
                    devTools.setDev_id(uuid);  //给备品备件表set设备信息外键
                    tools.add(devTools);
                }
            }

            eamDeviceService.insert(device);
            eamDeviceService.insertDetail(parts, tools);
            returnMap.put("flag",true);
            returnMap.put("msg","操作成功");
        }
        return returnMap;
    }

    /**
     * 修改设备信息
     * @param device
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "update")
    public Map<String,Object> update(Device device) {
        Map<String,Object> returnMap = new HashMap();
        String param = getPara("param");      //获取入参
        device = JSON.parseObject(param, Device.class);
        if(device == null) {
            returnMap.put("flag",false);
            returnMap.put("msg","参数错误");
            return returnMap;
        }else {
            //获取主键
            String uuid = device.getId_key();
            device.setId_key(uuid);

            // 备品备件列表
            List<DevSpareParts> sparePartsList = device.getSparePartsList();
            for(DevSpareParts devSpareParts : sparePartsList){
                devSpareParts.setId(IdGen.uuid());  //给备品备件表设置主键
                devSpareParts.setDev_id(uuid);  //给备品备件表set设备信息外键
            }

            // 工器具列表
            List<DevTools> devToolsList = device.getToolsList();
            for(DevTools devTools : devToolsList){
                devTools.setId(IdGen.uuid());  //给备品备件表设置主键
                devTools.setDev_id(uuid);  //给备品备件表set设备信息外键
            }

            eamDeviceService.deleDetail(uuid);//先物理删除设备信息表下的子表

            //分别插入标准工作和标准工作相关联的表数据
            eamDeviceService.update(device);
            eamDeviceService.insertDetail(sparePartsList, devToolsList);
            returnMap.put("flag",true);
            returnMap.put("msg","操作成功");
        }
        return returnMap;
    }

    /**
     * @creator wujh
     * @createtime 2017/9/18 19:58
     * @description: 根据设备id获取该设备下挂的工器具和备品备件
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getMaterials")
    public Map<String, Object> getDevice(){
        Map result = new HashMap();
        String materialIds = "";
        String materialNames = "";

        // 获取前台传参的标准工作id
        String deviceId = getPara("device_id");
        String typeFlag = getPara("type_flag");
        Map param = new HashMap();
        param.put("deviceId", deviceId);
        param.put("typeFlag", typeFlag);
        if(deviceId != null && !("").equals(deviceId)){
            List<Map<String, Object>> materials = eamDeviceService.getMaterials(param);
            if(materials != null && materials.size() != 0){
                for(int i = 0; i < materials.size(); i++) {
                    materialIds += materials.get(i).get("id_key") + ",";
                    materialNames += materials.get(i).get("material_name") + ",";
                }
                materialIds = materialIds.substring(0, materialIds.length() - 1);
                materialNames = materialNames.substring(0, materialNames.length() - 1);

                result.put("materialList", materials);
                result.put("materialIds", materialIds);
                result.put("materialNames", materialNames);
            }
        }

        return result;
    }

    /**
     * 根据id删除设备信息
     * @return
     */
    @ResponseBody
    @Transactional(readOnly=false)
    @RequiresPermissions("user")
    @RequestMapping(value = "delete")
    public Map<String,Object> delete() {
        String[] ids = getPara("id").split(",");
        return eamDeviceService.delete(ids);
    }

    /**
     * 将获取到的设备类别列表
     * @param devCategoryList
     * @return
     */
    public List<Map<String, Object>> getMapList(List<DevCategory> devCategoryList){
        List<Map<String, Object>> mapList = Lists.newArrayList();
        if(devCategoryList != null && devCategoryList.size() != 0) {
            for(int i = 0; i < devCategoryList.size(); i++) {
                DevCategory devCategory = devCategoryList.get(i);
                if(devCategory != null && devCategory.getCat_id() != null && devCategory.getCat_name() != null) {
                    Map<String, Object> map = Maps.newHashMap();
                    map.put("id", devCategory.getCat_id());
                    map.put("pId", devCategory.getCat_pid());
                    map.put("name", devCategory.getCat_name());
                    mapList.add(map);
                }
            }
        }
        return mapList;
    }
}
