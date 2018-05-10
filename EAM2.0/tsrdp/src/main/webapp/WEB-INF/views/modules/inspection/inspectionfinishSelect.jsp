<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<script src="/resource/plugins/jQuery/jquery-2.2.3.min.js"></script>
<!-- 巡检区域 -->
<link rel="stylesheet" type="text/css" href="/resource/plugins/select2/select2.css"/>
<link rel="stylesheet" type="text/css" href="/resource/plugins/select2/select2.min.css">
<!-- <link rel="stylesheet" type="text/css" href="/resource/plugins/webupload/style.css"/> -->
<link href="/resource/form.css" rel="stylesheet" type="text/css" />
<link href="/resource/plugins/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
<!-- <link rel="stylesheet" type="text/css" href="/resource/plugins/webupload/webuploader.css"/> -->
<link rel="stylesheet" type="text/css" href="/resource/plugins/bootstrap-switch/css/bootstrap3/bootstrap-switch.css"/>
<link rel="stylesheet" type="text/css" href="/resource/modules/inspection/inspectionfinishSelect.css"/>

<script src="/resource/plugins/select2/select2.js"></script>
<script src="/resource/plugins/ligerUI/js/ligerui.all.js"></script>
<script src="/resource/common.js"></script>
<!-- <script src="/resource/plugins/select2/select2.full.js" type="text/javascript"></script> -->
<!-- <script src="/resource/plugins/bootstrap-switch/js/bootstrap-switch.min.js"></script> -->
<script type="text/javascript" src="/static/ckfinder/ckfinder.js"></script>
<script src="/resource/modules/inspection/inspectionfinishSelect.js"></script>

<div class="patrol-area-detail" id="patrol-result-content">

    <!-- 巡检使用明细反馈 -->
    <form id="inputForm" action="" method="post" class="form-horizontal">

    </form>
    <div class="editDiv">
        <div class="subeditDiv" id="procedure"></div>
        <div class="subeditDiv" id="safety"></div>
        <div class="subeditDiv" id="tools"></div>
        <div class="subeditDiv" id="spareparts"></div>
        <div class="subeditDiv" id="person"></div>
        <div class="subeditDiv" id="otherexpenses"></div>
    </div>

    <%--<div class="title">--%>
        <%--<h1 class="rightSpan region">巡检结果反馈</h1>--%>
    <%--</div>--%>
    <div class="switchArea patrolResult" style="margin-top:30px;">
        <div class="tdUl">
            <ul class="patrol-area-list">

            </ul>

        </div>
    </div>

    <div class="btnlist">
        <a class="btn btn-default" id="submitBtnAll">提交</a>
    </div>
</div>