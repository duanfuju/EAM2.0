package com.tiansu.eam.interfaces.inspection.controller;

import com.alibaba.fastjson.JSON;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.interfaces.common.InfoConstantEnum;
import com.tiansu.eam.interfaces.inspection.service.InspectionRestService;
import com.tiansu.eam.interfaces.resulthelper.APPResponseBody;
import com.tiansu.eam.modules.act.service.EamProcessService;
import com.tiansu.eam.modules.employee.service.EamUserExtService;
import com.tiansu.eam.modules.faultOrder.controller.FaultOrderController;
import com.tiansu.eam.modules.faultOrder.entity.FaultOrder;
import com.tiansu.eam.modules.faultOrder.service.FaultOrderService;
import com.tiansu.eam.modules.inspection.controller.InspectionTaskController;
import com.tiansu.eam.modules.inspection.entity.*;
import com.tiansu.eam.modules.inspection.service.InspectionSubjectService;
import com.tiansu.eam.modules.inspection.service.InspectionTaskService;
import com.tiansu.eam.modules.material.service.MaterialInfoService;
import com.tiansu.eam.modules.sys.entity.User;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.activiti.engine.FormService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteDateUseDateFormat;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue;

/**
 * @author wangr
 * @description 巡检对外接口
 * @create 2017-10-30 上午 10:36
 */

@Path("/{tenant}/inspection")
@Controller
public class InspectionRestController extends BaseController {

    private static InspectionRestService inspectionService = SpringContextHolder.getBean(InspectionRestService.class);

    private static InspectionTaskService inspectionTaskService = SpringContextHolder.getBean(InspectionTaskService.class);


    private static TaskService taskService = SpringContextHolder.getBean(TaskService.class);

    private static FormService formService = SpringContextHolder.getBean(FormService.class);

    private static MaterialInfoService materialInfoService = SpringContextHolder.getBean(MaterialInfoService.class);  // 物料service

    private static ActTaskService actTaskService = SpringContextHolder.getBean(ActTaskService.class);

    private static EamUserExtService eamUserExtService = SpringContextHolder.getBean(EamUserExtService.class);

    private static InspectionSubjectService inspectionSubjectService = SpringContextHolder.getBean(InspectionSubjectService.class);

    private static EamProcessService eamProcessService = SpringContextHolder.getBean(EamProcessService.class);    // 工作进程service

    private static FaultOrderService faultOrderService = SpringContextHolder.getBean(FaultOrderService.class);

    /**
     * @param userId
     * @param start
     * @param length
     * @param response
     * @return
     * @creator wangr
     * @createtime 2017/10/30 0030 上午 10:41
     * @description: 获取我的巡检任务
     * @modifier wangr
     * @modifytime 2017/11/22 0022 上午 8:53
     * @modifyDec: 新增日志信息
     */
    @GET
    @Path("/getMyInspection")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMyInspection(@QueryParam("userId") String userId,
                                  @QueryParam("isoverdue") int isoverdue,
                                  @QueryParam("start") int start,
                                  @QueryParam("length") int length,
                                  @QueryParam("status") List<String> status,
                                  @Context HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        logger.info("==== 获取我的巡检任务 开始 =====");
        APPResponseBody result = new APPResponseBody();
        try {
            logger.info("==== 获取我的巡检任务 参数 开始");
            logger.info("==== userId:" + userId);
            logger.info("==== start:" + start);
            logger.info("==== length:" + length);
            logger.info("==== isoverdue:" + isoverdue);
            if (status != null && status.size() > 0) {
                for (String statue : status) {
                    logger.info("==== status:" + statue);
                }
            }
            logger.info("==== 获取我的巡检任务 参数 结束");
            Map map = inspectionService.findMyInspection(userId, start,
                    length, status, isoverdue);
            if (!(boolean) map.get("success")) {
                logger.info("==== 获取我的巡检任务 代办查询失败");
                result.setStatus(InfoConstantEnum.FAIL.getCode());
                result.setErrorcode(InfoConstantEnum.QUERY_FAIL.getCode());
                result.setErrmsg(InfoConstantEnum.QUERY_FAIL.getMessage());
                logger.info("==== 获取我的巡检任务 结束 =====");
                return JSON.toJSONString(result);
            }
            result.setData(map);
        } catch (Exception e) {
            logger.error("==== 查询我的巡检列表出错：", e);
            result.setStatus(InfoConstantEnum.FAIL.getCode());
            result.setErrorcode(InfoConstantEnum.QUERY_FAIL.getCode());
            result.setErrmsg(InfoConstantEnum.QUERY_FAIL.getMessage());
        }
        logger.info("==== 获取我的巡检任务 结束 =====");
        return JSON.toJSONString(result, WriteMapNullValue, WriteDateUseDateFormat);

    }

