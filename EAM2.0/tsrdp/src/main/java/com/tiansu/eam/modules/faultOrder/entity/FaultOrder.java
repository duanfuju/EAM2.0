package com.tiansu.eam.modules.faultOrder.entity;

import com.tiansu.eam.common.persistence.DataEntity;
import com.tiansu.eam.modules.device.entity.DevLocation;
import com.tiansu.eam.modules.device.entity.Device;
import com.tiansu.eam.modules.sys.entity.Dept;
import com.tiansu.eam.modules.sys.entity.User;

import java.util.List;

/**
 * @author wangjl
 * Created by wangjl on 2017/8/9.
 * 故障工单主表实体类
 */
public class FaultOrder extends DataEntity<FaultOrder> {
    private static final long serialVersionUID = 1L;
    //工单编码
    private String order_code;
    //报修电话
    private String notifier_tel;
    //报修人
    private String notifier;
    //报修部门
    private Dept notifier_dept;
    //人员工号
    private String notifier_no;
    //报修位置
    private DevLocation notifier_loc;
    //报修设备
    private Device order_device;
    //故障现象
    private String notifier_appearance;
    //报修来源
    private String notifier_source;
    //报修上传图片
    private String notifier_photo;
    //报修上传视频
    private String notifier_video;
    //处理方式 0-加急 1一般- 2-预约
    private String order_level;
    //期望解决时间
    private String order_expect_time;
    //具体地点
    private String detail_location;
    //备注说明
    private String notifier_remark;
    //故障来源（1 故障 2巡检 3保养 4异常）
    private String order_source;
    //故障来源关联外键id
    private String order_source_id;
    //工单状态（1待派单 2待接单 3转单中 4待到达 5挂起中 6已挂起 7处理中 8已完成 9已验收 10 已评价 11已归档）
    private String order_status;
    //工单计划开始时间
    private String order_plan_start_time;
    //工单计划结束时间
    private String order_plan_end_time;
    //工单接单时间
    private String order_taking_time;
    //工单反馈完成时间
    private String order_finish_time;
    //工单派单时间
    private String order_dispatch_time;
    //工单到达时间
    private String order_arrivetime;
    //工单到达上传图片
    private String arrive_img;
    //工单计划处理人
    private String order_receiver;
    //工单实际处理人
    private User order_solver;
    //抢单模式，工单是否超时
    private String isovertime;
    //工单派单人
    private User order_dispatcher;
    //工单处理反馈结果
    private String order_fk_result;
    //工单处理反馈图片
    private String order_fk_photo;
    //工单处理反馈视频
    private String order_fk_video;
    //工单处理反馈原因
    private String order_fk_reason;
    //工单流程实例id
    private String pstid;
    //工单评价得分
    private String evaluate_score;
    //工单评价说明
    private String evaluate_remark;
    //工单-工器具
    private List<OrderTool> orderTool;
    //工单-备品备件
    private List<OrderSparepart> orderAttachment;
    //工单-人员工时
    private List<OrderPerson> orderManhaur;
    //工单-其他费用
    private List<OrderOther> orderOther;


    public List<OrderTool> getOrderTool() {
        return orderTool;
    }

    public void setOrderTool(List<OrderTool> orderTool) {
        this.orderTool = orderTool;
    }

    public List<OrderSparepart> getOrderAttachment() {
        return orderAttachment;
    }

    public void setOrderAttachment(List<OrderSparepart> orderAttachment) {
        this.orderAttachment = orderAttachment;
    }

    public List<OrderPerson> getOrderManhaur() {
        return orderManhaur;
    }

    public void setOrderManhaur(List<OrderPerson> orderManhaur) {
        this.orderManhaur = orderManhaur;
    }

    public List<OrderOther> getOrderOther() {
        return orderOther;
    }

    public void setOrderOther(List<OrderOther> orderOther) {
        this.orderOther = orderOther;
    }





    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getNotifier_tel() {
        return notifier_tel;
    }

    public void setNotifier_tel(String notifier_tel) {
        this.notifier_tel = notifier_tel;
    }

    public String getNotifier() {
        return notifier;
    }

