package com.tiansu.eam.interfaces.common;

import java.io.Serializable;

/**
 * @author wangr
 * @description 工器具和备件
 * @create 2017-10-25 上午 11:22
 */
public class Tool_MaterialsJson implements Serializable {
    private static final long serialVersionUID = 1L;

    /*表id*/
    private String id;

    private String toolId;

    /*名称*/
    private String name;

    /*单位*/
    private String unit;

    /*规格型号*/
    private String model;

    /*数量*/
    private String number;

    /*单价*/
    private String price;

    /*备注*/
    private String note;

    private String total;

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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public String getToolId() {
        return toolId;
    }

    public void setToolId(String toolId) {
        this.toolId = toolId;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
