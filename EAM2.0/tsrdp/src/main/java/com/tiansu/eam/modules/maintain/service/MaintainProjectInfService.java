package com.tiansu.eam.modules.maintain.service;/**
 * Created by suven on 2017/11/2.
 */

import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.device.dao.EamDeviceDao;
import com.tiansu.eam.modules.inspection.entity.InspectionRoute;
import com.tiansu.eam.modules.maintain.dao.MaintainProjectInfDao;
import com.tiansu.eam.modules.maintain.entity.MaintainProjectInf;
import com.tiansu.eam.modules.maintain.entity.MaintainProjectInfDevice;
import com.tiansu.eam.modules.maintain.entity.MaintainProjectInfForImport;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保养设置服务层
 *
 * @author suven suven
 * @create 2017/11/2
 */
@Service
public class MaintainProjectInfService extends CrudService<MaintainProjectInfDao,MaintainProjectInf> {

    @Autowired
    private MaintainProjectInfDao maintainProjectInfDao;
    @Autowired
    private EamDeviceDao eamDeviceDao;

/**
*@Create
*@Description :判断该数据是否有效，是否被引用
*@Param :  * @param null
*@author : suven
*@Date : 19:20 2017/11/21
*/
public boolean queryForCanDelete(String ids){
    Map m=getEdit(ids);
    String status=(String)m.get("status");

    if(status.equals("1")){
        return false;
    }else{
        return true;
    }

}

