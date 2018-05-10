package com.tiansu.eam.modules.device.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.modules.device.dao.EamDevCategoryDao;
import com.tiansu.eam.modules.device.entity.DevCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备类别Service
 * Created by tiansu on 2017/8/7.
 */
@Service
@Transactional(readOnly = true)
public class EamDevCategoryService extends CrudService<EamDevCategoryDao, DevCategory> {

    @Autowired
    private EamDevCategoryDao eamDevCategoryDao;

    /**
     * 获取设备类别列表
     * @param devCategory
     * @return
     */
    public List<DevCategory> findList(DevCategory devCategory){
        List<DevCategory> devCategoryList = eamDevCategoryDao.findList(devCategory);
        return devCategoryList;
    }

    /**
     * 根据主键id获取要编辑的对象信息
     * @return
     */
    public Map getEdit(String id){
        return eamDevCategoryDao.getEdit(id);
    }

    public Map getDetail(String id){
        return eamDevCategoryDao.getDetail(id);
    }

    /**
     * 获取设备类别下拉树数据
     */
    public List<Map> getDevCategoryTree(){
        return eamDevCategoryDao.getDevCategoryTree();
    }

    /**
     * 修改设备类别信息
     * @param devCategory
     */
    @Transactional(readOnly = false)
    public void update(DevCategory devCategory){
        devCategory.preUpdate();
        eamDevCategoryDao.update(devCategory);
        eamDevCategoryDao.updateStatus(devCategory);
    }


    /**
     * 新增设备类别信息
     * @param devCategory
     */
    public void insert(DevCategory devCategory){
        devCategory.preInsert();
        devCategory.setCat_id(IdGen.randowNum());
        eamDevCategoryDao.insert(devCategory);
    }

    /**
     * 更新树存储过程
     */
    public void updateDevCategoryTree(){
        eamDevCategoryDao.updateDevCategoryTree();
    }

    /**
     * 查询当前编码在数据库中的数量
     * @param code
     * @return
     */
    public int getBycode(String code){
        Map<String,Object> map= eamDevCategoryDao.getByCode(code);
        return (int)map.get("cou");
    }

    /**
     * 根据ids删除设备类别信息
     * @param ids
     * @param catStatus
     * @return
     */
    @Transactional(readOnly = false)
    public Map<String,Object> delete(String[] ids, String catStatus){
        Map<String,Object> returnMap = new HashMap();
        if("1".equals(catStatus)) {
            returnMap.put("flag",false);
            returnMap.put("msg","此数据为有效数据不允许删除");
            return returnMap;
        } else {
            for(int i=0;i<ids.length;i++){
                int count = (int)eamDevCategoryDao.count(ids[i]).get("cnt");
                if(count > 0) {
                    returnMap.put("flag",false);
                    returnMap.put("msg","此数据被引用不允许删除");
                    return returnMap;
                }else {
                    DevCategory devCategory = new DevCategory();
                    devCategory.preUpdate();
                    devCategory.setId_key(ids[i]);
                    eamDevCategoryDao.delete(devCategory);
                }
            }
        }
        returnMap.put("flag",true);
        returnMap.put("msg","操作成功");
        return returnMap;
    }

    /**
     * @creator wujh
     * @createtime 2017/9/2 11:41
     * @description:  获取设备类别全部信息
     * @param map  入参
     * @return
     */
    public List<Map> findListByMap(Map map){
        return eamDevCategoryDao.findListByMap(map);
    }

    /**
     * @creator wujh
     * @createtime 2017/12/07
     * @description:
     *  重写列表排序功能
     * @param page
     * @param request
     * @return
     */
    @Override
    public Page setOrderBy(Page page, HttpServletRequest request){
        Enumeration<String> en = request.getParameterNames();
        while (en.hasMoreElements()) {
            String nms = en.nextElement().toString();
            if(nms.startsWith("order") && nms.endsWith("[column]")){
                String column = request.getParameterValues(nms)[0];
                String columnname = request.getParameterValues("columns["+column+"][data]")[0];
                if("0".equals(columnname)||"1".equals(columnname)){
                    return setDefaultOrderBy(page,request);
                }else if(columnname.equals("cat_pname") || columnname.equals("cat_pcode")){
                    columnname = "cat_pid";
                }
                String orderby = request.getParameterValues("order[0][dir]")[0];
                page.setOrderBy(columnname+" "+orderby);
                break;
            }
        }
        return page;
    }
}
