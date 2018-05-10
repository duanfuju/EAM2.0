//查询区域表单数据
var searchformConfig = null;
//列表标题数据
var inspectionTaskTitle = null;
//新增编辑表单数据
var formConfig = null;
//行内按钮
var btnInline = [];
//当前编辑条目的id
var editId;
//查询封装对象
var searchObj = {};
// 巡检任务状态下拉框相互局
var inspectionStatusSelect = null;
// 是否逾期
var taskIs_overDue = null;

define(["text!modules/inspection/inspectionTask.html", "text!modules/module1/module1.css","liger-all"], function (htmlTemp, cssTemp) {
    var module = {

        init:function(){
            var id = arguments[0];      //获取菜单编号入参

            $(".content-wrapper").html(htmlTemp);
            $(".css-attr").html(cssTemp);

            // 巡检任务的状态下拉数据获取
            common.callAjax('post',false,ctx + "/eam/bizDict/getValues","json",{"dict_type_code" : "inspection_task_status"},function(data){
                inspectionStatusSelect = data;
            });
            // 设备信息状态下拉数据获取
            common.callAjax('post',false,ctx + "/eam/bizDict/getValues","json",{"dict_type_code" : "is_overdue"},function(data){
                taskIs_overDue = data;
            });

            /**
             * 查询表格、表单、查询区域的字段权限
             */
            var param = {};
            param.menuno = id;
            common.callAjax('post',false,ctx + "/eam/inspectionTask/getfields","json",param,function(data){
                searchformConfig = data.srchfield;

                //初始化设备类型下拉树数据
                $.each(searchformConfig, function (index, val) {
                    if (val.name == 'task_status'){
                        val.options = {
                            data : inspectionStatusSelect
                        };
                    } else if(val.name == 'task_isoverdue') {
                        val.options = {
                            data : taskIs_overDue
                        };
                    }
                });

                //列表赋值
                inspectionTaskTitle = data.gridfield;
                //将需要render的列表做转换（eg:状态）
                for(var i = 0; i < inspectionTaskTitle.length; i++) {
                    inspectionTaskTitle[i].render = function (data) {
                        if(data == undefined || data == undefined){
                            return "";
                        }else{
                            return data;
                        }
                    };
                    if(inspectionTaskTitle[i].data == 'task_isoverdue') {
                        inspectionTaskTitle[i].render = function (data) {
                            if(data == '1') {
                                return "是";
                            } else {
                                return "否";
                            }
                        }
                    }
                    if(inspectionTaskTitle[i].data == 'task_status') {
                        inspectionTaskTitle[i].render = function (data) {
                            if(data == '0') {
                                return "待确认";
                            } else if(data == '1'){
                                return "进行中";
                            } else if(data == '2'){
                                return "已完成";
                            } else if(data == '3'){
                                return "申请挂起";
                            } else if(data == '4'){
                                return "申请转单";
                            } else if(data == '5'){
                                return "已挂起";
                            } else if(data == '6'){
                                return "申请撤销";
                            } else if(data == '7'){
                                return "已撤销";
                            } else {
                                return "已作废";
                            }
                        }
                    }
                }
                // 表单赋值
                formConfig = data.formfield;
            });

            // 初始化搜索区域字段
            var searchformConfig = {
                space : 50, labelWidth : 120, inputWidth : 200,
                fields : searchformConfig
            };

            /**渲染查询区域表单**/
            $("#searchForm").ligerForm(searchformConfig);
            $(".queryTable").append($("#searchForm"));
            //渲染查询按钮
            var btnHtml = "<div><input type='button' value='查询'  class='searchbtn' /><input type='button' value='重置'  class='resetbtn' /></div>";
            $("#searchForm").append(btnHtml);

            /**  渲染行内按钮区域 */
            common.callAjax('post',false,ctx + "/eam/button/getButtonByRole","json",{"id" : id},function(data){
                var btnHtml = "";
                for(var i = 0; i < data.length; i++) {
                    var btn = data[i];
                    if(btn.buttonno == "detail"){
                        var obj = {};
                        obj.icon = "fa-file-text-o";
                        obj.title = btn.buttonname;
                        obj.callBack = function(data) {
                            editId = data.id_key;
                            layer.open({
                                title: '详情信息',
                                type: 2,
                                skin: 'layui-layer-rim', //加上边框
                                area: common.getAreaXL(), //宽高
                                closeBtn: 1, //显示关闭按钮
                                content: "/a/eam/inspectionTask/detailUI"
                            });
                        };
                        btnInline.push(obj);
                    }else if(btn.buttonno == "edit"){
                        var obj = {};
                        obj.icon = "fa-pencil";
                        obj.title = btn.buttonname;
                        obj.callBack = function(data) {
                            editId = data.id_key;
                            layer.open({
                                type: 2,
                                skin: 'layui-layer-rim', //加上边框
                                area: common.getAreaXL(), //宽高
                                closeBtn: 1, //显示关闭按钮
                                content: "/a/eam/device/editUI"
                            });
                        };
                        btnInline.push(obj);
                    }
                    //渲染行外按钮渲染， 列表头上区域
                    if(btn.buttonno == 'exportall' || btn.buttonno == 'export'){
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
                //根据按钮查询前，先清空树节点查询条件
                searchObj = {};
                $('#mytable').dataTable().fnDraw(false);
                $(".queryTable").toggleClass("hide");
                var form = liger.get('searchForm');
                var queryInfo = '<li>查询条件：</li>';
                $.each(form.getData("display"),function(i, item){
                    if(item){
                        queryInfo += '<li><span>' + i + ':</span><span>' + item + '</span></li>';
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

            var tab = common.renderTable("mytable", {
                // "order": [[1, "desc"]],
                // "bStateSave":true,
                "serverSide": true,
                "hascheckbox": true,
                "hasIndex":true,
                "ordering":true,
                "opBtns": btnInline,
                "ajax": {
                    "url": common.interfaceUrl.inspectionTaskData,
                    "dataSrc": function (json) {
                        btnInline = [];
                        $("#rowsCount").html('共'+json.recordsTotal+'条记录');
                        return json.data;
                    },
                    "data": function (param) {
                        //如果按照树节点搜索条件为空,则按照搜索区域条件查询
                        param.search = liger.get("searchForm").getData();
                        console.log(param);
                    }
                },
                "columns": inspectionTaskTitle,
                "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                    //新增省略号
                    aData.dev_locname = aData.dev_locname || "";
                    aData.dev_name = aData.dev_name || "";
                    if (aData.dev_name && aData.dev_name.length > 8) {
                        $('td:eq(5)', nRow).html( '<li title="'+aData.dev_name +'" data-code="' + aData.dev_name + '">'+aData.dev_name.substr(0, 8)+'...</li>');
                    }else{
                        $('td:eq(5)', nRow).html( '<li title="'+aData.dev_name +'" data-code="' + aData.dev_name + '">'+aData.dev_name +'</li>');
                    }
                    if (aData.dev_locname && aData.dev_locname.length > 8) {
                        $('td:eq(11)', nRow).html( '<li title="'+aData.dev_locname +'" data-code="' + aData.dev_locname + '">'+aData.dev_locname.substr(0, 8)+'...</li>');
                    }else{
                        $('td:eq(11)', nRow).html( '<li title="'+aData.dev_locname +'" data-code="' + aData.dev_locname + '">'+aData.dev_locname +'</li>');
                    }
                },
                "aoColumnDefs" : [
                    { "bSearchable": false, "bSortable": false, "aTargets": [ 5 ] },
                    { "bSearchable": false, "bSortable": false, "aTargets": [ 11 ] }
                ]
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
                window.open('/exportExcel.do?ids='+(ids.substr(0,ids.length - 1))+'&name=inspectiontask');
            });
            //全部导出
            $("#exportall").on("click",function () {
                window.open('/exportExcel.do?name=inspectiontask');
            });

            // 导航
            $(".guide").on("click", function () {
                $('.wrapper').animate({scrollTop:0}, '100');
                setTimeout(function () {
                    var steplist = $.WebPageGuide({showCloseButton: true});
                    steplist.newGuidStep(".queryBtn", "查询", "点击显示查询区域");
                    steplist.newGuidStep("#export", "选中导出按钮", "选中导出");
                    steplist.newGuidStep("#exportall", "全部导出按钮", "全部导出");
                    steplist.startGuide();
                },300);
            });
        }
    };
    return module;
});


