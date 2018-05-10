package com.tiansu.eam.modules.device.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.device.entity.DevCategory;
import com.tiansu.eam.modules.device.entity.DevLocation;
import com.tiansu.eam.modules.device.service.EamDevLocService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @creator duanfuju
 * @createtime 2017/9/4 17:06
 * @description:
 *  空间信息controller
 */

@Controller
@RequestMapping(value = "${adminPath}/device/devLoction")
public class EamDevLocController extends BaseController {
    @Autowired
    EamDevLocService eamDevLocService;



    /**
     * @creator duanfuju
     * @createtime 2017/9/7 17:31
     * @description:
     * 获取设备类别下拉树数据
     */
    @ResponseBody
    @RequestMapping(value = "getDevLocationTree")
    public List<Map> getDevLocationTree(){
        List<Map> devLocation = eamDevLocService.getDevLocationTree();
        return getMapList(devLocation);

    }

    /**
     * 将获取到的设备类别列表
     * @param devLocationList
     * @return
     */
    public List<Map> getMapList(List<Map> devLocationList){
        List<Map> mapList = Lists.newArrayList();
        if(devLocationList != null && devLocationList.size() != 0) {
            for(int i = 0; i < devLocationList.size(); i++) {
                Map devMap = devLocationList.get(i);
                    Map<String, Object> map = Maps.newHashMap();
                    map.put("id", devMap.get("loc_id"));
                    map.put("pId", devMap.get("loc_pid"));
                    map.put("name", devMap.get("loc_name"));
                    mapList.add(map);
                }
            }
        return mapList;
    }

    /**
     * @creator duanfuju
     * @createtime 2017/9/6 10:32
     * @description: 
     * 删除
     */
    @ResponseBody
    @Transactional(readOnly=false)
    @RequiresPermissions("user")
    @RequestMapping(value = "delete")
    public String delete() {
        eamDevLocService.delete(getPara("id"));
        return "success";
    }

    /**
     *@creator duanfuju
     * @createtime 2017/9/6 10:18
     * @description: 
     *  修改前获取单个对象
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "editObj")
    public Map editObj() {
        String id=getPara("id");
        return eamDevLocService.findById(id);
    }
    /**
     * @creator duanfuju
     * @createtime 2017/9/6 10:18
     * @description: 
     * 修改
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "update")
    public String update(DevLocation devLocation) {
        eamDevLocService.update(devLocation);
        return "success";
    }

    /**
     * @creator duanfuju
     * @createtime 2017/9/6 10:18
     * @description:  
     * 新增
     */
    @Transactional
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "insert")
    public String insert(DevLocation devLocation) {
        devLocation.setLoc_id(IdGen.randowNum());
        eamDevLocService.insert(devLocation);
        eamDevLocService.updateDevLocationTree();
        return "success";
    }


    /**
     *@creator duanfuju
     * @createtime 2017/9/6 10:18
     * @description: 
     * 列表查询
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = {"dataTablePageMap"})
    public Map<String,Object> dataTablePageMap() {
        Map param = getFormMap();
        if(param.containsKey("id")){
            String[] ids=eamDevLocService.getDataByPId(param);
            if(ids.length>0){
                param.put("ids",ids );
                param.remove("id");
            }
        }
        Map<String,Object> map = eamDevLocService.dataTablePageMap(param);
        return map;
    }

    /**
     *@creator duanfuju
     * @createtime 2017/8/28 8:59
     * @description: 跳转新增页面路径
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "addUI")
    public String addUI(Map<String, Object> map) {
        return "modules/device/eamDevLoctionFormAdd";
    }

    /**
     * @creator duanfuju
     * @createtime 2017/9/6 9:31
     * @description:
     * 跳转修改页面路径
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "editUI")
    public String editUI() {
        return "modules/device/eamDevLoctionFormEdit";
    }


    /**
     * @creator duanfuju
     * @createtime 2017/9/6 9:31
     * @description:
     * 跳转详情页面路径
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "detailUI")
    public String detailUI() {
        return "modules/device/eamDevLoctionFormDetail";
    }

    /**
     * @creator duanfuju
     * @createtime 2017/11/21 9:46
     * @description:
     * 	二维码批量打印页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "qrCodePrintLocUI")
    public String qrCodePrintLocUI() {
        return "modules/device/qrCodePrintLocUI";
    }
}