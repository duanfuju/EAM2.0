package com.tiansu.eam.modules.sys.entity;/**
 * Created by suven on 2017/10/11.
 */

/**
 * 菜单信息
 *
 * @author suven suven
 * @create 2017/10/11
 */
public class MenuInfo {
    private static final long serialVersionUID = 1L;
    private String menucode;   // 菜单编号
    private String menuname;    // 名称
    private Integer orderid;    // 排序
    private Integer parentid; // 父级编号
    private String subsystemcode;  //子系统编号
    private String menulink;    // 链接
    private String menuicon;    // 图标
    private Integer menuclass;

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public String getMenuicon() {
        return menuicon;
    }

    public void setMenuicon(String menuicon) {
        this.menuicon = menuicon;
    }

    public Integer getMenuclass() {
        return menuclass;
    }

    public void setMenuclass(Integer menuclass) {
        this.menuclass = menuclass;
    }

    public String getMenucode() {
        return menucode;
    }

    public void setMenucode(String menucode) {
        this.menucode = menucode;
    }

    public String getMenuname() {
        return menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }





    public String getSubsystemcode() {
        return subsystemcode;
    }

    public void setSubsystemcode(String subsystemcode) {
        this.subsystemcode = subsystemcode;
    }

    public String getMenulink() {
        return menulink;
    }

    public void setMenulink(String menulink) {
        this.menulink = menulink;
    }


    }

