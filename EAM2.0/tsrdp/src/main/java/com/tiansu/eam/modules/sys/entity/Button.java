package com.tiansu.eam.modules.sys.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * Created by tiansu on 2017/7/20.
 */
public class Button extends DataEntity<Button> {

    private static final long serialVersionUID = 1L;

    private Menu menu;               //按钮所属菜单

    private ButtonExt buttonExt;    //按钮样式扩展

    private String buttonno;        //按钮编号

    private String buttonname;      //按钮名称

    private String subsystemno;     //子系统编号

    public ButtonExt getButtonExt() {
        return buttonExt;
    }

    public void setButtonExt(ButtonExt buttonExt) {
        this.buttonExt = buttonExt;
    }

    public Button() {
        super();
    }

    public Button(String id, Menu menu, ButtonExt buttonExt) {
        super(id);
        this.menu = menu;
        this.buttonExt = buttonExt;
    }

    public Menu getMenu() {

        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public String getButtonno() {
        return buttonno;
    }

    public void setButtonno(String buttonno) {
        this.buttonno = buttonno;
    }

    public String getButtonname() {
        return buttonname;
    }

    public void setButtonname(String buttonname) {
        this.buttonname = buttonname;
    }

    public String getSubsystemno() {
        return subsystemno;
    }

    public void setSubsystemno(String subsystemno) {
        this.subsystemno = subsystemno;
    }
}
