package com.tiansu.eam.interfaces.device.controller;


import com.alibaba.fastjson.JSON;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.tiansu.eam.common.web.BaseController;
import com.tiansu.eam.interfaces.device.entity.DataUtils;
import com.tiansu.eam.interfaces.resulthelper.APPResponseBody;
import com.tiansu.eam.modules.device.service.EamDevCategoryService;
import com.tiansu.eam.modules.device.service.EamDevLocService;
import com.tiansu.eam.modules.device.service.EamDevSparePartsService;
import com.tiansu.eam.modules.device.service.EamDeviceService;
import com.tiansu.eam.modules.sys.entity.CodeEnum;
import com.tiansu.eam.modules.sys.utils.SequenceUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @creator wangr
 * @create 2017/9/27 15:00
 * @description 设备相关接口
 */
@Path("/{tenant}/device")
@Controller
public class DeviceRestController extends BaseController {

    public static EamDevCategoryService eamDevCategoryService = SpringContextHolder.getBean(EamDevCategoryService.class);
    public static EamDevSparePartsService eamDevSparePartsService = SpringContextHolder.getBean(EamDevSparePartsService.class);

    public static EamDeviceService eamDeviceService = SpringContextHolder.getBean(EamDeviceService.class);
    public static EamDevLocService eamDevLocServicem = SpringContextHolder.getBean(EamDevLocService.class);

    /**
     * @creator wangr
     * @create 2017/9/27 15:00
     * @description
     */
    @Path("/get")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public @ResponseBody
    String get(@Context HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Map param = getFormMap();
        Map<String, Object> map = eamDeviceService.dataTablePageMap(param);
        APPResponseBody responseBody = new APPResponseBody("1", DataUtils.sucCode, DataUtils.sucMsg, map);
        return JSON.toJSONString(responseBody);
    }

    @Path("/getById")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public @ResponseBody
    String getById(@QueryParam("devId") String devId,
                   @Context HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Map<String, Object> map = eamDeviceService.getEdit(devId);
        APPResponseBody responseBody = new APPResponseBody("1", DataUtils.sucCode, DataUtils.sucMsg, map);
        return JSON.toJSONString(responseBody);
    }

    /**
     * 查询设备下的备品备件或者工器具 typeFlag类型标识：0备品备件  1工器具
     *
     * @return
     */
    @Path("/getDevMaterials")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public @ResponseBody
    String getSpareParts(@QueryParam("devId") String devId,
                         @QueryParam("pageSize") int pageSize,
                         @QueryParam("typeFlag") String typeFlag,
                         @QueryParam("keyWord") String keyWord,
                         @Context HttpServletResponse response) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        Map param = super.getFormMap();
        if (devId != null && devId != "") {
            param.put("dev_id", devId);
        }
        if (pageSize != 0) {
            param.put("length", pageSize);
        }
        if (typeFlag != null && typeFlag != "") {
            param.put("type_flag", typeFlag);
        }
        if (keyWord != null && keyWord != "") {
            param.put("key_word", keyWord);
        }
        Map<String, Object> map = eamDevSparePartsService.dataTablePageMapApp(param);
        APPResponseBody responseBody = new APPResponseBody("1", DataUtils.sucCode, DataUtils.sucMsg, map);
        return JSON.toJSONString(responseBody);
    }
