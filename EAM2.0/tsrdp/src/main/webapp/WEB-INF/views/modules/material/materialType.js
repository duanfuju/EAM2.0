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
//查询封装对象
var searchObj={};

$(function() {

});

function initTree(setting) {
    //请求物料类别树数据
    var zNodes=null;
    $.ajax({
        type : "post",
        async : false, //同步执行
        url : ctx+"/material/materialType/materialTypeTree",
        dataType : "json",
        success : function(data)
        {
            zNodes=data;
        }
    });

    $.fn.zTree.init($("#materTreeA"), setting, zNodes);
}

define(["text!modules/material/materialType.html", "text!modules/material/materialType.css","liger-all"], function (htmlTemp, cssTemp) {



    var module = {
       init:function(){

           //获取当前菜单模块的id
           var id = arguments[0];

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
                       pIdKey: "pid",
                       rootPId: ""
                   }
               },
               callback: {
                   onClick:function(event, treeId, treeNode, clickFlag){
                       //console.log(treeId);
                       console.log(treeNode.id);

                       searchObj.type_id=treeNode.id;
                       $('#mytable').dataTable().fnDraw(false);
                   }
                   /*beforeClick: function(treeId, treeNode) {
                       var zTree = $.fn.zTree.getZTreeObj("materTreeA");
                       if (treeNode.isParent) {
                           zTree.expandNode(treeNode);
                           return false;
                       }
                   }*/
               }
           };

           //树的初始化
           initTree(setting);


           /**
            * 查询表格、表单、查询区域的字段权限
            */

           $.ajax({
               type : "post",
               async : false, //同步执行
               url : ctx+"/material/materialType/getfields?menuno="+id,
               dataType : "json",
               success : function(data)
               {
                   //搜索区域赋值
                   searchformConfig=data.srchfield;

                   //列表赋值
                   materialTitle=data.gridfield;
                   //将需要render的列做转换（例如状态）
                   for(var i=0;i<materialTitle.length;i++){
                       materialTitle[i].render=function (data) {
                           if(data == undefined||data == undefined){
                               return "";
                           }else{
                               return data;
                           }
                       };

                       if(materialTitle[i].data=="type_status"){
                           materialTitle[i].render=function (data) {
                               if(data=="1"){
                                   return "有效";
                               }else{
                                   return "无效";
                               }
                           }
                       }
                   }

                   //表单赋值
                   formConfig=data.formfield;
               }
           });


           //初始化搜索区域字段
           var searchformConfig = {
               space : 50, labelWidth : 120 , inputWidth : 200,
               fields :searchformConfig

           };

           //树查询
           $("#findNodeA").on("click",function () {
               var posValue = $("#posA").val();
               if(posValue==null||posValue==""){
                   //如果查询条件为空，重新请求物料类别树数据
                   initTree(setting);
               }else{
                   common.getTreeByName("materTreeA",posValue);
               }

           });

           /**渲染查询区域表单**/
           $("#searchForm").ligerForm(searchformConfig);
           $(".queryTable").append($("#searchForm"));
           //渲染查询按钮
           var btnHtml = "<div style='padding-top: 15px;'><input type='button' value='查询'  class='searchbtn' /><input type='button' value='重置'  class='resetBtn' /></div>";
           $("#searchForm").append(btnHtml);



           /**渲染按钮区域**/
           $.ajax({
               type : "post",
               async : false, //同步执行
               url : ctx+"/eam/button/getButtonByRole?id="+id,
               dataType : "json",
               success : function(data)
               {
                   console.log(data);
                  //var materialBtn=data.data;
                   var btnHtml="";
                  for(var i=0;i<data.length;i++){
                      var btn = data[i];
                      if(btn!==null && btn!=""){
                          if(btn.buttonname=="编辑"){
                              var obj = {};
                              obj.icon="fa-pencil";
                              obj.title=btn.buttonname;
                              obj.callBack=function (data) {
                                  console.log(data);
                                  editId=data.id_key;
                                  layer.open({
                                      type: 2,
                                      title:'修改',
                                      skin: 'layui-layer-rim', //加上边框
                                      area: common.getAreaXS(), //宽高
                                      closeBtn: 1, //显示关闭按钮
                                      content: "/a/material/materialType/editUI"
                                  });
                              };
                              btnInline.push(obj);
                              continue;
                          }else if(btn.buttonname=="删除"){
                              var obj = {};
                              obj.icon="fa-times";
                              obj.title=btn.buttonname;
                              obj.callBack=function (data) {
                                  //alert(data.type_status);
                                  if(data.type_status==1){
                                      layer.msg("有效数据不可删除！", {time: 1000, icon: 2});
                                      return;
                                  }
                                  layer.confirm('确认删掉这条数据吗？',{icon: 3, title:'提示'},function() {
                                      $.ajax({
                                          type: "post",
                                          //async : false, //同步执行
                                          url: ctx + "/material/materialType/delete?id=" + data.id_key,
                                          dataType: "text", //传递数据形式为text
                                          "success": function (data) {
                                              if (data == "success") {

                                                  layer.msg('删除成功！', {icon: 1,time: 1000}, function (index) {
                                                      $('#mytable').dataTable().fnDraw(false);
                                                      // $("#mytable").DataTable().ajax.reload();
                                                      layer.close(index);
                                                  });
                                              } else {
                                                  layer.msg("删除失败！", {time: 1000, icon: 2});
                                              }
                                          }
                                      });
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
                                      title:'详情',
                                      skin: 'layui-layer-rim', //加上边框
                                      area: common.getAreaXS(), //宽高
                                      closeBtn: 1, //显示关闭按钮
                                      content: "/a/material/materialType/detailUI"
                                  });
                              };
                              btnInline.push(obj);
                          }
                          //行外按钮渲染
                          if(btn.buttonname!="查看详情"){//查看详情按钮行外不渲染
                              // #17881 liwenlong  标题按钮"删除"--->"批量删除"
                              if(btn.buttonname=="删除"){
                                  btn.buttonname="批量删除";
                              }
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

               }
           });


           //查询按钮操作
           $(".searchbtn").on("click",function () {
               //根据按钮查询前，先清空树节点查询条件
                searchObj={};
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

           //清空按钮操作
           $(".resetBtn").on("click",function () {
               var form = liger.get('searchForm');
               form.setData("empty");
               $('.queryInfo').html("");
               $('#mytable').dataTable().fnDraw(false);
           });


           //点击展示查询区域
           $(".queryBtn").on("click", function () {
               $(".queryTable").toggleClass("hide");
           });

           //隐藏树
           $(".hideTree").on("click", function () {
               $(".treeContainer").toggleClass("hide");
               $(".hideTreeBtn-show").toggleClass("hideTreeBtn-hide");
           });



           /*$("#guide").on("click", function () {
            var steplist = $.WebPageGuide({showCloseButton: true});
            steplist.newGuidStep(".queryBtn", "这是标题1啊", "点击显示查询区域");
            steplist.newGuidStep("#searchbtn", "这是标题2啊", "请输入查询条件");
            steplist.startGuide();
            })*/


           var tab = common.renderTable("mytable", {
               "order": [[2, "desc"]],
               "bStateSave":true,
               "serverSide": true,
               "hascheckbox": true,
               "hasIndex":true,
               //"ordering":true,
               "opBtns": btnInline,

               "ajax": {
                   "url": common.interfaceUrl.materialTypeData,
                   "dataSrc": function (json) {
                       btnInline=[];
                       $("#rowsCount").html('共'+json.recordsTotal+'条记录');
                       return json.data;
                   },
                   "data": function (param) {
                       //如果按照树节点搜索条件为空,则按照搜索区域条件查询
                        if(!searchObj.type_id){
                            var form = liger.get('searchForm');
                            param.search=form.getData();
                            //param.search=$("#searchForm").serializeJson();
                        }else{//否则，按照树节点id查询
                            param.search=searchObj;
                        }

                   }
               },

               "columns": materialTitle,
               /*"columnDefs": [
                   {
                       "render": function (data, type, row) {
                           if(row.type_pcode == undefined||row.type_pname == undefined){
                               return "";
                           }else{
                               return data;
                           }
                       },
                       "targets": [5]
                   }
               ]*/

           });

           //新增按钮
           $("#add").on("click", function () {

               layer.open({
                   type: 2,
                   title:'新增',
                   skin: 'layui-layer-rim', //加上边框
                   area: common.getAreaXS(), //宽高
                   closeBtn: 1, //显示关闭按钮
                   content: ctx+"/material/materialType/addUI"
               });
           });

           //编辑按钮
           $("#edit").on("click", function () {

               if(tab.getCheckedRow().length!=1){
                   layer.msg("请选择一条数据！",{time: 2000,icon:7});
                   return;
               }
               var id = tab.getCheckedRow()[0].id_key;
               editId=id;
               layer.open({
                   type: 2,
                   skin: 'layui-layer-rim', //加上边框
                   area: common.getAreaXS(), //宽高
                   closeBtn: 1, //显示关闭按钮
                   content: "/a/material/materialType/editUI"
               });
           });



           $("#delete").on("click",function () {
               if(tab.getCheckedRow().length==0){
                   layer.msg("请选择一条数据！",{time: 1000,icon:7});
                   return;
               }
               var ids="";
               for(var i=0;i<tab.getCheckedRow().length;i++){
                   if(tab.getCheckedRow()[i].type_status==1){
                       layer.msg("有效数据不可删除！", {time: 1000, icon: 2});
                       return;
                   }
                   ids+=tab.getCheckedRow()[i].id_key+",";
               }
               layer.confirm('确认删掉这条数据吗？',{icon: 3, title:'提示'},function() {
                   $.ajax({
                       type: "post",
                       //async : false, //同步执行
                       url: "/a/material/materialType/delete?id=" + ids,
                       dataType: "text", //传递数据形式为text
                       "success": function (data) {
                           if (data == "success") {

                               layer.msg('删除成功！', {icon: 1,time: 1000}, function (index) {
                                   $('#mytable').dataTable().fnDraw(false);
                                   layer.close(index);
                               });
                           } else {
                               layer.msg("删除失败！", {time: 1000, icon: 2});
                           }
                       }
                   });
               })
           });

           //导入选择文件
           $("#import").on("click",function () {
               $("#fileField").click();
           });
           //导入文件数据
           $("#fileField").on("change",function () {
               common.setExcel('materialType');
           });
           //下载模板
           $("#download").on("click",function () {
               common.loadExcelModel("materialType");
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
               window.open('/exportExcel.do?ids='+(ids.substr(0,ids.length - 1))+'&name=materialType');
           });
           //全部导出
           $("#exportall").on("click",function () {
               window.open('/exportExcel.do?name=materialType');
           });
            //导航
           $(".guide").on("click", function () {
               $('.wrapper').animate({scrollTop:0}, '100');
               setTimeout(function () {
                   var steplist = $.WebPageGuide({showCloseButton: true});
                   steplist.newGuidStep(".queryBtn", "查询", "点击显示查询区域");
                   steplist.newGuidStep(".btnArea", "操作", "这是导入导出和新增区域");
                   steplist.startGuide();
               },300);

           })
       }
   };
    return module;
});


