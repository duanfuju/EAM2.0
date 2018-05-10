package com.tiansu.eam.modules.sys.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.sys.entity.DataScope;

import java.util.List;

/**
 * Created by wangjl on 2017/7/24.
 */
@MyBatisDao
public interface EamDataScopeDao  extends CrudDao<DataScope> {

    /**
     * 根据角色查询所有的数据范围
     */
    public List<DataScope> findAllByRoleIds(String roleids);

    /**
     * 插入角色对应的数据范围信息
     * @param dataScope
     * @return
     */
    public int insertDataScopeInfo(DataScope dataScope);

    /**
     * 根据角色id删除角色数据范围
     * @param roleid
     * @return
     */
    public int deleteDataScope(String roleid);

    /**
     * 根据角色id更新
     * @param dataScope
     * @return
     */
    public int updateDataScope(DataScope dataScope);
}

