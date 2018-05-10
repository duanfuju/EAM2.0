<%@ page import="org.activiti.engine.form.FormType" %>
<%@ page import="org.activiti.engine.form.FormProperty" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>标准工单审批</title>
	<style type="text/css">
		#pinfo th {background: #f7f7f9;}
		.editDiv .l-panel-bar{
			display: none ;
		}
		#flowstatus tr {
			border: 1px solid rgba(34, 34, 34, 0.17);
		}
		#flowstatus td,#flowstatus th{
			width: 1%;
			height: 40px;
		}
		#flowstatus .blue{
			background-color: rgba(21, 199, 189, 0.19);
		}
	</style>
	<meta name="decorator" content="default"/>
	<script type="text/javascript" src="/js/bootstrap.min.js"></script>

	<script type="text/javascript">
        var executionId = '${task.executionId}';
        var materialSelect;
        var personSelect;
        $(function () {
            //完整的渲染方法
            var data='${activityList}';

            data=JSON.parse(data);
            console.log(data);
            function showData(data) {
                //debugger;
                var dom = '';
                $.each(data,function(index,e){
                    dom += '<tr class="blue"><td>'+ (index+1) +'</td><td>' + e.node +
                        '</td><td>操作者总计:' + e.operatorNum +
                        '</td><td>已提交:' + e.submit +
                        '</td><td></td><td></td></tr>' +
                        '<tr class="operator"><td></td><td>操作人</td><td>操作状态</td><td>接收时间</td><td>操作时间</td><td>操作损耗</td></tr>';
                    $.each(e.detail,function(index,i){
                        var a = "<tr><td></td><td>" + i.operator +
                            "</td><td>" + i.status +
                            "</td><td>" + i.accepttime +
                            "</td><td>" + i.submittime +
                            "</td><td>" + i.duration + "</td></tr>";
                        dom += a;
                    })
                })
                $('table').append(dom);
            }
            showData(data);

            //表单提交
            $("#btnSubmit").on('click', function() {

                $("#inputForm").on("submit", function () {

                    $(this).ajaxSubmit({
                        type: 'post',
                        success: function (data) {
                            if (data == "success") {
                                layer.msg('提交成功！',{icon:1,time: 1000}, function (index) {
                                    parent.$("#mytable").DataTable().ajax.reload();
                                    parent.layer.closeAll();
                                });
                            } else if (data == "noauth") {
                                layer.msg("无审批权限，请先签收任务！",{time: 1000,icon:7});
                            } else {
                                layer.msg("提交失败！",{time: 1000,icon:2});
                            }
                        }
                    });
                    return false;
                });
            });
            $.fn.outerHTML = function () {

                // IE, Chrome & Safari will comply with the non-standard outerHTML, all others (FF) will have a fall-back for cloning
                return (!this.length) ? this : (this[0].outerHTML ||
                (function (el) {
                    var div = document.createElement('div');
                    div.appendChild(el.cloneNode(true));
                    var contents = div.innerHTML;
                    div = null;
                    return contents;
                })(this[0]));

            };

            if ($('#processDiagram').length == 1) {
                showActivities();
            }
            function showActivities() {
                $.getJSON(ctx + '/eam/act/process/trace/data/' + executionId, function (infos) {
                    var positionHtml = "";

                    var diagramPositon = $('#processDiagram').position();
                    var varsArray = new Array();
                    $.each(infos, function (i, v) {
                        var $positionDiv = $('<div/>', {
                            'class': 'activity-attr'
                        }).css({
                            position: 'absolute',
                            left: (v.x + 2),
                            top: (v.y + 35),
                            width: (v.width - 2),
                            height: (v.height - 2),
                            backgroundColor: 'black',
                            opacity: 0
                        });

                        // 节点边框
                        var $border = $('<div/>', {
                            'class': 'activity-attr-border'
                        }).css({
                            position: 'absolute',
                            left: (v.x + 2),
                            top: (v.y + 35),
                            width: (v.width - 4),
                            height: (v.height - 3)
                        });

                        if (v.currentActiviti) {
                            $border.css({
                                border: '3px solid red'
                            }).addClass('ui-corner-all-12');
                        }
                        positionHtml += $positionDiv.outerHTML() + $border.outerHTML();
                        varsArray[varsArray.length] = v.vars;
                    });

                    $(positionHtml).appendTo('#flowchart').find('.activity-attr-border');

                    // 鼠标移动到活动上提示
                    $('.activity-attr-border').each(function (i, v) {
                        var tipContent = "<table class='table table-bordered'>";
                        $.each(varsArray[i], function(varKey, varValue) {
                            if (varValue) {
                                tipContent += "<tr><td>" + varKey + "</td><td>" + varValue + "</td></tr>";
                            }
                        });
                        tipContent += "</table>";
                        $(this).data('vars', varsArray[i]).data('toggle', 'tooltip').data('placement', 'bottom').data('title', '活动属性').attr('title', tipContent);
                    }).tooltip();
                });
            }

            var formField;

            // 获取页面标准工作（页面编号是1065）要显示的内容
            common.callAjax('POST', false, ctx + "/eam/device/getfields", 'json', {menuno: "1093"}, function (data) {
                formField = data.formfield;

                $.each(formField, function (index, val) {
                    val.readonly = true;
                });
            });

            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields: formField

            };
            $("#serviceForm").ligerForm(formConfig);
            //工器具，备品备件
            common.callAjax('post',false,common.interfaceUrl.toolsInfoData,"json",null,function(data){
                materialSelect = data;
            });
            common.callAjax('post',false,common.interfaceUrl.userExtGetAllUser,"json",null,function(data){
                personSelect = data;
            });
            var periodHtml='<div class="showpick">'+
                '<div class="day showpickdiv" style="display: block;">'+
                '<span>每</span>'+
                '<input type="text" class="choose"/>'+
                '<span>天</span>'+
                '<i class="fa fa-clock-o"></i>'+
                '<input type="text" class="time"/>'+
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
                ' <i class="fa fa-clock-o"></i>'+
                '<input type="text" class="time"/>'+
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
                '</div>'+
                '</div>';
            $('#route_periodBox').closest('.l-fieldcontainer').closest('ul').after(periodHtml);
            //给表单赋值
            $.ajax({
                type: 'POST',
                url: ctx + "/inspection/inspectionRoute/getLibByPIid",
                async: false,
                dataType: 'json',
                data: {pIid: $("#pIid").val()},
                success: function (data) {
					var insid=data.id;
                    //下拉初始化
                    common.callAjax('post',true,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "common"},function(data1){
                        var statusHtml="";
                        $.each(data1, function (i, item) {
                            statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                        });
                        $("#route_statusBox").html(statusHtml).val(data.route_status).trigger('change.select2');
                    });
                    common.callAjax('post',true,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "route_type"},function(data2){
                        var statusHtml="<option>请选择</option>";
                        $.each(data2, function (i, item) {
                            statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                        });
                        $("#route_typeBox").html(statusHtml).val(data.route_type).trigger('change.select2');
                    });
                    common.callAjax('post',true,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "route_mode"},function(data3){
                        var statusHtml="<option>请选择</option>";
                        $.each(data3, function (i, item) {
                            statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                        });
                        $("#route_modeBox").html(statusHtml).val(data.route_type).trigger('change.select2');
                    });
                    $('#route_isstandardBox').html('<option value="0">否</option><option value="1">是</option>').val(data.route_isstandard).trigger('change.select2');

                    // 选择巡检区域
                    $("input[name='route_area']").append($("<input></input>").attr("type", "hidden").attr("id", "route_area"));//设置隐藏域

                    //巡检周期
                    common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "route_period"},function(data4){
                        var statusHtml="";
                        $.each(data4, function (i, item) {
                            statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                        });
                        $("#route_periodBox").html(statusHtml).val(data.route_period).trigger('change.select2');
                        $('.showpick div.showpickdiv').eq(data.route_period).css('display','block').siblings().css('display','none');
                        var html ='<div class="addTimeDiv">' +
                            '<input type="text" class="time"/>' +
                            '</div>';
                        if(data.route_period==0){
                            var period_detail=data.route_period_detail.split('-');
                            $('.day .choose').val(period_detail[0]);
                            for(var i=1;i<period_detail.length;i++){
                                if(i==1){
                                    $('.day .time').first().val(period_detail[i]);
                                }else{
                                    $('.day .time').last().after(html);
                                    $('.day .time').last().val(period_detail[i]);
                                }

                            }
                        }else if(data.route_period==1){
                            var period_detail=data.route_period_detail.split('-');
                            var week=period_detail[0].split(',');
                            $('.week .choose').val(week[0]);
                            $('#dayweek').val(week[1]).trigger('change.select2');
                            for(var i=1;i<period_detail.length;i++){
                                if(i==1){
                                    $('.week .time').first().val(period_detail[i]);
                                }else{
                                    $('.week .time').last().after(html);
                                    $('.week .time').last().val(period_detail[i]);
                                }

                            }
                        }else if(data.route_period==2){
                            var period_detail=data.route_period_detail.split('-');
                            var month=period_detail[0].split(',');
                            $('.month .choose').val(month[0]);
                            $('#monthsel').val(month[1]).trigger('change.select2');
                            $('#monthweek').val(month[2]).trigger('change.select2');
                            for(var i=1;i<period_detail.length;i++){
                                if(i==1){
                                    $('.month .time').first().val(period_detail[i]);
                                }else{
                                    $('.month .time').last().after(html);
                                    $('.month .time').last().val(period_detail[i]);
                                }
                            }
                        }else if(data.route_period==3){
                            var period_detail=data.route_period_detail.split('-');
                            var season=period_detail[0].split(',');
                            $('.season .choose').val(season[0]);
                            $('#seasonsel').val(season[1]).trigger('change.select2');
                            $('#seamonthsel').val(season[2]).trigger('change.select2');
                            $('#seaweek').val(season[3]).trigger('change.select2');
                            for(var i=1;i<period_detail.length;i++){
                                if(i==1){
                                    $('.season .time').first().val(period_detail[i]);
                                }else{
                                    $('.season .time').last().after(html);
                                    $('.season .time').last().val(period_detail[i]);
                                }

                            }
                        }else{
                            var period_detail=data.route_period_detail.split('-');
                            var year=period_detail[0].split(',');
                            $('.year .choose').val(year[0]);
                            $('#yearsel').val(year[1]).trigger('change.select2');
                            $('#yearmonthsel').val(year[2]).trigger('change.select2');
                            $('#yearweek').val(year[3]).trigger('change.select2');
                            for(var i=1;i<period_detail.length;i++){
                                if(i==1){
                                    $('.year .time').first().val(period_detail[i]);
                                }else{
                                    $('.year .time').last().after(html);
                                    $('.year .time').last().val(period_detail[i]);
                                }

                            }
                        }
                        if(data.route_isstandard=='1'){//如果引用标准
                            var yrHtml = '<ul>' +
                                '<li class="l-fieldcontainer l-fieldcontainer-first" fieldindex="7">' +
                                '<ul>' +
                                '<li style="width:120px;text-align:right;">引入</li>' +
                                '<select id="yr" disabled>' +
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
                                '<select id="standwork" disabled>' +
                                '</select>' +
                                '</ul>' +
                                '</li>' +
                                '</ul>';
                            var standorderHtml='<ul style="display:none">' +
                                '<li class="l-fieldcontainer l-fieldcontainer-first" fieldindex="7">' +
                                '<ul>' +
                                '<li style="width:120px;text-align:right;">标准工单</li>' +
                                '<select id="standorder" disabled>' +
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
                            if(data.route_stand.indexOf(',') !=-1){
                                var stand=data.route_stand.split(',');
                                $('#yr').val(stand[0]).trigger('change.select2');
                                if(stand[0]=='0'){//标准工作
                                    $("#standwork").val(stand[1]).trigger('change.select2');
                                    $('#standwork').closest('.l-fieldcontainer').closest('ul').css('display',"inline-block");
                                    $('#standorder').closest('.l-fieldcontainer').closest('ul').css('display',"none");
                                }else{//标准工单
                                    $("#standorder").val(stand[1]).trigger('change.select2');
                                    $('#standwork').closest('.l-fieldcontainer').closest('ul').css('display',"none");
                                    $('#standorder').closest('.l-fieldcontainer').closest('ul').css('display',"inline-block");
                                }
                            }
                        }
                    });
                    $('input[name="route_code"]').val(data.route_code);
                    $('input[name="route_name"]').val(data.route_name);
                    $('input[name="route_object"]').val(data.route_object);
