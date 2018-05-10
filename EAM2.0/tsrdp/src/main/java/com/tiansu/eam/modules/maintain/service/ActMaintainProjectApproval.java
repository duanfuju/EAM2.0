package com.tiansu.eam.modules.maintain.service;

import com.tiansu.eam.modules.opestandard.service.StandardOrderService;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @creator duanfuju
 * @createtime 2017/11/14 14:45
 * @description:
 **/
public class ActMaintainProjectApproval implements TaskListener {
    private Expression approveReason;
    private Expression deptLeaderApproved;
    private Expression pIid;

    private MaintainProjectService maintainProjectService ;


    public void notify(DelegateTask delegateTask) {
        ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        maintainProjectService = (MaintainProjectService) context .getBean("maintainProjectService");

        String approve = (String) deptLeaderApproved.getValue(delegateTask);
        String piid=(String)pIid.getValue(delegateTask);
        String appReason=(String)approveReason.getValue(delegateTask);
        String createBy= UserUtils.getUser().getLoginname();
        Map<String,Object> map=new HashMap<String,Object>();
        if(approve !=null&& !"".equals(approve)){
            if(approve.equals("true")){
                map.put("project_status",2);
            }else{
                map.put("project_status",3);
            }
        }
        map.put("project_sp_empid",createBy);
        map.put("project_reason",appReason);
        map.put("pstid",piid);
        String time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        map.put("project_sp_time",time);

        map.put("update_by",createBy);
        map.put("update_time",time);
        maintainProjectService.approvalsp(map);

    }

}
