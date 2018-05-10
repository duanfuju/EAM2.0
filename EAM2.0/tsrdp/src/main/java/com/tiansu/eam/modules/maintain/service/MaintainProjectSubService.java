package com.tiansu.eam.modules.maintain.service;

import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.maintain.dao.MaintainProjectSubDao;
import com.tiansu.eam.modules.maintain.entity.MaintainProjectSub;
import com.tiansu.eam.modules.maintain.entity.MaintainProjectSubContent;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangww
 * @description 保养月计划
 * @create 2017-11-02 11:39
 **/
@Service
public class MaintainProjectSubService extends CrudService<MaintainProjectSubDao,MaintainProjectSub>{
    @Autowired
    private MaintainProjectSubDao maintainProjectSubDao;



    /**
     * @creator duanfuju
     * @createtime 2017/11/14 11:13
     * @description:
     * 获取导出的数据
     * @param map
     * @return
     */
    public List<Map> getExportData(Map map){
        List<Map> result =maintainProjectSubDao.getExportData(map);

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
                        str_dev_code+=device.get("dev_code").toString()+",";
                        str_dev_name+=device.get("dev_name").toString()+",";
                    }
                    str_dev_code=str_dev_code.substring(0,str_dev_code.length()-1);
                    str_dev_name=str_dev_name.substring(0,str_dev_name.length()-1);
                    m.put("dev_code",str_dev_code);
                    m.put("dev_name",str_dev_name);
                }
                //保养周期
                String[] strs=m.get("project_cycle").toString().split("_");
                if(m.get("project_period").toString().equals("天")){
                    m.put("project_period","每"+strs[0]+"天");
                }else if(m.get("project_period").toString().equals("周")){
                    m.put("project_period","每"+strs[0]+"周的周"+strs[1]);
                }else if(m.get("project_period").toString().equals("月")){
                    m.put("project_period","每"+strs[0]+"月的第"+strs[1]+"周的周"+strs[2]);
                }else if(m.get("project_period").toString().equals("季")){
                    m.put("project_period","每"+strs[0]+"季的第"+strs[1]+"月的第"+strs[2]+"周的周"+strs[3]);
                }else if(m.get("project_period").toString().equals("年")){
                    m.put("project_period","每"+strs[0]+"年的第"+strs[1]+"月的第"+strs[2]+"周的周"+strs[3]);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return  result;
    }
    /**
     * @creator duanfuju
     * @createtime 2017/11/14 13:49
     * @description:
     *  根据月计划的id获取保养设备
     * @param map
     * @return
     */
   public List<Map> findDeviceListByMap(Map map){
       return maintainProjectSubDao.findDeviceListByMap(map);
   }
   public List<MaintainProjectSubContent> findContentByMap(Map map){
       return maintainProjectSubDao.findContentByMap(map);
   }
    /*
    * @creator zhangww
    * @createtime 2017/11/6 10:12
    * @description:查询*/
    public Map dataTablePageMap(Map map) {
        Map datamap= super.dataTablePageMap(map);
        List<Map> datas=( List<Map>)datamap.get("data");
        StringBuffer device_id=null;
        for(Map data:datas){
            device_id=new StringBuffer();
            if(data.get("id_key") !=null){
                Map param=new HashMap();
                param.put("project_id",data.get("id_key"));
                List<Map> devlist=findDeviceListByMap(param);
                for(Map devmap:devlist){
                    device_id.append(","+devmap.get("dev_name"));
                }
                if(device_id.indexOf(",") !=-1) {
                    data.put("project_device", device_id.substring(1));
                }
            }

        }
        return datamap;
    }
    /*
        * @creator zhangww
        * @createtime 2017/11/6 10:12
        * @description:生成月计划*/
    public void insert(List list){
        maintainProjectSubDao.insertBatch(list);
    }
    //插入设备
    public void insertDevBatch(List list){
        maintainProjectSubDao.insertDevBatch(list);
    }
    //插入保养内容
    public void insertContentBatch(List list){
        maintainProjectSubDao.insertContentBatch(list);
    }
    //插入到保养任务的标准内容表
    public void insertTotaskContent(List list){
        maintainProjectSubDao.insertTotaskContent(list);
    }
