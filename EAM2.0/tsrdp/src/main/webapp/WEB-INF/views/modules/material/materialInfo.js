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
//供应商下拉数据
var supplierSelect=null;

$(function() {

});

define(["text!modules/material/materialInfo.html", "text!modules/material/materialInfo.css","liger-all"], function (htmlTemp, cssTemp) {



    var module = {
       init:function(){

           //获取当前菜单模块的id
           var id = arguments[0];

           $(".content-wrapper").html(htmlTemp);
           $(".css-attr").html(cssTemp);


           /**
            * 查询表格、表单、查询区域的字段权限
            */

           $.ajax({
               type : "post",
               async : false, //同步执行
               url : ctx+"/material/materialInfo/getfields?menuno="+id,
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

                       if(materialTitle[i].data=="material_status"){
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
           /**供应商下拉数据**/
           common.callAjax('post',false,ctx + "/material/materialInfo/supplierSelect","json",null,function(d){
               supplierSelect=d;
           });
           console.log(searchformConfig);
           $.each(searchformConfig, function(index,val){
                //查询区域初始化物料类别下拉树
               if(val.name=="material_type"){
                   val.options={
                       isMultiSelect:true,
                       valueField: 'id',
                       textField:'name',
                       treeLeafOnly: false,
                       tree: {
                           url:ctx+"/material/materialType/materialTypeTree",
                           checkbox: false,
                           idFieldName :'id',
                           parentIDFieldName :'pid',

                           parentIcon: null,
                           childIcon: null,
                           ajaxType: 'post',
                           onClick:function (note) {

                           }
                       }
                   }
               }else if(val.name=="material_status"){
                   val.options={
                       data: [
                           { text: '有效', id: '1' },
                           { text: '无效', id: '0' },
                       ]
                   }
               }else if(val.name=="material_level"){
                   val.options={
                       data: [
                           { text: 'A类', id: '0' },
                           { text: 'B类', id: '1' },
                           { text: 'C类', id: '2' },
                       ]
                   }
               }else if(val.name=="material_purchasing"){
                   val.options={
                       data: [
                           { text: '自购', id: '0' },
                           { text: '统购', id: '1' },
                       ]
                   }
               }else if(val.name=="material_supplier"){
                   val.options={
                       data: supplierSelect
                   }
               }

           });

           console.log(searchformConfig);




           //初始化搜索区域字段
           var searchformConfig = {
               space : 50, labelWidth : 120 , inputWidth : 200,
               fields :searchformConfig

           };
           /**渲染查询区域表单**/
           $("#searchForm").ligerForm(searchformConfig);
           $(".queryTable").append($("#searchForm"));
           //渲染查询按钮
           var btnHtml = "<div><input type='button' value='查询'  class='searchbtn' /><input type='button' value='重置'  class='resetBtn' /></div>";
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
                                      area: common.getAreaS(), //宽高
                                      closeBtn: 1, //显示关闭按钮
                                      content: ctx+"/material/materialInfo/editUI"
                                  });
                              };
                              btnInline.push(obj);
                              continue;
                          }else if(btn.buttonname=="删除"){
                              var obj = {};
                              obj.icon="fa-times";
                              obj.title=btn.buttonname;
                              obj.callBack=function (data) {
                                  if(data.material_status==1){
                                      layer.msg("有效数据不可删除！", {time: 1000, icon: 2});
                                      return;
                                  }
                                  layer.confirm('确认删掉这条数据吗？',{icon: 3, title:'提示'},function() {
                                      $.ajax({
                                          type: "post",
                                          //async : false, //同步执行
                                          url: ctx + "/material/materialInfo/delete?id=" + data.id_key,
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
                                      area: common.getAreaS(), //宽高
                                      closeBtn: 1, //显示关闭按钮
                                      content: ctx+"/material/materialInfo/detailUI"
                                  });
                              };
                              btnInline.push(obj);
                          }
                          //行外按钮渲染
                          if(btn.buttonname!="查看详情"&&btn.buttonname!="复制") {//查看详情、复制按钮行外不渲染
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

                   console.log(btnInline);

               }
           });


           //清空按钮操作
           $(".resetBtn").on("click",function () {
               var form = liger.get('searchForm');
               form.setData("empty");
               $('.queryInfo').html("");
               $('#mytable').dataTable().fnDraw(false);
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

           //点击展示查询区域
           $(".queryBtn").on("click", function () {
               $(".queryTable").toggleClass("hide");
           });


           /*$("#guide").on("click", function () {
               var steplist = $.WebPageGuide({showCloseButton: true});
               steplist.newGuidStep(".queryBtn", "这是标题1啊", "点击显示查询区域");
               steplist.newGuidStep("#searchbtn", "这是标题2啊", "请输入查询条件");
               steplist.startGuide();
           })*/

           var tab = common.renderTable("mytable", {
               "order": [[3, "desc"]],
               "bStateSave":true,
               "serverSide": true,
               "hascheckbox": true,
               "hasIndex":true,
               //"ordering":true,
               "opBtns": btnInline,

               "ajax": {
                   "url": common.interfaceUrl.materialInfoData,
                   "dataSrc": function (json) {
                       btnInline=[];
                       $("#rowsCount").html('共'+json.recordsTotal+'条记录');
                       return json.data;
                   },
                   "data": function (param) {
                       var form = liger.get('searchForm');
                       param.search=form.getData();
                       //param.search=$("#searchForm").serializeJson();

                   }
               },

               "columns": materialTitle,
               "sScrollX": "100%",
               "sScrollXInner": "100%",
               "bScrollCollapse": true

           });

           //新增按钮
           $("#add").on("click", function () {

               layer.open({
                   type: 2,
                   title:'新增',
                   skin: 'layui-layer-rim', //加上边框
                   area: common.getAreaS(), //宽高
                   closeBtn: 1, //显示关闭按钮
                   content: ctx+"/material/materialInfo/addUI"
               });
           });

           //编辑按钮
           $("#edit").on("click", function () {

               if(tab.getCheckedRow().length!=1){
                   layer.msg("请选择一条数据！",{time: 1000,icon:7});
                   return;
               }
               var id = tab.getCheckedRow()[0].id_key;
               editId=id;
               layer.open({
                   type: 2,
                   title:'修改',
                   skin: 'layui-layer-rim', //加上边框
                   area: common.getAreaS(), //宽高
                   closeBtn: 1, //显示关闭按钮
                   content: ctx+"/material/materialInfo/editUI"
               });
           });



           $("#delete").on("click",function () {
               if(tab.getCheckedRow().length==0){
                   layer.msg("请选择一条数据！",{time: 1000,icon:7});
                   return;
               }
               var ids = "";
               for (var i = 0; i < tab.getCheckedRow().length; i++) {
                   if(tab.getCheckedRow()[i].material_status==1){
                       layer.msg("有效数据不可删除！", {time: 1000, icon: 2});
                       return;
                   }
                   ids += tab.getCheckedRow()[i].id_key + ",";
               }
               layer.confirm('确认删掉这条数据吗？',{icon: 3, title:'提示'},function() {

                   $.ajax({
                       type: "post",
                       //async : false, //同步执行
                       url: ctx + "/material/materialInfo/delete?id=" + ids,
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

           $(".hideTree").on("click", function () {
               $(".treeContainer").toggleClass("hide");
           });

           //导入选择文件
           $("#import").on("click",function () {
               $("#fileField").click();
           });
           //导入文件数据
           $("#fileField").on("change",function () {
               common.setExcel('materialInfo');
           });
           //下载模板
           $("#download").on("click",function () {
               common.loadExcelModel("materialInfo");
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
               window.open('/exportExcel.do?ids='+(ids.substr(0,ids.length - 1))+'&name=materialInfo');
           });
           //全部导出
           $("#exportall").on("click",function () {
               window.open('/exportExcel.do?name=materialInfo');
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


