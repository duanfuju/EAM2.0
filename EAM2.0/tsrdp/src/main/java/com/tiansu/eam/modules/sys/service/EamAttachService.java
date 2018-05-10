package com.tiansu.eam.modules.sys.service;

import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.sys.dao.EamAttachDao;
import com.tiansu.eam.modules.sys.entity.Attachment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wangjl
 * @description
 * @create 2017-09-01 15:28
 **/
@Service
@Transactional(readOnly = true)
public class EamAttachService extends CrudService<EamAttachDao, Attachment> {

}
