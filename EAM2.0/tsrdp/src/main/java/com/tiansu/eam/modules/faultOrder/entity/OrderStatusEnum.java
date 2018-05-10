package com.tiansu.eam.modules.faultOrder.entity;

/**
 * @author wangjl
 * @description 故障工单状态
 * @create 2017-10-18 16:34
 **/
public enum OrderStatusEnum {
//    （1待派单 2待接单 3转单中 4待到达 5挂起中 6已挂起 7处理中 8已完成 9已验收 10 已评价 11已归档）
    PENDING_DISP("1"), PENDING_ACCEPT("2"), PENDING_TRANSFER("3"), PENDING_ARRIVE("4"),
    PENDING_HANGUP("5"), HANGUP("6"), PENDING_FINISH("7"), FINISHED("8"),CHECKED("9"),
    EVALUATEED("10"), ARCHIVED("11");

    private String value;

    private OrderStatusEnum(String value){
        this.value = value;
    }

    public String value(){
        return value;
    }

   /* public static void main(String args[]){
        System.out.println(OrderStatusEnum.ARCHIVED.value());
    }*/
}
