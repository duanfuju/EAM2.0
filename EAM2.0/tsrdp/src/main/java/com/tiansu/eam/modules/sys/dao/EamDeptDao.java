/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.tiansu.eam.modules.sys.dao;

import com.thinkgem.jeesite.common.persistence.TreeDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.sys.entity.Dept;
import com.tiansu.eam.modules.sys.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 机构DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface EamDeptDao extends TreeDao<Dept> {
    /**
     * 查询所有部门
     * @param dept
     * @return
     */
    public List<Dept> queryDeptList(Dept dept);

    /**
     * 查询某个部门详情信息
     * @param dept
     * @return
     */
    public Dept getDept(Dept dept);


    /**
     * 查询部门下所有人员
     * @param dept
     * @return
     */
    public List<User> getUserByDept(Dept dept);

    /**
     * 获取当前登录用户所拥有的数据权限的部门信息
     * @param paramMap
     * @return
     */
    List<Dept> queryDeptListByIds(Map<String,String> paramMap);
}
