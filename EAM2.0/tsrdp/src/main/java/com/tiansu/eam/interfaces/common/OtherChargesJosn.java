package com.tiansu.eam.interfaces.common;

import java.io.Serializable;

/**
 * @author wangr
 * @description 其他费用
 * @create 2017-10-25 上午 11:23
 */
public class OtherChargesJosn implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    /*其他费用*/
    private String name;

    /*金额*/
    private String price;

    /*备注*/
    private String note;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
