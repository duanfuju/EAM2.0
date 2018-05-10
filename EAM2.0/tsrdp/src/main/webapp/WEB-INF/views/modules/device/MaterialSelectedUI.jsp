<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp" %>
<html>
<head>
    <title>备品备件选择</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        var materialInfoData;    //所有物料信息数据
        var sparepartsIds = parent.$("input[name='spareparts_ids']").val();    // 前一个页面已保存的备品备件ids
        var toolsIds = parent.$("input[name='tools_ids']").val();    // 前一个页面已保存的工器具ids

        var selectIds = null;   // 存储的物料id
        var selectNames = null;   // 存储的物料名称
        var selectedIds = new Array();   // 存储选中的物料id

        var names = new Array();   // 存储选中的物料id
        var objs = new Array();    // 存储选中的物料对象

        if(parent.typeMaterial == '1') {
            selectedIds = sparepartsIds.split(",");
            objs = parent.selectedMaterialObj;
        } else {
            selectedIds = toolsIds.split(",");
            objs = parent.selectedToolsObj;
        }
        $(function () {
            $("#closeBtn1").on("click",function () {
                parent.layer.closeAll();
            });
            $(".selectedMaterial").show();
            // 渲染已选择的物料信息列表
            selected = $("#materialSelected").ligerGrid({
                columns: [
//                        {display: '物料Id', name: 'id_key', width: 400},
                    {display: '物料编码', name: 'material_code'},
                    {display: '物料名称', name: 'material_name'},
                    {display: '物料库存', name: 'material_qty'},
                    {display: '计量单位', name: 'material_unit'},
                    {display: '标准成本', name: 'material_cost'},

                ],
                width: '90%',
                dataAction: 'server', //服务器排序
                usePager: true,       //服务器分页
                allowHideColumn: false, rownumbers: true, colDraggable: true, rowDraggable: true,checkbox:false,
                data: {Rows:objs}, alternatingRow: false
            });
        });



    </script>
</head>
<body>

<div class="selectedMaterial"  style="display: none">
    <input type='button' value='物料列表'  class='infobtn' style="display: none;margin-left: 840px;margin-top:10px;margin-bottom: 10px"/>
    <div class="selectedDiv" id="materialSelected" style="margin-left:40px;"></div>
</div>
<div class="form-actions">
    <input id="closeBtn1" type="button" value="关 闭"/>
</div>
</body>
</html>
