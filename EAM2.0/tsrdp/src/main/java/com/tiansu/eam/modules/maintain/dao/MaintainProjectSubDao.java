package com.tiansu.eam.modules.maintain.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.maintain.entity.MaintainProjectSub;

import java.util.List;
import java.util.Map;

/**
 * @author zhangww
 * @description 保养月计划
 * @create 2017-11-02 11:36
 **/
@MyBatisDao
public interface MaintainProjectSubDao extends CrudDao<MaintainProjectSub> {
    /**
     * @creator duanfuju
     * @createtime 2017/11/14 11:14
     * @description:
     * 获取导出的数据
     * @param map
     * @return
     */
    List<Map> getExportData(Map map);
    /**
     * @creator duanfuju
     * @createtime 2017/11/14 13:49
     * @description:
     *  根据月计划的id获取保养设备
     * @param map
     * @return
     */
    List<Map> findDeviceListByMap(Map map);//查询对应设备
    List findContentByMap(Map map);//查询对应保养内容
    List<Map> getFromYear(Map map);//获取年计划
    List<Map> getFromSet(Map map);//获取设置

    List getYearDev(String project_id);//根据id获取年计划设备
    List getSetDev(String project_id);//根据id获取设置设备
    List getYearContent(String project_id);
    List getSetContent(String project_id);
    int insertBatch(List list);
    int insertDevBatch(List list);
    int insertContentBatch(List list);
    int insertTotaskContent(List list);//插入到保养任务的标准内容表
    int deleteByids(Map<String, Object> map);//删除
    Map<String,Object> deleBefore(String id);//删除前的引用判断

    // 设备类别/设备树
    List<Map> findDevCategoryList();
//删除设备
    public int deleDev(String project_id);

    //提交审批
    public int submitApprove(Map map);
    //根据pstid修改
    public int approveByPstid(Map map);
//根据pstid查询
    public MaintainProjectSub getMaintByPIid(String pstid);

    int insertMonProc(List list);
    int insertMonSafe(List list);
    int insertMonTool(List list);
    int insertMonSpare(List list);
    int insertMonPerson(List list);
    int insertMonOther(List list);

    List quMonProce(String project_id);
    List quMonSafe(String project_id);
    List quMonTool(String project_id);
    List quMonSpare(String project_id);
    List quMonPerson(String project_id);
    List quMonOther(String project_id);

    int deleMonProc(String id);
    int deleMonSafe(String id);
    int deleMonTool(String id);
    int deleMonSpare(String id);
    int deleMonPerson(String id);
    int deleMonOther(String id);
}
