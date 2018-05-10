package com.tiansu.eam.modules.faultOrder.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.act.service.EamProcessService;
import com.tiansu.eam.modules.employee.dao.EamUserExtDao;
import com.tiansu.eam.modules.faultOrder.entity.*;
import com.tiansu.eam.modules.faultOrder.service.FaultOrderService;
import com.tiansu.eam.modules.sys.entity.CodeEnum;
import com.tiansu.eam.modules.sys.entity.User;
import com.tiansu.eam.modules.sys.utils.SequenceUtils;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.activiti.engine.FormService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wangjl
 * @description 故障工单
 * @create 2017-08-18 15:48
 **/
@Controller
@RequestMapping(value = "${adminPath}/faultOrder")
public class FaultOrderController extends BaseController {

    @Autowired
    FaultOrderService faultOrderService;

    @Autowired
    private EamProcessService eamProcessService;

    @Autowired
    FormService formService;

    @Autowired
    private TaskService taskService;

    //报修表单数据key
    public static final String NOTIFY_FORM_DATA = "NOTIFY_FORM_DATA";
    //派单表单数据key
    public static final String MANUAL_DISP_FORM_DATA = "MANUAL_DISP_FORM_DATA";
    //反馈表单数据key
    public static final String FEEDBACK_FORM_DATA = "feedBackDatas";
    //转单表单数据key
    public static final String TRANSFER_FORM_DATA = "TRANSFER_FORM_DATA";
    //挂起表单数据key
    public static final String HANGUP_FORM_DATA = "HANGUP_FORM_DATA";

    /**
     * 获取故障工单记录
     * @param faultOrder
     * @return 故障工单记录
     */
    @ResponseBody
    @RequestMapping(value = {"getOrderList"})
    public Map<String,Object> getOrderList(FaultOrder faultOrder){
        Map paramMap = new HashMap();
        paramMap.put("major","1");
        paramMap.put("dbName", Global.getConfig("jdbc.type"));
        EamUserExtDao eamUserExtDao = SpringContextHolder.getBean(EamUserExtDao.class);
        List<Map> majorEmps = eamUserExtDao.findListByMap(paramMap);

        Map<String,Object> param = getFormMap();
        Iterator iterator = param.entrySet().iterator();

        while(iterator.hasNext()){
            String valueStr = "";
            Map.Entry entry = (Map.Entry) iterator.next();
            String value = (String) entry.getValue();
            if(value.indexOf(";")!=-1){
                List<String> valueLst = Arrays.asList(value.split(";"));
                for(String v : valueLst){
                    valueStr += v + ",";
                }
                if(valueStr.length()>1){
                    valueStr = valueStr.substring(0,valueStr.length()-1);
                }
                param.put((String)entry.getKey(),valueStr);
            }
        }
        Map<String,Object> result = faultOrderService.dataTablePageMap(param);
        return result;
    }

    /**
     * 获取故障工单基本信息
     * @return 故障工单记录
     */
    @ResponseBody
    @RequestMapping(value = {"getOrderDetail"})
    public FaultOrder getOrderDetail(){
        String pstid = getPara("pstid");
        FaultOrder faultOrder = faultOrderService.get(pstid);
        return faultOrder;
    }

    /**
     * 工单保存更新操作
     * @param obj
     * @return
     */
    @Transactional
    @RequestMapping(value = {"saveOrUpdateType"})
    public @ResponseBody Map<String,Object> saveOrUpdateType(@RequestBody JSONObject obj,HttpServletRequest request){

        Map<String,Object> returnMap = new HashMap();
        FaultOrder faultOrder = JSON.toJavaObject(obj,FaultOrder.class);
        if(faultOrder == null){
            returnMap.put("flag",false);
            returnMap.put("msg","参数错误");
            return returnMap;
        }
        faultOrder.setOrder_source(EAMConsts.ORDER_TYPE_FAULT);
        faultOrder.setOrder_status(OrderStatusEnum.PENDING_DISP.value());

        boolean suc = saveOrder(faultOrder);

        returnMap.put("flag",suc);
        returnMap.put("msg","操作成功");
        return returnMap;
    }


    public boolean saveOrder(FaultOrder faultOrder) {
        faultOrder.preInsert();
        //启动流程并保存派单数据；
//        String pstid = eamProcessService.startProcessByPdid(FaultOrderController.NOTIFY_FORM_DATA,"eam_fault_order",faultOrder.getId(),request);
        Map varMap = new HashMap();
        varMap.put(FaultOrderController.NOTIFY_FORM_DATA,faultOrder);
        String pstid = eamProcessService.startProcess(EAMConsts.FAULT_ORDER_FLOWDEF,"eam_fault_order",faultOrder.getId(),"故障工单报修",varMap);

        System.out.println("工单流程实例id["+pstid+"]");
        if("timeout".equals(pstid)){
            throw new RuntimeException("flow is timeout");
        }
        faultOrder.setPstid(pstid);
        faultOrderService.insert(faultOrder);

        return true;
    }





