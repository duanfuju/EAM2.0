package com.tiansu.eam.interfaces.inspection.entity;


import java.io.Serializable;

/**
 * @author wangr
 * @description
 * @create 2017-11-01 上午 11:42
 */
public class InspectionSparepartsJson implements Serializable {
    private static final long serialVersionUID = 1L;


    private String id;                 //主键id
    private String inspectiontask_id;      //标准工作id
    private String material_id;           //备品备件id
    private int spareparts_num;          //备品备件数量
    private String spareparts_remark;    //备品备件描述
    private float spareparts_total;    //备品备件小计
    /*名称*/
    private String name;

    /*单价*/
    private String price;

    /*单位*/
    private String unit;

    /*规格*/
    private String model;

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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
