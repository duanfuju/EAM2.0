package com.tiansu.eam.common.utils.excel;

import com.tiansu.eam.common.utils.excel.model.ExcelConfig;
import com.tiansu.eam.common.utils.excel.model.ExcelFieldModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * 导出通用
 *
 */
public class ExcelUtil {

	private String filename;
	private String title;
	private String[] titles;
	private List<String[]> list;
	private String sheet="Sheet1";
	private String []sheets;//多表导出创建多个sheet
	private String []titlem;//每个sheet一个标题
	private List<String[]> titlesm;//每个sheet里面不同的抬头名称
	private List<List<String[]>> listm;//每个sheet里面不同的集合
	private List<Integer> checkItem;
	private List<List> checkItems;//多表导出的必填颜色属性


	public List<List> getCheckItems() {
		return checkItems;
	}

	public ExcelUtil setCheckItems(List<List> checkItems) {
		this.checkItems = checkItems;
		return this;
	}

	public ExcelUtil setSheet(String sheet) {
		this.sheet = sheet;
		return this;
	}

	public ExcelUtil setFilename(String filename) {
		this.filename = filename;
		return this;
	}

	public ExcelUtil setTitle(String title) {
		this.title = title;
		return this;
	}

	public ExcelUtil setTitles(String[] titles) {
		this.titles = titles;
		return this;
	}

	public ExcelUtil setList(List<String[]> list) {
		this.list = list;
		return this;
	}

	public ExcelUtil  setSheets(String [] sheets) {
		this.sheets=sheets;
		return this;
	}

	public ExcelUtil  setTitlem(String [] titlem) {
		this.titlem=titlem;
		return this;
	}
	public ExcelUtil setTitlesm(List<String[]> titlesm) {
		this.titlesm=titlesm;
		return this;
	}
	public ExcelUtil setListm(List<List<String[]>> listm) {
		this.listm=listm;
		return this;
	}

	public ExcelUtil setCheckItem(List<Integer> checkitem) {
		this.checkItem=checkitem;
		return this;
	}

	@SuppressWarnings({ "deprecation" })
	public void start(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		//resp.setContentType("application/vnd.ms-excel");
		resp.setContentType("application/x-excel");
		resp.setCharacterEncoding("UTF-8");
		resp.setHeader("Content-Disposition", "attachment;filename=\""
				+ new String(filename.getBytes(), "iso-8859-1") + ".xlsm\"");
		String sp = System.getProperty("file.separator");
		String filepath = req.getRealPath("/") + "excel" + sp + filename+".xlsm";

		XSSFWorkbook wb = new XSSFWorkbook(
				OPCPackage.open(req.getRealPath("/") + "excelmodel" + sp + filename+".xlsm")
		);

		XSSFSheet sheet = wb.getSheet(this.sheet);
		//sheet.addMergedRegion(new CellRangeAddress(0,0,0,titles.length));
		for (int i = 0; i < titles.length; i++)
			sheet.setColumnWidth((short) i, (short) 5200);
		XSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		XSSFFont font = wb.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		style.setWrapText(true);

		XSSFCellStyle styles = wb.createCellStyle();
		styles.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		styles.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		styles.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		styles.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		styles.setBorderRight(XSSFCellStyle.BORDER_THIN);
		styles.setBorderTop(XSSFCellStyle.BORDER_THIN);
		XSSFFont fonts = wb.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		styles.setFont(fonts);
		styles.setWrapText(true);

		/*设置必填标红-start*/
		XSSFCellStyle styless = wb.createCellStyle();
		styless.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		styless.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		styless.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		styless.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		styless.setBorderRight(XSSFCellStyle.BORDER_THIN);
		styless.setBorderTop(XSSFCellStyle.BORDER_THIN);
		//创建一种字体
		XSSFFont fontt=wb.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		fontt.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		fontt.setColor(XSSFFont.COLOR_RED);
		styless.setFont(fontt);
		styless.setWrapText(true);
		/*设置必填标红-end*/

		XSSFRow row = sheet.createRow(0);
		row.setHeight((short) 500);
		XSSFCell cell = row.createCell((short) 0);
		/*cell.setCellValue(title);
		cell.setCellStyle(style);*/
		for (int i = 1; i <= titles.length; i++) {
			cell.setCellValue(title);
			cell = row.createCell((short) (i));
			cell.setCellStyle(style);
		}

		row = sheet.createRow(1);
		row.setHeight((short) 500);
		cell = row.createCell((short) 0);
		cell.setCellValue("序号");
		cell.setCellStyle(style);
		System.out.println();
		for (int i = 1; i <= titles.length; i++) {
			String ttt = titles[i-1];
			cell = row.createCell((short) (i));
			cell.setCellValue(ttt);
			cell.setCellStyle(style);
			if(checkItem!=null && checkItem.indexOf(i-1)>=0){
				cell.setCellStyle(styless);
			}
		}

		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(i + 2);
			row.setHeight((short) 350);
			cell = row.createCell((short) 0);
			cell.setCellValue(i + 1);
			cell.setCellStyle(styles);
			String[] strs = list.get(i);
			for (int j = 0; j < strs.length; j++) {
				String ttt = strs[j];
				cell = row.createCell((short) (j + 1));
				cell.setCellValue(ttt);
				cell.setCellStyle(styles);

			}

		}

