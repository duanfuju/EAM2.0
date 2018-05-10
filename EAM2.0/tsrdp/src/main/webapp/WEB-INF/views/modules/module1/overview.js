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
define(["text!modules/module1/overview.html", "text!modules/module1/overview.css"], function (htmlTemp, cssTemp) {
    var module = {
        init: function () {
            $(".content-wrapper").html(htmlTemp);
            $(".css-attr").html(cssTemp);
            FusionCharts.ready(function () {
                //工单总览
                var salesChart = new FusionCharts({
                    type: 'scrollstackedcolumn2d',
                    dataFormat: 'json',
                    renderAt: 'orderOv',
                    width: '100%',
                    height: '300',
                    dataSource: {
                        "chart": {
                            // "caption": "Sales Comparison",
                            // "subCaption": "(FY 2012 to FY 2013)",
                            // "captionFontSize": "14",
                            // "subcaptionFontSize": "14",
                            // "subcaptionFontBold": "0",
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
                                "category": [
                                    { "label": "强电系统" },
                                    { "label": "电梯系统" },
                                    { "label": "暖通系统" },
                                    { "label": "给排水系统" },
                                    { "label": "消防系统" },
                                    { "label": "弱电系统" }
                                ]
                            }
                        ],
                        "dataset": [
                            {
                                "seriesname": "已完成",
                                "color": "008ee4",
                                "data": [
                                    { "value": "2" },
                                    { "value": "3" },
                                    { "value": "6" },
                                    { "value": "4" },
                                    { "value": "9" },
                                    { "value": "1" }
                                ]
                            },
                            {
                                "seriesname": "工单总数",
                                "color": "f8bd19",
                                "data": [
                                    { "value": "10" },
                                    { "value": "11" },
                                    { "value": "12" },
                                    { "value": "15" },
                                    { "value": "11" },
                                    { "value": "9" }
                                ]
                            }
                        ]
                    }
                });
                setTimeout(function () {
                    salesChart.render();
                    $('#'+salesChart.args.renderAt).closest('.box-body').find('.loading').remove();
                },1000);
                //工单占比
                var pieChart = new FusionCharts({
                    type: 'pie2d',
                    renderAt: 'pieChart',
                    width: '100%',
                    height: '375',
                    dataFormat: 'json',
                    dataSource: {
                        "chart": {
                            // "caption": "工单总览",
                            // "subCaption": "2017",
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
                        "data": [
                            {
                                "label": "天溯",
                                "value": "1250400"
                            },
                            {
                                "label": "工程运维部",
                                "value": "1463300"
                            },
                            {
                                "label": "基线版本开发部",
                                "value": "1050700"
                            }
                        ]
                    }
                });
                setTimeout(function () {
                    pieChart.render();
                    $('#'+pieChart.args.renderAt).closest('.box-body').find('.loading').remove();
                },1000);
                //工单周期对比
                setTimeout(function () {
                    $('.loading').remove();
                },2000);
                //工单来源占比
                var pieChart2 = new FusionCharts({
                    type: 'pie2d',
                    renderAt: 'pieChart2',
                    width: '100%',
                    height: '375',
                    dataFormat: 'json',
                    dataSource: {
                        "chart": {
                            // "caption": "工单总览",
                            // "subCaption": "2017",
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
                        "data": [
                            {
                                "label": "天溯",
                                "value": "1250400"
                            },
                            {
                                "label": "工程运维部",
                                "value": "1463300"
                            },
                            {
                                "label": "基线版本开发部",
                                "value": "1050700"
                            }
                        ]
                    }
                });
                pieChart2.render();
                var salesChart1 = new FusionCharts({
                    type: 'scrollstackedcolumn2d',
                    dataFormat: 'json',
                    renderAt: 'orderOv1',
                    width: '100%',
                    height: '300',
                    dataSource: {
                        "chart": {
                            // "caption": "Sales Comparison",
                            // "subCaption": "(FY 2012 to FY 2013)",
                            // "captionFontSize": "14",
                            // "subcaptionFontSize": "14",
                            // "subcaptionFontBold": "0",
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
                                "category": [
                                    { "label": "强电系统" },
                                    { "label": "电梯系统" },
                                    { "label": "暖通系统" },
                                    { "label": "给排水系统" },
                                    { "label": "消防系统" },
                                    { "label": "弱电系统" }
                                ]
                            }
                        ],
                        "dataset": [
                            {
                                "seriesname": "已完成",
                                "color": "008ee4",
                                "data": [
                                    { "value": "2" },
                                    { "value": "3" },
                                    { "value": "6" },
                                    { "value": "4" },
                                    { "value": "9" },
                                    { "value": "1" }
                                ]
                            },
                            {
                                "seriesname": "工单总数",
                                "color": "f8bd19",
                                "data": [
                                    { "value": "10" },
                                    { "value": "11" },
                                    { "value": "12" },
                                    { "value": "15" },
                                    { "value": "11" },
                                    { "value": "9" }
                                ]
                            }
                        ]
                    }
                }).render();
                var salesChart2 = new FusionCharts({
                    type: 'scrollstackedcolumn2d',
                    dataFormat: 'json',
                    renderAt: 'orderOv2',
                    width: '100%',
                    height: '300',
                    dataSource: {
                        "chart": {
                            // "caption": "Sales Comparison",
                            // "subCaption": "(FY 2012 to FY 2013)",
                            // "captionFontSize": "14",
                            // "subcaptionFontSize": "14",
                            // "subcaptionFontBold": "0",
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
                                "category": [
                                    { "label": "强电系统" },
                                    { "label": "电梯系统" },
                                    { "label": "暖通系统" },
                                    { "label": "给排水系统" },
                                    { "label": "消防系统" },
                                    { "label": "弱电系统" }
                                ]
                            }
                        ],
                        "dataset": [
                            {
                                "seriesname": "已完成",
                                "color": "008ee4",
                                "data": [
                                    { "value": "2" },
                                    { "value": "3" },
                                    { "value": "6" },
                                    { "value": "4" },
                                    { "value": "9" },
                                    { "value": "1" }
                                ]
                            },
                            {
                                "seriesname": "工单总数",
                                "color": "f8bd19",
                                "data": [
                                    { "value": "10" },
                                    { "value": "11" },
                                    { "value": "12" },
                                    { "value": "15" },
                                    { "value": "11" },
                                    { "value": "9" }
                                ]
                            }
                        ]
                    }
                }).render();
                var revenueChart = new FusionCharts({
                    type: 'mscombidy2d',
                    renderAt: 'chart-container',
                    width: '100%',
                    height: '350',
                    dataFormat: 'json',
                    dataSource: {
                        "chart": {
                            "caption": "Revenues and Profits",
                            "subCaption": "For last year",
                            "xAxisname": "Month",
                            "pYAxisName": "Amount (In USD)",
                            "sYAxisName": "Profit %",
                            "numberPrefix": "$",
                            "sNumberSuffix" : "%",
                            "sYAxisMaxValue" : "50",

                            //Cosmetics
                            "paletteColors" : "#0075c2,#1aaf5d,#f2c500",
                            "baseFontColor" : "#333333",
                            "baseFont" : "Helvetica Neue,Arial",
                            "captionFontSize" : "14",
                            "subcaptionFontSize" : "14",
                            "subcaptionFontBold" : "0",
                            "showBorder" : "0",
                            "bgColor" : "#ffffff",
                            "showShadow" : "0",
                            "canvasBgColor" : "#ffffff",
                            "canvasBorderAlpha" : "0",
                            "divlineAlpha" : "100",
                            "divlineColor" : "#999999",
                            "divlineThickness" : "1",
                            "divLineIsDashed" : "1",
                            "divLineDashLen" : "1",
                            "divLineGapLen" : "1",
                            "usePlotGradientColor" : "0",
                            "showplotborder" : "0",
                            "showXAxisLine" : "1",
                            "xAxisLineThickness" : "1",
                            "xAxisLineColor" : "#999999",
                            "showAlternateHGridColor" : "0",
                            "showAlternateVGridColor" : "0",
                            "legendBgAlpha" : "0",
                            "legendBorderAlpha" : "0",
                            "legendShadow" : "0",
                            "legendItemFontSize" : "10",
                            "legendItemFontColor" : "#666666"
                        },
                        "categories": [{
                            "category": [
                                { "label": "Jan" },
                                { "label": "Feb" },
                                { "label": "Mar" },
                                { "label": "Apr" },
                                { "label": "May" },
                                { "label": "Jun" },
                                { "label": "Jul" },
                                { "label": "Aug" },
                                { "label": "Sep" },
                                { "label": "Oct" },
                                { "label": "Nov" },
                                { "label": "Dec" }
                            ]
                        }
                        ],
                        "dataset": [
                            {
                                "seriesName": "Revenues",
                                "data": [
                                    { "value" : "16000" },
                                    { "value" : "20000" },
                                    { "value" : "18000" },
                                    { "value" : "19000" },
                                    { "value" : "15000" },
                                    { "value" : "21000" },
                                    { "value" : "16000" },
                                    { "value" : "20000" },
                                    { "value" : "17000" },
                                    { "value" : "22000" },
                                    { "value" : "19000" },
                                    { "value" : "23000" }
                                ]
                            },
                            {
                                "seriesName": "Profits",
                                "renderAs": "area",
                                "showValues": "0",
                                "data": [
                                    { "value" : "4000" },
                                    { "value" : "5000" },
                                    { "value" : "3000" },
                                    { "value" : "4000" },
                                    { "value" : "1000" },
                                    { "value" : "7000" },
                                    { "value" : "1000" },
                                    { "value" : "4000" },
                                    { "value" : "1000" },
                                    { "value" : "8000" },
                                    { "value" : "2000" },
                                    { "value" : "7000" }
                                ]
                            },
                            {
                                "seriesName": "Profit %",
                                "parentYAxis": "S",
                                "renderAs": "line",
                                "showValues": "0",
                                "data": [
                                    { "value" : "25" },
                                    { "value" : "25" },
                                    { "value" : "16.66" },
                                    { "value" : "21.05" },
                                    { "value" : "6.66" },
                                    { "value" : "33.33" },
                                    { "value" : "6.25" },
                                    { "value" : "25" },
                                    { "value" : "5.88" },
                                    { "value" : "36.36" },
                                    { "value" : "10.52" },
                                    { "value" : "30.43"}
                                ]
                            }
                        ]
                    }
                });
                var revenueChart2 = new FusionCharts({
                    type: 'mscombidy2d',
                    renderAt: 'chart-container2',
                    width: '100%',
                    height: '350',
                    dataFormat: 'json',
                    dataSource: {
                        "chart": {
                            "caption": "Revenues and Profits",
                            "subCaption": "For last year",
                            "xAxisname": "Month",
                            "pYAxisName": "Amount (In USD)",
                            "sYAxisName": "Profit %",
                            "numberPrefix": "$",
                            "sNumberSuffix" : "%",
                            "sYAxisMaxValue" : "50",

                            //Cosmetics
                            "paletteColors" : "#0075c2,#1aaf5d,#f2c500",
                            "baseFontColor" : "#333333",
                            "baseFont" : "Helvetica Neue,Arial",
                            "captionFontSize" : "14",
                            "subcaptionFontSize" : "14",
                            "subcaptionFontBold" : "0",
                            "showBorder" : "0",
                            "bgColor" : "#ffffff",
                            "showShadow" : "0",
                            "canvasBgColor" : "#ffffff",
                            "canvasBorderAlpha" : "0",
                            "divlineAlpha" : "100",
                            "divlineColor" : "#999999",
                            "divlineThickness" : "1",
                            "divLineIsDashed" : "1",
                            "divLineDashLen" : "1",
                            "divLineGapLen" : "1",
                            "usePlotGradientColor" : "0",
                            "showplotborder" : "0",
                            "showXAxisLine" : "1",
                            "xAxisLineThickness" : "1",
                            "xAxisLineColor" : "#999999",
                            "showAlternateHGridColor" : "0",
                            "showAlternateVGridColor" : "0",
                            "legendBgAlpha" : "0",
                            "legendBorderAlpha" : "0",
                            "legendShadow" : "0",
                            "legendItemFontSize" : "10",
                            "legendItemFontColor" : "#666666"
                        },
                        "categories": [{
                            "category": [
                                { "label": "Jan" },
                                { "label": "Feb" },
                                { "label": "Mar" },
                                { "label": "Apr" },
                                { "label": "May" },
                                { "label": "Jun" },
                                { "label": "Jul" },
                                { "label": "Aug" },
                                { "label": "Sep" },
                                { "label": "Oct" },
                                { "label": "Nov" },
                                { "label": "Dec" }
                            ]
                        }
                        ],
                        "dataset": [
                            {
                                "seriesName": "Revenues",
                                "data": [
                                    { "value" : "16000" },
                                    { "value" : "20000" },
                                    { "value" : "18000" },
                                    { "value" : "19000" },
                                    { "value" : "15000" },
                                    { "value" : "21000" },
                                    { "value" : "16000" },
                                    { "value" : "20000" },
                                    { "value" : "17000" },
                                    { "value" : "22000" },
                                    { "value" : "19000" },
                                    { "value" : "23000" }
                                ]
                            },
                            {
                                "seriesName": "Profits",
                                "renderAs": "area",
                                "showValues": "0",
                                "data": [
                                    { "value" : "4000" },
                                    { "value" : "5000" },
                                    { "value" : "3000" },
                                    { "value" : "4000" },
                                    { "value" : "1000" },
                                    { "value" : "7000" },
                                    { "value" : "1000" },
                                    { "value" : "4000" },
                                    { "value" : "1000" },
                                    { "value" : "8000" },
                                    { "value" : "2000" },
                                    { "value" : "7000" }
                                ]
                            },
                            {
                                "seriesName": "Profit %",
                                "parentYAxis": "S",
                                "renderAs": "line",
                                "showValues": "0",
                                "data": [
                                    { "value" : "25" },
                                    { "value" : "25" },
                                    { "value" : "16.66" },
                                    { "value" : "21.05" },
                                    { "value" : "6.66" },
                                    { "value" : "33.33" },
                                    { "value" : "6.25" },
                                    { "value" : "25" },
                                    { "value" : "5.88" },
                                    { "value" : "36.36" },
                                    { "value" : "10.52" },
                                    { "value" : "30.43"}
                                ]
                            }
                        ]
                    }
                });
                revenueChart.render();
                revenueChart2.render();
                //设备总览1
                var pieCharta = new FusionCharts({
                    type: 'pie2d',
                    renderAt: 'deviceOv1',
                    width: '100%',
                    height: '340',
                    dataFormat: 'json',
                    dataSource: {
                        "chart": {
                            // "caption": "工单总览",
                            // "subCaption": "2017",
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
                        "data": [
                            {
                                "label": "天溯",
                                "value": "1250400"
                            },
                            {
                                "label": "工程运维部",
                                "value": "1463300"
                            },
                            {
                                "label": "基线版本开发部",
                                "value": "1050700"
                            }
                        ]
                    }
                });
                pieCharta.render();

                $('#stdTeam').select2();


                var zTree;
                var demoIframe;
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
                        beforeClick: function(treeId, treeNode) {
                            var zTree = $.fn.zTree.getZTreeObj(treeId);
                            if (treeNode.isParent) {
                                zTree.expandNode(treeNode);
                                return false;
                            } else {
                                demoIframe.attr("src","/modules/module2/demo/cn/"+treeNode.file + ".html");
                                return true;
                            }
                        }
                    }
                };


                //本周本月本季本年
                $('.timechooseBtn li').click(function () {
                    $(this).addClass('timechooseActive');
                    $(this).siblings().removeClass('timechooseActive');
                    choose($(this).index());
                })
                $('.timechoose .begin').datepicker({
                    changeMonth: true,
                    dateFormat: 'yy-mm-dd',
                    changeYear:true,
                    onClose: function(selectedDate) {
                        $(".timechoose .end").datepicker("option", "minDate", selectedDate);

                    }
                });
                $('.timechoose .end').datepicker({
                    changeMonth: true,
                    changeYear:true,
                    dateFormat: 'yy-mm-dd',
                    onClose: function(selectedDate) {
                        $(".timechoose .begin").datepicker("option", "maxDate", selectedDate);
                        $(".timechoose .end").val($(this).val());
                    }
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
                // zTree 的数据属性，深入使用请参考 API 文档（zTreeNode 节点数据详解）
                var zNodes =[
                    {id:1, pId:0, name:"[core] 基本功能 演示", open:true},
                    {id:101, pId:1, name:"最简单的树 --  标准 JSON 数据", file:"core/standardData"},
                    {id:102, pId:1, name:"最简单的树 --  简单 JSON 数据", file:"core/simpleData"},
                    {id:103, pId:1, name:"不显示 连接线", file:"core/noline"},
                    {id:104, pId:1, name:"不显示 节点 图标", file:"core/noicon"},
                    {id:105, pId:1, name:"自定义图标 --  icon 属性", file:"core/custom_icon"},
                    {id:106, pId:1, name:"自定义图标 --  iconSkin 属性", file:"core/custom_iconSkin"},
                    {id:107, pId:1, name:"自定义字体", file:"core/custom_font"},
                    {id:115, pId:1, name:"超链接演示", file:"core/url"},
                    {id:108, pId:1, name:"异步加载 节点数据", file:"core/async"},
                    {id:109, pId:1, name:"用 zTree 方法 异步加载 节点数据", file:"core/async_fun"},
                    {id:110, pId:1, name:"用 zTree 方法 更新 节点数据", file:"core/update_fun"},
                    {id:111, pId:1, name:"单击 节点 控制", file:"core/click"},
                    {id:112, pId:1, name:"展开 / 折叠 父节点 控制", file:"core/expand"},
                    {id:113, pId:1, name:"根据 参数 查找 节点", file:"core/searchNodes"},
                    {id:114, pId:1, name:"其他 鼠标 事件监听", file:"core/otherMouse"},

                    {id:2, pId:0, name:"[excheck] 复/单选框功能 演示", open:false},
                    {id:201, pId:2, name:"Checkbox 勾选操作", file:"excheck/checkbox"},
                    {id:206, pId:2, name:"Checkbox nocheck 演示", file:"excheck/checkbox_nocheck"},
                    {id:207, pId:2, name:"Checkbox chkDisabled 演示", file:"excheck/checkbox_chkDisabled"},
                    {id:208, pId:2, name:"Checkbox halfCheck 演示", file:"excheck/checkbox_halfCheck"},
                    {id:202, pId:2, name:"Checkbox 勾选统计", file:"excheck/checkbox_count"},
                    {id:203, pId:2, name:"用 zTree 方法 勾选 Checkbox", file:"excheck/checkbox_fun"},
                    {id:204, pId:2, name:"Radio 勾选操作", file:"excheck/radio"},
                    {id:209, pId:2, name:"Radio nocheck 演示", file:"excheck/radio_nocheck"},
                    {id:210, pId:2, name:"Radio chkDisabled 演示", file:"excheck/radio_chkDisabled"},
                    {id:211, pId:2, name:"Radio halfCheck 演示", file:"excheck/radio_halfCheck"},
                    {id:205, pId:2, name:"用 zTree 方法 勾选 Radio", file:"excheck/radio_fun"},

                    {id:3, pId:0, name:"[exedit] 编辑功能 演示", open:false},
                    {id:301, pId:3, name:"拖拽 节点 基本控制", file:"exedit/drag"},
                    {id:302, pId:3, name:"拖拽 节点 高级控制", file:"exedit/drag_super"},
                    {id:303, pId:3, name:"用 zTree 方法 移动 / 复制 节点", file:"exedit/drag_fun"},
                    {id:304, pId:3, name:"基本 增 / 删 / 改 节点", file:"exedit/edit"},
                    {id:305, pId:3, name:"高级 增 / 删 / 改 节点", file:"exedit/edit_super"},
                    {id:306, pId:3, name:"用 zTree 方法 增 / 删 / 改 节点", file:"exedit/edit_fun"},
                    {id:307, pId:3, name:"异步加载 & 编辑功能 共存", file:"exedit/async_edit"},
                    {id:308, pId:3, name:"多棵树之间 的 数据交互", file:"exedit/multiTree"},

                    {id:4, pId:0, name:"大数据量 演示", open:false},
                    {id:401, pId:4, name:"一次性加载大数据量", file:"bigdata/common"},
                    {id:402, pId:4, name:"分批异步加载大数据量", file:"bigdata/diy_async"},
                    {id:403, pId:4, name:"分批异步加载大数据量", file:"bigdata/page"},

                    {id:5, pId:0, name:"组合功能 演示", open:false},
                    {id:501, pId:5, name:"冻结根节点", file:"super/oneroot"},
                    {id:502, pId:5, name:"单击展开/折叠节点", file:"super/oneclick"},
                    {id:503, pId:5, name:"保持展开单一路径", file:"super/singlepath"},
                    {id:504, pId:5, name:"添加 自定义控件", file:"super/diydom"},
                    {id:505, pId:5, name:"checkbox / radio 共存", file:"super/checkbox_radio"},
                    {id:506, pId:5, name:"左侧菜单", file:"super/left_menu"},
                    {id:513, pId:5, name:"OutLook 风格", file:"super/left_menuForOutLook"},
                    {id:515, pId:5, name:"Awesome 风格", file:"super/awesome"},
                    {id:514, pId:5, name:"Metro 风格", file:"super/metro"},
                    {id:507, pId:5, name:"下拉菜单", file:"super/select_menu"},
                    {id:509, pId:5, name:"带 checkbox 的多选下拉菜单", file:"super/select_menu_checkbox"},
                    {id:510, pId:5, name:"带 radio 的单选下拉菜单", file:"super/select_menu_radio"},
                    {id:508, pId:5, name:"右键菜单 的 实现", file:"super/rightClickMenu"},
                    {id:511, pId:5, name:"与其他 DOM 拖拽互动", file:"super/dragWithOther"},
                    {id:512, pId:5, name:"异步加载模式下全部展开", file:"super/asyncForAll"},

                    {id:6, pId:0, name:"其他扩展功能 演示", open:false},
                    {id:601, pId:6, name:"隐藏普通节点", file:"exhide/common"},
                    {id:602, pId:6, name:"配合 checkbox 的隐藏", file:"exhide/checkbox"},
                    {id:603, pId:6, name:"配合 radio 的隐藏", file:"exhide/radio"}
                ];
                $(document).ready(function(){
                    $.fn.zTree.init($("#materTreeA"), setting, zNodes);
                });

            });
            //点击添加新模块按钮
            $('.subAddModule').click(function () {
                if($(".moduleSlc ul li").length>0){
                    $(this).hide();
                    $(this).next().show();
                }else{
                    layer.msg("无可添加的新模块！",{time: 1000,icon:7});
                }
            });
            //点击toggleClass
            $(".moduleSlc ul").delegate("li", "click", function () {
                $(this).toggleClass('li-active');
            });
            //添加新模块点击确定
            $('.addModule .submit').click(function () {
                $('.li-active').each(function(){
                    switch ($(this).text()){
                        case '绩效总览':{$('[data-id="performance"]').show();$('[data-id="performance"]').closest('.col-md-6').show();$(this).remove();break;} //绩效总览
                        case '报修总览':{$('[data-id="repair"]').show();$('[data-id="repair"]').closest('.col-md-6').show();$(this).remove();break;} //报修总览
                        case '工单总览':{$('[data-id="workorder"]').show();$('[data-id="workorder"]').closest('.col-md-6').show();$(this).remove();break;} //工单总览
                        case '保养总览':{$('[data-id="maintain"]').show();$('[data-id="maintain"]').closest('.col-md-6').show();$(this).remove();break;} //保养总览
                        case '巡检总览':{$('[data-id="inspection"]').show();$('[data-id="inspection"]').closest('.col-md-6').show();$(this).remove();break;} //巡检总览
                        case '运维总览':{$('[data-id="maintenance"]').show();$('[data-id="maintenance"]').closest('.col-md-6').show();$(this).remove();break;} //运维总览
                        case '设备总览':{$('[data-id="device"]').show();$('[data-id="device"]').closest('.col-md-6').show();$(this).remove();break;} //设备总览
                    }
                })
                $('.subAddModule').show();
                $('.moduleSlc').hide();
            });
            //添加新模块点击取消
            $('.addModule .cancelbtn').click(function () {
                $('.subAddModule').show();
                $('.moduleSlc').hide();
            })
        }
    }
    return module;
});