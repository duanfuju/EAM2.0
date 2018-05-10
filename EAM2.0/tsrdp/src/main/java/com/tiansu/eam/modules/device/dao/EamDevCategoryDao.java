package com.tiansu.eam.modules.device.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.device.entity.DevCategory;

import java.util.List;
import java.util.Map;

/**
 * 设备类型接口
 * Created by tiansu on 2017/8/7.
 */
@MyBatisDao
public interface EamDevCategoryDao extends CrudDao<DevCategory> {

    /**
     * 根据主键Id获取设备类别信息
     * @param id
     * @return
     */
    public Map<String,Object> getEdit(String id);

    /**
     * 根据实体对象删除符合条件记录
     * @param devCategory
     * @return
     */
    public int delete(DevCategory devCategory);

    /**
     * 根据id 获取map设备类别对象
     * @param id
     * @return
     */
    public Map getDetail(String id);

    /**
     * 获取设备类别下拉树数据
     * @return
     */
    public List<Map> getDevCategoryTree();

    /**
     * 批量导入
     * @param list
     * @return
     */
    public int insertBatch(List<DevCategory> list);//用于批量导入

    /**
     * 查询当前编码在数据库中的数量
     * @param code
     * @return
     */
    public Map<String,Object> getByCode(String code);

    /**
     * 判定当前设备类别是否被引用
     * @param id
     * @return
     */
    public Map<String,Object> count(String id);

    public int updateDevCategoryTree();

    public int updateStatus(DevCategory devCategory);

}
