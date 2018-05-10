package com.tiansu.eam.modules.act.entity;

import com.tiansu.eam.common.persistence.BaseEntity;
import com.tiansu.eam.modules.act.utils.Variable;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.List;
import java.util.Map;

/**
 * Created by marsart on 2017/7/27.
 * @author q1210
 * 工作任务实际的entity
 */
public class EamBusinessTask extends BaseEntity<EamBusinessTask> implements Comparable{

    //流程信息
    private String procInsId; 	// 流程实例ID 对应ProcessInstance
    private String executionId; // 流程执行Id
    private String procDefId; 	// 流程定义ID 对应ProcessDefine
    private String procDefKey; 	// 流程定义Key（流程定义标识）
    private Integer procDefVersion;  // 流程定义版本号
    private String procDefName;  // 流程定义Name

    private int isSuspended;                //是否挂起: 1:挂起,0:未挂起(active)
    private ProcessDefinition procDef; 	    // 流程定义对象
    private ProcessInstance procIns;	    // 流程实例对象

    private String processInstCreateDate;   //流程开始时间
    private String processInstEndeDate;     //流程结束时间
    private String processStarter;          //流程启动者
    private String processFinishReason;     //流程结束原因


    //流程当前任务
    private String taskId; 		// 任务编号
    private String taskName; 	// 任务名称
    private String taskDefKey; 	// 任务定义Key（任务环节标识）
    private Task task; 			// 任务对象

    //绑定业务表和业务信息
    //业务信息
    private String businessCode;    //业务标识（key）
    private String businessName;    //业务名称
    private String businessTable;	// 业务绑定Table
    private String businessId;		// 业务绑定ID

    private String title; 		// 任务标题
    private String status; 		// 任务状态 (todo/claim/finish/delete/hangup)
    private String tasktype;    // 任务类型 (origin任务/approval审批任务/)

    private String assignee; // 任务执行人编号
    private String assigneeName; // 任务执行人名称
    private List<String> candidateGroups;   //EAM中的role,对应activiti
    private List<String> candidateUsers;    //用户组

    private Variable vars; 		// 流程变量

    private String taskCreateDate; //任务创建时间
    private String taskEndDate; //任务结束时间

    private String currentTaskInfo;//流程所处当前节点的任务信息

    private String undoPerson;//当前节点任务未处理人



    public String getCurrentTaskInfo() {
        return currentTaskInfo;
    }

    public void setCurrentTaskInfo(String currentTaskInfo) {
        this.currentTaskInfo = currentTaskInfo;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getProcessFinishReason() {
        return processFinishReason;
    }

    public void setProcessFinishReason(String processFinishReason) {
        this.processFinishReason = processFinishReason;
    }

    public String getProcessStarter() {
        return processStarter;
    }

    public void setProcessStarter(String processStarter) {
        this.processStarter = processStarter;
    }

    public String getProcessInstCreateDate() {
        return processInstCreateDate;
    }

    public void setProcessInstCreateDate(String processInstCreateDate) {
        this.processInstCreateDate = processInstCreateDate;
    }

    public String getProcessInstEndeDate() {
        return processInstEndeDate;
    }

    public void setProcessInstEndeDate(String processInstEndeDate) {
        this.processInstEndeDate = processInstEndeDate;
    }

    public int getIsSuspended() {
        return isSuspended;
    }

    public void setIsSuspended(int isSuspended) {
        this.isSuspended = isSuspended;
    }
    public Integer getProcDefVersion() {
        return procDefVersion;
    }

    public void setProcDefVersion(Integer procDefVersion) {
        this.procDefVersion = procDefVersion;
    }

    public String getProcDefName() {
        return procDefName;
    }

    public void setProcDefName(String procDefName) {
        this.procDefName = procDefName;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getTasktype() {
        return tasktype;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }

    public String getTaskEndDate() {
        return taskEndDate;
    }

    public void setTaskEndDate(String taskEndDate) {
        this.taskEndDate = taskEndDate;
    }

    public String getTaskCreateDate() {
        return taskCreateDate;
    }

    public void setTaskCreateDate(String taskCreateDate) {
        this.taskCreateDate = taskCreateDate;
    }

    public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public String getProcDefKey() {
        return procDefKey;
    }

    public void setProcDefKey(String procDefKey) {
        this.procDefKey = procDefKey;
    }

    public ProcessDefinition getProcDef() {
        return procDef;
    }

    public void setProcDef(ProcessDefinition procDef) {
        this.procDef = procDef;
    }

    public ProcessInstance getProcIns() {
        return procIns;
    }

    public void setProcIns(ProcessInstance procIns) {
        this.procIns = procIns;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getBusinessTable() {
        return businessTable;
    }

    public void setBusinessTable(String businessTable) {
        this.businessTable = businessTable;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public Variable getVars() {
        return vars;
    }

    public void setVars(Variable vars) {
        this.vars = vars;
    }
    public void setVars(Map<String, Object> map) {
        this.vars = new Variable(map);
    }

    public List<String> getCandidateGroups() {
        return candidateGroups;
    }

    public void setCandidateGroups(List<String> candidateGroups) {
        this.candidateGroups = candidateGroups;
    }

    public List<String> getCandidateUsers() {
        return candidateUsers;
    }

    public void setCandidateUsers(List<String> candidateUsers) {
        this.candidateUsers = candidateUsers;
    }

    public String getUndoPerson() {
        return undoPerson;
    }

    public void setUndoPerson(String undoPerson) {
        this.undoPerson = undoPerson;
    }

    @Override
    public int compareTo(Object o) {
        return this.getTaskCreateDate().compareTo(((EamBusinessTask)o).getTaskCreateDate());
    }

    @Override
    public void preInsert() {

    }

    @Override
    public void preUpdate() {

    }
}
