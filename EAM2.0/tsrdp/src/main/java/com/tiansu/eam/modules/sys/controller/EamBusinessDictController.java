package com.tiansu.eam.modules.sys.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.sys.entity.DictTreeData;
import com.tiansu.eam.modules.sys.entity.EamBusinessDict;
import com.tiansu.eam.modules.sys.service.EamBusinessDictService;
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
 * @author wujh
 * @description 业务字典controller
 * @create 2017-11-30 14:24
 **/
@Transactional
@Controller
@RequestMapping(value = "${adminPath}/eam/bizDict")
public class EamBusinessDictController  extends BaseController {

    @Autowired
    private EamBusinessDictService eamBusinessDictService;

    /**
     * 获取设备类别树信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
        List<EamBusinessDict> businessDictList = eamBusinessDictService.findList(new EamBusinessDict());
        List<Map<String, Object>> mapList = getMapList(businessDictList);
        return mapList;
    }

    /**
     * 获取业务字段列表
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = {"listData"})
    public Map<String,Object> listData() {
        Map param = getFormMap();
        Map<String,Object> map = eamBusinessDictService.dataTablePageMap(param);
        return map;
    }

    /**
     * 跳转到业务字典录入页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "addUI")
    public String addUI() {
        return "modules/eamsys/sysConfig/businessDictFormAdd";
    }

    /**
     * 跳转到设备类别编辑页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "editUI")
    public String editUI() {
        return "modules/eamsys/sysConfig/businessDictFormEdit";
    }

    /**
     * 跳转到设备类别编辑页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "detailUI")
    public String detailUI() {
        return "modules/eamsys/sysConfig/businessDictFormDetail";
    }

    /**
     * 根据主键id获取业务字典信息
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "editObj")
    public Map editObj() {
        String id = getPara("id");
        return eamBusinessDictService.getEdit(id);
    }

    /**
     * 修改业务字典信息
     * @param dict
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "update")
    public Map<String,Object> update(EamBusinessDict dict) {
        Map<String,Object> returnMap = new HashMap();
        String param = getPara("param");      //获取入参
        dict = JSON.parseObject(param, EamBusinessDict.class);
        if(dict == null){
            returnMap.put("flag",false);
            returnMap.put("msg","参数错误");
            return returnMap;
        } else {
            eamBusinessDictService.update(dict);
            returnMap.put("flag",true);
            returnMap.put("msg","操作成功");
        }

        return returnMap;
    }

    /**
     * 根据id作废某条业务字典
     * @return
     */
    @ResponseBody
    @Transactional(readOnly=false)
    @RequiresPermissions("user")
    @RequestMapping(value = "close")
    public Map<String,Object> close() {
        String id = getPara("id");
        return eamBusinessDictService.close(id);
    }

    @Transactional
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "insert")
    public Map<String, Object> insert(EamBusinessDict dict){
        Map<String,Object> returnMap = new HashMap();
        String param = getPara("param");      //获取入参
        dict = JSON.parseObject(param, EamBusinessDict.class);
        if(dict == null){
            returnMap.put("flag",false);
            returnMap.put("msg","参数错误");
            return returnMap;
        } else {
            eamBusinessDictService.insert(dict);
            eamBusinessDictService.updateBusinessDictTree();
            returnMap.put("flag",true);
            returnMap.put("msg","操作成功");
        }

        return returnMap;
    }

    /**
     * 获取设备类别下拉树数据
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getBizDictTree")
    public List getBizDictTree() {
        String id = getPara("id");      //获取入参
        Map param = new HashMap();
        param.put("id_key", id);
        param.put("dbName", Global.getConfig("jdbc.type"));
        List<Map> mapList = eamBusinessDictService.getBusinessDictTree(param);

        if (mapList == null) {
            return null;
        }
        List<DictTreeData> DictTreeDataList = new ArrayList();
        List<DictTreeData> resultList = new ArrayList<>();

        for(int i = 0; i < mapList.size(); i++){
            String pid;
            Map map = mapList.get(i);
            if(map.containsKey("pId") && map.get("pId") != null && !"".equals(map.get("pId"))) {
                pid = map.get("pId").toString();
            } else {
                pid = "0";
            }
            DictTreeData deviceTreeData = new DictTreeData(map.get("id").toString(), pid,
                    map.get("name").toString(), map.get("value").toString(), map.get("desc").toString());
            deviceTreeData.setParentId(pid);
            deviceTreeData.setChildren(new ArrayList());
            DictTreeDataList.add(deviceTreeData);
        }

        for (DictTreeData tree : DictTreeDataList) {
            for (DictTreeData t : DictTreeDataList) {
                if (t.getParentId().equals(tree.getId())) {
                    if (tree.getChildren() == null || tree.getChildren().size() == 0) {
                        List<DictTreeData> myChildrens = new ArrayList();
                        myChildrens.add(t);
                        tree.setChildren(myChildrens);
                    } else {
                        tree.getChildren().add(t);
                    }
                }
            }
        }
        for (DictTreeData tree : DictTreeDataList) {
            if (DictTreeData.ROOT_PARENT.equals(tree.getParentId())) {
                resultList.add(tree);
            }
        }
        return resultList;

    }

    /**
     * 将获取到的业务字典列表
     * @param eamBusinessDictList
     * @return
     */
    public List<Map<String, Object>> getMapList(List<EamBusinessDict> eamBusinessDictList){
        List<Map<String, Object>> mapList = Lists.newArrayList();
        if(eamBusinessDictList != null && eamBusinessDictList.size() != 0) {
            for(int i = 0; i < eamBusinessDictList.size(); i++) {
                EamBusinessDict eamBusinessDict = eamBusinessDictList.get(i);
                if(eamBusinessDict != null && eamBusinessDict.getDict_name() != null) {
                    Map<String, Object> map = Maps.newHashMap();
                    map.put("id", eamBusinessDict.getId_key());
                    map.put("pId", eamBusinessDict.getDict_pid());
                    map.put("name", eamBusinessDict.getDict_name());
                    mapList.add(map);
                }
            }
        }
        return mapList;
    }

    /**
     * 获取单个模块的所有枚举值字典
     * @return
     */
    @Transactional(readOnly = false)
    @ResponseBody
    @RequestMapping(value = "getValues")
    public List<Map> getValues(){
        String dictValue = getPara("dict_type_code");   // 入参key值   dict_value
        Map map = new HashMap();
        map.put("dict_value", dictValue);
        List<Map> dictList = eamBusinessDictService.getByValue(map);
        return dictList;
    }

}
