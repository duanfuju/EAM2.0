package com.tiansu.eam.common.utils.excel.servlet;

import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SuppressWarnings("serial")
@Component()
public class UploadServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Object name  =request.getParameter("name");
		String fileName="";
		if(name.equals("customer")){
			 fileName="客户信息表";	
		}else if(name.equals("opertionlibrary")){
			fileName="标准库";
		}else if(name.equals("materialInfo")){
			fileName="物料信息表";
		}else if(name.equals("supplier")){
			fileName="供应商表";
		}else if(name.equals("devCategory")){
			fileName="设备类别表";
		}else if(name.equals("devLocation")){
			fileName="空间信息表";
		}else if(name.equals("device")){
			fileName="设备信息表";
		}else if(name.equals("materialType")){
			fileName="物料类别表";
		}else if(name.equals("eamUserExt")){
			fileName="人员信息表";
		}else if(name.equals("schedualType")){
			fileName="排班类型表";
		}else if(name.equals("schedual")){
			fileName="排班表";
		}else if(name.equals("inspectionsub")){
			fileName="巡检项表";
		}else if(name.equals("maintset")){
			fileName="保养设置表";
		}else if(name.equals("supplierType")){
			fileName="供应商类型表";
		}


		
		//获取网站部署路径(通过ServletContext对象)，用于确定下载文件位置，从而实现下载  
        String path = this.getServletContext().getRealPath("/");  
  
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型  
        response.setContentType("application/vnd.ms-excel;"); 
        response.setCharacterEncoding("UTF-8");
        //2.设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)  
        response.setHeader("Content-Disposition", "attachment;filename=\""
				+ new String(fileName.getBytes(), "iso-8859-1") + ".xlsm\"");
        //通过文件路径获得File对象(假如此路径中有一个download.pdf文件)  
        String sp = System.getProperty("file.separator");
        String filepath=path + "excelmodel" + sp + fileName +".xlsm";
        File file = new File(filepath);  
  
        try {  
            FileInputStream inputStream = new FileInputStream(file);  
  
            //3.通过response获取ServletOutputStream对象(out)  
          ServletOutputStream out = response.getOutputStream();  
  
            int len = -1;  
            byte[] buffer = new byte[1024];  
            while ((len = inputStream.read(buffer)) != -1){  
                //4.写到输出流(out)中  
                out.write(buffer,0,len);
            }  
            inputStream.close();
            out.close();  
            out.flush();  
  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}

}
