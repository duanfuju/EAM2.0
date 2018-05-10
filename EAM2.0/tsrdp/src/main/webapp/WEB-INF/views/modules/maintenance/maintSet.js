/**
 * Created by suven on 2017/11/1.
 */
//当前编辑条目的id
var editId;
//查询区域表单数据
var searchformConfig=null;
//设置树数据（用于子页面）
var _devLoctreeData = null;
//查询封装对象
var searchObj={};
//列表标题数据
var materialTitle=null;
//新增编辑表单数据
var formConfig=null;
var projectEmpid=null;
var projectType=null;
var projectMode=null;
var projectCycle=null;
var projectStatus=null;
//行内按钮
var btnInline=[];
var formFieldDisplay=[];

var materialSelect;
var personSelect;
$(function() {


});

define(["text!modules/maintenance/maintSet.html", "text!modules/maintenance/maintSet.css","liger-all"], function (htmlTemp, cssTemp) {

    var _devices="";
    window.getDevices = function () {
        return _devices;
    };
    window.setDevices = function (devices){
        _devices=devices;
    };

    var module = {
        init:function(menuno){
            var menuno = arguments[0];
            this.render(menuno);
        },
        render:function(menuno){
            this.initData();
            this.fillPage();
            //查询字段权限
            this.initFieldAuth(menuno);
        },
        fillPage:function() {
            $(".content-wrapper").html(htmlTemp);
            $(".css-attr").html(cssTemp);
        },
        initData:function(){
            //树
            common.callAjax('post',false, common.interfaceUrl.getDevLocationTree,"json",null,function(data) {
                _devLoctreeData = data;
            });
            //人员
            common.callAjax('post',false,common.interfaceUrl.userExtGetAllUser,"json",null,function(data){

                projectEmpid=data;
                console.log(projectEmpid);
            });
            //保养分类
            common.callAjax('post',true,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "maintain_type"},function(data){
                 projectType=data;
                console.log(projectType);


            });
            //保养类别
            common.callAjax('post',true,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "maintain_mode"},function(data){
                 projectMode=data;
                console.log(projectMode);

            });
            //巡检周期
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "maintain_period"},function(data){
                projectCycle=data;
                console.log(projectCycle);

            });
            //状态

            common.callAjax('post',true,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "common"},function(data){
                debugger;
                projectStatus=data;
                console.log(projectStatus);
            });


        },
        initFieldAuth:function (id) {
            var param = {};
            //param.menuno = id;
            //查询表格、表单、查询区域的字段权限
            common.callAjax('post',false,ctx+"/material/materialInfo/getfields?menuno="+id,"json",param,function(data){

                //搜索区域赋值
                searchformConfig = data.srchfield;
                $.each(searchformConfig,function(index,item){
                     if(item.name=='project_period'){
                        common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "maintain_period"},function(data1){
                            item.option={"data":data1};
                        });
                    }else if(item.name=='project_empid'){
                        common.callAjax('post',false,common.interfaceUrl.userExtGetAllUser,"json",null,function(data3){
                            item.option={"data":data3};
                        });
                    }else if(item.name == 'project_bm'){
                         item.options = {
                             isMultiSelect : false,
                             valueField : 'id',
                             treeLeafOnly : false,
                             tree : {
                                 url :   common.interfaceUrl.getDeptTreeData,
                                 checkbox : false,
                                 parentIcon: null,
                                 childIcon: null,
                                 idFieldName : 'id',
                                 parentIDFieldName : 'pId',
                                 nodeWidth:200,
                                 ajaxType : 'post',
                                 textFieldName:'name',//文本字段名，默认值text
                                 autoCheckboxEven:false,//复选框联动
                                 onSelect:function(e){
                                     console.log(e);
                                 }
                             }
                         }
                     }else  if(item.name=="dev_id"){
                         formFieldDisplay.push(item)
                     }
                });
                //列表赋值
                materialTitle = data.gridfield;
                //表单赋值
                debugger;
                formConfig = data.formfield;

            });
            this.initSearchForm();
            this.initButtonForm(id);
            this.initTableForm();
            this.initZtree()

        },
        initSearchForm : function () {
            //初始化搜索区域字段
            var searchform = {
                space : 20,
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
            btnInline =  common.ajaxForButton(id,'json',ctx+"/maintenance/maintSet/editUI",ctx + "/maintenance/maintSet/delete",ctx+"/maintenance/maintSet/detailUI",common.getAreaM());

            common.callAjax('post',false,common.interfaceUrl.toolsInfoData,"json",null,function(data){
                materialSelect = data;
            });
            common.callAjax('post',false,common.interfaceUrl.userExtGetAllUser,"json",null,function(data){
                personSelect = data;
            });
            $(".queryBtn").on("click", function () {
                $(".queryTable").toggleClass("hide");
            });
            // 设备名称树
            $("input[name='dev_id']").on("click",function () {
                layer.open({
                    type: 2,
                    title:'选择设备',
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getArea(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: common.interfaceUrl.inspectionSubjectDeviceSelectUI
                });
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
            //新增按钮
            $("#add").on("click", function () {
                layer.open({
                    type: 2,
                    title: '新增',
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getAreaM(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: ctx+"/maintenance/maintSet/addUI"
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
                if(materialTitle[i].data=="project_period"){
                    materialTitle[i].render = function (data) {
                        debugger;
                        if( data == "0"){
                            return "天";
                        }else if(data=="1"){
                            return "周";
                        }else if(data=="2"){
                            return "月";
                        }else if(data=="3"){
                            return "季";
                        }else{
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
                    "url":ctx+ '/maintenance/maintSet/listData',
                    "dataSrc": function (json) {
                        btnInline = [];
                        $("#rowsCount").html('共' + json.recordsTotal + '条记录');
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
                "columns": materialTitle

            });


            //导入选择文件
            $("#import").on("click",function () {
                $("#fileField").click();
            });
            //导入文件数据
            $("#fileField").on("change",function () {
                common.setExcel('maintset');
            });
            //下载模板
            $("#download").on("click",function () {
                common.loadExcelModel("maintset");
            });
            //导出
            $("#export").on("click",function () {
                if(tab.getCheckedRow().length==0){
                    layer.msg("请选择一条数据！",{icon:7,time: 1000});
                    return;
                }
                var ids="";
                for(var i=0;i<tab.getCheckedRow().length;i++){
                    ids+=tab.getCheckedRow()[i].id_key+",";
                }
                window.open('/exportExcel.do?ids='+(ids.substr(0,ids.length - 1))+'&name=maintset');
            });
            //全部导出
            $("#exportall").on("click",function () {
                window.open('/exportExcel.do?name=maintset');
            });
            $(".guide").on("click", function () {
                $('.wrapper').animate({scrollTop:0}, '100');
                setTimeout(function () {
                    var steplist = $.WebPageGuide({showCloseButton: true});
                    steplist.newGuidStep(".queryBtn", "查询", "点击显示查询区域");
                    steplist.newGuidStep(".btnArea", "操作", "导出新增区域");
                    steplist.startGuide();
                },300);

            })

        },

        initZtree:function(){
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
                    onClick: function(event, treeId, treeNode, clickFlag) {
                        searchObj.id = treeNode.id;
                        $('#mytable').dataTable().fnDraw(false);
                    }
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


