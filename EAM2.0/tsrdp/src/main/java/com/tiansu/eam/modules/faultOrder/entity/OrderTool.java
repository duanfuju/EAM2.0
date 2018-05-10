package com.tiansu.eam.modules.faultOrder.entity;

import com.tiansu.eam.common.persistence.DataEntity;
import com.tiansu.eam.modules.device.entity.DevLocation;
import com.tiansu.eam.modules.device.entity.Device;
import com.tiansu.eam.modules.sys.entity.Dept;
import com.tiansu.eam.modules.sys.entity.User;

/**
 * @creator caoh
 * @createtime 2017-10-20 9:12
 * @description: 工器具反馈表
 */
public class OrderTool extends DataEntity<OrderTool> {
    private static final long serialVersionUID = 1L;
    //工单id
    private String order_id;
    //工器具id
    private String tool_id;
    //工器具数量
    private String tool_num;
    //工器具备注
    private String tool_remark;


    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getTool_id() {
        return tool_id;
    }

    public void setTool_id(String tool_id) {
        this.tool_id = tool_id;
    }

    public String getTool_num() {
        return tool_num;
    }

    public void setTool_num(String tool_num) {
        this.tool_num = tool_num;
    }

    public String getTool_remark() {
        return tool_remark;
    }

    public void setTool_remark(String tool_remark) {
        this.tool_remark = tool_remark;
    }



}

