package com.tiansu.eam.modules.sys.service;

import com.tiansu.eam.common.service.CrudService;
import com.tiansu.eam.modules.sys.dao.EamRoleDao;
import com.tiansu.eam.modules.sys.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 角色service
 * Created by tiansu on 2017/7/25.
 */
@Service
@Transactional(readOnly = true)
public class EamRoleService extends CrudService<EamRoleDao, Role>{

    @Autowired
    private EamRoleDao eamRoleDao;

    @Transactional(readOnly = true)
    public Map dataTablePage(Role role) {
        return super.dataTablePage(role);

    }

    @Transactional(readOnly = true)
    public List<Role> findRole(Role role){
        return eamRoleDao.findList(role);
    }

    /**
     * 根据角色编号获取角色信息
     * @param role
     * @return
     */
    @Transactional(readOnly = true)
    public Role getByName(Role role) {
        return eamRoleDao.getByName(role);
    }

    /**
     * 根据角色编号获取数据范围信息
     * @param role
     * @return
     */
    public Role getDataScopeByName(Role role) {
        return eamRoleDao.getDataScopeByName(role);
    }
}
