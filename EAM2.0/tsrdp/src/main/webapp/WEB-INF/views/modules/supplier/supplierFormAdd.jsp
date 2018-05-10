<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>供应商管理</title>
	<meta name="decorator" content="default"/>
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

            var formField = cloneObject(parent.formConfig);

            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields: formField

            };

            //debugger;
            console.log(parent.formConfig)

            $("#inputForm").ligerForm(formConfig);
           liger.get('inputForm').setEnabled('supplier_code' , false);
//获取编码,并置灰
            common.ajaxForCode({type:"SUPPLIER"},false,"text",function(data){
                if (data !="" ||data !=null) {
					$("input[name='supplier_code']").val(data);
                }
            });


            //初始化供应商类型
            $.ajax({
                url:ctx+ '/supplier/supplierType/getsuppliertype',
                type:'post',
                async:false,
                dataType:'json',
                success:function(data){
                    var statusHtml="<option value>请选择</option>";
                    $.each(data, function (i, item) {
                        statusHtml += "<option value=\"" + item.id + "\">" + item.text + "</option>";
                    });
                    $("#supplier_typeBox").html(statusHtml).append($("<input></input>").attr("type", "hidden").attr("name", "supplier_type")).val(1).trigger('change.select2');
                }
            });
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "supplier_level"},function(data){
                var statusHtml="";
                $.each(data, function (i, item) {
                    statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                $("#supplier_levelBox").html(statusHtml).append($("<input></input>").attr("type", "hidden").attr("name", "supplier_level")).val(1).trigger('change.select2');
            });
            //状态
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "common"},function(data){
                var statusHtml="";
                $.each(data, function (i, item) {
                    statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
               $("#supplier_statusBox").html(statusHtml).append($("<input></input>").attr("type", "hidden").attr("name", "supplier_status")).val(1).trigger('change.select2');
            });
            // 信用等级：	 优秀AAA      良好AA        较好A     一般BBB       BB        差B

            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "credit_level"},function(data){
                var html="";
                $.each(data, function (i, item) {
                    html += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                $("#supplier_creditBox") .html(html).append($("<input></input>").attr("type", "hidden").attr("name", "supplier_credit")).val('AAA').trigger('change.select2');
            });

            $("#btnSubmit").on("click",function () {
				//给隐藏框赋值
                $("input[name='supplier_credit']").val($("#supplier_creditBox").val());
                $("input[name='supplier_status']").val($("#supplier_statusBox").val());
                $("input[name='supplier_level']").val($("#supplier_levelBox").val());
                $("input[name='supplier_type']").val($("#supplier_typeBox").val());
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
							}else if(data=="repeat"){
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
<form id="inputForm" action="${ctx}/supplier/supplier/insert" method="post" class="form-horizontal">


</form>
<div class="form-actions">

	<input id="btnSubmit" type="button" value="提 交"/>
</div>
</body>
</html>
