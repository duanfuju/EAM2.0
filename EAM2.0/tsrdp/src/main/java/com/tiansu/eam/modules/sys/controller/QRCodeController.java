package com.tiansu.eam.modules.sys.controller;

import com.thinkgem.jeesite.common.web.BaseController;
import com.tiansu.eam.common.EAMConsts;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.modules.sys.entity.SysConfigEntry;
import com.tiansu.eam.modules.sys.service.EamAttachService;
import com.tiansu.eam.modules.sys.service.SysConfigService;
import com.tiansu.eam.modules.sys.utils.QRCodeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

/**
 * @author wangjl
 * @description 二维码生成controller
 * @create 2017-08-29 16:44
 **/
@Controller
@RequestMapping(value = "${adminPath}/QRCode")
public class QRCodeController extends BaseController {
    public final char fg = File.separatorChar;

    @Autowired
    SysConfigService sysConfigService;

    @Autowired
    EamAttachService attachService;

    /**
     *
     * @param picUrl logo图片 /userfiles/files/attachment/QRCODE/001015839756.jpg
     * @param qrdata 二维码数据
     * @param request
     * @param response
     */
    @RequestMapping(value={"createQRCode",""})
    public void createQRCode(String picUrl, String qrdata, HttpServletRequest request, HttpServletResponse response){
        try {
            //不为空表示是二维码配置页面，否则为业务二维码生成页面。logo图片从前台传入，数据不需要；为空：logo图片从数据库查询，数据从前台传入
            if(StringUtils.isNotEmpty(picUrl)){
                picUrl = new String(picUrl.getBytes("iso8859-1"),"UTF-8");
                qrdata = "TYPE:DEVICE;ID:ABCDEFG";
            }else{
                picUrl = getPictureRes(); //logo图片的位置；
                if(StringUtils.isEmpty(picUrl)){
                    throw new RuntimeException("二维码图片资源没有找到，请先配置二维码logo图片");
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        picUrl = request.getSession().getServletContext().getRealPath(picUrl);
        String filePath = picUrl;//getPictureRes(picUrl); //logo图片的位置；
        File outputFile = null;
        try{
            //进行logo图片压缩，生成缩略图
//            String pressedLogoFile = IdGen.randowNum()."";
//            ImagineUtil.ThumbnailsCompressPic(filePath,pressedLogoFile, EAMConsts.QRCODE_LOGO_WIDTH,1);

            File logoFile = new File(filePath);
            if(StringUtils.isNotEmpty(qrdata)){
              String path = request.getSession().getServletContext().getRealPath("")+"userfiles"+fg+"tmp";

                outputFile = new File(path + fg + IdGen.uuid().toLowerCase() + ".png");
                if(outputFile.getParentFile() != null || !outputFile.getParentFile().isDirectory()){
                    //创建临时文件夹以保存二维码图片
                    outputFile.getParentFile().mkdirs();
                }
                BufferedImage image = QRCodeUtil.createQRCodeWithLogo(
                        qrdata.replace("[rn]", "\r\n"), logoFile);
                ImageIO.write(image, "png", outputFile);
                if (outputFile.exists()) {
                    response.setContentType("image/png");
                    InputStream fis = new BufferedInputStream(new FileInputStream(
                            outputFile));
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    fis.close();
                    OutputStream toClient = new BufferedOutputStream(
                            response.getOutputStream());
                    toClient.write(buffer);
                    toClient.flush();
                    toClient.close();
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            //删除临时文件
            if(outputFile!=null){
                outputFile.delete();
            }
        }
    }


    private String convertQRDataMapToString(Map<String,String> dataMap){
        StringBuffer qrDataString = new StringBuffer();
        if(dataMap != null) {
            Iterator iterator = dataMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                qrDataString.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
            }
        }
        return qrDataString.toString();
    }


    private String getPictureRes(){
        String bot_logo = "";   // 二维码中间logo图片地址
        //获取默认图片地址
        SysConfigEntry configEntry = sysConfigService.getByKeyName(EAMConsts.QRCODE_BOT_LOGO);
        if(configEntry!=null){
            bot_logo = configEntry.getConfig_value();
        }
        return bot_logo;
    }


    /**
     *
     * 保存二维码配置项
     * @param request
     * @param response
     */
    @RequestMapping(value={"saveQRCodeConfig",""})
    public @ResponseBody Map<String,Object> saveQRCodeConfig( HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> returnMap = new HashMap();
        try {
            String top_logo = getPara("top_logo");
            String title = getPara("title");
            String bot_logo = getPara("bot_logo");
            SysConfigEntry configEntry = new SysConfigEntry();
            if(StringUtils.isNotEmpty(top_logo)){
                configEntry.preInsert();
                configEntry.setConfig_key(EAMConsts.QRCODE_TOP_LOGO);
                configEntry.setConfig_value(top_logo);
                sysConfigService.updateByKeyName(configEntry);
            }
            if(StringUtils.isNotEmpty(title)){
                configEntry.preInsert();
                configEntry.setConfig_key(EAMConsts.QRCODE_TITLE);
                configEntry.setConfig_value(title);
                sysConfigService.updateByKeyName(configEntry);
            }
            if(StringUtils.isNotEmpty(bot_logo)){
                configEntry.preInsert();
                configEntry.setConfig_key(EAMConsts.QRCODE_BOT_LOGO);
                configEntry.setConfig_value(bot_logo);
                sysConfigService.updateByKeyName(configEntry);
            }

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

    /**
     *
     * 获取二维码配置项
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping(value={"getQRCodeConfig",""})
    public Map<String,Object> getQRCodeConfig( HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> returnMap = new HashMap();
        List<String> configs = new ArrayList();
        try {
            String top_logo = "";
            String title = "";
            String bot_logo = "";

            SysConfigEntry configEntry = sysConfigService.getByKeyName(EAMConsts.QRCODE_TOP_LOGO);
            if(configEntry!=null){
                top_logo = configEntry.getConfig_value();
            }
            configEntry = sysConfigService.getByKeyName(EAMConsts.QRCODE_TITLE);
            if(configEntry!=null){
                title = configEntry.getConfig_value();
            }
            configEntry = sysConfigService.getByKeyName(EAMConsts.QRCODE_BOT_LOGO);
            if(configEntry!=null){
                bot_logo = configEntry.getConfig_value();
            }
            configs.add(top_logo);
            configs.add(title);
            configs.add(bot_logo);

            returnMap.put("flag",true);
            returnMap.put("result",configs);
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
