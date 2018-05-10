package com.tiansu.eam.modules.schedual.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * @author wangjl
 * @description
 * @create 2017-09-26 11:31
 **/
public class SchedualTypeTime extends DataEntity<SchedualTypeTime> {
    //排班类型关联id
    private String type_id;
    //排班开始时间
    private String schedual_begin;
    //排班结束时间
    private String schedual_end;

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getSchedual_begin() {
        return schedual_begin;
    }

    public void setSchedual_begin(String schedual_begin) {
        this.schedual_begin = schedual_begin;
    }

    public String getSchedual_end() {
        return schedual_end;
    }

    public void setSchedual_end(String schedual_end) {
        this.schedual_end = schedual_end;
    }
}

