package com.tiansu.eam.modules.sys.entity;

import com.alibaba.druid.proxy.jdbc.ClobProxyImpl;
import com.tiansu.eam.common.persistence.DataEntity;

/**
 * Created by wangjl on 2017/7/27.
 * 字段权限
 */
public class FieldControl extends DataEntity<FieldControl> {
    private static final long serialVersionUID = 1L;

    private String id;

    private String menuno;

    private String menuname;

    private String rolecode;

    private String rolename;

    private Object content;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getMenuno() {
        return menuno;
    }

    public void setMenuno(String menuno) {
        this.menuno = menuno;
    }

    public String getMenuname() {
        return menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }

    public String getRolecode() {
        return rolecode;
    }

    public void setRolecode(String rolecode) {
        this.rolecode = rolecode;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public Object getContent() {
        if(content != null){
            if(content instanceof String){
                return  content;
            }else if(content instanceof ClobProxyImpl){
                ClobProxyImpl cil = (ClobProxyImpl) content;
                try {
                    String con = cil.getRawClob().getSubString(1, (int) cil.getRawClob().length());
                    return con;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        return "";
    }

    public void setContent(Object content) {
        this.content = content;
    }





}
