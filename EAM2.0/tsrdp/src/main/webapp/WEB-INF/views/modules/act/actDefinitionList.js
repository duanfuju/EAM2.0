/**
 * Created by tiansu on 2017/7/25.
 */
define(["text!modules/act/actDefinitionList.html", "text!modules/module1/module1.css",""], function (htmlTemp, cssTemp) {
    var module = {
        init:function(){
            //上面还要加一个创建按钮
            $(".content-wrapper").html(htmlTemp);
            $(".css-attr").html(cssTemp);

            /**导入模型上传按钮**/
            $("<input>",{
                type:'file',
                name:'file',
                id:'file',
                style:'display:none',
                // href:'/a/eam/act/model/create'
            }).appendTo($(".btnArea"));

            $("<input>",{
                type:'button',
                val:'导入模型',
                id:'import'
                // href:'/a/eam/act/model/create'
            }).appendTo($(".btnArea"));

            var tab = common.renderTable("mytable", {
                "order": [[1, "desc"]],
                "hascheckbox": true,
                "hasIndex":true,
                "opBtns": [
                    {
                        "icon": "fa-mail-forward", "title": "转换为模型", "callBack": function (data) {
                            $.ajax({
                                type : "post",
                                //async : false, //同步执行
                                url : ctx+"/eam/act/process/convert-to-model/"+ data.processDefineId,
                                dataType : "text", //传递数据形式为text
                                success : function(data)
                                {
                                    if(data=="success"){

                                        layer.msg('转换成功！',{icon:1,time: 1000}, function(index){
                                            $('#mytable').dataTable().fnDraw(false);
                                            // $("#mytable").DataTable().ajax.reload();
                                            layer.close(index);
                                        });
                                    }else{
                                        layer.msg("转换失败！",{time: 1000,icon:2});
                                    }
                                }
                            });
                        }
                    },
                    {
                        "icon": "fa-times", "title": "删除", "callBack": function (data) {
                            layer.confirm('操作将级联删除流程实例，确认删除吗？',{icon: 3, title:'提示'},function() {
                                $.ajax({
                                    type: "post",
                                    //async : false, //同步执行
                                    url: ctx + "/eam/act/model/deleteDeployment?deploymentId=" + data.deploymentId,
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

                    /*{
                        "icon": "fa-pencil", "title": "候选启动", "callBack": function (data) {
                            //console.log(data);
                            layer.open({
                                type: 2,
                                title: '候选启动',
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
                    },*/

                ],
                "ajax": {
                    "url": ctx+"/eam/act/process/listPage",
                    "dataSrc": function (json) {
                        $("#rowsCount").html('共'+json.recordsTotal+'条记录');
                        return json.data;
                    }
                },
                "columns": [
                    //{"data": "processDefineId", "orderable": false,title:"流程定义ID"},
                    //{"data": "processDefineKey", "orderable": false,title:"流程定义KEY"},
                    {"data": "name", "orderable": false,title:"流程定义名称"},
                    //{"data": "deploymentId", "orderable": false, title:"部署ID"},
                    {"data": "category", "orderable": false, title:"流程分类"},
                    {"data": "desc", "orderable": false, title:"流程描述"},
                    {"data": "version","orderable": false, title:"版本号"},
                    //{"data": "resourceName","orderable": false, title:"XML"},
                    {"data": "diagramResourceName","orderable": false, title:"流程图"}

                ],
                "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                    //新增省略号
                    if (aData.diagramResourceName.length > 5) {
                        $('td:eq(6)', nRow).html( '<a title="'+aData.diagramResourceName +'" data-code="' + aData.diagramResourceName + '">'+aData.diagramResourceName.substr(0, 12)+'...</a>');
                    }else{
                        $('td:eq(6)', nRow).html( '<a title="'+aData.diagramResourceName +'" data-code="' + aData.diagramResourceName + '">'+aData.diagramResourceName+'</a>');
                    }

                    // if (aData.processDefineId.length > 5) {
                    //     $('td:eq(2)', nRow).html( '<a title="'+aData.processDefineId +'" data-code="' + aData.processDefineId + '">'+aData.processDefineId.substr(0, 12)+'...</a>');
                    // }else{
                    //     $('td:eq(2)', nRow).html( '<a title="'+aData.processDefineId +'" data-code="' + aData.processDefineId + '">'+aData.processDefineId+'</a>');
                    // }
                },
                "columnDefs": [
                    {
                        /*"render": function (data, type, row) {
                            if(row.rolenote == undefined){

                                return "";
                            }else{
                                return data;
                            }
                        },
                        "targets": [8]*/
                    }]
            });

            //行内点击事件
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

            //导入模型按钮
            $("#import").on("click", function () {
                $("#file").trigger("click");
            });


            $("#file").change(function(){
                var filename = $(this).val();
                $.ajaxFileUpload(
                    {
                        url: ctx+'/eam/act/model/deployByFile', //用于文件上传的服务器端请求地址
                        secureuri: false, //是否需要安全协议，一般设置为false
                        fileElementId: 'file', //文件上传域的ID
                        dataType: 'text', //返回值类型 一般设置为json
                        success: function (data, status)  //服务器成功响应处理函数
                        {
                            //$("#img1").attr("src", data.imgurl);
                            if (data=="success"){
                                layer.msg('部署成功！',{icon:1,time: 1000}, function(index){
                                    $('#mytable').dataTable().fnDraw(false);
                                    layer.close(index);
                                });
                            }else{
                                layer.msg("部署失败！",{time: 1000,icon:2})
                            }

                        },
                        error: function (data, status, e)//服务器响应失败处理函数
                        {
                            layer.msg(e,{time: 1000,icon:2});
                        }
                    }
                )
            });
        }
    }
    return module;
});


