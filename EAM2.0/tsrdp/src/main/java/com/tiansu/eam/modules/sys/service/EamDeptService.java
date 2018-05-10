package com.tiansu.eam.modules.sys.service;

import com.thinkgem.jeesite.common.service.TreeService;
import com.tiansu.eam.modules.sys.dao.EamDeptDao;
import com.tiansu.eam.modules.sys.entity.Dept;
import com.tiansu.eam.modules.sys.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by tiansu on 2017/7/13.
 */

@Service
@Transactional(readOnly = true)
public class EamDeptService extends TreeService<EamDeptDao, Dept> {

    @Autowired
    private EamDeptDao deptDao;
    List<Dept> deptList = null;
    @Transactional(readOnly = true)
    public List<Dept> queryDeptList(){
        deptList = deptDao.queryDeptList(new Dept());
        return deptList;
    }

    @Transactional(readOnly = true)
    public List<Dept> getAllDeptList(){
        deptList = deptDao.findAllList(new Dept());
        return deptList;
    }

    @Transactional(readOnly = true)
    public Dept getDept(Dept dept){
            return deptDao.getDept(dept);
    }

    public List<User> getUserByDept(Dept dept){
        List<User> userList = deptDao.getUserByDept(dept);
        return userList;
    }

    public List<Dept> queryDeptListByIds(Map<String,String> paramMap) {
        return deptDao.queryDeptListByIds(paramMap);
    }
}
