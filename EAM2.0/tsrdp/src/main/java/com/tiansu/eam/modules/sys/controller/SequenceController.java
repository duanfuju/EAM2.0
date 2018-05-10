package com.tiansu.eam.modules.sys.controller;

import com.thinkgem.jeesite.common.web.BaseController;
import com.tiansu.eam.modules.sys.entity.CodeEnum;
import com.tiansu.eam.modules.sys.utils.SequenceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zhangww
 * @description 获取编码通用方法
 * @create 2017-09-08 11:14
 **/
@Controller
@RequestMapping(value = "${adminPath}/sequence")
public class SequenceController extends BaseController{
    @ResponseBody
    @RequestMapping(value={"getcode"})
    public String getCode(){
        String type=getPara("type");
        String code=SequenceUtils.getBySeqType(CodeEnum.valueOf(type));
        return code;
    }
}
