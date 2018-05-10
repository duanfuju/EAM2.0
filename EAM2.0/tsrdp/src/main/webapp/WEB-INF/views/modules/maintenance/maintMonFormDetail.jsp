<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>保养月计划</title>
	<meta name="decorator" content="default"/>

	<script type="text/javascript">
        var idStart='S';
        var idEnd='E';
        var sendObj={};
        var selectedAreaObj = [];
        $(function () {
            //深克隆
            function cloneObject(obj) {
                //var o = obj instanceof Array ? [] : {};
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
            var formFields = cloneObject(parent.formConfig);
            $.each(formFields, function (index, val) {
                val.readonly = true;
            })

            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                //validate: true,
                fields: formFields
            };

            console.log(formConfig);
            $("#inputForm").ligerForm(formConfig);
            liger.get('inputForm').setVisible('project_opework',false);
            //动态增加字段
            var periodHtml='<div class="showpick">'+
                '<div class="day showpickdiv" style="display: block;">'+
                '<span>每</span>'+
                '<input type="text" class="choose"/>'+
                '<span>天</span>'+
                '</div>'+
                '<div class="week showpickdiv" style="display: none;">'+
                '<span>每</span>'+
                '<input type="text" class="choose"/>'+
                '<span>个</span>' +
                '<select class="weeksel" id="dayweek">' +
                '<option data-value="1" value="1">周一</option>' +
                '<option data-value="2" value="2">周二</option>' +
                '<option data-value="3" value="3">周三</option>' +
                '<option data-value="4" value="4">周四</option>' +
                '<option data-value="5" value="5">周五</option>' +
                '<option data-value="6" value="6">周六</option>' +
                '<option data-value="7" value="7">周日</option>' +
                '</select>'+
                '</div>'+
                '<div class="month showpickdiv" style="display: none;">'+
                '<span>每</span>'+
                '<input type="text" class="choose"/>'+
                '<span>月的第</span>'+
                '<select class="monthsel" id="monthsel">' +
                '<option data-value="1" value="1">1</option>' +
                '<option data-value="2" value="2">2</option>' +
                '<option data-value="3" value="3">3</option>' +
                '<option data-value="4" value="4">4</option>' +
                '</select>'+
                '<span>个</span>'+
                '<select class="weeksel" id="monthweek">' +
                '<option data-value="1" value="1">周一</option>' +
                '<option data-value="2" value="2">周二</option>' +
                '<option data-value="3" value="3">周三</option>' +
                '<option data-value="4" value="4">周四</option>' +
                '<option data-value="5" value="5">周五</option>' +
                '<option data-value="6" value="6">周六</option>' +
                '<option data-value="7" value="7">周日</option>' +
                '</select>'+
                '</div>'+
                '<div class="season showpickdiv" style="display: none;">'+
                '<span>每</span>'+
                '<input type="text" class="choose"/>'+
                '<span>季的第</span>'+
                '<select class="seasonsel" id="seasonsel">' +
                '<option data-value="1" value="1">1</option>' +
                '<option data-value="2" value="2">2</option>' +
                '<option data-value="3" value="3">3</option>' +
                '</select>'+
                '<span>月的第</span>'+
                '<select class="monthsel" id="seamonthsel">' +
                '<option data-value="1" value="1">1</option>' +
                '<option data-value="2" value="2">2</option>' +
                '<option data-value="3" value="3">3</option>' +
                '<option data-value="4" value="4">4</option>' +
                '</select>'+
                '<span>个</span>'+
                '<select class="weeksel" id="seaweek">' +
                '<option data-value="1" value="1">周一</option>' +
                '<option data-value="2" value="2">周二</option>' +
                '<option data-value="3" value="3">周三</option>' +
                '<option data-value="4" value="4">周四</option>' +
                '<option data-value="5" value="5">周五</option>' +
                '<option data-value="6" value="6">周六</option>' +
                '<option data-value="7" value="7">周日</option>' +
                '</select>'+
                '</div>'+
                '<div class="year showpickdiv" style="display: none;">'+
                '<span>每</span>'+
                '<input type="text" class="choose"/>'+
                '<span>年</span>'+
                '<select class="yearsel" id="yearsel">' +
                '<option data-value="1" value="1">1</option>' +
                '<option data-value="1" value="2">2</option>' +
                '<option data-value="1" value="3">3</option>' +
                '<option data-value="1" value="4">4</option>' +
                '<option data-value="1" value="5">5</option>' +
                '<option data-value="1" value="6">6</option>' +
                '<option data-value="1" value="7">7</option>' +
                '<option data-value="1" value="8">8</option>' +
                '<option data-value="1" value="9">9</option>' +
                '<option data-value="1" value="10">10</option>' +
                '<option data-value="1" value="11">11</option>' +
                '<option data-value="1" value="12">12</option>' +
                '</select>'+
                '<span>月的第</span>'+
                '<select class="monthsel" id="yearmonthsel">' +
                '<option data-value="1" value="1">1</option>' +
                '<option data-value="2" value="2">2</option>' +
                '<option data-value="3" value="3">3</option>' +
                '<option data-value="4" value="4">4</option>' +
                '</select>'+
                '<span>个</span>'+
                '<select class="weeksel" id="yearweek">' +
                '<option data-value="1" value="1">周一</option>' +
                '<option data-value="2" value="2">周二</option>' +
                '<option data-value="3" value="3">周三</option>' +
                '<option data-value="4" value="4">周四</option>' +
                '<option data-value="5" value="5">周五</option>' +
                '<option data-value="6" value="6">周六</option>' +
                '<option data-value="7" value="7">周日</option>' +
                '</select>'+
                '</div>'+
                '</div>';
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "maintain_period"},function(data){
                var statusHtml="";
                $.each(data, function (i, item) {
                    statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                $("#project_periodBox").html(statusHtml).val(0).trigger('change.select2');
            });
            $('#project_periodBox').closest('.l-fieldcontainer').closest('ul').after(periodHtml);

            $('.weeksel').select2({
                allowClear: true,
                above: false,
                placeholder:'请选择',
                minimumResultsForSearch: 1
            })
            $('.monthsel').select2({
                allowClear: true,
                above: false,
                placeholder:'请选择',
                minimumResultsForSearch: 1
            })
            $('.seasonsel').select2({
                allowClear: true,
                above: false,
                placeholder:'请选择',
                minimumResultsForSearch: 1
            })
            $('.yearsel').select2({
                allowClear: true,
                above: false,
                placeholder:'请选择',
                minimumResultsForSearch: 1
            })
            $('.showpick .time').timepicker({
                hourGrid: 4,
                minuteGrid: 10
            });
            var detailHtml=	'<option value="0">工序</option>' +
				  			 '<option value="1">安全措施</option>' +
							  '<option value="2">工器具</option>' +
				              '<option value="3">备件材料</option>' +
								'<option value="4">人员工时</option>' +
								'<option value="5">其他费用</option>';
            $('#project_detailBox').html(detailHtml).val(0).trigger('change.select2');

            $('#inputForm').on('change','.order_detail',function(){//工单详情table切换
                if($(this).val()=='0'){
                    $("#procedure").show().siblings('.subeditDiv').hide();
                }else if ($(this).val()=='1'){
                    $("#safety").show().siblings('.subeditDiv').hide();
                }else if ($(this).val()=='2'){
                    $("#tools").show().siblings('.subeditDiv').hide();
                }else if ($(this).val()=='3'){
                    $("#spareparts").show().siblings('.subeditDiv').hide();
                }else if ($(this).val()=='4'){
                    $("#person").show().siblings('.subeditDiv').hide();
                }else{
                    $("#otherexpenses").show().siblings('.subeditDiv').hide();
                }
            });
            $('#project_detailBox').closest('.l-fieldcontainer').closest('ul').after('<div class="subeditDiv" id="procedure"></div><div class="subeditDiv" id="safety"></div><div class="subeditDiv" id="tools"></div><div class="subeditDiv" id="spareparts"></div><div class="subeditDiv" id="person"></div><div class="subeditDiv" id="otherexpenses"></div>');

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
            common.callAjax('post',false,ctx+'/maintain/maintainProjectSub/quMonProce',"json",{"project_id" : parent.editId},function(data){
                if(data == null || data.length == 0){
                    procedureTable = [{
                        "procedure_code": "GX001",
                        "procedure_desc" : null,
                        "procedure_standard" : null,
                        "procedure_remark" : null
                    }];
                } else {
                    procedureTable = data;
                }
            })
            //工序
            var procedure = $("#procedure").ligerGrid({
                columns: [
                    { display: '工序编码', name: 'procedure_code', type: 'text' },
                    { display: '描述', name: 'procedure_desc', type: 'text' },
                    { display: '质检标准', name: 'procedure_standard',type: 'text'},
                    { display: '备注', name: 'procedure_remark',type: 'text'}

                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                data:{Rows: procedureTable},
                width: '88%'
            });

            // safety    安全措施列表
            common.callAjax('post',false,ctx+'/maintain/maintainProjectSub/quMonSafe',"json",{"project_id" : parent.editId},function(data){
                if(data == null || data.length == 0){
                    safetyTable = [{
                        "safety_code": "AQCS001",
                        "safety_desc" : null,
                        "safety_standard" : null,
                        "safety_remark" : null
                    }]
                } else {
                    safetyTable = data;
                }
            });
            var safety = $("#safety").ligerGrid({
                columns: [
                    { display: '安全措施编码', name: 'safety_code', type: 'text' },
                    { display: '描述', name: 'safety_desc', type: 'text' },
                    { display: '质检标准', name: 'safety_standard',type: 'text'},
                    { display: '备注', name: 'safety_remark',  type: 'text' },
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
                data:{Rows: safetyTable},
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
            common.callAjax('post',false,ctx+'/maintain/maintainProjectSub/quMonTool',"json",{"project_id" : parent.editId},function(data){
                if(data == null || data.length == 0){
                    toolsTable = [{
                        "tool_id": null,
                        "tool_num": null,
                        "material_unit": null,
                        "material_model": null,
                        "tool_remark": null
                    }]
                } else {
                    toolsTable = data;
                }
            });
            var tools = $("#tools").ligerGrid({
                columns: [
                    { display: '工器具',  name: 'tool_id',type:'text',
                        editor: { type: 'selectGrid', data: parent.materialSelect,
                            valueField: 'material_id',textField: 'material_name'},
                        render: function (item)
                        {
                            var material_name;
                            if(item != null && item.tool_id != null && item.tool_id != ""){
                                $.each(parent.materialSelect, function (i, data) {
                                    if(data.material_id == item.tool_id){
                                        material_name = data.material_name;
                                    }
                                });
                            }
                            return material_name;
                        }
                    },
                    { display: '数量', name: 'tool_num', type: 'int' },
                    { display: '计量单位', name: 'material_unit',
                        render: function (rowdata, rowindex, value) {
                            var material_unit;
                            $.each(parent.materialSelect, function (i, data) {
                                if(data.material_id == rowdata.tool_id){
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
                                if(data.material_id == rowdata.tool_id){
                                    material_model = data.material_model;
                                }
                            });
                            return material_model;
                        }
                    },
                    { display: '备注', name: 'tool_remark',type: 'text' },

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
            common.callAjax('post',false,ctx+'/maintain/maintainProjectSub/quMonSpare',"json",{"project_id" : parent.editId},function(data){
                if(data == null || data.length == 0){
                    sparepartsTable = [{
                        "attachment_id": "",
                        "attachment_num": "",
                        "material_unit": "",
                        "material_price": "",
                        "spareparts_total": "",
                        "attachment_remark":""
                    }];
                } else {
                    sparepartsTable = data;
                }
            });
            var spareparts = $("#spareparts").ligerGrid({
                columns: [
                    { display: '备品备件',  name: 'attachment_id',type:'text',
                        editor: { type: 'selectGrid', data: parent.materialSelect,
                            valueField: 'material_id',textField: 'material_name'},
                        render: function (item)
                        {
                            var material_name;
                            if(item != null && item.attachment_id != null && item.attachment_id != ""){
                                $.each(parent.materialSelect, function (i, data) {
                                    if(data.material_id == item.attachment_id){
                                        material_name = data.material_name;
                                    }
                                });
                            }
                            return material_name;
                        }
                    },
                    { display: '数量', name: 'attachment_num', type: 'int' },
                    { display: '计量单位', name: 'material_unit',
                        render: function (rowdata, rowindex, value) {
                            var material_unit;
                            $.each(parent.materialSelect, function (i, data) {
                                if(data.material_id == rowdata.attachment_id){
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
                                if(data.material_id == rowdata.attachment_id){
                                    material_price = data.material_price;
                                }
                            });
                            return material_price;
                        }
                    },
                    { display: '小计',name:'spareparts_total', isSort: false,
                        render: function (rowdata, rowindex, value)
                        {
                            var total = parseInt(rowdata.attachment_num) * parseInt(rowdata.material_price);
                            return total;
                        }
                    },
                    { display: '备注', name: 'attachment_remark', type: 'text' },

                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,onAfterEdit: f_onAfterEdit,
                data:{Rows: sparepartsTable},
                width: '88%'
            });
            // 备品备件表格默认隐藏
            $("#spareparts").hide();

            //给联动字段赋值
            function f_onAfterEdit(e) {
                var material_price;
                var material_unit;
                $.each(parent.materialSelect, function (i, data) {
                    if(data.material_id == e.record.attachment_id){
                        material_price = data.material_price;
                        material_unit = data.material_unit;
                    }
                });
                spareparts.updateCell('material_price', material_price, e.record);
                spareparts.updateCell('material_unit', material_unit, e.record);
                spareparts.updateCell('spareparts_total', e.record.material_price * e.record.attachment_num, e.record);
            }

            // 人员工时列表
            common.callAjax('post',false,ctx+'/maintain/maintainProjectSub/quMonPerson',"json",{"project_id" : parent.editId},function(data){
                if(data == null || data.length == 0){
                    personhoursTable = [{
                        "emp_id": null,
                        "emp_hour": null,
                        "emp_price": null,
                        "person_hourtotal": null,
                        "emp_skills": null,
                        "emp_remark": null
                    }];
                } else {
                    personhoursTable = data;
                }
            });
            var person = $("#person").ligerGrid({
                columns: [
                    { display: '人员',  name: 'emp_id',type:'text',
                        editor: { type: 'selectGrid', data: parent.personSelect,
                            valueField: 'loginname',textField: 'b_realname'},
                        render: function (item)
                        {
                            var realname;
                            if(item != null && item.emp_id != null && item.emp_id != ""){
                                $.each(parent.personSelect, function (i, data) {
                                    if(data.loginname == item.emp_id){
                                        realname = data.b_realname;
                                    }
                                });
                            }
                            return realname;
                        }
                    },
                    { display: '额定工时', name: 'emp_hour', type: 'float' },
                    { display: '额定工时单价', name: 'emp_price', type: 'float'},
                    { display: '小计', name: 'person_hourtotal',
                        render: function (rowdata, rowindex, value) {
                            var total = parseFloat(rowdata.emp_hour) * parseFloat(rowdata.emp_price);
                            return total;
                        }
                    },
                    { display: '岗位技能', name: 'emp_skills', type: 'text' },
                    { display: '备注', name: 'emp_remark', type: 'text' },

                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,onAfterEdit: f_onAfterEdit1,
                data:{Rows:personhoursTable },
                width: '88%'
            });
            // 人员工时表格默认隐藏
            $("#person").hide();

            //给联动字段赋值
            function f_onAfterEdit1(e) {
                person.updateCell('person_hourtotal', e.record.emp_hour * e.record.emp_price, e.record);
            }

            // otherexpenses    其他费用列表
            common.callAjax('post',false,ctx+'/maintain/maintainProjectSub/quMonOther',"json",{"project_id" : parent.editId},function(data){
                if(data == null || data.length == 0){
                    othersTable = [{
                        "charge_name": null,
                        "charge_price" : null,
                        "charge_remark" : null
                    }];
                } else {
                    othersTable = data;
                }
            });
            var otherexpenses = $("#otherexpenses").ligerGrid({
                columns: [
                    { display: '其他费用事项', name: 'charge_name', type: 'text' },
                    { display: '金额', name: 'charge_price', type: 'float' },
                    { display: '备注', name: 'charge_remark',type: 'text'},
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

            $('#inputForm').on('change','#project_detailBox',function(){//工单详情table切换
                if($(this).val()=='0'){
                    $("#procedure").show().siblings('.subeditDiv').hide();
                }else if ($(this).val()=='1'){
                    $("#safety").show().siblings('.subeditDiv').hide();
                }else if ($(this).val()=='2'){
                    $("#tools").show().siblings('.subeditDiv').hide();
                }else if ($(this).val()=='3'){
                    $("#spareparts").show().siblings('.subeditDiv').hide();
                }else if ($(this).val()=='4'){
                    $("#person").show().siblings('.subeditDiv').hide();
                }else{
                    $("#otherexpenses").show().siblings('.subeditDiv').hide();
                }
            });
            $("#closeBtn").on("click",function () {
                parent.layer.closeAll();

            });
//给编辑页面字段赋值

            common.callAjax('post',false,ctx + "/maintain/maintainProjectSub/editObj","json",{"id" : parent.editId},function(data){

                //下拉初始化
                common.callAjax('post',true,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "common"},function(data1){
                    var statusHtml="";
                    $.each(data1, function (i, item) {
                        statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                    });
                    $("#statusBox").html(statusHtml).val(data.status).trigger('change.select2');
                });
                common.callAjax('post',true,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "maintain_type"},function(data2){
                    var statusHtml="";
                    $.each(data2, function (i, item) {
                        statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                    });
                    $("#project_typeBox").html(statusHtml).val(data.project_type).trigger('change.select2');
                });
                common.callAjax('post',true,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "maintain_mode"},function(data3){
                    var statusHtml="<option value>请选择</option>";
                    $.each(data3, function (i, item) {
                        statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                    });
                    $("#project_modeBox").html(statusHtml).val(data.project_mode).trigger('change.select2');
                });
                $('#project_isoperationBox').html('<option value="0">否</option><option value="1">是</option>').val(data.project_isoperation).trigger('change.select2');
                //巡检周期
                    $("#project_periodBox").val(data.project_period).trigger('change.select2');
                    $('.showpick div.showpickdiv').eq(data.project_period).css('display','block').siblings().css('display','none');
						if(data.project_period==0){
                            var period_detail=data.project_cycle.split('_');
                            $('.day .choose').val(period_detail[0]);
						}else if(data.project_period==1){
                            var period_detail=data.project_cycle.split('_');
                            $('.week .choose').val(period_detail[0]);
                            $('#dayweek').val(period_detail[1]).trigger('change.select2');
						}else if(data.project_period==2){
                            var project_detail=data.project_cycle.split('_');
                            $('.month .choose').val(project_detail[0]);
                            $('#monthsel').val(project_detail[1]).trigger('change.select2');
                            $('#monthweek').val(project_detail[2]).trigger('change.select2');
						}else if(data.project_period==3){
                            var project_detail=data.project_cycle.split('_');
                            $('.season .choose').val(project_detail[0]);
                            $('#seasonsel').val(project_detail[1]).trigger('change.select2');
                            $('#seamonthsel').val(project_detail[2]).trigger('change.select2');
                            $('#seaweek').val(project_detail[3]).trigger('change.select2');
						}else{
                            var project_detail=data.project_cycle.split('_');
                            $('.year .choose').val(project_detail[0]);
                            $('#yearsel').val(project_detail[1]).trigger('change.select2');
                            $('#yearmonthsel').val(project_detail[2]).trigger('change.select2');
                            $('#yearweek').val(project_detail[3]).trigger('change.select2');
						}
                //人员
                common.callAjax('post',true,common.interfaceUrl.userExtGetAllUser,"json",null,function(data4){
                    var statusHtml="";
                    $.each(data4, function (i, item) {
                        statusHtml += "<option value=\"" + item.b_loginname + "\">" + item.b_loginname + "</option>";
                    });
                    $("#project_empidBox").html(statusHtml).val(data.project_empid).trigger('change.select2');

                });
						$('input[name="project_time"]').val(data.project_cycleyear);
                		$('input[name="project_device"]').val(data.project_devname);
						$('input[name="project_code"]').val(data.project_code);
						$('input[name="project_name"]').val(data.project_name);
						$('input[name="project_stime"]').val(DateUtil.dateToStr('yyyy-MM-dd',new Date(data.project_stime)));
            });

//按天拆分日期
            function createArrDay(){
				/*日*/
                var num=idStart.toString().split("").length;
                var startS="#S";
                var endE="#E";
                var sendTim=[];
                for(var i=0;i<num;i++){
                    var dayGapNum = $(".day .choose").val();
                    var startDate = new Date($(startS).val());
                    var endDate = new Date($(endE).val());
                    if (!startDate)
                        return;
					/*opt全部的属性如下所示，不同的筛选条件，往里面插入相应的数据*/
                    //按日查看
                    var opt = {
                        startDate: startDate,//必填
                        endDate: endDate,    //必填
                        dayGapNum:parseInt(dayGapNum),    //按日查看
                    };
                    var arrTime = timeCount(opt);//分隔后的时间数组
                    sendTim=sendTim.concat(arrTime);
                    startS+='S';
                    endE+='E';

                }
                return sendTim;
            }


//按周查看
            function createArrWeek(){
				/*周*/
                var num=idStart.toString().split("").length;
                var startS="#S";
                var endE="#E";
                var sendTim=[];
                for(var i=0;i<num;i++){
                    var $WweekDom = $(".week .choose");
                    var $WdayDom = $("#dayweek");
                    var startDate = new Date($(startS).val());
                    var endDate = new Date($(endE).val());
                    if (!startDate)
                        return;
                    //按周查看
                    var opt2 = {
                        startDate: startDate,//必填
                        endDate: endDate,    //必填
                        weekGap: parseInt($WweekDom.val()),//按周查看
                        dayGap: parseInt($WdayDom.find('option:selected').attr("data-value"))//基层颗粒，如果年月周有的话，这个必须有
                    };
                    var arrTime2= timeCount(opt2);//分隔后的时间数组
                    sendTim=sendTim.concat(arrTime2);
                    startS+='S';
                    endE+='E';
                }

                return sendTim;
            }

//按月查看
            function createArrMonth(){
				/*月*/
                var num=idStart.toString().split("").length;
                var startS="#S";
                var endE="#E";
                var sendTim=[];
                for(var i=0;i<num;i++){
                    var $MmonthDom = $(".month .choose");
                    var $MweekDom = $("#monthsel");
                    var $MdayDom = $("#monthweek");
                    var startDate = new Date($(startS).val());
                    var endDate = new Date($(endE).val());
                    if (!startDate)
                        return;
                    //按月查看
                    var opt3 = {
                        startDate: startDate,//必填
                        endDate: endDate,    //必填
                        //dayGapNum:parseInt(dayGapNum),    //按日查看
                        //yearGap: parseInt($yearDom.val()), //按年查看
                        monthGap: parseInt($MmonthDom.val()),//按月查看
                        weekGap: parseInt($MweekDom.find('option:selected').attr("data-value")),//按周查看
                        dayGap: parseInt($MdayDom.find('option:selected').attr("data-value"))//基层颗粒，如果年月周有的话，这个必须有
                    };
                    var arrTime3= timeCount(opt3);//分隔后的时间数组
                    sendTim=sendTim.concat(arrTime3);
                    startS+='S';
                    endE+='E';
                }

                return sendTim;
            }

//按季度查看
            function createArrQuarter(){
				/*季度*/
                var num=idStart.toString().split("").length;
                var startS="#S";
                var endE="#E";
                var sendTim=[];
                for(var i=0;i<num;i++){
                    $QweekDom=$('#seamonthsel');
                    $QdayDom=$('#seaweek');
                    var startDate = new Date($(startS).val());
                    var endDate = new Date($(endE).val());
                    if (!startDate)
                        return;
					/*opt全部的属性如下所示，不同的筛选条件，往里面插入相应的数据*/
                    var opt5 = {
                        startDate: startDate,//必填
                        endDate: endDate,    //必填
                        yearGap: 1, //按年查看
                        monthGap:1 ,//按月查看
                        weekGap: parseInt($QweekDom.find('option:selected').attr("data-value")),//按周查看
                        dayGap: parseInt($QdayDom.find('option:selected').attr("data-value"))//基层颗粒，如果年月周有的话，这个必须有
                    };
                    //判断是第几个月的函数
                    function countMonth(season,month){
                        var arr=[[1,2,3],[4,5,6],[7,8,9],[10,11,12]];
                        return arr[season-1][month-1];
                    }
                    var seasonNum=parseInt($('.season .choose').val());
                    var monthNum=parseInt($('#seasonsel').find('option:selected').attr("data-value"));
                    var monthGap=countMonth(seasonNum,monthNum);
                    opt5.monthGap=monthGap;
                    var arrTime5 = timeCount(opt5);//分隔后的时间数组
                    sendTim=sendTim.concat(arrTime5);
                    startS+='S';
                    endE+='E';
                }

                return sendTim;
            }

//按年查看
            function createArrYear(){
				/*年*/
                var num=idStart.toString().split("").length;
                var startS="#S";
                var endE="#E";
                var sendTim=[];
                for(var i=0;i<num;i++){
                    var $yearDom = $(".year .choose");
                    var $YmonthDom = $("#yearsel");
                    var $YweekDom = $("#yearmonthsel");
                    var $YdayDom = $("#yearweek");
                    var startDate = new Date($(startS).val());
                    var endDate = new Date($(endE).val());
                    if (!startDate)
                        return;
                    var opt4 = {
                        startDate: startDate,//必填
                        endDate: endDate,    //必填
                        //dayGapNum:parseInt(dayGapNum),    //按日查看
                        yearGap: parseInt($yearDom.val()), //按年查看
                        monthGap: parseInt($YmonthDom.find('option:selected').attr("data-value")),//按月查看
                        weekGap: parseInt($YweekDom.find('option:selected').attr("data-value")),//按周查看
                        dayGap: parseInt($YdayDom.find('option:selected').attr("data-value"))//基层颗粒，如果年月周有的话，这个必须有
                    };
                    for(var key in opt4){
                        console.log(opt4[key]);
                    }
                    var arrTime4= timeCount(opt4);//分隔后的时间数组
                    sendTim=sendTim.concat(arrTime4);
                    startS+='S';
                    endE+='E';
                }

                return sendTim;
            }
            //日期计算函数
            function timeCount(opt) {
				/*
				 * 使用说明：
				 *   基于jquery.js
				 *   需要引入jquer.timerCount.js插件
				 *
				 *
				 * 使用方法：
				 * var opt={
				 startDate:startDate,//必填
				 endDate:endDate,    //必填
				 dayGapNum:parseInt(dayGapNum),    //按日查看
				 yearGap:parseInt($yearDom.val()), //按年查看
				 monthGap:parseInt($monthDom.find('option:selected').attr("data-value")),//按月查看
				 weekGap:parseInt($weekDom.find('option:selected').attr("data-value")),//按周查看
				 dayGap:parseInt($dayDom.find('option:selected').attr("data-value"))//基层颗粒，如果年月周有的话，这个必须有
				 };
				 var arrTime=that.timeCount(opt);
				 *
				 *
				 *
				 * opt格式要求：
				 *   一共分为四个筛选维度，当需要某个维度的时候，该值必须传递，如果为空，默认为1
				 * */


				/*四种函数声明start*/
				/*四种函数声明end*/

                //设置默认值为1
                for (var key in opt) {
                    if (!opt[key]) {
                        opt[key] = 1;
                    }
                }





                function yearGap(opt) {/*按年的计算*/
                    function firstDate(nowMonth,weekGap,dayGap){
                        var monthLength=$.timerCount(nowMonth);
                        var startDay=nowMonth.getDay();
                        if(dayGap-startDay>=0){
                            var firstDay=1+dayGap-startDay;

                        }else{
                            var firstDay=1+7+dayGap-startDay;
                        }
                        if(firstDay+(weekGap-1)*7>monthLength){
                            return "";
                        }else{
                            var nowDate=new Date(nowMonth.setDate(firstDay+(weekGap-1)*7));
                            return nowDate;
                        }
                    }
                    var startD = opt.startDate;
                    var endDate = opt.endDate;
                    var startYear=startD.getFullYear();
                    var endYear=endDate.getFullYear();
                    var yearGap = opt.yearGap;
                    var monthGap = opt.monthGap;
                    var weekGap = opt.weekGap;
                    var dayGap = opt.dayGap;
                    var startDateTime = opt.startDate.getTime();
                    var endDateTime = opt.endDate.getTime();
                    var yearArr = [];
                    for (var i =0 , len = endYear-startYear+1; i < len;i+=yearGap) {
                        var nowMonth = new Date((startYear+i)+'-'+monthGap+'-01');
                        yearArr.push(dateChange(firstDate(nowMonth,weekGap,dayGap)));
                    }
                    var lastDay=new Date(yearArr[yearArr.length-1]);
                    if(lastDay.getTime()>endDateTime){
                        yearArr[yearArr.length-1]="";
                    }
                    var firstDay=new Date(yearArr[0]);
                    if(firstDay.getTime()<startDateTime){
                        yearArr[0]="";
                    }

                    return yearArr;

                }

                function monthGap(opt) {/*按月的计算*/

                    function calEveryMonth(opt) {
                        var startDate = opt.startDate.getDate();
                        var startDay = opt.startDate.getDay();
                        var dayGap = opt.dayGap;
                        var weekGap=opt.weekGap;
                        if (dayGap - startDay >= 0) {
                            startDate += dayGap - startDay;
                        } else {
                            startDate += (7 + dayGap - startDay);
                        }
                        //var firstDate=opt.startDate;
                        var firstDate = opt.startDate;
                        firstDate.setDate(startDate+(weekGap-1)*7);
                        return firstDate.toLocaleDateString();
                    }
                    function monthCount(startDate, endDate) {//核算开始结束日期之间月份的个数
                        var startY = startDate.getFullYear();
                        var endDateY = endDate.getFullYear();
                        var startM = startDate.getMonth();
                        var endDateM = endDate.getMonth();
                        var total = (endDateY - startY) * 12 + (endDateM - startM) + 1;
                        return total;
                    }

                    var arrTime = [];

                    var startD = opt.startDate;
                    var endDate = opt.endDate;
                    var startDateTime = opt.startDate.getTime();
                    var endDateTime = opt.endDate.getTime();
                    var totalMonth = monthCount(startD, endDate);
                    var nowMonth = startD.getMonth();
                    var monthGap=opt.monthGap;
                    for (var i = 0; i < totalMonth; i+=monthGap) {
                        var nowDate = startD;
                        nowDate.setMonth(nowMonth + i);
                        if (i > 0) {
                            opt.startDate = nowDate;
                            opt.startDate.setDate(1);
                        }
                        arrTime.push(calEveryMonth(opt));
                    }
                    var lastDay=new Date(arrTime[arrTime.length-1]);
                    if(lastDay.getTime()>endDateTime){
                        arrTime[arrTime.length-1]="";
                    }
                    return arrTime;
                }

                function weekGap(opt) {/*按周的计算*/
                    var arrTime = [];
                    var dayGap = opt.dayGap;
                    var weekGap = opt.weekGap;
                    var dayInit = opt.startDate.getDay();
                    var startTime = start;
                    if (dayInit <= dayGap) {
                        startTime += (dayGap - dayInit) * 24 * 60 * 60 * 1000;
                    } else {
                        startTime += (7 - dayInit + dayGap) * 24 * 60 * 60 * 1000;
                    }
                    do {
                        var stringDate = dateChange(new Date(startTime));
                        arrTime.push(stringDate);
                        startTime += weekGap * 7 * 24 * 60 * 60 * 1000;
                    } while (startTime <= end);
                    return arrTime;

                }

                function dayGapNum(opt) {/*按天的计算*/
                    var arrTime = [];
                    var dayGapNum = opt.dayGapNum;
                    var startTime = start;
                    do {
                        var stringDate = dateChange(new Date(startTime));
                        arrTime.push(stringDate);
                        startTime += dayGapNum * 24 * 60 * 60 * 1000;
                    } while (startTime <= end);
                    return arrTime;
                }

                function dateChange(date) {/*日期格式化*/
                    var year = date.getFullYear();
                    var month = ('000' + (date.getMonth() + 1)).slice(-2);
                    var day = date.getDate();
                    return (year + '-' + month + '-' + day);
                }

				/*四种函数声明end*/

                var start = opt.startDate.getTime();
                var end = opt.endDate.getTime();


				/*根据参数的不同情况调用*/
                if (opt.dayGapNum) {
                    return dayGapNum(opt);
                } else if (opt.yearGap) {
                    return yearGap(opt);
                } else if (opt.monthGap) {
                    return monthGap(opt);
                } else if (opt.weekGap) {
                    return weekGap(opt);
                }
            }


			/*日期格式化*/
            Date.prototype.Format = function  (fmt) {
                var o = {
                    "M+": this.getMonth() + 1,
                    "d+": this.getDate(),
                    "h+": this.getHours(),
                    "m+": this.getMinutes(),
                    "s+": this.getSeconds(),
                    "q+": Math.floor((this.getMonth() + 3) / 3),
                    "S": this.getMilliseconds()
                };
                if (/(y+)/.test(fmt))
                    fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
                for (var k in o)
                    if (new RegExp("(" + k + ")").test(fmt))
                        fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
                return fmt;
            };
        });


	</script>
</head>
<body>
<form id="inputForm" action="" method="post" class="form-horizontal">


</form>
<div class="form-actions">
	<input id="deviceId" type="hidden"/>
	<input id="closeBtn"  type="button"  value="关 闭"/>
</div>
</body>
</html>
