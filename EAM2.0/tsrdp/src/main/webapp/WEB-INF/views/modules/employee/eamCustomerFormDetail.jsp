<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>客户信息管理</title>
	<meta name="decorator" content="default"/>
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
			var formField = cloneObject(parent.formConfig);

            $.each(formField, function(index,val){
                //设置只读
                val.readonly = true;

                if(val.name=="type_id"||val.name=="type_pid"){
					val.type="hidden";
                }
            });


            //创建表单结构
            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                //validate: true,
                fields :formField
            };

            $("#inputForm").ligerForm(formConfig);

            //下拉初始化
            //状态：   有效  1       无效  0
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "common"},function(data){
                var statusHtml="";
                $.each(data, function (i, item) {
                    statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                var customer_statusBox= $("#customer_statusBox");
                customer_statusBox .html(statusHtml);
                customer_statusBox.append($("<input></input>").attr("type", "hidden").attr("name", "customer_status"));//设置隐藏域

            });
            //客户分类：重点客户 1       一般客户 0
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "customer_level"},function(data){
                var html="";
                $.each(data, function (i, item) {
                    html += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                var customer_levelBox= $("#customer_levelBox");
                customer_levelBox .html(html);
                customer_levelBox.append($("<input></input>").attr("type", "hidden").attr("name", "customer_level"));//设置隐藏域

            });
            // 信用等级：	 优秀AAA      良好AA        较好A     一般BBB       BB        差B
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "credit_level"},function(data){
                var html="";
                $.each(data, function (i, item) {
                    html += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                var customer_creditBox= $("#customer_creditBox");
                customer_creditBox .html(html);
                customer_creditBox.append($("<input></input>").attr("type", "hidden").attr("name", "customer_credit"));//设置隐藏域

            });



            //编辑页面字段赋值
            common.callAjax('post',false,common.interfaceUrl.customerEditObj,"json",{"id":parent.editId},function(data){
                var editForm  = liger.get("inputForm");
                editForm.setData(data);
                //状态下拉设置默认值
                var customer_statusBox=  $("#customer_statusBox");
                customer_statusBox.val(data.customer_status);//设置默认值
                customer_statusBox.trigger('change.select2');
                //客户分类下拉设置默认值
                var customer_levelBox=  $("#customer_levelBox");
                customer_levelBox.val(data.customer_level);//设置默认值
                customer_levelBox.trigger('change.select2');
                //信用等级下拉设置默认值
                var customer_creditBox=  $("#customer_creditBox");
                customer_creditBox.val(data.customer_credit);//设置默认值
                customer_creditBox.trigger('change.select2');

            });
			//设置只读
            $("select").attr("disabled","disabled");
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
