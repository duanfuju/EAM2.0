package com.tiansu.eam.interfaces.order.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.interfaces.common.InfoConstantEnum;
import com.tiansu.eam.interfaces.order.service.OrderRestService;
import com.tiansu.eam.interfaces.resulthelper.APPResponseBody;
import com.tiansu.eam.modules.act.service.EamProcessService;
import com.tiansu.eam.modules.faultOrder.entity.*;
import com.tiansu.eam.modules.faultOrder.controller.FaultOrderController;
import com.tiansu.eam.modules.faultOrder.entity.FaultOrder;
import com.tiansu.eam.modules.faultOrder.service.FaultOrderService;
import com.tiansu.eam.modules.sys.entity.User;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.activiti.engine.FormService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteDateUseDateFormat;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue;

/**
 * @author wangr
 * @description 工单
 * @create 2017-10-11 下午 4:49
 */
@Path("/{tenant}/order")
@Controller
public class OrderRestController extends BaseController {

    private static FaultOrderService faultOrderService = SpringContextHolder.getBean(FaultOrderService.class);

    private static EamProcessService eamProcessService = SpringContextHolder.getBean(EamProcessService.class);

    private static OrderRestService orderRestService = SpringContextHolder.getBean(OrderRestService.class);

    private static TaskService taskService = SpringContextHolder.getBean(TaskService.class);

    private static FormService formService = SpringContextHolder.getBean(FormService.class);

    private static ActTaskService actTaskService = SpringContextHolder.getBean(ActTaskService.class);

    /**
     * @param userId   用户id
     * @param response
     * @return
     * @creator wangr
     * @createtime 2017/10/17 0017 下午 2:03
     * @description: 获取我的报修列表
     */
    @GET
    @Path("/getMyRepList")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMyRepList(@QueryParam("userId") String userId,
                               @QueryParam("start") int start,
                               @QueryParam("length") int length,
                               @QueryParam("status") List<String> status,
                               @Context HttpServletResponse response) {
        logger.info("==== 获取我的保修列表 开始 ====");
        response.setHeader("Access-Control-Allow-Origin", "*");
        APPResponseBody result = new APPResponseBody();
        try {
            Map param = getFormMapForApp();
            param.put("userId", userId);
            param.put("start", start);
            param.put("length", length);
            param.put("list", status);
            logger.info("==== 获取我的保修列表 参数 开始");
            logger.info("==== userId:" + userId);
            logger.info("==== start:" + start);
            logger.info("==== length:" + length);
            if (status != null && status.size() > 0) {
                for (String statue : status) {
                    logger.info("==== status:" + statue);
                }
            }
            logger.info("==== 获取我的保修列表 参数 结束");
            Map maResult = orderRestService.findMyRepList(param);
            result.setData(maResult);
        } catch (Exception e) {
            logger.error("获取我的保修列表查询出错：", e);
            result.setStatus(InfoConstantEnum.FAIL.getCode());
            result.setErrorcode(InfoConstantEnum.QUERY_FAIL.getCode());
            result.setErrmsg(InfoConstantEnum.QUERY_FAIL.getMessage());
        }
        logger.info("==== 获取我的保修列表 结束 ====");
        return JSON.toJSONString(result, WriteMapNullValue, WriteDateUseDateFormat);
    }


