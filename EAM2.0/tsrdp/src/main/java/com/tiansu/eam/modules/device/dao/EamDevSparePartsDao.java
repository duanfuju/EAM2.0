package com.tiansu.eam.modules.device.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.device.entity.DevSpareParts;

/**
 * @author wujh
 * @description 设备备品备件和工器具接口
 * @create 2017-09-05 16:59
 **/
@MyBatisDao
public interface EamDevSparePartsDao extends CrudDao<DevSpareParts>{

}
