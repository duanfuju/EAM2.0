package com.tiansu.eam.modules.sys.entity;/**
 * Created by suven on 2017/10/11.
 */


import java.io.Serializable;

/**
 * 系统信息
 *
 * @author suven suven
 * @create 2017/10/11
 */
public class SystemInfo  implements Serializable {
    private static final long serialVersionUID = 1L;

    private String subsystemno;             //子系统号
    private String subsystemname;           //子系统名字
    private String subsystemlink;           //子系统地址
    private Integer orderid;                //排序
    private String version;                 //系统版本
    private String dataconfiglink;          //数据配置连接
    private String datashowlink;            //数据展示链接
    private String hosturl;                 //主机地址
    private String subcallbackurl;          //回调地址

    public SystemInfo() {
    }






    public String getSubsystemno() {
        return subsystemno;
    }

    public void setSubsystemno(String subsystemno) {
        this.subsystemno = subsystemno;
    }

    public String getSubsystemname() {
        return subsystemname;
    }

    public void setSubsystemname(String subsystemname) {
        this.subsystemname = subsystemname;
    }

    public String getSubsystemlink() {
        return subsystemlink;
    }

    public void setSubsystemlink(String subsystemlink) {
        this.subsystemlink = subsystemlink;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDataconfiglink() {
        return dataconfiglink;
    }

    public void setDataconfiglink(String dataconfiglink) {
        this.dataconfiglink = dataconfiglink;
    }

    public String getDatashowlink() {
        return datashowlink;
    }

    public void setDatashowlink(String datashowlink) {
        this.datashowlink = datashowlink;
    }

    public String getHosturl() {
        return hosturl;
    }

    public void setHosturl(String hosturl) {
        this.hosturl = hosturl;
    }

    public String getSubcallbackurl() {
        return subcallbackurl;
    }

    public void setSubcallbackurl(String subcallbackurl) {
        this.subcallbackurl = subcallbackurl;
    }





}
