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
	<%--<script src="/resource/plugins/ligerUI/js/core/base.js" type="text/javascript"></script>--%>

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

            var formField = cloneObject(parent.formConfig);

			//初始化下拉树数据
            $.each(formField, function(index,val){
                console.log(val);
				if(val.name=="material_type"){
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
								//alert(note.data.text)
                               /* $.ajax({
                                    type : "post",
                                    //async : false, //同步执行
                                    url : ctx+"/material/materialType/detailObj?id="+note.data.id_key,
                                    dataType : "json", //传递数据形式为text
                                    "success" : function(data)
                                    {
                                        $("input[name='type_pcode']").val(data.type_pcode);
                                        $("input[name='type_pname']").val(data.type_pname);
                                    }
                                });*/
                            }
                        }
                    }
				}else if(val.name=="material_status"){
                    val.options={
                        data: [
                            { text: '有效', id: '1' },
                            { text: '无效', id: '0' },
                        ]
                    }
                }else if(val.name=="material_level"){
                    val.options={
                        data: [
                            { text: 'A类', id: 'A' },
                            { text: 'B类', id: 'B' },
                            { text: 'C类', id: 'C' },
                        ]
                    }
                }else if(val.name=="material_purchasing"){
                    val.options={
                        data: [
                            { text: '自购', id: '0' },
                            { text: '统购', id: '1' },
                        ]
                    }
                }else if(val.name=="material_supplier"){
                    val.options={
                        data: parent.supplierSelect
                    }
				}

                if(val.name=="type_pcode"||val.name=="type_pname"||val.name=="material_code"){
                    val.readonly = true;
                }

            });

			//创建表单结构
            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields: formField
            };

            $("#inputForm").ligerForm(formConfig);

//            $("#material_purchasingBox").append($("<input></input>").attr("type", "hidden").attr("name", "material_purchasing"));//设置隐藏域
//            $("#material_supplierBox").append($("<input></input>").attr("type", "hidden").attr("name", "material_supplier"));//设置隐藏域
//            $("#material_statusBox").append($("<input></input>").attr("type", "hidden").attr("name", "material_status"));//设置隐藏域
//            $("#material_levelBox").append($("<input></input>").attr("type", "hidden").attr("name", "material_level"));//设置隐藏域

			//获取编码
            common.ajaxForCode({type:"MATERIAL_INFO"},false,"text",function(data){
                if (data !="" ||data !=null) {
                    $("input[name='material_code']").val(data);
                }
            });

			//设置默认值
            $('#material_statusBox').val(['1']).trigger('change');

            //表单提交
            $("#btnSubmit").on("click",function () {

//                $("input[name='material_purchasing']").val($("#material_purchasingBox").val());//select2赋值
//                $("input[name='material_supplier']").val($("#material_supplierBox").val());//select2赋值
//                $("input[name='material_status']").val($("#material_statusBox").val());//select2赋值
//                $("input[name='material_level']").val($("#material_levelBox").val());//select2赋值
                     debugger;
                var form = liger.get('inputForm');
                if (!form.valid()) {
                    debugger;
                    form.showInvalid();
                }else{

					$("#inputForm").ajaxSubmit({
						type: 'post',
						dataType:"text",
						success:function( data ){
							if(data=="success"){
								layer.msg('新增成功！',{icon:1,time: 1000}, function(index){
									parent.$("#mytable").DataTable().ajax.reload();
									parent.layer.closeAll();
								});
							}else{
								layer.msg("新增失败！",{time: 1000,icon:2});
							}
						}
					});

                }

            });

        });


	</script>
</head>
<body>
<form id="inputForm" action="${ctx}/material/materialInfo/insert" method="post" class="form-horizontal">


</form>
<div class="form-actions">

	<input id="btnSubmit" type="button" value="提 交"/>
</div>
</body>
</html>
