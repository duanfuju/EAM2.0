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

	<%--时间控件--%>
	<script src="/resource/plugins/jQueryUI/jquery-ui.js"></script>
	<link href="/resource/plugins/jQuery-Timepicker-Addon/jquery-ui-timepicker-addon.css" type="text/css" />
	<script src="/resource/plugins/jQuery-Timepicker-Addon/jquery-ui-timepicker-addon.js" type="text/javascript"></script>

	<%--ztree--%>
	<script src="/resource/plugins/ztree/js/jquery.ztree.all.min.js"></script>
	<link rel="stylesheet" href="/resource/plugins/ztree/css/zTreeStyle/zTreeStyle.css">
	<link rel="stylesheet" type="text/css" href="/resource/index.css">

	<script type="text/javascript">
        // var groupicon = "/resource/plugins/ligerUI/skins/icons/communication.gif";

        $(function () {
            //深克隆
            function cloneObject(obj) {
                //var o = obj instanceof Array ? [] : {};
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
            //初始化下拉树数据
            //var formField = cloneObject(parent.formConfig);
            var formField = cloneObject(parent.formConfig);
            $.each(formField, function(index,val){

                if(val.name=="type_id"||val.name=="type_pid"){
                    val.type="hidden";
                }
                if(val.name=="type_pcode"||val.name=="type_pname"){
                    val.readonly = true;
				}
                if(val.name=="type_status"){
                    val.options={
                        data: [
                            { text: '有效', id: '1' },
                            { text: '无效', id: '0' },

                        ]
                    }
                }

            });


            //创建表单结构
            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields :formField
            };

            $("#inputForm").ligerForm(formConfig);
            //$("#type_statusBox").append($("<input></input>").attr("type", "hidden").attr("name", "type_status"));//设置隐藏域

			//编辑页面字段赋值
            $.ajax({
                type : "post",
                async : false, //同步执行
                url : "/a/material/materialType/editObj?id="+parent.editId,
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
                   // $("input[name='type_status']").val($("#type_statusBox").val());//select2赋值
                    $("#inputForm").ajaxSubmit({
                        type: 'post',
                        dataType:"text",
                        success:function( data ){
                            if(data=="success"){
                                layer.msg('修改成功！',{icon:1,time: 1000}, function(index){
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
<form id="inputForm" action="${ctx}/material/materialType/update" method="post" class="form-horizontal">


</form>
<div class="form-actions">

	<input id="btnSubmit"  type="button"  value="保 存"/>
</div>
</body>
</html>
