package com.tiansu.eam.common;

/**
 * Created by wangjl on 2017/7/31.
 * 系统常量类
 */
public class EAMConsts {

    //ckfinder控件自定义上传附件key
//    public static final String CK_UPLOAD_FILES = "uploadFileNameKey";


    /** 加密相关  **/
    //加密算法
    public static final String HASH_ALGORITHM = "md5";
    //salt计算次数
    public static final int HASH_INTERATIONS = 2;


    /** 系统配置表key **/
    //二维码配置顶层图片地址,
    public static final String QRCODE_TOP_LOGO = "QRCODE_TOP_LOGO";
    //二维码标题（项目名称）,
    public static final String QRCODE_TITLE = "QRCODE_TITLE";
    //二维码中间图片地址,
    public static final String QRCODE_BOT_LOGO = "QRCODE_BOT_LOGO";

    //维修工单派单方式
    public static final String REPAIR_ORDER_DISP_TYPE = "REPAIR_ORDER_DISP_TYPE";
    //维修工单运维人员最多处理工单数
    public static final String REPARI_ORDEDR_QRAB_ORDERS = "REPARI_ORDEDR_QRAB_ORDERS";
    //维修工单抢单时限
    public static final String REPARI_ORDEDR_QRAB_TIMEOUT = "REPARI_ORDEDR_QRAB_TIMEOUT";

    //保养工单派单方式
    public static final String MAINT_ORDER_DISP_TYPE = "MAINT_ORDER_DISP_TYPE";
    //巡检工单派单方式
    public static final String INSPECT_ORDER_DISP_TYPE = "INSPECT_ORDER_DISP_TYPE";

    //统计不同设备类别的指标时，统计级别设置：
    public static final String STATISTICS_LEVEL = "STATISTICS_LEVEL";

    //二维码logo压缩尺寸
    public static final int QRCODE_LOGO_WIDTH = 100;
    public static final int QRCODE_LOGO_HEIGHT = 100;

    //图片上传来源
    public static final String UPLOAD_SOURCE = "SYS";

    /** 单据 派单方式 **/
    //人工派单
    public static final String MANUAL_DISP_TYPE = "1";
    //自动派单
    public static final String AUTO_DISP_TYPE = "2";
    //抢单
    public static final String GRAB_DISP_TYPE = "3";

    /** 单据 审批状态 **/
    //待提交
    public static final String ORDER_DRAFT = "0";
    //待审批
    public static final String ORDER_PENDING_APPROVE = "1";
    //审批通过
    public static final String ORDER_APPROVED = "2";
    //审批退回
    public static final String ORDER_REJECTED = "3";

    /** 工单需要审批的操作类型，对应各switch表中的type **/
    //转单
    public static final String ORDER_SWITCH_TRANSFER = "1";
    //挂起
    public static final String ORDER_SWITCH_HANGUP = "2";
    //解挂
    public static final String ORDER_SWITCH_RELIEVE = "3";
    //验收
    public static final String ORDER_SWITCH_CHECK = "4";
    //归档
    public static final String ORDER_SWITCH_ARCHIVE = "5";

    /** 巡检任务状态 */
    // 进行中
    public static final String BE_ON = "1";
    public static final String ACCEPT_APPLY = "1";
    // 待确认
    public static final String TO_BE_CONFIRMED = "0";
    // 申请撤销
    public static final String CANCEL_APPLY = "3";
    public static final String CANCLE_STATUS = "6";
    // 已撤销
    public static final String CANCLED_STATUS = "7";
    // 申请转单
    public static final String TRANSFER_APPLY = "2";
    public static final String TRANSFER_STATUS = "4";
    // 申请挂起
    public static final String HANGUP_APPLY = "4";
    public static final String HANGUP_STATUS = "3";
    // 挂起
    public static final String HANGUPED_STATUS = "5";
    // 解挂
    public static final String UNHANG_APPLY = "6";
    // 撤销
    public static final String INSPECTION_SWITCH_CANCEL = "2";
    // 转单
    public static final String INSPECTION_SWITCH_TRANSFER = "0";
    // 挂起
    public static final String INSPECTION_SWITCH_HANGUP = "1";
    // 反馈
    public static final String FEEDBACK_APPLY = "5";  //反馈请求

    // 巡检反馈结果 0-正常 1-报修 2-异常
    public static final String CHECK_RESULT_NORMAL = "0";
    public static final String CHECK_RESULT_REPAIR = "1";
    public static final String CHECK_RESULT_ABNORMAL = "2";

    /** 故障来源 **/
    //1-故障
    public static final String ORDER_TYPE_FAULT = "1";
    //2-巡检
    public static final String ORDER_TYPE_INSPECTION = "2";
    //3-保养
    public static final String ORDER_TYPE_MAINTAIN = "3";
    //4-异常
    public static final String ORDER_TYPE_EXCEPTON = "4";

    /** 报修来源 **/
    public static final String ORDER_SOURCE_PHONE = "电话";
    public static final String ORDER_SOURCE_INSPECTION = "巡检";
    public static final String ORDER_SOURCE_APP = "APP";

    /*********************流程定义*************************/
    //报修流程
    public static final String FAULT_ORDER_FLOWDEF = "FaultRepairOrder";

    //巡检流程 add by wangr 2017-10-30 10:17
    public static final String INSPECTION_FLOWDEF = "inspectionFlow";

    public static final String MAINTAIN_FLOW = "maintainFlow";
    //end

}

