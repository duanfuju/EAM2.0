//查询区域表单数据
var searchformConfig = null;
//列表标题数据
var categoryTitle = null;
//新增编辑表单数据
var formConfig = null;
//行内按钮
var btnInline = [];
//当前编辑条目的id
var editId;
//查询封装对象
var searchObj={};
//设备类别状态下拉数据
var catStatusSelect = null;
//菜单id
var id;

$(function() {

});

define(["text!modules/device/deviceCategory.html", "text!modules/device/deviceCategory.css","liger-all"], function (htmlTemp, cssTemp) {
    var module = {

        init:function(){
            id = arguments[0];      //获取菜单编号入参

            $(".content-wrapper").html(htmlTemp);
            $(".css-attr").html(cssTemp);
            /**ztree数据初始化**/
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
                        debugger;
                        searchObj.cat_id = treeNode.id;
                        $('#mytable').dataTable().fnDraw(false);
                    }
                },
                view: {
                    fontCss:  function (treeId, treeNode) {
                        return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
                    }
                }
            };

           // $.ajaxSettings.async = false;   //同步加载
            var categoryTree;
            // 初始化树结构
            function refreshTree(){
                common.callAjax('post',false,ctx + "/eam/devCategory/treeData","json",null,function(data){
                    categoryTree = $.fn.zTree.init($("#categoryTree"), setting, data);
                });

            }
            refreshTree();

            // //新增zTree异步加载
            // var setting = {
            //     view: {
            //         dblClickExpand: false,
            //         showLine: true,
            //         selectedMulti: false
            //     },
            //     data: {
            //         simpleData: {
            //             enable:true,
            //             idKey: "id",
            //             pIdKey: "pid",
            //             rootPId: ""
            //         }
            //     },
            //     callback: {
            //         onClick:function(event, treeId, treeNode, clickFlag){
            //             //console.log(treeId);
            //              searchObj.cat_id = treeNode.id;
            //              $('#mytable').dataTable().fnDraw(false);
            //         },
            //         view: {
            //              fontCss:  function (treeId, treeNode) {
            //                  return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
            //              }
            //         }
            //         // ,
            //         // onAsyncSuccess : ztreeOnAsyncSuccess,
            //         // onAsyncError: zTreeOnAsyncError,
            //         // beforeClick:beforeClick //捕获单击节点之前的事件回调函数
            //     },
            //     async:{
            //         enable:true,
            //         type:"post",
            //         autoParam: ["id"],
            //         url:ctx + "/eam/devCategory/treeData"
            //     }
            // };

            // $.fn.zTree.init($("#categoryTree"), setting);
            // function zTreeOnAsyncError(event, treeId, treeNode){ alert("异步加载失败!"); }
            // function ztreeOnAsyncSuccess(event, treeId, treeNode, msg){ }
            // function beforeClick(treeId, treeNode, clickFlag) {
            //     if(treeNode.isParent)
            //         return;
            //     var url = ctx + "/eam/devCategory/treeData?extId=";
            //     if(treeNode == undefined){
            //         url += "1";
            //     }
            //     else{
            //         url += treeNode.id;
            //     }
            //     $.ajax({
            //         type : "post",
            //         url : url,
            //         data : "",
            //         dataType : "json",
            //         async : true,
            //         success : function(data) {
            //                 var treeObj = $.fn.zTree.getZTreeObj("categoryTree");
            //                 // var data = jsonData.unitList;
            //                 if(data != null && data.length != 0){
            //                     if(treeNode == undefined){
            //                         treeObj.addNodes(null,data,true);// 如果是根节点，那么就在null后面加载数据
            //                     }
            //                     else{
            //                         treeObj.addNodes(treeNode,data,true);//如果是加载子节点，那么就是父节点下面加载
            //                     }
            //                 }
            //                 treeObj.expandNode(treeNode,true, false, false);// 将新获取的子节点展开
            //         },
            //         error : function() {
            //             alert("请求错误！");
            //         }
            //     });
            // }

            // 设备类别状态下拉数据获取
            common.callAjax('post',false,ctx + "/eam/bizDict/getValues","json",{"dict_type_code" : "common"},function(data){
                catStatusSelect = data;
            });


            /**
             * 查询表格、表单、查询区域的字段权限
             */
            var param = {};
            param.menuno = id;
            common.callAjax('post',false,ctx + "/eam/devCategory/getfields","json",param,function(data){
                searchformConfig = data.srchfield;
                $.each(searchformConfig, function (index, val) {
                    if(val.name == 'cat_status') {
                        val.options = {
                            data : catStatusSelect
                        }
                    }
                });
                //列表赋值
                categoryTitle = data.gridfield;
                //将需要render的列表做转换（eg:状态）
                for(var i = 0; i < categoryTitle.length; i++) {
                    categoryTitle[i].render = function (data) {
                        if(data == undefined || data == null){
                            return "";
                        }else{
                            return data;
                        }
                    };
                    if(categoryTitle[i].data == 'cat_status') {
                        categoryTitle[i].render = function (data) {
                            if(data == '1') {
                                return "有效";
                            } else {
                                return "无效";
                            }
                        }
                    }
                }
                // 表单赋值
                formConfig = data.formfield;
            });

            //渲染查询按钮
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

            debugger;
            /**  渲染行内按钮区域 */
            common.callAjax('post',false,ctx + "/eam/button/getButtonByRole","json",{"id" : id},function(data){
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
                                content: "/a/eam/devCategory/detailUI"
                            });
                        };
                        btnInline.push(obj);
                    } else if(btn.buttonno == "edit"){
                        var obj = {};
                        obj.icon = btn.icon;
                        obj.title = btn.buttonname;
                        obj.callBack = function(data) {
                            editId = data.id_key;
                            layer.open({
                                title: '修改',
                                type: 2,
                                skin: 'layui-layer-rim', //加上边框
                                area: common.getAreaXS(), //宽高
                                closeBtn: 1, //显示关闭按钮
                                content: "/a/eam/devCategory/editUI"
                            });
                        };
                        btnInline.push(obj);
                    } else if(btn.buttonno == "delete"){
                        var obj = {};
                        obj.icon=btn.icon;
                        obj.title=btn.buttonname;
                        obj.callBack=function (data) {
                            common.callAjax('post',false,ctx + "/eam/devCategory/delete","json",{"id" : data.id_key, "cat_status" : data.cat_status},function(data){
                                if(data.flag){
                                    layer.msg(data.msg,{icon:1,time: 1000}, function(index){
                                        $('#mytable').dataTable().fnDraw(false);
                                        layer.close(index);
                                    });
                                }else{
                                    layer.msg(data.msg,{time: 1000,icon:2});
                                }
                            });
                        };
                        btnInline.push(obj);
                    }

                    console.log(btn);
                    //渲染行外按钮渲染， 列表头上区域
                    if(btn.buttonno == 'add' || btn.buttonno == 'import' || btn.buttonno == 'export'
                        || btn.buttonno == 'exportall' || btn.buttonno == 'download'){
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
                common.getTreeByName("categoryTree",posValue);
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
                    steplist.newGuidStep("#posA", "树查询条件", "请输入查询条件");
                    steplist.newGuidStep("#download", "模板下载按钮", "模板下载");
                    steplist.newGuidStep("#import", "导入按钮", "先下载模板,后下载");
                    steplist.newGuidStep("#export", "选中导出按钮", "选中导出");
                    steplist.newGuidStep("#exportall", "全部导出按钮", "全部导出");
                    steplist.newGuidStep("#add", "新增按钮", "增加");
                    steplist.startGuide();
                },300);
            });

            var tab = common.renderTable("mytable", {
                // "order": [[2, "desc"]],
                // "bStateSave":true,
                "serverSide": true,
                "hascheckbox": true,
                "hasIndex":true,
                "ordering":true,
                "opBtns": btnInline,
                "ajax": {
                    "url": common.interfaceUrl.devCategoryList,
                    "dataSrc": function (json) {
                        btnInline = [];
                        $("#rowsCount").html('共'+json.recordsTotal+'条记录');
                        return json.data;
                    },
                    "data": function (param) {
                        //如果按照树节点搜索条件为空,则按照搜索区域条件查询
                        if(!searchObj.cat_id){
                            // param.search=$("#searchForm").serializeJson();
                            param.search = liger.get("searchForm").getData();

                        }else{//否则，按照树节点id查询
                            param.search=searchObj;
                        }
                    }
                },

                "columns": categoryTitle,
            });

            //新增按钮
            $("#add").on("click", function () {
                layer.open({
                    title: '新增',
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getAreaXS(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: ctx+"/eam/devCategory/addUI"
                });
            });

            //导入选择文件
            $("#import").on("click",function () {
                $("#fileField").click();
            });
            //导入文件数据
            $("#fileField").on("change",function () {
                common.setExcel('devCategory');
            });
            //下载模板
            $("#download").on("click",function () {
                common.loadExcelModel("devCategory");
            });
            //导出
            $("#export").on("click",function () {
                debugger;
                if(tab.getCheckedRow().length == 0){
                    layer.msg("请选择一条数据！",{time: 1000,icon:7});
                    return;
                }
                var ids = "";
                for(var i=0;i<tab.getCheckedRow().length;i++){
                    ids += tab.getCheckedRow()[i].id_key+",";
                }
                window.open('/exportExcel.do?ids='+(ids.substr(0,ids.length - 1))+'&name=devCategory');
            });
            //全部导出
            $("#exportall").on("click",function () {
                window.open('/exportExcel.do?name=devCategory');
            });

            $(".hideTree").on("click", function () {
                $(".treeContainer").toggleClass("hide");
                $(".hideTreeBtn-show").toggleClass("hideTreeBtn-hide");
            })
        }
    };
    return module;
});

//子页面刷新父页面
window.RefreshPagesc = function () {
    common.loadModule("modules/device/deviceCategory",id);
};


