package com.tiansu.eam.modules.inspection.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * @author wujh
 * @description 巡检任务工器具实体类
 * @create 2017-10-13 14:19
 **/
public class InspectiontaskTools extends DataEntity<InspectiontaskTools> {

    private static final long serialVersionUID = 1L;

    private String id_key;                 //主键id
    private String inspectiontask_id;      //巡检任务id
    private String material_id;           //工器具id
    private int  tools_num;               //工器具数量
    private String tools_remark;          //工器具备注

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

    public int getTools_num() {
        return tools_num;
    }

    public void setTools_num(int tools_num) {
        this.tools_num = tools_num;
    }

    public String getTools_remark() {
        return tools_remark;
    }

    public void setTools_remark(String tools_remark) {
        this.tools_remark = tools_remark;
    }
}