    public void setNotifier(String notifier) {
        this.notifier = notifier;
    }

    public Dept getNotifier_dept() {
        return notifier_dept;
    }

    public void setNotifier_dept(Dept notifier_dept) {
        this.notifier_dept = notifier_dept;
    }

    public String getNotifier_no() {
        return notifier_no;
    }

    public void setNotifier_no(String notifier_no) {
        this.notifier_no = notifier_no;
    }

    public DevLocation getNotifier_loc() {
        return notifier_loc;
    }

    public void setNotifier_loc(DevLocation notifier_loc) {
        this.notifier_loc = notifier_loc;
    }

    public Device getOrder_device() {
        return order_device;
    }

    public void setOrder_device(Device order_device) {
        this.order_device = order_device;
    }

    public String getNotifier_appearance() {
        return notifier_appearance;
    }

    public void setNotifier_appearance(String notifier_appearance) {
        this.notifier_appearance = notifier_appearance;
    }

    public String getNotifier_source() {
        return notifier_source;
    }

    public void setNotifier_source(String notifier_source) {
        this.notifier_source = notifier_source;
    }

    public String getOrder_level() {
        return order_level;
    }

    public void setOrder_level(String order_level) {
        this.order_level = order_level;
    }

    public String getNotifier_photo() {
        return notifier_photo;
    }

    public void setNotifier_photo(String notifier_photo) {
        this.notifier_photo = notifier_photo;
    }

    public String getNotifier_video() {
        return notifier_video;
    }

    public void setNotifier_video(String notifier_video) {
        this.notifier_video = notifier_video;
    }

    public String getOrder_expect_time() {
        return order_expect_time;
    }

    public void setOrder_expect_time(String order_expect_time) {
        this.order_expect_time = order_expect_time;
    }

    public String getOrder_arrivetime() {
        return order_arrivetime;
    }

    public void setOrder_arrivetime(String order_arrivetime) {
        this.order_arrivetime = order_arrivetime;
    }

    public String getDetail_location() {
        return detail_location;
    }

    public void setDetail_location(String detail_location) {
        this.detail_location = detail_location;
    }

    public String getNotifier_remark() {
        return notifier_remark;
    }

    public void setNotifier_remark(String notifier_remark) {
        this.notifier_remark = notifier_remark;
    }

    public String getOrder_source() {
        return order_source;
    }

    public void setOrder_source(String order_source) {
        this.order_source = order_source;
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

    public String getOrder_plan_start_time() {
        return order_plan_start_time;
    }

    public void setOrder_plan_start_time(String order_plan_start_time) {
        this.order_plan_start_time = order_plan_start_time;
    }

    public String getOrder_plan_end_time() {
        return order_plan_end_time;
    }

    public void setOrder_plan_end_time(String order_plan_end_time) {
        this.order_plan_end_time = order_plan_end_time;
    }

    public String getOrder_taking_time() {
        return order_taking_time;
    }

    public void setOrder_taking_time(String order_taking_time) {
        this.order_taking_time = order_taking_time;
    }

    public String getOrder_finish_time() {
        return order_finish_time;
    }

    public void setOrder_finish_time(String order_finish_time) {
        this.order_finish_time = order_finish_time;
    }

    public String getOrder_dispatch_time() {
        return order_dispatch_time;
    }

    public void setOrder_dispatch_time(String order_dispatch_time) {
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

    public User getOrder_solver() {
        return order_solver;
    }

    public void setOrder_solver(User order_solver) {
        this.order_solver = order_solver;
    }

    public String getIsovertime() {
        return isovertime;
    }

    public void setIsovertime(String isovertime) {
        this.isovertime = isovertime;
    }

    public User getOrder_dispatcher() {
        return order_dispatcher;
    }

    public void setOrder_dispatcher(User order_dispatcher) {
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

    public String getEvaluate_score() {
        return evaluate_score;
    }

    public void setEvaluate_score(String evaluate_score) {
        this.evaluate_score = evaluate_score;
    }

    public String getEvaluate_remark() {
        return evaluate_remark;
    }

    public void setEvaluate_remark(String evaluate_remark) {
        this.evaluate_remark = evaluate_remark;
    }
}

