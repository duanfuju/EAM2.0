package com.tiansu.eam.modules.inspection.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.modules.inspection.dao.InspectionRouteDao;
import com.tiansu.eam.modules.inspection.dao.InspectionTaskDao;
import com.tiansu.eam.modules.inspection.entity.*;
import com.tiansu.eam.modules.opestandard.entity.*;
import com.tiansu.eam.modules.sys.service.SysConfigService;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * @author tiansu
 * @description
 * @create 2017-10-13 11:06
 **/
@Service
public class InspectionTaskService extends CrudService<InspectionTaskDao, InspectionTask> {

    @Autowired
    private InspectionTaskDao inspectionTaskDao;
    @Autowired
    private InspectionRouteDao inspectionRouteDao;

    public Map dataTablePageMap(Map map) {

        Map datamap = super.dataTablePageMap(map);
        List<Map> datas = (List<Map>) datamap.get("data");
        StringBuffer route_device = null;
        StringBuffer route_devLoc = null;
        for (Map data : datas) {
            route_device = new StringBuffer();
            route_devLoc = new StringBuffer();
            if (data.get("route_id") != null) {
                List<Map> devlist = inspectionRouteDao.getDevByRouteid((String) data.get("route_id"));
                for (Map devmap : devlist) {
                    route_device.append("," + devmap.get("dev_name"));
                    route_devLoc.append("," + devmap.get("loc_name"));
                }
                if (route_device.indexOf(",") != -1) {
                    data.put("dev_name", route_device.substring(1));
                }
                if (route_devLoc.indexOf(",") != -1) {
                    data.put("dev_locname", route_devLoc.substring(1));
                }
            }
        }
        return datamap;
    }

    /**
     * 根据主键id获取要编辑的对象信息
     *
     * @return
     */
    public Map getEdit(String id) {
        return inspectionTaskDao.getEdit(id);
    }

    public Map getInspectionTaskByPIid(String pstid) {
        Map map = inspectionTaskDao.getInspectionTaskByPIid(pstid);
        List<Map> areaList = inspectionTaskDao.getAreaInfoByTaskPstid(pstid);
        StringBuffer area_name = new StringBuffer();
        for (Map area : areaList) {
            area_name.append("," + area.get("area_name"));
        }
        if (area_name.indexOf(",") != -1) {
            map.put("route_area", area_name.substring(1));
        }
        return map;
    }

    public Map getInspectionTaskById(String inspectionTask_id) {
        Map map = inspectionTaskDao.getInspectionTaskById(inspectionTask_id);
        StringBuffer route_device = new StringBuffer();
        StringBuffer route_devLoc = new StringBuffer();
        if (map.get("route_id") != null) {
            List<Map> devlist = inspectionRouteDao.getDevByRouteid((String) map.get("route_id"));
            for (Map devmap : devlist) {
                route_device.append("," + devmap.get("dev_name"));
                route_devLoc.append("," + devmap.get("loc_name"));
            }
            if (route_device.indexOf(",") != -1) {
                map.put("dev_name", route_device.substring(1));
            }
            if (route_devLoc.indexOf(",") != -1) {
                map.put("dev_locname", route_devLoc.substring(1));
            }
        }
        if (map.get("pstid") != null) {
            List<Map> areaList = inspectionTaskDao.getAreaInfoByTaskPstid(map.get("pstid").toString());
            StringBuffer area_name = new StringBuffer();
            for (Map area : areaList) {
                area_name.append("," + area.get("area_name"));
            }
            if (area_name.indexOf(",") != -1) {
                map.put("area_name", area_name.substring(1));
            }
        }
        return map;

    }

    /**
     * 根据巡检路线批量插入巡检任务
     *
     * @param inspectionTaskList
     */
    public void insert(List<InspectionTask> inspectionTaskList) {
        inspectionTaskDao.insert(inspectionTaskList);
    }

