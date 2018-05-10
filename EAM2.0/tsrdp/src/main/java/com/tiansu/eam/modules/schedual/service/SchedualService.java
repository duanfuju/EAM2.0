package com.tiansu.eam.modules.schedual.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.schedual.dao.SchedualDao;
import com.tiansu.eam.modules.schedual.entity.Schedual;
import com.tiansu.eam.modules.schedual.entity.SchedualImport;
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
public class SchedualService extends CrudService<SchedualDao,Schedual>{

    @Autowired SchedualDao schedualDao;

    public List<Schedual> getSchedual(Schedual schedual) {
        return schedualDao.findAllList(schedual);
    }


    @Override
    public Page setOrderBy(Page page, HttpServletRequest request){
        page.setOrderBy("create_time desc");
        return page;
    }



    public void batchSave(List<Schedual> schedualList) {
        dao.batchInsert(schedualList);
    }

    public void deleteByOrderId(String order_id) {
        dao.deleteByOrderId(order_id);
    }

    /**
     * @creator duanfuju
     * @createtime 2017/10/12 15:35
     * @description:
     *      批量插入
     * @param schedualExports
     * @return
     */
    public int insertBatch(List<SchedualImport> schedualExports){
        return  schedualDao.insertBatch(schedualExports);
    }

}
