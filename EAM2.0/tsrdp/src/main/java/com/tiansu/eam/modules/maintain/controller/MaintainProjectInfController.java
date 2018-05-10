package com.tiansu.eam.modules.maintain.controller;/**
 * Created by suven on 2017/11/2.
 */

import com.alibaba.fastjson.JSON;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.act.service.EamProcessService;
import com.tiansu.eam.modules.inspection.entity.*;
import com.tiansu.eam.modules.inspection.service.InspectionRouteService;
import com.tiansu.eam.modules.maintain.entity.MaintainProjectInf;
import com.tiansu.eam.modules.maintain.entity.MaintainProjectInfContent;
import com.tiansu.eam.modules.maintain.entity.MaintainProjectInfDevice;
import com.tiansu.eam.modules.maintain.service.MaintainProjectInfService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteDateUseDateFormat;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue;

/**
 * 保养设置控制器
 *
 * @author suven suven
 * @create 2017/11/2
 */
@Controller
@RequestMapping(value = "${adminPath}/maintenance/maintSet")
public class MaintainProjectInfController extends BaseController {
    @Autowired
    private MaintainProjectInfService maintainProjectInfService;
    @Autowired
    private EamProcessService eamProcessService;//流程
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = {"listData"})
    //查询集合
    public String listData() {

        Map param = getFormMap();
        Map<String,Object> map = maintainProjectInfService.dataTablePageMap(param);

        return JSON.toJSONString(map,WriteMapNullValue, WriteDateUseDateFormat);
    }

    //编辑跳转
    @RequiresPermissions("user")
    @RequestMapping(value = "editUI")
    public String editUI() {

        return "modules/maintenance/maintSetFormEdit";

    }
    /**
     * 获取编辑页面字段数据
     * @param
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "editObj")
    public String  editObj() {
        String id = getPara("id");

        return JSON.toJSONString(maintainProjectInfService.getEdit(id),WriteMapNullValue, WriteDateUseDateFormat);
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "delete")
    public String delete(){
        //批量删除，单个删除
        String ids = getPara("id");
        if(maintainProjectInfService.queryForCanDelete(ids)){
            maintainProjectInfService.deleteByids(ids);
             return "success";
        }else{
            return "nodele";
        }
    }

    /**
     * 打开详情页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "detailUI")
    public String detailUI() {

        return "modules/maintenance/maintSetFormDetail";
    }

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "quMaintContent")
    public List quInsSafe(){
        String id = getPara("MaintSet_id");
        return maintainProjectInfService.quMaintContent(id);
    }
    //新增跳转
    @RequiresPermissions("user")
    @RequestMapping(value = "addUI")
    public String addUI() {

        return "modules/maintenance/maintSetFormAdd";

    }



    /**
     * 新增/修改页面的设备选择页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "DeviceSelectUI")
    public String DeviceSelectUI(){
        return "modules/maintenance/maintSetDeviceSelect";
    }
    /**
    *@Create
    *@Description :
    *@Param :  * @param null
    *@author : suven
    *@Date : 13:20 2017/11/4
    */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "insert")
    @Transactional
    public String insert(MaintainProjectInf maintainProjectInf, HttpServletRequest request) {
        String param=getPara("param");
        maintainProjectInf= JSON.parseObject(param,MaintainProjectInf.class);
        if(maintainProjectInf.getProject_code() !=null || !"".equals(maintainProjectInf.getProject_code())){
            int cou=maintainProjectInfService.getBycode(maintainProjectInf.getProject_code());
            if(cou>0){
                return "repeat";
            }
        }
        maintainProjectInf.preInsert();
        String uuid=maintainProjectInf.getId();
        List<MaintainProjectInfContent> maintainProjectInfContentList=maintainProjectInf.getMaintainProjectInfContentList();
        for(MaintainProjectInfContent mpic:maintainProjectInfContentList){
           mpic.setId(IdGen.uuid());
           mpic.setProject_id(uuid);

        }
        String[] dev_ids=maintainProjectInf.getDev_id().split(",");
        List<MaintainProjectInfDevice> maintainProjectInfDeviceList=new ArrayList<MaintainProjectInfDevice>();
        for(String dev_id:dev_ids){
            maintainProjectInfDeviceList.add(new MaintainProjectInfDevice(IdGen.uuid(),uuid,dev_id));
        }
        maintainProjectInfService.insert(maintainProjectInf);
        maintainProjectInfService.insertDetail(maintainProjectInfContentList,maintainProjectInfDeviceList);

        return "success";
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "update")
    @Transactional
    public String update(MaintainProjectInf maintainProjectInf) {

        String param=getPara("param");
        maintainProjectInf= JSON.parseObject(param,MaintainProjectInf.class);
        List<MaintainProjectInfDevice> maintainProjectInfDeviceList = new ArrayList<MaintainProjectInfDevice>();

        maintainProjectInf.preUpdate();
        String uuid=maintainProjectInf.getId_key();
        if(!(maintainProjectInf.getDev_id()==null||("").equals(maintainProjectInf.getDev_id()))) {
            String[] dev_ids = maintainProjectInf.getDev_id().split(",");

            for (String dev_id : dev_ids) {
                maintainProjectInfDeviceList.add(new MaintainProjectInfDevice(IdGen.uuid(), uuid, dev_id));
            }
        }
        maintainProjectInfService.deleteDetail(uuid);
        List<MaintainProjectInfContent> maintainProjectInfContentList=maintainProjectInf.getMaintainProjectInfContentList();
        for(MaintainProjectInfContent mpic:maintainProjectInfContentList){
            mpic.setId(IdGen.uuid());
            mpic.setProject_id(uuid);

        }

        maintainProjectInfService.insertDetail(maintainProjectInfContentList,maintainProjectInfDeviceList);
        maintainProjectInfService.update(maintainProjectInf);

        return "success";
    }


}
