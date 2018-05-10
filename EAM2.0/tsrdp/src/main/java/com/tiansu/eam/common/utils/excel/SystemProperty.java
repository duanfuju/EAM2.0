package com.tiansu.eam.common.utils.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SystemProperty {
	private static final String configFile = "config.properties";
	
	
	private static Properties prop;
	
	
	public static Properties getProperties(){
		if(prop == null){
			prop = new Properties();
		}
		return prop;
	}
	
	
	public static void loadProptyConfig(String filePath) {
		
        InputStream inputStream = null; 
        try { 
//        	System.out.println(context.getServletContext().getRealPath("/"));
        	//String resourcePath = filePath+File.separator+configFile;
			String resourcePath =filePath;
			File f = new File(resourcePath);
        	if(!f.exists() || !f.canRead()){
        		throw new RuntimeException(configFile+"配置文件读取失败");
        	}
        	 inputStream = new FileInputStream(f);
        	//            inputStream = getClass().getResourceAsStream(resourcePath); 
            SystemProperty.getProperties().load(inputStream); 
        } catch (Exception ex) { 
            ex.printStackTrace(); 
            throw new RuntimeException(ex);
        }finally{
        	if(inputStream!=null){
        		try {
					inputStream.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					 throw new RuntimeException(e);
				}
        	}
        }
	}
	
}
