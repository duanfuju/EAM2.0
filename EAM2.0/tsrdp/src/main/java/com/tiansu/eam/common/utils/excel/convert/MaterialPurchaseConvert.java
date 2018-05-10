package com.tiansu.eam.common.utils.excel.convert;

import java.util.HashMap;
import java.util.Map;

public class MaterialPurchaseConvert  implements IConvertClass {

	public Map<String, Object> getConvertDataMap() {
		Map<String, Object> convertMap = new HashMap<String, Object>();
		convertMap.put("0", "自购");
		convertMap.put("1", "统购");
		return convertMap;
	}

	public void dispose() {
		// TODO 自动生成的方法存根
		
	}
}
