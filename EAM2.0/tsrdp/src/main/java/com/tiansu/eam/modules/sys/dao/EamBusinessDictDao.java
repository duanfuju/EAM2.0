package com.tiansu.eam.modules.sys.dao;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.sys.entity.EamBusinessDict;

import java.util.List;
import java.util.Map;

/**
 * @author tiansu
 * @description
 * @create 2017-11-30 13:53
 **/
@MyBatisDao
public interface EamBusinessDictDao extends CrudDao<EamBusinessDict> {
    /**
     * 根据主键Id获取业务字典信息
     * @param id
     * @return
     */
    public Map<String,Object> getEdit(String id);

    /**
     * 获取业务字典下拉树数据
     * @return
     */
    public List<Map> getBusinessDictTree(Map map);

    public int updateBusinessDictTree();
    // 作废业务字典
    public int close(EamBusinessDict eamBusinessDict);

    public int updateStatus(EamBusinessDict eamBusinessDict);

    public List<Map> getByValue(Map map);
}
