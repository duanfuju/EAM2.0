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

$(function() {

});

define(["text!modules/material/testMaterial.html", "text!modules/material/testMaterial.css","liger-all"], function (htmlTemp, cssTemp) {



    var module = {
       init:function(){

           var id = arguments[0];

           $(".content-wrapper").html(htmlTemp);
           $(".css-attr").html(cssTemp);

           /**
            * 查询表格、表单、查询区域的字段权限
            */

$.ajax({
   type : "post",
   async : false, //同步执行
   url : ctx+"/material/testMaterial/getfields?menuno="+id,
   dataType : "json",
   success : function(data)
   {
       //搜索区域赋值
       searchformConfig=data.srchfield;

       searchformConfig[3]= {display: "有效状态1", name: "materialstatus1", comboboxName: "materialstatusBox1", type: "select",
           option:{
               data:[{text: '有效1', id: '1'},{text: '无效1', id: '0'}]
           }
       }
       searchformConfig[2] = {display: "有效状态", name: "materialstatus", comboboxName: "materialstatusBox", type: "select",
               option:{
               data:[{text: '有效', id: '1'},{text: '无效', id: '0'}]
           }
       }

       //列表赋值
       materialTitle=data.gridfield;
       //将需要render的列做转换（例如状态）
       for(var i=0;i<materialTitle.length;i++){
           if(materialTitle[i].data=="materialstatus"){
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
/**渲染查询区域表单**/
$("#searchForm").ligerForm(searchformConfig);
$(".queryTable").append($("#searchForm"))
//渲染查询按钮
var btnHtml = "<div><input type='button' value='查询'  class='searchbtn' /><input type='button' value='重置'  class='resetbtn' /></div>";
$("#searchForm").append(btnHtml);
           //物料状态下拉初始化
           // $("input[name='materialstatusBox']").ligerComboBox({
           //     width : 200,
           //     data: [
           //         { name: '有效', id: '1' },
           //         { name: '无效', id: '0' },
           //     ],
           //
           //     valueFieldID: 'materialstatus',
           //     textField: 'name',
           //     //value: 'bj',
           //     //initIsTriggerEvent: false,
           //     onSelected: function (value)
           //     {
           //         console.log(value)
           //     }
           // });


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
                              var obj = new Object()
                              obj.icon="fa-pencil";
                              obj.title=btn.buttonname;
                              obj.callBack=function (data) {
                                  editId=data.materialid;
                                  layer.open({
                                      type: 2,
                                      skin: 'layui-layer-rim', //加上边框
                                      area: ['900px', '500px'], //宽高
                                      closeBtn: 1, //显示关闭按钮
                                      content: "/a/material/testMaterial/editUI"
                                  });
                              }
                              btnInline.push(obj);
                          }else if(btn.buttonname=="删除"){

                                  var obj = new Object()
                                  obj.icon="fa-times";
                                  obj.title=btn.buttonname;
                                  obj.callBack=function (data) {
                                      layer.confirm('确认删掉这条数据吗？',{icon: 3, title:'提示'},function() {
                                          $.ajax({
                                              type : "post",
                                              //async : false, //同步执行
                                              url : ctx+"/material/testMaterial/delete?id="+data.materialid,
                                              dataType : "text", //传递数据形式为text
                                              "success" : function(data)
                                              {
                                                  if(data=="success"){
                                                      layer.msg('删除成功！',{icon: 1,time: 1000}, function(index){
                                                          $('#mytable').dataTable().fnDraw(false);
                                                          // $("#mytable").DataTable().ajax.reload();
                                                          layer.close(index);
                                                      });
                                                  }else{
                                                      layer.msg("删除失败！",{time: 1000,icon:2});
                                                  }
                                              }
                                          });
                                      })
                                  }
                                  btnInline.push(obj);
                          }
                          //行外按钮渲染
                          $("<input>",{
                              type:'button',
                              val:btn.buttonname,
                              id:btn.buttonno,
                              onclick:btn.onclickevent
                          }).appendTo($(".btnArea"));

                      }


                  }

                   console.log(btnInline);

               }
           });


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
               })
               $('.queryInfo').html(queryInfo);
           })

           //重置按钮
           $(".resetbtn").on("click",function () {
                var form = liger.get('searchForm');
                form.setData("empty");
               $('.queryInfo').html("");
               $('#mytable').dataTable().fnDraw(false);
           })

           $(".queryBtn").on("click", function () {
               $(".queryTable").toggleClass("hide");
           })


           $(".guide").on("click", function () {
               $('.wrapper').animate({scrollTop:0}, '100');
               setTimeout(function () {
                   var steplist = $.WebPageGuide({showCloseButton: true});
                   steplist.newGuidStep(".queryBtn", "这是标题1啊", "点击显示查询区域");
                   steplist.newGuidStep(".searchbtn", "这是标题2啊", "请输入查询条件");
                   steplist.startGuide();
               },300);
           })




           var tab = common.renderTable("mytable", {
               // "order": [[5, "desc"]],
               "bStateSave":true,
               "serverSide": true,
               "hascheckbox": true,
               "hasIndex":true,
               //"ordering":true,
               "opBtns": btnInline,

               "ajax": {
                   "url": common.interfaceUrl.materialData,
                   "dataSrc": function (json) {
                       btnInline=[];
                       $("#rowsCount").html('共'+json.recordsTotal+'条记录');
                       return json.data;
                   },
                   "data": function (param) {

                       param.search=$("#searchForm").serializeJson();
                       console.log(param);
                   }
               },

               "columns": materialTitle,

           });

           //新增按钮
           $("#add").on("click", function () {

               layer.open({
                   title:'这里是标题',
                   type: 2,
                   skin: 'layui-layer-rim', //加上边框
                   area: common.getArea(), //宽高
                   // area: common.getArea(), //宽高
                   closeBtn: 1, //显示关闭按钮
                   content: ctx+"/material/testMaterial/addUI"
               });
           });

           //编辑按钮
           $("#edit").on("click", function () {

               if(tab.getCheckedRow().length!=1){
                   layer.msg("请选择一条数据！",{icon:7, time: 1000});
                   return;
               }
               var materialid = tab.getCheckedRow()[0].materialid;
               editId=materialid;
               layer.open({
                   type: 2,
                   skin: 'layui-layer-rim', //加上边框
                   area: common.getArea(), //宽高
                   closeBtn: 1, //显示关闭按钮
                   content: "/a/material/testMaterial/editUI"
               });
           });



           $("#delete").on("click",function () {
               if(tab.getCheckedRow().length==0){
                   layer.msg("请选择一条数据！",{icon:7,time: 1000});
                   return;
               }
               layer.confirm('确认删掉这条数据吗？',{icon: 3, title:'提示'},function() {
                   var ids = "";
                   for (var i = 0; i < tab.getCheckedRow().length; i++) {
                       ids += tab.getCheckedRow()[i].materialid + ",";
                   }
                   $.ajax({
                       type: "post",
                       //async : false, //同步执行
                       url: "/a/material/testMaterial/delete?id=" + ids,
                       dataType: "text", //传递数据形式为text
                       "success": function (data) {
                           if (data == "success") {
                               layer.msg('删除成功！',{icon: 1,time: 1000}, function (index) {
                                   $('#mytable').dataTable().fnDraw(false);
                                   layer.close(index);
                               });
                           } else {
                               layer.msg("删除失败！",{time: 1000,icon:2});
                           }
                       }
                   });
               })

           });

           //点击checkbox获取当前行的值
           $('#mytable').on('click','input[name="checkbox"]',function(){
               //$(this).is(":checked");
               console.log(tab.row($(this).closest('tr')).data());
           })

       }
   }
    return module;
});


