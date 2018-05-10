package com.tiansu.eam.common.utils.excel.convert;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.common.utils.excel.dao.ConvertDataDao;
import com.tiansu.eam.common.utils.excel.service.ConvertDataService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wujh
 * @description 标准工作类型
 * @create 2017-09-22 9:19
 **/
public class OperationworkTypeConvert implements IConvertClass {
    private volatile Map<String, Object> convertMap = null;

    private ConvertDataDao convertDataService= SpringContextHolder.getBean(ConvertDataService.class);
    @Override
    public Map<String, Object> getConvertDataMap() throws Exception {
        if(convertMap != null){
            return convertMap;
        }
        convertMap = new HashMap<String, Object>();

        Map<String, Object> operationworkType_param = new HashMap<String, Object>();//入参对象
        operationworkType_param.put("tabname", "eam_dict");//根据code查出ID
        operationworkType_param.put("id", "dict_value");
        operationworkType_param.put("name", "dict_name");
        operationworkType_param.put("cond", " dict_type_code = 'operationwork_type'");

        List<Map<String, Object>> row_datas=convertDataService.getConvertData(operationworkType_param);

        for(Map<String,Object> object : row_datas){

            convertMap.put(object.get("id").toString(), object.get("name"));

        }

        return convertMap;
    }

    @Override
    public void dispose() {

    }
}
