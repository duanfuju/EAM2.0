package com.tiansu.eam.modules.base.service;

import com.thinkgem.jeesite.common.service.TreeService;
import com.tiansu.eam.modules.base.dao.BaseTreeDao;
import com.tiansu.eam.modules.base.entity.BaseTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

;

/**
 * @author wangjl
 * @description 排班类型服务类
 * @create 2017-08-21 8:40
 *
 **/
@Service
@Transactional(readOnly = true)
public class BaseTreeService  extends TreeService<BaseTreeDao, BaseTree>  {

    @Autowired
    BaseTreeDao baseTreeDao;


}
