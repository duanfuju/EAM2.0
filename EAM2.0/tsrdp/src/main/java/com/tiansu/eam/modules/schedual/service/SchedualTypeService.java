package com.tiansu.eam.modules.schedual.service;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.Page;
import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.schedual.dao.SchedualTypeDao;
import com.tiansu.eam.modules.schedual.entity.SchedualType;
import com.tiansu.eam.modules.schedual.entity.SchedualTypeTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tiansu.eam.modules.sys.utils.UserUtils.getDataScopeDeptIds;

/**
 * @author wangjl
 * @description 排班类型服务类
 * @create 2017-08-21 8:40
 *
 **/
@Service
@Transactional(readOnly = true)
public class SchedualTypeService extends CrudService<SchedualTypeDao,SchedualType>{

    @Autowired SchedualTypeDao schedualTypeDao;

    public List<SchedualType> getSchedualType(SchedualType type) {
        return schedualTypeDao.findAllList(type);
    }

    public List<SchedualTypeTime> getSchedualTimes(List<Map> schedualTypeMap){
        return schedualTypeDao.getSchedualTimes(schedualTypeMap);
    }
/*
    @Override
    public Page setOrderBy(Page page, HttpServletRequest request){
//        page.setOrderBy("create_time desc");
        return page;
    }*/

    public void deleteByTypeId(String id) {
        schedualTypeDao.deleteByTypeId(id);
    }

    public void batchSaveTime(List<SchedualTypeTime> schedual_time_list) {
        schedualTypeDao.batchSaveTime(schedual_time_list);
    }

    /**
     * @creator duanfuju
     * @createtime 2017/10/12 9:01
     * @description:
     *      批量插入
     * @param schedualTypes
     * @return
     */
    public int insertBatch(List<SchedualType> schedualTypes){
       return  schedualTypeDao.insertBatch(schedualTypes);
    }

    /**
     * @creator duanfuju
     * @createtime 2017/10/13 11:30
     * @description:
     * @param map
     * @return
     */
    public  List<Map> findListByMap(Map map){
       return  dao.findListByMap(map);
    }

    /**
     * @creator duanfuju
     * @createtime 2017/11/2 10:52
     * @description:
     *      获取导出数据 = =
     * @param map
     * @return
     */
    public  List<Map> findListByMapForExport(Map map){
        List<Map> result=dao.findListByMap(map);
        if(result!=null && result.size() > 0){
            List<SchedualTypeTime> timeList = dao.getSchedualTimes(result);
            for(int i = 0;i<result.size();i++){
                SchedualType type = (SchedualType) result.get(i);
                type.setSchedual_time_list(getTimesByTypeId(type.getId(),timeList));
            }
        }
        return  result;
    }
    @Override
    public List<Map> getPageTableData(Map param) {
        //分页查询，主子表分两次查询
        //查询主表
        List<Map> schedualTypeList = dao.findListByMap(param);
        if(schedualTypeList!=null && schedualTypeList.size() > 0){
            List<SchedualTypeTime> timeList = dao.getSchedualTimes(schedualTypeList);
            for(int i = 0;i<schedualTypeList.size();i++){
                SchedualType type = (SchedualType) schedualTypeList.get(i);
                type.setSchedual_time_list(getTimesByTypeId(type.getId(),timeList));
            }
        }
        return schedualTypeList;
    }

    /**
     * 根据排班类型id获取子表排班时间数据
     * @param id
     * @param times
     */
    private List<SchedualTypeTime> getTimesByTypeId(String id,List<SchedualTypeTime> times) {
        List<SchedualTypeTime> resultTimes = Lists.newArrayList();
        if(times != null && times.size()>0){
            for(SchedualTypeTime time : times){
                if(id.equals(time.getType_id())){
                    resultTimes.add(time);
                }
            }
        }
        return resultTimes;
    }
}
