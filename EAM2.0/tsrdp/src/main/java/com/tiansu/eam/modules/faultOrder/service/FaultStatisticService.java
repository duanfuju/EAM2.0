package com.tiansu.eam.modules.faultOrder.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.faultOrder.dao.FaultOrderDao;
import com.tiansu.eam.modules.faultOrder.dao.FaultStatisticDao;
import com.tiansu.eam.modules.faultOrder.entity.*;
import com.tiansu.eam.modules.sys.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author wangjl
 * @description 工单统计服务
 * @create 2017-08-21 8:40
 *
 **/
@Service
@Transactional(readOnly = true)
public class FaultStatisticService extends CrudService<FaultOrderDao,FaultOrder>{

    @Autowired
    FaultStatisticDao faultStatisticDao;

    /**
     * 统计某段期间各个部门产生的故障工单数
     * @param map
     * @return
     */
    public List<Map> getOccupancyByDept(Map map){
        return faultStatisticDao.getOccupancyByDept(map);
    }

    /**
     * 统计某段期间各专业产生的工单各个状态下的总数
     * @param map
     * @return
     */
    public List<Map> getOrderCountsByStatus(Map map){
        return faultStatisticDao.getOrderCountsByStatus(map);
    }


    /**
     * 统计各工单某个周期内增减情况
     * @param map

     * @return
     */
    public List<Map> getOrderCountsByPeriod(Map map){

        return null;
    }
/*
* 获取设备类别树*/
    public List<Map> getDevTree(){
        return faultStatisticDao.getDeviceTree();
    }
    /*获取人员树*/
    public List<Map> getPersonTree(){
        return faultStatisticDao.getPersonTree();
    }
    /**
     * 统计某段期间某设备或人员耗材数
     * @param map
     * @return
     */
    public List<Map> getCostsByDev(Map map){

        return faultStatisticDao.getCostsByDev(map);
    }
/*
* 通过设备类别查询*/
    public List<Map> getCostsByDevCategory(Map map){
        return faultStatisticDao.getCostsByDevCategory(map);
    }
    /*通过人员查询消耗*/
    public List<Map> getoolDetByEmp(Map map){
        return faultStatisticDao.getoolDetByEmp(map);
    }
    /*通过设备或人员查询总消耗*/
    public Map queryTotalCost(Map map){
        return faultStatisticDao.queryTotalCost(map);
    }
/*通过部门查询消耗*/
    public List<Map> getoolDetByEmpcategory(Map map){
        return faultStatisticDao.getoolDetByEmpcategory(map);
    }
    /*通过上级部门查找部门*/
    public void getDeptByno(String pid,List<String> list){
        List<Map> maps= faultStatisticDao.getDeptByno(pid);
        list.add(pid);
            for (int i = 0; i < maps.size(); i++) {
                String deptno = (String) maps.get(i).get("deptno");
                getDeptByno(deptno,list);//递归查询所有部门子节点
            }
    }
    /*通过部门或类别查询总消耗*/
    public Map queryTotalCostBycagory(Map map){
        return faultStatisticDao.queryTotalCostBycagory(map);
    }
    /**
     * 统计某段期间人员绩效
     * @param map

     * @return
     */
    public List<Map> getOrderPerformanceByEmp(Map map){

        return null;
    }

    /**
     * 统计某段期间工单来源占比
     * @param map

     * @return
     */
    public List<Map> getOrderCountsBySource(Map map){
        return faultStatisticDao.getOrderCountsBySource(map);
    }

    //统计某个时间段的工单总数
    public int getOrderCount(Map map){
        return faultStatisticDao.getOrderCount(map);
    }

    public List<Map> getDevCatalogStaticLevel(String level) {
        return faultStatisticDao.getDevCatalogStaticLevel(level);
    }

    /**
     * 统计工单类别下设备所属大类及其数量
     * @param paramMap
     * @return
     */
    public List<Map> getDevCatalogOrderCount(Map paramMap) {
        return faultStatisticDao.getDevCatalogOrderCount(paramMap);
    }

    /**
     * 统计完成率
     * @param users
     * @param paramMap
     */
    public JSONArray getOrderFinishRate(List<User> users, Map paramMap) {
        JSONArray result = new JSONArray();
        paramMap = initParamMap(paramMap,users);
        //统计某人已经完成的单子
        List<Map> finishedOrderList = faultStatisticDao.countFinishOrdersByUser(paramMap);
        //统计某人已经接了的单子
        List<Map> countAcceptOrdersByUser = faultStatisticDao.countAcceptOrdersByUser(paramMap);
        for(User user : users){
            JSONObject jsonObject = new JSONObject();
            String loginName = user.getLoginname();
            double finishCount = getCountFromListByLoginName(finishedOrderList,loginName);
            double receiveCount = getCountFromListByLoginName(countAcceptOrdersByUser,loginName);
            float rate = 0;
            if(receiveCount == 0){
                rate = 0;
            }else{
                rate = new BigDecimal( (float) 100*finishCount/receiveCount).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            }
            jsonObject.put("length",(int) rate+"%");//展示条长度
            jsonObject.put("rate",rate+"%");
            jsonObject.put("loginname",loginName);
            jsonObject.put("realname",user.getRealname());
            result.add(jsonObject);
        }
        return result;
    }

