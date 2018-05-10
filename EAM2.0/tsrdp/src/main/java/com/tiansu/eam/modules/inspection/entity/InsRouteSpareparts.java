package com.tiansu.eam.modules.inspection.entity;

/**
 * @author zhangdf
 * @description
 * @create 2017-10-17 18:59
 **/
public class InsRouteSpareparts {
    public String id;
    public String inspectionroute_id;
    public String material_id;
    public String  spareparts_num;
    public String spareparts_remark;
    public String spareparts_total;

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

    public String getSpareparts_num() {
        return spareparts_num;
    }

    public void setSpareparts_num(String spareparts_num) {
        this.spareparts_num = spareparts_num;
    }

    public String getSpareparts_remark() {
        return spareparts_remark;
    }

    public void setSpareparts_remark(String spareparts_remark) {
        this.spareparts_remark = spareparts_remark;
    }

    public String getSpareparts_total() {
        return spareparts_total;
    }

    public void setSpareparts_total(String spareparts_total) {
        this.spareparts_total = spareparts_total;
    }
}
