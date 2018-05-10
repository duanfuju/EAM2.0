package com.tiansu.eam.modules.inspection.entity;

import com.tiansu.eam.common.persistence.DataEntity;
import com.tiansu.eam.modules.sys.entity.User;

import java.util.Date;
import java.util.List;

/**
 * @author zhangdf
 * @description
 * @create 2017-10-13 10:29
 **/
public class InspectionRoute extends DataEntity<InspectionRoute> {
    private String id_key;
    private String route_code;
    private String route_name;
    private String route_object;
    private Integer route_type;//分类
    private Integer route_mode;//类型
    private Integer route_period;//周期
    private Integer route_isstandard;//是否标准工作
    private String route_stand;//引用的标准(0,编码-标准工作，1,编码-标准工单)
    private String route_totalhour;
    private String route_period_detail;
    private String route_taskdates;//巡检时间段
    private String route_enableperiod;//启用时间
    private Integer approve_type;//0录入审批1作废审批
    private User approve_by;//审批人
    private String approve_reason;
    private Date approve_time;
    private String approve_status;//审批状态
    private String route_area;//区域id
    //private String route_areaname;//区域名称
    private String route_status;//状态
    private String isClosed;//0-未关闭，1-申请关闭，2-关闭,-1退回
    private String close_reason;
    private String pstid;
    private List<InsRouteProc> insRouteProcList;
    private List<InsRouteSafe> insRouteSafeList;
    private List<InsRouteTools> insRouteToolsList;
    private List<InsRouteSpareparts> insRouteSparepartsList;
    private List<InsRoutePerson> insRoutePersonList;
    private List<InsRouteOthers> insRouteOthersList;

    public String getId_key() {
        return id_key;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }

    public String getRoute_code() {
        return route_code;
    }

    public void setRoute_code(String route_code) {
        this.route_code = route_code;
    }

    public String getRoute_name() {
        return route_name;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }

    public String getRoute_object() {
        return route_object;
    }

    public void setRoute_object(String route_object) {
        this.route_object = route_object;
    }

    public Integer getRoute_type() {
        return route_type;
    }

    public void setRoute_type(Integer route_type) {
        this.route_type = route_type;
    }

    public Integer getRoute_mode() {
        return route_mode;
    }

    public void setRoute_mode(Integer route_mode) {
        this.route_mode = route_mode;
    }

    public Integer getRoute_period() {
        return route_period;
    }

    public void setRoute_period(Integer route_period) {
        this.route_period = route_period;
    }

    public Integer getRoute_isstandard() {
        return route_isstandard;
    }

    public void setRoute_isstandard(Integer route_isstandard) {
        this.route_isstandard = route_isstandard;
    }

    public String getRoute_stand() {
        return route_stand;
    }

    public void setRoute_stand(String route_stand) {
        this.route_stand = route_stand;
    }

    public String getRoute_totalhour() {
        return route_totalhour;
    }

    public void setRoute_totalhour(String route_totalhour) {
        this.route_totalhour = route_totalhour;
    }

    public String getRoute_period_detail() {
        return route_period_detail;
    }

    public void setRoute_period_detail(String route_period_detail) {
        this.route_period_detail = route_period_detail;
    }

    public String getRoute_taskdates() {
        return route_taskdates;
    }

    public void setRoute_taskdates(String route_taskdates) {
        this.route_taskdates = route_taskdates;
    }

    public String getRoute_enableperiod() {
        return route_enableperiod;
    }

    public void setRoute_enableperiod(String route_enableperiod) {
        this.route_enableperiod = route_enableperiod;
    }

    public Integer getApprove_type() {
        return approve_type;
    }

    public void setApprove_type(Integer approve_type) {
        this.approve_type = approve_type;
    }

    public User getApprove_by() {
        return approve_by;
    }

    public void setApprove_by(User approve_by) {
        this.approve_by = approve_by;
    }

    public String getApprove_reason() {
        return approve_reason;
    }

    public void setApprove_reason(String approve_reason) {
        this.approve_reason = approve_reason;
    }

    public Date getApprove_time() {
        return approve_time;
    }

    public void setApprove_time(Date approve_time) {
        this.approve_time = approve_time;
    }

    public String getApprove_status() {
        return approve_status;
    }

    public void setApprove_status(String approve_status) {
        this.approve_status = approve_status;
    }

    public String getRoute_area() {
        return route_area;
    }

    public void setRoute_area(String route_area) {
        this.route_area = route_area;
    }

    public String getRoute_status() {
        return route_status;
    }

    public void setRoute_status(String route_status) {
        this.route_status = route_status;
    }

    public String getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(String isClosed) {
        this.isClosed = isClosed;
    }

    public String getClose_reason() {
        return close_reason;
    }

    public void setClose_reason(String close_reason) {
        this.close_reason = close_reason;
    }

    public List<InsRouteProc> getInsRouteProcList() {
        return insRouteProcList;
    }

    public void setInsRouteProcList(List<InsRouteProc> insRouteProcList) {
        this.insRouteProcList = insRouteProcList;
    }

    public List<InsRouteSafe> getInsRouteSafeList() {
        return insRouteSafeList;
    }

    public void setInsRouteSafeList(List<InsRouteSafe> insRouteSafeList) {
        this.insRouteSafeList = insRouteSafeList;
    }

    public List<InsRouteTools> getInsRouteToolsList() {
        return insRouteToolsList;
    }

    public void setInsRouteToolsList(List<InsRouteTools> insRouteToolsList) {
        this.insRouteToolsList = insRouteToolsList;
    }

    public List<InsRouteSpareparts> getInsRouteSparepartsList() {
        return insRouteSparepartsList;
    }

    public void setInsRouteSparepartsList(List<InsRouteSpareparts> insRouteSparepartsList) {
        this.insRouteSparepartsList = insRouteSparepartsList;
    }

    public List<InsRoutePerson> getInsRoutePersonList() {
        return insRoutePersonList;
    }

    public void setInsRoutePersonList(List<InsRoutePerson> insRoutePersonList) {
        this.insRoutePersonList = insRoutePersonList;
    }

    public List<InsRouteOthers> getInsRouteOthersList() {
        return insRouteOthersList;
    }

    public void setInsRouteOthersList(List<InsRouteOthers> insRouteOthersList) {
        this.insRouteOthersList = insRouteOthersList;
    }

    public String getPstid() {
        return pstid;
    }

    public void setPstid(String pstid) {
        this.pstid = pstid;
    }

}
