package com.tiansu.eam.modules.schedual.service;

import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.schedual.dao.SchedualOrderDao;
import com.tiansu.eam.modules.schedual.entity.Schedual;
import com.tiansu.eam.modules.schedual.entity.SchedualImport;
import com.tiansu.eam.modules.schedual.entity.SchedualOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author wangjl
 * @description 排班单据服务类
 * @create 2017-08-21 8:40
 *
 **/
@Service
@Transactional(readOnly = true)
public class SchedualOrderService extends CrudService<SchedualOrderDao,SchedualOrder>{

    @Autowired
    SchedualOrderDao schedualOrderDao;

    /**
     * @creator duanfuju
     * @createtime 2017/10/12 15:35
     * @description:
     *      批量插入
     * @param schedualExports
     * @return
     */
    public int insertBatch(List<SchedualImport> schedualExports){
        return  schedualOrderDao.insertBatch(schedualExports);
    }
    /**
     * @creator duanfuju
     * @createtime 2017/10/11 8:39
     * @description:
     *       获取导出数据
     * @param map
     * @return
     */
    public List<Map> getExportData(Map map){
        return schedualOrderDao.getExportData(map);
    }


    /**
     * @creator duanfuju
     * @createtime 2017/10/12 16:10
     * @description:
     *      用于导出去重
     * @param schedualImports
     * @return
     */
    public  List<SchedualImport> removeDupliByOrderCode(List<SchedualImport> schedualImports) {
        Set<SchedualImport> schedualImportsSet = new TreeSet<>((o1, o2) -> o1.getOrder_code().compareTo(o2.getOrder_code()));
        schedualImportsSet.addAll(schedualImports);
        return new ArrayList<>(schedualImportsSet);
    }
    
    /**
     * 根据派单单据其获取排班单据信息
     * @param schedualOrder
     * @return
     */
    public SchedualOrder getSchedualOrder(SchedualOrder schedualOrder) {
        return schedualOrderDao.get(schedualOrder.getId());
    }

    /**
     * 根据排班单获取排班记录
     * @param schedualOrder
     * @return
     */
    public List<Schedual> getSchedualList(SchedualOrder schedualOrder) {
        return schedualOrderDao.findListByOrder(schedualOrder);
    }


/*    @Override
    public Page setOrderBy(Page page, HttpServletRequest request){
        return super.setOrderBy(page,request);
//        return page;
    }*/
}
