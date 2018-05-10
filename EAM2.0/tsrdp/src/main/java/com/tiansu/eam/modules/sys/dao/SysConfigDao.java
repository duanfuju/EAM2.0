package com.tiansu.eam.modules.sys.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.sys.entity.SysConfigEntry;

import java.util.Map;

/**
 * @author wangjl
 * @description
 * @create 2017-08-31 17:07
 **/
@MyBatisDao
public interface SysConfigDao extends CrudDao<SysConfigEntry> {

    public SysConfigEntry getByKeyName(String keyName);

    public int updateByKeyName(SysConfigEntry config);

    public String getBusinessCode(Map map);
}
