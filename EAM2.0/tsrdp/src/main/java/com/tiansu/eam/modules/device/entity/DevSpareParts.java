package com.tiansu.eam.modules.device.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * @author wujh
 * @description 设备下所属的备品备件和工器具实体类
 * @create 2017-09-05 16:28
 **/
public class DevSpareParts extends DataEntity<DevSpareParts> {

    private static final long serialVersionUID = 1L;

    //设备id
    private String dev_id;

    // 物料id
    private String material_id;

    //区分备品备件和工器具的标识
    private String type_flag;

    public DevSpareParts(){
        super();
    }

    public DevSpareParts(String dev_id, String material_id, String type_flag) {
        this.dev_id = dev_id;
        this.material_id = material_id;
        this.type_flag = type_flag;
    }

    public String getDev_id() {
        return dev_id;
    }

    public void setDev_id(String dev_id) {
        this.dev_id = dev_id;
    }

    public String getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(String material_id) {
        this.material_id = material_id;
    }

    public String getType_flag() {
        return type_flag;
    }

    public void setType_flag(String type_flag) {
        this.type_flag = type_flag;
    }
}
