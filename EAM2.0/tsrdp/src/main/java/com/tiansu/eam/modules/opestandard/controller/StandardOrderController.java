package com.tiansu.eam.modules.opestandard.controller;

import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.modules.act.service.EamProcessService;
import com.tiansu.eam.modules.opestandard.entity.StandardOrder;
import com.tiansu.eam.modules.opestandard.service.StandardOrderService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangww
 * @description  标准工单
 * @create 2017-10-09 10:01
 **/
@Controller
@RequestMapping(value = "${adminPath}/opestandard/standardOrder")
public class StandardOrderController extends BaseController {
    @Autowired
    private StandardOrderService standardOrderService;
    @Autowired
    private EamProcessService eamProcessService;//流程
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = {"listData"})
    public Map<String,Object> listData() {
        Map param = getFormMap();
        Map<String,Object> map = standardOrderService.dataTablePageMap(param);

        return map;
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = {"getOrderwork"})
    public List<Map> getOrderwork(){
        return standardOrderService.getOrderwork();
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = {"getMajorByWork"})
    public List<Map> getMajorByWork(){
        String ids=getPara("workid");
        String []array=ids.split(";");
        if(ids !=null && !"".equals(ids)){
            Map map=new HashMap();
            map.put("ids",array);
            return standardOrderService.getMajorByWork(map);
        }
        return null;
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = {"getDevinfo"})
    public List<Map> getDevinfo(){
        String ids=getPara("workid");
        String []array=ids.split(";");
        if(ids !=null && !"".equals(ids)){
            Map map=new HashMap();
            map.put("ids",array);
        return standardOrderService.getDevinfo(map);
        }
        return null;
    }
    /**
     * @creator zhangww
     * @createtime 2017/10/9 11:20
     * @description:新增
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "addUI")
    public String addUI() {

        return "modules/opestandard/standardOrderFormAdd";
    }

    /**
     * @creator zhangww
     * @createtime 2017/10/9 11:23
     * @description: 编辑
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "editUI")
    public String editUI() {

        return "modules/opestandard/standardOrderFormEdit";

    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "insert")
    public String insert(StandardOrder standardOrder,HttpServletRequest request) {
        if(standardOrder.getOrder_code() !=null || !"".equals(standardOrder.getOrder_code())){
            int cou=standardOrderService.getBycode(standardOrder.getOrder_code());//判断编码是否重复
            if(cou>0){
                return "repeat";
            }
        }
        standardOrder.preInsert();
        String uuid=standardOrder.getId();
        String pstid = eamProcessService.startProcessByPdid("stand_order","eam_operation_orders",uuid,request);
        if("timeout".equals(pstid)){
            return "timeout";
        }
        standardOrder.setApprove_status("0");
        standardOrder.setPstid(pstid);
        standardOrderService.insert(standardOrder);
        return "success";
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "update")
    public String update(StandardOrder standardOrder) {
        standardOrderService.update(standardOrder);
        return "success";
    }
    /**
     * 获取编辑页面字段数据
     * @param
     * @return
     */
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "editObj")
    public StandardOrder editObj() {
        String id = getPara("id");
        return standardOrderService.getEdit(id);
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "delete")
    @Transactional
    public String delete(){
        //批量删除，单个删除
        String ids = getPara("id");
        StandardOrder standardOrder=standardOrderService.getEdit(ids);
        String status=standardOrder.getOrder_status();
        String approve_status=standardOrder.getApprove_status();
        if("1".equals(status)){
            return "nodele";
        }
        if(!"0".equals(approve_status)){
            return "nodelestatus";
        }
        standardOrderService.deleteByids(ids);
        return "success";
    }
    /**@creator zhangww
     * @createtime 2017/8/22 9:14
     * @description:详情页面
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "detailUI")
    public String detailUI() {

        return "modules/opestandard/standardOrderFormDeta";
    }
    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "getOrderByPIid")
    public Map getOrderByPIid(){
        String pIid=getPara("pIid");
        return standardOrderService.getLibByPIid(pIid);
    }

    /**
     * 获取所有审批通过的标准工单编码（共系统需要查找标准工作时下拉使用）
     * @modifier wangjl
     * @modifytime 2017/10/20 16:37
     * @modifyDec:
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getApprovedOrderCodes")
    public List<Map> getApprovedOrderCodes(){
        Map paramMap = new HashMap();
        paramMap.put("approve_status",1);
        List<Map> findListByMap = standardOrderService.getAllCodes(paramMap);
        return findListByMap;
    }

}

