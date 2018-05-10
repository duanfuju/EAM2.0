/**
 * Created by tiansu on 2017/7/25.
 */
define(["text!modules/act/actNativeTask.html", "text!modules/module1/module1.css",""], function (htmlTemp, cssTemp) {
    var module = {
        init:function(){

            $(".content-wrapper").html(htmlTemp);
            $(".css-attr").html(cssTemp);

            var searchField = new Array();
            var taskName = {display:"任务名称",editable:true,name:"taskName",type:"text"};
            var processName = {display:"流程名称",editable:true,name:"processName",type:"text"};
            searchField.push(taskName);
            searchField.push(processName);

            // 初始化搜索区域字段
            var searchformConfig = {
                space : 50, labelWidth : 120, inputWidth : 200,
                fields : searchField
            };

            /**渲染查询区域表单**/
            $("#searchForm").ligerForm(searchformConfig);
            $(".queryTable").append($("#searchForm"));
            //渲染查询按钮
            var btnHtml = "<div><input type='button' value='查询'  class='searchbtn' /><input type='button' value='重置'  class='resetbtn' /></div>";
            $("#searchForm").append(btnHtml);

            //查询按钮操作
            $(".searchbtn").on("click",function () {
                $('#mytable').dataTable().fnDraw(false);
                $(".queryTable").toggleClass("hide");

                var form = liger.get('searchForm');
                var queryInfo = '<li>查询条件：</li>';
                $.each(form.getData("display"),function(i, item){
                    if(item){
                        queryInfo += '<li><span>' + i + ':</span><span>' + item + '</span></li>';
                    }
                })
                $('.queryInfo').html(queryInfo);
            });

            //重置按钮
            $(".resetbtn").on("click",function () {
                var form = liger.get('searchForm');
                form.setData("empty");
                $('.queryInfo').html("");
                $('#mytable').dataTable().fnDraw(false);
            });

            //点击展示查询区域
            $(".queryBtn").on("click", function () {
                $(".queryTable").toggleClass("hide");
            });


            var tab = common.renderTable("mytable", {
                //"serverSide": false,
                //"order": [[7, "desc"]],
                "hascheckbox": true,
                "hasIndex":true,
                "opBtns": [
                    {
                        "icon": "fa-check", "title": "签收任务", "callBack": function (data) {
                            $.ajax({
                                type : "post",
                                url : ctx+"/eam/act/task/claim?taskid=" + data.taskId,
                                dataType : "text", //传递数据形式为text
                                success : function(data)
                                {
                                    if(data=="success"){

                                        layer.msg('签收成功！',{icon:1,time: 1000}, function(index){
                                            $('#mytable').dataTable().fnDraw(false);
                                            // $("#mytable").DataTable().ajax.reload();
                                            layer.close(index);
                                        });
                                    }else{
                                        layer.msg("签收失败！",{time: 1000,icon:2});
                                    }
                                }
                            });
                        }
                    },
                    {
                        "icon": "fa-times", "title": "反签收", "callBack": function (data) {
                            $.ajax({
                                type : "post",
                                url : ctx+"/eam/act/task/unclaim?taskid=" + data.taskId,
                                dataType : "text", //传递数据形式为text
                                success : function(data)
                                {
                                    if(data=="success"){

                                        layer.msg('任务反签收成功！',{icon:1,time: 1000}, function(index){
                                            $('#mytable').dataTable().fnDraw(false);
                                            // $("#mytable").DataTable().ajax.reload();
                                            layer.close(index);
                                        });
                                    }else{
                                        layer.msg("该任务不允许反签收！",{time: 1000,icon:2});
                                    }
                                }
                            });
                        }
                    },
                    {
                        "icon": "fa-navicon", "title": "查看任务", "callBack": function (data) {

                            layer.open({
                                type: 2,
                                title: '任务详情',
                                skin: 'layui-layer-rim', //加上边框
                                area: ['100%', '100%'], //宽高
                                closeBtn: 1, //显示关闭按钮
                                maxmin: true, //是否有最大最小化
                                content: [ctx+"/eam/act/process/getTaskForm?taskId=" + data.taskId+"&pdId="+data.procDefId+"&pIid="+data.procInsId],
                                end: function () {
                                    //location.reload();
                                }
                            });
                        }
                    }
                ],
                "ajax": {
                    "url": ctx+"/eam/act/task/todoList",
                    //"type":"post",
                    "data": function (param) {
                        //console.log(param.search);
                        param.search=$("#searchForm").serializeJson();

                    },
                    "dataSrc": function (json) {
                        $("#rowsCount").html('共'+json.recordsTotal+'条记录');
                        return json.data;
                    }
                },
                "columns": [
                    {"data": "title", "orderable": false,title:"请求标题"},
                    {"data": "procDefName", "orderable": false,title:"流程名称"},
                    {"data": "taskName", "orderable": false,title:"任务名称"},

                    {"data": "isSuspended","orderable": false, title:"状态"},
                    {"data": "taskCreateDate","orderable": false, title:"当前任务到达时间"},
                    {"data": "assignee","orderable": false, title:"办理人"}

                ],
                "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                    //流程定义id 新增省略号
                    // if (aData.procDefId.length > 5) {
                    //     $('td:eq(6)', nRow).html( '<a title="'+aData.procDefId +'" data-code="' + aData.procDefId + '">'+aData.procDefId.substr(0, 12)+'...</a>');
                    // }else{
                    //     $('td:eq(6)', nRow).html( '<a title="'+aData.procDefId +'" data-code="' + aData.procDefId + '">'+aData.procDefId+'</a>');
                    // }

                },
                "columnDefs": [

                    {
                        "render": function (data, type, row) {
                            if(data==1){
                                return "挂起";
                            }else{
                                return "正常";
                            }
                        },
                        "targets": [5]
                    }

                    ]
            });

            /*//新增按钮
            $("#add").on("click", function () {
                layer.open({
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['800px', '350px'], //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: "/a/eam/act/model/create"
                });
            });*/
        }
    }
    return module;
});


