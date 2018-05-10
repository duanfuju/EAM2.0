<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>保养年计划</title>
	<meta name="decorator" content="default"/>

	<style>
		.editDiv .l-panel-bar{
			display: none ;
		}
	</style>
	<script type="text/javascript">
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
            //初始化下拉树数据
            $.each(formField, function (index, val) {
                if(val.name == 'status'){
                    val.options = {
                        data : parent._statusData
                    }
                }else  if(val.name == 'project_mode'){
                    val.options = {
                        data : parent._maintainMode
                    }
                }else  if(val.name == 'project_type'){
                    val.options = {
                        data : parent._maintainType
                    }
                }else  if(val.name == 'project_period'){
                    val.options = {
                        data : parent._maintainPeriod
                    }
                }else  if(val.name == 'project_status'){
                    val.options = {
                        data : parent._approveStatus
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

            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields: formField

            };
            $("#inputForm").ligerForm(formConfig);
            // 设备名称树
            $("input[name='dev_id']").on("click",function () {
                layer.open({
                    type: 2,
                    title:'选择设备',
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getArea(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: common.interfaceUrl.inspectionSubjectDeviceSelectUI
                });
            });
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

			//给编辑页面字段赋值
            common.callAjax('post',false,common.interfaceUrl.maintainProjectEditObj,"json",{"id" : parent.editId},function(data){
				//审批时间时间转换
                data.project_stime=DateUtil.dateToStr('yyyy-MM-dd',new Date(data.project_stime));


                var editForm  = liger.get("inputForm");
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
            $('#inputForm').on('change','#project_periodBox',function(){
                $('.showpick div.showpickdiv').eq($(this).val()).css('display','block').siblings().css('display','none');
            });


            $("#btnSubmit").on("click",function () {
                var form = liger.get('inputForm');
                if (!form.valid()) {
                    form.showInvalid();
                }else{
                    //ajax提交
                    sendObj=form.getData();
                    //按周期和时间区间拆分日期
                    var DateArr=[];
                    var ResultArr=[];
                    if($('#project_periodBox').val()=='0'){
                        DateArr=createArrDay();
                        var timeobj = $(".day .hasDatepicker");
                        for(var i=0;i<DateArr.length;i++){
                            var dateTime = DateArr[i]+" "+timeobj.val();
                            ResultArr.push(dateTime);
                        }
                        var periodDetail = $(".day .choose").val();
                        if(periodDetail==""||periodDetail==null){
                            alert("请填写周期！");
                            return;
                        }
                        sendObj.project_cycle = periodDetail;
                    }else if($('#project_periodBox').val()=='1'){
                        DateArr=createArrWeek();
                        var timeobj = $(".week .hasDatepicker");
                        for(var i=0;i<DateArr.length;i++){
                            var dateTime = DateArr[i]+" "+timeobj.val();
                            ResultArr.push(dateTime);
                        }
                        var periodDetail = $(".week .choose").val();
                        if(periodDetail==""||periodDetail==null){
                            alert("请填写周期！");
                            return;
                        }
                        periodDetail+="_"+$("#dayweek").val();
                        sendObj.project_cycle = periodDetail;
                    }else if($('#project_periodBox').val()=='2'){
                        DateArr=createArrMonth();
                        var timeobj = $(".month .hasDatepicker");
                        for(var i=0;i<DateArr.length;i++){
                            var dateTime = DateArr[i]+" "+timeobj.val();
                            ResultArr.push(dateTime);
                        }
                        var periodDetail = $(".month .choose").val();
                        if(periodDetail==""||periodDetail==null){
                            alert("请填写周期！");
                            return;
                        }
                        periodDetail+="_"+$("#monthsel").val();
                        periodDetail+="_"+$("#monthweek").val();
                        sendObj.project_cycle = periodDetail;
                    }else if($('#project_periodBox').val()=='3'){
                        DateArr=createArrQuarter();
                        var timeobj = $(".season .hasDatepicker");
                        for(var i=0;i<DateArr.length;i++){
                            var dateTime = DateArr[i]+" "+timeobj.val();
                            ResultArr.push(dateTime);
                        }
                        var periodDetail = $(".season .choose").val();
                        if(periodDetail==""||periodDetail==null){
                            alert("请填写周期！");
                            return;
                        }
                        periodDetail+="_"+$("#seasonsel").val();
                        periodDetail+="_"+$("#seamonthsel").val();
                        periodDetail+="_"+$("#seaweek").val();
                        sendObj.project_cycle = periodDetail;
                    }else{
                        DateArr=createArrYear();
                        var timeobj = $(".year .hasDatepicker");
                        for(var i=0;i<DateArr.length;i++){
                            var dateTime = DateArr[i]+" "+timeobj.val();
                            ResultArr.push(dateTime);
                        }
                        var periodDetail = $(".year .choose").val();
                        if(periodDetail==""||periodDetail==null){
                            alert("请填写周期！");
                            return;
                        }
                        periodDetail+="_"+$("#yearsel").val();
                        periodDetail+="_"+$("#yearmonthsel").val();
                        periodDetail+="_"+$("#yearweek").val();
                        sendObj.project_cycle = periodDetail;
                    }

                    sendObj.id_key=parent.editId;
                    sendObj.dev_id=_devices;//设备
                    sendObj.maintainProjectContents = _periodGrid.getData();     //保养内容
                    common.callAjax('post',false,common.interfaceUrl.maintainProjectSave,"text",{param:JSON.stringify(sendObj)},function(data) {
                        if (data == "success") {
                            layer.msg('编辑成功！',{icon:1,time: 1000}, function (index) {
                                parent.$("#mytable").DataTable().ajax.reload();
                                parent.layer.closeAll();
                            });
                        }else {
                            layer.msg("编辑失败！",{time: 1000,icon:2});
                        }
                    })
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

            function f_initGrid1()
            {
                _periodGrid = $("#periodTable").ligerGrid({
                    columns: [{ display: '保养内容编码', name: 'maintain_code',editor: { type: 'text' }},
                        { display: '保养内容', name: 'maintain_content',editor: { type: 'text' }},
                        { display: '操作', isSort: false,render: function (rowdata, rowindex, value)
                        {
                            var h = "";
                            h += "<a class='add'>添加</a> ";
                            h += "<a class='dele' data-id='"+ rowindex +"'>删除</a> ";
                            return h;
                        }
                        }

                    ],
                    onSelectRow: function (rowdata, rowindex)
                    {
                    },
                    enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                    data:{Rows: _periodData},
                    width: '88%'
                });
            }

            $('#periodTable').on('click','.dele',function(){
                if (_periodGrid.getData().length ==1){
                    alert("只剩一项时无法删除");
                }else {
                    _periodGrid.deleteRow($(this).data("id"));
                }
            });
            $('#periodTable').on('click','.add',function(){

                var val = _periodGrid.getData()[_periodGrid.getData().length-1].maintain_code;
                val = header_code+(Array(3).join(0) + (parseInt(val.substring(header_code.length,val.length)) + 1)).slice(-3);
                _periodGrid.addRow( {
                    "maintain_code":val,
                    "maintain_content":null,

                });
            });

            f_initGrid1();

        });


	</script>
</head>
<body>
<form id="inputForm" action="" method="post" class="form-horizontal">


</form>
<div class="editDiv">
	<div class="l-clear"></div>
	<div class="subeditDiv" id="periodTable" ></div>
</div>
<div class="form-actions">

	<input id="btnSubmit" type="button" value="保 存"/>
</div>
</body>
</html>
