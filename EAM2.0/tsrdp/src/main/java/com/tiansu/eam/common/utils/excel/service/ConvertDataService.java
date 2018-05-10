package com.tiansu.eam.common.utils.excel.service;

import com.tiansu.eam.common.utils.excel.dao.ConvertDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by dfj on 2017/7/20 0020.
 * 转换文本
 */
@Service
public class ConvertDataService implements ConvertDataDao {

    @Autowired
    private ConvertDataDao convertDataDao;

    @Override
    public List<Map<String, Object>> getConvertData(Map<String, Object> map) throws Exception {
        return convertDataDao.getConvertData(map);
    }

    @Override
    public List<Map<String, Object>> getSingleList(Map<String, Object> map) throws Exception {
        return convertDataDao.getSingleList(map);
    }
}
