package com.tiansu.eam.modules.sys.service;

import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.sys.dao.EamBusinessDictDao;
import com.tiansu.eam.modules.sys.entity.EamBusinessDict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tiansu
 * @description
 * @create 2017-11-30 13:58
 **/
@Service
public class EamBusinessDictService extends CrudService<EamBusinessDictDao, EamBusinessDict> {

    @Autowired
    private EamBusinessDictDao eamBusinessDictDao;

    /**
     * 根据主键id获取要编辑的对象信息
     * @return
     */
    public Map getEdit(String id){
        return eamBusinessDictDao.getEdit(id);
    }

    /**
     * 获取设备类别下拉树数据
     */
    public List<Map> getBusinessDictTree(Map map){
        return eamBusinessDictDao.getBusinessDictTree(map);
    }

    /**
     * 修改设备类别信息
     * @param eamBusinessDict
     */
    @Transactional(readOnly = false)
    public void update(EamBusinessDict eamBusinessDict){
        eamBusinessDict.preUpdate();
        eamBusinessDictDao.update(eamBusinessDict);
        eamBusinessDictDao.updateBusinessDictTree();
    }


    /**
     * 新增设备类别信息
     * @param eamBusinessDict
     */
    public void insert(EamBusinessDict eamBusinessDict){
        eamBusinessDict.preInsert();
        eamBusinessDictDao.insert(eamBusinessDict);
    }

    /**
     * 根据id作废某条业务字典
     * @param id
     * @return
     */
    @Transactional(readOnly = false)
    public Map<String,Object> close(String id){
        Map<String,Object> returnMap = new HashMap<>();
        if(id == null && "".equals(id)){
            returnMap.put("flag",false);
            returnMap.put("msg","参数错误");
        } else {
            EamBusinessDict eamBusinessDict = new EamBusinessDict();
            eamBusinessDict.preUpdate();
            eamBusinessDict.setId_key(id);
            eamBusinessDictDao.close(eamBusinessDict);
            returnMap.put("flag",true);
            returnMap.put("msg","操作成功");
        }
        return returnMap;
    }

    /**
     * 更新树存储过程
     */
    public void updateBusinessDictTree(){
        eamBusinessDictDao.updateBusinessDictTree();
    }

    /**
     * @creator wujh
     * @createtime 2017/9/2 11:41
     * @description:  获取设备类别全部信息
     * @param map  入参
     * @return
     */
    public List<Map> findListByMap(Map map){
        return eamBusinessDictDao.findListByMap(map);
    }

    public List<Map> getByValue(Map map){
        List<Map> resultList = new ArrayList<>();
        List<Map> resultList1 = new ArrayList<>();
        List<Map> dictList = eamBusinessDictDao.getByValue(map);
        for(int i = 0; i < dictList.size(); i++) {
            Map dicta = dictList.get(i);
            boolean flag = false;
            for(int j = 0; j < dictList.size(); j++){
                Map dictb = dictList.get(j);
                if(dicta.get("pId").toString().equals(dictb.get("id_key").toString())){
                    flag = true;
                    break;
                }
            }
            if(flag) {
                resultList1.add(dicta);
            }
        }

        for(int i = 0; i < resultList1.size(); i++) {
            Map dictc = resultList1.get(i);
            boolean flag = false;
            for(int j = 0; j < resultList1.size(); j++){
                Map dictd = resultList1.get(j);
                if(dictc.get("pId").toString().equals(dictd.get("id_key").toString())){
                    flag = true;
                    break;
                }
            }
            if(flag) {
                resultList.add(dictc);
            } else{
                dictc.put("pId", "");
                resultList.add(dictc);
            }
        }
        System.out.println(resultList);
        return resultList;
    }

}
