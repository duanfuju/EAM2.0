package com.tiansu.eam.modules.inspection.entity;

/**
 * @author zhangww
 * @description  路线区域关联关系
 * @create 2017-10-26 16:03
 **/
public class InspectionRouteArea {

    private String id;
    private String route_id;
    private String area_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }
}
