package com.tiansu.eam.modules.faultOrder.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.faultOrder.entity.FaultSwitch;

/**
 * @author wangjl
 * @description 故障工单申请审批服务
 * @create 2017-08-21 8:39
 **/
@MyBatisDao
public interface FaultSwitchDao extends CrudDao<FaultSwitch> {



}
