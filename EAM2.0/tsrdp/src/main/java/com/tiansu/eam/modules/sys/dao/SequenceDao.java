package com.tiansu.eam.modules.sys.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.sys.entity.Sequence;

/**
 * Created by zhangdf on 2017/7/25.
 */
@MyBatisDao
public interface SequenceDao extends CrudDao<SequenceDao>{
    public Sequence getBySeqType(String seqtype);
    public int updateBySeqType(String seqtype);
    public int updateNoBySeqType(String seqtype);
}
