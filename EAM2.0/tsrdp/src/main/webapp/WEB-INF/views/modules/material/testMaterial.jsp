<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>物料信息管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">

    </script>
</head>
<body>

<section class="content module1">
    <button class="bugg">获取选中行数据</button>
    <button class="bugg1">跳页</button>
    <div class="row">
        <div class="col-xs-12">
            <div class="box">
                <div class="box-body">
                    <table id="mytable" class="table table-bordered table-hover mytable">
                        <thead>
                        <tr>
                            <th>物料编码</th>
                            <th>物料名称</th>
                            <th>库存数量</th>
                            <th>单位</th>
                            <th>标准成本</th>
                            <th>物料类别</th>
                            <%--<th>供应商</th>--%>
                            <th>重要程度</th>
                            <%--<th>采购方式</th>--%>
                            <th>状态</th>
                        </tr>
                        </thead>
                    </table>
                </div>
                <div class="box-body">
                    <table id="mytable2" class="table table-bordered table-hover mytable">
                    </table>
                </div>
                <!-- /.box-body -->
            </div>
        </div>
    </div>
</section>
</body>
</html>