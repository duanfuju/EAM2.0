package com.tiansu.eam.interfaces.maintain.entity;

import java.io.Serializable;

/**
 * @ClassName
 * @CreateUser 李豪杰 lihj@tiansu-china.com
 * @CreateDate 2017/11/8 8:43
 * @UpdateDate 2017/11/8 8:43
 * @UpdateUser 李豪杰 lihjlihj@tiansu-china.com
 * @Description保养内容
 * @Version V1.0.1
 */
public class MaintainContentJson implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 保养项code*/
    private String maintainCode;

    /* 保养项描述*/
    private String maintainContent;

    /* 保养项备注*/
    private String maintainNote;

    /* 保养项是否已经反馈*/
    private String flag;

    public String getMaintainCode(){return maintainCode;}

    public void setMaintainCode(String maintainCode){
        if(maintainCode == null){
            maintainCode = "";
        }
        this.maintainCode = maintainCode;
    }

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
