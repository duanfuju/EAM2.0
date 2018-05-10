package com.tiansu.eam.interfaces.maintain.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.act.service.ActTaskService;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.interfaces.common.InfoConstantEnum;
import com.tiansu.eam.interfaces.maintain.service.MaintainRestService;
import com.tiansu.eam.interfaces.resulthelper.APPResponseBody;
import com.tiansu.eam.modules.faultOrder.entity.OrderStatusEnum;
import com.tiansu.eam.modules.maintain.controller.MaintainTaskController;
import com.tiansu.eam.modules.maintain.entity.*;
import com.tiansu.eam.modules.maintain.service.MaintainTaskService;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteDateUseDateFormat;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue;

/**
 * @author wangr
 * @description 保养接口
 * @create 2017-11-06 下午 3:18
 */
@Path("/{tenant}/maintain")
@Controller
@Transactional
public class MaintainRestController extends BaseController {

    private static ActTaskService actTaskService = SpringContextHolder.getBean(ActTaskService.class);
    private static MaintainRestService maintainRestService = SpringContextHolder.getBean(MaintainRestService.class);

    private static FormService formService = SpringContextHolder.getBean(FormService.class);
    private static MaintainTaskService maintainTaskService = SpringContextHolder.getBean(MaintainTaskService.class);

    private static TaskService taskService = SpringContextHolder.getBean(TaskService.class);

    /**
     * @param userId   用户id
     * @param start    从start开始
     * @param length   页大小
     * @param status   工单状态
     * @param response
     * @return
     * @creator wangr
     * @createtime 2017/11/6 0006 下午 3:25
     * @description: 获取我的保养列表
     */
    @GET
    @Path("/getMaintainList")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMaintainList(@QueryParam("userId") String userId,
                                  @QueryParam("start") int start,
                                  @QueryParam("length") int length,
                                  @QueryParam("status") List<String> status,
                                  @Context HttpServletResponse response) {
        logger.info("==== 获取我的保养列表 开始 =====");
        response.setHeader("Access-Control-Allow-Origin", "*");
        APPResponseBody result = new APPResponseBody();
        try {
            Map param = new HashMap();
            //设置查询条件
            param.put("userId", userId);
            param.put("status", status);
            param.put("start", start);
            param.put("length", length);
            logger.info("==== 获取我的保修列表 参数 开始");
            logger.info("==== userId:" + userId);
            logger.info("==== start:" + start);
            logger.info("==== length:" + length);
            if (status != null && status.size() > 0) {
                for (String statue : status) {
                    logger.info("==== status:" + statue);
                }
            }
            logger.info("==== 获取我的保养列表 参数 结束");
            Map map = maintainRestService.getMaintainList(param);
            if (!(boolean) map.get("success")) {
                logger.info("==== 获取我的保养列表 代办查询失败");
                result.setStatus(InfoConstantEnum.FAIL.getCode());
                result.setErrorcode(InfoConstantEnum.QUERY_FAIL.getCode());
                result.setErrmsg(InfoConstantEnum.QUERY_FAIL.getMessage());
                logger.info("==== 获取我的保养列表 结束 =====");
                return JSON.toJSONString(result);
            }
            result.setData(map);
        } catch (Exception e) {
            logger.error("==== 查询我的保养列表出错：", e);
            result.setStatus(InfoConstantEnum.FAIL.getCode());
            result.setErrorcode(InfoConstantEnum.QUERY_FAIL.getCode());
            result.setErrmsg(InfoConstantEnum.QUERY_FAIL.getMessage());
        }
        logger.info("==== 获取我的保养列表 结束 =====");
        return JSON.toJSONString(result, WriteMapNullValue, WriteDateUseDateFormat);

    }

