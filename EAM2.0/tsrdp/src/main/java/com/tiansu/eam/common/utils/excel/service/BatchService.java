package com.tiansu.eam.common.utils.excel.service;

import com.tiansu.eam.common.utils.excel.dao.BatchDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Administrator on 2017/8/3 0003.
 */
@Service
public class BatchService  implements BatchDao{

    @Autowired
    private BatchDao batchDao;

    @Override
    public int insertCustomer(Map<String, Object> map) {
        return batchDao.insertCustomer(map);
    }
}
