package com.tiansu.eam.modules.inspection.service;

import com.tiansu.eam.modules.act.service.EamProcessService;
import com.tiansu.eam.modules.inspection.entity.InspectionTask;
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
public class ActInspectionRouteClosedAfter implements TaskListener {
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (approve != null && !"".equals(approve)) {
                if (approve.equals("true")) {
                    map.put("isClosed", 2);
                } else {
                    map.put("isClosed", -1);
                }
            }
            map.put("close_reason", appReason);
            map.put("pstidclose", piid);
            inspectionRouteService.updateCloseByPIid(map);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
