<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>设备信息详情</title>
    <meta name="decorator" content="default"/>
    <link href="/resource/plugins/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
    <link href="/resource/form.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="/resource/plugins/jQuery/jquery-form.js"></script>
    <script type="text/javascript" src="/resource/plugins/layer/layer.js"></script>

    <script src="/resource/plugins/ligerUI/js/ligerui.all.js"></script>

    <%--表单校验--%>
    <script src="/resource/plugins/ligerUI/jquery-validation/jquery.validate.min.js"></script>
    <script src="/resource/plugins/ligerUI/jquery-validation/jquery.metadata.js"></script>
    <script src="/resource/plugins/ligerUI/jquery-validation/messages_cn.js"></script>
    <script src="/resource/common.js"></script>
    <script src="/resource/plugins/select2/select2.full.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="/resource/plugins/select2/select2.min.css">

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

            //设置所有字段只读属性
            var formField = cloneObject(parent.formConfig);
            //初始化下拉树数据
            $.each(formField, function(index,val){
                val.readonly = true;
                if(val.name == 'cat_id'){
                    val.group = '基础信息';
                } else if (val.name == 'dev_supplier'){
                    val.group = '供应商信息';
                }  else if(val.name == 'dev_maintainer') {
                    val.group = '维护信息';
                } else if(val.name == 'dev_spareparts' ){
                    val.group = '设备清单';
                }

                if(val.name == 'cat_id'){
                    val.type = "hidden";
                } else if(val.name == 'dev_location'){
                    val.type = "hidden";
                }

                if(val.name=="cat_id"){
                    val.options={
                        isMultiSelect:true,
                        valueField: 'id',
                        textField:'name',
                        treeLeafOnly: false,
                        tree: {
                            url:ctx + "/eam/devCategory/getDevCategoryTree",
                            checkbox: false,
                            idFieldName :'id',
                            parentIDFieldName :'pid',
                            parentIcon: null,
                            childIcon: null,
                            ajaxType: 'post',
                            onClick:function (note) {
                                console.log(note.data);
                            }
                        }
                    }
                } else if(val.name == 'dev_location'){  //初始化控件位置下拉树数据
                    val.options = {
                        isMultiSelect : true,
                        valueFieldID : 'dev_location',
                        valueField : 'id_key',
                        treeLeafOnly : false,
                        tree : {
                            url :  common.interfaceUrl.getDevLocationTree,
                            checkbox : false,
                            parentIcon: null,
                            childIcon: null,
                            idFieldName : 'id',
                            parentIDFieldName : 'pId',
                            nodeWidth:200,
                            ajaxType : 'post',
                            onClick : function (note) {
                                console.log(note);
                            }
                        }
                    }
                }
            });

            //创建表单结构
            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                fields :formField
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

            //设备的重要程度下拉初始化
            var devLevelHtml = "<option value=''>请选择</option>";
            $.each(parent.devLevelSelect, function (i, item) {
                devLevelHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
            });
            var dev_levelBox = $("#dev_levelBox");
            dev_levelBox .html(devLevelHtml);
            dev_levelBox.append($("<input></input>").attr("type", "hidden").attr("name", "dev_level"));//设置隐藏域
            dev_levelBox.trigger('change.select2');
            dev_levelBox.prop("disabled", true);  // 设置下拉框为不可编辑模式

            //设备信息状态下拉框初始化
            var statusHtml = "<option value=''>请选择</option>";
            $.each(parent.devStatusSelect, function (i, item) {
                statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
            });
            var dev_statusBox = $("#dev_statusBox");
            dev_statusBox .html(statusHtml);
            dev_statusBox.append($("<input></input>").attr("type", "hidden").attr("name", "dev_status"));//设置隐藏域
            dev_statusBox.val(1);//设置默认值
            dev_statusBox.trigger('change.select2');
            dev_statusBox.prop("disabled", true);  // 设置下拉框为不可编辑模式

            //设备的二维码状态下拉初始化
            var qrcodeStatusHtml = "<option value=''>请选择</option>";
            $.each(parent.devQrcodeStatusSelect, function (i, item) {
                qrcodeStatusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
            });
            var dev_qrcode_statusBox = $("#dev_qrcode_statusBox");
            dev_qrcode_statusBox .html(qrcodeStatusHtml);
            dev_qrcode_statusBox.append($("<input></input>").attr("type", "hidden").attr("name", "dev_qrcode_status"));//设置隐藏域
            dev_qrcode_statusBox.trigger('change.select2');
            dev_qrcode_statusBox.prop("disabled", true);  // 设置下拉框为不可编辑模式

            //设备的归属负责人下拉初始化
            var devEmpHtml = "<option value=''>请选择</option>";
            $.each(parent.empSelect, function (i, item) {
                devEmpHtml += "<option value=\"" + item.loginname + "\">" + item.b_realname + "</option>";
            });
            var dev_empBox = $("#dev_empBox");
            dev_empBox .html(devEmpHtml);
            dev_empBox.append($("<input></input>").attr("type", "hidden").attr("name", "dev_emp"));//设置隐藏域
            dev_empBox.trigger('change.select2');
            dev_empBox.prop("disabled", true);  // 设置下拉框为不可编辑模式

            //设备的归属专业下拉初始化
            var devMajorHtml = "<option value=''>请选择</option>";
            $.each(parent.devMajorSelect, function (i, item) {
                devMajorHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
            });
            var dev_majorBox = $("#dev_majorBox");
            dev_majorBox .html(devMajorHtml);
            dev_majorBox.append($("<input></input>").attr("type", "hidden").attr("name", "dev_major"));//设置隐藏域
            dev_majorBox.trigger('change.select2');
            dev_majorBox.prop("disabled", true);  // 设置下拉框为不可编辑模式

            //设备的供应商下拉初始化
            var devSupplieHtml = "<option value=''>请选择</option>";
            $.each(parent.supplierSelect, function (i, item) {
                devSupplieHtml += "<option value=\"" + item.id_key + "\">" + item.supplier_name + "</option>";
            });
            var dev_supplierBox = $("#dev_supplierBox");
            dev_supplierBox .html(devSupplieHtml);
            dev_supplierBox.append($("<input></input>").attr("type", "hidden").attr("name", "dev_supplier"));//设置隐藏域
            dev_supplierBox.trigger('change.select2');
            dev_supplierBox.prop("disabled", true);  // 设置下拉框为不可编辑模式

            //设备的维护商下拉初始化
            var devMaintainerHtml = "<option value=''>请选择</option>";
            $.each(parent.supplierSelect, function (i, item) {
                devMaintainerHtml += "<option value=\"" + item.id_key + "\">" + item.supplier_name + "</option>";
            });
            var dev_maintainerBox = $("#dev_maintainerBox");
            dev_maintainerBox .html(devMaintainerHtml);
            dev_maintainerBox.append($("<input></input>").attr("type", "hidden").attr("name", "dev_maintainer"));//设置隐藏域
            dev_maintainerBox.trigger('change.select2');
            dev_maintainerBox.prop("disabled", true);  // 设置下拉框为不可编辑模式

            // 详情页面设置时间框为不可编辑
            $("input[name='dev_prodate']").prop("disabled", true);   //设备出厂日期
            $("input[name='dev_buy_time']").prop("disabled", true);   //设备购入日期
            $("input[name='dev_free_time']").prop("disabled", true);   //免费过保日期
            $("input[name='dev_pay_time']").prop("disabled", true);   //付费过保日期
            $("input[name='dev_startdate']").prop("disabled", true);   //设备启动日期
            $("input[name='dev_regect_time']").prop("disabled", true);   //设备报废日期

            // 二维码信息获取并显示（上图标 项目文字  设备二维码图片）
            common.callAjax('post',true,ctx + "/QRCode/getQRCodeConfig","json",{"device_id" : parent.editId},function(data){
                // 给下拉框设置获取到的值
                $("#QR-code-area-logo").attr("src",data.result[0]);
                $(".QR-code-area-txt").html(data.result[1]);
                createEwmPic();
            });

            //生成二维码,返回二维码图片地址
            function createEwmPic(){
                var xhr = new XMLHttpRequest();
                var obj = "TYPE:DEVICE;ID:" + parent.editId;
                xhr.open("get", ctx + "/QRCode/createQRCode?qrdata=" + obj, true);
                xhr.responseType = "blob";
                xhr.onload = function() {
                    if (this.status == 200) {
                        var blob = this.response;
                        $("#ewm_display").attr("src",window.URL.createObjectURL(blob));
                    }else if(this.status == 500){
                        layer.msg("系统错误",{time: 2000,icon:2});
                    }
                }
                xhr.send();
            }

            //给编辑页面字段赋值
            common.callAjax('post',false,ctx + "/eam/device/editObj","json",{"id" : parent.editId},function(data){
                var editForm = liger.get("inputForm");
                editForm.setData(data);

                // 给下拉框设置获取到的值
                $("#dev_statusBox").val(data.dev_status).trigger('change.select2');  //设备状态
                $("#dev_qrcode_statusBox").val(data.dev_qrcode_status).trigger('change.select2');  //设备二维码状态
                $("#dev_supplierBox").val(data.dev_supplier).trigger('change.select2');  //设备供应商
                $("#dev_maintainerBox").val(data.dev_maintainer).trigger('change.select2');  //设备维护商
                $("#dev_empBox").val(data.dev_emp).trigger('change.select2');  //设备归属负责人
                $("#dev_majorBox").val(data.dev_major).trigger('change.select2');  //设备专业
                $("#dev_levelBox").val(data.dev_level).trigger('change.select2');  //设备重要程度
                $(".ewmcode_display").html(data.dev_code);   // 给二维码下的设备编码赋值
                $(".ewmmc_display").html(data.dev_name);   // 给二维码下的设备名称赋值
            });

            // 给备品备件赋值
            common.callAjax('post',true,ctx + "/eam/device/getMaterials","json",{"device_id" : parent.editId, "type_flag" : '1'},function(data){
                // 给已选择的备品备件对象赋值
                selectedMaterialObj = data.materialList ? data.materialList : [];

                // 给下拉框设置获取到的值
                if(data.materialNames != null && data.materialNames != "null"){
                    $("input[name='dev_spareparts']").val(data.materialNames);  //备品备件
                }
                $("input[name='spareparts_ids']").val(data.materialIds);  //备品备件id
            });

            // 给工器具赋值
            common.callAjax('post',true,ctx + "/eam/device/getMaterials","json",{"device_id" : parent.editId, "type_flag" : '2'},function(data){
                // 给已选择的备品备件对象赋值
                selectedToolsObj = data.materialList ? data.materialList : [];

                // 给下拉框设置获取到的值
                if(data.materialNames != null && data.materialNames != "null"){
                    $("input[name='dev_tools']").val(data.materialNames);  //工器具
                }
                $("input[name='tools_ids']").val(data.materialIds);  //设备工器具id
            });

            // 备品备件选择多个物料的弹出框
            $("input[name='dev_spareparts']").append($("<input></input>").attr("type", "hidden").attr("name", "spareparts_ids"));//设置隐藏域
            $("input[name='dev_spareparts']").on("click",function () {
                typeMaterial = '1';    //物料类型 1-备品备件
                layer.open({
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getAreaM(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: "/a/eam/device/MaterialSelectedUI"
                });
            });

            // 工器具选择多个物料的弹出框
            $("input[name='dev_tools']").append($("<input></input>").attr("type", "hidden").attr("name", "tools_ids"));//设置隐藏域
            $("input[name='dev_tools']").on("click",function () {
                typeMaterial = '2';    //物料类型 2-工器具
                layer.open({
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getAreaM(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: "/a/eam/device/MaterialSelectedUI"
                });
            });

            $("#closeBtn").on("click",function () {
                parent.layer.closeAll();
            });

        });


    </script>
