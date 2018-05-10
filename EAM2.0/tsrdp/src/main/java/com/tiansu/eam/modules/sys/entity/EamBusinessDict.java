package com.tiansu.eam.modules.sys.entity;
import com.tiansu.eam.common.persistence.DataEntity;

/**
 * @author wujh
 * @description 业务字典表
 * @create 2017-11-30 12:50
 **/
public class EamBusinessDict extends DataEntity<EamBusinessDict> {

    private static final long serialVersionUID = 1L;

    private String id_key;           //id
    private String dict_pid;             //父级业务字典
    private String dict_seq;             // 字典序列
    private int level;                   //字典等级
    private String isleaf;               // 是否叶子节点
    private String dict_value;           //数据字典值
    private String dict_name;           //数据字典名称
    private String dict_desc;           //数据字典描述
    private String text;                //数据字典名称 用于select2
    private String dict_status;        // 状态
    private EamBusinessDict eamBusinessDict;     // 父级业务字典

    public EamBusinessDict() {
    }

    public String getId_key() {
        return id_key;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }

    public String getDict_pid() {
        return dict_pid;
    }

    public void setDict_pid(String dict_pid) {
        this.dict_pid = dict_pid;
    }

    public String getDict_seq() {
        return dict_seq;
    }

    public void setDict_seq(String dict_seq) {
        this.dict_seq = dict_seq;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getIsleaf() {
        return isleaf;
    }

    public void setIsleaf(String isleaf) {
        this.isleaf = isleaf;
    }

    public String getDict_value() {
        return dict_value;
    }

    public void setDict_value(String dict_value) {
        this.dict_value = dict_value;
    }

    public String getDict_name() {
        return dict_name;
    }

    public void setDict_name(String dict_name) {
        this.dict_name = dict_name;
    }

    public String getDict_desc() {
        return dict_desc;
    }

    public void setDict_desc(String dict_desc) {
        this.dict_desc = dict_desc;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDict_status() {
        return dict_status;
    }

    public void setDict_status(String dict_status) {
        this.dict_status = dict_status;
    }

    public EamBusinessDict getEamBusinessDict() {
        return eamBusinessDict;
    }

    public void setEamBusinessDict(EamBusinessDict eamBusinessDict) {
        this.eamBusinessDict = eamBusinessDict;
    }
}
