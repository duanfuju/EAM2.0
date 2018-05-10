package com.tiansu.eam.modules.faultOrder.service;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.modules.faultOrder.controller.FaultOrderController;
import com.tiansu.eam.modules.faultOrder.dao.FaultOrderDao;
import com.tiansu.eam.modules.faultOrder.entity.FaultOrder;
import com.tiansu.eam.modules.faultOrder.entity.OrderStatusEnum;
import com.tiansu.eam.modules.sys.entity.User;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wangjl
 * @description
 * @create 2017-10-13 11:37
 *
 *
 * 步骤一，根据报修设备，判断设备归属专业，找到所有该专业的运维人员。（人员管理功能里专业为必填）
<br>步骤二，根据报修设备位置，匹配该专业下具有此责任区域的运维人员。（人员管理功能里责任区域为必填）。
<br>步骤三，根据排班表，匹配出当前在班人员。
<br>步骤四，如果在前三个判断条件中，未找到相关的派单对象，则派给相关专业（报修设备所归属的专业）运维班长和主管，由班长或主管进行转单操作。
<br>班长或主管进行转单操作：是指针派给运维班长和主管的单子，由班长或主管进行接单人员（可以看到全部运维人员）的选择，从而完成转单操作。
<br>备注：自动派单为即时（30秒之内）派单。

 **/
@Service
public class FaultOrderAutoDispService implements JavaDelegate {


    /**
     * 实现JavaDelegate接口，使用其中的execute方法 由于要放入流程定义中，所以要实现可序列话接口
     *
     */
    private static final long serialVersionUID = 5593437463482732772L;

    /** 流程变量**/
    private Expression disp_type;

    @Autowired
    FaultOrderService faultOrderService= SpringContextHolder.getBean(FaultOrderService.class);;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
//      获取报修表单数据
        FaultOrder cachedFaultOrder = (FaultOrder) delegateExecution.getVariable(FaultOrderController.NOTIFY_FORM_DATA);
        FaultOrder faultOrder = faultOrderService.get(cachedFaultOrder.getId());
        if(faultOrder == null){
            faultOrder = cachedFaultOrder;
        }

        FaultOrder newOrder = new FaultOrder();
        newOrder.setId(faultOrder.getId());
        faultOrder.setOrder_status(OrderStatusEnum.PENDING_ACCEPT.value());
        faultOrder.setOrder_dispatcher(new User(null,"系统"));
        faultOrder.setOrder_dispatch_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        faultOrder.preUpdate();
        faultOrderService.update(faultOrder);
    }

    /**
     * 获取自动派单的参与者
     * @param faultOrder
     * @return
     *
     */
    private List<String> getParticipants(FaultOrder faultOrder) {
        List<String> result = new ArrayList<>();
        result.add("tiger");
//        TODO Step1:找到相关专业的用户



        return result;
    }
}
