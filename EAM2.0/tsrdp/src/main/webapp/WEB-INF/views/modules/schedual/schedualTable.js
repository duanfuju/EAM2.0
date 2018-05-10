//当前编辑条目的id
var editId;
var selectedOrderId;
//查询区域表单数据
var searchformConfig=null;
//列表标题数据
var materialTitle=null;
//新增编辑表单数据
var formConfig=null;
//行内按钮
var btnInline=[];

var tab=null;//DataTables对象

$(function() {

});

define(["text!modules/schedual/schedualTable.html", "text!modules/material/testMaterial.css","liger-all"], function (htmlTemp, cssTemp) {

    var module = {
        init:function(menuno){
            // menuno= 1059;
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
                materialTitle = data.gridfield;
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

            $.each(searchformConfig, function(index,val) {
                //查询区域初始化下拉树
                if (val.name == "approve_status") {
                    val.options = {
                        data: [
                            {text: '待提交', id: '0'},
                            {text: '待审批', id: '1'},
                            {text: '审批通过', id: '2'},
                            {text: '审批退回', id: '3'},
                        ]
                    }
                }
            });



            /**渲染查询区域表单**/
            var ligerSearch = $("#searchForm").ligerForm(searchform);
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
                                selectedOrderId = data.id;
                                var param = {};
                                param.id = data.id;
                                //跳转到排班页面并把selectedOrderId参数传递过去
                                $(".sidebar-menu a[data-link='modules/schedual/schedual']").trigger("click",[selectedOrderId]);
                            }
                        }else if(btn.buttonname=="提交"){
                            var obj = {};
                            obj.icon = "fa-check-square-o";
                            obj.title = btn.buttonname;
                            obj.callBack = function (data) {
                                if("0" != data.approve_status){
                                    layer.msg("只有待提交状态的可以提交",{time: 1000,icon:2});
                                    return;
                                }
                                common.callAjax('post',false,ctx + "/schedualOrder/commitSchedualOrder?id="+data.id,"json",null,function(d){
                                    layer.msg(d.msg,{time: 1000,icon:1});
                                    if(d.flag) {
                                        $('#mytable').dataTable().fnDraw(false);
                                    }
                                });
                            }

                        }else if(btn.buttonname=="编辑"){
                            /*var obj = new Object();
                            obj.icon="fa-pencil";selectedOrderId
                            obj.title=btn.buttonname;
                            obj.callBack=function (data) {
                                editId=data.id;
                                layer.open({
                                    type: 2,
                                    skin: 'layui-layer-rim', //加上边框
                                    area: ['900px', '500px'], //宽高
                                    closeBtn: 1, //显示关闭按钮
                                    content: ctx+"/schedualType/editType?id="+editId+"&type=edit"
                                });
                            }
                            */
                        }else if(btn.buttonname=="删除"){
                            var obj = {};
                            // obj.icon="fa-trash-o";
                            obj.icon="fa-times";
                            obj.title=btn.buttonname;
                            obj.callBack=function (data) {
                                if("0" != data.approve_status){
                                    layer.msg("只有待提交状态的可以删除",{time: 2000,icon:2});
                                    return;
                                }
                                var cindex = layer.confirm('确定要删除吗？', {
                                    btn: ['删除','取消'], //按钮
                                    shade: false //不显示遮罩
                                }, function(){
                                    common.callAjax('post',false,ctx + "/schedualOrder/deleteSchedualOrder?id="+data.id,"json",null,function(d){
                                        layer.msg(d.msg,{time: 1000,icon:1});
                                        if(d.flag) {
                                            $('#mytable').dataTable().fnDraw(false);
                                        }
                                    });
                                }, function(){
                                    layer.close(cindex);
                                });
                            }

                        }
                        if("删除" == btn.buttonname || "提交" == btn.buttonname || "详情" == btn.buttonname){
                            btnInline.push(obj);
                        }

                        if("删除" != btn.buttonname && "编辑" != btn.buttonname && "详情" != btn.buttonname && "提交" != btn.buttonname) {
                            //行外按钮渲染
                            $("<input>", {
                                type: 'button',
                                val: btn.buttonname,
                                id: btn.buttonno,
                                onclick: btn.onclickevent
                            }).appendTo($(".btnArea"));
                        }

                    }
                }
                console.log(btnInline);
            });



            //查询按钮操作
            $("#searchbtn").on("click",function () {
                $('#mytable').dataTable().fnDraw(false);
            });
            //新增按钮
            $("#add").on("click", function () {
                $(".sidebar-menu a[data-link='modules/schedual/schedual']").trigger("click",[selectedOrderId]);
            });
            //导入选择文件
            $("#import").on("click",function () {
                $("#fileField").click();
            });
            //导入文件数据
            $("#fileField").on("change",function () {
                common.setExcel('schedual');
            });
            //下载模板
            $("#download").on("click",function () {
                common.loadExcelModel("schedual");
            });
            //导出
            $("#export").on("click",function () {
                if(tab.getCheckedRow().length==0){
                    layer.msg("请选择一条数据！",{icon:7, time: 1000});
                    return;
                }
                var ids="";
                for(var i=0;i<tab.getCheckedRow().length;i++){
                    ids+=tab.getCheckedRow()[i].id+",";
                }
                window.open('/exportExcel.do?ids='+(ids.substr(0,ids.length - 1))+'&name=schedual');
            });
            //全部导出
            $("#exportall").on("click",function () {
                window.open('/exportExcel.do?name=schedual');
            });

        },

        initTableForm : function () {
             tab = common.renderTable("mytable", {
                "order": [[2, "desc"]],
                "bStateSave":true,
                "serverSide": true,
                "hascheckbox": true,
                "hasIndex":true,
                "ordering":true,
                "opBtns": btnInline,
                "ajax": {
                    "url": ctx+"/schedualOrder/getSchedualOrders",
                    "dataSrc": function (json) {
                        btnInline=[];
                        $("#rowsCount").html('共'+json.recordsTotal+'条记录');
                        return json.data;
                    },
                    "data": function (param) {
                        var form = liger.get('searchForm');
                        param.search = form.getData();
                        console.log(param);
                    }
                },
                "columns": materialTitle,
                "columnDefs": [
                    {
                        "render": function (dvalue, type, row) {
                            if(dvalue == '0'){
                                return "待提交";
                            }else if(dvalue == '1'){
                                return "待审批";
                            }else if(dvalue == '2'){
                                return "审批通过";
                            }else if(dvalue == '3'){
                                return "审批退回";
                            }
                        },
                        "targets": [5]
                    }]
            });
        }



    };
    return module;
});

//状态下拉初始化
/*
common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "common"},function(data){
    //状态：   有效  1       无效  0
    var statusHtml="";
    $.each(data, function (i, item) {
        statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
    });
    var type_statusBox= $("#type_statusBox");
    type_statusBox .html(statusHtml);
    type_statusBox.append($("<input></input>").attr("type", "hidden").attr("name", "type_status"));//设置隐藏域
    type_statusBox.val(1);//设置默认值
    type_statusBox.trigger('change.select2');
});*/
