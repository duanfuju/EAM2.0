package com.tiansu.eam.modules.schedual.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.common.web.BaseResult;
import com.tiansu.eam.modules.act.service.EamProcessService;
import com.tiansu.eam.modules.schedual.entity.Schedual;
import com.tiansu.eam.modules.schedual.entity.SchedualOrder;
import com.tiansu.eam.modules.schedual.service.SchedualOrderService;
import com.tiansu.eam.modules.schedual.service.SchedualService;
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
 * @description 排班controller
 * @create 2017-08-18 15:48
 **/
@Controller
@RequestMapping(value = "${adminPath}/schedualOrder")
public class SchedualOrderController extends BaseController {

    @Autowired
    SchedualService schedualService;
    @Autowired
    SchedualOrderService schedualOrderService;
    @Autowired
    private EamProcessService eamProcessService;

    public static final String SCHEDUAL_ORDER_FLOW = "schedualOrder";
    /**
     * 根据排班记录查看排班信息
     * @param schedualOrder
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"getSchedualDetail"})
    public SchedualOrder getSchedualDetail(SchedualOrder schedualOrder, Model model){
        schedualOrder = schedualOrderService.get(schedualOrder.getId());
        return schedualOrder;
    }

    /**
     * 创建排班表表单页面
     * @param schedualOrder
     * @return
     */
    @RequestMapping(value = {"createOrderPage"})
    public String createOrderPage(SchedualOrder schedualOrder, Model model){
        SchedualOrder resultOrder = new SchedualOrder();
        if(StringUtils.isEmpty(schedualOrder.getId()) || "null".equals(schedualOrder.getId())){
            resultOrder.setOrder_code(SequenceUtils.getBySeqType(CodeEnum.SCHEDUAL_ORDER));
        }else{
            resultOrder = schedualOrderService.get(schedualOrder.getId());
        }

        model.addAttribute("schedualOrder",resultOrder);
        return "modules/schedual/schedualOrder";
    }


    /**
     * 获取排班编辑记录
     * @param schedualOrder
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"getSchedualOrders"})
    public Map<String,Object> getSchedualOrders(SchedualOrder schedualOrder){
        Map param = getFormMap();
        Map<String,Object> result = schedualOrderService.dataTablePageMap(param);
        return result;
    }

    /**
     * 保存或更新排班记录。前台排班表为数组数据，无法通过spring-mvc直接参数映射，故先转换为jsonobject
     * 采用事物同时存储多表数据
     * @param
     * @param
     * @return
     */
    @Transactional
    @ResponseBody
    @RequestMapping(value = {"saveOrUpdate"})
    public BaseResult saveOrUpdate(@RequestBody JSONObject obj) {
        try {
            SchedualOrder schedualOrder = JSON.toJavaObject(obj,SchedualOrder.class);
            if(schedualOrder == null){
                return new BaseResult(false,null,"参数错误");
            }
            if(schedualOrder.getSchedualList() == null || schedualOrder.getSchedualList().size() == 0 ){
                throw new RuntimeException("参数错误");
            }

            if(StringUtils.isEmpty(schedualOrder.getId())){
                schedualOrderService.insert(schedualOrder);
            }else{
                //更新操作: 子表采取先删再插的形式
                schedualOrder.preUpdate();
                schedualOrderService.update(schedualOrder);
                schedualService.deleteByOrderId(schedualOrder.getId());
            }

            int MAX_BATCH_SIZE = 1000; //sqlserver批量插入insert最长不能超过2000条
            List<List<Schedual>> subSchedualList = Lists.partition(schedualOrder.getSchedualList(), MAX_BATCH_SIZE);
            for (List<Schedual> schedualList : subSchedualList) {
                for(Schedual schedual : schedualList){
                    schedual.setOrder_id(schedualOrder.getId());
                    schedual.preInsert();
                }
                schedualService.batchSave(schedualList);
            }
            return new BaseResult(true,null,"操作成功");

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }



    }

    /**
     * 删除排班记录
     * @param schedualOrder
     * @param model
     * @return
     */
    @Transactional
    @ResponseBody
    @RequestMapping(value = {"deleteSchedualOrder"})
    public BaseResult deleteSchedualOrder(SchedualOrder schedualOrder, Model model){
        if(schedualOrder == null){
            return new BaseResult(false,null,"参数错误");
        }
        schedualOrder.preDelete();
        schedualOrderService.delete(schedualOrder);
        schedualService.deleteByOrderId(schedualOrder.getId());

        return new BaseResult(true,null,"操作成功");
    }



    @Transactional
    @ResponseBody
    @RequestMapping(value = {"commitSchedualOrder"})
    public BaseResult commitSchedualOrder(SchedualOrder schedualOrder, Model model){
        if(schedualOrder == null){
            return new BaseResult(false,null,"参数错误");
        }
        schedualOrder.preUpdate();
        schedualOrder.setApprove_status(EAMConsts.ORDER_PENDING_APPROVE);


        Map varMap = new HashMap();
        varMap.put(SCHEDUAL_ORDER_FLOW,schedualOrder);
        String pstid = eamProcessService.startProcess(SCHEDUAL_ORDER_FLOW,"eam_schedual_order",schedualOrder.getId(),"排班表审批",varMap);

        if("timeout".equals(pstid)){
            throw new RuntimeException("flow is timeout");
        }
        schedualOrder.setPstid(pstid);
        schedualOrderService.update(schedualOrder);




        return new BaseResult(true,null,"操作成功");
    }



}