    /**
     * @param pstId    保养任务流程id
     * @param response
     * @return
     * @creator lihj
     * @createtime 2017/11/7 0001 下午 1:49
     * @description: 根据流程id获取保养任务详情
     */
    @Path("/getMaintainDetail")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getMaintainDetail(@QueryParam("pstId") String pstId,
                                    @QueryParam("devId") String devId,
                                    @QueryParam("userId") String userId,
                                    @Context HttpServletResponse response) {
        logger.info("==== 根据流程id获取保养任务详情 开始 =====");
        logger.info("==== userId:" + userId + ";devId:" + devId + ";devId:" + devId + ";pstId:" + pstId);
        response.setHeader("Access-Control-Allow-Origin", "*");
        APPResponseBody result = new APPResponseBody();
        if (StringUtils.isBlank(pstId) || StringUtils.isBlank(userId)) {
            logger.info("==== 参数为空");
            result.setStatus(InfoConstantEnum.FAIL.getCode());
            result.setErrorcode(InfoConstantEnum.PARAM.getCode());
            result.setErrmsg(InfoConstantEnum.PARAM.getMessage());
            logger.info("==== 根据流程id获取保养任务详情 结束 =====");
            return JSON.toJSONString(result);
        }
        try {
            result.setData(maintainRestService.getMaintainDetail(pstId,
                    devId, userId, 0));
        } catch (Exception e) {
            logger.error("==== 查询我的保养详情出错：", e);
            result.setStatus(InfoConstantEnum.FAIL.getCode());
            result.setErrorcode(InfoConstantEnum.QUERY_FAIL.getCode());
            result.setErrmsg(InfoConstantEnum.QUERY_FAIL.getMessage());
        }
        logger.info("==== 根据流程id获取保养任务详情 结束 =====");
        return JSON.toJSONString(result, WriteMapNullValue, WriteDateUseDateFormat);
    }

    /**
     * @param pstId    保养任务流程id
     * @param response
     * @return
     * @creator lihj
     * @createtime 2017/11/7 0005 下午 1:49
     * @description: 根据流程id获取保养反馈项详情
     * @modifier wangr
     * @modifytime 2017/11/10 0010 下午 2:15
     * @modifyDec: 设备id和用户id不为空时代表扫码查看保养详情
     */
    @Path("/getMaintainFeedDetail")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getMaintainFeedDetail(@QueryParam("pstId") String pstId,
                                        @Context HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        logger.info("==== 根据流程id获取保养反馈项详情 开始 =====");
        logger.info("==== pstId:" + pstId);
        APPResponseBody result = new APPResponseBody();
        if (StringUtils.isBlank(pstId)) {
            logger.info("==== pstId是空");
            result.setStatus(InfoConstantEnum.FAIL.getCode());
            result.setErrorcode(InfoConstantEnum.PARAM.getCode());
            result.setErrmsg(InfoConstantEnum.PARAM.getMessage());
            logger.info("==== 根据流程id获取保养反馈项详情 结束 =====");
            return JSON.toJSONString(result);
        }
        try {
            result.setData(maintainRestService.getMaintainDetail(pstId,
                    "", "", 1));
        } catch (Exception e) {
            logger.error("MaintainRestController.getMaintainFeedDetail--根据流程id获取保养反馈项详情出错：", e);
            result.setStatus(InfoConstantEnum.FAIL.getCode());
            result.setErrorcode(InfoConstantEnum.QUERY_FAIL.getCode());
            result.setErrmsg(InfoConstantEnum.QUERY_FAIL.getMessage());
        }
        logger.info("==== 根据流程id获取保养反馈项详情 结束 =====");
        return JSON.toJSONString(result, WriteMapNullValue, WriteDateUseDateFormat);
    }


