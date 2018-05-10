<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/common/global.jsp" %>
    <%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/include-base-styles.jsp" %>
    <%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp"%>
    <title>流程跟踪--${historicProcessInstance.processInstanceId}</title>
    <style type="text/css">
        #pinfo th {background: #f7f7f9;}

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

    <script src="/resource/plugins/jQuery/jquery-2.2.3.min.js" type="text/javascript"></script>
    <%--<script type="text/javascript" src="/resource/plugins/bootstrap/js/bootstrap.min.js"></script>--%>
    <%--<script src="/js/jquery.js" type="text/javascript"></script>--%>
    <script type="text/javascript" src="/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/resource/modules/act/actTraceprocess.js"></script>
    <script type="text/javascript">
        var processInstanceId = '${historicProcessInstance.processInstanceId}';
        var executionId = '${executionId}';
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
                $('#flowDetails').append(dom);
            }
            showData(data);
        })
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li role="presentation" class="active"><a href="#taskContent" role="tab" data-toggle="tab" aria-controls="home" aria-expanded="true">流程图</a></li>
    <li role="presentation" ><a href="#flowstatus" role="tab" data-toggle="tab" aria-controls="profile" aria-expanded="false">流程状态</a></li>

</ul>
<div class="tab-content">
    <div id="taskContent" role="tabpanel" class="tab-pane active">
        ${activeActivities}
        <%-- 先读取图片再通过Javascript定位 --%>
        <div>
            <img id="processDiagram" src="${ctx }/eam/act/process/read-resource?pdid=${historicProcessInstance.processDefinitionId}&resourceName=${processDefinition.diagramResourceName}" />
        </div>

        <%-- 通过引擎自动生成图片并用红色边框标注
        <div>
            <img id="processDiagramAuto" src="${ctx }/chapter13/process/trace/data/auto/${historicProcessInstance.processInstanceId}" />
        </div>--%>
        <hr>
        <fieldset>
            <legend>流程综合信息-【${processDefinition.name}】<%--<button id="changeToAutoDiagram" class="btn btn-info">坐标错位请点击这里</button>--%></legend>
            <table id="pinfo" class="table table-bordered table-hover table-condensed">
                <tr>
                    <th width="100">流程ID</th>
                    <td>
                        ${historicProcessInstance.id}
                        <c:if test="${not empty parentProcessInstance.id}"><a href="${parentProcessInstance.id}" style="margin-left: 2em;">父流程：${parentProcessInstance.id}</a></c:if>
                    </td>
                    <th width="100">流程定义ID</th>
                    <td>${historicProcessInstance.processDefinitionId}</td>
                    <th width="100">业务KEY</th>
                    <td>${historicProcessInstance.businessKey}</td>
                </tr>
                <tr>
                    <th width="100">流程启动时间</th>
                    <td><fmt:formatDate value="${historicProcessInstance.startTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
                    <th width="100">流程结束时间</th>
                    <td><fmt:formatDate value="${historicProcessInstance.endTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
                    <th width="100">流程状态</th>
                    <td>${empty historicProcessInstance.endTime ? '未结束': '已结束'}</td>
                </tr>
            </table>
        </fieldset>
        <fieldset>
            <legend>活动记录</legend>
            <table width="100%" class="table table-bordered table-hover table-condensed">
                <thead>
                <tr>
                    <th>活动ID</th>
                    <th>活动名称</th>
                    <th>活动类型</th>
                    <th>任务ID</th>
                    <th>办理人</th>
                    <th>活动开始时间</th>
                    <td>活动结束时间</td>
                    <td>活动耗时（秒）</td>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${activities}" var="activity">
                    <tr>
                        <td>${activity.id}</td>
                        <td>${activity.activityName}</td>
                        <td>${activity.activityType}</td>
                        <td>${activity.taskId}</td>
                        <td>${activity.assignee}</td>
                        <td><fmt:formatDate value="${activity.startTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
                        <td><fmt:formatDate value="${activity.endTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
                        <td>${activity.durationInMillis / 1000}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </fieldset>
        <fieldset>
            <legend>表单属性</legend>
            <table width="100%" class="table table-bordered table-hover table-condensed">
                <thead>
                <tr>
                    <th>属性名称</th>
                    <th>属性值</th>
                    <th>任务ID</th>
                    <th>设置时间</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${formProperties}" var="prop">
                    <tr>
                        <td>${prop.propertyId}</td>
                        <td>${prop.propertyValue}</td>
                        <td>${prop.taskId}</td>
                        <td><fmt:formatDate value="${prop.time}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </fieldset>
        <fieldset>
        <legend>相关变量</legend>
        <table width="100%" class="table table-bordered table-hover table-condensed">
            <thead>
            <tr>
                <th>变量名称</th>
                <th>变量类型</th>
                <th>值</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${variableInstances}" var="var">
                <tr>
                    <td>${var.variableName}</td>
                    <td>${var.variableType.typeName}</td>
                    <td>${var.value}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </fieldset>
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
            <table id="flowDetails">
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
</body>
</html>