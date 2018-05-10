<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>保养设置</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" type="text/css" href="/resource/form.css">

	<style>
		.editDiv .l-panel-bar{
			display: none ;
		}
	</style>
	<script type="text/javascript">
        var idStart='S';
        var idEnd='E';
        $(function () {
            //深克隆
            function cloneObject(obj) {
                //var o = obj instanceof Array ? [] : {};
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
                    index.newline=true;

                }

            });
            //设置所有字段只读属性
            //初始化下拉树数据
            var formFieldDisplay=[];
            var formFields = cloneObject(parent.formConfig);
            $.each(formFields, function(index,val){
                if(val.name=="project_bm"){
                    val.options = {
                        isMultiSelect : true,
                        valueField : 'id',
                        treeLeafOnly : true,
                        tree : {
                            url :   common.interfaceUrl.getDeptTreeData,
                            checkbox : false,
                            parentIcon: null,
                            childIcon: null,
                            idFieldName : 'id',
                            parentIDFieldName : 'pId',
                            nodeWidth:200,
                            ajaxType : 'post',
                            textFieldName:'name',//文本字段名，默认值text
                            autoCheckboxEven:false,//复选框联动
                            onSelect:function(e){
                                console.log(e);
                            }
                        }
                    }
                }else if(val.name=="dev_id"){
                    formFieldDisplay.push(val)
                }else if(val.name=="project_empid"){
                    val.options={
                        data: parent.projectEmpid
                    }
                }else if(val.name=="project_mode"){
                    val.options={
                        data: parent.projectMode
                    }

                }else if(val.name=="project_type"){
                    val.options={
                        data: parent.projectType
                    }

                }
                else if(val.name=="project_period"){
                    val.options={
                        data: parent.projectCycle
                    }

                }else if(val.name=="status"){
                    val.options={
                        data: parent.projectStatus
                    }

                }
                val.readonly = true;

            });

            //创建表单结构
            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields: formFields

            };
            $("#inputForm").ligerForm(formConfig);


            //保养周期
            var periodHtml='<div class="showpick">'+
                '<div class="day showpickdiv" style="display: block;">'+
                '<span>每</span>'+
                '<input type="text" class="choose"/>'+
                '<span>天</span>'+
                '</div>'+
                '<div class="week showpickdiv" style="display: none;">'+
                '<span>每</span>'+
                '<input type="text" class="choose"/>'+
                '<span>周</span>' +
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
                '<option data-value="4" value="5">5</option>'+
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
                '<option data-value="4" value="5">5</option>'+
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
                '<option data-value="4" value="5">5</option>' +
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
//            $('.showpick .time').timepicker({
//                hourGrid: 4,
//                minuteGrid: 10
//            })
            ;

            // 保养内容列表
            var _subjectDefault=[];
            var now_time=new Date().format("yyyyMMddhhmmss");
            console.log(now_time);

            var header_code= 'BYBM'+ now_time;
            var f_code=header_code+"001";
            var maintset_content=null;
            common.callAjax('post',false,ctx+'/maintenance/maintSet/quMaintContent',"json",{"MaintSet_id" : parent.editId},function(data){
                if(data == null || data.length == 0){
                    maintset_content = [{
                        "procedure_code": f_code,
                        "procedure_desc" : null,
                        "procedure_standard" : null,
                        "procedure_remark" : null
                    }];
                } else {
                    maintset_content = data;
                }
            })
            function f_initGrid1() {
                _charGrid = $("#charStdTable").ligerGrid({
                    columns: [{display: '保养内容编码', name: 'maintain_code', editor: {type: 'text'}},
                        {display: '保养内容', name: 'maintain_content', editor: {type: 'text'}}

                    ],
                    onSelectRow: function (rowdata, rowindex) {
                    },
                    enabledEdit: false, isScroll: false, checkbox: false, rownumbers: true,

                    data: {Rows: maintset_content},
                    width: '88%'
                });
            }





            f_initGrid1();



