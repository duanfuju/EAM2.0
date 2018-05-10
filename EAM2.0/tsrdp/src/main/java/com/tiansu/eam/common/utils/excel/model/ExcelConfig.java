package com.tiansu.eam.common.utils.excel.model;

import com.thinkgem.jeesite.common.utils.StringUtils;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.common.utils.excel.convert.IConvertClass;
import com.tiansu.eam.common.utils.excel.convert.ITreeConvertClass;

import java.util.*;

public class ExcelConfig {

	private ExcelConfigModel configModel;
	
	public ExcelConfig(ExcelConfigModel configModel){
		if(configModel==null){
			throw new RuntimeException("构造函数参数不能为空");
		}
		this.configModel = configModel;
	}
	
	
	public ExcelConfigModel getConfigModel(){
		return configModel;
	}

	/**
	 *
	 * @param excelDataList
	 * @return 合并数据库和excel中树形结构数据
	 * @throws Exception
	 */
	public Map<String,Object> initTreeCacheData(List<String[]> excelDataList) throws Exception {
		//只支持列表中仅一条树形结构的数据
		Map<String,Object> dbDataMap = new HashMap<>();
		if(configModel != null ){
			for(ExcelFieldModel fieldModel : configModel.getFields()){
				if(fieldModel.isConvert()){
					IConvertClass c = fieldModel.getConvertClass();
					String parentColIndex = fieldModel.getParentColIndex();
					if(StringUtils.isNotEmpty(parentColIndex)){
						ITreeConvertClass tc = (ITreeConvertClass) c;
						dbDataMap = c.getConvertDataMap();
						//预生成excel中的id,code并与库中数据合并
						Map<String,Object> excelPreGenIdDataMap = new HashMap();//存储excel中和数据库不重复的数据id及编码
						for(String[] values : excelDataList){
							String value = values[Integer.parseInt(parentColIndex)];
							if(!dbDataMap.containsValue(value)){
								excelPreGenIdDataMap.put(IdGen.randowNum(),value);
							}
						}
						//将excelPreGenIdDataMap中的数据放入缓存中
						Iterator iterator = excelPreGenIdDataMap.entrySet().iterator();
						while(iterator.hasNext()){
							Map.Entry entry = (Map.Entry) iterator.next();
							tc.pushCachedValue((String) entry.getKey(),entry.getValue());
							dbDataMap.put((String) entry.getKey(),entry.getValue());
						}


					}
				}
			}
		}
		return dbDataMap;
	}




	/**
	 * 释放convertpool内存资源
	 */
	public void dispose(){
		if(configModel != null ){
			for(ExcelFieldModel fieldModel : configModel.getFields()){
				if(fieldModel.isConvert()){
					IConvertClass c = fieldModel.getConvertClass();
					c.dispose();
				}
			}
		}
		
	}
}
