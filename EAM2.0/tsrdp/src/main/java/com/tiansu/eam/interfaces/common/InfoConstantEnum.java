package com.tiansu.eam.interfaces.common;

/**
 * @author wangr
 * @create 2017-09-27 15:10
 * @description App  消息常量
 */
public enum InfoConstantEnum {
    PARAM("1001","数据为空！"),
    QUERY_FAIL("1011","查询失败！"),
    ORDER_INSERT_FAIL("1021","我的保修新增失败！"),
    ORDER_FEEDBACK_FAIL("1028","我的保修新增失败！"),

    ACTIVItY_FAIL("2001","流程执行失败！"),
    ACTIVItY_PERMISSIONS_FAIL("2002","权限检查-任务的办理人和当前人不一致不能完成任务！"),

    DATABASE_FAIL("4001","数据库错误！"),
    Process_FAIL("5001","进入流程失败"),
    Process_TIMEOUT("5002","流程启动超时"),

    SUCCESS("1", "成功！"),
    FAIL("0", "失败！");

    private String code;
    private String message;

    InfoConstantEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
