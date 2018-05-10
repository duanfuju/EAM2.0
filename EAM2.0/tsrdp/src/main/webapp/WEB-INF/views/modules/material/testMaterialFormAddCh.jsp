<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp" %>
<html>
<head>
    <title>物料信息管理</title>
    <meta name="decorator" content="default"/>
    <link href="/resource/plugins/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css"/>
    <link href="/resource/form.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="/resource/plugins/jQuery/jquery-form.js"></script>
    <script type="text/javascript" src="/resource/plugins/layer/layer.js"></script>
    <%--<script src="/resource/plugins/ligerUI/js/core/base.js" type="text/javascript"></script>--%>

    <script src="/resource/plugins/ligerUI/js/ligerui.all.js"></script>

    <%--表单校验--%>
    <script src="/resource/plugins/ligerUI/jquery-validation/jquery.validate.min.js"></script>
    <script src="/resource/plugins/ligerUI/jquery-validation/jquery.metadata.js"></script>
    <script src="/resource/plugins/ligerUI/jquery-validation/messages_cn.js"></script>

    <%--时间控件--%>
    <script src="/resource/plugins/jQueryUI/jquery-ui.js"></script>
    <link href="/resource/plugins/jQuery-Timepicker-Addon/jquery-ui-timepicker-addon.css" type="text/css"/>
    <script src="/resource/plugins/jQuery-Timepicker-Addon/jquery-ui-timepicker-addon.js"
            type="text/javascript"></script>

    <script type="text/javascript" src="/resource/common.js"></script>
    <script src="/resource/plugins/datatables/jquery.dataTables.min.js" type="text/javascript"></script>
    <script src="/resource/plugins/datatables/dataTables.bootstrap.min.js" type="text/javascript"></script>

    <link rel="stylesheet" type="text/css" href="/resource/plugins/datatables/dataTables.bootstrap.css">
    <link rel="stylesheet" type="text/css" href="/resource/plugins/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="/resource/plugins/font-awesome/css/font-awesome.min.css">
    <script type="text/javascript">
        // var groupicon = "/resource/plugins/ligerUI/skins/icons/communication.gif";
        $(function () {
            debugger;
            /**
             * 查询表格、表单、查询区域的字段权限
             */
            var formField = null;
            $.ajax({
                type: "post",
                async: false, //同步执行
                url: ctx + "/material/testMaterial/getfields?menuno=1102",
                dataType: "json",
                success: function (data) {
                    //表单赋值
                    formField = data.formfield;
                }
            });

            var formConfig = {
                space: 50, labelWidth: 80, inputWidth: 200,
                validate: true,
                fields: formField

            };

            $("#inputForm").ligerForm(formConfig);


            $("#btnSubmit").on("click", function () {
                var form = liger.get('inputForm');
                if (!form.valid()) {
                    form.showInvalid();
                } else {
                    //表单提交

                    $("#inputForm").ajaxSubmit({
                        type: 'post',
                        dataType: "text",
                        success: function (data) {
                            if (data == "success") {
                                layer.msg('新增成功！',{icon: 1,time: 1000},function (index) {
                                    parent.$("#mytable").DataTable().ajax.reload();
                                    parent.layer.closeAll();
                                });
                            } else if (data == "timeout") {
                                layer.msg("登录超时或无权限！",{time: 1000,icon:7});
                            } else {
                                layer.msg("新增失败！",{time: 1000,icon:2});
                            }
                        }
                    });

                }

            })



            $("input[name='materialstatusBox']").ligerComboBox({
                width: 200,
                data: [
                    {name: '有效', id: '1'},
                    {name: '无效', id: '0'}
                ],
                valueFieldID: 'type_status',
                textField: 'name',
                onSelected: function (value) {
                    console.log(value)
                }
            });

        });
    </script>
</head>
<body>
<form id="inputForm" action="${ctx}/material/testMaterial/insert" method="post" class="form-horizontal">

</form>

<div class="form-actions">
    <input id="btnSubmit" type="button" value="保 存"/>
</div>
</body>
</html>
