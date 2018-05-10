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
            initForm();
            initData();
            initActEvent();
        }catch (e){
            console.error(e);
        }

    });

    function initForm() {

        var formField = cloneObject(parent.formConfig);
        var g_array = new Array({group:"操作信息"});
        formField = g_array.concat(formField);

        formField.forEach(function (element) {
            //加入校验属性，否则无法进行jquery validate;
           /* if (element.validate == undefined) {
                element.validate = new Array();
            }*/

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
                        },
                        render:function (data) {
                           if("devCategory" == data.type){
                                return "<div style='color:gainsboro'>"+data.text+"</div>";
                            }else{
                                return data.text;
                            }
                        }
                    },
                    onBeforeSelect: function (newvalue){
                        debugger;
                        if(newvalue.data != undefined && 'devCategory' == newvalue.data.type){
                            layer.msg('该节点为设备类别节点，请选择设备节点',{time: 1000,icon:2});
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
        ligerform = $("#inputForm").ligerForm(formConfig);
        //可编辑性设置
        formField.forEach(function (element) {
           if (element.editable == "false") {
                ligerform.setEnabled(element.name, false);
            }
            if (true) {
                ligerform.setVisible(element.name, true);
            }
        });
    }



    function initData() {




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

        //获取默认编码
        console.log('${faultOrder}');

        ligerform.getEditor('order_code').setValue('${faultOrder.order_code}');
        ligerform.getEditor('notifier_source').setValue('${faultOrder.notifier_source}');
        ligerform.getEditor('creator').setValue('${faultOrder.createBy.realname}');

    }

    function initActEvent() {

        //电话输入后，带出人员和部门
        $("input[name='notifier_tel']").change(function(){

            //区号+座机号码+分机号码：
            //var regexpp = /^(0[0-9]{2,3})?([2-9][0-9]{6,7})+([0-9]{1,4})?$/;
            //手机 11位 有号段 13[0-9] 14[57] 15[0-9] 17[0-9] 18[0-9]
            //var regMobile = /^(13[0-9]|14[57]|15[0-9]|17[0-9]|18[0-9])\d{8}$/;

              //以上暂时隐去 设置为只可以填数字  想用的时候,可以打开
            var numbers = /^[0-9]*$/;
            if (numbers.test(this.value)) {

            } else {
                $(this).val("");
                layer.msg('电话号码输入错误,请重新输入',{icon:2,time: 2000});
                return;
            }


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


        $("#btnSubmit").on("click",function () {
            //给隐藏域赋值
            var form = liger.get('inputForm');
            if (!form.valid()) {
                form.showInvalid();
            } else{
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
                console.log(JSON.stringify(param));
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
                                layer.msg(data.msg,{icon:1,time: 1000}, function(index){
                                    parent.$("#mytable").DataTable().ajax.reload();
                                    parent.layer.closeAll();
                                });
                            }
                        } catch (e) {
                            console.error(e);
                        }
                    },
                    error: function(data) {
                        var result = data.responseText;
                        layer.msg('操作失败！',{time: 1000,icon:2});
                        debugger;
                        console.error(result);
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

<div id="baseForm" class="liger-form"></div>
<form id="inputForm"  class="form-horizontal">


</form>

<div class="form-actions">
    <input id="btnSubmit" type="button" value="提 交"/>
</div>
</body>
</html>
