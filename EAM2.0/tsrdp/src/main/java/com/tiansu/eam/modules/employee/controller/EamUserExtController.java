package com.tiansu.eam.modules.employee.controller;/**
 * @description
 * @author duanfuju
 * @create 2017-09-14 16:33
 **/

import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.employee.entity.EamUserExt;
import com.tiansu.eam.modules.employee.service.EamUserExtService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


/**
 * @author duanfuju
 * @create 2017-09-14 16:33
 * @desc 人员信息扩展controller层
 **/
@Controller
@RequestMapping(value = "${adminPath}/employee/eamUserExt")
public class EamUserExtController  extends BaseController {

    @Autowired
    private EamUserExtService eamUserExtService;

    /**
     *
     * @param loginname
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "findByLoginName")
    public List<Map> findByLoginName(String loginname){
        return eamUserExtService.findByLoginName(loginname);
    }
    /**
     * @creator duanfuju
     * @createtime 2017/9/14 16:38
     * @description: 
     * 跳转新增页面路径
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "addUI")
    public String addUI(Map<String, Object> map) {
        return "modules/employee/eamUserExtFormAdd";
    }

    /**
     * @creator duanfuju
     * @createtime 2017/9/14 16:38
     * @description: 
     * 跳转修改页面路径
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "editUI")
    public String editUI() {
        return "modules/employee/eamUserExtFormEdit";
    }

    /**
     * @creator duanfuju
     * @createtime 2017/9/14 16:39
     * @description: 
     * 跳转详情页面路径
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "detailUI")
    public String detailUI() {
        return "modules/employee/eamUserExtFormDetail";
    }




    /**
     * @creator duanfuju
     * @createtime 2017/9/14 16:39
     * @description: 
     *  删除
     */
    @ResponseBody
    @Transactional(readOnly=false)
    @RequiresPermissions("user")
    @RequestMapping(value = "delete")
    public String delete() {
        eamUserExtService.delete(getPara("id"));
        return "success";
    }

    /**
     * @creator duanfuju
     * @createtime 2017/9/14 16:39
     * @description: 
     * 修改前获取单个对象
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "editObj")
    public Map editObj() {
        String id=getPara("id");
        return eamUserExtService.findById(id);
    }
    /**
     * @creator duanfuju
     * @createtime 2017/9/14 16:39
     * @description: 
     * : 修改
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "update")
    public String update(EamUserExt eamUserExt) {
        eamUserExtService.update(eamUserExt);
        return "success";
    }

    /**
     * @creator duanfuju
     * @createtime 2017/9/14 16:39
     * @description: 
     * 新增
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "insert")
    public String insert(EamUserExt eamUserExt) {
        eamUserExtService.insert(eamUserExt);
        return "success";
    }


    /**
     * @creator duanfuju
     * @createtime 2017/9/14 19:43
     * @description: 
     * 列表查询
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = {"dataTablePageMap"})
    public Map<String,Object> dataTablePageMap() {
        Map param = getFormMap();
        Map<String,Object> map = eamUserExtService.dataTablePageMap(param);
        return map;
    }

    /**
     * @creator duanfuju
     * @createtime 2017/9/19 10:57
     * @description:
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = {"getAllUser"})
    public  List<Map> getAllUser() {
        List<Map> map= eamUserExtService.getAllUser();
        return map;
    }



}