    /**
     *@creator duanfuju
     * @createtime 2017/11/15 16:24
     * @description:
     * 批量插入
     * @param maintainProjectInfForImport
     * @return
     */
    public int insertBatch(List<MaintainProjectInfForImport> maintainProjectInfForImport){
          return maintainProjectInfDao.insertBatch(maintainProjectInfForImport);
    }
    /**
     * @creator duanfuju
     * @createtime 2017/11/15 15:01
     * @description:
     * 获取导出的数据
     * @param map
     * @return
     */
    public List<Map> getExportData(Map map){
        List<Map> result =maintainProjectInfDao.getExportData(map);
        for (int i = 0; i < result.size(); i++) {
            try{
                Map m=result.get(i);
                Map param = new HashMap();
                param.put("project_id",m.get("id_key"));
                List<Map> devices= findDeviceListByMap(param);//设备编码和设备名称
                if(devices.size()>0){
                    String str_dev_code="";
                    String str_dev_name="";
                    for (int j = 0; j < devices.size(); j++) {
                        Map device=devices.get(j);
                        str_dev_code+=device.get("dev_id").toString()+",";
                        str_dev_name+=device.get("dev_name").toString()+",";
                    }
                    str_dev_code=str_dev_code.substring(0,str_dev_code.length()-1);
                    str_dev_name=str_dev_name.substring(0,str_dev_name.length()-1);
                    m.put("dev_code",str_dev_code);
                    m.put("dev_name",str_dev_name);
                }
                //保养内容
                List<Map> contents = findContentListByMap(param);
                String project_content="";
                for (int j = 0; j <contents.size(); j++) {
                    Map mn=contents.get(j);
                    project_content+=mn.get("maintain_content").toString()+",";
                }
                m.put("project_content",project_content.substring(0,project_content.length()-1));
                //保养周期
                String[] strs=m.get("project_cycle").toString().split("_");
                if(m.get("project_period_name").toString().equals("天")){
                    m.put("project_cycle","每"+strs[0]+"天");
                }else if(m.get("project_period_name").toString().equals("周")){
                    m.put("project_cycle","每"+strs[0]+"周的周"+strs[1]);
                }else if(m.get("project_period_name").toString().equals("月")){
                    m.put("project_cycle","每"+strs[0]+"月的第"+strs[1]+"周的周"+strs[2]);
                }else if(m.get("project_period_name").toString().equals("季")){
                    m.put("project_cycle","每"+strs[0]+"季的第"+strs[1]+"月的第"+strs[2]+"周的周"+strs[3]);
                }else if(m.get("project_period_name").toString().equals("年")){
                    m.put("project_cycle","每"+strs[0]+"年的第"+strs[1]+"月的第"+strs[2]+"周的周"+strs[3]);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return result;
    }
    /**
     * @creator duanfuju
     * @createtime 2017/11/15 15:00
     * @description:
     *  根据年计划的id获取保养内容
     * @param map
     * @return
     */
    public List<Map> findContentListByMap(Map map){
        return  maintainProjectInfDao.findContentListByMap(map);
    }

    /**
     * @creator duanfuju
     * @createtime 2017/11/15 15:00
     * @description:
     *  根据年计划的id获取保养设备
     * @param map
     * @return
     */
    public List<Map> findDeviceListByMap(Map map){
        return  maintainProjectInfDao.findDeviceListByMap(map);
    }
    public void deleteByids(String ids){
        Map map=new HashMap();
        map.put("id_key",ids);
        map.put("updateBy", UserUtils.getUser().getLoginname());
        map.put("updateDate",new Date());
        maintainProjectInfDao.deleteByids(map);
    }

    public List quMaintContent(String maintSetId){
        return maintainProjectInfDao.quMaintContent(maintSetId);
    }
    public int getBycode(String code){
        Map<String,Object> map= maintainProjectInfDao.getByCode(code);
        return (int)map.get("cou");
    }

    public Map dataTablePageMap(Map map) {
        Map datamap= super.dataTablePageMap(map);
        List<Map> datas=( List<Map>)datamap.get("data");
        StringBuffer maint_device=null;
        for(Map data:datas){
            maint_device=new StringBuffer();
            String project_mode=(String)data.get("project_mode");
            String project_bm=(String)data.get("project_bm");
            if( project_mode!=null){
                project_mode=maintainProjectInfDao.getProject_mode(project_mode);
                data.put("project_mode",project_mode);
            }
            if( project_bm!=null){

                project_bm=maintainProjectInfDao.getProject_bm(project_bm);
                data.put("project_bm",project_bm);
            }
            if(data.get("id_key") !=null){
                List<String> devidbyds=maintainProjectInfDao.getDevidsByMaint_id((String)data.get("id_key"));
               if(devidbyds.size()==0){
                   data.put("dev_id", "");
               }else{

                   for(String devidbyd:devidbyds){
                       String dev_name=eamDeviceDao.getDev_name(devidbyd);
                       maint_device.append(","+dev_name);
                   }

                if(maint_device.indexOf(",") !=-1) {
                    data.put("dev_id", maint_device.substring(1));
                }
               }
            }

        }
        return datamap;
    }
    public void insert(MaintainProjectInf maintainProjectInf){
        maintainProjectInfDao.insert(maintainProjectInf);
    }
    public void insertDetail(List list1, List list2) {
        maintainProjectInfDao.insertMaintCont(list1);
        maintainProjectInfDao.insertMaintDev(list2);
    }

    public Map getEdit(String id){
        Map obj = maintainProjectInfDao.getList(id);
        List<String> devidbyds=maintainProjectInfDao.getDevidsByMaint_id(id);
        StringBuffer maint_device=new StringBuffer();
        StringBuffer maint_ids=new StringBuffer();
        String project_bm=(String)obj.get("project_bm");
        if( project_bm!=null&&!("").equals(project_bm)){

            String project_bm_name=maintainProjectInfDao.getProject_bm(project_bm);
            obj.put("project_bm_name",project_bm_name);
        }

        if(devidbyds.size()==0){
            obj.put("dev_names", "");
        }else{

            for(String devidbyd:devidbyds){
                String dev_name=eamDeviceDao.getDev_name(devidbyd);
                maint_device.append(","+dev_name);
                maint_ids.append(","+devidbyd);
            }

            if(maint_device.indexOf(",") !=-1) {
                obj.put("dev_names", maint_device.substring(1));
                obj.put("dev_id", maint_ids.substring(1));
            }

        }

        return obj;

    }

    public void deleteDetail(String project_id){
        maintainProjectInfDao.delMaintContent(project_id);
        maintainProjectInfDao.delMaintDevice(project_id);
    }


















}
