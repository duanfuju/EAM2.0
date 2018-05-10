package com.tiansu.eam.modules.sys.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.sys.entity.FieldControl;
import com.tiansu.eam.modules.sys.entity.MenuFields;
import com.tiansu.eam.modules.sys.model.fieldcontrol.PageFieldConfigModel;
import com.tiansu.eam.modules.sys.service.EamFieldControllService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjl on 2017/7/27.
 */
@Controller
@RequestMapping(value = "${adminPath}/eam/fieldControl")
public class EamFieldConController extends BaseController {
    @Autowired
    private EamFieldControllService controllService;

    /**
     * 根据角色id和菜单id获取其字段权限
     * @param
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"getMenuFieldJson"})
    public Object getMenuFieldJson(Model model){
//        UserUtils.getMenuFieldConfigList("1102");
        String rolecode = getPara("rolecode");
        String menuid = getPara("menuno");
        if(StringUtils.isEmpty(rolecode) || StringUtils.isEmpty(menuid) ){
            return "";
        }
        //查询出所有的菜单的所有字段，以及菜单的字段配置信息
        List<MenuFields> menuFieldsList = controllService.getMenuFields(menuid);
        FieldControl control = controllService.getFieldControl(rolecode,menuid);
        List<PageFieldConfigModel> fieldConfigList = new ArrayList<>();
        if(control != null){
            fieldConfigList = JSONArray.parseArray((String)control.getContent(),PageFieldConfigModel.class);
        }

        int count = menuFieldsList!=null?menuFieldsList.size():0;

        //循环所有菜单字段，如果有新添加的或是删除的，则同步修改配置中数据，存放在resultModelList中，注：此处无法修改已经存在的字段的信息
        List<PageFieldConfigModel> resultModelList = new ArrayList<>();
        if(menuFieldsList != null){
            for(MenuFields field : menuFieldsList){
                //从配置中获取，如果获取不到，则创建一个初始化的新字段配置
                PageFieldConfigModel configModel = getConfigFromList(field,fieldConfigList);
                if(configModel == null ){
                    configModel = getPageFieldConfigModel(field);
                }
                resultModelList.add(configModel);
            }

        }

        String jsonCnt = JSONArray.toJSONString(resultModelList);

        model.addAttribute("control", control);
        return "{\"Rows\":"+jsonCnt+",\"Total\":"+count+"}";

    }

    private PageFieldConfigModel getConfigFromList(MenuFields field, List<PageFieldConfigModel> fieldConfigList) {
        for(PageFieldConfigModel m : fieldConfigList){
            if(m.getFieldName().equals(field.getField_name())){
                //一些字段基础项，eam_menu_field表的字段（页面上无法编辑）项始终读取字段表
                m.setDisplayName(field.getDisplay_name());
                m.setMarginType(field.getField_type());
                m.setValidateType(field.getValidate_type());
                return m;
            }
        }
        return null;
    }

    /**
     * //设置菜单字段默认配置
     * @param field
     * @return
     */
    private PageFieldConfigModel getPageFieldConfigModel(MenuFields field) {
        PageFieldConfigModel fcm = new PageFieldConfigModel();
        fcm.setFieldName(field.getField_name());
        fcm.setDisplayName(field.getDisplay_name());
        fcm.setMarginType(field.getField_type());
        fcm.setNullable(String.valueOf(true));
        fcm.setEditable(String.valueOf(true));
        fcm.setGridWidth(100);
        fcm.setShowInGrid(String.valueOf(true));
        fcm.setShowInSearch(String.valueOf(false));
        fcm.setShowInForm(String.valueOf(true));
        fcm.setValidateType("");
        return fcm;
    }

    /**
     * 根据角色id和菜单id获取其字段权限
     * @param
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"saveOrUpdate"})
    public String saveMenuFieldJson(FieldControl fieldControl,Model model){

        JSONArray jsonArray = (JSONArray) JSON.parse((String) fieldControl.getContent());
        String json  = jsonArray.toJSONString();
        fieldControl.setContent(json);

        String rolecode = getPara("rolecode");
        String menuid = getPara("menuno");
        FieldControl control = controllService.getFieldControl(rolecode,menuid);

        if(control == null){
            controllService.insert(fieldControl);
        }else{
            fieldControl.setId(control.getId());
            controllService.update(fieldControl);
        }


        return "success";

    }

}
