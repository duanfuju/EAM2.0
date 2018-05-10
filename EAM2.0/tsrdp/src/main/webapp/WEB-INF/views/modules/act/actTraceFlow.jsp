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
    </style>

    <script src="/resource/plugins/jQuery/jquery-2.2.3.min.js" type="text/javascript"></script>
    <%--<script type="text/javascript" src="/resource/plugins/bootstrap/js/bootstrap.min.js"></script>--%>
    <%--<script src="/js/jquery.js" type="text/javascript"></script>--%>
    <script type="text/javascript" src="/js/bootstrap.min.js"></script>
    <script type="text/javascript">
        var processInstanceId = '${historicProcessInstance.processInstanceId}';
        var executionId = '${executionId}';
    </script>
    <script type="text/javascript" src="/resource/modules/act/actTraceprocess.js"></script>
</head>
<body>${activeActivities}
    <%-- 先读取图片再通过Javascript定位 --%>
    <div>
        <img id="processDiagram" src="${ctx }/eam/act/process/read-resource?pdid=${historicProcessInstance.processDefinitionId}&resourceName=${processDefinition.diagramResourceName}" />
    </div>

</body>
</html>