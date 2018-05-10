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
 * @author zhangww
 * @description  流程结束完相关信息入业务表
 * @create 2017-09-22 9:36
 **/
public class ActStandardOrderAfter implements TaskListener {
    private Expression approveReason;
    private Expression deptLeaderApproved;
    private Expression pIid;

    private StandardOrderService standardOrderService ;


    public void notify(DelegateTask delegateTask) {
        ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        standardOrderService = (StandardOrderService) context .getBean("standardOrderService");

        String approve = (String) deptLeaderApproved.getValue(delegateTask);
        String piid=(String)pIid.getValue(delegateTask);
        String appReason=(String)approveReason.getValue(delegateTask);
        String createBy= UserUtils.getUser().getLoginname();
        Map<String,Object> map=new HashMap<String,Object>();
        if(approve !=null&& !"".equals(approve)){
            if(approve.equals("true")){
                map.put("approve_status",1);
            }else{
                map.put("approve_status",2);
            }
        }
        map.put("approve_by",createBy);
        map.put("approve_reason",appReason);
        map.put("pstid",piid);
        String time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        map.put("approve_time",time);

        standardOrderService.updateAprByPIid(map);

    }

}
