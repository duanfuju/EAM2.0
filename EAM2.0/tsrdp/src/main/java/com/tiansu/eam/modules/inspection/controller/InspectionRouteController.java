package com.tiansu.eam.modules.inspection.controller;

import com.alibaba.fastjson.JSON;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.act.service.EamProcessService;
import com.tiansu.eam.modules.inspection.entity.*;
import com.tiansu.eam.modules.inspection.service.InspectionRouteService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangww on 2017/10/15.
 * 巡检路线
 */
@Controller
@RequestMapping(value = "${adminPath}/inspection/inspectionRoute")
public class InspectionRouteController extends BaseController{
    @Autowired
    private InspectionRouteService inspectionRouteService;
    @Autowired
    private EamProcessService eamProcessService;//流程
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = {"listData"})
    //查询集合
    public Map<String,Object> listData() {

        Map param = getFormMap();
        Map<String,Object> map = inspectionRouteService.dataTablePageMap(param);

        return map;
    }

    //新增跳转
    @RequiresPermissions("user")
    @RequestMapping(value = "addUI")
    public String addUI() {

        return "modules/inspection/inspectionRouteFormAdd";

    }
    //编辑跳转
    @RequiresPermissions("user")
    @RequestMapping(value = "editUI")
    public String editUI() {

        return "modules/inspection/inspectionRouteFormEdit";

    }
   /*
   * @creator zhangww
   * @createtime 2017/10/17 9:10
   * @description:选择巡检项区域*/
    @RequiresPermissions("user")
    @RequestMapping(value = "insAreaSelectUI")
    public String insAreaSelectUI(){
        return "modules/inspection/insAreaSelect";
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "insert")
    @Transactional
    public String insert(InspectionRoute inspectionRoute,HttpServletRequest request) {
        String param=getPara("param");
        inspectionRoute= JSON.parseObject(param,InspectionRoute.class);
        if(inspectionRoute.getRoute_code() !=null || !"".equals(inspectionRoute.getRoute_code())){
            int cou=inspectionRouteService.getBycode(inspectionRoute.getRoute_code());
            if(cou>0){
                return "repeat";
            }
        }
        inspectionRoute.preInsert();
        String uuid=inspectionRoute.getId();
        List<InsRouteProc> insRouteProcList= inspectionRoute.getInsRouteProcList();
        List<InsRouteSafe> insRouteSafeList=inspectionRoute.getInsRouteSafeList();
        List<InsRouteTools> insRouteToolsList=inspectionRoute.getInsRouteToolsList();
        List<InsRouteSpareparts> insRouteSparepartsList=inspectionRoute.getInsRouteSparepartsList();
        List<InsRoutePerson> insRoutePersonList=inspectionRoute.getInsRoutePersonList();
        List<InsRouteOthers> insRouteOthersList=inspectionRoute.getInsRouteOthersList();
        for(InsRouteProc insRouteProc:insRouteProcList){
            insRouteProc.setId(IdGen.uuid());
            insRouteProc.setInspectionroute_id(uuid);
        }
        for(InsRouteSafe insRouteSafe:insRouteSafeList){
            insRouteSafe.setId(IdGen.uuid());
            insRouteSafe.setInspectionroute_id(uuid);
        }
        for(InsRouteTools insRouteTools:insRouteToolsList){
            insRouteTools.setId(IdGen.uuid());
            insRouteTools.setInspectionroute_id(uuid);
        }
        for(InsRouteSpareparts insRouteSpareparts:insRouteSparepartsList){
            insRouteSpareparts.setId(IdGen.uuid());
            insRouteSpareparts.setInspectionroute_id(uuid);
        }
        for(InsRoutePerson insRoutePerson:insRoutePersonList){
            insRoutePerson.setId(IdGen.uuid());
            insRoutePerson.setInspectionroute_id(uuid);
        }
        for(InsRouteOthers insRouteOthers:insRouteOthersList){
            insRouteOthers.setId(IdGen.uuid());
            insRouteOthers.setInspectionroute_id(uuid);
        }
        String pstid = eamProcessService.startProcessByPdid("inspectionRoute_approve","eam_inspection_route",uuid,request);
        if("timeout".equals(pstid)){
            return "timeout";
        }
        inspectionRoute.setPstid(pstid);
        inspectionRouteService.insert(inspectionRoute);//插入主表
        inspectionRouteService.insertDetail(insRouteProcList,insRouteSafeList,insRouteToolsList,insRouteSparepartsList,insRoutePersonList,insRouteOthersList);
       //插入路线区域关联表
        List<InspectionRouteArea> inspectionRouteAreaList=new ArrayList<InspectionRouteArea>();
        if(inspectionRoute.getRoute_area()!=null &&!"".equals(inspectionRoute.getRoute_area())){
            for(String area:inspectionRoute.getRoute_area().split(",")){
                InspectionRouteArea rarea=new InspectionRouteArea();
                rarea.setId(IdGen.uuid());
                rarea.setArea_id(area);
                rarea.setRoute_id(uuid);
                inspectionRouteAreaList.add(rarea);
            }
            inspectionRouteService.insertArea(inspectionRouteAreaList);
        }
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
    public Map editObj() {
        String id = getPara("id");

        return inspectionRouteService.getEdit(id);
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "update")
    @Transactional
    public String update(InspectionRoute inspectionRoute) {

        String param=getPara("param");
        inspectionRoute= JSON.parseObject(param,InspectionRoute.class);
        inspectionRoute.preUpdate();
        String uuid=inspectionRoute.getId_key();
        List<InsRouteProc> insRouteProcList= inspectionRoute.getInsRouteProcList();
        List<InsRouteSafe> insRouteSafeList=inspectionRoute.getInsRouteSafeList();
        List<InsRouteTools> insRouteToolsList=inspectionRoute.getInsRouteToolsList();
        List<InsRouteSpareparts> insRouteSparepartsList=inspectionRoute.getInsRouteSparepartsList();
        List<InsRoutePerson> insRoutePersonList=inspectionRoute.getInsRoutePersonList();
        List<InsRouteOthers> insRouteOthersList=inspectionRoute.getInsRouteOthersList();
        for(InsRouteProc insRouteProc:insRouteProcList){
            insRouteProc.setId(IdGen.uuid());
            insRouteProc.setInspectionroute_id(uuid);
        }
        for(InsRouteSafe insRouteSafe:insRouteSafeList){
            insRouteSafe.setId(IdGen.uuid());
            insRouteSafe.setInspectionroute_id(uuid);
        }
        for(InsRouteTools insRouteTools:insRouteToolsList){
            insRouteTools.setId(IdGen.uuid());
            insRouteTools.setInspectionroute_id(uuid);
        }
        for(InsRouteSpareparts insRouteSpareparts:insRouteSparepartsList){
            insRouteSpareparts.setId(IdGen.uuid());
            insRouteSpareparts.setInspectionroute_id(uuid);
        }
        for(InsRoutePerson insRoutePerson:insRoutePersonList){
            insRoutePerson.setId(IdGen.uuid());
            insRoutePerson.setInspectionroute_id(uuid);
        }
        for(InsRouteOthers insRouteOthers:insRouteOthersList){
            insRouteOthers.setId(IdGen.uuid());
            insRouteOthers.setInspectionroute_id(uuid);
        }
        inspectionRouteService.update(inspectionRoute);//插入主表
        inspectionRouteService.deleteDetail(uuid);
        inspectionRouteService.insertDetail(insRouteProcList,insRouteSafeList,insRouteToolsList,insRouteSparepartsList,insRoutePersonList,insRouteOthersList);
        //修改路线区域关联表
        List<InspectionRouteArea> inspectionRouteAreaList=new ArrayList<InspectionRouteArea>();
        if(inspectionRoute.getRoute_area()!=null &&!"".equals(inspectionRoute.getRoute_area())){
            for(String area:inspectionRoute.getRoute_area().split(",")){
                InspectionRouteArea rarea=new InspectionRouteArea();
                rarea.setId(IdGen.uuid());
                rarea.setArea_id(area);
                rarea.setRoute_id(uuid);
                inspectionRouteAreaList.add(rarea);
            }
            inspectionRouteService.insertArea(inspectionRouteAreaList);
        }
        return "success";
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "delete")
    public String delete(){
        //批量删除，单个删除
        String ids = getPara("id");
        inspectionRouteService.deleteByids(ids);
        return "success";
    }
    /**
     * 打开详情页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "detailUI")
    public String detailUI() {

        return "modules/inspection/inspectionRouteFormDetail";
    }

    @ResponseBody
    @RequestMapping(value = "delebefore")
    public String deleteBefore(){
        String id=getPara("id");
        Map map=inspectionRouteService.deleBefore(id);
        int num=(int)map.get("num");
        if(num>0){
            return "false";
        }
        return "success";
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "quInsProce")
    public List quInsProce(){
        String id = getPara("inspectionroute_id");
        return inspectionRouteService.quInsProce(id);
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "quInsSafe")
    public List quInsSafe(){
        String id = getPara("inspectionroute_id");
        return inspectionRouteService.quInsSafe(id);
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "quInsTool")
    public List quInsTool(){
        String id = getPara("inspectionroute_id");
        return inspectionRouteService.quInsTool(id);
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "quInsSpare")
    public List quInsSpare(){
        String id = getPara("inspectionroute_id");
        return inspectionRouteService.quInsSpare(id);
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "quInsPerson")
    public List quInsPerson(){
        String id = getPara("inspectionroute_id");
        return inspectionRouteService.quInsPerson(id);
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "quInsOther")
    public List quInsOther(){
        String id = getPara("inspectionroute_id");
        return inspectionRouteService.quInsOther(id);
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getLibByPIid")
    public Map getLibByPIid(){
        String pIid=getPara("pIid");
        return inspectionRouteService.getLibByPIid(pIid);
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getLibByPIidclose")
    public Map getLibByPIidclose(){
        String pIidclose=getPara("pIid");
        return inspectionRouteService.getLibByPIidclose(pIidclose);
    }
    /**
     * 巡检路线作废
     * @param
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "updatePIidcloseByid")
    public String updatePIidcloseByid(){
        HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String id = getPara("id");
        InspectionRoute inspectionRoute=inspectionRouteService.get(id);
        String uuid=inspectionRoute.getId_key();
        String pstidclose = eamProcessService.startProcessByPdid("inspectionRoute_closed","eam_inspection_route",uuid,request);
        Map map=new HashMap();
        map.put("pstidclose",pstidclose);
        map.put("id",id);
        inspectionRouteService.updatePIidcloseByid(map);
        return "success";
    }
}
