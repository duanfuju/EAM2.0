<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

<html>
<head>
    <title>业务字典录入</title>
    <script src="/resource/plugins/jQuery/jquery-2.2.3.min.js"></script>
    <link href="/resource/plugins/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
    <link href="/resource/form.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="/resource/plugins/jQuery/jquery-form.js"></script>
    <script type="text/javascript" src="/resource/plugins/layer/layer.js"></script>

    <script src="/resource/plugins/ligerUI/js/ligerui.all.js"></script>

    <%--表单校验--%>
    <script src="/resource/plugins/ligerUI/jquery-validation/jquery.validate.min.js"></script>
    <script src="/resource/plugins/ligerUI/jquery-validation/jquery.metadata.js"></script>
    <script src="/resource/plugins/ligerUI/jquery-validation/messages_cn.js"></script>
    <script src="/resource/common.js"></script>
    <script src="/resource/plugins/select2/select2.full.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="/resource/plugins/select2/select2.min.css">
    <link rel="stylesheet" href="/resource/plugins/bootstrap/css/bootstrap.css">

    <script type="text/javascript">
        $(function () {

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

            var formField = cloneObject(parent.formConfig);

            $.each(formField, function(index,val){
                if(val.name=="dict_pid"){
                    val.type="hidden";
                }
                if(val.name=="dict_pvalue" || val.name=="dict_pname"){
                    val.readonly = true;
                }
            });

            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields: formField

            };
            console.log(formConfig);
            $("#inputForm").ligerForm(formConfig);

            //给编辑页面字段赋值
            common.callAjax('post',false,ctx + "/eam/bizDict/editObj","json",{"id" : parent.editId},function(data){
                var editForm = liger.get("inputForm");
                editForm.setData(data);
            });

            $("#btnSubmit").on("click",function () {
                //给隐藏域赋值
                var form = liger.get('inputForm');
                if (!form.valid()) {
                    form.showInvalid();
                }else{
                    //表单提交
                    var param = new Object();
                    param.id_key = parent.editId;
                    param.dict_pid = $("input[name='dict_pid']").attr("data-id");  // 父级业务字典id
                    param.dict_value = $("input[name='dict_value']").val();   //业务字典值
                    param.dict_name = $("input[name='dict_name']").val();   //业务字典名称
                    param.dict_desc = $("input[name='dict_desc']").val();   //业务字典描述

                    common.callAjax('post', false, '${ctx}/eam/bizDict/update', "json", {param: JSON.stringify(param)}, function (data) {
                        if (data.flag) {
                            layer.msg(data.msg,{icon:1,time: 1000}, function (index) {
                                parent.RefreshPagesd();//刷新父页面
                                parent.layer.closeAll();
                            });
                        } else {
                            layer.msg(data.msg,{time: 1000,icon:2});
                        }
                    });
                }
            })
        });
    </script>
</head>
<body>
<form id="inputForm" action="${ctx}/eam/bizDict/update" method="post" class="form-horizontal">

</form>
<div class="form-actions">
    <input id="btnSubmit" type="button" value="保 存"/>
</div>
</body>
</html>
