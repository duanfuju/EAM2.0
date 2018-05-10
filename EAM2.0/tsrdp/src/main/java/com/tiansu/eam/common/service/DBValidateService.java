package com.tiansu.eam.common.service;

import com.tiansu.eam.common.dao.ValidateDBDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by wangjl on 2017/8/5.
 * 持久化校验服务类
 */
@Service
@Transactional(readOnly = true)
public class DBValidateService{

    @Autowired
    private ValidateDBDao validateDBDao;

    /**
     * 校验数据是否存在
     * @param keyData  校验数据项
     * @param tableName 校验数据项所在表
     * @param keyField 校验数据项所在表字段
     * @param whereCond 扩展的where条件（可用于过滤掉已逻辑删除掉的数据）
     * @return
     */
    public boolean checkExists(String keyData,String tableName,String keyField,String whereCond){
        whereCond = StringUtils.isEmpty(whereCond)? "" : "AND "+whereCond;
        int result = validateDBDao.checkExists(keyData,tableName,keyField,whereCond);
        return result>0 ? true : false;
    }




}


