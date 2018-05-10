package com.tiansu.eam.modules.inspection.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.inspection.entity.InspectionRoute;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangww on 2017/8/7.
 */
@MyBatisDao
public interface InspectionRouteDao extends CrudDao<InspectionRoute> {
/*其他方法在父接口全都有*/

 int deleteByids(Map map);

 Map<String,Object> getByCode(String code);

 Map deleBefore(String id);

 int insertInsProc(List list);
 int insertInsSafe(List list);
 int insertInsTool(List list);
 int insertInsSpare(List list);
 int insertInsPerson(List list);
 int insertInsOther(List list);

 Map getList(String id);
 List<Map> getAreaByrid(String id);

 List quInsProce(String insrouteId);
 List quInsSafe(String insrouteId);
 List quInsTool(String insrouteId);
 List quInsSpare(String insrouteId);
 List quInsPerson(String insrouteId);
 List quInsOther(String insrouteId);

 int deleInsProc(String id);
 int deleInsSafe(String id);
 int deleInsTool(String id);
 int deleInsSpare(String id);
 int deleInsPerson(String id);
 int deleInsOther(String id);
 int deleRouteArea(String id);

 Map getLibByPIid(String pstid);
 int updateAprByPIid(Map map);

 Map getLibByPIidclose(String pstidclose);
 int updatePIidcloseByid(Map map);
 int updateCloseByPIid(Map map);

 int insertArea(List list);

 List<Map> getDevByRouteid(String id_key);


  /**
   * @creator duanfuju
   * @createtime 2017/10/30 17:20
   * @description:
   *  根据条件获取数据（导出时）
   * @param map
   * @return
   */
  List<Map> findListByMap(Map map);
}
