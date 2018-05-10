package com.tiansu.eam.interfaces.inspection.entity;

import java.util.List;

/**
 * @author wangr
 * @description
 * @create 2017-10-31 上午 9:07
 */
public class InspectionDetailJson extends InspectionListJson {

    private static final long serialVersionUID = 1L;

    /*区域*/
    private List<AreaDetailJson> area;

    /*工序*/
    private List<?> procedure;

    /*安全措施*/
    private List<?> Safe;

    /*工器具*/
    private List<?> tools;

    /*备件*/
    private List<?> spareparts;

    /*人员工时*/
    private List<?> person;

    /*其他费用*/
    private List<?> others;

    private List<?> history;

    public List<AreaDetailJson> getArea() {
        return area;
    }

    public void setArea(List<AreaDetailJson> area) {
        this.area = area;
    }

    public List<?> getProcedure() {
        return procedure;
    }

    public void setProcedure(List<?> procedure) {
        this.procedure = procedure;
    }

    public List<?> getSafe() {
        return Safe;
    }

    public void setSafe(List<?> safe) {
        Safe = safe;
    }

    public List<?> getTools() {
        return tools;
    }

    public void setTools(List<?> tools) {
        this.tools = tools;
    }

    public List<?> getSpareparts() {
        return spareparts;
    }

    public void setSpareparts(List<?> spareparts) {
        this.spareparts = spareparts;
    }

    public List<?> getPerson() {
        return person;
    }

    public void setPerson(List<?> person) {
        this.person = person;
    }

    public List<?> getOthers() {
        return others;
    }

    public void setOthers(List<?> others) {
        this.others = others;
    }

    public List<?> getHistory() {
        return history;
    }

    public void setHistory(List<?> history) {
        this.history = history;
    }
}
