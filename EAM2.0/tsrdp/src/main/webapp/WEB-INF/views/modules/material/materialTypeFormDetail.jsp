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

            //设置所有字段只读属性
            console.log(parent.formConfig)
			var formField = cloneObject(parent.formConfig);

            $.each(formField, function(index,val){
                //debugger;
                val.readonly = true;
                if(val.name=="type_id"||val.name=="type_pid"){
					val.type="hidden";
                }else if(val.name=="type_status"){
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
                //validate: true,
                fields :formField
            };

            $("#inputForm").ligerForm(formConfig);


			//详情页面字段赋值
            $.ajax({
                type : "post",
                async : false, //同步执行
                url : ctx+"/material/materialType/editObj?id="+parent.editId,
                dataType : "json", //传递数据形式为json
                success : function(data)
                {
                    console.log(data)
                    var editForm  = liger.get("inputForm");
                    editForm.setData(data);
                }
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
