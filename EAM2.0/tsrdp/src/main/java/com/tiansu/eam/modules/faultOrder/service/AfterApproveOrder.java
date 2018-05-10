package com.tiansu.eam.modules.faultOrder.service;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.web.Servlets;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.modules.faultOrder.controller.FaultOrderController;
import com.tiansu.eam.modules.faultOrder.dao.FaultOrderDao;
import com.tiansu.eam.modules.faultOrder.dao.FaultSwitchDao;
import com.tiansu.eam.modules.faultOrder.entity.FaultOrder;
import com.tiansu.eam.modules.faultOrder.entity.FaultSwitch;
import com.tiansu.eam.modules.faultOrder.entity.OrderStatusEnum;
import com.tiansu.eam.modules.opestandard.controller.StandardOrderController;
import com.tiansu.eam.modules.opestandard.entity.StandardOrder;
import com.tiansu.eam.modules.opestandard.service.StandardOrderService;
import com.tiansu.eam.modules.sys.entity.CodeEnum;
import com.tiansu.eam.modules.sys.entity.User;
import com.tiansu.eam.modules.sys.utils.SequenceUtils;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangjl
 * @description 工单派单后动作
 * @create 2017-10-16 10:02
 **/
public class AfterApproveOrder implements TaskListener {
    private Expression result;
    private Expression remark;
    private Expression opType;

    private static FaultSwitchDao faultSwitchDao = SpringContextHolder.getBean(FaultSwitchDao.class);

    private static FaultOrderDao faultOrderDao = SpringContextHolder.getBean(FaultOrderDao.class);

    public void notify(DelegateTask delegateTask) {
//        获取报修表单数据
        FaultOrder faultOrder = (FaultOrder) delegateTask.getVariable(FaultOrderController.NOTIFY_FORM_DATA);
        String opTypeStr = (String) opType.getValue(delegateTask);
        boolean approved = "1".equals(result.getValue(delegateTask));
        FaultOrder newOrder = new FaultOrder();
        if(faultOrder!=null){
            newOrder.setId(faultOrder.getId());
            newOrder.preUpdate();
        }
        if("转单".equals(opTypeStr)){
            //拒绝，则直接进入待到达状态
            if(!approved){
                newOrder.setOrder_status(OrderStatusEnum.PENDING_ARRIVE.value());
                FaultSwitch faultSwitch = (FaultSwitch) delegateTask.getVariable(FaultOrderController.TRANSFER_FORM_DATA);
                if(faultSwitch!=null){
                    newOrder.setOrder_solver(new User(null,faultSwitch.getRequest_person()));
                    newOrder.setOrder_taking_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                }
            }else{
                newOrder.setOrder_status(OrderStatusEnum.PENDING_DISP.value());
            }
            updateSwitchInfo(EAMConsts.ORDER_SWITCH_TRANSFER,delegateTask);
        }else if("挂起".equals(opTypeStr)){
            /** 挂起审批 **/
            newOrder.setOrder_status(approved? OrderStatusEnum.HANGUP.value():OrderStatusEnum.PENDING_FINISH.value());
            updateSwitchInfo(EAMConsts.ORDER_SWITCH_HANGUP,delegateTask);
        }else if("解挂".equals(opTypeStr)){
            /** 解挂 **/
            newOrder.setOrder_status(OrderStatusEnum.PENDING_FINISH.value());
            updateSwitchInfo(EAMConsts.ORDER_SWITCH_RELIEVE,delegateTask);
        }else if("验收".equals(opTypeStr)) {
            /** 验收审批 **/
            newOrder.setOrder_status(approved? OrderStatusEnum.CHECKED.value():OrderStatusEnum.PENDING_FINISH.value());
            updateSwitchInfo(EAMConsts.ORDER_SWITCH_CHECK, delegateTask);
        }else if("归档".equals(opTypeStr)) {
            /** 归档审批 **/
            newOrder.setOrder_status(approved? OrderStatusEnum.ARCHIVED.value():OrderStatusEnum.EVALUATEED.value());
            if(approved){
                StandardOrder standardOrder = new StandardOrder();
                if(faultOrder!=null){
                    standardOrder.preInsert();
                    standardOrder.setId(faultOrder.getId());
                    standardOrder.setOrder_code(SequenceUtils.getBySeqType(CodeEnum.STAND_ORDER));
                    standardOrder.setOrder_content("故障工单标准单_"+faultOrder.getOrder_code());
                    standardOrder.setOrder_desc(faultOrder.getNotifier_appearance());
                    standardOrder.setOrder_code(SequenceUtils.getBySeqType(CodeEnum.STAND_ORDER));
//                    standardOrder.setOrder_major();
                    //TODO 统一性有待适配
                    if("0".equals(faultOrder.getOrder_level())){
                        standardOrder.setOrder_priority("1");
                    }else if("1".equals(faultOrder.getOrder_level())){
                            standardOrder.setOrder_priority("0");
                    }else if("2".equals(faultOrder.getOrder_level())){
                        standardOrder.setOrder_priority("2");
                    }


//                    standardOrder.setOrder_remark();
                    standardOrder.setOrder_status("1"); //有效
                    standardOrder.setApprove_status("1");//已审批
//                    standardOrder.setOrder_totalhours();
                    StandardOrderService standardOrderService = SpringContextHolder.getBean(StandardOrderService.class);
//                    new StandardOrderController().insert(standardOrder, Servlets.getRequest());
                    standardOrderService.insert(standardOrder);
                }
                updateSwitchInfo(EAMConsts.ORDER_SWITCH_ARCHIVE, delegateTask);
            }


        }
        //设置流程转向
        delegateTask.setVariable("approved",approved?"1":"0");
        faultOrderDao.update(newOrder);

    }

