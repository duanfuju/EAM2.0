package com.tiansu.eam.modules.sys.entity;

/**
 * Created by wangjl on 2017/8/1.
 */

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * Created by wangjl on 2017/7/27.
 * 字段权限
 */
public class MenuFields extends DataEntity<MenuFields> {
    private static final long serialVersionUID = 1L;

    private String id;

    private String menu_id;

    private String field_name;

    private String display_name;

    private String field_type;

    private String validate_type;

    private int sort_order;

    private boolean is_basic;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getField_name() {
        return field_name;
    }

    public void setField_name(String field_name) {
        this.field_name = field_name;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getField_type() {
        return field_type;
    }

    public void setField_type(String field_type) {
        this.field_type = field_type;
    }

    public String getValidate_type() {
        return validate_type;
    }

    public void setValidate_type(String validate_type) {
        this.validate_type = validate_type;
    }

    public int getSort_order() {
        return sort_order;
    }

    public void setSort_order(int sort_order) {
        this.sort_order = sort_order;
    }

    public boolean isIs_basic() {
        return is_basic;
    }

    public void setIs_basic(boolean is_basic) {
        this.is_basic = is_basic;
    }
}