    @RequestMapping(value = {"addOrder"})
    public String addPage(Model model){
        //根据menuno获取数据信息；
        FaultOrder order = new FaultOrder();
        order.setOrder_code(SequenceUtils.getBySeqType(CodeEnum.FAULT_ORDER));
        order.setNotifier_source(EAMConsts.ORDER_SOURCE_PHONE);
        order.setCreateBy(UserUtils.getUser());
        model.addAttribute("faultOrder",order);
        return "modules/faultOrder/faultOrderCreate";
    }

    @RequestMapping(value = {"redirectPage"})
    public String redirectPage(String page){
        return "modules/faultOrder/"+page;
    }


    @RequestMapping(value = {"editOrder"})
    public String editPage(String id, String type,Model model){
        FaultOrder faultOrder = faultOrderService.get(id);
        String order_expect_time=faultOrder.getOrder_expect_time();
        String[] order_expect_times=order_expect_time.split("\\.");
        if(order_expect_time!=null&&!("").equals(order_expect_time)){
            faultOrder.setOrder_expect_time(order_expect_times[0]);
        }

        model.addAttribute("faultOrder",faultOrder);
        //设置页面是编辑页面还是详情页面
        model.addAttribute("ptype",type);
        return "modules/faultOrder/faultOrderEdit";
    }

