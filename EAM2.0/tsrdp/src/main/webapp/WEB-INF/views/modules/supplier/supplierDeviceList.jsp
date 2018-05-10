<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp" %>
<html>
<head>
    <title>供应设备清单</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">

        $(function () {
            // 页面效果渲染
            var manager;
            manager = $("#treeGrid").ligerGrid({
                columns: [
                    {display: '设备编码', name: 'dev_code'},
                    {display: '设备名称', name: 'dev_name'},
                    {display: '设备位置', name: 'dev_locname'},
                    {display: '工单总数', name: 'ordertotal'},
                    {display: '巡检总数', name: 'inspection_num'},
                    {display: '保养总数', name: 'maint_num'},
                    {display: '异常总数', name: ''}
                ],
                width: '90%',method:'get',
                url:'${ctx}/supplier/supplier/getDeviceList',
                parms:[{ name: 'search[dev_supplier]', value:parent.editId }],
                dataAction: 'server', //服务器排序
                usePager: true,       //服务器分页
                allowHideColumn: false, rownumbers: true, colDraggable: true, rowDraggable: true,checkbox:true,
                alternatingRow: false
            });

            $('#closed').on('click',function(){
                parent.layer.closeAll();
            })
        });
    </script>
</head>
<body>
<div class="editDiv">

    <div class="subeditDiv" id="treeGrid"></div>
</div>
<div class="form-actions">
    <input id="closed" type="button" value="关闭"/>
</div>
</body>
</html>
