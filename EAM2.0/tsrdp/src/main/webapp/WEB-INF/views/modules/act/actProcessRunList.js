/**
 * Created by tiansu on 2017/7/25.
 */
define(["text!modules/act/actProcessRunList.html", "text!modules/module1/module1.css",""], function (htmlTemp, cssTemp) {
    var module = {
        init:function(){
            //上面还要加一个创建按钮
            $(".content-wrapper").html(htmlTemp);
            $(".css-attr").html(cssTemp);

            var searchField = new Array();
            var pdName = {display:"流程名称",editable:true,name:"processDefName",type:"text"};
            searchField.push(pdName);

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
                //"order": [[5, "desc"]],
                "hascheckbox": true,
                "hasIndex":true,
                "opBtns": [
                    {
                        "icon": "fa-history", "title": "流程跟踪", "callBack": function (data) {

                            layer.open({
                                type: 2,
                                title: '流程跟踪',
                                skin: 'layui-layer-rim', //加上边框
                                area: ['100%', '100%'], //宽高
                                closeBtn: 1, //显示关闭按钮
                                maxmin: true, //是否有最大最小化
                                content: [ctx+"/eam/act/process/trace/view?executionId="+data.procInsId],
                                end: function () {
                                    //location.reload();
                                }
                            });
                        }
                    },
                    {
                        "icon": "fa-times", "title": "删除", "callBack": function (data) {

                            layer.confirm('操作将使流程归档，确认删除吗？',{icon: 3, title:'提示'},function() {
                                $.ajax({
                                    type: "post",
                                    //async : false, //同步执行
                                    url: ctx + "/eam/act/process/deleteProcessInstance?processInstanceId=" + data.procInsId,
                                    dataType: "text", //传递数据形式为text
                                    success: function (data) {
                                        if (data == "success") {
                                            layer.msg('删除成功！', {icon: 1, time: 1000}, function (index) {
                                                $('#mytable').dataTable().fnDraw(false);
                                                // $("#mytable").DataTable().ajax.reload();
                                                layer.close(index);
                                            });
                                        } else {
                                            layer.msg("删除失败！", {time: 1000, icon: 2});
                                        }
                                    }
                                });
                            });
                        }
                    }

                ],
                "ajax": {
                    "url": ctx+"/eam/act/process/running",
                    "type":"post",
                    "data": function (param) {
                        console.log(param.search);
                        param.search=$("#searchForm").serializeJson();

                    },
                    "dataSrc": function (Object) {
                        $("#rowsCount").html('共'+Object  .recordsTotal+'条记录');
                        return Object.data;
                    }
                },
                "columns": [

                    {"data": "title", "orderable": false,title:"请求标题"},
                    {"data": "procDefName", "orderable": false,title:"流程名称"},

                    //{"data": "procDefVersion","orderable": false, title:"版本号"},
                    {"data": "processStarter","orderable": false, title:"创建人"},
                    {"data": "processInstCreateDate", "orderable": false,title:"创建时间"},
                    {"data": "currentTaskInfo","orderable": false, title:"当前节点"},
                    {"data": "isSuspended","orderable": false, title:"状态"},


                ],
                "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                   /* //新增省略号
                    if (aData.procDefId.length > 5) {
                        $('td:eq(3)', nRow).html( '<a title="'+aData.procDefId +'" data-code="' + aData.procDefId + '">'+aData.procDefId.substr(0, 12)+'...</a>');
                    }else{
                        $('td:eq(3)', nRow).html( '<a title="'+aData.procDefId +'" data-code="' + aData.procDefId + '">'+aData.procDefId+'</a>');
                    }

                    if (aData.businessCode.length > 5) {
                        $('td:eq(4)', nRow).html( '<a title="'+aData.businessCode +'" data-code="' + aData.businessCode + '">'+aData.businessCode.substr(0, 12)+'...</a>');
                    }else{
                        $('td:eq(4)', nRow).html( '<a title="'+aData.businessCode +'" data-code="' + aData.businessCode + '">'+aData.businessCode+'</a>');
                    }*/
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
                        "targets": [7]
                    }

                    ]
            });

          // tab.order([5,'desc']).draw();

        }
    }
    return module;
});