    /**
     * 更新switch审批信息
     * * @param switch_type:需审批的操作类型 1-转单 2-挂起 3-解挂 4-验收 5-归档
     * @return
     */
    private FaultSwitch updateSwitchInfo(String switch_type, DelegateTask delegateTask) {
        FaultSwitch faultSwitch = null;
        if(EAMConsts.ORDER_SWITCH_TRANSFER.equals(switch_type)){
            faultSwitch = (FaultSwitch) delegateTask.getVariable(FaultOrderController.TRANSFER_FORM_DATA);
        }else if(EAMConsts.ORDER_SWITCH_HANGUP.equals(switch_type)){
            faultSwitch = (FaultSwitch) delegateTask.getVariable(FaultOrderController.HANGUP_FORM_DATA);
        }
        if(faultSwitch != null){
            faultSwitch.preUpdate();
            if(EAMConsts.ORDER_SWITCH_TRANSFER.equals(switch_type) ||
                    EAMConsts.ORDER_SWITCH_HANGUP.equals(switch_type) ||
                    EAMConsts.ORDER_SWITCH_CHECK.equals(switch_type) ||
                    EAMConsts.ORDER_SWITCH_ARCHIVE.equals(switch_type)){
                faultSwitch.setApprove_person(delegateTask.getAssignee());
                faultSwitch.setApprove_time(new Date());
                String approved = (String) result.getValue(delegateTask);
                if("0".equals(approved)){
                    faultSwitch.setApprove_status(EAMConsts.ORDER_REJECTED);
                }else{
                    faultSwitch.setApprove_status(EAMConsts.ORDER_APPROVED);
                }
                faultSwitch.setApprove_info((String) remark.getValue(delegateTask));
            }else if(EAMConsts.ORDER_SWITCH_RELIEVE.equals(switch_type)){
                //解挂
//                faultSwitch.setRelieve_time(new Date());
            }
            faultSwitchDao.update(faultSwitch);


        }

        return faultSwitch;
    }

}
