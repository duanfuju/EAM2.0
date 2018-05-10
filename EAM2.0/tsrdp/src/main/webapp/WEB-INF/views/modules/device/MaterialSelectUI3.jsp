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
            if(selectedIds.length == 0 || (selectedIds.length == 1 && selectedIds[0] == "")) {
                objs = new Array();
            } else {
                objs = parent.selectedMaterialObj;
            }
        } else {
            selectedIds = toolsIds.split(",");
            if(selectedIds.length == 0 || (selectedIds.length == 1 && selectedIds[0] == "")) {
                objs = new Array();
            } else {
                objs = parent.selectedToolsObj;
            }

        }
        $(function () {
            // 页面效果渲染
            var manager;
            manager = $("#treeGrid").ligerGrid({
                columns: [
//                    {display: '物料Id', name: 'id_key', width: 400},
                    {display: '物料编码', name: 'material_code'},
                    {display: '物料名称', name: 'material_name'},
                    {display: '物料库存', name: 'material_qty'},
                    {display: '计量单位', name: 'material_unit'},
                    {display: '标准成本', name: 'material_cost'},
                    {display: '物料类型', name: 'material_type'}
                ],
                width: '90%',method:'get',
                url:common.interfaceUrl.materialInfoData,
                dataAction: 'server', //服务器排序
                usePager: true,       //服务器分页
                allowHideColumn: false, rownumbers: true, colDraggable: true, rowDraggable: true,checkbox:true,
                alternatingRow: false, isChecked: getSelect, onCheckRow: check
            });

            // 全选
            $("#treeGrid").on("click", ".l-grid-hd-cell-btn-checkbox",function ()
            {
                var data = manager.getData();
                allCheck = $(".l-selected").length == parseInt($("select[name='rp']").eq(0).val()) *2 ? true:false;
                if(!allCheck) {
                    for(var i in data) {
                        $.each(objs,function(index,o){
                            if(o.id_key == data[i].id_key){
                                selectedIds.splice($.inArray(data[i].id_key, selectedIds), 1);
                                names.splice($.inArray(data[i].material_name, names), 1);
                                objs.splice(index,1);
                                return false;
                            }
                        });
                    }
                    for(var i in data) {
                        selectedIds.push(data[i].id_key);
                        names.push(data[i].material_name);
                        objs.push(data[i]);
                    }
                } else {
                    for(var i in data) {
                        $.each(objs,function(index,o){
                            if(o.id_key == data[i].id_key){
                                selectedIds.splice($.inArray(data[i].id_key, selectedIds), 1);
                                names.splice($.inArray(data[i].material_name, names), 1);
                                objs.splice(index,1);
                                return false;
                            }
                        });
                    }
                }
            });

            // 查询
            $(".searchbtn").on("click", function ()
            {
                manager.set({
                    parms:[{ name: 'search[material_name]', value: $("#material_name").val() }]
                });
                manager.loadData();
            });

            // 重置查询条件，并查询
            $(".resetbtn").on("click", function(){
                $("#material_name").val("");
                manager.set({
                    parms:[]
                });
                manager.loadData();
            });

            // 获取查询数据对象
            function getWhere() {
                if (!manager) return null;
                var clause = function (rowdata, rowindex)
                {
                    var value = false;
                    var key = $("#material_name").val();
                    if (rowdata.material_name.indexOf(key) > -1)
                    {
                        value = true;
                    }
                    return value;
                };
                return clause;
            }

            // 查询已勾选的选项并令其选中
            function getSelect(rowdata) {
                var result = false;
                if(selectedIds) {
                    for(var i = 0; i < selectedIds.length; i++) {
                        if(rowdata.id_key == selectedIds[i]){
                            result =  true;
                        }
                    }
                    return result;
                }
            };

            function check(checked, rowdata, rowindex, rowDomElement){
               if(checked) {
                   selectedIds.push(rowdata.id_key);
                   names.push(rowdata.material_name);
                   objs.push(rowdata);
               } else {
                   selectedIds.splice($.inArray(rowdata.id_key, selectedIds), 1);
                   names.splice($.inArray(rowdata.material_name, names), 1);
                   $.each(objs,function(index,o){
                       if(o.id_key == rowdata.id_key){
                           objs.splice(index,1);
                       }
                   })
               }
            }

            function getCheckedData()
            {
                var rows = manager.getCheckedRows();
                $(rows).each(function ()
                {
                    selectedIds.push(this.id_key);
                    names.push(this.material_name);
                    objs.push(this);
                });
            }


            //  修改前后重新渲染的有勾选效果的物料列表页面
            function getShowChange() {
                manager = new Object();
                manager = $("#treeGrid").ligerGrid({
                    columns: [
//                        {display: '物料Id', name: 'id_key', width: 400},
                        {display: '物料编码', name: 'material_code'},
                        {display: '物料名称', name: 'material_name'},
                        {display: '物料库存', name: 'material_qty'},
                        {display: '计量单位', name: 'material_unit'},
                        {display: '标准成本', name: 'material_cost'},
                        {display: '物料类型', name: 'material_type'}
                    ],
                    width: '90%',
                    dataAction: 'server', //服务器排序
                    usePager: true,       //服务器分页
                    allowHideColumn: false, rownumbers: true, colDraggable: true, rowDraggable: true,checkbox:true,
                    data: materialInfoData, alternatingRow: false, isChecked: getSelect,
                });
            };

            var selected;
            // 点击已选，获取已经选择的物料信息列表
            $(".selectedbtn").on("click", function(){
                $(".editDiv").hide();
                $(".selectedMaterial").show();
                $(".infobtn").show();   // 物料列表按钮显示
                // 渲染已选择的物料信息列表
                selected = $("#materialSelected").ligerGrid({
                    columns: [
//                        {display: '物料Id', name: 'id_key', width: 400},
                        {display: '物料编码', name: 'material_code'},
                        {display: '物料名称', name: 'material_name'},
                        {display: '物料库存', name: 'material_qty'},
                        {display: '计量单位', name: 'material_unit'},
                        {display: '标准成本', name: 'material_cost'},
                        {
                            display: '操作', isSort: true,
                            render: function (rowdata, rowindex, value) {
                                console.log(rowdata);
                                var h = "";
                                h += "<a  class='del' data-id='"+ rowindex +"' data-idkey='" + rowdata.id_key + "'>删除</a> ";
                                return h;
                            }
                        }
                    ],
                    width: '90%',
                    dataAction: 'server', //服务器排序
                    usePager: true,       //服务器分页
                    allowHideColumn: false, rownumbers: true, colDraggable: true, rowDraggable: true,checkbox:false,
                    data: {Rows:objs}, alternatingRow: false
                });
            });

            $('#materialSelected').on('click','.del',function(){
                var id_key = $(this).data("idkey");
                for(var i = 0; i < objs.length; i++) {
                    var obj = objs[i];
                    if(id_key == obj.id_key){
                        objs.splice($.inArray(obj, objs), 1);
                        selectedIds.splice($.inArray(id_key, selectedIds), 1);
                        names.splice($.inArray(obj.material_name, names), 1);
                    }
                }
                selected.deleteRow($(this).data("id"));
//                selected.deleteRow($(this).closest('tr')[0]);  // 删除行操作

                // 要同步勾选到物料列表页面，否则在已选页面点击保存，会取到之前的数据，已删除数据也会取到
                getShowChange();
            });

            // 点击物料列表按钮，切换物料列表页面
            $(".infobtn").on("click", function () {
                $(".selectedMaterial").hide();
                $(".editDiv").show();
                $(".selectedbtn").show();   // 物料列表按钮显示

                // 重新渲染物料列表，刷新勾选数据
                getShowChange();
                if($(".l-selected").length == parseInt($("select[name='rp']").eq(0).val()) *2){
                    $(".l-grid-hd-row").eq(0).addClass("l-checked");
                }
            });

            // 保存提交操作功能
            $("#btnSubmit").on("click",function () {
                $.each(objs, function (i, item) {
                    selectIds += item.id_key + ",";
                    selectNames += item.material_name + ",";
                });
                if(selectIds) {
                    selectIds = selectIds.substring(4, selectIds.length - 1);
                }
                if(selectNames) {
                    selectNames = selectNames.substring(4, selectNames.length - 1);
                }

                if(parent.typeMaterial == '1') {
                    parent.$("input[name='spareparts_ids']").val(selectIds);
                    parent.$("input[name='dev_spareparts']").val(selectNames);
                    parent.selectedMaterialObj = objs;
                } else {
                    parent.$("input[name='tools_ids']").val(selectIds);
                    parent.$("input[name='dev_tools']").val(selectNames);
                    parent.selectedToolsObj = objs;
                }
                parent.layer.closeAll();
            });
        });
    </script>
</head>
<body>
    <div class="editDiv">
        <form id="searchForm"  class="form-horizontal" style="margin-left: 40px; margin-top: 10px; margin-bottom:10px">
            物料名称 <input style="border: 1px solid #7f7a74; height:30px;border-radius:2px; padding: 5px" type="text" id="material_name"/>
            <input type='button' value='查询'  class='searchbtn' /><input type='button' value='重置'  class='resetbtn' />
            <input type='button' value='已选'  class='selectedbtn' style="margin-left:330px" />
        </form>

        <div class="subeditDiv" id="treeGrid"></div>
    </div>

    <div class="selectedMaterial"  style="display: none">
        <input type='button' value='物料列表'  class='infobtn' style="display: none;margin-left: 840px;margin-top:10px;margin-bottom: 10px"/>
        <div class="selectedDiv" id="materialSelected" style="margin-left:40px;"></div>
    </div>
    <div class="form-actions">
        <input id="btnSubmit" type="button" value="保 存"/>
    </div>
</body>
</html>
