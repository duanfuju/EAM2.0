package com.tiansu.eam.modules.schedual.entity;

import com.tiansu.eam.common.persistence.DataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjl on 2017/8/9.
 * 排班类型
 */
public class SchedualType extends DataEntity<SchedualType> {
    //类型编码
    private String type_code;
    //类型名称
    private String type_name;
    //排班时间
    private List<SchedualTypeTime> schedual_time_list;
    //类型描述
    private String type_desc;
    private String type_remark;
    //类型状态
    private String type_status;

    private String schedual_time_str;

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

    public List<SchedualTypeTime> getSchedual_time_list() {
        return schedual_time_list;
    }

    public void setSchedual_time_list(List<SchedualTypeTime> schedual_time_list) {
        this.schedual_time_list = schedual_time_list;
    }

    public String getType_desc() {
        return type_desc;
    }

    public void setType_desc(String type_desc) {
        this.type_desc = type_desc;
    }

    public String getType_remark() {
        return type_remark;
    }

    public void setType_remark(String type_remark) {
        this.type_remark = type_remark;
    }

    public String getType_status() {
        return type_status;
    }

    public void setType_status(String type_status) {
        this.type_status = type_status;
    }

    public String getSchedual_time_str() {
        schedual_time_str = "";
        if(schedual_time_list != null){
            for(SchedualTypeTime schedualTypeTime: schedual_time_list){
                schedual_time_str += schedualTypeTime.getSchedual_begin()+"-"+schedualTypeTime.getSchedual_end()+",";
            }
        }
        schedual_time_str = schedual_time_str.length()>0?schedual_time_str.substring(0,schedual_time_str.length()-1):schedual_time_str;
        return schedual_time_str;
    }

    public void setSchedual_time_str(String schedual_time_str) {
        this.schedual_time_str = schedual_time_str;
    }

    /**
     * @creator duanfuju
     * @createtime 2017/10/11 17:31
     * @description:
     *      排班表类型时间转换（str转对象集合）
     */
    public void schedualTimeStrConvertSchedualTimeList(){
        if(schedual_time_str.length()>0){
            schedual_time_list= new ArrayList<SchedualTypeTime>();
            String[] time1=schedual_time_str.split(",");
            if(time1.length>0){
                for (int i = 0; i < time1.length; i++) {
                    String timestr=time1[i];// 00:00:00-08:00:00
                    if(timestr.length()>0){
                        String[] time=timestr.split("-");
                        SchedualTypeTime schedualTypeTime=new SchedualTypeTime();
                        if(time[0].length()>0){//开始时间
                            schedualTypeTime.setSchedual_begin(time[0]);
                        }
                        if(time[1].length()>0){//结束时间
                            schedualTypeTime.setSchedual_end(time[1]);
                        }
                        if(id.length()>0){//排班类型
                            schedualTypeTime.setType_id(id);
                        }
                        schedualTypeTime.preInsert();
                        schedual_time_list.add(schedualTypeTime);
                    }
                }
            }
        }
    }
}

