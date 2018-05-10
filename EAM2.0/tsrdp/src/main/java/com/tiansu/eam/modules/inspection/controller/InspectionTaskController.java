package com.tiansu.eam.modules.inspection.controller;

import com.alibaba.fastjson.JSON;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.act.service.EamProcessService;
import com.tiansu.eam.modules.device.entity.DevLocation;
import com.tiansu.eam.modules.device.entity.Device;
import com.tiansu.eam.modules.device.service.EamDeviceService;
import com.tiansu.eam.modules.employee.service.EamUserExtService;
import com.tiansu.eam.modules.faultOrder.controller.FaultOrderController;
import com.tiansu.eam.modules.faultOrder.entity.FaultOrder;
import com.tiansu.eam.modules.faultOrder.entity.OrderStatusEnum;
import com.tiansu.eam.modules.faultOrder.service.FaultOrderService;
import com.tiansu.eam.modules.inspection.entity.*;
import com.tiansu.eam.modules.inspection.service.InspectionRouteService;
import com.tiansu.eam.modules.inspection.service.InspectionSubjectService;
import com.tiansu.eam.modules.inspection.service.InspectionTaskService;
import com.tiansu.eam.modules.material.service.MaterialInfoService;
import com.tiansu.eam.modules.opestandard.entity.StandardFailure;
import com.tiansu.eam.modules.sys.entity.CodeEnum;
import com.tiansu.eam.modules.sys.entity.Dept;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteDateUseDateFormat;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue;

/**
 * @author wujh
 * @description 巡检任务接口controller层
 * @create 2017-10-14 11:16
 **/
@Controller
@RequestMapping(value = "${adminPath}/eam/inspectionTask")
public class InspectionTaskController extends BaseController {
    @Autowired
    private InspectionTaskService inspectionTaskService;

    @Autowired
    private InspectionRouteService inspectionRouteService;

    @Autowired
    private InspectionSubjectService inspectionSubjectService;

    @Autowired
    private EamUserExtService eamUserExtService;

    @Autowired
    private EamDeviceService deviceService;

    @Autowired
    private EamProcessService eamProcessService;    // 工作进程service

    @Autowired
    private MaterialInfoService materialInfoService;    // 物料service

    @Autowired
    private FaultOrderService faultOrderService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    //反馈表单数据key
    public static final String FEEDBACK_FORM_DATA = "feedBackDatas";

