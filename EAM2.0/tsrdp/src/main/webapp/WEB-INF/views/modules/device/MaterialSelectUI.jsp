<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp" %>
<html>
<head>
    <title>备品备件选择</title>
    <link href="/resource/plugins/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
    <link href="/resource/form.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="/resource/plugins/jQuery/jquery-2.2.3.min.js"></script>
    <script type="text/javascript" src="/resource/plugins/jQuery/jquery-form.js"></script>
    <script type="text/javascript" src="/resource/plugins/layer/layer.js"></script>
    <script src="/resource/plugins/ligerUI/js/ligerui.all.js"></script>

    <%--表单校验--%>
    <script src="/resource/plugins/ligerUI/jquery-validation/jquery.validate.min.js"></script>
    <script src="/resource/plugins/ligerUI/jquery-validation/jquery.metadata.js"></script>
    <script src="/resource/plugins/ligerUI/jquery-validation/messages_cn.js"></script>
    <script src="/resource/common.js"></script>
    <script src="/resource/plugins/datatables/jquery.dataTables.min.js" type="text/javascript"></script>
    <script src="/resource/plugins/datatables/dataTables.bootstrap.min.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="/resource/plugins/datatables/dataTables.bootstrap.css">
    <link rel="stylesheet" type="text/css" href="/resource/plugins/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="/resource/plugins/font-awesome/css/font-awesome.min.css">
    <script src="/resource/plugins/select2/select2.full.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="/resource/plugins/select2/select2.min.css">

    <script type="text/javascript">
        var materialInfoData;    //所有物料信息数据
        var param = new Object();

        var selectedIds = new Array();   // 存储选中的物料id
        var selectedNames = new Array();   // 存储选中的物料名称

        var selectedObj = new Array();      // 存储选中的物料全部对象信息

