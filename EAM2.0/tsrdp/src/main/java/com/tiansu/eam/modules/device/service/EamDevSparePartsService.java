package com.tiansu.eam.modules.device.service;

import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.modules.device.dao.EamDevSparePartsDao;
import com.tiansu.eam.modules.device.entity.DevSpareParts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author tiansu
 * @description
 * @create 2017-09-06 10:18
 **/
@Service
public class EamDevSparePartsService extends CrudService<EamDevSparePartsDao, DevSpareParts> {

    @Autowired
    private EamDevSparePartsDao eamDevSparePartsDao;

    /**
     * 插入设备备品备件或者工器具信息
     * @param devSpareParts
     */
    @Transactional(readOnly = false)
    public void insert(DevSpareParts devSpareParts) {
        //插入新的备品备件之前要先删除设备之前
        super.delete(devSpareParts);
        //给主键id赋值
        devSpareParts.setId(IdGen.uuid());
        super.insert(devSpareParts);
    }
}
