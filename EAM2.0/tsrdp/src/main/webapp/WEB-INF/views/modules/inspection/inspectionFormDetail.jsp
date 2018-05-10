<%@ page import="org.activiti.engine.form.FormType" %>
<%@ page import="org.activiti.engine.form.FormProperty" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp"%>
<html>
<head>
    <title>巡检任务反馈</title>
    <style type="text/css">
        #pinfo th {background: #f7f7f9;}
         .editDiv .l-panel-bar{
             display: none ;
         }
    </style>
    <meta name="decorator" content="default"/>
    <script type="text/javascript" src="/js/bootstrap.min.js"></script>

    <script type="text/javascript">
        var executionId = '${task.executionId}';
        var taskid = '${task.id}';
        var pstid = '${pstid}';

        $(function () {
            var kkk = "eam_fault_order:4HQv5Pcc5VPEik6r";
            var businessKey = kkk.substring(kkk.indexOf(":")+1);

            //ajax提交
            $("#btnFeedBack").on('click', function() {

                var form = liger.get('serviceForm');
                if (!form.valid()) {
                    form.showInvalid();
                }

                //复杂表单用ajax提交
                var param=new Object();//对象存储object
                var order_reason=$("input[name='order_fk_reason']").val();
                var oeder_result=$('#order_fk_resultBox').val();

                var tool_data = manager1.getData();
                var attachment_data = manager2.getData();
                var manhaur_data = manager3.getData();
                var other_data = manager4.getData();

                console.log(tool_data);
                console.log(attachment_data);
                console.log(manhaur_data);
                console.log(other_data);



                param.id = businessKey;
                param.order_fk_result = oeder_result;
                param.order_fk_reason = order_reason;
                param.orderTool = tool_data;
                param.orderAttachment = attachment_data;
                param.orderManhaur = manhaur_data;
                param.orderOther = other_data;


                /**获取上传的图片*/
                var fileimg="";
                $('.fileimg').each(function(){//上传文件
                    if($(this).find('input').val()!="" || $(this).find('input').val()!=null){
                        fileimg+=","+$(this).find('input').val();
                    }
                });
                if(fileimg.indexOf(",")!=-1){
                    param.order_fk_photo=fileimg.substring(1);
                }

                /**获取上传的视频*/
                var filevid="";
                $('.filevid').each(function(){//上传文件
                    if($(this).find('input').val()!="" || $(this).find('input').val()!=null){
                        filevid+=","+$(this).find('input').val();
                    }
                });
                if(filevid.indexOf(",")!=-1){
                    param.order_fk_video=filevid.substring(1);
                }

                common.callAjax('post',false,'${ctx}/faultOrder/feedBack',"text",{param:JSON.stringify(param),taskid:taskid},function(data){
                    if(data=="success"){
                        layer.msg('提交成功！',{icon:1,time: 1000}, function(index){
                            parent.$("#mytable").DataTable().ajax.reload();
                            parent.layer.closeAll();
                        });
                    }else if (data == "noauth") {
                        layer.msg('登录超时或无权限！' ,{time: 1000,icon:7},function(index){
                            layer.close(index);
                        });

                    }else{
                        layer.msg('提交失败！',{time: 1000,icon:2}, function(index){
                            layer.close(index);
                        });
                    }
                })
            });

            /**附件上传-图片*/
            var FieldCount=0;
            $('#uploadmoreimg').on('click',function(){
                var maxInputfile=4;
                if($(".file").length<maxInputfile){
                    FieldCount++;
                    $('#filearea').append('<div class="fileimg"><input type="text" name="mytext[]" id="img_'+FieldCount+'" readonly /><a href="#" class="uploadclass">上传</a>&nbsp;<a href="#" class="removeclass">删除</a></div>');
                }

            })
            $('#imgarea').on('click','.uploadclass',function(){
                var id=$(this).prev().attr('id');
                var finder = new CKFinder();
                finder.selectActionFunction = function(fileUrl){
                    $("#"+id).val(fileUrl);

                } //当选中图片时执行的函数
                finder.popup();//调用窗口
            })
            $('#imgarea').on('click','.removeclass',function(){
                if($(".fileimg").length>1){
                    $(this).parent('div').remove();
                }
            })

            /**附件上传-视频*/
            var FieldCount=0;
            $('#uploadmorevid').on('click',function(){
                var maxInputfile=4;
                if($(".file").length<maxInputfile){
                    FieldCount++;
                    $('#filearea').append('<div class="filevid"><input type="text" name="mytext[]" id="vid_'+FieldCount+'" readonly /><a href="#" class="uploadclass">上传</a>&nbsp;<a href="#" class="removeclass">删除</a></div>');
                }

            })
            $('#vidarea').on('click','.uploadclass',function(){
                var id=$(this).prev().attr('id');
                var finder = new CKFinder();
                finder.selectActionFunction = function(fileUrl){
                    $("#"+id).val(fileUrl);

                } //当选中图片时执行的函数
                finder.popup();//调用窗口
            })
            $('#vidarea').on('click','.removeclass',function(){
                if($(".filevid").length>1){
                    $(this).parent('div').remove();
                }
            })



            $.fn.outerHTML = function () {

                // IE, Chrome & Safari will comply with the non-standard outerHTML, all others (FF) will have a fall-back for cloning
                return (!this.length) ? this : (this[0].outerHTML ||
                (function (el) {
                    var div = document.createElement('div');
                    div.appendChild(el.cloneNode(true));
                    var contents = div.innerHTML;
                    div = null;
                    return contents;
                })(this[0]));

            };

            if ($('#processDiagram').length == 1) {
                showActivities();
            }
            function showActivities() {
                $.getJSON(ctx + '/eam/act/process/trace/data/' + executionId, function (infos) {
                    var positionHtml = "";

                    var diagramPositon = $('#processDiagram').position();
                    var varsArray = new Array();
                    $.each(infos, function (i, v) {
                        var $positionDiv = $('<div/>', {
                            'class': 'activity-attr'
                        }).css({
                            position: 'absolute',
                            left: (v.x + 2),
                            top: (v.y + 35),
                            width: (v.width - 2),
                            height: (v.height - 2),
                            backgroundColor: 'black',
                            opacity: 0
                        });

                        // 节点边框
                        var $border = $('<div/>', {
                            'class': 'activity-attr-border'
                        }).css({
                            position: 'absolute',
                            left: (v.x + 2),
                            top: (v.y + 35),
                            width: (v.width - 4),
                            height: (v.height - 3)
                        });

                        if (v.currentActiviti) {
                            $border.css({
                                border: '3px solid red'
                            }).addClass('ui-corner-all-12');
                        }
                        positionHtml += $positionDiv.outerHTML() + $border.outerHTML();
                        varsArray[varsArray.length] = v.vars;
                    });

                    $(positionHtml).appendTo('#flowchart').find('.activity-attr-border');

                    // 鼠标移动到活动上提示
                    $('.activity-attr-border').each(function (i, v) {
                        var tipContent = "<table class='table table-bordered'>";
                        $.each(varsArray[i], function(varKey, varValue) {
                            if (varValue) {
                                tipContent += "<tr><td>" + varKey + "</td><td>" + varValue + "</td></tr>";
                            }
                        });
                        tipContent += "</table>";
                        $(this).data('vars', varsArray[i]).data('toggle', 'tooltip').data('placement', 'bottom').data('title', '活动属性').attr('title', tipContent);
                    }).tooltip();
                });
            }

            var formField = new Array();

            /** 获取巡检任务下的巡检区域信息*/
            var areaGroup = new Object();
            common.callAjax('POST',false,ctx+"/eam/inspectionTask/getAreaInfoByTaskPstid",'json',{"pstid":"171023114148054hxf3K"},function(data){
                for(var i = 0; i < data.length; i++) {
                    areaGroup.display = data[i].area_name;
                    areaGroup.name = data[i].id;
                    areaGroup.type = "text";
                    areaGroup.group = data[i].area_name;
                }
            });

            var tab = common.renderTable("devSubjectTable", {
                "bStateSave":true,
                "serverSide": true,
                "hascheckbox": false,
                "hasIndex":true,
                "opBtns": [{
                    "title": "反馈", "callBack": aa,"label":"反馈","class":"mayclass"
                }],
                "ajax": {
                    "url": "a/eam/inspectionTask/getSubjectInfoByAreaId",
                    "dataSrc": function (json) {
                        return json.data;
                    },
                },
                "columns":  [
                    {"data": "id_key", "title": "ID", "bVisible": false},
                    {"data": "material_code", "title": "物料编码"},
                    {"data": "material_name", "title": "物料名称"},
                    {"data": "material_qty", "title": "物料库存"},
                    {"data": "material_unit", "title": "计量单位"},
                    {"data": "material_cost", "title": "标准成本"}
                ],
            });


            var selected = common.renderTable("materialSelected", {
                "bStateSave": false,
                "serverSide": false,
                "hascheckbox": false,
                "hasIndex": true,
                "bDestroy": true,   // 先destory之前的表格，否则会出现序号和操作列重复
                "opBtns": [{
                    "title": "删除", "callBack": aa,"label":"删除","class":"mayclass"
                }],
                "data": selectedObj,
                "columns": [
                    {"data": "id_key", "title": "ID", "bVisible": false},
                    {"data": "material_code", "title": "物料编码"},
                    {"data": "material_name", "title": "物料名称"},
                    {"data": "material_qty", "title": "物料库存"},
                    {"data": "material_unit", "title": "计量单位"},
                    {"data": "material_cost", "title": "标准成本"}
                ],
            });

            /**表单字段暂时写死，后期再通过接口查询*/
                //反馈结果字段
            var result = {display:"反馈结果",editable:true,name:"order_fk_result",type:"select",comboboxName:"order_fk_resultBox",
                    options:{
                        data: [
                            { text: '修好', id: '1' },
                            { text: '未修好', id: '0' }
                        ]
                    }
                };


            //原因字段
            var reason = {display:"原因",editable:true,name:"order_fk_reason",type:"text"};


            //工单实际明细
            var detail = {display:"工单实际明细",editable:true,name:"order_fk_detail",type:"select",comboboxName:"order_fk_detailBox",
                options:{
                    data: [
                        { text: '工器具', id: '0' },
                        { text: '备件材料', id: '1' },
                        { text: '人员工时', id: '2' },
                        { text: '其他费用', id: '3' }
                    ]
                }
            };

            formField.push(areaGroup);
            formField.push(result);
            formField.push(reason);
            formField.push(detail);

            var formConfig = {
                space : 50, labelWidth : 80 , inputWidth : 200,
                validate: true,
                fields: formField
            };

            //console.log(formField)
            $("#serviceForm").ligerForm(formConfig);
            $(".togglebtn").click();

            //值类型下拉切换
            $("#order_fk_detailBox").on('change',function () {

                //alert($("#subject_valuetypeBox").val());
                if ($("#toolStdTable").children().length <1){
                    $(f_initGrid1);
                    $(f_initGrid2);
                    $(f_initGrid3);
                    $(f_initGrid4);
                }

                if($("#order_fk_detailBox").val() =="0"){
                    $("#toolStdTable").show().siblings().hide();
                }else if($("#order_fk_detailBox").val() =="1"){
                    $("#attachmentStdTable").show().siblings().hide();
                }else if($("#order_fk_detailBox").val() =="2"){
                    $("#manhaurStdTable").show().siblings().hide();
                }else{
                    $("#otherStdTable").show().siblings().hide();
                }

            });



            //给表单赋值
            //var data1,data2,data3,data4,data5,data6;
            /*$.ajax({
             type:'POST',
             url:ctx+"/opestandard/standardLibrary/getLibByPIid",
             async:false,
             dataType:'json',
             data:{pIid:$("#pIid").val()},
             success:function(data){
             var libid=data.id;
             common.callAjax('POST',true,ctx+"/opestandard/standardLibrary/editObj",'json',{id:libid},function(data){
             var editForm  = liger.get("serviceForm");
             editForm.setData(data);
             })
             if(libid !=null && libid !=""){
             $('#library_code').val(data.library_code);
             $('#library_name').val(data.library_name);
             //给检修列表赋值
             common.callAjax('POST',false,ctx+"/opestandard/standardLibrary/quFault",'json',{id:libid},function(data){
             data1={Rows:data};
             })
             common.callAjax('POST',false,ctx+"/opestandard/standardLibrary/quMaintain",'json',{id:libid},function(data){
             data2={Rows:data};
             })
             common.callAjax('POST',false,ctx+"/opestandard/standardLibrary/quPatrol",'json',{id:libid},function(data){
             data3={Rows:data};
             })
             common.callAjax('POST',false,ctx+"/opestandard/standardLibrary/quFailure",'json',{id:libid},function(data){
             data4={Rows:data};
             })
             common.callAjax('POST',false,ctx+"/opestandard/standardLibrary/quOpe",'json',{id:libid},function(data){
             data5={Rows:data};
             })
             common.callAjax('POST',false,ctx+"/opestandard/standardLibrary/quSafe",'json',{id:libid},function(data){
             data6={Rows:data};
             })

             }
             }
             })*/

            var data1 = {
                Rows: [
                    {
                        "order_id":businessKey,
                        "tool_id":null,
                        "tool_num":null,
                        "tool_model":null,
                        "tool_remark":null
                    }
                ]
            };

            //$(f_initGrid1);
            //工器具
            var manager1, g1;
            function f_initGrid1()
            {
                g1 =  manager1 = $("#toolStdTable").ligerGrid({
                    columns: [
                        { display: '工器具', name: 'tool_id',
                            editor: {
                                type: 'selectGrid',
                                data:[{id:0,name:"螺丝"},{id:1,name:"剪刀"},{id:2,name:"夹子"},{id:3,name:"钻头"}],
                                valueField: 'id',
                                textField: 'name',
                                onSelected: function (value)
                                {
                                    //alert(value)
                                }

                            },
                            render: function (item) {

                                var text = "";
                                $.each([{id:0,name:"螺丝"},{id:1,name:"剪刀"},{id:2,name:"夹子"},{id:3,name:"钻头"}], function (i, data) {
                                    if (data.id == item.tool_id) {
                                        text = data.name;
                                    }
                                })
                                return text;
                            }
                        },
                        { display: '数量', name: 'tool_num',
                            editor: { type: 'text' }
                        },
                        { display: '规格型号', name: 'tool_model',
                            editor: { type: 'text' }
                        },
                        { display: '备注', name: 'tool_remark',
                            editor: { type: 'text' }
                        },
                        { display: '操作', isSort: false,
                            render: function (rowdata, rowindex, value)
                            {
                                var h = "";
                                h += "<a class='add'>添加</a> ";
                                h += "<a class='dele' data-id='"+ rowindex +"'>删除</a> ";
                                return h;
                            }
                        }

                    ],
                    onSelectRow: function (rowdata, rowindex)
                    {
                    },
                    enabledEdit: true, isScroll: false, checkbox:true,rownumbers:true,
                    data:data1,
                    width: '88%'
                });
            }
            $('#toolStdTable').on('click','.dele',function(){
                if (manager1.getData().length ==1){
                    alert("只剩一项时无法删除");
                }else {
                    manager1.deleteRow($(this).data("id"));
                }
            });
            $('#toolStdTable').on('click','.add',function(){
                //表格参数key必须填，哪怕value是null，否则getData()取不到值
                manager1.addRow({
                    "order_id":businessKey,
                    "tool_id":null,
                    "tool_num":null,
                    "subject_tool_model":null,
                    "tool_remark":null
                });
            });



            //备品备件
            var data2 = {
                Rows: [
                    {
                        "order_id":businessKey,
                        "part_id":null,
                        "part_num":null,
                        "part_remark":null
                    }
                ]
            };
            var manager2, g2;
            function f_initGrid2()
            {
                g2 =  manager2 = $("#attachmentStdTable").ligerGrid({
                    columns: [
                        { display: '备品备件', name: 'part_id',
                            editor: {
                                type: 'selectGrid',
                                data:[{id:0,name:"备件A"},{id:1,name:"备件B"},{id:2,name:"备件C"},{id:3,name:"备件D"}],
                                valueField: 'id',
                                textField: 'name',
                                onSelected: function (value)
                                {
                                }

                            },
                            render: function (item) {

                                var text = "";
                                $.each([{id:0,name:"备件A"},{id:1,name:"备件B"},{id:2,name:"备件C"},{id:3,name:"备件D"}], function (i, data) {
                                    if (data.id == item.part_id) {
                                        text = data.name;
                                    }
                                })
                                return text;
                            }
                        },
                        { display: '数量', name: 'part_num',
                            editor: { type: 'text' }
                        },

                        { display: '备注', name: 'part_remark',
                            editor: { type: 'text' }
                        },
                        { display: '操作', isSort: false,
                            render: function (rowdata, rowindex, value)
                            {
                                var h = "";
                                h += "<a class='add'>添加</a> ";
                                h += "<a class='dele' data-id='"+ rowindex +"'>删除</a> ";
                                return h;
                            }
                        }

                    ],
                    onSelectRow: function (rowdata, rowindex)
                    {
                    },
                    enabledEdit: true, isScroll: false, checkbox:true,rownumbers:true,
                    data:data2,
                    width: '88%'
                });
            }
            $('#attachmentStdTable').on('click','.dele',function(){
                if (manager2.getData().length ==1){
                    alert("只剩一项时无法删除");
                }else {
                    manager2.deleteRow($(this).data("id"));
                }
            });
            $('#attachmentStdTable').on('click','.add',function(){
                //表格参数key必须填，哪怕value是null，否则getData()取不到值
                manager2.addRow({
                    "order_id":businessKey,
                    "part_id":null,
                    "part_num":null,
                    "part_remark":null
                });
            });

            //人员工时
            var data3 = {
                Rows: [
                    {
                        "order_id":businessKey,
                        "emp_id":null,
                        "emp_hour":null,
                        "emp_charge":null,
                        "emp_remark":null
                    }
                ]
            };

            var empdata = null;
            $.ajax({
                type : "post",
                async : false, //同步执行
                url : ctx+"/sys/eamuser/getUsersSelect",
                dataType : "json", //传递数据形式为text
                success : function(data)
                {
                    empdata=data;
                }
            });

            var manager3, g3;
            function f_initGrid3()
            {
                g3 =  manager3 = $("#manhaurStdTable").ligerGrid({
                    columns: [
                        { display: '人员', name: 'emp_id',
                            editor: {
                                type: 'selectGrid',
                                data:empdata,
                                valueField: 'id',
                                textField: 'name',
                                onSelected: function (value)
                                {
                                }

                            },
                            render: function (item) {

                                var text = "";
                                $.each(empdata, function (i, data) {
                                    if (data.id == item.emp_id) {
                                        text = data.name;
                                    }
                                })
                                return text;
                            }
                        },
                        { display: '实际工时', name: 'emp_hour',
                            editor: { type: 'text' }
                        },
                        { display: '工时单价', name: 'emp_charge',
                            editor: { type: 'text' }
                        },
                        { display: '岗位技能', name: 'emp_skill',
                            editor: { type: 'text' }
                        },
                        { display: '备注', name: 'emp_remark',
                            editor: { type: 'text' }
                        },
                        { display: '操作', isSort: false,
                            render: function (rowdata, rowindex, value)
                            {
                                var h = "";
                                h += "<a class='add'>添加</a> ";
                                h += "<a class='dele' data-id='"+ rowindex +"'>删除</a> ";
                                return h;
                            }
                        }

                    ],
                    onSelectRow: function (rowdata, rowindex)
                    {
                    },
                    enabledEdit: true, isScroll: false, checkbox:true,rownumbers:true,
                    data:data3,
                    width: '88%'
                });
            }
            $('#manhaurStdTable').on('click','.dele',function(){
                if (manager3.getData().length ==1){
                    alert("只剩一项时无法删除");
                }else {
                    manager3.deleteRow($(this).data("id"));
                }
            });
            $('#manhaurStdTable').on('click','.add',function(){
                //表格参数key必须填，哪怕value是null，否则getData()取不到值

                manager3.addRow({
                    "order_id":businessKey,
                    "emp_id":null,
                    "emp_hour":null,
                    "emp_charge":null,
                    "emp_remark":null
                });
            });


            //其他费用
            var data4 = {
                Rows: [
                    {
                        "order_id":businessKey,
                        "charge_name":null,
                        "charge_price":null,
                        "charge_remark":null
                    }
                ]
            };
            var manager4, g4;
            function f_initGrid4()
            {
                g4 =  manager4 = $("#otherStdTable").ligerGrid({
                    columns: [
                        { display: '其他费用事项', name: 'charge_name',
                            editor: { type: 'text' }
                        },

                        { display: '金额', name: 'charge_price',
                            editor: { type: 'text' }
                        },
                        {
                            display: '备注', name: 'charge_remark',
                            editor: { type: 'text' }
                        },
                        { display: '操作', isSort: false,
                            render: function (rowdata, rowindex, value)
                            {
                                var h = "";
                                h += "<a class='add'>添加</a> ";
                                h += "<a class='dele' data-id='"+ rowindex +"'>删除</a> ";
                                return h;
                            }
                        }
                    ],
                    onSelectRow: function (rowdata, rowindex)
                    {
                    },
                    enabledEdit: true, isScroll: false, checkbox:true,rownumbers:true,
                    data:data4,
                    width: '88%'
                });
            }
            $('#otherStdTable').on('click','.dele',function(){
                if (manager4.getData().length ==1){
                    alert("只剩一项时无法删除");
                }else {
                    manager4.deleteRow($(this).data("id"));
                }
            });
            $('#otherStdTable').on('click','.add',function(){
                //表格参数key必须填，哪怕value是null，否则getData()取不到值
                manager4.addRow({
                    "order_id":businessKey,
                    "charge_name":null,
                    "charge_price":null,
                    "charge_remark":null
                });
            });

        });

    </script>