    /**
     * @param userId
     * @param param
     * @param taskId
     * @param response
     * @return
     * @creator wangr
     * @createtime 2017/11/21 0021 下午 6:06
     * @description: 保养反馈
     */
    @Path("/feedBack")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String feedBack(@FormParam("userId") String userId,
                           @FormParam("param") String param,
                           @FormParam("taskId") String taskId,
                           @Context HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        logger.info("==== 保养反馈 开始 =====");
        logger.info("==== userId:" + userId);
        logger.info("==== param:" + param);
        logger.info("==== taskId:" + taskId);
        APPResponseBody result = new APPResponseBody();

        //参数不为空
        if (StringUtils.isBlank(param) || StringUtils.isBlank(taskId)
                || StringUtils.isBlank(userId)) {
            logger.info("==== 参数为空");
            result.setStatus(InfoConstantEnum.FAIL.getCode());
            result.setErrorcode(InfoConstantEnum.PARAM.getCode());
            result.setErrmsg(InfoConstantEnum.PARAM.getMessage());
            logger.info("==== 保养反馈 结束 =====");
            return JSON.toJSONString(result);
        }
        try {
            //签收参数（任务id，签收人）
            actTaskService.claim(taskId, userId);
            logger.info("==== 保养签收成功");
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            logger.info("==== 查询任务结束！");
            // 权限检查-任务的办理人和当前人不一致不能完成任务
            if (!userId.equals(task.getAssignee())) {
                result.setStatus(InfoConstantEnum.FAIL.getCode());
                result.setErrorcode(InfoConstantEnum.ACTIVItY_PERMISSIONS_FAIL.getCode());
                result.setErrmsg(InfoConstantEnum.ACTIVItY_PERMISSIONS_FAIL.getMessage());
                logger.info("==== 任务的办理人和当前人不一致不能完成任务！实际：" + task.getAssignee());
                logger.info("==== 保养反馈 结束 ====");
                return JSON.toJSONString(result);
            }
            //获取报修表单数据
            // FaultOrder faultNotifyOrder = (FaultOrder) taskService.getVariable(taskId, FaultOrderController.NOTIFY_FORM_DATA);
            //将序列化的json字符串转为实体
            MaintainTask maintainTask = JSON.parseObject(param, MaintainTask.class);
            Map formValues = new HashMap();
            formValues.put(MaintainTaskController.FEEDBACK_FORM_DATA, param);


            //faultOrder.setId(faultOrder.getId());
            maintainTask.preUpdate();
            maintainTask.setTask_status(OrderStatusEnum.FINISHED.value());
            maintainTask.setTask_time_finish(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            List<MaintainTool> newMaintainToolList = Lists.newArrayList();
            List<MaintainAttachment> newMaintainSparepartList = Lists.newArrayList();
            List<MaintainEmp> newMaintainPersonList = Lists.newArrayList();
            List<MaintainOther> newMaintainOthersList = Lists.newArrayList();

            List<MaintainTool> maintainToolList = maintainTask.getToolList();//工器具
            for (MaintainTool s : maintainToolList) {
                if (s == null || StringUtils.isEmpty(s.getTool_id())) {
                    continue;
                }
                s.setId(IdGen.uuid());
                newMaintainToolList.add(s);
            }
            List<MaintainAttachment> maintainAttachmentList = maintainTask.getAttachmentList();//备品备件
            for (MaintainAttachment s : maintainAttachmentList) {
                if (s == null || StringUtils.isEmpty(s.getAttachment_id())) {
                    continue;
                }
                s.setId(IdGen.uuid());
                newMaintainSparepartList.add(s);
            }
            List<MaintainEmp> maintainPersonList = maintainTask.getEmpList();//人员工时
            for (MaintainEmp s : maintainPersonList) {
                if (s == null || StringUtils.isEmpty(s.getEmp_id())) {
                    continue;
                }
                s.setId(IdGen.uuid());
                newMaintainPersonList.add(s);
            }
            List<MaintainOther> maintainOtherList = maintainTask.getOtherList();//其他费用
            for (MaintainOther s : maintainOtherList) {
                if (s == null || StringUtils.isEmpty(s.getCharge_name())) {
                    continue;
                }
                s.setId(IdGen.uuid());
                newMaintainOthersList.add(s);
            }
            maintainTask.setToolList(newMaintainToolList);
            maintainTask.setAttachmentList(newMaintainSparepartList);
            maintainTask.setEmpList(newMaintainPersonList);
            maintainTask.setOtherList(newMaintainOthersList);

            formService.submitTaskFormData(taskId, formValues);
            logger.info("==== 保养反馈流程提交");
            maintainTaskService.update(maintainTask);
            logger.info("==== 保养更新成功");
            maintainTaskService.insertActualDetail(newMaintainToolList, newMaintainSparepartList, newMaintainPersonList, newMaintainOthersList);
            logger.info("==== 工器具新增成功");
        } catch (Exception e) {
            logger.error("==== MaintainRestController.feedBack--保养反馈：", e);
            result.setErrorcode(InfoConstantEnum.ORDER_FEEDBACK_FAIL.getCode());
            result.setErrmsg(InfoConstantEnum.ORDER_FEEDBACK_FAIL.getMessage());
            result.setStatus(InfoConstantEnum.FAIL.getCode());
        }
        logger.info("==== 保养反馈 结束 =====");
        return JSON.toJSONString(result);
    }

}
