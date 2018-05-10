package com.tiansu.eam.common.utils.excel.convert;

import java.util.HashMap;
import java.util.Map;

public class SupplierXyConvert  implements IConvertClass {

	public Map<String, Object> getConvertDataMap() {
		Map<String, Object> convertMap = new HashMap<String, Object>();
		convertMap.put("AAA", "优秀");
		convertMap.put("AA", "良好");
		convertMap.put("A", "较好");
		convertMap.put("BBB", "一般");
		convertMap.put("BB", "较差");
		convertMap.put("B", "差");
		return convertMap;
	}

	public void dispose() {
		// TODO 自动生成的方法存根
		
	}
}