    /**
     * @param pstId    巡检任务流程id
     * @param response
     * @return
     * @creator wangr
     * @createtime 2017/11/2 0002 下午 5:49
     * @description: 根据流程id获取巡检任务详情
     * @modifier wangr
     * @modifytime 2017/11/22 0022 上午 9:28
     * @modifyDec: 新增日志消息
     */
    @Path("/getInspectionDetail")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInspectionDetail(@QueryParam("pstId") String pstId,
                                      @Context HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        logger.info("==== 根据流程id获取巡检任务详情 开始 ====");
        logger.info("==== pstId:" + pstId);
        APPResponseBody result = new APPResponseBody();
        if (StringUtils.isBlank(pstId)) {
            logger.info("==== pstId是空！");
            result.setStatus(InfoConstantEnum.FAIL.getCode());
            result.setErrorcode(InfoConstantEnum.PARAM.getCode());
            result.setErrmsg(InfoConstantEnum.PARAM.getMessage());
            logger.info("==== 根据流程id获取巡检任务详情 结束 ====");
            return JSON.toJSONString(result);
        }
        try {
            result.setData(inspectionService.getInspectionDetail(pstId));
        } catch (Exception e) {
            logger.error("==== 查询我的巡检详情出错：", e);
            result.setStatus(InfoConstantEnum.FAIL.getCode());
            result.setErrorcode(InfoConstantEnum.QUERY_FAIL.getCode());
            result.setErrmsg(InfoConstantEnum.QUERY_FAIL.getMessage());
        }
        logger.info("==== 根据流程id获取巡检任务详情 结束 ====");
        return JSON.toJSONString(result, WriteMapNullValue, WriteDateUseDateFormat);
    }

    /**
     * @param inspectionId 巡检任务id
     * @param devId        设备id
     * @param areaId
     * @param userId
     * @param response
     * @return
     * @creator wangr
     * @createtime 2017/11/2 下午 5:50
     * @description: 获取巡检项详情
     * @modifier wangr
     * @modifytime 2017/11/22 上午 9:41
     * @modifyDec: 新增日志消息
     */
    @Path("/getInspectionSubjectDetail")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInspectionSubjectDetail(@QueryParam("inspectionId") String inspectionId,
                                             @QueryParam("devId") String devId,
                                             @QueryParam("areaId") String areaId,
                                             @QueryParam("userId") String userId,
                                             @Context HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        logger.info("==== 获取巡检项详情 开始 ====");
        logger.info("==== inspectionId：" + inspectionId);
        logger.info("==== devId：" + devId);
        logger.info("==== areaId：" + areaId);
        logger.info("==== userId：" + userId);
        APPResponseBody result = new APPResponseBody();
        if (StringUtils.isBlank(inspectionId) || StringUtils.isBlank(devId)) {
            result.setStatus(InfoConstantEnum.FAIL.getCode());
            result.setErrorcode(InfoConstantEnum.PARAM.getCode());
            result.setErrmsg(InfoConstantEnum.PARAM.getMessage());
            logger.info("==== 参数是空");
            logger.info("==== 获取巡检项详情 结束 ====");
            return JSON.toJSONString(result);
        }
        try {
            Map map = new HashMap();
            map.put("inspectionId", inspectionId);
            map.put("devId", devId);
            map.put("areaId", areaId);
            result.setData(inspectionService.getInspectionFKDetail(map));
        } catch (Exception e) {
            logger.error("==== 查询我的巡检项详情出错：", e);
            result.setStatus(InfoConstantEnum.FAIL.getCode());
            result.setErrorcode(InfoConstantEnum.QUERY_FAIL.getCode());
            result.setErrmsg(InfoConstantEnum.QUERY_FAIL.getMessage());
        }
        logger.info("==== 获取巡检项详情 结束 ====");
        return JSON.toJSONString(result, WriteMapNullValue, WriteDateUseDateFormat);
    }