    /**
     *  获取巡检任务列表
     * @return map
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "listData")
    public String listData(){
        Map param = super.getFormMap();
        Map<String, Object> map = inspectionTaskService.dataTablePageMap(param);
        // 将接口查询反馈的结果根据详细的格式转换，若无数据，则返回空，若有日期数据，则将数据转化为日期格式
        return JSON.toJSONString(map,WriteMapNullValue, WriteDateUseDateFormat);
    }

    /**
     * 根据页面编号和当前用户的角色，查当前页面可以展示的字段信息
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getfields")
    public Map getFields(){
        String menuno = getPara("menuno");
        Map<String, Object> result = getFieldsByMenuno(menuno);
        return result;
    }

    @RequestMapping(value = "start")
    public void test( HttpServletRequest request){
        List<InspectionTask> inspectionTaskList = new ArrayList<InspectionTask>();
        for(int i = 0; i < 1; i++) {
            InspectionTask inspectionTask = new InspectionTask();
            inspectionTask.setRoute_id("1CF35D4A-3937-4470-8188-BD1E47561AD7");
            inspectionTask.setTask_time_plan_begin(new Date());
            inspectionTask.setTask_time_plan_finish(new Date());
            inspectionTask.setTask_totalhour_plan(10.8);
            inspectionTask.setTask_processor_plan("emagine");
            inspectionTaskList.add(inspectionTask);
        }
        insert(inspectionTaskList, request);
    }

    /**
     * 一条巡检路线根据周期生成多条巡检任务，并启动流程
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "insert")
    @Transactional
    public Map<String, Object> insert(List<InspectionTask> inspectionTaskList, HttpServletRequest request){
        Map<String,Object> returnMap = new HashMap();
        if(inspectionTaskList == null || inspectionTaskList.size() == 0) {
            returnMap.put("flag",false);
            returnMap.put("msg","参数错误");
            return returnMap;
        } else {
            for(InspectionTask inspectionTask : inspectionTaskList) {
                if(inspectionTask.getRoute_id() != null && !"".equals(inspectionTask.getRoute_id())){
                    String uuid = IdGen.uuid();
                    inspectionTask.setId(uuid);  // 给每条巡检任务设置主键id
                    inspectionTask.setId_key(uuid);
                    inspectionTask.setCreate_by(UserUtils.getUser().getLoginname());
                    inspectionTask.setUpdate_by(UserUtils.getUser().getLoginname());
                    inspectionTask.setCreate_time(new Date());
                    inspectionTask.setUpdate_time(new Date());
                    String pstid = eamProcessService.startProcessByPdid("inspectionFlow","inspectionTaskList",uuid,request);
                    if("timeout".equals(pstid)){
                        returnMap.put("flag",false);
                        returnMap.put("msg","超时");
                    } else {
                        inspectionTask.setPstid(pstid);
                    }
                }
            }
            inspectionTaskService.insert(inspectionTaskList);
            returnMap.put("flag",true);
            returnMap.put("msg","操作成功");
        }

        return returnMap;
    }

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getInspectionTaskByPIid")
    public Map getInspectionTaskByPIid(){
        String pIid=getPara("pIid");
        return inspectionTaskService.getInspectionTaskByPIid(pIid);
    }

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getInspectionTaskById")
    public Map getInspectionTaskById(){
        String inspectionTask_id =getPara("inspectionTask_id");
        return inspectionTaskService.getInspectionTaskById(inspectionTask_id);
    }

    /**
     * 根据巡检任务id获取巡检区域下的设备以及巡检项信息
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getSubjectInfosByPstid")
    public String getSubjectInfosByPstid(){
        String pstid = getPara("pstid");   // 巡检任务id
        List<Map> areaList = inspectionTaskService.getAreaInfoByTaskPstid(pstid);
        if(areaList != null && areaList.size() != 0) {
            for(int i = 0; i < areaList.size(); i++){
                Map map = areaList.get(i);
                String inspectiontask_id = map.get("inspectiontask_id").toString();
                String area_id = map.get("id").toString();

                Map param = new HashMap();
                param.put("pstid", pstid);
                List<Map> devList = inspectionTaskService.getDeviceInfos(param);
                if(devList != null && devList.size() > 0) {
                    for (int j = 0; j < devList.size(); j++) {
                        Map result = devList.get(j);
                        Map para = new HashMap();
                        para.put("area_id", area_id);
                        para.put("dev_id", result.get("dev_id").toString());
                        para.put("inspectiontask_id", inspectiontask_id);
                        List<Map> subjectList = inspectionTaskService.getSubjectInfos(para);
                        if (subjectList != null && subjectList.size() > 0) {
                            String s = "";
                            String s1 = "";
                            for (int m = 0; m < subjectList.size(); m++) {
                                s += subjectList.get(m).get("subject_content").toString() + ",";
                                s1 += subjectList.get(m).get("id").toString() + ",";
                            }
                            s = s.substring(0, s.length() - 1);
                            s1 = s1.substring(0, s1.length() - 1);
                            result.put("subjectNames", s);
                            result.put("subjectIds", s1);
                        }
                        result.put("subjects", subjectList);
                        List<StandardFailure> standardFailureList = inspectionTaskService.getFailureList(result.get("dev_id").toString());
                        result.put("standardFailureList", standardFailureList);
                    }
                }
                map.put("devList", devList);
            }
        }

        return JSON.toJSONString(areaList,WriteMapNullValue, WriteDateUseDateFormat);
    }

    /**
     * 根据巡检任务流程id获取当前任务下的巡检区域信息
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getAreaInfoByTaskPstid")
    public List<Map> getAreaInfoByTaskPstid(){
        String pstid = getPara("pstid");   // 巡检任务id
        return inspectionTaskService.getAreaInfoByTaskPstid(pstid);
    }

    /**
     * 根据巡检区域id获取巡检项信息
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getSubjectInfoByAreaId")
    public String getSubjectInfoByAreaId(){
        String area_id = getPara("area_id");
        String inspectiontask_id = getPara("inspectiontask_id");
        Map map = new HashMap();
        map.put("area_id", area_id);
        List<Map> devList = inspectionTaskService.getDeviceInfos(map);
        if(devList != null && devList.size() > 0) {
            for(int i = 0; i < devList.size(); i++) {
                Map result = devList.get(i);
                Map param = new HashMap();
                param.put("area_id", area_id);
                param.put("dev_id", result.get("dev_id").toString());
                param.put("inspectiontask_id", inspectiontask_id);
                List<Map> subjectList = inspectionTaskService.getSubjectInfos(param);
                if(subjectList != null && subjectList.size() > 0){
                    String s = "";
                    for(int m = 0; m < subjectList.size(); m++) {
                        s += subjectList.get(m).get("subject_content").toString() + ",";
                    }
                    s = s.substring(0, s.length() - 1);
                    result.put("subjectNames", s);
                }
                List<StandardFailure> standardFailureList = inspectionTaskService.getFailureList(result.get("dev_id").toString());
                result.put("standardFailureList", standardFailureList);
                result.put("subjects", subjectList);
            }
        }
        return JSON.toJSONString(devList,WriteMapNullValue, WriteDateUseDateFormat);
//        return devList;
    }

    /**
     * @creator wujh
     * @createtime 2017/9/5 14:34
     * @description: 巡检任务的详情页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "detailUI")
    public String detailUI() {
        return "modules/inspection/inspectionTaskDetailUI";
    }

    /**
     * 巡检任务反馈页面
     * @param taskFeekBack
     * @param request
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "feedBack")
    @Transactional
    public String insertFeedBack(TaskFeekBack taskFeekBack, HttpServletRequest request){
        Map<String,Object> returnMap = new HashMap();
        String param = getPara("param");      //获取入参
        String taskId = getPara("taskid");//工作流任务id
        String pstid = getPara("pstid");    //工作流程id
        String userId = UserUtils.getUser().getLoginname();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        // 权限检查-任务的办理人和当前人不一致不能完成任务
        if (!userId.equals(task.getAssignee())) {
            return "noauth";
        } else {
            //将序列化的json字符串转为实体
            taskFeekBack = JSON.parseObject(param, TaskFeekBack.class);
            if(taskFeekBack == null) {
                return "fail";
            } else {
                List<InspectiontaskFeedback> inspectiontaskFeedbackList = taskFeekBack.getInspectiontaskFeedbackList();
                if(inspectiontaskFeedbackList == null || inspectiontaskFeedbackList.size() == 0) {
                    return "fail";
                } else {
                    String inspectiontask_id = taskFeekBack.getInspectiontask_id();
                    Map userMap = eamUserExtService.findByLoginName(userId).get(0);

                    for(int i = 0; i < inspectiontaskFeedbackList.size(); i++) {
                        InspectiontaskFeedback inspectiontaskFeedback = inspectiontaskFeedbackList.get(i);
                        inspectiontaskFeedback.setIssubmit("1");
                        inspectiontaskFeedback.preInsert();
                        Map inspectionSubject = inspectionSubjectService.findById(inspectiontaskFeedback.getSubject_id());
                        if(EAMConsts.CHECK_RESULT_REPAIR.equals(inspectiontaskFeedback.getCheck_result())) {
                            FaultOrder faultOrder = getFaultOrder(userId, userMap, inspectionSubject, inspectiontaskFeedback);
                            Map varMap = new HashMap();
                            varMap.put(FaultOrderController.NOTIFY_FORM_DATA,faultOrder);
                            String faultPstid = eamProcessService.startProcess(EAMConsts.FAULT_ORDER_FLOWDEF,"eam_fault_order",faultOrder.getId(),"故障工单报修",varMap);

                            System.out.println("工单流程实例id["+pstid+"]");
                            if("timeout".equals(pstid)){
                                throw new RuntimeException("flow is timeout");
                            }
                            faultOrder.setPstid(faultPstid);
                            faultOrderService.insert(faultOrder);
                        }
                    }

                    //工序列表
                    List<InspectionTaskProcedure> procedureList = taskFeekBack.getProcedureList();
                    for(InspectionTaskProcedure procedure : procedureList){
                        procedure.setId(IdGen.uuid());  //给工序表设置主键
                        procedure.setInspectiontask_id(inspectiontask_id);  //给工序表set巡检任务外键
                    }

                    //安全措施列表
                    List<InspectiontaskSafety> safetyList = taskFeekBack.getSafetyList();
                    for(InspectiontaskSafety safety : safetyList) {
                        safety.setId(IdGen.uuid());  //给安全措施表设置主键
                        safety.setInspectiontask_id(inspectiontask_id);   //给安全措施表set巡检任务外键
                    }

                    // 工器具列表
                    List<InspectiontaskTools> toolsList = taskFeekBack.getToolsList();
                    if(toolsList != null && toolsList.size() != 0) {
                        for(InspectiontaskTools tools : toolsList) {
                            if(tools != null && tools.getMaterial_id() != null && !("").equals(tools.getMaterial_id())){
                                Map map = materialInfoService.getDetail(tools.getMaterial_id());
                                // 统计该工器具的库存数量
                                int qty = Integer.parseInt(map.get("material_qty").toString());
                                if(tools.getTools_num() > qty) {
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
                    if(sparepartsList != null && sparepartsList.size() != 0) {
                        for(InspectiontaskSpareparts spareparts : sparepartsList) {
                            if(spareparts != null && spareparts.getMaterial_id() != null && !("").equals(spareparts.getMaterial_id())) {
                                Map map = materialInfoService.getDetail(spareparts.getMaterial_id());
                                // 统计该工器具的库存数量
                                int qty = Integer.parseInt(map.get("material_qty").toString());
                                if(spareparts.getSpareparts_num() > qty) {
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
                    for(InspectiontaskPerson person : personList) {
                        person.setId(IdGen.uuid());  //给人员工时表设置主键
                        person.setInspectiontask_id(inspectiontask_id);  //给人员工时表set巡检任务外键
                    }

                    // 其他费用列表
                    List<InspectiontaskOthers> othersList = taskFeekBack.getOthersList();
                    for(InspectiontaskOthers others : othersList) {
                        others.setId(IdGen.uuid());  //给其他费用表设置主键
                        others.setInspectiontask_id(inspectiontask_id);  //给其他费用表set巡检任务外键
                    }

                    // 将页面表单提交的数据保存到流程中，可以在走完该节点之后在
                    Map formValues = new HashMap();
                    formValues.put(InspectionTaskController.FEEDBACK_FORM_DATA,param);
                    formService.submitTaskFormData(taskId, formValues);

                    Map map = new HashMap();
                    map.put("inspectiontask_id", inspectiontask_id);
                    Map param1 = new HashMap();
                    param1.put("pstid", pstid);  // 工作流程id
                    param1.put("update_by", UserUtils.getUser().getLoginname());
                    param1.put("update_time", new Date());
                    param1.put("task_status", "2");
                    param1.put("task_time_finish", new Date());
                    inspectionTaskService.updateAprByPIid(param1);   // 更新任务状态
                    inspectionTaskService.insertDetail1(procedureList, safetyList,toolsList, sparepartsList, personList, othersList);
                    inspectionTaskService.deleteFeedBack(map);  // 提交之前先将之前保存的巡检项数据删除，然后再重新插入，防止巡检项重复插入
                    inspectionTaskService.insertFeedBackList(inspectiontaskFeedbackList);
                }
            }
        }
        return "success";
    }

    /**
     * 组装拼接报修单信息
     * @return
     */
    public FaultOrder getFaultOrder(String userId, Map userMap, Map inspectionSubject, InspectiontaskFeedback inspectiontaskFeedback){
        FaultOrder faultOrder = new FaultOrder();
        faultOrder.preInsert();
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
        faultOrder.setNotifier_source(EAMConsts.ORDER_SOURCE_PHONE);  // 报修来源 pc端都是电话
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
        faultOrder.setOrder_level(inspectiontaskFeedback.getProcessMode());
        return faultOrder;
    }

