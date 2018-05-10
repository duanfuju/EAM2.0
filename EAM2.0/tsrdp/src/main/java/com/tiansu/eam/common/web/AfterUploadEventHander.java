package com.tiansu.eam.common.web;

import com.ckfinder.connector.configuration.IConfiguration;
import com.ckfinder.connector.data.AfterFileUploadEventArgs;
import com.ckfinder.connector.data.EventArgs;
import com.ckfinder.connector.data.IEventHandler;
import com.ckfinder.connector.errors.ConnectorException;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.modules.sys.entity.Attachment;
import com.tiansu.eam.modules.sys.service.EamAttachService;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author wangjl
 * @description AfterFileUploadEventArgs
 * @create 2017-09-04 16:31
 **/
@Component
public class AfterUploadEventHander implements IEventHandler {

    EamAttachService eamAttachService = SpringContextHolder.getBean(EamAttachService.class);

    @Override
    public boolean runEventHandler(EventArgs eventArgs, IConfiguration iConfiguration) throws ConnectorException {
        AfterFileUploadEventArgs aupEventArgs = (AfterFileUploadEventArgs) eventArgs;
        saveAttachment(aupEventArgs,iConfiguration);

        //上传完毕后做持久化操作
        return true;
    }

    private boolean saveAttachment(AfterFileUploadEventArgs aupEventArgs,IConfiguration iConfiguration){
        boolean success = false;
        File uploadedFile = aupEventArgs.getFile();
        try {
            Attachment attachment = new Attachment();
            attachment.preUpload();
            attachment.setAttach_name(uploadedFile.getName());

            //获取相对路径
            String userBaseDir = Global.getUserfilesBaseDir();
            if(userBaseDir.endsWith("/")){
                userBaseDir = userBaseDir.substring(0,userBaseDir.length()-2);
            }
            if(userBaseDir!=null){
                int startIndex = uploadedFile.getAbsolutePath().indexOf(userBaseDir);
                String path = uploadedFile.getAbsolutePath().substring(startIndex+userBaseDir.length(),uploadedFile.getAbsolutePath().length());
                attachment.setAttach_path(path);
            }
            String type = uploadedFile.getName().substring(uploadedFile.getName().lastIndexOf('.')+1,uploadedFile.getName().length());
            attachment.setAttach_type(type);
            attachment.setAttach_source(EAMConsts.UPLOAD_SOURCE);
            eamAttachService.insert(attachment);
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return true;
    }

}
