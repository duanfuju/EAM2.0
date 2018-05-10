package com.tiansu.eam.common.utils.excel.convert;

import com.tiansu.eam.common.utils.excel.dao.ConvertDataDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class OrganizationConvert implements IConvertClass {
	private volatile Map<String, Object> convertMap = null;

	@Autowired
	private ConvertDataDao convertDataService;

	public Map<String, Object> getConvertDataMap()  throws Exception{
		if(convertMap != null){
			return convertMap;
		}
		convertMap = new HashMap<String, Object>();
		
		Map<String, Object> dev_param=new HashMap<String, Object>();//入参对象
		dev_param.put("tabname", "org_organization");//根据code查出ID
		dev_param.put("id", "orgid");
		dev_param.put("name", "orgcode");
		dev_param.put("cond", " find_in_set(orgtype,'hd,h,bz') ");
		List<Map<String, Object>> row_datas=convertDataService.getConvertData(dev_param);
		//Object [] row_datas = (Object[]) DatabaseExt.queryByNamedSql("default", "com.primeton.eos.pub.getSelectDatas.getConvertData", dev_param);


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
