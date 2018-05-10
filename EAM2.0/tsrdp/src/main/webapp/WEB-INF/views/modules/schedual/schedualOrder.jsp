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

    <script src="/resource/common.js"></script>
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
                <input type="hidden" id="schedualOrderId" value="${schedualOrder.id}"/>
                <li>
                    <div class="schedulename">排班编码</div>
                    <input id="schedualOrderCode" type="text" value="${schedualOrder.order_code}" readonly>
                </li>
                <li>
                    <div class="schedulename">排班名称</div>
                    <div class="scheduleinput">
                        <input id="schedualOrderName" type="text" value="${schedualOrder.order_name}">
                    </div>
                </li>
                <li>
                    <div class="schedulename">备注</div>
                    <div class="scheduleinput">
                        <textarea id="schedualOrderRemark" name="remark" style="width:200px;height:80px;" >${schedualOrder.remark}</textarea>
                    </div>
                </li>
                <%--<li>
                    <div class="schedulename">有效性</div>
                    <div class="scheduleinput">
                        <select id="schedualOrderEffictive" style="width: 200px">
                            &lt;%&ndash;TODO setEditValue&ndash;%&gt;
                            <option value="1" selected='selected'>有效</option>
                            <option value="0">无效</option>
                        </select>
                    </div>
                </li>--%>

            </ul>
        </div>
    </form>
</section>
</div>
</body>
<script>

    $(function () {
//        initData();
//        initActEvent();
    });

    function initData() {
        initSchedualType();
        initCalendar();
    }

    function initCalendar() {

    }

    function initActEvent() {
        //确定事件
        $('.Cfm').click(function(){

            parent.cpevent.start=$('#starttime').datepicker('getDate');
            var formend = $('#endtime').datepicker('getDate');
            if(formend!=undefined){
                parent.cpevent.end= new Date(formend.getFullYear(),formend.getMonth(),formend.getDate());
            }
            parent.cpevent.schedualType = $("#schedualType").val();
            parent.cpevent.title = parent.cpevent.emp+ "      "+$("#schedualType").find("option:selected").text();
            parent.$('#calendar').fullCalendar('updateEvent',parent.cpevent);
            parent.layer.closeAll('iframe');
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
                var innerHtml = '';
                $.each(data,function (index,element) {
                    innerHtml += "<option value='" + element.id+"'" ;
                    if(index == 0){
                        innerHtml+=" selected='selected'";
                    }
                    innerHtml+=  ">" + element.type_name + "</option>";
                });
            }
            $("#schedualType").html(innerHtml);

        });
    }

</script>
