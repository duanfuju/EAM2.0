package com.tiansu.eam.modules.opestandard.entity;

import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;
import java.util.List;

/**
 * @author wujh
 * @description 标准工作实体类
 * @create 2017-09-07 9:59
 **/
public class OperationWork extends DataEntity<OperationWork> {

    private static final long serialVersionUID = 1L;

    private String id_key;                    //主键id
    private String operationwork_code;      //标准工作编码
    private String operationwork_content;   //标准工作内容
    private String operationwork_type;      //标准工作类型
    private String operationwork_totaltime; //标准工作额定总工时
    private String approve_by;               //标准工作审批人
    private String approve_reason;          //标准工作审批原因
    private Date approve_time;              //标准工作审批时间
    private String approve_status;          //标准工作审批状态
    private String operationwork_status;   //标准工作状态
    private String pstid; 				//流程实例id
    private List<OperationworkDevice> deviceList;      //标准工作关联设备
    private List<OperationworkProcedure> procedureList;  //标准工作关联工序
    private List<OperationworkSafety> safetyList;      //标准工作关联安全措施
    private List<OperationworkTools> toolsList;        //标准工作关联工器具
    private List<OperationworkSpareparts> sparepartsList;  //标准工作关联备品备件
    private List<OperationworkPerson> personList;      //标准工作关联人员工时
    private List<OperationworkOthers> othersList;      //标准工作关联其他费用

    public OperationWork() {
    }

    public OperationWork(String id_key, String operationwork_code, String operationwork_content, String operationwork_type, String operationwork_totaltime, String approve_by, String approve_reason, Date approve_time, String approve_status, String operationwork_status) {
        this.id_key = id_key;
        this.operationwork_code = operationwork_code;
        this.operationwork_content = operationwork_content;
        this.operationwork_type = operationwork_type;
        this.operationwork_totaltime = operationwork_totaltime;
        this.approve_by = approve_by;
        this.approve_reason = approve_reason;
        this.approve_time = approve_time;
        this.approve_status = approve_status;
        this.operationwork_status = operationwork_status;
    }

    public String getId_key() {
        return id_key;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }

    public String getOperationwork_code() {
        return operationwork_code;
    }

    public void setOperationwork_code(String operationwork_code) {
        this.operationwork_code = operationwork_code;
    }

    public String getOperationwork_content() {
        return operationwork_content;
    }

    public void setOperationwork_content(String operationwork_content) {
        this.operationwork_content = operationwork_content;
    }

    public String getOperationwork_type() {
        return operationwork_type;
    }

    public void setOperationwork_type(String operationwork_type) {
        this.operationwork_type = operationwork_type;
    }

    public String getOperationwork_totaltime() {
        return operationwork_totaltime;
    }

    public void setOperationwork_totaltime(String operationwork_totaltime) {
        this.operationwork_totaltime = operationwork_totaltime;
    }

    public String getApprove_by() {
        return approve_by;
    }

    public void setApprove_by(String approve_by) {
        this.approve_by = approve_by;
    }

    public String getApprove_reason() {
        return approve_reason;
    }

    public void setApprove_reason(String approve_reason) {
        this.approve_reason = approve_reason;
    }

    public Date getApprove_time() {
        return approve_time;
    }

    public void setApprove_time(Date approve_time) {
        this.approve_time = approve_time;
    }

    public String getApprove_status() {
        return approve_status;
    }

    public void setApprove_status(String approve_status) {
        this.approve_status = approve_status;
    }

    public String getOperationwork_status() {
        return operationwork_status;
    }

    public void setOperationwork_status(String operationwork_status) {
        this.operationwork_status = operationwork_status;
    }

    public String getPstid() {
        return pstid;
    }

    public void setPstid(String pstid) {
        this.pstid = pstid;
    }

    public List<OperationworkDevice> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<OperationworkDevice> deviceList) {
        this.deviceList = deviceList;
    }

    public List<OperationworkProcedure> getProcedureList() {
        return procedureList;
    }

    public void setProcedureList(List<OperationworkProcedure> procedureList) {
        this.procedureList = procedureList;
    }

    public List<OperationworkSafety> getSafetyList() {
        return safetyList;
    }

    public void setSafetyList(List<OperationworkSafety> safetyList) {
        this.safetyList = safetyList;
    }

    public List<OperationworkTools> getToolsList() {
        return toolsList;
    }

    public void setToolsList(List<OperationworkTools> toolsList) {
        this.toolsList = toolsList;
    }

    public List<OperationworkSpareparts> getSparepartsList() {
        return sparepartsList;
    }

    public void setSparepartsList(List<OperationworkSpareparts> sparepartsList) {
        this.sparepartsList = sparepartsList;
    }

    public List<OperationworkPerson> getPersonList() {
        return personList;
    }

    public void setPersonList(List<OperationworkPerson> personList) {
        this.personList = personList;
    }

    public List<OperationworkOthers> getOthersList() {
        return othersList;
    }

    public void setOthersList(List<OperationworkOthers> othersList) {
        this.othersList = othersList;
    }
}
