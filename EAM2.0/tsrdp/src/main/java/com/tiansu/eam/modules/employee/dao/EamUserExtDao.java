package com.tiansu.eam.modules.employee.dao;/**
 * @description
 * @author duanfuju
 * @create 2017-09-14 16:24
 **/

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.employee.entity.EamUserExt;

import java.util.List;
import java.util.Map;

/**
 * @author duanfuju
 * @create 2017-09-14 16:24
 * @desc 人员信息扩展DAO接口
 **/
@MyBatisDao
public interface EamUserExtDao  extends CrudDao<EamUserExt> {
    /**
     * @creator duanfuju
     * @createtime 2017/9/15 16:34
     * @description: 
     * 列表查询
     * @param map
     * @return
     */
    List<Map> findListByMap(Map map);

    List<Map> findDept(Map map);
    /**
     * @creator duanfuju
     * @createtime 2017/9/15 16:35
     * @description: 
     * 根据id获取
     * @param id
     * @return
     */
    Map findById(String id);

    /**
     * @creator duanfuju
     * @createtime 2017/9/15 16:34
     * @description:
     * 根据登录名称获取人员信息
     * @param loginname
     * @return
     */
    List<Map> findByLoginName(String loginname);

    /**
     * @creator duanfuju
     * @createtime 2017/9/15 16:35
     * @description: 
     * 根据id删除（逻辑）
     * @param map
     * @return
     */
    int delete(Map map);

    /**
     * @creator duanfuju
     * @createtime 2017/9/15 16:35
     * @description: 
     *批量插入
     * @return
     */
    int insertBatch(List<EamUserExt> eamUserExt);

    /**
     * @creator duanfuju
     * @createtime 2017/9/22 14:24
     * @description:
     * 获取所有的人员
     * @return
     */
      List<Map> getAllUser();

    /**
     * @creator duanfuju
     * @createtime 2017/9/28 17:05
     * @description:
     *  查询可以导入扩展表的人员信息
     * @return
     */
    List<Map> selectNeedLoadData();

    List<Map> findUser(Map map);
}
