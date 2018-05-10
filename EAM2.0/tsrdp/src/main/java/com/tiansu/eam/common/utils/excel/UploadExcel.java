/**
 * 
 */
package com.tiansu.eam.common.utils.excel;

/*import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;*/
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @author zhangww
 * @date 2016-11-07 15:21:07
 *@modifier duanfuju
 * @modifytime 2017/9/4 15:11
 * @modifyDec:
 * jxl不兼容2007excel，换成poi
 */
public class UploadExcel {
//校验正则表达式
/*	public static String[] getRegex(String regex){
		if(regex.equals("customer")){
			return new String[]{"^[A-Za-z0-9]+$","^[A-Za-z0-9\u4E00-\u9FA5]+$","^[A-Za-z0-9\u4E00-\u9FA5]+$","^[A-Za-z0-9]+$","^[A-Za-z0-9\u4E00-\u9FA5]*$","^[A-Za-z0-9]+$","^\\d{4}-\\d{2}-\\d{2}$","^[A-Za-z0-9\u4E00-\u9FA5]*$","^[A-Za-z0-9\u4E00-\u9FA5]*$","^[A-Za-z0-9\u4E00-\u9FA5]*$","^[A-Za-z0-9\u4E00-\u9FA5]*$","^[A-Za-z0-9\u4E00-\u9FA5]*$","^[A-Za-z0-9\u4E00-\u9FA5]*$","^[A-Za-z0-9\u4E00-\u9FA5]*$","^[A-Za-z0-9\u4E00-\u9FA5]*$","^[A-Za-z0-9\u4E00-\u9FA5]*$","^[A-Za-z0-9\u4E00-\u9FA5]*$","^[A-Za-z0-9\u4E00-\u9FA5]*$","^[A-Za-z0-9\u4E00-\u9FA5]*$","^[A-Za-z0-9\u4E00-\u9FA5]*$","^\\d{4}-\\d{2}-\\d{2}$","^[A-Za-z0-9\u4E00-\u9FA5]*$"} ;
		}else if(regex.equals("oprationLibrary")){
			return new String[]{"^[A-Za-z0-9]+$","^[,,A-Za-z0-9]+$","^[A-Za-z0-9]+$","^[A-Za-z0-9]+$"};
		}else if(regex.equals("materialInfo")){
			return new String[]{"^[A-Za-z0-9]+$","^[A-Za-z0-9\u4E00-\u9FA5]+$","^[0-9]+$","^[A-Za-z0-9\u4E00-\u9FA5]+$","^[A-Za-z0-9\u4E00-\u9FA5]+$","^[A-Za-z0-9]*$","^[A-Za-z0-9]*$","^[A-Za-z0-9\u4E00-\u9FA5]+$","^[A-Za-z0-9\u4E00-\u9FA5]+$","^[A-Za-z0-9\u4E00-\u9FA5]+$","^[A-Za-z0-9\u4E00-\u9FA5]*$","^[A-Za-z0-9\u4E00-\u9FA5]*$","^[A-Za-z0-9\u4E00-\u9FA5]*$"};
		}
		return null;
	}*/
	// 把excel解析成数组
/*	public static List<String[]> readExcel(String filePath) {
		List<String[]> list = new ArrayList<String[]>();
		try {
			InputStream is = new FileInputStream(filePath);
			Workbook rwb = Workbook.getWorkbook(is);
			Sheet st = rwb.getSheet("Sheet1");
			if(st == null){
				return null;
			}
			// 获取列
			int col = st.getColumns();
			// 获取行
			int rows = st.getRows();
			// 从第三行获取
			for (int k = 2; k < rows; k++) {

				String[] values = new String[col];
				for (int i = 1; i < col; i++) {
					Cell co = st.getCell(i, k);
					// 通用的获取cell值的方式,返回字符串
					String value = co.getContents().replaceAll(" ", "");
					/*boolean validata=value.matches(str[i-1]);
					if(validata==false){
						//System.out.println("excel数据不符合标准");
						return null;
					}
					values[i] = value;
					//System.out.println(value);
				}
				list.add(values);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}*/
	//可以根据sheet名解析对应的sheet
	/*public static List<String[]> readExcels(String filePath,String s){
		
		List<String[]> list = new ArrayList<String[]>();
		try {
			InputStream is = new FileInputStream(filePath);
			Workbook rwb = Workbook.getWorkbook(is);
			Sheet st = rwb.getSheet(s);
			if(st == null){
				return null;
			}
			// 获取列
			int col = st.getColumns();
			// 获取行
			int rows = st.getRows();
			// 从第三行获取
			for (int k = 2; k < rows; k++) {

				String[] values = new String[col];
				for (int i = 1; i < col; i++) {
					Cell co = st.getCell(i, k);
					// 通用的获取cell值的方式,返回字符串
					String value = co.getContents().replaceAll(" ", "");
					*//*boolean validata=value.matches(str[i-1]);
					if(validata==false){
						//System.out.println("excel数据不符合标准");
						return null;
					}*//*
					values[i] = value;
					//System.out.println(value);
				}
				list.add(values);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}*/

