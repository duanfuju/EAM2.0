package com.tiansu.eam.interfaces.act.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.interfaces.common.InfoConstantEnum;
import com.tiansu.eam.interfaces.resulthelper.APPResponseBody;
import com.tiansu.eam.modules.act.service.EamActTaskService;
import org.activiti.engine.FormService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wangr
 * @description 任务提交
 * @create 2017-10-23 下午 2:42
 */
@Path("/{tenant}/act")
@Controller
public class TaskRestController extends BaseController {

    private static ActTaskService actTaskService = SpringContextHolder.getBean(ActTaskService.class);

    private static TaskService taskService = SpringContextHolder.getBean(TaskService.class);

    private static IdentityService identityService = SpringContextHolder.getBean(IdentityService.class);

    private static FormService formService = SpringContextHolder.getBean(FormService.class);

    private static EamActTaskService eamActTaskService = SpringContextHolder.getBean(EamActTaskService.class);
    public static ObjectMapper mapper = new ObjectMapper();

    /**
     * @creator fy
     * @createtime 2017-11-28
     * @description: 我的代办任务列表
     */
    @GET
    @Path("/todoList")
    @Produces(MediaType.APPLICATION_JSON)
    public String todoList(@QueryParam("taskName") String taskName,
                           @QueryParam("processName") String processName,
                           @QueryParam("userId") String userId,
                           @Context HttpServletResponse response,
                           @Context HttpServletRequest request) {
        logger.info("==我的代办流程开始==");
        response.setHeader("Access-Control-Allow-Origin", "*");
        //mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        APPResponseBody result = new APPResponseBody();
        String mapJackson = "";
        try {
            //过滤表中不需要的字段
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
            mapper.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            Map map = eamActTaskService.todoList(taskName, processName, userId);
            result.setData(map);
            mapJackson = mapper.writeValueAsString(result);
            logger.info("==我的代办流程结束==");
        } catch (Exception e) {
            logger.error("todoList--查询出错：", e);
            result.setStatus(InfoConstantEnum.FAIL.getCode());
            result.setErrorcode(InfoConstantEnum.QUERY_FAIL.getCode());
            result.setErrmsg(InfoConstantEnum.QUERY_FAIL.getMessage());
        }
        return mapJackson;
    }

    /**
     * @creator fy
     * @createtime 2017-11-28
     * @description: 我的已办任务列表
     */
    @GET
    @Path("/finishedList")
    @Produces(MediaType.APPLICATION_JSON)
    public String finishedList(@QueryParam("taskName") String taskName,
                               @QueryParam("userId") String uName,
                               @Context HttpServletResponse response) {
        logger.info("==我的已办流程开始==");
        response.setHeader("Access-Control-Allow-Origin", "*");
        APPResponseBody result = new APPResponseBody();
        String mapJackson = "";
        try {
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
            mapper.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            Map<String, Object> map = eamActTaskService.finishedList(taskName, uName);
            result.setData(map);
            logger.info("==我的已办流程结束==");
            mapJackson = mapper.writeValueAsString(result);
        } catch (Exception e) {
            logger.error("finishedList--查询出错：", e);
            result.setStatus(InfoConstantEnum.FAIL.getCode());
            result.setErrorcode(InfoConstantEnum.QUERY_FAIL.getCode());
            result.setErrmsg(InfoConstantEnum.QUERY_FAIL.getMessage());
        }
        return mapJackson;
    }

