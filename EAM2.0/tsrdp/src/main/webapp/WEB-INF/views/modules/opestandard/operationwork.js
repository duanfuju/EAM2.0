//当前编辑条目的id
var editId;
//查询区域表单数据
var searchformConfig = null;
//列表标题数据
var operationworkTitle = null;
//新增编辑表单数据
var formConfig = null;
//行内按钮
var btnInline = [];
//查询封装对象
var searchObj = {};
//标准工作类型下拉数据
var operationworkTypeSelect = null;
//标准工作审批状态下拉数据
var approveStatusSelect = null;
//状态下拉数据
var statusSelect = null;
// 初始化工器具和备品备件的下拉框列表数据
var materialSelect = null;
// 人员下拉数据
var personSelect = null;

$(function() {

});

define(["text!modules/opestandard/operationwork.html", "text!modules/opestandard/operationwork.css","liger-all"], function (htmlTemp, cssTemp) {
    var module = {
        init:function(){

            //获取当前菜单模块的id
            var id = arguments[0];

            $(".content-wrapper").html(htmlTemp);
            $(".css-attr").html(cssTemp);


            /**标准类型下拉数据**/
            common.callAjax('post',false,ctx + "/eam/bizDict/getValues","json",{"dict_type_code" : "operationwork_type"},function(data){
                operationworkTypeSelect = data;
            });
            /**审批状态下拉数据**/
            common.callAjax('post',false,ctx + "/eam/bizDict/getValues","json",{"dict_type_code" : "operation_approval_status"},function(data){
                approveStatusSelect = data;
            });
            /**状态下拉数据**/
            common.callAjax('post',false,ctx + "/eam/bizDict/getValues","json",{"dict_type_code" : "common"},function(data){
                statusSelect = data;
            });
            common.callAjax('post',false,common.interfaceUrl.toolsInfoData,"json",null,function(data){
                materialSelect = data;
            });
            common.callAjax('post',false,common.interfaceUrl.userExtGetAllUser,"json",null,function(data){
                personSelect = data;
            });

            /**
             * 查询表格、表单、查询区域的字段权限
             */

            $.ajax({
                type : "post",
                async : false, //同步执行
                url : ctx + "/eam/device/getfields?menuno=" + id,
                dataType : "json",
                success : function(data)
                {
                    //搜索区域赋值
                    searchformConfig = data.srchfield;
                    //初始化设备类型下拉树数据
                    $.each(searchformConfig, function (index, val) {
                        if(val.name == 'operationwork_type') {
                            val.options = {
                                data : operationworkTypeSelect
                            }
                        } else if (val.name == 'approve_status'){
                            val.options = {
                                data : approveStatusSelect
                            };
                        }
                    });

                    //列表赋值
                    operationworkTitle = data.gridfield;
                    //将需要render的列做转换（例如状态）
                    for(var i = 0;i < operationworkTitle.length;i++){
                        operationworkTitle[i].render  =  function (data) {
                            if(data == undefined || data == undefined){
                                return "";
                            }else{
                                return data;
                            }
                        };
                        if(operationworkTitle[i].data == "operationwork_status"){
                            operationworkTitle[i].render = function (data) {
                                return data == '1' ? "有效" : "无效";
                            }
                        }

                        if(operationworkTitle[i].data == "operationwork_type"){
                            operationworkTitle[i].render = function (data) {
                                if(data == '0') {
                                    return "检修标准";
                                }else if(data == '1'){
                                    return "保养标准";
                                }else {
                                    return '巡检标准';
                                }
                            }
                        }

                        if(operationworkTitle[i].data == "approve_status"){
                            operationworkTitle[i].render = function (data) {
                                if(data == '0') {
                                    return "待审批";
                                }else if(data == '1'){
                                    return "通过";
                                }else {
                                    return '退回';
                                }
                            }
                        }
                    }

                    //表单赋值
                    formConfig = data.formfield;
                }
            });



            //初始化搜索区域字段
            var searchformConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                fields :searchformConfig

            };
            /**渲染查询区域表单**/
            $("#searchForm").ligerForm(searchformConfig);
            $(".queryTable").append($("#searchForm"));
            //渲染查询按钮
            var btnHtml = "<div><input type='button' value='查询'  class='searchbtn' /><input type='button' value='重置'  class='resetbtn' /></div>";
            $("#searchForm").append(btnHtml);

            /**渲染按钮区域**/
            /**  渲染行内按钮区域 */
            common.callAjax('post',false,ctx + "/eam/button/getButtonByRole","json",{"id" : id},function(data){
                var btnHtml = "";
                for(var i = 0; i < data.length; i++) {
                    var btn = data[i];
                    if(btn.buttonno == "edit"){
                        var obj = {};
                        obj.icon = "fa-pencil";
                        obj.title = btn.buttonname;
                        obj.callBack = function(data) {
                            editId = data.id_key;
                                if(data.approve_status != '0'){   // 0-待审批 1-通过 2-退回
                                    layer.msg('只允许编辑待审批的数据！',{time: 1000,icon:7}, function (index) {
                                    });
                                    return;
                                }

                            layer.open({
                                title: '修改',
                                type: 2,
                                skin: 'layui-layer-rim', //加上边框
                                area: common.getAreaXL(), //宽高
                                closeBtn: 1, //显示关闭按钮
                                content: "/a/eam/operationwork/editUI"
                            });
                        };
                        btnInline.push(obj);
                    }else if(btn.buttonno == "detail"){
                        var obj = {};
                        obj.icon = "fa-file-text-o";
                        obj.title = btn.buttonname;
                        obj.callBack = function(data) {
                            editId = data.id_key;
                            layer.open({
                                title: '详情',
                                type: 2,
                                skin: 'layui-layer-rim', //加上边框
                                area: common.getAreaM(), //宽高
                                closeBtn: 1, //显示关闭按钮
                                content: "/a/eam/operationwork/detailUI"
                            });
                        };
                        btnInline.push(obj);
                    } else if(btn.buttonno == "delete"){
                        var obj = {};
                        obj.icon="fa-times";
                        obj.title=btn.buttonname;
                        obj.callBack=function (data) {
                            $.ajax({
                                type : "post",
                                async : false, //同步执行
                                data : {"id" : data.id_key},
                                url : ctx+"/eam/operationwork/delete",
                                dataType : "json", //传递数据形式为text
                                "success" : function(data)
                                {
                                    if (data.flag) {
                                        layer.msg(data.msg,{icon:1,time: 1000}, function (index) {
                                            $('#mytable').dataTable().fnDraw(false);
                                            layer.close(index);
                                        });
                                    } else {
                                        layer.msg(data.msg,{time: 1000,icon:2});
                                    }
                                }
                            });
                        };
                        btnInline.push(obj);
                    }

                    //渲染行外按钮渲染， 列表头上区域
                    //标准工作只有全部导出/选中导出/新增行外按钮
                    if(btn.buttonno == 'add' || btn.buttonno == 'exportall' || btn.buttonno == 'export'){
                        $("<input>",{
                            type:'button',
                            val:btn.buttonname,
                            id:btn.buttonno,
                            onclick:btn.onclickevent
                        }).appendTo($(".btnArea"));
                    }
                }
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

            //点击展示查询区域
            $(".queryBtn").on("click", function () {
                $(".queryTable").toggleClass("hide");
            });

            // 导航
            $(".guide").on("click", function () {
                $('.wrapper').animate({scrollTop:0}, '100');
                setTimeout(function () {
                    var steplist = $.WebPageGuide({showCloseButton: true});
                    steplist.newGuidStep(".queryBtn", "查询", "点击显示查询区域");
                    steplist.newGuidStep("#export", "选中导出按钮", "选中导出");
                    steplist.newGuidStep("#exportall", "全部导出按钮", "全部导出");
                    steplist.newGuidStep("#add", "新增按钮", "增加");
                    steplist.startGuide();
                },300);
            });

            var tab = common.renderTable("mytable", {
                // "order": [[3, "desc"]],
                // "bStateSave":true,
                "serverSide": true,
                "hascheckbox": true,
                "hasIndex":true,
                "opBtns": btnInline,
                "ajax": {
                    "url": common.interfaceUrl.operationworkData,
                    "dataSrc": function (json) {
                        btnInline = [];
                        $("#rowsCount").html('共'+json.recordsTotal+'条记录');
                        return json.data;
                    },
                    "data": function (param) {
                        // param.search = $("#searchForm").serializeJson();
                        param.search = liger.get("searchForm").getData();
                    }
                },
                "columns": operationworkTitle,
                "aoColumnDefs" : [
                    { "bSearchable": false, "bSortable": false, "aTargets": [4] },
                ]
            });

            //新增按钮
            $("#add").on("click", function () {

                layer.open({
                    title: '新增',
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getAreaXL(), //宽高
                    // area: common.getArea(),
                    closeBtn: 1, //显示关闭按钮
                    content: ctx + "/eam/operationwork/addUI"
                });
            });

            //导出
            $("#export").on("click",function () {
                if(tab.getCheckedRow().length == 0){
                    layer.msg("请选择一条数据！",{time: 1000,icon:7});
                    return;
                }
                var ids = "";
                for(var i=0;i<tab.getCheckedRow().length;i++){
                    ids += tab.getCheckedRow()[i].id_key+",";
                }
                window.open('/exportExcel.do?ids='+(ids.substr(0,ids.length - 1))+'&name=stand_work');
            });
            //全部导出
            $("#exportall").on("click",function () {
                window.open('/exportExcel.do?name=stand_work');
            });
        }
    };
    return module;
});


