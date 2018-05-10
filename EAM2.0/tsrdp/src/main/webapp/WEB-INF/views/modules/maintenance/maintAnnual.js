//当前编辑条目的id
var editId;
//查询区域表单数据
var searchformConfig=null;
//列表标题数据
var tableTitle=null;
//新增编辑表单数据
var formConfig=null;
//行内按钮
var btnInline=[];
//列表对象（DataTables）
var tab;
var _statusData=null;//状态
var _approveStatus=null;//审批状态
var _maintainPeriod=null;//保养周期
var _maintainMode=null;//保养类型
var _maintainType=null;//保养分类


var _menuno=null;

$(function() {

});

define(["text!modules/maintenance/maintAnnual.html", "text!modules/employee/eamCustomer.css","liger-all"], function (htmlTemp, cssTemp) {

    var module = {
        init:function(menuno){
            _menuno = arguments[0];
            this.render(_menuno);
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

            //状态
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "common"},function(data){
                _statusData=data;
            });
            //保养类型
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "maintain_mode"},function(data){
                _maintainMode=data;
            });
            //保养分类
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "maintain_type"},function(data){
                _maintainType=data;
            });
            //保养周期
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "maintain_period"},function(data){
                _maintainPeriod=data;
            });
            //审批状态
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "APPROVE_STATUS"},function(data){
                _approveStatus=data;
            });


            //查询表格、表单、查询区域的字段权限
            common.callAjax('post',false,common.interfaceUrl.getfields,"json",param,function(data){
                //搜索区域赋值
                searchformConfig = data.srchfield;
                $.each(searchformConfig, function (index, val) {
                    if(val.name == 'status'){
                        val.options = {
                            data : _statusData
                        }
                    }else  if(val.name == 'project_mode'){
                        val.options = {
                            data : _maintainMode
                        }
                    }else  if(val.name == 'project_type'){
                        val.options = {
                            data : _maintainType
                        }
                    }else  if(val.name == 'project_period'){
                        val.options = {
                            data : _maintainPeriod
                        }
                    }else  if(val.name == 'project_status'){
                        val.options = {
                            data : _approveStatus
                        }
                    }
                });
                //列表赋值
               tableTitle = data.gridfield;
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
            //查询按钮隐藏
            $(".queryBtn").on("click", function () {
                $(".queryTable").toggleClass("hide");
            });

            /**渲染按钮区域**/
            common.callAjax('post',false,common.interfaceUrl.getButtonByRole,"json",{"id":id},function(data){
                console.log(data);
                var btnHtml="";
                for(var i=0;i<data.length;i++){
                    var btn = data[i];
                    if(btn!==null && btn!=""){
                        if(btn.buttonname=="编辑"){
                            var obj = {};
                            obj.icon="fa-pencil";
                            obj.title=btn.buttonname;
                            obj.callBack=function (data) {
                                if(data.project_status=="审批通过"){
                                    layer.msg("审批通过数据不允许修改" ,{icon:7,time: 1000});
                                    return;
                                }
                                editId=data.id_key;
                                layer.open({
                                    type: 2,
                                    title:'修改',
                                    skin: 'layui-layer-rim', //加上边框
                                    area: common.getAreaM(), //宽高
                                    closeBtn: 1, //显示关闭按钮
                                    content:common.interfaceUrl.maintainProjectEditUI
                                });
                            };
                            btnInline.push(obj);
                        }else if(btn.buttonname=="删除"){
                            var obj = {};
                            obj.icon="fa-times";
                            obj.title=btn.buttonname;
                            obj.callBack=function (data) {
                                //判断是否状态为无效可删除数据
                                var status=data.status;
                                if(status=="有效"){
                                    layer.msg("有效数据无法删除，请先修改状态",{icon:7,time: 1000});
                                    return;
                                }
                                if(data.project_status=="审批通过"){
                                    layer.msg("审批通过数据不允许修改" ,{icon:7,time: 1000});
                                    return;
                                }
                                if(data.project_status=="审批驳回"){
                                    layer.msg("审批驳回数据不允许修改" ,{icon:7,time: 1000});
                                    return;
                                }
                                if(data.project_status=="待审批"){
                                    layer.msg("待审批数据不允许修改" ,{icon:7,time: 1000});
                                    return;
                                }
                                layer.confirm('确认删掉这条数据吗？',{icon: 3, title:'提示'}, function(){
                                    common.callAjax('post',false,common.interfaceUrl.maintainProjectDelete,"text",{id:data.id_key},function(d){
                                        if(d=="success"){
                                            layer.msg('删除成功！',{icon: 1,time: 1000}, function(index){
                                                $('#mytable').dataTable().fnDraw(false);
                                                layer.close(index);
                                            });
                                        }else{
                                            layer.msg("删除失败！",{time: 1000,icon:2});
                                        }
                                    });
                                }, function(){

                                });

                            };
                            btnInline.push(obj);
                        }else  if(btn.buttonname=="查看详情"){
                            var obj = {};
                            obj.icon="fa-file-text-o";
                            obj.title=btn.buttonname;
                            obj.callBack=function (data) {
                                editId=data.id_key;
                                layer.open({
                                    type: 2,
                                    title:'详情',
                                    skin: 'layui-layer-rim', //加上边框
                                    area: common.getAreaM(), //宽高
                                    closeBtn: 1, //显示关闭按钮
                                    content: common.interfaceUrl.maintainProjectDetailUI
                                });
                            };
                            btnInline.push(obj);
                        }
                        //行外按钮渲染
                        if(btn.buttonname!="查看详情"&&btn.buttonname!="编辑"&&btn.buttonname!="删除") {
                            $("<input>", {
                                type: 'button',
                                val: btn.buttonname,
                                id: btn.buttonno,
                                onclick: btn.onclickevent
                            }).appendTo($(".btnArea"));
                        }
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

            //编辑按钮
            $("#edit").on("click", function () {
                if(tab.getCheckedRow().length!=1){
                    layer.msg("请选择一条数据！",{icon:7, time: 1000});
                    return;
                }
                editId = tab.getCheckedRow()[0].id_key;
                layer.open({
                    type: 2,
                    title:'修改',
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getAreaM(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content:common.interfaceUrl.customerEditUI
                });
            });

            //重置按钮
            $(".resetbtn").on("click",function () {
                var form = liger.get('searchForm');
                form.setData("empty");
                $('.queryInfo').html("");
                $('#mytable').dataTable().fnDraw(false);
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
                window.open('/exportExcel.do?ids='+(ids.substr(0,ids.length - 1))+'&name=maintannual');
            });
            //全部导出
            $("#exportall").on("click",function () {
                window.open('/exportExcel.do?name=maintannual');
            });

            $(".guide").on("click", function () {
                $('.wrapper').animate({scrollTop:0}, '100');
                setTimeout(function () {
                    var steplist = $.WebPageGuide({showCloseButton: true});
                    steplist.newGuidStep(".queryBtn", "查询", "点击显示查询区域");
                    steplist.newGuidStep(".searchbtn", "查询按钮", "请输入查询条件");
                    steplist.newGuidStep(".resetbtn", "重置按钮", "重置查询条件");
                    steplist.newGuidStep("#export", "选中导出按钮", "选中导出");
                    steplist.newGuidStep("#exportall", "全部导出按钮", "全部导出");
                    steplist.startGuide();
                },300);
            });
            $("#createapproval").on("click",function () {
                layer.open({
                    type: 2,
                    title:'生成',
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getAreaM(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content:common.interfaceUrl.maintainProjectApprovalUI
                });
            });
        },
        initTableForm : function () {

             tab = common.renderTable("mytable", {
                //"order": [[2, "desc"]],
               // "bStateSave":true,
                "serverSide": true,
                "hascheckbox": true,
                "hasIndex":true,
                //"ordering":true,
                "opBtns": btnInline,
                "ajax": {
                    "url": common.interfaceUrl.maintainProjectDataTablePageMap,
                    "dataSrc": function (json) {
                        btnInline=[];
                        $("#rowsCount").html('共'+json.recordsTotal+'条记录');
                        return json.data;
                    },
                    "data": function (param) {
                      //  param.search=$("#searchForm").serializeJson();
                        var form = liger.get('searchForm');
                        param.search = form.getData();
                    }
                },
                "columns": tableTitle
            });

        },
    };
    return module;
});


