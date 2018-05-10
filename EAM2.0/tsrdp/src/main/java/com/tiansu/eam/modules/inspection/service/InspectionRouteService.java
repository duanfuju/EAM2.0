package com.tiansu.eam.modules.inspection.service;

import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.inspection.dao.InspectionAreaDao;
import com.tiansu.eam.modules.inspection.dao.InspectionRouteDao;
import com.tiansu.eam.modules.inspection.entity.InspectionRoute;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangww on 2017/8/15.
 */
@Service
public class InspectionRouteService extends CrudService<InspectionRouteDao,InspectionRoute> {
    @Autowired
    private InspectionRouteDao inspectionRouteDao;
    @Autowired
    private InspectionAreaDao inspectionAreaDao; //区域

    /**
     * @creator duanfuju
     * @createtime 2017/10/30 11:45
     * @description:
     *  根据条件获取数据（导出时）
     * @param map
     * @return
     */
    public List<Map> findListByMap(Map map){
        List<Map>datas=inspectionRouteDao.findListByMap(map);
        StringBuffer route_device=null;
        for(Map data:datas){
            route_device=new StringBuffer();
            if(data.get("id_key") !=null){
                List<Map> devlist=inspectionRouteDao.getDevByRouteid((String)data.get("id_key"));
                for(Map devmap:devlist){
                    route_device.append(","+devmap.get("dev_name"));
                }
                if(route_device.indexOf(",") !=-1) {
                    data.put("route_device", route_device.substring(1));
                }
            }
        }
        return  datas;
    }
    public Map dataTablePageMap(Map map) {
        Map datamap= super.dataTablePageMap(map);
        List<Map> datas=( List<Map>)datamap.get("data");
        StringBuffer route_device=null;
        for(Map data:datas){
            route_device=new StringBuffer();
            if(data.get("id_key") !=null){
                List<Map> devlist=inspectionRouteDao.getDevByRouteid((String)data.get("id_key"));
                for(Map devmap:devlist){
                    route_device.append(","+devmap.get("dev_name"));
                }
                if(route_device.indexOf(",") !=-1) {
                    data.put("route_device", route_device.substring(1));
                }
            }

        }
        return datamap;
    }

    public void insert(InspectionRoute inspectionRoute){
        inspectionRouteDao.insert(inspectionRoute);
    }
    public void insertDetail(List list1,List list2,List list3,List list4,List list5,List list6){
        inspectionRouteDao.insertInsProc(list1);
        inspectionRouteDao.insertInsSafe(list2);
        inspectionRouteDao.insertInsTool(list3);
        inspectionRouteDao.insertInsSpare(list4);
        inspectionRouteDao.insertInsPerson(list5);
        inspectionRouteDao.insertInsOther(list6);
    }

    public Map getEdit(String id){
        Map obj = inspectionRouteDao.getList(id);
        List<Map> maps=inspectionRouteDao.getAreaByrid(id);
        StringBuffer route_areaname=new StringBuffer();
        StringBuffer route_areaid=new StringBuffer();
        String  route_area[]=new String[maps.size()];
          for( int i=0;i<maps.size();i++){
              route_areaname.append(","+maps.get(i).get("area_name"));
              route_areaid.append(","+maps.get(i).get("id"));
              route_area[i]= maps.get(i).get("id").toString();//区域Id
          }
            if (route_area.length>0) {
                Map param = new HashedMap();
                param.put("ids",route_area);
                List<Map> rareas=inspectionAreaDao.findListByMap(param);
                obj.put("rareas",rareas);
                     }
        obj.put("route_area",route_areaname.substring(1).toString());
        obj.put("route_areaid",route_areaid.substring(1).toString());
        return obj;

    }
    public InspectionRoute get(String id){
        return inspectionRouteDao.get(id);
    }
    public void update(InspectionRoute inspectionRoute){
        inspectionRoute.preUpdate();
        inspectionRouteDao.update(inspectionRoute);
    }
    public void deleteByids(String ids){
        Map map=new HashMap();
        map.put("id_key",ids);
        map.put("updateBy", UserUtils.getUser().getLoginname());
        map.put("updateDate",new Date());
        inspectionRouteDao.deleteByids(map);
    }

    public int getBycode(String code){
        Map<String,Object> map= inspectionRouteDao.getByCode(code);
        return (int)map.get("cou");
    }

    public Map deleBefore(String id){
        return inspectionRouteDao.deleBefore(id);
    }

    public List quInsProce(String insrouteId){
        return inspectionRouteDao.quInsProce(insrouteId);
    }
    public List quInsSafe(String insrouteId){
        return inspectionRouteDao.quInsSafe(insrouteId);
    }
    public List quInsTool(String insrouteId){
        return inspectionRouteDao.quInsTool(insrouteId);
    }
    public List quInsSpare(String insrouteId){
        return inspectionRouteDao.quInsSpare(insrouteId);
    }
    public List quInsPerson(String insrouteId){
        return inspectionRouteDao.quInsPerson(insrouteId);
    }
    public List quInsOther(String insrouteId){
        return inspectionRouteDao.quInsOther(insrouteId);
    }

    public void deleteDetail(String insrouteId){
        inspectionRouteDao.deleInsProc(insrouteId);
        inspectionRouteDao.deleInsSafe(insrouteId);
        inspectionRouteDao.deleInsTool(insrouteId);
        inspectionRouteDao.deleInsSpare(insrouteId);
        inspectionRouteDao.deleInsPerson(insrouteId);
        inspectionRouteDao.deleInsOther(insrouteId);
        inspectionRouteDao.deleRouteArea(insrouteId);
    }

    public Map getLibByPIid(String pstid){
        Map obj= inspectionRouteDao.getLibByPIid(pstid);
        List<Map> maps=inspectionRouteDao.getAreaByrid((String)obj.get("id"));
        StringBuffer route_areaname=new StringBuffer();
        StringBuffer route_areaid=new StringBuffer();
        String  route_area[]=new String[maps.size()];
        for( int i=0;i<maps.size();i++){
            route_areaname.append(","+maps.get(i).get("area_name"));
            route_areaid.append(","+maps.get(i).get("id"));
            route_area[i]= maps.get(i).get("id").toString();//区域Id
        }
        if (route_area.length>0) {
            Map param = new HashedMap();
            param.put("ids",route_area);
            List<Map> rareas=inspectionAreaDao.findListByMap(param);
            obj.put("rareas",rareas);
        }
        obj.put("route_area",route_areaname.substring(1).toString());
        obj.put("route_areaid",route_areaid.substring(1).toString());

        return obj;
    }

    public void updateAprByPIid(Map map){
        inspectionRouteDao.updateAprByPIid(map);
    }

    public Map getLibByPIidclose(String pstidclose){
        return inspectionRouteDao.getLibByPIidclose(pstidclose);
    }
    public void updatePIidcloseByid(Map map){
        inspectionRouteDao.updatePIidcloseByid(map);
    }
    public void updateCloseByPIid(Map map){
        inspectionRouteDao.updateCloseByPIid(map);
    }

    public void insertArea(List list){
        inspectionRouteDao.insertArea(list);
    }
}
