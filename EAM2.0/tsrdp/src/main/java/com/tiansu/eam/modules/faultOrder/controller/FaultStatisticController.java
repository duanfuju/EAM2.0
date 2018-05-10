package com.tiansu.eam.modules.faultOrder.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.faultOrder.entity.*;
import com.tiansu.eam.modules.faultOrder.service.FaultStatisticService;
import com.tiansu.eam.modules.sys.controller.EamDeptController;
import com.tiansu.eam.modules.sys.dao.EamUserDao;
import com.tiansu.eam.modules.sys.entity.Dept;
import com.tiansu.eam.modules.sys.entity.Dict;
import com.tiansu.eam.modules.sys.entity.SysConfigEntry;
import com.tiansu.eam.modules.sys.entity.User;
import com.tiansu.eam.modules.sys.service.EamDictService;
import com.tiansu.eam.modules.sys.service.SysConfigService;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wangjl
 * @description 故障工单统计
 * @create 2017-08-18 15:48
 **/
@Controller
@RequestMapping(value = "${adminPath}/statistic/faultOrder")
public class FaultStatisticController extends BaseController {

    @Autowired
    FaultStatisticService faultStatisticService;

    @Autowired
    private EamDictService eamDictService;

    @Autowired
    SysConfigService sysConfigService;

    @Autowired
    EamUserDao eamUserDao;

    @Autowired
    EamDeptController deptService;



    /**
     * 统计某段期间各个部门产生的故障工单数
     * @return
     */

    @ResponseBody
    @RequestMapping(value = {"getOccupancyByDept"})
    public Map<String,String> getOccupancyByDept(){
        Map result = new HashMap();
        JSONArray dataArray = new JSONArray();
        Map paramMap = getParamMap();
        if(paramMap.size() == 0){
            result.put("data",dataArray);
            return result;
        }
        List<Map> occupancyValues = faultStatisticService.getOccupancyByDept(paramMap);
        if(occupancyValues != null){
            for(Map value : occupancyValues){
                JSONObject dataObj = new JSONObject();
                dataObj.put("label",value.get("deptname"));
                dataObj.put("value",value.get("total_num"));
                dataArray.add(dataObj);
            }
        }
        result.put("data",dataArray);
        return result;
    }


    /**
     * 统计某段期间各专业产生的工单各个状态下的总数

     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"getOrderCountsByStatus"})
    public Map<String,String> getOrderCountsByStatus(){
        Map result = new HashMap();
        Map paramMap = getParamMap();
        if(paramMap.size() == 0){
            return result;
        }
        JSONObject resultJsonObj = new JSONObject();
        //统计某个时间段内工单状态及其总数
        JSONArray dataArray = getOrderCountByStatus(paramMap);
        resultJsonObj.put("orderStatusCount",dataArray);
        //统计各专业工单总数及完成数
        JSONObject majorOrderCounts = getOrderCountByMajor(paramMap);
        resultJsonObj.put("majorOrderCount",majorOrderCounts);
        result.put("data",resultJsonObj);
        return result;
    }

    /**
     * //统计各专业工单总数及完成数
     * @param paramMap
     * @return
     */
    private JSONObject getOrderCountByMajor(Map paramMap) {
        //获取当前统计级别下统计大类名称
        List<Map> devCatalogStaticLst = getDevCatalogStaticLevel();
        //统计工单类别下设备所属大类及其工单数量
        paramMap.put("dbName",Global.getConfig("jdbc.type"));
        List<Map> devCatalogOrderCount = faultStatisticService.getDevCatalogOrderCount(paramMap);
        //统计工单类别下设备所属大类及已完成的工单数量
        paramMap.put("order_status",OrderStatusEnum.FINISHED.value());
        List<Map> devCatalogFinishOrderCount = faultStatisticService.getDevCatalogOrderCount(paramMap);
        //拼接数据结果(顺序和devCatalogStaticLst一致)
        JSONObject resultObj = new JSONObject();
        JSONArray categoryArray = new JSONArray();
        JSONArray finishOrderArray = new JSONArray();
        JSONArray orderArray = new JSONArray();
        for(Map m : devCatalogStaticLst){
            String cat_id = m.get("cat_id")!=null?String.valueOf(m.get("cat_id")):"";
            String cat_name = m.get("cat_id")!=null?String.valueOf(m.get("cat_name")):"";
            JSONObject categoryObj = new JSONObject();
            categoryObj.put("label",cat_name);
            categoryArray.add(categoryObj);
            JSONObject finishOrderObj = new JSONObject();
            finishOrderObj.put("value",getValueFromMapList(cat_id,devCatalogFinishOrderCount,"static_id","total_num"));
            finishOrderArray.add(finishOrderObj);
            JSONObject orderObj = new JSONObject();
            orderObj.put("value",getValueFromMapList(cat_id, devCatalogOrderCount,"static_id","total_num"));
            orderArray.add(orderObj);
        }

        resultObj.put("category",categoryArray);
        resultObj.put("finishOrders",finishOrderArray);
        resultObj.put("allOrders",orderArray);
        return resultObj;
    }

