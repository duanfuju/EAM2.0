package com.tiansu.eam.modules.sys.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.sys.entity.Dict;

import java.util.List;
import java.util.Map;

/**
 * Created by wujh on 2017/7/26.
 * 字典DAO接口
 */
@MyBatisDao
public interface EamDictDao extends CrudDao<Dict> {

    public List<String> findTypeList(Dict dict);

    /**
     * 根据主键Id获取设备类别信息
     * @param id
     * @return
     */
    public Map<String,Object> getEdit(String id);

    /**
     * 根据实体对象删除符合条件记录
     * @param dict
     * @return
     */
    public int delete(Dict dict);

    public List<Dict> getByCode(String typeCode);
}
