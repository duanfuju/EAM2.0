package com.tiansu.eam.interfaces.inspection.service;

import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.interfaces.act.service.TaskRestService;
import com.tiansu.eam.interfaces.common.History;
import com.tiansu.eam.interfaces.inspection.dao.InspectionRestDao;
import com.tiansu.eam.interfaces.inspection.entity.*;
import com.tiansu.eam.interfaces.order.entity.OrderDetailsJson;
import com.tiansu.eam.modules.device.entity.DevLocation;
import com.tiansu.eam.modules.device.entity.Device;
import com.tiansu.eam.modules.device.service.EamDeviceService;
import com.tiansu.eam.modules.faultOrder.entity.FaultOrder;
import com.tiansu.eam.modules.faultOrder.entity.OrderStatusEnum;
import com.tiansu.eam.modules.inspection.entity.InspectionTaskSwitch;
import com.tiansu.eam.modules.inspection.entity.InspectiontaskFeedback;
import com.tiansu.eam.modules.sys.entity.CodeEnum;
import com.tiansu.eam.modules.sys.entity.Dept;
import com.tiansu.eam.modules.sys.entity.User;
import com.tiansu.eam.modules.sys.utils.SequenceUtils;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.impl.persistence.entity.HistoricFormPropertyEntity;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 * @author wangr
 * @description 巡检对外流程业务类
 * @create 2017-10-30 上午 10:39
 */
