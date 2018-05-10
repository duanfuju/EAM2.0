<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<html>
<head>
    <title>故障工单报修</title>
    <meta name="decorator" content="default"/>
</head>
<body>


<script type="text/javascript">
    var ligerform;

    $(function () {
        try{
            ligerform = liger.get('form1');
            initForm();
            initData();
            initActEvent();
        }catch (e){
            console.error(e);
        }

        //shufq:#17911【故障报修】查看详情页面缺少关闭按钮
        $("#closeBtn").on("click", function () {
            parent.layer.closeAll();
        });
    });

    function initForm() {

        var formField = cloneObject(parent.formConfig);



        //可编辑性设置
        formField.forEach(function (element) {




            if(element.name == 'order_device'){
                //设备树初始化 order_device
                element.options = {
                    isMultiSelect: true,
                    valueField: 'id',
                    treeLeafOnly: true,
                    tree: {
                        url: common.interfaceUrl.getDevTree,
                        // data:treeData, //存在数据累加
                        checkbox: false,
                        parentIcon: null,
                        childIcon: null,
                        idFieldName: 'id',
                        parentIDFieldName: 'pId',
                        nodeWidth: 200,
                        ajaxType: 'post',
                        textFieldName: 'name',//文本字段名，默认值text
                        onClick: function (note) {
                            console.log(note);
                        }
                    },
                    onBeforeSelect: function (newvalue){
                        if(newvalue.data != undefined && 'devCategory' == newvalue.data.type){
                            layer.msg('该节点为设备类别节点，请选择设备节点',{time: 1000,icon:7});
//                            $("input[name='notifier_tel']").val('');
//                            $("#order_device").val('');
                            ligerform.getEditor('order_device').setValue('');
                            return false;
                        }
                     },

                }
            }else if(element.name == 'notifier_dept'){
                //部门树初始化 notifier_dept
                element.options = {
                    isMultiSelect: true,
                    valueField: 'id',
                    treeLeafOnly: false,
                    tree: {
                        url: common.interfaceUrl.getDeptTreeData,
                        // data:treeData, //存在数据累加
                        checkbox: false,
                        parentIcon: null,
                        childIcon: null,
                        idFieldName: 'id',
                        parentIDFieldName: 'pId',
                        nodeWidth: 200,
                        ajaxType: 'post',
                        textFieldName: 'name',//文本字段名，默认值text
                        onClick: function (note) {
                            console.log(note);
                        }
                    }
                }
            }else if(element.name == 'notifier_loc'){
                //空间位置树初始化 notifier_loc
                element.options = {
                    isMultiSelect: true,
                    valueField: 'id',
                    treeLeafOnly: false,
                    tree: {
                        url: common.interfaceUrl.getDevLocationTree,
                        // data:treeData, //存在数据累加
                        checkbox: false,
                        parentIcon: null,
                        childIcon: null,
                        idFieldName: 'id',
                        parentIDFieldName: 'pId',
                        nodeWidth: 200,
                        ajaxType: 'post',
                        textFieldName: 'name',//文本字段名，默认值text
                        onClick: function (note) {
                            console.log(note);
                        }
                    }
                }
            }


        });

        //创建表单结构
        var formConfig = {
            space : 50, labelWidth : 120 , inputWidth : 200,
            validate: true,
            fields: formField
        };
        $("#form1").ligerForm(formConfig);


    }



    function initData() {
        if('view' == '${ptype}'){
            //可编辑性设置
            parent.formConfig.forEach(function (index) {
                ligerform.setEnabled(index.name, false);

                ligerform.setVisible(index.name, true);

            });
            $("#order_levelBox").attr("disabled",true);
            $("#notifier_deptBox").attr("disabled",true);
            $("#notifier_locBox").attr("disabled",true);
            $("#order_deviceBox").attr("disabled",true);
        }






        //处理方式下拉初始化
        common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "repair_order_level"},function(data){
            var statusHtml="";
            $.each(data, function (i, item) {
                statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
            });
            var order_levelBox= $("#order_levelBox");
            order_levelBox .html(statusHtml);
            order_levelBox.append($("<input></input>").attr("type", "hidden").attr("name", "order_level"));//设置隐藏域
            order_levelBox.val(0);//设置默认值
            order_levelBox.trigger('change.select2');
        });

        console.log('${faultOrder}');
        ligerform.getEditor('order_code').setValue('${faultOrder.order_code}');
        ligerform.getEditor('notifier_tel').setValue('${faultOrder.notifier_tel}');
        ligerform.getEditor('notifier').setValue('${faultOrder.notifier}');
        ligerform.getEditor('notifier_no').setValue('${faultOrder.notifier_no}');
        ligerform.getEditor('notifier_appearance').setValue('${faultOrder.notifier_appearance}');
        ligerform.getEditor('notifier_source').setValue('${faultOrder.notifier_source}');
        ligerform.getEditor('order_expect_time').setValue('${faultOrder.order_expect_time}');
        ligerform.getEditor('detail_location').setValue('${faultOrder.detail_location}');
        ligerform.getEditor('notifier_remark').setValue('${faultOrder.notifier_remark}');

        $("#order_levelBox").val('${faultOrder.order_level}').trigger('change.select2');
        $('input[name="notifier_dept"]').val('${faultOrder.notifier_dept.deptno}');
        $('input[name="notifier_loc"]').val('${faultOrder.notifier_loc.id}');
        $('input[name="order_device"]').val('${faultOrder.order_device.id}');
        //shufq:#17879 【故障报修】故障报修，查看详情，填单人员不显示
        ligerform.getEditor('creator').setValue('${faultOrder.createBy.realname}');
        //shufq:#17919 处理方式为预约时应该是预约时间而不是期望解决时间
        if($("#order_levelBox").val()==2){
            $("input[name='order_expect_time']").parent().parent().prev().html('预约时间');
        }
    }

    function initActEvent() {

        //电话输入后，带出人员和部门
        $("input[name='notifier_tel']").change(function(){
            var param = new Object();
            param.loginname = ligerform.getEditor('notifier_tel').getValue();
            common.callAjax('post',false,ctx + "/sys/eamuser/queryUser","json",param,function(data){
                if(data != null){
                    ligerform.getEditor('notifier').setValue(data.realname);
                    ligerform.getEditor('notifier_dept').setValue(data.userdeptno);
                }


            });

        });

        //处理方式改变预约时间label
        $("#order_levelBox").change(function(data){
            if(data.val == 2){
                $("input[name='order_expect_time']").parent().parent().prev().html('预约时间');
            }else{
                $("input[name='order_expect_time']").parent().parent().prev().html('期望解决时间');
            }
        });


        //表单提交
        $("#btnSubmit").on("click", function () {
            debugger;
            if (!ligerform.valid()) {
                ligerform.showInvalid();
            } else {
                //表单提交
                var param = ligerform.getData();
                param.order_level = $("#order_levelBox").val();
                //拼装json格式便于保存
                var notifier_dept = param.notifier_dept;
                param.notifier_dept = new Object();
                param.notifier_dept.deptno = notifier_dept;
                var notifier_loc = param.notifier_loc;
                param.notifier_loc = new Object();
                param.notifier_loc.id = notifier_loc;
                var order_device = param.order_device;
                param.order_device = new Object();
                param.order_device.id = order_device;

                debugger;
                console.log(param);
                var saveData = JSON.stringify(param);
                //定义了contentType，不能调用common.callAjax;
                $.ajax({
                    type: 'post',
                    async: false, //同步执行
                    url: ctx + "/faultOrder/saveOrUpdateType",
                    data:saveData,
                    dataType: "json",
                    contentType: "application/json; charset=utf-8",
                    success: function (data) {
                        try {
                            if(data.flag){
                                layer.msg(data.msg,{icon:1,time: 1000});
                                parent.$("#mytable").DataTable().ajax.reload();
//                                parent.layer.closeAll();
                            }
                        } catch (e) {
                            console.error(e);
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        layer.msg('保存失败',{time: 1000,icon:2});
                    }
                });

                common.callAjax('post', false, ctx + "/faultOrder/saveOrUpdateType", "json", saveData, function (data) {
                    if (data.flag) {
                        layer.msg(data.msg,{icon:1,time: 1000}, function (index) {
                            parent.$("#mytable").DataTable().ajax.reload();
                            parent.layer.closeAll();
                        });
                    } else {
                        layer.msg(data.msg,{time: 1000,icon:2});
                    }
                });
            }
        });
        
    }

    //深克隆
    function cloneObject(obj) {
        var o = obj.length >0 ? [] : {};
        for (var i in obj) {
            if (obj.hasOwnProperty(i)) {
                if (typeof obj[i] === "object" && obj[i] != null) {
                    o[i] = cloneObject(obj[i]);
                } else {
                    o[i] = obj[i];
                }
            }
        }
        return o;
    }

</script>

<div id="form1" class="liger-form">

</div>

<div class="form-actions">
    <c:if test="${ptype != 'view'}">
        <input id="btnSubmit" type="button" value="提 交"/>
    </c:if>

    <c:if test="${ptype == 'view'}">
        <input id="closeBtn" type="button" value="关 闭"/>
    </c:if>

</div>
</body>
</html>
