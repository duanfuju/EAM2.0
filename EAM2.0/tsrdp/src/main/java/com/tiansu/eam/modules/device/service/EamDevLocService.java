package com.tiansu.eam.modules.device.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.device.dao.EamDevLocDao;
import com.tiansu.eam.modules.device.entity.DevLocation;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by wangjl on 2017/8/9.
 * @modifier duanfuju
 * @modifytime 2017/9/4 16:34
 * @modifyDec:
 */
@Service
@Transactional(readOnly = true)
public class EamDevLocService extends CrudService<EamDevLocDao, DevLocation> {

    @Autowired
    private EamDevLocDao eamDevLocDao;


    /**
     * @creator duanfuju
     * @createtime 2017/11/23 18:36
     * @description:
     * 根据pid递归查询
     * @return
     */
    public String[]  getDataByPId(Map map){
        List<Map> results = eamDevLocDao.getDataByPId(map);
        String[] ids_array=null;
        if(results.size()>0){
            ids_array = new String[results.size()+1];
            ids_array[0]=map.get("id").toString();
            for (int i = 0; i <results.size()+1 ; i++) {
                Map m=results.get(i);
                ids_array[i+1]=m.get("id").toString();
            }
        }else{
            ids_array = new String[1];
            ids_array[0]=map.get("id").toString();
        }

        return  ids_array;
    }

    /**
     * @creator duanfuju
     * @createtime 2017/11/24 8:34
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
                }else if(columnname.equals("pCode")||columnname.equals("pName")){
                    columnname="loc_pid";
                }
                String orderby = request.getParameterValues("order[0][dir]")[0];
                page.setOrderBy(columnname+" "+orderby);
                break;
            }
        }
        return page;
    }

    /**
     * @modifier duanfuju
     * @modifytime 2017/9/4 16:34
     * @modifyDec:
     * 获取所有未删除的设备
     */
    public List<Map> findListByMap(Map map){
        return eamDevLocDao.findListByMap(map);
    }


    /**
     * @creator duanfuju
     * @createtime 2017/9/6 8:44
     * @description: 
     * 获取设备类别下拉树数据
     */
    public List<Map> getDevLocationTree(){
        return eamDevLocDao.getDevLocationTree();
    }
    /**
     *@creator duanfuju
     * @createtime 2017/9/4 17:05
     * @description: 
     * 根据id获取
     * @param id
     * @return
     */
    public Map findById(String id) {
        Map map=eamDevLocDao.findById(id);
        if(map.containsKey("loc_dept")&&StringUtils.isNotEmpty(String.valueOf(map.get("loc_dept")))) {
            map.put("loc_dept", map.get("loc_dept").toString().trim().replace(",", ";"));
        }
        return map;
    }

    /**
     *@creator shufq
     * @createtime 2017/10/25 11:05
     * @description:
     * 根据位置id获取下一级的位置
     * @param id
     * @return
     */
    public Map findByLocId(String id){
        Map result=new HashMap();
        List<Map> map=eamDevLocDao.findByLocId(id);
        result.put("loc",map);
        return  result;
    }

    /**
     * @creator duanfuju
     * @createtime 2017/9/8 11:41
     * @description:
     *  为app提供接口（根据id获取信息）
     * @param id
     * @return
     */
    public Map findByIdForApp(String id) {
        Map result=new HashMap();
        Map map=eamDevLocDao.findById(id);
        if(map==null){
            return result;
        }
        result.put("id_key",map.get("id_key"));
        result.put("loc_code",map.get("loc_code"));
        result.put("loc_desc",map.get("loc_desc"));
        result.put("loc_name",map.get("loc_name"));
        result.put("loc_pid",map.get("loc_pid"));
        return result;
    }
    /**
     * @creator duanfuju
     * @createtime 2017/9/6 8:52
     * @description:
     * 新增
     * @param devLocation
     */
    public void insert(DevLocation devLocation){
        devLocation.preInsert();
        devLocation.setLoc_dept(devLocation.getLoc_dept().toString().trim().replace(";",","));
        eamDevLocDao.insert(devLocation);
    }

    /**
     * 更新空间信息表的等级字段
     */
    public void updateDevLocationTree(){
        eamDevLocDao.updateDevLocationTree();
    }

    /**
     * @creator duanfuju
     * @createtime 2017/9/13 14:53
     * @description:
     * 修改
     * @param devLocation
     */
    public void update(DevLocation devLocation){
        devLocation.preUpdate();
        devLocation.setLoc_dept(devLocation.getLoc_dept().toString().trim().replace(";",","));
        eamDevLocDao.update(devLocation);
    }
    /**
     *@creator duanfuju
     * @createtime 2017/9/4 16:36
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
        return  eamDevLocDao.delete(map);
    }

    /**
     * @creator duanfuju
     * @createtime 2017/9/4 16:37
     * @description:
     *批量插入
     * @return
     */

    public int insertBatch(List<DevLocation> devLocation){
        return  eamDevLocDao.insertBatch(devLocation);
    }




}
