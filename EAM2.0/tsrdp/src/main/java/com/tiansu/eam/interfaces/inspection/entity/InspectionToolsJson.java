package com.tiansu.eam.interfaces.inspection.entity;


import java.io.Serializable;

/**
 * @author wangr
 * @description
 * @create 2017-11-01 上午 11:37
 */
public class InspectionToolsJson implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;                 //主键id
    private String inspectionRoute_id;      //巡检任务id
    private String material_id;           //工器具id
    private int  tools_num;               //工器具数量
    private String tools_remark;          //工器具备注

    /*名称*/
    private String name;

    /*单价*/
    private String price;

    /*单位*/
    private String unit;

    /*规格*/
    private String model;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInspectionRoute_id() {
        return inspectionRoute_id;
    }

    public void setInspectionRoute_id(String inspectionRoute_id) {
        this.inspectionRoute_id = inspectionRoute_id;
    }

    public String getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(String material_id) {
        this.material_id = material_id;
    }

    public int getTools_num() {
        return tools_num;
    }

    public void setTools_num(int tools_num) {
        this.tools_num = tools_num;
    }

    public String getTools_remark() {
        return tools_remark;
    }

    public void setTools_remark(String tools_remark) {
        this.tools_remark = tools_remark;
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
}