    /**
     * @param toolsList      工器具列表
     * @param sparepartsList 备品备件列表
     * @param personList     人员工时列表
     * @param othersList     其他费用列表
     * @creator wujh
     * @createtime 2017/9/20 14:43
     * @description: 分别给工序/安全措施/工器具/备品备件/人员工时/其他费用插入新增数据
     */
    public void insertDetail(List<InspectiontaskTools> toolsList,
                             List<InspectiontaskSpareparts> sparepartsList, List<InspectiontaskPerson> personList, List<InspectiontaskOthers> othersList) {
//        inspectionTaskDao.insertProcedure(procedureList);
//        inspectionTaskDao.insertSafety(safetyList);
        inspectionTaskDao.insertTools(toolsList);
        inspectionTaskDao.insertSpareparts(sparepartsList);
        inspectionTaskDao.insertPersonHours(personList);
        inspectionTaskDao.insertOthers(othersList);
    }

    /**
     * @param procedureList  工序列表
     * @param safetyList     安全措施列表
     * @param toolsList      工器具列表
     * @param sparepartsList 备品备件列表
     * @param personList     人员工时列表
     * @param othersList     其他费用列表
     * @creator wujh
     * @createtime 2017/9/20 14:43
     * @description: 分别给工序/安全措施/工器具/备品备件/人员工时/其他费用插入新增数据
     * @modifier wangr
     * @modifytime 2017/11/27 下午 5:28
     * @modifyDec: 非空判断
     */
    public void insertDetail1(List<InspectionTaskProcedure> procedureList, List<InspectiontaskSafety> safetyList, List<InspectiontaskTools> toolsList,
                              List<InspectiontaskSpareparts> sparepartsList, List<InspectiontaskPerson> personList, List<InspectiontaskOthers> othersList) {

        if (procedureList != null && procedureList.size() != 0)
            inspectionTaskDao.insertProcedure(procedureList);
        if (safetyList != null && safetyList.size() != 0)
            inspectionTaskDao.insertSafety(safetyList);
        if (toolsList != null && toolsList.size() != 0)
            inspectionTaskDao.insertTools(toolsList);
        if (sparepartsList != null && sparepartsList.size() != 0)
            inspectionTaskDao.insertSpareparts(sparepartsList);
        if (personList != null && personList.size() != 0)
            inspectionTaskDao.insertPersonHours(personList);
        if (othersList != null && othersList.size() != 0)
            inspectionTaskDao.insertOthers(othersList);
    }

    /**
     * @param pstid
     * @return
     * @creator wujh
     * @createtime 2017/9/23 11:49
     * @description: 根据工作流id获取巡检任务的信息
     */
    public Map getInspectionIdByPIid(String pstid) {
        return inspectionTaskDao.getInspectionIdByPIid(pstid);
    }

    /**
     * 修改巡检任务的审批相关信息
     *
     * @param map
     */
    @Transactional
    public void updateAprByPIid(Map map) {
        inspectionTaskDao.updateAprByPIid(map);
    }

    /*
     * 根据巡检任务id/巡检任务流程id获取相应的设备专业和空间区域信息
     * @param map
     */
    public List<Map> getDeviceInfos(Map map) {
        return inspectionTaskDao.getDeviceInfos(map);
    }

    public List<Map> getAreaInfoByTaskPstid(String pstid) {
        return inspectionTaskDao.getAreaInfoByTaskPstid(pstid);
    }

    /**
     * 根据巡检区域id获取巡检项信息
     *
     * @param map 巡检区域id和巡检区域下的设备id
     * @return
     */
    public List<Map> getSubjectInfos(Map map) {
        return inspectionTaskDao.getSubjectInfos(map);
    }

    /**
     * 存储巡检任务反馈结果
     *
     * @param inspectiontaskFeedbackList
     */
    public void insertFeedBackList(List<InspectiontaskFeedback> inspectiontaskFeedbackList) {
        inspectionTaskDao.insertFeedBackList(inspectiontaskFeedbackList);
    }

    /**
     * 根据巡检任务id删除该任务下的已保存的巡检项反馈信息
     *
     * @param map
     */
    public void deleteFeedBack(Map map) {
        inspectionTaskDao.deleteFeedBack(map);
    }

    /**
     * 根据任务id获取当前任务id已反馈保存的巡检项反馈信息
     *
     * @param map
     * @return
     */
    public List<InspectiontaskFeedback> getFeedBackInfoByTaskId(Map map) {
        return inspectionTaskDao.getFeedBackInfoByTaskId(map);
    }

    /**
     * 根据设备id获取标准库下的故障标准信息
     *
     * @param dev_id
     * @return
     */
    public List<StandardFailure> getFailureList(String dev_id) {
        return inspectionTaskDao.getFailureList(dev_id);
    }

