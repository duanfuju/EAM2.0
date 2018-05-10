package com.tiansu.eam.interfaces.resulthelper;

/**
 * @author wujh
 * @description 对外接口返回结果体
 * @create 2017-09-01 11:00
 **/
public class APPResponseBody {
    /**
     * @Field 接口状态 0失败 1成功
     */
    private String status;

    /**
     * @Field rspCode 响应码
     */
    private String errorcode;

    /**
     * @Field rspMsg 响应消息
     */
    private String errmsg;

    /**
     * @Field val 返回报文信息
     */
    private Object data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public APPResponseBody() {
        this.status = "1";
        this.errmsg = "成功";
    }
    public APPResponseBody(Object data) {
        this.status = "1";
        this.errmsg = "成功";
        this.data = data;
    }
    public APPResponseBody(String status,String rspCode, String rspMsg, Object data) {
        this.status = status;
        this.errorcode = rspCode;
        this.errmsg = rspMsg;
        this.data = data;
    }

}
