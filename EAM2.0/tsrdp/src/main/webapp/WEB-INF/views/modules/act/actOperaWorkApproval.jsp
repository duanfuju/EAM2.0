<%@ page import="org.activiti.engine.form.FormType" %>
<%@ page import="org.activiti.engine.form.FormProperty" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp"%>
<html>
<head>
    <title>标准工作审批</title>
    <style type="text/css">
        .editDiv .l-panel-bar{
            display: none ;
        }
        #flowstatus tr {
            border: 1px solid rgba(34, 34, 34, 0.17);
        }
        #flowstatus td,#flowstatus th{
            width: 1%;
            height: 40px;
        }
        #flowstatus .blue{
            background-color: rgba(21, 199, 189, 0.19);
        }
    </style>
    <meta name="decorator" content="default"/>

    <script type="text/javascript" src="/js/bootstrap.min.js"></script>

    <script type="text/javascript">
        var operationworkTypeSelect;
        var statusSelect;
        //标准工作状态下拉初始化
        var statusHtml = "";
        //标准工作类型下拉初始化
        var typeHtml = "";
        // 工序表格数据
        var procedureTable = null;
        // 安全措施表格数据
        var safetyTable = null;
        // 工器具表格数据
        var toolsTable = null;
        // 备品备件表格数据
        var sparepartsTable = null;
        // 人员工时表格数据
        var personhoursTable = null;
        // 其他费用表格数据
        var othersTable = null;
        // 标准工作id
        var operaworkId = null;
        // 初始化工器具和备品备件的下拉框列表数据
        var materialSelect = null;
        // 人员下拉数据
        var personSelect = null;

        var executionId = '${task.executionId}';

        $(function () {
            //完整的渲染方法
            var data='${activityList}';

            data=JSON.parse(data);
            console.log(data);
            function showData(data) {
                //debugger;
                var dom = '';
                $.each(data,function(index,e){
                    dom += '<tr class="blue"><td>'+ (index+1) +'</td><td>' + e.node +
                        '</td><td>操作者总计:' + e.operatorNum +
                        '</td><td>已提交:' + e.submit +
                        '</td><td></td><td></td></tr>' +
                        '<tr class="operator"><td></td><td>操作人</td><td>操作状态</td><td>接收时间</td><td>操作时间</td><td>操作损耗</td></tr>';
                    $.each(e.detail,function(index,i){
                        var a = "<tr><td></td><td>" + i.operator +
                            "</td><td>" + i.status +
                            "</td><td>" + i.accepttime +
                            "</td><td>" + i.submittime +
                            "</td><td>" + i.duration + "</td></tr>";
                        dom += a;
                    })
                })
                $('table').append(dom);
            }
            showData(data);

            //表单提交
            $("#btnSubmit").on('click', function() {

                $("#inputForm").on("submit", function () {

                    $(this).ajaxSubmit({
                        type: 'post',
                        success: function (data) {
                            if (data == "success") {
                                layer.msg('提交成功！',{icon:1,time: 1000}, function (index) {
                                    parent.$("#mytable").DataTable().ajax.reload();
                                    parent.layer.closeAll();
                                });
                            } else if (data == "noauth") {
                                layer.msg("无审批权限，请先签收任务！",{time: 1000,icon:7});
                            } else {
                                layer.msg("提交失败！",{time: 1000,icon:2});
                            }
                        }
                    });
                    return false;
                });
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

            var formField;

            // 获取页面标准工作（页面编号是1065）要显示的内容
            common.callAjax('POST', false, ctx + "/eam/device/getfields", 'json', {menuno: "1065"}, function (data) {
                formField = data.formfield;

                $.each(formField, function (index, val) {
                    val.readonly = true;
                    if (val.name == 'device_name' || val.name == 'device_major') {
                        val.type="hidden";
                    }
                });
            });

            var length = formField.length;
            formField[length] = {
                editable: "true",
                display: "标准工作明细",
                name: "standWorkDetailTab",
                comboboxName: "standWorkDetailTabBox",
                type: "select"
            };

            //创建表单结构
            var formConfig = {
                space: 50, labelWidth: 120, inputWidth: 200,
                validate: true,
                fields: formField
            };
            $("#serviceForm").ligerForm(formConfig);

            var operationwork_typeBox = $("#operationwork_typeBox");
            var operationwork_statusBox = $("#operationwork_statusBox");

            /**标准类型下拉数据**/
            common.callAjax('post',true,ctx + "/eam/bizDict/getValues","json",{"dict_type_code" : "operationwork_type"},function(data){
                operationworkTypeSelect = data;

                $.each(operationworkTypeSelect, function (i, item) {
                    typeHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                operationwork_typeBox .html(typeHtml);
                operationwork_typeBox.append($("<input></input>").attr("type", "hidden").attr("name", "operationwork_type"));//设置隐藏域
                operationwork_typeBox.trigger('change.select2');
                operationwork_typeBox.prop("disabled", true);
            });
            /**状态下拉数据**/
            common.callAjax('post',true,ctx + "/eam/bizDict/getValues","json",{"dict_type_code" : "common"},function(data){
                statusSelect = data;
                $.each(statusSelect, function (i, item) {
                    statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                operationwork_statusBox .html(statusHtml);
                operationwork_statusBox.append($("<input></input>").attr("type", "hidden").attr("name", "operationwork_status"));//设置隐藏域
                operationwork_statusBox.val(1);//设置默认值:1-有效
                operationwork_statusBox.trigger('change.select2');
                operationwork_statusBox.prop("disabled", true);

            });

            common.callAjax('post',false,common.interfaceUrl.toolsInfoData,"json",null,function(data){
                materialSelect = data;
            });
            common.callAjax('post',false,common.interfaceUrl.userExtGetAllUser,"json",null,function(data){
                personSelect = data;
            });

            var tableData = [{text: '工序', id: '0'}, {text: '安全措施', id: '1'}, {text: '工器具', id: '2'},
                {text: '备品备件', id: '3'}, {text: '人员工时', id: '4'}, {text: '其他费用', id: '5'}];
            var optionHtml = "";
            $.each(tableData, function (i, item) {
                optionHtml += "<option value=\"" + item.id + "\">" + item.text + "</option>";
            });
            $("#standWorkDetailTabBox").html(optionHtml);
            $("#standWorkDetailTabBox").val("0").trigger("change");

            common.callAjax('post', false, ctx + "/eam/operationwork/getOperaworkIdByPIid", "json", {pIid: $("#pIid").val()}, function (data) {
                operaworkId = data.id;
                //给编辑页面字段赋值
                common.callAjax('post', true, ctx + "/eam/operationwork/editObj", "json", {"id": operaworkId}, function (data) {
                    var editForm  = liger.get("serviceForm");
                    editForm.setData(data);
                    operationwork_statusBox.val(data.operationwork_status).trigger('change.select2');//根据详情渲染下拉框页面显示值
                    operationwork_typeBox.val(data.operationwork_type).trigger('change.select2');    //根据详情渲染下拉框页面显示值
                });

                /**标准工作工序表格数据**/
                common.callAjax('post', false, common.interfaceUrl.workProcedure, "json", {"operationwork_id": operaworkId}, function (data) {
                    procedureTable = data;
                });
                /**标准工作安全措施表格数据**/
                common.callAjax('post', false, common.interfaceUrl.workSafety, "json", {"operationwork_id": operaworkId}, function (data) {
                    safetyTable = data;
                });
                /**标准工作工器具表格数据**/
                common.callAjax('post', false, common.interfaceUrl.workTools, "json", {"operationwork_id": operaworkId}, function (data) {
                    toolsTable = data;
                });
                /**标准工作备品备件表格数据**/
                common.callAjax('post', false, common.interfaceUrl.workSpareparts, "json", {"operationwork_id": operaworkId}, function (data) {
                    sparepartsTable = data;
                });
                /**标准工作人员工时表格数据**/
                common.callAjax('post', false, common.interfaceUrl.workPersonHours, "json", {"operationwork_id": operaworkId}, function (data) {
                    personhoursTable = data;
                });
                /**标准工作其他费用表格数据**/
                common.callAjax('post', false, common.interfaceUrl.workOtherexpenses, "json", {"operationwork_id": operaworkId}, function (data) {
                    othersTable = data;
                });
            });


            // procedure    工序列表
            var procedure = $("#procedure").ligerGrid({
                columns: [
                    { display: '工序编码', name: 'procedure_code', type: 'text'},
                    { display: '描述', name: 'procedure_desc', type: 'text'},
                    { display: '质检标准', name: 'procedure_standard',type: 'text'},
                    { display: '备注', name: 'procedure_remark'}
                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                data:{Rows: procedureTable},
                width: '98%'
            });

            // safety    安全措施列表
            var safety = $("#safety").ligerGrid({
                columns: [
                    { display: '安全措施编码', name: 'safety_code', type: 'text'},
                    { display: '描述', name: 'safety_desc', type: 'text'},
                    { display: '质检标准', name: 'safety_standard',type: 'text'},
                    { display: '备注', name: 'safety_remark'}
                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                data:{Rows: safetyTable},
                width: '98%'
            });
            // 安全措施表格默认隐藏
            $("#safety").hide();

            // tools   工器具列表
            var tools = $("#tools").ligerGrid({
                columns: [
                    { display: '工器具',  name: 'material_id',type:'text',
                        render: function (item)
                        {
                            var material_name;
                            if(item != null && item.material_id != null && item.material_id != ""){
                                $.each(materialSelect, function (i, data) {
                                    if(data.material_id == item.material_id){
                                        material_name = data.material_name;
                                    }
                                });
                            }
                            return material_name;
                        }
                    },
                    { display: '数量', name: 'tools_num', type: 'text'},
                    { display: '计量单位', name: 'material_unit',
                        render: function (rowdata, rowindex, value) {
                            var material_unit;
                            $.each(materialSelect, function (i, data) {
                                if(data.material_id == rowdata.material_id){
                                    material_unit = data.material_unit;
                                }
                            });
                            return material_unit;
                        }
                    },
                    { display: '规格型号', name: 'material_model',
                        render: function (rowdata, rowindex, value) {
                            var material_model;
                            $.each(materialSelect, function (i, data) {
                                if(data.material_id == rowdata.material_id){
                                    material_model = data.material_model;
                                }
                            });
                            return material_model;
                        }
                    },
                    { display: '备注', name: 'tools_remark'}
                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                data:{Rows: toolsTable},
                width: '98%'
            });
            // 其他费用表格默认隐藏
            $("#tools").hide();

            // spareparts   备品备件列表
            var spareparts = $("#spareparts").ligerGrid({
                columns: [
                    { display: '备品备件',  name: 'material_id',type:'text',
                        render: function (item)
                        {
                            var material_name;
                            if(item != null && item.material_id != null && item.material_id != ""){
                                $.each(materialSelect, function (i, data) {
                                    if(data.material_id == item.material_id){
                                        material_name = data.material_name;
                                    }
                                });
                            }
                            return material_name;
                        }
                    },
                    { display: '数量', name: 'spareparts_num', type: 'text'},
                    { display: '计量单位', name: 'material_unit',
                        render: function (rowdata, rowindex, value) {
                            var material_unit;
                            $.each(materialSelect, function (i, data) {
                                if(data.material_id == rowdata.material_id){
                                    material_unit = data.material_unit;
                                }
                            });
                            return material_unit;
                        }
                    },
                    { display: '单价', name: 'material_price',
                        render: function (rowdata, rowindex, value) {
                            var material_price;
                            $.each(materialSelect, function (i, data) {
                                if(data.material_id == rowdata.material_id){
                                    material_price = data.material_price;
                                }
                            });
                            return material_price;
                        }
                    },
                    { display: '小计',name:'spareparts_total', isSort: false,type: 'text' },
                    { display: '备注', name: 'spareparts_remark', type: 'text', editor: { type: 'text'} }
                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                data:{Rows: sparepartsTable},
                width: '98%'
            });
            // 备品备件表格默认隐藏
            $("#spareparts").hide();

            // 人员工时列表
            var person = $("#person").ligerGrid({
                columns: [
                    { display: '人员',  name: 'loginname',type:'text',
                        render: function (item)
                        {
                            var realname;
                            if(item != null && item.loginname != null && item.loginname != ""){
                                $.each(personSelect, function (i, data) {
                                    if(data.loginname == item.loginname){
                                        realname = data.b_realname;
                                    }
                                });
                            }
                            return realname;
                        }
                    },
                    { display: '额定工时', name: 'person_hours', type: 'text'},
                    { display: '额定工时单价', name: 'person_hourprice', type: 'text'},
                    { display: '小计', name: 'person_hourtotal', type: 'text'},
                    { display: '岗位技能', name: 'person_postskill'},
                    { display: '备注', name: 'person_remark'}
                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                data:{Rows: personhoursTable},
                width: '98%'
            });
            // 人员工时表格默认隐藏
            $("#person").hide();

            // otherexpenses    其他费用列表
            var otherexpenses = $("#otherexpenses").ligerGrid({
                columns: [
                    { display: '其他费用事项', name: 'otherexpenses', type: 'text'},
                    { display: '金额', name: 'otherexpenses_amount', type: 'text'},
                    { display: '备注', name: 'otherexpenses_remark',type: 'text'}
                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                data:{Rows: othersTable},
                width: '98%'
            });
            // 其他费用表格默认隐藏
            $("#otherexpenses").hide();

            // 设置页面的编辑框为不可编辑模式
            $("input[name='operationwork_content']").prop("disabled", true);
            $("input[name='device_major']").prop("disabled", true);
            $("input[name='operationwork_totaltime']").prop("disabled", true);

            //下拉框选中事件
            $("#standWorkDetailTabBox").on('change',function () {
                if($("#standWorkDetailTabBox").val() == "0"){
                    $("#procedure").show();
                    $("#safety").hide();
                    $("#tools").hide();
                    $("#spareparts").hide();
                    $("#person").hide();
                    $("#otherexpenses").hide();
                } else if($("#standWorkDetailTabBox").val() == "1"){
                    $("#procedure").hide();
                    $("#safety").show();
                    $("#tools").hide();
                    $("#spareparts").hide();
                    $("#person").hide();
                    $("#otherexpenses").hide();
                } else if($("#standWorkDetailTabBox").val() == "2"){
                    $("#procedure").hide();
                    $("#safety").hide();
                    $("#tools").show();
                    $("#spareparts").hide();
                    $("#person").hide();
                    $("#otherexpenses").hide();
                } else if($("#standWorkDetailTabBox").val() == "3"){
                    $("#procedure").hide();
                    $("#safety").hide();
                    $("#tools").hide();
                    $("#spareparts").show();
                    $("#person").hide();
                    $("#otherexpenses").hide();
                } else if($("#standWorkDetailTabBox").val() == "4"){
                    $("#procedure").hide();
                    $("#safety").hide();
                    $("#tools").hide();
                    $("#spareparts").hide();
                    $("#person").show();
                    $("#otherexpenses").hide();
                } else if($("#standWorkDetailTabBox").val() == "5"){
                    $("#procedure").hide();
                    $("#safety").hide();
                    $("#tools").hide();
                    $("#spareparts").hide();
                    $("#person").hide();
                    $("#otherexpenses").show();
                }
            });
        });
    </script>
</head>
<body>
<div>
    <ul class="nav nav-tabs">
        <li role="presentation" class="active"><a href="#taskContent" role="tab" data-toggle="tab" aria-controls="home" aria-expanded="true">任务详情</a></li>
        <li role="presentation" ><a href="#flowchart" role="tab" data-toggle="tab" aria-controls="profile" aria-expanded="false">流程图</a></li>
        <li role="presentation" ><a href="#flowstatus" role="tab" data-toggle="tab" aria-controls="profile" aria-expanded="false">流程状态</a></li>
    </ul>
    <div class="tab-content">
        <div id="taskContent" role="tabpanel" class="tab-pane active">
            <form  class="form-horizontal" action="" method="post" id="serviceForm">

            </form>
            <div class="editDiv">
                <div class="l-clear"></div>
                <div class="subeditDiv" id="procedure"></div>
                <div class="subeditDiv" id="safety"></div>
                <div class="subeditDiv" id="tools"></div>
                <div class="subeditDiv" id="spareparts"></div>
                <div class="subeditDiv" id="person"></div>
                <div class="subeditDiv" id="otherexpenses"></div>
            </div>
            <form  class="form-horizontal" action="${ctx }/eam/act/task/complete?taskid=${task.id}" method="post" id="inputForm">
                <%--<h4>任务内容</h4>--%>
                <c:if test="${hasFormKey}">
                    ${taskFormData}
                </c:if>
                <c:if test="${!hasFormKey}">
                    <c:forEach items="${taskFormData.formProperties}" var="fp">
                        <c:set var="fpo" value="${fp}"/>
                        <c:set var="disabled" value="${fp.writable ? '' : 'disabled'}" />
                        <c:set var="readonly" value="${fp.writable ? '' : 'readonly'}" />
                        <c:set var="required" value="${fp.required ? 'required' : ''}" />
                        <%
                            // 把需要获取的属性读取并设置到pageContext域
                            FormType type = ((FormProperty)pageContext.getAttribute("fpo")).getType();
                            String[] keys = {"datePattern", "values"};
                            for (String key: keys) {
                                pageContext.setAttribute(key, type.getInformation(key));
                            }
                        %>
                        <div class="control-group">
                                <%-- 文本或者数字类型 --%>
                            <c:if test="${fp.type.name == 'string' || fp.type.name == 'long' || fp.type.name == 'double'}">
                                <label class="control-label" for="${fp.id}">${fp.name}:</label>
                                <div class="controls">
                                    <input type="text" id="${fp.id}" name="${fp.id}" data-type="${fp.type.name}" value="${fp.value}" ${readonly} ${required} />
                                </div>
                            </c:if>

                                <%-- 大文本 --%>
                            <c:if test="${fp.type.name == 'bigtext'}">
                                <label class="control-label" for="${fp.id}">${fp.name}:</label>
                                <div class="controls">
                                    <textarea id="${fp.id}" name="${fp.id}" data-type="${fp.type.name}" ${readonly} ${required}>${fp.value}</textarea>
                                </div>
                            </c:if>

                                <%-- 日期 --%>
                            <c:if test="${fp.type.name == 'date'}">
                                <label class="control-label" for="${fp.id}">${fp.name}:</label>
                                <div class="controls">
                                    <input type="text" id="${fp.id}" name="${fp.id}" class="datepicker" value="${fp.value}" data-type="${fp.type.name}" data-date-format="${fn:toLowerCase(datePattern)}" ${readonly} ${required}/>
                                </div>
                            </c:if>

                                <%-- 下拉框 --%>
                            <c:if test="${fp.type.name == 'enum'}">
                                <label class="control-label" for="${fp.id}">${fp.name}:</label>
                                <div class="controls">
                                    <select name="${fp.id}" id="${fp.id}" ${disabled} ${required}>
                                        <c:forEach items="${values}" var="item">
                                            <option value="${item.key}" <c:if test="${item.value == fp.value}">selected</c:if>>${item.value}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </c:if>

                                <%-- Javascript --%>
                            <c:if test="${fp.type.name == 'javascript'}">
                                <script type="text/javascript">${fp.value};</script>
                            </c:if>

                        </div>
                    </c:forEach>
                </c:if>

                <%-- 按钮区域 --%>
                <div class="control-group">
                    <div class="controls">

                        <input type="hidden" value="${task.id}" id="taskid">
                        <input type="hidden" value="${pIid}" id="pIid" name="pIid">
                        <%--<c:if test="${not empty task.assignee}">--%>
                        <button type="submit" class="btn btn-primary" id="btnSubmit"><i class="icon-ok"></i>完成任务</button>
                        <%--</c:if>--%>
                        <%--<c:if test="${empty task.assignee}">--%>
                            <%--<a class="btn" id="claim"><i class="icon-ok"></i>签收</a>--%>
                        <%--</c:if>--%>
                    </div>
                </div>
            </form>
        </div>
        <div id="flowchart" role="tabpanel" class="tab-pane">
            <img id="processDiagram" src="${ctx }/eam/act/process/read-resource?pdid=${processDefinition.id}&resourceName=${processDefinition.diagramResourceName}" />
        </div>
        <div id="flowstatus" role="tabpanel" class="tab-pane">
            <div>
                <table>
                    <tr>
                        <th>序号</th>
                        <th>节点</th>
                        <th>操作情况统计</th>
                        <th></th>
                        <th></th>
                        <th></th>
                    </tr>

                </table>
            </div>
        </div>
    </div>
</div>

</body>
</html>
