//查询区域表单数据
var searchformConfig = null;
//列表标题数据
var deviceTitle = null;
//新增编辑表单数据
var formConfig = null;
//行内按钮
var btnInline = [];
//当前编辑条目的id
var editId;
//查询封装对象
var searchObj = {};
//设备类别下拉框数据
var devLevelSelect = null;
//设备状态下拉框数据
var devStatusSelect = null;
//设备二维码下拉数据
var devQrcodeStatusSelect = null;
//设备专业的下拉数据
var devMajorSelect = null;
//供应商和维护商的下拉数据
var supplierSelect = null;
//归属负责人下拉数据
var empSelect = null;
// 选择的打印二维码数据
var objArray = [];

var id = "";//当前页面菜单编号

define(["text!modules/device/device.html", "text!modules/device/device.css","liger-all"], function (htmlTemp, cssTemp) {
    layer.closeAll('loading');
    var module = {

        init:function(){
            id = arguments[0];      //获取菜单编号入参

            $(".content-wrapper").html(htmlTemp);
            $(".css-attr").html(cssTemp);
            /**ztree设备类别设备树数据初始化**/
            var zTree;
            var demoIframe;

            var setting = {
                view: {
                    dblClickExpand: false,
                    showLine: true,
                    selectedMulti: false
                },
                data: {
                    simpleData: {
                        enable:true,
                        idKey: "id",
                        pIdKey: "pId",
                        rootPId: ""
                    }
                },
                callback: {
                    onClick: function(event, treeId, treeNode, clickFlag) {
                        //该树是设备类别以及该设备类别下的设备组成的一个树，所以既有设备类别信息又有设备信息
                        if(treeNode.type == "device"){
                            //当选中的树节点类型为设备时，要根据设备的id去查询，searchObj为全局变量，因此要清除之前选中的信息
                            searchObj.dev_category = null;
                            searchObj.id_key = treeNode.id;
                        } else {
                            //选中的树节点类型是设备类别时，根据设备类别id，查询该设备类别以及其子节点下的设备信息
                            searchObj.id_key = null;
                            searchObj.dev_category = treeNode.id;
                        }
                        $('#mytable').dataTable().fnDraw(false);
                    }
                },
                view: {
                    fontCss:  function (treeId, treeNode) {
                        return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
                    }
                }
            };

            //$.ajaxSettings.async = false;   //同步加载
            var deviceTree;
            // 初始化设备类别和设备信息树结构
            function refreshTree(){
                common.callAjax('post',false,ctx + "/eam/device/categoryTreeData","json",null,function(data){
                    deviceTree = $.fn.zTree.init($("#deviceTree"), setting, data);
                });
            }
            refreshTree();

            //设备的重要程度下拉数据获取
            common.callAjax('post',false,ctx + "/eam/bizDict/getValues","json",{"dict_type_code" : "dev_level"},function(data){
                devLevelSelect = data;
            });
            // 设备信息状态下拉数据获取
            common.callAjax('post',false,ctx + "/eam/bizDict/getValues","json",{"dict_type_code" : "common"},function(data){
                devStatusSelect = data;
            });
            //设备的二维码状态下拉数据获取
            common.callAjax('post',false,ctx + "/eam/bizDict/getValues","json",{"dict_type_code" : "dev_qrcode_status"},function(data){
                devQrcodeStatusSelect = data;
            });
            //设备专业下拉数据获取
            common.callAjax('post',false,ctx + "/eam/bizDict/getValues","json",{"dict_type_code" : "dev_major"},function(data){
                devMajorSelect = data;
            });
            //供应商和维护商的下拉数据获取
            common.callAjax('post',false,ctx + "/supplier/supplier/getSupplierList","json",null,function(data){
                supplierSelect = data;
            });
            //归属负责人的下拉数据获取
            common.callAjax('post',true,common.interfaceUrl.userExtGetAllUser,"json",null,function(data){
                empSelect = data;
            });

            /**
             * 查询表格、表单、查询区域的字段权限
             */
            var param = {};
            param.menuno = id;
            common.callAjax('post',false,ctx + "/eam/device/getfields","json",param,function(data){
                searchformConfig = data.srchfield;

                //初始化设备类型下拉树数据
                $.each(searchformConfig, function (index, val) {
                    if(val.name == 'dev_location') {
                        val.options = {
                            isMultiSelect: true,
                            valueField: 'id',
                            valueFieldID : 'dev_location',
                            treeLeafOnly: false,
                            tree: {
                                url: common.interfaceUrl.getDevLocationTree,
                                checkbox: false,
                                parentIcon: null,
                                childIcon: null,
                                idFieldName: 'id',
                                parentIDFieldName: 'pId',
                                nodeWidth: 200,
                                ajaxType: 'post',
                                textFieldName: 'name',//文本字段名，默认值text
                                onClick: function (note) {
                                }
                            }
                        }
                    } else if (val.name == 'dev_level'){
                        val.options = {
                            data : devLevelSelect
                        };
                    } else if (val.name == 'dev_major'){
                        val.options = {
                            data : devMajorSelect
                        };
                    } else if (val.name == 'dev_supplier'){
                        val.options = {
                            data : supplierSelect
                        };
                    }else if (val.name == 'dev_status'){
                        val.options = {
                            data : devStatusSelect
                        };
                    }
                });

                //列表赋值
                deviceTitle = data.gridfield;
                //将需要render的列表做转换（eg:状态）
                for(var i = 0; i < deviceTitle.length; i++) {
                    deviceTitle[i].render = function (data) {
                        if(data == undefined || data == undefined){
                            return "";
                        }else{
                            return data;
                        }
                    };
                    if(deviceTitle[i].data == 'dev_status') {
                        deviceTitle[i].render = function (data) {
                            if(data == '1') {
                                return "有效";
                            } else {
                                return "无效";
                            }
                        }
                    } else if(deviceTitle[i].data == 'dev_qrcode_status'){
                        deviceTitle[i].render = function (data) {
                            if(data == '1') {
                                return "已张贴";
                            } else {
                                return "未张贴";
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
                                title: '详情',
                                type: 2,
                                skin: 'layui-layer-rim', //加上边框
                                area: common.getAreaXL(), //宽高
                                closeBtn: 1, //显示关闭按钮
                                content: "/a/eam/device/detailUI"
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
                                title: '修改',
                                type: 2,
                                skin: 'layui-layer-rim', //加上边框
                                area: common.getAreaXL(), //宽高
                                closeBtn: 1, //显示关闭按钮
                                content: "/a/eam/device/editUI"
                            });
                        };
                        btnInline.push(obj);
                    } else if(btn.buttonno == "delete"){
                        var obj = {};
                        obj.icon="fa-times";
                        obj.title=btn.buttonname;
                        obj.callBack=function (data) {
                            common.callAjax('post',false,ctx + "/eam/device/delete","json",{"id" : data.id_key},function(data){
                                if(data.flag){
                                    layer.msg(data.msg,{icon:1,time: 1000}, function(index){
                                        $('#mytable').dataTable().fnDraw(false);
                                        layer.close(index);
                                    });
                                }else{
                                    layer.msg(data.msg,{time: 1000,icon:7});
                                }
                            });
                        };
                        btnInline.push(obj);
                    }

                    //渲染行外按钮渲染， 列表头上区域
                    if(btn.buttonno == 'add' || btn.buttonno == 'import' || btn.buttonno == 'exportall' || btn.buttonno == 'export'
                        || btn.buttonno == 'download' || btn.buttonno == 'print'){
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
                //下拉状态赋值
                $("input[name='dev_level']").val($("#dev_levelBox").val());
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

            //树查询
            $("#findNodeA").on("click",function () {
                var posValue = $("#posA").val();
                common.getTreeByName("deviceTree",posValue);
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
                    "url": common.interfaceUrl.deviceList,
                    "dataSrc": function (json) {
                        btnInline = [];
                        $("#rowsCount").html('共'+json.recordsTotal+'条记录');
                        return json.data;
                    },
                    "data": function (param) {
                        //如果按照树节点搜索条件为空,则按照搜索区域条件查询
                        if(!searchObj.dev_category && !searchObj.id_key){
                            // param.search=$("#searchForm").serializeJson();
                            param.search = liger.get("searchForm").getData();
                        }else{//否则，按照树节点id查询
                            param.search=searchObj;
                        }
                        console.log(param);
                    }
                },

                "columns": deviceTitle,
                "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                    //新增省略号
                    aData.dev_name = aData.dev_name || "";
                    aData.dev_code = aData.dev_code || "";
                    if (aData.dev_name.length > 8) {
                        $('td:eq(4)', nRow).html( '<li title="'+aData.dev_name +'" data-code="' + aData.dev_name + '">'+aData.dev_name.substr(0, 8)+'...</li>');
                    }else{
                        $('td:eq(4)', nRow).html( '<li title="'+aData.dev_name +'" data-code="' + aData.dev_name + '">'+aData.dev_name+'</li>');
                    }
                    if (aData.dev_code.length > 8) {
                        $('td:eq(3)', nRow).html( '<li title="'+aData.dev_code +'" data-code="' + aData.dev_code + '">'+aData.dev_code.substr(0, 8)+'...</li>');
                    }else{
                        $('td:eq(3)', nRow).html( '<li title="'+aData.dev_code +'" data-code="' + aData.dev_code + '">'+aData.dev_code+'</li>');
                    }
                }
            });

            //新增按钮
            $("#add").on("click", function () {
                layer.open({
                    title: '新增',
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getAreaXL(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: ctx+"/eam/device/addUI"
                });
            });

            //导入选择文件
            $("#import").on("click",function () {
                $("#fileField").click();
            });
            //导入文件数据
            $("#fileField").on("change",function () {
                common.setExcel('device');
            });
            //下载模板
            $("#download").on("click",function () {
                common.loadExcelModel("device");
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
                window.open('/exportExcel.do?ids='+(ids.substr(0,ids.length - 1))+'&name=device');
            });
            //全部导出
            $("#exportall").on("click",function () {
                window.open('/exportExcel.do?name=device');
            });

            //批量打印二维码
            $("#print").on("click",function () {
                if(tab.getCheckedRow().length == 0){
                    layer.msg("请选择一条数据！",{time: 1000,icon:7});
                    return;
                }
                objArray = [];
                for(var i=0;i<tab.getCheckedRow().length;i++){
                    objArray.push(tab.getCheckedRow()[i]);
                }
                layer.open({
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['450px', '650px'], //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: ctx+"/eam/device/qrCodePrintUI"
                });
            });


            $(".hideTree").on("click", function () {
                $(".treeContainer").toggleClass("hide");
                $(".hideTreeBtn-show").toggleClass("hideTreeBtn-hide");
            });

            // 导航
            $(".guide").on("click", function () {
                $('.wrapper').animate({scrollTop:0}, '100');
                setTimeout(function () {
                    var steplist = $.WebPageGuide({showCloseButton: true});
                    steplist.newGuidStep(".queryBtn", "查询", "点击显示查询区域");
                    steplist.newGuidStep("#posA", "树查询条件", "请输入查询条件");
                    steplist.newGuidStep("#download", "模板下载按钮", "模板下载");
                    steplist.newGuidStep("#import", "导入按钮", "先下载模板,后下载");
                    steplist.newGuidStep("#export", "选中导出按钮", "选中导出");
                    steplist.newGuidStep("#exportall", "全部导出按钮", "全部导出");
                    steplist.newGuidStep("#add", "新增按钮", "增加");
                    steplist.startGuide();
                },300);
            });
        }
    };
    return module;
});

