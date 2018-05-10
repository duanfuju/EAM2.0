package com.tiansu.eam.modules.opestandard.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * @author wujh
 * @description 标准工作的备品备件实体类
 * @create 2017-09-07 11:27
 **/
public class OperationworkSpareparts extends DataEntity<OperationworkSpareparts> {

    private static final long serialVersionUID = 1L;

    private String id_key;                 //主键id
    private String operationwork_id;      //标准工作id
    private String material_id;           //备品备件id
    private int spareparts_num;          //备品备件数量
    private String spareparts_remark;    //备品备件描述
    private float spareparts_total;    //备品备件小计

    public OperationworkSpareparts() {
    }

    public OperationworkSpareparts(String operationwork_id, String material_id, int spareparts_num, String spareparts_remark, float spareparts_total) {
        this.operationwork_id = operationwork_id;
        this.material_id = material_id;
        this.spareparts_num = spareparts_num;
        this.spareparts_remark = spareparts_remark;
        this.spareparts_total = spareparts_total;
    }

    public String getId_key() {
        return id_key;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }

    public String getOperationwork_id() {
        return operationwork_id;
    }

    public void setOperationwork_id(String operationwork_id) {
        this.operationwork_id = operationwork_id;
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
