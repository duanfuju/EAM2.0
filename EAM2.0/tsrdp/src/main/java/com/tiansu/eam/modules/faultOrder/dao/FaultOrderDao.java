package com.tiansu.eam.modules.faultOrder.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.faultOrder.entity.*;

import java.util.List;

/**
 * @author wangjl
 * @description
 * @create 2017-08-21 8:39
 **/
@MyBatisDao
public interface FaultOrderDao extends CrudDao<FaultOrder> {


//  派单计划消耗
    public int insertPlanTool(List<OrderTool> list);
    public int insertPlanSparepart(List<OrderSparepart> list);
    public int insertPlanPerson(List<OrderPerson> list);
    public int insertPlanOther(List<OrderOther> list);
//  派单实际消耗
    public int insertActualTool(List<OrderTool> list);
    public int insertActualSparepart(List<OrderSparepart> list);
    public int insertActualPerson(List<OrderPerson> list);
    public int insertActualOther(List<OrderOther> list);
}
