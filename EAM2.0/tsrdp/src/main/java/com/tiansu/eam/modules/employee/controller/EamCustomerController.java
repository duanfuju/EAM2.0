package com.tiansu.eam.modules.employee.controller;

import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.employee.entity.EamCustomer;
import com.tiansu.eam.modules.employee.service.EamCustomerService;
import com.tiansu.eam.modules.sys.utils.SequenceUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

import static com.tiansu.eam.modules.sys.entity.CodeEnum.CUSTOMER;

/**
 * @creator duanfuju
 * @createtime 2017/8/28 8:59
 * @description: 客户信息Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/employee/eamCustomer")
public class EamCustomerController extends BaseController {

    @Autowired
    private EamCustomerService eamCustomerService;


    /**
     *@creator duanfuju
     * @createtime 2017/8/28 8:59
     * @description: 跳转新增页面路径
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "addUI")
    public String addUI(Map<String, Object> map) {
        String customer_code=SequenceUtils.getBySeqType(CUSTOMER);
        map.put("customer_code_hidden",customer_code);
        return "modules/employee/eamCustomerFormAdd";
    }

    /**
     * @creator duanfuju
     * @createtime 2017/8/28 9:00
     * @description: 跳转修改页面路径
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "editUI")
    public String editUI() {
        return "modules/employee/eamCustomerFormEdit";
    }

    /**
     * @creator duanfuju
     * @createtime 2017/8/28 9:00
     * @description:  跳转详情页面路径
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "detailUI")
    public String detailUI() {
        return "modules/employee/eamCustomerFormDetail";
    }




    /**
     * @creator duanfuju
     * @createtime 2017/8/28 9:00
     * @description: 删除
     */
    @ResponseBody
    @Transactional(readOnly=false)
    @RequiresPermissions("user")
    @RequestMapping(value = "delete")
    public String delete() {
        eamCustomerService.delete(getPara("id"));
        return "success";
    }

    /**
     * @creator duanfuju
     * @createtime 2017/8/28 9:01
     * @description: 修改前获取单个对象
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "editObj")
    public Map editObj() {
        String id=getPara("id");
        return eamCustomerService.findById(id);
    }
    /**
     * @creator duanfuju
     * @createtime 2017/8/28 9:01
     * @description: 修改
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "update")
    public String update(EamCustomer eamCustomer) {
        eamCustomerService.update(eamCustomer);
        return "success";
    }

    /**
     * @creator duanfuju
     * @createtime 2017/8/28 9:01
     * @description: 新增
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "insert")
    public String insert(EamCustomer eamCustomer) {
        eamCustomerService.insert(eamCustomer);
        return "success";
    }


    /**
     * @creator duanfuju
     * @createtime 2017/8/28 9:01
     * @description: 列表查询
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = {"dataTablePageMap"})
    public Map<String,Object> dataTablePageMap() {
        Map param = getFormMap();
        Map<String,Object> map = eamCustomerService.dataTablePageMap(param);
        return map;
    }


}