//        var materialids = null;     //选中设备id
//        var materialNames = null;   //选中设备名称

        $(function () {
            //获取要被选择的物料信息
            common.callAjax('post',false,common.interfaceUrl.materialInfoData,"json",param,function(data){
                materialInfoData = data;
            });

            // 渲染table
            var tab = common.renderTable("materialGrid", {
                "order": [[3, "desc"]],
                "bStateSave":true,
                "serverSide": true,
                "hascheckbox": true,
                "hasIndex":true,
                "ajax": {
                    "url": common.interfaceUrl.materialInfoData,
                    "dataSrc": function (json) {
                        $("#rowsCount").html('共'+json.recordsTotal+'条记录');
                        return json.data;
                    },
                    "data": function (param) {
                        param.search.material_code = $("#material_code").val();
                        param.search.material_name = $("#material_name").val();
                    }
                },
                "columns": [
                    {"data": "id_key", "title": "ID", "bVisible": false},
                    {"data": "material_code", "title": "物料编码"},
                    {"data": "material_name", "title": "物料名称"},
                    {"data": "material_qty", "title": "物料库存"},
                    {"data": "material_unit", "title": "计量单位"},
                    {"data": "material_cost", "title": "标准成本"},
                    {"data": "material_type", "title": "物料类型"}
                ],
                "render": {

                }
            });

            $("#materialGrid tbody").on("click",'input[name="checkbox"]',function(){
                var flag = $(this).is(":checked");   //判断当前行是否被选中
                //如何得到选中行的索引, 解决跨页选中问题
//                var obj = tab.rows($('input:checkbox:checked').closest('tr')).data();  // 获取所有选中行的信息
                var obj = tab.row($(this).closest('tr')).data();
                if(flag) {  // 若当前行是选中状态，则将该行的值放到数组中
                    selectedIds.push(obj.id_key);
                    selectedNames.push(obj.material_name);
                    selectedObj.push(obj);
                } else {   // 若当前行是未选中状态，即去选，之前已经选中，后去选，则将该行的值从数组中删除
                    selectedIds.splice($.inArray(obj.id_key, selectedIds), 1);
                    selectedNames.splice($.inArray(obj.material_name, selectedNames), 1);
                    selectedObj.splice($.inArray(obj, selectedObj), 1);
                }

                console.log(selectedIds);
                console.log(selectedNames);
            });

            //查询按钮操作
            $(".searchBtn").on("click",function () {
                $('#materialGrid').dataTable().fnDraw(false);
            });

            //重置按钮操作
            $(".resetbtn").on("click",function () {
                $("#material_code").val("");
                $("#material_name").val("");
                $('#materialGrid').dataTable().fnDraw(false);
            });

            // 点击已选择按钮，切换已选择页面
            $(".selectedbtn").on("click", function () {
                $(".editDiv").hide();
                $(".selectedMaterial").show();
                $(".infobtn").show();   // 物料列表按钮显示
               var aa =  function (data) {
                    selectedObj.splice($.inArray(data, selectedObj), 1);
                    selectedIds.splice($.inArray(data.id_key, selectedIds), 1);
                    selectedNames.splice($.inArray(data.material_name, selectedNames), 1);
                   var selected = common.renderTable("materialSelected", {
                       "bStateSave": false,
                       "serverSide": false,
                       "hascheckbox": false,
                       "hasIndex": true,
                       "bDestroy": true,   // 先destory之前的表格，否则会出现序号和操作列重复
                       "opBtns": [{
                           "title": "删除", "callBack": aa,"label":"删除","class":"mayclass"
                       }],
                       "data": selectedObj,
                       "columns": [
//                           {"data": "id_key", "title": "ID", "bVisible": false},
                           {"data": "material_code", "title": "物料编码"},
                           {"data": "material_name", "title": "物料名称"},
                           {"data": "material_qty", "title": "物料库存"},
                           {"data": "material_unit", "title": "计量单位"},
                           {"data": "material_cost", "title": "标准成本"}
                       ],
                   });
                };
                // 渲染已选择table列表
                var selected = common.renderTable("materialSelected", {
                    "bStateSave": false,
                    "serverSide": false,
                    "hascheckbox": false,
                    "hasIndex": true,
                    "bDestroy": true,   // 先destory之前的表格，否则会出现序号和操作列重复
                    "opBtns": [{
                        "title": "删除", "callBack": aa,"label":"删除","class":"mayclass"
                    }],
                    "data": selectedObj,
                    "columns": [
                        {"data": "id_key", "title": "ID", "bVisible": false},
                        {"data": "material_code", "title": "物料编码"},
                        {"data": "material_name", "title": "物料名称"},
                        {"data": "material_qty", "title": "物料库存"},
                        {"data": "material_unit", "title": "计量单位"},
                        {"data": "material_cost", "title": "标准成本"}
                    ],
                });
            });
			
			/* $('#materialSelected').on('click', 'i', function () {
                var trd = $(this).closest('tr');
                $(this).closest('tr').remove();
                selectedObj.splice($.inArray(data, selectedObj), 1);
                selectedIds.splice($.inArray(data.id_key, selectedIds), 1);
                selectedNames.splice($.inArray(data.material_name, selectedNames), 1);
            }); */

            // 点击物料列表按钮，切换物料列表页面
            $(".infobtn").on("click", function () {
                $(".selectedMaterial").hide();
                $(".editDiv").show();
                $(".selectedbtn").show();   // 物料列表按钮显示
                $('#materialGrid').dataTable().fnDraw(false);
            });

            // 保存提交操作功能
            $("#btnSubmit").on("click",function () {
//                for(var i = 0;i < tab.getCheckedRow().length;i++){
//                    materialids += tab.getCheckedRow()[i].id_key + ",";
//                    materialNames += tab.getCheckedRow()[i].material_name + ",";
//                }
//                materialids = materialids.substring(4, materialids.length - 1);
//                materialNames = materialNames.substring(4, materialNames.length - 1);
                if(parent.typeMaterial == '1') {
//                    parent.$("input[name='spareparts_ids']").val(materialids);
//                    parent.$("input[name='dev_spareparts']").val(materialNames);

                    parent.$("input[name='spareparts_ids']").val(selectedIds);
                    parent.$("input[name='dev_spareparts']").val(selectedNames);
                } else {
//                    parent.$("input[name='tools_ids']").val(materialids);
//                    parent.$("input[name='dev_tools']").val(materialNames);
                    parent.$("input[name='tools_ids']").val(selectedIds);
                    parent.$("input[name='dev_tools']").val(selectedNames);
                }

                parent.layer.closeAll();
            });
        });

    </script>
</head>
<body>
    <div class="editDiv">
        <form id="searchForm"  class="form-horizontal" style="margin-left: 60px; margin-top: 10px">
            物料编码 <input style="border: 1px solid #7f7a74; height:30px;border-radius:2px; padding: 5px" type="text" id="material_code"/>
            物料名称 <input style="border: 1px solid #7f7a74; height:30px;border-radius:2px; padding: 5px" type="text" id="material_name"/>
            <input type='button' value='查询'  class='searchbtn' /><input type='button' value='重置'  class='resetbtn' />
            <input type='button' value='已选'  class='selectedbtn' style="margin-left:150px" />
        </form>

        <div class="row" style="margin-top: 20px">
            <div class="col-xs-12">
                <div class="box">
                    <div class="box-body">
                        <span class="record" id="rowsCount" style="margin-left: 60px; margin-top: 10px">共0条记录</span>
                        <table id="materialGrid" class="table table-bordered table-hover mytable">
                            <thead>
                            </thead>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="selectedMaterial"  style="display: none">
        <input type='button' value='物料列表'  class='infobtn' style="display: none;margin-left: 850px;margin-top:10px;"/>
        <div class="row" style="margin-top: 20px">
            <div class="col-xs-12">
                <div class="box">
                    <div class="box-body">
                        <table id="materialSelected" class="table table-bordered table-hover mytable">
                            <thead>
                            </thead>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="form-actions">
        <input id="btnSubmit" type="button" value="保 存"/>

    </div>
</body>
</html>
