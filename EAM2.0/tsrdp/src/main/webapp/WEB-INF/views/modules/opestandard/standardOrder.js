//当前编辑条目的id
var editId;
//查询区域表单数据
var searchformConfig=null;
//列表标题数据
var materialTitle=null;
//新增编辑表单数据
var formConfig=null;
//行内按钮
var btnInline=[];

$(function() {

});

define(["text!modules/opestandard/standardOrder.html", "text!modules/opestandard/standardOrder.css","liger-all"], function (htmlTemp, cssTemp) {



    var module = {
       init:function(menuno){
           var menuno = arguments[0];
           this.render(menuno);
       },
        render:function(menuno){
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
                /**审批状态下拉数据**/
                var approve;
                var order_priority;
                common.callAjax('post',false,ctx + "/eam/bizDict/getValues","json",{"dict_type_code" : "operation_approval_status"},function(data){
                    approve= data;
                });
                common.callAjax('post',false,ctx + "/eam/bizDict/getValues","json",{"dict_type_code" : "priority"},function(data){
                    order_priority= data;
                });
                searchformConfig = data.srchfield;
                $.each(searchformConfig,function(index,item){
                    if(item.name=='approve_status'){
                        item.option={data:approve};
                    }
                    if(item.name=='order_priority'){
                        item.option={data:order_priority};
                    }
                });
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
            /**渲染查询区域表单**/
            $("#searchForm").ligerForm(searchform);
            $(".queryTable").append($("#searchForm"));
            //渲染查询按钮
            var btnHtml = "<div><input type='button' value='查询'  class='searchbtn' /><input type='button' value='重置'  class='resetbtn' /></div>";
            $("#searchForm").append(btnHtml);

        },
        initButtonForm : function (id) {

            /**渲染按钮区域**/
            btnInline =  common.ajaxForButton(id,'json',ctx+"/opestandard/standardOrder/editUI",ctx + "/opestandard/standardOrder/delete",ctx+"/opestandard/standardOrder/detailUI",common.getAreaS(),null,'approve_status',0);
            //点击展示查询区域
            $(".queryBtn").on("click", function () {
                $(".queryTable").toggleClass("hide");
            });

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
            //新增按钮
            $(".btnArea").on("click",'#add',function () {
                layer.open({
                    type: 2,
                    title: '新增',
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getAreaS(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: ctx+"/opestandard/standardOrder/addUI"
                });
            });
            $(".guide").on("click", function () {
                $('.wrapper').animate({scrollTop:0}, '100');
                setTimeout(function () {
                    var steplist = $.WebPageGuide({showCloseButton: true});
                    steplist.newGuidStep(".queryBtn", "查询", "点击显示查询区域");
                    steplist.newGuidStep(".btnArea", "操作", "这是导入导出和新增区域");
                    steplist.startGuide();
                },300);

            })
        },
        initTableForm : function () {

            //将需要render的列做转换（例 如状态）
            for (var i = 0; i < materialTitle.length; i++) {
                if (materialTitle[i].data == "order_status") {
                    materialTitle[i].render = function (data) {
                        return data == "1" ? "有效" : "无效";
                    }
                }
                if (materialTitle[i].data == "approve_status") {
                    materialTitle[i].render = function (data) {
                        if(data=="0"){
                            return "待审批";
                        }else if(data=="1"){
                            return "通过";
                        }else {
                            return "拒绝";
                        }
                    }
                }
                if (materialTitle[i].data == "order_priority") {
                    materialTitle[i].render = function (data) {
                        if(data=="0"){
                            return "一般";
                        }else if(data=="1"){
                            return "加急";
                        }
                    }
                }
            }

            var tab = common.renderTable("mytable", {
                //"order": [[3, "desc"]],
                //"bStateSave": true,
                "serverSide": true,
                "hascheckbox": true,
                "hasIndex": true,
                //"ordering":true,
                "opBtns": btnInline,
                "ajax": {
                    "url":ctx+ '/opestandard/standardOrder/listData',
                    "dataSrc": function (json) {
                        btnInline = [];
                        $("#rowsCount").html('共' + json.recordsTotal + '条记录');
                        return json.data;
                    },
                    "data": function (param) {
                        //param.search = $("#searchForm").serializeJson();
                        var form = liger.get('searchForm');
                        param.search=form.getData();
                        console.log(param);
                    }
                },
                "columns": materialTitle

            });


            //编辑按钮
            $("#edit").on("click", function () {
                alert('编辑');
                if(tab.getCheckedRow().length!=1){
                    layer.msg("请选择一条数据！",{icon:7, time: 1000});
                    return;
                }
                var id = tab.getCheckedRow()[0].id;
                editId=id;
                layer.open({
                    type: 2,
                    title: '修改',
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getAreaS(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: ctx+"/opestandard/standardOrder/editUI"
                });
            });
            $("#delete").on("click",function () {
                if(tab.getCheckedRow().length==0){
                    layer.msg("请选择一条数据！",{time: 1000,icon:7});
                    return;
                }
                layer.confirm('确认删掉这条数据吗？',{icon: 3, title:'提示'},function() {
                    var ids = "";
                    for (var i = 0; i < tab.getCheckedRow().length; i++) {
                        ids += tab.getCheckedRow()[i].id + ",";
                    }
                    common.callAjax('post', true, ctx + "/opestandard/standardOrder/delete", "text", {id: ids}, function (data) {
                        if (data == "success") {
                            layer.msg('删除成功！', {icon: 1,time: 1000}, function (index) {
                                $('#mytable').dataTable().fnDraw(false);
                                layer.close(index);
                            });
                        } else {
                            layer.msg("删除失败！", {time: 1000, icon: 2});
                        }
                    });
                })

            });


            //导出
            $("#export").on("click",function () {
                if(tab.getCheckedRow().length==0){
                    layer.msg("请选择一条数据！",{icon:7, time: 1000});
                    return;
                }
                var ids="";
                for(var i=0;i<tab.getCheckedRow().length;i++){
                    ids+=tab.getCheckedRow()[i].id_key+",";
                }
                window.open('/exportExcel.do?ids='+(ids.substr(0,ids.length - 1))+'&name=operationOrders');
            });
            //全部导出
            $("#exportall").on("click",function () {
                window.open('/exportExcel.do?name=operationOrders');
            });
        }
   };
    return module;
});


