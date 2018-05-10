package com.tiansu.eam.modules.device.entity;

import com.tiansu.eam.common.persistence.DataEntity;

import java.util.List;

/**
 * 设备类别entity类
 * Created by tiansu on 2017/8/7.
 */
public class DevCategory extends DataEntity<DevCategory> {

    private static final long serialVersionUID = 1L;

    private String id_key;            //主键id
    private String cat_id;       //设备类别编号
    private String cat_pid;      //设备类别上级
    private String cat_code;     //设备类别编码
    private String cat_name;     //设备类别名称
    private int cat_level;       //设备类别等级
    private String cat_status;   //是否可用
    private String cat_desc;     //设备类型描述
    private String cat_seq;      //设备类型序列
    private char isleaf;      //是否是叶子节点
    private char isdelete;      //是否已删除
    private DevCategory parent;   //设备类型父级节点
    private List<DevCategory> devCategoryList;    //设备类型所有父级节点

   public DevCategory(){
       super();
   }

    public DevCategory(String catId, String catPid, String catCode, String catName, int catLevel, String catStatus, String catDesc, String catSeq, char isleaf) {
        this.cat_id = catId;
        this.cat_pid = catPid;
        this.cat_code = catCode;
        this.cat_name = catName;
        this.cat_level = catLevel;
        this.cat_status = catStatus;
        this.cat_desc = catDesc;
        this.cat_seq = catSeq;
        this.isleaf = isleaf;
    }

    public DevCategory(String catId) {
        super(catId);
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_pid() {
        return cat_pid;
    }

    public void setCat_pid(String cat_pid) {
        this.cat_pid = cat_pid;
    }

    public String getCat_code() {
        return cat_code;
    }

    public void setCat_code(String cat_code) {
        this.cat_code = cat_code;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public int getCat_level() {
        return cat_level;
    }

    public void setCat_level(int cat_level) {
        this.cat_level = cat_level;
    }

    public String getCat_status() {
        return cat_status;
    }

    public void setCat_status(String cat_status) {
        this.cat_status = cat_status;
    }

    public String getCat_desc() {
        return cat_desc;
    }

    public void setCat_desc(String cat_desc) {
        this.cat_desc = cat_desc;
    }

    public String getCat_seq() {
        return cat_seq;
    }

    public void setCat_seq(String cat_seq) {
        this.cat_seq = cat_seq;
    }

    public char getIsleaf() {
        return isleaf;
    }

    public void setIsleaf(char isleaf) {
        this.isleaf = isleaf;
    }

    public List<DevCategory> getDevCategoryList() {
        return devCategoryList;
    }

    public void setDevCategoryList(List<DevCategory> devCategoryList) {
        this.devCategoryList = devCategoryList;
    }

    public DevCategory getParent() {
        return parent;
    }

    public void setParent(DevCategory parent) {
        this.parent = parent;
    }

    public char getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(char isdelete) {
        this.isdelete = isdelete;
    }

    public String getId_key() {
        return id_key;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }
}
