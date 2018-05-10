var cpevent;
define(["text!modules/schedual/schedual.html", "text!modules/schedual/schedual.css"], function (htmlTemp, cssTemp) {
    var calendarObj;var select_order_id = null;
    //0:待提交  1：待审批  2：已审批   3：已驳回
    var status_draft = 0; var status_pending_approve = 1;
    var status_approved = 2; var status_rejected = 3;
    var module = {
        init: function () {
            $(".content-wrapper").html(htmlTemp);
            $(".css-attr").html(cssTemp);
            try{
                //是否是从排班查询中跳转过来，是的话selectedOrderId有值，否则值为null或undefined
                if("undefined" != typeof selectedOrderId){
                    select_order_id = selectedOrderId;
                }
                this.render();
                selectedOrderId = null;
            }catch (e){
                console.error(e);
            }

        },
        render: function () {
            this.wenlongdata();
            this.initSettingForm();
            this.initCalendar();
            this.btnEvents();
            this.initCalendarData();

        },
        btnEvents:function () {
            $('#choseEmps').on("click",function(){
                layer.open({
                    type: 2,
                    title: '选择部门',
                    skin: 'layui-layer-rim', //加上边框
                    area: ['400px', '300px'], //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: ctx + "/common/tree/buildTree?type=org_dept_tree&isMult=true&leafOnly=true",
                    btn: ['确定', '取消']
                    ,yes: function(index, layero){
                        layer.close(index);
                        //查出所选部门人员并渲染成日历表的事件源
                        try{
                            var layerCtx = layero.find("iframe")[0].contentWindow;
                            var treeSelectedNode=layerCtx.zTreeObj.getSelectedNodes(true);
                            if(treeSelectedNode != undefined && treeSelectedNode.length > 0){
                                var deptId = treeSelectedNode[0].id;
                                var param = new Object();
                                param.deptno = deptId;
                                var colors = ["bg-green","bg-yellow","bg-aqua","bg-light-blue","bg-red","bg-purple",
                                    "bg-teal","bg-lime","bg-fuchsia","bg-navy"];
                                $('#external-events').html('');
                                //获取该部门下的人员；
                                common.callAjax('post',false,ctx + "/eam/dept/getUserByDept","json",param,function(data){
                                    $.each(data,function (index,element) {
                                        var cIndex = index%colors.length;//取余数，循环取颜色
                                        // var innerHtml = '<div class="external-event '+colors[cIndex]+'">'+ element.loginname+'</div>';
                                        var innerHtml = '<div class="external-event bg-green">'+ element.loginname+'</div>';
                                        $('#external-events').append(innerHtml);
                                    });
                                    //刷新事件源
                                    // calendarObj.fullCalendar('removeEvents');
                                    module.ini_events();
                                });
                            }
                        }catch (e){
                            console.error(e);
                        }
                    }
                    ,btn2: function(index, layero){
                        //按钮【按钮二】的回调
                        layer.close(index);
                    }
                });
            });

            //保存动作
            $('#saveBtn').on("click",function(){
                //新的需要创建排班编码；老的则不用创建新编码
                    layer.open({
                    type: 2,
                    title: '排班表保存',
                    skin: 'layui-layer-rim', //加上边框
                    area: ['500px', '400px'], //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: ctx + "/schedualOrder/createOrderPage?id=" + select_order_id,
                    // maxmin: true,
                    btn: ['确定', '取消']
                    , yes: function (index, layero) {
                        try {
                            debugger;
                            var layerCtx = layero.find("iframe")[0].contentWindow;
                            var schedualOrder = new Object();
                            schedualOrder.id = layerCtx.$('#schedualOrderId').val();
                            schedualOrder.order_code = layerCtx.$('#schedualOrderCode').val();
                            schedualOrder.order_name = layerCtx.$('#schedualOrderName').val();
                            schedualOrder.remark = layerCtx.$('#schedualOrderRemark').val();
                            schedualOrder.approve_status = status_draft;//草稿状态
                            schedualOrder.schedualList = new Array();
                            var schedualDetail = calendarObj.fullCalendar('clientEvents');
                            $.each(schedualDetail,function (index,element) {
                                var tmpDate = element.start._d;
                                if(!element.end || element.end== null){
                                    element.end = element.start;
                                }
                                if(element.end == element.start){
                                    var detail = new Object();
                                    detail.id = element.id;
                                    detail.schedual_emp = element.emp;
                                    detail.schedule_date = DateUtil.dateToStr('yyyy-MM-dd hh:mm:ss',tmpDate);
                                    var approve_status = element.status;
                                    if(approve_status){
                                        detail.status = status_draft;
                                    }else{
                                        detail.status = approve_status;
                                    }
                                    detail.schedual_type = new Object();
                                    detail.schedual_type.id = element.schedualType;
                                    schedualOrder.schedualList.push(detail);
                                }else{
                                    while (tmpDate < element.end._d){
                                        var detail = new Object();
                                        detail.id = element.id;
                                        detail.schedual_emp = element.emp;
                                        detail.schedule_date = DateUtil.dateToStr('yyyy-MM-dd hh:mm:ss',tmpDate);
                                        var approve_status = element.status;
                                        if(approve_status){
                                            detail.status = status_draft;
                                        }else{
                                            detail.status = approve_status;
                                        }
                                        detail.schedual_type = new Object();
                                        detail.schedual_type.id = element.schedualType;
                                        schedualOrder.schedualList.push(detail);
                                        tmpDate = DateUtil.dateAdd('d',1,tmpDate);
                                    }
                                }


                            });
                            console.log(schedualOrder);
                            schedualOrder = JSON.stringify(schedualOrder);
                            //定义了contentType，不能调用common.callAjax;
                            $.ajax({
                                type: 'post',
                                async: false, //同步执行
                                url: ctx + "/schedualOrder/saveOrUpdate",
                                data:schedualOrder,
                                dataType: "json",
                                contentType: "application/json; charset=utf-8",
                                success: function (data) {
                                    try {
                                        debugger;
                                        if(data.flag){
                                            layer.msg(data.msg,{time: 1000,icon:1});
                                            // if(select_order_id == null){
                                                //从详情页跳过来
                                                $(".sidebar-menu a[data-link='modules/schedual/schedualTable']").trigger("click");
                                            // }
                                        }
                                    } catch (e) {
                                        console.error(e);
                                    }
                                },
                                error: function(XMLHttpRequest, textStatus, errorThrown) {
                                    layer.msg('保存失败',{time: 1000,icon:2});
                                }
                            });
                            layer.close(index);


                        } catch (e) {
                            console.error(e);
                        }
                    }
                    , btn2: function (index, layero) {
                        //按钮【按钮二】的回调
                        layer.close(index);
                    }
                });
            });

        },

        initSettingForm:function () {


            var now = new Date();
            var d = now.getDate(),
                m = now.getMonth(),
                y = now.getFullYear();
            // 默认本月最后一天
            var lastDate = DateUtil.getLastDayOfMonth();


            $('#schedualEnd').datepicker({
                language:"zh-CN",
                dateFormat: "yy-mm-dd",
                minDate:now,

                showOtherMonths: true,
                selectOtherMonths: true,
                showButtonPanel: true,
                showOn: "both",
                buttonImageOnly: true,
                buttonImage: "calendar.gif",
                buttonText: "",
                changeMonth: true,
                changeYear: true,


                maxDate:new Date(y+1,m,d)
            });
            $('#schedualEnd').val(lastDate);

        },
        initCalendarData:function () {
            //初始化排班类型数据
            this.initSchedualType();
            this.initOrderSchedual();


        },

        initCalendar:function () {
            //初始化事件源（对应人员列表）
            this.ini_events();
            /* initialize the calendar
             -----------------------------------------------------------------*/
            //Date for the calendar events (dummy data)
            var date = new Date();
            var d = date.getDate(),
                m = date.getMonth(),
                y = date.getFullYear();
            var n = 0;


            /**
             * TODO:fullCalendar总结：
             * 1、fullCalendar控件日期本身使用的是utc国际标准时间，向eventObject的start、end赋值时要转换为utc时间（Date.utc(y,m,d,h,mi,s,ms)）;
             * 2、end时间可选填，如果是eventObj为一天事件时end可为空。end为闭合值，故要定位到某天，需要把end设为其后一天
             * 3、allDay为false时，日历上会加上时间展示
             * @type {jQuery}
             */
            calendarObj = $('#calendar').fullCalendar({
                // timezone:'local',
                // eventStartEditable:false,

                header: {
                    // left: 'month,agendaWeek,agendaDay',
                    left: '',
                    center: 'title',
                    right: 'today, prev, next'
                },
                monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
                monthNamesShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
                dayNames: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
                dayNamesShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
                today: ["今天"],
                firstDay: 1,
                buttonText: {
                    // prev: '&laquo;',
                    // next: '&raquo;',
                    prevYear: '&nbsp;&lt;&lt;&nbsp;',
                    nextYear: '&nbsp;&gt;&gt;&nbsp;',
                    today: '今天',
                    month: '月',
                    week: '周',
                    day: '日'
                },
                //Random default events
                events: [
                    /*{
                        id:57,
                        title: 'Click for Google',
                        start: new Date(y, m, 28),
                        end: new Date(y, m, 29),
                        url: 'http://google.com/',//点击时出发的事件
                        backgroundColor: "#3c8dbc", //Primary (light-blue)
                        borderColor: "#3c8dbc" //Primary (light-blue)
                    }*/
                ],
                editable: true,
                eventLimit: true, // 多个事件用more显示
                //日程点击事件
                eventClick: function (event, element) {
                    //传入到schedualEdit表单页面中作为参数
                    // event.end=Date.UTC(2017,8,22,0,0,0,0);
                    // $('#calendar').fullCalendar('updateEvent',event);

                    cpevent = event;
                    layer.open({
                        type: 2,
                        title: '编辑',
                        skin: 'layui-layer-rim', //加上边框
                        area: ['500px', '400px'], //宽高
                        closeBtn: 1, //显示关闭按钮
                        content: ctx+"/schedual/getSchedualForShow"
                    });
                },
                dayClick: function(date, allDay, jsEvent, view) {
                  //点击日历空白处时


                },
                droppable: true,
                drop: function (date, allDay) {
                    //向日历表拖动添加日程事件
                    var originalEventObject = $(this).data('eventObject');
                    originalEventObject.backgroundColor = $(this).css("background-color");
                    originalEventObject.borderColor = "#000000";//$(this).css("borderColor");
                    var newEventObjs = createEventObject(date,originalEventObject);
                    // 一旦日历重新取得日程源，则原有日程将消失，当指定stick为true时，日程将永久的保存到日历上。
                    $.each(newEventObjs,function (index,obj) {
                        $('#calendar').fullCalendar('renderEvent', obj, true);
                    });



                },
                eventDragStop: function(calEvent, jsEvent, ui, view){
                    //日程事件拖动释放时触发
                    // layer.alert('d');
                },
                eventResize:function (event, dayDelta, minuteDelta, revertFunc, jsEvent, ui, view) {
                    debugger;
                    /*var newEventObjs = createEventObject(event.start,event);
                    // 一旦日历重新取得日程源，则原有日程将消失，当指定stick为true时，日程将永久的保存到日历上。
                    $.each(newEventObjs,function (index,obj) {
                        $('#calendar').fullCalendar('renderEvent', obj, true);
                    });*/

                }


            });
            $('.fc-left').append('<input id="saveBtn" type = "button" value="保存"> ');

        //根据排班周期和截止日期创建eventObject对象
        function createEventObject(sDate,oriEventObj)  {
            try{
                var period = $('#schedualPeriod').val();
                debugger;
                /*
                 * 系统从datepicker控件获取的时间需要DateUtil转换成utc时间才能与fullcalendar时间交互；
                 */
                var eDate = DateUtil.strFormatToDate('yyyy-MM-dd',$('#schedualEnd').val(),true);
                eDate = DateUtil.dateAdd('d',1,eDate);//fullCandual控件end为后一天
                var rEventObjects = new Array();
                if(period == '' || eDate =='' || schedualType == ''){
                    layer.msg('周期，截止日期，排班类型不能为空',{time: 1000,icon:7});
                    return rEventObjects;
                }
                var tmpDate = sDate._d;
                if(period == 1){
                    var cEventObject = createNewObject(tmpDate,eDate,oriEventObj);
                    rEventObjects.push(cEventObject);
                }else{
                    while (tmpDate <= eDate){
                        var cEventObject = createNewObject(tmpDate,tmpDate,oriEventObj);
                        rEventObjects.push(cEventObject);
                        tmpDate = DateUtil.dateAdd('d',period,tmpDate);
                    }
                }
                return rEventObjects;

            }catch (e){
                console.error(e);
            }
        }

        //根据排班周期和截止日期创建eventObject对象
        function createNewObject(sDate,eDate,oriEventObj)  {
            try{
                    var cEventObject = $.extend({}, oriEventObj);
                    //多次拖动id不能重复，否则点击挪动会当成一个对象一起挪移,日历采用utc时间格式，与时区相差8小时，交互时要注意转换
                    cEventObject.id = cEventObject.title+$("#schedualType").val()+sDate.getTime()+eDate.getTime();//n++;
                    cEventObject.start = sDate;
                    cEventObject.allDay = true;
                    if(sDate.getTime() != eDate.getTime()){
                        cEventObject.end = eDate;
                    }
                    cEventObject.backgroundColor = oriEventObj.backgroundColor;//thisObj.css("background-color");
                    cEventObject.title="待提交      "+cEventObject.title + "      "
                        + $("#schedualType").find("option:selected").text();
                    cEventObject.emp = oriEventObj.title;
                    cEventObject.schedualType = $("#schedualType").val();
                    cEventObject.status= status_draft;
                    return cEventObject;
                } catch (e){
                    console.error(e);
                }
            }

        },
        //刷新事件源
        ini_events:function () {
            var ele = $('#external-events div.external-event');
            ele.each(function () {
                var eventObject = {
                    title: $.trim($(this).text()) // use the element's text as the event title
                };
                $(this).data('eventObject', eventObject);
                $(this).draggable({
                    zIndex: 1070,
                    revert: true, // will cause the event to go back to its
                    revertDuration: 0  //  original position after the drag
                });
            });
        },initSchedualType:function(){
            common.callAjax('post',false,ctx + "/schedualType/getAllSchedualType","json",null,function(data){
                // $("#schedualType").empty();//首先清空select现在有的内容
                if(data != undefined){
                    var typeData = [];
                    $.each(data,function (index,element) {
                        var type = new Object;
                        type.text = element.type_name;
                        type.id = element.id;
                        typeData.push(type);
                    });
                }


                $("#schedualType").select2({
                    allowClear: true,
                    above: false,
                    data: typeData,
                    placeholder:'请选择',
                    minimumResultsForSearch: 1
                });
            });

        },
        initOrderSchedual:function(){
            if(select_order_id != null){
                var param = new Object();
                param.id = select_order_id;
                common.callAjax('post',false,ctx + "/schedualOrder/getSchedualDetail","json",param,function(data){
                    debugger;
                    if(data && data.schedualList){
                        $.each(data.schedualList,function (index,element) {
                            var cEventObject = new Object();
                            cEventObject.id = element.id;
                            var schedule_dataStr = DateUtil.dateToStr('yyyy-MM-dd hh:mm:ss',new Date(element.schedule_date));
                            cEventObject.start = DateUtil.strFormatToDate('yyyy-MM-dd',schedule_dataStr,true);
                            // cEventObject.end = new Date();
                            cEventObject.allDay = true;
                            cEventObject.status=data.approve_status;
                            cEventObject.backgroundColor = module.getColorByStatus(data.approve_status);
                            cEventObject.borderColor = "#000000";
                            cEventObject.emp = element.schedual_emp;
                            cEventObject.schedualType = element.schedual_type.id;
                            cEventObject.title=module.getStatusName(data.approve_status)+"      "+element.schedual_emp
                                + "      " + element.schedual_type.type_name;

                            $('#calendar').fullCalendar('renderEvent', cEventObject, true);
                        });
                    }

                });
            }
        },
        getColorByStatus : function(status){
            if(status == 0){
                return "#00a65a";
            } else if (status == 1) {
                return "#f39c12";
            } else if (status == 2) {
                return "#00c0ef";
            } else if (status == 3) {
                return "#dd4b39";
            } else{
                return "#00a65a";
            }
        },
        getStatusName : function(status) {
            if (status == 0) {
                return "待提交";
            } else if (status == 1) {
                return "待审批";
            } else if (status == 2) {
                return "已通过";
            } else if (status == 3) {
                return "已退回";
            } else{
                return "";
            }
        },
        wenlongdata :function(){
            $.datepicker.regional['zh-CN'] = {
                clearText: '清除',
                clearStatus: '清除已选日期',
                closeText: '关闭',
                closeStatus: '不改变当前选择',
                prevText: '< 上月',
                prevStatus: '显示上月',
                prevBigText: '<<',
                prevBigStatus: '显示上一年',
                nextText: '下月>',
                nextStatus: '显示下月',
                nextBigText: '>>',
                nextBigStatus: '显示下一年',
                currentText: '今天',
                currentStatus: '显示本月',
                monthNames: ['一月','二月','三月','四月','五月','六月', '七月','八月','九月','十月','十一月','十二月'],
                monthNamesShort: ['一月','二月','三月','四月','五月','六月', '七月','八月','九月','十月','十一月','十二月'],
                monthStatus: '选择月份',
                yearStatus: '选择年份',
                weekHeader: '周',
                weekStatus: '年内周次',
                dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],
                dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],
                dayNamesMin: ['日','一','二','三','四','五','六'],
                dayStatus: '设置 DD 为一周起始',
                dateStatus: '选择 m月 d日, DD',
                dateFormat: 'yy-mm-dd',
                firstDay: 1,
                initStatus: '请选择日期',
                isRTL: false};
            $.datepicker.setDefaults($.datepicker.regional['zh-CN']);
        }


    }
    return module;
});