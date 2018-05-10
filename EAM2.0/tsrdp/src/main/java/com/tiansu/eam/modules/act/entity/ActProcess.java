package com.tiansu.eam.modules.act.entity;

import com.tiansu.eam.common.persistence.DataEntity;

/**
 * Created by marsart on 2017/8/29.
 * 流程entity 对应activiti的流程定义
 * @author q1210
 */
public class ActProcess extends DataEntity<ActProcess> {

    private String processDefineId;   //流程id: 流程KEY:版本:唯一id
    private String processDefineKey;  //流程定义KEY
    private String version;           //流程版本
    private String name;              //流程定义名称
    private String deploymentId;      //部署id
    private String desc;              //流程描述
    private String category;          //流程分类
    private String resourceName;      //流程bpmn20.xml 资源名
    private String diagramResourceName;//流程图资源名
    private int isSuspended;          //是否挂起: 1:挂起,0:未挂起(active)

    public String getProcessDefineId() {
        return processDefineId;
    }

    public void setProcessDefineId(String processDefineId) {
        this.processDefineId = processDefineId;
    }

    public String getProcessDefineKey() {
        return processDefineKey;
    }

    public void setProcessDefineKey(String processDefineKey) {
        this.processDefineKey = processDefineKey;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDiagramResourceName() {
        return diagramResourceName;
    }

    public void setDiagramResourceName(String diagramResourceName) {
        this.diagramResourceName = diagramResourceName;
    }

    public int getIsSuspended() {
        return isSuspended;
    }

    public void setIsSuspended(int isSuspended) {
        this.isSuspended = isSuspended;
    }
}
