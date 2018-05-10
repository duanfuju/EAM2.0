<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>物料信息管理</title>
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
        // var groupicon = "/resource/plugins/ligerUI/skins/icons/communication.gif";

        $(function () {
            //创建表单结构
            var formConfig = {
                space : 50, labelWidth : 80 , inputWidth : 200,
                validate: true,
                fields :parent.formConfig
            };

            $("#inputForm").ligerForm(formConfig);

            //物料类型下拉初始化
            $("input[name='materialstatusBox']").ligerComboBox({
                width : 200,
                data: [
                    { name: '有效', id: '1' },
                    { name: '无效', id: '0' },

                ],

                valueFieldID: 'materialtype',
                textField: 'name',
                //value: 'bj',
                //initIsTriggerEvent: false,
                onSelected: function (value)
                {
                    console.log(value)
                }
            });

            //日期初始化
            $("input[name='materialdate']").ligerDateEditor(
                {

                    showTime: true,
                    //format: "MM-dd-yyyy hh:mm:ss",

                });

			//编辑页面字段赋值
            $.ajax({
                type : "post",
                async : false, //同步执行
                url : "/a/material/testMaterial/editObj?id="+parent.editId,
                dataType : "json", //传递数据形式为text
                success : function(data)
                {
                    console.log(data)
                    var editForm  = liger.get("inputForm");
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
<form id="inputForm" action="${ctx}/material/testMaterial/update" method="post" class="form-horizontal">


</form>
<div class="form-actions">

	<input id="btnSubmit"  type="button"  value="保 存"/>
</div>
</body>
</html>
