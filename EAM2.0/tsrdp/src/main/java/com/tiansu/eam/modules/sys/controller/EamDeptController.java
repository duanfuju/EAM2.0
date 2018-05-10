package com.tiansu.eam.modules.sys.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.sys.dao.EamUserDao;
import com.tiansu.eam.modules.sys.entity.Button;
import com.tiansu.eam.modules.sys.entity.Dept;
import com.tiansu.eam.modules.sys.entity.User;
import com.tiansu.eam.modules.sys.service.EamButtonService;
import com.tiansu.eam.modules.sys.service.EamDeptService;
import com.tiansu.eam.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tiansu on 2017/7/19.
 */
@Controller
@RequestMapping(value = "${adminPath}/eam/dept")
public class EamDeptController extends BaseController {
    @Autowired
    private EamDeptService deptService;
    @Autowired
    private EamButtonService eamButtonService;

    @Autowired
    private EamUserDao eamUserDao;
    /**
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<Dept> deptList = deptService.queryDeptList();
        if(deptList != null && deptList.size() != 0) {
            for(int i = 0; i < deptList.size(); i++) {
                Dept dept = deptList.get(i);
                if(dept != null && dept.getDeptno() != null && dept.getDeptname() != null) {
                    Map<String, Object> map = Maps.newHashMap();
                    map.put("id", dept.getDeptno());
                    map.put("pId", dept.getParentid());
                    map.put("name", dept.getDeptname());
                    map.put("text", dept.getDeptname());//ligerui的树节点需要
                    map.put("type", "dept");
                    mapList.add(map);
                }
            }
        }
        System.out.println(mapList);
        return mapList;
    }

    /**
     * 获取部门人员树
     * @param extId
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "deptUserTreeData")
    public List<Map<String, Object>> deptUserTreeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
        List<Map<String, Object>> mapList = treeData(extId,response);
        List<User> eamUserList = eamUserDao.findAllList();
        if(eamUserList != null && eamUserList.size() != 0) {
            for(int i = 0; i < eamUserList.size(); i++) {
                User user = eamUserList.get(i);
                if(user != null) {
                    Map<String, Object> map = Maps.newHashMap();
                    map.put("id", user.getLoginname());
                    map.put("pId", user.getUserdeptno());
                    map.put("name", user.getRealname());
                    map.put("text", user.getRealname());//ligerui的树节点需要
                    map.put("type", "user");
                    mapList.add(map);
                }
            }
        }
        return mapList;
    }


    @ResponseBody
    @RequestMapping(value = "buttonList")
    public List<Button> getButtonList (){
        List<Button> buttonList = UserUtils.getButtonList();
        return buttonList;
    }

    @ResponseBody
    @RequestMapping(value = "getAllButtonList")
    public List<Button> getAllButtonList (){
        List<Button> buttonList = eamButtonService.findList();
        return buttonList;
    }

    @RequestMapping(value={"getUserByDept",""})
    @ResponseBody
    /**
     * 查询某个部门下的用户
     */
    public List<User> getUserByDept(Dept dept) {
        List<User> users = deptService.getUserByDept(dept);
        return users;
    }


    /**
     * 获取当前登录用户所拥有的数据权限的部门信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"getDataScopeDepts"})
    public Map<String,String> getDataScopeDepts(){
        String deptIds = UserUtils.getDataScopeDeptIds();
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("deptIds",deptIds);
        List<Dept> depts = deptService.queryDeptListByIds(paramMap);
        Map result = new HashMap();
        JSONArray dataArray = new JSONArray();
        for(Dept dept : depts){
            JSONObject deptObj = new JSONObject();
            deptObj.put("deptno",dept.getDeptno());
            deptObj.put("deptname",dept.getDeptname());
            dataArray.add(deptObj);
        }
        result.put("data",dataArray);
        return result;
    }



}
