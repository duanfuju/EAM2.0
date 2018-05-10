package com.tiansu.eam.modules.sys.entity;

import com.tiansu.eam.common.persistence.DataEntity;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author wangjl
 * @description
 * @create 2017-09-01 15:21
 **/
public class Attachment extends DataEntity<Attachment> {

    private static final long serialVersionUID = 1L;
    //附件类型
    private String attach_type;
    //附件名称
    private String attach_name;
    //上传路径
    private String attach_path;
    //上传来源
    private String attach_source;
    private String upload_ip;
    private Date upload_time;
    private User upload_by;


    public String getAttach_type() {
        return attach_type;
    }

    public void setAttach_type(String attach_type) {
        this.attach_type = attach_type;
    }

    public String getAttach_name() {
        return attach_name;
    }

    public void setAttach_name(String attach_name) {
        this.attach_name = attach_name;
    }

    public String getAttach_path() {
        return attach_path;
    }

    public void setAttach_path(String attach_path) {
        this.attach_path = attach_path;
    }

    public String getAttach_source() {
        return attach_source;
    }

    public void setAttach_source(String attach_source) {
        this.attach_source = attach_source;
    }

    public String getUpload_ip() {
        return upload_ip;
    }

    public void setUpload_ip(String upload_ip) {
        this.upload_ip = upload_ip;
    }

    public Date getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(Date upload_time) {
        this.upload_time = upload_time;
    }

    public User getUpload_by() {
        return upload_by;
    }

    public void setUpload_by(User upload_by) {
        this.upload_by = upload_by;
    }


    public void preUpload(){
        setId(IdGen.uuid());
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getId())){
            this.upload_by = user;
            this.upload_ip = user.getLoginip();
        }
        this.upload_time = new Date();
    }
}
