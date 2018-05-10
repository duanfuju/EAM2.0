package com.tiansu.eam.modules.inspection.entity;

import com.tiansu.eam.common.persistence.DataEntity;

import java.util.List;

/**
 * @author wujh
 * @description 接收前端传的实体list对象
 * @create 2017-10-25 10:21
 **/
public class TaskFeekBack extends DataEntity<TaskFeekBack> {
    private String inspectiontask_id;    // 要返回的巡检任务id

    private List<InspectiontaskFeedback> inspectiontaskFeedbackList;

    private List<InspectionTaskProcedure> procedureList;  //巡检任务关联工序
    private List<InspectiontaskSafety> safetyList;      //巡检任务关联安全措施
    private List<InspectiontaskTools> toolsList;        //巡检任务关联工器具
    private List<InspectiontaskSpareparts> sparepartsList;  //巡检任务关联备品备件
    private List<InspectiontaskPerson> personList;      //巡检任务关联人员工时
    private List<InspectiontaskOthers> othersList;      //巡检任务关联其他费用

    public TaskFeekBack() {
    }

    public String getInspectiontask_id() {
        return inspectiontask_id;
    }

    public void setInspectiontask_id(String inspectiontask_id) {
        this.inspectiontask_id = inspectiontask_id;
    }

    public List<InspectiontaskFeedback> getInspectiontaskFeedbackList() {
        return inspectiontaskFeedbackList;
    }

    public void setInspectiontaskFeedbackList(List<InspectiontaskFeedback> inspectiontaskFeedbackList) {
        this.inspectiontaskFeedbackList = inspectiontaskFeedbackList;
    }

    public TaskFeekBack(List<InspectiontaskFeedback> inspectiontaskFeedbackList) {
        this.inspectiontaskFeedbackList = inspectiontaskFeedbackList;
    }

    public List<InspectionTaskProcedure> getProcedureList() {
        return procedureList;
    }

    public void setProcedureList(List<InspectionTaskProcedure> procedureList) {
        this.procedureList = procedureList;
    }

    public List<InspectiontaskSafety> getSafetyList() {
        return safetyList;
    }

    public void setSafetyList(List<InspectiontaskSafety> safetyList) {
        this.safetyList = safetyList;
    }

    public List<InspectiontaskTools> getToolsList() {
        return toolsList;
    }

    public void setToolsList(List<InspectiontaskTools> toolsList) {
        this.toolsList = toolsList;
    }

    public List<InspectiontaskSpareparts> getSparepartsList() {
        return sparepartsList;
    }

    public void setSparepartsList(List<InspectiontaskSpareparts> sparepartsList) {
        this.sparepartsList = sparepartsList;
    }

    public List<InspectiontaskPerson> getPersonList() {
        return personList;
    }

    public void setPersonList(List<InspectiontaskPerson> personList) {
        this.personList = personList;
    }

    public List<InspectiontaskOthers> getOthersList() {
        return othersList;
    }

    public void setOthersList(List<InspectiontaskOthers> othersList) {
        this.othersList = othersList;
    }
}
