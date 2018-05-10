<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>供应商类型管理</title>
	<meta name="decorator" content="default"/>
	<link href="/resource/plugins/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
	<link href="/resource/form.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="/resource/plugins/jQuery/jquery-form.js"></script>
	<script type="text/javascript" src="/resource/plugins/layer/layer.js"></script>
	<%--<script src="/resource/plugins/ligerUI/js/core/base.js" type="text/javascript"></script>--%>

	<script src="/resource/plugins/ligerUI/js/ligerui.all.js"></script>
	<script src="/resource/common.js"></script>
	<%--表单校验--%>
	<script src="/resource/plugins/ligerUI/jquery-validation/jquery.validate.min.js"></script>
	<script src="/resource/plugins/ligerUI/jquery-validation/jquery.metadata.js"></script>
	<script src="/resource/plugins/ligerUI/jquery-validation/messages_cn.js"></script>

	<script type="text/javascript">
        $(function () {
            //创建表单结构
            parent.formConfig.forEach(function(index){
                if(index.type=="combobox"){
                    index.option= {
                        isMultiSelect: true,
                        valueField: 'text',
                        tree: {
                            url: "/resource/data/tree.json",
                            idFieldName: 'text',
                            ajaxType: 'get'
                        }

                    }
                    index.newline=false;

                }

            });

            //delete parent.formConfig[1]["group"];

            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields: parent.formConfig

            };

            //debugger;
            console.log(parent.formConfig)

			console.log(formConfig);
            $("#inputForm").ligerForm(formConfig);
            liger.get('inputForm').setEnabled('type_code' , false);
//获取编码
			common.ajaxForCode({type:"SUPPLIER_TYPE"},false,"text",function(data){
                if (data !="" ||data !=null) {
                    $("input[name='type_code']").val(data);
                }
			})

            //下拉初始化
                common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "common"},function(data){
                var statusHtml="";
                $.each(data, function (i, item) {
                    statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                $("#type_statusBox").html(statusHtml).append($("<input></input>").attr("type", "hidden").attr("name", "type_status")).val(1).trigger('change.select2');
            });

            $("#btnSubmit").on("click",function () {
                $("input[name='type_status']").val($("#type_statusBox").val());
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
							}else if(data=='repeat'){
                                layer.msg("编码重复！",{time: 1000,icon:7});
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
<form id="inputForm" action="${ctx}/supplier/supplierType/insert" method="post" class="form-horizontal">


</form>
<div class="form-actions">

	<input id="btnSubmit" type="button" value="提 交"/>
</div>
</body>
</html>
