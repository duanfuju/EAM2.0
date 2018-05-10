package com.tiansu.eam.common.web;

/**
 * @author wangjl
 * @description 请求结果返回类
 * @create 2017-09-12 18:46
 **/
public class BaseResult {
    //结果是否成功
    private boolean flag;
    //返回的数据
    private Object data;
    //返回信息
    private String msg;

    public BaseResult(boolean flag,Object data,String msg){
        this.flag = flag;
        this.data = data;
        this.msg = msg;
    }

    public boolean isFlag() {
        return flag;
    }

    public Object getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }
}
