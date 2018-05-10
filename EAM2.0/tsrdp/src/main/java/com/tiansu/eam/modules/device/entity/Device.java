package com.tiansu.eam.modules.device.entity;

import com.tiansu.eam.common.persistence.DataEntity;
import com.tiansu.eam.modules.supplier.entity.Supplier;

import java.util.Date;
import java.util.List;

/**
 * 设备信息entity
 * Created by tiansu on 2017/8/8.
 */

public class Device extends DataEntity<Device> {

    private static final long serialVersionUID = 1L;

    private String id_key;            //主键id
    private String dev_code;     //设备编码
    private String dev_name;     //设备名称
    private String dev_location;   //设备位置编号
    private String dev_category;   //设备类别编号
    private String cat_id;         //设备类别编号
    private String cat_name;       //设备所属类别
    private String dev_level;    //设备重要程度
    private String dev_level_code;    //设备重要程度
    private Date dev_expirate_date;   //设备保修过期日期
    private String dev_brand;     //设备品牌
    private String dev_model;     //设备型号
    private String dev_supplier;  //设备供应商
    private String dev_maintainer;  //设备维护商
    private Date dev_startdate;   //设备启用日期
    private Date dev_prodate;       //设备出厂日期
    private String dev_param_val;   //参数值
    private String dev_param_name;  //参数名
    private String dev_emp;          //所属负责人
    private String dev_qrcode_status;  //二维码状态0
    private String dev_status;       //设备状态（1-有效  0-无效）
    private Date dev_buy_time;       //设备购入日期
    private Date dev_free_time;      //免费过保日期
    private Date dev_pay_time;;      //付费过保日期
    private Date dev_regect_time;    //报废日期
    private String dev_pic;           //设备图片
    private String dev_major;         //所属专业
    private String dev_major_code;         //所属专业
    private String isdelete;            //是否删除

    private DevCategory devCategory;   //设备类别
    private DevLocation devLocation;   //设备位置
    private Supplier supplier;         //供应商
    private Supplier maintainer;       //设备维护商

    private List<DevSpareParts> sparePartsList;  // 备品备件列表
    private List<DevTools> toolsList;      // 工器具列表

    public Device(String dev_code, String dev_name, String dev_location, String dev_category,String cat_id, String dev_level, String dev_level_code, Date dev_expirate_date, String dev_brand, String dev_model, String dev_supplier, String dev_maintainer, Date dev_startdate, Date dev_prodate, String dev_param_val, String dev_param_name, String dev_emp, String dev_qrcode_status, String dev_status, Date dev_buy_time, Date dev_free_time, Date dev_pay_time, Date dev_regect_time, String dev_pic, String dev_major, String dev_major_code) {
        this.dev_code = dev_code;
        this.dev_name = dev_name;
        this.dev_location = dev_location;
        this.dev_category = dev_category;
        this.cat_id = cat_id;
        this.dev_level = dev_level;
        this.dev_level_code = dev_level_code;
        this.dev_expirate_date = dev_expirate_date;
        this.dev_brand = dev_brand;
        this.dev_model = dev_model;
        this.dev_supplier = dev_supplier;
        this.dev_maintainer = dev_maintainer;
        this.dev_startdate = dev_startdate;
        this.dev_prodate = dev_prodate;
        this.dev_param_val = dev_param_val;
        this.dev_param_name = dev_param_name;
        this.dev_emp = dev_emp;
        this.dev_qrcode_status = dev_qrcode_status;
        this.dev_status = dev_status;
        this.dev_buy_time = dev_buy_time;
        this.dev_free_time = dev_free_time;
        this.dev_pay_time = dev_pay_time;
        this.dev_regect_time = dev_regect_time;
        this.dev_pic = dev_pic;
        this.dev_major = dev_major;
        this.dev_major_code = dev_major_code;
    }

    public Device() {
        super();
    }

