//当前编辑条目的id
var editId;
//查询区域表单数据
var searchformConfig=null;
//列表标题数据
var faultOrderTitle=null;
//新增编辑表单数据
var formConfig=null;
//行内按钮
var btnInline=[];
var searchLigerForm;
var tab;


var createOrderLayer;

define(["text!modules/faultOrder/faultNotify.html", "text!modules/material/testMaterial.css","liger-all"], function (htmlTemp, cssTemp) {
    var order_type_enum;
    var order_status_enum;
    var module = {
        init:function(menuno){
            // menuno= 1069;
            // var tt = new Map();
            // tt.set(1,2);
            this.render(menuno);
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
            //查询表格、表单、查询区域的字段权限
            common.callAjax('post',false,ctx + "/material/testMaterial/getfields","json",param,function(data){
                //搜索区域赋值
                searchformConfig = data.srchfield;
                //列表赋值
                faultOrderTitle = data.gridfield;
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
            var param = {};
            param.dict_type_code = 'order_type';
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",param,function(data){
                order_type_enum = data;
            });
            param.dict_type_code = 'repair_order_status';
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",param,function(data){
                order_status_enum = data;
            });
            searchformConfig.forEach(function (element) {
                //设备选择下拉框
                if (element.name == 'order_device') {
                    //设备树初始化 order_device
                    element.options = {
                        isMultiSelect: true,
                        valueField: 'id',
                        treeLeafOnly: true,
                        tree: {
                            url: common.interfaceUrl.getDevTree,
                            // data:treeData, //存在数据累加
                            checkbox: false,
                            parentIcon: null,
                            childIcon: null,
                            idFieldName: 'id',
                            parentIDFieldName: 'pId',
                            nodeWidth: 200,
                            ajaxType: 'post',
                            textFieldName: 'name',//文本字段名，默认值text
                            onClick: function (note) {
                                console.log(note);
                            }
                        },
                        onBeforeSelect: function (newvalue) {
                            if (newvalue.data != undefined && 'devCategory' == newvalue.data.type) {
                                layer.msg('该节点为设备类别节点，请选择设备节点',{icon:7,time: 1000});
                                searchLigerForm.getEditor('order_device').setValue('');
                                return false;
                            }
                        },

                    }
                }else if(element.name == 'order_source'){
                    //故障来源初始化
                    element.options = {
                        isMultiSelect : true,
                        valueField : 'dict_value',
                        treeLeafOnly : false,
                        tree : {
                            // url :  common.interfaceUrl.getDictByDictTypeCode +"?dict_type_code=order_type",
                            data : order_type_enum,
                            checkbox : true,
                            parentIcon: null,
                            childIcon: null,
                            nodeWidth:200,
                            ajaxType : 'post',
                            textFieldName:'dict_name',//文本字段名，默认值text
                            autoCheckboxEven:false,//复选框联动
                            onClick : function (note) {

                            }
                        }
                    }
                }else if(element.name == 'order_status'){
                    //状态下拉初始化
                    element.options = {
                        isMultiSelect : true,
                        valueField : 'dict_value',
                        treeLeafOnly : false,
                        tree : {
                            // url :  common.interfaceUrl.getDictByDictTypeCode +"?dict_type_code=repair_order_status",
                            data : order_status_enum,
                            checkbox : true,
                            parentIcon: null,
                            childIcon: null,
                            nodeWidth:200,
                            ajaxType : 'post',
                            textFieldName:'dict_name',//文本字段名，默认值text
                            autoCheckboxEven:false,//复选框联动
                            onClick : function (note) {

                            }
                        }
                    }
                }
            });


            /**渲染查询区域表单**/
            searchLigerForm = $("#searchForm").ligerForm(searchform);
            $(".queryTable").append($("#searchForm"));
            //渲染查询按钮
            var btnHtml = "<div><input type='button' value='查询'  class='searchbtn' /><input type='button' value='重置'  class='resetbtn' /></div>";
            $("#searchForm").append(btnHtml);

            //查询按钮操作
            $(".searchbtn").on("click",function () {
                $('#mytable').dataTable().fnDraw(false);
                $(".queryTable").toggleClass("hide");
                var form = liger.get('searchForm');

                //var json =form.getData();
                //form.setData({materialcode: "", materialname: "", materialstatus: "1", materialstatus1: ""});
                //form.setData({materialstatus: "1"});

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

            $(".queryBtn").on("click", function () {
                $(".queryTable").toggleClass("hide");
            });

        },
        initButtonForm : function (id) {
            /**渲染按钮区域**/
            common.callAjax('post',false,ctx + "/eam/button/getButtonByRole?id="+id,"json",null,function(data){
                console.log(data);
                var btnHtml="";
                for(var i=0;i<data.length;i++){
                    var btn = data[i];
                    if(btn!==null && btn!=""){
                        if(btn.buttonname == "详情") {
                            var obj = {};
                            obj.icon = btn.icon;
                            obj.title = btn.buttonname;
                            obj.callBack = function (data) {
                                layer.open({
                                    type: 2,
                                    title: '详情',
                                    skin: 'layui-layer-rim', //加上边框
                                    area: common.getAreaS(), //宽高
                                    closeBtn: 1, //显示关闭按钮
                                    content: ctx + "/faultOrder/editOrder?id=" + data.id + "&type=view"
                                });
                            }
                        }else if(btn.buttonname=="编辑"){
                            var obj = {};
                            obj.icon=btn.icon;
                            obj.title=btn.buttonname;
                            obj.callBack=function (data) {
                                editId=data.id;
                            }
                        }else if(btn.buttonname=="删除"){
                            var obj = {};
                            obj.icon=btn.icon;
                            obj.title=btn.buttonname;
                            obj.callBack=function (data) {

                            }
                        }

                        if("删除" == btn.buttonname || "编辑" == btn.buttonname || "详情" == btn.buttonname){
                            btnInline.push(obj);
                        }

                        if("删除" != btn.buttonname && "编辑" != btn.buttonname && "详情" != btn.buttonname){
                            //行外按钮渲染
                            $("<input>",{
                                type:'button',
                                val:btn.buttonname,
                                id:btn.buttonno,
                                onclick:btn.onclickevent
                            }).appendTo($(".btnArea"));
                        }


                    }
                }
                console.log(btnInline);
            });

            //新增按钮
            $("#add").on("click", function () {
                createOrderLayer = layer.open({
                    type: 2,
                    title: '新增',
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getAreaS(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: ctx+"/faultOrder/addOrder"
                });
            });
            // 选中导出
            $("#export").on("click", function () {
                layer.open({
                    title: '接单或转单',
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getAreaS(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: ctx+"/faultOrder/redirectPage?page=tmp"
                });
            });



        },
        initTableForm : function () {
            var order_levels;
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "repair_order_level"},function(data){
                order_levels = data;
            });

            //将需要render的列做转换（例如状态）order_status_enum order_type_enum
            for (var i = 0; i < faultOrderTitle.length; i++) {
                if (faultOrderTitle[i].data == "order_status") {
                    faultOrderTitle[i].render = function (data) {
                        var showValue = "";
                        $.each(order_status_enum, function (i, item) {
                            if(data == item.dict_value){
                                showValue = item.dict_name;
                            }
                        });
                        return showValue;
                    }
                }else if(faultOrderTitle[i].data == "order_source"){
                    debugger;
                    faultOrderTitle[i].render = function (data) {
                        var showValue = "";
                        $.each(order_type_enum, function (i, item) {
                            if(data == item.dict_value){
                                showValue = item.dict_name;
                            }
                        });
                        return showValue;
                    }
                }else if(faultOrderTitle[i].data == "order_level"){
                    faultOrderTitle[i].render = function (data) {
                        var showValue = "";
                        $.each(order_levels, function (i, item) {
                            if(data == item.dict_value) {
                                showValue = item.dict_name;
                            }
                        });
                        return showValue;
                    }

                }else if(faultOrderTitle[i].data == "order_device"){
                    faultOrderTitle[i].render = function (data) {
                        if(data !=null && data!=""){
                            return data.dev_name;
                        }
                    }
                }
            }

            tab = common.renderTable("mytable", {
                "order": [[2, "desc"]],
                "bStateSave":true,
                "serverSide": true,
                "hascheckbox": true,
                "hasIndex":true,
                "ordering":true,
                "opBtns": btnInline,
                "ajax": {
                    "url": ctx+"/faultOrder/getOrderList",
                    "dataSrc": function (json) {
                        btnInline=[];
                        $("#rowsCount").html('共'+json.recordsTotal+'条记录');
                        return json.data;
                    },
                    "data": function (param) {
                        // param.search=$("#searchForm").serializeJson();
                        var form = liger.get('searchForm');
                        param.search = form.getData();
                        console.log(param);
                    }
                },
                "columns": faultOrderTitle,
                "columnDefs": []
            });
        }



    };
    return module;
});

