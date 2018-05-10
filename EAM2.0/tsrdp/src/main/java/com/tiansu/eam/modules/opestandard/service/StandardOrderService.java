package com.tiansu.eam.modules.opestandard.service;

import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.opestandard.dao.StandardOrderDao;
import com.tiansu.eam.modules.opestandard.entity.StandardOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author zhangww
 * @description
 * @create 2017-08-31 14:22
 **/
@Service
public class StandardOrderService extends CrudService<StandardOrderDao,StandardOrder> {
    @Autowired
    private StandardOrderDao standardOrderDao;

    /**
     * @creator duanfuju
     * @createtime 2017/10/30 11:45
     * @description:
     *  根据条件获取数据（导出时）
     * @param map
     * @return
     */
    public List<Map> findListByMap(Map map){
        return standardOrderDao.findListByMap(map);
    }
    public Map dataTablePageMap(Map map) {
        return super.dataTablePageMap(map);
    }

    public void insert(StandardOrder standardOrder){
        standardOrderDao.insert(standardOrder);
    }

    public StandardOrder getEdit(String id){
        return standardOrderDao.get(id);
    }

    public void update(StandardOrder standardOrder){
        standardOrderDao.update(standardOrder);
    }
    public void deleteByids(String ids){
        StandardOrder standardOrder=standardOrderDao.get(ids);
        standardOrder.preUpdate();
        standardOrderDao.deleteByids(standardOrder);
    }

    public int getBycode(String code){
        Map<String,Object> map= standardOrderDao.getByCode(code);
        return (int)map.get("cou");
    }
    public List<Map> getOrderwork(){
        return standardOrderDao.getOrderwork();
    }
    public List<Map> getMajorByWork(Map map){
        return standardOrderDao.getMajorByWork(map);
    }
    public List<Map> getDevinfo(Map map){
        return standardOrderDao.getDevinfo(map);
    }

    public Map getLibByPIid(String pstid){
        return standardOrderDao.getLibByPIid(pstid);
    }

    public void updateAprByPIid(Map map){
        standardOrderDao.updateAprByPIid(map);
    }

    public List<Map> getAllCodes(Map paramMap) {
        return standardOrderDao.getAllCodes(paramMap);
    }
}