//           //给编辑页面字段赋值
//            common.callAjax('post',false,ctx + "/maintenance/maintSet/editObj","json",{"id" : parent.editId},function(alldata){
//
//                //下拉初始化
//                common.callAjax('post',true,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "common"},function(data1){
//                    var statusHtml="";
//                    $.each(data1, function (i, item) {
//                        statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
//                    });
//                    $("#statusBox").html(statusHtml).val(alldata.status).trigger('change.select2');
//                });
//                //保养分类
//                common.callAjax('post',true,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "maintain_type"},function(data){
//                    var statusHtml="";
//                    $.each(data, function (i, item) {
//                        statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
//                    });
//                    $("#project_typeBox").html(statusHtml).val(alldata.project_type).trigger('change.select2');
//                });
//                //保养类别
//                common.callAjax('post',true,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "maintain_mode"},function(data){
//                    var statusHtml="";
//                    $.each(data, function (i, item) {
//                        statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
//                    });
//                    $("#project_modeBox").html(statusHtml).val(alldata.project_mode).trigger('change.select2');
//                });
//
//                //人员
//                common.callAjax('post',false,common.interfaceUrl.userExtGetAllUser,"json",null,function(data){
//
//                    var statusHtml="";
//                    $.each(data, function (i, item) {
//                        statusHtml += "<option value=\"" + item.loginname + "\">" + item.loginname + "</option>";
//                    });
//                    $("#project_empidBox").html(statusHtml).val(alldata.project_empid).trigger('change.select2');
//
//                });
//  //巡检周期
//                common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "maintain_period"},function(data){
//                    var statusHtml="";
//                    $.each(data, function (i, item) {
//                        statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
//                    });
//                    $("#project_cycleBox").html(statusHtml).val(alldata.project_period).trigger('change.select2');
//
//                    $('.showpick div.showpickdiv').eq(alldata.project_period).css('display','block').siblings().css('display','none');
//
//                    var period_detail=alldata.project_cycle;
//                    if(alldata.project_period==0){
//                        $('.day .choose').val(period_detail);
//                    }else if(alldata.project_period==1){
//                        debugger;
//                        var week=period_detail.split('_');
//                        $('.week .choose').val(week[0]);
//                        $('#dayweek').val(week[1]).trigger('change.select2');
//                    }else if(alldata.project_period==2){
//                        var month=period_detail.split('_');
//                        $('.month .choose').val(month[0]);
//                        $('#monthsel').val(month[1]).trigger('change.select2');
//                        $('#monthweek').val(month[2]).trigger('change.select2');
//                    }else if(alldata.project_period==3){
//                        var season=period_detail.split('_');
//                        $('.season .choose').val(season[0]);
//                        $('#seasonsel').val(season[1]).trigger('change.select2');
//                        $('#seamonthsel').val(season[2]).trigger('change.select2');
//                        $('#seaweek').val(season[3]).trigger('change.select2');
//                    }else{
//                        var year=period_detail.split('_');
//                        $('.year .choose').val(year[0]);
//                        $('#yearsel').val(year[1]).trigger('change.select2');
//                        $('#yearmonthsel').val(year[2]).trigger('change.select2');
//                        $('#yearweek').val(year[3]).trigger('change.select2');
//                    }
//                });
//
//                //巡检周期change事件
//                $('#inputForm').on('change','#project_cycleBox',function(){
//                    $('.showpick div.showpickdiv').eq($(this).val()).css('display','block');
//                    $('.showpick div.showpickdiv').eq($(this).val()).siblings().css('display','none');
//                });
//
//                $('input[name="maint_check_cont"]').val(alldata.maint_check_cont);
//                $('input[name="maint_check_mark"]').val(alldata.maint_check_mark);
//                $('input[name="project_code"]').val(alldata.project_code);
//                $('input[name="project_name"]').val(alldata.project_name);
//                $('input[name="project_bm"]').val(alldata.project_bm_name);
//                $('input[name="project_stime"]').val(alldata.project_stime);
//                $("#project_bm").val(alldata.project_bm);
//
//
////				if(html !="")
////                    $('.addEnable').before(html);
//
//                    //巡检区域
//				$("input[name='dev_id']").val(alldata.dev_names);
//                _devices=alldata.dev_id;
////                    selectedAreaObj=data.rareas;
//
//
//            });
//            巡检周期change事件
            $('#inputForm').on('change','#project_periodBox',function(){
                $('.showpick div.showpickdiv').eq($(this).val()).css('display','block');
                $('.showpick div.showpickdiv').eq($(this).val()).siblings().css('display','none');
            });
            //编辑页面字段赋值
            $.ajax({
                type : "post",
                async : false, //同步执行
                url : ctx +"/maintenance/maintSet/editObj?id="+parent.editId,
                dataType : "json", //传递数据形式为text
                success : function(data)
                {
                    debugger;
                    console.log(data)
                    var editForm  = liger.get("inputForm");
                    editForm.setData(data);
                    $("input[name='dev_id']").val(data.dev_names);
                    $($("input[name='project_bm']")[0]).val(data.project_bm_name);
                    _devices=data.dev_id;
                    var period_detail=data.project_cycle;
                    debugger;
                    if(data.project_period==0){
                        $('.day .choose').val(period_detail);
                    }else if(data.project_period==1){
                        debugger;
                        var week=period_detail.split('_');
                        $('.week .choose').val(week[0]);
                        $('#dayweek').val(week[1]).trigger('change.select2');
                    }else if(data.project_period==2){
                        var month=period_detail.split('_');
                        $('.month .choose').val(month[0]);
                        $('#monthsel').val(month[1]).trigger('change.select2');
                        $('#monthweek').val(month[2]).trigger('change.select2');
                    }else if(data.project_period==3){
                        var season=period_detail.split('_');
                        $('.season .choose').val(season[0]);
                        $('#seasonsel').val(season[1]).trigger('change.select2');
                        $('#seamonthsel').val(season[2]).trigger('change.select2');
                        $('#seaweek').val(season[3]).trigger('change.select2');
                    }else{
                        var year=period_detail.split('_');
                        $('.year .choose').val(year[0]);
                        $('#yearsel').val(year[1]).trigger('change.select2');
                        $('#yearmonthsel').val(year[2]).trigger('change.select2');
                        $('#yearweek').val(year[3]).trigger('change.select2');
                    }

                }
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
            $("#closeBtn").on("click",function () {
                parent.layer.closeAll();

            });

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



            //设置只读
         $("select").attr("disabled","disabled");

          $(".choose").attr("disabled","disabled");


            //时间设置为只读 找到input当前元素,设置只读.
            $("input[ligeruiid='project_stime']").attr("disabled","disabled");

        });


	</script>
</head>
<body>
<form id="inputForm" action="" method="post" class="form-horizontal">


</form>
<div class="editDiv">
	<div class="l-clear"></div>
	<div class="subeditDiv" id="charStdTable" ></div>
</div>
<div class="form-actions">

	<input id="closeBtn"  type="button"  value="关 闭"/>
</div>
</body>
</html>
