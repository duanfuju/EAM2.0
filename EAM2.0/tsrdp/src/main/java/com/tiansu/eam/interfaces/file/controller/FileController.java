
package com.tiansu.eam.interfaces.file.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.io.Files;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.interfaces.file.entity.AddressPicJson;
import com.tiansu.eam.interfaces.resulthelper.APPResponseBody;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by shufq on 2017/10/26.
 */
@Path("/{tenant}/appFile")
//@RequestMapping(value = "/{tenant}/appFile")
@Controller
public class FileController extends BaseController {

    /**
     * @param file     文件
     * @param response
     * @return
     * @creator lihj
     * @createtime 2017/11/8 0001 上午 9:09
     * @description: 图片视频上传
     * @modifier wangr
     * @modifytime 2017/12/4 下午 4:54
     * @modifyDec: 上传文件加注释
     */
    @Path("/uploadImages")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String uploadImages(FormDataMultiPart file,
                               @Context HttpServletRequest request,
                               @Context HttpServletResponse response) throws IOException {
        logger.info("==== 上传文件开始 ====");
        response.setHeader("Access-Control-Allow-Origin", "*");
        List<AddressPicJson> addressPicList = new ArrayList<AddressPicJson>();
        APPResponseBody result = new APPResponseBody();
        //获取文件流
        List<FormDataBodyPart> filePart = file.getFields("file");
        logger.info("==== 文件个数 ：" + filePart.size());
        for (FormDataBodyPart p : filePart) {
            //把表单内容转换成流
            InputStream fileInputStream = p.getValueAs(InputStream.class);
            FormDataContentDisposition disposition = p.getFormDataContentDisposition();
            String classPath = request.getSession().getServletContext().getRealPath("");
            //相对目录
            String basePath = "userfiles" + File.separatorChar + "app" + File.separatorChar;
            String path = classPath + basePath;
            File pictureFile = new File(path);
            if (!pictureFile.exists()) {
                pictureFile.mkdirs();
            }
            String fileName = disposition.getFileName();
            logger.info("==== 源文件名 ：" + fileName);
            //生成文件名
            String imageName = Calendar.getInstance().getTimeInMillis() + "."
                    + fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            File files = writeToFile(fileInputStream, path + imageName);
            logger.info("==== 文件名：" + imageName);
            logger.info("==== 文件是否存在 ：" + files.exists());
            if (files.exists()) {
                AddressPicJson address = new AddressPicJson();
                address.setAddrUrl("userfiles/" + "app/" + imageName);
                addressPicList.add(address);
                logger.info("==== 返回地址：userfiles/" + "app/" + imageName);
            }
        }
        result.setData(addressPicList);
        logger.info("==== 上传文件结束 ====");
        return JSON.toJSONString(result);
    }

    public static File writeToFile(InputStream is, String uploadedFileLocation) {
        File file = new File(uploadedFileLocation);
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];
            while ((is.read(buffer)) != -1) {
                os.write(buffer);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }


    @Path("/uploadImage")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String uploadImage(@RequestParam() File file,
                              @Context HttpServletRequest request,
                              @Context HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String address = "";
        String fileName = file.getName();
        String rootpath = request.getSession().getServletContext().getRealPath("");
        String path = "userfiles" + File.separatorChar + "app" + File.separatorChar;
        File tempFile = new File(rootpath + path);
//        String txtName = Calendar.getInstance().getTimeInMillis()
//                    + ".txt";
//                + fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
//        File files = new File(rootpath + path + txtName);//txt文件
        String imageName = Calendar.getInstance().getTimeInMillis()
                + ".jpg";
        File files = new File(rootpath + path + imageName);//jpg文件


        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        try {
            Files.copy(file, files);
        } catch (IOException e) {
            e.printStackTrace();
        }
        address = rootpath + path + imageName;
        return address;
    }
}

