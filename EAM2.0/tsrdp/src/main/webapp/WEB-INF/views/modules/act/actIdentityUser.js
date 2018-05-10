/**
 * Created by tiansu on 2017/7/25.
 */
define(["text!modules/act/actProcessRunList.html", "text!modules/module1/module1.css",""], function (htmlTemp, cssTemp) {
    var module = {
        init:function(){
            //上面还要加一个创建按钮
            $(".content-wrapper").html(htmlTemp);
            $(".css-attr").html(cssTemp);


            var tab = common.renderTable("mytable", {
                "order": [[1, "desc"]],
                "hascheckbox": true,
                "hasIndex":true,
                "opBtns": [
                    {
                        "icon": "fa-history", "title": "流程跟踪", "callBack": function (data) {

                            layer.open({
                                type: 2,
                                title: '流程启动',
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
                        /*{
                            "icon": "fa-check-square-o", "title": "部署", "callBack": function (data) {
                            console.log(data);
                            alert(11);
                            $.ajax({
                                type : "post",
                                //async : false, //同步执行
                                url : ctx+"/eam/act/model/deploy?modelid=" + data.modelId,
                                dataType : "text", //传递数据形式为text
                                success : function(data)
                                {
                                    if(data=="success"){

                                        layer.alert('部署成功！', function(index){
                                            $('#mytable').dataTable().fnDraw(false);
                                            // $("#mytable").DataTable().ajax.reload();
                                            layer.close(index);
                                        });
                                    }else{
                                        layer.alert("部署失败！");
                                    }
                                }
                            });

                        }
                    },*/
                    /*{
                        "icon": "fa-times", "title": "删除", "callBack": function (data) {
                            console.log(data);
                            alert(22);
                            $.ajax({
                                type : "post",
                                //async : false, //同步执行
                                url : ctx+"/eam/act/model/delete?modelid=" + data.modelId,
                                dataType : "text", //传递数据形式为text
                                success : function(data)
                                {
                                    if(data=="success"){

                                        layer.alert('删除成功！', function(index){
                                            $('#mytable').dataTable().fnDraw(false);
                                            // $("#mytable").DataTable().ajax.reload();
                                            layer.close(index);
                                        });
                                    }else{
                                        layer.alert("删除失败！");
                                    }
                                }
                            });

                        }
                    }*/
                ],
                "ajax": {
                    "url": ctx+"/eam/act/process/running",
                    "type":"post",
                    "dataSrc": function (Object) {
                        return Object.data;
                    }
                },
                "columns": [

                    {"data": "procInsId", "orderable": false,title:"流程实例ID"},
                    {"data": "procDefId", "orderable": false,title:"流程定义ID"},
                    {"data": "procDefName", "orderable": false,title:"流程定义名称"},
                    {"data": "procDefVersion","orderable": false, title:"版本号"},
                    {"data": "businessCode","orderable": false, title:"业务KEY"},
                    {"data": "isSuspended","orderable": false, title:"状态"}

                ],
                "columnDefs": [
                    {
                        "render": function (data, type, row) {
                            if(row.rolenote == undefined){
                                return "";
                            }else{
                                return data;
                            }
                        },
                        "targets": [8]
                    },
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


