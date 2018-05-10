package com.tiansu.eam.interfaces.common;

import java.io.Serializable;

/**
 * @author wujh
 * @description 巡检任务安全措施实体类
 * @create 2017-10-13 14:17
 **/
public class SafetyJson implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;                 //主键id
    private String inspectiontask_id;      //标准工作id
    private String safety_code;           //安全措施编码
    private String safety_desc;           //安全措施描述
    private String safety_standard;      //安全措施质检标准
    private String safety_remark;        //安全措施备注id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInspectiontask_id() {
        return inspectiontask_id;
    }

    public void setInspectiontask_id(String inspectiontask_id) {
        this.inspectiontask_id = inspectiontask_id;
    }

    public String getSafety_code() {
        return safety_code;
    }

    public void setSafety_code(String safety_code) {
        this.safety_code = safety_code;
    }

    public String getSafety_desc() {
        return safety_desc;
    }

    public void setSafety_desc(String safety_desc) {
        this.safety_desc = safety_desc;
    }

    public String getSafety_standard() {
        return safety_standard;
    }

    public void setSafety_standard(String safety_standard) {
        this.safety_standard = safety_standard;
    }

    public String getSafety_remark() {
        return safety_remark;
    }

    public void setSafety_remark(String safety_remark) {
        this.safety_remark = safety_remark;
    }
}
