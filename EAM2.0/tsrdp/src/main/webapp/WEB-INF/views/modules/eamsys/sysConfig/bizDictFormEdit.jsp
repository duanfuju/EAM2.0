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
    <title>业务字典修改</title>
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

            //创建表单结构
            var formConfig = {
                space : 50, labelWidth : 120, inputWidth : 200,
                validate : true,
                fields : formField
            };
            $("#inputForm").ligerForm(formConfig);

            //给编辑页面字段赋值
            $.ajax({
                type : "post",
                async : false,     //同步执行
                data : {"id" : parent.editId},
                url : ctx+"/eam/bizDict/editObj",
                dataType : "json",
                success : function (data) {
                    console.log(data);
                    var editForm = liger.get("inputForm");
                    editForm.setData(data);
                }
            });

            $("#btnSubmit").on("click",function () {
                var form = liger.get('inputForm');
                if (!form.valid()) {
                    form.showInvalid();
                }else{
                    $("#inputForm").ajaxSubmit({
                        type: 'post',
                        dataType:"text",
                        success:function( data ){
                            if(data=="success"){
                                layer.msg('修改成功！',{icon: 1,time: 1000}, function(index){
                                    parent.$("#mytable").DataTable().ajax.reload();
                                    parent.layer.closeAll();
                                });
                            }else{
                                layer.msg("修改失败！",{time: 1000,icon:2});
                            }
                        }
                    });
                }
            });
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