    public String getId_key() {
        return id_key;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getDev_code() {
        return dev_code;
    }

    public void setDev_code(String dev_code) {
        this.dev_code = dev_code;
    }

    public String getDev_level_code() {
        return dev_level_code;
    }

    public void setDev_level_code(String dev_level_code) {
        this.dev_level_code = dev_level_code;
    }

    public String getDev_major_code() {
        return dev_major_code;
    }

    public void setDev_major_code(String dev_major_code) {
        this.dev_major_code = dev_major_code;
    }

    public String getDev_name() {
        return dev_name;
    }

    public void setDev_name(String dev_name) {
        this.dev_name = dev_name;
    }

    public String getDev_location() {
        return dev_location;
    }

    public void setDev_location(String dev_location) {
        this.dev_location = dev_location;
    }

    public String getDev_category() {
        return dev_category;
    }

    public void setDev_category(String dev_category) {
        this.dev_category = dev_category;
    }

    public String getDev_level() {
        return dev_level;
    }

    public void setDev_level(String dev_level) {
        this.dev_level = dev_level;
    }

    public Date getDev_expirate_date() {
        return dev_expirate_date;
    }

    public void setDev_expirate_date(Date dev_expirate_date) {
        this.dev_expirate_date = dev_expirate_date;
    }

    public String getDev_brand() {
        return dev_brand;
    }

    public void setDev_brand(String dev_brand) {
        this.dev_brand = dev_brand;
    }

    public String getDev_model() {
        return dev_model;
    }

    public void setDev_model(String dev_model) {
        this.dev_model = dev_model;
    }

    public String getDev_supplier() {
        return dev_supplier;
    }

    public void setDev_supplier(String dev_supplier) {
        this.dev_supplier = dev_supplier;
    }

    public String getDev_maintainer() {
        return dev_maintainer;
    }

    public void setDev_maintainer(String dev_maintainer) {
        this.dev_maintainer = dev_maintainer;
    }

    public Date getDev_startdate() {
        return dev_startdate;
    }

    public void setDev_startdate(Date dev_startdate) {
        this.dev_startdate = dev_startdate;
    }

    public Date getDev_prodate() {
        return dev_prodate;
    }

    public void setDev_prodate(Date dev_prodate) {
        this.dev_prodate = dev_prodate;
    }

    public String getDev_param_val() {
        return dev_param_val;
    }

    public void setDev_param_val(String dev_param_val) {
        this.dev_param_val = dev_param_val;
    }

    public String getDev_param_name() {
        return dev_param_name;
    }

    public void setDev_param_name(String dev_param_name) {
        this.dev_param_name = dev_param_name;
    }

    public String getDev_emp() {
        return dev_emp;
    }

    public void setDev_emp(String dev_emp) {
        this.dev_emp = dev_emp;
    }

    public String getDev_qrcode_status() {
        return dev_qrcode_status;
    }

    public void setDev_qrcode_status(String dev_qrcode_status) {
        this.dev_qrcode_status = dev_qrcode_status;
    }

    public String getDev_status() {
        return dev_status;
    }

    public void setDev_status(String dev_status) {
        this.dev_status = dev_status;
    }

    public Date getDev_buy_time() {
        return dev_buy_time;
    }

    public void setDev_buy_time(Date dev_buy_time) {
        this.dev_buy_time = dev_buy_time;
    }

    public Date getDev_free_time() {
        return dev_free_time;
    }

    public void setDev_free_time(Date dev_free_time) {
        this.dev_free_time = dev_free_time;
    }

    public Date getDev_pay_time() {
        return dev_pay_time;
    }

    public void setDev_pay_time(Date dev_pay_time) {
        this.dev_pay_time = dev_pay_time;
    }

    public Date getDev_regect_time() {
        return dev_regect_time;
    }

    public void setDev_regect_time(Date dev_regect_time) {
        this.dev_regect_time = dev_regect_time;
    }

    public String getDev_pic() {
        return dev_pic;
    }

    public void setDev_pic(String dev_pic) {
        this.dev_pic = dev_pic;
    }

    public String getDev_major() {
        return dev_major;
    }

    public void setDev_major(String dev_major) {
        this.dev_major = dev_major;
    }

    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }

    public DevCategory getDevCategory() {
        return devCategory;
    }

    public void setDevCategory(DevCategory devCategory) {
        this.devCategory = devCategory;
    }

    public DevLocation getDevLocation() {
        return devLocation;
    }

    public void setDevLocation(DevLocation devLocation) {
        this.devLocation = devLocation;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Supplier getMaintainer() {
        return maintainer;
    }

    public void setMaintainer(Supplier maintainer) {
        this.maintainer = maintainer;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public List<DevSpareParts> getSparePartsList() {
        return sparePartsList;
    }

    public void setSparePartsList(List<DevSpareParts> sparePartsList) {
        this.sparePartsList = sparePartsList;
    }

    public List<DevTools> getToolsList() {
        return toolsList;
    }

    public void setToolsList(List<DevTools> toolsList) {
        this.toolsList = toolsList;
    }
}
