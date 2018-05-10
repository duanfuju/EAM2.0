package com.tiansu.eam.interfaces.order.entity;

import com.tiansu.eam.common.persistence.DataEntity;

import java.util.Date;

/**
 * @author wangr
 * @description
 * @create 2017-10-11 下午 4:52
 */
public class Order extends DataEntity<Order> {
    private static final long serialVersionUID = 1L;

    /*主键*/
    private String id_key;

    /*用户id*/
    private String userId;

    /*工单编码 order_code*/
    private String code;

    /*保修电话 notifier_telnotifier_tel*/
    private String tel;

    /*报修人员*/
    private String notifier;

    /*报修部门*/
    private String notifier_dept;

    /*人员工号*/
    private String notifier_no;

    /*保修位置 notifier_loc*/
    private String location;

    /*保修设备 order_device*/
    private String devId;

    /*故障现象 notifier_appearance*/
    private String phenomenon;

    /*报修来源 notifier_source*/
    private String pcormob;

    /*处理方式 0-加急 1-一般 2-预约 order_level*/
    private int level;

    /*期望解决时间（预约时间） order_expect_time*/
    private Date expTime;

    /*具体地点*/
    private String detail_location;

    /*备注说明 notifier_remark*/
    private String describe;

    /*故障来源（1 故障 2巡检 3保养 4） order_source*/
    private int orderType;

    /*故障来源关联外键id*/
    private String order_source_id;

    /*工单状态（1待派单 2待接单*/
    private String order_status;

    /*工单计划开始时间*/
    private Date order_plan_start_time;

    /*工单计划结束时间*/
    private Date order_plan_end_time;

    /*工单接单时间*/
    private Date zorder_taking_time;

    /*工单反馈完成时间*/
    private Date order_finish_time;

    /*工单反馈完成时间*/
    private Date order_dispatch_time;

    /*工单到达图片*/
    private String arrive_img;

    /*工单计划处理人*/
    private String order_receiver;

    /*工单实际处理人*/
    private String order_solver;

    /*对于抢单模式，是否超过规定时间*/
    private String isovertime;

    /*派单人员*/
    private String order_dispatcher;

    /*反馈结果*/
    private String order_fk_result;

    /*反馈图片*/
    private String order_fk_photo;

    /*反馈视频*/
    private String order_fk_video;

    /*反馈原因*/
    private String order_fk_reason;

    /*流程实例id*/
    private String pstid;

    private String create_by;        // 创建人
    private Date create_time;        // 创建时间
    private String update_by;        // 更新人
    private Date update_time;        // 更新日期
    private char isdelete;        // 删除标志

    public String getId_key() {
        return id_key;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getNotifier() {
        return notifier;
    }

    public void setNotifier(String notifier) {
        this.notifier = notifier;
    }

    public String getNotifier_dept() {
        return notifier_dept;
    }

    public void setNotifier_dept(String notifier_dept) {
        this.notifier_dept = notifier_dept;
    }

    public String getNotifier_no() {
        return notifier_no;
    }

    public void setNotifier_no(String notifier_no) {
        this.notifier_no = notifier_no;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getPhenomenon() {
        return phenomenon;
    }

    public void setPhenomenon(String phenomenon) {
        this.phenomenon = phenomenon;
    }

    public String getPcormob() {
        return pcormob;
    }

    public void setPcormob(String pcormob) {
        this.pcormob = pcormob;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Date getExpTime() {
        return expTime;
    }

    public void setExpTime(Date expTime) {
        this.expTime = expTime;
    }

    public String getDetail_location() {
        return detail_location;
    }

    public void setDetail_location(String detail_location) {
        this.detail_location = detail_location;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getOrder_source_id() {
        return order_source_id;
    }

    public void setOrder_source_id(String order_source_id) {
        this.order_source_id = order_source_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public Date getOrder_plan_start_time() {
        return order_plan_start_time;
    }

    public void setOrder_plan_start_time(Date order_plan_start_time) {
        this.order_plan_start_time = order_plan_start_time;
    }

    public Date getOrder_plan_end_time() {
        return order_plan_end_time;
    }

    public void setOrder_plan_end_time(Date order_plan_end_time) {
        this.order_plan_end_time = order_plan_end_time;
    }

    public Date getZorder_taking_time() {
        return zorder_taking_time;
    }

    public void setZorder_taking_time(Date zorder_taking_time) {
        this.zorder_taking_time = zorder_taking_time;
    }

    public Date getOrder_finish_time() {
        return order_finish_time;
    }

    public void setOrder_finish_time(Date order_finish_time) {
        this.order_finish_time = order_finish_time;
    }

    public Date getOrder_dispatch_time() {
        return order_dispatch_time;
    }

    public void setOrder_dispatch_time(Date order_dispatch_time) {
        this.order_dispatch_time = order_dispatch_time;
    }

    public String getArrive_img() {
        return arrive_img;
    }

    public void setArrive_img(String arrive_img) {
        this.arrive_img = arrive_img;
    }

    public String getOrder_receiver() {
        return order_receiver;
    }

    public void setOrder_receiver(String order_receiver) {
        this.order_receiver = order_receiver;
    }

    public String getOrder_solver() {
        return order_solver;
    }

    public void setOrder_solver(String order_solver) {
        this.order_solver = order_solver;
    }

    public String getIsovertime() {
        return isovertime;
    }

    public void setIsovertime(String isovertime) {
        this.isovertime = isovertime;
    }

    public String getOrder_dispatcher() {
        return order_dispatcher;
    }

    public void setOrder_dispatcher(String order_dispatcher) {
        this.order_dispatcher = order_dispatcher;
    }

    public String getOrder_fk_result() {
        return order_fk_result;
    }

    public void setOrder_fk_result(String order_fk_result) {
        this.order_fk_result = order_fk_result;
    }

    public String getOrder_fk_photo() {
        return order_fk_photo;
    }

    public void setOrder_fk_photo(String order_fk_photo) {
        this.order_fk_photo = order_fk_photo;
    }

    public String getOrder_fk_video() {
        return order_fk_video;
    }

    public void setOrder_fk_video(String order_fk_video) {
        this.order_fk_video = order_fk_video;
    }

    public String getOrder_fk_reason() {
        return order_fk_reason;
    }

    public void setOrder_fk_reason(String order_fk_reason) {
        this.order_fk_reason = order_fk_reason;
    }

    public String getPstid() {
        return pstid;
    }

    public void setPstid(String pstid) {
        this.pstid = pstid;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_by() {
        return update_by;
    }

    public void setUpdate_by(String update_by) {
        this.update_by = update_by;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public char getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(char isdelete) {
        this.isdelete = isdelete;
    }
}