    private double getCountFromListByLoginName(List<Map> finishedOrderList, String loginName) {
        if(finishedOrderList!=null){
            for(Map map : finishedOrderList){
                if(loginName.equals(map.get("user_name"))){
                    return Double.parseDouble(String.valueOf(map.get("total_num"))) ;
                }
            }
        }
        return 0;
    }

    private double getMaxValueFromList(List<Map> orderList) {
        double maxValue = 0;
        if(orderList!=null){
            for(Map map : orderList){
                double tmpValue = Double.parseDouble(String.valueOf(map.get("total_num")));
                if(tmpValue > maxValue){
                    maxValue = tmpValue;
                }
            }
        }
        return maxValue;
    }

    /**
     * 统计及时率：半小时内接单算及时
     * @param users
     * @param paramMap
     */
    public JSONArray getOrderTimelyRate(List<User> users, Map paramMap) {
        JSONArray result = new JSONArray();
        paramMap = initParamMap(paramMap,users);
        //统计某人接单时间少于半小时的单子
        List<Map> finishedOrderList = faultStatisticDao.countTimelyOrdersByUser(paramMap);
        //统计某人已经接了的单子
        List<Map> countAcceptOrdersByUser = faultStatisticDao.countAcceptOrdersByUser(paramMap);
        for(User user : users){
            JSONObject jsonObject = new JSONObject();
            String loginName = user.getLoginname();
            double finishCount = getCountFromListByLoginName(finishedOrderList,loginName);
            double receiveCount = getCountFromListByLoginName(countAcceptOrdersByUser,loginName);
            float rate = 0;
            if(receiveCount == 0){
                rate = 0;
            }else{
                rate = new BigDecimal((float)100*finishCount/receiveCount).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            }
            jsonObject.put("length",(int) rate+"%");//展示条长度
            jsonObject.put("rate",rate+"%");
            jsonObject.put("loginname",loginName);
            jsonObject.put("realname",user.getRealname());
            result.add(jsonObject);
        }
        return result;
    }

    /**
     * 统计工时
     * @param users
     * @param paramMap
     */
    public JSONArray getOrderWorkHours(List<User> users, Map paramMap) {
        JSONArray result = new JSONArray();
        paramMap = initParamMap(paramMap,users);
        List<Map> workHourOrderList = faultStatisticDao.countWorkHoursByUser(paramMap);
        //获取最长hour用于计算比例
        double maxValue = getMaxValueFromList(workHourOrderList);
        for(User user : users){
            JSONObject jsonObject = new JSONObject();
            String loginName = user.getLoginname();
            double finishCount = getCountFromListByLoginName(workHourOrderList,loginName);
            int length = maxValue !=0?(int) (finishCount/maxValue):0;
            float rate = new BigDecimal((float)finishCount/60).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            jsonObject.put("length",100*length+"%");//展示条长度
            jsonObject.put("rate",rate+"小时");
            jsonObject.put("loginname",loginName);
            jsonObject.put("realname",user.getRealname());
            result.add(jsonObject);
        }
        return result;
    }

    /**
     * 统计评分
     * @param users
     * @param paramMap
     */
    public JSONArray getOrderEvaluateScore(List<User> users, Map paramMap) {
        JSONArray result = new JSONArray();
        paramMap = initParamMap(paramMap,users);
        List<Map> scoreOrderList = faultStatisticDao.countEvaluateScoreByUser(paramMap);
        //获取最大数据用于计算比例
        double maxValue = getMaxValueFromList(scoreOrderList);
        for(User user : users){
            JSONObject jsonObject = new JSONObject();
            String loginName = user.getLoginname();
            double finishCount = getCountFromListByLoginName(scoreOrderList,loginName);
            float score = new BigDecimal((float)finishCount).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            int length = maxValue !=0?(int) (score/maxValue):0;
            jsonObject.put("length",100*length+"%");//展示条长度
            jsonObject.put("rate",score+"分");
            jsonObject.put("loginname",loginName);
            jsonObject.put("realname",user.getRealname());
            result.add(jsonObject);
        }
        return result;
    }




    private Map initParamMap(Map paramMap,List<User> users){
        String userStr = "";
        Map<String,User> userInfoMap = new HashMap<>();
        for(User user : users){
            userStr+="'"+user.getLoginname()+"',";
            userInfoMap.put(user.getLoginname(),user);
        }
        if(userStr.length() >0){
            userStr = userStr.substring(0,userStr.length()-1);
        }
        paramMap.put("userStr",userStr);
        paramMap.put("dbName", Global.getConfig("jdbc.type"));

        return paramMap;
    }

}
