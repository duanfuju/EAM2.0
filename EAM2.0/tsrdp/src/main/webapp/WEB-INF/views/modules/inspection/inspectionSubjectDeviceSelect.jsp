<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp" %>
<html>
<head>
    <title>选择设备</title>
    <link href="/resource/form.css" rel="stylesheet" type="text/css" />
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        var deviceSelected;    //已选择设备id
        var deviceTreeData;  // 设备类别/设备树
        var deviceIds = null;     //选中设备id
        var deviceName = null;   //选中设备名称
        var deviceMajor = null;   //选中设备专业
        var param = {};
        $(function () {
            // 获取设备类别设备树信息
            //请求入参
            param.operationwork_id = parent.parent.editId;
            common.callAjax('post',false,common.interfaceUrl.inspectionSubjectGetDeviceTreeDataForInspectionSubject,"json",param,function(data){
                deviceTreeData = {Rows: data};
            });
            var _devices=parent.getDevices();
            if(_devices){
                deviceSelected =_devices.split(",");
            }


            // 页面效果渲染
            var manager = $("#treeGrid").ligerGrid({
                    columns: [
                        {display: '设备名称', name: 'name', id: 'id1', align: 'left'},
                        {display: '编码', name: 'code', align: 'left'},
                        {display: '类型', name: 'type', align: 'left',
                            render: function (rowdata, rowindex, value) {
                                var result = "";
                                if(rowdata.type == 'device') {
                                    result = '设备';
                                } else {
                                    result = '设备类别';
                                }
                                return result;
                            }
                        }
                    ],width: '90%',pageSize:50,pageSizeOptions: [50, 100, 150, 200],allowHideColumn: false, rownumbers: true, colDraggable: true, rowDraggable: true,checkbox:true,
                data: arrayPush(deviceTreeData), alternatingRow: false, isChecked: getSelect, tree: {columnId: 'id1',
                    idField: 'id',
                    parentIDField: 'parentId'},
                }
            );
            //查询按钮操作
            $("#searchBtn").on("click", function ()
            {
                manager.collapseAll();
                manager.expandAll();
//                manager.options.data = deviceTreeData;
//                manager.loadData(getWhere());
                cal(deviceTreeData);
                manager.loadData();
            });

            //重置按钮操作 #17882 liwenlong 12-07
            $(".resetbtn").on("click",function () {
                $("#searchContent").val("");
                $('#searchContent').dataTable().fnDraw(false);
            });

            function cal(dataC) {
                debugger;
                var num = 0;
                var data = $.extend(true, {}, dataC);
                var a = data.Rows;
                var b = $.extend(true, {}, data);
                var key = $("#searchContent").val();
                var subclause = function(a){
                    if (a instanceof Array){
                        $.each(a, function (i, e) {
                            if (e.children) {
                                if (e.children.length !=0) {
                                    subclause(e.children);
                                }else if (e.name.indexOf(key) < 0) {
                                    filter(b.Rows, e.id);
                                }
                            } else if (e.name.indexOf(key) < 0) {
                                filter(b.Rows, e.id);
                            }
                        })
                    }else if(a.name.indexOf(key) < 0){
                        filter(b.Rows, e.id);
                    }
                };
                function filter(arr, id) {
                    num = 1;
                    for (var i = 0; i < arr.length; i++) {
                        var el = arr[i];
                        if (el.id === id) {
                            arr.splice(i, 1)
                        } else {
                            if (el.children && el.children.length) {
                                filter(el.children, id)
                            }
                        }
                    }
                    return arr;
                }
                subclause(a);
                if(num ==0){
                    manager.options.data =  arrayPush(b);
                }else {
                    cal(b);
                }
            }
            function arrayPush(b) {
                var a = {"Rows":[]};
                var c = $.extend(true, {}, b.Rows);
                var subclause = function(c) {
                    $.each(c, function (i, e) {
                        var eC = $.extend(true, {}, e);
                        delete eC.children;
                        a.Rows.push(eC);
                        if (e.children &&e.children.length != 0) {
                            subclause(e.children);
                        }
                    })
                };
                subclause(c);
                return a;
            }
            function getWhere() {
                if (!manager) return null;
                var clause = function (rowdata, rowindex)
                {
                    var value = false;
                    var key = $("#searchContent").val();
                    if (rowdata.name.indexOf(key) > -1)
                    {
                        value = true;
                    }else if(rowdata.children){
                        $.each(rowdata.children, function (i, item) {
                            if (item.name.indexOf(key) > -1) {
                                value = true;
                            }else if(item.children){
                                $.each(item.children, function (i, e) {
                                    if (e.name.indexOf(key) > -1) {
                                        value = true;
                                    }
                                });
                            }
                        });
                    }
                    return value;
                };
                return clause;
            }
            function getSelect(rowdata) {
                var result = false;
                if(deviceSelected) {
                    for (var index in deviceSelected)
                    {
                        if (rowdata.id == deviceSelected[index])
                            result =  true;
                    }
                    return result;
                }
            }
            $("#btnSubmit").on("click",function () {
                var obj = manager.getSelectedRows();
                $.each(obj, function (i, item) {
                    if(item.type == 'device') {
                        deviceIds += item.id + ",";
                        deviceName += item.name + ",";
                    }
                });
                if(deviceIds) {
                    deviceIds = deviceIds.substring(4, deviceIds.length - 1);
                }
                if(deviceName) {
                    deviceName = deviceName.substring(4, deviceName.length - 1);
                }
                if(deviceIds != null && deviceIds != ""){
                        parent.$("input[name='dev_id']").val(deviceName);
                        parent.setDevices(deviceIds);
                        parent.layer.closeAll();
                }else {
                    parent.$("input[name='dev_id']").val('');
                    parent.setDevices('');
                    parent.layer.closeAll();
                }

            })


        })
    </script>
</head>
<body>
<form id="inputForm" action="" method="post" class="form-horizontal">

</form>
<div class="editDiv">
    <div class="subeditDiv" style="margin-left: 43px; margin-top: 10px">设备名称：
        <input style="border: 1px solid #7f7a74; height:28px;border-radius:2px" type="text" id="searchContent"/>
        <input style="position:relative;top:-5px;" type="button" id="searchBtn"  value="查询"/>
        <input style="position:relative;top:-5px;" type='button' class="resetbtn" value='重置'/>
    </div>

    <div class="subeditDiv" id="treeGrid"></div>
</div>
<div class="form-actions">
    <input id="btnSubmit" type="button" value="保 存"/>

</div>
</body>
</html>
