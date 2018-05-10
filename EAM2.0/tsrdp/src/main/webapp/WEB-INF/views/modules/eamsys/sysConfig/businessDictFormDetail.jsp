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
//                if(val.name=="dict_pvalue" || val.name=="dict_pname"){
                val.readonly = true;
//                }
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
            $("#closeBtn").on("click",function () {
                parent.layer.closeAll();
            });
        });
    </script>
</head>
<body>
<form id="inputForm" action="" method="post" class="form-horizontal">

</form>
<div class="form-actions">
    <input id="closeBtn"  type="button"  value="关 闭"/>
</div>
</body>
</html>
