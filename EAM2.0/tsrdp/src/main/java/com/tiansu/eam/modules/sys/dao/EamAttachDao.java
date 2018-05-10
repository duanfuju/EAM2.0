package com.tiansu.eam.modules.sys.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.sys.entity.Attachment;

/**
 * @author wangjl
 * @description
 * @create 2017-09-01 15:29
 **/
@MyBatisDao
public interface EamAttachDao extends CrudDao<Attachment> {
}
