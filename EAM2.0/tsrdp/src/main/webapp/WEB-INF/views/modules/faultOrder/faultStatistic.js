/**
 * Created by qiye on 2017/9/22.
 */
//拖拽事件
function allowDrop(ev) {
    ev.preventDefault();
}

var srcdiv = null;
function drag(ev, divdom) {
    srcdiv = divdom;
    ev.dataTransfer.setData("text/html", divdom.innerHTML);
}

function drop(ev, divdom) {
    ev.preventDefault();
    if (srcdiv != divdom) {
        srcdiv.innerHTML = divdom.innerHTML;
        divdom.innerHTML = ev.dataTransfer.getData("text/html");
    }
}
define(["text!modules/faultOrder/faultStatistic.html", "text!modules/faultOrder/faultStatistic.css"], function (htmlTemp, cssTemp) {
    var module = {

        init: function (menuno) {
            $(".content-wrapper").html(htmlTemp);
            $(".css-attr").html(cssTemp);
            this.initActionEvent();
            this.initData();

            FusionCharts.ready(function () {
                searchData();
            });

        },

        initData:function() {
            //初始化查询时间
            initStatisticDate();
            function initStatisticDate() {
                $('.timechooseBtn').find('li')[0].click();
            }

            //初始化绩效部门列表
            common.callAjax('post',false,ctx + "/eam/dept/getDataScopeDepts","json",null,function(data){
                if(data.data!=undefined){
                    var dept_options = data.data;
                    $.each(dept_options, function (i, item) {
                        $("#stdTeam").append('<option value="'+item.deptno+'">'+item.deptname+'</option>');
                    });

                };
            });

        },
        initActionEvent:function() {
            //本周本月本季本年
            $('.timechooseBtn li').click(function () {
                $(this).addClass('timechooseActive');
                $(this).siblings().removeClass('timechooseActive');
                choose($(this).index());
                searchData();
            })
            $('.timechoose .begin').datepicker({
                changeMonth: true,
                dateFormat: 'yy-mm-dd',
                // onClose: function(selectedDate) {
                //     $(".timechoose .end").datepicker("option", "minDate", selectedDate);
                // }
            });
            $('.timechoose .end').datepicker({
                changeMonth: true,
                dateFormat: 'yy-mm-dd',
                // onClose: function(selectedDate) {
                //     $(".timechoose .begin").datepicker("option", "maxDate", selectedDate);
                //     $(".timechoose .end").val($(this).val());
                // }
            });
            $("#stdTeam").on("change",function () {
                setScoreTableData("完成率");
            });

            $("#jxzl_cond span").on('click', function () {
                var val = $(this).html();
                $(this).addClass('region');
                $(this).siblings().removeClass('region');
                setScoreTableData(val);
            });
            //工单消耗切换
            $("#gdxh_cond span").on('click', function () {
                $(this).addClass('region');
                $(this).siblings().removeClass('region');
                // setScoreTableData(val);
                //切换树
                var id = $(this).attr('data-id');
                $("."+id).show().siblings('.ztreeBox').hide();
            });

            //提交请求(采用异步请求，一次请求会造成页面卡顿后全部加载,每个控件窗口单独请求，故采取多次请求，)
            $(".searchbtn").on("click",function () {
                searchData();
            });








            function choose(n){
                var now=new Date();
                var nowDayOfWeek = now.getDay(); //今天本周的第几天
                var nowDay = now.getDate(); //当前日
                var nowMonth = now.getMonth(); //当前月
                var nowYear = now.getFullYear(); //当前年
                if(n==0){//本月
                    var monthStartDate = new Date(nowYear, nowMonth, 1);
                    var monthEndDate = new Date(nowYear, nowMonth, getMonthDays(nowYear,nowMonth));
                    $('.timechoose .begin').datepicker('setDate',monthStartDate);
                    $('.timechoose .end').datepicker('setDate',monthEndDate);
                }else if(n==1){//本周
                    var weekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek);
                    var weekEndDate = new Date(nowYear, nowMonth, nowDay + (6 - nowDayOfWeek));
                    $('.timechoose .begin').datepicker('setDate',weekStartDate);
                    $('.timechoose .end').datepicker('setDate',weekEndDate);
                }else if(n==2){//本季
                    var quarterStartDate = new Date(nowYear, getQuarterStartMonth(nowMonth), 1);
                    var quarterEndMonth = getQuarterStartMonth(nowMonth) + 2;
                    var quarterEndDate = new Date(nowYear, quarterEndMonth,
                        getMonthDays(nowYear,quarterEndMonth));
                    $('.timechoose .begin').datepicker('setDate',quarterStartDate);
                    $('.timechoose .end').datepicker('setDate',quarterEndDate);
                }else if(n==3){//本年
                    $('.timechoose .begin').datepicker('setDate',new Date(nowYear,0,1));
                    $('.timechoose .end').datepicker('setDate',new Date(nowYear,11,getMonthDays(nowYear,12)));
                }
            }
            //获得本季度的开始月份
            function getQuarterStartMonth(nowMonth) {
                var quarterStartMonth = 0;
                if (nowMonth < 3) {
                    quarterStartMonth = 0;
                }
                if (2 < nowMonth && nowMonth < 6) {
                    quarterStartMonth = 3;
                }
                if (5 < nowMonth && nowMonth < 9) {
                    quarterStartMonth = 6;
                }
                if (nowMonth > 8) {
                    quarterStartMonth = 9;
                }
                return quarterStartMonth;
            }
            //获得某月的天数
            function getMonthDays(nowYear,myMonth) {
                var monthStartDate = new Date(nowYear, myMonth, 1);
                var monthEndDate = new Date(nowYear, myMonth + 1, 1);
                var days = (monthEndDate - monthStartDate) / (1000 * 60 * 60 * 24);
                return days;
            }

        }





    }

    function searchData() {
        var paramObj = getDateCond();
        if(paramObj == null){
            return;
        }
        //工单占比
        common.callAjax('post',true,ctx + "/statistic/faultOrder/getOccupancyByDept","json",paramObj,function(data){
            if(data.data!=undefined){
                initOrderDeptChart(data.data);
                // FusionCharts.ready(function () {
                //     order_dept_chart.render();
                // });
            }
        });

        //工单总览
        common.callAjax('post',true,ctx + "/statistic/faultOrder/getOrderCountsByStatus","json",paramObj,function(data){
            if(data.data!=undefined){
                initOrderViewChart(data.data);
            }
        });
        //工单周期对比
        common.callAjax('post',true,ctx + "/statistic/faultOrder/getOrderCountsByPeriod","json",paramObj,function(data){
            if(data.data!=undefined){
                initOrderPeriodCompChart(data.data.toDayOrders,data.data.lastDayOrders,data.data.thisWeekOrders,
                    data.data.lastWeekOrders,data.data.thisMonthOrders,data.data.lastMonOrders,data.data.thisYearOrders,data.data.lastYearOrders);
            }



        });
        // //工单消耗
        common.callAjax('post',true,ctx + "/statistic/faultOrder/getDevTree","json",paramObj,function(data){
            if(data !=undefined){
                initDevtree(data);
            }
        });
        //工单人员消耗
        common.callAjax('post',true,ctx + "/statistic/faultOrder/getPersonTree","json",paramObj,function(data){
            if(data !=undefined){
                initPresontree(data);
            }
        });
        //绩效总览
        setScoreTableData('完成率');
        /*common.callAjax('post',true,ctx + "/statistic/faultOrder/getOrderPerformanceByEmp","json",paramObj,function(data){
         if(data.data!=undefined){
         initPerformanceChart(data.data);
         };
         });*/
        //工单来源统计
        common.callAjax('post',true,ctx + "/statistic/faultOrder/getOrderCountsBySource","json",paramObj,function(data){
            if(data.data!=undefined){
                initOrderSourceChart(data.data);
            }
        });
    }

    function initOrderDeptChart(data) {
        //工单占比
        var order_dept_chart = new FusionCharts({
            type: 'pie2d',
            renderAt: 'pieChart',
            width: '100%',
            height: '375',
            dataFormat: 'json',
            dataSource: {
                "chart": {
                    "paletteColors": "#0075c2,#1aaf5d,#f2c500,#f45b00,#8e0000",
                    "bgColor": "#ffffff",
                    "baseFontSize":'14',
                    "showBorder": "0",
                    "use3DLighting": "0",
                    "showShadow": "0",
                    "enableSmartLabels": "0",
                    "startingAngle": "0",
                    "showPercentValues": "1",
                    "showPercentInTooltip": "0",
                    "decimals": "1",
                    "captionFontSize": "18",
                    "subcaptionFontSize": "16",
                    "subcaptionFontBold": "2",
                    "toolTipColor": "#ffffff",
                    "toolTipBorderThickness": "0",
                    "toolTipBgColor": "#000000",
                    "toolTipBgAlpha": "80",
                    "toolTipBorderRadius": "2",
                    "toolTipPadding": "5",
                    "showHoverEffect": "1",
                    "showLegend": "1",
                    "legendBgColor": "#ffffff",
                    "legendBorderAlpha": '0',
                    "legendShadow": '0',
                    // "legendItemFontSize": '16',
                    "legendItemFontColor": '#666666'
                },
                "data": data
            }
        }).render();
    }

    function initOrderViewChart(data) {
        //工单总览
        var orderStatus = data.orderStatusCount;
        var totalOrders = 0;
        var statusBallHtml = '';
        $.each(orderStatus, function (i, item) {
            var id = 'ballsTop'+eval(i+1);
            var ballClass = id;
            var ballNum = item.numbers;
            var title = item.title;
            totalOrders += parseInt(ballNum);
            statusBallHtml = statusBallHtml+ '<li><span id="'+id+'" class="ballsTop '+ballClass+'">'+ballNum+'</span><p class="ballsBottom">'+title+'</p></li>';
        });
        var totalNumHtml = '<ul><li><span id="numBall" class="numBall">'+totalOrders+'</span><p class="ballsBottom">工单总数</p></li>';
        statusBallHtml = totalNumHtml + statusBallHtml+ '</ul>';

        $('#orderStatusBall').html(statusBallHtml);
        var categorys = data.majorOrderCount.category;
        var allOrders = data.majorOrderCount.allOrders;
        var finishOrders = data.majorOrderCount.finishOrders;

        var order_view_chart = new FusionCharts({
            type: 'scrollstackedcolumn2d',
            dataFormat: 'json',
            renderAt: 'orderOv',
            width: '100%',
            height: '300',
            dataSource: {
                "chart": {
                    "xaxisname": "系统",
                    "yaxisname": "条",
                    "showvalues": "0",
                    "baseFontSize":'14',
                    "numberprefix": "",
                    "legendBgAlpha": "0",
                    "legendBorderAlpha": "0",
                    "legendShadow": "1",
                    "showborder": "0",
                    "bgcolor": "#ffffff",
                    "showalternatehgridcolor": "0",
                    "showplotborder": "0",
                    "showcanvasborder": "0",
                    "legendshadow": "0",
                    "plotgradientcolor": "",
                    "showCanvasBorder": "0",
                    "showAxisLines": "0",
                    "showAlternateHGridColor": "0",
                    "divlineAlpha": "100",
                    "divlineThickness": "1",
                    "divLineIsDashed": "1",
                    "divLineDashLen": "1",
                    "divLineGapLen": "1",
                    "lineThickness": "3",
                    "flatScrollBars": "1",
                    "scrollheight": "10",
                    "numVisiblePlot": "12",
                    "showHoverEffect":"1"
                },
                "categories": [
                    {
                        "category": categorys
                    }
                ],
                "dataset": [
                    {
                        "seriesname": "已完成",
                        "color": "008ee4",
                        "data": finishOrders
                    },
                    {
                        "seriesname": "工单总数",
                        "color": "f8bd19",
                        "data": allOrders
                    }
                ]
            }
        }).render();
    }
    // 工单周期对比
    function initOrderPeriodCompChart(toDayOrders,lastDayOrders,thisWeekOrders,lastWeekOrders,thisMonthOrders,lastMonOrders,thisYearOrders,lastYearOrders) {
        var dayIncreaceClass = getIncreaceClass(toDayOrders,lastDayOrders);
        var dayNumIncreaceClass = getIncreaceNumClass(toDayOrders,lastDayOrders);
        var dayNumIncreaceRate = getIncreaceRate(toDayOrders,lastDayOrders);
        var weekIncreaceClass = getIncreaceClass(thisWeekOrders,lastWeekOrders);
        var weekNumIncreaceClass = getIncreaceNumClass(thisWeekOrders,lastWeekOrders);
        var weekNumIncreaceRate = getIncreaceRate(thisWeekOrders,lastWeekOrders);
        var monthIncreaceClass = getIncreaceClass(thisMonthOrders,lastMonOrders);
        var monthNumIncreaceClass = getIncreaceNumClass(thisMonthOrders,lastMonOrders);
        var monthNumIncreaceRate = getIncreaceRate(thisMonthOrders,lastMonOrders);
        var yearIncreaceClass = getIncreaceClass(thisYearOrders,lastYearOrders);
        var yearNumIncreaceClass = getIncreaceNumClass(thisYearOrders,lastYearOrders);
        var yearNumIncreaceRate = getIncreaceRate(thisYearOrders,lastYearOrders);
        var periodDivHtml = '<div class="periodCata"><div class="periodLeft">日</div><ul class="periodContent"><li>今日工单总数</li><li>昨日工单总数</li></ul>' +
            '<ul class="periodNum"><li>'+toDayOrders+'</li><li>'+lastDayOrders+'</li></ul><div class="'+dayIncreaceClass+'"  center"></div>' +
            '<div class="percentNum '+dayNumIncreaceClass+' center">'+dayNumIncreaceRate+'</div></div>'
        periodDivHtml += '<div class="periodCata"><div class="periodLeft">周</div><ul class="periodContent"><li>本周工单总数</li><li>上周工单总数</li></ul>' +
            '<ul class="periodNum"><li>'+thisWeekOrders+'</li><li>'+lastWeekOrders+'</li></ul><div class="'+weekIncreaceClass+'"  center"></div>' +
            '<div class="percentNum '+weekNumIncreaceClass+' center">'+weekNumIncreaceRate+'</div></div>'
        periodDivHtml += '<div class="periodCata"><div class="periodLeft">月</div><ul class="periodContent"><li>本月工单总数</li><li>上月工单总数</li></ul>' +
            '<ul class="periodNum"><li>'+thisMonthOrders+'</li><li>'+lastMonOrders+'</li></ul><div class="'+monthIncreaceClass+'"  center"></div>' +
            '<div class="percentNum '+monthNumIncreaceClass+' center">'+monthNumIncreaceRate+'</div></div>';
        periodDivHtml += '<div class="periodCata"><div class="periodLeft">年</div><ul class="periodContent"><li>今年工单总数</li><li>去年工单总数</li></ul>' +
            '<ul class="periodNum"><li>'+thisYearOrders+'</li><li>'+lastYearOrders+'</li></ul><div class="'+yearIncreaceClass+'"  center"></div>' +
            '<div class="percentNum '+yearNumIncreaceClass+' center">'+yearNumIncreaceRate+'</div></div>';
        $("#periodDataDiv").html(periodDivHtml);

        function getIncreaceClass(val1,val2){
            if(val1 == val2){
                return 'equal';
            }else if(val1 > val2){
                return 'up';
            }else if(val1 < val2){
                return 'down';
            }
        }
        function getIncreaceNumClass(val1,val2){
            if(val1 == val2){
                return 'equalNum';
            }else if(val1 > val2){
                return 'upNum';
            }else if(val1 < val2){
                return 'downNum';
            }
        }
        function getIncreaceRate(val1,val2){
            var rate;
            if(val1 == val2){
                rate = '---';
            }else if(val1 > val2 && val2 != '0'){
                rate = parseInt((val1-val2)*100/val2)+'%';
            }else if(val1 < val2 && val1 != '0'){
                rate = parseInt((val2-val1)*100/val1);
                rate = '-'+rate+'%'
            }else if(val1 < val2){
                rate = '-∞';
            }else if(val1 > val2){
                rate = '∞';
            }
            return rate;
        }

    }
//工单消耗
    function initDevtree(zNodes) {
        // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）

        var setting = {
            view: {
                dblClickExpand: false,
                showLine: true,
                selectedMulti: false
            },
            data: {
                simpleData: {
                    enable:true,
                    idKey: "id",
                    pIdKey: "pId",
                    rootPId: ""
                }
            },
            callback: {
                onClick:zTreeOnClick
            }
        };
        function zTreeOnClick(event, treeId, treeNode,clickFlag){
            var type;
            var param=new Object();
            param.startDate=$('.begin').val();
            param.endDate=$('.end').val();
            param.id=treeNode.id;
            if (treeNode.isParent) {// 父菜单
                param.type="devcategory";
            }else{
                param.type="devid";
            }
            common.callAjax('post',true,ctx + "/statistic/faultOrder/getCostsByDev","json",param,function(data){
                costDetailTable(data.data);
                totalCostDetail(data.total);
            });
        }
    function costDetailTable(data){
        var trHTML="";
        $.each(data,function(i,dataList){
            var sign=(i%2==0?"odd":"");
            trHTML+="<tr class=\""+sign+"\"><td>"+(i+1)+"</td><td>"+dataList.tool_name+"</td><td>"+dataList.num+"</td><td>"+dataList.total+"</td></tr>";
        });

        $("#costDetailTable tbody").html(trHTML);
    }
    function totalCostDetail(data){
        if(data.total_num==undefined){
            $("#sumToolNum").html(0+"个");
        }else{
            $("#sumToolNum").html(data.total_num+"个");
        }
        if(data.total_hour==undefined){
            $("#sumEmpHour").html(0+"小时");
        }else{
            $("#sumEmpHour").html(data.total_hour+"小时");
        }
        if(data.total_pay==undefined ){
            $("#sumCost").html(0+"元");
        }else{
            $("#sumCost").html(data.total_pay+"元");
        }
    }

        $(document).ready(function(){
            $.fn.zTree.init($("#deviceTree"), setting, zNodes);
        });
    }
    //人员消耗
    function initPresontree(zNodes){

        // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）

        var setting = {
            view: {
                dblClickExpand: false,
                showLine: true,
                selectedMulti: false
            },
            data: {
                simpleData: {
                    enable:true,
                    idKey: "id",
                    pIdKey: "pId",
                    rootPId: ""
                }
            },
            callback: {
                onClick:zTreeOnClick
            }
        };
        function zTreeOnClick(event, treeId, treeNode,clickFlag){
            var type;
            var param=new Object();
            param.startDate=$('.begin').val();
            param.endDate=$('.end').val();
            param.id=treeNode.id;
            if (treeNode.isParent) {// 父菜单
                param.type="dept";
            }else{
                param.type="emp";
            }
            common.callAjax('post',true,ctx + "/statistic/faultOrder/getCostsByEmp","json",param,function(data){
                costDetailTable(data.data);
                totalCostDetail(data.total);
            });
        }
        function costDetailTable(data){
            var trHTML="";
            $.each(data,function(i,dataList){
                var sign=(i%2==0?"odd":"");
                trHTML+="<tr class=\""+sign+"\"><td>"+(i+1)+"</td><td>"+dataList.tool_name+"</td><td>"+dataList.num+"</td><td>"+dataList.total+"</td></tr>";
            });

            $("#costDetailTable tbody").html(trHTML);
        }
        function totalCostDetail(data){
            if(data.total_num==undefined){
                $("#sumToolNum").html(0+"个");
            }else{
                $("#sumToolNum").html(data.total_num+"个");
            }
            if(data.total_hour==undefined){
                $("#sumEmpHour").html(0+"小时");
            }else{
                $("#sumEmpHour").html(data.total_hour+"小时");
            }
            if(data.total_pay==undefined ){
                $("#sumCost").html(0+"元");
            }else{
                $("#sumCost").html(data.total_pay+"元");
            }
        }

        $(document).ready(function(){
            $.fn.zTree.init($("#empTree"), setting, zNodes);
        });
    }

    function initPerformanceChart(data) {
        var scoreTableHtml = '<tbody>';
        $.each(data, function (i, item) {
            var index = i+1;
            scoreTableHtml+='<tr><td class="tr-num">'+index+'</td><td width="45%"><div class="td-progress-bar"><span class="td-progress-shape" style="width:'+item.length+';"></span>';
            scoreTableHtml+='<span class="td-progress-score">'+item.rate+'</span></div></td><td class="tr-time txt-align-r">'+item.realname+'</td></tr>';
        });
        scoreTableHtml += '</tbody>';
        $('#scoreTable').html(scoreTableHtml);

    }

    function initOrderSourceChart(data) {
        //工单来源占比
        var order_source_chart = new FusionCharts({
            type: 'pie2d',
            renderAt: 'pieChart2',
            width: '100%',
            height: '375',
            dataFormat: 'json',
            dataSource: {
                "chart": {
                    "paletteColors": "#0075c2,#1aaf5d,#f2c500,#f45b00,#8e0000",
                    "bgColor": "#ffffff",
                    "baseFontSize":'14',
                    "showBorder": "0",
                    "use3DLighting": "0",
                    "showShadow": "0",
                    "enableSmartLabels": "0",
                    "startingAngle": "0",
                    "showPercentValues": "1",
                    "showPercentInTooltip": "0",
                    "decimals": "1",
                    "captionFontSize": "18",
                    "subcaptionFontSize": "16",
                    "subcaptionFontBold": "2",
                    "toolTipColor": "#ffffff",
                    "toolTipBorderThickness": "0",
                    "toolTipBgColor": "#000000",
                    "toolTipBgAlpha": "80",
                    "toolTipBorderRadius": "2",
                    "toolTipPadding": "5",
                    "showHoverEffect": "1",
                    "showLegend": "1",
                    "legendBgColor": "#ffffff",
                    "legendBorderAlpha": '0',
                    "legendShadow": '0',
                    // "legendItemFontSize": '16',
                    "legendItemFontColor": '#666666'
                },
                "data": data
            }
        }).render();
    }

    //获取绩效总览的数据
    function setScoreTableData(_jxzl_cond_click_data) {
        var paramObj = getDateCond();
        if(paramObj == null){
            return;
        }
        paramObj.deptno = $("#stdTeam").val();
        paramObj.type = _jxzl_cond_click_data;
        common.callAjax('post',true,ctx + "/statistic/faultOrder/getOrderPerformanceByEmp","json",paramObj,function(data){
            if(data.data!=undefined){
                initPerformanceChart(data.data);
            }else{
                $('#scoreTable').html("");
            };
        });
    }

    function getDateCond() {
        var paramObj = new Object();
        paramObj.startDate = $('.timechoose .begin').val();
        paramObj.endDate = $('.timechoose .end').val();
        if(paramObj.startDate == ''|| paramObj.endDate ==''){
            layer.msg('请先选择查询时间',{icon:1,time: 1000});
            return null;
        }
        return paramObj;
    }


    return module;
});