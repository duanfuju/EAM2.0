<%@ page import="org.activiti.engine.form.FormType" %>
<%@ page import="org.activiti.engine.form.FormProperty" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="/resource/plugins/select2/select2.css"/>
    <link rel="stylesheet" type="text/css" href="/resource/plugins/select2/select2.min.css">
    <link rel="stylesheet" type="text/css" href="/resource/plugins/webupload/style.css"/>
    <link href="/resource/plugins/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="/resource/plugins/webupload/webuploader.css"/>
    <link rel="stylesheet" type="text/css" href="/resource/plugins/bootstrap-switch/css/bootstrap3/bootstrap-switch.css"/>
    <link rel="stylesheet" type="text/css" href="/resource/modules/inspection/inspectionfinishSelect.css"/>
    <link rel="stylesheet" type="text/css" href="/resource/plugins/jQuery-Timepicker-Addon/jquery-ui-timepicker-addon.css"/>

    <style>
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
    <script src="/resource/plugins/jQuery/jquery-2.2.3.min.js"></script>
    <script src="/resource/plugins/jQueryUI/jquery-ui.min.js"></script>
    <script src="/resource/plugins/select2/select2.js"></script>
    <script src="/resource/plugins/ligerUI/js/ligerui.all.js"></script>
    <script src="/resource/common.js"></script>
    <script src="/resource/plugins/bootstrap-switch/js/bootstrap-switch.min.js"></script>
    <script type="text/javascript" src="/static/ckfinder/ckfinder.js"></script>
    <script type="text/javascript" src="/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/resource/plugins/jQuery-Timepicker-Addon/jquery-ui-timepicker-addon.js"></script>


    <script>
        //@ sourceURL=inspectionTaskFeedBack2.jsp
        var sbjCount = 0;
        var dat = null;  //所有巡检项数据
        var nowDate = null;
        var nowTime = null;
        var nowSubCode = null;
        var materialSelect = new Object();
        var personSelect = new Object();
        var allInperctionData = [];
        var allLength = 0;
        var inspectiontask_id = "";
        var vidList = []//存放上传的视频
        var imgList = []//存放上传的图片

        var executionId = '${task.executionId}';
        var pstid = '${task.executionId}';  // 流程id
        // 任务id
        var taskid = '${task.id}';

        function TroubleInfo() {

        }

        TroubleInfo.prototype = {
            init: function () {
                this.render();
            },
            render: function () {
                this.getTable();
                this.appendArea();
                this.patrolArea();
                this.uploadArea();
                this.orderDetailBtnCtrl();//巡检明细移过来的两个
                this.materialSelect();
            },

            //流程图
            getTable:function() {
            /**
             * 获取元素的outerHTML
             */
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

            $(document).ready(function(){
//
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
            });

        },
            //是否按钮控制其对应的div是否显示
            orderDetailBtnCtrl: function () {
                //点击是的时候，子按钮组显示，同时默认显示选中巡检任务按钮以及巡检任务的工序页面显示
                $('#MotheryesBtn').on('click', function () {
                    $('.standard-Btn').show();
                    $('.intro-standard-packet-Y').show();
                    $('.intro-standard-packet-N').hide();
                    // $('span[data-id="standard-work"]').addClass('region');
                });
                //点击否的时候，巡检任务和标准工单子按钮组隐藏
                $('#MothernoBtn').on('click', function () {
                    $('.standard-Btn').hide();
                    $('.intro-standard-packet-Y').hide();
                    $('.intro-standard-packet-N').show();
                });
                //点击巡检任务和标准工单按钮控制其div是否显示
                $('.standard-Btn-ctrl').on('click', function () {
                    var id = $(this).attr('data-id');
                    // console.log(id);
                    $("#" + id).addClass('activeStd').siblings('.activeStd').removeClass('activeStd');
                });
                //点击工单明细按钮组，控制器不同的form表单显示
                $('.order-detail-btnGp>span').on('click', function () {
                    var id = $(this).attr('data-id');
                    //获取当前编辑的form
                    nowForm = $(this).attr('data-id');
                    // console.log(id);
                    $('form[name=' + id + ']').addClass('activeForm').siblings('.activeForm').removeClass('activeForm');
                });
            },

            //巡检区域的展示
            appendArea: function () {
                var id = parent.resultId;
                /*---加载巡检区域详情start---*/
                var areas = null;

                common.callAjax('post',false,common.interfaceUrl.inspectionAreaInfoByTaskPstid,"json",{pstid: pstid},function(data){
                    areas = data;
                    if(data && data.length>0){
                        inspectiontask_id = data[0].inspectiontask_id;
                    }
                });
                for (var i = 0; i < areas.length; i++) {
                    var areaName = '<li data-areaId="' + areas[i].id + '"><span class="serialNum"></span>' + areas[i].area_code + areas[i].area_name + '<span class="toggleIcon toggleIconClick"></span></li>';
                    $("#patrol-result-content .patrol-area-list").append(areaName);
                }
                /*---加载巡检区域详情end---*/
                Date.prototype.Format = function (fmt) {
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
                }
            },

            //巡检项的展示
            patrolArea: function () {
                var that = this;
                //巡检区域的序号生成
                $("#patrol-result-content .patrol-area-list .serialNum").each(function (i) {
                    var str = ("00" + (i + 1)).slice(-2);
                    $(this).html(str);
                });
                //隐藏显示“现象录入”
                $("#patrol-result-content").on("click", "span", function () {
                    var index = $(this).index();
                    var $pheno = $(this).closest("tr").next(".phenomenon-content");
                    if (index == 0) {
                        $pheno.find("input.phenomenon").val("");
                        $pheno.addClass("hide");
                    } else {
                        $pheno.removeClass("hide");
                    }
                });

                //点击箭头按钮生成巡检区域表格数据
                $("#patrol-result-content").on("click", ".toggleIcon", function () {
                    //获取当前区域id
                    var areaId = $(this).parent().attr("data-areaId");
                    var $nextLi = $(this).closest("li").next();
                    var everInit = $nextLi.hasClass("patrol-area-table");
                    var hasToggle = $(this).hasClass("toggleIconClick");
                    if (hasToggle) {
                        $(".patrol-area-list .toggleIcon").addClass("toggleIconClick");
                        $(".patrol-area-list .patrol-area-table").addClass("hide");
                        $(this).removeClass("toggleIconClick");
                        $nextLi.removeClass("hide");

                        if (!everInit) {
                            $(this).closest("li").siblings(".patrol-area-table").addClass("hide");
                            /*
                             * 数据说明：status状态判断该条数据是否标记红色，异常或者报修需要标记红色
                             * */

                            common.callAjax('post',false,common.interfaceUrl.inspectionSubjectInfoByAreaId,"json",{area_id: areaId,inspectiontask_id:inspectiontask_id},function(data){
                                dat = data;
                                allLength +=data.length;
                                if (dat && dat.length > 0) {
                                    for (var i = 0; i < dat.length; i++) {
                                        var subjects = dat[i].subjects;
                                        var patrolCode = [];
                                        if (subjects && subjects.length > 0) {
                                            for (var j = 0; j < subjects.length; j++) {
                                                patrolCode.push(subjects[j].id);
                                                dat[i].patrolCode = patrolCode.join("_")
                                            }
                                        }
                                    }
                                }
                                //巡检项总数赋值
                                sbjCount += dat.length;
                            });

                            var jsonString = JSON.stringify(dat);
                            var json = JSON.parse(jsonString);

                            var waitingData = {
                                /*数据要求：传来的patrolCode唯一，否则会造成文件上传显示错误*/
                                "retCode": 0,
                                "data": json
                            };
                            that.orderCompleteCTable(waitingData.data, this);
                            $("#patrol-result-content .toggleIcon").addClass("toggleIconClick");
                            $(this).removeClass("toggleIconClick");
                        } else {
                            $(this).closest("li").next().removeClass("hide");
                        }
                    } else {
                        $(this).toggleClass("toggleIconClick");
                        $nextLi.toggleClass("hide");
                    }


                });

                //点击表格中操作按钮生成
            },

            //巡检设备详情展示
            uploadArea: function () {
                var that = this;

                //点击反馈按钮生成录入区域，并初始化
                $("#patrol-result-content").on("click", ".patrol-feedback-icon", function () {
                    $(this).addClass("thisEdit").closest("tr").siblings().find(".patrol-feedback-icon").removeClass("thisEdit");
                    //获取当前巡检项的数据
                    var thisSubDat = new Object();
                    if (dat && dat.length) {
                        var thisIndex = $(this).closest("tr").children("td:first").text() - 1;
                        thisSubDat = dat[thisIndex];
                    }

                    var tib = $(this).parent("td").prev().prev().prev().text();
                    $(this).closest('table').parent().next().children().hide();

                    if ($(this).closest('tr').hasClass('nowTr editingTr')) {
                        var containBox = $(this).closest('table').parent().next().find('div.contain-box');
                        $.each(containBox, function (index, contain) {
                            var ensureName = $(contain).find('table.ensureTable').find('td:nth-child(2)').text();
                            if (tib == ensureName) {
                                $(contain).removeClass("hide").show();
                            }
                        });
                        return;
                    }

                    //获取当前并生成随机数，用于命名图片和视频--caoh
                    nowDate = new Date().Format('yyyy-MM-dd hh:mm:ss').replace(/-/g, "").replace(" ", "").replace(/:/g, "");
                    var num = parseInt(1000 * Math.random());
                    nowDate += num;


                    var thatIcon = this;
                    var nowTr = $(this).closest("tr");
                    nowTr.addClass("nowTr editingTr");
                    //nowTr.addClass("nowTr editingTr").siblings("tr").removeClass("nowTr editingTr");
                    var domId = nowTr.find("td:nth-child(2)").html();

                    //获取当前正在编辑的subjectCode--caoh
                    nowSubCode = domId;

                    that.createLoadArea(domId, thatIcon, that, nowDate, thisSubDat);
                    $(".switch input").bootstrapSwitch({
                        size: "mini",
                        onSwitchChange: function (event, state) {
                            $(this).closest("td").find("span.patrol-feedback-icon").click();
                            var switchall = $(this).closest("table").attr("class");
                            var thisText = $(this).closest("td").prev().prev().prev().text();
                            var thisTables = $(this).closest("table").parent().next().find(".contain-box");
                            if (state == true) {
                                $(this).closest("table").next(".hideEle").hide();
                                if (switchall == "std-workDet-tbl") {
                                    $.each(thisTables, function (index, thisTable) {
                                        var thatText = $(thisTable).find('table.ensureTable').find('td:nth-child(2)').text();
                                        if (thatText == thisText) {
                                            $(thisTable).find('table.unsureTable').find('tr:first').find('td.switch').find('div.bootstrap-switch-off').find('input').click();
                                        }
                                    });
                                }
                            } else {
                                $(this).closest("table").next(".hideEle").show();
                                if (switchall == "std-workDet-tbl") {
                                    $.each(thisTables, function (index, thisTable) {
                                        var thatText = $(thisTable).find('table.ensureTable').find('td:nth-child(2)').text();
                                        if (thatText == thisText) {
                                            $(thisTable).children('table.unsureTable').find('tr:first').find('td.switch').find('div.bootstrap-switch-on').find('input').click();
                                        }
                                    });
                                }
                            }
                        }
                    });
                    $('.unusual').on('click', function () {
                        $(this).closest('tr').find('.repair').show();
                        $(this).closest('tr').next('tr.repair').hide();
                        $(this).addClass("region");
                        $(this).siblings().removeClass("region");
                    });
                    $('.useRight').on('click', function () {
                        $(this).closest('tr').find('.repair').hide();
                        $(this).closest('tr').next('tr.repair').hide();
                        $(this).addClass("region");
                        $(this).siblings().removeClass("region");
                    });
                    $('.rightSpan.unusual').on('click', function () {
                        $(this).closest('tr').next('tr.repair').show();
                    })

                    $('.checkValue input').on('blur',function(){
                        var id = $(this).attr("id").substring(8);
                        var ckValue = $("#referValue" + id).html();
                        //如果参考值是区间值
                        if (ckValue.indexOf("~") >= 0) {
                            var xxValue = ckValue.substring(0, ckValue.indexOf("~"));
                            var sxValue = ckValue.substring(ckValue.indexOf("~") + 1);
                            //如果所填值超过参考值区间
                            if ($(this).val() > parseFloat(sxValue) || $(this).val() < parseFloat(xxValue)) {
                                if (!confirm("所填值超过参考区间，是否确认？")) {
                                    $(this).val("");
                                    return;
                                } else {
                                    console.log();
                                }
                            }

                        }
                    })
                    $(".popInp").on("blur", function () {
                        var id = $(this).attr("id").substring(8);
                        var ckValue = $("#referValue" + id).html();
                        //如果参考值是区间值
                        if (ckValue.indexOf("~") >= 0) {
                            var xxValue = ckValue.substring(0, ckValue.indexOf("~"));
                            var sxValue = ckValue.substring(ckValue.indexOf("~") + 1);
                            //如果所填值超过参考值区间
                            if ($(this).val() > parseFloat(sxValue) || $(this).val() < parseFloat(xxValue)) {
                                if (!confirm("所填值超过参考区间，是否确认？")) {
                                    $(this).val("");
                                    return;
                                } else {
                                    console.log();
                                }
                            }

                        } else {
                            //如果所填值与参考值不等
                            if ($(this).val() != parseFloat(ckValue)) {
                                if (!confirm("所填值与参考值不一致，是否确认？")) {
                                    $(this).val("");
                                    return;
                                } else {
                                    console.log();
                                }
                            }
                        }


                        var date = new Date().Format('yyyy-MM-dd hh:mm:ss');

                        $("#checkTime" + id).html(date);
                    });

                    if (thisSubDat && thisSubDat.subjects) {
                        var objs = thisSubDat.subjects;
                        for (var i = 0; i < objs.length; i++) {
                            var subid = objs[i].id;
                            $("#checkVal" + subid).val(objs[i].subject_ck_value);
                            $("#excDescription" + subid).val(objs[i].remark);
                            $("#checkResult" + subid).find("span").eq(objs[i].check_result).trigger("click");
                            if (objs[i].check_result == '1' || objs[i].check_result == '2') {
                                $("#phenomenon" + subid).val(objs[i].appearance);
                            }


                        }
                    }
                });

                //点击编辑按钮显示具体内容
                $("#patrol-result-content").on("click", ".patrol-edit-icon", function () {
                    var tib = $(this).parent("td").prev().prev().prev().text();
                    var containBox = $(this).closest('table').parent().next().find('div.contain-box');
                    $.each(containBox, function (index, contain) {
                        $(contain).addClass("hide");
                        var ensureName = $(contain).find('table.ensureTable').find('td:nth-child(2)').text();
                        if (tib == ensureName) {
                            $(contain).removeClass("hide").show();
                        }
                    });
                    var nowTr = $(this).closest("tr");
                    var domId = nowTr.find("td:nth-child(2)").html();
                    $(this).closest(".patrol-area-table").find("#popTable" + domId).removeClass("hide");
                    $(this).next(".switch-mini").removeClass("hide");
                });

                //取消按钮，清除所有的unsureTable

                $("#patrol-result-content").on("click", ".cancelBtn", function () {
                    $(".contain-box").addClass("hide");
                });

                //确认按钮
                $("#patrol-result-content").on("click", ".submitBtn", function (event) {
                    var that = this;
                    var result = false;
                    // 获取到每一个巡检项的整个table
                    var tables = $(this).parent(".btnlist").parent("li").parent(".btnlist_list").siblings(".unsureTable");
                    //获取每个table里的数据
                    var inspectiontaskFeedbackList = [];
                    var requireDown = true;
                    $.each(tables, function (index, table) {
                        var inspectiontaskFeedback = new Object();   //每个设备的数据
                        inspectiontaskFeedback.check_value = $(table).find('td.checkValue span.select2-selection__rendered').text()!=""?$(table).find('td.checkValue span.select2-selection__rendered').text():$(table).find('td.checkValue input').val();  //检查值
                        inspectiontaskFeedback.subject_id = $(table).find('span.subnamme').text();  //巡检项编码
                        inspectiontaskFeedback.remark = $(table).find('td textarea').val();  //备注

                        inspectiontaskFeedback.check_time = $(table).find(".nowTime").html() + "";//时间
                        if (inspectiontaskFeedback.check_time == "") {
                            inspectiontaskFeedback.check_time = new Date().Format('yyyy-MM-dd hh:mm:ss');
                        }

                        inspectiontaskFeedback.isclose = 0;  //开关,默认打开
                        if ($(table).find("td.switch").find("div.bootstrap-switch-wrapper").hasClass("bootstrap-switch-off")) {
                            inspectiontaskFeedback.isclose = 1;
                        }

                        if ($(table).find(".revResult").find("span").eq(0).hasClass("region")) {
                            inspectiontaskFeedback.check_result = 0; //巡检结果正常
                        } else if ($(table).find(".revResult").find("span").eq(1).hasClass("region")) {
                            inspectiontaskFeedback.check_result = 1; //报修
                            // //期望完成时间
                             inspectiontaskFeedback.expect_time = $(".expectTime" + inspectiontaskFeedback.subject_id).val();
//                                 $(table).find('.expect-time input').val();
                            // //处理方式
                             inspectiontaskFeedback.processMode = $(".processMode" + inspectiontaskFeedback.subject_id).val();
//                                 $(table).find('tr.repair span.select2-selection__rendered').text();
                        } else {
                            inspectiontaskFeedback.check_result = 2; //异常
                        }

                        inspectiontaskFeedback.inspectiontask_id = inspectiontask_id;//巡检任务id

                        //
                        inspectiontaskFeedback.check_picture=imgList;//图片
                        inspectiontaskFeedback.check_video=vidList;//视频

//                        inspectiontaskFeedback.check_picture="";
//                        var getImgList = $(that).closest("ul").prev("tr").find(".fileimg input");//图片
//                        if(getImgList.length>0){
//                            var check_picture=[];
//                            $.each(getImgList,function(index,getImg){
//                                check_picture.push($(getImg).val());
//                                inspectiontaskFeedback.check_picture = check_picture.join(";")
//                        });}
//
//                        inspectiontaskFeedback.check_video="";
//                        var getVidList = $(that).closest("ul").prev("tr").find(".filevid input");//视频
//                        if(getVidList.length>0){
//                            var check_video=[];
//                            $.each(getVidList,function(index,getVid){
//                                check_video.push($(getVid).val());
//                                inspectiontaskFeedback.check_video = check_video.join(";")
//                            });
//                        }
                        inspectiontaskFeedback.issubmit = 0  //是否已提交，默认为0
                        inspectiontaskFeedback.appearance = "";  //结果异常，展示现象的值
                        if (inspectiontaskFeedback.isclose == 0 && !($(table).find("td.repair").is(':hidden'))) {
                            inspectiontaskFeedback.appearance = $(table).find('td.repair span.select2-selection__rendered').text();
                            if (inspectiontaskFeedback.appearance == "请选择或者输入故障现象" || inspectiontaskFeedback.appearance == null) {
                                layer.msg("打开的必选项需填写",{time: 1000,icon:7});
                                requireDown = false;
                                return false;
                            }
                        }
                        if (inspectiontaskFeedback.isclose == 0 && inspectiontaskFeedback.check_value == "请输入检查值") {
                            layer.msg("打开的必选项需填写",{time: 1000,icon:7});
                            requireDown = false;
                            return false;
                        }
                        inspectiontaskFeedbackList.push(inspectiontaskFeedback);
                    });
                    if (!requireDown) {
                        return;
                    }

                    var inspectiontask = {
                        "inspectiontask_id": inspectiontask_id,
                        "inspectiontaskFeedbackList": inspectiontaskFeedbackList
                    }


                    common.callAjax('post',false,common.interfaceUrl.inspectionSaveSubjectFeedBack,"json",{param: JSON.stringify(inspectiontask)},function(data){
                        var devids = $(that).closest("patrol-area-table-feedback").find('span.subnamme');
                        for(var k=0;k<devids.length;k++){
                            var subject_id = devids[k].text();
                            for(var i = 0;i<allInperctionData.length;i++){
                                if(allInperctionData[i].subject_id == subject_id){
                                    allInperctionData.splice(i,1);
                                }
                            }
                        }
                        allInperctionData = allInperctionData.concat(inspectiontaskFeedbackList);
                        $(that).addClass("finishInp")
                        if (data.flag == true) {
                            layer.msg("保存成功！",{time: 1000,icon:1});
                        } else {
                            layer.msg("保存失败！",{time: 1000,icon:7});
                        }
                    });
                    var $thisPoptable = $(this).closest('ul').parent().find("table.patrol-area-popTable");
                    var $thisLi = $(this).closest(".patrol-area-table");
                    $thisPoptable.parent().addClass("hide");
                    $thisLi.find(".nowTr .patrol-feedback-icon.thisEdit").addClass("hide");
                    $thisLi.find(".nowTr .patrol-feedback-icon.thisEdit").closest("td").find(".nowTr .switch-mini").addClass("hide");
                    $thisLi.find(".nowTr .patrol-feedback-icon.thisEdit").next().removeClass("hide");

                });

            },

            //录入区域的生成函数
            createLoadArea: function (domId, thatIcon, that, nowDate, thisSubDat) {

                var domIds = domId.split("_");
                var devname = $(thatIcon).parent().prev().prev().prev().text();
                var devloca = $(thatIcon).parent().prev().prev().text();
                var region = 0;

                if (domIds.length > 0) {

                    var divs = '<div class="contain-box showTable" style="border: 1px solid #15c7bd;">';
                    var tableTitle = '<table width=900 class="userFormTable ensureTable"><tr><td width=9%><span class="tdlable devicenames">设备名称：</span></td><td width=30%>' + devname + '</td><td width=10%>设备位置：</td><td width=28%><span class="eqPos">' + devloca + '</span></td><td width=12%></td></tr></table>';
                    divs = $(divs).append(tableTitle);
                    $.each(domIds, function (index, item) {
                         vidList = []//清空，存放上传的视频
                         imgList = []//清空，存放上传的图片
                        var thisSubject = thisSubDat.subjects[index];
                        var tableHTML = "";
                        var upload='<div style="overflow:hidden"><div  class="upload">' +
                            '<span class="txt">上传图片</span>' +
                            '<ul class="imgArea">' +
//                            '<li><img src="/resource/img/add.png"><div class="delIcon">×</div></li>' +
//                            '<li><img src="/resource/img/add.png"><div class="delIcon">×</div></li>' +
//                            '<li><img src="/resource/img/add.png"><div class="delIcon">×</div></li>' +
//                            '<li><img src="/resource/img/add.png"><div class="delIcon">×</div></li>' +
//                            '<li><img src="/resource/img/add.png"></li>' +
//                            '<li><img src="/resource/img/add.png"></li>' +
                            '<div class="addIcon"></div>' +
                            '</ul>' +
                            '</div>';



                        upload += '<div  class="upload">' +
                            '<span class="txt">上传视频</span>' +
                            '<ul class="videoArea">' +
//                            '<li><video src="/resource/img/add.png"><div class="delIcon">×</div></li>' +
//                            '<li><video src="/resource/img/add.png"></li>' +
//                            '<li><video src="/resource/img/add.png"></li>' +
//                            '<li><video src="/resource/img/add.png"></li>' +
//                            '<li><img src="/resource/img/add.png"></li>' +
//                            '<li><img src="/resource/img/add.png"></li>' +
                            '<div class="addIcon"></div>' +
                            '</ul>' +
                            '</div></div>';
//                        var upload = '<tr><td style="display:block;margin-right:60px"><div class="uploadFile"><div id="imgarea'+item+'"><div class="fileimg"><label>上传图片：</label><img id="img1'+item+'" class="img1'+item+'" src="" style="width:200px;height:200px;"><input  type="hidden" name="mytext[]" id="img'+item+'" readonly/><a class="uploadclass">上传</a>&nbsp;<a id="uploadmoreimg'+item+'">新增</a></div></div></div></td>' +
//                            '<td><div class="uploadFile"><div id="vidarea'+item+'"><div class="filevid"><label>上传视频：</label><video width="200" height="200" controls="controls"><source id="vid1'+item+'" class="img1'+item+'" src="" type="video/mp4"></video><input type="hidden" name="mytext[]" id="vid'+item+'" readonly/><a class="uploadclass">上传</a>&nbsp;<a id="uploadmorevid'+item+'">新增</a></div></div></div></td></tr>';

//                        var upload = '<tr><td style="display:block;margin-right:60px"><div class="uploadFile"><div id="imgarea'+item+'"><div class="fileimg"><label>上传图片：</label><img id="img1'+item+'" class="img1'+item+'" src="" style="width:200px;height:200px;"><input  type="hidden" name="mytext[]" id="img'+item+'" readonly/><a class="uploadclass">上传</a>&nbsp;<a id="uploadmoreimg'+item+'">新增</a></div></div></div></td>' +
//                                     '<td><div class="uploadFile"><div id="vidarea'+item+'"><div class="filevid"><label>上传视频：</label><img id="vid1'+item+'" class="img1'+item+'" src="" style="width:200px;height:200px;"><input type="hidden" name="mytext[]" id="vid'+item+'" readonly/><a class="uploadclass">上传</a>&nbsp;<a id="uploadmorevid'+item+'">新增</a></div></div></div></td></tr>';
                        //内容是否可编辑
                        var hidEle = '<div style="position:absolute;z-index:10;background-color:#f5f2ec;opacity:0.5" class="getwid' + index + ' hideEle" hidden></div>';

                        tableHTML += '<table style="position:relative; border-collapse:collapse;"  class="userFormTable patrol-area-popTable unsureTable" id="popTable' + item + '"><tbody>';

                        tableHTML += '<tr style="position: relative;"><td class="tdlable" width=11%>巡检项编码：</td><td width=32%><span' +
                            ' class="patrolList subnamme" id="subnamme' + item + '">' + item + '</span></td>' +
                            ' <td class="tdlable" width=11%>巡检项：</td><td style="word-wrap: break-word;word-break: break-all;" width=32%><span class="patrolList" id="subname' + item + '">温度检查</span></td>' +
                            ' <td class="switch" style="text-align: center; z-index:11"><div class="switch switch-mini"><input type="checkbox" checked /></div></td></tr>';

                        tableHTML += '<tr ><td class="tdlable"><span class="requireStar">*</span>检查值：</td><td class="checkValue"><select id="checkVal' + item + '" name="eqName" class="popInp requireCheck requireCheckNor"></select><input id="inputVal' + item + '" hidden></td><td class="tdlable">单位：</td><td><span class="unit" id="checkUnit' + item + '">无</span></td></tr><tr style="position: relative;"><td class="tdlable">参考值：</td><td><span class="referValue" id="referValue' + item + '">无</span></td><td class="tdlable"><span>测量时间：</span></td><td><span class="nowTime" id="checkTime' + item + '"></span></td></tr>';
                        tableHTML += '<tr><td class="tdlable"><span class="requireStar">*</span>反馈结果：</td><td><div class="multisSpan"><div style="margin:0;padding:0;" class="radio clearfix revResult" id="checkResult' + item + '"><span class="rightSpan region useRight">正常</span><span class="rightSpan unusual">报修</span><span class="unusual">异常</span></div></div></td><td class="tdlable repair" hidden><span class="requireStar">*</span>现象：</td><td class="repair" hidden><select id="moreExc' + item + '" name="eqName" class="wideInp requireCheck"></select></td></tr>';

                        tableHTML += '<tr class="repair" hidden><td class="tdlable"><span class="requireStar">*</span>期望完成时间：</td><td><div class="expect-time"><input type="text"class="expectTime' + item + '"></div></td>'+
                            '<td class="tdlable"><span class="requireStar">*</span>处理方式：</td><td><select class="processMode' + item + '"></select></td></tr>'

                        tableHTML += '<tr><td class="tdlable remarks">备注：</td><td colspan="4"><textarea name="excDescription"' +
                            ' placeholder="备注描述" id="excDescription' + item + '" maxlength="150"' +
                            ' style="width: 75%;padding: 5px 0 0;"></textarea><span class="s_area2" style="position: relative; top:' +
                            ' 18px;left:15px;color: #CCCCCC; display:none">0/150</span></td></tr>';
                        tableHTML += '<tr><td class="tdlable"></td><td><div style="width: 275%;border: 1px dashed #CCCCCC;border:' +
                            ' box;border-bottom: 0px;margin-bottom: 0px;margin-left: -60px;"></div></td></tr></tbody></table>';

                        divs = $(divs).append(tableHTML);
                        divs = $(divs).append(hidEle);



                        var tablebtm = '<ul class="btnlist_list clearfix"><li></li><li style="margin-top:10px;margin-left:-98px;"><div class="btnlist"><a class="submitBtn" id="submitBtn1' + domId + '">确定</a><a' +
                            ' class="ml15 cancelBtn" id="cancelBtn1' + domId + '">取消</a></div></li><li colspan="2"></li></ul></div>';
                        if (domIds.length == index + 1) {
                            $(divs).append(upload);
                            divs = $(divs).append(tablebtm);
                        }

//                        var vidList = []//存放上传的视频
//                        var imgList = []//存放上传的图片
                        //删除图片
                        $(document).on("click",".imgArea .delIcon",function() {
                            var thisImgId = $(this).prev().attr("src");
                            for(var i=0;i<imgList.length;i++){
                                if(imgList[i] == thisImgId){
                                    imgList.splice(i,1);
                                }
                            }
                            //显示添加按钮
                            if($(this).closest('.imgArea').find('li').length<=6){
                                $(this).closest('.imgArea').find('.addIcon').css('display','block');
                            }
                            $(this).closest('li').remove();
                        });

                        //删除视频
                        $(document).on("click",".videoArea .delIcon",function() {
                            var thisVid = $(this).prev().attr("src");
                            for(var i=0;i<vidList.length;i++){
                                if(vidList[i] == thisVid){
                                    imgList.splice(i,1);
                                }
                            }
                            //显示添加按钮
                            if($(this).closest('.videoArea').find('li').length<=6){
                                $(this).closest('.videoArea').find('.addIcon').css('display','block');
                            }
                            $(this).closest('li').remove();
                        });

                        //上传图片
                        $(document).on('click','.imgArea .addIcon',function () {
                            var addHtml = '<li><img src=""><div class="delIcon">×</div></li>'
//                            $(this).before(addHtml);
                            //上传图片
                            var $this = $(this);
                            var finder = new CKFinder();
//                            var imgId = $(this).prev();
                            finder.selectActionFunction = function (fileUrl) {
                                $this.before(addHtml);
                                imgList.push(fileUrl);
                                var nImgList = imgList.sort();
                                for(var i=0;i<nImgList.length;i++){
                                    if(nImgList[i] == nImgList[i+1]){
                                        imgList.splice(i,1);
                                        layer.msg("无法重复上传",{time: 1000,icon:7});
                                        $($this).prev('li').remove();
                                        return;
                                    }
                                }
                                    $($this).prev().find('img').attr("src",fileUrl);


                            } //当选中图片时执行的函数
                            finder.popup();//调用窗口
                            //最多上传6张图片，隐藏添加按钮
                            if($this.closest('.imgArea').find('li').length>=5){
                                $this.css('display','none');
                            }

                        });
//                        视频添加
                        $(document).on('click','.videoArea .addIcon',function () {
                            var addHtml = '<li><video src="" controls="controls"></video><div class="delIcon">×</div></li>'
//                            $(this).before(addHtml);
                            //上传图片
                            var $this = $(this);
                            var finder = new CKFinder();
                            var imgId = $(this).prev();
                            finder.selectActionFunction = function (fileUrl) {
                                $this.before(addHtml);
                                vidList.push(fileUrl);
                                var nvidList = vidList.sort();
                                for(var i=0;i<nvidList.length;i++){
                                    if(nvidList[i] == nvidList[i+1]){
                                        vidList.splice(i,1);
                                        layer.msg("无法重复上传",{time: 1000,icon:7});
                                        $($this).prev('li').remove();
                                        return;
                                    }
                                }
                                $($this).prev().find('video').attr("src",fileUrl);


                            } //当选中图片时执行的函数
                            finder.popup();//调用窗口
                            //最多上传6个视频，隐藏添加按钮
                            if($this.closest('.videoArea').find('li').length>=5){
                                $this.css('display','none');
                            }
                        })
                        $(thatIcon).closest(".patrol-area-table").find(".patrol-area-table-feedback").append(divs);

                        //初始化赋值
                        if(thisSubject.check_result == 1){
                            $("#checkResult"+item).closest('tr').find('.repair').show();

                            $("#checkResult"+item).find(".rightSpan.unusual").addClass("region");

                        }else if(thisSubject.check_result == 2){
                            $("#checkResult"+item).closest('tr').find('.repair').show();
                            $("#checkResult"+item).find(".unusual:last").addClass("region");
                        }else{
                            $("#checkResult"+item).find(".rightSpan.useRight").addClass("region");
                        }

//                        /**附件上传-图片*/
//                        var FieldCount = 0;
////                        var imgList = [];
//                        $('#uploadmoreimg'+item).on('click', function () {
//                            var maxInputfile = 6;
//                            if (FieldCount < maxInputfile - 1) {
//                                FieldCount++;
//                                $('#imgarea'+item).append('<div class="fileimg"><img id="img1'+item+FieldCount +'" class="img1'+item+FieldCount +'" src="" style="width:200px;height:200px;"><input class="getImg" type="hidden" name="mytext[]" id="img_' + item + FieldCount +'" readonly /><a class="uploadclass">上传</a>&nbsp;<a class="removeclass">删除</a></div>');
//                            } else {
//                                layer.msg("最多可上传6张图片",{time: 1000,icon:7});
//                            }
//
//                        });
//                        $('#imgarea'+item).on('click', '.uploadclass', function () {
//                            var id = $(this).prev().attr('id');
//                            var imgId = $(this).prev().prev();//shufq:bug17621图片赋值
//                            var finder = new CKFinder();
//                            finder.selectActionFunction = function (fileUrl) {
//                                imgList.push(fileUrl);
//                                var nImgList = imgList.sort();
//                                for(var i=0;i<nImgList.length;i++){
//                                    if(nImgList[i] == nImgList[i+1]){
//                                        imgList.splice(i,1);
//                                        layer.msg("无法重复上传",{time: 1000,icon:7});
//                                        return;
//                                    }
//                                }
//                                $("#" + id).val(fileUrl);
//                                imgId.attr("src",fileUrl);//shufq:bug17621图片赋值
//                            } //当选中图片时执行的函数
//                            finder.popup();//调用窗口
//                        })
//                        $('#imgarea'+item).on('click', '.removeclass', function () {
//                            if ($(".fileimg").length > 1) {
//                                var thisimg = $(this).prev().prev().val();
//                                for(var i=0;i<imgList.length;i++){
//                                    if(imgList[i] == thisimg){
//                                        imgList.splice(i,1);
//                                    }
//                                }
//                                $(this).parent('div').remove();
//                            }
//                        })
//
//                        /**附件上传-视频*/
//                        var FieldCountvid=0;
////                        var vidList = []
//                        $('#uploadmorevid'+item).on('click',function(){
//                            var maxInputfile=6;
//                            if(FieldCountvid < maxInputfile - 1){
//                                FieldCountvid++;
//                                $('#vidarea'+item).append('<div class="filevid"><video width="200" height="200" controls="controls"><source id="vid1'+item+'" class="img1'+item+'" src="" type="video/mp4"></video><input class="getVid" type="hidden" name="mytext[]" id="vid_'+item+FieldCountvid+'" readonly /><a class="uploadclass">上传</a>&nbsp;<a class="removeclass">删除</a></div>');
//                            } else {
//                                layer.msg("最多可上传6张视频",{time: 1000,icon:7});
//                            }
//
//                        })
//                        $('#vidarea'+item).on('click', '.uploadclass', function () {
//                            var id = $(this).prev().attr('id');
//                            var vid = $(this).prev().prev();//shufq:bug17621视频赋值
//                            var finder = new CKFinder();
//                            finder.selectActionFunction = function (fileUrl) {
//                                vidList.push(fileUrl);
//                                var nVidList = vidList.sort();
//                                for(var i=0;i<nVidList.length;i++){
//                                    if(nVidList[i] == nVidList[i+1]){
//                                        vidList.splice(i,1);
//                                        layer.msg("无法重复上传",{time: 1000,icon:7});
//                                        return;
//                                    }
//                                }
//                                $("#" + id).val(fileUrl);
//                                vid.attr("src",fileUrl);//shufq:bug17621视频赋值
//
//                            } //当选中图片时执行的函数
//                            finder.popup();//调用窗口
//                        })
//                        $('#vidarea'+item).on('click','.removeclass',function(){
//                            if($(".filevid").length>1){
//                                var thisVid = $(this).prev().prev().val();
//                                for(var i=0;i<vidList.length;i++){
//                                    if(vidList[i] == thisVid){
//                                        vidList.splice(i,1);
//                                    }
//                                }
//                                $(this).parent('div').remove();
//                            }
//                        })

                        //日期插件
                        function getNowFormatDate() {
                            var date = new Date(new Date().getTime()+2*60*60*1000);   //当前时间加2小时
                            var seperator1 = "-";
                            var seperator2 = ":";
                            var month = date.getMonth() + 1;
                            var strDate = date.getDate();
                            if (month >= 1 && month <= 9) {
                                month = "0" + month;
                            }
                            if (strDate >= 0 && strDate <= 9) {
                                strDate = "0" + strDate;
                            }
                            var hour = date.getHours();
                            var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
                                    + " " + date.getHours() + seperator2 + date.getMinutes();
                            return currentdate;
                        }
                        var nowHour = new Date().getHours();
                        var nowMinutes = new Date().getMinutes();
                        $('.expectTime'+item).attr('value',getNowFormatDate());
                        $('.expectTime'+item).datetimepicker({
                            timeFormat: 'HH:mm',
                            dateFormat: "yy-mm-dd",
                            hour: nowHour + 2,
                            minures: nowMinutes,
                        })

                        //处理方式
                        var processMode = [{id:"1",text:"一般"},{id:"0",text:"加急"},{id:"2",text:"预约"}];
                        var pro = $('.processMode'+item).select2({
                            data:processMode,
                            width:200,
                            minimumResultsForSearch: -1,
                            tags: true,
                            theme: "ems"
                        })
                        for(var a in processMode){
                            if(processMode[a].text == "thisSubject.process"){
                                $('.processMode'+item).val(processMode[a].id).trigger('change');  //赋默认值
                            }
                        }


                        //检查值
                        var checkVals = [];
                        if (thisSubject && thisSubject.subject_unit == undefined) {

                            thisSubject.subject_value1 && checkVals.push(thisSubject.subject_value1);
                            thisSubject.subject_value2 && checkVals.push(thisSubject.subject_value2);
                            thisSubject.subject_value3 && checkVals.push(thisSubject.subject_value3);
                            $("#checkVal" + thisSubject.id).select2({
                                data: checkVals,
                                width: 200,
                                allowClear: false,
                                above: false,
                                placeholder:  thisSubject.check_value ||'请选择',
                                disabled: false,
                                minimumResultsForSearch: -1,
                                tags: true,
                                theme: "ems"
                            }).on('change', function () {
                                nowTime = new Date().Format("yyyy-MM-dd hh:mm:ss");
                                $("#checkTime" + item).html(nowTime);
                                $("#checkTime" + item).show();
                            });
                        } else if (thisSubject && thisSubject.subject_unit != undefined) {
                            // thisSubject.check_value && checkVals.push(thisSubject.check_value);
                            thisSubject.subject_ck_value && checkVals.push(thisSubject.subject_ck_value);
                            thisSubject.subject_sx_value && checkVals.push(thisSubject.subject_sx_value);
                            thisSubject.subject_xx_value && checkVals.push(thisSubject.subject_xx_value);
                            checkVals.sort(function (a, b) {
                                return a - b;
                            })
                            $("#referValue" + thisSubject.id).html(checkVals[0] + "~" + checkVals[checkVals.length-1]);  //参考值

                            $('#inputVal'+ thisSubject.id).show();
//                            $("#checkVal" + thisSubject.id).select2({
//                                data: [thisSubject.check_value],
//                                width: 200,
//                                allowClear: false,
//                                above: false,
//                                placeholder:  thisSubject.check_value ||'请输入检查值',
//                                disabled: false,
//                                minimumResultsForSearch: 0,
//                                tags: true,
//                                theme: "ems"
//                            }).on('change', function () {
//                                var thisCheckVal = $("#checkVal" + item).val();
//                                var thisReferVal = $("#referValue" + item).html();
//                                if (thisReferVal != "") {
//                                    referValList = thisReferVal.split("~").sort(function (a, b) {
//                                        return a - b
//                                    });
//                                    if (!Number(thisCheckVal) || Number(thisCheckVal) < Number(referValList[0]) || Number(thisCheckVal) > Number(referValList[1])) {
//                                        layer.msg("输入的值不在参考值范围内,请确认",{time: 1000,icon:7});
//                                    }
//                                }
//                                nowTime = new Date().Format("yyyy-MM-dd hh:mm:ss");
//                                $("#checkTime" + item).html(nowTime);
//                                $("#checkTime" + item).show();
//                            });
                        }
                        if (thisSubject) {
                            $("#subname" + thisSubject.id).html(thisSubject.subject_content);   //巡检项
                            $("#checkUnit" + thisSubject.id).html(thisSubject.unit_name);  //单位
                            $("#img" + thisSubject.id).val(thisSubject.check_picture);//图片
                            $("#vid" + thisSubject.id).val(thisSubject.check_video);//视频
                            $("#checkTime" + thisSubject.id).html(thisSubject.check_time);    // 测量时间
                        }

                        var standardFailureList = [];
                        if(thisSubDat && thisSubDat.standardFailureList.length>0){
                            for(var i=0;i<thisSubDat.standardFailureList.length;i++){
                                thisSubDat.standardFailureList[i].failure_phenomena_desc!=null && standardFailureList.push(thisSubDat.standardFailureList[i].failure_phenomena_desc);
                            }
                        }
                        //故障现象
                        $("#moreExc" + item).select2({
                            data: standardFailureList,
                            width: 200,
                            allowClear: false,
                            above: false,
                            placeholder: thisSubject.appearance || '请选择或者输入故障现象',
                            disabled: false,
                            minimumResultsForSearch: 0,
                            tags: true,
                            theme: "ems"
                        });

                        $(".getwid" + index).width("100%");
                        $(".getwid" + index).height(217);
                        $(".getwid" + index).css("margin-top", -217);

                    });
                }
            },

            //已完成页面的巡检区域编码表格生成
            orderCompleteCTable: function (data, that) {

                var liHTML = '<li class="patrol-area-table"><div class="table-scroll"><table class="std-workDet-tbl"><thead><tr><th>序号</th><th>设备名称</th><th>设备位置</th><th>巡检项</th><th>操作</th></tr></thead><tbody>';

                $.each(data, function (index, dataList) {
                    var patrolCode = dataList.patrolCode || "";
                    var dev_name = dataList.dev_name || "";
                    var loc_name = dataList.loc_name || "";
                    var subjectNames = dataList.subjectNames || "";
                    liHTML += '<tr class="' + status + '"><td>' + (1 + index) + '</td>';
                    liHTML += '<td style="display:none">' + patrolCode + '</td>' + '<td>' + dev_name + '</td>' + '<td>' + loc_name + '</td>' + '<td>' + subjectNames + '</td>';
                    liHTML += '<td><span class="patrol-feedback-icon"></span><span class="patrol-edit-icon' +
                        ' hide"></span><span stype="z-index:11;" class="switch switch-mini" ><input type="checkbox" checked /></span></td></tr>';
                });
                liHTML += "</tbody></table></div>" + '<div class="patrol-area-table-feedback" style="margin-top: 10px;"></div>';
                $(that).closest("li").after(liHTML);
            },


            /**工器具与备品备件下拉框数据**/
            materialSelect :function(){
                common.callAjax('post',false,common.interfaceUrl.toolsInfoData,"json",{"inspectiontask_id":inspectiontask_id},function(data){
                    if(data == null || data.length == 0){
                        materialSelect = [{
                            "material_code": null,
                            "material_id" : null,
                            "material_model" : null,
                            "material_name" : null,
                            "material_price" : null,
                            "material_unit" : null
                        }];
                    } else {
                        materialSelect = data;
                    }
                });
                /**人员工时下拉框数据**/
                common.callAjax('post',false,common.interfaceUrl.userExtGetAllUser,"json",{"inspectiontask_id":inspectiontask_id},function(data){
                    if(data == null || data.length == 0){
                        personSelect = [{
                            "b_realname": null
                        }];
                    } else {
                        personSelect = data;
                    }
                });
            }

        };

        $(function () {
            var troubleInfo = new TroubleInfo();
            troubleInfo.init();

        });

        //反馈明细模块
        $(function(){

            //设置所有字段只读属性
            var formField = [{editable:"true",display: "巡检使用明细", name: "standWorkDetailTab", comboboxName: "standWorkDetailTabBox", type: "select"}];
            //创建表单结构
            var formConfig = {
                space : 50, labelWidth : 120, inputWidth : 200,
                validate : true,
                fields : formField
            };
            $("#inputForm").ligerForm(formConfig);

            // 下拉表格
            var tableData=[{text: '工序', id: '0'},{text: '安全措施', id: '1'},{text: '工器具', id: '2'},
                {text: '备品备件', id: '3'},{text: '人员工时', id: '4'},{text: '其他费用', id: '5'}];
            var optionHtml = "";
            $.each(tableData, function (i, item) {
                optionHtml += "<option value=\"" + item.id + "\">" + item.text + "</option>";
            });
            $("#standWorkDetailTabBox").html(optionHtml);

            $(".select2-results__option").mouseover(function(){
                $(".select2-results__option").addClass("select2-highlighted");
            })
            $(".select2-results__option").mouseout(function(){
                $(".select2-results__option").removeClass("select2-highlighted");
            })


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

            /**巡检使用明细反馈表格数据**/
            common.callAjax('post',false,common.interfaceUrl.workProcedure,"json",{"operationwork_id" : parent.editId},function(data){
                if(data == null || data.length == 0){
                    procedureTable = [{
                        "procedure_code": "SWGX001",
                        "procedure_desc" : null,
                        "procedure_standard" : null,
                        "procedure_remark" : null
                    }];
                } else {
                    procedureTable = data;
                }
            });
            /**巡检任务安全措施表格数据**/
            common.callAjax('post',false,common.interfaceUrl.workSafety,"json",{"operationwork_id" : parent.editId},function(data){
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
            /**巡检任务工器具表格数据**/
            common.callAjax('post',false,common.interfaceUrl.workTools,"json",{"operationwork_id" : parent.editId},function(data){
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
            });
            /**巡检任务备品备件表格数据**/
            common.callAjax('post',false,common.interfaceUrl.workSpareparts,"json",{"operationwork_id" : parent.editId},function(data){
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
            });
            /**巡检任务人员工时表格数据**/
            common.callAjax('post',false,common.interfaceUrl.workPersonHours,"json",{"operationwork_id" : parent.editId},function(data){
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
            });
            /**巡检任务其他费用表格数据**/
            common.callAjax('post',false,common.interfaceUrl.workOtherexpenses,"json",{"operationwork_id" : parent.editId},function(data){
                if(data == null || data.length == 0){
                    othersTable = [{
                        "otherexpenses": null,
                        "otherexpenses_amount" : null,
                        "otherexpenses_remark" : null
                    }];
                } else {
                    othersTable = data;
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
                data:{Rows: procedureTable},
                width: '85%'
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
                    layer.msg("只剩一项时无法删除",{time: 1000,icon:7});
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
                data:{Rows: safetyTable},
                width: '85%'
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
                    layer.msg("只剩一项时无法删除",{time: 1000,icon:7});
                } else {
                    safety.deleteRow($(this).data("id"));
                }
            });

            // tools   工器具列表
            var tools = $("#tools").ligerGrid({
                columns: [
                    { display: '工器具',  name: 'material_id',type:'text',
                        editor: { type: 'selectGrid', data: materialSelect,
                            valueField: 'material_id',textField: 'material_name'},
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
                    { display: '数量', name: 'tools_num', type: 'text', editor: { type: 'text'} },
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
                    { display: '备注', name: 'tools_remark',editor: { type: 'text' },editor: { type: 'text' }},
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
                data:{Rows: toolsTable},
                width: '85%'
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
                    layer.msg("只剩一项时无法删除",{time: 1000,icon:7});
                } else {
                    tools.deleteRow($(this).data("id"));
                }
            });

            // spareparts   备品备件列表
            var spareparts = $("#spareparts").ligerGrid({
                columns: [
                    { display: '备品备件',  name: 'material_id',type:'text',
                        editor: { type: 'selectGrid', data: materialSelect,
                            valueField: 'material_id',textField: 'material_name'},
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
                    { display: '数量', name: 'spareparts_num', type: 'text', editor: { type: 'text'} },
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
                data:{Rows: sparepartsTable},
                width: '85%'
            });
            // 备品备件表格默认隐藏
            $("#spareparts").hide();
            // 备品备件表格内新增行
            $('#spareparts').on('click','.add',function(){
                spareparts.addRow({
                    "material_id": "",
                    "spareparts_num": "",
                    "material_unit": "",
                    "material_price": "",
                    "spareparts_total": "",
                    "spareparts_remark":""
                });
            });

            // 备品备件表格内删除行
            $('#spareparts').on('click','.del',function(){
                if(spareparts.getData().length == 1) {
                    layer.msg("只剩一项时无法删除",{time: 1000,icon:7});
                } else {
                    spareparts.deleteRow($(this).data("id"));
                }
            });

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

            // 人员工时列表
            var person = $("#person").ligerGrid({
                columns: [
                    { display: '人员',  name: 'loginname',type:'text',
                        editor: { type: 'selectGrid', data: personSelect,
                            valueField: 'loginname',textField: 'b_realname'},
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
                    { display: '实际工时', name: 'person_hours', type: 'text', editor: { type: 'text'} },
                    { display: '实际工时单价', name: 'person_hourprice', type: 'text', editor: { type: 'text'} },
                    { display: '小计', name: 'person_hourtotal',
                        render: function (rowdata, rowindex, value) {
                            var total = parseInt(rowdata.person_hours) * parseInt(rowdata.person_hourprice);
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
                data:{Rows: personhoursTable},
                width: '85%'
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
                    layer.msg("只剩一项时无法删除",{time: 1000,icon:7});
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
                    { display: '金额', name: 'otherexpenses_amount', type: 'text', editor: { type: 'text'} },
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
                data:{Rows: othersTable},
                width: '85%'
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
                    layer.msg("只剩一项时无法删除",{time: 1000,icon:7});
                } else {
                    otherexpenses.deleteRow($(this).data("id"));
                }
            });

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
            $("#standWorkDetailTabBox").val("0").trigger("change");

            $("#submitBtnAll").on("click",function () {
                var finishInp = $(".finishInp").length;
                if(finishInp == 0 || finishInp != allLength){
                    layer.msg("存在未完成的巡检项",{time: 1000,icon:7});
                    return;
                }
                //复杂表单用ajax提交
                var procedure1 = procedure.getData();     //工序表格数据
                $.each(procedure1,function(index,pro){
                    pro.inspectiontask_id = inspectiontask_id
                })
                var safety1 = safety.getData();     //安全措施表格数据
                $.each(safety1,function(index,pro){
                    pro.inspectiontask_id = inspectiontask_id
                })
                var tools1 = tools.getData();     //工器具表格数据
                $.each(tools1,function(index,pro){
                    pro.inspectiontask_id = inspectiontask_id
                })
                var spareparts1 = spareparts.getData();     //备品备件表格数据
                $.each(spareparts1,function(index,pro){
                    pro.inspectiontask_id = inspectiontask_id
                })
                var person1 = person.getData();     //人员工时表格数据
                $.each(person1,function(index,pro){
                    pro.inspectiontask_id = inspectiontask_id
                })
                var otherexpenses1 = otherexpenses.getData();     //其他费用表格数据
                $.each(otherexpenses1,function(index,pro){
                    pro.inspectiontask_id = inspectiontask_id
                })

                var param = new Object();
                param.procedureList = procedure1;
                param.safetyList = safety1;
                param.toolsList = tools1;
                param.sparepartsList = spareparts1;
                param.personList = person1;
                param.othersList = otherexpenses1;
                param.inspectiontask_id = inspectiontask_id;
                param.inspectiontaskFeedbackList = allInperctionData;

                common.callAjax('post',false,'${ctx}/eam/inspectionTask/feedBack',"text",{param:JSON.stringify(param),taskid:taskid, pstid:pstid},function(data){
                    if(data=="success"){
                        layer.msg('提交成功！',{icon:1,time: 1000}, function(index){
                            parent.$("#mytable").DataTable().ajax.reload();
                            parent.layer.closeAll();
                        });
                    }else if (data == "noauth") {
                        layer.msg('登录超时或无权限！',{time: 1000,icon:7}, function(index){
                            layer.close(index);
                        });

                    }else{
                        layer.msg('提交失败！',{time: 1000,icon:2}, function(index){
                            layer.close(index);
                        });
                    }
                });

            });

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
                <div class="patrol-area-detail" id="patrol-result-content">

                    <!-- 巡检使用明细反馈 -->
                    <form id="inputForm" action="" method="post" class="form-horizontal">

                    </form>
                    <div class="editDiv">
                        <div class="subeditDiv" id="procedure"></div>
                        <div class="subeditDiv" id="safety"></div>
                        <div class="subeditDiv" id="tools"></div>
                        <div class="subeditDiv" id="spareparts"></div>
                        <div class="subeditDiv" id="person"></div>
                        <div class="subeditDiv" id="otherexpenses"></div>
                    </div>
                    <div class="switchArea patrolResult" style="margin-top:30px;">
                        <div class="tdUl">
                            <ul class="patrol-area-list">

                            </ul>

                        </div>
                    </div>

                    <div class="btnlist">
                        <div id="submitBtnAll">提交</div>
                    </div>
                </div>
            </div>

            <%--流程图tab页--%>
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
    </div>
</body>
</html>