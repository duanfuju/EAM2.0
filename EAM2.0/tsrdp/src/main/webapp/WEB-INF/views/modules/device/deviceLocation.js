//查询区域表单数据
var searchformConfig = null;
//列表标题数据
var locationTitle = null;
//新增编辑表单数据
var formConfig = null;
//行内按钮
var btnInline = [];
//当前编辑条目的id
var editId;
//查询封装对象
var searchObj={};
//设置树数据（用于子页面）
var _devLoctreeData = null;
//列表对象（DataTables）
var tab;
var _locStatusData=null;//状态数据
var _deptTreeData=null; //获取部门数据
var _menuno="";//当前页面菜单编号
var objArray=[];
var QRCodeSize="";
$(function() {

});

define(["text!modules/device/deviceLocation.html", "text!modules/device/deviceLocation.css","liger-all"], function (htmlTemp, cssTemp) {
    var module = {
        init:function(){
               _menuno = arguments[0];      //获取菜单编号入参
            //_menuno=1061;
            $(".content-wrapper").html(htmlTemp);
            $(".css-attr").html(cssTemp);
            this.initData();
            this.render(_menuno);
        },
        render:function(menuno) {
            this.initForm(menuno);//查询表格、表单、查询区域的字段权限
            this.initZtree(); //加载树
            this.initButton(menuno);//按钮
            this.initTable();//DataTables列表渲染
        },
        initData:function(){
            //状态数据
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "common"},function(data){
                _locStatusData=data;
            });
            //获取部门数据
            common.callAjax('post',false, common.interfaceUrl.getDeptTreeData,"json",null,function(data){
                _deptTreeData=data;
            });
            //树
            common.callAjax('post',false, common.interfaceUrl.getDevLocationTree,"json",null,function(data) {
                _devLoctreeData = data;
            });
        },
        initTable:function () {
             tab = common.renderTable("mytable", {
              //  "order": [[0, "desc"]],
               // "bStateSave":true,
                "serverSide": true,
                "hascheckbox": true,
                "hasIndex":true,
                "ordering":true,
                "opBtns": btnInline,
                "ajax": {
                    "url": common.interfaceUrl.devLoctionDataTablePageMap,
                    "dataSrc": function (json) {
                        btnInline = [];
                        $("#rowsCount").html('共'+json.recordsTotal+'条记录');
                        return json.data;
                    },
                    "data": function (param) {
                        //如果按照树节点搜索条件为空,则按照搜索区域条件查询
                        if(!searchObj.id){
                            //param.search=$("#searchForm").serializeJson();
                            var form = liger.get('searchForm');
                            param.search = form.getData();
                        }else{//否则，按照树节点id查询
                            param.search=searchObj;
                        }
                    }
                },

                "columns": locationTitle,
            });
        },
        initButton:function(id){
            //查询按钮隐藏
            $(".queryBtn").on("click", function () {
                $(".queryTable").toggleClass("hide");
            });
            /**  渲染行内按钮区域 */
            common.callAjax('post',false,common.interfaceUrl.getButtonByRole,"json",{"id" : id},function(data){
                var btnHtml = "";
                for(var i = 0; i < data.length; i++) {
                    var btn = data[i];
                    if(btn.buttonno == "edit"){
                        var obj = {};
                        obj.icon = "fa-pencil";
                        obj.title = btn.buttonname;
                        obj.callBack = function(data) {
                            editId = data.id_key;
                            layer.open({
                                type: 2,
                                title:'修改',
                                skin: 'layui-layer-rim', //加上边框
                                area: common.getAreaS(), //宽高
                                closeBtn: 1, //显示关闭按钮
                                content: common.interfaceUrl.devLoctionEditUI
                            });
                        };
                        btnInline.push(obj);
                    } else if(btn.buttonno == "delete"){
                        var obj = {};
                        obj.icon="fa-times";
                        obj.title=btn.buttonname;
                        obj.callBack=function (data) {
                            //判断是否状态为无效可删除数据
                            var loc_status=data.loc_status;
                            if(loc_status=="有效"){
                                layer.msg("有效数据无法删除，请先修改状态",{time: 1000,icon:7});
                                return;
                            }
                            layer.confirm('确认删掉这条数据吗？',{icon: 3, title:'提示'}, function(){
                                common.callAjax('post',false,common.interfaceUrl.devLoctionDelete,"text",{id:data.id_key},function(d){
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
                    }else if(btn.buttonno=="detail"){
                        var obj = {};
                        obj.icon="fa-file-text-o";
                        obj.title=btn.buttonname;
                        obj.callBack=function (data) {
                            editId=data.id_key;
                            layer.open({
                                type: 2,
                                title:'详情',
                                skin: 'layui-layer-rim', //加上边框
                                area: common.getAreaS(), //宽高
                                closeBtn: 1, //显示关闭按钮
                                content: common.interfaceUrl.devLoctionDetailUI
                            });
                        };
                        btnInline.push(obj);
                    }

                    //渲染行外按钮渲染， 列表头上区域
                    if(btn.buttonname!="查看详情"&&btn.buttonname!="编辑"&&btn.buttonname!="删除") {
                        $("<input>", {
                            type: 'button',
                            val: btn.buttonname,
                            id: btn.buttonno,
                            onclick: btn.onclickevent
                        }).appendTo($(".btnArea"));
                    }
                }

            });
            //查询按钮操作
            $(".searchbtn").on("click",function () {
                searchObj = {};
                $('#mytable').dataTable().fnDraw(false);

                $(".queryTable").toggleClass("hide");
                var form = liger.get('searchForm');

                console.log(form.getData());
                var queryInfo = '<li>查询条件：</li>';
                $.each(form.getData("display"),function(i, item){
                    if(item){
                        queryInfo += '<li><span>'+i+':</span><span>'+item+'</span></li>';
                    }
                });
                $('.queryInfo').html(queryInfo);

            });
            //新增按钮
            $("#add").on("click", function () {
                layer.open({
                    type: 2,
                    title:'新增',
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getAreaS(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content:common.interfaceUrl.devLoctionAddUI
                });
            });
            //编辑按钮
            $("#edit").on("click", function () {
                if(tab.getCheckedRow().length!=1){
                    layer.msg("请选择一条数据！",{time: 1000,icon:7 });
                    return;
                }
                editId = tab.getCheckedRow()[0].id_key;
                layer.open({
                    type: 2,
                    title:'修改',
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getAreaS(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content:common.interfaceUrl.devLoctionEditUI
                });
            });
            $("#delete").on("click",function () {
                if(tab.getCheckedRow().length==0){
                    layer.msg("请选择一条数据！",{time: 1000,icon:7});
                    return;
                }
                //判断是否状态为无效可删除数据
                var rows=tab.getCheckedRow();
                for(var i=0;i<rows.length;i++){
                    var loc_status=rows[i].loc_status;
                    alert(loc_status);
                    if(loc_status=="有效"){
                        layer.msg("有效数据无法删除，请先修改状态",{time: 1000,icon:7});
                        return;
                    }
                }
                layer.confirm('确认删掉这条数据吗？',{icon: 3, title:'提示'},function() {
                    var ids="";
                    for(var i=0;i<tab.getCheckedRow().length;i++){
                        ids+=tab.getCheckedRow()[i].id_key+",";
                    }
                    common.callAjax('post',false,common.interfaceUrl.devLoctionDelete,"text",{"id":ids},function(data){
                        if(data=="success"){
                            layer.msg('删除成功！',{icon: 1,time: 1000}, function (index) {
                                $('#mytable').dataTable().fnDraw(false);
                                layer.close(index);
                            });
                        }else{
                            layer.msg("删除失败！",{time: 1000,icon:2});
                        }
                    });
                }, function(){

                });

            });
            //重置按钮
            $(".resetbtn").on("click",function () {
                var form = liger.get('searchForm');
                form.setData("empty");
                $('.queryInfo').html("");
                $('#mytable').dataTable().fnDraw(false);
            });
            //导入选择文件
            $("#import").on("click",function () {
                $("#fileField").click();
            });
            //导入文件数据
            $("#fileField").on("change",function () {
                common.setExcel('devLocation');
            });
            //下载模板
            $("#download").on("click",function () {
                common.loadExcelModel("devLocation");
            });
            //导出
            $("#export").on("click",function () {
                if(tab.getCheckedRow().length==0){
                    layer.msg("请选择一条数据！",{time: 1000,icon:7});
                    return;
                }
                var ids="";
                for(var i=0;i<tab.getCheckedRow().length;i++){
                    ids+=tab.getCheckedRow()[i].id_key+",";
                }
                window.open('/exportExcel.do?ids='+(ids.substr(0,ids.length - 1))+'&name=devLocation');
            });
            //全部导出
            $("#exportall").on("click",function () {
                window.open('/exportExcel.do?name=devLocation');
            });

            //树查询
            $("#findNodeA").on("click",function () {
                var posValue=$("#posA").val();
                common.getTreeByName("locationTree",posValue);
            });
            $(".hideTree").on("click", function () {
                $(".treeContainer").toggleClass("hide");
                $(".hideTreeBtn-show").toggleClass("hideTreeBtn-hide");
            });
            $(".guide").on("click", function () {
                $('.wrapper').animate({scrollTop:0}, '100');
                setTimeout(function () {
                    var steplist = $.WebPageGuide({showCloseButton: true});
                    steplist.newGuidStep(".queryBtn", "查询", "点击显示查询区域");
                    steplist.newGuidStep(".searchbtn", "查询按钮", "请输入查询条件");
                    steplist.newGuidStep(".resetbtn", "重置按钮", "重置查询条件");
                    steplist.newGuidStep("#posA", "树查询条件", "请输入查询条件");
                    steplist.newGuidStep("#findNodeA", "树查询按钮", "点击查询");
                    steplist.newGuidStep("#download", "模板下载按钮", "模板下载");
                    steplist.newGuidStep("#import", "导入按钮", "先下载模板,后下载");
                    steplist.newGuidStep("#export", "选中导出按钮", "选中导出");
                    steplist.newGuidStep("#exportall", "全部导出按钮", "全部导出");
                    steplist.newGuidStep("#add", "新增按钮", "增加");
                    steplist.newGuidStep("#delete", "删除按钮", "先选中一条或多条数据,然后删除");
                    steplist.startGuide();
                },300);
            });
            //批量打印二维码
            $("#print").after('<div class="selectSize" hidden><li class="select-one">88cm*55cm</li><li >50cm*35cm</li><div>');
            $("#print").parent().css('position','relative');
            $("#print").on("click",function () {
                $('.selectSize').show();
                $('.selectSize').unbind("click");
                $('.selectSize').on('click','li',function(){
                    QRCodeSize = $(this).text();   //取得尺寸
                    $('.selectSize').hide();
                    if(tab.getCheckedRow().length == 0){
                        layer.msg("请选择一条数据！",{time: 1000,icon:7});
                        return false;
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
                        content:common.interfaceUrl.devLoctionQrCodePrintLocUI
                    });
                })

            });
        },
        initForm:function(id){


            /**
             * 查询表格、表单、查询区域的字段权限
             */
            var param = {};
            param.menuno = id;
            common.callAjax('post',false,common.interfaceUrl.getfields,"json",param,function(data){
                searchformConfig = data.srchfield;
                $.each(searchformConfig, function (index, val) {
                    if(val.name == 'loc_pid'){
                        val.options = {
                            isMultiSelect : true,
                            valueField : 'id',
                            valueFieldID : 'loc_pid',
                            treeLeafOnly : false,
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
                    }else if(val.name == 'loc_dept'){
                        val.options = {
                            isMultiSelect : true,
                            valueField : 'id',
                            treeLeafOnly : false,
                            tree : {
                              //  url :   common.interfaceUrl.getDeptTreeData,
                                data:_deptTreeData,
                                checkbox : true,
                                parentIcon: null,
                                childIcon: null,
                                idFieldName : 'id',
                                parentIDFieldName : 'pId',
                                nodeWidth:200,
                                ajaxType : 'post',
                                autoCheckboxEven:false,//复选框联动
                            }
                        }
                    }else  if(val.name == 'loc_status') {
                        val.options = {
                            data : _locStatusData
                        }
                    }

                });


                //列表赋值
                locationTitle = data.gridfield;
                //将需要render的列表做转换（eg:状态）
                for(var i = 0; i < locationTitle.length; i++) {
                    if(_deptTreeData!=null){
                        if(locationTitle[i].data == 'loc_dept'){
                            locationTitle[i].render = function (data) {
                                var returnData="";//返回字符串
                                if (data==null||data==undefined){
                                    return "";
                                }
                                var data=data.split(",");
                                for (var i=0;i<data.length;i++){
                                    $.each(_deptTreeData,function(j,n){
                                        if(data[i]==n.id){
                                            returnData+=n.name+";";
                                        }
                                    })
                                }
                                returnData=returnData.substring(0,returnData.length-1);
                                return returnData;
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
        },
        initZtree:function(){

            function treenodeClick(event, treeId, treeNode, clickFlag) {
                if(treeNode.pId!=0){
                    searchObj.id=treeNode.id;
                }else{
                    searchObj={};
                }
                $('#mytable').dataTable().fnDraw(false);
            }
            /**ztree数据初始化**/
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
                    onClick: treenodeClick
                },
                view: {
                    fontCss:  function (treeId, treeNode) {
                        return (treeNode.highlight ? {"color":"#A60000", "font-weight":"bold"} : {"color":"#333", "font-weight":"normal"});
                    }
                }
            };
          //  $.ajaxSettings.async = false;   //同步加载
            var locationTree;
            // 初始化树结构
            locationTree = $.fn.zTree.init($("#locationTree"), setting, _devLoctreeData);
        },
    };
    return module;
});
//子页面刷新父页面
window.RefreshPage = function () {
  common.loadModule("modules/device/deviceLocation",_menuno);
};
