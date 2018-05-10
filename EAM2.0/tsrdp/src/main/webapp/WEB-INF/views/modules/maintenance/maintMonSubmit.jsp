<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp" %>
<html>
<head>
    <title>保养月计划生成</title>
    <meta name="decorator" content="default"/>
    <script src="/resource/plugins/jedate/jedate.js"></script>
    <link rel="stylesheet" href="/resource/plugins/jedate/skin/jedate.css">
    <script type="text/javascript">

        $(function () {
            var date;
            var tab=null;
            var btnInline=[];
            var tableTitle =null;
            //查询表格、表单、查询区域的字段权限
            common.callAjax('post',false,common.interfaceUrl.getfields,"json",{ "menuno":parent._menuno},function(data){
               tableTitle = data.gridfield;
                for (var i = 0; i < tableTitle.length; i++) {
                    if (tableTitle[i].data == "status") {
                        tableTitle[i].render = function (data) {
                            return data == "1" ? "有效" : "无效";
                        }
                    }
                    if(tableTitle[i].data == "project_status"){
                        tableTitle[i].render = function (data) {
                            if (data == "0") {
                                return "待提交";
                            } else if (data == "1") {
                                return "待审批";
                            } else if (data == "2") {
                                return "通过";
                            } else {
                                return "拒绝";
                            }
                        }
                    }
                    if(tableTitle[i].data == "project_period"){
                        tableTitle[i].render = function (data) {
                            if (data == "0") {
                                return "天";
                            } else if (data == "1") {
                                return "周";
                            } else if (data == "2") {
                                return "月";
                            } else if (data == "3") {
                                return "季";
                            } else {
                                return "年";
                            }
                        }
                    }
                }
                tab = common.renderTable("mytable", {
                    "serverSide": true,
                    "hascheckbox": true,
                    "hasIndex":true,
                    "opBtns":btnInline,
                    "ajax": {
                        "url": ctx+ '/maintain/maintainProjectSub/listData',
                        "dataSrc": function (json) {
                            btnInline=[];
                            $("#rowsCount").html('共'+json.recordsTotal+'条记录');
                            return json.data;
                        },
                        "data": function (param) {
                            param.search = {project_status:0,status:1};
                        }
                    },
                    "columns": tableTitle
                });

            });

            //时间插件
            date = new Date().getFullYear() + '-' + (new Date().getMonth()+1);
            $('#time').jeDate({
                    dateCell: '#time',
                    format:"YYYY-MM",
                    isClear:false,
                    isinitVal:true,
                    isToday:true,
                    fixed:true,
                    isCondition:false,
                    minDate:'1900-06-16 23:59:59',
                    maxDate:'2099-06-16 23:59:59',
                    okfun: function(obj){
                        date = obj.val;
                    }
                });

            //生成按钮
            $(".createData").on("click",function () {
                if(!date){
                    layer.msg("请先选择生成的年份！",{time: 1000,icon:2});
                    return;
                }
                layer.confirm('确认生成'+date+'数据吗？',{icon: 3, title:'提示'},function() {
                    common.callAjax('post',false,'${ctx}/maintain/maintainProjectSub/produce',"text",{"date":$('#time').val(),"way":0},function(data){
                        if(data=="success"){
                            layer.msg('生成成功！',{icon: 1,time: 1000}, function (index) {
                                $('#mytable').dataTable().fnDraw(false);
                                layer.close(index);
                            });
                        }else if(data=="norecord"){
                            layer.msg("没有数据生成！",{time: 1000,icon:3});
                        }else{
                            layer.msg("生成失败！",{time: 1000,icon:2});
                        }
                    });
                }, function(){
                });
            });
            //选中提交
            $(".submitByIds").on("click",function () {
                if(tab.getCheckedRow().length==0){
                    layer.msg("请选择一条数据！",{icon:7,time: 1000});
                    return;
                }
                layer.confirm('确认提交这条数据吗？',{icon: 3, title:'提示'},function() {
                    var ids="";
                    for(var i=0;i<tab.getCheckedRow().length;i++){
                        ids+=tab.getCheckedRow()[i].id_key+",";
                    }
                    common.callAjax('post',false,'${ctx}/maintain/maintainProjectSub/submitApprove',"text",{"ids":ids,"project_status":1},function(data){
                        if(data=="success"){
                            layer.msg('提交成功！',{icon: 1,time: 1000}, function (index) {
                                $('#mytable').dataTable().fnDraw(false);
                                layer.close(index);
                                parent.$('#mytable').dataTable().fnDraw(false);
                            });
                        }else{
                            layer.msg("提交失败！",{time: 1000,icon:2});
                        }
                    });
                }, function(){
                });
            });
            //全部提交
            $(".submitAll").on("click",function () {
                layer.confirm('确认提交全部数据吗？',{icon: 3, title:'提示'},function() {
                    common.callAjax('post',false,'${ctx}/maintain/maintainProjectSub/submitApprove',"text",{"project_status":1},function(data){
                        if(data=="success"){
                            layer.msg('提交成功！',{icon: 1,time: 1000}, function (index) {
                                $('#mytable').dataTable().fnDraw(false);
                                layer.close(index);
                                parent.$('#mytable').dataTable().fnDraw(false);
                            });
                        }else{
                            layer.msg("提交失败！",{time: 1000,icon:2});
                        }
                    });
                }, function(){
                });
            });
        });
    </script>
</head>
<body>
<div class="editDiv">
    <form id="searchForm"  class="form-horizontal" style="margin-left: 40px; margin-top: 10px; margin-bottom:10px">
        时间 <div class="jeinpbox" style="display:inline-block"><input style="border: 1px solid #7f7a74; height:30px;border-radius:2px; padding: 5px" type="text" class="jeinput" id="time" placeholder="YYYY-MM"></div>
        <input type='button' value='生成'  class='createData' />
        <input type='button' value='选中提交'  class='submitByIds' />
        <input type='button' value='全部提交'  class='submitAll' />
    </form>
</div>

<table id="mytable" class="table table-bordered table-hover mytable">
    <thead>
    </thead>
</table>
</body>
</html>
