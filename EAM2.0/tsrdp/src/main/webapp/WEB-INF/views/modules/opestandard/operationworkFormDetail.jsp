<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>标准工作编辑</title>
    <meta name="decorator" content="default"/>
    <style>
        .select2-container.select2-container-disabled .select2-choice {
            background-color: #fefefe !important;
        }
         .editDiv .l-panel-bar{
             display: none ;
         }
    </style>
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
    <script src="/resource/plugins/datatables/jquery.dataTables.min.js" type="text/javascript"></script>
    <script src="/resource/plugins/datatables/dataTables.bootstrap.min.js" type="text/javascript"></script>

    <link rel="stylesheet" type="text/css" href="/resource/plugins/datatables/dataTables.bootstrap.css">
    <link rel="stylesheet" type="text/css" href="/resource/plugins/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="/resource/plugins/font-awesome/css/font-awesome.min.css">

    <script src="/resource/plugins/select2/select2.full.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="/resource/plugins/select2/select2.min.css">

    <script type="text/javascript">
        $(function() {
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

            //设置所有字段只读属性
            console.log(parent.formConfig)

            var formField = cloneObject(parent.formConfig);
            var length = formField.length;
            formField[length] = {
                editable: "true",
                display: "标准工作明细",
                name: "standWorkDetailTab",
                comboboxName: "standWorkDetailTabBox",
                type: "select"
            };
            //创建表单结构
            var formConfig = {
                space: 50, labelWidth: 120, inputWidth: 200,
                validate: true,
                fields: formField
            };
            $("#inputForm").ligerForm(formConfig);

            /**标准工作关联设备**/
            common.callAjax('post',false,ctx + "/eam/operationwork/getDeviceInfo","json",{"operationwork_id" : parent.editId},function(data){
                // 标准工作选择多个设备的弹出框
                $("input[name='device_name']").val(data.deviceNames);
                $("input[name='device_major']").val(data.deviceMajors);
            });


            // 工序表格数据
            var procedureTable = null;
            // 安全措施表格数据
            var safetyTable = null;
            // 工器具表格数据
            var toolsTable = null;
            // 备品备件表格数据
            var sparepartsTable = null;
            // 人员工时表格数据
            var personhoursTable = null;
            // 其他费用表格数据
            var othersTable = null;

            /**标准工作工序表格数据**/
            common.callAjax('post',false,common.interfaceUrl.workProcedure,"json",{"operationwork_id" : parent.editId},function(data){
                procedureTable = data;
            });
            /**标准工作安全措施表格数据**/
            common.callAjax('post',false,common.interfaceUrl.workSafety,"json",{"operationwork_id" : parent.editId},function(data){
                safetyTable = data;
            });
            /**标准工作工器具表格数据**/
            common.callAjax('post',false,common.interfaceUrl.workTools,"json",{"operationwork_id" : parent.editId},function(data){
                toolsTable = data;
            });
            /**标准工作备品备件表格数据**/
            common.callAjax('post',false,common.interfaceUrl.workSpareparts,"json",{"operationwork_id" : parent.editId},function(data){
                sparepartsTable = data;
            });
            /**标准工作人员工时表格数据**/
            common.callAjax('post',false,common.interfaceUrl.workPersonHours,"json",{"operationwork_id" : parent.editId},function(data){
                personhoursTable = data;
            });
            /**标准工作其他费用表格数据**/
            common.callAjax('post',false,common.interfaceUrl.workOtherexpenses,"json",{"operationwork_id" : parent.editId},function(data){
                othersTable = data;
            });


            // procedure    工序列表
            var procedure = $("#procedure").ligerGrid({
                columns: [
                    { display: '工序编码', name: 'procedure_code', type: 'text'},
                    { display: '描述', name: 'procedure_desc', type: 'text'},
                    { display: '质检标准', name: 'procedure_standard',type: 'text'},
                    { display: '备注', name: 'procedure_remark'}
                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                data:{Rows: procedureTable},
                width: '88%'
            });

            // safety    安全措施列表
            var safety = $("#safety").ligerGrid({
                columns: [
                    { display: '安全措施编码', name: 'safety_code', type: 'text'},
                    { display: '描述', name: 'safety_desc', type: 'text'},
                    { display: '质检标准', name: 'safety_standard',type: 'text'},
                    { display: '备注', name: 'safety_remark'}
                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                data:{Rows: safetyTable},
                width: '88%'
            });
            // 安全措施表格默认隐藏
            $("#safety").hide();

            // tools   工器具列表
            var tools = $("#tools").ligerGrid({
                columns: [
                    { display: '工器具',  name: 'material_id',type:'text',
                        render: function (item)
                        {
                            var material_name;
                            if(item != null && item.material_id != null && item.material_id != ""){
                                $.each(parent.materialSelect, function (i, data) {
                                    if(data.material_id == item.material_id){
                                        material_name = data.material_name;
                                    }
                                });
                            }
                            return material_name;
                        }
                    },
                    { display: '数量', name: 'tools_num', type: 'text'},
                    { display: '计量单位', name: 'material_unit',
                        render: function (rowdata, rowindex, value) {
                            var material_unit;
                            $.each(parent.materialSelect, function (i, data) {
                                if(data.material_id == rowdata.material_id){
                                    material_unit = data.material_unit;
                                }
                            });
                            return material_unit;
                        }
                    },
                    { display: '规格型号', name: 'material_model',
                        render: function (rowdata, rowindex, value) {
                            var material_model;
                            $.each(parent.materialSelect, function (i, data) {
                                if(data.material_id == rowdata.material_id){
                                    material_model = data.material_model;
                                }
                            });
                            return material_model;
                        }
                    },
                    { display: '备注', name: 'tools_remark'}
                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                data:{Rows: toolsTable},
                width: '88%'
            });
            // 其他费用表格默认隐藏
            $("#tools").hide();

            // spareparts   备品备件列表
            var spareparts = $("#spareparts").ligerGrid({
                columns: [
                    { display: '备品备件',  name: 'material_id',type:'text',
                        render: function (item)
                        {
                            var material_name;
                            if(item != null && item.material_id != null && item.material_id != ""){
                                $.each(parent.materialSelect, function (i, data) {
                                    if(data.material_id == item.material_id){
                                        material_name = data.material_name;
                                    }
                                });
                            }
                            return material_name;
                        }
                    },
                    { display: '数量', name: 'spareparts_num', type: 'text'},
                    { display: '计量单位', name: 'material_unit',
                        render: function (rowdata, rowindex, value) {
                            var material_unit;
                            $.each(parent.materialSelect, function (i, data) {
                                if(data.material_id == rowdata.material_id){
                                    material_unit = data.material_unit;
                                }
                            });
                            return material_unit;
                        }
                    },
                    { display: '单价', name: 'material_price',
                        render: function (rowdata, rowindex, value) {
                            var material_price;
                            $.each(parent.materialSelect, function (i, data) {
                                if(data.material_id == rowdata.material_id){
                                    material_price = data.material_price;
                                }
                            });
                            return material_price;
                        }
                    },
                    { display: '小计',name:'spareparts_total', isSort: false,
                        render: function (rowdata, rowindex, value)
                        {
                            var total = parseInt(rowdata.spareparts_num) * parseInt(rowdata.material_price);
                            return total;
                        }
                    },
                    { display: '备注', name: 'spareparts_remark', type: 'text', editor: { type: 'text'} }
                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                data:{Rows: sparepartsTable},
                width: '88%'
            });
            // 备品备件表格默认隐藏
            $("#spareparts").hide();

            // 人员工时列表
            var person = $("#person").ligerGrid({
                columns: [
                    { display: '人员',  name: 'loginname',type:'text',
                        render: function (item)
                        {
                            var realname;
                            if(item != null && item.loginname != null && item.loginname != ""){
                                $.each(parent.personSelect, function (i, data) {
                                    if(data.loginname == item.loginname){
                                        realname = data.b_realname;
                                    }
                                });
                            }
                            return realname;
                        }
                    },
                    { display: '额定工时', name: 'person_hours', type: 'text'},
                    { display: '额定工时单价', name: 'person_hourprice', type: 'text'},
                    { display: '小计', name: 'person_hourtotal',
                        render: function (rowdata, rowindex, value) {
                            var total = parseInt(rowdata.person_hours) * parseInt(rowdata.person_hourprice);
                            return total;
                        }
                    },
                    { display: '岗位技能', name: 'person_postskill'},
                    { display: '备注', name: 'person_remark'}
                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                data:{Rows: personhoursTable},
                width: '88%'
            });
            // 人员工时表格默认隐藏
            $("#person").hide();

            // otherexpenses    其他费用列表
            var otherexpenses = $("#otherexpenses").ligerGrid({
                columns: [
                    { display: '其他费用事项', name: 'otherexpenses', type: 'text'},
                    { display: '金额', name: 'otherexpenses_amount', type: 'text'},
                    { display: '备注', name: 'otherexpenses_remark',type: 'text'}
                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                data:{Rows: othersTable},
                width: '88%'
            });
            // 其他费用表格默认隐藏
            $("#otherexpenses").hide();

            //标准工作类型下拉初始化
            var typeHtml = "";
            $.each(parent.operationworkTypeSelect, function (i, item) {
                typeHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
            });
            var operationwork_typeBox = $("#operationwork_typeBox");
            operationwork_typeBox .html(typeHtml);
            operationwork_typeBox.append($("<input></input>").attr("type", "hidden").attr("name", "operationwork_type"));//设置隐藏域
            operationwork_typeBox.trigger('change.select2');
            operationwork_typeBox.prop("disabled", true);

            //标准工作状态下拉初始化
            var statusHtml = "";
            $.each(parent.statusSelect, function (i, item) {
                statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
            });
            var operationwork_statusBox = $("#operationwork_statusBox");
            operationwork_statusBox .html(statusHtml);
            operationwork_statusBox.append($("<input></input>").attr("type", "hidden").attr("name", "operationwork_status"));//设置隐藏域
            operationwork_statusBox.val(1);//设置默认值:1-有效
            operationwork_statusBox.trigger('change.select2');
            operationwork_statusBox.prop("disabled", true);

            var operworkData = null;
            //给编辑页面字段赋值
            common.callAjax('post', false, ctx + "/eam/operationwork/editObj", "json", {"id": parent.editId}, function (data) {
                var editForm = liger.get("inputForm");
                editForm.setData(data);
                operationwork_statusBox.val(data.operationwork_status).trigger('change.select2');//根据详情渲染下拉框页面显示值
                operationwork_typeBox.val(data.operationwork_type).trigger('change.select2');    //根据详情渲染下拉框页面显示值
            });

            // 下拉表格
            var tableData = [{text: '工序', id: '0'}, {text: '安全措施', id: '1'}, {text: '工器具', id: '2'},
                {text: '备品备件', id: '3'}, {text: '人员工时', id: '4'}, {text: '其他费用', id: '5'}];
            var optionHtml = "";
            $.each(tableData, function (i, item) {
                optionHtml += "<option value=\"" + item.id + "\">" + item.text + "</option>";
            });
            $("#standWorkDetailTabBox").html(optionHtml);
            $("#standWorkDetailTabBox").val("0").trigger("change");
            //下拉框选中事件
            $("#standWorkDetailTabBox").on('change',function () {
                if($("#standWorkDetailTabBox").val() == "0"){
                    $("#procedure").show();
                    $("#safety").hide();
                    $("#tools").hide();
                    $("#spareparts").hide();
                    $("#person").hide();
                    $("#otherexpenses").hide();
                } else if($("#standWorkDetailTabBox").val() == "1"){
                    $("#procedure").hide();
                    $("#safety").show();
                    $("#tools").hide();
                    $("#spareparts").hide();
                    $("#person").hide();
                    $("#otherexpenses").hide();
                } else if($("#standWorkDetailTabBox").val() == "2"){
                    $("#procedure").hide();
                    $("#safety").hide();
                    $("#tools").show();
                    $("#spareparts").hide();
                    $("#person").hide();
                    $("#otherexpenses").hide();
                } else if($("#standWorkDetailTabBox").val() == "3"){
                    $("#procedure").hide();
                    $("#safety").hide();
                    $("#tools").hide();
                    $("#spareparts").show();
                    $("#person").hide();
                    $("#otherexpenses").hide();
                } else if($("#standWorkDetailTabBox").val() == "4"){
                    $("#procedure").hide();
                    $("#safety").hide();
                    $("#tools").hide();
                    $("#spareparts").hide();
                    $("#person").show();
                    $("#otherexpenses").hide();
                } else if($("#standWorkDetailTabBox").val() == "5"){
                    $("#procedure").hide();
                    $("#safety").hide();
                    $("#tools").hide();
                    $("#spareparts").hide();
                    $("#person").hide();
                    $("#otherexpenses").show();
                }
            });

            // 设置页面的编辑框为不可编辑模式
            $("input[name='operationwork_content']").prop("disabled", true);
            $("input[name='device_name']").prop("disabled", true);
            $("input[name='device_major']").prop("disabled", true);
            $("input[name='operationwork_totaltime']").prop("disabled", true);

            //设置选择下拉框只读
//            $("select").attr("disabled","disabled");



            $("#closeBtn").on("click", function () {
                parent.layer.closeAll();
            });
        });
    </script>
</head>
<body>
<form id="inputForm" action="" method="post" class="form-horizontal">

</form>
<div class="editDiv">
    <div class="subeditDiv" id="procedure"></div>
    <div class="subeditDiv" id="safety"></div>
    <div class="subeditDiv" id="tools"></div>
    <div class="subeditDiv" id="spareparts"></div>
    <div class="subeditDiv" id="person"></div>
    <div class="subeditDiv" id="otherexpenses"></div>
</div>
<div class="form-actions">
    <input id="closeBtn"  type="button"  value="关 闭"/>
</div>
</body>
</html>
