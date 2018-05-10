package com.tiansu.eam.interfaces.order.entity;

import com.tiansu.eam.interfaces.common.OtherChargesJosn;
import com.tiansu.eam.interfaces.common.PersonJson;
import com.tiansu.eam.interfaces.common.Tool_MaterialsJson;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangr
 * @description 显示反馈信息
 * @create 2017-10-26 下午 5:23
 */
public class OrderFKJson implements Serializable {
    private static final long serialVersionUID = 1L;

    /*工器具*/
    private List<Tool_MaterialsJson> tools;

    /*备件*/
    private List<Tool_MaterialsJson> materials;

    /*人员工时*/
    private List<PersonJson> manHour;

    /*其他费用*/
    private List<OtherChargesJosn> other;

    public List<Tool_MaterialsJson> getTools() {
        return tools;
    }

    public void setTools(List<Tool_MaterialsJson> tools) {
        this.tools = tools;
    }

    public List<Tool_MaterialsJson> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Tool_MaterialsJson> materials) {
        this.materials = materials;
    }

    public List<PersonJson> getManHour() {
        return manHour;
    }

    public void setManHour(List<PersonJson> manHour) {
        this.manHour = manHour;
    }

    public List<OtherChargesJosn> getOther() {
        return other;
    }

    public void setOther(List<OtherChargesJosn> other) {
        this.other = other;
    }
}
