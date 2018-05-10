package com.tiansu.eam.modules.inspection.service;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.modules.sys.entity.SysConfigEntry;
import com.tiansu.eam.modules.sys.service.SysConfigService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * @author wujh
 * @description 获取系统配置的巡检任务派单方式，并自动选择任务执行方式流程
 * @create 2017-10-16 15:25
 **/
public class InspectionTaskAutoDispService implements JavaDelegate {
    /**
     * 实现JavaDelegate接口，使用其中的execute方法 由于要放入流程定义中，所以要实现可序列话接口
     *
     */
    private static final long serialVersionUID = 5593437463482732772L;

    // 系统配置service
    private static SysConfigService sysConfigService = SpringContextHolder.getBean(SysConfigService.class);

    /** 流程变量**/
    private Expression disp_type;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        // 获取巡检任务配置方式
        SysConfigEntry sysConfigEntry = sysConfigService.getByKeyName(EAMConsts.INSPECT_ORDER_DISP_TYPE);
        String disp_type = EAMConsts.MANUAL_DISP_TYPE;    // 默认按责任人派单
        if(sysConfigEntry != null) {
            disp_type = sysConfigEntry.getConfig_value();
        }
        delegateExecution.setVariable("disp_type", disp_type);
    }
}
