package com.tiansu.eam.modules.sys.controller;

import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.sys.dao.EamRoleDao;
import com.tiansu.eam.modules.sys.entity.FieldControl;
import com.tiansu.eam.modules.sys.entity.Role;
import com.tiansu.eam.modules.sys.service.EamRoleService;
import com.tiansu.eam.modules.sys.service.EamSystemService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by tiansu on 2017/7/22.
 */
@Controller
@RequestMapping(value = "${adminPath}/eam/role")
public class EamRoleController extends BaseController {
    @Autowired
    private EamSystemService eamSystemService;

    @Autowired
    private EamRoleDao eamRoleDao;
    @Autowired
    private EamRoleService eamRoleService;


    /**
     * @creator duanfuju
     * @createtime 2017/9/7 9:54
     * @description:
     * 根据菜单id查询表格、表单、查询区域的字段权限
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getfields")
    public Map getFields(){
        String menuno = getPara("menuno");
        Map<String,Object> result = getFieldsByMenuno(menuno);
        return result;
    }



    @ResponseBody
    @RequestMapping(value = {"getRole"})
    public String getRole(@RequestParam(required=false) String extId, HttpServletResponse response) {
        System.out.println(getPara("name"));
        Role r = new Role();
        r.setRolename(getPara("name"));
        Role role = eamRoleDao.getByName(r);
        return role.toString();
    }

    @ResponseBody
    @RequestMapping(value = {"getRolesList"})
    public List<Role> getRolesList(@RequestParam(required=false) String extId, HttpServletResponse response) {
        List<Role> roleList = eamRoleDao.findList(new Role());
        return roleList;
    }

    @ResponseBody
    @RequestMapping(value = {"listData"})
    public Map<String,Object> listData(HttpServletRequest request) {
        Map param = getFormMap();
        Map<String,Object> map = eamRoleService.dataTablePageMap(param);

        return map;
    }

    @RequiresPermissions("user")
    @RequestMapping(value = "roleForm")
    public String roleForm(Role role, Model model) {
        String rolecode = getPara("id");
        role.setRolecode(rolecode);
        role = eamRoleService.getByName(role);
        model.addAttribute("role", role);
        return "modules/eamsys/sysConfig/roleForm";
    }

    @RequiresPermissions("user")
    @RequestMapping(value = "roleMenuFieldForm")
    public String roleMenuFieldForm(Role role, Model model) {
        String rolecode = getPara("id");
        String rolename = getPara("name");
        role.setRolename(rolename);
        role.setRolecode(rolecode);
        FieldControl fieldControl = new FieldControl();
        fieldControl.setRolecode(rolecode);
        fieldControl.setRolename(rolename);
        model.addAttribute("fieldControl", fieldControl);
        return "modules/eamsys/sysConfig/roleMenuFieldForm";
    }

	@RequestMapping(value = "addDataScope", method = RequestMethod.POST)
    @ResponseBody
    public boolean save(){
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        boolean flag = eamSystemService.saveDataScope(getPara("roleCode"), getPara("data_scope"), getPara("custom_detail"));
        return flag;
    }

    /**
     * @creator caoh
     * @createtime 2017-9-19 16:31
     * @description: 获取角色下拉
     * @return
     */
    @RequestMapping(value={"getRolesSelect",""})
    @RequiresPermissions("user")
    @ResponseBody
    public List  getRolesSelect() {
        List roles = eamSystemService.roleSelect();
        return roles;
    }
}
