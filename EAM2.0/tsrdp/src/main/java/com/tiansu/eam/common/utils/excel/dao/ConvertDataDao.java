package com.tiansu.eam.common.utils.excel.dao;


import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import java.util.List;
import java.util.Map;
@MyBatisDao
public interface ConvertDataDao {
    /**
     * 字段所对应的key和value的转换
     * @param map
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getConvertData(Map<String, Object> map)throws Exception;

    /**
     * 根据条件查询指定的表，确认是否存在
     * @param map
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getSingleList(Map<String, Object> map)throws Exception;

}
