package com.tiansu.eam.modules.sys.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.tiansu.eam.modules.sys.entity.FieldControl;
import com.tiansu.eam.modules.sys.entity.MenuFields;

import java.util.List;
import java.util.Map;

/**
 * Created by wangjl on 2017/7/27.
 */
@MyBatisDao
public interface EamFieldControlDao  extends CrudDao<FieldControl> {

    /**
     * 根据角色id和菜单id查询content json信息
     */

    public FieldControl getFieldControlDetail(Map map);

    public List<MenuFields> getMenuFields(String menuid);
}
