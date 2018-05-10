package com.tiansu.eam.modules.maintain.controller;

import com.alibaba.fastjson.JSON;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.act.service.EamProcessService;
import com.tiansu.eam.modules.inspection.entity.InspectionSubjectForInsert;
import com.tiansu.eam.modules.maintain.entity.MaintainProject;
import com.tiansu.eam.modules.maintain.service.MaintainProjectService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @creator duanfuju
 * @createtime 2017/11/3 16:43
 * @description: 
 * 		保养年计划controller
 */
@Controller
@RequestMapping(value = "${adminPath}/maintain/maintainproject")
public class MaintainProjectController extends BaseController {

    @Autowired
    private MaintainProjectService maintainProjectService;


    /**
     * @creator duanfuju
     * @createtime 2017/11/13 15:17
     * @description:
     *      修改
     * @param maintainProject
     * @param request
     * @return
     */
    @ResponseBody
    @Transactional(readOnly=false)
    @RequiresPermissions("user")
    @RequestMapping(value = "save")
    public String save(MaintainProject maintainProject, HttpServletRequest request){
        String param = getPara("param");      //获取入参
        maintainProject = JSON.parseObject(param, MaintainProject.class);
        maintainProjectService.save(maintainProject);
        return "success";
    }
    /**
     * @creator duanfuju
     * @createtime 2017/11/10 13:45
     * @description:
     * 删除
     * @return
     */
    @ResponseBody
    @Transactional(readOnly=false)
    @RequiresPermissions("user")
    @RequestMapping(value = "delete")
    public String delete() {
        maintainProjectService.delete(getPara("id"));
        return "success";
    }
    /**
     * @creator duanfuju
     * @createtime 2017/11/8 9:02
     * @description: 
     * 从保养设置获取数据添加到保养年内计划中
     * @param year
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "createProjectData")
    @Transactional
    public String createProjectData(String year){
            return maintainProjectService.createProjectData(year);
    }


    /**
     * @creator duanfuju
     * @createtime 2017/11/7 11:21
     * @description:
     *      审批
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "approvalsp")
    public Map approvalsp(HttpServletRequest request) {
        Map map = new HashMap();
        map.put("project_status",getPara("project_status"));
        String ids=getPara("ids");
        if(ids!=null){ map.put("ids",ids);}
        maintainProjectService.approval(map,request);
        map.put("msg","success");
        return  map;
    }

    /**
     * 生成数据
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "approval")
    public Map approval() {
        Map map = new HashMap();
        map.put("project_status",getPara("project_status"));
        String ids=getPara("ids");
        if(ids!=null){ map.put("ids",ids);}
        maintainProjectService.approval(map,null);
        map.put("msg","success");
        return  map;
    }

    /**
     * @creator duanfuju
     * @createtime 2017/11/7 13:58
     * @description:
     * 跳转到生成计划的页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "approvalUI")
    public String approvalUI() {
        return "modules/maintenance/mainAnnualApprovalCreateList";
    }

    /**
     *@creator duanfuju
     * @createtime 2017/10/24 9:34
     * @description:
     *  跳转修改页面路径
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "editUI")
    public String editUI() {
        return "modules/maintenance/maintAnnualFormEdit";
    }

    /**
     * @creator duanfuju
     * @createtime 2017/10/24 9:34
     * @description:
     * 跳转详情页面路径
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "detailUI")
    public String detailUI() {
        return "modules/maintenance/maintAnnualFormDetail";
    }





    /**
     * @creator duanfuju
     * @createtime 2017/10/24 9:34
     * @description:
     * 修改前获取单个对象
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "editObj")
    public Map editObj() {
        String id=getPara("id");
        return maintainProjectService.findById(id);
    }

    /**
     * @creator duanfuju
     * @createtime 2017/11/15 8:59
     * @description:
     * 根据流程实例id获取数据
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "findByPstid")
    public Map findByPstid() {
        String pstid=getPara("pstid");
        return maintainProjectService.findByPstid(pstid);
    }


    /**
     * @creator duanfuju
     * @createtime 2017/10/24 9:34
     * @description:
     * 列表查询
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = {"dataTablePageMap"})
    public Map<String,Object> dataTablePageMap() {
        Map param = getFormMap();
        Map<String,Object> map = maintainProjectService.dataTablePageMap(param);
        return map;
    }


}
