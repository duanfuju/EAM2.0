<%--
@creator duanfuju
* @createtime 2017/9/18 9:39
* @description: 
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>人员信息管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(function () {
            var formField= parent.formConfig;

            //初始化下拉树数据
            $.each(formField, function (index, val) {
                if(val.name == 'own_area'){//归属区域
                    val.options = {
                        isMultiSelect : true,
                        valueField : 'id',
                        treeLeafOnly : false,
                        tree : {
                            url :  common.interfaceUrl.getDevLocationTree,
                            checkbox : true,
                            parentIcon: null,
                            childIcon: null,
                            idFieldName : 'id',
                            parentIDFieldName : 'pId',
                            nodeWidth:200,
                            ajaxType : 'post',
                            textFieldName:'name',//文本字段名，默认值text
                            autoCheckboxEven:false,//复选框联动
                            onClick : function (note) {

                            }
                        }
                    }
                }else if(val.name == 'duty_area'){//责任区域
                    val.options = {
                        isMultiSelect : true,
                        valueField : 'id',
                        treeLeafOnly : false,
                        tree : {
                            url :  common.interfaceUrl.getDevLocationTree,
                            checkbox : true,
                            parentIcon: null,
                            childIcon: null,
                            idFieldName : 'id',
                            parentIDFieldName : 'pId',
                            nodeWidth:200,
                            ajaxType : 'post',
                            textFieldName:'name',//文本字段名，默认值text
                            autoCheckboxEven:false,//复选框联动
                            onClick : function (note) {

                            }
                        }
                    }
                }else if(val.name == 'major'){//专业
                    val.options = {
                        isMultiSelect : true,
                        valueField : 'dict_value',
                        treeLeafOnly : false,
                        tree : {
                            url :  common.interfaceUrl.getDictByDictTypeCode +"?dict_type_code=dev_major",
                            checkbox : true,
                            parentIcon: null,
                            childIcon: null,
                            nodeWidth:200,
                            ajaxType : 'post',
                            textFieldName:'dict_name',//文本字段名，默认值text
                            autoCheckboxEven:false,//复选框联动
                            onClick : function (note) {

                            }
                        }
                    }
                }
            });


            //创建表单结构
            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields:formField
            };
           var  form=$("#inputForm").ligerForm(formConfig);


            //下拉初始化
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "user_status"},function(data){
                var html="";
                $.each(data, function (i, item) {
                    html += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                var statusBox= $("#statusBox");
                statusBox .html(html);
                statusBox.append($("<input></input>").attr("type", "hidden").attr("name", "status"));//设置隐藏域
                statusBox.val(0);//设置默认值
                statusBox.trigger('change.select2');
            });

            common.callAjax('post',false,common.interfaceUrl.userExtGetAllUser,"json",null,function(data){
                var html="";
                $.each(data, function (i, item) {
                    html += "<option value=\"" + item.id_key + "\">" + item.b_realname + "</option>";
                });
                var higher_authorBox= $("#higher_authorBox");
                higher_authorBox .html(html);
                higher_authorBox.append($("<input></input>").attr("type", "hidden").attr("name", "higher_author"));//设置隐藏域
            });

            //表单提交
            $("#btnSubmit").on("click",function () {
                //给隐藏域赋值
                $("input[name='status']").val($("#statusBox").val());
                $("input[name='higher_author']").val($("#higher_authorBox").val());

                //判断新增的用户是否存在想用的登录名称
                common.callAjax('post',false,common.interfaceUrl.userExtFindByLoginName,"json",{"loginname":$("input[name='loginname']").val()},function(data){
                   if(data.length>0){
                       layer.msg('登录名已存在！',{icon:7, time: 1000});
                       return;
                   }
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
        });
    </script>
</head>
<body>
<form id="inputForm" action="${ctx}/employee/eamUserExt/insert"  method="post" class="form-horizontal">
</form>
<div class="form-actions">
    <input id="btnSubmit" type="button" value="保 存"/>
</div>
</body>
</html>