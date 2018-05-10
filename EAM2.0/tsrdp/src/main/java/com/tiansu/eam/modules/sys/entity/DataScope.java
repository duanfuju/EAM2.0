package com.tiansu.eam.modules.sys.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * Created by wangjl on 2017/7/24.
 */
public class DataScope extends DataEntity<DataScope> {
    private static final long serialVersionUID = 1L;

    private String id;

    private String role_id;

    private String data_scope;

    private String custom_detail;

    // 数据范围（1：所有数据；2：所在公司及以下数据；3：所在公司数据；4：所在部门及以下数据；5：所在部门数据；8：仅本人数据；9：按明细设置）
    public static final String DATA_SCOPE_ALL = "1";
    public static final String DATA_SCOPE_COMPANY_AND_CHILD = "2";
    public static final String DATA_SCOPE_COMPANY = "3";
    public static final String DATA_SCOPE_OFFICE_AND_CHILD = "4";
    public static final String DATA_SCOPE_OFFICE = "5";
    public static final String DATA_SCOPE_SELF = "8";
    public static final String DATA_SCOPE_CUSTOM = "9";

    public DataScope(){
        super();
    }

    public DataScope(String role_id, String data_scope, String custom_detail) {
        this.role_id = role_id;
        this.data_scope = data_scope;
        this.custom_detail = custom_detail;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getData_scope() {
        return data_scope;
    }

    public void setData_scope(String data_scope) {
        this.data_scope = data_scope;
    }

    public String getCustom_detail() {
        return custom_detail;
    }

    public void setCustom_detail(String custom_detail) {
        this.custom_detail = custom_detail;
    }
}