    /**
     * @creator Douglas
     * @createtime 2017-10-20 15:43
     * @description: 工单反馈提交
     * @param request
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "feedBack")
    @Transactional
    public String feedBack(HttpServletRequest request) {
        String param=getPara("param");
        String taskId = getPara("taskid");//工作流任务id
        String userId = UserUtils.getUser().getLoginname();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        // 权限检查-任务的办理人和当前人不一致不能完成任务
        if (!userId.equals(task.getAssignee())) {
            return "noauth";
        }
        //获取报修表单数据
        FaultOrder faultNotifyOrder = (FaultOrder) taskService.getVariable(taskId,FaultOrderController.NOTIFY_FORM_DATA);
        //将序列化的json字符串转为实体
        FaultOrder faultOrder=JSON.parseObject(param,FaultOrder.class);
        Map formValues = new HashMap();
        formValues.put(FaultOrderController.FEEDBACK_FORM_DATA,param);


        faultOrder.setId(faultNotifyOrder.getId());
        faultOrder.preUpdate();
        faultOrder.setOrder_status(OrderStatusEnum.FINISHED.value());
        faultOrder.setOrder_finish_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        List<OrderTool> newOrderToolList = Lists.newArrayList();
        List<OrderSparepart> newOrderSparepartList = Lists.newArrayList();
        List<OrderPerson> newOrderPersonList = Lists.newArrayList();
        List<OrderOther> newOrderOthersList = Lists.newArrayList();
        List<OrderTool> orderToolList=faultOrder.getOrderTool();//工器具
        if(orderToolList != null){
            for(OrderTool s:orderToolList){
                if(s == null || StringUtils.isEmpty(s.getTool_id())){
                    continue;
                }
                s.setId(IdGen.uuid());
                s.setOrder_id(faultNotifyOrder.getId());
                newOrderToolList.add(s);
            }
        }
        List<OrderSparepart> orderAttachmentList=faultOrder.getOrderAttachment();//备品备件
        if(orderAttachmentList != null){
            for(OrderSparepart s:orderAttachmentList){
                if(s == null || StringUtils.isEmpty(s.getPart_id())){
                    continue;
                }
                s.setId(IdGen.uuid());
                s.setOrder_id(faultNotifyOrder.getId());
                newOrderSparepartList.add(s);
            }
        }
        List<OrderPerson> orderPersonList=faultOrder.getOrderManhaur();//人员工时
        if(orderPersonList != null){
            for(OrderPerson s:orderPersonList){
                if(s == null || StringUtils.isEmpty(s.getEmp_id())){
                    continue;
                }
                s.setId(IdGen.uuid());
                s.setOrder_id(faultNotifyOrder.getId());
                newOrderPersonList.add(s);
            }
        }
        List<OrderOther> orderOtherList=faultOrder.getOrderOther();//其他费用
        if(orderOtherList != null){
            for(OrderOther s:orderOtherList){
                if(s == null || StringUtils.isEmpty(s.getCharge_name())){
                    continue;
                }
                s.setId(IdGen.uuid());
                s.setOrder_id(faultNotifyOrder.getId());
                newOrderOthersList.add(s);
            }
        }
        faultOrder.setOrderTool(newOrderToolList);
        faultOrder.setOrderAttachment(newOrderSparepartList);
        faultOrder.setOrderManhaur(newOrderPersonList);
        faultOrder.setOrderOther(newOrderOthersList);

        faultOrderService.update(faultOrder);
        faultOrderService.insertActualDetail(newOrderToolList,newOrderSparepartList,newOrderPersonList,newOrderOthersList);

        formService.submitTaskFormData(taskId, formValues);
        return "success";
    }

    /**
     * @creator wangjls
     * @createtime 2017-10-20 15:43
     * @description: 工单手动派单
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "dispOrder")
    @Transactional
    public String dispOrder(HttpServletRequest request) {
        String param=getPara("param");
        String taskId = getPara("taskid");//工作流任务id
        String userId = UserUtils.getUser().getLoginname();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        // 权限检查-任务的办理人和当前人不一致不能完成任务
        if (!userId.equals(task.getAssignee())) {
            return "noauth";
        }
        //获取报修表单数据
        FaultOrder faultNotifyOrder = (FaultOrder) taskService.getVariable(taskId,FaultOrderController.NOTIFY_FORM_DATA);
        //将序列化的json字符串转为实体
        FaultOrder faultOrder=JSON.parseObject(param,FaultOrder.class);
        Map formValues = new HashMap();
        formValues.put(FaultOrderController.MANUAL_DISP_FORM_DATA,param);
        //设置手动派单,适配此场景：自动派单，运维主管转单后手工派单，
        // 设置参与者时若此处不指定手动派单仍会按自动派单逻辑设定参与者，导致运维主管还得手动派单，造成死循环
        taskService.setVariable(taskId,"disp_type",EAMConsts.MANUAL_DISP_TYPE);

        faultOrder.setId(faultNotifyOrder.getId());
        faultOrder.setOrder_status(OrderStatusEnum.PENDING_ACCEPT.value());
        faultOrder.setOrder_dispatcher(UserUtils.getUser());
        faultOrder.setOrder_dispatch_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        faultOrder.preUpdate();
        List<OrderTool> newOrderToolList = Lists.newArrayList();
        List<OrderSparepart> newOrderSparepartList = Lists.newArrayList();
        List<OrderPerson> newOrderPersonList = Lists.newArrayList();
        List<OrderOther> newOrderOthersList = Lists.newArrayList();
        List<OrderTool> orderToolList=faultOrder.getOrderTool();//工器具
        if(orderToolList != null){
            for(OrderTool s:orderToolList){
                if(s == null || StringUtils.isEmpty(s.getTool_id())){
                    continue;
                }
                s.setId(IdGen.uuid());
                s.setOrder_id(faultNotifyOrder.getId());
                newOrderToolList.add(s);
            }
        }
        List<OrderSparepart> orderAttachmentList=faultOrder.getOrderAttachment();//备品备件
        if(orderAttachmentList != null){
            for(OrderSparepart s:orderAttachmentList){
                if(s == null || StringUtils.isEmpty(s.getPart_id())){
                    continue;
                }
                s.setId(IdGen.uuid());
                s.setOrder_id(faultNotifyOrder.getId());
                newOrderSparepartList.add(s);
            }
        }
        List<OrderPerson> orderPersonList=faultOrder.getOrderManhaur();//人员工时
        if(orderPersonList != null){
            for(OrderPerson s:orderPersonList){
                if(s == null || StringUtils.isEmpty(s.getEmp_id())){
                    continue;
                }
                s.setId(IdGen.uuid());
                s.setOrder_id(faultNotifyOrder.getId());
                newOrderPersonList.add(s);
            }
        }
        List<OrderOther> orderOtherList=faultOrder.getOrderOther();//其他费用
        if(orderOtherList != null){
            for(OrderOther s:orderOtherList){
                if(s == null || StringUtils.isEmpty(s.getCharge_name())){
                    continue;
                }
                s.setId(IdGen.uuid());
                s.setOrder_id(faultNotifyOrder.getId());
                newOrderOthersList.add(s);
            }
        }

        faultOrder.setOrderTool(newOrderToolList);
        faultOrder.setOrderAttachment(newOrderSparepartList);
        faultOrder.setOrderManhaur(newOrderPersonList);
        faultOrder.setOrderOther(newOrderOthersList);

        faultOrderService.update(faultOrder);
        faultOrderService.insertPlanDetail(newOrderToolList,newOrderSparepartList,newOrderPersonList,newOrderOthersList);

        formService.submitTaskFormData(taskId, formValues);


        return "success";
    }


}
