<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
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
	<%--ztree--%>
	<script src="/resource/plugins/ztree/js/jquery.ztree.all.min.js"></script>
	<link rel="stylesheet" href="/resource/plugins/ztree/css/zTreeStyle/zTreeStyle.css">
	<link rel="stylesheet" type="text/css" href="/resource/index.css">

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

            var formField = cloneObject(parent.formConfig);

			//初始化下拉树数据
            $.each(formField, function(index,val){
                //console.log(val)
				if(val.name=="type_pid"){
                    val.options={
                        isMultiSelect:true,
                        valueField: 'id',
                        textField:'name',
                        treeLeafOnly: false,
                        tree: {
                            url:ctx+"/material/materialType/materialTypeTree",
                            checkbox: false,
                            idFieldName :'id',
                            parentIDFieldName :'pid',

                            parentIcon: null,
                            childIcon: null,
                            ajaxType: 'post',
                            onClick:function (note) {
								//alert(note.name)
                                $.ajax({
                                    type : "post",
                                    //async : false, //同步执行
                                    url : ctx+"/material/materialType/detailObj?id="+note.id_key,
                                    dataType : "json", //传递数据形式为text
                                    "success" : function(data)
                                    {
                                        $("input[name='type_pcode']").val(data.type_code);
                                        $("input[name='type_pname']").val(data.type_name);
                                    }
                                });
                            }
                        }
                    }
				}else if(val.name=="type_status"){
                    val.options={
                        data: [
                            { text: '有效', id: '1' },
                            { text: '无效', id: '0' },

                        ]
                    }
				}

                if(val.name=="type_pcode"||val.name=="type_pname"||val.name=="type_code"){
                    val.readonly = true;
                }

            });

			//创建表单结构
            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields: formField
            };

            console.log(formField);

            $("#inputForm").ligerForm(formConfig);

            //$("#type_statusBox").append($("<input></input>").attr("type", "hidden").attr("name", "type_status"));//设置隐藏域


            //获取编码
            common.ajaxForCode({type:"MATERIAL_TYPE"},false,"text",function(data){
                if (data !="" ||data !=null) {
                    $("input[name='type_code']").val(data);
                }
            });
			//设置默认值
            //$("#type_statusBox").val(1).trigger('change.select2');
            $('#type_statusBox').val(['1']).trigger('change');

            //表单提交
            $("#btnSubmit").on("click",function () {
                //$("input[name='type_status']").val($("#type_statusBox").val());//select2赋值

                var form = liger.get('inputForm');
                if (!form.valid()) {
                    form.showInvalid();
                }else{

					$("#inputForm").ajaxSubmit({
						type: 'post',
						dataType:"text",
						success:function( data ){
							if(data=="success"){
								layer.msg('新增成功！',{icon:1,time: 1000}, function(index){
									parent.$("#mytable").DataTable().ajax.reload();

                                    parent.$("#findNodeA").trigger("click");
									parent.layer.closeAll();
								});
							}else{
                                layer.msg("新增失败！",{time: 1000,icon:2});
							}
                            common.callAjax('post',false,ctx + "/material/materialType/updateMaterialTypeTree","json",null,function(data){
                            });
						}

					});

                }

            });

        });


	</script>
</head>
<body>
<form id="inputForm" action="${ctx}/material/materialType/insert" method="post" class="form-horizontal">


</form>
<div class="form-actions">

	<input id="btnSubmit" type="button" value="提 交"/>
</div>
</body>
</html>