</head>
<body>
<%--<div class="QR-code-area" style="height: auto !important;">--%>

    <%--<div style="padding: 0 26px;">--%>
        <%--<!-- 图片 -->--%>
        <%--<div class="QR-code-area-logo">--%>
            <%--<img id="QR-code-area-logo" style="width:180px; height:30px;" src=""></img>--%>
        <%--</div>--%>
        <%--<!-- 文字 -->--%>
        <%--<div class="QR-code-area-txt">南京天溯</div>--%>
        <%--<!-- 二维码 -->--%>
        <%--<div class="QR-code" style="text-align:center;"><img id="ewm_display" src="" alt="" style="width:180px;height:180px;"></div>--%>
        <%--<!--设备名称和设备编码文字 -->--%>
        <%--<div class="eqpInfo-txt pt10"><p class="QR-code-area-txtP1">设备名称:<span class="ewmmc_display">asdsadas</span></p>--%>
            <%--<p class="QR-code-area-txtP2">设备编码:<span class="ewmcode_display">12312312321313454323</span></p></div>--%>
    <%--</div><!-- 底部阴影条 -->--%>
<%--</div>--%>
<form id="inputForm" action="" method="post" class="form-horizontal">


</form>
<div class="form-actions">

    <input id="closeBtn"  type="button"  value="关 闭"/>
</div>
</body>
</html>
