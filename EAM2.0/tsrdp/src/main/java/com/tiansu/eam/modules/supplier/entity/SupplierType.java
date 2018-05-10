package com.tiansu.eam.modules.supplier.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * Created by zhangww on 2017/8/7.
 * 供应商类型mnodel
 */
public class SupplierType extends DataEntity<SupplierType> {
    private String id_key;
    private String type_code;
    private String type_name;
    private String type_desc;
    private String type_status;//状态
    public String getId_key() {
        return id_key;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }


    public String getType_code() {
        return type_code;
    }

    public void setType_code(String type_code) {
        this.type_code = type_code;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getType_desc() {
        return type_desc;
    }

    public void setType_desc(String type_desc) {
        this.type_desc = type_desc;
    }

    public String getType_status() {
        return type_status;
    }

    public void setType_status(String type_status) {
        this.type_status = type_status;
    }
}
