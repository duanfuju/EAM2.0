<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <link rel="stylesheet" type="text/css" href="/resource/plugins/jQuery-Timepicker-Addon/jquery-ui-timepicker-addon.css">
    <script type="text/javascript" src="/resource/plugins/jQuery/jquery-2.2.3.min.js"></script>
    <script src="/resource/plugins/jQueryUI/jquery-ui.js"></script>
    <script src="/resource/plugins/jQuery-Timepicker-Addon/jquery-ui-timepicker-addon.js"
            type="text/javascript"></script>
    <script src="/resource/plugins/jQuery-Timepicker-Addon/jquery.ui.datepicker-zh-CN.js"
            type="text/javascript"></script>
    <script src="/resource/plugins/jQuery-Timepicker-Addon/jquery-ui-timepicker-zh-CN.js"
            type="text/javascript"></script>

    <script src="/resource/plugins/DateUtil.js" type="text/javascript"></script>
    <script src="/resource/common.js"></script>

    <script src="/resource/plugins/select2/select2.full.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="/resource/plugins/select2/select2.min.css">

    <style>
    .scheduleDetail{
        margin: 12px 0;
    }
    .scheduleDetail ul{
        list-style: none;
        font-size: 14px;
        color: #222222;
        line-height: 26px;
        padding: 0;
        margin: 0 auto;
        width: 300px;
    }
    .scheduleDetail .schedulename{
        margin-right: 10px;
        width: 80px;
        text-align: right;
        float: left;
        clear: both;
    }
    .scheduleDetail .scheduleinput ,.scheduleDetail input{
        width: 200px;
        display: inline;
    }
    .scheduleDetail li{
        margin-bottom: 20px;
    }
    .scheduleDetail li:last-child{
        text-align: center;
    }
    .scheduleDetail li .btn{
        height: 26px;
        width: 70px;
        border-radius: 4px;
        background-color: #15c7bd;
        color: #ffffff;
        font-size: 14px;
        border: none;
        margin-left: 10px;
    }
    div#ui-datepicker-div {
        font-size: 75%;
    }
</style>
</head>
<body>
<div id="main">
<section class="content module1">
    <form>
        <div class="scheduleDetail">
            <ul>
                <li>
                    <div class="schedulename">人员</div>
                    <div id="name" class="scheduleinput"></div>
                </li>
                <li>
                    <div class="schedulename"><span style="color:Red">*</span> 开始时间</div>
                    <div class="scheduleinput">
                        <input id="starttime" type="text">
                    </div>
                </li>
                <li>
                    <div class="schedulename"><span style="color:Red">*</span> 结束时间</div>
                    <div class="scheduleinput">
                        <input id="endtime" type="text">
                    </div>
                </li>
                <li>
                    <div class="schedulename"><span style="color:Red">*</span> 排班类型</div>
                    <div class="scheduleinput">
                        <input type="text" id="schedualType" name="schedualType">
                    </div>
                </li>
                <li class="btnGroup">
                    <input class="btn Del" type="button" value="删除">
                    <input class="btn Cfm" type="button" value="确定">
                    <input class="btn Cancel" type="button" value="取消">
                </li>
            </ul>
        </div>
    </form>
</section>
</div>
</body>
<script>

    $(function () {
        initData();
        initActEvent();
    });

    function initData() {
        initSchedualType();
        initCalendar();
    }

    function initCalendar() {
        $('#name').text(parent.cpevent.emp);
        $('#starttime').datepicker({
            dateFormat: "yy-mm-dd"
        });
        $('#endtime').datepicker({
            dateFormat: "yy-mm-dd"
        });
        $('#starttime').datepicker('setDate',parent.cpevent.start._d);
        if(parent.cpevent.end){
            //end为闭环，实际时间为end前一天
            var end = DateUtil.dateAdd('d',-1,parent.cpevent.end._d);
        }else{
            var end = parent.cpevent.start._d;
        }
        $('#endtime').datepicker('setDate',end);
        $('#schedualType').val(parent.cpevent.schedualType).trigger("change");
    }

    function initActEvent() {
        //确定事件
        $('.Cfm').click(function(){
            try{
                if($('#starttime').val() == '' || $('#endtime').val() == ''){
                    layer.msg('开始时间和结束时间不能为空',{time: 1000,icon:7});
                }
                var start = DateUtil.strFormatToDate('yyyy-MM-dd',$('#starttime').val(),true);
                var end = DateUtil.strFormatToDate('yyyy-MM-dd',$('#endtime').val(),true);
                debugger;
                parent.cpevent.start = start;
                parent.cpevent.end =Date.UTC(end.getFullYear(),end.getMonth(),end.getDate()+1,0,0,0,0);//end为闭环，时间+1
                parent.cpevent.schedualType = $("#schedualType").val();
                parent.cpevent.title = getStatusName(parent.cpevent.status)+"      " + parent.cpevent.emp+ "      " +
                    $("#schedualType").find("option:selected").text();
//            parent.cpevent.allDay = false;
                parent.$('#calendar').fullCalendar('updateEvent',parent.cpevent);
                parent.layer.closeAll('iframe');
            }catch (e){
                console.error(e);
            }

        })
        //删除事件
        $('.Del').click(function(){
            parent.$('#calendar').fullCalendar( 'removeEvents', parent.cpevent.id );
            parent.layer.closeAll('iframe');
        })
        //取消事件
        $('.Cancel').click(function(){
            parent.layer.closeAll('iframe');
        })
    }

    function initSchedualType(){
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
    }
    function getStatusName (status) {
        if (status == 0) {
            return "待提交";
        } else if (status == 1) {
            return "待审批";
        } else if (status == 2) {
            return "已通过";
        } else if (status == 3) {
            return "已退回";
        } else {
            return "";
        }
    }
</script>
