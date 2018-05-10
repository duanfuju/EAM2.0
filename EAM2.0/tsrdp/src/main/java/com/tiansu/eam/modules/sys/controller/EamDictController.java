package com.tiansu.eam.modules.sys.controller;

import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.sys.entity.Dict;
import com.tiansu.eam.modules.sys.service.EamDictService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by wujh on 2017/8/24.
 */
@Controller
@RequestMapping(value = "${adminPath}/eam/bisinessDict")
public class EamDictController extends BaseController {

    @Autowired
    private EamDictService eamDictService;

    /**
     * 获取当前菜单页面的字段
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
     * 获取业务字段列表
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = {"listData"})
    public Map<String,Object> listData() {
        Map param = getFormMap();
        Map<String,Object> map = eamDictService.dataTablePageMap(param);
        return map;
    }

    /**
     * 跳转到业务字典录入页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "addUI")
    public String addUI() {
        return "modules/eamsys/sysConfig/bizDictFormAdd";
    }

    /**
     * 跳转到设备类别编辑页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "editUI")
    public String editUI() {
        return "modules/eamsys/sysConfig/bizDictFormEdit";
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
        return eamDictService.getEdit(id);
    }

    /**
     * 修改业务字典信息
     * @param dict
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "update")
    public String update(Dict dict) {
        eamDictService.update(dict);
        return "success";
    }

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "insert")
    public String insert(Dict dict){
        eamDictService.insert(dict);
        return "success";
    }

    /**
     * 根据id删除业务字典
     * @return
     */
    @ResponseBody
    @Transactional(readOnly=false)
    @RequiresPermissions("user")
    @RequestMapping(value = "delete")
    public String delete() {
        String[] ids = getPara("id").split(",");
        return eamDictService.delete(ids);
    }

    /**
     * 获取单个模块的所有枚举值字典
     * @return
     */
    @Transactional(readOnly = false)
    @ResponseBody
    @RequestMapping(value = "getValues")
    public List<Dict> getValues(){
        String typeCode = getPara("dict_type_code");
        List<Dict> dictList = eamDictService.getByCode(typeCode);
        return dictList;
    }
}
