<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>设备信息修改</title>
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

    <script type="text/javascript">
        var typeMaterial = null;
        var selectedMaterialObj = new Array();
        var selectedToolsObj = new Array();
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
            console.log(parent.formConfig);
            var formField = cloneObject(parent.formConfig);
            console.log(formField);

            //初始化下拉树数据
            $.each(formField, function (index, val) {
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
                } else if(val.name == 'loc_name'){
                    val.type="hidden";
                }

                if(val.name == 'cat_id'){
                    val.readonly = true;
                    val.options = {
                        isMultiSelect : true,
                        valueFieldID : "dev_category",
                        valueField : 'id',
                        textField : 'text',
                        treeLeafOnly : false,
                        tree : {
                            url : ctx + '/eam/devCategory/getDevCategoryTree',
                            checkbox : false,
                            parentIcon: null,
                            childIcon: null,
                            idFieldName : 'id',
                            parentIDFieldName : 'pid',
                            ajaxType : 'post',
                            onClick : function (note) {
                                $("input[name='cat_name']").val(text);
                            }
                        }
                    }
                }else if(val.name == 'dev_location') {   //初始化控件位置下拉树数据
                    val.options = {
                        isMultiSelect: true,
                        valueField: 'id',
                        treeLeafOnly: false,
                        tree: {
                            url: common.interfaceUrl.getDevLocationTree,
                            // data:treeData, //存在数据累加
                            checkbox: false,
                            parentIcon: null,
                            childIcon: null,
                            idFieldName: 'id',
                            parentIDFieldName: 'pId',
                            nodeWidth: 200,
                            ajaxType: 'post',
                            textFieldName: 'name',//文本字段名，默认值text
                            onClick: function (note) {
                                console.log(note);
                            }
                        }
                    }
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
            //设备的重要程度下拉初始化
            var devLevelHtml = "<option value=''>请选择</option>";
            $.each(parent.devLevelSelect, function (i, item) {
                devLevelHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
            });
            var dev_levelBox = $("#dev_levelBox");
            dev_levelBox .html(devLevelHtml);
            dev_levelBox.append($("<input></input>").attr("type", "hidden").attr("name", "dev_level"));//设置隐藏域
            dev_levelBox.trigger('change.select2');

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

            //设备的二维码状态下拉初始化
            var qrcodeStatusHtml = "<option value=''>请选择</option>";
            $.each(parent.devQrcodeStatusSelect, function (i, item) {
                qrcodeStatusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
            });
            var dev_qrcode_statusBox = $("#dev_qrcode_statusBox");
            dev_qrcode_statusBox .html(qrcodeStatusHtml);
            dev_qrcode_statusBox.append($("<input></input>").attr("type", "hidden").attr("name", "dev_qrcode_status"));//设置隐藏域
            dev_qrcode_statusBox.trigger('change.select2');

            //设备的归属负责人下拉初始化
            var devEmpHtml = "<option value=''>请选择</option>";
            $.each(parent.empSelect, function (i, item) {
                devEmpHtml += "<option value=\"" + item.loginname + "\">" + item.b_realname + "</option>";
            });
            var dev_empBox = $("#dev_empBox");
            dev_empBox .html(devEmpHtml);
            dev_empBox.append($("<input></input>").attr("type", "hidden").attr("name", "dev_emp"));//设置隐藏域
            dev_empBox.trigger('change.select2');

            //设备的归属专业下拉初始化
            var devMajorHtml = "<option value=''>请选择</option>";
            $.each(parent.devMajorSelect, function (i, item) {
                devMajorHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
            });
            var dev_majorBox = $("#dev_majorBox");
            dev_majorBox .html(devMajorHtml);
            dev_majorBox.append($("<input></input>").attr("type", "hidden").attr("name", "dev_major"));//设置隐藏域
            dev_majorBox.trigger('change.select2');

            //设备的供应商下拉初始化
            var devSupplieHtml = "<option value=''>请选择</option>";
            $.each(parent.supplierSelect, function (i, item) {
                devSupplieHtml += "<option value=\"" + item.id_key + "\">" + item.supplier_name + "</option>";
            });
            var dev_supplierBox = $("#dev_supplierBox");
            dev_supplierBox .html(devSupplieHtml);
            dev_supplierBox.append($("<input></input>").attr("type", "hidden").attr("name", "dev_supplier"));//设置隐藏域
            dev_supplierBox.trigger('change.select2');

            $("#dev_supplierBox").on('change',function () {
                for(var i = 0; i < parent.supplierSelect.length; i++) {
                    if($("#dev_supplierBox").val() == parent.supplierSelect[i].id_key){
                        $("input[name='supplier_linkman']").val(parent.supplierSelect[i].supplier_linkman);
                        $("input[name='supplier_phone']").val(parent.supplierSelect[i].supplier_phone);
                    }
                }
            });

            //设备的维护商下拉初始化
            var devMaintainerHtml = "<option value=''>请选择</option>";
            $.each(parent.supplierSelect, function (i, item) {
                devMaintainerHtml += "<option value=\"" + item.id_key + "\">" + item.supplier_name + "</option>";
            });
            var dev_maintainerBox = $("#dev_maintainerBox");
            dev_maintainerBox .html(devMaintainerHtml);
            dev_maintainerBox.append($("<input></input>").attr("type", "hidden").attr("name", "dev_maintainer"));//设置隐藏域
            dev_maintainerBox.trigger('change.select2');

            $("#dev_maintainerBox").on('change',function () {
                for(var i = 0; i < parent.supplierSelect.length; i++) {
                    if($("#dev_maintainerBox").val() == parent.supplierSelect[i].id_key){
                        $("input[name='maintainer_linkman']").val(parent.supplierSelect[i].supplier_linkman);
                        $("input[name='maintainer_phone']").val(parent.supplierSelect[i].supplier_phone);
                    }
                }
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
                    content: "/a/eam/device/MaterialSelectUI"
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
                    content: "/a/eam/device/MaterialSelectUI"
                });
            });

            var treeData = {};
            common.callAjax('post',false,common.interfaceUrl.getDevLocationTree,"json",{"id" : parent.editId},function(data) {
                treeData = data;
            });

            //给编辑页面字段赋值
            common.callAjax('post',false,ctx + "/eam/device/editObj","json",{"id" : parent.editId},function(data){
                var editForm = liger.get("inputForm");
                editForm.setData(data);
                // 给下拉框设置获取到的值
                var locatId = $($('input[name="dev_location"]')[0]).val();
                $.each(treeData,function(index,d){
                    if(d.id_key ==locatId){
                        locatId = d.loc_name;
                        $($('input[name="dev_location"]')[0]).val(locatId);
                    }
                });
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
                        layer.msg("系统错误",{time: 1000,icon:2});
                    }
                }
                xhr.send();
            }


            // 给备品备件赋值
            common.callAjax('post',true,ctx + "/eam/device/getMaterials","json",{"device_id" : parent.editId, "type_flag" : '1'},function(data){
                // 给已选择的备品备件对象赋值
                selectedMaterialObj = data.materialList ? data.materialList : [];
                // 给下拉框设置获取到的值
                if(data.materialNames != null && data.materialNames != "null"){
                    $("input[name='dev_spareparts']").val(data.materialNames);  //备品备件
                }
                $("input[name='spareparts_ids']").val(data.materialIds);  //设备工器具
            });

            // 给工器具赋值
            common.callAjax('post',true,ctx + "/eam/device/getMaterials","json",{"device_id" : parent.editId, "type_flag" : '2'},function(data){
                // 给已选择的备品备件对象赋值
                selectedToolsObj = data.materialList ? data.materialList : [];

                // 给下拉框设置获取到的值
                if(data.materialNames != null && data.materialNames != "null"){
                    $("input[name='dev_tools']").val(data.materialNames);  //工器具
                }
                $("input[name='tools_ids']").val(data.materialIds);  //设备工器具
            });

            $("#order_levelBox").attr("disabled",true);

            $("#btnSubmit").on("click",function () {
                //给设备状态和二维码状态隐藏域设置值
                $("input[name='dev_status']").val($("#dev_statusBox").val());   //设备状态
                $("input[name='dev_qrcode_status']").val($("#dev_qrcode_statusBox").val());    //设备二维码状态
                $("input[name='dev_supplier']").val($("#dev_supplierBox").val());    //设备供应商
                $("input[name='dev_maintainer']").val($("#dev_maintainerBox").val());    //设备维护商
                $("input[name='dev_emp']").val($("#dev_empBox").val());    //设备归属负责人
                $("input[name='dev_major']").val($("#dev_majorBox").val());    //设备专业
                $("input[name='dev_level']").val($("#dev_levelBox").val());    //设备重要程度

                var form = liger.get('inputForm');
                if (!form.valid()) {
                    form.showInvalid();
                }else{
                    var param = new Object();
                    param.id_key = $("input[name='id_key']").val();   //设备主键id
                    param.dev_category = $("input[name='dev_category']").val();   //设备类别
                    param.dev_code = $("input[name='dev_code']").val();   // 设备编码
                    param.dev_name = $("input[name='dev_name']").val();   //设备名称
                    if($("input[name='dev_location']").length>1){
                        param.dev_location = $($("input[name='dev_location']")[1]).val();     // 设备位置
                    }else{
                        param.dev_location = $("input[name='dev_location']").val();     // 设备位置
                    }

                    param.dev_level_code = $("#dev_levelBox").val();           // 设备重要程度
                    param.dev_emp =  $("#dev_empBox").val();              // 设备归属负责人
                    param.dev_model = $("input[name='dev_model']").val();   //设备型号
                    param.dev_brand = $("input[name='dev_brand']").val();   //设备品牌
                    param.dev_major_code = $("#dev_majorBox").val();       // 设备专业
                    param.dev_param_name = $("input[name='dev_param_name']").val();   //设备参数名
                    param.dev_param_val = $("input[name='dev_param_val']").val();   //设备参数值
                    param.dev_supplier = $("#dev_supplierBox").val();       // 设备供应商
                    param.dev_maintainer = $("#dev_maintainerBox").val();       // 设备维护商
                    param.dev_prodate = $("input[name='dev_prodate']").val();   //设备出厂日期
                    param.dev_buy_time = $("input[name='dev_buy_time']").val();   //设备购入日期
                    param.dev_free_time = $("input[name='dev_free_time']").val();   //设备免费过保日期
                    param.dev_pay_time = $("input[name='dev_pay_time']").val();   //设备付费过保日期
                    param.dev_startdate = $("input[name='dev_startdate']").val();   //设备启动日期
                    param.dev_regect_time = $("input[name='dev_regect_time']").val();   //设备报废日期
                    param.dev_qrcode_status = $("#dev_qrcode_statusBox").val();       // 设备二维码状态
                    param.dev_status = $("#dev_statusBox").val();       // 设备状态
                    var dev_sparepartsIds = $("input[name='spareparts_ids']").val();  //设备备品备件id
                    var dev_toolsIds = $("input[name='tools_ids']").val();  //设备工器具id
                    var dev_sparepartsList = new Array();
                    var dev_toolsList = new Array();

                    var strs1 = dev_sparepartsIds.split(",");
                    var strs2 = dev_toolsIds.split(",");

                    // 给备品备件列表赋值
                    $.each(strs1, function (i, item) {
                        var str = {};
                        if(item != null && item != ''){
                            str.material_id = item;
                            str.type_flag = '1';
                            dev_sparepartsList.push(str);
                        }
                    });
                    // 给工器具列表赋值
                    $.each(strs2, function (i, item) {
                        var str = {};
                        if(item != null && item != ''){
                            str.material_id = item;
                            str.type_flag = '2';
                            dev_toolsList.push(str);
                        }
                    });
                    param.sparePartsList = dev_sparepartsList;
                    param.toolsList = dev_toolsList;

                    common.callAjax('post',false,'${ctx}/eam/device/update',"json",{param :JSON.stringify(param)},function(data) {
                        if (data.flag) {
                            layer.msg(data.msg,{icon:1,time: 1000}, function (index) {
                                parent.$("#mytable").DataTable().ajax.reload(null, false);// 刷新表格数据，分页信息不会重置
//                                parent.$("#mytable").DataTable().ajax.reload();
                                parent.layer.closeAll();
                            });
                        } else {
                            layer.msg(data.msg,{time: 1000,icon:2});
                        }
                    });
                }
            });
        });
    </script>
