package com.tiansu.eam.modules.faultOrder.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.faultOrder.dao.FaultOrderDao;
import com.tiansu.eam.modules.faultOrder.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author wangjl
 * @description 排班类型服务类
 * @create 2017-08-21 8:40
 *
 **/
@Service
@Transactional(readOnly = true)
public class FaultOrderService extends CrudService<FaultOrderDao,FaultOrder>{

    @Autowired
    FaultOrderDao faultOrderDao;

    @Override
    public Page setOrderBy(Page page, HttpServletRequest request){
        page.setOrderBy(" a.order_level asc,a.create_time desc");
        return page;
    }

    @Transactional
    public void insertPlanDetail(List<OrderTool> list1, List<OrderSparepart> list2, List<OrderPerson> list3, List<OrderOther> list4){
        if(list1 != null && list1.size() >0){
            faultOrderDao.insertPlanTool(list1);
        }
        if(list2 != null && list2.size() >0){
            faultOrderDao.insertPlanSparepart(list2);
        }
        if(list3 != null && list3.size() >0){
            faultOrderDao.insertPlanPerson(list3);
        }
        if(list4 != null && list4.size() >0){
            faultOrderDao.insertPlanOther(list4);
        }
    }


    @Transactional
    public void insertActualDetail(List<OrderTool> list1, List<OrderSparepart> list2, List<OrderPerson> list3, List<OrderOther> list4){
        if(list1 != null && list1.size() >0){
            faultOrderDao.insertActualTool(list1);
        }
        if(list2 != null && list2.size() >0){
            faultOrderDao.insertActualSparepart(list2);
        }
        if(list3 != null && list3.size() >0){
            faultOrderDao.insertActualPerson(list3);
        }
        if(list4 != null && list4.size() >0){
            faultOrderDao.insertActualOther(list4);
        }
    }


}
