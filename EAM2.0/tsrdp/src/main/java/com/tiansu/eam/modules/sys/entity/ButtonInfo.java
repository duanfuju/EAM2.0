package com.tiansu.eam.modules.sys.entity;/**
 * Created by suven on 2017/10/11.
 */

/**
 * 按钮信息
 *
 * @author suven suven
 * @create 2017/10/11
 */
public class ButtonInfo {
    private static final long serialVersionUID = 1L;

    private String buttoncode;        //按钮编号
    private String buttonname;      //按钮名称
    private String menucode;
    private String subsystemcode;     //子系统编号
    private Integer orderid;



    public String getButtoncode() {
        return buttoncode;
    }

    public void setButtoncode(String buttoncode) {
        this.buttoncode = buttoncode;
    }

    public String getButtonname() {
        return buttonname;
    }

    public void setButtonname(String buttonname) {
        this.buttonname = buttonname;
    }

    public String getMenucode() {
        return menucode;
    }

    public void setMenucode(String menucode) {
        this.menucode = menucode;
    }

    public String getSubsystemcode() {
        return subsystemcode;
    }

    public void setSubsystemcode(String subsystemcode) {
        this.subsystemcode = subsystemcode;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }
}
