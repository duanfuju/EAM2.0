//查询区域表单数据
var searchformConfig = null;
//列表标题数据
var bizDictTitle = null;
//新增编辑表单数据
var formConfig = null;
//行内按钮
var btnInline = [];
//当前编辑条目的id
var editId;
//查询封装对象
var searchObj = {};
// 表格树初始化数据
var dictTreeData;
var id;  //菜单编号

$(function() {

});

function initTree(setting) {
    //请求物料类别树数据
    var dictTree;
    $.ajax({
        type : "post",
        async : false, //同步执行
        url : ctx+"/eam/bizDict/treeData",
        dataType : "json",
        success : function(data)
        {
            dictTree = data;
        }
    });
    $.fn.zTree.init($("#dictTree"), setting, dictTree);
}

define(["text!modules/eamsys/sysConfig/businessDict.html", "text!modules/eamsys/sysConfig/businessDict.css","liger-all"], function (htmlTemp, cssTemp) {
    var module = {

        init:function(){
            id = arguments[0];      //获取菜单编号入参

            $(".content-wrapper").html(htmlTemp);
            $(".css-attr").html(cssTemp);

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
                        searchObj.id = treeNode.id;
                        $('#mytable').dataTable().fnDraw(false);
                    }
                },
                view: {
                    fontCss:  function (treeId, treeNode) {
                        return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
                    }
                }
            };

            //树的初始化
            initTree(setting);

            /**
             * 获取业务字典表格树数据
             */
            common.callAjax('post',false,ctx + "/eam/bizDict/getBizDictTree","json",null,function(data){
                dictTreeData = {Rows: data};
            });

            /**
             * 查询表格、表单、查询区域的字段权限
             */
            $.ajax({
                type : "post",
                async : false,    //同步执行
                data : {"menuno" : id},
                url : common.interfaceUrl.getfields,
                dataType : "json",
                success : function(data) {
                    searchformConfig = data.srchfield;

                    //列表赋值
                    bizDictTitle = data.gridfield;
                    //将需要render的列表做转换（eg:状态）
                    for(var i = 0; i < bizDictTitle.length; i++) {
                        bizDictTitle[i].render = function (data) {
                            if(data == undefined || data == undefined){
                                return "";
                            }else{
                                return data;
                            }
                        }
                        if(bizDictTitle[i].data == 'dict_status') {
                            bizDictTitle[i].render = function (data) {
                                if(data == '1') {
                                    return "已作废";
                                }
                            }
                        }
                    }
                    // 表单赋值
                    formConfig = data.formfield;
                }
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
            $.ajax({
                    type : "post",
                    async : false,      //同步执行
                    url : ctx + "/eam/button/getButtonByRole?id=" + id,
                    dataType : "json",
                    success : function(data){
                        var btnHtml = "";
                        for(var i = 0; i < data.length; i++) {
                            var btn = data[i];
                            if(btn.buttonno == "detail"){
                                var obj = {};
                                obj.icon = btn.icon;
                                obj.title = btn.buttonname;
                                obj.callBack = function(data) {
                                    editId = data.id_key;
                                    layer.open({
                                        title: '详情',
                                        type: 2,
                                        skin: 'layui-layer-rim', //加上边框
                                        area: common.getAreaXS(), //宽高
                                        closeBtn: 1, //显示关闭按钮
                                        content: "/a/eam/bizDict/detailUI"
                                    });
                                };
                                btnInline.push(obj);
                            } else if(btn.buttonno == "edit"){
                                var obj = new Object();
                                obj.icon = btn.icon;
                                obj.title = btn.buttonname;
                                obj.callBack = function(data) {
                                    editId = data.id_key;
                                    layer.open({
                                        type: 2,
                                        skin: 'layui-layer-rim', //加上边框
                                        area: common.getAreaXS(), //宽高
                                        closeBtn: 1, //显示关闭按钮
                                        content: "/a/eam/bizDict/editUI"
                                    });
                                }
                                btnInline.push(obj);
                            }else if(btn.buttonno=="closed"){
                                var obj = {};
                                obj.icon= btn.icon;
                                obj.title=btn.buttonname;
                                obj.callBack=function (data) {
                                    if(data.dict_status == '1') {
                                        layer.msg("数据已作废，不可再操作",{time: 1000,icon:2});
                                    } else {
                                        layer.confirm('确认申请作废这条数据吗？',{icon: 3, title:'提示'},function() {
                                            common.callAjax('post',false,ctx + "/eam/bizDict/close","json",{"id" : data.id_key},function(data){
                                                if(data.flag){
                                                    layer.msg(data.msg,{icon:1,time: 1000}, function(index){
                                                        RefreshPages();
                                                        layer.close(index);
                                                    });
                                                }else{
                                                    layer.msg(data.msg,{time: 1000,icon:2});
                                                }
                                            });
                                        });
                                    }
                                };
                                btnInline.push(obj);
                            }

                            //渲染行外按钮渲染， 列表头上区域
                            if(btn.buttonno == 'add'){
                                $("<input>",{
                                    type:'button',
                                    val:btn.buttonname,
                                    id:btn.buttonno,
                                    onclick:btn.onclickevent
                                }).appendTo($(".btnArea"));
                            }
                        }
                    }
                }
            );
            //查询按钮操作
            $(".searchbtn").on("click",function () {
                searchObj = {};
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

            //树查询
            $("#findNodeA").on("click",function () {
                var posValue = $("#posA").val();
                common.getTreeByName("dictTree",posValue);
            });

            //新增按钮
            $("#add").on("click", function () {
                layer.open({
                    type: 2,
                    title:'新增',
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getAreaXS(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content:common.interfaceUrl.businessDictAddUI
                });
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


            $(".hideTree").on("click", function () {
                $(".treeContainer").toggleClass("hide");
            });

            // 页面效果渲染
            var tab = common.renderTable("mytable", {
                "order": [[1, "desc"]],
                "bStateSave":true,
                "serverSide": true,
                "hascheckbox": true,
                "hasIndex":true,
                "ordering":true,
                "opBtns": btnInline,
                "ajax": {
                    "url": common.interfaceUrl.businessDictList,
                    "dataSrc": function (json) {
                        btnInline = [];
                        $("#rowsCount").html('共'+json.recordsTotal+'条记录');
                        return json.data;
                    },
                    "data": function (param) {
                        //如果按照树节点搜索条件为空,则按照搜索区域条件查询
                        if(!searchObj.id){
                            param.search = liger.get("searchForm").getData();

                        }else{//否则，按照树节点id查询
                            param.search=searchObj;
                        }
                    }
                },

                "columns": bizDictTitle,
            });


            $(".guide").on("click", function () {
                $('.wrapper').animate({scrollTop:0}, '100');
                setTimeout(function () {
                    var steplist = $.WebPageGuide({showCloseButton: true});
                    steplist.newGuidStep(".queryBtn", "查询", "点击显示查询区域");
                    steplist.newGuidStep(".searchbtn", "查询按钮", "请输入查询条件");
                    steplist.newGuidStep(".resetbtn", "重置按钮", "重置查询条件");
                    steplist.newGuidStep("#add", "新增按钮", "增加");
                    steplist.startGuide();
                },300);
            });
        }
    }
    return module;
});

//子页面刷新父页面
window.RefreshPagesd = function () {
    common.loadModule("modules/eamsys/sysConfig/businessDict",id);
};
