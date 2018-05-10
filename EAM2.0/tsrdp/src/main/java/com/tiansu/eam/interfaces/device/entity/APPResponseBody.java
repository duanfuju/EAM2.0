package com.tiansu.eam.interfaces.device.entity;

/**
 * @author wujh
 * @description 对外接口返回结果体
 * @create 2017-09-01 11:00
 **/
public class APPResponseBody<T> {

    /**
     * @Field  rspCode 响应码
     */
    private String rspCode;

    /**
     * @Field  rspMsg 响应消息
     */
    private String rspMsg;

    /**
     * @Field val 返回报文信息
     */
    private T data;

    public String getRspCode() {
        return rspCode;
    }

    public void setRspCode(String rspCode) {
        this.rspCode = rspCode;
    }

    public String getRspMsg() {
        return rspMsg;
    }

    public void setRspMsg(String rspMsg) {
        this.rspMsg = rspMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public APPResponseBody(String rspCode, String rspMsg, T data) {
        this.rspCode = rspCode;
        this.rspMsg = rspMsg;
        this.data = data;
    }

    public APPResponseBody() {
    }
}
