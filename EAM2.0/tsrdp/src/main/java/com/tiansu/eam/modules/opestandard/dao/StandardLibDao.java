package com.tiansu.eam.modules.opestandard.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.opestandard.entity.*;

import java.util.List;
import java.util.Map;

/**
 * @author zhangww
 * @description
 * @create 2017-08-31 14:21
 **/
@MyBatisDao
public interface StandardLibDao extends CrudDao<StandardLib> {

    public int deleteByids(StandardLib standardLib);

    public Map<String,Object> getByCode(String code);

    public int insertLibBatch(List<StandardLib> list);//用于批量导入,主表，附表用下面的导入
    public int insertLibDevBatch(List<StandardDevice> list);
    public int  import_after();//导入完修改

    public List<Map> findListByMap(Map map);//用于导出的方法

    public List<Map> getDevBylib(String id_key);//查询设备名称

    public int insertDev(List<StandardDevice> list);
    public int insertFault(List<StandardFault> list);
    public int insertMaintain(List<StandardMaintain> list);
    public int insertPatrol(List<StandardPatrol> list);
    public int insertFailure(List<StandardFailure> list);
    public int insertOpe(List<StandardOpe> list);
    public int insertSafe(List<StandardSafety> list);

    public int deleFault(String library_id);
    public int deleMatain(String library_id);
    public int delePatrol(String library_id);
    public int deleFailure(String library_id);
    public int deleOpe(String library_id);
    public int deleSafe(String library_id);
    public int deleDev(String library_id);

    public List<StandardFault> quFault(String id);
    public List<StandardMaintain> quMaintain(String id);
    public List<StandardPatrol> quPatrol(String id);
    public List<StandardFailure> quFailure(String id);
    public List<StandardOpe> quOpe(String id);
    public List<StandardSafety> quSafe(String id);

    public List<Map> findStand(Map map);
    public List<Map> findFault(Map map);
    public List<Map> findMaintain(Map map);
    public List<Map> findPatrol(Map map);
    public List<Map> findFailure(Map map);
    public List<Map> findOpe(Map map);
    public List<Map> findSafe(Map map);

    public Map getLibByPIid(String pstid);
    public int updateAprByPIid(Map map);
    // 设备类别/设备树
    List<Map> findDevCategoryList(Map map);

    public Map deleBefore(String id);
}
