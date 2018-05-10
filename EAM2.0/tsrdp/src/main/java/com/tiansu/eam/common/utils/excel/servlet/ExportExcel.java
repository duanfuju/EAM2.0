package com.tiansu.eam.common.utils.excel.servlet;

import com.tiansu.eam.common.utils.excel.ExcelConfigParser;
import com.tiansu.eam.common.utils.excel.ExcelUtil;
import com.tiansu.eam.common.utils.excel.model.ExcelConfig;
import com.tiansu.eam.common.utils.excel.model.ExcelFieldModel;
import com.tiansu.eam.modules.device.service.EamDevCategoryService;
import com.tiansu.eam.modules.device.service.EamDevLocService;
import com.tiansu.eam.modules.device.service.EamDeviceService;
import com.tiansu.eam.modules.employee.service.EamCustomerService;
import com.tiansu.eam.modules.employee.service.EamUserExtService;
import com.tiansu.eam.modules.inspection.service.InspectionAreaService;
import com.tiansu.eam.modules.inspection.service.InspectionRouteService;
import com.tiansu.eam.modules.inspection.service.InspectionSubjectService;
import com.tiansu.eam.modules.inspection.service.InspectionTaskService;
import com.tiansu.eam.modules.maintain.service.MaintainProjectInfService;
import com.tiansu.eam.modules.maintain.service.MaintainProjectService;
import com.tiansu.eam.modules.maintain.service.MaintainProjectSubService;
import com.tiansu.eam.modules.material.service.MaterialInfoService;
import com.tiansu.eam.modules.material.service.MaterialTypeService;
import com.tiansu.eam.modules.opestandard.service.EamOperationWorkService;
import com.tiansu.eam.modules.opestandard.service.StandardLibService;
import com.tiansu.eam.modules.opestandard.service.StandardOrderService;
import com.tiansu.eam.modules.schedual.entity.SchedualType;
import com.tiansu.eam.modules.schedual.service.SchedualOrderService;
import com.tiansu.eam.modules.schedual.service.SchedualTypeService;
import com.tiansu.eam.modules.supplier.service.SupplierService;
import com.tiansu.eam.modules.supplier.service.SupplierTypeService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExportExcel extends HttpServlet{

	private  String exportConfigId = "equipInfo";
	private String sheet="Sheet1";


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(req.getSession().getServletContext());
        EamCustomerService eamCustomerService = (EamCustomerService) ctx.getBean("eamCustomerService");//客户信息
        EamUserExtService eamUserExtService = (EamUserExtService) ctx.getBean("eamUserExtService");//人员信息
        SupplierService supplierService = (SupplierService) ctx.getBean("supplierService");//供应商
        SupplierTypeService supplierTypeService = (SupplierTypeService) ctx.getBean("supplierTypeService");//供应商类型
        EamDevCategoryService eamDevCategoryService = (EamDevCategoryService) ctx.getBean("eamDevCategoryService");  //设备类别
        EamDeviceService eamDeviceService = (EamDeviceService) ctx.getBean("eamDeviceService");  //设备信息
        EamDevLocService eamDevLocService = (EamDevLocService) ctx.getBean("eamDevLocService");  //空间信息
        StandardLibService standardLibService = (StandardLibService) ctx.getBean("standardLibService");
        EamOperationWorkService eamOperationWorkService = (EamOperationWorkService) ctx.getBean("eamOperationWorkService"); //标准工作
        MaterialTypeService materialTypeService = (MaterialTypeService)ctx.getBean("materialTypeService");//物料类型
        MaterialInfoService materialInfoService = (MaterialInfoService)ctx.getBean("materialInfoService");//物料信息
        SchedualTypeService schedualTypeService=(SchedualTypeService)ctx.getBean("schedualTypeService");//排班表类型
        SchedualOrderService schedualOrderService=(SchedualOrderService)ctx.getBean("schedualOrderService");//排班表
        InspectionSubjectService inspectionSubjectService = (InspectionSubjectService)ctx.getBean("inspectionSubjectService");//巡检项
        InspectionAreaService inspectionAreaService = (InspectionAreaService)ctx.getBean("inspectionAreaService");//巡检区域
        StandardOrderService standardOrderService = (StandardOrderService)ctx.getBean("standardOrderService");//标准工单
        InspectionRouteService inspectionRouteService = (InspectionRouteService)ctx.getBean("inspectionRouteService");//巡检路线
        InspectionTaskService inspectionTaskService = (InspectionTaskService)ctx.getBean("inspectionTaskService");//巡检任务
        MaintainProjectService maintainProjectService = (MaintainProjectService)ctx.getBean("maintainProjectService");//保养年计划
        MaintainProjectSubService maintainProjectSubService = (MaintainProjectSubService)ctx.getBean("maintainProjectSubService");//保养月计划
        MaintainProjectInfService maintainProjectInfService = (MaintainProjectInfService)ctx.getBean("maintainProjectInfService");//保养设置

        resp.setContentType("application/x-excel");
        resp.setCharacterEncoding("UTF-8");
        Object ids = req.getParameter("ids");
        exportConfigId = req.getParameter("name");
        boolean type = Boolean.valueOf(req.getParameter("type"));//是否用于导入（下载模板） ture:使用，false:不使用
        String menuno =String.valueOf(req.getParameter("menuno"));//菜单编号
        List<Map<String,Object>> roleFields=null;
        List<String[]> list = new ArrayList<String[]>();
        List<Map> objc = null;
        List<Map> objFault = null;//检修标准
        List<Map> objMaintain = null;//保养标准
        List<Map> objPatrol = null;//巡检标准
        List<Map> objFail = null;//缺陷故障库
        List<Map> objOpe = null;//设备运行标准
        List<Map> objSafe = null;//设备安全标准
        List<Map> objcProcedure = null;   //标准工作工序
        List<Map> objcSafety = null;   //标准工作安全措施
        List<Map> objcTools = null;   //标准工作工器具
        List<Map> objcSpareparts = null;   //标准工作备品备件
        List<Map> objcPersonHours = null;   //标准工作人员工时
        List<Map> objcOthers = null;   //标准工作其他费用

        Map map = new HashMap();
        map.put("export", "export");//判断使用哪一个include,个别的导出sql需要调整
        if (ids == null || ids.equals("")) {//无参数的统一写在这边（全部导出）

            if (exportConfigId.equals("customer")) {//客户信息
                objc = eamCustomerService.findList(map);
            } else if (exportConfigId.equals("eamUserExt")) {//人员信息
              /*  if(type){//是否下载模板
                    objc=eamUserExtService.selectNeedLoadData();
                }else{
                    objc = eamUserExtService.findList(map);
                }*/
                objc = eamUserExtService.findList(map);
            } else if (exportConfigId.equals("supplier")) {//供应商
                objc = supplierService.findListByMape(map);
            } else if (exportConfigId.equals("supplierType")) {//供应商类型
                objc = supplierTypeService.findListByMap(map);
            } else if (exportConfigId.equals("devCategory")) {   //设备类别
                objc = eamDevCategoryService.findListByMap(map);
            } else if (exportConfigId.equals("device")) {        //设备信息
                objc = eamDeviceService.findListByMap(map);
            } else if (exportConfigId.equals("standardlib")) {   //标准库
                objc = standardLibService.findStand(map);
                objFault = standardLibService.findFault(map);
                objMaintain = standardLibService.findMaintain(map);
                objPatrol = standardLibService.findPatrol(map);
                objFail = standardLibService.findFailure(map);
                objOpe = standardLibService.findOpe(map);
                objSafe = standardLibService.findSafe(map);
            } else if (exportConfigId.equals("devLocation")) {//空间信息
                objc = eamDevLocService.findListByMap(map);
            } else if (exportConfigId.equals("stand_work")) {   //标准工作
                objc = eamOperationWorkService.findWorkListByMap(map);
                objcProcedure = eamOperationWorkService.findProcedureByMap(map);
                objcSafety = eamOperationWorkService.findSafetyByMap(map);
                objcTools = eamOperationWorkService.findToolsByMap(map);
                objcSpareparts = eamOperationWorkService.findSparepartsByMap(map);
                objcPersonHours = eamOperationWorkService.findPersonHoursByMap(map);
                objcOthers = eamOperationWorkService.findOthersByMap(map);
            }else if (exportConfigId.equals("materialType")) {//物料类别
                objc = materialTypeService.findListByMap(map);
            }else if (exportConfigId.equals("materialInfo")) {//物料信息
                objc = materialInfoService.findListByMap(map);
            }else if(exportConfigId.equals("schedualType")){//排班类型
               try {
                    Map<String,Object> result = new HashedMap();//map不能直接转对象，必须先转Object然后再转
                    result.put("data",schedualTypeService.findListByMapForExport(map));
                    List<SchedualType> res=(List<SchedualType>)result.get("data");//获取排班类型和排班类型时间表
                    objc=new ArrayList<>();
                    //取出指定的对象属性封装
                    for (int i = 0; i <res.size(); i++) {
                        Map m=new HashMap();
                        SchedualType schedualType=res.get(i);
                        m.put("type_code",schedualType.getType_code());
                        m.put("type_name",schedualType.getType_name());
                        m.put("schedual_time_str",schedualType.getSchedual_time_str());//时间范围
                        m.put("type_desc",schedualType.getType_desc());
                        m.put("type_remark",schedualType.getType_remark());
                        m.put("type_status",schedualType.getType_status());
                        objc.add(m);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (exportConfigId.equals("schedual")){//排班
                objc = schedualOrderService.getExportData(map);
            }else if (exportConfigId.equals("inspectionsub")){//巡检项
                objc = inspectionSubjectService.findListByMap(map);
            }else if (exportConfigId.equals("inspectionarea")) {//巡检区域
                objc = inspectionAreaService.findListByMap(map);
            }else if (exportConfigId.equals("inspectionroute")) {//巡检路线
                objc = inspectionRouteService.findListByMap(map);
            }else if (exportConfigId.equals("inspectiontask")) {//巡检任务
                objc = inspectionTaskService.findListByMap(map);
            }else if (exportConfigId.equals("operationOrders")){//标准工单
                    objc = standardOrderService.findListByMap(map);
            }else if (exportConfigId.equals("maintannual")){//保养年计划
                objc =  maintainProjectService.getExportData(map);
            }else if (exportConfigId.equals("maintmon")){//保养月计划
                objc =  maintainProjectSubService.getExportData(map);
            }else if (exportConfigId.equals("maintset")){//保养设置
                objc =  maintainProjectInfService.getExportData(map);
            }





        } else {//这边是选中导出
            String[] ids_array = ids.toString().split(",");
            map.put("ids", ids_array);
            if (exportConfigId.equals("customer")) {    //客户信息
                objc = eamCustomerService.findList(map);
            } else if (exportConfigId.equals("eamUserExt")) {//人员信息
                objc = eamUserExtService.findList(map);
            } else if (exportConfigId.equals("supplier")) {
                objc = supplierService.findListByMape(map);
            } else if (exportConfigId.equals("supplierType")) {
                objc = supplierTypeService.findListByMap(map);
            } else if (exportConfigId.equals("devCategory")) {
                objc = eamDevCategoryService.findListByMap(map);
            } else if (exportConfigId.equals("device")) {
                objc = eamDeviceService.findListByMap(map);
            } else if (exportConfigId.equals("standardlib")) {
                objc = standardLibService.findStand(map);
                objFault = standardLibService.findFault(map);
                objMaintain = standardLibService.findMaintain(map);
                objPatrol = standardLibService.findPatrol(map);
                objFail = standardLibService.findFailure(map);
                objOpe = standardLibService.findOpe(map);
                objSafe = standardLibService.findSafe(map);
            } else if (exportConfigId.equals("devLocation")) {//空间信息
                objc = eamDevLocService.findListByMap(map);
            } else if (exportConfigId.equals("stand_work")) {   //标准工作
                objc = eamOperationWorkService.findWorkListByMap(map);
                objcProcedure = eamOperationWorkService.findProcedureByMap(map);
                objcSafety = eamOperationWorkService.findSafetyByMap(map);
                objcTools = eamOperationWorkService.findToolsByMap(map);
                objcSpareparts = eamOperationWorkService.findSparepartsByMap(map);
                objcPersonHours = eamOperationWorkService.findPersonHoursByMap(map);
                objcOthers = eamOperationWorkService.findOthersByMap(map);
            }else if (exportConfigId.equals("materialType")) {//物料类别
                objc = materialTypeService.findListByMap(map);
            }else if (exportConfigId.equals("materialInfo")) {//物料信息
                objc = materialInfoService.findListByMap(map);
            }else if(exportConfigId.equals("schedualType")){//排班类型
                try {
                    Map<String,Object> result = new HashedMap();//map不能直接转对象，必须先转Object然后再转
                    result.put("data",schedualTypeService.findListByMapForExport(map));
                    List<SchedualType> res=(List<SchedualType>)result.get("data");//获取排班类型和排班类型时间表
                    objc=new ArrayList<>();
                    //取出指定的对象属性封装
                    for (int i = 0; i <res.size(); i++) {
                        Map m=new HashMap();
                        SchedualType schedualType=res.get(i);
                        m.put("type_code",schedualType.getType_code());
                        m.put("type_name",schedualType.getType_name());
                        m.put("schedual_time_str",schedualType.getSchedual_time_str());//时间范围
                        m.put("type_desc",schedualType.getType_desc());
                        m.put("type_remark",schedualType.getType_remark());
                        m.put("type_status",schedualType.getType_status());
                        objc.add(m);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (exportConfigId.equals("schedual")){//排班
                objc = schedualOrderService.getExportData(map);
            }else if (exportConfigId.equals("inspectionsub")){//巡检项
                objc = inspectionSubjectService.findListByMap(map);
            }else if (exportConfigId.equals("inspectionarea")){//巡检区域
                objc = inspectionAreaService.findListByMap(map);
            }else if (exportConfigId.equals("inspectionroute")) {//巡检路线
                objc = inspectionRouteService.findListByMap(map);
            }else if (exportConfigId.equals("inspectiontask")) {//巡检任务
                objc = inspectionTaskService.findListByMap(map);
            }else if (exportConfigId.equals("operationOrders")){//标准工单
                objc = standardOrderService.findListByMap(map);
            }else if (exportConfigId.equals("maintannual")){//保养年计划
                objc =  maintainProjectService.getExportData(map);
            }else if (exportConfigId.equals("maintmon")){//保养月计划
                objc =  maintainProjectSubService.getExportData(map);
            }else if (exportConfigId.equals("maintset")){//保养设置
                objc =  maintainProjectInfService.getExportData(map);
            }

        }
        List[] values = {objc, objFault, objMaintain, objPatrol, objFail, objOpe, objSafe};
        List[] workValues = {objc, objcProcedure, objcSafety, objcTools, objcSpareparts, objcPersonHours, objcOthers};

        if (!exportConfigId.equals("standardlib") && !exportConfigId.equals("stand_work")) {
            //下面的代码每个导出都一样
            ExcelConfig excelConfig = ExcelConfigParser.getConfigById(exportConfigId);
            String fileName = excelConfig.getConfigModel().getExportFileName();
            String title = fileName;
            String[] titles = null;
            List<Integer> checkItemLst = null;
            if (objc == null || objc.size() == 0) {
                resp.getWriter().print("提示:数据不存在!");
                return;
            }
            try {
                for (int i = 0; i < objc.size(); i++) {
                    Map dos = objc.get(i);
                    List<ExcelFieldModel> fields = excelConfig.getConfigModel().getFields();
                    titles = new String[fields.size()];
                    String[] strs = new String[fields.size()];
                    checkItemLst = new ArrayList<Integer>();
                    for (int j = 0; j < fields.size(); j++) {
                        ExcelFieldModel field = fields.get(j);
                        titles[j] = (field.getShowName());
                        if (!field.isNullbale()) {
                            checkItemLst.add(j);
                        }
                        strs[j] = field.getExportData(dos);
                    }
                    if (objc != null)
                        list.add(strs);
                }
            } catch (Exception e) {
                e.printStackTrace();
                resp.getWriter().print("error:" + e.getMessage());
                return;
            } finally {
                excelConfig.dispose();
            }
            try {
                sheet=title;
                new ExcelUtil()
                        .setSheet(sheet)
                        .setFilename(fileName)
                        .setTitle(title)
                        .setTitles(titles)
                        .setCheckItem(checkItemLst)
                        .setList(list).start(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (exportConfigId.equals("standardlib")) {                //适用标准库，因为标准库导出多个表
            String[] exportConfigIds = {"standardlib", "standardfault", "standardmaintain", "standardpatrol", "standardfail", "standardope", "standardsafe"};
            String title = "";
            List<List<String[]>> listm = new ArrayList<List<String[]>>();
            List<String[]> ltitles = new ArrayList<String[]>();
            List checkItems = new ArrayList();
            String[] titles = null;
            List<Integer> checkItemLst = null;
            String[] titlem = new String[exportConfigIds.length];
            String fileName = "标准库";//文件名
            for (int m = 0; m < exportConfigIds.length; m++) {
                List listss = new ArrayList();//标准库每张表
                ExcelConfig excelConfig = ExcelConfigParser.getConfigById(exportConfigIds[m]);
                String file = excelConfig.getConfigModel().getExportFileName();
                titlem[m] = file;
                if (values[m] == null || values[m].size() == 0) {
                    resp.getWriter().print("提示:Sheet" + (m + 1) + "数据不存在!");
                    return;
                }
                try {
                    if(m==0){
                        for (int i = 0; i < values[m].size(); i++) {
                            Map dos = (Map) values[m].get(i);
                            StringBuffer device_id=null;
                            device_id=new StringBuffer();
                            if(dos.get("id_key") !=null){
                                List<Map> devlist=standardLibService.getDevBylib((String)dos.get("id_key"));
                                for(Map devmap:devlist){
                                    device_id.append(","+devmap.get("dev_code"));
                                }
                                if(device_id.indexOf(",") !=-1) {
                                    dos.put("dev_code", device_id.substring(1));
                                }

                            }
                            List<ExcelFieldModel> fields = excelConfig.getConfigModel().getFields();
                            titles = new String[fields.size()];
                            String[] strs = new String[fields.size()];
                            checkItemLst = new ArrayList<Integer>();
                            for (int j = 0; j < fields.size(); j++) {
                                ExcelFieldModel field = fields.get(j);
                                titles[j] = (field.getShowName());
                                if (!field.isNullbale()) {
                                    checkItemLst.add(j);
                                }
                                strs[j] = field.getExportData(dos);
                            }
                            if (values[m] != null)
                                listss.add(strs);
                        }
                    }else{
                        for (int i = 0; i < values[m].size(); i++) {

                            Map dos = (Map) values[m].get(i);
                            List<ExcelFieldModel> fields = excelConfig.getConfigModel().getFields();
                            titles = new String[fields.size()];
                            String[] strs = new String[fields.size()];
                            checkItemLst = new ArrayList<Integer>();
                            for (int j = 0; j < fields.size(); j++) {
                                ExcelFieldModel field = fields.get(j);
                                titles[j] = (field.getShowName());
                                if (!field.isNullbale()) {
                                    checkItemLst.add(j);
                                }
                                strs[j] = field.getExportData(dos);
                            }
                            if (values[m] != null)
                                listss.add(strs);
                        }
                    }
                    listm.add(listss);
                    ltitles.add(titles);
                    checkItems.add(checkItemLst);
                } catch (Exception e) {
                    e.printStackTrace();
                    resp.getWriter().print("error:" + e.getMessage());
                    return;
                } finally {
                    excelConfig.dispose();
                }
            }
            try {
                new ExcelUtil()
                        .setFilename(fileName)
                        .setSheets(new String[]{"标准库", "检修标准", "保养标准", "巡检标准", "缺陷故障库", "运行标准", "安全标准"})
                        .setTitlem(titlem)
                        .setTitlesm(ltitles)
                        .setCheckItems(checkItems)
                        .setListm(listm).makeExcel(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (exportConfigId.equals("stand_work")) {
            String[] exportConfigIds = {"operationWork", "operationworkProcedure", "operationworkSafety", "operationworkTools",
                    "operationworkSpareparts", "operationworkPersonHours", "operationworkOthers"};
            String title = "";
            List<List<String[]>> listm = new ArrayList<List<String[]>>();
            List<String[]> ltitles = new ArrayList<String[]>();
            List checkItems = new ArrayList();
            String[] titles = null;
            List<Integer> checkItemLst = null;
            String[] titlem = new String[exportConfigIds.length];
            String fileName = "标准工作表";//文件名
            for (int m = 0; m < exportConfigIds.length; m++) {
                List listss = new ArrayList();//标准库每张表
                ExcelConfig excelConfig = ExcelConfigParser.getConfigById(exportConfigIds[m]);
                String file = excelConfig.getConfigModel().getExportFileName();
                titlem[m] = file;
//                if (workValues[m] == null || workValues[m].size() == 0) {
//                    resp.getWriter().print("提示:Sheet" + (m + 1) + "数据不存在!");
//                    return;
//                }
                try {
                    for (int i = 0; i < workValues[m].size(); i++) {
                        Map dos = (Map) workValues[m].get(i);
                        List<ExcelFieldModel> fields = excelConfig.getConfigModel().getFields();
                        titles = new String[fields.size()];
                        String[] strs = new String[fields.size()];
                        checkItemLst = new ArrayList<Integer>();
                        for (int j = 0; j < fields.size(); j++) {
                            ExcelFieldModel field = fields.get(j);
                            titles[j] = (field.getShowName());
                            if (!field.isNullbale()) {
                                checkItemLst.add(j);
                            }
                            strs[j] = field.getExportData(dos);
                        }
                        if (workValues[m] != null)
                            listss.add(strs);
                    }
                    listm.add(listss);
                    ltitles.add(titles);
                    checkItems.add(checkItemLst);
                } catch (Exception e) {
                    e.printStackTrace();
                    resp.getWriter().print("error:" + e.getMessage());
                    return;
                } finally {
                    excelConfig.dispose();
                }
            }
            try {
                new ExcelUtil()
                        .setFilename(fileName)
                        .setSheets(new String[]{"标准工作", "工序", "安全措施", "工器具", "备品备件", "人员工时", "其他费用"})
                        .setTitlem(titlem)
                        .setTitlesm(ltitles)
                        .setCheckItems(checkItems)
                        .setListm(listm).makeExcel(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);

	}




}
