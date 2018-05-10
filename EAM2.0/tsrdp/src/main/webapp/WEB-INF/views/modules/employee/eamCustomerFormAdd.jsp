<%--
  Created by IntelliJ IDEA.
  User: duanfuju
  Date: 2017/8/14
  Time: 17:18
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>客户信息管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(function () {
            //创建表单结构
            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields: parent.formConfig
            };
            console.log(formConfig);

           var  form=$("#inputForm").ligerForm(formConfig);

            //设置默认编码
            var customer_code= $("#customer_code_hidden").val();
            var obj={
                customer_code:customer_code
            };
            form.setData(obj);

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
                customer_statusBox.val(1);//设置默认值
                customer_statusBox.trigger('change.select2');
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
                customer_levelBox.val(0);//设置默认值
                customer_levelBox.trigger('change.select2');
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
                customer_creditBox.val('AAA');//设置默认值
                customer_creditBox.trigger('change.select2');
            });




            //表单提交
            $("#btnSubmit").on("click",function () {
                //给隐藏域赋值
                $("input[name='customer_status']").val($("#customer_statusBox").val());
                $("input[name='customer_level']").val($("#customer_levelBox").val());
                $("input[name='customer_credit']").val($("#customer_creditBox").val());

                var form = liger.get('inputForm');
                if (!form.valid()) {
                    form.showInvalid();
                }else{
                    $("#inputForm").ajaxSubmit({
                        type: 'post',
                        dataType:"text",
                        success:function( data ){
                            if(data=="success"){
                                layer.msg('新增成功！',{icon: 1,time: 1000},function (index) {
                                    parent.$("#mytable").DataTable().ajax.reload();
                                    parent.layer.closeAll();
                                });
                            }else if (data == "timeout") {
                                layer.msg("登录超时或无权限！",{time: 1000,icon:7});
                            } else {
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
<form id="inputForm" action="${ctx}/employee/eamCustomer/insert"  method="post" class="form-horizontal">
</form>
<div class="form-actions">
    <input type="hidden" value="${customer_code_hidden}" id="customer_code_hidden" />
    <input id="btnSubmit" type="button" value="提 交"/>
</div>
</body>
</html>