package com.tiansu.eam.common.utils.excel.model;

import com.tiansu.eam.common.utils.excel.ExcelConfigParser;
import com.tiansu.eam.common.utils.excel.ExcelUtil;
import com.tiansu.eam.common.utils.excel.convert.IConvertClass;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
public class ExcelFieldModel {

	/*	 1、字段顺序为导入导出excel的顺序
	 2、nullable用于校验非空，
	 3、type用于校验格式，支持（string,date,convert[入库需转存或需关联校验时]）
	 4、只有type是convert时convertid，ismult才有效
	 5、ismult用于标识该字段是否多条以逗号分隔  */

	private String name;
	private String showName;
	private String defaultVal;
	private String eosPropName;
	private boolean nullbale;
	private String type;
	private String convertid;
	private String parentColIndex;
	private boolean ismult;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShowName() {
		return showName;
	}
	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getDefaultVal() {
		return defaultVal;
	}
	public void setDefaultVal(String defaultVal) {
		this.defaultVal = defaultVal;
	}

	public String getParentColIndex() {
		return parentColIndex;
	}

	public void setParentColIndex(String parentColIndex) {
		this.parentColIndex = parentColIndex;
	}

	public String getEosPropName() {
		return eosPropName;
	}
	public void setEosPropName(String eosPropName) {
		this.eosPropName = eosPropName;
	}
	public boolean isNullbale() {
		return nullbale;
	}
	public void setNullbale(boolean nullbale) {
		this.nullbale = nullbale;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getConvertid() {
		return convertid;
	}
	public void setConvertid(String convertid) {
		this.convertid = convertid;
	}
	public boolean isIsmult() {
		return ismult;
	}
	public void setIsmult(boolean ismult) {
		this.ismult = ismult;
	}

	public boolean isInt(){
		return "int".equalsIgnoreCase(type) || "integer".equalsIgnoreCase(type);
	}
	public boolean isDouble(){
		return "double".equalsIgnoreCase(type);
	}

	public boolean isDate(){
		return "date".equalsIgnoreCase(type);
	}
	public boolean isDateTime(){
		return "datetime".equalsIgnoreCase(type);
	}
	public boolean isTimeStamp(){
		return "timestamp".equalsIgnoreCase(type);
	}

	public boolean isConvert(){
		return "convert".equalsIgnoreCase(type);
	}

	public IConvertClass getConvertClass(){
		IConvertClass convercls = null;
		if(StringUtils.isNotEmpty(convertid)){
			convercls = ExcelConfigParser.getConvertDateById(convertid);
		}
		if(convercls == null){
			throw new RuntimeException(name+":"+convertid+"转换类获取失败");
		}else{
			return convercls;
		}
	}

	public String checkData(String value,int index)throws Exception{
		StringBuffer errMsg = new StringBuffer();

		if(!nullbale && StringUtils.isEmpty(value)){
			return errMsg.append(ExcelUtil.excelColIndexToStr(index)+"列不能为空").toString();
		}
		if(StringUtils.isNotEmpty(value) && isDate()){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				 Calendar calendar = Calendar.getInstance();
				 //TODO 读取excel时如果日期格式不对或不是文本类型，读取出来的可能有误  如2017-05-19读成17-05-19；格式化也不报错误
			      Date date = sdf.parse(value);
			      // 对 calendar 设置为 date 所定的日期
			      calendar.setTime(date);
			      int year = calendar.get(Calendar.YEAR);
			      if (year <1990){
			    	  return errMsg.append(ExcelUtil.excelColIndexToStr(index)+"列日期格式不对["+value+"]").toString();
			      }
			} catch (ParseException e) {
				return errMsg.append(ExcelUtil.excelColIndexToStr(index)+"列日期格式不对").toString();
			}
		}else if(StringUtils.isNotEmpty(value) && type.equalsIgnoreCase("convert")){
			IConvertClass convertInstance = getConvertClass();
			if(ismult){
				for(String v : value.split(",")){
					if(!convertInstance.getConvertDataMap().containsValue(v)){
						return errMsg.append(ExcelUtil.excelColIndexToStr(index)+"列不存在").toString();
					}
				}
			}else{
				if(!convertInstance.getConvertDataMap().containsValue(value)){
					return errMsg.append(ExcelUtil.excelColIndexToStr(index)+"列不存在").toString();
				}
			}
		}



		return errMsg.toString();

	}

	/**
	 * 导出，取的value值
	 * @param key
	 * @return
	 */
	private String getConvertExportData(String key) throws Exception{
		if(StringUtils.isEmpty(key)){
			return "";
		}
		String returnData = "";
		IConvertClass convertInstance = getConvertClass();

		if(ismult){
			for(String v : key.split(",")){
				if(!convertInstance.getConvertDataMap().containsKey(v)){
//					throw new RuntimeException(key+"数据不存在");
					System.out.println("error:"+ key +"数据不存在");
					return "";
				}
				returnData = returnData+convertInstance.getConvertDataMap().get(v)+",";
			}
			if(returnData.length()>0){
				returnData = returnData.substring(0, returnData.length()-1);
			}
		}else{
			if(!convertInstance.getConvertDataMap().containsKey(key)){
				System.out.println("error:"+ key +"数据不存在");
//				throw new RuntimeException(key+"非法数据，数据不存在");
				return "";
			}
			returnData = (String) convertInstance.getConvertDataMap().get(key);
		}
		return returnData;
	}

	/**
	 * 导入，取的key值（id）
	 * @param value
	 * @return
	 */
	public String getConvertImportData(String value) throws Exception{
		if(StringUtils.isEmpty(value)){
			return value;
		}
		String returnData = "";
		IConvertClass convertInstance = getConvertClass();
		if(ismult){
			for(String v : value.split(",")){
				String returnv = getFirstKeyByValue(convertInstance.getConvertDataMap(),v);
				if(StringUtils.isNotEmpty(v)  && returnv == null){
					throw new RuntimeException("{"+v+"}数据不存在");
				}
				returnData = returnData + returnv+",";
			}
			if(returnData.length()>0){
				returnData = returnData.substring(0, returnData.length()-1);
			}
		}else{
			returnData = getFirstKeyByValue(convertInstance.getConvertDataMap(),value);
			if(StringUtils.isNotEmpty(value)  && returnData == null){
				throw new RuntimeException("{"+value+"}数据不存在");
			}
		}
		return returnData;
	}
	/**
	 * Map 根据value取key
	 * @param map
	 * @param value
	 * @return
	 */
	private String getFirstKeyByValue(Map<String,Object> map,String value){
		Iterator iter = map.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String,Object> entry = (Entry<String, Object>) iter.next();
			if(StringUtils.isNotEmpty(value) && value.equals(entry.getValue())){
				return entry.getKey();
			}
		}
		return null;

	}
	public String getExportData(Map<String,Object> dos)  throws Exception{
		Object value = dos.get(getName());

		if(isDateTime() && value!= null && value.toString().length()>0){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date d=sdf.parse(value.toString().trim());
			String date=sdf.format(d);
			return date;
		}else if(isDate() && value!= null &&value.toString().length()>0){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d=sdf.parse(value.toString().trim());
			String date=sdf.format(d);//  sdf.format(value);
			return date;
		}else if(isConvert()&& value!= null &&value!=""&&value.toString().length()>0){
			return getConvertExportData(dos.get(getName()).toString().trim());
		}else{
			if(value==null){
				return "";
			}else{
				return dos.get(getName()).toString().trim();
			}

		}
	}
}
