package com.tiansu.eam.modules.supplier.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.supplier.entity.Supplier;
import com.tiansu.eam.modules.supplier.entity.SupplierType;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangww on 2017/8/7.
 */
@MyBatisDao
public interface SupplierTypeDao extends CrudDao<SupplierType> {
/*其他方法在父接口全都有*/

 public List<SupplierType> getSupplierTypeByMap(Map<String,Object> map);

 public int deleteByids(Map map);

 public Map<String,Object> getByCode(String code);

 public int insertBatch(List<SupplierType> list);//用于批量导入

 public List<Map> findListByMap(Map map);//用于导出的方法

 public Map<String,Object> deleBefore(String id);//删除前的引用判断

 public List<Map> getsuppliertype();
}
