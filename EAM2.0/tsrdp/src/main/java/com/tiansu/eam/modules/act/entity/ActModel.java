package com.tiansu.eam.modules.act.entity;


import com.tiansu.eam.common.persistence.DataEntity;

/**
 * Created by marsart on 2017/8/8.
 * 工作流模型entity
 */
public class ActModel extends DataEntity<ActModel> {

    private String modelId;    //模型编号
    private String modelKey;   //模型标识，同一个流程，不同模型的标识应该相同，即对应一个processdefine
    private String modelName;  //模型名称
    private String createTime; //创建时间
    private String version;    //版本
    private String category;   //类型
    private String modifyTime; //修改时间

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelKey() {
        return modelKey;
    }

    public void setModelKey(String modelKey) {
        this.modelKey = modelKey;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
