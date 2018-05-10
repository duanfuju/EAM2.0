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
var n=0;
var pevent;
define(["text!modules/module1/module1.html", "text!modules/module1/module1.css"], function (htmlTemp, cssTemp) {
    var module = {
        init: function () {
            $(".content-wrapper").html(htmlTemp);
            $(".css-attr").html(cssTemp);
            //打开页面跳出帮助
            // var steplist = $.WebPageGuide({showCloseButton:true});
            // steplist.newGuidStep("#bugg","这是标题1啊","更嫩诶诶额");
            // steplist.newGuidStep(".bugg1","这是标题2啊","更嫩诶诶额");
            // steplist.startGuide();
            //打开页面跳出帮助
            var tab = common.renderTable("mytable", {
                "order": [[3, "desc"]],
                "hascheckbox": true,
                "hasIndex": true,
                "opBtns": [{
                    "icon": "fa-comment-o", "title": "删除", "callBack": function (data) {
                        console.log(data)
                    }
                }, {
                    "icon": "fa-book", "title": "删除", "callBack": function (data) {
                        console.log(data)
                    }
                }],
                "ajax": {
                    "url": common.interfaceUrl.tableData,
                    "dataSrc": function (json) {
                        return json.data.data;
                    }
                },
                "columns": [
                    {"data": "materialCode"},
                    {"data": "materialName"},
                    {"data": "materialNumber"},
                    {"data": "materialUnit"},
                    {"data": "materialCost"},
                    {"data": "materialType"},
                    {"data": "materialSupplier"},
                    {"data": "materialLevel"},
                    {"data": "materialPurchaseMethods"},
                    {"data": "materialStatus"},

                ]
            });
            var tab2 = common.renderTable("mytable2", {
                "order": [[3, "desc"]],
                "hascheckbox": true,
                "opBtns": [{
                    "icon": "fa-book", "title": "删除", "callBack": function (data) {
                        console.log(data)
                    }
                }, {
                    "icon": "fa-book", "title": "删除", "callBack": function (data) {
                        console.log(data)
                    }
                }],
                "fnDrawCallback": function () {
                    this.api().column(1).nodes().each(function (cell, i) {
                        cell.innerHTML = i + 1;
                    });
                },
                "ajax": {
                    "url": common.interfaceUrl.tableData,
                    "dataSrc": function (json) {
                        return json.data.data;
                    }
                },
                "columns": [
                    {"data": "id", "orderable": false, title: "序号"},
                    {"data": "materialCode", title: "1"},
                    {"data": "materialName", title: "2"},
                    {"data": "materialNumber", title: "3"},
                    {"data": "materialUnit", title: "4"},
                    {"data": "materialCost", title: "5"},
                    {"data": "materialType", title: "6"},
                    {"data": "materialSupplier", title: "7"},
                    {"data": "materialLevel", title: "8"},
                    {"data": "materialPurchaseMethods", title: "9"},
                    {"data": "materialStatus", title: "10"},
                ],
            });
            $(".bugg").on("click", function () {
                alert(JSON.stringify(tab.getCheckedRow()));
            })
            $(".bugg1").on("click", function () {
                common.loadModule("modules/module2/module2")
            })
            $(".importExcel").on("click", function () {
                $("#fileField").click();
            })

            $(".submit").on("click", function () {
                setExcel();
            });

            //导入excel
            function setExcel() {
                $.ajaxFileUpload({
                    /*java.io.IOException: Corrupt form data: premature ending*/
                    /*   url:common.interfaceUrl.fileUp,*/
                    url: "../fileupload.do",
                    secureuri: false,
                    fileElementId: 'fileField',
                    dataType: 'html',
                    success: function (d, s) {
                        var json = eval('(' + d + ')');
                        $.ajax({
                            /*  url: "../importServlet.do?a=customer",*/
                            url: common.interfaceUrl.importExcel,
                            type: 'POST',
                            data: {'name': json.uid + json.type, 'fileName': json.fileName, a: 'customer'},
                            async: false,
                            dataType: "json",
                            success: function (data) {
                                console.log(data);
                                alert(1)
                                alert(data.msg);
                            },
                            error: function (data) {
                                alert(0)
                                var msg = data.responseText;
                                layer.open({
                                    type: 1,
                                    skin: 'layui-layer-rim', //加上边框
                                    area: ['420px', '240px'], //宽高
                                    content: msg
                                });
                            }
                        });
                    }
                })
            }

            //jedate使用方法
            $('#test01').jeDate({
                format:"YYYY-MM-DD hh:mm:ss",               //日期格式
                minDate:"1900-01-01 00:00:00",              //最小日期 或者 "1900-01-01" 或者 "10:30:25"
                maxDate:"2099-12-31 23:59:59",              //最大日期 或者 "2099-12-31" 或者 "16:35:25"
                isShow:true,                                //是否显示为固定日历，为false的时候固定显示
                multiPane:true,                             //是否为双面板，为false是展示双面板
                onClose:true,                               //是否为选中日期后关闭弹层，为false时选中日期后关闭弹层
                range:null,                                 //如果不为空，则会进行区域选择，例如 " 至 "，" ~ "，" To "                               //有效日期与非有效日期，例如 ["0[4-7]$,1[1-5]$,2[58]$",true]
                isinitVal:false,                            //是否初始化时间，默认不初始化时间
                initDate:{},                                //初始化时间，加减 天 时 分
                isTime:true,                                //是否开启时间选择
                isClear:true,                               //是否显示清空
                isToday:true,                               //是否显示今天或本月
                fixed:true,                                 //是否静止定位，为true时定位在输入框，为false时居中定位
                clearfun:function(elem, val) {},            //清除日期后的回调, elem当前输入框ID, val当前选择的值
                okfun:function(obj) {var data = obj.val},   //点击确定后的回调,obj包含{ elem当前输入框ID, val当前选择的值, date当前的日期值}
                success:function(elem) {},                  //层弹出后的成功回调方法, elem当前输入框ID
            })


            FusionCharts.ready(function () {
                var csatGauge = new FusionCharts({
                    "type": "angulargauge",
                    "renderAt": "csatGauge",
                    "width": "630",
                    "height": "300",
                    "dataFormat": "json",
                    "dataSource": {
                        "chart": {
                            "caption": "Customer Satisfaction Score",
                            "subcaption": "Last week",
                            "lowerLimit": "0",
                            "upperLimit": "100",
                            "theme": "fint"
                        },
                        "colorRange": {
                            "color": [
                                {
                                    "minValue": "0",
                                    "maxValue": "50",
                                    "code": "#e44a00"
                                },
                                {
                                    "minValue": "50",
                                    "maxValue": "75",
                                    "code": "#f8bd19"
                                },
                                {
                                    "minValue": "75",
                                    "maxValue": "100",
                                    "code": "#6baa01"
                                }
                            ]
                        },
                        "dials": {
                            "dial": [
                                {
                                    "value": "67"
                                }
                            ]
                        }
                    }
                });
                csatGauge.render();

                var Gauge = new FusionCharts({
                    "type": "column2d",
                    "renderAt": "Gauge",
                    "width": "630",
                    "height": "300",
                    "dataFormat": "json",
                    "dataSource": {
                        "chart": {
                            "caption": "Monthly revenue for last year",
                            "subCaption": "Harry's SuperMart",
                            "xAxisName": "Month",
                            "yAxisName": "Revenues (In USD)",
                            "theme": "fint"
                        },
                        "data": [
                            {
                                "label": "Jan",
                                "value": "420000"
                            },
                            {
                                "label": "Feb",
                                "value": "810000"
                            },
                            {
                                "label": "Mar",
                                "value": "720000"
                            },
                            {
                                "label": "Apr",
                                "value": "550000"
                            },
                            {
                                "label": "May",
                                "value": "910000"
                            },
                            {
                                "label": "Jun",
                                "value": "510000"
                            },
                            {
                                "label": "Jul",
                                "value": "680000"
                            },
                            {
                                "label": "Aug",
                                "value": "620000"
                            },
                            {
                                "label": "Sep",
                                "value": "610000"
                            },
                            {
                                "label": "Oct",
                                "value": "490000"
                            },
                            {
                                "label": "Nov",
                                "value": "900000"
                            },
                            {
                                "label": "Dec",
                                "value": "730000"
                            }
                        ]
                    }
                });
                Gauge.render();

                var Map = new FusionCharts({
                    "type": "maps/usa",
                    "renderAt": "Map",
                    "width": "630",
                    "height": "300",
                    "dataFormat": "json",
                    "dataSource": {
                        "chart": {
                            "caption": "Annual Sales by State",
                            "subcaption": "Last year",
                            "entityFillHoverColor": "#cccccc",
                            "numberScaleValue": "1,1000,1000",
                            "numberScaleUnit": "K,M,B",
                            "numberPrefix": "$",
                            "showLabels": "1",
                            "theme": "fint"
                        },
                        "colorrange": {
                            "minvalue": "0",
                            "startlabel": "Low",
                            "endlabel": "High",
                            "code": "#e44a00",
                            "gradient": "1",
                            "color": [
                                {
                                    "maxvalue": "56580",
                                    "displayvalue": "Average",
                                    "code": "#f8bd19"
                                },
                                {
                                    "maxvalue": "100000",
                                    "code": "#6baa01"
                                }
                            ]
                        },
                        "data": [
                            {
                                "id": "HI",
                                "value": "3189"
                            },
                            {
                                "id": "DC",
                                "value": "2879"
                            },
                            {
                                "id": "MD",
                                "value": "920"
                            },
                            {
                                "id": "DE",
                                "value": "4607"
                            },
                            {
                                "id": "RI",
                                "value": "4890"
                            },
                            {
                                "id": "WA",
                                "value": "34927"
                            },
                            {
                                "id": "OR",
                                "value": "65798"
                            },
                            {
                                "id": "CA",
                                "value": "61861"
                            },
                            {
                                "id": "AK",
                                "value": "58911"
                            },
                            {
                                "id": "ID",
                                "value": "42662"
                            },
                            {
                                "id": "NV",
                                "value": "78041"
                            },
                            {
                                "id": "AZ",
                                "value": "41558"
                            },
                            {
                                "id": "MT",
                                "value": "62942"
                            },
                            {
                                "id": "WY",
                                "value": "78834"
                            },
                            {
                                "id": "UT",
                                "value": "50512"
                            },
                            {
                                "id": "CO",
                                "value": "73026"
                            },
                            {
                                "id": "NM",
                                "value": "78865"
                            },
                            {
                                "id": "ND",
                                "value": "50554"
                            },
                            {
                                "id": "SD",
                                "value": "35922"
                            },
                            {
                                "id": "NE",
                                "value": "43736"
                            },
                            {
                                "id": "KS",
                                "value": "32681"
                            },
                            {
                                "id": "OK",
                                "value": "79038"
                            },
                            {
                                "id": "TX",
                                "value": "75425"
                            },
                            {
                                "id": "MN",
                                "value": "43485"
                            },
                            {
                                "id": "IA",
                                "value": "46515"
                            },
                            {
                                "id": "MO",
                                "value": "63715"
                            },
                            {
                                "id": "AR",
                                "value": "34497"
                            },
                            {
                                "id": "LA",
                                "value": "70706"
                            },
                            {
                                "id": "WI",
                                "value": "42382"
                            },
                            {
                                "id": "IL",
                                "value": "73202"
                            },
                            {
                                "id": "KY",
                                "value": "79118"
                            },
                            {
                                "id": "TN",
                                "value": "44657"
                            },
                            {
                                "id": "MS",
                                "value": "66205"
                            },
                            {
                                "id": "AL",
                                "value": "75873"
                            },
                            {
                                "id": "GA",
                                "value": "76895"
                            },
                            {
                                "id": "MI",
                                "value": "67695"
                            },
                            {
                                "id": "IN",
                                "value": "33592"
                            },
                            {
                                "id": "OH",
                                "value": "32960"
                            },
                            {
                                "id": "PA",
                                "value": "54346"
                            },
                            {
                                "id": "NY",
                                "value": "42828"
                            },
                            {
                                "id": "VT",
                                "value": "77411"
                            },
                            {
                                "id": "NH",
                                "value": "51403"
                            },
                            {
                                "id": "ME",
                                "value": "64636"
                            },
                            {
                                "id": "MA",
                                "value": "51767"
                            },
                            {
                                "id": "CT",
                                "value": "57353"
                            },
                            {
                                "id": "NJ",
                                "value": "80788"
                            },
                            {
                                "id": "WV",
                                "value": "95890"
                            },
                            {
                                "id": "VA",
                                "value": "83140"
                            },
                            {
                                "id": "NC",
                                "value": "97344"
                            },
                            {
                                "id": "SC",
                                "value": "88234"
                            },
                            {
                                "id": "FL",
                                "value": "88234"
                            }
                        ]
                    }
                });
                Map.render();

                var fusioncharts = new FusionCharts({
                    type: 'column2d',
                    renderAt: 'fusioncharts',
                    width: '630',
                    height: '300',
                    dataFormat: 'json',
                    dataSource: {
                        "chart": {
                            "caption": "Monthly revenue for last year",
                            "subCaption": "Harry's SuperMart",
                            "xAxisName": "Month",
                            "yAxisName": "Revenues (In USD)",
                            "numberPrefix": "$",
                            "theme": "fint"
                        },

                        "data": [{
                            "label": "Jan",
                            "value": "420000"
                        }, {
                            "label": "Feb",
                            "value": "810000"
                        }, {
                            "label": "Mar",
                            "value": "720000"
                        }, {
                            "label": "Apr",
                            "value": "550000"
                        }, {
                            "label": "May",
                            "value": "910000"
                        }, {
                            "label": "Jun",
                            "value": "510000"
                        }, {
                            "label": "Jul",
                            "value": "680000"
                        }, {
                            "label": "Aug",
                            "value": "620000"
                        }, {
                            "label": "Sep",
                            "value": "610000"
                        }, {
                            "label": "Oct",
                            "value": "490000"
                        }, {
                            "label": "Nov",
                            "value": "900000"
                        }, {
                            "label": "Dec",
                            "value": "730000"
                        }]
                    }
                });
                fusioncharts.render();

                var pieChart = new FusionCharts({
                    type: 'pie2d',
                    renderAt: 'pieChart',
                    width: '630',
                    height: '300',
                    dataFormat: 'json',
                    dataSource: {
                        "chart": {
                            "caption": "Split of Visitors by Age Group",
                            "subCaption": "Last year",
                            "paletteColors": "#0075c2,#1aaf5d,#f2c500,#f45b00,#8e0000",
                            "bgColor": "#ffffff",
                            "showBorder": "0",
                            "use3DLighting": "0",
                            "showShadow": "0",
                            "enableSmartLabels": "0",
                            "startingAngle": "0",
                            "showPercentValues": "1",
                            "showPercentInTooltip": "0",
                            "decimals": "1",
                            "captionFontSize": "14",
                            "subcaptionFontSize": "14",
                            "subcaptionFontBold": "0",
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
                            "legendItemFontSize": '10',
                            "legendItemFontColor": '#666666'
                        },
                        "data": [
                            {
                                "label": "Teenage",
                                "value": "1250400"
                            },
                            {
                                "label": "Adult",
                                "value": "1463300"
                            },
                            {
                                "label": "Mid-age",
                                "value": "1050700"
                            },
                            {
                                "label": "Senior",
                                "value": "491000"
                            }
                        ]
                    }
                });
                pieChart.render();

                var revenueChart = new FusionCharts({
                    type: 'mscombidy2d',
                    renderAt: 'revenueChart',
                    width: '630',
                    height: '300',
                    dataFormat: 'json',
                    dataSource: {
                        "chart": {
                            "caption": "Revenues and Profits",
                            "subCaption": "For last year",
                            "xAxisname": "Month",
                            "pYAxisName": "Amount (In USD)",
                            "sYAxisName": "Profit %",
                            "numberPrefix": "$",
                            "sNumberSuffix": "%",
                            "sYAxisMaxValue": "50",

                            //Cosmetics
                            "paletteColors": "#0075c2,#1aaf5d,#f2c500",
                            "baseFontColor": "#333333",
                            "baseFont": "Helvetica Neue,Arial",
                            "captionFontSize": "14",
                            "subcaptionFontSize": "14",
                            "subcaptionFontBold": "0",
                            "showBorder": "0",
                            "bgColor": "#ffffff",
                            "showShadow": "0",
                            "canvasBgColor": "#ffffff",
                            "canvasBorderAlpha": "0",
                            "divlineAlpha": "100",
                            "divlineColor": "#999999",
                            "divlineThickness": "1",
                            "divLineIsDashed": "1",
                            "divLineDashLen": "1",
                            "divLineGapLen": "1",
                            "usePlotGradientColor": "0",
                            "showplotborder": "0",
                            "showXAxisLine": "1",
                            "xAxisLineThickness": "1",
                            "xAxisLineColor": "#999999",
                            "showAlternateHGridColor": "0",
                            "showAlternateVGridColor": "0",
                            "legendBgAlpha": "0",
                            "legendBorderAlpha": "0",
                            "legendShadow": "0",
                            "legendItemFontSize": "10",
                            "legendItemFontColor": "#666666"
                        },
                        "categories": [{
                            "category": [
                                {"label": "Jan"},
                                {"label": "Feb"},
                                {"label": "Mar"},
                                {"label": "Apr"},
                                {"label": "May"},
                                {"label": "Jun"},
                                {"label": "Jul"},
                                {"label": "Aug"},
                                {"label": "Sep"},
                                {"label": "Oct"},
                                {"label": "Nov"},
                                {"label": "Dec"}
                            ]
                        }
                        ],
                        "dataset": [
                            {
                                "seriesName": "Revenues",
                                "data": [
                                    {"value": "16000"},
                                    {"value": "20000"},
                                    {"value": "18000"},
                                    {"value": "19000"},
                                    {"value": "15000"},
                                    {"value": "21000"},
                                    {"value": "16000"},
                                    {"value": "20000"},
                                    {"value": "17000"},
                                    {"value": "22000"},
                                    {"value": "19000"},
                                    {"value": "23000"}
                                ]
                            },
                            {
                                "seriesName": "Profits",
                                "renderAs": "area",
                                "showValues": "0",
                                "data": [
                                    {"value": "4000"},
                                    {"value": "5000"},
                                    {"value": "3000"},
                                    {"value": "4000"},
                                    {"value": "1000"},
                                    {"value": "7000"},
                                    {"value": "1000"},
                                    {"value": "4000"},
                                    {"value": "1000"},
                                    {"value": "8000"},
                                    {"value": "2000"},
                                    {"value": "7000"}
                                ]
                            },
                            {
                                "seriesName": "Profit %",
                                "parentYAxis": "S",
                                "renderAs": "line",
                                "showValues": "0",
                                "data": [
                                    {"value": "25"},
                                    {"value": "25"},
                                    {"value": "16.66"},
                                    {"value": "21.05"},
                                    {"value": "6.66"},
                                    {"value": "33.33"},
                                    {"value": "6.25"},
                                    {"value": "25"},
                                    {"value": "5.88"},
                                    {"value": "36.36"},
                                    {"value": "10.52"},
                                    {"value": "30.43"}
                                ]
                            }
                        ]
                    }
                });
                revenueChart.render();


                /* initialize the external events
                 -----------------------------------------------------------------*/
                function ini_events(ele) {
                    ele.each(function () {

                        // create an Event Object (http://arshaw.com/fullcalendar/docs/event_data/Event_Object/)
                        // it doesn't need to have a start or end
                        var eventObject = {
                            title: $.trim($(this).text()) // use the element's text as the event title
                        };

                        // store the Event Object in the DOM element so we can get to it later
                        $(this).data('eventObject', eventObject);
                        // make the event draggable using jQuery UI
                        $(this).draggable({
                            zIndex: 1070,
                            revert: true, // will cause the event to go back to its
                            revertDuration: 0  //  original position after the drag
                        });

                    });
                }
                //enabletime
                //动态增加字段
                var sttimeHtml='<div class="enableTime">'
                    +'<div class="Enable">'
                    +'<div class="textColtr">启用时间</div>'
                    +'<div class="l-text"><input type="text" id="S" class="enable-begin"/></div>'
                    +'<span class="fixpos">至</span>'
                    +'<div class="l-text"><input type="text" id="E" class="enable-end"/></div>'
                    +'</div>'
                    +'<div class="addEnable">'
                    +'<div class="textColtr">增加启用时间段</div>'
                    +'<div class="addIcon"></div>'
                    +'</div>'
                    +'</div>';
                $('.timechoose').append(sttimeHtml);
                //点击删除添加按钮方法
                $('.addIcon').click(function () {
                    var html = '<div class="Enable">' +
                        '<div class="textColtr">启用时间</div>' +
                        '<div class="l-text"><input type="text"class="enable-begin"/></div> ' +
                        '<span>至</span> ' +
                        '<div class="l-text"><input type="text" class="enable-end"/></div> ' +
                        '<div class="delIcon"></div> ' +
                        '</div>';
                    $('.addEnable').before(html);
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
                });
                $("body").delegate(".delIcon","click",function(){
                    $(this).closest('.Enable').remove();
                });
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
                //本周本月本季本年
                $('.timechooseBtn li').click(function () {
                    $(this).addClass('timechooseActive');
                    $(this).siblings().removeClass('timechooseActive');
                    choose($(this).index());
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
                //周期下拉框加载
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
                $('#qyq').append(periodHtml);
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
                //周期下拉框加载
                /* 初始化事件
                 -----------------------------------------------------------------*/
                function ini_events(ele) {
                    ele.each(function () {

                        // 创建一个事件
                        var eventObject = {
                            title: $.trim($(this).text()) // 事件title
                        };

                        //存在dom元素供后面使用
                        $(this).data('eventObject', eventObject);

                        //引用jQuery UI的拖拽事件
                        $(this).draggable({
                            zIndex: 1070,
                            revert: true, // will cause the event to go back to its
                            revertDuration: 0  //  original position after the drag
                        });

                    });
                }

                ini_events($('#external-events div.external-event'));
                /* 初始化日历
                 -----------------------------------------------------------------*/
                //日历配置数据
                var date = new Date();
                var d = date.getDate(),
                    m = date.getMonth(),
                    y = date.getFullYear();

                $('#calendar').fullCalendar({
                    header: {
                        left: 'prev,next today',
                        center: 'title',
                        right: 'month,agendaWeek,agendaDay'
                    },
                    buttonText: {
                        today: 'today',
                        month: 'month',
                        week: 'week',
                        day: 'day'
                    },
                    //假数据
                    events: [
                        {
                            id: 41,
                            title: 'All Day Event',
                            start: new Date(y, m, 1),
                            end: new Date(y, m, d - 2),
                            backgroundColor: "#f56954", //red
                            borderColor: "#f56954" //red
                        },
                        {
                            id: 42,
                            title: 'Long Event',
                            start: new Date(y, m, d - 5),
                            end: new Date(y, m, d - 2),
                            backgroundColor: "#f39c12", //yellow
                            borderColor: "#f39c12" //yellow
                        },
                        {
                            id: 54,
                            title: 'Lunch',
                            start: new Date(y, m, d, 12, 0),
                            end: new Date(y, m, d, 14, 0),
                            allDay: false,
                            backgroundColor: "#00c0ef", //Info (aqua)
                            borderColor: "#00c0ef" //Info (aqua)
                        },
                        {
                            id:55,
                            title: 'Birthday Party',
                            start: new Date(y, m, d + 1, 19, 0),
                            end: new Date(y, m, d + 1, 22, 30),
                            allDay: false,
                            backgroundColor: "#00a65a", //Success (green)
                            borderColor: "#00a65a" //Success (green)
                        },
                        {
                            id:57,
                            title: 'Click for Google',
                            start: new Date(y, m, 28,10, 0),
                            end: new Date(y, m, 29, 10, 30),
                            backgroundColor: "#3c8dbc", //Primary (light-blue)
                            borderColor: "#3c8dbc" //Primary (light-blue)
                        }
                        ,{
                            id:571,
                            title: '123123',
                            start: 'Wed Sep 13 2017 10:00:00 GMT+0800 (中国标准时间)',
                            end: 'Fri Sep 15 2017 19:30:00 GMT+0800 (中国标准时间)',
                            backgroundColor: "#3c8dbc", //Primary (light-blue)
                            borderColor: "#3c8dbc" //Primary (light-blue)
                        }
                    ],
                    eventLimit: true, // 多个事件用more显示
                    editable: false,
                    //获取当前页面的所有数据对象
                    //var list =  $('#calendar').fullCalendar('getDatas')
                    eventClick: function (event, element) {
                        pevent = event;
                        layer.open({
                            type: 2,
                            title: '编辑',
                            skin: 'layui-layer-rim', //加上边框
                            area: ['500px', '400px'], //宽高
                            closeBtn: 1, //显示关闭按钮
                            content: "/resource/modules/module1/calendarEdit.html"
                        });
                    },
                    editable: true,//可编辑
                    droppable: true, // 可拖拽
                    //拖拽事件
                    drop: function (date, allDay) {

                        // 获取拖拽的事件
                        var originalEventObject = $(this).data('eventObject');

                        // 复制该事件，防止多重事件有相同的引用
                        var copiedEventObject = $.extend({}, originalEventObject);

                        // 分配相关的数值
                        copiedEventObject.id = n;
                        copiedEventObject.start = date;
                        copiedEventObject.allDay = allDay;
                        copiedEventObject.backgroundColor = $(this).css("background-color");
                        copiedEventObject.borderColor = $(this).css("border-color");

                        // 在日历上加载时间
                        $('#calendar').fullCalendar('renderEvent', copiedEventObject, true);

                        //remove after drop是否选中
                        if ($('#drop-remove').is(':checked')) {
                            // 如果是， 在列表清除该事件
                            $(this).remove();
                        }

                    }
                });
            });
        }
    }
    return module;
});


