<%@ page import="org.activiti.engine.form.FormType" %>
<%@ page import="org.activiti.engine.form.FormProperty" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<html>
<head>
	<title>物料信息管理</title>
	<style type="text/css">
		#pinfo th {background: #f7f7f9;}
		.menu{
			display: flex;
			margin-top: 10px;
		}
		.menu>div{
			width: 100px;
			height: 100px;
			border: 1.5px solid #d2d6de;
			border-radius: 5px;
			text-align: center;
			margin-right: 10px;
			margin-left: 10px;
		}
		.menu li {
			padding-top: 24px;
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

	<%--<script src="/resource/plugins/jQuery/jquery-2.2.3.min.js" type="text/javascript"></script>--%>
	<script type="text/javascript" src="/js/bootstrap.min.js"></script>

	<script type="text/javascript" src="/resource/modules/act/actTraceFlow.js"></script>
	<%--<script src="/static/jquery-select2/3.4/select2.min.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="/static/jquery-select2/3.4/select2.min.css">
	<link rel="stylesheet" type="text/css" href="/resource/plugins/select2/select2.min.css">
	<script type="text/javascript" src="/resource/plugins/select2/select2.min.js"></script>--%>
	<script type="text/javascript">
		var executionId = '${task.executionId}';

        $(document).ready(function(){
//
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
		})

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
			<%--action="${ctx }/eam/act/task/complete?taskid=${task.id}"--%>
			<form  class="form-horizontal" action="${ctx }/eam/act/task/complete?taskid=${task.id}"  method="post" id="inputForm">
				<%--<form  class="form-horizontal" method="post" id="inputForm">--%>
				<input type="hidden" value="${task.id}" id="taskid">
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
					<div class="controls form-actions">

						<input type="hidden" value="${task.id}" id="taskid">
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
			<%--<div class="menu">
				<div><li>5</li>总人次</div>
				<div><li>4</li>已提交</div>
				<div><li>3</li>未提交</div>
				<div><li>2</li>已查看</div>
				<div><li>1</li>未查看</div>
			</div>--%>
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
