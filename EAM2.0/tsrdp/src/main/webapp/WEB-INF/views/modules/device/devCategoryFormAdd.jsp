<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>设备类别录入</title>
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

            var formField = cloneObject(parent.formConfig);


            //初始化下拉树数据
            $.each(formField, function (index, val) {
                console.log(val);
                if(val.name == 'cat_pid'){
                    val.options = {
                        isMultiSelect : true,
                        valueField : 'id',
                        treeLeafOnly : false,
                        tree : {
                            url : ctx + '/eam/devCategory/getDevCategoryTree',
                            checkbox : false,
                            parentIcon: null,
                            childIcon: null,
                            idFieldName : 'id',
                            parentIDFieldName : 'pId',
                            ajaxType : 'post',
                            onClick : function (note) {
                                common.callAjax('post',false,ctx + "/eam/devCategory/detailObj","json",{'id' : note.id_key},function(data){
                                    $("input[name='cat_pcode']").val(data.cat_code);
                                    $("input[name='cat_pname']").val(data.cat_name);
                                });
                            }
                        }
                    }
                }
                if(val.name=="cat_pcode"){
                    val.readonly = true;
                }else if(val.name=="cat_pname"){
                    val.type = "hidden";
                }
            });

            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields: formField

            };
            console.log(formConfig);
            $("#inputForm").ligerForm(formConfig);

            //状态下拉初始化
            var statusHtml="";
            $.each(parent.catStatusSelect, function (i, item) {
                statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
            });
            var cat_statusBox= $("#cat_statusBox");
            cat_statusBox .html(statusHtml);
            cat_statusBox.append($("<input></input>").attr("type", "hidden").attr("name", "cat_status"));//设置隐藏域
            cat_statusBox.val(1);//设置默认值
            cat_statusBox.trigger('change.select2');

            //获取编码
            common.ajaxForCode({type:"DEV_CATEGORY"},false,"text",function(data){
                if (data != "" || data != null) {
                    $("input[name='cat_code']").val(data);
                }
            });

            $("#btnSubmit").on("click",function () {
                //给隐藏域赋值
                $("input[name='cat_status']").val($("#cat_statusBox").val());
                var form = liger.get('inputForm');
                if (!form.valid()) {
                    form.showInvalid();
                }else{
                    //表单提交

                    $("#inputForm").ajaxSubmit({
                        type: 'post',
                        dataType:"json",
                        success:function( data ){
                            try{
                                if(data.flag){
                                    layer.msg(data.msg,{icon:1,time: 1000}, function(index){
                                        parent.RefreshPagesc();//刷新父页面
                                        parent.layer.closeAll();
                                    });
                                }else{
                                    layer.msg(data.msg,{time: 1000,icon:2});
                                }
                            } catch (e) {
                                console.log(e);
                            }
                        }
                    });
                }
            })
        });
    </script>
</head>
<body>
<form id="inputForm" action="${ctx}/eam/devCategory/insert" method="post" class="form-horizontal">


</form>
<div class="form-actions">

    <input id="btnSubmit" type="button" value="提 交"/>
</div>
</body>
</html>
