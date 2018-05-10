package com.tiansu.eam.interfaces.file.entity;

import java.io.Serializable;

/**
 * @ClassName
 * @CreateUser 李豪杰 lihj@tiansu-china.com
 * @CreateDate 2017/11/8 15:35
 * @UpdateDate 2017/11/8 15:35
 * @UpdateUser 李豪杰 lihjlihj@tiansu-china.com
 * @Description
 * @Version V1.0.1
 */
public class AddressPicJson implements Serializable {

    private static final long serialVersionUID = 1L;

    private String addrUrl;

    public String getAddrUrl() {
        return addrUrl;
    }

    public void setAddrUrl(String addrUrl) {
        if(addrUrl==null){
            addrUrl="";
        }
        this.addrUrl = addrUrl;
    }
}
