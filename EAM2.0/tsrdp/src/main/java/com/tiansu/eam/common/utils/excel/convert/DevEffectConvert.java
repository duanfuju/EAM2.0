package com.tiansu.eam.common.utils.excel.convert;

import java.util.HashMap;
import java.util.Map;

public class DevEffectConvert  implements IConvertClass {

	public Map<String, Object> getConvertDataMap() {
		Map<String, Object> convertMap = new HashMap<String, Object>();
		convertMap.put("1", "有效");
		convertMap.put("0", "无效");
		return convertMap;
	}

	public void dispose() {
		// TODO 自动生成的方法存根
		
	}
}
