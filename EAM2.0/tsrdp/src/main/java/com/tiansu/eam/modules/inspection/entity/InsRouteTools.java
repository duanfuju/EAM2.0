package com.tiansu.eam.modules.inspection.entity;

/**
 * @author zhangdf
 * @description
 * @create 2017-10-17 18:57
 **/
public class InsRouteTools {
    public String id;
    public String inspectionroute_id;
    public String material_id;
    public String tools_num;
    public String tools_remark;

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

    public String getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(String material_id) {
        this.material_id = material_id;
    }

    public String getTools_num() {
        return tools_num;
    }

    public void setTools_num(String tools_num) {
        this.tools_num = tools_num;
    }

    public String getTools_remark() {
        return tools_remark;
    }

    public void setTools_remark(String tools_remark) {
        this.tools_remark = tools_remark;
    }
}
