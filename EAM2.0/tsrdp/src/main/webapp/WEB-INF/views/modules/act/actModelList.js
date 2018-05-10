/**
 * Created by tiansu on 2017/7/25.
 */
define(["text!modules/act/actModelList.html", "text!modules/module1/module1.css",""], function (htmlTemp, cssTemp) {
    var module = {
        init:function(){
            //上面还要加一个创建按钮
            $(".content-wrapper").html(htmlTemp);
            $(".css-attr").html(cssTemp);

            $("<input>",{
                type:'button',
                val:'创建模型',
                id:'add'
                // href:'/a/eam/act/model/create'
            }).appendTo($(".btnArea"));



            var tab = common.renderTable("mytable", {
                "order": [[1, "desc"]],
                "hascheckbox": true,
                "hasIndex":true,
                "opBtns": [{
                    "icon": "fa-pencil", "title": "修改模型", "callBack": function (data) {
                    console.log(data);
                    layer.open({
                                    type: 2,
                                    title: '模型编辑器',
                                    skin: 'layui-layer-rim', //加上边框
                                    area: ['100%', '100%'], //宽高
                                    closeBtn: 1, //显示关闭按钮
                                    maxmin: true, //是否有最大最小化
                                    content: ["/act/process-editor/modeler.jsp?modelId=" + data.modelId],
                                    end: function () {
                                        //location.reload();
                                    }
                                });
                            }
                        },
                        /*{
                            "icon": "fa-check-square-o", "title": "部署", "callBack": function (data) {
                                console.log(data);

                                $.ajax({
                                    type : "post",
                                    //async : false, //同步执行
                                    url : ctx+"/eam/act/model/deploy?modelid=" + data.modelId,
                                    dataType : "text", //传递数据形式为text
                                    success : function(data)
                                    {
                                        if(data=="success"){

                                            layer.msg('部署成功！',{icon:1,time: 1000}, function(index){
                                                $('#mytable').dataTable().fnDraw(false);
                                                // $("#mytable").DataTable().ajax.reload();
                                                layer.close(index);
                                            });
                                        }else{
                                            layer.msg("部署失败！",{time: 1000,icon:2});
                                        }
                                    }
                                });

                            }
                        },*/
                        {
                            "icon": "fa-external-link", "title": "导出", "callBack": function (data) {
                                console.log(data);

                                window.open(ctx+"/eam/act/model/export?id=" + data.modelId);
                                /*$.ajax({
                                    type : "post",
                                    //async : false, //同步执行
                                    url : ctx+"/eam/act/model/export?id=" + data.modelId,
                                    dataType : "text", //传递数据形式为text
                                    success : function(data)
                                    {
                                        //alert(11);
                                    }
                                });*/

                            }
                        },
                        {
                            "icon": "fa-times", "title": "删除", "callBack": function (data) {
                                console.log(data);
                            layer.confirm('确认删掉这条数据吗？',{icon: 3, title:'提示'},function() {
                                $.ajax({
                                    type: "post",
                                    //async : false, //同步执行
                                    url: ctx + "/eam/act/model/delete?modelid=" + data.modelId,
                                    dataType: "text", //传递数据形式为text
                                    success: function (data) {
                                        if (data == "success") {

                                            layer.msg('删除成功！', {icon: 1,time: 1000}, function (index) {
                                                $('#mytable').dataTable().fnDraw(false);
                                                // $("#mytable").DataTable().ajax.reload();
                                                layer.close(index);
                                            });
                                        } else {
                                            layer.msg("删除失败！", {time: 1000, icon: 2});
                                        }
                                    }
                                });
                            })

                            }
                        }
                ],
                "ajax": {
                    "url": ctx+"/eam/act/model/list",
                    "dataSrc": function (json) {
                        $("#rowsCount").html('共'+json.recordsTotal+'条记录');
                        return json.data;
                    }
                },
                "columns": [
                    //{"data": "modelId", "orderable": false,title:"模型ID"},
                    {"data": "modelName", "orderable": false,title:"模型名称"},
                    {"data": "modelKey", "orderable": false,title:"模型标识"},

                    {"data": "createTime", "orderable": false, title:"创建时间"},
                    {"data": "modifyTime", "orderable": false, title:"修改时间"},
                    {"data": "version","orderable": false, title:"版本号"}
                ],
                "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                    //新增省略号
                    /*if (aData.modelName.length > 5) {
                        $('td:eq(2)', nRow).html( '<a title="'+aData.modelName +'" data-code="' + aData.modelName + '">'+aData.modelName.substr(0, 16)+'...</a>');
                    }else{
                        $('td:eq(2)', nRow).html( '<a title="'+aData.modelName +'" data-code="' + aData.modelName + '">'+aData.modelName+'</a>');
                    }*/

                },

            });

            //新增按钮
            $("#add").on("click", function () {
                layer.open({
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['800px', '350px'], //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: "/a/eam/act/model/create"
                });
            });

        }
    }
    return module;
});


