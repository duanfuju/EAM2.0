package com.tiansu.eam.common.utils.excel.service;
/**
 * @description
 * @author duanfuju
 * @create 2017-08-28 8:52
 **/

import com.alibaba.fastjson.JSONObject;
import com.oreilly.servlet.MultipartRequest;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.tiansu.eam.common.utils.IdGen;
import com.tiansu.eam.common.utils.excel.ExcelConfigParser;
import com.tiansu.eam.common.utils.excel.ExcelUtil;
import com.tiansu.eam.common.utils.excel.ImportInterceptor;
import com.tiansu.eam.common.utils.excel.UploadExcel;
import com.tiansu.eam.common.utils.excel.model.ExcelConfig;
import com.tiansu.eam.modules.act.service.EamProcessService;
import com.tiansu.eam.modules.device.dao.EamDevCategoryDao;
import com.tiansu.eam.modules.device.dao.EamDevLocDao;
import com.tiansu.eam.modules.device.dao.EamDeviceDao;
import com.tiansu.eam.modules.device.entity.DevCategory;
import com.tiansu.eam.modules.device.entity.DevLocation;
import com.tiansu.eam.modules.device.entity.Device;
import com.tiansu.eam.modules.employee.dao.EamCustomerDao;
import com.tiansu.eam.modules.employee.dao.EamUserExtDao;
import com.tiansu.eam.modules.employee.entity.EamCustomer;
import com.tiansu.eam.modules.employee.entity.EamUserExt;
import com.tiansu.eam.modules.inspection.dao.InspectionSubjectDao;
import com.tiansu.eam.modules.inspection.entity.InspectionSubject;
import com.tiansu.eam.modules.maintain.dao.MaintainProjectInfDao;
import com.tiansu.eam.modules.maintain.entity.MaintainProjectInfContent;
import com.tiansu.eam.modules.maintain.entity.MaintainProjectInfDevice;
import com.tiansu.eam.modules.maintain.entity.MaintainProjectInfForImport;
import com.tiansu.eam.modules.material.dao.MaterialInfoDao;
import com.tiansu.eam.modules.material.dao.MaterialTypeDao;
import com.tiansu.eam.modules.material.entity.MaterialInfo;
import com.tiansu.eam.modules.material.entity.MaterialType;
import com.tiansu.eam.modules.opestandard.dao.StandardLibDao;
import com.tiansu.eam.modules.opestandard.entity.*;
import com.tiansu.eam.modules.schedual.dao.SchedualDao;
import com.tiansu.eam.modules.schedual.dao.SchedualOrderDao;
import com.tiansu.eam.modules.schedual.dao.SchedualTypeDao;
import com.tiansu.eam.modules.schedual.entity.SchedualImport;
import com.tiansu.eam.modules.schedual.entity.SchedualOrder;
import com.tiansu.eam.modules.schedual.entity.SchedualType;
import com.tiansu.eam.modules.schedual.entity.SchedualTypeTime;
import com.tiansu.eam.modules.schedual.service.SchedualOrderService;
import com.tiansu.eam.modules.supplier.dao.SupplierDao;
import com.tiansu.eam.modules.supplier.dao.SupplierTypeDao;
import com.tiansu.eam.modules.supplier.entity.Supplier;
import com.tiansu.eam.modules.supplier.entity.SupplierType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


/**
 * @author duanfuju
 * @create 2017-08-28 8:52
 * @desc 解析上传的文件
 **/
@Service
@Transactional(readOnly = true)
public class FileUpLoadService {

    @Autowired
    private EamCustomerDao eamCustomerDao;//客户信息
    @Autowired
    private EamDevLocDao eamDevLocDao;//空间信息
    @Autowired
    private EamUserExtDao eamUserExtDao;//人员信息
    @Autowired
    private MaterialInfoDao materialInfoDao;//物料信息
    @Autowired
    private MaterialTypeDao materialTypeDao;//物料类别
    @Autowired
    private SupplierDao supplierDao;//供应商管理
    @Autowired
    private EamDevCategoryDao eamDevCategoryDao;  //设备类别
    @Autowired
    private EamDeviceDao eamDeviceDao;   //设备信息
    @Autowired
    private StandardLibDao standardLibDao;//标准库
    @Autowired
    private EamProcessService eamProcessService;//流程
    @Autowired
    private SchedualTypeDao schedualTypeDao;//排班表类型
    @Autowired
    private SchedualOrderDao schedualOrderDao;//排班单据
    @Autowired
    private SchedualOrderService schedualOrderService;//排班单据
    @Autowired
    private SchedualDao schedualDao;//排班
    @Autowired
    private InspectionSubjectDao  inspectionSubjectDao;//巡检项
    @Autowired
    private MaintainProjectInfDao maintainProjectInfDao;//保养设置

    @Autowired
    private SupplierTypeDao supplierTypeDao;//供应商类型

