package com.tiansu.eam.modules.inspection.service;

import com.tiansu.eam.modules.act.service.EamProcessService;
import com.tiansu.eam.modules.inspection.entity.InspectionTask;
import com.tiansu.eam.modules.sys.entity.CodeEnum;
import com.tiansu.eam.modules.sys.utils.SequenceUtils;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangww
 * @description  流程结束完相关信息入业务表
 * @create 2017-09-22 9:36
 **/
@Service
@Transactional
public class ActInspectionRouteAfter implements TaskListener {
    private Expression approveReason;
    private Expression deptLeaderApproved;
    private Expression pIid;

    private InspectionRouteService inspectionRouteService;
    private InspectionTaskService inspectionTaskService;
    private EamProcessService eamProcessService;    // 工作进程service
    @Transactional
    public void notify(DelegateTask delegateTask) {
        ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        inspectionRouteService = (InspectionRouteService) context.getBean("inspectionRouteService");
        inspectionTaskService=(InspectionTaskService)context.getBean("inspectionTaskService");
        eamProcessService=(EamProcessService)context.getBean("eamProcessService");//流程service
        String approve = (String) deptLeaderApproved.getValue(delegateTask);
        String piid = (String) pIid.getValue(delegateTask);
        String appReason = (String) approveReason.getValue(delegateTask);
        String createBy = UserUtils.getUser().getLoginname();
        Map<String, Object> map = new HashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (approve != null && !"".equals(approve)) {
                if (approve.equals("true")) {
                    map.put("approve_status", 1);
                    //审批通过，生成巡检任务

                    Map inspectionRoute = inspectionRouteService.getLibByPIid(piid);
                    List<InspectionTask> inspectionTaskList = new ArrayList<InspectionTask>();
                    HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                    if (inspectionRoute.get("route_taskdates") != null) {
                        for (String task : inspectionRoute.get("route_taskdates").toString().split(",")) {
                            InspectionTask inspectionTask = new InspectionTask();
                            inspectionTask.setTask_code(SequenceUtils.getBySeqType(CodeEnum.INSPECTION_TASK));
                            inspectionTask.setRoute_id((String) inspectionRoute.get("id"));
                            inspectionTask.setTask_time_plan_begin(sdf.parse(task));
                            inspectionTask.setTask_time_plan_finish(sdf.parse(new SimpleDateFormat("yyyy-MM-dd").format(sdf.parse(task)) + " 23:59:59"));
                            inspectionTask.setTask_totalhour_plan(Double.parseDouble(inspectionRoute.get("route_totalhour").toString()));
                            inspectionTask.setTask_processor_plan((String) inspectionRoute.get("person"));
                            inspectionTask.preInsert();
                            String uuid=inspectionTask.getId();
                            inspectionTask.setId_key(uuid);
                            String pstid = eamProcessService.startProcessByPdid("inspectionFlow","eam_inspection_task",uuid,request);
                            inspectionTask.setPstid(pstid);
                            inspectionTaskList.add(inspectionTask);
                        }
                        inspectionTaskService.insert(inspectionTaskList);
                    }
                } else {
                    map.put("approve_status", 2);
                }
            }
            map.put("approve_by", createBy);
            map.put("approve_reason", appReason);
            map.put("pstid", piid);
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            map.put("approve_time", time);

            inspectionRouteService.updateAprByPIid(map);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
