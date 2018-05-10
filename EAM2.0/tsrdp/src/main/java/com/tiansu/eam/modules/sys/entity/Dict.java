package com.tiansu.eam.modules.sys.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * Created by wujh on 2017/7/26.
 * 数据字典表entity
 */
public class Dict extends DataEntity<Dict> {
    private static final long serialVersionUID = 1L;

    private String id_key;           //id
    private String dict_type_code;       //数据字典类型编码
    private String dict_type_name;      //数据字典类型名称
    private String dict_type_desc;       //数据字典类型描述
    private String dict_value;           //数据字典值
    private String dict_name;           //数据字典名称
    private String dict_desc;           //数据字典描述
    private String text;                //数据字典名称 用于select2

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Dict() {
        super();
    }

    public Dict(String id){
        super(id);
    }

    public String getId_key() {
        return id_key;
    }

    public void setId_key(String id_key) {
        this.id_key = id_key;
    }

    public String getDict_type_code() {
        return dict_type_code;
    }

    public void setDict_type_code(String dict_type_code) {
        this.dict_type_code = dict_type_code;
    }

    public String getDict_type_name() {
        return dict_type_name;
    }

    public void setDict_type_name(String dict_type_name) {
        this.dict_type_name = dict_type_name;
    }

    public String getDict_type_desc() {
        return dict_type_desc;
    }

    public void setDict_type_desc(String dict_type_desc) {
        this.dict_type_desc = dict_type_desc;
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
}
