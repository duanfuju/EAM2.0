
<%--
    @creator duanfuju
    * @createtime 2017/9/11 10:53
    * @description:
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>>空间信息查询</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">

            $(function(){
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

                //设置所有字段只读属性
                var formField = cloneObject(parent.formConfig);

                $.each(formField, function(index,val){
                    if(val.name == 'loc_pid'){
                        val.type="hidden";
                    }
                    if(val.name=="pCode" || val.name=="pName"||val.name == 'loc_pid'){
                        val.readonly = true;
                    }
                    if(val.name == 'loc_dept'){
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
                                textFieldName:'name',//文本字段名，默认值text
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
                    if (val.name == 'loc_code') {
                        val.group = '基础信息';
                    } else if (val.name == 'pCode') {
                        val.group = '上层空间';
                    }
                });

                //创建表单结构
                var formConfig = {
                    space : 50, labelWidth : 120, inputWidth : 200,
                    validate : true,
                    fields : formField
                };
                var qrCodeDisplay = {
                    display: "二维码", name: "qrcode", type: "qrcode", group: "基础信息",
                    option: {
                        data: {
                            qrcode: '',
                            title: "南京天溯",
                            equipname: '',
                            equipcode: '',
                            logo: ''
                        }
                    }
                };
                formConfig.fields.splice(0, 0, qrCodeDisplay);
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
                //给编辑页面字段赋值
                common.callAjax('post',false,common.interfaceUrl.devLoctionEditObj,"json",{"id" : parent.editId},function(data) {
                    var editForm = liger.get("inputForm");
                    editForm.setData(data);
                    //状态下拉设置默认值
                    $("#loc_statusBox").val(data.loc_status).trigger('change.select2');
                    //空间功能属性
                    $("#loc_funcpropBox").val(data.loc_funcprop).trigger('change.select2');

                    $(".QR-code-area-txtP1").html("空间名称:<span class='ewmmc_display'>"+data.loc_name+"</span>");
                    $(".QR-code-area-txtP2").html("空间编码:<span class='ewmcode_display'>"+data.loc_code+"</span>");

                });



                // 二维码信息获取并显示（上图标 项目文字  设备二维码图片）
                common.callAjax('post',true,ctx + "/QRCode/getQRCodeConfig","json",{"device_id" : parent.editId},function(data){
                    // 给下拉框设置获取到的值f
                    $("#QR-code-area-logo").attr("src",data.result[0]);
                    $(".QR-code-area-txt").html(data.result[1]);
                    createEwmPic();
                });

                //生成二维码,返回二维码图片地址
                function createEwmPic(){
                    var xhr = new XMLHttpRequest();
                    var obj = "TYPE:LOCATION;ID:" + parent.editId;
                    xhr.open("get", ctx + "/QRCode/createQRCode?qrdata=" + obj, true);
                    xhr.responseType = "blob";
                    xhr.onload = function() {
                        if (this.status == 200) {
                            var blob = this.response;
                            $("#ewm_display").attr("src",window.URL.createObjectURL(blob));
                        }else if(this.status == 500){
                            layer.msg("系统错误",{time: 1000,icon:2});
                        }
                    };
                    xhr.send();
                }



                $("#btnSubmit").on("click",function () {
                    //给隐藏域赋值
                    $("input[name='loc_status']").val($("#loc_statusBox").val());
                    $("input[name='loc_funcprop']").val($("#loc_funcpropBox").val());
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
                                        parent.RefreshPage();//刷新父页面
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
<form id="inputForm" action="${ctx}/device/devLoction/update" method="post" class="form-horizontal">

</form>
<div class="form-actions">
    <input id="closeBtn"  type="button"  value="关 闭"/>
</div>
</body>
</html>
