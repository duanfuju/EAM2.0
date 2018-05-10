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
var _levelData=null;//客户分类
var _creditData=null;//信用等级

$(function() {

});

define(["text!modules/employee/eamCustomer.html", "text!modules/employee/eamCustomer.css","liger-all"], function (htmlTemp, cssTemp) {

    var module = {
        init:function(menuno){
            menuno = 1054;
            // var menuno = arguments[0];
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

            //状态：   有效  1       无效  0
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "common"},function(data){
                _statusData=data;
            });
            //客户分类：重点客户 1       一般客户 0
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "customer_level"},function(data){
                _levelData=data;
            });
            // 信用等级：	 优秀AAA      良好AA        较好A     一般BBB       BB        差B
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "credit_level"},function(data){
                _creditData=data;
            });
            //查询表格、表单、查询区域的字段权限
            common.callAjax('post',false,common.interfaceUrl.getfields,"json",param,function(data){
                //搜索区域赋值
                searchformConfig = data.srchfield;
                $.each(searchformConfig, function (index, val) {
                    if(val.name == 'customer_status'){
                        val.options = {
                            data : _statusData
                        }
                    }else  if(val.name == 'customer_level'){
                        val.options = {
                            data : _levelData
                        }
                    }else  if(val.name == 'customer_credit'){
                        val.options = {
                            data : _creditData
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
                                editId=data.id_key;
                                layer.open({
                                    type: 2,
                                    title:'修改',
                                    skin: 'layui-layer-rim', //加上边框
                                    area: common.getAreaM(), //宽高
                                    closeBtn: 1, //显示关闭按钮
                                    content:common.interfaceUrl.customerEditUI
                                });
                            };
                            btnInline.push(obj);
                        }else if(btn.buttonname=="删除"){
                            var obj = {};
                            obj.icon="fa-times";
                            obj.title=btn.buttonname;
                            obj.callBack=function (data) {
                                //判断是否状态为无效可删除数据
                                var customer_status=data.customer_status;
                                if(customer_status=="有效"){
                                    layer.msg("有效数据无法删除，请先修改状态",{icon:7});
                                    return;
                                }
                                layer.confirm('确认删掉这条数据吗？',{icon: 3, title:'提示'}, function(){
                                    common.callAjax('post',false,common.interfaceUrl.customerDelete,"text",{id:data.id_key},function(d){
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
                        }else if(btn.buttonname=="查看详情"){
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
                                    content: common.interfaceUrl.customerDetailUI
                                });
                            };
                            btnInline.push(obj);
                        }
                        //行外按钮渲染
                        if(btn.buttonname!="查看详情"&&btn.buttonname!="编辑") {
                            // #17881 liwenlong  标题按钮"删除"--->"批量删除"
                            if(btn.buttonname=="删除"){
                                btn.buttonname="批量删除";
                            }
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

            //新增按钮
            $("#add").on("click", function () {
                layer.open({
                    type: 2,
                    title:'新增',
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getAreaM(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content:common.interfaceUrl.customerAddUI
                });
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
            $("#delete").on("click",function () {
                if(tab.getCheckedRow().length==0){
                    layer.msg("请选择一条数据！",{icon:7,time: 1000});
                    return;
                }
                //判断是否状态为无效可删除数据
                var rows=tab.getCheckedRow();
                for(var i=0;i<rows.length;i++){
                    var customer_status=rows[i].customer_status;
                    if(customer_status=="有效"){
                        layer.msg("有效数据无法删除，请先修改状态",{icon:7});
                        return;
                    }
                }
                layer.confirm('确认删掉这条数据吗？',{icon: 3, title:'提示'},function() {
                    var ids="";
                    for(var i=0;i<tab.getCheckedRow().length;i++){
                        ids+=tab.getCheckedRow()[i].id_key+",";
                    }
                    common.callAjax('post',false,common.interfaceUrl.customerDelete,"text",{"id":ids},function(data){
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
                common.setExcel('customer');
            });
            //下载模板
            $("#download").on("click",function () {
                common.loadExcelModel("customer");
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
                window.open('/exportExcel.do?ids='+(ids.substr(0,ids.length - 1))+'&name=customer&menuno='+id);
            });
            //全部导出
            $("#exportall").on("click",function () {
                window.open('/exportExcel.do?name=customer&menuno='+id);
            });

            $(".guide").on("click", function () {
                $('.wrapper').animate({scrollTop:0}, '100');
                setTimeout(function () {
                    var steplist = $.WebPageGuide({showCloseButton: true});
                    steplist.newGuidStep(".queryBtn", "查询", "点击显示查询区域");
                    steplist.newGuidStep(".searchbtn", "查询按钮", "请输入查询条件");
                    steplist.newGuidStep(".resetbtn", "重置按钮", "重置查询条件");
                    steplist.newGuidStep("#download", "模板下载按钮", "模板下载");
                    steplist.newGuidStep("#import", "导入按钮", "先下载模板,后下载");
                    steplist.newGuidStep("#export", "选中导出按钮", "选中导出");
                    steplist.newGuidStep("#exportall", "全部导出按钮", "全部导出");
                    steplist.newGuidStep("#add", "新增按钮", "增加");
                    steplist.newGuidStep("#delete", "删除按钮", "先选中一条或多条数据,然后删除");
                    steplist.startGuide();
                },300);
            });

        },
        initTableForm : function () {
            //将需要render的列做转换（例如状态）
           /* for (var i = 0; i < tableTitle.length; i++) {
                if (tableTitle[i].data == "customer_level") {//客户分类
                    tableTitle[i].render = function (data) {
                        for(var j=0;j<_levelData.length;j++){
                            var value=_levelData[j];
                            if(data==value.dict_value){
                                return value.dict_name;
                            }
                        }
                    }
                }else  if (tableTitle[i].data == "customer_status") {//状态
                    tableTitle[i].render = function (data) {
                        for(var j=0;j<_statusData.length;j++){
                            var value=_statusData[j];
                            if(data==value.dict_value){
                                return value.dict_name;
                            }
                        }
                    }
                }else  if (tableTitle[i].data == "customer_credit") {//信用等级
                    tableTitle[i].render = function (data) {
                        for(var j=0;j<_creditData.length;j++){
                            var value=_creditData[j];
                            if(data==value.dict_value){
                                return value.dict_name;
                            }
                        }
                    }
                }
            }*/
             tab = common.renderTable("mytable", {
                //"order": [[2, "desc"]],
               // "bStateSave":true,
                "serverSide": true,
                "hascheckbox": true,
                "hasIndex":true,
                //"ordering":true,
                "opBtns": btnInline,
                "ajax": {
                    "url": common.interfaceUrl.customerDataTablePageMap,
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


