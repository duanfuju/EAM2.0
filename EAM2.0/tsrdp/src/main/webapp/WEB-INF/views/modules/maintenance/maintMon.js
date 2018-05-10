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
var materialSelect;
var personSelect;
var _menuno;//菜单
$(function() {

});

define(["text!modules/maintenance/maintMon.html", "text!modules/maintenance/maintMon.css","liger-all"], function (htmlTemp, cssTemp) {



    var module = {
       init:function(menuno){
           _menuno= arguments[0];
           this.render(_menuno);
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
                searchformConfig = data.srchfield;
                $.each(searchformConfig,function(index,item){
                    if(item.name=='project_period'){
                        common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "maintain_period"},function(data){
                            item.option={"data":data};
                        });
                    }else if(item.name=='project_status'){
                        common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "APPROVE_STATUS"},function(data){
                            item.option={"data":data};
                        });
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
            common.callAjax('post',false,ctx + "/eam/button/getButtonByRole?id="+id,"json",null,function(data){
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
                                editId=data.id_key;
                                layer.open({
                                    type: 2,
                                    title: '修改',
                                    skin: 'layui-layer-rim', //加上边框
                                    area: common.getAreaM(), //宽高
                                    closeBtn: 1, //显示关闭按钮
                                    content: ctx+"/maintain/maintainProjectSub/editUI"
                                });
                            };
                            btnInline.push(obj);
                        }else if(btn.buttonname=="删除"){
                            var obj = {};
                            obj.icon="fa-times";
                            obj.title=btn.buttonname;
                            obj.callBack=function (data) {
                                common.callAjax('post', false, ctx + "/supplier/supplier/delebefore", "text", {"id":data.id_key}, function (d) {
                                            if(d=="false"){
                                                layer.msg('此数据已被引用,禁止删除！',{time: 1000,icon:7},function(index){
                                                    layer.close(index);
                                                    
                                                });
                                            }else{
                                                layer.confirm('确认删掉这条数据吗？',{icon: 3,title:'提示'},function(){
                                                        common.callAjax('post', false, ctx + "/maintain/maintainProjectSub/delete?id=" + data.id_key, "text", null, function (d) {
                                                            if (d == "success") {
                                                                layer.msg('删除成功！',{icon: 1,time: 1000}, function (index) {
                                                                    $('#mytable').dataTable().fnDraw(false);
                                                                    layer.close(index);
                                                                });
                                                            } else {
                                                                layer.msg("删除失败！",{time: 1000,icon:2});
                                                            }
                                                        })
                                                    }
                                                )
                                            }
                                })

                            };
                            btnInline.push(obj);
                        }else if(btn.buttonname=="查看详情"){
                            var obj = {};
                            obj.icon="fa-file-text-o";
                            obj.title=btn.buttonname;
                            obj.callBack=function (data) {
                                editId=data.id_key;
                                layer.open({
                                    type: 2,
                                    title: '详情',
                                    skin: 'layui-layer-rim', //加上边框
                                    area: common.getAreaM(), //宽高
                                    closeBtn: 1, //显示关闭按钮
                                    content: "/a/maintain/maintainProjectSub/detailUI"
                                });
                            };
                            btnInline.push(obj);
                        }
                        //行外按钮渲染
                       if(btn.buttonno=='add'||btn.buttonno=='export'||btn.buttonno=='exportall'||btn.buttonno=='download'||btn.buttonno=='import'||btn.buttonno=='produce') {
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

            common.callAjax('post',false,common.interfaceUrl.toolsInfoData,"json",null,function(data){
                materialSelect = data;
            });
            common.callAjax('post',false,common.interfaceUrl.userExtGetAllUser,"json",null,function(data){
                personSelect = data;
            });
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
            $(".guide").on("click", function () {
                $('.wrapper').animate({scrollTop:0}, '100');
                setTimeout(function () {
                    var steplist = $.WebPageGuide({showCloseButton: true});
                    steplist.newGuidStep(".queryBtn", "查询", "点击显示查询区域");
                    steplist.newGuidStep(".btnArea", "操作", "这是导入导出和新增区域");
                    steplist.startGuide();
                },300);

            });
            //跳转生成审批页面
            $("#produce").on("click", function () {
                layer.open({
                    type: 2,
                    title: '生成审批',
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getAreaM(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: ctx+"/maintain/maintainProjectSub/maintMonSubmitUI"
                });
            });
        },
        initTableForm : function () {
            //将需要render的列做转换（例 如状态）
            for (var i = 0; i < materialTitle.length; i++) {
                if (materialTitle[i].data == "status") {
                    materialTitle[i].render = function (data) {
                        return data == "1" ? "有效" : "无效";
                    }
                }
                    if(materialTitle[i].data == "project_status"){
                        materialTitle[i].render = function (data) {
                            if (data == "0") {
                                return "待提交";
                            } else if (data == "1") {
                                return "待审批";
                            } else if (data == "2") {
                                return "通过";
                            } else {
                                return "拒绝";
                            }
                        }
                    }
                    if(materialTitle[i].data == "project_period"){
                        materialTitle[i].render = function (data) {
                            if (data == "0") {
                                return "天";
                            } else if (data == "1") {
                                return "周";
                            } else if (data == "2") {
                                return "月";
                            } else if (data == "3") {
                                return "季";
                            } else {
                                return "年";
                            }
                        }
                    }
            }

            var tab = common.renderTable("mytable", {
                //"order": [[4, "desc"]],
                //"bStateSave": true,
                "serverSide": true,
                "hascheckbox": true,
                "hasIndex": true,
                //"ordering":true,
                "opBtns": btnInline,
                "ajax": {
                    "url":ctx+ '/maintain/maintainProjectSub/listData',
                    "dataSrc": function (json) {
                        btnInline = [];
                        $("#rowsCount").html('共' + json.recordsTotal + '条记录');
                        return json.data;
                    },
                    "data": function (param) {
                       // param.search = $("#searchForm").serializeJson();
                        var form = liger.get('searchForm');
                        param.search=form.getData();
                        console.log(param);
                    }
                },
                "columns": materialTitle

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
                window.open('/exportExcel.do?ids='+(ids.substr(0,ids.length - 1))+'&name=maintmon');
            });
            //全部导出
            $("#exportall").on("click",function () {
                window.open('/exportExcel.do?name=maintmon');
            });
        }
   };
    return module;
});


