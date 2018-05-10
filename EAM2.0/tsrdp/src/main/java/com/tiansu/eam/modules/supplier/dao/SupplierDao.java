package com.tiansu.eam.modules.supplier.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.supplier.entity.Supplier;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangdf on 2017/8/7.
 */
@MyBatisDao
public interface SupplierDao extends CrudDao<Supplier> {
/*其他方法在父接口全都有*/

 public List<Supplier> getSupplierByMap(Map<String, Object> map);

 public int deleteByids(Map<String, Object> map);

 public Map<String,Object> getByCode(String code);

 public int insertBatch(List<Supplier> list);//用于批量导入

 public List<Map> findListByMape(Map map);//用于导出的方法

 public  Map<String,Object> deleBefore(String id);//删除前的引用判断

 public List<Map> getDeviceList(Map map);
}
