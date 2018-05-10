//当前编辑条目的id
var editId;
//查询区域表单数据
var searchformConfig=null;
//列表标题数据
var schedualTypeTitle=null;
//新增编辑表单数据
var formConfig=null;
//行内按钮
var btnInline=[];

var tab;
$(function() {

});

define(["text!modules/schedual/schedualType.html", "text!modules/material/testMaterial.css","liger-all"], function (htmlTemp, cssTemp) {

    var module = {
        init:function(menuno){
            // menuno= 1056;
            this.render(menuno);
        },
        render:function(menuno) {
            this.fillPage();
            //查询字段权限
            this.initFieldAuth(menuno);
        },
        fillPage:function() {
            $(".content-wrapper").html(htmlTemp);
            $(".css-attr").html(cssTemp);
        },
        initFieldAuth:function (id) {
            var param = {};
            param.menuno = id;
            //查询表格、表单、查询区域的字段权限
            common.callAjax('post',false,ctx + "/material/testMaterial/getfields","json",param,function(data){
                //搜索区域赋值
                searchformConfig = data.srchfield;
                //列表赋值
                schedualTypeTitle = data.gridfield;
                //表单赋值
                formConfig = data.formfield;
            });
            this.initSearchForm();
            this.initButtonForm(id);
            this.initTableForm();

        },
        initSearchForm : function () {
            //初始化搜索区域字段
            var searchform = {
                space : 50,
                labelWidth : 120 ,
                inputWidth : 200,
                fields : searchformConfig
            };
            /**渲染查询区域表单**/
            $("#searchForm").ligerForm(searchform);
            $(".queryTable").append($("#searchForm"));
            //渲染查询按钮
            var btnHtml = "<div><input type='button' value='查询'  class='searchbtn' /><input type='button' value='重置'  class='resetbtn' /></div>";
            $("#searchForm").append(btnHtml);

            //查询按钮操作
            $(".searchbtn").on("click",function () {
                $('#mytable').dataTable().fnDraw(false);
                $(".queryTable").toggleClass("hide");
                var form = liger.get('searchForm');

                //var json =form.getData();
                //form.setData({materialcode: "", materialname: "", materialstatus: "1", materialstatus1: ""});
                //form.setData({materialstatus: "1"});

                var queryInfo = '<li>查询条件：</li>';
                $.each(form.getData("display"),function(i, item){
                    if(item){
                        queryInfo += '<li><span>'+i+':</span><span>'+item+'</span></li>';
                    }
                });
                $('.queryInfo').html(queryInfo);
            });

            //重置按钮
            $(".resetbtn").on("click",function () {
                var form = liger.get('searchForm');
                form.setData("empty");
                $('.queryInfo').html("");
                $('#mytable').dataTable().fnDraw(false);
            });

            $(".queryBtn").on("click", function () {
                $(".queryTable").toggleClass("hide");
            });




        },
        initButtonForm : function (id) {
            /**渲染按钮区域**/
            common.callAjax('post',false,ctx + "/eam/button/getButtonByRole?id="+id,"json",null,function(data){
                console.log(data);
                var btnHtml="";
                for(var i=0;i<data.length;i++){
                    var btn = data[i];
                    if(btn!==null && btn!=""){
                        if(btn.buttonname == "详情") {
                            var obj = {};
                            obj.icon = "fa-file-text-o";
                            obj.title = btn.buttonname;
                            obj.callBack = function (data) {
                                layer.open({
                                    type: 2,
                                    title:'详情',
                                    skin: 'layui-layer-rim', //加上边框
                                    area: common.getAreaS(), //宽高
                                    closeBtn: 1, //显示关闭按钮
                                    content: ctx + "/schedualType/editType?id=" + data.id + "&type=view"
                                });
                            }
                        }else if(btn.buttonname=="编辑"){
                            var obj = {};
                            obj.icon="fa-pencil";
                            obj.title=btn.buttonname;
                            obj.callBack=function (data) {
                                editId=data.id;
                                layer.open({
                                    type: 2,
                                    title:'修改',
                                    skin: 'layui-layer-rim', //加上边框
                                    area: common.getAreaS(), //宽高
                                    closeBtn: 1, //显示关闭按钮
                                    content: ctx+"/schedualType/editType?id="+editId+"&type=edit"
                                });
                            }
                        }else if(btn.buttonname=="删除"){
                            var obj = {};
                            obj.icon="fa-times";
                            obj.title=btn.buttonname;
                            obj.callBack=function (data) {
                                if(data.type_status=="1"){
                                    layer.msg("有效数据无法删除，请先修改状态",{icon:7,time: 500});
                                    return;
                                }
                                var cindex =  layer.confirm('确认删掉这条数据吗？',{icon: 3, title:'提示'},function() {
                                    common.callAjax('post',false,ctx + "/schedualType/deleteType?id="+data.id,"json",null,function(d){
                                        layer.msg(d.msg,{icon:1,time: 1000});
                                        if(d.flag) {
                                            $('#mytable').dataTable().fnDraw(false);
                                        }
                                    });
                                }, function(){
                                    layer.close(cindex);
                                });
                            }
                        }

                        if("删除" == btn.buttonname || "编辑" == btn.buttonname || "详情" == btn.buttonname){
                            btnInline.push(obj);
                        }

                        if("删除" != btn.buttonname && "编辑" != btn.buttonname && "详情" != btn.buttonname){
                            //行外按钮渲染
                            $("<input>",{
                                type:'button',
                                val:btn.buttonname,
                                id:btn.buttonno,
                                onclick:btn.onclickevent
                            }).appendTo($(".btnArea"));
                        }


                    }
                }
                console.log(btnInline);
            });

            //新增按钮
            $("#add").on("click", function () {
                layer.open({
                    type: 2,
                    title:'新增',
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getAreaS(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: ctx+"/schedualType/addType?id="+id
                });
            });
            //导入选择文件
            $("#import").on("click",function () {
                $("#fileField").click();
            });
            //导入文件数据
            $("#fileField").on("change",function () {
                common.setExcel('schedualType');
            });
            //下载模板
            $("#download").on("click",function () {
                common.loadExcelModel("schedualType");
            });
            //导出
            $("#export").on("click",function () {
                if(tab.getCheckedRow().length==0){
                    layer.msg("请选择一条数据！",{time: 1000,icon:7});
                    return;
                }
                var ids="";
                for(var i=0;i<tab.getCheckedRow().length;i++){
                    ids+=tab.getCheckedRow()[i].id+",";
                }
                window.open('/exportExcel.do?ids='+(ids.substr(0,ids.length - 1))+'&name=schedualType');
            });
            //全部导出
            $("#exportall").on("click",function () {
                window.open('/exportExcel.do?name=schedualType');
            });

        },
        initTableForm : function () {

            //将需要render的列做转换（例如状态）
            for (var i = 0; i < schedualTypeTitle.length; i++) {
                if (schedualTypeTitle[i].data == "materialstatus") {
                    schedualTypeTitle[i].render = function (data) {
                        return data =="1"?"有效":"无效";
                    }
                }
            }

            tab = common.renderTable("mytable", {
                "order": [[2, "desc"]],
                "bStateSave":true,
                "serverSide": true,
                "hascheckbox": true,
                "hasIndex":true,
                "ordering":true,
                "opBtns": btnInline,
                "ajax": {
                    "url": ctx+"/schedualType/getSchedualType",
                    "dataSrc": function (json) {
                        btnInline=[];
                        $("#rowsCount").html('共'+json.recordsTotal+'条记录');
                        return json.data;
                    },
                    "data": function (param) {
                        // param.search=$("#searchForm").serializeJson();
                        var form = liger.get('searchForm');
                        param.search = form.getData();
                        console.log(param);
                    }
                },
                "columns": schedualTypeTitle,
                "columnDefs": [
                    {
                        "render": function (dvalue, type, row) {
                            return dvalue =="1"?"有效":"无效";
                        },
                        "targets": [7]
                    }
                ]
            });
        }



    };
    return module;
});

