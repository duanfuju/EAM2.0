package com.tiansu.eam.modules.supplier.service;

import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.supplier.dao.SupplierTypeDao;
import com.tiansu.eam.modules.supplier.entity.SupplierType;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangww on 2017/8/15.
 */
@Service
public class SupplierTypeService extends CrudService<SupplierTypeDao,SupplierType> {
    @Autowired
    private SupplierTypeDao supplierTypeDao;

    public Map dataTablePageMap(Map map) {
        return super.dataTablePageMap(map);

    }

    public void insert(SupplierType supplierType){
        supplierType.preInsert();
        supplierTypeDao.insert(supplierType);
    }

    public SupplierType getEdit(String id){
        return supplierTypeDao.get(id);

    }
    public void update(SupplierType supplierType){
        supplierType.preUpdate();
        supplierTypeDao.update(supplierType);
    }
    public void deleteByids(String ids){
        Map map=new HashMap();
        String [] id=ids.split(",");
        map.put("ids",id);
        map.put("updateBy", UserUtils.getUser().getLoginname());
        map.put("updateDate",new Date());
        supplierTypeDao.deleteByids(map);
    }

    public int getBycode(String code){
        Map<String,Object> map= supplierTypeDao.getByCode(code);
        return (int)map.get("cou");
    }

    public List<Map> findListByMap(Map map){
        return supplierTypeDao.findListByMap(map);
    }

    public Map deleBefore(String id){
        return supplierTypeDao.deleBefore(id);
    }

    public List<Map> getsuppliertype(){
        return supplierTypeDao.getsuppliertype();
    }
}