	// 上传excel到服务器
	public static String uploadExcel(HttpServletRequest request,
			HttpServletResponse response,String savePath) throws ServletException, IOException {
		
		File file = new File(savePath);
		String excelPath = "";
		if (!file.exists() && !file.isDirectory()) {
			// System.out.println("目录或文件不存在！");
			file.mkdir();
		}
		try {
			// 使用Apache文件上传组件处理文件上传步骤：
			// 1、创建一个DiskFileItemFactory工厂
			DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
			// 2、创建一个文件上传解析器
			ServletFileUpload fileUpload = new ServletFileUpload(
					diskFileItemFactory);
			// 解决上传文件名的中文乱码
			fileUpload.setHeaderEncoding("UTF-8");
			// 3、判断提交上来的数据是否是上传表单的数据
			if (!fileUpload.isMultipartContent(request)) {
				// 按照传统方式获取数据
				return null;
			}
			// 4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
			List<FileItem> list = fileUpload.parseRequest(request);
			for (FileItem item : list) {
				// 如果fileitem中封装的是普通输入项的数据
				if (item.isFormField()) {
					String name = item.getFieldName();
					// 解决普通输入项的数据的中文乱码问题
					String value = item.getString("UTF-8");
					String value1 = new String(name.getBytes("iso8859-1"),
							"UTF-8");
					// System.out.println(name+"  "+value);
					// System.out.println(name+"  "+value1);
				} else {
					// 如果fileitem中封装的是上传文件，得到上传的文件名称，
					String fileName = item.getName();
					//System.out.println(fileName);
					if (fileName == null || fileName.trim().equals("")) {
						continue;
					}
					// 注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：
					// c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
					// 处理获取到的上传文件的文件名的路径部分，只保留文件名部分
					fileName = fileName.substring(fileName
							.lastIndexOf(File.separator) + 1);
					// 获取item中的上传文件的输入流
					InputStream is = item.getInputStream();
					// 创建一个文件输出流
					excelPath = savePath + File.separator + fileName;
					FileOutputStream fos = new FileOutputStream(excelPath);

					// 创建一个缓冲区
					byte buffer[] = new byte[1024];
					// 判断输入流中的数据是否已经读完的标识
					int length = 0;
					// 循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
					while ((length = is.read(buffer)) > 0) {
						// 使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\"
						// + filename)当中
						fos.write(buffer, 0, length);
					}
					// 关闭输入流
					is.close();
					// 关闭输出流
					fos.close();
					// 删除处理文件上传时生成的临时文件
					item.delete();
				}
			}
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return excelPath;
	}


	/**
	 *@creator duanfuju
	 * @createtime 2017/9/4 8:36
	 * @description:
	 * 	解析excel文件的数据
	 * @param excelPath
	 * @param sheetname
	 * @return
	 * @throws IOException
	 */
	public  static List<String[]>  readExcelsByPoi(String excelPath,String sheetname) throws IOException {
		//String file_dir = "test.xlsx";
		List<String[]> list = new ArrayList<String[]>();
		Workbook book = null;
		book = getExcelWorkbook(excelPath);
		Sheet sheet = getSheetBySheetName(book,sheetname);
		//获取最后一行
		int lastRowNum = sheet.getLastRowNum();
		System.out.println("last number is "+ lastRowNum);
		for(int i = 2 ; i <= lastRowNum ; i++){
			Row row = null;
			row = sheet.getRow(i);
			if( row != null){//如果行不为空
				System.out.println("reading line is " + i);
				int lastCellNum = row.getLastCellNum();//获取最后一列
				System.out.println("lastCellNum is " + lastCellNum );
				Cell cell = null;
				String[] values = new String[lastCellNum];//用于存储行数据
				for( int j = 0 ; j < lastCellNum ; j++ ){
					cell = row.getCell(j);
					if( cell != null ){//如果列不为空
						//判断单元格格式（转化成String）
						String cellValue = parseExcel(cell);
						System.out.println("cell value is \n" + cellValue);
						values[j]=cellValue.trim();
					}else{
						values[j]="";
					}
				}
				list.add(values);
			}
		}
		return list;
	}


	/**
	 * @creator duanfuju
	 * @createtime 2017/9/4 8:36
	 * @description:
	 * 将单元格中的数据转换成String字符串
	 * @param cell
	 * @return
	 */
	private static String parseExcel(Cell cell) {
		String result = new String();
		switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_NUMERIC:// 数字类型
				if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
					SimpleDateFormat sdf = null;
					if (cell.getCellStyle().getDataFormat() == HSSFDataFormat
							.getBuiltinFormat("h:mm")) {
						sdf = new SimpleDateFormat("HH:mm");
					} else {// 日期
						sdf = new SimpleDateFormat("yyyy-MM-dd");
					}
					Date date = cell.getDateCellValue();
					result = sdf.format(date);
				} else if (cell.getCellStyle().getDataFormat() == 58) {
					// 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					double value = cell.getNumericCellValue();
					Date date = org.apache.poi.ss.usermodel.DateUtil
							.getJavaDate(value);
					result = sdf.format(date);
				} else {
					double value = cell.getNumericCellValue();
					CellStyle style = cell.getCellStyle();
					DecimalFormat format = new DecimalFormat();
					String temp = style.getDataFormatString();
					// 单元格设置成常规
					if (temp.equals("General")) {
						format.applyPattern("#");
					}
					result = format.format(value);
				}
				break;
			case HSSFCell.CELL_TYPE_STRING:// String类型
				result = cell.getRichStringCellValue().toString();
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				result = "";
			default:
				result = "";
				break;
		}
		return result;
	}


	/**
	 * @creator duanfuju
	 * @createtime 2017/9/4 8:39
	 * @description:
	 * 根据名称获取Sheet
	 * @param book
	 * @param sheetname
	 * @return
	 */
	public static Sheet getSheetBySheetName(Workbook book,String sheetname){
		Sheet sheet = null;
		try {
			sheet = book.getSheet(sheetname);
//          if(sheet == null){
//              sheet = book.createSheet("Sheet"+number);
//          }
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return sheet;
	}

	/**
	 * @creator duanfuju
	 * @createtime 2017/9/4 8:40
	 * @description:
	 * 根据路径获取Workbook
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static Workbook getExcelWorkbook(String filePath) throws IOException{
		Workbook book = null;
		File file  = null;
		FileInputStream fis = null;

		try {
			file = new File(filePath);
			if(!file.exists()){
				throw new RuntimeException("文件不存在");
			}else{
				fis = new FileInputStream(file);
				book = WorkbookFactory.create(fis);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if(fis != null){
				fis.close();
			}
		}
		return book;
	}









}
