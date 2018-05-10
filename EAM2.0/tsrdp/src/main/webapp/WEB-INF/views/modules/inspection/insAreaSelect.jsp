<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp" %>
<html>
<head>
    <title>巡检项选择</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        var InspectionAreaData;    //所有巡检项信息数据
        var route_areaids = parent.$("#route_area").val();    // 前一个页面已保存的巡检项ids

        var selectIds = null;   // 存储的巡检id
        var selectNames = null;   // 存储的物料名称
        var selectedIds = [];   // 存储选中的巡检区域id

        var names = [];   // 存储选中的巡检区域名称
        var objs = [];    // 存储选中的巡检区域对象

        selectedIds = route_areaids.split(",");
        objs = parent.selectedAreaObj;

        $(function () {
            // 页面效果渲染
            var manager;
            manager = $("#treeGrid").ligerGrid({
                columns: [
                    {display: '巡检区域编码', name: 'area_code'},
                    {display: '巡检区域名称', name: 'area_name'},
                    {display: '设备名称', name: 'area_device_names'},
                    {display: '设备位置', name: 'area_device_location_names'}
                ],
                width: '90%',method:'get',
                url:ctx+"/inspection/inspectionArea/dataTablePageMap",

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
                                names.splice($.inArray(data[i].area_name, names), 1);
                                objs.splice(index,1);
                                return false;
                            }
                        });
                    }
                    for(var i in data) {
                        selectedIds.push(data[i].id_key);
                        names.push(data[i].area_name);
                        objs.push(data[i]);
                    }
                } else {
                    for(var i in data) {
                        $.each(objs,function(index,o){
                            if(o.id_key == data[i].id_key){
                                selectedIds.splice($.inArray(data[i].id_key, selectedIds), 1);
                                names.splice($.inArray(data[i].area_name, names), 1);
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
                    parms:[{ name: 'search[area_name]', value: $("#area_name").val() }]
                });
                manager.loadData();
            });

            // 重置查询条件，并查询
            $(".resetbtn").on("click", function(){
                $("#searchForm #area_name").val("");
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
                    var key = $("#area_name").val();
                    if (rowdata.area_name.indexOf(key) > -1)
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
            }
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
            //  修改前后重新渲染的有勾选效果的巡检项列表页面
            function getShowChange() {
                manager = {};
                manager = $("#treeGrid").ligerGrid({
                    columns: [
                        //{display: 'ID', name: 'id_key'},
                        {display: '巡检区域编码', name: 'area_code'},
                        {display: '巡检区域名称', name: 'area_name'},
                        {display: '设备名称', name: 'area_device_names'},
                        {display: '设备位置', name: 'area_device_location_names'}
                    ],
                    width: '90%',
                    dataAction: 'server', //服务器排序
                    usePager: true,       //服务器分页
                    allowHideColumn: false, rownumbers: true, colDraggable: true, rowDraggable: true,checkbox:true,
                    data: InspectionAreaData, alternatingRow: false, isChecked: getSelect,
                });
            }
            var selected;
            // 点击已选，获取已经选择的巡检项信息列表
            $(".selectedbtn").on("click", function(){
                $(".editDiv").hide();
                $(".selectedSubject").show();
                $(".infobtn").show();   // 巡检项列表按钮显示
                // 渲染已选择的巡检项信息列表
                selected = $("#materialSelected").ligerGrid({
                    columns: [
                        //{display: 'ID', name: 'id_key'},
                        {display: '巡检区域编码', name: 'area_code'},
                        {display: '巡检区域名称', name: 'area_name'},
                        {display: '设备名称', name: 'area_device_names'},
                        {display: '设备位置', name: 'area_device_location_names'},
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

                // 要同步勾选到巡检项列表页面，否则在已选页面点击保存，会取到之前的数据，已删除数据也会取到
                getShowChange();
            });

            // 点击巡检项列表按钮，切换巡检项列表页面
            $(".infobtn").on("click", function () {
                $(".selectedSubject").hide();
                $(".editDiv").show();
                $(".selectedbtn").show();   // 巡检项列表按钮显示

                // 重新渲染巡检项列表，刷新勾选数据
                getShowChange();
                if($(".l-selected").length == parseInt($("select[name='rp']").eq(0).val()) *2){
                    $(".l-grid-hd-row").eq(0).addClass("l-checked");
                }
            });

            // 保存提交操作功能
            $("#btnSubmit").on("click",function () {
                var selectIds = "";   // 存储的巡检区域id
                var selectNames = "";   // 存储的巡检区域名称
                $.each(objs, function (i, item) {
                    selectIds += item.id_key + ",";
                    selectNames += item.area_name + ",";
                });
                if(selectIds) {
                    selectIds = selectIds.substring(0, selectIds.length - 1);
                    selectNames = selectNames.substring(0, selectNames.length - 1);
                }

                parent.$("input[name='route_area']").val(selectNames);
                parent.$("#route_area").val(selectIds);
                parent.selectedAreaObj = objs;
                parent.layer.closeAll();
            });
        });
    </script>
</head>
<body>
<div class="editDiv">
    <form id="searchForm"  class="form-horizontal" style="margin-left: 40px; margin-top: 10px; margin-bottom:10px">
        巡检区域名称 <input style="border: 1px solid #7f7a74; height:30px;border-radius:2px; padding: 5px" type="text" id="area_name"/>
        <input type='button' value='查询'  class='searchbtn' />
        <input type='button' value='重置'  class='resetbtn' />
        <input type='button' value='已选'  class='selectedbtn' style="margin-right:25px;float: right" />
    </form>

    <div class="subeditDiv" id="treeGrid"></div>
</div>

<div class="selectedSubject"  style="display: none">
    <div class="infobtnDiv">
        <input type='button' value='巡检区域列表'  class='infobtn' style="display: none;float: right;width: auto;"/>
    </div>

    <div class="selectedDiv" id="materialSelected" style="margin-left:40px;"></div>
</div>
<div class="form-actions">
    <input id="btnSubmit" type="button" value="保 存"/>
</div>
</body>
</html>
