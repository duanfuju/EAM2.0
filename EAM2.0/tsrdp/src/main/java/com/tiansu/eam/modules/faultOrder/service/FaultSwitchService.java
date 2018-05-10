package com.tiansu.eam.modules.faultOrder.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.faultOrder.dao.FaultSwitchDao;
import com.tiansu.eam.modules.faultOrder.entity.FaultSwitch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wangjl
 * @description 故障工单申请审批服务类
 * @create 2017-08-21 8:40
 *
 **/
@Service
@Transactional(readOnly = true)
public class FaultSwitchService extends CrudService<FaultSwitchDao,FaultSwitch>{

    @Autowired
    FaultSwitchDao faultSwitchDao;

    @Override
    public Page setOrderBy(Page page, HttpServletRequest request){
        page.setOrderBy(" a.id desc");
        return page;
    }


}
