package com.tiansu.eam.common.utils.excel.convert;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.tiansu.eam.common.utils.excel.dao.ConvertDataDao;
import com.tiansu.eam.common.utils.excel.service.ConvertDataService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wujh
 * @description 标准工作数据转换类
 * @create 2017-09-21 20:44
 **/
public class OperationworkConvert implements IConvertClass {
    private volatile Map<String, Object> convertMap = null;

    private ConvertDataDao convertDataService= SpringContextHolder.getBean(ConvertDataService.class);
    @Override
    public Map<String, Object> getConvertDataMap() throws Exception {
        if(convertMap != null){
            return convertMap;
        }
        convertMap = new HashMap<String, Object>();

        Map<String, Object> operationwork_param = new HashMap<String, Object>();//入参对象
        operationwork_param.put("tabname", "eam_operation_work");//根据code查出ID
        operationwork_param.put("id", "id");
        operationwork_param.put("name", "operationwork_code");

        List<Map<String, Object>> row_datas=convertDataService.getConvertData(operationwork_param);

        for(Map<String,Object> object : row_datas){

            convertMap.put(object.get("id").toString(), object.get("name"));

        }

        return convertMap;
    }

    @Override
    public void dispose() {

    }
}