//
//    /**
//     * 查询设备下的工器具
//     * @return
//     */
//    @Path("/getTools")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public @ResponseBody String getTools(@QueryParam("devId") String devId, @QueryParam("pageSize") int pageSize,@QueryParam("typeFlag") String typeFlag) {
//        Map param = super.getFormMap();
//        if(devId != null && devId != ""){
//            param.put("dev_id", devId);
//        }
//        if(pageSize != 0){
//            param.put("length", pageSize);
//        }
//        if(typeFlag != null && typeFlag != ""){
//            param.put("type_flag", typeFlag);
//        }
//        Map<String, Object> map = eamDevSparePartsService.dataTablePageMapApp(param);
//        APPResponseBody responseBody = new APPResponseBody(DataUtils.sucCode, DataUtils.sucMsg, map);
//        return JSON.toJSONString(responseBody);
//    }

    /**
     * 获取下一级的位置信息
     * @param locationId
     * @param response
     * @return
     */
    @Path("/getLocation")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public @ResponseBody
    String getDevLocation(@QueryParam("devLocId") String locationId,
                          @Context HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        //查根目录时先转换
        if(locationId.equals("0")){
            locationId = "";
        }
        Map<String, Object> map = eamDevLocServicem.findByLocId(locationId);
        APPResponseBody responseBody = new APPResponseBody("1", DataUtils.sucCode, DataUtils.sucMsg, map);
        return JSON.toJSONString(responseBody);
    }

    /**
     * 根据空间编码获取相关信息—首页扫码
     * @param devLocId
     * @param response
     * @return
     */
    @Path("/getDevices")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public @ResponseBody
    String getDevices(@QueryParam("devLocId") String devLocId,
                      @Context HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Map<String, Object> map = eamDeviceService.getDevices(devLocId);
        APPResponseBody responseBody = new APPResponseBody("1", DataUtils.sucCode, DataUtils.sucMsg, map);
        return JSON.toJSONString(responseBody);
    }

    public void test() {
        SecurityUtils.getSubject().isPermitted("sys:user:edit");
        DictUtils.getDictLabel("", "", "");
    }


    /**
     * 获取故障现象
     * @param devId
     * @param response
     * @return
     */
    @Path("/getFaultAppearance")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public @ResponseBody
    String getFaultAppearance(@QueryParam("devId") String devId,
                              @Context HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        List<Map> map = eamDeviceService.getFaultAppearance(devId);
        APPResponseBody responseBody = new APPResponseBody("1", DataUtils.sucCode, DataUtils.sucMsg, map);
        return JSON.toJSONString(responseBody);
//        String res = map.toString();
//        return res;
    }

    /**
     * 获取故障编码
     * @param response
     * @return
     */
    @Path("/getFaultCode")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public @ResponseBody
    String getFaultCode(@Context HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");

        Map<String, Object> map = new HashedMap();
        String code = SequenceUtils.getBySeqType(CodeEnum.valueOf("FAULT_ORDER"));
        map.put("code",code);
        APPResponseBody responseBody = new APPResponseBody("1", DataUtils.sucCode, DataUtils.sucMsg, map);
        return JSON.toJSONString(responseBody);
    }

    /**
     * 根据设备或者位置获取上级位置节点直到根目录
     * @param devId
     * @param devLocId
     * @param response
     * @return
     */
    @Path("/getAllLoc")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public @ResponseBody
    String getAllLoc(@QueryParam("devId") String devId,
                        @QueryParam("devLocId") String devLocId,
                        @Context HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Map param = super.getFormMap();
        if (devId != null && devId != "") {
            Map<String, Object> map = eamDeviceService.getEdit(devId);

            param.put("dev_Loc_id", map.get("dev_location"));
        }
        if (devLocId != null && devLocId != "") {
            param.put("dev_Loc_id", devLocId);
        }
        Map<String, Object> map = eamDeviceService.getAllLoc(param);
        APPResponseBody responseBody = new APPResponseBody("1", DataUtils.sucCode, DataUtils.sucMsg, map);
        return JSON.toJSONString(responseBody);
    }

    /**
     * 更新二维码粘贴状态
     * @param devId
     * @param response
     * @return
     */
    @Path("/updateQrcode")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public @ResponseBody
    String updateQrcode(@QueryParam("devId") String devId,@Context HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", "*");
        Map param = super.getFormMap();
        param.put("id",devId);
        eamDeviceService.updateQrcode(param);
        APPResponseBody responseBody = new APPResponseBody();
        return JSON.toJSONString(responseBody);
    }
}