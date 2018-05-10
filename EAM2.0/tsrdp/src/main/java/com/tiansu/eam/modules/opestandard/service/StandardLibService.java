package com.tiansu.eam.modules.opestandard.service;

import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.opestandard.dao.StandardLibDao;
import com.tiansu.eam.modules.opestandard.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author zhangww
 * @description
 * @create 2017-08-31 14:22
 **/
@Service
public class StandardLibService extends CrudService<StandardLibDao,StandardLib> {
    @Autowired
    private StandardLibDao standardLibDao;

    public Map dataTablePageMap(Map map) {
         Map datamap=super.dataTablePageMap(map);
        List<Map> datas=( List<Map>)datamap.get("data");
        StringBuffer device_id=null;
        for(Map data:datas){
            device_id=new StringBuffer();
            if(data.get("id_key") !=null){
                List<Map> devlist=standardLibDao.getDevBylib((String)data.get("id_key"));
                    for(Map devmap:devlist){
                        device_id.append(","+devmap.get("dev_name"));
                    }
                    if(device_id.indexOf(",") !=-1) {
                        data.put("device_id", device_id.substring(1));
                    }
            }

        }
        return datamap;
    }

    public void insert(StandardLib standardLib){
        standardLibDao.insert(standardLib);
    }
    public void insertDetail(List<StandardFault> list1,List<StandardMaintain> list2,List<StandardPatrol> list3,List<StandardFailure> list4,List<StandardOpe> list5,List<StandardSafety> list6,List<StandardDevice> list7){
        standardLibDao.insertFault(list1);
        standardLibDao.insertMaintain(list2);
        standardLibDao.insertPatrol(list3);
        standardLibDao.insertFailure(list4);
        standardLibDao.insertOpe(list5);
        standardLibDao.insertSafe(list6);
        standardLibDao.insertDev(list7);
    }

    public StandardLib getEdit(String id){
        StandardLib standardLib=standardLibDao.get(id);
        String id_key=standardLib.getId_key();
        if(id_key !=null && !"".equals(id_key)){
            List<Map> devlist=standardLibDao.getDevBylib((String)id_key);
            StringBuffer device_name=new StringBuffer();
            StringBuffer device_id=new StringBuffer();
            for(Map devmap:devlist){
                device_name.append(","+devmap.get("dev_name"));
                device_id.append(","+devmap.get("dev_id"));
            }
            if(device_name.indexOf(",") !=-1) {
                standardLib.setDevicename(device_name.substring(1));
                standardLib.setDeviceids(device_id.substring(1));
            }
        }
        return standardLib;
    }
    public List<StandardFault> quFault(String id){
        return standardLibDao.quFault(id);
    }
    public List<StandardMaintain> quMaintain(String id){
        return standardLibDao.quMaintain(id);
    }
    public List<StandardPatrol> quPatrol(String id){
        return standardLibDao.quPatrol(id);
    }
    public List<StandardFailure> quFailure(String id){
        return standardLibDao.quFailure(id);
    }
    public List<StandardOpe> quOpe(String id){
        return standardLibDao.quOpe(id);
    }
    public List<StandardSafety> quSafe(String id){
        return standardLibDao.quSafe(id);
    }

    public void update(StandardLib standardLib){
        standardLibDao.update(standardLib);
    }
    public void deleDetail(String library_id){
        standardLibDao.deleFault(library_id);
        standardLibDao.deleMatain(library_id);
        standardLibDao.delePatrol(library_id);
        standardLibDao.deleFailure(library_id);
        standardLibDao.deleOpe(library_id);
        standardLibDao.deleSafe(library_id);
        standardLibDao.deleDev(library_id);
    }
    public void deleteByids(String ids){
        StandardLib standardLib=standardLibDao.get(ids);
        standardLib.preUpdate();
        standardLibDao.deleteByids(standardLib);
    }

    public int getBycode(String code){
        Map<String,Object> map= standardLibDao.getByCode(code);
        return (int)map.get("cou");
    }

    //导出
    public List<Map> findStand(Map map){
        return standardLibDao.findStand(map);
    }
    public List<Map> findFault(Map map){
        return standardLibDao.findFault(map);
    }
    public List<Map> findMaintain(Map map){
        return standardLibDao.findMaintain(map);
    }
    public List<Map> findPatrol(Map map){
        return standardLibDao.findPatrol(map);
    }
    public List<Map> findFailure(Map map){
        return standardLibDao.findFailure(map);
    }
    public List<Map> findOpe(Map map){
        return standardLibDao.findOpe(map);
    }
    public List<Map> findSafe(Map map){
        return standardLibDao.findSafe(map);
    }

    public Map getLibByPIid(String pstid){
        return standardLibDao.getLibByPIid(pstid);
    }

    public void updateAprByPIid(Map map){
        standardLibDao.updateAprByPIid(map);
    }

    public List<Map> getDevBylib(String id_key){//查询设备名称

        return standardLibDao.getDevBylib(id_key);
    }

    /**
     * 查询设备类别/设备树
     * @param map
     * @return
     */
    public List<Map> findDevCategoryList(Map map){
        return standardLibDao.findDevCategoryList(map);
    }
}
