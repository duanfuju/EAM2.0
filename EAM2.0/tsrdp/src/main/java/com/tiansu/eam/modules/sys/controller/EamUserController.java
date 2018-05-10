package com.tiansu.eam.modules.sys.controller;

import com.alibaba.fastjson.JSON;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.tiansu.eam.modules.sys.entity.Menu;
import com.tiansu.eam.modules.sys.entity.MenuPage;
import com.tiansu.eam.modules.sys.entity.User;
import com.tiansu.eam.modules.sys.service.EamSystemService;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

//import com.tiansu.eam.modules.sys.entity.MenuTreeData;

/**
 * Created by zhangdf on 2017/7/20.
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/eamuser")
public class EamUserController extends BaseController{
    @Autowired
    private EamSystemService eamSystemService;
   
    @RequestMapping(value={"findMenupage",""})
    public Page<MenuPage> findMenupage(MenuPage menuPage,HttpServletRequest request, HttpServletResponse response,Model model){
        Page<MenuPage> page=eamSystemService.findMenuPage(new Page<MenuPage>(request,response),menuPage);
        model.addAttribute("page",page);
        return page;
    }


    @RequestMapping(value={"getLoginUser",""})
    @RequiresPermissions("user")
    @ResponseBody
    public User getLoginUser() {
        return UserUtils.getUser();
    }

    @RequestMapping(value={"getMenuTreeByLoginUser",""})
    @RequiresPermissions("user")
    @ResponseBody
    public List  getMenuTreeByLoginUser() {
//        String t = UserUtils.getDataScopeUserIds();
        List menuTreeData = Menu.convertMenuListToTree(UserUtils.getMenuList());
        System.out.println(JSON.toJSON(menuTreeData));
        return menuTreeData;
    }


//    /**
//     * @creator caoh
//     * @createtime 2017-9-19 16:31
//     * @description: 获取人员下拉
//     * @return
//     */
//    @RequestMapping(value={"getUsersSelect",""})
//    @RequiresPermissions("user")
//    @ResponseBody
//    public List  getUsersSelect(@RequestParam(value = "type",required = false) String type,
//                                @RequestParam(value = "taskId",required = false) String taskId) {
//        List users = eamSystemService.userSelect();
//
//        if(type!=null&&!("").equals(type)){
//            if(type.equals("faultOrder")){
//                for(Object user:users){
//                    String major= (String)(((Map)user).get("major"));
//                }
//            }
//        }
//
//        return users;
//    }
    /**
     * @creator caoh
     * @createtime 2017-9-19 16:31
     * @description: 获取人员下拉
      * @return
     */
    @RequestMapping(value={"getUsersSelect",""})
    @RequiresPermissions("user")
    @ResponseBody
    public List  getUsersSelect(@RequestParam(value = "type" ,required = false) String type,
                                @RequestParam(value = "businessKey",required = false) String businessKey) {

        List users = eamSystemService.userSelect();
        if(type!=null&&!("").equals(type)){
            if(type.equals("faultOrder")){
                if(businessKey!=null&&!("").equals(businessKey)) {
                    users = eamSystemService.getSameMajorBetweenUserAndDevice(users, businessKey);
                }
            }
        }
        return users;
    }

    /**
     * 查询用户信息(根据登录名或电话或邮箱)
     * @return
     */
    @RequestMapping(value={"queryUser",""})
    @RequiresPermissions("user")
    @ResponseBody
    public User queryUser(User user) {
        User ruser = eamSystemService.getByLoginName(user);
        return ruser;
    }

}