    /**
     * @param request
     * @param response
     * @return
     * @creator wangr
     * @createtime 2017/10/17 0017 下午 2:04
     * @description: 新增故障报修并启动流程
     */
    @Path("/insert")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String orderInsert(@FormParam("userId") String userId,
                              @FormParam("param") String param,
                              @Context HttpServletResponse response) {
        logger.info("==== 新增故障报修并启动流程 开始 =====");
        response.setHeader("Access-Control-Allow-Origin", "*");

        APPResponseBody result = new APPResponseBody();
        try {

            FaultOrder order = JSON.parseObject(param, FaultOrder.class);
            logger.info("==== 参数:" + param);
            if (order == null || StringUtils.isBlank(userId)) {
                result.setStatus(InfoConstantEnum.FAIL.getCode());
                result.setErrorcode(InfoConstantEnum.PARAM.getCode());
                result.setErrmsg(InfoConstantEnum.PARAM.getMessage());
                logger.info("==== 参数为空 userId:" + userId + "\t\tparam:" + param);
                logger.info("==== 新增故障报修并启动流程 结束 =====");
                return JSON.toJSONString(result);
            }
            //设置新增故障工单来源初始状态
            order.setOrder_source(EAMConsts.ORDER_TYPE_FAULT);
            order.setOrder_status(OrderStatusEnum.PENDING_DISP.value());
            //设置工单的主键
            order.setId(IdGen.uuid());
            //工单表新增更新设置
            order.setCreateBy(UserUtils.get(userId));
            order.setCreateDate(new Date());
            order.setUpdateBy(UserUtils.get(userId));
            order.setUpdateDate(new Date());

            Map varMap = new HashMap();
            varMap.put(FaultOrderController.NOTIFY_FORM_DATA, order);
            logger.info("==== 启动流程");
            String pstid = eamProcessService.startProcess(EAMConsts.FAULT_ORDER_FLOWDEF, "eam_fault_order", order.getId(), "故障工单报修", varMap);
            logger.info("==== 流程id：" + pstid);
            //进入流程是否超时
            if ("timeout".equals(pstid)) {
                result.setStatus(InfoConstantEnum.FAIL.getCode());
                result.setErrorcode(InfoConstantEnum.Process_TIMEOUT.getCode());
                result.setErrmsg(InfoConstantEnum.Process_TIMEOUT.getMessage());
                logger.info("==== 启动流程超时");
                throw new RuntimeException("flow is timeout");
            } else {

                order.setPstid(pstid);
                faultOrderService.insert(order);
                logger.info("==== APP工单新增成功");
            }

        } catch (Exception e) {
            logger.error("==== 新增故障报修并启动流程出错：", e);
            result.setErrorcode(InfoConstantEnum.ORDER_INSERT_FAIL.getCode());
            result.setErrmsg(InfoConstantEnum.ORDER_FEEDBACK_FAIL.getMessage());
            result.setStatus(InfoConstantEnum.FAIL.getCode());
        }
        logger.info("==== 新增故障报修并启动流程 结束 =====");
        return JSON.toJSONString(result);
    }


    /**
     * @param userId   用户id
     * @param start    从start开始
     * @param length   页大小
     * @param status   工单状态
     * @param response
     * @return
     * @creator wangr
     * @createtime 2017/11/2 0002 上午 9:40
     * @description: 分页获取我的工单列表
     */
    @GET
    @Path("/getMyOrderList")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMyOrderList(@QueryParam("userId") String userId,
                                 @QueryParam("start") int start,
                                 @QueryParam("length") int length,
                                 @QueryParam("status") List<String> status,
                                 @Context HttpServletResponse response) {
        logger.info("==== 分页获取我的工单列表 开始 ====");
        response.setHeader("Access-Control-Allow-Origin", "*");
        APPResponseBody result = new APPResponseBody();
        try {
            logger.info("==== 分页获取我的工单列表 参数 开始");
            logger.info("==== userId:" + userId);
            logger.info("==== start:" + start);
            logger.info("==== length:" + length);
            if (status != null && status.size() > 0) {
                for (String statue : status) {
                    logger.info("==== status:" + statue);
                }
            }
            logger.info("==== 分页获取我的工单列表 参数 结束");
            Map map = orderRestService.findMyOrderList(userId, start,
                    length, status);
            if (!(boolean) map.get("success")) {
                logger.info("==== 分页获取我的工单列表 代办查询失败");
                result.setStatus(InfoConstantEnum.FAIL.getCode());
                result.setErrorcode(InfoConstantEnum.QUERY_FAIL.getCode());
                result.setErrmsg(InfoConstantEnum.QUERY_FAIL.getMessage());
                logger.info("==== 分页获取我的工单列表 结束 ====");
                return JSON.toJSONString(result);
            }
            result.setData(map);
        } catch (Exception e) {
            logger.error("==== 分页获取我的工单列表：", e);
            result.setStatus(InfoConstantEnum.FAIL.getCode());
            result.setErrorcode(InfoConstantEnum.QUERY_FAIL.getCode());
            result.setErrmsg(InfoConstantEnum.QUERY_FAIL.getMessage());
        }
        logger.info("==== 分页获取我的工单列表 结束 ====");
        return JSON.toJSONString(result, WriteMapNullValue, WriteDateUseDateFormat);
    }

