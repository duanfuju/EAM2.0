<%@ page import="org.activiti.engine.form.FormType" %>
<%@ page import="org.activiti.engine.form.FormProperty" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp"%>
<html>
<head>
	<title>标准库审批</title>
	<style type="text/css">
		#pinfo th {background: #f7f7f9;}
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

            var typeDate=[{id:0,name:"设备运维标准",pid:""},{id:1,name:"设备运行标准",pid:""},{id:2,name:"设备安全标准",pid:""},{id:3,name:"故障检修标准",pid:"0"},{id:4,name:"保养标准",pid:"0"},{id:5,name:"巡检标准",pid:"0"},{id:6,name:"缺陷故障库",pid:"0"}];
            var formFieldnew=[{display:"标准库编码",name:"library_code",type:"text"},{display:"标准库名称",name:"library_name",type:"text"},
                { display:"标准库类型",name:"library_type",comboboxName:"library_typeBox","type": "combobox",
                    options:{
                        isMultiSelect : true,
                        valueField : 'id',
                        textField: 'name',
                        treeLeafOnly : false,
                        tree : {
                            //url :  ,
                            data:typeDate, //存在数据累加
                            checkbox : false,
                            parentIcon: null,
                            childIcon: null,
                            idFieldName : 'id',
                            parentIDFieldName : 'pid',
                            textFieldName:'name',//文本字段名，默认值text
                            nodeWidth:200,
                            ajaxType : 'post',
                            onClick : function (note) {
                                if ($("#maintainStdTable").children().length <1){
                                    $(f_initGrid2);
                                    $(f_initGrid3);
                                    $(f_initGrid4);
                                    $(f_initGrid5);
                                    $(f_initGrid6);
                                }
                                if(note.data.id =="3" || note.data.id =="0"){
                                    $("#faultStdTable").show().siblings().hide();
                                }else if(note.data.id =="4"){
                                    $("#maintainStdTable").show().siblings().hide();
                                }else if(note.data.id =="5"){
                                    $("#patrolStdTable").show().siblings().hide();
                                }else if(note.data.id =="6"){
                                    $("#failureStdTable").show().siblings().hide();
                                }else if(note.data.id =="1"){
                                    $("#operationStdTable").show().siblings().hide();
                                }else if(note.data.id =="2") {
                                    $("#safeStdTable").show().siblings().hide();
                                }
                            }
                        }
                    }
                }]
            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields: formFieldnew

            };
            $("#serviceForm").ligerForm(formConfig);
            //给标准库类型初始化
            $('input[name="library_typeBox"]').val(typeDate[3].name);
            //给表单赋值
            var data1,data2,data3,data4,data5,data6;
            $.ajax({
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
			 })
            $(f_initGrid1);
            var manager1, g1;
            function f_initGrid1()
            {
                g1 =  manager1 = $("#faultStdTable").ligerGrid({
                    columns: [
                        { display: '检修标准编码', name: 'fault_standard_code',
                             type: 'text'
                        },
                        { display: '检修标准描述', name: 'fault_standard_desc',
                             type: 'text'
                        },
                        { display: '检修标准说明', name: 'fault_standard_explain',
                             type: 'text'
                        },
                        { display: '备注', name: 'fault_standard_remark',
                             type: 'text'
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

            //保养标准
            var manager2, g2;
            function f_initGrid2()
            {
                g2 =  manager2 = $("#maintainStdTable").ligerGrid({
                    columns: [
                        { display: '保养标准编码', name: 'maintain_standard_code',
                            type: 'text'
                        },
                        { display: '保养标准描述', name: 'maintain_standard_desc',
                             type: 'text'
                        },
                        { display: '保养标准说明', name: 'maintain_standard_explain',
                             type: 'text'
                        },
                        { display: '备注', name: 'maintain_standard_remark',
                             type: 'text'
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

            //巡检标准
            var manager3, g3;
            function f_initGrid3()
            {
                g3 =  manager3 = $("#patrolStdTable").ligerGrid({
                    columns: [
                        { display: '巡检标准编码', name: 'patrol_standard_code',
                            type: 'text'
                        },
                        { display: '巡检标准描述', name: 'patrol_standard_desc',
                             type: 'text'
                        },
                        { display: '巡检标准说明', name: 'patrol_standard_explain',
                             type: 'text'
                        },
                        { display: '备注', name: 'patrol_standard_remark',
                             type: 'text'
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


            //缺陷故障
            var manager4, g4;
            function f_initGrid4()
            {
                g4 =  manager4 = $("#failureStdTable").ligerGrid({
                    columns: [
                        { display: '故障现象编码', name: 'failure_phenomena_code',
                            type: 'text'
                        },
                        { display: '优先级', name: 'failure_phenomena_priority',
                            type: 'selectGrid', data: [{ priority: 0, text: '高' }, { priority: 1, text: '中'},{ priority: 2, text: '低'}],
                                valueField: 'priority',textField: 'text',
                            render: function (item) {
                                var text = "";
                                $.each([{ priority: 0, text: '高' }, { priority: 1, text: '中'},{ priority: 2, text: '低'}], function (i, data) {
                                    if (data.priority == item.failure_phenomena_priority) {
                                        text = data.text;
                                    }
                                })
                                return text;
                            }
                        },
                        { display: '故障现象描述', name: 'failure_phenomena_desc',
                             type: 'text'
                        },
                        { display: '故障原因编码', name: 'failure_cause_code',
                             type: 'text'
                        },
                        { display: '严重程度', name: 'failure_cause_serverity',
                             type: 'selectGrid', data: [{ serverity: 0, text: '非常严重' }, { serverity: 1, text: '严重'},{ serverity: 2, text: '一般'}],
                                valueField: 'serverity',textField: 'text',
                            render: function (item) {
                                var text = "";
                                $.each([{ serverity: 0, text: '非常严重' }, { serverity: 1, text: '严重'},{ serverity: 2, text: '一般'}], function (i, data) {
                                    if (data.serverity == item.failure_cause_serverity) {
                                        text = data.text;
                                    }
                                })
                                return text;
                            }
                        },
                        { display: '故障原因描述', name: 'failure_cause_desc',
                            type: 'text'
                        },
                        {
                            display: '故障措施编码', name: 'failure_measures_code',
                             type: 'text'
                        },
                        {
                            display: '故障措施描述', name: 'failure_measures_desc',
                             type: 'text'
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



            //运行标准
            var manager5, g5;
            function f_initGrid5()
            {
                g5 =  manager5 = $("#operationStdTable").ligerGrid({
                    columns: [
                        { display: '运行标准编码', name: 'operation_standard_code',
                            type: 'text'
                        },
                        { display: '运行标准描述', name: 'operation_standard_desc',
                             type: 'text'
                        },
                        { display: '运行标准说明', name: 'operation_standard_explain',
                             type: 'text'
                        },
                        { display: '备注', name: 'operation_standard_remark',
                             type: 'text'
                        }


                    ],
                    onSelectRow: function (rowdata, rowindex)
                    {
                    },
                    enabledEdit: true, isScroll: false, checkbox:true,rownumbers:true,
                    data:data5,
                    width: '88%'
                });
            }


            //安全标准
            var manager6, g6;
            function f_initGrid6()
            {
                g6 =  manager6 = $("#safeStdTable").ligerGrid({
                    columns: [
                        { display: '安全标准编码', name: 'safety_standard_code',
                             type: 'text'
                        },
                        { display: '安全标准描述', name: 'safety_standard_desc',
                            type: 'text'
                        },
                        { display: '安全标准说明', name: 'safety_standard_explain',
                            type: 'text'
                        },
                        { display: '备注', name: 'safety_standard_remark',
                             type: 'text'
                        }

                    ],
                    onSelectRow: function (rowdata, rowindex)
                    {
                    },
                    enabledEdit: true, isScroll: false, checkbox:true,rownumbers:true,
                    data:data6,
                    width: '88%'
                });
            }
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
	<div class="subeditDiv" id="faultStdTable" ></div>
	<div class="subeditDiv" id="maintainStdTable" ></div>
	<div class="subeditDiv" id="patrolStdTable" ></div>
	<div class="subeditDiv" id="failureStdTable" ></div>
	<div class="subeditDiv" id="operationStdTable" ></div>
	<div class="subeditDiv" id="safeStdTable"></div>
</div>
<%--action="${ctx }/eam/act/task/complete?taskid=${task.id}"--%>
<form  class="form-horizontal" action="${ctx }/eam/act/task/complete?taskid=${task.id}" method="post" id="inputForm">
	<%--<input type="hidden" value="${task.id}" id="taskid">--%>
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
			<%--</c:if>
			<c:if test="${empty task.assignee}">
				<a class="btn" id="claim"><i class="icon-ok"></i>签收</a>
			</c:if>--%>
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