    /**
     * 从mapList中取得map的key为keyvalue的value值
     * @param keyvalue
     * @param mapList
     * @return
     */
    private String getValueFromMapList(String keyvalue, List<Map> mapList,String keyName,String valueName) {
        for(Map map : mapList){
            if(keyvalue.equals(map.get(keyName))){
                return map.get("total_num")!=null?String.valueOf(map.get(valueName)):"";
            }
        }
        return "0";
    }

    /**
     * 获取当前统计级别下统计大类名称
     * @return
     */
    private List<Map> getDevCatalogStaticLevel() {
        //查询当前统计级别：企业级还是集团级（企业级1：设备大类在2级设备类别树下，集团级2：设备大类在3级设备类别树下）
        SysConfigEntry configEntry = sysConfigService.getByKeyName("STATISTICS_LEVEL");
        String level = "1".equals(configEntry.getConfig_value())?"1":"2";
        List<Map> devCatalogStaticInfo = faultStatisticService.getDevCatalogStaticLevel(level);
        return devCatalogStaticInfo;
    }

    /**
     * 统计某个时间段内工单状态及其总数
     * @param paramMap
     * @return
     */
    private JSONArray getOrderCountByStatus(Map paramMap) {
        JSONArray dataArray = new JSONArray();
        List<Map> orderCountByStatus = faultStatisticService.getOrderCountsByStatus(paramMap);
        List<Dict> statusDicts = eamDictService.getByCode("repair_order_status");

        for(Dict dict : statusDicts){
            JSONObject dataObj = new JSONObject();
            String status = dict.getDict_value();
            dataObj.put("status",status);
            dataObj.put("numbers",getValueFromMapList(status,orderCountByStatus,"order_status","total_num"));
            dataObj.put("title",dict.getDict_name());
            dataArray.add(dataObj);
        }

        return dataArray;
    }



    /**
     * 统计各工单某个周期内增减情况

     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"getOrderCountsByPeriod"})
    public Map<String,String> getOrderCountsByPeriod(){
        Map result = new HashMap();
        Map paramMap = new HashMap();
        //查询今日工单总数
        Date start = DateUtils.getTimesmorning();
        Date end = DateUtils.getTimesnight();
        paramMap.put("start",start);
        paramMap.put("end",end);
        int todayOrders = faultStatisticService.getOrderCount(paramMap);
        //查询昨日工单总数
        start = DateUtils.addDays(start,-1);
        end = DateUtils.addDays(end,-1);
        paramMap.put("start",start);
        paramMap.put("end",end);
        int yestodayOrders = faultStatisticService.getOrderCount(paramMap);
        //查询本周工单总数
        start = DateUtils.getTimesWeekmorning();;
        end = DateUtils.getTimesWeeknight();
        paramMap.put("start",start);
        paramMap.put("end",end);
        int thisWeekOrders = faultStatisticService.getOrderCount(paramMap);
        //查询上周工单总数
        start = DateUtils.addWeeks(start,-1);
        end = DateUtils.addWeeks(end,-1);
        paramMap.put("start",start);
        paramMap.put("end",end);
        int lastWeekOrders = faultStatisticService.getOrderCount(paramMap);
        //查询本月工单总数
        start = DateUtils.getTimesMonthmorning();
        end = DateUtils.getTimesMonthnight();
        paramMap.put("start",start);
        paramMap.put("end",end);
        int thisMonthOrders = faultStatisticService.getOrderCount(paramMap);
        //查询上月工单总数
        start = DateUtils.addMonths(start,-1);
        end = DateUtils.addMonths(end,-1);
        paramMap.put("start",start);
        paramMap.put("end",end);
        int lastMonthOrders = faultStatisticService.getOrderCount(paramMap);
        //查询今年工单总数
        start = DateUtils.getTimesYearmorning();
        end = DateUtils.getTimesYearnight();
        paramMap.put("start",start);
        paramMap.put("end",end);
        int thisYearOrders = faultStatisticService.getOrderCount(paramMap);
        //查询去年工单总数
        start = DateUtils.addYears(start,-1);
        end = DateUtils.addYears(end,-1);
        paramMap.put("start",start);
        paramMap.put("end",end);
        int lastYearOrders = faultStatisticService.getOrderCount(paramMap);

        JSONObject dataObj = new JSONObject();
        dataObj.put("toDayOrders",todayOrders);
        dataObj.put("lastDayOrders",yestodayOrders);
        dataObj.put("thisWeekOrders",thisWeekOrders);
        dataObj.put("lastWeekOrders",lastWeekOrders);
        dataObj.put("thisMonthOrders",thisMonthOrders);
        dataObj.put("lastMonOrders",lastMonthOrders);
        dataObj.put("thisYearOrders",thisYearOrders);
        dataObj.put("lastYearOrders",lastYearOrders);
        result.put("data",dataObj);
        return result;
    }

    /*
    * 查询设备树*/
    @ResponseBody
    @RequestMapping(value = {"getDevTree"})
    public List<Map> getDevTree(){
    return faultStatisticService.getDevTree();
    }

