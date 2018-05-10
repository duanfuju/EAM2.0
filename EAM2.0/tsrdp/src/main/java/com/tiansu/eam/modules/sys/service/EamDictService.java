package com.tiansu.eam.modules.sys.service;

import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.modules.sys.dao.EamDictDao;
import com.tiansu.eam.modules.sys.entity.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by tiansu on 2017/7/26.
 */
@Service
public class EamDictService  extends CrudService<EamDictDao, Dict> {
    @Autowired
    private EamDictDao eamDictDao;

    /**
     * 查询字段类型列表
     * @return
     */
    public List<String> findTypeList(){
        return dao.findTypeList(new Dict());
    }

    /**
     * 根据主键id获取要编辑的对象信息
     * @return
     */
    public Map getEdit(String id){
        return eamDictDao.getEdit(id);
    }

    /**
     * 修改设备类别信息
     * @param dict
     */
    @Transactional(readOnly = false)
    public void update(Dict dict){
        dict.preUpdate();
        eamDictDao.update(dict);
    }

    /**
     * 新增设备类别信息
     * @param dict
     */
    public void insert(Dict dict){
        dict.preInsert();
        dict.setId_key(IdGen.uuid());
        eamDictDao.insert(dict);
    }

    /**
     * 根据主键id删除业务字典
     * @param ids
     * @return
     */
    @Transactional(readOnly = false)
    public String delete(String[] ids){
        for(int i=0;i<ids.length;i++){
            Dict dict = new Dict();
            dict.setId_key(ids[i]);
            eamDictDao.delete(dict);
        }
        return "success";
    }

    /**
     * 根据模块获取枚举值
     * @param typeCode
     * @return
     */
    public List<Dict> getByCode(String typeCode) {
        return eamDictDao.getByCode(typeCode);
    }
}
