package com.tiansu.eam.modules.sys.service;

import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.sys.dao.EamFieldControlDao;
import com.tiansu.eam.modules.sys.entity.FieldControl;
import com.tiansu.eam.modules.sys.entity.MenuFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangjl on 2017/7/27.
 */
@Service
@Transactional(readOnly = true)
public class EamFieldControllService  extends CrudService<EamFieldControlDao,FieldControl> {

    @Autowired
    private EamFieldControlDao fieldControlDao;

    /**
     * 根据角色和菜单查询字段权限配置
     * @param rolecode
     * @param menuid
     * @return
     */
    public FieldControl getFieldControl(String rolecode,String menuid ){
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("rolecode",rolecode);
        paramMap.put("menuno",menuid);
        FieldControl fieldControl = fieldControlDao.getFieldControlDetail(paramMap);
        return fieldControl;
    }

    /**
     * 根据菜单id查询该菜单页面具有的所有字段
     * @param menuid
     * @return
     */
    public List<MenuFields> getMenuFields(String menuid) {
        List<MenuFields> menuFieldsList = fieldControlDao.getMenuFields(menuid);
        return menuFieldsList;
    }
}
