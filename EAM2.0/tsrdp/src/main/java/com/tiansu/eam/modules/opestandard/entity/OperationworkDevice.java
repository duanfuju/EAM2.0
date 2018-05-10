package com.tiansu.eam.modules.opestandard.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * @author wujh
 * @description  标准工作设备表实体类
 * @create 2017-09-07 10:31
 **/
public class OperationworkDevice extends DataEntity<OperationworkDevice> {

    private static final long serialVersionUID = 1L;

    private String id_key;         //主键id
    private String operationwork_id;      //标准工作主键id
    private String dev_id;         //设备主键id

    public OperationworkDevice() {
    }

    public OperationworkDevice(String id_key, String operationwork_id, String dev_id) {
        this.id_key = id_key;
        this.operationwork_id = operationwork_id;
        this.dev_id = dev_id;
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

    public String getDev_id() {
        return dev_id;
    }

    public void setDev_id(String dev_id) {
        this.dev_id = dev_id;
    }
}
