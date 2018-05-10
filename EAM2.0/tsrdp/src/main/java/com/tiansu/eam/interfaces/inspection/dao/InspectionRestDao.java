package com.tiansu.eam.interfaces.inspection.dao;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.interfaces.common.*;
import com.tiansu.eam.interfaces.inspection.entity.*;
import com.tiansu.eam.modules.inspection.entity.InspectionTaskSwitch;
import org.activiti.engine.task.Task;

import java.util.List;
import java.util.Map;

/**
 * @author wangr
 * @description 巡检对外接口持久化层
 * @create 2017-10-30 上午 10:42
 */
@MyBatisDao
public interface InspectionRestDao {

    /**
     * @param map
     * @return
     * @creator wangr
     * @createtime 2017/10/30 0030 上午 11:20
     * @description: 查询我的巡检任务
     */
    List<InspectionListJson> findMyInspection(Map map);

    /**
     * @creator wangr
     * @createtime 2017/10/31 0031 上午 10:56
     * @description: 根据id获取巡检任务id
     * @param pstid
     * @return
     */
    InspectionDetailJson getInspectionById(String pstid);


    /**
     * @creator wangr
     * @createtime 2017/10/31 0031 下午 2:58
     * @description: 根据巡检任务流程id获取区域信息
     * @param pstid
     * @return
     */
    List<AreaDetailJson> getAreaByTaskPstid(String pstid);

    /**
     * @creator wangr
     * @createtime 2017/10/31 0031 下午 5:49
     * @description: 获取巡检区域下设备
     * @param areaId
     * @return
     */
    List<SubjectDetailJson> getSubjectDeviceByAreaId(String areaId);

    /**
     * @creator wangr
     * @createtime 2017/10/31 0031 下午 5:50
     * @description: 获取巡检区域下设备是否进行反馈
     * @param map
     * @return
     */
    int getSubjectIsFeebBack(Map map);

    /**
     * @creator wangr
     * @createtime 2017/11/2 0002 上午 9:19
     * @description: 获取巡检路线工器具
     * @param id
     * @return
     */
    List<Tool_MaterialsJson> getRouteTools(String id);

    /**
     * @creator wangr
     * @createtime 2017/11/2 0002 上午 9:20
     * @description: 获取巡检任务工器具
     * @param id
     * @return
     */
    List<Tool_MaterialsJson> getInspectionTools(String id);

    /**
     * @creator wangr
     * @createtime 2017/11/2 0002 上午 9:22
     * @description: 巡检路线备件
     * @param id
     * @return
     */
    List<Tool_MaterialsJson> getRouteSpareparts(String id);

    /**
     * @creator wangr
     * @createtime 2017/11/2 0002 上午 9:23
     * @description: 巡检任务备件
     * @param id
     * @return
     */
    List<Tool_MaterialsJson> getInspectionSpareparts(String id);

    /**
     * @creator wangr
     * @createtime 2017/11/7 0007 上午 11:36
     * @description: 获取巡检路线安全措施
     * @param id
     * @return
     */
    List<SafetyJson> getRouteSafety(String id);

    /**
     * @creator wangr
     * @createtime 2017/11/7 0007 上午 11:36
     * @description: 获取巡检任务安全措施
     * @param id
     * @return
     */
    List<SafetyJson> getInspectionSafety(String id);


    /**
     * @creator wangr
     * @createtime 2017/11/7 0007 上午 11:36
     * @description: 获取巡检路线工序
     * @param id
     * @return
     */
    List<ProcedureJson> getRouteProcedure(String id);

    /**
     * @creator wangr
     * @createtime 2017/11/7 0007 上午 11:36
     * @description: 获取巡检任务工序
     * @param id
     * @return
     */
    List<ProcedureJson> getInspectionProcedure(String id);

    /**
     * @creator wangr
     * @createtime 2017/11/7 0007 上午 11:36
     * @description: 获取巡检路线人员工时
     * @param id
     * @return
     */
    List<PersonJson> getRoutePerson(String id);

    /**
     * @creator wangr
     * @createtime 2017/11/7 0007 上午 11:36
     * @description: 获取巡检任务人员工时
     * @param id
     * @return
     */
    List<PersonJson> getInspectionPerson(String id);

    /**
     * @creator wangr
     * @createtime 2017/11/7 0007 上午 11:36
     * @description: 获取巡检路线其他费用
     * @param id
     * @return
     */
    List<OtherChargesJosn> getRouteOthers(String id);

    /**
     * @creator wangr
     * @createtime 2017/11/7 0007 上午 11:36
     * @description: 获取巡检任务其他费用
     * @param id
     * @return
     */
    List<OtherChargesJosn> getInspectionOthers(String id);

    /**
     * @creator wangr
     * @createtime 2017/11/2 0002 下午 5:00
     * @description: 查询巡检项---巡检任务对象
     * @param map
     * @return
     */
    InspectionFKDetailJson getInspectionFKDetail(Map map);

    /**
     * @creator wangr
     * @createtime 2017/11/2 0002 下午 5:01
     * @description: 根据设备 巡检任务id 查询巡检项
     * @param map
     * @return
     */
    List<SubjectFKDetailJson> getSubjectFKDetail(Map map);

    /**
     * @creator wangr
     * @createtime 2017/11/14 0014 下午 4:01
     * @description: 获取单个巡检操作对象
     * @param inspId
     * @return
     */
    InspectionTaskSwitch getFaultSwitch(String inspId);

}
