package com.tiansu.eam.common.utils;

/**
 * @creator duanfuju
 * @createtime 2017/8/28 9:06
 * @description: 正则工具类
 */
public class RegularUtils {
    //手机
    public final static String phone="^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";

    //电话号码("XXX-XXXXXXX"、"XXXX-XXXXXXXX"、"XXX-XXXXXXX"、"XXX-XXXXXXXX"、"XXXXXXX"和"XXXXXXXX)
    public final static String tel="^(\\(\\d{3,4}-)|\\d{3.4}-)?\\d{7,8}$";

    //Email地址
    public final static String email="^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    //date日期 yyyy-mmm-dd
    public final static String date="^\\d{4}-\\d{1,2}-\\d{1,2}";

    public static  String getRegular(String type) {
        if (type.equalsIgnoreCase("phone")) {
            return phone;
        } else if (type.equalsIgnoreCase("email")) {
            return email;
        } else if (type.equalsIgnoreCase("tel")) {
            return tel;
        } else if (type.equalsIgnoreCase("date")) {
            return date;
        }
        return "";
    }
}
