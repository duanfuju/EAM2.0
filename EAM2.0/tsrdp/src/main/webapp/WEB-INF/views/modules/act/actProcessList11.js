/**
 * Created by tiansu on 2017/7/25.
 */
define(["text!modules/act/actDefinitionList.html", "text!modules/module1/module1.css",""], function (htmlTemp, cssTemp) {
    var module = {
        init:function(){
            //上面还要加一个创建按钮
            $(".content-wrapper").html(htmlTemp);
            $(".css-attr").html(cssTemp);

           /* $("<input>",{
                type:'button',
                val:'创建模型',
                id:'add'
                // href:'/a/eam/act/model/create'
            }).appendTo($(".btnArea"));*/

            var tab = common.renderTable("mytable", {
                "order": [[1, "desc"]],
                "hascheckbox": true,
                "hasIndex":true,
                "opBtns": [
                    {
                        "icon": "fa-pencil", "title": "启动流程", "callBack": function (data) {
                            console.log(data);
                            layer.open({
                                type: 2,
                                title: '流程启动',
                                skin: 'layui-layer-rim', //加上边框
                                area: ['100%', '100%'], //宽高
                                closeBtn: 1, //显示关闭按钮
                                maxmin: true, //是否有最大最小化
                                content: [ctx+"/eam/act/process/startForm?processDefinitionId="+data.processDefineId],
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
                    "url": ctx+"/eam/act/process/list",
                    "dataSrc": function (json) {
                        $("#rowsCount").html('共'+json.recordsTotal+'条记录');
                        return json.data;
                    }
                },
                "columns": [
                    {"data": "processDefineId", "orderable": false,title:"流程定义ID"},
                    {"data": "processDefineKey", "orderable": false,title:"流程定义KEY"},
                    {"data": "name", "orderable": false,title:"流程定义名称"},
                    {"data": "deploymentId", "orderable": false, title:"部署ID"},
                    {"data": "desc", "orderable": false, title:"流程描述"},
                    {"data": "version","orderable": false, title:"版本号"},
                    //{"data": "resourceName","orderable": false, title:"XML"},
                    {"data": "diagramResourceName","orderable": false, title:"流程图"}

                ],
                "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                    //新增省略号
                    if (aData.diagramResourceName.length > 5) {
                        $('td:eq(8)', nRow).html( '<a title="'+aData.diagramResourceName +'" data-code="' + aData.diagramResourceName + '">'+aData.diagramResourceName.substr(0, 12)+'...</a>');
                    }else{
                        $('td:eq(8)', nRow).html( '<a title="'+aData.diagramResourceName +'" data-code="' + aData.diagramResourceName + '">'+aData.diagramResourceName+'</a>');
                    }

                    if (aData.processDefineId.length > 5) {
                        $('td:eq(2)', nRow).html( '<a title="'+aData.processDefineId +'" data-code="' + aData.processDefineId + '">'+aData.processDefineId.substr(0, 12)+'...</a>');
                    }else{
                        $('td:eq(2)', nRow).html( '<a title="'+aData.processDefineId +'" data-code="' + aData.processDefineId + '">'+aData.processDefineId+'</a>');
                    }

                },
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
                    }]
            });

            $('#mytable').on('click','a',function(){
                var tr = $(this).closest("tr");
                var row = tab.row( tr );
                var processDefineId = row.data().processDefineId;
                var diagramResourceName = row.data().diagramResourceName;

                layer.open({
                    type: 2,
                    title: '流程图',
                    skin: 'layui-layer-rim', //加上边框
                    area: ['100%', '100%'], //宽高
                    closeBtn: 1, //显示关闭按钮
                    maxmin: true, //是否有最大最小化
                    content: [ctx+"/eam/act/process/read-resource?pdid="+processDefineId+"&resourceName="+diagramResourceName],
                    end: function () {
                        //location.reload();
                    }
                });
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


