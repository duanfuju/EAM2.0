package com.tiansu.eam.modules.inspection.entity;

/**
 * @author zhangdf
 * @description
 * @create 2017-10-17 18:57
 **/
public class InsRouteSafe {
    public String id;
    public String inspectionroute_id;
    public String safety_code;
    public String safety_desc;
    public String safety_standard;
    public String safety_remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInspectionroute_id() {
        return inspectionroute_id;
    }

    public void setInspectionroute_id(String inspectionroute_id) {
        this.inspectionroute_id = inspectionroute_id;
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