@Service
@Transactional(readOnly = true)
public class InspectionRestService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    public static final String USERTASK = "userTask";


    @Autowired
    private HistoryService historyService;

    @Autowired
    private InspectionRestDao inspectionDao;

    @Autowired
    private TaskRestService taskRestService;

    @Autowired
    private EamDeviceService deviceService;

    /**
     * @param userId
     * @param start
     * @param length
     * @param status
     * @param isoverdue
     * @return
     * @creator wangr
     * @createtime 2017/10/30 0030 上午 11:20
     * @description: 查询我的巡检任务
     * @modifier wangr
     * @modifytime 2017/11/22 0022 上午 9:16
     * @modifyDec: 新增日志信息
     */
    public Map findMyInspection(String userId, int start, int length,
                                List<String> status, int isoverdue) {
        Map param = new HashMap();

        param.put("userId", userId);
        param.put("status", status);
        param.put("isoverdue", isoverdue);
        param.put("processDefKey", EAMConsts.INSPECTION_FLOWDEF);
        param.put("start", start);
        param.put("length", length);
        logger.info("==== 查询我的巡检任务 进入代办查询");
        Map map = taskRestService.todoList(param);
        if (!(boolean) map.get("success")) return map;

        List<String> pstids = (List<String>) map.get("data");

        if (null == pstids || pstids.size() == 0) {
            return map;
        }
        List<Task> taskList = (List<Task>) map.get("task");
        param.put("list", pstids);
        param.put("orderBy", "a.task_time_plan_begin desc");
        List<InspectionListJson> inspectionList = inspectionDao.findMyInspection(param);

        for (int i = 0; i < inspectionList.size(); i++) {

            if ("0".equals(inspectionList.get(i).getStatus()) ||
                    "1".equals(inspectionList.get(i).getStatus())) {

                inspectionList.get(i).setTimeName(getTimeName(inspectionList.get(i).getInspectionId(),
                        inspectionList.get(i).getFinishTime(), inspectionList.get(i).getPlanFinishTime()));

            }
            for (int j = 0; j < taskList.size(); j++) {
                if (taskList.get(j).getProcessInstanceId().equals(inspectionList.get(i).getPstid())) {
                    inspectionList.get(i).setTaskId(taskList.get(j).getId());
                    inspectionList.get(i).setTaskDefKey(taskList.get(j).getTaskDefinitionKey());
                    logger.info("==== 流程id：" + inspectionList.get(i).getPstid());
                    logger.info("==== 流程任务id：" + taskList.get(j).getId());
                    logger.info("==== 流程任务key：" + taskList.get(j).getTaskDefinitionKey());
                    break;
                }
            }
            inspectionList.get(i).setRouteCycle(cycle(inspectionList.get(i).getPeriod(),
                    inspectionList.get(i).getRouteCycle()));
        }
        map.remove("task");
        map.put("data", inspectionList);
        return map;
    }

    /**
     * @param pstid 流程id
     * @return
     * @creator wangr
     * @createtime 2017/10/31 0031 下午 2:58
     * @description: 根据巡检任务流程id获取区域信息
     * @modifier wangr
     * @modifytime 2017/12/4 下午 3:54
     * @modifyDec: 修改巡检排序
     */
    public InspectionDetailJson getInspectionDetail(String pstid) {

        InspectionDetailJson inspection = new InspectionDetailJson();
        logger.info("==== 巡检详情查询 开始");
        inspection = inspectionDao.getInspectionById(pstid);
        logger.info("==== 巡检详情查询 结束");
        inspection.setArea(getArea(pstid, inspection.getInspectionId()));
        inspection.setRouteCycle(cycle(inspection.getPeriod(), inspection.getRouteCycle()));
        if ("2".equals(inspection.getStatus())) {
            logger.info("==== 巡检详情实际工器具等查询 开始");
            String inspectionId = inspection.getInspectionId();
            inspection.setProcedure(inspectionDao.getInspectionProcedure(inspectionId));
            inspection.setSafe(inspectionDao.getInspectionSafety(inspectionId));
            inspection.setTools(inspectionDao.getInspectionTools(inspectionId));
            inspection.setSpareparts(inspectionDao.getInspectionSpareparts(inspectionId));
            inspection.setPerson(inspectionDao.getInspectionPerson(inspectionId));
            inspection.setOthers(inspectionDao.getInspectionOthers(inspectionId));
            logger.info("==== 巡检详情实际工器具等查询 结束");
        } else {
            String route_id = inspection.getRouteId();
            logger.info("==== 巡检详情计划工器具等查询 开始");
            inspection.setProcedure(inspectionDao.getRouteProcedure(route_id));
            inspection.setSafe(inspectionDao.getRouteSafety(route_id));
            inspection.setTools(inspectionDao.getRouteTools(route_id));
            inspection.setSpareparts(inspectionDao.getRouteSpareparts(route_id));
            inspection.setPerson(inspectionDao.getRoutePerson(route_id));
            inspection.setOthers(inspectionDao.getRouteOthers(route_id));
            logger.info("==== 巡检详情计划工器具等查询 开始");
            Task task = taskRestService.getTask(pstid);

            if (task != null) {
                inspection.setTaskDefKey(task.getTaskDefinitionKey() != null ? task.getTaskDefinitionKey() : "");
                inspection.setTaskId(task.getId());
                logger.info("==== taskDefKey:" + task.getTaskDefinitionKey());
                logger.info("==== taskId:" + task.getId());
            }
        }
        if ("0".equals(inspection.getStatus()) ||
                "1".equals(inspection.getStatus())) {

            inspection.setTimeName(getTimeName(inspection.getInspectionId(),
                    inspection.getFinishTime(), inspection.getPlanFinishTime()));

        }
        List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(inspection.getPstid()).list();
        List<HistoricDetail> formProperties = historyService.createHistoricDetailQuery().processInstanceId(inspection.getPstid()).formProperties().list();
        getHistoryList(inspection, activityInstances, formProperties);
        return inspection;
    }


    /**
     * @param inspection
     * @param activityList
     * @param formPropertiesList
     * @return
     * @creator wangr
     * @createtime 2017/11/17 0017 下午 3:00
     * @description: 用于巡检历史流程
     * @modifier wangr
     * @modifytime 2017/11/22 0022 上午 9:38
     * @modifyDec: 新增日志消息
     */
    public InspectionDetailJson getHistoryList(InspectionDetailJson inspection,
                                               List<HistoricActivityInstance> activityList,
                                               List<HistoricDetail> formPropertiesList) {
        logger.info("==== 巡检历史流程 开始");
        //用于分装整理之后的历史流程数据
        List<History> histories = new ArrayList<>();
        //循环操作历史任务流程记录
        for (HistoricActivityInstance activity : activityList) {
            //userTask  用户可以处理的任务流程节点
            if (USERTASK.equals(activity.getActivityType())) {
                History history = new History();
                //流程节点名称 和 开始时间
                history.setActivityName(activity.getActivityName());
                history.setActivityTime(activity.getStartTime());
                //将userId转换成用户
                User user = UserUtils.get(activity.getAssignee());
                if (user != null) {
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
                        //巡检历史流程中 的转单 撤销  挂起 审批等的原因和建议
                        if ("requestReason".equals(propertyId) ||
                                "approveReason".equals(propertyId)) {
                            history.setReason(propertyValue);
                        }
                        //巡检历史流程中 审批是否同意
                        if ("deptLeaderApproved".equals(propertyId)) {

                            history.setResult("true".equals(propertyValue) ? "同意" : "拒绝");
                        }
                        //巡检历史流程中 转单节点的转给的某个用户
                        if ("switch_person".equals(propertyId)) {
                            User user1 = UserUtils.get(propertyValue);
                            if (user1 != null && user1.getRealname() != null && user1.getLoginphone() != null) {
                                history.setSwitchPerson(user1.getRealname());
                                history.setSwitchPhone(user1.getLoginphone());
                            }
                        }
                    }
                }
                histories.add(history);
            }
        }
        //放入工单历史流程列表中
        inspection.setHistory(histories);
        logger.info("==== 巡检历史流程 结束");
        return inspection;
    }

    /**
     * @param pstid
     * @return
     * @creator wangr
     * @createtime 2017/10/31 0031 下午 5:49
     * @description: 获取巡检区域下设备
     * @modifier wangr
     * @modifytime 2017/11/22 0022 上午 9:31
     * @modifyDec: 新增日志消息
     */
    public List<AreaDetailJson> getArea(String pstid, String inspectioId) {
        logger.info("==== 巡检区域查询 开始");
        logger.info("==== pstid：" + pstid);
        logger.info("==== inspectioId：" + inspectioId);
        List<AreaDetailJson> areaList = inspectionDao.getAreaByTaskPstid(pstid);
        logger.info("==== 巡检区域查询 结束");
        for (int i = 0; i < areaList.size(); i++) {

            String areaId = areaList.get(i).getAreaId();
            logger.info("==== 巡检区域下的巡检项查询 开始");
            logger.info("==== areaId:" + areaId);
            List<SubjectDetailJson> subjectList = inspectionDao.getSubjectDeviceByAreaId(areaId);
            logger.info("==== 巡检区域下的巡检项查询 结束");
            for (int j = 0; j < subjectList.size(); j++) {
                String devId = subjectList.get(j).getDevId();
                Map param = new HashMap();
                param.put("inpectionId", inspectioId);
                param.put("areaId", areaId);
                param.put("devId", devId);

                logger.info("==== 获取巡检区域下设备是否进行反馈 开始");
                int result = inspectionDao.getSubjectIsFeebBack(param);
                logger.info("==== result:" + result);
                logger.info("==== 获取巡检区域下设备是否进行反馈 结束");
                subjectList.get(j).setIsInspection(result == 0 ? 0 : 1);
            }
            areaList.get(i).setSubject(subjectList);
        }

        return areaList;
    }

    /**
     * @param map
     * @return
     * @creator wangr
     * @createtime 2017/11/17 0017 下午 3:21
     * @description: 获取反馈的工序
     * @modifier wangr
     * @modifytime 2017/11/22 0022 上午 9:39
     * @modifyDec: 新增日志消息
     */
    public InspectionFKDetailJson getInspectionFKDetail(Map map) {
        logger.info("==== 获取反馈的工序 开始");
        InspectionFKDetailJson inspectionFKDetailJson = inspectionDao.getInspectionFKDetail(map);
        List<SubjectFKDetailJson> list = inspectionDao.getSubjectFKDetail(map);
        inspectionFKDetailJson.setSubject(list);
        if (list != null && list.size() > 0) {
            inspectionFKDetailJson.setImgUrl(list.get(0).getCheck_picture());
            inspectionFKDetailJson.setVoideUrl(list.get(0).getCheck_video());
        }
        logger.info("==== 获取反馈的工序 结束");
        return inspectionFKDetailJson;
    }


    /**
     * @param period
     * @param periodDetail
     * @return
     * @creator wangr
     * @createtime 2017/10/30 0030 下午 4:07
     * @description: 拼接周期详情
     */
    public static String cycle(String period, String periodDetail) {
        String reult = "";
        String str[] = periodDetail.split(",");
        if ("0".equals(period)) {
            String variable[] = str[0].split("-");
            reult += "每" + variable[0] + "天";
            if (variable.length > 1) {
                reult += "的" + variable[1];
            }
        } else if ("1".equals(period)) {
            reult += "每" + str[0] + "周";
            String variable[] = str[1].split("-");
            reult += "的" + week(variable[0]);
            if (variable.length > 1) {
                reult += "的" + variable[1];
            }
        } else if ("2".equals(period)) {
            reult += "每" + str[0] + "月的第" + str[1] + "个";
            String variable[] = str[2].split("-");
            reult += week(variable[0]);
            if (variable.length > 1) {
                reult += "的" + variable[1];
            }
        } else if ("3".equals(period)) {
            reult += "每" + str[0] + "季度的第" + str[1] + "月的第" + str[2] + "个";
            String variable[] = str[3].split("-");
            reult += week(variable[0]);
            if (variable.length > 1) {
                reult += "的" + variable[1];
            }
        } else if ("4".equals(period)) {
            reult += "每" + str[0] + "年" + str[1] + "月的第" + str[2] + "个";
            String variable[] = str[4].split("-");
            reult += week(variable[0]);
            if (variable.length > 1) {
                reult += "的" + variable[1];
            }
        }
        return reult;
    }

    /**
     * @param index
     * @return
     * @creator wangr
     * @createtime 2017/10/30 0030 下午 4:07
     * @description: 获取周
     */
    public static String week(String index) {
        String week = "";
        switch (index) {
            case "1":
                week = "周一";
                break;
            case "2":
                week = "周二";
                break;
            case "3":
                week = "周三";
                break;
            case "4":
                week = "周四";
                break;
            case "5":
                week = "周五";
                break;
            case "6":
                week = "周六";
                break;
            case "7":
                week = "周日";
                break;
            default:
                week = "";
        }
        return week;
    }

    /**
     * @param inspId
     * @param finishTime
     * @param planFinishTime
     * @return
     * @creator wangr
     * @createtime 2017/11/24 下午 3:40
     * @description: 巡检超时计算
     */
    public String getTimeName(String inspId, Date finishTime, Date planFinishTime) {
        String timeName = "";

        long l;
        long nh = 1000 * 60 * 60;
        long nHour = 0;
        InspectionTaskSwitch taskSwitch = inspectionDao.getFaultSwitch(inspId);

        if (null != finishTime) {
            if (taskSwitch != null && taskSwitch.getRelieve_time() != null) {
                long diff = taskSwitch.getRelieve_time().getTime() - taskSwitch.getApprove_time().getTime();
                nHour = diff / nh;
                l = finishTime.getTime() - (planFinishTime.getTime() + nHour);
            } else {
                l = finishTime.getTime() - planFinishTime.getTime();
            }

        } else {
            if (taskSwitch != null && taskSwitch.getRelieve_time() != null) {
                long diff = taskSwitch.getRelieve_time().getTime() - taskSwitch.getApprove_time().getTime();
                nHour = diff / nh;
                l = new Date().getTime() - (planFinishTime.getTime() + nHour);
            } else {
                l = new Date().getTime() - planFinishTime.getTime();
            }
        }
        if (l < 1) {
            if (taskSwitch != null && taskSwitch.getRelieve_time() != null) {
                long diff = taskSwitch.getRelieve_time().getTime() - taskSwitch.getApprove_time().getTime();
                nHour = diff / nh;
                l = planFinishTime.getTime() + nHour - new Date().getTime();
            } else {
                l = planFinishTime.getTime() - new Date().getTime();
            }

            long day = l / (24 * 60 * 60 * 1000);
            if (day == 0) {//一天以内，以分钟或者小时显示
                long hour = (l / (60 * 60 * 1000) - day * 24);
                if (hour == 0) {
                    long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
                    timeName = "剩余0时" + min + "分";
                } else {
                    long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
                    timeName = "剩余" + hour + "时" + min + "分";
                }

            } else {
                long hour = (l / (60 * 60 * 1000) - day * 24);
                timeName = "剩余" + day + "天" + hour + "时";
            }
            return timeName;
        }
        long day = l / (24 * 60 * 60 * 1000);
        if (day == 0) {//一天以内，以分钟或者小时显示
            long hour = (l / (60 * 60 * 1000) - day * 24);
            if (hour == 0) {
                long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
                if (min == 0) {
                    timeName = "刚刚逾期";
                } else {
                    timeName = "超时0时" + min + "分";
                }
            } else {
                long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
                timeName = "超时" + hour + "时" + min + "分";
            }

        } else {
            long hour = (l / (60 * 60 * 1000) - day * 24);
            timeName = "超时" + day + "天" + hour + "时";

        }


        return timeName;

    }

    /**
     * @param userId
     * @param userMap
     * @param inspectionSubject
     * @param inspectiontaskFeedback
     * @return
     * @creator wangr
     * @createtime 2017/11/24 0024 下午 3:41
     * @description: APP组装拼接报修单信息
     * @modifier wangr
     * @modifytime 2017/12/5 下午 2:50
     * @modifyDec: 添加工单创建人 默认处理方式和保修时间
     */
    public FaultOrder getFaultOrder(String userId, Map userMap, Map inspectionSubject, InspectiontaskFeedback inspectiontaskFeedback) {
        FaultOrder faultOrder = new FaultOrder();
        User user = UserUtils.get(userId);
        faultOrder.setCreateBy(user);
        faultOrder.setCreateDate(new Date());
        faultOrder.setUpdateBy(user);
        faultOrder.setUpdateDate(new Date());
        faultOrder.setOrder_code(SequenceUtils.getBySeqType(CodeEnum.FAULT_ORDER));
        Device device = new Device();
        String id = inspectionSubject.get("dev_id").toString();  // 设备id
        device.setId(id);  //报修设备 -- 设备id
        device.setDev_location(deviceService.get(id).getDev_location());  //  报修位置-设备位置
        faultOrder.setOrder_device(device);   // 报修的设备相关信息
        DevLocation devLocation = new DevLocation();
        devLocation.setId(deviceService.get(id).getDev_location());
        faultOrder.setNotifier_loc(devLocation);   // 报修位置
        faultOrder.setOrder_status(OrderStatusEnum.PENDING_DISP.value());   // 保修单状态
        faultOrder.setOrder_source(EAMConsts.ORDER_TYPE_INSPECTION);       // 保修单来源
        faultOrder.setNotifier_source(EAMConsts.ORDER_SOURCE_APP);  // 报修来源 APP
        faultOrder.setNotifier_tel(userMap.get("b_loginphone").toString());  // 报修电话
        faultOrder.setNotifier_appearance(inspectiontaskFeedback.getAppearance());   // 报修的故障现象
        faultOrder.setNotifier(userId);
        String userdeptno = userMap.get("b_userdeptno").toString();
        Dept dept = new Dept();
        dept.setDeptno(userdeptno);
        faultOrder.setNotifier_dept(dept);          // 报修部门（报修人部门）
        faultOrder.setNotifier_no(userMap.get("b_userjobno").toString());   //报修人工号
        faultOrder.setOrder_source_id(inspectiontaskFeedback.getId());   // 报修id--巡检任务id
        faultOrder.setOrder_expect_time(inspectiontaskFeedback.getExpect_time());  // 期望解决时间
        faultOrder.setOrder_level("1");//紧急程度默认为1  一般
        return faultOrder;
    }
}
