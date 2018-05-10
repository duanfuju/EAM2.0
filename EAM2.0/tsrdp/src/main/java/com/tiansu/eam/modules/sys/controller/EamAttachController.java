package com.tiansu.eam.modules.sys.controller;

import com.thinkgem.jeesite.common.web.BaseController;
import com.tiansu.eam.common.web.Servlets;
import com.tiansu.eam.modules.sys.entity.Attachment;
import com.tiansu.eam.modules.sys.service.EamAttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangjl
 * @description
 * @create 2017-09-01 15:15
 **/
@Controller
@RequestMapping(value = "${adminPath}/attachement")
public class EamAttachController extends BaseController {

    @Autowired
    EamAttachService attachService;

    @ResponseBody
    @RequestMapping(value = {"getAttachement"})
    public Map<String,Object> getAttachment(String attachid){
        Map<String,Object> returnMap = new HashMap();
        if(attachid == null){
            returnMap.put("flag",false);
            returnMap.put("msg","操作失败，参数为空");
            return returnMap;
        }
        try {
            Attachment attachment = attachService.get(attachid);

            returnMap.put("data",attachment);
            returnMap.put("flag",true);
            returnMap.put("msg","操作成功");
            return returnMap;
        }catch (Exception e){
            e.printStackTrace();
            returnMap.put("flag",false);
            returnMap.put("msg","操作失败["+e.getMessage()+"]");
        }
        return returnMap;
    }


    @ResponseBody
    @RequestMapping(value = {"deleteAttachement"})
    public Map<String,Object> deleteAttachement(String attachid){
        Map<String,Object> returnMap = new HashMap();
        if(attachid == null){
            returnMap.put("flag",false);
            returnMap.put("msg","操作失败，参数为空");
            return returnMap;
        }
        try {
            Attachment attachment = new Attachment();
            attachment.setId(attachid);
            attachService.delete(attachment);

            returnMap.put("flag",true);
            returnMap.put("msg","操作成功");
            return returnMap;
        }catch (Exception e){
            e.printStackTrace();
            returnMap.put("flag",false);
            returnMap.put("msg","操作失败["+e.getMessage()+"]");
        }
        return returnMap;
    }



    @ResponseBody
    @RequestMapping(value = {"addAttachement"})
    public Map<String,Object> addAttachement(Attachment attachment){
HttpServletRequest request = Servlets.getRequest();
request.getParameter("upload");

        Map<String,Object> returnMap = new HashMap();
        if(attachment == null){
            returnMap.put("flag",false);
            returnMap.put("msg","操作失败，参数为空");
            return returnMap;
        }
        try {
            attachment.preUpload();
            attachService.insert(attachment);

            returnMap.put("flag",true);
            returnMap.put("msg","操作成功");
            return returnMap;
        }catch (Exception e){
            e.printStackTrace();
            returnMap.put("flag",false);
            returnMap.put("msg","操作失败["+e.getMessage()+"]");
        }
        return returnMap;
    }

}