    /**
     * @param userId   用户id
     * @param orderId  工单id
     * @param devId    设备id
     * @param flag     是否从我的保修列表进入工单详情  0是 1否
     * @param response
     * @return
     * @creator wangr
     * @createtime 2017/11/2 0002 上午 9:41
     * @description: 根据工单id和用户获取工单详情（扫码获取）
     */
    @GET
    @Path("/getMyOrderDetails")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMyOrderDetails(@QueryParam("userId") String userId,
                                    @QueryParam("orderId") String orderId,
                                    @QueryParam("devId") String devId,
                                    @QueryParam("flag") int flag,
                                    @Context HttpServletResponse response) {
        logger.info("==== 根据工单id和用户获取工单详情 开始 =====");
        logger.info("==== userId:" + userId + ";orderId:" + orderId + ";devId:" + devId + ";flag:" + flag);
        response.setHeader("Access-Control-Allow-Origin", "*");
        APPResponseBody result = new APPResponseBody();

        try {
            result.setData(orderRestService.findMyOrderDetail(userId, orderId,
                    devId, flag));
        } catch (Exception e) {
            logger.error("==== 查询我的工单详情出错（扫码获取）：", e);
            result.setStatus(InfoConstantEnum.FAIL.getCode());
            result.setErrorcode(InfoConstantEnum.QUERY_FAIL.getCode());
            result.setErrmsg(InfoConstantEnum.QUERY_FAIL.getMessage());
        }
        logger.info("==== 根据工单id和用户获取工单详情 结束 =====");
        return JSON.toJSONString(result, WriteMapNullValue, WriteDateUseDateFormat);
    }

    /**
     * @param userId
     * @param orderId
     * @param response
     * @return
     * @creator wangr
     * @createtime 2017/11/2 0002 上午 9:49
     * @description: 获取工单反馈时需要的工单计划工序等
     */
    @GET
    @Path("/getMyOrderFKDetails")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMyOrderFKDetails(@QueryParam("userId") String userId,
                                      @QueryParam("orderId") String orderId,
                                      @Context HttpServletResponse response) {
        logger.info("==== 获取工单反馈时需要的工单计划工序等 开始 =====");
        response.setHeader("Access-Control-Allow-Origin", "*");
        APPResponseBody result = new APPResponseBody();
        try {
            result.setData(orderRestService.getMyOrderFKDetails(orderId, userId));
        } catch (Exception e) {
            logger.error("==== 获取工单反馈时需要的工单计划工序等：", e);
            result.setStatus(InfoConstantEnum.FAIL.getCode());
            result.setErrorcode(InfoConstantEnum.QUERY_FAIL.getCode());
            result.setErrmsg(InfoConstantEnum.QUERY_FAIL.getMessage());
        }
        logger.info("==== 获取工单反馈时需要的工单计划工序等 结束 =====");
        return JSON.toJSONString(result, WriteMapNullValue, WriteDateUseDateFormat);
    }


