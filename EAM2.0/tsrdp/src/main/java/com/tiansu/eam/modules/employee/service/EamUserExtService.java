package com.tiansu.eam.modules.employee.service;/**
 * @description
 * @author duanfuju
 * @create 2017-09-14 16:27
 **/

import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.employee.dao.EamUserExtDao;
import com.tiansu.eam.modules.employee.entity.EamUserExt;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author duanfuju
 * @create 2017-09-14 16:27
 * @desc 人员信息扩展 Service
 **/
@Service
public class EamUserExtService extends CrudService<EamUserExtDao,EamUserExt> {

    @Autowired
    private EamUserExtDao eamUserExtDao;

    /**
     * @creator duanfuju
     * @createtime 2017/11/17 14:45
     * @description:
     *     查询指定数据再进行筛选
     * @param map
     * @return
     */
    public List<Map> findListByMapForInterface(Map map) {
        List<Map> res =eamUserExtDao.findListByMap(map);
        List<Map> result = new ArrayList<Map>();
        for (int i = 0; i < res.size(); i++) {
            Map resultSingle=res.get(i);
            boolean flag=true;
            try{
                if(map.containsKey("majorList")){
                    int num=0;
                    List<String> arr_major=(List<String>)map.get("majorList");
                    String[] arr_major1=resultSingle.get("major").toString().split(",");
                    for (int j = 0; j < arr_major.size(); j++) {
                        for (int k = 0; k < arr_major1.length; k++) {
                            if (arr_major.get(j).equalsIgnoreCase(arr_major1[k])){
                                num++;
                            }
                        }
                    }
                    if(num!=arr_major.size()){
                        flag=false;
                    }
                }
                if(map.containsKey("dutyAreaList")){
                    int num=0;
                    List<String> duty_area=(List<String>)map.get("dutyAreaList");
                    String[] duty_area1=resultSingle.get("duty_area").toString().split(",");
                    for (int j = 0; j < duty_area.size(); j++) {
                        for (int k = 0; k < duty_area1.length; k++) {
                            if (duty_area.get(j).equalsIgnoreCase(duty_area1[k])){
                                num++;
                            }
                        }
                    }
                    if(num!=duty_area.size()){
                        flag=false;
                    }
                }
                if(flag){
                    result.add(resultSingle);
                }

            }catch (Exception e){
                e.printStackTrace();
                continue;
            }
        }
        return result;
    }

    /**
     * @creator duanfuju
     * @createtime 2017/9/15 16:34
     * @description:
     * 根据登录名称获取人员信息
     * @param loginname
     * @return
     */
    public List<Map> findByLoginName(String loginname){
        return eamUserExtDao.findByLoginName(loginname);
    }
    /**
     * @creator duanfuju
     * @createtime 2017/9/14 16:32
     * @description: 
     * 列表查询
     * @param map
     * @return
     */
    public List<Map> findList(Map map) {
        return eamUserExtDao.findListByMap(map);
    }

    /**
     * @creator shufq
     * @createtime 2017/10/13  10:00
     * @description 根据部门信息
     * @param map
     * @return
     */
    public List<Map> findDept(Map map) {
        return eamUserExtDao.findDept(map);
    }
    /**
     * @creator duanfuju
     * @createtime 2017/9/14 16:32
     * @description: 
     * 根据id获取
     * @param id
     * @return
     */
    public Map findById(String id) {
        Map map=eamUserExtDao.findById(id);
        if(map.containsKey("own_area")&&StringUtils.isNotEmpty(String.valueOf(map.get("own_area")))){
            map.put("own_area",map.get("own_area").toString().trim().replace(",",";"));//归属区域
        }
        if(map.containsKey("duty_area")&&StringUtils.isNotEmpty(String.valueOf(map.get("duty_area")))){
            map.put("duty_area",map.get("duty_area").toString().trim().replace(",",";"));//责任区域
        }
        if(map.containsKey("major")&&StringUtils.isNotEmpty(String.valueOf(map.get("major")))){
            map.put("major",map.get("major").toString().trim().replace(",",";"));//专业
        }
        return map;
    }
    /**
     * @creator duanfuju
     * @createtime 2017/9/14 16:32
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
        return  eamUserExtDao.delete(map);
    }

    /**
     * @creator duanfuju
     * @createtime 2017/9/14 16:32
     * @description: 
     *批量插入
     * @return
     */
    public int insertBatch(List<EamUserExt> EamUserExt){
        return  eamUserExtDao.insertBatch(EamUserExt);
    }

    /**
     * @creator duanfuju
     * @createtime 2017/9/19 15:09
     * @description:
     *  新增
     * @param eamUserExt
     */
    public void insert(EamUserExt eamUserExt){
        eamUserExt.preInsert();
        //修改多选的连接符号
        eamUserExt.setOwn_area(eamUserExt.getOwn_area().trim().replace(";",","));//归属区域
        eamUserExt.setDuty_area(eamUserExt.getDuty_area().trim().replace(";",","));//责任区域
        eamUserExt.setMajor(eamUserExt.getMajor().trim().replace(";",","));//专业
        eamUserExtDao.insert(eamUserExt);
    }

    /**
     * @creator duanfuju
     * @createtime 2017/9/19 15:09
     * @description:
     *  修改
     * @param eamUserExt
     */
    public void update(EamUserExt eamUserExt){
        eamUserExt.preUpdate();
        //修改多选的连接符号
        eamUserExt.setOwn_area(eamUserExt.getOwn_area().trim().replace(";",","));//归属区域
        eamUserExt.setDuty_area(eamUserExt.getDuty_area().trim().replace(";",","));//责任区域
        eamUserExt.setMajor(eamUserExt.getMajor().trim().replace(";",","));//专业
        eamUserExtDao.update(eamUserExt);
    }
    /**
     * @creator duanfuju
     * @createtime 2017/9/22 14:24
     * @description:
     * 获取所有的人员
     * @return
     */
    public List<Map> getAllUser(){
        return eamUserExtDao.getAllUser();
    }


    /**
     * @creator duanfuju
     * @createtime 2017/9/28 17:05
     * @description:
     *  查询可以导入扩展表的人员信息
     * @return
     */
    public List<Map> selectNeedLoadData(){
        return eamUserExtDao.selectNeedLoadData();
    }

    /**
     * @creator shufq
     * @createtime 2017/10/26  10:00
     * @description 根据部门查询人员信息
     * @param map
     * @return
     */
    public List<Map> findUser(Map map) {
        return eamUserExtDao.findUser(map);
    }
}
