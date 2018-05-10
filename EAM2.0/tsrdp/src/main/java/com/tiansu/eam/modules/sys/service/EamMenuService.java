package com.tiansu.eam.modules.sys.service;

import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.sys.dao.EamMenuDao;
import com.tiansu.eam.modules.sys.entity.Menu;
import com.tiansu.eam.modules.sys.entity.MenuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by tiansu on 2017/7/13.
 */

@Service
@Transactional(readOnly = true)
public class EamMenuService extends CrudService<EamMenuDao, Menu> {
    @Autowired
    private EamMenuDao eamMenuDao;

    List<Menu> menuList = null;
    public List<Menu> getMenuList(){
        menuList = eamMenuDao.getMenuList(new Menu());
        return menuList;
    }

    List<MenuInfo> menuInfoList = null;
    public List<MenuInfo> getRelatedMenuList(){
        menuInfoList = eamMenuDao.getRelatedMenuList(new MenuInfo());
        return menuInfoList;
    }

}
