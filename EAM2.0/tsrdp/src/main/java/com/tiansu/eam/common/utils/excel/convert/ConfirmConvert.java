package com.tiansu.eam.common.utils.excel.convert;

import java.util.HashMap;
import java.util.Map;

public class ConfirmConvert  implements IConvertClass {

	public Map<String, Object> getConvertDataMap() {
		Map<String, Object> convertMap = new HashMap<String, Object>();
		convertMap.put("1", "是");
		convertMap.put("0", "否");
		return convertMap;
	}

	public void dispose() {
		// TODO 自动生成的方法存根
		
	}
}
