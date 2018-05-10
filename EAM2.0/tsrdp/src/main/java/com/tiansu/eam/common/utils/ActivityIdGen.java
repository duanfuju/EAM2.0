package com.tiansu.eam.common.utils;

import org.activiti.engine.impl.cfg.IdGenerator;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.springframework.context.annotation.Lazy;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author wangjl
 * @description 适配工作流主键生成机制，按时间生成，否则流程实例没有按时间查询的接口，列表无法排序
 * @create 2017-10-16 18:36
 **/
@Lazy(false)
public class ActivityIdGen implements IdGenerator, SessionIdGenerator {

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
    @Override
    public String getNextId() {
        return ActivityIdGen.uuidD();
    }

    @Override
    public Serializable generateId(Session session) {
        return ActivityIdGen.uuidD();
    }

    public static String uuidM(){
        //毫秒数加上5位随机数保证并发重复率
        return Calendar.getInstance().getTimeInMillis()+IdGen.uuid(5);
    }

    public static String uuidD(){
        //毫秒数加上5位随机数保证并发重复率
        return sdf.format(Calendar.getInstance().getTime())+IdGen.uuid(5);
    }

}
