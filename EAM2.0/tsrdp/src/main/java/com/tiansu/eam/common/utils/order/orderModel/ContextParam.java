package com.tiansu.eam.common.utils.order.orderModel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangjl
 * @description 工单派单handler 上下文参数
 * @create 2017-09-06 16:35
 **/
public class ContextParam {
    private Map<String,Object> paramMap ;

    protected ContextParam(){
        this.paramMap = new HashMap<>();
    }

    public Object getParamValue(String paramName){
        return paramMap.get(paramName);
    }

    public Object addParamValue(String paramName,Object paramValue){
        return paramMap.put(paramName,paramValue);
    }
}
