package com.tiansu.eam.interfaces.maintain.dao;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.interfaces.common.OtherChargesJosn;
import com.tiansu.eam.interfaces.common.PersonJson;
import com.tiansu.eam.interfaces.common.Tool_MaterialsJson;
import com.tiansu.eam.interfaces.inspection.entity.InspectionSparepartsJson;
import com.tiansu.eam.interfaces.inspection.entity.InspectionToolsJson;
import com.tiansu.eam.interfaces.maintain.entity.MaintainDetailJson;
import com.tiansu.eam.interfaces.maintain.entity.MaintainListJson;

import java.util.List;
import java.util.Map;

/**
 * @author wangr
 * @description 保养接口dao
 * @create 2017-11-06 下午 3:20
 */
@MyBatisDao
public interface MaintainRestDao {

    /**
     * @param param
     * @return
     * @creator wangr
     * @createtime 2017/11/7 0007 下午 3:47
     * @description: 获取我的保养列表
     */
    List<MaintainListJson> getMaintainList(Map param);

    /**
     * @param pstid
     * @return
     * @creator lihj
     * @createtime 2017/11/7 00 上午 2:26
     * @description: 根据id获取保养任务id
     * @modifier wangr
     * @modifytime 2017/11/10 0010 下午 3:23
     * @modifyDec: 列表 适用于
     */
    List<MaintainDetailJson> getMaintainById(Map param);

    /**
     * 根据保养任务主键id获取保养任务下的工序数据
     *
     * @param id
     * @return
     */
    public List<Map<String, Object>> getProcedure(String id);

    public List<Map<String, Object>> getProcedureFK(String id);

    /**
     * 根据保养任务主键id获取保养任务下的安全措施数据
     *
     * @param id
     * @return
     */
    public List<Map<String, Object>> getSafety(String id);

    public List<Map<String, Object>> getSafetyFK(String id);


    /**
     * 根据保养任务主键id获取保养任务下的人员工时数据
     *
     * @param id
     * @return
     */
    public List<PersonJson> getPersonhours(String id);

    public List<PersonJson> getPersonhoursFK(String id);

    /**
     * 根据保养任务主键id获取保养任务下的工序数据
     *
     * @param id
     * @return
     */
    public List<OtherChargesJosn> getOtherexpenses(String id);

    public List<OtherChargesJosn> getOtherexpensesFK(String id);

    /**
     * @param id
     * @return
     * @creator lihj
     * @createtime 2017/11/7 0004 下午 2:40
     * @description: 获取保养任务工器具
     */
    List<Tool_MaterialsJson> getMaintainTools(String id);

    List<Tool_MaterialsJson> getMaintainToolsFK(String id);

    /**
     * @param id
     * @return
     * @creator lihj
     * @createtime 2017/11/7 0005 下午 2:42
     * @description: 保养备件
     */
    List<Tool_MaterialsJson> getMaintainSpareparts(String id);

    List<Tool_MaterialsJson> getMaintainSparepartsFK(String id);


    /**
     * @param map
     * @return
     * @creator wangr
     * @createtime 2017/11/10 0010 下午 2:10
     * @description: 根据设备和userId 获取时间最近的保养任务  用于扫码
     */
    String getMaintainByDevId(Map map);
}
