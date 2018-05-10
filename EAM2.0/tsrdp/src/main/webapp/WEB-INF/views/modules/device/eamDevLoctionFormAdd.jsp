<%--
    @creator duanfuju
    * @createtime 2017/9/11 10:53
    * @description:
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>设备位置录入</title>
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
                if(val.name == 'loc_pid'){
                    val.options = {
                        isMultiSelect : true,
                        valueField : 'id',
                        treeLeafOnly : false,
                        tree : {
                           url :  common.interfaceUrl.getDevLocationTree,
                           // data:treeData, //存在数据累加
                            checkbox : false,
                            parentIcon: null,
                            childIcon: null,
                            idFieldName : 'id',
                            parentIDFieldName : 'pId',
                            nodeWidth:200,
                            ajaxType : 'post',
                            textFieldName:'name',//文本字段名，默认值text
                            onClick : function (note) {
                                common.callAjax('post',false,common.interfaceUrl.devLoctionEditObj,"json",{'id' : note.id_key},function(data){
                                    $("input[name='pCode']").val(data.loc_code);
                                    $("input[name='pName']").val(data.loc_name);
                                });
                            }
                        }
                    }
                }else if(val.name == 'loc_dept'){
                    val.options = {
                        isMultiSelect : true,
                        valueField : 'id',
                        treeLeafOnly : false,
                        tree : {
                            url :   common.interfaceUrl.getDeptTreeData,
                            checkbox : true,
                            parentIcon: null,
                            childIcon: null,
                            idFieldName : 'id',
                            parentIDFieldName : 'pId',
                            nodeWidth:200,
                            ajaxType : 'post',
                            autoCheckboxEven:false,//复选框联动
                            onSelect:function(){

                            }
                        }
                    }
                }else  if(val.name == 'loc_status') {
                    val.options = {
                        data : parent._locStatusData
                    }
                }
                if(val.name=="pCode"||val.name=="pName"||val.name=="loc_code"){
                    val.readonly = true;
                }

                if (val.name == 'loc_code') {
                    val.group = '基础信息';
                } else if (val.name == 'loc_pid') {
                    val.group = '上层空间';
                }
            });

            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields: formField

            };
            $("#inputForm").ligerForm(formConfig);


            //空间功能属性
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "function_property"},function(data){
                var html="";
                $.each(data, function (i, item) {
                    html += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                var loc_funcpropBox= $("#loc_funcpropBox");
                loc_funcpropBox .html(html);
                loc_funcpropBox.append($("<input></input>").attr("type", "hidden").attr("name", "loc_funcprop"));//设置隐藏域
            });
            //编码初始化
            common.ajaxForCode({type:"DEV_LOCATION"},false,"text",function(data){
                if (data !="" ||data !=null) {
                    $("input[name='loc_code']").val(data);
                }
            });
            //设置默认值
            $("#loc_statusBox").val(1).trigger('change.select2');

            $("#btnSubmit").on("click",function () {

                //给隐藏域赋值
                $("input[name='loc_status']").val($("#loc_statusBox").val());
                $("input[name='loc_funcprop']").val($("#loc_funcpropBox").val());
                var form = liger.get('inputForm');
                if (!form.valid()) {
                    form.showInvalid();
                }else{
                    //表单提交
                    $("#inputForm").ajaxSubmit({
                        type: 'post',
                        dataType:"text",
                        success:function( data ){
                            try{
                                if(data=="success"){
                                    layer.msg('新增成功！',{icon: 1,time: 1000},function (index) {
                                        parent.RefreshPage();//刷新父页面
                                        parent.layer.closeAll();
                                    });
                                }else if (data == "timeout") {
                                    layer.msg("登录超时或无权限！",{time: 1000,icon:7});
                                } else {
                                    layer.msg("新增失败！",{time: 1000,icon:2});
                                }
                            } catch (e) {
                                console.log(e);
                            }
                        }
                    });
                }
            });

            //电话输入后，带出人员和部门
            $("input[name='loc_tel']").change(function(){
                //区号+座机号码+分机号码：
                //var regexpp = /^(0[0-9]{2,3})?([2-9][0-9]{6,7})+([0-9]{1,4})?$/;
                //手机 11位 有号段 13[0-9] 14[57] 15[0-9] 17[0-9] 18[0-9]
                //var regMobile = /^(13[0-9]|14[57]|15[0-9]|17[0-9]|18[0-9])\d{8}$/;

                //以上暂时隐去 设置为只可以填数字  想用的时候,可以打开
                var numbers = /^[0-9]*$/;
                if (numbers.test(this.value)) {

                } else {
                    $(this).val("");
                    layer.msg('电话号码输入错误,请重新输入',{icon:2,time: 2000});

                }

            });

        });
    </script>
</head>
<body>
<form id="inputForm" action="${ctx}/device/devLoction/insert" method="post" class="form-horizontal">


</form>
<div class="form-actions">

    <input id="btnSubmit" type="button" value="提 交"/>
</div>
</body>
</html>
