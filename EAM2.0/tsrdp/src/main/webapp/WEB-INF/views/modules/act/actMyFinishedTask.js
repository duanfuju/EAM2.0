/**
 * Created by tiansu on 2017/7/25.
 */
define(["text!modules/act/actMyFinishedTask.html", "text!modules/module1/module1.css",""], function (htmlTemp, cssTemp) {
    var module = {
        init:function(){

            $(".content-wrapper").html(htmlTemp);
            $(".css-attr").html(cssTemp);

            var searchField = new Array();
            //var taskName = {display:"任务名称",editable:true,name:"taskName",type:"text"};
            var processName = {display:"流程名称",editable:true,name:"processName",type:"text"};
            //searchField.push(taskName);
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
                "order": [[1, "desc"]],
                "hascheckbox": true,
                "hasIndex":true,
                "opBtns": [
                    {
                        "icon": "fa-history", "title": "流程跟踪", "callBack": function (data) {
                            layer.open({
                                type: 2,
                                title: '流程图',
                                skin: 'layui-layer-rim', //加上边框
                                area: ['100%', '100%'], //宽高
                                closeBtn: 1, //显示关闭按钮
                                maxmin: true, //是否有最大最小化
                                content: [ctx+"/eam/act/process/trace/flow?executionId="+data.procInsId],
                                end: function () {
                                    //location.reload();
                                }
                            });
                        }
                    },

                ],
                "ajax": {
                    "url": ctx+"/eam/act/task/historic",
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
                    {"data": "processStarter","orderable": false, title:"创建人"},
                    {"data": "currentTaskInfo","orderable": false, title:"当前节点"}

                ],
                "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                   /* //新增省略号
                    if (aData.procDefId.length > 5) {
                        $('td:eq(4)', nRow).html( '<a title="'+aData.procDefId +'" data-code="' + aData.procDefId + '">'+aData.procDefId.substr(0, 12)+'...</a>');
                    }else{
                        $('td:eq(4)', nRow).html( '<a title="'+aData.procDefId +'" data-code="' + aData.procDefId + '">'+aData.procDefId+'</a>');
                    }*/

                },
                "columnDefs": [

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


