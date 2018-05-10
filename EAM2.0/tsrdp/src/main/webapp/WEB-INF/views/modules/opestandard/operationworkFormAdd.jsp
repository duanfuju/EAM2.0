<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp" %>
<html>
<head>
    <title>标准工作录入</title>
    <meta name="decorator" content="default"/>
    <style>
        .editDiv .l-panel-bar{
            display: none ;
        }
    </style>

    <script type="text/javascript">
        var devInfo;
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
            var length = formField.length;
            formField[length] = {editable:"true",display: "标准工作明细", name: "standWorkDetailTab", comboboxName: "standWorkDetailTabBox", type: "select"};
            console.log(formField);
            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields: formField

            };
            $("#inputForm").ligerForm(formConfig);

            //获取编码
            common.ajaxForCode({type:"STAND_WORK"},false,"text",function(data){
                if (data != "" || data != null) {
                    $("input[name='operationwork_code']").val(data);
                }
            });

            // 标准工作选择多个设备的弹出框
            $("input[name='device_name']").append($("<input></input>").attr("type", "hidden").attr("name", "device_ids"));//设置隐藏域
            $("input[name='device_name']").on("click",function () {
                layer.open({
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getAreaM(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: "/a/eam/operationwork/DeviceSelectUI"
                });
            });

            // 下拉表格
            var tableData=[{text: '工序', id: '0'},{text: '安全措施', id: '1'},{text: '工器具', id: '2'},
                {text: '备品备件', id: '3'},{text: '人员工时', id: '4'},{text: '其他费用', id: '5'}];
            var optionHtml = "";
            $.each(tableData, function (i, item) {
                optionHtml += "<option value=\"" + item.id + "\">" + item.text + "</option>";
            });
            $("#standWorkDetailTabBox").html(optionHtml);
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

            // procedure    工序列表
            var procedure = $("#procedure").ligerGrid({
                columns: [
                    { display: '工序编码', name: 'procedure_code', type: 'text', editor: { type: 'text'} },
                    { display: '描述', name: 'procedure_desc', type: 'text', editor: { type: 'text'} },
                    { display: '质检标准', name: 'procedure_standard',type: 'text',editor: { type: 'text' }},
                    { display: '备注', name: 'procedure_remark',editor: { type: 'text' },editor: { type: 'text' }},
                    { display: '操作', isSort: false,
                        render: function (rowdata, rowindex, value)
                        {
                            var h = "";
                            h += "<a  class='add'>添加</a> ";
                            h += "<a  class='del' data-id='" + rowindex + "'>删除</a> ";
                            return h;
                        }
                    },

                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                data:{Rows: [{"procedure_code": "SWGX001",
                    "procedure_desc" : null,
                    "procedure_standard" : null,
                    "procedure_remark" : null}]},
                width: '88%'
            });

            // 工序表格内新增行
            $('#procedure').on('click','.add',function(){
                var val = procedure.getData()[procedure.getData().length-1].procedure_code;
                val = 'SWGX'+ (Array(3).join(0) + (parseInt(val.substring(4,val.length)) + 1)).slice(-3);
                procedure.addRow({
                    "procedure_code": val,
                    "procedure_desc" : null,
                    "procedure_standard" : null,
                    "procedure_remark" : null
                });
            });

            // 工序表格内删除行
            $('#procedure').on('click','.del',function(){
                if(procedure.getData().length == 1) {
                    alert("只剩一项时无法删除");
                } else {
                    procedure.deleteRow($(this).data("id"));
                }
            });

            // safety    安全措施列表
            var safety = $("#safety").ligerGrid({
                columns: [
                    { display: '安全措施编码', name: 'safety_code', type: 'text', editor: { type: 'text'} },
                    { display: '描述', name: 'safety_desc', type: 'text', editor: { type: 'text'} },
                    { display: '质检标准', name: 'safety_standard',type: 'text',editor: { type: 'text' }},
                    { display: '备注', name: 'safety_remark',editor: { type: 'text' },editor: { type: 'text' }},
                    { display: '操作', isSort: false,
                        render: function (rowdata, rowindex, value)
                        {
                            var h = "";
                            h += "<a  class='add'>添加</a> ";
                            h += "<a  class='del' data-id='" + rowindex + "'>删除</a> ";
                            return h;
                        }
                    },

                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                data:{Rows: [{"safety_code": "AQCS001",
                    "safety_desc" : null,
                    "safety_standard" : null,
                    "safety_remark" : null}]},
                width: '88%'
            });
            // 安全措施表格默认隐藏
            $("#safety").hide();
            // 安全措施表格内新增行
            $('#safety').on('click','.add',function(){
                var val = safety.getData()[safety.getData().length-1].safety_code;
                val = 'AQCS'+ (Array(3).join(0) + (parseInt(val.substring(4,val.length)) + 1)).slice(-3);
                safety.addRow({
                    "safety_code": val,
                    "safety_desc" : null,
                    "safety_standard" : null,
                    "safety_remark" : null
                });
            });

            // 安全措施表格内删除行
            $('#safety').on('click','.del',function(){
                if(safety.getData().length == 1) {
                    alert("只剩一项时无法删除");
                } else {
                    safety.deleteRow($(this).data("id"));
                }
            });

            // tools   工器具列表
            var tools = $("#tools").ligerGrid({
                columns: [
                    { display: '工器具',  name: 'material_id',type:'text',
                        editor: { type: 'selectGrid', data: parent.materialSelect,
                            valueField: 'material_id',textField: 'material_name'},
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
                    { display: '数量', name: 'tools_num', type: 'int', editor: { type: 'int'} },
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
                    { display: '备注', name: 'tools_remark',editor: { type: 'text' }},
                    { display: '操作', isSort: false,
                        render: function (rowdata, rowindex, value)
                        {
                            var h = "";
                            h += "<a  class='add'>添加</a> ";
                            h += "<a  class='del' data-id='" + rowindex + "'>删除</a> ";
                            return h;
                        }
                    },

                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                data:{Rows: [{
                    "material_id": null,
                    "tools_num": null,
                    "material_unit": null,
                    "material_model": null,
                    "tools_remark": null}]},
                width: '88%'
            });
            // 其他费用表格默认隐藏
            $("#tools").hide();
            // 工器具表格内新增行
            $('#tools').on('click','.add',function(){
                tools.addRow({
                    "material_id": null,
                    "tools_num": null,
                    "material_unit": null,
                    "material_model": null,
                    "tools_remark": null
                });
            });

            // 工器具表格内删除行
            $('#tools').on('click','.del',function(){
                if(tools.getData().length == 1) {
                    alert("只剩一项时无法删除");
                } else {
                    tools.deleteRow($(this).data("id"));
                }
            });

            // spareparts   备品备件列表
            var spareparts = $("#spareparts").ligerGrid({
                columns: [
                    { display: '备品备件',  name: 'material_id',type:'text',
                        editor: { type: 'selectGrid', data: parent.materialSelect,
                            valueField: 'material_id',textField: 'material_name'},
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
                    { display: '数量', name: 'spareparts_num', type: 'int', editor: { type: 'int'} },
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
                    { display: '备注', name: 'spareparts_remark', type: 'text', editor: { type: 'text'} },
                    { display: '操作', isSort: false,
                        render: function (rowdata, rowindex, value)
                        {
                            var h = "";
                            h += "<a  class='add'>添加</a> ";
                            h += "<a  class='del' data-id='" + rowindex + "'>删除</a> ";
                            return h;
                        }
                    },

                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,onAfterEdit: f_onAfterEdit,
                data:{Rows: [{
                    "material_id": null,
                    "spareparts_num": null,
                    "material_unit": null,
                    "material_price": null,
                    "spareparts_total": null,
                    "spareparts_remark":null
                }]},
                width: '88%'
            });
            // 备品备件表格默认隐藏
            $("#spareparts").hide();
            // 备品备件表格内新增行
            $('#spareparts').on('click','.add',function(){
                spareparts.addRow({
                    "material_id": null,
                    "spareparts_num": null,
                    "material_unit": null,
                    "material_price": null,
                    "spareparts_total": null,
                    "spareparts_remark":null
                });
            });

            // 备品备件表格内删除行
            $('#spareparts').on('click','.del',function(){
                if(spareparts.getData().length == 1) {
                    alert("只剩一项时无法删除");
                } else {
                    spareparts.deleteRow($(this).data("id"));
                }
            });
            //给联动字段赋值
            function f_onAfterEdit(e) {
                var material_price;
                var material_unit;
                $.each(parent.materialSelect, function (i, data) {
                    if(data.material_id == e.record.material_id){
                        material_price = data.material_price;
                        material_unit = data.material_unit;
                    }
                });
                spareparts.updateCell('material_price', material_price, e.record);
                spareparts.updateCell('material_unit', material_unit, e.record);
                spareparts.updateCell('spareparts_total', e.record.material_price * e.record.spareparts_num, e.record);
            }

            // 人员工时列表
            var person = $("#person").ligerGrid({
                columns: [
                    { display: '人员',  name: 'loginname',type:'text',
                        editor: { type: 'selectGrid', data: parent.personSelect,
                            valueField: 'loginname',textField: 'b_realname'},
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
                    { display: '额定工时', name: 'person_hours', type: 'float', editor: { type: 'float'} },
                    { display: '额定工时单价', name: 'person_hourprice', type: 'float', editor: { type: 'float'} },
                    { display: '小计', name: 'person_hourtotal',
                        render: function (rowdata, rowindex, value) {
                            var total = parseFloat(rowdata.person_hours) * parseFloat(rowdata.person_hourprice);
                            return total;
                        }
                    },
                    { display: '岗位技能', name: 'person_postskill',editor: { type: 'text' },editor: { type: 'text' }},
                    { display: '备注', name: 'person_remark',editor: { type: 'text' },editor: { type: 'text' }},
                    { display: '操作', isSort: false,
                        render: function (rowdata, rowindex, value)
                        {
                            var h = "";
                            h += "<a  class='add'>添加</a> ";
                            h += "<a  class='del' data-id='" + rowindex + "'>删除</a> ";
                            return h;
                        }
                    },

                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,onAfterEdit: f_onAfterEdit1,
                data:{Rows: [{
                    "loginname": null,
                    "person_hours": null,
                    "person_hourprice": null,
                    "person_hourtotal": null,
                    "person_postskill": null,
                    "person_remark": null
                }]},
                width: '88%'
            });
            // 人员工时表格默认隐藏
            $("#person").hide();
            // 人员工时表格内删除行
            $('#person').on('click','.add',function(){
                person.addRow({
                    "loginname": null,
                    "person_hours": null,
                    "person_hourprice": null,
                    "person_hourtotal": null,
                    "person_postskill": null,
                    "person_remark": null
                });
            });

            // 人员工时表格内删除行
            $('#person').on('click','.del',function(){
                if(person.getData().length == 1) {
                    alert("只剩一项时无法删除");
                } else {
                    person.deleteRow($(this).data("id"));
                }
            });

            //给联动字段赋值
            function f_onAfterEdit1(e) {
                person.updateCell('person_hourtotal', e.record.person_hours * e.record.person_hourprice, e.record);
            }

            // otherexpenses    其他费用列表
            var otherexpenses = $("#otherexpenses").ligerGrid({
                columns: [
                    { display: '其他费用事项', name: 'otherexpenses', type: 'text', editor: { type: 'text'} },
                    { display: '金额', name: 'otherexpenses_amount', type: 'float', editor: { type: 'float'} },
                    { display: '备注', name: 'otherexpenses_remark',type: 'text',editor: { type: 'text' }},
                    { display: '操作', isSort: false,
                        render: function (rowdata, rowindex, value)
                        {
                            var h = "";
                            h += "<a  class='add'>添加</a> ";
                            h += "<a  class='del' data-id='" + rowindex + "'>删除</a> ";
                            return h;
                        }
                    },

                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                data:{Rows: [{
                    "otherexpenses": null,
                    "otherexpenses_amount" : null,
                    "otherexpenses_remark" : null

                }]},
                width: '88%'
            });
            // 其他费用表格默认隐藏
            $("#otherexpenses").hide();
            // 其他费用表格内新增行
            $('#otherexpenses').on('click','.add',function(){
                otherexpenses.addRow({
                    "otherexpenses": null,
                    "otherexpenses_amount" : null,
                    "otherexpenses_remark" : null
                });
            });

            // 其他费用表格内删除行
            $('#otherexpenses').on('click','.del',function(){
                debugger;
                if(otherexpenses.getData().length == 1) {
                    alert("只剩一项时无法删除");
                } else {
                    otherexpenses.deleteRow($(this).data("id"));
                }
            });

            //标准工作类型下拉初始化
            var typeHtml = "";
            $.each(parent.operationworkTypeSelect, function (i, item) {
                typeHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
            });
            var operationwork_typeBox = $("#operationwork_typeBox");
            operationwork_typeBox .html(typeHtml);
            operationwork_typeBox.append($("<input></input>").attr("type", "hidden").attr("name", "operationwork_type"));//设置隐藏域
            operationwork_typeBox.trigger('change.select2');

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

            $("#standWorkDetailTabBox").val("0").trigger("change");  //标准工作明细默认显示工序列表

            $("#btnSubmit").on("click",function () {
                var form = liger.get('inputForm');
                if (!form.valid()) {
                    form.showInvalid();
                }else{
                    //复杂表单用ajax提交
                    var operationwork_code = $("input[name='operationwork_code']").val();  //标准工作编码
                    var operationwork_content = $("input[name='operationwork_content']").val();  //标准工作内容
                    var operationwork_type = $('#operationwork_typeBox').val();  //标准工作类型
                    var operationwork_totaltime = $("input[name='operationwork_totaltime']").val();  //额定总工时
                    var operationwork_status = $('#operationwork_statusBox').val();  //标准工作状态
                    var operationwork_devices = $("input[name='device_ids']").val();  //标准工作关联设备id
                    var procedure1 = procedure.getData();     //工序表格数据
                    var safety1 = safety.getData();     //安全措施表格数据
                    var tools1 = tools.getData();     //工器具表格数据
                    var spareparts1 = spareparts.getData();     //备品备件表格数据
                    var person1 = person.getData();     //人员工时表格数据
                    var otherexpenses1 = otherexpenses.getData();     //其他费用表格数据
                    var deviceList = new Array();
                    var strs = operationwork_devices.split(",");

                    // 给设备列表赋值
                    $.each(strs, function (i, item) {
                        var str = {};
                        str.dev_id = item;
                        deviceList.push(str);
                    });

                    var param = new Object();
                    param.operationwork_code = operationwork_code;
                    param.operationwork_content = operationwork_content;
                    param.operationwork_type = operationwork_type;
                    param.operationwork_totaltime = operationwork_totaltime;
                    param.operationwork_status = operationwork_status;
                    param.deviceList = deviceList;
                    param.procedureList = procedure1;
                    param.safetyList = safety1;
                    param.toolsList = tools1;
                    param.sparepartsList = spareparts1;
                    param.personList = person1;
                    param.othersList = otherexpenses1;
                    console.log(param);

                    common.callAjax('post',false,'${ctx}/eam/operationwork/insert',"json",{param :JSON.stringify(param)},function(data) {
                        if (data.flag) {
                            layer.msg(data.msg,{icon:1,time: 1000}, function (index) {
                                parent.$("#mytable").DataTable().ajax.reload();
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
<form id="inputForm" action="${ctx}/eam/operationwork/insert" method="post" class="form-horizontal">


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
    <input id="btnSubmit" type="button" value="提 交"/>

</div>
</body>
</html>
