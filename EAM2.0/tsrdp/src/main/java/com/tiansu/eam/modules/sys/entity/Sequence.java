package com.tiansu.eam.modules.sys.entity;

import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;

/**
 * Created by zhangdf on 2017/7/25.
 */
public class Sequence extends DataEntity<Sequence> {
    private String seq_type;//序列类型
    private String seq_const;//序列前缀
    private String seq_timestamp;//时间类型(yyyy-mm-dd)
    private Integer serial_no;//流水号
    private Integer seq_length;//序列号长度
    private Integer seq_incre;//增长幅度
    private Integer seq_clean;//(0清空1不清)
    private Date seq_currenttime;//当前时间

    public Date getSeq_currenttime() {
        return seq_currenttime;
    }

    public void setSeq_currenttime(Date seq_currenttime) {
        this.seq_currenttime = seq_currenttime;
    }

    public String getSeq_type() {

        return seq_type;
    }

    public void setSeq_type(String seq_type) {
        this.seq_type = seq_type;
    }

    public String getSeq_const() {
        return seq_const;
    }

    public void setSeq_const(String seq_const) {
        this.seq_const = seq_const;
    }

    public String getSeq_timestamp() {
        return seq_timestamp;
    }

    public void setSeq_timestamp(String seq_timestamp) {
        this.seq_timestamp = seq_timestamp;
    }

    public Integer getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(Integer serial_no) {
        this.serial_no = serial_no;
    }

    public Integer getSeq_length() {
        return seq_length;
    }

    public void setSeq_length(Integer seq_length) {
        this.seq_length = seq_length;
    }

    public Integer getSeq_incre() {
        return seq_incre;
    }

    public void setSeq_incre(Integer seq_incre) {
        this.seq_incre = seq_incre;
    }

    public Integer getSeq_clean() {
        return seq_clean;
    }

    public void setSeq_clean(Integer seq_clean) {
        this.seq_clean = seq_clean;
    }
}
