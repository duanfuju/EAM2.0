<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>供应商管理</title>
	<meta name="decorator" content="default"/>

	<script type="text/javascript">
        // var groupicon = "/resource/plugins/ligerUI/skins/icons/communication.gif";

        $(function () {
            //创建表单结构
            var formConfig = {
                space : 50, labelWidth :120 , inputWidth : 200,
                validate: true,
                fields :parent.formConfig
            };

            $("#inputForm").ligerForm(formConfig);
            liger.get('inputForm').setEnabled('supplier_code' , false);
            //状态初始化

            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "supplier_level"},function(data){
                var statusHtml="";
                $.each(data, function (i, item) {
                    statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                $("#supplier_levelBox").html(statusHtml).append($("<input></input>").attr("type", "hidden").attr("name", "supplier_level")).val(1).trigger('change.select2');
            });
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
			//编辑页面字段赋值
            $.ajax({
                type : "post",
                async : false, //同步执行
                url : "/a/supplier/supplier/editObj?id="+parent.editId,
                dataType : "json", //传递数据形式为text
                success : function(data)
                {
                    data.supplier_busdate_start=DateUtil.dateToStr('yyyy-MM-dd',new Date(data.supplier_busdate_start));
                    data.supplier_busdate_end=DateUtil.dateToStr('yyyy-MM-dd',new Date(data.supplier_busdate_end));
                    var editForm  = liger.get("inputForm");
                    console.log(data);
                    editForm.setData(data);
                   $("#supplier_creditBox").val(data.supplier_credit).trigger('change.select2');
                    $("#supplier_statusBox").val(data.supplier_status).trigger('change.select2');
                    $("#supplier_levelBox").val(data.supplier_level).trigger('change.select2');
                    $("#supplier_typeBox").val(data.supplier_type).trigger('change.select2');
                }
            });

            $("#btnSubmit").on("click",function () {
                $("input[name='supplier_credit']").val($("#supplier_creditBox").val());
                $("input[name='supplier_status']").val($("#supplier_statusBox").val());
                $("input[name='supplier_level']").val($("#supplier_levelBox").val());
                $("input[name='supplier_type']").val($("#supplier_typeBox").val());
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
<form id="inputForm" action="${ctx}/supplier/supplier/update" method="post" class="form-horizontal">


</form>
<div class="form-actions">

	<input id="btnSubmit"  type="button"  value="保 存"/>
</div>
</body>
</html>
