package com.tiansu.eam.common.dao;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import java.util.Map;

/**
 * Created by wangjl on 2017/8/7.
 */
@MyBatisDao
public interface ValidateDBDao {

    public int checkExists(String keyData,String tableName,String keyField,String whereCond);

    public Map<String,Object> executeSql(Map<String,String> executeSql);
}
