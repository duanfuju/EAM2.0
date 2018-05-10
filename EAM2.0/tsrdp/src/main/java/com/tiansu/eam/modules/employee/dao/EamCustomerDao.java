package com.tiansu.eam.modules.employee.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.employee.entity.EamCustomer;

import java.util.List;
import java.util.Map;

/**
 *@creator duanfuju
 * @createtime 2017/8/28 9:02
 * @description: 
 * 客户信息DAO接口
 */
@MyBatisDao
public interface EamCustomerDao extends CrudDao<EamCustomer> {

    /**
     * @creator duanfuju
     * @createtime 2017/8/28 9:02
     * @description: 
     * 列表查询
     * @param map
     * @return
     */
     List<Map> findListByMap(Map map);

    /**
     * @creator duanfuju
     * @createtime 2017/8/28 9:02
     * @description: 
     * 根据id获取
     * @param id
     * @return
     */
    Map findById(String id);

    /**
     * @creator duanfuju
     * @createtime 2017/8/28 9:02
     * @description: 
     * 根据id删除（逻辑）
     * @param map
     * @return
     */
    int delete(Map map);

    /**
     * @creator duanfuju
     * @createtime 2017/8/28 9:08
     * @description: 
     *批量插入
     * @return
     */
    int insertBatch(List<EamCustomer> eamCustomer);
}
