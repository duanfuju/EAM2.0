package com.tiansu.eam.modules.supplier.controller;

import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.supplier.entity.SupplierType;
import com.tiansu.eam.modules.supplier.service.SupplierTypeService;
import com.tiansu.eam.modules.sys.entity.CodeEnum;
import com.tiansu.eam.modules.sys.utils.SequenceUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangww on 2017/8/15.
 * 供应商类型
 */
@Controller
@RequestMapping(value = "${adminPath}/supplier/supplierType")
public class SupplierTypeController extends BaseController{
    @Autowired
    private SupplierTypeService supplierTypeService;

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = {"listData"})
    //查询集合
    public Map<String,Object> listData() {

        Map param = getFormMap();
        Map<String,Object> map = supplierTypeService.dataTablePageMap(param);

        return map;
    }

    //新增跳转
    @RequiresPermissions("user")
    @RequestMapping(value = "addUI")
    public String addUI() {

        return "modules/supplier/supplierTypeFormAdd";

    }
    //编辑跳转
    @RequiresPermissions("user")
    @RequestMapping(value = "editUI")
    public String editUI() {

        return "modules/supplier/supplierTypeFormEdit";

    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "insert")
    public String insert(SupplierType supplierType) {
        if(supplierType.getType_code() !=null || !"".equals(supplierType.getType_code())){
            int cou=supplierTypeService.getBycode(supplierType.getType_code());
            if(cou>0){
                return "repeat";
            }
        }
        supplierTypeService.insert(supplierType);
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
    public SupplierType editObj() {
        String id = getPara("id");

        return supplierTypeService.getEdit(id);
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "update")
    public String update(SupplierType supplierType) {

        supplierTypeService.update(supplierType);
        return "success";
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "delete")
    public String delete(){
        //批量删除，单个删除
        String ids = getPara("id");
        Map map=supplierTypeService.deleBefore(ids);
        String status=supplierTypeService.getEdit(ids).getType_status();
        int num=(int)map.get("num");
        if(num>0||"1".equals(status)){
            return "nodele";
        }
        supplierTypeService.deleteByids(ids);
        return "success";
    }
    /**
     * 打开详情页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "detailUI")
    public String detailUI() {

        return "modules/supplier/supplierTypeFormDetail";
    }
    @Transactional
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "copy")
    public String copy(){
        String id = getPara("id");
        SupplierType supplierType=supplierTypeService.getEdit(id);
        supplierType.setType_code(SequenceUtils.getBySeqType(CodeEnum.SUPPLIER_TYPE));
        supplierType.preInsert();
        supplierTypeService.insert(supplierType);
        return "success";
    }

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = {"getsuppliertype"})
    //查询集合
    public List<Map> getsuppliertype() {

        return supplierTypeService.getsuppliertype();
    }
}
