package com.tiansu.eam.common.utils.excel.controller;

import com.tiansu.eam.common.utils.excel.service.FileUpLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @creator duanfuju
 * @createtime 2017/8/28 9:09
 * @description: 导入excel解析Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/eam/excels")
public class FileUpLoadController{




    @Autowired
    private FileUpLoadService fileUpLoadService;

    /**
     * @creator duanfuju
     * @createtime 2017/8/28 9:10
     * @description: 
     * 导入数据
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = {"importExcel"})
    public String importExcel (HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
      return  fileUpLoadService.importExcel(request,response);
    }

    /**
     * @creator duanfuju
     * @createtime 2017/8/28 9:10
     * @description: 
     * 上传（暂时有问题，用不了）
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"fileUp"})
    public void fileup(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
        fileUpLoadService.fileup(req,resp);
    }
}