</head>
<body>
<div>
    <ul class="nav nav-tabs">
        <li role="presentation" class="active"><a href="#taskContent" role="tab" data-toggle="tab" aria-controls="home" aria-expanded="true">任务详情</a></li>
        <li role="presentation" ><a href="#flowchart" role="tab" data-toggle="tab" aria-controls="profile" aria-expanded="false">流程图</a></li>
    </ul>
    <div class="tab-content">
        <div id="taskContent" role="tabpanel" class="tab-pane active">
            <form  class="form-horizontal" action="" method="post" id="serviceForm">
                <table id="devSubjectTable" class="table table-bordered table-hover mytable">
                    <thead>
                    </thead>
                </table>
                <%--附件区域-图片--%>
                <div class="path">
                    <div id="imgarea">
                        <div class="fileimg"><label>上传图片</label><input type="text" name="mytext[]" id="img" readonly/><a href="#" class="uploadclass">上传</a>&nbsp;<a href="#" id="uploadmoreimg">新增</a></div>
                    </div>
                </div>
                <%--附件区域-视频--%>
                <div class="path">
                    <div id="vidarea">
                        <div class="filevid"><label>上传视频</label><input type="text" name="mytext[]" id="vid" readonly/><a href="#" class="uploadclass">上传</a>&nbsp;<a href="#" id="uploadmorevid">新增</a></div>
                    </div>
                </div>
            </form>
            <div class="editDiv">
                <div class="l-clear"></div>
                <div class="subeditDiv" id="toolStdTable" ></div>
                <div class="subeditDiv" id="attachmentStdTable" ></div>
                <div class="subeditDiv" id="manhaurStdTable" ></div>
                <div class="subeditDiv" id="otherStdTable" ></div>
            </div>

            <div class="control-group">
                <div class="controls form-actions">
                    <button type="" class="btn btn-primary" id="btnFeedBack"><i class="icon-ok"></i>完成任务</button>
                </div>
            </div>
        </div>
        <%--流程图tab页--%>
        <div id="flowchart" role="tabpanel" class="tab-pane">
            <img id="processDiagram" src="${ctx }/eam/act/process/read-resource?pdid=${processDefinition.id}&resourceName=${processDefinition.diagramResourceName}" />
        </div>
    </div>
</div>
</body>
</html>
