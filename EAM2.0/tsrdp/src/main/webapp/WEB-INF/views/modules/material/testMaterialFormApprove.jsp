<%@ page import="org.activiti.engine.form.FormType" %>
<%@ page import="org.activiti.engine.form.FormProperty" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp"%>
<html>
<head>
	<title>物料信息管理</title>
	<meta name="decorator" content="default"/>
	<link href="/resource/plugins/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
	<link href="/resource/form.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="/resource/plugins/jQuery/jquery-form.js"></script>
	<script type="text/javascript" src="/resource/plugins/layer/layer.js"></script>
	<%--<script src="/resource/plugins/ligerUI/js/core/base.js" type="text/javascript"></script>--%>

	<script src="/resource/plugins/ligerUI/js/ligerui.all.js"></script>

	<%--表单校验--%>
	<script src="/resource/plugins/ligerUI/jquery-validation/jquery.validate.min.js"></script>
	<script src="/resource/plugins/ligerUI/jquery-validation/jquery.metadata.js"></script>
	<script src="/resource/plugins/ligerUI/jquery-validation/messages_cn.js"></script>

	<%--时间控件--%>
	<script src="/resource/plugins/jQueryUI/jquery-ui.js"></script>
	<link href="/resource/plugins/jQuery-Timepicker-Addon/jquery-ui-timepicker-addon.css" type="text/css" />
	<script src="/resource/plugins/jQuery-Timepicker-Addon/jquery-ui-timepicker-addon.js" type="text/javascript"></script>

	<script type="text/javascript">
        // var groupicon = "/resource/plugins/ligerUI/skins/icons/communication.gif";
        $(function () {
           /* /!**
             * 查询表格、表单、查询区域的字段权限
             *!/
            var formField = null;
            $.ajax({
                type : "post",
                async : false, //同步执行
                url : ctx+"/material/testMaterial/getfields?menuno=1102",
                dataType : "json",
                success : function(data)
                {
                    //表单赋值
                    formField=data.formfield;
                }
            });

            var formConfig = {
                space : 50, labelWidth : 80 , inputWidth : 200,
                validate: true,
                fields: formField

            };


            $("#inputForm").ligerForm(formConfig);


            //物料类型下拉初始化
            $("input[name='materialstatusBox']").ligerComboBox({
                width : 200,
                data: [
                    { name: '有效', id: '1' },
                    { name: '无效', id: '0' },

                ],

                valueFieldID: 'type_status',
                textField: 'name',
                //value: 'bj',
                //initIsTriggerEvent: false,
                onSelected: function (value)
                {
                    console.log(value)
                }
            });
*/


            $("#btnSubmit").on("click",function () {
                var form = liger.get('inputForm');
                if (!form.valid()) {
                    form.showInvalid();
                }else{
                    //表单提交

					$("#inputForm").ajaxSubmit({
						type: 'post',
						dataType:"text",
						success:function( data ){
							if(data=="success"){
                                layer.msg('新增成功！',{icon: 1,time: 1000},function (index) {
									parent.$("#mytable").DataTable().ajax.reload();
									parent.layer.closeAll();
								});
							}else if(data=="timeout"){
                                layer.msg("登录超时或无权限！",{time: 1000,icon:7});
							}else{
                                layer.msg("新增失败！",{time: 1000,icon:2});
							}
						}
					});

                }

            })

        });


	</script>
</head>
<body>

<form action="${ctx }/chapter6/task/complete/${task.id}" class="form-horizontal" method="post">
	<h4>任务内容</h4>
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
			<a href="javascript:history.back();" class="btn"><i class="icon-backward"></i>返回列表</a>
			<c:if test="${not empty task.assignee}">
				<button type="submit" class="btn btn-primary"><i class="icon-ok"></i>完成任务</button>
			</c:if>
			<c:if test="${empty task.assignee}">
				<a class="btn" href="${ctx }/chapter6/task/claim/${task.id}?nextDo=handle"><i class="icon-ok"></i>签收</a>
			</c:if>
		</div>
	</div>
</form>
<div class="form-actions">
	<input id="btnSubmit" type="button" value="保 存"/>
</div>
</body>
</html>
