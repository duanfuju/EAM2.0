<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>巡检路线管理</title>
	<meta name="decorator" content="default"/>
    <script type="text/javascript" src="/resource/plugins/FormUtil.js"></script>
	<script type="text/javascript">
        var idStart='S';
        var idEnd='E';
        var sendObj={};
        var selectedAreaObj = [];
        $(function () {
            var data1;
            var data2;
            var data3;
            var date4;
            var date5;
            var date6;
            //创建表单结构
            parent.formConfig.forEach(function(index){
                if(index.type=="combobox"){
                    index.option= {
                        isMultiSelect: true,
                        valueField: 'text',
                        tree: {
                            url: "/resource/data/tree.json",
                            idFieldName: 'text',
                            ajaxType: 'get'
                        }

                    }
                    index.newline=false;

                }

            });

            //delete parent.formConfig[1]["group"];

            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields: parent.formConfig

            };

            //debugger;
            console.log(parent.formConfig)

			console.log(formConfig);
            $("#inputForm").ligerForm(formConfig);
            //动态增加字段
			var sttimeHtml='<div class="enableTime">'
								+'<div class="Enable">'
									+'<div class="textColtr">启用时间</div>'
									+'<div class="l-text"><input type="text" id="S" class="enable-begin"/></div>'
									+'<span class="fixpos">至</span>'
									+'<div class="l-text"><input type="text" id="E" class="enable-end"/></div>'
                					+'<div class="addIcon"></div>'
								+'</div>'
//								+'<div class="addEnable">'
//									+'<div class="textColtr">增加启用时间段</div>'
//									+'<div class="addIcon"></div>'
//								+'</div>'
				          +'</div>';
            $('#route_isstandardBox').closest('.l-fieldcontainer').closest('ul').after(sttimeHtml);
            //点击删除添加按钮方法
            $('.enableTime .addIcon').click(function () {
                idStart+='S';
                idEnd+='E';
                var html = '<div class="Enable">' +
                    			'<div class="textColtr">启用时间</div>' +
								'<div class="l-text"><input type="text" id="'+idStart+'" class="enable-begin"/></div> ' +
                    			'<span>至</span> ' +
								'<div class="l-text"><input type="text" id="'+idEnd+'" class="enable-end"/></div> ' +
								'<div class="delIcon"></div> ' +
                    		'</div>';
                $('.enableTime').append(html);

//                var startDateTextBox = $('.enable-begin');
                var startDateTextBox = $("#"+ idStart);

//                var endDateTextBox = $('.enable-end');
                var endDateTextBox = $("#"+ idEnd);

                $('#'+idStart).datetimepicker({
                    showSecond: true,
                    dateFormat: 'yy-mm-dd',
                    timeFormat: 'HH:mm:ss',
                    changeMonth: true,
                    changeYear:true,
                    minDate:0,
                    onClose: function(selectedDate) {
                        var clearflag = $(this).closest('.Enable').find('.enable-end').val();
                        $(this).closest('.Enable').find('.enable-end').datetimepicker("option", "minDateTime", new Date(selectedDate));
                        $(this).closest('.Enable').find('.enable-end').datetimepicker("option", "minDate", selectedDate);
                        if(!clearflag){
                            $(this).closest('.Enable').find('.enable-end').val('');
						}
                    }
//                    onSelect: function (selectedDateTime){
////                        endDateTextBox.datetimepicker('option', 'minDate', startDateTextBox.datetimepicker('getDate') );
//                    },
                });
                $('#'+idEnd).datetimepicker({
                    showSecond: true,
                    dateFormat: 'yy-mm-dd',
                    timeFormat: 'HH:mm:ss',
                    changeMonth: true,
                    changeYear:true,
                    minDate:0,
                    onClose: function(selectedDate) {
                        var clearflag = $(this).closest('.Enable').find('.enable-begin').val();
                        $(this).closest('.Enable').find('.enable-begin').datetimepicker("option", "maxDateTime", new Date(selectedDate));
                        $(this).closest('.Enable').find('.enable-begin').datetimepicker("option", "maxDate", selectedDate);
                        if(!clearflag){
                            $(this).closest('.Enable').find('.enable-begin').val('');
                        }
                    }
//                    onSelect: function (selectedDateTime){
////                        startDateTextBox.datetimepicker('option', 'maxDate', endDateTextBox.datetimepicker('getDate') );
//                    }
                });
            });
            $("body").delegate(".Enable .delIcon","click",function(){
                $(this).closest('.Enable').remove();
            });

            var startDateTextBox = $("#"+ idStart);
            var endDateTextBox = $("#"+ idEnd);


            $('#S').datetimepicker({
                showSecond: true,
                dateFormat: 'yy-mm-dd',
                timeFormat: 'HH:mm:ss',
                changeMonth: true,
                changeYear:true,
                minDate:0,
				onClose: function(selectedDate) {
                    var clearflag = $('#E').val();
                    $('#E').datetimepicker("option", "minDateTime", new Date(selectedDate));
                    $('#E').datetimepicker("option", "minDate", selectedDate);
                    if(!clearflag){
                        $('#E').val('');
					}
                    }
//                onSelect: function (selectedDateTime){
//                    endDateTextBox.datetimepicker('option', 'minDate', startDateTextBox.datetimepicker('getDate') );
//                }


            });
            $('#E').datetimepicker({
                showSecond: true,
                dateFormat: 'yy-mm-dd',
                timeFormat: 'HH:mm:ss',
                changeMonth: true,
                changeYear:true,
                minDate:0,
                onClose: function(selectedDate) {
                    var clearflag = $('#S').val();
                    $('#S').datetimepicker("option", "maxDateTime", new Date(selectedDate));
                    $('#S').datetimepicker("option", "maxDate", selectedDate);
                    if(!clearflag){
                        $('#S').val('');
					}
                }
            });
            var periodHtml='<div class="showpick">'+
                                '<div class="day showpickdiv" style="display: block;">'+
                                    '<span>每</span>'+
                                    '<input type="text" class="choose"/>'+
                                    '<span>天</span>'+
                                    '<i class="fa fa-clock-o"></i>'+
                                    '<input type="text" class="time"/>'+
                                    '<div class="addIcon"></div>'+
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
                                    '<i class="fa fa-clock-o"></i>'+
                                    '<input type="text" class="time"/>'+
                                    '<div class="addIcon"></div>'+
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
                                    '<i class="fa fa-clock-o"></i>'+
                                    '<input type="text" class="time"/>'+
                                    '<div class="addIcon"></div>'+
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
                                    '<i class="fa fa-clock-o"></i>'+
                                    '<input type="text" class="time"/>'+
                                    '<div class="addIcon"></div>'+
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
                                    '<i class="fa fa-clock-o"></i>'+
                                    '<input type="text" class="time"/>'+
                                    '<div class="addIcon"></div>'+
                                '</div>'+
                             '</div>';
            $('#route_periodBox').closest('.l-fieldcontainer').closest('ul').after(periodHtml);
			//周期下拉框加载
            $('.periodBtn li').click(function () {
                $(this).addClass('periodActive');
                $(this).siblings().removeClass('periodActive');
                $('.showpick div.showpickdiv').eq($(this).index()).css('display','block');
                $('.showpick div.showpickdiv').eq($(this).index()).siblings().css('display','none');
            })
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
            $(".showpick .addIcon").click(function(){
                var html ='<div class="addTimeDiv">' +
                                '<input type="text" class="time"/>' +
                                '<div class="delIcon">×</div>' +
                        '</div>';
                $(this).before(html);
                $('.showpick .time').timepicker({
                    hourGrid: 4,
                    minuteGrid: 10
                });
            });
            $("body").delegate(".showpick .delIcon","click",function(){
                $(this).closest('.addTimeDiv').remove();
            });
            var detailHtml='<ul>' +
								'<li class="l-fieldcontainer l-fieldcontainer-first" fieldindex="7">' +
									'<ul>' +
										'<li style="width:120px;text-align:right;">工作明细</li>' +
											'<select class="order_detail">' +
											'<option value="0">工序</option>' +
											'<option value="1">安全措施</option>' +
											'<option value="2">工器具</option>' +
											'<option value="3">备件材料</option>' +
											'<option value="4">人员工时</option>' +
											'<option value="5">其他费用</option>' +
										'</select>' +
									'</ul>' +
								'</li>' +
							'</ul>';
            $('#route_isstandardBox').closest('.l-fieldcontainer').closest('ul').after(detailHtml);

            $('#inputForm').on('change','.order_detail',function(){//工单详情table切换
                if($(this).val()=='0'){
                    $("#procedure").show().siblings('.subeditDiv').hide();
                }else if ($(this).val()=='1'){
                    $("#safety").show().siblings('.subeditDiv').hide();
                }else if ($(this).val()=='2'){
                    $("#tools").show().siblings('.subeditDiv').hide();
                }else if ($(this).val()=='3'){
//                    $("#spareparts").show('.subeditDiv').siblings('.subeditDiv').hide();
                    $("#spareparts").show().siblings('.subeditDiv').hide();
                }else if ($(this).val()=='4'){
                    $("#person").show().siblings('.subeditDiv').hide();
                }else{
                    $("#otherexpenses").show().siblings('.subeditDiv').hide();
                }
            });
            $("#inputForm").on('change','#route_isstandardBox',function(){
                if($(this).val()=='0'){//否
                        if($('#yr').length>0)
                            $('#yr').closest('.l-fieldcontainer').closest('ul').css('display',"none");
							$('#standwork').closest('.l-fieldcontainer').closest('ul').css('display',"none");
							$('#standorder').closest('.l-fieldcontainer').closest('ul').css('display',"none");
                    }else{//是
                    if($('#yr').length>0) {
                        $('#yr').closest('.l-fieldcontainer').closest('ul').css('display', "block");
                        $('#standwork').closest('.l-fieldcontainer').closest('ul').css('display',"inline-block");
                        $('#standorder').closest('.l-fieldcontainer').closest('ul').css('display',"none");
                        $('#yr').val('0').trigger('change');
                    }else {
                        var yrHtml = '<ul>' +
										'<li class="l-fieldcontainer l-fieldcontainer-first" fieldindex="7">' +
											'<ul>' +
												'<li style="width:120px;text-align:right;">引入</li>' +
												'<select id="yr" >' +
												'<option value="0">标准工作</option>' +
												'<option value="1">标准工单</option>' +
												'</select>' +
											'</ul>' +
										'</li>' +
									'</ul>';
                        $('#route_isstandardBox').closest('.l-fieldcontainer').closest('ul').after(yrHtml);
                        $('#yr').select2({
                            allowClear: true,
                            above: false,
                            placeholder:'请选择',
                            minimumResultsForSearch: 1
                        });
                        var standworkHtml='<ul style="display:none">' +
												'<li class="l-fieldcontainer l-fieldcontainer-first" fieldindex="7">' +
													'<ul>' +
														'<li style="width:120px;text-align:right;">标准工作</li>' +
														'<select id="standwork" >' +
														'</select>' +
													'</ul>' +
												'</li>' +
											'</ul>';
                        var standorderHtml='<ul style="display:none">' +
												'<li class="l-fieldcontainer l-fieldcontainer-first" fieldindex="7">' +
													'<ul>' +
														'<li style="width:120px;text-align:right;">标准工单</li>' +
														'<select id="standorder" >' +
														'</select>' +
													'</ul>' +
												'</li>' +
											'</ul>';
                        $('#yr').closest('.l-fieldcontainer').closest('ul').after(standorderHtml);
                        $('#yr').closest('.l-fieldcontainer').closest('ul').after(standworkHtml);
                        $('#standwork').select2({
                            allowClear: true,
                            above: false,
                            placeholder:'请选择',
                            minimumResultsForSearch: 1
                        });
                        $('#standorder').select2({
                            allowClear: true,
                            above: false,
                            placeholder:'请选择',
                            minimumResultsForSearch: 1
                        });
                        FormUtil.initSelectBox(ctx+"/eam/operationwork/getApprovedWorkCodes","standCode",$("#standwork"),null,null,"id","code");
                        FormUtil.initSelectBox(ctx+"/opestandard/standardOrder/getApprovedOrderCodes","standCode",$("#standorder"),null,null,"id","code");
                        $('#standwork').closest('.l-fieldcontainer').closest('ul').css('display',"inline-block");
                        $('#standorder').closest('.l-fieldcontainer').closest('ul').css('display',"none");
                    }
                }
            });
            $('#inputForm').on('change','#yr',function(){//引入选择事件
                if($(this).val()=='0'){
                    $('#standwork').closest('.l-fieldcontainer').closest('ul').css('display',"inline-block");
                    $('#standorder').closest('.l-fieldcontainer').closest('ul').css('display',"none");
                }else{
                    $('#standwork').closest('.l-fieldcontainer').closest('ul').css('display',"none");
                    $('#standorder').closest('.l-fieldcontainer').closest('ul').css('display',"inline-block");
                }
            });
            $('.order_detail').closest('.l-fieldcontainer').closest('ul').after('<div class="subeditDiv" id="procedure"></div><div class="subeditDiv" id="safety"></div><div class="subeditDiv" id="tools"></div><div class="subeditDiv" id="spareparts"></div><div class="subeditDiv" id="person"></div><div class="subeditDiv" id="otherexpenses"></div>');
            $('.order_detail').select2({
                allowClear: true,
                above: false,
                placeholder:'请选择',
                minimumResultsForSearch: 1
            });
            date1={ Rows: [{
                "procedure_code": "GX001",
                "procedure_desc": null,
                "procedure_standard": null,
                "procedure_remark": null
            		}]}
            //工序
			f_initGrid1();
            var procedure;
            function f_initGrid1() {
                 procedure = $("#procedure").ligerGrid({
                    columns: [
                        {display: '工序编码', name: 'procedure_code', type: 'text', editor: {type: 'text'}},
                        {display: '描述', name: 'procedure_desc', type: 'text', editor: {type: 'text'}},
                        {display: '质检标准', name: 'procedure_standard', type: 'text', editor: {type: 'text'}},
                        {display: '备注', name: 'procedure_remark', editor: {type: 'text'}, editor: {type: 'text'}},
                        {
                            display: '操作', isSort: false,
                            render: function (rowdata, rowindex, value) {
                                var h = "";
                                h += "<a  class='add'>添加</a> ";
                                h += "<a  class='del' data-id='" + rowindex + "'>删除</a> ";
                                return h;
                            }
                        },

                    ],
                    onSelectRow: function (rowdata, rowindex) {
                    },
                    enabledEdit: true, isScroll: false, checkbox: false, rownumbers: true,
                    data: date1,
                    width: '88%'
                });
            }
            // 工序表格内新增行
            $('#procedure').on('click','.add',function(){
                var val = procedure.getData()[procedure.getData().length-1].procedure_code;
                val = 'GX'+ (Array(3).join(0) + (parseInt(val.substring(4,val.length)) + 1)).slice(-3);
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
			data2={Rows: [{
                "safety_code": "AQCS001",
                "safety_desc": null,
                "safety_standard": null,
                "safety_remark": null
            }]};
			f_initGrid2();
            var safety;
            function f_initGrid2() {
                 safety = $("#safety").ligerGrid({
                    columns: [
                        {display: '安全措施编码', name: 'safety_code', type: 'text', editor: {type: 'text'}},
                        {display: '描述', name: 'safety_desc', type: 'text', editor: {type: 'text'}},
                        {display: '质检标准', name: 'safety_standard', type: 'text', editor: {type: 'text'}},
                        {display: '备注', name: 'safety_remark', editor: {type: 'text'}, editor: {type: 'text'}},
                        {
                            display: '操作', isSort: false,
                            render: function (rowdata, rowindex, value) {
                                var h = "";
                                h += "<a  class='add'>添加</a> ";
                                h += "<a  class='del' data-id='" + rowindex + "'>删除</a> ";
                                return h;
                            }
                        },

                    ],
                    onSelectRow: function (rowdata, rowindex) {
                    },
                    enabledEdit: true, isScroll: false, checkbox: false, rownumbers: true,
                    data:data2 ,
                    width: '88%'
                });
            }
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
			date3={Rows: [{
                "material_id": null,
                "tools_num": null,
                "material_unit": null,
                "material_model": null,
                "tools_remark": null}]};
			f_initGrid3();
            var tools;
            function f_initGrid3() {
                 tools = $("#tools").ligerGrid({
                    columns: [
                        {
                            display: '工器具', name: 'material_id', type: 'text',
                            editor: {
                                type: 'selectGrid', data: parent.materialSelect,
                                valueField: 'material_id', textField: 'material_name'
                            },
                            render: function (item) {
                                var material_name;
                                if (item != null && item.material_id != null && item.material_id != "") {
                                    $.each(parent.materialSelect, function (i, data) {
                                        if (data.material_id == item.material_id) {
                                            material_name = data.material_name;
                                        }
                                    });
                                }
                                return material_name;
                            }
                        },
                        {display: '数量', name: 'tools_num', type: 'int', editor: {type: 'int'}},
                        {
                            display: '计量单位', name: 'material_unit',
                            render: function (rowdata, rowindex, value) {
                                var material_unit;
                                $.each(parent.materialSelect, function (i, data) {
                                    if (data.material_id == rowdata.material_id) {
                                        material_unit = data.material_unit;
                                    }
                                });
                                return material_unit;
                            }
                        },
                        {
                            display: '规格型号', name: 'material_model',
                            render: function (rowdata, rowindex, value) {
                                var material_model;
                                $.each(parent.materialSelect, function (i, data) {
                                    if (data.material_id == rowdata.material_id) {
                                        material_model = data.material_model;
                                    }
                                });
                                return material_model;
                            }
                        },
                        {display: '备注', name: 'tools_remark', editor: {type: 'text'}, editor: {type: 'text'}},
                        {
                            display: '操作', isSort: false,
                            render: function (rowdata, rowindex, value) {
                                var h = "";
                                h += "<a  class='add'>添加</a> ";
                                h += "<a  class='del' data-id='" + rowindex + "'>删除</a> ";
                                return h;
                            }
                        },

                    ],
                    onSelectRow: function (rowdata, rowindex) {
                    },
                    enabledEdit: true, isScroll: false, checkbox: false, rownumbers: true,
                    data: date3,
                    width: '88%'
                });
            }
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
			date4={
                Rows: [{
                    "material_id": null,
                    "spareparts_num": null,
                    "material_unit": null,
                    "material_price": null,
                    "spareparts_total": null,
                    "spareparts_remark": null
                }]
            };
			f_initGrid4();
            var spareparts;
            function f_initGrid4() {
                 spareparts = $("#spareparts").ligerGrid({
                    columns: [
                        {
                            display: '备品备件', name: 'material_id', type: 'text',
                            editor: {
                                type: 'selectGrid', data: parent.materialSelect,
                                valueField: 'material_id', textField: 'material_name'
                            },
                            render: function (item) {
                                var material_name;
                                if (item != null && item.material_id != null && item.material_id != "") {
                                    $.each(parent.materialSelect, function (i, data) {
                                        if (data.material_id == item.material_id) {
                                            material_name = data.material_name;
                                        }
                                    });
                                }
                                return material_name;
                            }
                        },
                        {display: '数量', name: 'spareparts_num', type: 'int', editor: {type: 'int'}},
                        {
                            display: '计量单位', name: 'material_unit',
                            render: function (rowdata, rowindex, value) {
                                var material_unit;
                                $.each(parent.materialSelect, function (i, data) {
                                    if (data.material_id == rowdata.material_id) {
                                        material_unit = data.material_unit;
                                    }
                                });
                                return material_unit;
                            }
                        },
                        {
                            display: '单价', name: 'material_price',
                            render: function (rowdata, rowindex, value) {
                                var material_price;
                                $.each(parent.materialSelect, function (i, data) {
                                    if (data.material_id == rowdata.material_id) {
                                        material_price = data.material_price;
                                    }
                                });
                                return material_price;
                            }
                        },
                        {
                            display: '小计', name: 'spareparts_total', isSort: false,
                            render: function (rowdata, rowindex, value) {
                                var total = parseInt(rowdata.spareparts_num) * parseInt(rowdata.material_price);
                                return total;
                            }
                        },
                        {display: '备注', name: 'spareparts_remark', type: 'text', editor: {type: 'text'}},
                        {
                            display: '操作', isSort: false,
                            render: function (rowdata, rowindex, value) {
                                var h = "";
                                h += "<a  class='add'>添加</a> ";
                                h += "<a  class='del' data-id='" + rowindex + "'>删除</a> ";
                                return h;
                            }
                        },

                    ],
                    onSelectRow: function (rowdata, rowindex) {
                    },
                    enabledEdit: true, isScroll: false, checkbox: false, rownumbers: true, onAfterEdit: f_onAfterEdit,
                    data:date4 ,
                    width: '88%'
                });
            }
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
			date5={
                Rows: [{
                    "loginname": null,
                    "person_hours": null,
                    "person_hourprice": null,
                    "person_hourtotal": null,
                    "person_postskill": null,
                    "person_remark": null
                }]
            };
            f_initGrid5();
            var person;
            function f_initGrid5() {
                 person = $("#person").ligerGrid({
                    columns: [
                        {
                            display: '人员', name: 'loginname', type: 'text',
                            editor: {
                                type: 'selectGrid', data: parent.personSelect,
                                valueField: 'loginname', textField: 'b_realname'
                            },
                            render: function (item) {
                                var realname;
                                if (item != null && item.loginname != null && item.loginname != "") {
                                    $.each(parent.personSelect, function (i, data) {
                                        if (data.loginname == item.loginname) {
                                            realname = data.b_realname;
                                        }
                                    });
                                }
                                return realname;
                            }
                        },
                        {display: '额定工时', name: 'person_hours', type: 'float', editor: {type: 'float'}},
                        {display: '额定工时单价', name: 'person_hourprice', type: 'float', editor: {type: 'float'}},
                        {
                            display: '小计', name: 'person_hourtotal',
                            render: function (rowdata, rowindex, value) {
                                var total = parseFloat(rowdata.person_hours) * parseFloat(rowdata.person_hourprice);
                                return total;
                            }
                        },
                        {display: '岗位技能', name: 'person_postskill', editor: {type: 'text'}, editor: {type: 'text'}},
                        {display: '备注', name: 'person_remark', editor: {type: 'text'}, editor: {type: 'text'}},
                    ],
                    onSelectRow: function (rowdata, rowindex) {
                    },
                    enabledEdit: true, isScroll: false, checkbox: false, rownumbers: true, onAfterEdit: f_onAfterEdit1,
                    data:date5 ,
                    width: '88%'
                });
            }
            // 人员工时表格默认隐藏
            $("#person").hide();

            //给联动字段赋值
            function f_onAfterEdit1(e) {
                person.updateCell('person_hourtotal', e.record.person_hours * e.record.person_hourprice, e.record);
            }

            // otherexpenses    其他费用列表
			date6={
                Rows: [{
                    "otherexpenses": null,
                    "otherexpenses_amount": null,
                    "otherexpenses_remark": null
                }]
            };
            f_initGrid6();
            var otherexpenses
            function f_initGrid6() {
                 otherexpenses = $("#otherexpenses").ligerGrid({
                    columns: [
                        {display: '其他费用事项', name: 'otherexpenses', type: 'text', editor: {type: 'text'}},
                        {display: '金额', name: 'otherexpenses_amount', type: 'float', editor: {type: 'float'}},
                        {display: '备注', name: 'otherexpenses_remark', type: 'text', editor: {type: 'text'}},
                        {
                            display: '操作', isSort: false,
                            render: function (rowdata, rowindex, value) {
                                var h = "";
                                h += "<a  class='add'>添加</a> ";
                                h += "<a  class='del' data-id='" + rowindex + "'>删除</a> ";
                                return h;
                            }
                        },

                    ],
                    onSelectRow: function (rowdata, rowindex) {
                    },
                    enabledEdit: true, isScroll: false, checkbox: false, rownumbers: true,
                    data: date6,
                    width: '88%'
                });
            }
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
                if(otherexpenses.getData().length == 1) {
                    alert("只剩一项时无法删除");
                } else {
                    otherexpenses.deleteRow($(this).data("id"));
                }
            });
			//获取编码
			common.ajaxForCode({type:"INSPECTION_ROUTE"},false,"text",function(data){
                if (data !="" ||data !=null) {
                    $("input[name='route_code']").val(data);
                }
			})
            //下拉初始化
                common.callAjax('post',true,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "common"},function(data){
                var statusHtml="";
                $.each(data, function (i, item) {
                    statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                $("#route_statusBox").html(statusHtml).val(1).trigger('change.select2');
            });
            common.callAjax('post',true,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "route_type"},function(data){
                var statusHtml="<option value>请选择</option>";
                $.each(data, function (i, item) {
                    statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                $("#route_typeBox").html(statusHtml).val('').trigger('change.select2');
            });
            common.callAjax('post',true,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "route_mode"},function(data){
                var statusHtml="<option value>请选择</option>";
                $.each(data, function (i, item) {
                    statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                $("#route_modeBox").html(statusHtml).val('').trigger('change.select2');
            });
            $('#route_isstandardBox').html('<option value="0">否</option><option value="1">是</option>').val('0').trigger('change.select2');
            //巡检周期
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "route_period"},function(data){
                var statusHtml="";
                $.each(data, function (i, item) {
                    statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                $("#route_periodBox").html(statusHtml).val(0).trigger('change.select2');
            });
		// 选择巡检区域
            $("input[name='route_area']").append($("<input></input>").attr("type", "hidden").attr("id", "route_area"));//设置隐藏域
            $("input[name='route_area']").on("click", function () {
                typeMaterial = '1';    //物料类型 1-备品备件
                layer.open({
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getAreaInner(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: "/a/inspection/inspectionRoute/insAreaSelectUI"
                });
            });
            //巡检周期change事件
            $('#inputForm').on('change','#route_periodBox',function(){
                $('.showpick div.showpickdiv').eq($(this).val()).css('display','block');
                $('.showpick div.showpickdiv').eq($(this).val()).siblings().css('display','none');
            });
            $("#btnSubmit").on("click",function () {
                $("input[name='type_status']").val($("#type_statusBox").val());
                var form = liger.get('inputForm');
                if (!form.valid()) {
                    form.showInvalid();
                }else{
                    //ajax提交
                    //按周期和时间区间拆分日期
                    var DateArr=new Array();
                    var ResultArr=new Array();
                    if($('#route_periodBox').val()=='0'){
                        DateArr=createArrDay();
                        var timeobj = $(".day .hasDatepicker");
                        for(var i=0;i<DateArr.length;i++){
                            for(var j=0;j<timeobj.length;j++){
                                var dateTime = DateArr[i]+" "+timeobj.eq(j).val();
                                ResultArr.push(dateTime);
                            }
                        }
                        var periodDetail = $(".day .choose").val();
                        if(periodDetail==""||periodDetail==null){
                            alert("请填写周期！");
                            return;
                        }
                        $('.day .hasDatepicker').each(function(){
                            periodDetail+="-"+$(this).val();
                        });

                        sendObj.route_period_detail = periodDetail;
                    }else if($('#route_periodBox').val()=='1'){
                        DateArr=createArrWeek();
                        var timeobj = $(".week .hasDatepicker");
                        for(var i=0;i<DateArr.length;i++){
                            for(var j=0;j<timeobj.length;j++) {
                                var dateTime = DateArr[i] + " " + timeobj.eq(j).val();
                                ResultArr.push(dateTime);
                            }
                        }
                        var periodDetail = $(".week .choose").val();
                        if(periodDetail==""||periodDetail==null){
                            alert("请填写周期！");
                            return;
                        }
                        periodDetail+=","+$("#dayweek").val();
                        $('.week .hasDatepicker').each(function(){
                            periodDetail+="-"+$(this).val();
                        });
                        sendObj.route_period_detail = periodDetail;
                    }else if($('#route_periodBox').val()=='2'){
                        DateArr=createArrMonth();
                        var timeobj = $(".month .hasDatepicker");
                        for(var i=0;i<DateArr.length;i++){
                            for(var j=0;j<timeobj.length;j++) {
                                var dateTime = DateArr[i] + " " + timeobj.eq(j).val();
                                ResultArr.push(dateTime);
                            }
                        }
                        var periodDetail = $(".month .choose").val();
                        if(periodDetail==""||periodDetail==null){
                            alert("请填写周期！");
                            return;
                        }
                        periodDetail+=","+$("#monthsel").val();
                        periodDetail+=","+$("#monthweek").val();
                        $('.month .hasDatepicker').each(function(){
                            periodDetail+="-"+$(this).val();
                        });
                        sendObj.route_period_detail = periodDetail;
                    }else if($('#route_periodBox').val()=='3'){
                        DateArr=createArrQuarter();
                        var timeobj = $(".season .hasDatepicker");
                        for(var i=0;i<DateArr.length;i++){
                            for(var j=0;j<timeobj.length;j++) {
                                var dateTime = DateArr[i] + " " + timeobj.eq(j).val();
                                ResultArr.push(dateTime);
                            }
                        }
                        var periodDetail = $(".season .choose").val();
                        if(periodDetail==""||periodDetail==null){
                            alert("请填写周期！");
                            return;
                        }
                        periodDetail+=","+$("#seasonsel").val();
                        periodDetail+=","+$("#seamonthsel").val();
                        periodDetail+=","+$("#seaweek").val();
                        $('.season .hasDatepicker').each(function(){
                            periodDetail+="-"+$(this).val();
                        });
                        sendObj.route_period_detail = periodDetail;
                    }else{
                        DateArr=createArrYear();
                        var timeobj = $(".year .hasDatepicker");
                        for(var i=0;i<DateArr.length;i++){
                            for(var j=0;j<timeobj.length;j++) {
                                var dateTime = DateArr[i] + " " + timeobj.eq(j).val();
                                ResultArr.push(dateTime);
                            }
                        }
                        var periodDetail = $(".year .choose").val();
                        if(periodDetail==""||periodDetail==null){
                            alert("请填写周期！");
                            return;
                        }
                        periodDetail+=","+$("#yearsel").val();
                        periodDetail+=","+$("#yearmonthsel").val();
                        periodDetail+=","+$("#yearweek").val();
                        $('.year .hasDatepicker').each(function(){
                            periodDetail+="-"+$(this).val();
                        });
                        sendObj.route_period_detail = periodDetail;
                    }
                        sendObj.route_code=$('input[name="route_code"]').val();
                        sendObj.route_name=$('input[name="route_name"]').val();
                         sendObj.route_object=$('input[name="route_object"]').val();
                         sendObj.route_type=$('#route_typeBox').val();
                         sendObj.route_mode=$('#route_modeBox').val();
                         sendObj.route_period=$('#route_periodBox').val();
                         sendObj.route_isstandard=$('#route_isstandardBox').val();//是否引用标准
					     sendObj.route_area=$("#route_area").val();//巡检区域
                         if($('#route_isstandardBox').val()=='1'){
                             if($('#yr').val()==0){//引入标准工作
                                 sendObj.route_stand='0,'+$("#standwork").val();
                             }else{//引入标准工单
                                 sendObj.route_stand='1,'+$("#standorder").val();
                             }
                         }
                         var route_taskdates ="";
                    $.each(ResultArr,function(index,arrList){
                        var date = new Date(arrList);
                        var datetime=date.Format('yyyy-MM-dd hh:mm:ss');
                        console.log(datetime);
                        route_taskdates+=datetime;
                        route_taskdates+=",";
                    });
                    sendObj.route_taskdates=route_taskdates;//巡检任务时间安排
                    var route_enableperiod="";
                    $('.Enable').each(function(){
                        route_enableperiod+=$(this).find('.enable-begin').val()+"至";
                        route_enableperiod+=$(this).find('.enable-end').val()+",";
                    })
                    sendObj.route_enableperiod=route_enableperiod;//巡检时间段
                    sendObj.route_status=$('#route_statusBox').val();
                    var procedure = $("#procedure").ligerGrid().getData();     //工序表格数据
                    var safety = $("#safety").ligerGrid().getData();     //安全措施表格数据
                    var tools = $("#tools").ligerGrid().getData();     //工器具表格数据
                    var spareparts = $("#spareparts").ligerGrid().getData();     //备品备件表格数据
                    var person = $("#person").ligerGrid().getData();     //人员工时表格数据
                    var otherexpenses = $("#otherexpenses").ligerGrid().getData();     //其他费用表格数据
                    sendObj.insRouteProcList=procedure;
                    sendObj.insRouteSafeList=safety;
                    sendObj.insRouteToolsList=tools;
                    sendObj.insRouteSparepartsList=spareparts;
                    sendObj.insRoutePersonList=person;
                    sendObj.insRouteOthersList=otherexpenses;
                    sendObj.route_totalhour=$("#person").ligerGrid().getData()[0].person_hours;//额定工时
                    console.log(sendObj);
                    common.callAjax('post',false,'${ctx}/inspection/inspectionRoute/insert',"text",{param:JSON.stringify(sendObj)},function(data) {
                        if (data == "success") {
                            layer.msg('新增成功！',{icon: 1,time: 1000},function (index) {
                                parent.$("#mytable").DataTable().ajax.reload();
                                parent.layer.closeAll();
                            });
                        } else if (data == "repeat") {
                            layer.msg("登录超时或无权限！",{time: 1000,icon:7});
                        } else {
                            layer.msg("新增失败！",{time: 1000,icon:2});
                        }
                    })
                }


            })
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
                debugger;
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

                        var monthLength=parent.$.timerCount(nowMonth);
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

	<input id="btnSubmit" type="button" value="提 交"/>
</div>
</body>
</html>