    /**
     * @creator wangr
     * @createtime 2017-10-23 14:47
     * @description: 签收完成任务
     */
    @Path("/claim")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String claim(@Context HttpServletRequest request,
                        @Context HttpServletResponse response) {
        logger.info("==== 流程动作签收完成任务 开始 ====");
        response.setHeader("Access-Control-Allow-Origin", "*");
        APPResponseBody result = new APPResponseBody();
        String taskId = getPara("taskId");
        String userId = getPara("userId");
        logger.info("==== 流程动作签收完成任务参数-->userId:" + userId + "\t\ttaskId:" + taskId);
        if (StringUtils.isBlank(taskId) || StringUtils.isBlank(userId)) {
            result.setStatus(InfoConstantEnum.FAIL.getCode());
            result.setErrorcode(InfoConstantEnum.PARAM.getCode());
            result.setErrmsg(InfoConstantEnum.PARAM.getMessage());
            logger.info("==== 流程动作签收完成任务参数为空");
            logger.info("==== 流程动作签收完成任务 结束 ====");
            return JSON.toJSONString(result);
        }
        try {
            actTaskService.claim(taskId, userId);//签收参数（任务id，签收人）
            logger.info("==== 签收成功");
            identityService.setAuthenticatedUserId(userId);
            logger.info("==== 设置流程发起用户成功");
            logger.info("==== 查询任务开始！taskId:" + taskId);
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            logger.info("==== processDefInId:" + task.getProcessDefinitionId());
            logger.info("==== name:" + task.getName());
            logger.info("==== owner:" + task.getOwner());
            logger.info("==== 查询任务结束！");
            // 如果任务的流程定义任务Key为空则认为是手动创建的任务
            if (StringUtils.isBlank(task.getTaskDefinitionKey())) {
                taskService.complete(taskId);

                logger.info("==== 任务的流程定义任务Key为空则认为是手动创建的任务");
                result.setStatus(InfoConstantEnum.SUCCESS.getCode());
                result.setErrmsg(InfoConstantEnum.SUCCESS.getMessage());
                logger.info("==== 流程动作签收完成任务 结束 ====");
                return JSON.toJSONString(result);
            }

            // 权限检查-任务的办理人和当前人不一致不能完成任务
            if (!userId.equals(task.getAssignee())) {

                result.setErrorcode(InfoConstantEnum.ACTIVItY_PERMISSIONS_FAIL.getCode());
                result.setErrmsg(InfoConstantEnum.ACTIVItY_PERMISSIONS_FAIL.getMessage());
                result.setStatus(InfoConstantEnum.FAIL.getCode());
                logger.info("==== 任务的办理人和当前人不一致不能完成任务！userId：" + userId);
                logger.info("==== 流程动作签收完成任务 结束 ====");
                return JSON.toJSONString(result);
            }

            // 单独处理被委派的任务
            if (task.getDelegationState() == DelegationState.PENDING) {
                taskService.resolveTask(taskId);
                logger.info("==== 单独处理被委派的任务");
                result.setStatus(InfoConstantEnum.SUCCESS.getCode());
                result.setErrmsg(InfoConstantEnum.SUCCESS.getMessage());
                logger.info("==== 流程动作签收完成任务 结束 ====");
                return JSON.toJSONString(result);
            }
            //根据taskId获取流程任务中的表单内容
            TaskFormData taskFormData = formService.getTaskFormData(taskId);
            logger.info("==== 根据taskId获取流程任务中的表单内容 taskId：" + taskId);
            String formKey = taskFormData.getFormKey();
            // 从请求中获取表单字段的值
            List<FormProperty> formProperties = taskFormData.getFormProperties();
            Map<String, String> formValues = new HashMap<String, String>();

            if (StringUtils.isNotBlank(formKey)) { // formkey表单
                Map<String, String[]> parameterMap = request.getParameterMap();
                Set<Map.Entry<String, String[]>> entrySet = parameterMap.entrySet();
                for (Map.Entry<String, String[]> entry : entrySet) {
                    String key = entry.getKey();
                    formValues.put(key, entry.getValue()[0]);
                    logger.info("==== 获取参数 key:" + key + "; value:" + entry.getValue()[0]);
                }
            } else { // 动态表单
                for (FormProperty formProperty : formProperties) {
                    if (formProperty.isWritable()) {
                        String value = request.getParameter(formProperty.getId());
                        formValues.put(formProperty.getId(), value);
                        logger.info("==== 获取参数 key:" + formProperty.getId() + "; value:" + value);
                    }
                }
            }

            formService.submitTaskFormData(taskId, formValues);
            logger.info("===== 流程动作签收完成任务提交成功！");
            Thread.sleep(500);
            result.setStatus(InfoConstantEnum.SUCCESS.getCode());
            result.setErrmsg(InfoConstantEnum.SUCCESS.getMessage());
        } catch (Exception e) {
            logger.error("==== 流程动作签收完成任务失败", e);
            result.setErrorcode(InfoConstantEnum.ACTIVItY_FAIL.getCode());
            result.setErrmsg(InfoConstantEnum.ACTIVItY_FAIL.getMessage());
            result.setStatus(InfoConstantEnum.FAIL.getCode());
        }
        logger.info("==== 流程动作签收完成任务 结束 ====");
        return JSON.toJSONString(result);
    }

    /**
     * @creator wangr
     * @createtime 2017-10-23 14:47
     * @description: 反签收任务
     */
    @Path("/unclaim")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String unclaim(@Context HttpServletResponse response) {
        logger.info("==== 反签收任务 开始 ====");
        response.setHeader("Access-Control-Allow-Origin", "*");
        APPResponseBody result = new APPResponseBody();

        String taskId = getPara("taskId");
        logger.info("==== 任务id：" + taskId);
        try {
            // 反签收条件过滤
            List<IdentityLink> links = taskService.getIdentityLinksForTask(taskId);
            for (IdentityLink identityLink : links) {
                // 如果一个任务有相关的候选人、组就可以反签收
                if (StringUtils.equals(IdentityLinkType.CANDIDATE, identityLink.getType())) {
                    taskService.claim(taskId, null);

                    result.setStatus(InfoConstantEnum.SUCCESS.getCode());
                    result.setErrmsg(InfoConstantEnum.SUCCESS.getMessage());

                    return JSON.toJSONString(result);
                }
            }

            result.setErrorcode(InfoConstantEnum.ACTIVItY_FAIL.getCode());
            result.setErrmsg(InfoConstantEnum.ACTIVItY_FAIL.getMessage());
            result.setStatus(InfoConstantEnum.FAIL.getCode());

        } catch (Exception e) {
            logger.error("==== 反签收失败", e);
            result.setErrorcode(InfoConstantEnum.ACTIVItY_FAIL.getCode());
            result.setErrmsg(InfoConstantEnum.ACTIVItY_FAIL.getMessage());
            result.setStatus(InfoConstantEnum.FAIL.getCode());
        }
        logger.info("==== 反签收 结束 =====");
        return JSON.toJSONString(result);
    }

//    @Path("/")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public String claim(
//                        @Context HttpServletResponse response) {
//
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        APPResponseBody result = new APPResponseBody();
//
//        return "";
//    }
}
