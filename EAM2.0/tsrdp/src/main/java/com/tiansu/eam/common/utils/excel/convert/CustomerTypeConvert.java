package com.tiansu.eam.common.utils.excel.convert;

import java.util.HashMap;
import java.util.Map;

public class CustomerTypeConvert  implements IConvertClass {

	public Map<String, Object> getConvertDataMap() {
		Map<String, Object> convertMap = new HashMap<String, Object>();
		convertMap.put("1", "重点客户");
		convertMap.put("0", "一般客户");
		return convertMap;
	}

	public void dispose() {
		// TODO 自动生成的方法存根
		
	}
}