</head>
<body>
<%--<div class="QR-code-area" style="height: auto !important;">--%>

    <%--<div style="padding: 0 26px;">--%>
        <%--<!-- 图片 -->--%>
        <%--<div class="QR-code-area-logo">--%>
            <%--<img id="QR-code-area-logo"  src=""></img>--%>
        <%--</div>--%>
        <%--<!-- 文字 -->--%>
        <%--<div class="QR-code-area-txt">南京天溯</div>--%>
        <%--<!-- 二维码 -->--%>
        <%--<div class="QR-code" style="text-align:center;"><img id="ewm_display" src="" alt=""></div>--%>
        <%--<!--设备名称和设备编码文字 -->--%>
        <%--<div class="eqpInfo-txt pt10"><p class="QR-code-area-txtP1">设备名称:<span class="ewmmc_display">asdsadas</span></p>--%>
            <%--<p class="QR-code-area-txtP2">设备编码:<span class="ewmcode_display">12312312321313454323</span></p></div>--%>
    <%--</div><!-- 底部阴影条 -->--%>
<%--</div>--%>
<form id="inputForm" action="${ctx}/eam/device/update" method="post" class="form-horizontal">

</form>
<div class="form-actions">
    <input id="btnSubmit" type="button" value="保 存"/>
</div>
</body>
</html>