    /*
    * 查询人员树*/
    @ResponseBody
    @RequestMapping(value = {"getPersonTree"})
    public List<Map> getPersonTree(){
        return faultStatisticService.getPersonTree();
    }
    /**
     * 统计某段期间某设备或人员耗材数

     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"getCostsByDev"})
    public Map<String,Object> getCostsByDev(){
        String type=getPara("type");//对应那种类型查询
        String id=getPara("id");
        Map result = new HashMap();
        Map paramMap = getParamMap();
        if(paramMap.size() == 0){
            return result;
        }
        List<Map> data=null;
        Map total=null;
        if(type !=null &&!"".equals(type) && id !=null && !"".equals(id)) {
            if (type.equals("devid")) {//通过设备查询
                paramMap.put("deviceId",id);
                data= faultStatisticService.getCostsByDev(paramMap);
                total=faultStatisticService.queryTotalCost(paramMap);
            } else if (type.equals("devcategory")) {//通过设备类别查询
                paramMap.put("categoryId",id);
                data=faultStatisticService.getCostsByDevCategory(paramMap);
                total=faultStatisticService.queryTotalCostBycagory(paramMap);
            }
        }
        result.put("data",data);
        result.put("total",total);
        return result;
    }
    @ResponseBody
    @RequestMapping(value = {"getCostsByEmp"})
    public Map<String,Object> getCostsByEmp(){
        String type=getPara("type");//对应那种类型查询
        String id=getPara("id");
        Map result = new HashMap();
        Map paramMap = getParamMap();
        if(paramMap.size() == 0){
            return result;
        }
        List<Map> data=null;
        Map total=null;
        List<String> list=new ArrayList<String>();
        if(type !=null &&!"".equals(type) && id !=null && !"".equals(id)) {
            if (type.equals("emp")) {//通过人员查询
                paramMap.put("empid",id);
                data=faultStatisticService.getoolDetByEmp(paramMap);
                total=faultStatisticService.queryTotalCost(paramMap);
            } else if (type.equals("dept")) {//通过人员部门查询
                faultStatisticService.getDeptByno(id,list);//把对应部门的所有子节点都放进list
                paramMap.put("dept",list);
                data=faultStatisticService.getoolDetByEmpcategory(paramMap);
                total=faultStatisticService.queryTotalCostBycagory(paramMap);
            }
        }
        result.put("data",data);
        result.put("total",total);
        return result;
    }
    /**
     * 统计某段期间人员绩效
     * 1-完成率，计量单位%，小数位数保留2位；工单类的完成率以计划完成时间进行计算；保养类的完成率为当天的24点进行计算；
        巡检类的如果是到天，就以当天的24点计算，如果是一天好几个班次的，第一次的完成率以下一班次的开始时间计算，最后班次的以当天的24点进行计算。
     2-及时率，计量单位%，小数位数保留2位；工单的及时率是指半小时内接单（如果是自动派单的业务形态，就是半小时内确认）。
        比如派给检修人员A的工单为10，但是他半小时内接了5单，那么他的及时率就是50%。保养、巡检的及时率是指在计划开始时间之前进行任务的确认。
     3-工时，计量单位小时，小数位保留2位（不知道有没有小数位，如果计算到分、时就会有）；工单的工时就是从接单时间到反馈完成时间；
        保养的工时就是从实际开始时间（默认系统时间，就是任务确认环节）到实际完成时间（反馈结束时间，默认系统时间）；
        巡检的工时就是从实际开始时间（默认系统时间，就是任务确认环节）到实际完成时间（反馈结束时间，默认系统时间）。
     4-评分，计量单位分，没有小数位；评分主要是针对工单的，按加权平均算，比如派给检修人员A的工单为3，其得分别为3、5、1，那么其排名时候显示的分数是（3+5+1）/3=3分。
     5-所有的统计类数据，与查询的时间同步。
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"getOrderPerformanceByEmp"})
    public Map<String,String> getOrderPerformanceByEmp(){
        Map result = new HashMap();
        JSONArray dataArray = new JSONArray();
        Map paramMap = getParamMap();
        if(paramMap.size() == 0){
            result.put("data",dataArray);
            return result;
        }
        String type = getPara("type");
        String deptno = getPara("deptno");
        //查询需要统计的人员信息
        Dept paramDept = new Dept();
        paramDept.setDeptno(deptno);
        List<User> users = deptService.getUserByDept(paramDept);
        if(users.size() == 0){
            result.put("data",dataArray);
            return result;
        }
//        type = "及时率";
//        type = "工时";
//        type = "评分";
        if("完成率".equals(type)){
            dataArray = faultStatisticService.getOrderFinishRate(users,paramMap);
        }else if("及时率".equals(type)){
            dataArray = faultStatisticService.getOrderTimelyRate(users,paramMap);
        }else if("工时".equals(type)){
            dataArray = faultStatisticService.getOrderWorkHours(users,paramMap);
        }else if("评分".equals(type)){
            dataArray = faultStatisticService.getOrderEvaluateScore(users,paramMap);
        }

        result.put("data",dataArray);
        return result;
    }

    /**
     * 数据权限统计查看时使用（如统计人员绩效）：根据所选择的部门信息查询部门下的人员
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"getDataScopeDeptUsers"})
    public Map<String,String> getDataScopeDeptUsers(){
        String deptno = getPara("deptno");
        Dept paramDept = new Dept();
        paramDept.setDeptno(deptno);
        List<User> users = deptService.getUserByDept(paramDept);

        Map result = new HashMap();
        JSONArray dataArray = new JSONArray();
        for(User user : users){
            JSONObject deptObj = new JSONObject();
            deptObj.put("loginname",user.getLoginname());
            deptObj.put("realname",user.getRealname());
            dataArray.add(deptObj);
        }
        result.put("data",dataArray);
        return result;
    }

    /**
     * 统计某段期间工单来源占比
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"getOrderCountsBySource"})
    public Map<String,String> getOrderCountsBySource(){
        Map result = new HashMap();
        JSONArray dataArray = new JSONArray();
        Map paramMap = getParamMap();
        if(paramMap.size() == 0){
            result.put("data",dataArray);
            return result;
        }
        List<Map> occupancyValues = faultStatisticService.getOrderCountsBySource(paramMap);
        if(occupancyValues != null){
            for(Map value : occupancyValues){
                JSONObject dataObj = new JSONObject();
                String name = getOrderSource(String.valueOf(value.get("order_source")));
                if(StringUtils.isNotEmpty(name)){
                    dataObj.put("label",name);
                    dataObj.put("value",value.get("total_num"));
                }

                dataArray.add(dataObj);
            }
        }
        result.put("data",dataArray);
        return result;
    }

    private Map<String,Object> getParamMap(){
        //SimpleDateFormat 非线程安全，并发查询时会转换错误，不能放在全局变量中
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map paramMap = new HashMap();
        String startStr = getPara("startDate")+" 00:00:00";
        String endStr = getPara("endDate")+" 23:59:59";
        try {
            if(StringUtils.isNotEmpty(startStr) && StringUtils.isNotEmpty(endStr)){
                paramMap.put("start",sdf.parse(startStr));
                paramMap.put("end",sdf.parse(endStr));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return paramMap;
    }

    private String getOrderSource(String signValue){
        if(EAMConsts.ORDER_TYPE_FAULT.equals(signValue)){
            return "报修";
        }else if(EAMConsts.ORDER_TYPE_INSPECTION.equals(signValue)){
            return "巡检";
        }else if(EAMConsts.ORDER_TYPE_MAINTAIN.equals(signValue)){
            return "保养";
        }else if(EAMConsts.ORDER_TYPE_EXCEPTON.equals(signValue)){
            return "异常";
        }else{
            return "";
        }
    }

}
