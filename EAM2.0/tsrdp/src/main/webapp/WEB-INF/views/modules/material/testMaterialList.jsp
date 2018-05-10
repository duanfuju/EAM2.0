<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>物料信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/material/testMaterial/">物料信息列表</a></li>
		<shiro:hasPermission name="material:testMaterial:edit"><li><a href="${ctx}/material/testMaterial/form">物料信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="testMaterial" action="${ctx}/material/testMaterial/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>编码：</label>
				<form:input path="materialcode" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>名称：</label>
				<form:input path="materialname" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>编码</th>
				<th>名称</th>
				<th>数量</th>
				<th>单位</th>
				<th>价格</th>
				<th>有效状态</th>
				<th>物资等级</th>
				<th>物资类别</th>
				<shiro:hasPermission name="material:testMaterial:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="testMaterial">
			<tr>
				<td><a href="${ctx}/material/testMaterial/form? id=${testMaterial.id}">
					${testMaterial.materialcode}
				</a></td>
				<td>
					${testMaterial.materialname}
				</td>
				<td>
					${testMaterial.materialnumber}
				</td>
				<td>
					${fns:getDictLabel(testMaterial.materialunit, 'materialUnit', '')}
				</td>
				<td>
					${testMaterial.materialcost}
				</td>
				<td>
					${fns:getDictLabel(testMaterial.materialstatus, 'yes_no', '')}
				</td>
				<td>
					${fns:getDictLabel(testMaterial.materiallevel, 'materialLevel', '')}
				</td>
				<td>
					${fns:getDictLabel(testMaterial.materialtype, 'materialType', '')}
				</td>
				<shiro:hasPermission name="material:testMaterial:edit"><td>
    				<a href="${ctx}/material/testMaterial/form?id=${testMaterial.id}">修改</a>
					<a href="${ctx}/material/testMaterial/delete?id=${testMaterial.id}" onclick="return confirmx('确认要删除该物料信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>