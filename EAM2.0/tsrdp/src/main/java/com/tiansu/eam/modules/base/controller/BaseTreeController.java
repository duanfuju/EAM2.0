package com.tiansu.eam.modules.base.controller;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.base.entity.BaseTree;
import com.tiansu.eam.modules.base.service.BaseTreeService;
import com.tiansu.eam.modules.sys.entity.Dept;
import com.tiansu.eam.modules.sys.service.EamDeptService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author wangjl
 * @description 树结构生成controller
 * @create 2017-09-16 10:50
 **/
@Controller
@RequestMapping(value = "${adminPath}/common/tree")
public class BaseTreeController  extends BaseController {



    @Autowired
    private BaseTreeService eamTreeService;
    @Autowired
    private EamDeptService deptService;

    /**
     * 生成tree信息
     * @param title 页面标题
     * @param type 树类型
     * @param isMult 能否多选
     * @param leafOnly 是否只能选择叶子节点
     * @param model
     * @return
     */
    @RequestMapping(value = {"buildTree"})
    public String buildTree(String title,String type,boolean isMult,boolean leafOnly, Model model){
        List<Map<String, Object>> treeDataList = Lists.newArrayList();
        if(TREE_TYPE.ORG_DEPT_TREE.toString().toLowerCase() .equals(type) ){
            List<Dept> deptList = deptService.queryDeptList();
            if(deptList != null && deptList.size() != 0) {
                for(int i = 0; i < deptList.size(); i++) {
                    Dept dept = deptList.get(i);
                    if(dept != null && dept.getDeptno() != null && dept.getDeptname() != null) {
                        Map<String, Object> map = Maps.newHashMap();
                        map.put("id", dept.getDeptno());
                        map.put("pid", dept.getParentid());
                        map.put("name", dept.getDeptname());
                        map.put("text", dept.getDeptname());//ligerui的树节点需要
                        treeDataList.add(map);
                    }
                }
            }
        }
        String treeDataJson = "";
        if(treeDataList!=null){
            treeDataJson = JSONArray.toJSONString(treeDataList);
        }
        model.addAttribute("title", title);
        model.addAttribute("treeData", treeDataJson);
        model.addAttribute("isMult", isMult);
        model.addAttribute("leafOnly", leafOnly);
        return "modules/schedual/treeSelect";
    }





    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<BaseTree> list = eamTreeService.findList(new BaseTree());
        for (int i=0; i<list.size(); i++){
            BaseTree e = list.get(i);
            if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                map.put("pId", e.getParentId());
                map.put("name", e.getName());
                mapList.add(map);
            }
        }
        return mapList;
    }
}

enum TREE_TYPE {
    //组织结构树
    ORG_DEPT_TREE
}