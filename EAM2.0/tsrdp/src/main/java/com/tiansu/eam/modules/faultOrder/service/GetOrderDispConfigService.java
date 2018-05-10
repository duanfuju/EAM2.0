package com.tiansu.eam.modules.faultOrder.service;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.modules.faultOrder.dao.FaultOrderDao;
import com.tiansu.eam.modules.sys.dao.SysConfigDao;
import com.tiansu.eam.modules.sys.entity.SysConfigEntry;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * @author wangjl
 * @description
 * @create 2017-10-13 11:37
 **/

public class GetOrderDispConfigService implements JavaDelegate {


    /**
     * 实现JavaDelegate接口，使用其中的execute方法 由于要放入流程定义中，所以要实现可序列话接口
     *
     */
    private static final long serialVersionUID = 5593437463482732772L;

    /** 流程变量 工单派发方式**/
    private Expression disp_type;



    private static FaultOrderDao faultOrderDao = SpringContextHolder.getBean(FaultOrderDao.class);

    private static SysConfigDao sysConfigDao = SpringContextHolder.getBean(SysConfigDao.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("---------------------------------------------");
        SysConfigEntry configEntry = sysConfigDao.getByKeyName(EAMConsts.REPAIR_ORDER_DISP_TYPE);
        String disp_type = EAMConsts.MANUAL_DISP_TYPE;
        if(configEntry != null){
            disp_type = configEntry.getConfig_value();
        }
        delegateExecution.setVariable("disp_type", disp_type);
        System.out.println("--------------------故障工单派单方式为["+disp_type+"]-------------------------");
    }
}