    /**
     * @param param    反馈参数
     * @param userId   用户id
     * @param imgUrl
     * @param videoUrl
     * @param response
     * @return
     * @creator wangr
     * @createtime 2017/11/2 0002 下午 6:52
     * @description: 反馈页面保存巡检项项反馈信息，只保存巡检项的反馈信息，
     * 其他的备品备件/工器具/人员工时/其他事项等数据要在最终巡检任务反馈提交的时候保存
     * @modifier wangr
     * @modifytime 2017/11/22 0022 上午 9:45
     * @modifyDec: 新增日志消息
     */
    @Path(value = "subjectFeedBack")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String subjectFeedBack(@FormParam("param") String param,
                                  @FormParam("userId") String userId,
                                  @FormParam("imgUrl") String imgUrl,
                                  @FormParam("videoUrl") String videoUrl,
                                  @Context HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        logger.info("==== 保存巡检项的反馈信息 开始 ====");
        logger.info("==== param:" + param);
        logger.info("==== userId:" + userId);
        logger.info("==== imgUrl:" + imgUrl);
        logger.info("==== videoUrl:" + videoUrl);
        APPResponseBody result = new APPResponseBody();
        try {
            TaskFeekBack taskFeekBack = JSON.parseObject(param, TaskFeekBack.class);
            if (taskFeekBack == null) {
                result.setStatus(InfoConstantEnum.FAIL.getCode());
                result.setErrorcode(InfoConstantEnum.PARAM.getCode());
                result.setErrmsg(InfoConstantEnum.PARAM.getMessage());
                logger.info("==== 参数是空");
                logger.info("==== 保存巡检项的反馈信息 结束 ====");
                return JSON.toJSONString(result);
            }

            List<InspectiontaskFeedback> inspectiontaskFeedbackList = taskFeekBack.getInspectiontaskFeedbackList();
            if (inspectiontaskFeedbackList == null || inspectiontaskFeedbackList.size() == 0) {
                result.setStatus(InfoConstantEnum.FAIL.getCode());
                result.setErrorcode(InfoConstantEnum.PARAM.getCode());
                result.setErrmsg(InfoConstantEnum.PARAM.getMessage());
                logger.info("==== List<InspectiontaskFeedback>无值！");
                logger.info("==== 保存巡检项的反馈信息 结束 ====");
                return JSON.toJSONString(result);
            } else {
                String inspectiontask_id = taskFeekBack.getInspectiontask_id();

                Map param1 = new HashMap();
                param1.put("inspectiontask_id", inspectiontask_id);
                List<InspectiontaskFeedback> feedBackList = inspectionTaskService.getFeedBackInfoByTaskId(param1);
                for (int j = 0; j < inspectiontaskFeedbackList.size(); j++) {
                    InspectiontaskFeedback inspectiontaskFeedback = inspectiontaskFeedbackList.get(j);
                    if (feedBackList != null && feedBackList.size() > 0) {
                        for (int i = 0; i < feedBackList.size(); i++) {
                            if (feedBackList.get(i).getSubject_id().equals(inspectiontaskFeedback.getSubject_id())) {
                                Map para1 = new HashMap();
                                para1.put("inspectiontask_id", inspectiontask_id);
                                para1.put("subject_id", inspectiontaskFeedback.getSubject_id());
                                logger.info("==== 删除原保存的反馈项 开始");
                                inspectionTaskService.deleteFeedBack(para1);
                                logger.info("==== 删除原保存的反馈项 结束");
                            }
                        }
                    }
                    Date date = new Date();
                    inspectiontaskFeedback.setCheck_time(date);
                    inspectiontaskFeedback.setCheck_picture(imgUrl);
                    inspectiontaskFeedback.setCheck_video(videoUrl);
                    inspectiontaskFeedback.setId_key(IdGen.uuid());
                    inspectiontaskFeedback.setId(IdGen.uuid());

                    User user = UserUtils.get(userId);
                    inspectiontaskFeedback.setCreateBy(user);
                    inspectiontaskFeedback.setUpdateBy(user);
                    inspectiontaskFeedback.setCreateDate(date);
                    inspectiontaskFeedback.setUpdateDate(date);
                }


                inspectionTaskService.insertFeedBackList(inspectiontaskFeedbackList);
                logger.info("==== 新增巡检项反馈信息成功");
            }
        } catch (Exception e) {
            logger.error("==== 巡检项保存出错：", e);
            result.setStatus(InfoConstantEnum.FAIL.getCode());
        }
        logger.info("==== 保存巡检项的反馈信息 结束 ====");
        return JSON.toJSONString(result, WriteMapNullValue, WriteDateUseDateFormat);
    }


