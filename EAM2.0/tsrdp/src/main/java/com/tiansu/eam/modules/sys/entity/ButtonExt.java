package com.tiansu.eam.modules.sys.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * Created by tiansu on 2017/7/24.
 */
public class ButtonExt extends DataEntity<ButtonExt> {

    private static final long serialVersionUID = 1L;

    private String buttonno;       //按钮编号

    private String buttonstyle;   //按钮样式

    private String field1;        //预留字段1

    private String field2;        //预留字段2

    public ButtonExt(String buttonno, String buttonStyle) {
        this.buttonno = buttonno;
        this.buttonstyle = buttonStyle;
    }

    public ButtonExt(String id) {
        super(id);
    }

    public String getButtonno() {
        return buttonno;
    }

    public void setButtonno(String buttonno) {
        this.buttonno = buttonno;
    }

    public String getButtonStyle() {
        return buttonstyle;
    }

    public void setButtonStyle(String buttonStyle) {
        this.buttonstyle = buttonStyle;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }
}
