package com.tiansu.eam.modules.sys.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * Created by zhangdf on 2017/7/20.
 */
public class MenuPage extends DataEntity<MenuPage> {
    public String content;//页面信息内容(存储json格式)
    public String analyze_type;//解析类型
    public String menu_id;//关联菜单编号
    public String getContent() {
        return content;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnalyze_type() {
        return analyze_type;
    }

    public void setAnalyze_type(String analyze_type) {
        this.analyze_type = analyze_type;
    }
}
