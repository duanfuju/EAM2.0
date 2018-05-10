<%--
  Created by IntelliJ IDEA.
  User: tiansu
  Date: 2017/8/11
  Time: 13:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>详情</title>
    <meta name="decorator" content="default"/>
    <link href="/resource/plugins/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
    <link href="/resource/form.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="/resource/plugins/jQuery/jquery-form.js"></script>
    <script type="text/javascript" src="/resource/plugins/layer/layer.js"></script>
    <%--	<script src="/resource/plugins/ligerUI/js/core/base.js" type="text/javascript"></script>--%>

    <script src="/resource/plugins/ligerUI/js/ligerui.all.js"></script>

    <%--表单校验--%>
    <script src="/resource/plugins/ligerUI/jquery-validation/jquery.validate.min.js"></script>
    <script src="/resource/plugins/ligerUI/jquery-validation/jquery.metadata.js"></script>
    <script src="/resource/plugins/ligerUI/jquery-validation/messages_cn.js"></script>
    <script src="/resource/common.js"></script>
    <script src="/resource/plugins/select2/select2.full.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="/resource/plugins/select2/select2.min.css">

    <script type="text/javascript">

        $(function(){
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

            //设置所有字段只读属性
            console.log(parent.formConfig)
            var formField = cloneObject(parent.formConfig);

            $.each(formField, function(index,val){
                val.readonly = true;
                //debugger;
                if(val.name=="cat_id" || val.name=="cat_pid"){
                    val.type="hidden";
                 }
                if(val.name=="cat_pcode" || val.name=="cat_pname" || val.name == "cat_code"){
                    val.readonly = true;
                }
            });
            console.log(formField)

            //创建表单结构
            var formConfig = {
                space : 50, labelWidth : 120, inputWidth : 200,
                validate : true,
                fields : formField
            };
            $("#inputForm").ligerForm(formConfig);

            //设备类别状态下拉框初始化
            var statusHtml="";
            $.each(parent.catStatusSelect, function (i, item) {
                statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
            });
            var cat_statusBox= $("#cat_statusBox");
            cat_statusBox .html(statusHtml);
            cat_statusBox.append($("<input></input>").attr("type", "hidden").attr("name", "cat_status"));//设置隐藏域
            cat_statusBox.val(1);//设置默认值
            cat_statusBox.trigger('change.select2');

            //给编辑页面字段赋值
            common.callAjax('post',false,ctx + "/eam/devCategory/editObj","json",{"id" : parent.editId},function(data){
                var editForm = liger.get("inputForm");
                editForm.setData(data);
                $("#cat_statusBox").val(data.cat_status).trigger('change.select2');  //设置默认值

            });

            $("#btnSubmit").on("click",function () {
                //给隐藏域赋值
                $("input[name='cat_status']").val($("#cat_statusBox").val());
                var form = liger.get('inputForm');
                if (!form.valid()) {
                    form.showInvalid();
                }else{
                    $("#inputForm").ajaxSubmit({
                        type: 'post',
                        dataType:"json",
                        success:function( data ){
                            if(data.flag){
                                layer.msg(data.msg,{icon:1,time: 1000}, function (index) {
                                    parent.$("#mytable").DataTable().ajax.reload();
                                    parent.layer.closeAll();
                                });
                            }else{
                                layer.msg(data.msg,{time: 1000,icon:2});
                            }
                        }
                    });
                }
            });

            //设置只读
            $("select").attr("disabled","disabled");
            $("#closeBtn").on("click",function () {
                parent.layer.closeAll();
            });
        });
    </script>
</head>
<body>
    <form id="inputForm" action="${ctx}/eam/devCategory/update" method="post" class="form-horizontal">

    </form>
    <div class="form-actions">
        <input id="btnSubmit" type="button" value="关闭"/>
    </div>
</body>
</html>
