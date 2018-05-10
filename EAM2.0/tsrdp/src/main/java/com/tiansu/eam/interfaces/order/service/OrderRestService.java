package com.tiansu.eam.interfaces.order.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.interfaces.act.service.TaskRestService;
import com.tiansu.eam.interfaces.common.History;
import com.tiansu.eam.interfaces.order.dao.OrderRestDao;
import com.tiansu.eam.interfaces.order.entity.Order;
import com.tiansu.eam.interfaces.order.entity.OrderDetailsJson;
import com.tiansu.eam.interfaces.order.entity.OrderFKJson;
import com.tiansu.eam.interfaces.order.entity.OrderListJson;
import com.tiansu.eam.modules.faultOrder.entity.FaultSwitch;
import com.tiansu.eam.modules.sys.entity.User;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.impl.persistence.entity.HistoricFormPropertyEntity;
import org.activiti.engine.task.Task;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * @author wangr
 * @description
 * @create 2017-10-11 下午 4:54
 */
@Service
@Transactional(readOnly = true)
public class OrderRestService extends CrudService<OrderRestDao, Order> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public static final String SOURCE = "APP";
    public static final String STARTEVENT = "startEvent";
    public static final String USERTASK = "userTask";

    //工单接口dao
    @Autowired
    private OrderRestDao orderRestDao;

    //历史任务Service
    @Autowired
    private HistoryService historyService;

    //任务接口Service
    @Autowired
    private TaskRestService taskRestService;

    /**
     * @param param
     * @return
     * @creator wangr
     * @createtime 2017/10/17 0017 下午 2:06
     * @description: 我的保修列表
     * @modifier wangr
     * @modifytime 2017/12/1 0001 上午 11:13
     * @modifyDec: 修改报修人
     */
    public Map findMyRepList(Map param) {
        Map map = new HashMap();

        //设置分页数据
        int start = Integer.parseInt(param.get("start").toString());
        int length = Integer.parseInt(param.get("length").toString());

        Page<T> page = new Page();

        page.setPageNo(start / length + 1);
        page.setPageSize(length);
        //获取排序数据
        //排序  按照报修时间降序排序
        String orderBy = "a.create_time desc";
        if (StringUtils.isNotBlank(orderBy)) {
            page.setOrderBy(orderBy);
        }

        param.put("page", page);
        logger.info("==== 分页查询开始");
        List<OrderListJson> list = orderRestDao.findMyRepList(param);
        logger.info("==== 分页查询结束");
        for (OrderListJson orderListJson : list) {
            if ("1".equals(orderListJson.getOrderStatus()) ||
                    "2".equals(orderListJson.getOrderStatus()) ||
                    "3".equals(orderListJson.getOrderStatus()) ||
                    "4".equals(orderListJson.getOrderStatus()) ||
                    "7".equals(orderListJson.getOrderStatus())) {

                if (2 == orderListJson.getOrderLevel()) {
                    orderListJson.setTimeName(getTimeName(orderListJson.getOrderLevel(),
                            orderListJson.getOrderStatus(), orderListJson.getOrderId(),
                            orderListJson.getExpectTime()));
                } else {
                    orderListJson.setTimeName(getTimeName(orderListJson.getOrderLevel(),
                            orderListJson.getOrderStatus(), orderListJson.getOrderId(),
                            orderListJson.getCreateTime()));
                }
            }
            if (SOURCE.equals(orderListJson.getSource())) {
                orderListJson.setBugLinkMan(UserUtils.get(orderListJson.getBugLinkMan()).getRealname());
            }
            logger.info("==== 流程id：" + orderListJson.getPstid());
            Task task = taskRestService.getTask(orderListJson.getPstid());
            if (task != null) {
                orderListJson.setTaskId(task.getId());
                orderListJson.setTaskDefKey(task.getTaskDefinitionKey());
                logger.info("==== 流程任务id：" + task.getId());
                logger.info("==== 流程任务key：" + task.getTaskDefinitionKey());
            }
        }

        map.put("recordsFiltered", page.getCount());
        map.put("recordsTotal", page.getCount());
        map.put("data", list);
        return map;
    }

    /**
     * @param userId 用户id
     * @param start  从start开始
     * @param length 页大小
     * @param status 工单状态
     * @return
     * @creator wangr
     * @createtime 2017/11/2 0002 上午 9:28
     * @description: 分也查询工单列表
     * @modifier wangr
     * @modifytime 2017/12/1 0001 上午 11:13
     * @modifyDec: 修改报修人
     */
    public Map findMyOrderList(String userId, int start, int length,
                               List<String> status) {
        Map param = new HashMap();
        //设置查询条件
        param.put("userId", userId);
        param.put("status", status);
        param.put("processDefKey", EAMConsts.FAULT_ORDER_FLOWDEF);
        param.put("start", start);
        param.put("length", length);

        //分也查询我的工单代办 这个sql写在了 TaskRestService 中 注意 查询代办sql的改变及时
        //更改TaskRestService 中查询代办的sql
        logger.info("==== 分页获取我的工单列表 进入代办查询");
        Map map = taskRestService.todoList(param);
        if (!(boolean) map.get("success")) return map;
        List<String> pstids = (List<String>) map.get("data");


        if (null == pstids || pstids.size() == 0) {
            return map;
        }
        List<Task> taskList = (List<Task>) map.get("task");
        //根据获取的流程id查询我的工单
        param.put("list", pstids);
        param.put("orderBy", "a.create_time desc");
        List<OrderListJson> orderList = orderRestDao.findMyOrderList(param);

        //循环插入流程生成的任务id
        for (int i = 0; i < orderList.size(); i++) {
            if (SOURCE.equals(orderList.get(i).getSource())) {
                orderList.get(i).setBugLinkMan(UserUtils.get(orderList.get(i).getBugLinkMan()).getRealname());
            }
            if ("1".equals(orderList.get(i).getOrderStatus()) ||
                    "2".equals(orderList.get(i).getOrderStatus()) ||
                    "3".equals(orderList.get(i).getOrderStatus()) ||
                    "4".equals(orderList.get(i).getOrderStatus()) ||
                    "7".equals(orderList.get(i).getOrderStatus())) {

                if (2 == orderList.get(i).getOrderLevel()) {
                    orderList.get(i).setTimeName(getTimeName(orderList.get(i).getOrderLevel(),
                            orderList.get(i).getOrderStatus(), orderList.get(i).getOrderId(),
                            orderList.get(i).getExpectTime()));
                } else {
                    orderList.get(i).setTimeName(getTimeName(orderList.get(i).getOrderLevel(),
                            orderList.get(i).getOrderStatus(), orderList.get(i).getOrderId(),
                            orderList.get(i).getCreateTime()));
                }
            }
            for (int j = 0; j < taskList.size(); j++) {
                if (taskList.get(j).getProcessInstanceId().equals(orderList.get(i).getPstid())) {
                    orderList.get(i).setTaskDefKey(taskList.get(j).getTaskDefinitionKey());
                    orderList.get(i).setTaskId(taskList.get(j).getId());
                    logger.info("==== 流程id：" + orderList.get(i).getPstid());
                    logger.info("==== 流程任务id：" + taskList.get(j).getId());
                    logger.info("==== 流程任务key：" + taskList.get(j).getTaskDefinitionKey());
                    break;
                }
            }
        }
        map.remove("task");
        map.put("data", orderList);
        return map;
    }

    /**
     * @param map
     * @return
     * @creator wangr
     * @createtime 2017/10/25 0025 下午 7:25
     * @description: 我的工单详情
     * @modifier wangr
     * @modifytime 2017/12/1 0001 上午 11:13
     * @modifyDec: 修改报修人
     */
    public List<OrderDetailsJson> findMyOrderDetail(String userId, String orderId,
                                                    String devId, int flag) {

        //排序
        String orderBy = "CASE WHEN a.order_level = '1' THEN 0 ELSE 1 END , a.create_time desc";
        Map param = new HashMap();
        param.put("userId", userId);
        param.put("orderId", orderId);
        param.put("devId", devId);
        param.put("flag", flag);
        param.put("orderBy", orderBy);

        List<OrderDetailsJson> list = orderRestDao.findMyOrderDetail(param);
        for (OrderDetailsJson order : list) {
            Task task = taskRestService.getTask(order.getPstid());
            if (flag == 0 && task != null) {
                order.setTaskId(task.getId());
                order.setTaskDefKey(task.getTaskDefinitionKey());
            }
            if (SOURCE.equals(order.getSource())) {
                order.setBugLinkMan(UserUtils.get(order.getBugLinkMan()).getRealname());
            }
            if ("1".equals(order.getOrderStatus()) ||
                    "2".equals(order.getOrderStatus()) ||
                    "3".equals(order.getOrderStatus()) ||
                    "4".equals(order.getOrderStatus()) ||
                    "7".equals(order.getOrderStatus())) {

                if (2 == order.getOrderLevel()) {
                    order.setTimeName(getTimeName(order.getOrderLevel(),
                            order.getOrderStatus(), order.getOrderId(),
                            order.getExpectTime()));
                } else {
                    order.setTimeName(getTimeName(order.getOrderLevel(),
                            order.getOrderStatus(), order.getOrderId(),
                            order.getCreateTime()));
                }
            }

            if (StringUtils.isNotBlank(order.getOrderResult())) {
                //获取实际工序
                order.setTools(orderRestDao.findOrderToolFK(order.getOrderId()));
                order.setMaterials(orderRestDao.findOrderSparepartsFK(order.getOrderId()));
                order.setManHour(orderRestDao.findOrderPersonFK(order.getOrderId()));
                order.setOther(orderRestDao.findOrderOtherChargesFK(order.getOrderId()));
            } else {
                //获取计划工序
                order.setTools(orderRestDao.findOrderTool(order.getOrderId()));
                order.setMaterials(orderRestDao.findOrderSpareparts(order.getOrderId()));
                order.setManHour(orderRestDao.findOrderPerson(order.getOrderId()));
                order.setOther(orderRestDao.findOrderOtherCharges(order.getOrderId()));
            }
//获取反馈结果
            if ("1".equals(order.getOrderResult())) {
                order.setOrderResult("修好");
            } else if ("0".equals(order.getOrderResult())) {
                order.setOrderResult("未修好");
            }
            String processInstanceId = order.getPstid();
            //获取历史任务
            List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
            List<HistoricDetail> formProperties = historyService.createHistoricDetailQuery().processInstanceId(processInstanceId).formProperties().list();
            getHistoryList(order, activityInstances, formProperties);
        }


        return list;
    }


    /**
     * @param order
     * @param activityList
     * @param formPropertiesList
     * @return
     * @creator wangr
     * @createtime 2017/11/17 0017 下午 3:00
     * @description: 用于工单历史流程
     */
    public OrderDetailsJson getHistoryList(OrderDetailsJson order,
                                           List<HistoricActivityInstance> activityList,
                                           List<HistoricDetail> formPropertiesList) {
        //用于分装整理之后的历史流程数据
        List<History> histories = new ArrayList<>();
        //循环操作历史任务流程记录
        for (HistoricActivityInstance activity : activityList) {

            //startEvent   流程任务开始节点标志
            if (STARTEVENT.equals(activity.getActivityType())) {
                History history = new History();
                //流程节点名称 和 开始时间
                history.setActivityName(activity.getActivityName());
                history.setActivityTime(activity.getStartTime());
                //工单流程开始是报修  所以保修人和手机号码不能用 流程任务中记录的用户
                history.setUserName(order.getBugLinkMan());
                history.setPhone(order.getBugLinkManPhone());
                histories.add(history);
            } else if (USERTASK.equals(activity.getActivityType())) {
                History history = new History();
                //流程节点名称 和 开始时间
                history.setActivityName(activity.getActivityName());
                history.setActivityTime(activity.getStartTime());
                //将userId转换成用户
                User user = UserUtils.get(activity.getAssignee());
                if (user != null) {
                    //userTask  用户可以处理的任务流程节点
                    //根据任务流程中的任务处理人userId转换成用户名 手机
                    history.setUserName(user.getRealname());
                    history.setPhone(user.getLoginphone());
                }
                //循环操作 历史任务流程中表单提价的数据
                for (HistoricDetail detail : formPropertiesList) {
                    HistoricFormPropertyEntity historicFormPropertyEntity = (HistoricFormPropertyEntity) detail;
                    //多个表单提交数据记录对应一个 任务节点id  所以要根据任务id进行对比
                    if (activity.getTaskId().equals(detail.getTaskId())) {
                        String propertyId = historicFormPropertyEntity.getPropertyId();
                        String propertyValue = historicFormPropertyEntity.getPropertyValue();
                        //工单历史流程中 的转单 撤销  挂起 审批等的原因和建议
                        if ("reason".equals(propertyId)) {
                            history.setReason(propertyValue);
                        }
                        //工单历史流程中 审批是否同意
                        if ("result".equals(propertyId)) {

                            history.setResult("1".equals(propertyValue) ? "同意" : "拒绝");
                        }
                        //工单历史流程中 评价节点的评分
                        if ("score".equals(propertyId)) {
                            history.setScore(propertyValue);
                        }
                        //工单历史流程中 评价节点的【评价内容
                        if ("evaluate".equals(propertyId)) {
                            history.setEvaluate(propertyValue);
                        }
                        //工单历史流程中 转单节点的转给的某个用户
                        if ("switch_person".equals(propertyId)) {
                            User user1 = UserUtils.get(propertyValue);
                            history.setSwitchPerson(user1.getRealname());
                            history.setSwitchPhone(user1.getLoginphone());
                        }
                    }
                }
                histories.add(history);
            }

        }
        //放入工单历史流程列表中
        order.setHistory(histories);
        return order;
    }

    /**
     * @param orderId
     * @return
     * @creator wangr
     * @createtime 2017/10/26 0026 下午 5:29
     * @description: 用于反馈页面查询工序
     */
    public OrderFKJson getMyOrderFKDetails(String orderId, String userId) {

        //排序
        String orderBy = "CASE WHEN a.order_level = '1' THEN 0 ELSE 1 END , a.create_time desc";
        Map param = new HashMap();
        param.put("userId", userId);
        param.put("orderId", orderId);
        param.put("devId", "");
        param.put("flag", 0);
        param.put("orderBy", orderBy);

        List<OrderDetailsJson> list = orderRestDao.findMyOrderDetail(param);
        OrderFKJson fkJson = new OrderFKJson();
        if (list != null && list.size() > 0) {
            OrderDetailsJson order = list.get(0);
            if (StringUtils.isNotBlank(order.getOrderResult())) {
                fkJson.setTools(orderRestDao.findOrderToolFK(order.getOrderId()));
                fkJson.setMaterials(orderRestDao.findOrderSparepartsFK(order.getOrderId()));
                fkJson.setManHour(orderRestDao.findOrderPersonFK(order.getOrderId()));
                fkJson.setOther(orderRestDao.findOrderOtherChargesFK(order.getOrderId()));
            } else {
                fkJson.setTools(orderRestDao.findOrderTool(order.getOrderId()));
                fkJson.setMaterials(orderRestDao.findOrderSpareparts(order.getOrderId()));
                fkJson.setManHour(orderRestDao.findOrderPerson(order.getOrderId()));
                fkJson.setOther(orderRestDao.findOrderOtherCharges(order.getOrderId()));
            }
        }

        return fkJson;
    }

    /**
     * @param level
     * @param orderId
     * @param startTime
     * @return
     * @creator wangr
     * @createtime 2017/11/14 0014 下午 4:02
     * @description: 获取超时剩余
     */
    public String getTimeName(int level, String status, String orderId, Date startTime) {

        //工单超时计算
        //工单处理方式分为一般、紧急、预约三种
        //一般要求半小时到达，4小时完成反馈
        //紧急要求10分钟到达，30分钟完成反馈
        //预约要求在预约时间到达现场，预约时间后4小时完成反馈
        long n, l;
        long urgent = 0,
                dayTime = 24 * 60 * 60 * 1000,
                hourTime = 60 * 60 * 1000,
                minTime = 60 * 1000;
        String timeName = "";
        Map param = new HashMap();
        param.put("orderId", orderId);
        param.put("switchType", 2);
        param.put("approveStatus", 2);

        FaultSwitch faultSwitch = orderRestDao.getFaultSwitch(param);
        if (faultSwitch != null && faultSwitch.getRelieve_time() != null) {
            n = new Date().getTime() - faultSwitch.getRelieve_time().getTime();
        } else {
            n = new Date().getTime() - startTime.getTime();
        }

        //紧急
        if (0 == level) {
            if ("7".equals(status)) {
                urgent = 40 * 60 * 1000;
            } else {
                urgent = 10 * 60 * 1000;
            }
        } else if (1 == level) {
            if ("7".equals(status)) {
                urgent = 270 * 60 * 1000;
            } else {
                urgent = 30 * 60 * 1000;
            }
        } else {
            if ("7".equals(status)) {
                urgent = 240 * 60 * 1000;
            }
        }
        if (n <= urgent) {
            l = startTime.getTime() + urgent - new Date().getTime();

            long day = l / dayTime;
            if (day == 0) {//一天以内，以分钟或者小时显示
                long hour = (l / hourTime - day * 24);
                if (hour == 0) {
                    long min = ((l / (minTime)) - day * 24 * 60 - hour * 60);
                    timeName = "剩余0时" + min + "分";
                } else {
                    long min = ((l / (minTime)) - day * 24 * 60 - hour * 60);
                    timeName = "剩余" + hour + "时" + min + "分";
                }

            } else {
                long hour = (l / (hourTime) - day * 24);
                timeName = "剩余" + day + "天" + hour + "时";
            }
            return timeName;
        } else {

            l = new Date().getTime() - startTime.getTime() - urgent;
            long day = l / dayTime;
            if (day == 0) {//一天以内，以分钟或者小时显示
                long hour = (l / hourTime - day * 24);
                if (hour == 0) {
                    long min = ((l / minTime) - (day * 24 * 60) - (hour * 60));
                    if (min == 0) {
                        timeName = "刚刚逾期";
                    } else {
                        timeName = "超时0时" + min + "分";
                    }
                } else {
                    long min = ((l / minTime) - day * 24 * 60 - hour * 60);
                    timeName = "超时" + hour + "时" + min + "分";
                }

            } else {
                long hour = (l / hourTime - day * 24);
                timeName = "超时" + day + "天" + hour + "时";

            }
            return timeName;
        }

    }

}