		FileOutputStream fout = new FileOutputStream(filepath);
		wb.write(fout);
		fout.close();

		byte[] buffer = new byte[4096];
		BufferedOutputStream output = null;
		BufferedInputStream input = null;
		try {
			File file = new File(filepath);
			output = new BufferedOutputStream(resp.getOutputStream());
			input = new BufferedInputStream(new FileInputStream(file));
			int n = -1;
			while ((n = input.read(buffer, 0, 4096)) > -1) {
				output.write(buffer, 0, n);
			}
			output.flush();
			resp.flushBuffer();
		} catch (Exception e) {
		} finally {
			if (input != null)
				input.close();
			if (output != null)
				output.close();
		}
	}

	//多表导出
	@SuppressWarnings("deprecation")
	public void makeExcel(HttpServletRequest req, HttpServletResponse resp) throws Exception{
		resp.setContentType("application/vnd.ms-excel;");
		resp.setCharacterEncoding("UTF-8");
		resp.setHeader("Content-Disposition", "attachment;filename=\""
				+ new String(filename.getBytes(), "iso-8859-1") + ".xlsm\"");
		String sp = System.getProperty("file.separator");
		String filepath = req.getRealPath("/") + "excel" + sp + filename+".xlsm";
		XSSFWorkbook wb = new XSSFWorkbook(
				OPCPackage.open(req.getRealPath("/") + "excelmodel" + sp + filename+".xlsm")
		);

		XSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		XSSFFont font = wb.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		style.setWrapText(true);

		XSSFCellStyle styles = wb.createCellStyle();
		styles.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		styles.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		styles.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		styles.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		styles.setBorderRight(XSSFCellStyle.BORDER_THIN);
		styles.setBorderTop(XSSFCellStyle.BORDER_THIN);
		XSSFFont fonts = wb.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		styles.setFont(fonts);
		styles.setWrapText(true);

		/*设置必填标红-start*/
		XSSFCellStyle styless = wb.createCellStyle();
		styless.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		styless.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		styless.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		styless.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		styless.setBorderRight(XSSFCellStyle.BORDER_THIN);
		styless.setBorderTop(XSSFCellStyle.BORDER_THIN);
		//创建一种字体
		XSSFFont fontt=wb.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		fontt.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		fontt.setColor(XSSFFont.COLOR_RED);
		styless.setFont(fontt);
		styless.setWrapText(true);
		/*设置必填标红-end*/

		if(sheets.length>0){
			for(int m=0;m<sheets.length;m++){
				XSSFSheet sheet = wb.getSheet(sheets[m]);
				//sheet.addMergedRegion(new CellRangeAddress(0,0,0,titlesm.get(m).length));
				for (int j = 0; j < titlesm.get(m).length; j++){
					sheet.setColumnWidth((short) j, (short) 5200);
				}

				XSSFRow row = sheet.createRow(0);
				row.setHeight((short) 500);
				XSSFCell cell = row.createCell((short) 0);
				cell.setCellValue(titlem[m]);
				cell.setCellStyle(style);
				for (int i = 1; i <= titlesm.get(m).length; i++) {
					cell.setCellValue(titlem[m]);
					cell = row.createCell((short) (i - 1));
					cell.setCellStyle(style);

				}

				row = sheet.createRow(1);
				row.setHeight((short) 500);
				cell = row.createCell((short) 0);
				cell.setCellValue("序号");
				cell.setCellStyle(style);
				for (int i = 0; i < titlesm.get(m).length; i++) {
					String ttt = titlesm.get(m)[i];
					cell = row.createCell((short) (i + 1));
					cell.setCellValue(ttt);
					cell.setCellStyle(style);
					if(checkItems!=null && checkItems.get(m).indexOf(i)>=0){
						cell.setCellStyle(styless);
					}
				}

				for (int i = 0; i < listm.get(m).size(); i++) {
					row = sheet.createRow(i + 2);
					row.setHeight((short) 350);
					cell = row.createCell((short) 0);
					cell.setCellValue(i + 1);
					cell.setCellStyle(styles);
					String[] strs = listm.get(m).get(i);
					for (int j = 0; j < strs.length; j++) {
						String ttt = strs[j];
						cell = row.createCell((short) (j + 1));
						cell.setCellValue(ttt);
						cell.setCellStyle(styles);

					}
				}

			}
			FileOutputStream fout = new FileOutputStream(filepath);
			wb.write(fout);
			fout.close();

			byte[] buffer = new byte[4096];
			BufferedOutputStream output = null;
			BufferedInputStream input = null;
			try {
				File file = new File(filepath);
				output = new BufferedOutputStream(resp.getOutputStream());
				input = new BufferedInputStream(new FileInputStream(file));
				int n = -1;
				while ((n = input.read(buffer, 0, 4096)) > -1) {
					output.write(buffer, 0, n);
				}
				output.flush();
				resp.flushBuffer();
			} catch (Exception e) {
			} finally {
				if (input != null)
					input.close();
				if (output != null)
					output.close();
			}
		}

	}

	/**
     * Excel column index begin 1
     * @param colStr
     * @param length
     * @return
     */
    public static int excelColStrToNum(String colStr, int length) {
        int num = 0;
        int result = 0;
        for(int i = 0; i < length; i++) {
            char ch = colStr.charAt(length - i - 1);
            num = ch - 'A' + 1;
            num *= Math.pow(26, i);
            result += num;
        }
        return result;
    }

    /**
     * Excel column index begin 1
     * @param columnIndex
     * @return
     */
    public static String excelColIndexToStr(int columnIndex) {
        if (columnIndex <= 0) {
            return null;
        }
        String columnStr = "";
        columnIndex--;
        do {
            if (columnStr.length() > 0) {
                columnIndex--;
            }
            columnStr = ((char) (columnIndex % 26 + (int) 'A')) + columnStr;
            columnIndex = (columnIndex - columnIndex % 26) / 26;
        } while (columnIndex > 0);
        return columnStr;
    }






	/**
	 * eos bean  没有 get/set方法
	 * @param excelConfig
	 * @param values
	 * @param bean
	 * @param startIndex
	 * @return
	 * @throws IntrospectionException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws ParseException
	 */
	public static <T> T convertToBean(ExcelConfig excelConfig,
										 String[] values, T bean, int startIndex) throws Exception {
		Object beanInstance = null;
		try {
			beanInstance = bean.getClass().newInstance();
		} catch (InstantiationException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		//bean.getClass().getField("name").set(obj, value);

		List<ExcelFieldModel> val = excelConfig.getConfigModel().getFields();
		for(int i = 0;i<val.size();i++){
			ExcelFieldModel model  = val.get(i);
			/*Field[] fs=bean.getClass().getDeclaredFields();
			Field[] fs1=bean.getClass().getFields();*/
			for(int j=0;j<bean.getClass().getDeclaredFields().length;j++){
				Field f=bean.getClass().getDeclaredFields()[j];
				String fieldName=f.getName();
				if(model.getName().toString().equals(fieldName)){
					// 获得属性的首字母并转换为大写，与setXXX对应
					String firstLetter = fieldName.substring(0, 1).toUpperCase();
					String setMethodName = "set" + firstLetter
							+ fieldName.substring(1);
					//转换格式
					//setMethodName=initcap(setMethodName);
					Method setMethod = bean.getClass().getMethod(setMethodName,
                            f.getType());
					Object value = values[i+startIndex];
					//数据转换
					if(StringUtils.isEmpty((String) value) && StringUtils.isNotEmpty(model.getDefaultVal())){
						value = model.getDefaultVal();
					}
					if("".equals(value)){
						value = null;//不要把数据库里的null替换为“”
					}

					if(model.isConvert()){//convert校验
						if(StringUtils.isNotEmpty((String) value)){
							value = model.getConvertImportData((String)value);
							//导入时，缓存数据需要实时从库中刷新；
//							model.getConvertClass().dispose();
						}
					}else if(model.isInt()){
						if(StringUtils.isNotEmpty((String) value)){
							value = Integer.valueOf(value.toString());
						}else{
							value=0;
						}
					}else if(model.isDouble() ){
						if( StringUtils.isNotEmpty((String) value)){
							value = Double.valueOf(value.toString());
						}else{
							value=0.0;
						}

					}else if(model.isDate()){
						if(StringUtils.isNotEmpty((String) value)) {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							value = sdf.parse((String) value);
						}
					}
					try {
						//反射值到对象上
						setMethod.invoke(beanInstance, value);//调用对象的setXXX方法
					}catch (Exception e){
						throw new Exception("请检查excelmodel.xml文件中的数据类型是否和实体类的数据类型一致");
					}
					break;
				}
			}


		}
		return (T) beanInstance;
	}

	/**
	 * 功能：将输入字符串的首字母改成大写
	 * @param str
	 * @return
	 */
	private static String initcap(String str) {
		int[] num=new int[str.length()];
		char[] ch = str.toCharArray();
		/*if(ch[0] >= 'a' && ch[0] <= 'z'){
			ch[0] = (char)(ch[0] - 32);
		}*/
		for (int i = 0; i < ch.length; i++) {
			if (ch[i]=='_') {
				num[i]=i+1;
				if(ch[num[i]] >= 'a' && ch[num[i]] <= 'z'){
					ch[num[i]] = (char)(ch[num[i]] - 32);
				}
			}

		}

		String newStr=new String(ch).replaceAll("_", "");
		return newStr;
	}
}