//巡检区域
                    $("input[name='route_area']").val(data.route_area);
                    common.callAjax('post',true,ctx+'/inspection/inspectionRoute/quInsProce',"json",{"inspectionroute_id" : insid},function(data){
                        // 工序表格数据
                        var procedureTable = null;
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
                        //工序
                        var procedure = $("#procedure").ligerGrid({
                            columns: [
                                { display: '工序编码', name: 'procedure_code', type: 'text' },
                                { display: '描述', name: 'procedure_desc', type: 'text'},
                                { display: '质检标准', name: 'procedure_standard',type: 'text'},
                                { display: '备注', name: 'procedure_remark',type: 'text' },

                            ],
                            onSelectRow: function (rowdata, rowindex)
                            {
                            },
                            enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                            data:{Rows: procedureTable},
                            width: '98%'
                        });
                    })
                    // safety    安全措施列表
                    common.callAjax('post',true,ctx+'/inspection/inspectionRoute/quInsSafe',"json",{"inspectionroute_id" : insid},function(data){
                        // 安全措施表格数据
                        var safetyTable = null;
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
                        var safety = $("#safety").ligerGrid({
                            columns: [
                                { display: '安全措施编码', name: 'safety_code', type: 'text'},
                                { display: '描述', name: 'safety_desc', type: 'text'},
                                { display: '质检标准', name: 'safety_standard',type: 'text'},
                                { display: '备注', name: 'safety_remark', type: 'text' },
                            ],
                            onSelectRow: function (rowdata, rowindex)
                            {
                            },
                            enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                            data:{Rows: safetyTable},
                            width: '98%'
                        });
                        // 安全措施表格默认隐藏
                        $("#safety").hide();
                    });
                    // tools   工器具列表
                    common.callAjax('post',true,ctx+'/inspection/inspectionRoute/quInsTool',"json",{"inspectionroute_id" : insid},function(data){
                        // 工器具表格数据
                        var toolsTable = null;
                        if(data == null || data.length == 0){
                            toolsTable = [{
                                "material_id": null,
                                "tools_num": null,
                                "material_unit": null,
                                "material_model": null,
                                "tools_remark": null
                            }]
                        } else {
                            toolsTable = data;
                        }
                        var tools = $("#tools").ligerGrid({
                            columns: [
                                { display: '工器具',  name: 'material_id',type:'text',

                                    render: function (item)
                                    {
                                        var material_name;
                                        if(item != null && item.material_id != null && item.material_id != ""){
                                            $.each(materialSelect, function (i, data) {
                                                if(data.material_id == item.material_id){
                                                    material_name = data.material_name;
                                                }
                                            });
                                        }
                                        return material_name;
                                    }
                                },
                                { display: '数量', name: 'tools_num', type: 'int' },
                                { display: '计量单位', name: 'material_unit',
                                    render: function (rowdata, rowindex, value) {
                                        var material_unit;
                                        $.each(materialSelect, function (i, data) {
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
                                        $.each(materialSelect, function (i, data) {
                                            if(data.material_id == rowdata.material_id){
                                                material_model = data.material_model;
                                            }
                                        });
                                        return material_model;
                                    }
                                },
                                { display: '备注', name: 'tools_remark',type: 'text' },

                            ],
                            onSelectRow: function (rowdata, rowindex)
                            {
                            },
                            enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                            data:{Rows: toolsTable},
                            width: '98%'
                        });
                        // 其他费用表格默认隐藏
                        $("#tools").hide();
                    });
                    // spareparts   备品备件列表
                    common.callAjax('post',true,ctx+'/inspection/inspectionRoute/quInsSpare',"json",{"inspectionroute_id" : insid},function(data){
                        // 备品备件表格数据
                        var sparepartsTable = null;
                        if(data == null || data.length == 0){
                            sparepartsTable = [{
                                "material_id": "",
                                "spareparts_num": "",
                                "material_unit": "",
                                "material_price": "",
                                "spareparts_total": "",
                                "spareparts_remark":""
                            }];
                        } else {
                            sparepartsTable = data;
                        }
                        var spareparts = $("#spareparts").ligerGrid({
                            columns: [
                                { display: '备品备件',  name: 'material_id',type:'text',
                                    render: function (item)
                                    {
                                        var material_name;
                                        if(item != null && item.material_id != null && item.material_id != ""){
                                            $.each(materialSelect, function (i, data) {
                                                if(data.material_id == item.material_id){
                                                    material_name = data.material_name;
                                                }
                                            });
                                        }
                                        return material_name;
                                    }
                                },
                                { display: '数量', name: 'spareparts_num', type: 'int' },
                                { display: '计量单位', name: 'material_unit',
                                    render: function (rowdata, rowindex, value) {
                                        var material_unit;
                                        $.each(materialSelect, function (i, data) {
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
                                        $.each(materialSelect, function (i, data) {
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
                                { display: '备注', name: 'spareparts_remark', type: 'text'},

                            ],
                            onSelectRow: function (rowdata, rowindex)
                            {
                            },
                            enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,onAfterEdit: f_onAfterEdit,
                            data:{Rows: sparepartsTable},
                            width: '98%'
                        });
                        // 备品备件表格默认隐藏
                        $("#spareparts").hide();
                        //给联动字段赋值
                        function f_onAfterEdit(e) {
                            var material_price;
                            var material_unit;
                            $.each(materialSelect, function (i, data) {
                                if(data.material_id == e.record.material_id){
                                    material_price = data.material_price;
                                    material_unit = data.material_unit;
                                }
                            });
                            spareparts.updateCell('material_price', material_price, e.record);
                            spareparts.updateCell('material_unit', material_unit, e.record);
                            spareparts.updateCell('spareparts_total', e.record.material_price * e.record.spareparts_num, e.record);
                        }
                    });
                    // 人员工时列表
                    common.callAjax('post',true,ctx+'/inspection/inspectionRoute/quInsPerson',"json",{"inspectionroute_id" : insid},function(data){
                        // 人员工时表格数据
                        var personhoursTable = null;
                        if(data == null || data.length == 0){
                            personhoursTable = [{
                                "loginname": null,
                                "person_hours": null,
                                "person_hourprice": null,
                                "person_hourtotal": null,
                                "person_postskill": null,
                                "person_remark": null
                            }];
                        } else {
                            personhoursTable = data;
                        }
                        var person = $("#person").ligerGrid({
                            columns: [
                                { display: '人员',  name: 'loginname',type:'text',
                                    render: function (item)
                                    {
                                        var realname;
                                        if(item != null && item.loginname != null && item.loginname != ""){
                                            $.each(personSelect, function (i, data) {
                                                if(data.loginname == item.loginname){
                                                    realname = data.b_realname;
                                                }
                                            });
                                        }
                                        return realname;
                                    }
                                },
                                { display: '额定工时', name: 'person_hours', type: 'float' },
                                { display: '额定工时单价', name: 'person_hourprice', type: 'float' },
                                { display: '小计', name: 'person_hourtotal',
                                    render: function (rowdata, rowindex, value) {
                                        var total = parseFloat(rowdata.person_hours) * parseFloat(rowdata.person_hourprice);
                                        return total;
                                    }
                                },
                                { display: '岗位技能', name: 'person_postskill',editor: { type: 'text' }},
                                { display: '备注', name: 'person_remark', type: 'text'},

                            ],
                            onSelectRow: function (rowdata, rowindex)
                            {
                            },
                            enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,onAfterEdit: f_onAfterEdit1,
                            data:{Rows: personhoursTable},
                            width: '98%'
                        });
                        // 人员工时表格默认隐藏
                        $("#person").hide();

                        //给联动字段赋值
                        function f_onAfterEdit1(e) {
                            person.updateCell('person_hourtotal', e.record.person_hours * e.record.person_hourprice, e.record);
                        }
                    });
                    // otherexpenses    其他费用列表
                    common.callAjax('post',true,ctx+'/inspection/inspectionRoute/quInsOther',"json",{"inspectionroute_id" : insid},function(data){
                        // 其他费用表格数据
                        var othersTable = null;
                        if(data == null || data.length == 0){
                            othersTable = [{
                                "otherexpenses": null,
                                "otherexpenses_amount" : null,
                                "otherexpenses_remark" : null
                            }];
                        } else {
                            othersTable = data;
                        }
                        var otherexpenses = $("#otherexpenses").ligerGrid({
                            columns: [
                                { display: '其他费用事项', name: 'otherexpenses', type: 'text' },
                                { display: '金额', name: 'otherexpenses_amount', type: 'float'},
                                { display: '备注', name: 'otherexpenses_remark',type: 'text'},

                            ],
                            onSelectRow: function (rowdata, rowindex)
                            {
                            },
                            enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                            data:{Rows: othersTable},
                            width: '98%'
                        });
                        // 其他费用表格默认隐藏
                        $("#otherexpenses").hide();
                    });

//动态增加字段
                    var sttimeHtml='<div class="enableTime">'
                        +'<div class="Enable">'
                        +'<div class="textColtr">启用时间</div>'
                        +'<div class="l-text"><input type="text" id="S" class="enable-begin"/></div>'
                        +'<span class="fixpos">至</span>'
                        +'<div class="l-text"><input type="text" id="E" class="enable-end"/></div>'
                        +'</div>'
                        +'</div>';
                    $('#route_isstandardBox').closest('.l-fieldcontainer').closest('ul').after(sttimeHtml);
                    //启用时间
                    var times=data.route_enableperiod.split(',');
                    var html="";
                    $.each(times,function(index,time){
                        var timeall = time.split('至');
                        if(time !="" &&time !=null) {
                            if (index == 0) {
                                $('#S').val(timeall[0]);
                                $('#E').val(timeall[1]);
                            } else {
                                idStart += 'S';
                                idEnd += 'E';
                                html = '<div class="Enable">' +
                                    '<div class="textColtr">启用时间</div>' +
                                    '<div class="l-text"><input type="text" id="' + idStart + '" class="enable-begin" value="' + timeall[0] + '"/></div> ' +
                                    '<span>至</span> ' +
                                    '<div class="l-text"><input type="text" id="' + idEnd + '" class="enable-end" value="' + timeall[1] + '"/></div> ' +
                                    '<div class="delIcon"></div> ' +
                                    '</div>';
                            }
                        }
                    })
                    if(html !="")
                        $('.addEnable').before(html);
                }
            })

            $('.enable-begin').datetimepicker({
                showSecond: true,
                dateFormat: 'yy-mm-dd',
                timeFormat: 'HH:mm:ss'
            });
            $('.enable-end').datetimepicker({
                showSecond: true,
                dateFormat: 'yy-mm-dd',
                timeFormat: 'HH:mm:ss'
            });

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
            var detailHtml='<ul>' +
                '<li class="l-fieldcontainer l-fieldcontainer-first" fieldindex="7">' +
                '<ul>' +
                '<li style="width:120px;text-align:right;">工单明细</li>' +
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

            $('#serviceForm').on('change','.order_detail',function(){//工单详情table切换
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
            $('.order_detail').closest('.l-fieldcontainer').closest('ul').after('<div class="subeditDiv" id="procedure"></div><div class="subeditDiv" id="safety"></div><div class="subeditDiv" id="tools"></div><div class="subeditDiv" id="spareparts"></div><div class="subeditDiv" id="person"></div><div class="subeditDiv" id="otherexpenses"></div>');
            $('.order_detail').select2({
                allowClear: true,
                above: false,
                placeholder:'请选择',
                minimumResultsForSearch: 1
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
<div>
	<ul class="nav nav-tabs">
		<li role="presentation" class="active"><a href="#taskContent" role="tab" data-toggle="tab" aria-controls="home" aria-expanded="true">任务详情</a></li>
		<li role="presentation" ><a href="#flowchart" role="tab" data-toggle="tab" aria-controls="profile" aria-expanded="false">流程图</a></li>
		<li role="presentation" ><a href="#flowstatus" role="tab" data-toggle="tab" aria-controls="profile" aria-expanded="false">流程状态</a></li>
	</ul>
	<div class="tab-content">
		<div id="taskContent" role="tabpanel" class="tab-pane active">
<form  class="form-horizontal" action="" method="post" id="serviceForm">

</form>
<%--action="${ctx }/eam/act/task/complete?taskid=${task.id}"--%>
<form  class="form-horizontal" action="${ctx }/eam/act/task/complete?taskid=${task.id}" method="post" id="inputForm">
	<%--<input type="hidden" value="${task.id}" id="taskid">--%>
	<%--<h4>任务内容</h4>--%>
	<c:if test="${hasFormKey}">
		${taskFormData}
	</c:if>
		<c:if test="${!hasFormKey}">
			<c:forEach items="${taskFormData.formProperties}" var="fp">
				<c:set var="fpo" value="${fp}"/>
				<c:set var="disabled" value="${fp.writable ? '' : 'disabled'}" />
				<c:set var="readonly" value="${fp.writable ? '' : 'readonly'}" />
				<c:set var="required" value="${fp.required ? 'required' : ''}" />
				<%
					// 把需要获取的属性读取并设置到pageContext域
					FormType type = ((FormProperty)pageContext.getAttribute("fpo")).getType();
					String[] keys = {"datePattern", "values"};
					for (String key: keys) {
						pageContext.setAttribute(key, type.getInformation(key));
					}
				%>
				<div class="control-group">
						<%-- 文本或者数字类型 --%>
					<c:if test="${fp.type.name == 'string' || fp.type.name == 'long' || fp.type.name == 'double'}">
						<label class="control-label" for="${fp.id}">${fp.name}:</label>
						<div class="controls">
							<input type="text" id="${fp.id}" name="${fp.id}" data-type="${fp.type.name}" value="${fp.value}" ${readonly} ${required} />
						</div>
					</c:if>

						<%-- 大文本 --%>
					<c:if test="${fp.type.name == 'bigtext'}">
						<label class="control-label" for="${fp.id}">${fp.name}:</label>
						<div class="controls">
							<textarea id="${fp.id}" name="${fp.id}" data-type="${fp.type.name}" ${readonly} ${required}>${fp.value}</textarea>
						</div>
					</c:if>

						<%-- 日期 --%>
					<c:if test="${fp.type.name == 'date'}">
						<label class="control-label" for="${fp.id}">${fp.name}:</label>
						<div class="controls">
							<input type="text" id="${fp.id}" name="${fp.id}" class="datepicker" value="${fp.value}" data-type="${fp.type.name}" data-date-format="${fn:toLowerCase(datePattern)}" ${readonly} ${required}/>
						</div>
					</c:if>

						<%-- 下拉框 --%>
					<c:if test="${fp.type.name == 'enum'}">
						<label class="control-label" for="${fp.id}">${fp.name}:</label>
						<div class="controls">
							<select name="${fp.id}" id="${fp.id}" ${disabled} ${required}>
								<c:forEach items="${values}" var="item">
									<option value="${item.key}" <c:if test="${item.value == fp.value}">selected</c:if>>${item.value}</option>
								</c:forEach>
							</select>
						</div>
					</c:if>

						<%-- Javascript --%>
					<c:if test="${fp.type.name == 'javascript'}">
						<script type="text/javascript">${fp.value};</script>
					</c:if>

				</div>
			</c:forEach>
		</c:if>

	<%-- 按钮区域 --%>
	<div class="control-group">
		<div class="controls">

			<input type="hidden" value="${task.id}" id="taskid">
			<input type="hidden" value="${pIid}" id="pIid" name="pIid">
			<%--<c:if test="${not empty task.assignee}">--%>
				<button type="submit" class="btn btn-primary" id="btnSubmit"><i class="icon-ok"></i>完成任务</button>
			<%--</c:if>
			<c:if test="${empty task.assignee}">
				<a class="btn" id="claim"><i class="icon-ok"></i>签收</a>
			</c:if>--%>
		</div>
	</div>
</form>
		</div>
		<div id="flowchart" role="tabpanel" class="tab-pane">
			<img id="processDiagram" src="${ctx }/eam/act/process/read-resource?pdid=${processDefinition.id}&resourceName=${processDefinition.diagramResourceName}" />
		</div>
		<div id="flowstatus" role="tabpanel" class="tab-pane">
			<div>
				<table>
					<tr>
						<th>序号</th>
						<th>节点</th>
						<th>操作情况统计</th>
						<th></th>
						<th></th>
						<th></th>
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>
</body>
</html>