    /**
     * @param map
     * @return
     * @creator duanfuju
     * @createtime 2017/10/31 9:27
     * @description: 查询列表
     */
    public List<Map> findListByMap(Map map) {
        return inspectionTaskDao.findListByMap(map);
    }

    /**
     * @param inspectiontask_id
     * @return
     * @creator wujh
     * @createtime 2017/9/18 20:01
     * @description: 根据巡检任务id获取该巡检任务下的工序数据
     */
    public List<Map<String, Object>> getProcedure(String inspectiontask_id) {
        return inspectionTaskDao.getProcedure(inspectiontask_id);
    }

    /**
     * @param id
     * @return
     * @creator wujh
     * @createtime 2017/9/18 20:01
     * @description: 根据巡检任务id获取该巡检任务下的安全措施数据
     */
    public List<Map<String, Object>> getSafety(String id) {
        return inspectionTaskDao.getSafety(id);
    }

    /**
     * @param id
     * @return
     * @creator wujh
     * @createtime 2017/9/18 20:01
     * @description: 根据巡检任务id获取该巡检任务下的工器具数据
     */
    public List<Map<String, Object>> getWorkTools(String id) {
        return inspectionTaskDao.getWorkTools(id);
    }

    /**
     * @param id
     * @return
     * @creator wujh
     * @createtime 2017/9/18 20:01
     * @description: 根据巡检任务id获取该巡检任务下的备品备件数据
     */
    public List<Map<String, Object>> getSpareparts(String id) {
        return inspectionTaskDao.getSpareparts(id);
    }

    /**
     * @param id
     * @return
     * @creator wujh
     * @createtime 2017/9/18 20:01
     * @description: 根据巡检任务id获取该巡检任务下的工序数据
     */
    public List<Map<String, Object>> getPersonhours(String id) {
        return inspectionTaskDao.getPersonhours(id);
    }

    /**
     * @param id
     * @return
     * @creator wujh
     * @createtime 2017/9/18 20:01
     * @description: 根据巡检任务id获取该巡检任务下的其他费用数据
     */
    public List<Map<String, Object>> getOtherexpenses(String id) {
        return inspectionTaskDao.getOtherexpenses(id);
    }

    /**
     * @param map
     * @return
     * @creator wangr
     * @createtime 2017/11/3 0003 下午 5:13
     * @description: 巡检任务反馈时 根据 inspectiontask_id 更新表eam_inspectiontask_feedback字段issubmit为1
     */
    public int updateIssubmit(Map map) {
        return inspectionTaskDao.updateIssubmit(map);
    }

    public List<Map> getPersonInfos(Map map){
        return inspectionTaskDao.getPersonInfos(map);
    }

    public List<Map> getToolInfos(Map map){
        return inspectionTaskDao.getToolInfos(map);
    }

    public List<Map> getSparepartsInfos(Map map){
        return inspectionTaskDao.getSparepartsInfos(map);
    }

    public List<Map> getOtherexpenseInfos(Map map) {
        return inspectionTaskDao.getOtherexpenseInfos(map);
    };

    /**
     * @creator wujh
     * @createtime 2017/12/07
     * @description:
     *  重写列表排序功能
     * @param page
     * @param request
     * @return
     */
    @Override
    public Page setOrderBy(Page page, HttpServletRequest request){
        Enumeration<String> en = request.getParameterNames();
        while (en.hasMoreElements()) {
            String nms = en.nextElement().toString();
            if(nms.startsWith("order") && nms.endsWith("[column]")){
                String column = request.getParameterValues(nms)[0];
                String columnname = request.getParameterValues("columns["+column+"][data]")[0];
                if("0".equals(columnname)||"1".equals(columnname)){
                    return setDefaultOrderBy(page,request);
                }else if(columnname.equals("route_code")||columnname.equals("route_name") || columnname.equals("route_object")){
                    columnname = "route_id";
                } else if(columnname.equals("status_name")) {
                    columnname = "task_status";
                } else if(columnname.equals("realname")) {
                    columnname = "task_processor";
                }
                String orderby = request.getParameterValues("order[0][dir]")[0];
                page.setOrderBy(columnname+" "+orderby);
                break;
            }
        }
        return page;
    }
}