    /**
     * @param param
     * @param userId
     * @param taskId
     * @param pstId
     * @param response
     * @return
     * @creator wangr
     * @createtime 2017/11/2 上午 9:49
     * @description: 巡检反馈
     * @modifier wangr
     * @modifytime 2017/11/24 下午 3:45
     * @modifyDec: 修改巡检反馈 增加异常 故障反馈生成故障工单
     */
    @Path(value = "inspectionFeedBack")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String inspectionFeedBack(@FormParam("param") String param,
                                     @FormParam("userId") String userId,
                                     @FormParam("taskId") String taskId,
                                     @FormParam("pstId") String pstId,
                                     @Context HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        logger.info("==== 巡检反馈 开始 ====");
        logger.info("==== param:" + param);
        logger.info("==== userId:" + userId);
        logger.info("==== taskId:" + taskId);
        logger.info("==== pstId:" + pstId);
        APPResponseBody result = new APPResponseBody();
        try {
            //签收任务
            actTaskService.claim(taskId, userId);
            logger.info("==== 签收成功");

            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            // 权限检查-任务的办理人和当前人不一致不能完成任务
            if (!userId.equals(task.getAssignee())) {
                result.setStatus(InfoConstantEnum.FAIL.getCode());
                result.setErrorcode(InfoConstantEnum.ACTIVItY_PERMISSIONS_FAIL.getCode());
                result.setErrmsg(InfoConstantEnum.ACTIVItY_PERMISSIONS_FAIL.getMessage());
                logger.info("==== 任务的办理人和当前人不一致不能完成任务,实际：" + task.getAssignee());
                logger.info("==== 巡检反馈 结束 ====");
                return JSON.toJSONString(result);
            }

            TaskFeekBack taskFeekBack = JSON.parseObject(param, TaskFeekBack.class);
            if (taskFeekBack == null) {
                result.setStatus(InfoConstantEnum.FAIL.getCode());
                result.setErrorcode(InfoConstantEnum.PARAM.getCode());
                result.setErrmsg(InfoConstantEnum.PARAM.getMessage());
                logger.info("==== taskFeekBack是空");
                logger.info("==== 巡检反馈 结束 ====");
                return JSON.toJSONString(result);
            }
            String inspectiontask_id = taskFeekBack.getInspectiontask_id();
            Map inspIdMap = new HashMap();
            inspIdMap.put("inspectiontask_id", inspectiontask_id);
            //巡检任务反馈实体类
            List<InspectiontaskFeedback> inspectiontaskFeedbackList = inspectionTaskService.getFeedBackInfoByTaskId(inspIdMap);
            if (inspectiontaskFeedbackList != null && inspectiontaskFeedbackList.size() != 0) {
                Map userMap = eamUserExtService.findByLoginName(userId).get(0);

                for (int i = 0; i < inspectiontaskFeedbackList.size(); i++) {
                    InspectiontaskFeedback inspectiontaskFeedback = inspectiontaskFeedbackList.get(i);
                    inspectiontaskFeedback.setIssubmit("1");

                    inspectiontaskFeedback.setId(IdGen.uuid());

                    User user = UserUtils.get(userId);
                    if (StringUtils.isNotBlank(user.getId())){
                        inspectiontaskFeedback.setCreateBy(user);
                        inspectiontaskFeedback.setUpdateBy(user);
                        inspectiontaskFeedback.setCreateDate(new Date());
                        inspectiontaskFeedback.setUpdateDate(new Date());
                    }

                    Map inspectionSubject = inspectionSubjectService.findById(inspectiontaskFeedback.getSubject_id());
                    if (EAMConsts.CHECK_RESULT_REPAIR.equals(inspectiontaskFeedback.getCheck_result())) {
                        FaultOrder faultOrder = inspectionService.getFaultOrder(userId, userMap, inspectionSubject, inspectiontaskFeedback);
                        Map varMap = new HashMap();
                        varMap.put(FaultOrderController.NOTIFY_FORM_DATA, faultOrder);
                        String faultPstid = eamProcessService.startProcess(EAMConsts.FAULT_ORDER_FLOWDEF, "eam_fault_order", faultOrder.getId(), "故障工单报修", varMap);

                        logger.info("==== 工单流程实例id[" + pstId + "]");
                        if ("timeout".equals(pstId)) {
                            result.setStatus(InfoConstantEnum.FAIL.getCode());
                            result.setErrorcode(InfoConstantEnum.Process_TIMEOUT.getCode());
                            result.setErrmsg(InfoConstantEnum.Process_TIMEOUT.getMessage());
                            logger.info("==== 启动流程超时");
                            throw new RuntimeException("flow is timeout");
                        }
                        faultOrder.setPstid(faultPstid);
                        faultOrderService.insert(faultOrder);
                    }
                }
            }

            //工序列表
            List<InspectionTaskProcedure> procedureList = taskFeekBack.getProcedureList();
            if (procedureList != null && procedureList.size() != 0) {
                for (InspectionTaskProcedure procedure : procedureList) {
                    procedure.setId(IdGen.uuid());  //给工序表设置主键
                    procedure.setInspectiontask_id(inspectiontask_id);  //给工序表set巡检任务外键
                }
            }
            //安全措施列表
            List<InspectiontaskSafety> safetyList = taskFeekBack.getSafetyList();
            if (safetyList != null && safetyList.size() != 0) {
                for (InspectiontaskSafety safety : safetyList) {
                    safety.setId(IdGen.uuid());  //给安全措施表设置主键
                    safety.setInspectiontask_id(inspectiontask_id);   //给安全措施表set巡检任务外键
                }
            }

            // 工器具列表
            List<InspectiontaskTools> toolsList = taskFeekBack.getToolsList();
            if (toolsList != null && toolsList.size() != 0) {
                for (InspectiontaskTools tools : toolsList) {
                    if (tools != null && tools.getMaterial_id() != null && !("").equals(tools.getMaterial_id())) {
                        Map map = materialInfoService.getDetail(tools.getMaterial_id());
                        // 统计该工器具的库存数量
                        int qty = Integer.parseInt(map.get("material_qty").toString());
                        if (tools.getTools_num() > qty) {
                            return "fail";
                        } else {
                            tools.setId(IdGen.uuid());  //给工器具表设置主键
                            tools.setInspectiontask_id(inspectiontask_id);  //给工器具表set巡检任务外键
                        }
                    } else {
                        tools.setId(IdGen.uuid());  //给工器具表设置主键
                        tools.setInspectiontask_id(inspectiontask_id);  //给工器具表set巡检任务外键
                    }
                }
            }

            // 备品备件列表
            List<InspectiontaskSpareparts> sparepartsList = taskFeekBack.getSparepartsList();
            if (sparepartsList != null && sparepartsList.size() != 0) {
                for (InspectiontaskSpareparts spareparts : sparepartsList) {
                    if (spareparts != null && spareparts.getMaterial_id() != null && !("").equals(spareparts.getMaterial_id())) {
                        Map map = materialInfoService.getDetail(spareparts.getMaterial_id());
                        // 统计该工器具的库存数量
                        int qty = Integer.parseInt(map.get("material_qty").toString());
                        if (spareparts.getSpareparts_num() > qty) {
                            return "fail";
                        } else {
                            spareparts.setId(IdGen.uuid());  //给备品备件表设置主键
                            spareparts.setInspectiontask_id(inspectiontask_id);  //给备品备件表set巡检任务外键
                        }
                    } else {
                        spareparts.setId(IdGen.uuid());  //给备品备件表设置主键
                        spareparts.setInspectiontask_id(inspectiontask_id);  //给备品备件表set巡检任务外键
                    }
                }
            }

            // 人员工时列表
            List<InspectiontaskPerson> personList = taskFeekBack.getPersonList();
            if (personList != null && personList.size() != 0) {
                for (InspectiontaskPerson person : personList) {
                    person.setId(IdGen.uuid());  //给人员工时表设置主键
                    person.setInspectiontask_id(inspectiontask_id);  //给人员工时表set巡检任务外键
                }
            }

            // 其他费用列表
            List<InspectiontaskOthers> othersList = taskFeekBack.getOthersList();
            if (othersList != null && othersList.size() != 0) {
                for (InspectiontaskOthers others : othersList) {
                    others.setId(IdGen.uuid());  //给其他费用表设置主键
                    others.setInspectiontask_id(inspectiontask_id);  //给其他费用表set巡检任务外键
                }
            }

            // 将页面表单提交的数据保存到流程中，可以在走完该节点之后在
            Map formValues = new HashMap();
            formValues.put(InspectionTaskController.FEEDBACK_FORM_DATA, param);
            formService.submitTaskFormData(taskId, formValues);

            logger.info("==== 巡检反馈流程提交成功");
            Map map = new HashMap();
            map.put("inspectiontask_id", inspectiontask_id);

            Map param1 = new HashMap();
            param1.put("pstid", pstId);  // 工作流程id
            param1.put("update_by", userId);
            param1.put("update_time", new Date());
            param1.put("task_status", "2");
            param1.put("task_time_finish", new Date());
            inspectionTaskService.updateAprByPIid(param1);   // 更新任务状态
            logger.info("==== 巡检反馈状态更新成功");
            inspectionTaskService.insertDetail1(procedureList, safetyList, toolsList, sparepartsList, personList, othersList);
            inspectionTaskService.deleteFeedBack(map);  // 提交之前先将之前保存的巡检项数据删除，然后再重新插入，防止巡检项重复插入
            logger.info("==== 巡检反馈工序更新成功");
            param1.put("inspectiontask_id", inspectiontask_id);
            inspectionTaskService.insertFeedBackList(inspectiontaskFeedbackList);
            logger.info("==== 巡检反馈是否提交状态更新成功");

        } catch (Exception e) {
            logger.error("==== 巡检反馈出错：", e);
            result.setStatus(InfoConstantEnum.FAIL.getCode());
        }
        logger.info("==== 巡检反馈 结束 ====");
        return JSON.toJSONString(result, WriteMapNullValue, WriteDateUseDateFormat);
    }

}
