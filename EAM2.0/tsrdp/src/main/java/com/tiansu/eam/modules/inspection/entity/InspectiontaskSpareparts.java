package com.tiansu.eam.modules.inspection.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * @author wujh
 * @description  巡检任务备品备件实体类
 * @create 2017-10-13 14:20
 **/
public class InspectiontaskSpareparts extends DataEntity<InspectiontaskSpareparts> {

    private static final long serialVersionUID = 1L;

    private String id_key;                 //主键id
    private String inspectiontask_id;      //标准工作id
    private String material_id;           //备品备件id
    private int spareparts_num;          //备品备件数量
    private String spareparts_remark;    //备品备件描述
    private float spareparts_total;    //备品备件小计

    public String getId_key() {
        return id_key;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }

    public String getInspectiontask_id() {
        return inspectiontask_id;
    }

    public void setInspectiontask_id(String inspectiontask_id) {
        this.inspectiontask_id = inspectiontask_id;
    }

    public String getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(String material_id) {
        this.material_id = material_id;
    }

    public int getSpareparts_num() {
        return spareparts_num;
    }

    public void setSpareparts_num(int spareparts_num) {
        this.spareparts_num = spareparts_num;
    }

    public String getSpareparts_remark() {
        return spareparts_remark;
    }

    public void setSpareparts_remark(String spareparts_remark) {
        this.spareparts_remark = spareparts_remark;
    }

    public float getSpareparts_total() {
        return spareparts_total;
    }

    public void setSpareparts_total(float spareparts_total) {
        this.spareparts_total = spareparts_total;
    }
}
