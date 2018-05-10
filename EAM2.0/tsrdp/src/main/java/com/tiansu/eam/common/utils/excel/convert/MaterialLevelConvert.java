package com.tiansu.eam.common.utils.excel.convert;

import java.util.HashMap;
import java.util.Map;

public class MaterialLevelConvert  implements IConvertClass {

private volatile Map<String, Object> convertMap = null;
	
	
	public Map<String, Object> getConvertDataMap() {
		if(convertMap != null){
			return convertMap;
		}
		convertMap = new HashMap<String, Object>();
		
		convertMap.put("0", "A类");
		convertMap.put("1", "B类");
		convertMap.put("2", "C类");
		return convertMap;
		
	}

	public void dispose() {
		// TODO 自动生成的方法存根
		convertMap = null;
	}
}
