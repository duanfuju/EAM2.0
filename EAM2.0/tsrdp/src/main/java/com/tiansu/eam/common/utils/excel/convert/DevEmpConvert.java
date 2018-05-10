package com.tiansu.eam.common.utils.excel.convert;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.common.utils.excel.dao.ConvertDataDao;
import com.tiansu.eam.common.utils.excel.service.ConvertDataService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevEmpConvert implements IConvertClass{

    private volatile Map<String, Object> convertMap = null;
    private ConvertDataDao convertDataService= SpringContextHolder.getBean(ConvertDataService.class);

    public Map<String, Object> getConvertDataMap()  throws Exception{
        if(convertMap != null){
            return convertMap;
        }
        convertMap = new HashMap<String, Object>();

        Map<String, Object> dev_emp=new HashMap<String, Object>();//入参对象
        dev_emp.put("tabname", "ioms_user i inner join  eam_user_ext e on  i.loginname = e.loginname");
        dev_emp.put("id", "e.loginname");
        dev_emp.put("name", "i.realname");

        List<Map<String, Object>> row_datas=convertDataService.getConvertData(dev_emp);
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