/*
* @creator zhangww
* @createtime 2017/11/6 14:05
* @description:获取年计划*/
    public List<Map> getFromYear(Map map){
        return maintainProjectSubDao.getFromYear(map);
    }
    public List<Map> getFromSet(Map map){
        return maintainProjectSubDao.getFromSet(map);
    }
    //获取年计划，设置设备
    public List getYearDev(String project_id){
        return maintainProjectSubDao.getYearDev(project_id);
    }
    public List getSetDev(String project_id){
        return maintainProjectSubDao.getSetDev(project_id);
    }
    /*获取年计划设备保养内容*/
    public List getYearContent(String project_id){
        return maintainProjectSubDao.getYearContent(project_id);
    }
    public List getSetContent(String project_id){
        return maintainProjectSubDao.getSetContent(project_id);
    }
    public void update(MaintainProjectSub maintainProjectSub){
        maintainProjectSubDao.update(maintainProjectSub);
    }
    //删除
    public void deleteByids(String ids){
        Map map=new HashMap();
        String id[] = ids.split(",");
        map.put("ids",id);
        map.put("updateBy", UserUtils.getUser().getLoginname());
        map.put("updateDate",new Date());
        maintainProjectSubDao.deleteByids(map);
    }
    public Map deleBefore(String id){//删除前的判断
        return maintainProjectSubDao.deleBefore(id);
    }
//编辑
    public MaintainProjectSub getEdit(String id){
        MaintainProjectSub maintainProjectSub=  maintainProjectSubDao.get(id);

        StringBuffer dev_id=new StringBuffer();
        StringBuffer dev_name=new StringBuffer();
            if(maintainProjectSub.getId_key() !=null) {
                Map param = new HashMap();
                param.put("project_id", maintainProjectSub.getId_key());
                List<Map> devlist = findDeviceListByMap(param);
                for (Map devmap : devlist) {
                    dev_id.append("," + devmap.get("dev_name"));
                    dev_name.append(","+devmap.get("dev_id"));
                }
                if (dev_id.indexOf(",") != -1) {
                     maintainProjectSub.setProject_device(dev_name.substring(1));
                     maintainProjectSub.setProject_devname(dev_id.substring(1));
                }
            }
            return maintainProjectSub;
    }

    public void insertDetail(List list1,List list2,List list3,List list4,List list5,List list6){
        maintainProjectSubDao.insertMonProc(list1);
        maintainProjectSubDao.insertMonSafe(list2);
        maintainProjectSubDao.insertMonTool(list3);
        maintainProjectSubDao.insertMonSpare(list4);
        maintainProjectSubDao.insertMonPerson(list5);
        maintainProjectSubDao.insertMonOther(list6);
    }
    public List quMonProce(String project_id){
        return maintainProjectSubDao.quMonProce(project_id);
    }
    public List quMonSafe(String project_id){
        return maintainProjectSubDao.quMonSafe(project_id);
    }
    public List quMonTool(String project_id){
        return maintainProjectSubDao.quMonTool(project_id);
    }
    public List quMonSpare(String project_id){
        return maintainProjectSubDao.quMonSpare(project_id);
    }
    public List quMonPerson(String project_id){
        return maintainProjectSubDao.quMonPerson(project_id);
    }
    public List quMonOther(String project_id){
        return maintainProjectSubDao.quMonOther(project_id);
    }

    public void deleteDetail(String project_id){
        maintainProjectSubDao.deleMonProc(project_id);
        maintainProjectSubDao.deleMonSafe(project_id);
        maintainProjectSubDao.deleMonTool(project_id);
        maintainProjectSubDao.deleMonSpare(project_id);
        maintainProjectSubDao.deleMonPerson(project_id);
        maintainProjectSubDao.deleMonOther(project_id);
    }

//根据pstid查询
    public MaintainProjectSub getMaintByPIid(String pstid){
        MaintainProjectSub maintainProjectSub= maintainProjectSubDao.getMaintByPIid(pstid);
        StringBuffer dev_id=new StringBuffer();
        StringBuffer dev_name=new StringBuffer();
        if(maintainProjectSub.getId_key() !=null) {
            Map param = new HashMap();
            param.put("project_id", maintainProjectSub.getId_key());
            List<Map> devlist = findDeviceListByMap(param);
            for (Map devmap : devlist) {
                dev_id.append("," + devmap.get("dev_name"));
                dev_name.append(","+devmap.get("dev_id"));
            }
            if (dev_id.indexOf(",") != -1) {
                maintainProjectSub.setProject_device(dev_name.substring(1));
                maintainProjectSub.setProject_devname(dev_id.substring(1));
            }
        }
        return maintainProjectSub;
    }
    /**
     * 查询设备类别/设备树
     * @param
     * @return
     */
    public List<Map> findDevCategoryList(){
        return maintainProjectSubDao.findDevCategoryList();
    }
    public void deleDev(String project_id){
        maintainProjectSubDao.deleDev(project_id);
    }

    //提交审批
    public void submitApprove(Map map){
        maintainProjectSubDao.submitApprove(map);
    }
    //查询
    public List findListByMap(Map map){
        return maintainProjectSubDao.findListByMap(map);
    }
    //根据pstid修改
    public void approveByPstid(Map map){
        maintainProjectSubDao.approveByPstid(map);
    }
}
