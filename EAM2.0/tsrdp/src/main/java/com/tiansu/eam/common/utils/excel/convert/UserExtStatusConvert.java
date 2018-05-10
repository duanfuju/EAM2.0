package com.tiansu.eam.common.utils.excel.convert;

import java.util.HashMap;
import java.util.Map;

public class UserExtStatusConvert implements IConvertClass {

	public Map<String, Object> getConvertDataMap() {
		Map<String, Object> convertMap = new HashMap<String, Object>();
		convertMap.put("0", "在岗");
		convertMap.put("1", "待岗");
		convertMap.put("2", "退休");
		convertMap.put("3", "离职");
		return convertMap;
	}

	public void dispose() {
		// TODO 自动生成的方法存根
		
	}
}
