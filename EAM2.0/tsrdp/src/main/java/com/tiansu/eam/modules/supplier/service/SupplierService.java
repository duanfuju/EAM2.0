package com.tiansu.eam.modules.supplier.service;
import com.thinkgem.jeesite.common.persistence.Page;
import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.supplier.dao.SupplierDao;
import com.tiansu.eam.modules.supplier.entity.Supplier;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangww on 2017/8/15.
 */
@Service
public class SupplierService extends CrudService<SupplierDao,Supplier> {
    @Autowired
    private SupplierDao supplierDao;

    public Map dataTablePageMap(Map map) {
        return super.dataTablePageMap(map);

    }

    public void insert(Supplier supplier){
        supplier.preInsert();
        supplierDao.insert(supplier);
    }

    public Supplier getEdit(String id){
        return supplierDao.get(id);

    }
    public void update(Supplier supplier){
        supplier.preUpdate();
        supplierDao.update(supplier);
    }
    public void deleteByids(String ids){
        Map map=new HashMap();
        String id[] = ids.split(",");
        map.put("ids",id);
        map.put("updateBy", UserUtils.getUser().getLoginname());
        map.put("updateDate",new Date());
        supplierDao.deleteByids(map);
    }

    public int getBycode(String code){
       Map<String,Object> map= supplierDao.getByCode(code);
       return (int)map.get("cou");
    }
    public List<Map> findListByMape(Map map){
        return supplierDao.findListByMape(map);
    }

    public Map deleBefore(String id){
        return supplierDao.deleBefore(id);
    }
    //设备清单
    public Map getDeviceList(Map param){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Page<Supplier> page = new Page();
        Map map= new HashMap();
        //设置分页数据
        String sta = request.getParameter("start")==null?"0":request.getParameter("start");
        String len = request.getParameter("length")==null?"10":request.getParameter("length");
        String dra = request.getParameter("draw")==null?"1":request.getParameter("draw");
        int start = Integer.parseInt(sta);
        int length = Integer.parseInt(len);
        int draw = Integer.parseInt(dra);

        page.setPageNo(start/length+1);
        page.setPageSize(length);

        //获取排序数据
        page = this.setOrderBy(page,request);
        if(false){
            //获取所拥有数据权限的部门Id
            String deptIds = UserUtils.getDataScopeDeptIds();
            param.put("dept",deptIds);
        }
        param.put("page",page);
        List<Map> list = dao.getDeviceList(param);
        map.put("recordsFiltered",page.getCount());
        map.put("recordsTotal",page.getCount());
        map.put("data",list);
        map.put("draw",draw);
        return map;

    }
}
