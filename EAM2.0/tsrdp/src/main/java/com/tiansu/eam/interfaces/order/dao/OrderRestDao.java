package com.tiansu.eam.interfaces.order.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.interfaces.common.PersonJson;
import com.tiansu.eam.interfaces.common.OtherChargesJosn;
import com.tiansu.eam.interfaces.common.Tool_MaterialsJson;
import com.tiansu.eam.interfaces.order.entity.Order;
import com.tiansu.eam.interfaces.order.entity.OrderDetailsJson;
import com.tiansu.eam.interfaces.order.entity.OrderListJson;
import com.tiansu.eam.modules.faultOrder.entity.FaultSwitch;

import java.util.List;
import java.util.Map;

/**
 * @author wangr
 * @description
 * @create 2017-10-11 下午 4:51
 */
@MyBatisDao
public interface OrderRestDao extends CrudDao<Order> {


    /**
     * @param map
     * @return
     * @creator wangr
     * @createtime 2017/10/17 0017 下午 2:02
     * @description: 获取我的报修列表
     */
    public List<OrderListJson> findMyRepList(Map map);

    /**
     * @creator wangr
     * @createtime 2017/10/25 0025 下午 7:24
     * @description: 我的工单列表
     * @param map
     * @return
     */
    public List<OrderListJson> findMyOrderList(Map map);

    /**
     * @param map
     * @return
     * @creator wangr
     * @createtime 2017/10/25 0025 下午 7:25
     * @description: 我的工单详情
     */
    public List<OrderDetailsJson> findMyOrderDetail(Map map);

    /**
     * @creator wangr
     * @createtime 2017/10/25 0025 下午 7:25
     * @description: 工单工器具-计划
     * @param orderId
     * @return
     */
    public List<Tool_MaterialsJson> findOrderTool(String orderId);

    /**
     * @creator wangr
     * @createtime 2017/10/25 0025 下午 7:25
     * @description: 工单工器具-反馈
     * @param orderId
     * @return
     */
    public List<Tool_MaterialsJson> findOrderToolFK(String orderId);


    /**
     * @creator wangr
     * @createtime 2017/10/25 0025 下午 7:25
     * @description: 工单备件-计划
     * @param orderId
     * @return
     */
    public List<Tool_MaterialsJson> findOrderSpareparts(String orderId);

    /**
     * @creator wangr
     * @createtime 2017/10/25 0025 下午 7:25
     * @description: 工单备件-反馈
     * @param orderId
     * @return
     */
    public List<Tool_MaterialsJson> findOrderSparepartsFK(String orderId);

    /**
     * @creator wangr
     * @createtime 2017/10/25 0025 下午 7:28
     * @description: 工单人员工时-计划
     * @param orderId
     * @return
     */
    public List<PersonJson> findOrderPerson(String orderId);

    /**
     * @creator wangr
     * @createtime 2017/10/25 0025 下午 7:28
     * @description: 工单人员工时-反馈
     * @param orderId
     * @return
     */
    public List<PersonJson> findOrderPersonFK(String orderId);

    /**
     * @creator wangr
     * @createtime 2017/10/25 0025 下午 7:29
     * @description: 工单其他费用-计划
     * @param orderId
     * @return
     */
    public List<OtherChargesJosn> findOrderOtherCharges(String orderId);

    /**
     * @creator wangr
     * @createtime 2017/10/25 0025 下午 7:29
     * @description: 工单其他费用-反馈
     * @param orderId
     * @return
     */
    public List<OtherChargesJosn> findOrderOtherChargesFK(String orderId);

    /**
     * @creator wangr
     * @createtime 2017/11/14 0014 下午 1:43
     * @description: 获取工单申请信息
     * @param param
     * @return
     */
    public FaultSwitch getFaultSwitch(Map param);

}
