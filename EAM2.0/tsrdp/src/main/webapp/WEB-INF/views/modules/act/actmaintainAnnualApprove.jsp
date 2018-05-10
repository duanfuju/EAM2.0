<%@ page import="org.activiti.engine.form.FormType" %>
<%@ page import="org.activiti.engine.form.FormProperty" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>保养月计划审批</title>
	<style type="text/css">
		#pinfo th {background: #f7f7f9;}
	</style>
	<meta name="decorator" content="default"/>
	<script type="text/javascript" src="/js/bootstrap.min.js"></script>

	<script type="text/javascript">
        var executionId = '${task.executionId}';
        var materialSelect;
        var personSelect;
        $(function () {

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
                    var varsArray = [];
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
            var _statusData=null;//状态
            var _approveStatus=null;//审批状态
            var _maintainPeriod=null;//保养周期
            var _maintainMode=null;//保养类型
            var _maintainType=null;//保养分类
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "common"},function(data){
                _statusData=data;
            });
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "maintain_mode"},function(data){
                _maintainMode=data;
            });
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "maintain_type"},function(data){
                _maintainType=data;
            });
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "maintain_period"},function(data){
                _maintainPeriod=data;
            });
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "APPROVE_STATUS"},function(data){
                _approveStatus=data;
            });

            // 获取页面标准工作（页面编号是1065）要显示的内容
            common.callAjax('POST', false, ctx + "/eam/device/getfields", 'json', {menuno: "1082"}, function (data) {
                formField = data.formfield;

                $.each(formField, function (index, val) {
                    val.readonly = true;
                    if(val.name == 'status'){
                        val.options = {
                            data : _statusData
                        }
                    }else  if(val.name == 'project_mode'){
                        val.options = {
                            data : _maintainMode
                        }
                    }else  if(val.name == 'project_type'){
                        val.options = {
                            data : _maintainType
                        }
                    }else  if(val.name == 'project_period'){
                        val.options = {
                            data : _maintainPeriod
                        }
                    }else  if(val.name == 'project_status'){
                        val.options = {
                            data : _approveStatus
                        }
                    }else  if(val.name == 'project_bm'){
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
                    }
                });
            });

            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields: formField

            };
            $("#serviceForm").ligerForm(formConfig);
            common.callAjax('post',false,common.interfaceUrl.userExtGetAllUser,"json",null,function(data){
                var html="";
                $.each(data, function (i, item) {
                    html += "<option value=\"" + item.id_key + "\">" + item.b_realname + "</option>";
                });
                var project_empidBox= $("#project_empidBox");
                project_empidBox .html(html);
                project_empidBox.append($("<input></input>").attr("type", "hidden").attr("name", "project_empid"));//设置隐藏域
            });

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
            $('#project_periodBox').closest('.l-fieldcontainer').closest('ul').after(periodHtml);
            //周期下拉框加载
            $('.periodBtn li').click(function () {
                $(this).addClass('periodActive');
                $(this).siblings().removeClass('periodActive');
                $('.showpick div.showpickdiv').eq($(this).index()).css('display','block');
                $('.showpick div.showpickdiv').eq($(this).index()).siblings().css('display','none');
            });
            $('.weeksel').select2({
                allowClear: true,
                above: false,
                placeholder:'请选择',
                minimumResultsForSearch: 1
            });
            $('.monthsel').select2({
                allowClear: true,
                above: false,
                placeholder:'请选择',
                minimumResultsForSearch: 1
            });
            $('.seasonsel').select2({
                allowClear: true,
                above: false,
                placeholder:'请选择',
                minimumResultsForSearch: 1
            });
            $('.yearsel').select2({
                allowClear: true,
                above: false,
                placeholder:'请选择',
                minimumResultsForSearch: 1
            });
            $('.showpick .time').timepicker({
                hourGrid: 4,
                minuteGrid: 10
            });

            var idStart='S';
            var idEnd='E';
            var sendObj={};
            // 保养内容列表
            var _periodData=[];
            var _devices="";
            window.getDevices = function () {
                return _devices;
            };
            window.setDevices = function (devices){
                _devices=devices;
            };
            //给编辑页面字段赋值
            common.callAjax('post',false,common.interfaceUrl.maintainProjectFindByPstid,"json",{"pstid" : $("#pIid").val()},function(data){
                //审批时间时间转换
                data.project_stime=DateUtil.dateToStr('yyyy-MM-dd',new Date(data.project_stime));


                var editForm  = liger.get("serviceForm");
                editForm.setData(data);
                //将设备名称设置到页面上的dev_id的input上
                $("input[name='dev_id']").val(data.dev_name);
                _devices=data.dev_id;

                //保养内容赋值
                _periodData= data.contentList;
                //类型
                var project_modeBox=  $("#project_modeBox");
                project_modeBox.val(data.project_mode);
                project_modeBox.trigger('change.select2');
                //分类
                var project_typeBox=  $("#project_typeBox");
                project_typeBox.val(data.project_type);
                project_typeBox.trigger('change.select2');
                //保养人
                var project_empidBox=  $("#project_empidBox");
                project_empidBox.val(data.project_empid);
                project_empidBox.trigger('change.select2');
                //审批状态
                var project_statusBox=  $("#project_statusBox");
                project_statusBox.val(data.project_status);
                project_statusBox.trigger('change.select2');
                project_statusBox.attr("readonly","readonly");
                // 状态
                var statusBox=  $("#statusBox");
                statusBox.val(data.status);
                statusBox.trigger('change.select2');



                //保养周期
                common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "maintain_period"},function(data4){
                    var statusHtml="";
                    $.each(data4, function (i, item) {
                        statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                    });
                    $("#project_periodBox").html(statusHtml).val(data.project_period).trigger('change.select2');
                    $('.showpick div.showpickdiv').eq(data.project_period).css('display','block').siblings().css('display','none');
                    if(data.project_period==0){
                        var period_detail=data.project_cycle.split('_');
                        $('.day .choose').val(period_detail[0]);
                    }else if(data.project_period==1){
                        var week=data.project_cycle.split('_');
                        $('.week .choose').val(week[0]);
                        $('#dayweek').val(week[1]).trigger('change.select2');
                    }else if(data.project_period==2){
                        var month=data.project_cycle.split('_');
                        $('.month .choose').val(month[0]);
                        $('#monthsel').val(month[1]).trigger('change.select2');
                        $('#monthweek').val(month[2]).trigger('change.select2');
                    }else if(data.project_period==3){
                        var season=data.project_cycle.split('_');
                        $('.season .choose').val(season[0]);
                        $('#seasonsel').val(season[1]).trigger('change.select2');
                        $('#seamonthsel').val(season[2]).trigger('change.select2');
                        $('#seaweek').val(season[3]).trigger('change.select2');
                    }else{
                        var year=data.project_cycle.split('_');
                        $('.year .choose').val(year[0]);
                        $('#yearsel').val(year[1]).trigger('change.select2');
                        $('#yearmonthsel').val(year[2]).trigger('change.select2');
                        $('#yearweek').val(year[3]).trigger('change.select2');
                    }
                });
            });


            //巡检周期change事件
            $('#serviceForm').on('change','#project_periodBox',function(){
                $('.showpick div.showpickdiv').eq($(this).val()).css('display','block').siblings().css('display','none');
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



            var now_time=new Date().format("yyyyMMddhhmmss");
            var header_code= 'BYBM'+ now_time;
            var f_code=header_code+"001";
            var _periodGrid=null;
            if(_periodData==null||_periodData==""||_periodData.length==0){
                _periodData =  [{
                    "maintain_code":f_code,
                    "maintain_content":null,
                }];
            }

            function f_initGrid1() {
                _periodGrid = $("#periodTable").ligerGrid({
                    columns: [{ display: '保养内容编码', name: 'maintain_code',editor: { type: 'text' }},
                        { display: '保养内容', name: 'maintain_content',editor: { type: 'text' }},
                    ],
                    onSelectRow: function (rowdata, rowindex)
                    {
                    },
                    enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                    data:{Rows: _periodData},
                    width: '88%'
                });
            }


            f_initGrid1();
            //设置只读
            $("#serviceForm select,serviceForm input").attr("disabled","disabled");

        });

	</script>
</head>
<body>
<div>
	<ul class="nav nav-tabs">
		<li role="presentation" class="active"><a href="#taskContent" role="tab" data-toggle="tab" aria-controls="home" aria-expanded="true">任务详情</a></li>
		<li role="presentation" ><a href="#flowchart" role="tab" data-toggle="tab" aria-controls="profile" aria-expanded="false">流程图</a></li>
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
                        <script type="text/javascript">${fp.value}</script>
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

	</div>
</div>
</body>
</html>
