package com.tiansu.eam.modules.device.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.device.dao.EamDevCategoryDao;
import com.tiansu.eam.modules.device.entity.DevCategory;
import com.tiansu.eam.modules.device.service.EamDevCategoryService;
import com.tiansu.eam.modules.material.entity.MaterialType;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备类别Controller
 * Created by tiansu on 2017/8/7.
 */
@Controller
@RequestMapping(value = "${adminPath}/eam/devCategory")
public class EamDevCategoryController extends BaseController {

    @Autowired
    public EamDevCategoryDao eamDevCategoryDao;

    @Autowired
    public EamDevCategoryService eamDevCategoryService;

    /**
     * 获取设备类别下拉树数据
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getDevCategoryTree")
    public List<Map> getDevCategoryTree() {
        return eamDevCategoryService.getDevCategoryTree();

    }

    /**
     * 获取设备类别树信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
        List<DevCategory> devCategoryList = eamDevCategoryDao.findList(new DevCategory());
        List<Map<String, Object>> mapList = getMapList(devCategoryList);
        return mapList;
    }
//    /**
//     * 获取设备类别树顶级父级信息
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = "treeData")
//    public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
//        Map<String,Object> m=new HashMap();
//        extId = extId==null?"":extId;
//        m.put("cat_pid",extId);
//        List<Map<String,Object>> devCategoryList=eamDevCategoryDao.findListBypPid(m);
//        return devCategoryList;
//    }

    /*
    获取设备信息列表数据
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = {"listData"})
    public Map<String,Object> listData() {
        Map param = getFormMap();
        Map<String,Object> map = eamDevCategoryService.dataTablePageMap(param);

        return map;
    }

    /**
     * 获取当前菜单页面下的字段
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getfields")
    public Map getFields(){
        String menuno = getPara("menuno");
        Map<String,Object> result = getFieldsByMenuno(menuno);
        return result;
    }

    /**
     * 跳转到设备类别录入页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "addUI")
    public String addUI() {
        return "modules/device/devCategoryFormAdd";
    }

    /**
     * 跳转到设备类别编辑页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "detailUI")
    public String detailUI() {
        return "modules/device/devCategoryFormDetail";
    }

    /**
     * 跳转到设备类别详情页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "editUI")
    public String editUI() {
        return "modules/device/devCategoryFormEdit";
    }

    /**
     * 根据主键id获取设备类别信息
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "editObj")
    public Map editObj() {
        String id = getPara("id");
        return eamDevCategoryService.getEdit(id);
    }

    /**
     * 修改设备类别信息
     * @param devCategory
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "update")
    public Map<String,Object> update(DevCategory devCategory) {
        Map<String,Object> returnMap = new HashMap();
        if(devCategory == null){
            returnMap.put("flag",false);
            returnMap.put("msg","参数错误");
            return returnMap;
        } else {
            eamDevCategoryService.update(devCategory);
        }
        returnMap.put("flag",true);
        returnMap.put("msg","操作成功");
        return returnMap;
    }

    /**
     * 新增设备类别
     * @param devCategory
     * @return
     */
    @Transactional
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "insert")
    public Map<String,Object> insert(DevCategory devCategory){
        Map<String,Object> returnMap = new HashMap();
        if(devCategory == null){
            returnMap.put("flag",false);
            returnMap.put("msg","参数错误");
            return returnMap;
        } else {
            if(devCategory.getCat_code() !=null && !"".equals(devCategory.getCat_code())){
                int cou = eamDevCategoryService.getBycode(devCategory.getCat_code());
                if(cou > 0){
                    returnMap.put("flag",false);
                    returnMap.put("msg","编码重复");
                    return returnMap;
                }
            }
            eamDevCategoryService.insert(devCategory);
            eamDevCategoryService.updateDevCategoryTree();
        }
        returnMap.put("flag",true);
        returnMap.put("msg","操作成功");
        return returnMap;
    }

    /**
     * 根据id删除设备类别
     * @return
     */
    @ResponseBody
    @Transactional(readOnly=false)
    @RequiresPermissions("user")
    @RequestMapping(value = "delete")
    public Map<String,Object> delete() {
        String[] ids = getPara("id").split(",");
        String catStatus = getPara("cat_status");
        return eamDevCategoryService.delete(ids, catStatus);
    }

//    @ResponseBody
//    @RequestMapping(value="/del/{id}", method = RequestMethod.DELETE)
//    public String deleteById(@PathVariable String id, HttpServletRequest request, HttpServletResponse response){
//        return "good";
//    }

//    @RequestMapping(value="/{id}", method = RequestMethod.GET)
//    public @ResponseBody String get(@PathVariable String id){
//        return JSON.toJSONString(eamDevCategoryService.getDetail(id));
//    }

    /**
     * 获取详情页面字段数据
     * @param materialType
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "detailObj")
    public Map detailObj(MaterialType materialType) {
        String id = getPara("id");
        return eamDevCategoryService.getDetail(id);
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
