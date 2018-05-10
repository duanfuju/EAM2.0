package com.tiansu.eam.common.utils.excel.convert;

/**
 * @author wangjl
 * @description
 * @create 2017-12-07 9:59
 **/
public interface ITreeConvertClass extends IConvertClass {

    /**
     * 针对树形结构，有上下级引用时，批量导入无法从数据库中数据查询判断上级是否存在，故在导入时把excel数据预加入缓存中
     */
    public void pushCachedValue(String key,Object value);
}
