package com.tiansu.eam.modules.maintain.service;

import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.inspection.dao.InspectionTaskDao;
import com.tiansu.eam.modules.inspection.entity.InspectionTask;
import com.tiansu.eam.modules.maintain.dao.MaintainTaskDao;
import com.tiansu.eam.modules.maintain.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author caoh
 * @description 保养任务服务类
 * @create 2017-10-13 11:06
 **/
@Service
public class MaintainTaskService extends CrudService<MaintainTaskDao, MaintainTask> {

    @Autowired
    private MaintainTaskDao maintainTaskDao;


    /**
     * @creator caoh
     * @createtime 2017-10-31 10:21
     * @description: 查询单个保养任务中的保养内容
     * @param id
     * @return
     */
    public List<Map> findMaintainProject(String id){
        return maintainTaskDao.findMaintainProject(id);
    }


    /**
     * @creator caoh
     * @createtime 2017-11-2 11:18
     * @description: 获取反馈页面数据
     * @param id
     * @return
     */
    public Map getFeedBackDatas(String id){
        return maintainTaskDao.getFeedBackDatas(id);
    }

    @Transactional
    public void insertActualDetail(List<MaintainTool> list1, List<MaintainAttachment> list2, List<MaintainEmp> list3, List<MaintainOther> list4){
        if(list1 != null && list1.size() >0){
            maintainTaskDao.insertActualTool(list1);
        }
        if(list2 != null && list2.size() >0){
            maintainTaskDao.insertActualSparepart(list2);
        }
        if(list3 != null && list3.size() >0){
            maintainTaskDao.insertActualPerson(list3);
        }
        if(list4 != null && list4.size() >0){
            maintainTaskDao.insertActualOther(list4);
        }
    }

//生成多个保养任务
    public void insertTask(List<MaintainTask> list){
        maintainTaskDao.insertTask(list);
    }

}
