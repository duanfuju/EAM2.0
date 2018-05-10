<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>业务字典录入</title>
    <meta name="decorator" content="default"/>
    <link href="/resource/plugins/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
    <link href="/resource/form.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="/resource/plugins/jQuery/jquery-form.js"></script>
    <script type="text/javascript" src="/resource/plugins/layer/layer.js"></script>

    <script src="/resource/plugins/ligerUI/js/ligerui.all.js"></script>

    <%--表单校验--%>
    <script src="/resource/plugins/ligerUI/jquery-validation/jquery.validate.min.js"></script>
    <script src="/resource/plugins/ligerUI/jquery-validation/jquery.metadata.js"></script>
    <script src="/resource/plugins/ligerUI/jquery-validation/messages_cn.js"></script>

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

            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields: formField

            };
            console.log(formConfig);
            $("#inputForm").ligerForm(formConfig);

            $("#btnSubmit").on("click",function () {
                var form = liger.get('inputForm');
                if (!form.valid()) {
                    form.showInvalid();
                }else{
                    //表单提交

                    $("#inputForm").ajaxSubmit({
                        type: 'post',
                        dataType:"text",
                        success:function( data ){
                            if(data=="success"){
                                layer.msg('新增成功！',{icon: 1,time: 1000},function (index) {
                                    parent.$("#mytable").DataTable().ajax.reload();
                                    parent.layer.closeAll();
                                });
                            }else{
                                layer.msg("新增失败！",{time: 1000,icon:2});
                            }
                        }
                    });
                }
            })
        });
    </script>
</head>
<body>
<form id="inputForm" action="${ctx}/eam/bizDict/insert" method="post" class="form-horizontal">

</form>
<div class="form-actions">

    <input id="btnSubmit" type="button" value="提 交"/>
</div>
</body>
</html>
