package com.tiansu.eam.modules.employee.service;

import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.employee.dao.EamCustomerDao;
import com.tiansu.eam.modules.employee.entity.EamCustomer;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *@creator duanfuju
 * @createtime 2017/8/28 9:02
 * @description:  客户信息 Service
 */
@Service
public class EamCustomerService extends CrudService<EamCustomerDao,EamCustomer> {

    @Autowired
    private EamCustomerDao eamCustomerDao;

    /**
     * @creator duanfuju
     * @createtime 2017/8/28 9:03
     * @description: 
     * 列表查询
     * @param map
     * @return
     */
    public List<Map> findList(Map map) {
        return eamCustomerDao.findListByMap(map);
    }

    /**
     * @creator duanfuju
     * @createtime 2017/8/28 9:03
     * @description: 
     * 根据id获取
     * @param id
     * @return
     */
    public Map findById(String id) {
        return eamCustomerDao.findById(id);
    }

    /**
     * @creator duanfuju
     * @createtime 2017/8/28 9:03
     * @description: 
     * 根据id删除（逻辑）
     * @param id
     * @return
     */
    public int delete(String id){
        Map map = new HashMap();
        String ids[] = id.split(",");
        map.put("ids",ids);
        map.put("updateBy", UserUtils.getUser().getLoginname());
        map.put("updateDate",new Date());
       return  eamCustomerDao.delete(map);
    }

    /**
     * @creator duanfuju
     * @createtime 2017/8/28 9:07
     * @description: 
     *批量插入
     * @return
     */
    public int insertBatch(List<EamCustomer> eamCustomer){
        return  eamCustomerDao.insertBatch(eamCustomer);
    }
}
