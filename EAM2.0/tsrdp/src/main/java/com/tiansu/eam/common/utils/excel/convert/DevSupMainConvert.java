package com.tiansu.eam.common.utils.excel.convert;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.common.utils.excel.dao.ConvertDataDao;
import com.tiansu.eam.common.utils.excel.service.ConvertDataService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevSupMainConvert implements IConvertClass {

    private volatile Map<String, Object> convertMap = null;
    private ConvertDataDao convertDataService= SpringContextHolder.getBean(ConvertDataService.class);

    public Map<String, Object> getConvertDataMap()  throws Exception{
        if(convertMap != null){
            return convertMap;
        }
        convertMap = new HashMap<String, Object>();

        Map<String, Object> dev_sup_main=new HashMap<String, Object>();//入参对象
        dev_sup_main.put("tabname", "eam_supplier");
        dev_sup_main.put("id", "id");
        dev_sup_main.put("name", "supplier_code");

        List<Map<String, Object>> row_datas=convertDataService.getConvertData(dev_sup_main);
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
