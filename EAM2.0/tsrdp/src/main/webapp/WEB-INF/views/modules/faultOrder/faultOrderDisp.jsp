<%@ page import="org.activiti.engine.form.FormType" %>
<%@ page import="org.activiti.engine.form.FormProperty" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<html>
<head>
    <title>标准库审批</title>
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
        var businessKey = '${businessKey}';

        businessKey = businessKey.substring(businessKey.indexOf(":")+1);
        var bsligerform;
        var empdata = null;
        $(function () {


            //表单提交
            $("#btnSubmit").on('click', function() {

                var form = liger.get('inputForm');

                debugger;
                //复杂表单用ajax提交
                var param=new Object();//对象存储object

                var order_plan_start_time=$("input[name='order_plan_start_time']").val();
                var order_plan_end_time=$("input[name='order_plan_end_time']").val();
                var order_receiver=$('#order_receiverBox').val();

                if(manager1 != undefined){
                    var tool_data = manager1.getData();
                    var attachment_data = manager2.getData();
                    var manhaur_data = manager3.getData();
                    var other_data = manager4.getData();

                    param.orderTool = tool_data;
                    param.orderAttachment = attachment_data;
                    param.orderManhaur = manhaur_data;
                    param.orderOther = other_data;
                }


                param.order_receiver = order_receiver;
                param.order_plan_start_time = order_plan_start_time;
                param.order_plan_end_time = order_plan_end_time;

                console.log("表单数据:");
                console.log(JSON.stringify(param));
                common.callAjax('post',false,'${ctx}/faultOrder/dispOrder',"text",{param:JSON.stringify(param),taskid:taskid},function(data){
                    debugger;
                    if(data=="success"){
                        layer.msg('派单成功！',{icon:1,time: 1000}, function(index){
                            parent.$("#mytable").DataTable().ajax.reload();
                            parent.layer.closeAll();
                        });
                    }else if(data=="repeat"){
                        layer.msg("编码重复！",{time: 1000,icon:7});
                    }else if (data == "timeout") {
                        layer.msg("登录超时或无权限！",{time: 1000,icon:7});
                    }else{
                        layer.msg("新增失败！",{time: 1000,icon:2});
                    }
                })
            });



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

            /**表单字段暂时写死，后期再通过接口查询*/
                //人员下拉列表
            var order_receiver = {display:"接单人员",editable:true,name:"order_receiver",type:"select",comboboxName:"order_receiverBox",
                    options:{
                        /*  data: empdata,
                         valueField: 'id',
                         textField: 'name',*/
                    }
                };

            //是否引入标准工作
            var isImportStand = {display:"引入标准工作包",editable:true,name:"isImportStand",type:"select",comboboxName:"isImportStandBox",
                options:{
                    data: [
                        { text: '是', id: '1' },
                        { text: '否', id: '0' }
                    ]
                }
            };

            //引入标准工作类型
            var importStandType = {display:"标准类型",editable:true,name:"importStandType",type:"select",comboboxName:"importStandTypeBox",
                options:{
                    data: [
                        { text: '标准工作', id: '1' },
                        { text: '标准工单', id: '2' }
                    ]
                }
            };

            //标准工作编码
            var standCode = {display:"类型编码",editable:true,name:"standCode",type:"select",comboboxName:"standCodeBox",
                options:{
                    /*data: [
                     { text: '是', id: '1' },
                     { text: '否', id: '0' }
                     ]*/
                }
            };

            //人员部门树
            /*var order_receiver = {display:"接单人员",editable:true,name:"order_receiver",type:"combobox",comboboxName:"order_receiverBox",
             options:{
             isMultiSelect: true,
             valueField: 'id',
             treeLeafOnly: true,
             tree: {
             url: common.interfaceUrl.getDeptUserTreeData,
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
             if (newvalue.data != undefined && 'dept' == newvalue.data.type) {
             layer.alert('该节点为部门，请选择人员');
             searchLigerForm.getEditor('order_receiver').setValue('');
             return false;
             }
             },
             }
             };*/


            //计划开始时间
            var order_plan_start_time = {display:"计划开始时间",editable:true,name:"order_plan_start_time",type:"dateTime",begintime:true};

            var order_plan_end_time = {display:"计划结束时间",editable:true,name:"order_plan_end_time",type:"dateTime",endtime:true};

            //工单实际明细
            var detail = {display:"工单计划明细",editable:true,name:"order_detail",type:"select",comboboxName:"order_detailBox",group: "",
                options:{
                    data: [
                        { text: '工器具', id: '0' },
                        { text: '备件材料', id: '1' },
                        { text: '人员工时', id: '2' },
                        { text: '其他费用', id: '3' }
                    ]
                }
            };

            var g_array = new Array({group:"操作信息"})
            formField = g_array.concat(formField);
            formField.push(order_receiver);
            formField.push(order_plan_start_time);
            formField.push(order_plan_end_time);
            formField.push(isImportStand);
            formField.push(importStandType);
            formField.push(standCode);
            formField.push(detail);

            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields: formField
            };
            bsligerform = $("#serviceForm").ligerForm(formConfig);

            empdata = FormUtil.initSelectBox(ctx+"/sys/eamuser/getUsersSelect?type=faultOrder&businessKey="+businessKey,"order_receiver",$("#order_receiverBox"),null,null,"id","name");

            initFormEvent();

            function initFormEvent() {
                $("#isImportStandBox").on('change',function () {
                    var value = $("#isImportStandBox").val();
                    if("1" == value){
                        //隐藏标准工作输入
                        bsligerform.setVisible('importStandType', true);
                        bsligerform.setVisible('standCode', true);
                    }else{
                        bsligerform.setVisible('importStandType', false);
                        bsligerform.setVisible('standCode', false);
                    }
                });

                //默认不引入标准工作包
                $("#isImportStandBox").val("0").trigger('change.select2');
                bsligerform.setVisible('importStandType', false);
                bsligerform.setVisible('standCode', false);

                $("#importStandTypeBox").on('change',function () {
                    var value = $("#importStandTypeBox").val();
                    if("1" == value){
                        //标准工作编码
                        FormUtil.initSelectBox(ctx+"/eam/operationwork/getApprovedWorkCodes","standCode",$("#standCodeBox"),null,null,"id","code");
                    }else{
                        //标准工单
                        FormUtil.initSelectBox(ctx+"/opestandard/standardOrder/getApprovedOrderCodes","standCode",$("#standCodeBox"),null,null,"id","code");
                    }
                });

                //值类型下拉切换
                $("#order_detailBox").on('change',function () {

                    //alert($("#subject_valuetypeBox").val());
                    if ($("#toolStdTable").children().length <1){
                        $(f_initGrid1);
                        $(f_initGrid2);
                        $(f_initGrid3);
                        $(f_initGrid4);
                    }

                    if($("#order_detailBox").val() =="0"){
                        $("#toolStdTable").show().siblings().hide();
                    }else if($("#order_detailBox").val() =="1"){
                        $("#attachmentStdTable").show().siblings().hide();
                    }else if($("#order_detailBox").val() =="2"){
                        $("#manhaurStdTable").show().siblings().hide();
                    }else{
                        $("#otherStdTable").show().siblings().hide();
                    }
                });
            }


            var data1 = {
                Rows: [
                    {
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
                        {
                            display: '操作', isSort: false,
                            render: function (rowdata, rowindex, value) {
                                var h = "";
                                h += "<a class='add'>添加</a> ";
                                h += "<a  class='del' data-id='" + rowindex + "'>删除</a> ";
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

                $('#toolStdTable').on('click', '.add', function () {
                    //表格参数key必须填，哪怕value是null，否则getData()取不到值
                    manager1.addRow({});
                });
                $('#toolStdTable').on('click','.del',function(){
                    if (manager1.getData().length == 1) {
                        layer.msg("首行不能删除",{time: 1000,icon:7});
                    } else {
                        manager1.deleteRow($(this).data("id"));
                    }
                })

            }



            //备品备件
            var data2 = {
                Rows: [
                    {
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
                                    //alert(value)
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
                        {
                            display: '操作', isSort: false,
                            render: function (rowdata, rowindex, value) {
                                var h = "";
                                h += "<a class='add'>添加</a> ";
                                h += "<a  class='del' data-id='" + rowindex + "'>删除</a> ";
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

                $('#attachmentStdTable').on('click', '.add', function () {
                    manager2.addRow({});
                });
                $('#attachmentStdTable').on('click','.del',function(){
                    if (manager2.getData().length == 1) {
                        layer.msg("首行不能删除",{time: 1000,icon:7});
                    } else {
                        manager2.deleteRow($(this).data("id"));
                    }
                })
            }

            //人员工时
            var data3 = {
                Rows: [
                    {
                        "emp_id":null,
                        "hour":null,
                        "charge":null,
                        "remark":null
                    }
                ]
            };



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
                                    //alert(value)
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
                        { display: '预计工时', name: 'hour',
                            editor: { type: 'text' }
                        },
                        { display: '工时单价', name: 'charge',
                            editor: { type: 'text' }
                        },
                        { display: '备注', name: 'remark',
                            editor: { type: 'text' }
                        },
                        {
                            display: '操作', isSort: false,
                            render: function (rowdata, rowindex, value) {
                                var h = "";
                                h += "<a class='add'>添加</a> ";
                                h += "<a  class='del' data-id='" + rowindex + "'>删除</a> ";
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
                $('#manhaurStdTable').on('click', '.add', function () {
                    //表格参数key必须填，哪怕value是null，否则getData()取不到值
                    manager3.addRow({});
                });
                $('#manhaurStdTable').on('click','.del',function(){
                    if (manager3.getData().length == 1) {
                        layer.msg("首行不能删除",{time: 1000,icon:7});
                    } else {
                        manager3.deleteRow($(this).data("id"));
                    }
                })
            }


            //其他费用
            var data4 = {
                Rows: [
                    {
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
                        {
                            display: '操作', isSort: false,
                            render: function (rowdata, rowindex, value) {
                                var h = "";
                                h += "<a class='add'>添加</a> ";
                                h += "<a  class='del' data-id='" + rowindex + "'>删除</a> ";
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
                $('#otherStdTable').on('click', '.add', function () {
                    //表格参数key必须填，哪怕value是null，否则getData()取不到值
                    manager4.addRow({});
                });
                $('#otherStdTable').on('click','.del',function(){
                    if (manager4.getData().length == 1) {
                        layer.msg("首行不能删除",{time: 1000,icon:7});
                    } else {
                        manager4.deleteRow($(this).data("id"));
                    }
                })
            }




        });

    </script>
    <script>
        $(function () {
            try{
                //创建表单结构
                var baseligerForm = $("#baseForm").ligerForm({
                    space : 40,
                    labelWidth : 120 ,
                    inputWidth : 200,
                    fields: [
                        {display:"工单编码",editable:true,name:"order_code_display",type:"labelText",group: "基本信息"},
                        {display:"报修位置",editable:true,name:"order_location_display",type:"labelText",group: "基本信息"},
                        {display:"报修设备",editable:true,name:"order_device_display",type:"labelText",group: "基本信息"},
                        {display:"报修人员",editable:true,name:"order_notify_display",type:"labelText",group: "基本信息"},
                        {display:"报修时间",editable:true,name:"order_notify_time_display",type:"labelText",group: "基本信息"},
                        {display:"故障现象",editable:true,name:"appearance_display",type:"labelText",group: "基本信息"},
                    ]
                });
                debugger;
                var pa = new Object();
                pa.pstid = executionId;
                //查询工单基本信息
                common.callAjax('post',false,ctx + "/faultOrder/getOrderDetail","json",pa,function(data){
                    debugger;
                    if(data != null){
                        baseligerForm.getEditor('order_code_display').setValue(data.order_code);
                        baseligerForm.getEditor('appearance_display').setValue(data.notifier_appearance);
                        baseligerForm.getEditor('order_device_display').setValue(data.order_device.dev_name);
                        baseligerForm.getEditor('order_location_display').setValue(data.notifier_loc.loc_name);
                        baseligerForm.getEditor('order_notify_display').setValue(data.notifier);
                        baseligerForm.getEditor('order_notify_time_display').setValue(data.createDate);
                    }
                });



            }catch (e){
                console.error(e);
            }

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
            <div id="baseForm" class="liger-form"></div>
            <form  class="form-horizontal" action="" method="post" id="serviceForm">

            </form>
            <div class="editDiv">
                <div class="l-clear"></div>
                <div class="subeditDiv" id="toolStdTable" ></div>
                <div class="subeditDiv" id="attachmentStdTable" ></div>
                <div class="subeditDiv" id="manhaurStdTable" ></div>
                <div class="subeditDiv" id="otherStdTable" ></div>
            </div>

            <%-- 按钮区域 --%>
            <div class="control-group">
                <div class="controls form-actions">
                    <button type="" class="btn btn-primary" id="btnSubmit"><i class="icon-ok"></i>完成任务</button>
                </div>
            </div>

        </div>
        <div id="flowchart" role="tabpanel" class="tab-pane">
            <img id="processDiagram" src="${ctx }/eam/act/process/read-resource?pdid=${processDefinition.id}&resourceName=${processDefinition.diagramResourceName}" />
        </div>

    </div>
</div>
</body>
</html>