    /**
     * @param param    反馈参数
     * @param taskId   任务id
     * @param userId   用户id
     * @param response
     * @return
     * @creator wangr
     * @createtime 2017/10/20 下午 4:58
     * @description: 工单反馈
     */
    @Path("/feedBack")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String feedBack(@FormParam("param") String param,
                           @FormParam("taskId") String taskId,
                           @FormParam("userId") String userId,
                           @Context HttpServletResponse response) {
        logger.info("==== 工单反馈 开始 =====");
        logger.info("==== param:" + param);
        logger.info("==== userId:" + userId);
        logger.info("==== taskId:" + taskId);
        response.setHeader("Access-Control-Allow-Origin", "*");

        APPResponseBody result = new APPResponseBody();
        //参数不为空
        if (StringUtils.isBlank(param) || StringUtils.isBlank(taskId)
                || StringUtils.isBlank(userId)) {
            logger.info("==== 工单反馈参数是空");
            result.setStatus(InfoConstantEnum.FAIL.getCode());
            result.setErrorcode(InfoConstantEnum.PARAM.getCode());
            result.setErrmsg(InfoConstantEnum.PARAM.getMessage());
            logger.info("==== 工单反馈 结束 =====");
            return JSON.toJSONString(result);
        }

        try {
            //签收参数（任务id，签收人）
            actTaskService.claim(taskId, userId);
            logger.info("==== 签收成功");
            //将序列化的json字符串转为实体
            FaultOrder faultOrder = JSON.parseObject(param, FaultOrder.class);
            if (faultOrder == null) {
                logger.info("==== faultOrder是空！");
                result.setStatus(InfoConstantEnum.FAIL.getCode());
                result.setErrorcode(InfoConstantEnum.PARAM.getCode());
                result.setErrmsg(InfoConstantEnum.PARAM.getMessage());
                logger.info("==== 工单反馈 结束 =====");
                return JSON.toJSONString(result);
            }

            // 权限检查-任务的办理人和当前人不一致不能完成任务
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            logger.info("==== 查询任务结束！");
            if (!userId.equals(task.getAssignee())) {
                result.setStatus(InfoConstantEnum.FAIL.getCode());
                result.setErrorcode(InfoConstantEnum.ACTIVItY_PERMISSIONS_FAIL.getCode());
                result.setErrmsg(InfoConstantEnum.ACTIVItY_PERMISSIONS_FAIL.getMessage());
                logger.info("==== 任务的办理人和当前人不一致不能完成任务！userId：" + userId);
                logger.info("==== 工单反馈 结束 ====");
                return JSON.toJSONString(result);
            }

            //获取报修表单数据
            FaultOrder faultNotifyOrder = (FaultOrder) taskService.getVariable(taskId, FaultOrderController.NOTIFY_FORM_DATA);
            Map formValues = new HashMap();
            formValues.put(FaultOrderController.FEEDBACK_FORM_DATA, param);
            //反馈时改变工单状态新增完成时间
            User user = UserUtils.get(userId);
            faultOrder.setId(faultNotifyOrder.getId());
            faultOrder.setUpdateBy(user);
            faultOrder.setUpdateDate(new Date());
            faultOrder.setOrder_status(OrderStatusEnum.FINISHED.value());
            faultOrder.setOrder_finish_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            List<OrderTool> newOrderToolList = Lists.newArrayList();
            List<OrderSparepart> newOrderSparepartList = Lists.newArrayList();
            List<OrderPerson> newOrderPersonList = Lists.newArrayList();
            List<OrderOther> newOrderOthersList = Lists.newArrayList();

            List<OrderTool> orderToolList = faultOrder.getOrderTool();//工器具
            //工器具
            if (orderToolList != null && orderToolList.size() > 0) {
                for (OrderTool s : orderToolList) {
                    if (s == null || StringUtils.isEmpty(s.getTool_id())) {
                        continue;
                    }
                    s.setId(IdGen.uuid());
                    s.setOrder_id(faultNotifyOrder.getId());
                    newOrderToolList.add(s);
                }
            }

            //备品备件
            List<OrderSparepart> orderAttachmentList = faultOrder.getOrderAttachment();
            if (orderAttachmentList != null && orderAttachmentList.size() > 0) {
                for (OrderSparepart s : orderAttachmentList) {
                    if (s == null || StringUtils.isEmpty(s.getPart_id())) {
                        continue;
                    }
                    s.setId(IdGen.uuid());
                    s.setOrder_id(faultNotifyOrder.getId());
                    newOrderSparepartList.add(s);
                }
            }

            //人员工时
            List<OrderPerson> orderPersonList = faultOrder.getOrderManhaur();
            if (orderPersonList != null && orderPersonList.size() > 0) {
                for (OrderPerson s : orderPersonList) {
                    if (s == null || StringUtils.isEmpty(s.getEmp_id())) {
                        continue;
                    }
                    s.setId(IdGen.uuid());
                    s.setOrder_id(faultNotifyOrder.getId());
                    newOrderPersonList.add(s);
                }
            }
            //其他费用
            List<OrderOther> orderOtherList = faultOrder.getOrderOther();
            if (orderOtherList != null && orderOtherList.size() > 0) {
                for (OrderOther s : orderOtherList) {
                    if (s == null || StringUtils.isEmpty(s.getCharge_name())) {
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
            logger.info("==== 工单更新成功");
            faultOrderService.insertActualDetail(newOrderToolList, newOrderSparepartList, newOrderPersonList, newOrderOthersList);
            logger.info("==== 新增工器具成功");
            formService.submitTaskFormData(taskId, formValues);
            logger.info("==== 流程提价成功");
        } catch (Exception e) {
            logger.error("==== 工单反馈：", e);
            result.setErrorcode(InfoConstantEnum.ORDER_FEEDBACK_FAIL.getCode());
            result.setErrmsg(InfoConstantEnum.ORDER_FEEDBACK_FAIL.getMessage());
            result.setStatus(InfoConstantEnum.FAIL.getCode());
        }
        logger.info("==== 工单反馈 结束 =====");
        return JSON.toJSONString(result);
    }


    /**
     * @param param
     * @param taskId
     * @param userId
     * @param response
     * @return
     * @creator wangr
     * @createtime 2017/11/6 0006 下午 3:41
     * @description: 工单手动派单
     */
    @Path("/dispOrder")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String dispOrder(@FormParam("param") String param,
                            @FormParam("taskId") String taskId,
                            @FormParam("userId") String userId,
                            @Context HttpServletResponse response) {
        logger.info("==== 工单手动派单 开始 =====");
        logger.info("==== param:" + param);
        logger.info("==== userId:" + userId);
        logger.info("==== taskId:" + taskId);
        response.setHeader("Access-Control-Allow-Origin", "*");
        APPResponseBody result = new APPResponseBody();

        try {
            //签收参数（任务id，签收人）
            actTaskService.claim(taskId, userId);
            logger.info("==== 签收成功");
            //将序列化的json字符串转为实体
            FaultOrder faultOrder = JSON.parseObject(param, FaultOrder.class);
            if (faultOrder == null) {
                logger.info("==== 将序列化的json字符串转为实体 faultOrder是空");
                result.setStatus(InfoConstantEnum.FAIL.getCode());
                result.setErrorcode(InfoConstantEnum.PARAM.getCode());
                result.setErrmsg(InfoConstantEnum.PARAM.getMessage());
                logger.info("==== 工单手动派单 结束 =====");
                return JSON.toJSONString(result);
            }

            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            logger.info("==== 任务查询结束");
            // 权限检查-任务的办理人和当前人不一致不能完成任务
            if (!userId.equals(task.getAssignee())) {
                result.setStatus(InfoConstantEnum.FAIL.getCode());
                result.setErrorcode(InfoConstantEnum.ACTIVItY_PERMISSIONS_FAIL.getCode());
                result.setErrmsg(InfoConstantEnum.ACTIVItY_PERMISSIONS_FAIL.getMessage());
                logger.info("==== 任务的办理人和当前人不一致不能完成任务 实际是：" + task.getAssignee());
                logger.info("==== 工单手动派单 结束 =====");
                return JSON.toJSONString(result);
            }
            //获取报修表单数据
            FaultOrder faultNotifyOrder = (FaultOrder) taskService.getVariable(taskId, FaultOrderController.NOTIFY_FORM_DATA);
            if (StringUtils.isBlank(faultNotifyOrder.getId())) {
                result.setStatus(InfoConstantEnum.FAIL.getCode());
                result.setErrorcode(InfoConstantEnum.PARAM.getCode());
                result.setErrmsg(InfoConstantEnum.PARAM.getMessage());
                logger.info("==== 工单id为空");
                logger.info("==== 工单手动派单 结束 =====");
                return JSON.toJSONString(result);
            }

            Map formValues = new HashMap();
            formValues.put(FaultOrderController.MANUAL_DISP_FORM_DATA,param);

            User user = UserUtils.get(userId);
            faultOrder.setId(faultNotifyOrder.getId());
            faultOrder.setOrder_status(OrderStatusEnum.PENDING_ACCEPT.value());
            faultOrder.setOrder_dispatcher(user);
            faultOrder.setOrder_dispatch_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            faultOrder.setUpdateBy(user);
            faultOrder.setUpdateDate(new Date());

            faultOrderService.update(faultOrder);
            logger.info("==== 工单更新成功");
            formService.submitTaskFormData(taskId, formValues);
            logger.info("==== 工单派单流程提交成功");
        } catch (Exception e) {
            logger.error("==== 工单手动派单：", e);
            result.setStatus(InfoConstantEnum.FAIL.getCode());
            result.setErrmsg(InfoConstantEnum.FAIL.getMessage());
            return JSON.toJSONString(result);
        }
        logger.info("==== 工单手动派单 结束 =====");
        return JSON.toJSONString(result);
    }

    /**
     * @param response
     * @return
     * @creator wangr
     * @createtime 2017/11/6 0006 下午 3:40
     * @description: 工单统计
     */
    @Path("/orderCounts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String orderCounts(@Context HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        APPResponseBody result = new APPResponseBody();
        try {

        } catch (Exception e) {

        }
        return JSON.toJSONString(result);
    }

}
