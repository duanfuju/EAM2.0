package com.tiansu.eam.modules.schedual.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.tiansu.eam.common.persistence.RelatedModel;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.schedual.entity.SchedualType;
import com.tiansu.eam.modules.schedual.entity.SchedualTypeTime;
import com.tiansu.eam.modules.schedual.service.SchedualTypeService;
import com.tiansu.eam.modules.sys.entity.CodeEnum;
import com.tiansu.eam.modules.sys.utils.SequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangjl
 * @description 排班类型controller
 * @create 2017-08-18 15:48
 **/
@Controller
@RequestMapping(value = "${adminPath}/schedualType")
public class SchedualTypeController extends BaseController {

    @Autowired
    SchedualTypeService schedualTypeService;

    /**
     * @creator wangjl
     * @createtime 2017/8/23 16:10
     * @description: 获取排班类型
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"getSchedualType"})
    public Map<String,Object> getSchedualType(){
        Map param = getFormMap();
        Map<String,Object> result = schedualTypeService.dataTablePageMap(param);
        /**
         * 对于主子表等一对多结构的 查询的个数和打印的sql执行结果不一致，在此重新查询子表数据
         * PaginationInterceptor.intercept无法适配此种结构
         */
        List<Map> list = (List<Map>) result.get("data");
        if(list != null && list.size()>0){
//            schedualTypeService.getSchedualTimes(list);
        }


        return result;
    }

    /**
     * @creator wangjl
     * @createtime 2017/8/23 16:10
     * @description: 获取排班类型
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"getAllSchedualType"})
    public List<Map> getAllSchedualType(){
        Map param = getFormMap();
        List<Map> result = schedualTypeService.findListByMap(param);
        return result;
    }

//    @ModelAttribute("schedualType")
    @RequestMapping(value = {"addType"})
    public String addPage(String id, Model model){
//        ModelAndView
//        return "modules/material/testMaterialFormAdd";
        //根据menuno获取数据信息；
        SchedualType type = new SchedualType();
        type.setType_code(SequenceUtils.getBySeqType(CodeEnum.SCHEDUAL_TYPE));
        model.addAttribute("schedualType",type);
        return "modules/schedual/schedualTypeAdd";
    }


    @RequestMapping(value = {"editType"})
    public String editPage(String id, String type,Model model){
        SchedualType schedualType = schedualTypeService.get(id);
        model.addAttribute("schedualType",schedualType);
        //设置页面是编辑页面还是详情页面
        model.addAttribute("ptype",type);
        return "modules/schedual/schedualTypeEdit";
    }

    /**
     * 排班类型保存更新操作
     * @param obj
     * @return
     */
    @Transactional
    @RequestMapping(value = {"saveOrUpdateType"})
    public @ResponseBody Map<String,Object> saveOrUpdateType(@RequestBody JSONObject obj){

        Map<String,Object> returnMap = new HashMap();
        SchedualType schedualType = JSON.toJavaObject(obj,SchedualType.class);
        if(schedualType == null){
            returnMap.put("flag",false);
            returnMap.put("msg","参数错误");
            return returnMap;
        }

        if(schedualType != null && StringUtils.isEmpty(schedualType.getId())){
            schedualTypeService.insert(schedualType);
            for(SchedualTypeTime time : schedualType.getSchedual_time_list()){
                time.setType_id(schedualType.getId());
                time.preInsert();
            }
            if(schedualType.getSchedual_time_list() !=null && schedualType.getSchedual_time_list().size() > 0){
                schedualTypeService.batchSaveTime(schedualType.getSchedual_time_list());
            }

        }else{
            //更新操作: 子表采取先删再插的形式
            schedualType.preUpdate();
            schedualTypeService.update(schedualType);
            schedualTypeService.deleteByTypeId(schedualType.getId());
            for(SchedualTypeTime time : schedualType.getSchedual_time_list()){
                time.setType_id(schedualType.getId());
                time.preInsert();
            }
            if(schedualType.getSchedual_time_list() !=null && schedualType.getSchedual_time_list().size() > 0){
                schedualTypeService.batchSaveTime(schedualType.getSchedual_time_list());
            }
        }

        returnMap.put("flag",true);
        returnMap.put("msg","操作成功");
        return returnMap;
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = {"deleteType"})
    public Map<String,Object> deleteSchedualType(SchedualType type, Model model){
        Map<String,Object> returnMap = new HashMap();
        if(type == null){
            returnMap.put("flag",false);
            returnMap.put("msg","参数错误");
            return returnMap;
        }
        type.preDelete();
        List<RelatedModel> relatedModelList = Lists.newArrayList();
        relatedModelList.add(new RelatedModel("eam_schedual","schedual_type_id","eam_schedual.isdelete !=1"));
        if(schedualTypeService.isRelated(relatedModelList,"eam_schedual_type","id",type.getId())){
            returnMap.put("flag",false);
            returnMap.put("msg","数据被引用，不能删除");
        }else{
            schedualTypeService.delete(type);
            returnMap.put("flag",true);
            returnMap.put("msg","操作成功");
        }




        return returnMap;
    }
}
