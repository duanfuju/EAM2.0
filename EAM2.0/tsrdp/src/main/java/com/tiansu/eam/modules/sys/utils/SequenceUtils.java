package com.tiansu.eam.modules.sys.utils;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.modules.sys.dao.SequenceDao;
import com.tiansu.eam.modules.sys.entity.CodeEnum;
import com.tiansu.eam.modules.sys.entity.Sequence;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhangdf on 2017/7/31.
 */
public class SequenceUtils {
    //序列号实体
    private static SequenceDao sequenceDao= SpringContextHolder.getBean(SequenceDao.class);



    public static String getBySeqType(CodeEnum seqtype){  //获取对应内容的序列编码
        String code="";
        Sequence sequence=sequenceDao.getBySeqType(String.valueOf(seqtype));
        if(sequence !=null){
            Integer seqClean=sequence.getSeq_clean();
            Date seqCurrenttime =sequence.getSeq_currenttime();
            Date d=new Date();
            String daytime=new SimpleDateFormat("yyyy-MM-dd").format(d);

            String time= new SimpleDateFormat("yyyy-MM-dd").format(seqCurrenttime);
            if(!daytime.equals(time)&& seqClean==1){//流水号从0开始,更新时间
                sequenceDao.updateBySeqType(String.valueOf(seqtype));
            }
            sequence=sequenceDao.getBySeqType(String.valueOf(seqtype));
            String seqConst=sequence.getSeq_const();
            String seqTimeStamp=sequence.getSeq_timestamp();
            Integer serialNo=sequence.getSerial_no();
            Integer seqlength=sequence.getSeq_length();
            Integer seqIncre=sequence.getSeq_incre();
            code=seqConst+new SimpleDateFormat(seqTimeStamp).format(d)+String.format("%0"+seqlength+"d",serialNo+seqIncre);//对应的业务编码
            sequenceDao.updateNoBySeqType(String.valueOf(seqtype));//每次都更新一下流水号
        }else{
            throw new RuntimeException("数据异常");
        }

        return code;
    }
}
