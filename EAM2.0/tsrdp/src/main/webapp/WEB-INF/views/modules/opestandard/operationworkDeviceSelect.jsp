<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp" %>
<html>
<head>
    <title>选择设备</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        var deviceSelected;    //已选择设备id
        var deviceTreeData;  // 设备类别/设备树
        var deviceIds = null;     //选中设备id
        var deviceNames = null;   //选中设备名称
        var deviceMajor = null;   //选中设备专业
        var param = new Object();
        $(function () {
            // 获取设备类别设备树信息
            //请求入参
            param.operationwork_id = parent.parent.editId;
            common.callAjax('post',false,ctx + "/eam/operationwork/getDeviceTreeData","json",param,function(data){
                deviceTreeData = {Rows: data};
            });

//           deviceTreeData = {"Rows":[{"id":"1","parentId":"0","name":"天安云谷设备树2017","code":"TAYG103102","type":"devCategory","children":[{"id":"001521148699","parentId":"1","name":"1#B座PT柜123","code":"TAYG201710100060","type":"devCategory","children":[{"id":"000676933039","parentId":"001521148699","name":"编辑后新名称","code":"TAYG201711070181","type":"devCategory"},{"id":"001807303666","parentId":"001521148699","name":"设备类别2","code":"TAYG201711070182","type":"devCategory"},{"id":"7j1cOVdHJNRtWu7S","parentId":"001521148699","name":"新设备1","code":"SBBM201711070454","type":"device"},{"id":"9D80gsD1ibGIdi7s","parentId":"001521148699","name":"设备信息1","code":"SBBM201711070466","type":"device"},{"id":"ae6MsPVTnE3yQRI8","parentId":"001521148699","name":"新设备1","code":"SBBM201711070458","type":"device"},{"id":"gEYWc5n12zIkZSf2","parentId":"001521148699","name":"设备信息1","code":"SBBM201711070460","type":"device"},{"id":"hwncx1NZlIzd149i","parentId":"001521148699","name":"设备信息1","code":"SBBM201711070467","type":"device"},{"id":"jdaAm1BIfgmkf8pK","parentId":"001521148699","name":"设备信息1","code":"SBBM201711070463","type":"device"},{"id":"NndrcH26kRhFrYpM","parentId":"001521148699","name":"设备信息1","code":"SBBM201711070461","type":"device"},{"id":"P2YVKOd5RUhsFbi3","parentId":"001521148699","name":"设备信息1","code":"SBBM201711070462","type":"device"},{"id":"ZF0Umr9kNyGYbA5g","parentId":"001521148699","name":"设备信息1","code":"SBBM201711070464","type":"device"}]},{"id":"2","parentId":"1","name":"1#B座PT柜123","code":"YGYQ","type":"devCategory","children":[{"id":"000217285013","parentId":"2","name":"导入1","code":"TAYG04020208","type":"devCategory"},{"id":"002052056212","parentId":"2","name":"北区控制箱","code":"TAYG04020208","type":"devCategory"},{"id":"73","parentId":"2","name":"电梯系统","code":"TAYG02","type":"devCategory","children":[{"id":"74","parentId":"73","name":"客梯","code":"TAYG0201","type":"devCategory","children":[{"id":"80","parentId":"74","name":"3#C座客梯","code":"TAYG020106","type":"devCategory","children":[{"id":"001216893841","parentId":"80","name":"20170908","code":"20170908","type":"devCategory"}]},{"id":"78","parentId":"74","name":"3#A座客梯","code":"TAYG020104","type":"devCategory"},{"id":"76","parentId":"74","name":"1#B座客梯","code":"TAYG020102","type":"devCategory"},{"id":"000168338644","parentId":"74","name":"2#40客梯","code":"TAYG020140","type":"devCategory"},{"id":"001779705439","parentId":"74","name":"产招客梯","code":"TAYG201709280048","type":"devCategory"},{"id":"75","parentId":"74","name":"1#A座客梯","code":"TAYG020101","type":"devCategory"},{"id":"77","parentId":"74","name":"2#客梯","code":"TAYG020103","type":"devCategory"},{"id":"79","parentId":"74","name":"3#B座客梯","code":"TAYG020105","type":"devCategory"},{"id":"81","parentId":"74","name":"3#D座客梯","code":"TAYG020107","type":"devCategory"}]}]},{"id":"3","parentId":"2","name":"强电系统","code":"TAYG01","type":"devCategory","children":[{"id":"4","parentId":"3","name":"高压","code":"TAYG0101","type":"devCategory","children":[{"id":"15","parentId":"4","name":"母联柜","code":"TAYG010104","type":"devCategory","children":[{"id":"16","parentId":"15","name":"2#母联柜","code":"TAYG01010401","type":"devCategory"}]},{"id":"12","parentId":"4","name":"PT柜","code":"TAYG010103","type":"devCategory","children":[{"id":"13","parentId":"12","name":"1#B座PT柜","code":"TAYG01010301","type":"devCategory"},{"id":"14","parentId":"12","name":"3#A座PT柜","code":"TAYG01010302","type":"devCategory"}]},{"id":"24","parentId":"4","name":"出线柜","code":"TAYG010107","type":"devCategory","children":[{"id":"25","parentId":"24","name":"1#B座出线柜","code":"TAYG01010701","type":"devCategory"},{"id":"27","parentId":"24","name":"3#A座出线柜","code":"TAYG01010703","type":"devCategory"},{"id":"26","parentId":"24","name":"2#出线柜","code":"TAYG01010702","type":"devCategory"}]},{"id":"28","parentId":"4","name":"隔离柜","code":"TAYG010108","type":"devCategory","children":[{"id":"29","parentId":"28","name":"2#隔离柜","code":"TAYG01010801","type":"devCategory"},{"id":"002013743457","parentId":"28","name":"过滤器","code":"TAYG201709220039","type":"devCategory"}]},{"id":"9","parentId":"4","name":"计量柜","code":"TAYG010102","type":"devCategory","children":[{"id":"10","parentId":"9","name":"1#B座计量柜","code":"TAYG01010201","type":"devCategory"},{"id":"11","parentId":"9","name":"3#A座计量柜","code":"TAYG01010202","type":"devCategory"}]},{"id":"5","parentId":"4","name":"进线柜","code":"TAYG010101","type":"devCategory","children":[{"id":"6","parentId":"5","name":"1#B座进线柜","code":"TAYG01010101","type":"devCategory"}]},{"id":"001016964342","parentId":"4","name":"高压高压","code":"TAYG201709120010","type":"devCategory"},{"id":"17","parentId":"4","name":"环网柜","code":"TAYG010105","type":"devCategory","children":[{"id":"19","parentId":"17","name":"3#D座环网柜","code":"TAYG01010502","type":"devCategory"},{"id":"18","parentId":"17","name":"3#C座环网柜","code":"TAYG01010501","type":"devCategory"}]},{"id":"20","parentId":"4","name":"直流屏","code":"TAYG010106","type":"devCategory","children":[{"id":"22","parentId":"20","name":"2#直流屏","code":"TAYG01010602","type":"devCategory"},{"id":"21","parentId":"20","name":"1#B座直流屏","code":"TAYG01010601","type":"devCategory"},{"id":"23","parentId":"20","name":"3#A座直流屏","code":"TAYG01010603","type":"devCategory"}]}]},{"id":"30","parentId":"3","name":"低压","code":"TAYG0102","type":"devCategory","children":[{"id":"55","parentId":"30","name":"低压联络柜","code":"TAYG010205","type":"devCategory","children":[{"id":"58","parentId":"55","name":"3#C座联络柜","code":"TAYG01020503","type":"devCategory"},{"id":"59","parentId":"55","name":"3#D座联络柜","code":"TAYG01020504","type":"devCategory"},{"id":"56","parentId":"55","name":"1#B座联络柜","code":"TAYG01020501","type":"devCategory"},{"id":"57","parentId":"55","name":"3#A座联络柜","code":"TAYG01020502","type":"devCategory"}]},{"id":"60","parentId":"30","name":"双电源自动切换","code":"TAYG010206","type":"devCategory","children":[{"id":"63","parentId":"60","name":"3#A座双电源自动切换","code":"TAYG01020603","type":"devCategory"},{"id":"62","parentId":"60","name":"2#双电源自动切换","code":"TAYG01020602","type":"devCategory"},{"id":"64","parentId":"60","name":"3#C座双电源自动切换","code":"TAYG01020604","type":"devCategory"},{"id":"61","parentId":"60","name":"1#B座双电源自动切换","code":"TAYG01020601","type":"devCategory"}]},{"id":"67","parentId":"30","name":"发电机本体","code":"TAYG010209","type":"devCategory","children":[{"id":"71","parentId":"67","name":"3#D发座电机组","code":"TAYG01020904","type":"devCategory"},{"id":"70","parentId":"67","name":"3#A座发电机组","code":"TAYG01020903","type":"devCategory"},{"id":"68","parentId":"67","name":"1#B座发电机组","code":"TAYG01020901","type":"devCategory"},{"id":"69","parentId":"67","name":"2#发电机组","code":"TAYG01020902","type":"devCategory"}]},{"id":"72","parentId":"30","name":"UPS本体","code":"TAYG010210","type":"devCategory"},{"id":"31","parentId":"30","name":"变压器","code":"TAYG010201","type":"devCategory","children":[{"id":"33","parentId":"31","name":"2#变压器","code":"TAYG01020102","type":"devCategory"},{"id":"34","parentId":"31","name":"3#A座变压器","code":"TAYG01020103","type":"devCategory"},{"id":"35","parentId":"31","name":"3#C座变压器","code":"TAYG01020104","type":"devCategory"},{"id":"36","parentId":"31","name":"3#D座变压器","code":"TAYG01020105","type":"devCategory"},{"id":"32","parentId":"31","name":"1#B座变压器","code":"TAYG01020101","type":"devCategory"}]},{"id":"43","parentId":"30","name":"低压出线柜","code":"TAYG010203","type":"devCategory","children":[{"id":"46","parentId":"43","name":"3#A座低压出线柜","code":"TAYG01020303","type":"devCategory"},{"id":"44","parentId":"43","name":"1#B座低压出线柜","code":"TAYG01020301","type":"devCategory"},{"id":"45","parentId":"43","name":"2#低压出线柜","code":"TAYG01020302","type":"devCategory"},{"id":"47","parentId":"43","name":"3#C座低压出线柜","code":"TAYG01020304","type":"devCategory"},{"id":"48","parentId":"43","name":"3#D座低压出线柜","code":"TAYG01020305","type":"devCategory"}]},{"id":"66","parentId":"30","name":"照明配电柜","code":"TAYG010208","type":"devCategory"},{"id":"49","parentId":"30","name":"电容补偿柜","code":"TAYG010204","type":"devCategory","children":[{"id":"50","parentId":"49","name":"1#B座电容补偿柜","code":"TAYG01020401","type":"devCategory"},{"id":"52","parentId":"49","name":"3#A座电容补偿柜","code":"TAYG01020403","type":"devCategory"},{"id":"51","parentId":"49","name":"2#电容补偿柜","code":"TAYG01020402","type":"devCategory"},{"id":"53","parentId":"49","name":"3#C座电容补偿柜","code":"TAYG01020404","type":"devCategory"},{"id":"54","parentId":"49","name":"3#D座电容补偿柜","code":"TAYG01020405","type":"devCategory"}]},{"id":"37","parentId":"30","name":"低压进线柜","code":"TAYG010202","type":"devCategory","children":[{"id":"38","parentId":"37","name":"1#B座低压进线柜","code":"TAYG01020201","type":"devCategory"},{"id":"40","parentId":"37","name":"3#A座低压进线柜","code":"TAYG01020203","type":"devCategory"},{"id":"41","parentId":"37","name":"3#C座低压进线柜","code":"TAYG01020204","type":"devCategory"},{"id":"42","parentId":"37","name":"3#D座低压进线柜","code":"TAYG01020205","type":"devCategory"},{"id":"39","parentId":"37","name":"2#低压进线柜","code":"TAYG01020202","type":"devCategory"}]},{"id":"65","parentId":"30","name":"动力配电柜","code":"TAYG010207","type":"devCategory"}]}]}]},{"id":"001100150274","parentId":"1","name":"1#B座PT柜123","code":"TAYG10","type":"devCategory"}]}]};

            deviceSelected = parent.$("input[name='device_ids']").val().split(",");
            console.log(deviceSelected);

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
                    ], width: '90%',pageSize:50,pageSizeOptions: [50, 100, 150, 200],allowHideColumn: false, rownumbers: true, colDraggable: true, rowDraggable: true,checkbox:true,
                    data: arrayPush(deviceTreeData), alternatingRow: false, isChecked: getSelect, tree: {columnId: 'id1',
//                    columnName: 'name',
                    idField: 'id',
                    parentIDField: 'parentId'},
                }
            );

            $("#searchBtn").on("click", function ()
            {
                manager.collapseAll();
                manager.expandAll();
                // manager.options.data = cal(deviceTreeData);
                //manager.loadData(getWhere());
                cal(deviceTreeData);
                manager.loadData();
            });

            function cal(dataC) {
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
                }
                function filter(arr, id) {
                    num = 1;
                    for (var i = 0; i < arr.length; i++) {
                        var el = arr[i]
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
                }
                subclause(c);
                return a;
            }


            function getWhere() {
                if (!manager) return null;
                var clause = function (rowdata, rowindex)
                {
                    var value = false;
                    var key = $("#searchContent").val();
                    var subclause = function(rowdata){
                        if (rowdata.name.indexOf(key) > -1 || rowdata.type =="devCategory")
                        {
                            value = true;
                        }
//                        else if(rowdata.children){
//                            $.each(rowdata.children, function (i, item) {
//                                subclause(item);
//                            })
//                        }
                    }
                    subclause(rowdata);
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
                    if(item.type == 'device'){
                        deviceIds += item.id + ",";
                        deviceNames += item.name + ",";
                    }
                });
                if(deviceIds) {
                    deviceIds = deviceIds.substring(4, deviceIds.length - 1);
                }
                if(deviceNames) {
                    deviceNames = deviceNames.substring(4, deviceNames.length - 1);
                }

                parent.$("input[name='device_ids']").val(deviceIds);
                parent.$("input[name='device_name']").val(deviceNames);

                if(deviceIds != null && deviceIds != ""){
                    common.callAjax('post',false,ctx + "/eam/operationwork/getDeviceMajor","json",{deviceIds : deviceIds},function(data){
                        if(data && data.length > 0) {
                            $.each(data, function (i, item) {
                                deviceMajor += item.dict_name + ",";
                            });
                            deviceMajors = deviceMajor.substring(4, deviceMajor.length - 1);
                        } else {
                            deviceMajors = '';
                        }
                        parent.$("input[name='device_major']").val(deviceMajors);

//                        parent.devInfo.deviceIds = deviceIds;
                        parent.layer.closeAll();
                    });
                } else {
                    parent.$("input[name='device_ids']").val(null);
                    parent.$("input[name='device_name']").val(null);
                    parent.$("input[name='device_major']").val(null);
                    parent.layer.closeAll();
                }

            });
        })
    </script>
</head>
<body>
<form id="inputForm" action="" method="post" class="form-horizontal">

</form>
<div class="editDiv">
    <div class="subeditDiv" style="margin-left: 43px; margin-top: 10px">设备名称：
        <input style="border: 1px solid #7f7a74; height:26px;border-radius:2px" type="text" id="searchContent"/>
        <input type="button" style="top:-5px;position:relative;" id="searchBtn" value="查询"/>
    </div>

    <div class="subeditDiv" id="treeGrid"></div>
</div>
<div class="form-actions">
    <input id="btnSubmit" type="button" value="保 存"/>

</div>
</body>
</html>
