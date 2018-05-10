package com.tiansu.eam.common.utils.excel.convert;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.common.utils.excel.service.ConvertDataService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchedualTypeCodeConvert implements IConvertClass {

	private volatile Map<String, Object> convertMap = null;

	private ConvertDataService convertDataService=SpringContextHolder.getBean(ConvertDataService.class);

	public Map<String, Object> getConvertDataMap()  throws Exception{
		if(convertMap != null){
			return convertMap;
		}
		convertMap = new HashMap<String, Object>();

		Map<String, Object> param=new HashMap<String, Object>();//入参对象
		param.put("tabname", "eam_schedual_type");
		param.put("id", "id");
		param.put("name", "type_code");
		List<Map<String, Object>> row_datas=convertDataService.getConvertData(param);
		for(Map<String,Object> object : row_datas){
			convertMap.put(object.get("id").toString(), object.get("name"));
		}
		return convertMap;
	}

	public void dispose() {
		// TODO 自动生成的方法存根
		convertMap = null;
	}
}
