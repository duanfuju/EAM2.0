package com.tiansu.eam.modules.opestandard.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.opestandard.entity.StandardLib;

import java.util.List;
import java.util.Map;

/**
 * @author zhangww
 * @description
 * @create 2017-08-31 14:21
 **/
@MyBatisDao
public interface StandardLibApprDao extends CrudDao<StandardLib> {

    public List<Map> findListByMap(Map map);//用于导出的方法
}
