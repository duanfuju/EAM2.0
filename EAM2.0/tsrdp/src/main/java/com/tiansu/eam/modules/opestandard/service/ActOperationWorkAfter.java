package com.tiansu.eam.modules.opestandard.service;

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
 * @author wujh
 * @description 标准工作审批流程之后，要同步审批信息到业务表操作
 * @create 2017-09-23 14:04
 **/
public class ActOperationWorkAfter implements TaskListener {

    // 审批结果  1 通过 2退回
    public static final String APPROVE_SUCCESS = "1";
    public static final String APPROVE_FAIL = "2";
    // 标准工作service
    private EamOperationWorkService eamOperationWorkService ;
    private Expression approveReason;
    private Expression deptLeaderApproved;
    private Expression pIid;

    public void setEamOperationWorkService(EamOperationWorkService eamOperationWorkService) {
        this.eamOperationWorkService = eamOperationWorkService;
    }

    public EamOperationWorkService getEamOperationWorkService() {
        return eamOperationWorkService;
    }

    public void notify(DelegateTask delegateTask) {
        ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        eamOperationWorkService = (EamOperationWorkService) context.getBean("eamOperationWorkService");

        String approve = (String) deptLeaderApproved.getValue(delegateTask);
        String piid = (String)pIid.getValue(delegateTask);
        String appReason = (String)approveReason.getValue(delegateTask);
        String createBy = UserUtils.getUser().getLoginname();
        Map<String,Object> map = new HashMap<String,Object>();
        if(approve !=null && !"".equals(approve)){
            if(approve.equals("true")){
                map.put("approve_status", APPROVE_SUCCESS);

            }else{
                map.put("approve_status", APPROVE_FAIL);
            }
        }
        map.put("approve_by",createBy);
        map.put("approve_reason",appReason);
        map.put("pstid",piid);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        map.put("approve_time",time);

        eamOperationWorkService.updateAprByPIid(map);

    }

}