    /**s
     * @creator duanfuju
     * @createtime 2017/8/28 9:09
     * @description: 
     * 导入数据
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String importExcel (HttpServletRequest request,
                               HttpServletResponse response) throws ServletException, IOException {
        PrintWriter write = response.getWriter();
        JSONObject jsonObject=new JSONObject();
        Map<String, Object> map = request.getParameterMap();
        int readLineLog = 0;
        String err_line = "";
        Map<String, Object> param=new HashMap<String, Object>();//入参对象
        //上传文件到服务器
        String name = request.getParameter("name");
        String excelPath=request.getSession().getServletContext().getRealPath("/file/"+name);
        String[] a=(String[]) map.get("a");

        if (excelPath != null || !"".equals(excelPath)) {
            ExcelConfig excelConfig = ExcelConfigParser.getConfigById(a[0]);
            String sheetName=excelConfig.getConfigModel().getExportFileName();
            // 解析excel
            List<String[]> myList = UploadExcel.readExcelsByPoi(excelPath, sheetName);
            if(myList == null){
                // response.getWriter().write("数据读取失败，请检查工作表名称是否为【Sheet1】");
                jsonObject.put("msg","数据读取失败，请检查工作表名称是否为【"+sheetName+"】");
            }
            excelConfig.dispose();
            try{
                if(a[0].equals("maintset")) {//保养设置=====================================================================>maintset
                    List<MaintainProjectInfForImport> maintainProjectInfForImports=new ArrayList<MaintainProjectInfForImport>();
                    for (int i = 0; i < myList.size(); i++) {
                        readLineLog = i + 3;//从第3行算第一行;
                        err_line = "第" + readLineLog + "行";
                        String[] values = myList.get(i);
                        if (excelConfig != null) {
                            //excel value从第1个索引开始，第0个统一为序号列
                            int startIndex = 1;
                            MaintainProjectInfForImport cus = ExcelUtil.convertToBean(excelConfig, values, new MaintainProjectInfForImport(), startIndex);
                            cus.preInsert();
                            maintainProjectInfForImports.add(cus);
                        }
                    }
                    //获取所有的数据
                    List<Map> allMaintainProjectInfForImport=maintainProjectInfDao.findListByMap(null);//获取所有的数据
                    List<MaintainProjectInfForImport> needInsert=new ArrayList<MaintainProjectInfForImport>();//需要新增的列表
                    List<MaintainProjectInfForImport> needUpdate=new ArrayList<MaintainProjectInfForImport>();//需要更新的列表
                    List<MaintainProjectInfDevice> maintainProjectInfDevices=new ArrayList<>();//设备
                    List<MaintainProjectInfContent> maintainProjectInfContents= new ArrayList<>();//保养内容

                    for(int i=0;i<maintainProjectInfForImports.size();i++){
                        MaintainProjectInfForImport maintainProjectInfForImport =maintainProjectInfForImports.get(i);
                        boolean flag=false;//新增或者是修改的标志
                        for (int j=0;j<allMaintainProjectInfForImport.size();j++){
                            Map c = allMaintainProjectInfForImport.get(j);
                            if (maintainProjectInfForImport.getProject_code().equalsIgnoreCase(c.get("project_code").toString())){
                                maintainProjectInfForImport.setId(c.get("id_key").toString());//设置主键值
                                maintainProjectInfForImport.setId_key(maintainProjectInfForImport.getId());//设置主键值
                                flag=true;
                                break;
                            }
                        }
                        //转周期
                        String cycle=maintainProjectInfForImport.getProject_cycle();
                        try {
                            if(maintainProjectInfForImport.getProject_period().equals("0")){//天       每i天
                                cycle=cycle.substring(1,cycle.indexOf("天"));
                            }else if(maintainProjectInfForImport.getProject_period().equals("1")){//周   每i周的周i
                                String str1=cycle.substring(1,cycle.indexOf("周"));
                                String str2=cycle.split("周")[2];
                                cycle=str1+"_"+str2;
                            }else if(maintainProjectInfForImport.getProject_period().equals("2")){//月             每i月的第i周的周i
                                String str1=cycle.substring(1,cycle.indexOf("月"));
                                String str2=cycle.split("第")[1].split("周")[0];
                                String str3=cycle.split("周")[2];
                                cycle=str1+"_"+str2+"_"+str3;
                            }else if(maintainProjectInfForImport.getProject_period().equals("3")){//季            每i季的第i月的第i周的周i
                                String str1=cycle.substring(1,cycle.indexOf("季"));
                                String str2=cycle.split("第")[1].split("月")[0];
                                String str3=cycle.split("第")[2].split("周")[0];
                                String str4=cycle.split("第")[2].split("周")[2];
                                cycle=str1+"_"+str2+"_"+str3+"_"+str4;
                            }else if(maintainProjectInfForImport.getProject_period().equals("4")){//年            每i年的第i月的第i周的周i
                                String str1=cycle.substring(1,cycle.indexOf("年"));
                                String str2=cycle.split("第")[1].split("月")[0];
                                String str3=cycle.split("第")[2].split("周")[0];
                                String str4=cycle.split("第")[2].split("周")[2];
                                cycle=str1+"_"+str2+"_"+str3+"_"+str4;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            response.setCharacterEncoding("utf-8");
                            response.setContentType("text/html; charset=utf-8");
                            jsonObject.put("msg","周期填写的格式有问题，请检查");
                            ImportInterceptor.createFile(name,"周期填写的格式有问题，请检查");
                            return jsonObject.toString();
                        }
                        maintainProjectInfForImport.setProject_cycle(cycle);
                        //处理关联表数据
                        maintainProjectInfForImport.setContentData();
                        maintainProjectInfForImport.setDeviceData();
                        maintainProjectInfDevices.addAll(maintainProjectInfForImport.getMaintainProjectInfDevices());
                        if(maintainProjectInfForImport.getProject_content()!=null){
                            maintainProjectInfContents.addAll(maintainProjectInfForImport.getMaintainProjectInfContents());
                        }
                        if (flag){
                            needUpdate.add(maintainProjectInfForImport);//加入更新列表
                        }else{
                            needInsert.add(maintainProjectInfForImport);//加入新增列表
                        }
                    }


                    //新增
                    if(needInsert.size()>0){
                        int lsize=needInsert.size();
                        if(lsize>50){//如果新增的数据量大于50则分段提交
                            List<MaintainProjectInfForImport> iList = new ArrayList<MaintainProjectInfForImport>();
                            for (int i = 0, n=lsize; i < n; i++) {
                                iList.add(needInsert.get(i));
                                if ((i>0 && i % 50 == 0) || i == lsize - 1) {
                                    maintainProjectInfDao.insertBatch(iList);
                                    iList = new ArrayList<MaintainProjectInfForImport>();
                                }
                            }
                        }else {
                            maintainProjectInfDao.insertBatch(needInsert);
                        }
                    }else{
                        //修改
                        for (int i=0;i<needUpdate.size();i++){
                            MaintainProjectInfForImport single =needUpdate.get(i);
                            maintainProjectInfDao.delMaintContent(single.getId());
                            maintainProjectInfDao.delMaintDevice(single.getId());
                            single.preUpdate();
                            maintainProjectInfDao.updateSingle(single);
                        }
                        //关联表新增
                        maintainProjectInfDao.insertMaintCont(maintainProjectInfContents);
                        int lsize=maintainProjectInfDevices.size();
                        if(lsize>50){//如果新增的数据量大于50则分段提交
                            List<MaintainProjectInfDevice> iList = new ArrayList<MaintainProjectInfDevice>();
                            for (int i = 0, n=lsize; i < n; i++) {
                                iList.add(maintainProjectInfDevices.get(i));
                                if ((i>0 && i % 50 == 0) || i == lsize - 1) {
                                    maintainProjectInfDao.insertMaintDev(iList);
                                    iList = new ArrayList<MaintainProjectInfDevice>();
                                }
                            }
                        }else {
                            maintainProjectInfDao.insertMaintDev(maintainProjectInfDevices);
                        }
                    }

                }else  if(a[0].equals("inspectionsub")) {//巡检项=====================================================================>inspectionsub
                    List<InspectionSubject> inspectionSubjects=new ArrayList<InspectionSubject>();
                    for (int i = 0; i < myList.size(); i++) {
                        readLineLog = i + 3;//从第3行算第一行;
                        err_line = "第" + readLineLog + "行";
                        String[] values = myList.get(i);
                        if (excelConfig != null) {
                            //excel value从第1个索引开始，第0个统一为序号列
                            int startIndex = 1;
                            InspectionSubject cus = ExcelUtil.convertToBean(excelConfig, values, new InspectionSubject(), startIndex);
                            cus.preInsert();
                            inspectionSubjects.add(cus);
                        }
                    }
                   // inspectionSubjectDao.insertBatch(inspectionSubjects);
                    //获取所有的巡检项数据
                    Map allMap=new HashMap();
                    allMap.put("export", "export");//判断使用哪一个include,个别的导出sql需要调整
                    List<Map> allInspectionSubject=inspectionSubjectDao.findListByMap(allMap);//获取所有的数据
                    List<InspectionSubject> needInsert=new ArrayList<InspectionSubject>();//需要新增的列表
                    List<InspectionSubject> needUpdate=new ArrayList<InspectionSubject>();//需要更新的列表

                    for(int i=0;i<inspectionSubjects.size();i++){
                        InspectionSubject inspectionSubject =inspectionSubjects.get(i);
                        boolean flag=false;//新增或者是修改的标志
                        for (int j=0;j<allInspectionSubject.size();j++){
                            Map c = allInspectionSubject.get(j);
                            //巡检项的更新需要3个步骤：1.巡检项名称一致、设备id一致、值类型一致
                            if (inspectionSubject.getSubject_name().equalsIgnoreCase(c.get("subject_name").toString())
                                    &&inspectionSubject.getDev_id().equalsIgnoreCase(c.get("dev_id").toString())
                                    &&inspectionSubject.getSubject_valuetype().equalsIgnoreCase(c.get("subject_valuetype").toString())){
                                inspectionSubject.setId_key(c.get("id_key").toString());//设置主键值
                                flag=true;
                                break;
                            }
                        }
                        if (flag){
                            needUpdate.add(inspectionSubject);//加入更新列表
                        }else{
                            needInsert.add(inspectionSubject);//加入新增列表
                        }
                    }
                    //新增
                    if(needInsert.size()>0){
                        int lsize=needInsert.size();
                        if(lsize>50){//如果新增的数据量大于50则分段提交
                            List<InspectionSubject> iList = new ArrayList<InspectionSubject>();
                            for (int i = 0, n=lsize; i < n; i++) {
                                iList.add(needInsert.get(i));
                                if ((i>0 && i % 50 == 0) || i == lsize - 1) {
                                    inspectionSubjectDao.insertBatch(iList);
                                    iList = new ArrayList<InspectionSubject>();
                                }
                            }
                        }else {
                            inspectionSubjectDao.insertBatch(needInsert);
                        }
                    }
                    //修改
                    for (int i=0;i<needUpdate.size();i++){
                        InspectionSubject single =needUpdate.get(i);
                        single.preUpdate();
                        inspectionSubjectDao.update(single);
                    }
                }else if(a[0].equals("schedualType")) {//排班类型=====================================================================>schedualType
                    List<SchedualType> schedualTypes=new ArrayList<SchedualType>();
                    List<SchedualTypeTime> schedualTypeTimes=new ArrayList<SchedualTypeTime>();
                    for (int i = 0; i < myList.size(); i++) {
                        readLineLog = i + 3;//从第3行算第一行;
                        err_line = "第" + readLineLog + "行";
                        String[] values = myList.get(i);
                        if (excelConfig != null) {
                            //excel value从第1个索引开始，第0个统一为序号列
                            int startIndex = 1;
                            SchedualType dl = ExcelUtil.convertToBean(excelConfig, values, new SchedualType(), startIndex);
                            dl.preInsert();

                            schedualTypes.add(dl);
                        }
                    }
                    List<SchedualType> needInsert=new ArrayList<SchedualType>();//需要新增(排班类型)
                    List<SchedualTypeTime> needInsertTimes=new ArrayList<SchedualTypeTime>();//需要新增(排班类型时间)
                    List<SchedualType> needUpdate=new ArrayList<SchedualType>();//需要更新(排班类型)

                    //1.获取排班的所有数据
                    List<SchedualType> allSchedualType = schedualTypeDao.findList(new SchedualType());
                    //2.数据库中的数据和此次添加的数据进行排班类型编码的比对，存在则更新（先删除排班类型时间，后新增）
                    for (int i = 0; i < schedualTypes.size(); i++) {
                        SchedualType schedualType = schedualTypes.get(i);
                        boolean flag = false;
                        for (int j = 0; j <allSchedualType.size(); j++) {
                            SchedualType m=allSchedualType.get(j);
                            if (schedualType.getType_code().equals(m.getType_code())){
                                schedualType.setId(m.getId());
                                schedualTypeDao.deleteByTypeId(schedualType.getId());//需要更新的数据先删除子表
                                flag=true;
                                break;
                            }
                        }

                        if(flag){
                            needUpdate.add(schedualType);
                        }else{
                            needInsert.add(schedualType);
                        }
                        schedualType.schedualTimeStrConvertSchedualTimeList();
                        needInsertTimes.addAll(schedualType.getSchedual_time_list());
                    }
                    //排班类型批量插入
                    if(needInsert.size()>0){
                        int lsize=needInsert.size();
                        if(lsize>50){//如果新增的数据量大于50则分段提交
                            List<SchedualType> iList = new ArrayList<SchedualType>();
                            for (int i = 0, n=lsize; i < n; i++) {
                                iList.add(needInsert.get(i));
                                if ((i>0 && i % 50 == 0) || i == lsize - 1) {
                                    schedualTypeDao.insertBatch(iList);
                                    iList = new ArrayList<SchedualType>();
                                }
                            }
                        }else{
                            schedualTypeDao.insertBatch(needInsert);
                        }
                    }
                    //排班类型时间批量插入
                    if(needInsertTimes.size()>0){
                        int lsize=needInsertTimes.size();
                        if(lsize>50){//如果新增的数据量大于50则分段提交
                            List<SchedualTypeTime> iList = new ArrayList<SchedualTypeTime>();
                            for (int i = 0, n=lsize; i < n; i++) {
                                iList.add(needInsertTimes.get(i));
                                if ((i>0 && i % 50 == 0) || i == lsize - 1) {
                                    schedualTypeDao.batchSaveTime(iList);
                                    iList = new ArrayList<SchedualTypeTime>();
                                }
                            }
                        }else {
                            schedualTypeDao.batchSaveTime(needInsertTimes);
                        }
                    }
                    //排班类型更新
                    for (int i = 0; i < needUpdate.size(); i++) {
                        SchedualType single=needUpdate.get(i);
                        single.preUpdate();
                        schedualTypeDao.update(single);
                    }
                }else if(a[0].equals("schedual")) {//排班=====================================================================>schedual
                    List<SchedualImport> schedualImports=new ArrayList<SchedualImport>();
                    for (int i = 0; i < myList.size(); i++) {
                        readLineLog = i + 3;//从第3行算第一行;
                        err_line = "第" + readLineLog + "行";
                        String[] values = myList.get(i);
                        if (excelConfig != null) {
                            //excel value从第1个索引开始，第0个统一为序号列
                            int startIndex = 1;
                            SchedualImport dl = ExcelUtil.convertToBean(excelConfig, values, new SchedualImport(), startIndex);
                            dl.preInsert();
                            //将排班单据和排班建立联系
                            dl.setOrder_id(dl.getId());
                            schedualImports.add(dl);
                        }
                    }

                    List<SchedualImport> needInsert=new ArrayList<SchedualImport>();//需要新增
                    List<SchedualImport> needUpdate=new ArrayList<SchedualImport>();//需要更新
                    List<SchedualImport> needHandleOrder=new ArrayList<SchedualImport>();//排班单据
                    //排班单据的新增和修改
                    List<SchedualOrder> allSchedualOrder = schedualOrderDao.findList(new SchedualOrder());
                    //排班单据根据ordercode去重
                    needHandleOrder = schedualOrderService.removeDupliByOrderCode(schedualImports);
                    for (int i = 0; i <needHandleOrder.size() ; i++) {
                        SchedualImport schedualImport=needHandleOrder.get(i);
                        boolean flag = false;

                        for (int j = 0; j < allSchedualOrder.size(); j++) {
                            SchedualOrder schedualOrder=allSchedualOrder.get(j);
                            if(schedualImport.getOrder_code().equalsIgnoreCase(schedualOrder.getOrder_code())){
                                schedualImport.setOrder_id(schedualOrder.getId());
                                //删除和排班单据有关联的排班数据
                                schedualDao.deleteByOrderId(schedualImport.getOrder_id());
                                flag=true;
                                break;
                            }
                        }

                        if(flag){
                            needUpdate.add(schedualImport);
                        }else{
                            needInsert.add(schedualImport);
                        }
                    }
                    //排班单据批量插入
                    if(needInsert.size()>0){
                        int lsize=needInsert.size();
                        if(lsize>50){//如果新增的数据量大于50则分段提交
                            List<SchedualImport> iList = new ArrayList<SchedualImport>();
                            for (int i = 0, n=lsize; i < n; i++) {
                                iList.add(needInsert.get(i));
                                if ((i>0 && i % 50 == 0) || i == lsize - 1) {
                                    schedualOrderDao.insertBatch(iList);
                                    iList = new ArrayList<SchedualImport>();
                                }
                            }
                        }else {
                            schedualOrderDao.insertBatch(needInsert);
                        }
                    }
                    //排班单据更新
                    for (int i = 0; i < needUpdate.size(); i++) {
                        SchedualImport schedualImport=needUpdate.get(i);
                        SchedualOrder schedualOrder = new SchedualOrder();
                        schedualOrder.setId(schedualImport.getOrder_id());
                        schedualOrder.setOrder_code(schedualImport.getOrder_code());
                        schedualOrder.setOrder_name(schedualImport.getOrder_name());
                        schedualOrder.setApprove_status(schedualImport.getApprove_status());
                        schedualOrder.setRemark(schedualImport.getRemark());
                        schedualOrder.preUpdate();
                        schedualOrderDao.update(schedualOrder);
                    }
                    //排班批量插入        ==>对于排班单据而言，在更新操作时会删除和排班的所有关联，所以只要新增就行
                    if(schedualImports.size()>0){
                        for (int i = 0; i < schedualImports.size(); i++) {
                            SchedualImport schedualImport=schedualImports.get(i);
                            for (int j = 0; j <needHandleOrder.size(); j++) {
                                SchedualImport nho=needHandleOrder.get(j);
                                if(schedualImport.getOrder_code().equals(nho.getOrder_code())){
                                    schedualImport.setOrder_id(nho.getOrder_id());
                                    break;
                                }
                            }
                        }
                        int lsize=schedualImports.size();
                        if(lsize>50){//如果新增的数据量大于50则分段提交
                            List<SchedualImport> iList = new ArrayList<SchedualImport>();
                            for (int i = 0, n=lsize; i < n; i++) {
                                iList.add(schedualImports.get(i));
                                if ((i>0 && i % 50 == 0) || i == lsize - 1) {
                                    schedualDao.insertBatch(iList);
                                    iList = new ArrayList<SchedualImport>();
                                }
                            }
                        }else {
                            schedualDao.insertBatch(schedualImports);
                        }
                    }
                }else if(a[0].equals("customer")) {//客户信息=====================================================================>customer
                    List<EamCustomer> customers=new ArrayList<EamCustomer>();
                    for (int i = 0; i < myList.size(); i++) {
                        readLineLog = i + 3;//从第3行算第一行;
                        err_line = "第" + readLineLog + "行";
                        String[] values = myList.get(i);
                        if (excelConfig != null) {
                            //excel value从第1个索引开始，第0个统一为序号列
                            int startIndex = 1;
                            EamCustomer cus = ExcelUtil.convertToBean(excelConfig, values, new EamCustomer(), startIndex);
                            cus.preInsert();
                            customers.add(cus);
                        }
                    }
                   // eamCustomerDao.insertBatch(customers);

                   //获取所有的客户信息数据
                    Map allMap=new HashMap();
                    List<Map> allEamCustomer=eamCustomerDao.findListByMap(allMap);//获取所有的数据
                    List<EamCustomer> needInsert=new ArrayList<EamCustomer>();//需要新增的列表
                    List<EamCustomer> needUpdate=new ArrayList<EamCustomer>();//需要更新的列表

                    for(int i=0;i<customers.size();i++){
                        EamCustomer eamCustomer =customers.get(i);
                        boolean flag=false;//新增或者是修改的标志
                        for (int j=0;j<allEamCustomer.size();j++){
                            Map c = allEamCustomer.get(j);
                            if (eamCustomer.getCustomer_code().equalsIgnoreCase(c.get("customer_code").toString())){
                                eamCustomer.setId_key(c.get("id_key").toString());//设置主键值
                                flag=true;
                                break;
                            }
                        }
                        if (flag){
                            needUpdate.add(eamCustomer);//加入更新列表
                        }else{
                            needInsert.add(eamCustomer);//加入新增列表
                        }
                    }
                    //新增
                    if(needInsert.size()>0){
                        int lsize=needInsert.size();
                        if(lsize>50){//如果新增的数据量大于50则分段提交
                            List<EamCustomer> iList = new ArrayList<EamCustomer>();
                            for (int i = 0, n=lsize; i < n; i++) {
                                iList.add(needInsert.get(i));
                                if ((i>0 && i % 50 == 0) || i == lsize - 1) {
                                    eamCustomerDao.insertBatch(iList);
                                    iList = new ArrayList<EamCustomer>();
                                }
                            }
                        }else {
                            eamCustomerDao.insertBatch(needInsert);
                        }
                    }
                    //修改
                    for (int i=0;i<needUpdate.size();i++){
                        EamCustomer single =needUpdate.get(i);
                        single.preUpdate();
                        eamCustomerDao.update(single);
                    }
                }else if(a[0].equals("devLocation")) {//空间信息=====================================================================>devLocation
                    List<DevLocation> devLocations=new ArrayList<DevLocation>();
                    Map<String,Object> excelPreGenIdDataMap = excelConfig.initTreeCacheData(myList);
                    for (int i = 0; i < myList.size(); i++) {
                        readLineLog = i + 3;//从第3行算第一行;
                        err_line = "第" + readLineLog + "行";
                        String[] values = myList.get(i);
                        if (excelConfig != null) {
                            //excel value从第1个索引开始，第0个统一为序号列
                            int startIndex = 1;
                            DevLocation dl = ExcelUtil.convertToBean(excelConfig, values, new DevLocation(), startIndex);
                            String value = excelConfig.getConfigModel().getTreeColIndex();
                            if(StringUtils.isNotEmpty(value)){
                                List<String> cat_ids = getKeyList(excelPreGenIdDataMap,values[Integer.parseInt(value)]);
                                if(cat_ids.size() > 1){
                                    System.out.println(values[Integer.parseInt(value)]+"存在重复编码");
                                    throw new RuntimeException(ExcelUtil.excelColIndexToStr(Integer.parseInt(value))+"列"+values[Integer.parseInt(value)]+"存在重复编码");
                                }
                                dl.setLoc_id(cat_ids.get(0));
                            }
                            dl.preInsert();
                            devLocations.add(dl);
                        }
                    }
                    //TODO 这里的思路是获取excel表里面的数据和表里查出来的数据，按照loc_pid上级编码（父节点）和loc_code设备编码（子节点）

                    Map allMap=new HashMap();
                    List<Map> allDevLocations=eamDevLocDao.findListByMap(allMap);//获取所有的数据
                    List<DevLocation> needInsert=new ArrayList<DevLocation>();//需要新增的列表
                    List<DevLocation> needUpdate=new ArrayList<DevLocation>();//需要更新的列表

                    for(int i=0;i<devLocations.size();i++){
                        DevLocation devLocation =devLocations.get(i);
                        boolean flag=false;//新增或者是修改的标志
                        for (int j=0;j<allDevLocations.size();j++){
                            Map c = allDevLocations.get(j);
                            if (devLocation.getLoc_code().equalsIgnoreCase(c.get("loc_code").toString())){
                                devLocation.setId_key(c.get("id_key").toString());//设置主键值
                                flag=true;
                                break;
                            }
                        }
                        if (flag){
                            needUpdate.add(devLocation);//加入更新列表
                        }else{
                            needInsert.add(devLocation);//加入新增列表
                        }
                    }
                    //新增

                    if(needInsert.size()>0) {
                        int lsize = needInsert.size();
                        if (lsize > 50) {//如果新增的数据量大于50则分段提交
                            List<DevLocation> iList = new ArrayList<DevLocation>();
                            for (int i = 0, n = lsize; i < n; i++) {
                                iList.add(needInsert.get(i));
                                if ((i > 0 && i % 50 == 0) || i == lsize - 1) {
                                    eamDevLocDao.insertBatch(iList);
                                    iList = new ArrayList<DevLocation>();
                                }
                            }
                        } else {
                            eamDevLocDao.insertBatch(devLocations);
                        }
                    }

                    //修改
                    for (int i=0;i<needUpdate.size();i++){
                        DevLocation single =needUpdate.get(i);
                        single.preUpdate();
                        eamDevLocDao.update(single);
                    }
                    eamDevLocDao.updateDevLocationTree();
                }else if(a[0].equals("eamUserExt")) {//人员信息=====================================================================>eamUserExt
                    List<EamUserExt> eamUserExts=new ArrayList<EamUserExt>();
                    for (int i = 0; i < myList.size(); i++) {
                        readLineLog = i + 3;//从第3行算第一行;
                        err_line = "第" + readLineLog + "行";
                        String[] values = myList.get(i);
                        if (excelConfig != null) {
                            //excel value从第1个索引开始，第0个统一为序号列
                            int startIndex = 1;
                            EamUserExt eamUserExt = ExcelUtil.convertToBean(excelConfig, values, new EamUserExt(), startIndex);
                            eamUserExt.preInsert();
                            eamUserExts.add(eamUserExt);
                        }
                    }
                    Map allMap=new HashMap();
                    List<Map> allEamUserExts=eamUserExtDao.findListByMap(allMap);//获取所有的数据
                    List<EamUserExt> needInsert=new ArrayList<EamUserExt>();//需要新增的列表
                    List<EamUserExt> needUpdate=new ArrayList<EamUserExt>();//需要更新的列表

                    for(int i=0;i<eamUserExts.size();i++){
                        EamUserExt eamUserExt =eamUserExts.get(i);
                        boolean flag=false;//新增或者是修改的标志
                        for (int j=0;j<allEamUserExts.size();j++){
                            Map c = allEamUserExts.get(j);
                            if (eamUserExt.getLoginname().equalsIgnoreCase(c.get("loginname").toString())){
                                eamUserExt.setId_key(c.get("id_key").toString());//设置主键值
                                flag=true;
                                break;
                            }
                        }
                        if (flag){
                            needUpdate.add(eamUserExt);//加入更新列表
                        }else{
                            needInsert.add(eamUserExt);//加入新增列表
                        }
                    }
                    //新增
                    if(needInsert.size()>0) {
                        int lsize = needInsert.size();
                        if (lsize > 50) {//如果新增的数据量大于50则分段提交
                            List<EamUserExt> iList = new ArrayList<EamUserExt>();
                            for (int i = 0, n = lsize; i < n; i++) {
                                iList.add(needInsert.get(i));
                                if ((i > 0 && i % 50 == 0) || i == lsize - 1) {
                                    eamUserExtDao.insertBatch(iList);
                                    iList = new ArrayList<EamUserExt>();
                                }
                            }
                        } else {
                            eamUserExtDao.insertBatch(eamUserExts);
                        }
                    }
                    //修改
                    for (int i=0;i<needUpdate.size();i++){
                        EamUserExt single =needUpdate.get(i);
                        single.preUpdate();
                        eamUserExtDao.update(single);
                    }

                }else if(a[0].equals("supplier")){//供应商管理//=====================================================================>supplier
                    List<Supplier> suppliers=new ArrayList<Supplier>();
                    for(int i=0;i<myList.size();i++){
                        readLineLog = i + 3;//从第3行算第一行;
                        err_line = "第" + readLineLog + "行";
                        String[] values = myList.get(i);
                        if (excelConfig != null) {
                            int startIndex = 1;
                            Supplier supplier=ExcelUtil.convertToBean(excelConfig, values, new Supplier(), startIndex);
                            supplier.preInsert();
                            suppliers.add(supplier);
                        }
                    }
                    Map allMap=new HashMap();
                    List<Map> allSuppliers=supplierDao.findListByMap(allMap);//获取所有的数据
                    List<Supplier> needInsert=new ArrayList<Supplier>();//需要新增的列表
                    List<Supplier> needUpdate=new ArrayList<Supplier>();//需要更新的列表

                    for(int i=0;i<suppliers.size();i++){
                        Supplier supplier =suppliers.get(i);
                        boolean flag=false;//新增或者是修改的标志
                        for (int j=0;j<allSuppliers.size();j++){
                            Map c = allSuppliers.get(j);
                            if (supplier.getSupplier_code().equalsIgnoreCase(c.get("supplier_code").toString())){
                                supplier.setId_key(c.get("id_key").toString());//设置主键值
                                flag=true;
                                break;
                            }
                        }
                        if (flag){
                            needUpdate.add(supplier);//加入更新列表
                        }else{
                            needInsert.add(supplier);//加入新增列表
                        }
                    }
                    //新增
                    if(needInsert.size()>0) {
                        int lsize = needInsert.size();
                        if (lsize > 50) {//如果新增的数据量大于50则分段提交
                            List<Supplier> iList = new ArrayList<Supplier>();
                            for (int i = 0, n = lsize; i < n; i++) {
                                iList.add(needInsert.get(i));
                                if ((i > 0 && i % 50 == 0) || i == lsize - 1) {
                                    supplierDao.insertBatch(iList);
                                    iList = new ArrayList<Supplier>();
                                }
                            }
                        } else {
                            supplierDao.insertBatch(suppliers);
                        }
                    }
                    //修改
                    for (int i=0;i<needUpdate.size();i++){
                        Supplier single =needUpdate.get(i);
                        single.preUpdate();
                        supplierDao.update(single);
                    }

                }else if(a[0].equals("devCategory")){//设备类别//=====================================================================>devCategory
                    List<DevCategory> devCategoryList=new ArrayList<DevCategory>();
                    Map<String,Object> excelPreGenIdDataMap = excelConfig.initTreeCacheData(myList);
                    for(int i=0;i<myList.size();i++){
                        readLineLog = i + 3;//从第3行算第一行;
                        err_line = "第" + readLineLog + "行";
                        String[] values = myList.get(i);
                        if (excelConfig != null) {
                            int startIndex = 1;
                            DevCategory devCategory = ExcelUtil.convertToBean(excelConfig, values, new DevCategory(), startIndex);
                            devCategory.preInsert();
                            String value = excelConfig.getConfigModel().getTreeColIndex();
                            if(StringUtils.isNotEmpty(value)){
                                List<String> cat_ids = getKeyList(excelPreGenIdDataMap,values[Integer.parseInt(value)]);
                                if(cat_ids.size() > 1){
                                    System.out.println(values[Integer.parseInt(value)]+"存在重复编码");
                                    throw new RuntimeException(ExcelUtil.excelColIndexToStr(Integer.parseInt(value))+"列"+values[Integer.parseInt(value)]+"存在重复编码");
                                }
                                devCategory.setCat_id(cat_ids.get(0));
                            }
                            devCategoryList.add(devCategory);
                        }
                    }
                    Map allMap=new HashMap();
                    List<Map> allDevCategorys=eamDevCategoryDao.findListByMap(allMap);//获取所有的数据
                    List<DevCategory> needInsert=new ArrayList<DevCategory>();//需要新增的列表
                    List<DevCategory> needUpdate=new ArrayList<DevCategory>();//需要更新的列表

                    for(int i=0;i<devCategoryList.size();i++){
                        DevCategory devCategory =devCategoryList.get(i);
                        boolean flag=false;//新增或者是修改的标志
                        for (int j=0;j<allDevCategorys.size();j++){
                            Map c = allDevCategorys.get(j);
                            if (devCategory.getCat_code().equalsIgnoreCase(c.get("cat_code").toString())){
                                devCategory.setId_key(c.get("id_key").toString());//设置主键值
                                flag=true;
                                break;
                            }
                        }
                        if (flag){
                            needUpdate.add(devCategory);//加入更新列表
                        }else{
                            needInsert.add(devCategory);//加入新增列表
                        }
                    }
                    //新增
                    if(needInsert.size()>0) {
                        int lsize = needInsert.size();
                        if (lsize > 50) {//如果新增的数据量大于50则分段提交
                            List<DevCategory> iList = new ArrayList<DevCategory>();
                            for (int i = 0, n = lsize; i < n; i++) {
                                iList.add(needInsert.get(i));
                                if ((i > 0 && i % 50 == 0) || i == lsize - 1) {
                                    eamDevCategoryDao.insertBatch(iList);
                                    iList = new ArrayList<DevCategory>();
                                }
                            }
                        } else {
                            eamDevCategoryDao.insertBatch(devCategoryList);
                        }
                    }
                    //修改
                    for (int i=0;i<needUpdate.size();i++){
                        DevCategory single =needUpdate.get(i);
                        single.preUpdate();
                        eamDevCategoryDao.update(single);
                    }
                    eamDevCategoryDao.updateDevCategoryTree();
                }else if(a[0].equals("device")){//设备信息//=====================================================================>device
                    List<Device> deviceList = new ArrayList<Device>();
                    for(int i = 0;i < myList.size();i++){
                        readLineLog = i + 3;//从第3行算第一行;
                        err_line = "第" + readLineLog + "行";
                        String[] values = myList.get(i);
                        if (excelConfig != null) {
                            int startIndex = 1;
                            Device device = ExcelUtil.convertToBean(excelConfig, values, new Device(), startIndex);
                            device.preInsert();
                            deviceList.add(device);
                        }
                    }
                    Map allMap=new HashMap();
                    List<Map> allDevices=eamDeviceDao.findListByMap(allMap);//获取所有的数据
                    List<Device> needInsert=new ArrayList<Device>();//需要新增的列表
                    List<Device> needUpdate=new ArrayList<Device>();//需要更新的列表

                    for(int i=0;i<deviceList.size();i++){
                        Device device =deviceList.get(i);
                        boolean flag=false;//新增或者是修改的标志
                        for (int j=0;j<allDevices.size();j++){
                            Map c = allDevices.get(j);
                            if (device.getDev_code().equalsIgnoreCase(c.get("dev_code").toString())){
                                device.setId_key(c.get("id_key").toString());//设置主键值
                                flag=true;
                                break;
                            }
                        }
                        if (flag){
                            needUpdate.add(device);//加入更新列表
                        }else{
                            needInsert.add(device);//加入新增列表
                        }
                    }
                    //新增
                    if(needInsert.size()>0) {
                        int lsize = needInsert.size();
                        if (lsize > 50) {//如果新增的数据量大于50则分段提交
                            List<Device> iList = new ArrayList<Device>();
                            for (int i = 0, n = lsize; i < n; i++) {
                                iList.add(needInsert.get(i));
                                if ((i > 0 && i % 50 == 0) || i == lsize - 1) {
                                    eamDeviceDao.insertBatch(iList);
                                    iList = new ArrayList<Device>();
                                }
                            }
                        } else {
                            eamDeviceDao.insertBatch(deviceList);
                        }
                    }
                    //修改
                    for (int i=0;i<needUpdate.size();i++){
                        Device single =needUpdate.get(i);
                        single.preUpdate();
                        eamDeviceDao.update(single);
                    }

                }else if(a[0].equals("materialType")){//物料类别//=====================================================================>materialType
                    List<MaterialType> materialTypeList = new ArrayList<MaterialType>();
                    Map<String,Object> excelPreGenIdDataMap = excelConfig.initTreeCacheData(myList);
                    for(int i = 0;i < myList.size();i++){
                        readLineLog = i + 3;//从第3行算第一行;
                        err_line = "第" + readLineLog + "行";
                        String[] values = myList.get(i);
                        if (excelConfig != null) {
                            int startIndex = 1;
                            MaterialType materialType = ExcelUtil.convertToBean(excelConfig, values, new MaterialType(), startIndex);
                            materialType.preInsert();
                            //生成主键
                            materialType.setId_key(IdGen.uuid());
                            //生成树节点id
                            String value = excelConfig.getConfigModel().getTreeColIndex();
                            if(StringUtils.isNotEmpty(value)){
                                List<String> cat_ids = getKeyList(excelPreGenIdDataMap,values[Integer.parseInt(value)]);
                                if(cat_ids.size() > 1){
                                    System.out.println(values[Integer.parseInt(value)]+"存在重复编码");
                                    throw new RuntimeException(ExcelUtil.excelColIndexToStr(Integer.parseInt(value))+"列"+values[Integer.parseInt(value)]+"存在重复编码");
                                }
                                materialType.setType_id(cat_ids.get(0));
                            }
                            materialType.setCreate_by(materialType.getCreateBy().getLoginname());
                            materialType.setCreate_time(materialType.getCreateDate());
                            materialTypeList.add(materialType);
                        }
                    }
                    Map allMap=new HashMap();
                    List<Map> allMaterialTypes=materialTypeDao.findListByMap(allMap);//获取所有的数据
                    List<MaterialType> needInsert=new ArrayList<MaterialType>();//需要新增的列表
                    List<MaterialType> needUpdate=new ArrayList<MaterialType>();//需要更新的列表

                    for(int i=0;i<materialTypeList.size();i++){
                        MaterialType materialType =materialTypeList.get(i);
                        boolean flag=false;//新增或者是修改的标志
                        for (int j=0;j<allMaterialTypes.size();j++){
                            Map c = allMaterialTypes.get(j);
                            if (materialType.getType_code().equalsIgnoreCase(c.get("type_code").toString())){
                                materialType.setId_key(c.get("id_key").toString());//设置主键值
                                flag=true;
                                break;
                            }
                        }
                        if (flag){
                            needUpdate.add(materialType);//加入更新列表
                        }else{
                            needInsert.add(materialType);//加入新增列表
                        }
                    }
                    //新增
                    if(needInsert.size()>0) {
                        int lsize = needInsert.size();
                        if (lsize > 50) {//如果新增的数据量大于50则分段提交
                            List<MaterialType> iList = new ArrayList<MaterialType>();
                            for (int i = 0, n = lsize; i < n; i++) {
                                iList.add(needInsert.get(i));
                                if ((i > 0 && i % 50 == 0) || i == lsize - 1) {
                                    materialTypeDao.insertBatch(iList);
                                    iList = new ArrayList<MaterialType>();
                                }
                            }
                        } else {
                            materialTypeDao.insertBatch(materialTypeList);
                        }
                    }
                    //修改
                    for (int i=0;i<needUpdate.size();i++){
                        MaterialType single =needUpdate.get(i);
                        single.preUpdate();
                        materialTypeDao.update(single);
                    }
                    materialTypeDao.updateMaterialTypeTree();
                }else if(a[0].equals("materialInfo")){//物料信息//=====================================================================>materialInfo
                    List<MaterialInfo> materialInfoList = new ArrayList<MaterialInfo>();
                    for(int i = 0;i < myList.size();i++){
                        readLineLog = i + 3;//从第3行算第一行;
                        err_line = "第" + readLineLog + "行";
                        String[] values = myList.get(i);
                        if (excelConfig != null) {
                            int startIndex = 1;
                            MaterialInfo materialInfo = ExcelUtil.convertToBean(excelConfig, values, new MaterialInfo(), startIndex);
                            materialInfo.preInsert();
                            //生成主键
                            materialInfo.setId_key(IdGen.uuid());
                            materialInfo.setCreate_by(materialInfo.getCreateBy().getLoginname());
                            materialInfo.setCreate_time(materialInfo.getCreateDate());
                            materialInfoList.add(materialInfo);
                        }
                    }


                    Map allMap=new HashMap();
                    List<Map> allMaterialInfos=materialInfoDao.findListByMap(allMap);//获取所有的数据
                    List<MaterialInfo> needInsert=new ArrayList<MaterialInfo>();//需要新增的列表
                    List<MaterialInfo> needUpdate=new ArrayList<MaterialInfo>();//需要更新的列表

                    for(int i=0;i<materialInfoList.size();i++){
                        MaterialInfo materialInfo =materialInfoList.get(i);
                        boolean flag=false;//新增或者是修改的标志
                        for (int j=0;j<allMaterialInfos.size();j++){
                            Map c = allMaterialInfos.get(j);
                            if (materialInfo.getMaterial_code().equalsIgnoreCase(c.get("material_code").toString())){
                                materialInfo.setId_key(c.get("id_key").toString());//设置主键值
                                flag=true;
                                break;
                            }
                        }
                        if (flag){
                            needUpdate.add(materialInfo);//加入更新列表
                        }else{
                            needInsert.add(materialInfo);//加入新增列表
                        }
                    }
                    //新增
                    if(needInsert.size()>0) {
                        int lsize = needInsert.size();
                        if (lsize > 50) {//如果新增的数据量大于50则分段提交
                            List<MaterialInfo> iList = new ArrayList<MaterialInfo>();
                            for (int i = 0, n = lsize; i < n; i++) {
                                iList.add(needInsert.get(i));
                                if ((i > 0 && i % 50 == 0) || i == lsize - 1) {
                                    materialInfoDao.insertBatch(iList);
                                    iList = new ArrayList<MaterialInfo>();
                                }
                            }
                        } else {
                            materialInfoDao.insertBatch(materialInfoList);
                        }
                    }
                    //修改
                    for (int i=0;i<needUpdate.size();i++){
                        MaterialInfo single =needUpdate.get(i);
                        single.preUpdate();
                        materialInfoDao.update(single);
                    }
                }else if(a[0].equals("supplierType")){//供应商类型//=====================================================================>supplierType
                    //liwenlong 1355
                    List<SupplierType> supplierTypes = new ArrayList<SupplierType>();
                    for(int i=0;i<myList.size();i++){
                        readLineLog = i + 3;//从第3行算第一行;
                        err_line = "第" + readLineLog + "行";
                        String[] values = myList.get(i);
                        if (excelConfig != null) {
                            int startIndex = 1;
                            SupplierType supplierType=ExcelUtil.convertToBean(excelConfig, values, new SupplierType(), startIndex);
                            supplierType.preInsert();
                            supplierTypes.add(supplierType);
                        }
                    }
                    Map allMap=new HashMap();
                    List<Map> allSupplierTypes=supplierTypeDao.findListByMap(allMap);//获取所有的数据
                    List<SupplierType> needInsert=new ArrayList<SupplierType>();//需要新增的列表
                    List<SupplierType> needUpdate=new ArrayList<SupplierType>();//需要更新的列表

                    for(int i=0;i<supplierTypes.size();i++){
                        SupplierType supplierType =supplierTypes.get(i);
                        boolean flag=false;//新增或者是修改的标志
                        for (int j=0;j<allSupplierTypes.size();j++){
                            Map c = allSupplierTypes.get(j);
                            if (supplierType.getType_code().equalsIgnoreCase(c.get("type_code").toString())){
                                supplierType.setId_key(c.get("id_key").toString());//设置主键值
                                flag=true;
                                break;
                            }
                        }
                        if (flag){
                            needUpdate.add(supplierType);//加入更新列表
                        }else{
                            needInsert.add(supplierType);//加入新增列表
                        }
                    }
                    //新增
                    if(needInsert.size()>0) {
                        int lsize = needInsert.size();
                        if (lsize > 50) {//如果新增的数据量大于50则分段提交
                            List<SupplierType> iList = new ArrayList<SupplierType>();
                            for (int i = 0, n = lsize; i < n; i++) {
                                iList.add(needInsert.get(i));
                                if ((i > 0 && i % 50 == 0) || i == lsize - 1) {
                                    supplierTypeDao.insertBatch(iList);
                                    iList = new ArrayList<SupplierType>();
                                }
                            }
                        } else {
                            supplierTypeDao.insertBatch(supplierTypes);
                        }
                    }
                    //修改
                    for (int i=0;i<needUpdate.size();i++){
                        SupplierType single =needUpdate.get(i);
                        single.preUpdate();
                        supplierTypeDao.update(single);
                    }
                }else if(a[0].equals("standardlib")){  //标准库,多表导入
                    List<String[]> myList1 = UploadExcel.readExcelsByPoi(excelPath, "标准库");
                    List<String[]> myList2 = UploadExcel.readExcelsByPoi(excelPath, "检修标准");
                    List<String[]> myList3 = UploadExcel.readExcelsByPoi(excelPath, "保养标准");
                    List<String[]> myList4 = UploadExcel.readExcelsByPoi(excelPath, "巡检标准");
                    List<String[]> myList5 = UploadExcel.readExcelsByPoi(excelPath, "缺陷故障库");
                    List<String[]> myList6 = UploadExcel.readExcelsByPoi(excelPath, "运行标准");
                    List<String[]> myList7 = UploadExcel.readExcelsByPoi(excelPath, "安全标准");
                    excelConfig = ExcelConfigParser.getConfigById(a[0]);//标准库模版
                    ExcelConfig excelConfigDe = ExcelConfigParser.getConfigById("standardDevice");//中间表模版
                    ExcelConfig excelConfigFa = ExcelConfigParser.getConfigById("standardfault");//检修
                    ExcelConfig excelConfigMa= ExcelConfigParser.getConfigById("standardmaintain");//保养
                    ExcelConfig excelConfigPa= ExcelConfigParser.getConfigById("standardpatrol");//巡检
                    ExcelConfig excelConfigFi= ExcelConfigParser.getConfigById("standardfail");//故障库
                    ExcelConfig excelConfigOp= ExcelConfigParser.getConfigById("standardope");//运行
                    ExcelConfig excelConfigSa= ExcelConfigParser.getConfigById("standardsafe");//安全

                    List<StandardLib> standardLibs=new ArrayList<StandardLib>();//导入的标准库集合
                    List<StandardDevice> standardDevices=new ArrayList<StandardDevice>();//设备集合
                    List<StandardFault> standardFaults=new ArrayList<StandardFault>();//检修
                    List<StandardMaintain> standardMaintains=new ArrayList<StandardMaintain>();//保养
                    List<StandardPatrol> standardPatrols=new ArrayList<StandardPatrol>();//巡检
                    List<StandardFailure> standardFailures=new ArrayList<StandardFailure>();//故障库
                    List<StandardOpe> standardOpes=new ArrayList<StandardOpe>();//运行
                    List<StandardSafety> standardSafetys=new ArrayList<StandardSafety>();//安全
                    for(int i=0;i<myList1.size();i++){
                        readLineLog=i+3;
                        err_line = "[标准库]第" + readLineLog + "行";
                        String[] values = myList1.get(i);
                        StandardLib standardLib= ExcelUtil.convertToBean(excelConfig, values, new StandardLib(), 1);
                        standardLib.preInsert();
                        String pstid = eamProcessService.startProcessByPdid("library_approvenew","test_material",standardLib.getId(),request);
                        standardLib.setPstid(pstid);
                        standardLibs.add(standardLib);
                        if(values[3]!=null &&!"".equals(values[3])){
                            String [] device=values[3].split(",");
                            for(int j=0;j<device.length;j++) {//插入中间表设备
                                String []devalues={device[j],standardLib.getId()};
                                StandardDevice standardDevice= ExcelUtil.convertToBean(excelConfigDe,devalues,new StandardDevice(),0);
                                standardDevice.setId(IdGen.uuid());
                                standardDevices.add(standardDevice);
                            }
                        }
                    }
                    standardLibDao.insertLibBatch(standardLibs);//导入主表
                    standardLibDao.insertLibDevBatch(standardDevices);
                    for (int i=0;i<myList2.size();i++){
                        readLineLog=i+3;
                        err_line = "[检修标准]第" + readLineLog + "行";
                        String[] values = myList2.get(i);
                        StandardFault standardFault= ExcelUtil.convertToBean(excelConfigFa, values, new StandardFault(), 1);
                        standardFault.setId(IdGen.uuid());
                        standardFaults.add(standardFault);
                    }
                    standardLibDao.insertFault(standardFaults);
                    for (int i=0;i<myList3.size();i++){
                        readLineLog=i+3;
                        err_line = "[保养标准]第" + readLineLog + "行";
                        String[] values = myList3.get(i);
                        StandardMaintain standardMaintain= ExcelUtil.convertToBean(excelConfigMa, values, new StandardMaintain(), 1);
                        standardMaintain.setId(IdGen.uuid());
                        standardMaintains.add(standardMaintain);
                    }
                    standardLibDao.insertMaintain(standardMaintains);
                    for (int i=0;i<myList4.size();i++){
                        readLineLog=i+3;
                        err_line = "[巡检标准]第" + readLineLog + "行";
                        String[] values = myList4.get(i);
                        StandardPatrol standardPatrol=ExcelUtil.convertToBean(excelConfigPa, values, new StandardPatrol(), 1);
                        standardPatrol.setId(IdGen.uuid());
                        standardPatrols.add(standardPatrol);
                    }
                    standardLibDao.insertPatrol(standardPatrols);
                    for (int i=0;i<myList5.size();i++){
                        readLineLog=i+3;
                        err_line = "[缺陷故障库]第" + readLineLog + "行";
                        String[] values = myList5.get(i);
                        StandardFailure standardFailure=ExcelUtil.convertToBean(excelConfigFi, values, new StandardFailure(), 1);
                        standardFailure.setId(IdGen.uuid());
                        standardFailures.add(standardFailure);
                    }
                    standardLibDao.insertFailure(standardFailures);
                    for (int i=0;i<myList6.size();i++){
                        readLineLog=i+3;
                        err_line = "[运行标准]第" + readLineLog + "行";
                        String[] values = myList6.get(i);
                        StandardOpe standardOpe= ExcelUtil.convertToBean(excelConfigOp, values, new StandardOpe(), 1);
                        standardOpe.setId(IdGen.uuid());
                        standardOpes.add(standardOpe);
                    }
                    standardLibDao.insertOpe(standardOpes);
                    for (int i=0;i<myList7.size();i++){
                        readLineLog=i+3;
                        err_line = "[安全标准]第" + readLineLog + "行";
                        String[] values = myList7.get(i);
                        StandardSafety standardSafety=ExcelUtil.convertToBean(excelConfigSa, values, new StandardSafety(), 1);
                        standardSafety.setId(IdGen.uuid());
                        standardSafetys.add(standardSafety);
                    }
                    standardLibDao.insertSafe(standardSafetys);
                    standardLibDao.import_after();
                }
                jsonObject.put("msg","导入成功");
            } catch (Exception e) {
                e.printStackTrace();
                response.setCharacterEncoding("utf-8");
                response.setContentType("text/html; ch想·想·arset=utf-8");
                write.print(err_line+"导入失败["+e.getMessage()+"]");
                jsonObject.put("msg",err_line+"导入失败["+e.getMessage()+"]");
                ImportInterceptor.createFile(name,err_line+"导入失败["+e.getMessage()+"]");
                return jsonObject.toString();
            }
        }else{
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html; charset=utf-8");
            write.print("文件解析失败");
            jsonObject.put("msg","文件解析失败");
            ImportInterceptor.createFile(name,"文件解析失败");
            return jsonObject.toString();
        }
        return jsonObject.toString();
    }





    /**
     * @creator duanfuju
     * @createtime 2017/8/28 9:09
     * @description: 
     * 上传（暂时有问题，用不了）
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void fileup(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
        char fg = File.separatorChar;
        String path = req.getSession().getServletContext().getRealPath("/")+"file";
        int maxPostSize = 1 * 100 * 1024 * 1024;
        MultipartRequest mr = new MultipartRequest(req, path, maxPostSize,
                "UTF-8");
        Enumeration files = mr.getFileNames();
        while (files.hasMoreElements()) {
            String name = (String) files.nextElement();
            File f = mr.getFile(name);
            if (f != null) {
                String fileName = mr.getFilesystemName(name);
                UUID uuid = UUID.randomUUID();
                String uid = uuid.toString().replaceAll("-", "").toLowerCase();
                String type = fileName.substring(fileName.lastIndexOf("."),
                        fileName.length());
                File fs = new File(path + fg + fileName);
                File fsto = new File(path + fg + uid + type);
                fs.renameTo(fsto);
                fs.delete();
                String json="{'uid':'"+uid+"','type':'"+type+"','fileName':'"+fileName+"'}";
                resp.getWriter().write(json);
            }
            break;
        }
    }

    /**
     * 根据value取key值
     * @param map
     * @param value
     * @return
     */
    public List<String> getKeyList(Map<String, Object> map, Object value){
        List<String> keyList = new ArrayList();
        for(String getKey: map.keySet()){
            if(map.get(getKey).equals(value)){
                keyList.add(getKey);
            }
        }
        return keyList;
    }
}
