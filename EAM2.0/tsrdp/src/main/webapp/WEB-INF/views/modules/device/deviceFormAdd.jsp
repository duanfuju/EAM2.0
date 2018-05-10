<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>设备录入</title>
    <meta name="decorator" content="default"/>
    <link href="/resource/plugins/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css"/>
    <link href="/resource/form.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="/resource/plugins/jQuery/jquery-form.js"></script>
    <script type="text/javascript" src="/resource/plugins/layer/layer.js"></script>

    <script src="/resource/plugins/ligerUI/js/ligerui.all.js"></script>
    <script src="/resource/plugins/ztree/js/jquery.ztree.all.min.js"></script>
    <link rel="stylesheet" href="/resource/plugins/ztree/css/zTreeStyle/zTreeStyle.css">
    <link rel="stylesheet" type="text/css" href="/resource/form.css">

    <%--表单校验--%>
    <script src="/resource/plugins/ligerUI/jquery-validation/jquery.validate.min.js"></script>
    <script src="/resource/plugins/ligerUI/jquery-validation/jquery.metadata.js"></script>
    <script src="/resource/plugins/ligerUI/jquery-validation/messages_cn.js"></script>
    <script src="/resource/common.js"></script>
    <script src="/resource/plugins/select2/select2.full.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="/resource/plugins/select2/select2.min.css">

    <script type="text/javascript">
        var typeMaterial = null;
        var selectedMaterialObj = new Array();
        var selectedToolsObj = new Array();
        $(function () {
            //深克隆
            function cloneObject(obj) {
                var o = obj.length > 0 ? [] : {};
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
            console.log(formField);

            //初始化设备类型下拉树数据
            $.each(formField, function (index, val) {
                if (val.name == 'cat_id') {
                    val.group = '基础信息';
                } else if (val.name == 'dev_supplier') {
                    val.group = '供应商信息';
                } else if (val.name == 'dev_maintainer') {
                    val.group = '维护信息';
                } else if (val.name == 'dev_spareparts') {
                    val.group = '设备清单';
                }

                if(val.name == 'cat_name'){
                    val.type="hidden";
                } else if(val.name == 'loc_name'){
                    val.type="hidden";
                }

                if (val.name == 'cat_id') {
                    val.options = {
                        isMultiSelect: true,
                        valueField: 'id',
                        treeLeafOnly: false,
                        tree: {
                            url: ctx + '/eam/devCategory/getDevCategoryTree',
                            checkbox: false,
                            parentIcon: null,
                            childIcon: null,
                            idFieldName: 'id',
                            parentIDFieldName: 'pId',
                            nodeWidth: 200,
                            ajaxType: 'post',
                            textFieldName: 'name',//文本字段名，默认值text
                            onClick: function (note) {
                                console.log(note.data);
                            }
                        }
                    }
                } else if (val.name == 'dev_location') {
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

            var formConfig = {
                space: 50, labelWidth: 120, inputWidth: 200,
                validate: true,
                fields: formField
            };
            console.log(formConfig);
            $("#inputForm").ligerForm(formConfig);

            //获取编码
            common.ajaxForCode({type: "DEVICE"}, false, "text", function (data) {
                if (data != "" || data != null) {
                    $("input[name='dev_code']").val(data);
                }
            });

            //设备的重要程度下拉初始化
            var devLevelHtml = "<option value=''>请选择</option>";
            $.each(parent.devLevelSelect, function (i, item) {
                devLevelHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
            });
            var dev_levelBox = $("#dev_levelBox");
            dev_levelBox.html(devLevelHtml);
            dev_levelBox.append($("<input></input>").attr("type", "hidden").attr("name", "dev_level"));//设置隐藏域
            dev_levelBox.trigger('change.select2');

            //设备信息状态下拉框初始化
            var statusHtml = "<option value=''>请选择</option>";
            $.each(parent.devStatusSelect, function (i, item) {
                statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
            });
            var dev_statusBox = $("#dev_statusBox");
            dev_statusBox.html(statusHtml);
            dev_statusBox.append($("<input></input>").attr("type", "hidden").attr("name", "dev_status"));//设置隐藏域
            dev_statusBox.val(1);//设置默认值
            dev_statusBox.trigger('change.select2');

            //设备的二维码状态下拉初始化
            var qrcodeStatusHtml = "<option value=''>请选择</option>";
            $.each(parent.devQrcodeStatusSelect, function (i, item) {
                qrcodeStatusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
            });
            var dev_qrcode_statusBox = $("#dev_qrcode_statusBox");
            dev_qrcode_statusBox.html(qrcodeStatusHtml);
            dev_qrcode_statusBox.append($("<input></input>").attr("type", "hidden").attr("name", "dev_qrcode_status"));//设置隐藏域
            dev_qrcode_statusBox.trigger('change.select2');

            //设备的归属负责人下拉初始化
            var devEmpHtml = "<option value=''>请选择</option>";
            $.each(parent.empSelect, function (i, item) {
                devEmpHtml += "<option value=\"" + item.loginname + "\">" + item.b_realname + "</option>";
            });
            var dev_empBox = $("#dev_empBox");
            dev_empBox.html(devEmpHtml);
            dev_empBox.append($("<input></input>").attr("type", "hidden").attr("name", "dev_emp"));//设置隐藏域
            dev_empBox.trigger('change.select2');

            //设备的归属专业下拉初始化
            var devMajorHtml = "<option value=''>请选择</option>";
            $.each(parent.devMajorSelect, function (i, item) {
                devMajorHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
            });
            var dev_majorBox = $("#dev_majorBox");
            dev_majorBox.html(devMajorHtml);
            dev_majorBox.append($("<input></input>").attr("type", "hidden").attr("name", "dev_major"));//设置隐藏域
            dev_majorBox.trigger('change.select2');

            //设备的供应商下拉初始化
            var devSupplieHtml = "<option value=''>请选择</option>";
            $.each(parent.supplierSelect, function (i, item) {
                devSupplieHtml += "<option value=\"" + item.id_key + "\">" + item.supplier_name + "</option>";
            });
            var dev_supplierBox = $("#dev_supplierBox");
            dev_supplierBox.html(devSupplieHtml);
            dev_supplierBox.append($("<input></input>").attr("type", "hidden").attr("name", "dev_supplier"));//设置隐藏域
            dev_supplierBox.trigger('change.select2');

            $("#dev_supplierBox").on('change', function () {
                for (var i = 0; i < parent.supplierSelect.length; i++) {
                    if ($("#dev_supplierBox").val() == parent.supplierSelect[i].id_key) {
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
            dev_maintainerBox.html(devMaintainerHtml);
            dev_maintainerBox.append($("<input></input>").attr("type", "hidden").attr("name", "dev_maintainer"));//设置隐藏域
            dev_maintainerBox.trigger('change.select2');

            $("#dev_maintainerBox").on('change', function () {
                for (var i = 0; i < parent.supplierSelect.length; i++) {
                    if ($("#dev_maintainerBox").val() == parent.supplierSelect[i].id_key) {
                        $("input[name='maintainer_linkman']").val(parent.supplierSelect[i].supplier_linkman);
                        $("input[name='maintainer_phone']").val(parent.supplierSelect[i].supplier_phone);
                    }
                }
            });

            // 备品备件选择多个物料的弹出框
            $("input[name='dev_spareparts']").append($("<input></input>").attr("type", "hidden").attr("name", "spareparts_ids"));//设置隐藏域
            $("input[name='dev_spareparts']").on("click", function () {
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
            $("input[name='dev_tools']").on("click", function () {
                typeMaterial = '2';    //物料类型 2-工器具
                layer.open({
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getAreaM(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: "/a/eam/device/MaterialSelectUI"
                });
            });

            $("#btnSubmit").on("click", function () {
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
                } else {
                    //表单提交
                    var param = new Object();
                    param.id_key = $("input[name='id_key']").val();   //设备主键id
                    param.dev_category = $("input[name='cat_id']").val();   //设备类别
                    param.dev_code = $("input[name='dev_code']").val();   // 设备编码
                    param.dev_name = $("input[name='dev_name']").val();   //设备名称
                    param.dev_location = $("input[name='dev_location']").val();     // 设备位置
                    param.dev_level = $("#dev_levelBox").val();           // 设备重要程度
                    param.dev_emp = $("#dev_empBox").val();              // 设备归属负责人
                    param.dev_model = $("input[name='dev_model']").val();   //设备型号
                    param.dev_brand = $("input[name='dev_brand']").val();   //设备品牌
                    param.dev_major = $("#dev_majorBox").val();       // 设备专业
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
                    common.callAjax('post', false, '${ctx}/eam/device/insert', "json", {param: JSON.stringify(param)}, function (data) {
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
            })
        });
    </script>
</head>
<body>
<form id="inputForm" action="${ctx}/eam/device/insert" method="post" class="form-horizontal">


</form>
<div class="form-actions">

    <input id="btnSubmit" type="button" value="提 交"/>
</div>
</body>

</html>
