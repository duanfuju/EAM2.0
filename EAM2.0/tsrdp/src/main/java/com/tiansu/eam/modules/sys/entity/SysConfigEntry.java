package com.tiansu.eam.modules.sys.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * @author wangjl
 * @description
 * @create 2017-08-31 17:03
 **/
public class SysConfigEntry extends DataEntity<SysConfigEntry> {

    private static final long serialVersionUID = 1L;

    private String config_key;
    private String config_value;
    private String config_desc;

    public String getConfig_key() {
        return config_key;
    }

    public void setConfig_key(String config_key) {
        this.config_key = config_key;
    }

    public String getConfig_value() {
        return config_value;
    }

    public void setConfig_value(String config_value) {
        this.config_value = config_value;
    }

    public String getConfig_desc() {
        return config_desc;
    }

    public void setConfig_desc(String config_desc) {
        this.config_desc = config_desc;
    }
}
