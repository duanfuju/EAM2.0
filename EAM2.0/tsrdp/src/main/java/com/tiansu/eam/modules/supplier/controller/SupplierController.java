package com.tiansu.eam.modules.supplier.controller;

import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.supplier.entity.Supplier;
import com.tiansu.eam.modules.supplier.service.SupplierService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangww on 2017/8/15.
 * 供应商类型
 */
@Controller
@RequestMapping(value = "${adminPath}/supplier/supplier")
public class SupplierController extends BaseController{
    @Autowired
    private SupplierService supplierService;

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = {"listData"})
    //查询集合
    public Map<String,Object> listData() {

        Map param = getFormMap();
        Map<String,Object> map = supplierService.dataTablePageMap(param);

        return map;
    }

    //新增跳转
    @RequiresPermissions("user")
    @RequestMapping(value = "addUI")
    public String addUI() {

        return "modules/supplier/supplierFormAdd";

    }
    //编辑跳转
    @RequiresPermissions("user")
    @RequestMapping(value = "editUI")
    public String editUI() {

        return "modules/supplier/supplierFormEdit";

    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "insert")
    public String insert(Supplier supplier) {
        if(supplier.getSupplier_code() !=null || !"".equals(supplier.getSupplier_code())){
            int cou=supplierService.getBycode(supplier.getSupplier_code());//判断编码是否重复
            if(cou>0){
                return "repeat";
            }
        }
        supplierService.insert(supplier);
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
    public Supplier editObj() {
        String id = getPara("id");

        return supplierService.getEdit(id);
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "update")
    public String update(Supplier supplier) {
        supplierService.update(supplier);
        return "success";
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "delete")
    public String delete(){
        //批量删除，单个删除
        String ids = getPara("id");
        Map map=supplierService.deleBefore(ids);
        String status=supplierService.getEdit(ids).getSupplier_status();
        int num=(int)map.get("num");
        if(num>0||"1".equals(status)){
            return "nodele";
        }
        supplierService.deleteByids(ids);
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

        return "modules/supplier/supplierFormDetail";
    }
    /**
     * @creator zhangww
     * @createtime 2017/8/23 9:15
     * @description:复制功能
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "copy")
    public String copy(){
        String id = getPara("id");
        Supplier supplier= supplierService.getEdit(id);
        supplier.preInsert();
        supplierService.insert(supplier);
        return "success";
    }

    /**
     * 获取供应商列表
     * @return
     */
    @Transactional(readOnly = false)
    @ResponseBody
    @RequestMapping(value = "getSupplierList")
    public List<Supplier> getSupplierList(){
        List<Supplier> supplierList = supplierService.findList(new Supplier());
        return supplierList;
    }
    /*
    * @creator zhangww
    * @createtime 2017/9/11 10:52
    * @description:删除前的引用判断,已不用
    * */
    @ResponseBody
    @RequestMapping(value = "delebefore")
    public String deleteBefore(){
        String id=getPara("id");
        Map map=supplierService.deleBefore(id);
        int num=(int)map.get("num");
        if(num>0){
            return "false";
        }
        return "success";
    }

    /**@creator zhangww
     * @createtime 2017/8/22 9:14
     * @description:供应设备清单页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "supplierDeviceUI")
    public String supplierDeviceUI() {

        return "modules/supplier/supplierDeviceList";
    }
    /**
     * 获取设备清单
     * @return
     */
    @Transactional(readOnly = false)
    @ResponseBody
    @RequestMapping(value = "getDeviceList")
    public Map getDeviceList(){
        Map param = getFormMap();
        return supplierService.getDeviceList(param);
    }
}
