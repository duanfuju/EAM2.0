package com.tiansu.eam.modules.faultOrder.service;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.modules.faultOrder.controller.FaultOrderController;
import com.tiansu.eam.modules.faultOrder.dao.FaultOrderDao;
import com.tiansu.eam.modules.faultOrder.dao.FaultSwitchDao;
import com.tiansu.eam.modules.faultOrder.entity.FaultOrder;
import com.tiansu.eam.modules.faultOrder.entity.FaultSwitch;
import com.tiansu.eam.modules.faultOrder.entity.OrderStatusEnum;
import com.tiansu.eam.modules.sys.entity.User;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangjl
 * @description 工单申请后动作
 * @create 2017-10-16 10:02
 **/
public class AfterRequestOrder implements TaskListener {
    //操作类型：接单转单-transfer ;挂起-hangup; 解挂relieve 验收 check
    private Expression opType;
    private Expression result;
    private Expression reason;

    private static FaultOrderDao faultOrderDao = SpringContextHolder.getBean(FaultOrderDao.class);

    private static FaultSwitchDao faultSwitchDao = SpringContextHolder.getBean(FaultSwitchDao.class);

    public void notify(DelegateTask delegateTask) {
//        获取报修表单数据
        FaultOrder faultOrder = (FaultOrder) delegateTask.getVariable(FaultOrderController.NOTIFY_FORM_DATA);
        String opTypeStr = (String) opType.getValue(delegateTask);
        FaultOrder newOrder = new FaultOrder();
        if(faultOrder != null){
            newOrder.setId(faultOrder.getId());
            newOrder.preUpdate();
        }
        if("1".equals(opTypeStr)){
            /** 处理接单/转单的Request **/
            if("0".equals(result.getValue(delegateTask))){
                //接单
                newOrder.setOrder_solver(new User(null,delegateTask.getAssignee()));
                newOrder.setOrder_taking_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                newOrder.setOrder_status(OrderStatusEnum.PENDING_ARRIVE.value());
            }else{//转单
                newOrder.setOrder_status(OrderStatusEnum.PENDING_TRANSFER.value());
                FaultSwitch faultSwitch = saveSwitchInfo(EAMConsts.ORDER_SWITCH_TRANSFER,delegateTask,faultOrder);
                delegateTask.setVariable(FaultOrderController.TRANSFER_FORM_DATA,faultSwitch);
            }
            //设置流程转向
            delegateTask.setVariable("transfer","0".equals(result.getValue(delegateTask))?"0":"1");
        }else if("2".equals(opTypeStr)){
            /** 处理挂起/反馈的Request **/
            if("0".equals(result.getValue(delegateTask))){
                //反馈动作：复杂表单交由controller处理
            }else{
                //保存挂起申请信息
                newOrder.setOrder_status(OrderStatusEnum.PENDING_HANGUP.value());
                FaultSwitch faultSwitch = saveSwitchInfo(EAMConsts.ORDER_SWITCH_HANGUP,delegateTask,faultOrder);
                delegateTask.setVariable(FaultOrderController.HANGUP_FORM_DATA,faultSwitch);
            }
            //设置流程转向
            delegateTask.setVariable("hangup","0".equals(result.getValue(delegateTask))?"0":"1");
        }else if("解挂".equals(opTypeStr)){
            FaultSwitch faultSwitch = (FaultSwitch) delegateTask.getVariable(FaultOrderController.HANGUP_FORM_DATA);
            if(faultSwitch != null){
                faultSwitch.preUpdate();
                faultSwitch.setSwitch_type("3");
                faultSwitch.setRelieve_time(new Date());
                faultSwitchDao.update(faultSwitch);
            }
            newOrder.setOrder_status(OrderStatusEnum.PENDING_FINISH.value());
        }
        faultOrderDao.update(newOrder);

    }

    /**
     * 保存switch信息
     * * @param switch_type:需审批的操作类型 1-转单 2-挂起 3-解挂 4-验收 5-归档
     * @return
     */
    private FaultSwitch saveSwitchInfo(String switch_type,DelegateTask delegateTask, FaultOrder faultOrder) {
        FaultSwitch faultSwitch = new FaultSwitch();
        faultSwitch.preInsert();
        faultSwitch.setOrder_id(faultOrder.getId());
//        if(EAMConsts.ORDER_SWITCH_TRANSFER.equals(switch_type)){
            faultSwitch.setRequest_person(delegateTask.getAssignee());
            faultSwitch.setSwitch_type(switch_type);
            faultSwitch.setSwitch_time(new Date());
            faultSwitch.setApprove_status(EAMConsts.ORDER_PENDING_APPROVE);
            faultSwitch.setSwitch_reason((String) reason.getValue(delegateTask));
//        }
        faultSwitchDao.insert(faultSwitch);
        return faultSwitch;
    }


}
