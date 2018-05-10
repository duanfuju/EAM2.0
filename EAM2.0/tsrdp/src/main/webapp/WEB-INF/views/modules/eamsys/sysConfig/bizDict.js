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

$(function() {

});

define(["text!modules/eamsys/sysConfig/bizDict.html", "text!modules/module1/module1.css","liger-all"], function (htmlTemp, cssTemp) {
    var module = {

        init:function(){
            var id = arguments[0];      //获取菜单编号入参

            $(".content-wrapper").html(htmlTemp);
            $(".css-attr").html(cssTemp);

            /**
             * 查询表格、表单、查询区域的字段权限
             */
            $.ajax({
                type : "post",
                async : false,    //同步执行
                data : {"menuno" : id},
                url : ctx + "/eam/bizDict/getfields",
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
            // var btnHtml = "<div><input type='button' value='查询'  id='searchbtn' /><input type='button' value='重置'  id='resetbtn' /></div>";
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
                            if(btn.buttonno == "edit"){
                                var obj = new Object();
                                obj.icon = "fa-pencil";
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
                            } else if(btn.buttonno == "delete"){
                                var obj = new Object()
                                obj.icon="fa-times";
                                obj.title=btn.buttonname;
                                obj.callBack=function (data) {
                                    layer.confirm('确认删掉这条数据吗？',{icon: 3, title:'提示'},function() {
                                        $.ajax({
                                            type: "post",
                                            async: false, //同步执行
                                            data: {"id": data.id_key},
                                            url: ctx + "/eam/bizDict/delete",
                                            dataType: "text", //传递数据形式为text
                                            "success": function (data) {
                                                if (data == "success") {

                                                    layer.msg('删除成功！', {icon: 1}, function (index) {
                                                        $('#mytable').dataTable().fnDraw(false);
                                                        layer.close(index);
                                                    });
                                                } else {
                                                    layer.msg(data,{time: 1000,icon:2});
                                                }
                                            }
                                        });
                                    })
                                }
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
                        queryInfo += '<li><span>' + i + ':</span><span>' + item + '</span></li>';
                    }
                })
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
                "order": [[1, "desc"]],
                "bStateSave":true,
                "serverSide": true,
                "hascheckbox": true,
                "hasIndex":true,
                "ordering":true,
                "opBtns": btnInline,
                "ajax": {
                    "url": common.interfaceUrl.bizDictList,
                    "dataSrc": function (json) {
                        btnInline = [];
                        $("#rowsCount").html('共'+json.recordsTotal+'条记录');
                        return json.data;
                    },
                    "data": function (param) {
                        //如果按照树节点搜索条件为空,则按照搜索区域条件查询
                        if(!searchObj.cat_id){
                            param.search=$("#searchForm").serializeJson();
                        }else{//否则，按照树节点id查询
                            param.search=searchObj;
                        }
                    }
                },

                "columns": bizDictTitle,
            });

            //新增按钮
            $("#add").on("click", function () {
                layer.open({
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getAreaXS(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: ctx+"/eam/bizDict/addUI"
                });
            });

            $(".hideTree").on("click", function () {
                $(".treeContainer").toggleClass("hide");
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


