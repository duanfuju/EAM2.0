package com.tiansu.eam.interfaces.maintain.entity;


import com.tiansu.eam.interfaces.common.OtherChargesJosn;
import com.tiansu.eam.interfaces.common.PersonJson;
import com.tiansu.eam.interfaces.common.Tool_MaterialsJson;

import java.util.List;

/**
 * @ClassName
 * @CreateUser 李豪杰 lihj@tiansu-china.com
 * @CreateDate 2017/11/7 14:25
 * @UpdateDate 2017/11/7 14:25
 * @UpdateUser 李豪杰 lihjlihj@tiansu-china.com
 * @Description
 * @Version V1.0.1
 */
public class MaintainDetailJson extends MaintainListJson {
    private static final long serialVersionUID = 1L;

    /* 保养项描述*/
    private String maintainContent;

    /* 保养项备注*/
    private String maintainNote;

    /* 保养项是否已经反馈*/
    private String flag;

    private List<MaintainContentJson> maintainContentJson;
    /*工序*/
    private List<?> procedure;

    /*安全措施*/
    private List<?> Safe;

    /*工器具*/
    private List<Tool_MaterialsJson> tools;

    /*备件*/
    private List<Tool_MaterialsJson> materials;

    /*人员工时*/
    private List<PersonJson> manHour;

    /*其他费用*/
    private List<OtherChargesJosn> other;

    private List<?> history;

    public List<?> getProcedure() {
        return procedure;
    }

    public void setProcedure(List<?> procedure) {
        this.procedure = procedure;
    }

    public List<?> getSafe() {
        return Safe;
    }

    public void setSafe(List<?> safe) {
        Safe = safe;
    }

    public List<Tool_MaterialsJson> getTools() {
        return tools;
    }

    public void setTools(List<Tool_MaterialsJson> tools) {
        this.tools = tools;
    }

    public List<Tool_MaterialsJson> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Tool_MaterialsJson> materials) {
        this.materials = materials;
    }

    public List<PersonJson> getManHour() {
        return manHour;
    }

    public void setManHour(List<PersonJson> manHour) {
        this.manHour = manHour;
    }

    public List<OtherChargesJosn> getOther() {
        return other;
    }

    public void setOther(List<OtherChargesJosn> other) {
        this.other = other;
    }
    public List<?> getHistory() {
        return history;
    }

    public void setHistory(List<?> history) {
        this.history = history;
    }

    public List<MaintainContentJson> getMaintainContentJson(){return maintainContentJson;}

    public void setMaintainContentJson(List<MaintainContentJson> maintainContentJson) {this.maintainContentJson = maintainContentJson;}

    public String getMaintainContent(){return maintainContent;}

    public void setMaintainContent(String maintainContent){
        if(maintainContent == null){
            maintainContent = "";
        }
        this.maintainContent = maintainContent;
    }

    public String getMaintainNote(){return maintainNote;}

    public void setMaintainNote(String maintainNote){
        if(maintainNote == null){
            maintainNote = "";
        }
        this.maintainNote = maintainNote;
    }

    public String getFlag(){return flag;}

    public void setFlag(String flag){
        if(flag == null){
            flag = "";
        }
        this.flag = flag;
    }

}
