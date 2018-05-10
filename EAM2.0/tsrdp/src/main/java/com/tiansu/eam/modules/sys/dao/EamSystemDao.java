package com.tiansu.eam.modules.sys.dao;/**
 * Created by suven on 2017/10/11.
 */

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.sys.entity.SystemInfo;

import java.util.List;


/**
 * 系统信息
 *
 * @author suven suven
 * @create 2017/10/11
 */
@MyBatisDao
public interface EamSystemDao extends CrudDao<SystemInfo> {
    /**
    *@Create
    *@Description :获得子系统注册相关信息
    *@Param :  * @param null
    *@author : suven
    *@Date : 16:29 2017/10/11
    */
    public List<SystemInfo> findRelatedList(SystemInfo sys);
}