    /**
     * 巡检任务反馈页面
     * @param taskFeekBack
     * @param request
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "feedBack1")
    @Transactional
    public Map<String, Object> insertFeedBack1(TaskFeekBack taskFeekBack, HttpServletRequest request){
        Map<String,Object> returnMap = new HashMap();
        String param = getPara("param");      //获取入参
        //将序列化的json字符串转为实体
        taskFeekBack = JSON.parseObject(param, TaskFeekBack.class);
        if(taskFeekBack == null) {
            returnMap.put("flag",false);
            returnMap.put("msg","提交失败，参数错误");
            return returnMap;
        } else {
            List<InspectiontaskFeedback> inspectiontaskFeedbackList = taskFeekBack.getInspectiontaskFeedbackList();
            if(inspectiontaskFeedbackList == null || inspectiontaskFeedbackList.size() == 0) {
                returnMap.put("flag",false);
                returnMap.put("msg","提交失败，参数错误");
                return returnMap;
            } else {
                String inspectiontask_id = taskFeekBack.getInspectiontask_id();

                for(int i = 0; i < inspectiontaskFeedbackList.size(); i++) {
                    InspectiontaskFeedback inspectiontaskFeedback = inspectiontaskFeedbackList.get(i);
                    inspectiontaskFeedback.preInsert();
                }

                //工序列表
                    List<InspectionTaskProcedure> procedureList = taskFeekBack.getProcedureList();
                    for(InspectionTaskProcedure procedure : procedureList){
                        procedure.setId(IdGen.uuid());  //给工序表设置主键
                        procedure.setInspectiontask_id(inspectiontask_id);  //给工序表set巡检任务外键
                    }

                //安全措施列表
                    List<InspectiontaskSafety> safetyList = taskFeekBack.getSafetyList();
                    for(InspectiontaskSafety safety : safetyList) {
                        safety.setId(IdGen.uuid());  //给安全措施表设置主键
                        safety.setInspectiontask_id(inspectiontask_id);   //给安全措施表set巡检任务外键
                    }

                // 工器具列表
                List<InspectiontaskTools> toolsList = taskFeekBack.getToolsList();
                if(toolsList != null && toolsList.size() != 0) {
                    for(InspectiontaskTools tools : toolsList) {
                        if(tools != null && tools.getMaterial_id() != null && ("").equals(tools.getMaterial_id())){
                            Map map = materialInfoService.getDetail(tools.getMaterial_id());
                            // 统计该工器具的库存数量
                            int qty = Integer.parseInt(map.get("material_qty").toString());
                            if(tools.getTools_num() > qty) {
                                returnMap.put("flag",false);
                                returnMap.put("msg","工器具数量超过库存数量" + qty);
                                return returnMap;
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
                if(sparepartsList != null && sparepartsList.size() != 0) {
                    for(InspectiontaskSpareparts spareparts : sparepartsList) {
                        if(spareparts != null && spareparts.getMaterial_id() != null && ("").equals(spareparts.getMaterial_id())) {
                            Map map = materialInfoService.getDetail(spareparts.getMaterial_id());
                            // 统计该工器具的库存数量
                            int qty = Integer.parseInt(map.get("material_qty").toString());
                            if(spareparts.getSpareparts_num() > qty) {
                                returnMap.put("flag",false);
                                returnMap.put("msg","备品备件数量超过库存数量" + qty);
                                return returnMap;
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
                for(InspectiontaskPerson person : personList) {
                    person.setId(IdGen.uuid());  //给人员工时表设置主键
                    person.setInspectiontask_id(inspectiontask_id);  //给人员工时表set巡检任务外键
                }

                // 其他费用列表
                List<InspectiontaskOthers> othersList = taskFeekBack.getOthersList();
                for(InspectiontaskOthers others : othersList) {
                    others.setId(IdGen.uuid());  //给其他费用表设置主键
                    others.setInspectiontask_id(inspectiontask_id);  //给其他费用表set巡检任务外键
                }

                Map map = new HashMap();
                map.put("inspectiontask_id", inspectiontask_id);
                inspectionTaskService.insertDetail1(procedureList, safetyList,toolsList, sparepartsList, personList, othersList);
                inspectionTaskService.deleteFeedBack(map);  // 提交之前先将之前保存的巡检项数据删除，然后再重新插入，防止巡检项重复插入
                inspectionTaskService.insertFeedBackList(inspectiontaskFeedbackList);
            }
        }
        returnMap.put("flag",true);
        returnMap.put("msg","操作成功");
        return returnMap;
    }

    /**
     * 反馈页面保存巡检项项反馈信息，只保存巡检项的反馈信息，其他的备品备件/工器具/人员工时/其他事项等数据要在最终巡检任务反馈提交的时候保存
     * @param taskFeekBack 巡检项反馈信息
     * @param request
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "saveSubjectFeedBack")
    @Transactional
    public Map<String, Object> saveSubjectFeedBack(TaskFeekBack taskFeekBack, HttpServletRequest request){
        Map<String,Object> returnMap = new HashMap();
        String param = getPara("param");      //获取入参
        taskFeekBack = JSON.parseObject(param, TaskFeekBack.class);
        if(taskFeekBack == null) {
            returnMap.put("flag",false);
            returnMap.put("msg","参数错误");
            return returnMap;
        } else {
            List<InspectiontaskFeedback> inspectiontaskFeedbackList = taskFeekBack.getInspectiontaskFeedbackList();
            if(inspectiontaskFeedbackList == null || inspectiontaskFeedbackList.size() == 0) {
                returnMap.put("flag",false);
                returnMap.put("msg","参数错误");
                return returnMap;
            } else {
                String inspectiontask_id = taskFeekBack.getInspectiontask_id();

                Map param1 = new HashMap();
                param1.put("inspectiontask_id", inspectiontask_id);
                List<InspectiontaskFeedback> feedBackList = inspectionTaskService.getFeedBackInfoByTaskId(param1);
                for (int j = 0; j < inspectiontaskFeedbackList.size(); j++) {
                    InspectiontaskFeedback inspectiontaskFeedback = inspectiontaskFeedbackList.get(j);
                    if(feedBackList != null && feedBackList.size() > 0) {
                        for(int i = 0; i < feedBackList.size(); i++) {
                            if(feedBackList.get(i).getSubject_id().equals(inspectiontaskFeedback.getSubject_id())) {
                                Map para1 = new HashMap();
                                para1.put("inspectiontask_id", inspectiontask_id);
                                para1.put("subject_id", inspectiontaskFeedback.getSubject_id());
                                inspectionTaskService.deleteFeedBack(para1);
                            }
                        }
                    }
                    inspectiontaskFeedback.setId_key(IdGen.uuid());
                    inspectiontaskFeedback.preInsert();
                }


                inspectionTaskService.insertFeedBackList(inspectiontaskFeedbackList);
                returnMap.put("flag", true);
                returnMap.put("msg", "操作成功");
            }
        }
        return returnMap;
    }

    /**
     * 根据巡检任务id获取路线中的工序/安全措施/工器具/备品备件/人员工时/其他费用
     * refs #17884 巡检任务详情中添加巡检路线中设置的预计使用明细和实际使用明细比对记录
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getDetailList")
    @Transactional
    public Map getDetailList(){
        String inspectiontask_id = getPara("inspectiontask_id");
        Map map = new HashMap();
        String route_id = inspectionTaskService.getEdit(inspectiontask_id).get("route_id").toString();
        if(route_id != null && !route_id.equals("")){
            Map paramMap = new HashMap();
            paramMap.put("inspectiontask_id", inspectiontask_id);
            paramMap.put("route_id", route_id);
            List<Map<String, Object>> procedureList = inspectionTaskService.getProcedure(inspectiontask_id);
            if(procedureList == null || procedureList.size() == 0) {
                List<InsRouteProc> procList = inspectionRouteService.quInsProce(route_id);
                map.put("procedure", procList);
            } else {
                map.put("procedure", procedureList);
            }

            List<Map<String, Object>> safetyList = inspectionTaskService.getSafety(inspectiontask_id);
            if(safetyList == null || safetyList.size() == 0) {
                List<InsRouteSafe> safeList = inspectionRouteService.quInsSafe(route_id);
                map.put("safety", safeList);
            } else {
                map.put("safety", safetyList);
            }

            List<Map> toolsList = inspectionTaskService.getToolInfos(paramMap);
            if(toolsList != null && toolsList.size() > 0){
                for(int i = 0; i < toolsList.size(); i++){
                    Map mapTools = toolsList.get(i);
                    mapTools.put("tools_num", mapTools.get("tools_num1").toString() + "/" + mapTools.get("tools_num2").toString());
                    if(mapTools.containsKey("tools_remark1") && mapTools.get("tools_remark1").toString() != null && !"".equals(mapTools.get("tools_remark1").toString())){
                        if(mapTools.containsKey("tools_remark2") && mapTools.get("tools_remark2").toString() != null && !"".equals(mapTools.get("tools_remark2").toString())){
                            mapTools.put("tools_remark", mapTools.get("tools_remark1").toString() + "/" + mapTools.get("tools_remark2").toString());
                        } else {
                            mapTools.put("tools_remark", mapTools.get("tools_remark1").toString() + "/ ");
                        }
                    }else {
                        if(mapTools.containsKey("tools_remark2") && mapTools.get("tools_remark2").toString() != null && !"".equals(mapTools.get("tools_remark2").toString())){
                            mapTools.put("tools_remark", " /" + mapTools.get("tools_remark2").toString());
                        }
                    }
                }
            } else {
                Map map1 = new HashMap();
                map1.put("inspectiontask_id", inspectiontask_id);
                toolsList.add(map1);
            }
            map.put("tools", toolsList);

            List<Map> sparepartsList = inspectionTaskService.getSparepartsInfos(paramMap);
            if(sparepartsList != null && sparepartsList.size() > 0){
                for(int i = 0; i < sparepartsList.size(); i++){
                    Map mapTools = sparepartsList.get(i);
                    mapTools.put("spareparts_num", mapTools.get("spareparts_num1").toString() + "/" + mapTools.get("spareparts_num2").toString());
                    if(mapTools.containsKey("spareparts_remark1") && mapTools.get("spareparts_remark1").toString() != null && !"".equals(mapTools.get("spareparts_remark1").toString())){
                        if(mapTools.containsKey("spareparts_remark2") && mapTools.get("spareparts_remark2").toString() != null && !"".equals(mapTools.get("spareparts_remark2").toString())){
                            mapTools.put("spareparts_remark", mapTools.get("spareparts_remark1").toString() + "/" + mapTools.get("spareparts_remark2").toString());
                        } else {
                            mapTools.put("spareparts_remark", mapTools.get("spareparts_remark1").toString() + "/ ");
                        }
                    }else {
                        if(mapTools.containsKey("spareparts_remark2") && mapTools.get("spareparts_remark2").toString() != null && !"".equals(mapTools.get("spareparts_remark2").toString())){
                            mapTools.put("spareparts_remark", " /" + mapTools.get("spareparts_remark2").toString());
                        }
                    }
                    mapTools.put("spareparts_total", mapTools.get("spareparts_total1").toString() + "/" + mapTools.get("spareparts_total2").toString());
                }
            } else {
                Map map1 = new HashMap();
                map1.put("inspectiontask_id", inspectiontask_id);
                sparepartsList.add(map1);
            }
            map.put("spareparts", sparepartsList);

            List<Map> personList = inspectionTaskService.getPersonInfos(paramMap);
            if(personList != null && personList.size() > 0){
                for(int i = 0; i < personList.size(); i++){
                    Map mapPerson = personList.get(i);
                    mapPerson.put("person_hours", mapPerson.get("person_hours1").toString() + "/" + mapPerson.get("person_hours2").toString());
                    mapPerson.put("person_hourprice", mapPerson.get("person_hourprice1").toString() + "/" + mapPerson.get("person_hourprice2").toString());
                    mapPerson.put("person_hourtotal", mapPerson.get("person_hourtotal1").toString() + "/" + mapPerson.get("person_hourtotal2").toString());
                    if(mapPerson.containsKey("person_postskill1") && mapPerson.get("person_postskill1").toString() != null && !"".equals(mapPerson.get("person_postskill1").toString())){
                        if(mapPerson.containsKey("person_postskill2") && mapPerson.get("person_postskill2").toString() != null && !"".equals(mapPerson.get("person_postskill2").toString())){
                            mapPerson.put("person_postskil", mapPerson.get("person_postskill1").toString() + "/" + mapPerson.get("person_postskill2").toString());
                        } else {
                            mapPerson.put("person_postskil", mapPerson.get("person_postskill1").toString() + "/ ");
                        }
                    }else {
                        if(mapPerson.containsKey("person_postskill2") && mapPerson.get("person_postskill2").toString() != null && !"".equals(mapPerson.get("person_postskill2").toString())){
                            mapPerson.put("person_postskil", " /" + mapPerson.get("person_postskill2").toString());
                        }
                    }
                    if(mapPerson.containsKey("person_remark1") && mapPerson.get("person_remark1").toString() != null && !"".equals(mapPerson.get("person_remark1").toString())){
                        if(mapPerson.containsKey("person_remark2") && mapPerson.get("person_remark2").toString() != null && !"".equals(mapPerson.get("person_remark2").toString())){
                            mapPerson.put("person_remark", mapPerson.get("person_remark1").toString() + "/" + mapPerson.get("person_remark2").toString());
                        } else {
                            mapPerson.put("person_remark", mapPerson.get("person_remark1").toString() + "/ ");
                        }
                    }else {
                        if(mapPerson.containsKey("person_remark2") && mapPerson.get("person_remark2").toString() != null && !"".equals(mapPerson.get("person_remark2").toString())){
                            mapPerson.put("person_remark", " /" + mapPerson.get("person_remark2").toString());
                        }
                    }
                }
            } else {
                Map map1 = new HashMap();
                map1.put("inspectiontask_id", inspectiontask_id);
                personList.add(map1);
            }
            map.put("personList", personList);

            List<Map> othersList = inspectionTaskService.getOtherexpenseInfos(paramMap);
            if(othersList != null && othersList.size() > 0) {
                for(int i = 0; i < othersList.size(); i++){
                    Map mapOthers = othersList.get(i);
                    mapOthers.put("otherexpenses_amount", mapOthers.get("otherexpenses_amount1").toString() + "/" + mapOthers.get("otherexpenses_amount2").toString());
                    if(mapOthers.containsKey("otherexpenses_remark1") && mapOthers.get("otherexpenses_remark1").toString() != null && !"".equals(mapOthers.get("otherexpenses_remark1").toString())){
                        if(mapOthers.containsKey("otherexpenses_remark2") && mapOthers.get("otherexpenses_remark2").toString() != null && !"".equals(mapOthers.get("otherexpenses_remark2").toString())){
                            mapOthers.put("otherexpenses_remark", mapOthers.get("otherexpenses_remark1").toString() + "/" + mapOthers.get("otherexpenses_remark2").toString());
                        } else {
                            mapOthers.put("otherexpenses_remark", mapOthers.get("otherexpenses_remark1").toString() + "/ ");
                        }
                    }else {
                        if(mapOthers.containsKey("otherexpenses_remark2") && mapOthers.get("otherexpenses_remark2").toString() != null && !"".equals(mapOthers.get("otherexpenses_remark2").toString())){
                            mapOthers.put("otherexpenses_remark", " /" + mapOthers.get("otherexpenses_remark2").toString());
                        }
                    }
                }
            } else {
                Map map1 = new HashMap();
                map1.put("inspectiontask_id", inspectiontask_id);
                othersList.add(map1);
            }
            map.put("othersList", othersList);
        }
        return map;
    }

    /**
     * 根据设备id获取标准库下的故障标准
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getFailureList")
    @Transactional
    public List<StandardFailure> getFailureList(){
        String dev_id = getPara("dev_id");
        List<StandardFailure> standardFailureList = inspectionTaskService.getFailureList(dev_id);
        return standardFailureList;
    }
}

