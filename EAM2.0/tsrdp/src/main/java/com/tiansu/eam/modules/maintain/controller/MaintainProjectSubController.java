package com.tiansu.eam.modules.maintain.controller;

import com.alibaba.fastjson.JSON;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.act.service.EamProcessService;
import com.tiansu.eam.modules.maintain.entity.*;
import com.tiansu.eam.modules.maintain.service.MaintainProjectSubService;
import com.tiansu.eam.modules.opestandard.entity.DeviceTreeData;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhangww
 * @description
 * @create 2017-11-06 11:35
 **/
@Controller
@RequestMapping(value = "${adminPath}/maintain/maintainProjectSub")
public class MaintainProjectSubController extends BaseController {
    @Autowired
    private MaintainProjectSubService maintainProjectSubService;
    @Autowired
    private EamProcessService eamProcessService;//流程
    //查询集合
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = {"listData"})
    public Map<String,Object> listData() {
        Map param = getFormMap();
        Map<String,Object> map = maintainProjectSubService.dataTablePageMap(param);

        return map;
    }
    /*
    * @creator zhangww
    * @createtime 2017/11/6 14:03
    * @description:生成月计划*/
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = {"produce"})
    @Transactional
    public String produce() {
       String way=getPara("way");
       String date=getPara("date");
       String[] dates= date.split("-");//日期拆分
       String yea=dates[0];
       int month=0;
       if(Integer.parseInt(dates[1])> 9){
           month=Integer.parseInt(dates[1]);
       }else{
           month=Integer.parseInt(dates[1].substring(1));
       }
        List<MaintainProjectSub> list=new ArrayList<MaintainProjectSub>();
        List<MaintainProjectSubDevice> devices=new ArrayList<MaintainProjectSubDevice>();//设备
        List<MaintainProjectSubContent> content=new ArrayList<MaintainProjectSubContent>();
        Map parammap=getFormMap();
        parammap.put("cycleyear",date);
        if(way.equals("0")){//按年计划生成
            List<Map> year=maintainProjectSubService.getFromYear(parammap);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for( Map map: year){
                String stime=map.get("project_stime").toString();//设备启用时间
                String dtime=getLastDayOfMonth(Integer.parseInt(yea),month);//生成当月计划的最后一天
                String cycle=(String)map.get("project_cycle");
                String period=(String)map.get("project_period");
                try {
                    List<String> monthdate=getCycleDays(sdf.parse(stime),sdf.parse(dtime),cycle,period,"month");//获取所有时间到月
                    if(monthdate.contains(date)){
                        MaintainProjectSub ms=new MaintainProjectSub();
                        ms.setProject_code((String)map.get("project_code"));
                        ms.setProject_name((String)map.get("project_name"));
                        ms.setProject_mode((String)map.get("project_mode"));
                        ms.setProject_type((String)map.get("project_type"));
                        ms.setProject_empid((String)map.get("project_empid"));
                        ms.setProject_cycleyear((String)map.get("project_cycleyear"));
                        ms.setProject_cycle(cycle);
                        ms.setProject_stime(stime);
                        ms.setProject_dtime(dtime);
                        ms.setProject_bm((String)map.get("project_bm"));
                        ms.setProject_period(period);
                        ms.setProject_producedate(date);
                        ms.setProjectyear_id((String)map.get("id"));
                        ms.preInsert();
                        list.add(ms);
                        List<MaintainProjectSubDevice>  dev=maintainProjectSubService.getYearDev((String)map.get("id"));
                        for(MaintainProjectSubDevice mtd:dev){
                            mtd.setId(IdGen.uuid());
                            mtd.setProject_id(ms.getId());
                        }
                        devices.addAll(dev);//设备
                        List<MaintainProjectSubContent> con=maintainProjectSubService.getYearContent((String)map.get("id"));
                        for(MaintainProjectSubContent mc:con){
                            mc.setId(IdGen.uuid());
                            mc.setProject_id(ms.getId());
                        }
                        content.addAll(con);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }else{//按保养设置生成
            // 获取保养设置中当前时间未生成月计划的记录
            List<Map> set = maintainProjectSubService.getFromSet(parammap);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for(Map map : set){
                String stime = map.get("project_stime").toString();//设备启用时间
                String dtime = getLastDayOfMonth(Integer.parseInt(yea),month);//生成当月计划的最后一天
                String cycle = (String)map.get("project_cycle");
                String period = (String)map.get("project_period");
                try{
                    List<String> monthdate=getCycleDays(sdf.parse(stime),sdf.parse(dtime),cycle,period,"month");//获取所有时间到月
                    if(monthdate.contains(date)){
                        MaintainProjectSub ms = new MaintainProjectSub();
                        ms.setProject_code((String)map.get("project_code"));
                        ms.setProject_name((String)map.get("project_name"));
                        ms.setProject_mode((String)map.get("project_mode"));
                        ms.setProject_type((String)map.get("project_type"));
                        ms.setProject_empid((String)map.get("project_empid"));
                        ms.setProject_cycleyear((String)map.get("project_cycleyear"));
                        ms.setProject_cycle(cycle);
                        ms.setProject_stime(stime);
                        ms.setProject_dtime(dtime);
                        ms.setProject_bm((String)map.get("project_bm"));
                        ms.setProject_period(period);
                        ms.setProject_producedate(date);
                        ms.setProjectset_id((String)map.get("id"));
                        ms.preInsert();
                        list.add(ms);
                        List<MaintainProjectSubDevice>  dev = maintainProjectSubService.getSetDev((String)map.get("id"));
                        for(MaintainProjectSubDevice mtd:dev){
                            mtd.setId(IdGen.uuid());
                            mtd.setProject_id(ms.getId());
                        }
                        devices.addAll(dev);
                        List<MaintainProjectSubContent> con=maintainProjectSubService.getSetContent((String)map.get("id"));
                        for(MaintainProjectSubContent mc:con){
                            mc.setId(IdGen.uuid());
                            mc.setProject_id(ms.getId());
                        }
                        content.addAll(con);
                    }
                }catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if(list.size()>0){
            maintainProjectSubService.insert(list);
            if(devices.size()>0) {
                maintainProjectSubService.insertDevBatch(devices);
            }
            if(content.size()>0){
                maintainProjectSubService.insertContentBatch(content);
            }
        }else{
            return "norecord";
        }
        return "success";
    }
    //编辑跳转
    @RequiresPermissions("user")
    @RequestMapping(value = "editUI")
    public String editUI() {

        return "modules/maintenance/maintMonFormEdit";

    }
    //跳转选择设备
    @RequiresPermissions("user")
    @RequestMapping(value = "DeviceSelectUI")
    public String deviceSelectUI() {

        return "modules/maintenance/maintMonDevSelect";

    }
    //详情
    @RequiresPermissions("user")
    @RequestMapping(value = "detailUI")
    public String detailUI() {

        return "modules/maintenance/maintMonFormDetail";

    }
    //月计划生成跳转页面
    @RequiresPermissions("user")
    @RequestMapping(value="maintMonSubmitUI")
    public String maintMonSubmit(){
        return "modules/maintenance/maintMonSubmit";
    }
    /**
     * 获取编辑页面字段数据
     * @param
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "editObj")
    public MaintainProjectSub editObj() {
        String id = getPara("id");

        return maintainProjectSubService.getEdit(id);
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getMaintByPIid")
    public MaintainProjectSub getMaintByPIid(){
        String pstid=getPara("pstid");
        return maintainProjectSubService.getMaintByPIid(pstid);
    }
    /**
     * 获取设备和设备类别树数据
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getDeviceTreeData")
    public List getDeviceTreeData(){
        //String res = getPara("operationwork_id");
        //Map param = new HashMap();
        //param.put("operationwork_id", res);
        List<Map> mapList = maintainProjectSubService.findDevCategoryList();
        System.out.println(mapList);

        if (mapList == null) {
            return null;
        }
        List<DeviceTreeData> deviceTreeDataList = new ArrayList();
        List<DeviceTreeData> resultList = new ArrayList<>();

        for(int i = 0; i < mapList.size(); i++){
            String pid;
            Map map = mapList.get(i);
            if(map.containsKey("pId") && map.get("pId") != null && !"".equals(map.get("pId"))) {
                pid = map.get("pId").toString();
            } else {
                pid = "0";
            }
            DeviceTreeData deviceTreeData = new DeviceTreeData(map.get("id").toString(), pid,
                    map.get("name").toString(), map.get("type").toString(), map.get("code").toString());
            deviceTreeData.setParentId(pid);
            deviceTreeData.setChildren(new ArrayList());
            deviceTreeDataList.add(deviceTreeData);
        }

        for (DeviceTreeData tree : deviceTreeDataList) {
            for (DeviceTreeData t : deviceTreeDataList) {
                if (t.getParentId().equals(tree.getId())) {
                    if (tree.getChildren() == null || tree.getChildren().size() == 0) {
                        List<DeviceTreeData> myChildrens = new ArrayList();
                        myChildrens.add(t);
                        tree.setChildren(myChildrens);
                    } else {
                        tree.getChildren().add(t);
                    }
                }
            }
        }

        for (DeviceTreeData tree : deviceTreeDataList) {
            if (DeviceTreeData.ROOT_PARENT.equals(tree.getParentId())) {
                resultList.add(tree);
            }
        }
        return resultList;
    }

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "delete")
    public String delete(){
        //批量删除，单个删除
        String ids = getPara("id");
        maintainProjectSubService.deleteByids(ids);
        return "success";
    }

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "quMonProce")
    public List quMonProce(){
        String id = getPara("project_id");
        return maintainProjectSubService.quMonProce(id);
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "quMonSafe")
    public List quMonSafe(){
        String id = getPara("project_id");
        return maintainProjectSubService.quMonSafe(id);
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "quMonTool")
    public List quMonTool(){
        String id = getPara("project_id");
        return maintainProjectSubService.quMonTool(id);
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "quMonSpare")
    public List quMonSpare(){
        String id = getPara("project_id");
        return maintainProjectSubService.quMonSpare(id);
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "quMonPerson")
    public List quMonPerson(){
        String id = getPara("project_id");
        return maintainProjectSubService.quMonPerson(id);
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "quMonOther")
    public List quMonOther(){
        String id = getPara("project_id");
        return maintainProjectSubService.quMonOther(id);
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "update")
    @Transactional
    public String update(MaintainProjectSub maintainProjectSub){
        String param=getPara("param");
        maintainProjectSub= JSON.parseObject(param,MaintainProjectSub.class);
        maintainProjectSub.preUpdate();
        maintainProjectSubService.update(maintainProjectSub);//修改主信息
        String uuid=maintainProjectSub.getId_key();
        List<MaintSubProc> list1=maintainProjectSub.getMaintSubProcs();
       for(MaintSubProc maintSubProc:list1){
           maintSubProc.setId(IdGen.uuid());
           maintSubProc.setProject_id(uuid);
       }
        List<MaintSubSafe> list2=maintainProjectSub.getMaintSubSafes();
        for(MaintSubSafe maintSubSafe:list2){
            maintSubSafe.setId(IdGen.uuid());
            maintSubSafe.setProject_id(uuid);
        }
        List<MaintSubTools> list3=maintainProjectSub.getMaintSubTools();
        for(MaintSubTools maintSubTools:list3){
            maintSubTools.setId(IdGen.uuid());
            maintSubTools.setProject_id(uuid);
        }
        List<MaintSubSpareparts> list4=maintainProjectSub.getMaintSubSpareparts();
        for(MaintSubSpareparts maintSubSpareparts:list4){
            maintSubSpareparts.setId(IdGen.uuid());
            maintSubSpareparts.setProject_id(uuid);
        }
        List<MaintsubPerson> list5=maintainProjectSub.getMaintsubPersons();
        for(MaintsubPerson maintsubPerson:list5){
            maintsubPerson.setId(IdGen.uuid());
            maintsubPerson.setProject_id(uuid);
        }
        List<MaintsubOthers> list6=maintainProjectSub.getMaintsubOthers();
        for(MaintsubOthers maintsubOthers:list6){
            maintsubOthers.setId(IdGen.uuid());
            maintsubOthers.setProject_id(uuid);
        }
        //删除附表,在插入附表
        maintainProjectSubService.deleteDetail(uuid);
        maintainProjectSubService.insertDetail(list1,list2,list3,list4,list5,list6);
        //删除所有设备,在插入对应设备
        maintainProjectSubService.deleDev(uuid);
        if(maintainProjectSub.getProject_device()!=null && !"".equals(maintainProjectSub.getProject_device())){
            List<MaintainProjectSubDevice> mtDevs=new ArrayList<MaintainProjectSubDevice>();
            for(String devid:maintainProjectSub.getProject_device().split(",")){
            MaintainProjectSubDevice maintainProjectSubDevice=new MaintainProjectSubDevice();
                maintainProjectSubDevice.setId(IdGen.uuid());
                maintainProjectSubDevice.setProject_id(uuid);
                maintainProjectSubDevice.setDev_id(devid);
                mtDevs.add(maintainProjectSubDevice);
            }
            maintainProjectSubService.insertDevBatch(mtDevs);
        }

        return "success";
    }
    /*
    * @creator zhangww
    * @createtime 2017/11/14 16:53
    * @description:提交流程*/
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "submitApprove")
    @Transactional
    public String submitApprove(HttpServletRequest request){
        String ids=getPara("ids");
        Map map=new HashMap();
        map.put("project_status","0");
        if(ids !=null && !"".equals(ids)){
            map.put("ids",ids.split(","));
        }
        List<Map> list=maintainProjectSubService.findListByMap(map);
        for(Map maps:list){
            String pstid = eamProcessService.startProcessByPdid("maintMon_approve","test_material",(String)maps.get("id_key"),request);
            Map mapnew =new HashMap();
            mapnew.put("id",maps.get("id_key"));
            mapnew.put("pstid",pstid);
            mapnew.put("project_status",getPara("project_status"));
            maintainProjectSubService.submitApprove(mapnew);
        }
        return "success";
    }

    //获取某月最后一天
    public String getLastDayOfMonth(int year,int month)
    {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());

        return lastDayOfMonth;
    }
    /**'
     *
     * @param start
     * @param end
     * @param cycle //cycle中1表示周一，7表示周日
     * @param period
     * @return
     */
    public static List<String> getCycleDays(Date start,Date end,String cycle,String period,String mode){
        List<String> cycleDayLst = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);

        int startYear = startCalendar.get(Calendar.YEAR);
        int startMonth = startCalendar.get(Calendar.MONTH);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        long endLongTime = end.getTime();
        int endYear = endCalendar.get(Calendar.YEAR);
        int endMonth = endCalendar.get(Calendar.MONTH);

        String[] ary = cycle.split("_");
        //周粒度 ，每ary[0]的周ary[1];
        if(PeriodEnum.WEEK_PERIOD.value().equals(period)){
            long startLongTime = startCalendar.getTimeInMillis();
            int firstDateDay = startCalendar.get(Calendar.DAY_OF_WEEK)-1;
            startLongTime += ((Integer.parseInt(ary[1]) - firstDateDay+7) % 7)*24*3600*1000;
            while(startLongTime <= endLongTime){
                String date = DateFormatUtils.format(new Date(startLongTime), "yyyy-MM-dd");
                cycleDayLst.add(date);
                startLongTime += (Integer.parseInt(ary[0])*7)*24*3600*1000;
            }
        }else if(PeriodEnum.MONTH_PERIOD.value().equals(period)) {
            //'每 '+ary[0]+'月的第'+ary[1]+'周的周'+ary[2]; 每月第一个周一所在周为第一周；
            Calendar sdateCalendar = Calendar.getInstance();
            sdateCalendar.setTime(start);
            sdateCalendar.set(Calendar.DAY_OF_MONTH,1);
            int firstDateDay = sdateCalendar.get(Calendar.DAY_OF_WEEK)-1;
            long startLongTime = sdateCalendar.getTimeInMillis();
            startLongTime += (((Integer.parseInt(ary[2]) - firstDateDay+7) % 7)+(Integer.parseInt(ary[1])-1)*7)*24*3600*1000;
            while(startLongTime <= endLongTime){
                String date = DateFormatUtils.format(new Date(startLongTime), "yyyy-MM-dd");
                cycleDayLst.add(date);
                startMonth = startMonth+Integer.parseInt(ary[0]);
                sdateCalendar.set(startYear,startMonth,1);
                startLongTime = sdateCalendar.getTimeInMillis();
                firstDateDay = sdateCalendar.get(Calendar.DAY_OF_WEEK)-1;
                startLongTime += (((Integer.parseInt(ary[2]) - firstDateDay+7) % 7)+(Integer.parseInt(ary[1])-1)*7)*24*3600*1000;
            }
        }else if(PeriodEnum.SEASON_PERIOD.value().equals(period)) {
            //'每 '+ary[0]+'季'+ary[1]+'月的第'+ary[2]+'周的周'+ary[3];
            Calendar sdateCalendar = Calendar.getInstance();
            sdateCalendar.set(startYear,Integer.parseInt(ary[1])-1,1);
            int firstDateDay = sdateCalendar.get(Calendar.DAY_OF_WEEK)-1;
            long startLongTime = sdateCalendar.getTimeInMillis();
            startLongTime += (((Integer.parseInt(ary[3]) - firstDateDay+7) % 7)+(Integer.parseInt(ary[2])-1)*7)*24*3600*1000;

            Calendar edateCalendar = Calendar.getInstance();
            edateCalendar.set(endYear,11,31);
            long endLongdataTime = edateCalendar.getTimeInMillis();
            while(startLongTime <= endLongdataTime) {
                if (startLongTime > start.getTime()) {
                    String date = DateFormatUtils.format(new Date(startLongTime), "yyyy-MM-dd");
                    cycleDayLst.add(date);
                }
                startMonth = startMonth + 3 * (Integer.parseInt(ary[0]));
                sdateCalendar.set(startYear, startMonth, 1);
                firstDateDay = sdateCalendar.get(Calendar.DAY_OF_WEEK)-1;
                startLongTime = sdateCalendar.getTimeInMillis();
                startLongTime += (((Integer.parseInt(ary[3]) - firstDateDay + 7) % 7) + (Integer.parseInt(ary[2]) - 1) * 7) * 24 * 3600 * 1000;

            }
        }else if(PeriodEnum.YEAR_PERIOD.value().equals(period)) {
            //'每 '+ary[0]+'年 '+ary[1]+'月的第'+ary[2]+'周的周'+ary[3];
            Calendar sdateCalendar = Calendar.getInstance();
            sdateCalendar.set(startYear,Integer.parseInt(ary[1])-1,1);
            int firstDateDay = sdateCalendar.get(Calendar.DAY_OF_WEEK)-1;
            long startLongTime = sdateCalendar.getTimeInMillis();
            startLongTime += (((Integer.parseInt(ary[3]) - firstDateDay+7) % 7)+(Integer.parseInt(ary[2])-1)*7)*24*3600*1000;

            Calendar edateCalendar = Calendar.getInstance();
            edateCalendar.set(endYear,11,31);
            long endLongdataTime = edateCalendar.getTimeInMillis();
            while(startLongTime <= endLongdataTime){
                if(startLongTime > start.getTime()){
                    String date = DateFormatUtils.format(new Date(startLongTime), "yyyy-MM-dd");
                    cycleDayLst.add(date);
                }
                startYear = startYear+Integer.parseInt(ary[0]);
                sdateCalendar.set(startYear,Integer.parseInt(ary[1])-1,1);
                firstDateDay = sdateCalendar.get(Calendar.DAY_OF_WEEK)-1;
                startLongTime = sdateCalendar.getTimeInMillis();
                startLongTime += (((Integer.parseInt(ary[3]) - firstDateDay+7) % 7)+(Integer.parseInt(ary[2])-1)*7)*24*3600*1000;
            }

        }else if(PeriodEnum.DAY_PERIOD.value().equals(period)) {
            Calendar sdateCalendar = Calendar.getInstance();
            sdateCalendar.setTime(start);
            long startLongTime = sdateCalendar.getTimeInMillis();
            Calendar edateCalendar = Calendar.getInstance();
            edateCalendar.setTime(end);
            long endLongdataTime = edateCalendar.getTimeInMillis();
            while(startLongTime <= endLongdataTime){
                String date = DateFormatUtils.format(new Date(startLongTime), "yyyy-MM-dd");
                cycleDayLst.add(date);
                startLongTime += Integer.parseInt(cycle)*24*3600*1000;
            }
        }

        //过滤
        return filterDate(cycleDayLst,mode,endYear,endMonth);
    }
    public static List<String> filterDate(List<String> cycleDayLst,String mode,int endYear,int endMonth){
        List<String> filterDayLst = new ArrayList<>();
        if(mode.equals(PeriodEnum.DAY_FILTER.value())){//只获取当前月的任务
            for(String date : cycleDayLst){
                int arr_year = Integer.parseInt(date.split("-")[0]);
                int arr_month = Integer.parseInt(date.split("-")[1]);
                if(endYear == arr_year && (endMonth+1) == arr_month){
                    System.out.println(date);
                    filterDayLst.add(date);
                }
            }
        }else if(mode.equals(PeriodEnum.MONTH_FILTER.value())){//获取生成的任务存在哪些年月
            Map<String,Object> map=new HashMap<String,Object>();
            for(String date : cycleDayLst){
                String time=date.substring(0,date.lastIndexOf("-"));
                map.put(time,time);
            }
          for(Map.Entry<String, Object> entry:map.entrySet()){
              System.out.println(entry.getKey());
              filterDayLst.add(entry.getKey());
          }
        }else{//获取生成的任务存在哪些年份
            Map<String,Object> map=new HashMap<String,Object>();
            for(String date : cycleDayLst){
                String time=date.substring(0,date.indexOf("-"));
                map.put(time,time);
            }
            for(Map.Entry<String, Object> entry:map.entrySet()){
                System.out.println(entry.getKey());
                filterDayLst.add(entry.getKey());
            }
        }
        return filterDayLst;
    }

    public static void main(String args[]){
        Calendar sdateCalendar = Calendar.getInstance();
        sdateCalendar.set(2017,11,28);
        Calendar edateCalendar = Calendar.getInstance();
        edateCalendar.set(2017,12,30);
//        每ary[1]+'周的周'+ary[2]
        String cycle = "1_1";
        String period = PeriodEnum.WEEK_PERIOD.value();
        List<String> list=getCycleDays(sdateCalendar.getTime(),edateCalendar.getTime(),cycle,period,"day");
    }


}
enum PeriodEnum{
    //    0天1周2月3季4年,过滤方式
    DAY_PERIOD("0"), WEEK_PERIOD("1"), MONTH_PERIOD("2"),SEASON_PERIOD("3"), YEAR_PERIOD("4"),YEAR_FILTER("year"),MONTH_FILTER("month"),DAY_FILTER("day");

    private String value;

    private PeriodEnum(String value){
        this.value = value;
    }

    public String value(){
        return value;
    }

}
