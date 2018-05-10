package com.tiansu.eam.interfaces.maintain.service;

import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.interfaces.act.service.TaskRestService;
import com.tiansu.eam.interfaces.common.History;
import com.tiansu.eam.interfaces.maintain.dao.MaintainRestDao;
import com.tiansu.eam.interfaces.maintain.entity.MaintainContentJson;
import com.tiansu.eam.interfaces.maintain.entity.MaintainDetailJson;
import com.tiansu.eam.interfaces.maintain.entity.MaintainListJson;
import com.tiansu.eam.modules.sys.entity.User;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.impl.persistence.entity.HistoricFormPropertyEntity;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.ArrayList;

/**
 * @author wangr
 * @description 保养接口业务类
 * @create 2017-11-06 下午 3:21
 */
@Service
@Transactional(readOnly = true)
public class MaintainRestService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public static final String USERTASK = "userTask";

    @Autowired
    private MaintainRestDao maintainRestDao;

    @Autowired
    private TaskRestService taskRestService;

    @Autowired
    private HistoryService historyService;

    /**
     * @param param
     * @return
     * @creator wangr
     * @createtime 2017/11/7 0007 下午 3:47
     * @description: 获取我的保养列表
     */
    public Map getMaintainList(Map param) {

        param.put("processDefKey", EAMConsts.MAINTAIN_FLOW);
        logger.info("==== 获取我的保修列表 进入代办查询");
        Map map = taskRestService.todoList(param);
        if (!(boolean) map.get("success")) return map;

        List<String> pstids = (List<String>) map.get("data");


        if (null == pstids || pstids.size() == 0) {
            return map;
        }
        List<Task> taskList = (List<Task>) map.get("task");
        param.put("list", pstids);
        param.put("orderBy", "a.task_time_plan desc");
        List<MaintainListJson> maintainList = maintainRestDao.getMaintainList(param);

        for (int i = 0; i < maintainList.size(); i++) {
            for (int j = 0; j < taskList.size(); j++) {
                if (taskList.get(j).getProcessInstanceId().equals(maintainList.get(i).getPstid())) {
                    maintainList.get(i).setTaskId(taskList.get(j).getId());
                    maintainList.get(i).setTaskDefKey(taskList.get(j).getTaskDefinitionKey());
                    logger.info("==== 流程id：" + maintainList.get(i).getPstid());
                    logger.info("==== 流程任务id：" + taskList.get(j).getId());
                    logger.info("==== 流程任务key：" + taskList.get(j).getTaskDefinitionKey());
                    break;
                }
            }
        }
        map.remove("task");
        map.put("data", maintainList);
        return map;
    }

    /**
     * @param pstid  流程id
     * @param devId  设备id
     * @param userId 用户id
     * @return
     * @creator lihj
     * @createtime 2017/11/7 002 下午 1:58
     * @description: 根据保养任务流程id获取信息
     * @modifier wangr
     * @modifytime 2017/11/10 0010 下午 2:15
     * @modifyDec: 设备id和用户id不为空时代表扫码查看保养详情
     */
    public List<MaintainDetailJson> getMaintainDetail(String pstid, String devId,
                                                      String userId, int flag) {
        Map param = new HashMap();
        param.put("devId", devId);
        param.put("userId", userId);
        param.put("pstid", pstid);

        List<MaintainDetailJson> maintainList = maintainRestDao.getMaintainById(param);
        for (MaintainDetailJson maintain : maintainList) {
            if (flag == 1) {
                //获取保养项
                List<MaintainContentJson> maintainContentJson = getMaintainContent(maintain);
                maintain.setMaintainContentJson(maintainContentJson);
            }
            if ("2".equals(maintain.getStatus())) {
                String maintainId = maintain.getMaintainId();
//                maintain.setProcedure(maintainRestDao.getProcedureFK(maintainId));
//                maintain.setSafe(maintainRestDao.getSafetyFK(maintainId));
                maintain.setTools(maintainRestDao.getMaintainToolsFK(maintainId));
                maintain.setMaterials(maintainRestDao.getMaintainSparepartsFK(maintainId));
                maintain.setManHour(maintainRestDao.getPersonhoursFK(maintainId));
                maintain.setOther(maintainRestDao.getOtherexpensesFK(maintainId));
            } else {
                String maintainId = maintain.getMaintainId();
//                maintain.setProcedure(maintainRestDao.getProcedure(maintainId));
//                maintain.setSafe(maintainRestDao.getSafety(maintainId));
                maintain.setTools(maintainRestDao.getMaintainTools(maintainId));
                maintain.setMaterials(maintainRestDao.getMaintainSpareparts(maintainId));
                maintain.setManHour(maintainRestDao.getPersonhours(maintainId));
                maintain.setOther(maintainRestDao.getOtherexpenses(maintainId));
            }
            if (flag == 0) {
                List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(maintain.getPstid()).list();
                List<HistoricDetail> formProperties = historyService.createHistoricDetailQuery().processInstanceId(maintain.getPstid()).formProperties().list();
                getHistoryList(maintain, activityInstances, formProperties);
            }
        }
        return maintainList;
    }

    /**
     * @param maintain
     * @param activityList
     * @param formPropertiesList
     * @return
     * @creator wangr
     * @createtime 2017/11/17 0017 下午 3:00
     * @description: 用于保养历史流程
     */
    public MaintainDetailJson getHistoryList(MaintainDetailJson maintain,
                                             List<HistoricActivityInstance> activityList,
                                             List<HistoricDetail> formPropertiesList) {
        //用于分装整理之后的历史流程数据
        List<History> histories = new ArrayList<>();
        //循环操作历史任务流程记录
        for (HistoricActivityInstance activity : activityList) {
            //userTask  用户可以处理的任务流程节点
            if (USERTASK.equals(activity.getActivityType())) {
                History history = new History();
                //流程节点名称 和 开始时间
                history.setActivityName(activity.getActivityName());
                history.setActivityTime(activity.getStartTime());
                //将userId转换成用户
                User user = UserUtils.get(activity.getAssignee());
                if (user != null) {
                    //根据任务流程中的任务处理人userId转换成用户名 手机
                    history.setUserName(user.getRealname());
                    history.setPhone(user.getLoginphone());
                }
                //循环操作 历史任务流程中表单提价的数据
                for (HistoricDetail detail : formPropertiesList) {
                    HistoricFormPropertyEntity historicFormPropertyEntity = (HistoricFormPropertyEntity) detail;
                    //多个表单提交数据记录对应一个任务节点id  所以要根据任务id进行对比
                    if (activity.getTaskId().equals(detail.getTaskId())) {
                        String propertyId = historicFormPropertyEntity.getPropertyId();
                        String propertyValue = historicFormPropertyEntity.getPropertyValue();
                        //保养历史流程中 的转单 撤销  挂起 审批等的原因和建议
                        if ("requestReason".equals(propertyId) ||
                                "approveReason".equals(propertyId)) {
                            history.setReason(propertyValue);
                        }
                        //保养历史流程中 审批是否同意
                        if ("deptLeaderApproved".equals(propertyId)) {

                            history.setResult("true".equals(propertyValue) ? "同意" : "拒绝");
                        }
                        //保养历史流程中 转单节点的转给的某个用户
                        if ("switch_person".equals(propertyId)) {
                            User user1 = UserUtils.get(propertyValue);
                            history.setSwitchPerson(user1.getRealname());
                            history.setSwitchPhone(user1.getLoginphone());
                        }
                    }
                }
                histories.add(history);
            }


        }
        //放入工单历史流程列表中
        maintain.setHistory(histories);
        return maintain;
    }

    /**
     * 获取保养项
     *
     * @param maintain
     * @return
     */
    public List<MaintainContentJson> getMaintainContent(MaintainDetailJson maintain) {
        List<MaintainContentJson> maintainContentJson = new ArrayList<>();
        String itemCode = maintain.getMaintainCode();
        String itemContent = maintain.getMaintainContent();
        String itemMark = maintain.getMaintainNote();
        String itemFlag = maintain.getFlag();
        if (itemCode != null && itemCode.length() > 0) {
            String itemCodeSplit[] = itemCode.split(",");
            for (int i = 0; i < itemCodeSplit.length; i++) {
                MaintainContentJson item = new MaintainContentJson();
                if (itemContent != null && itemContent.length() > 0) {
                    String itemContentSplit[] = itemContent.split(",");
                    item.setMaintainContent(itemContentSplit[i]);
                }
                if (itemMark != null && !"".equals(itemMark)) {
                    String itemMarkSplit[] = itemMark.split(",");
                    item.setMaintainNote(itemMarkSplit[i]);
                }
                if (itemFlag != null && itemFlag.length() > 0) {
                    String itemFlagSplit[] = itemFlag.split(";");
                    item.setFlag(itemFlagSplit[i]);
                }
                item.setMaintainCode(itemCodeSplit[i]);
                maintainContentJson.add(item);
            }
        }
        return maintainContentJson;
    }
}
