package com.tiansu.eam.modules.sys.service;

import com.tiansu.eam.modules.sys.dao.SysConfigDao;
import com.tiansu.eam.modules.sys.entity.SysConfigEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wangjl
 * @description
 * @create 2017-08-31 16:58
 **/
@Service
@Transactional(readOnly = true)
public class SysConfigService {
    @Autowired
    private SysConfigDao sysConfigDao;

    public SysConfigEntry getByKeyName(String keyName){
        SysConfigEntry sysConfigEntry = sysConfigDao.getByKeyName(keyName);
        return sysConfigEntry;
    }

    public int updateByKeyName(SysConfigEntry config){
        int suc = sysConfigDao.updateByKeyName(config);
        return suc;
    }

}
