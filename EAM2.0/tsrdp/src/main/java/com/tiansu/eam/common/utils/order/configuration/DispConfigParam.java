package com.tiansu.eam.common.utils.order.configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangjl
 * @description 工单派单handler 配置参数
 * @create 2017-09-06 16:35
 **/
public class DispConfigParam {
    private Map<String,Object> paramMap ;

    public DispConfigParam(){
        this.paramMap = new HashMap<>();
    }

    public Object getParamValue(String paramName){
        return paramMap.get(paramName);
    }

    public Object addParamValue(String paramName,Object paramValue){
        return paramMap.put(paramName,paramValue);
    }

}
