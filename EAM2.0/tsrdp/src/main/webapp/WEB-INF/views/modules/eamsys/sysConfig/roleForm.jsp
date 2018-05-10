<%--
  Created by IntelliJ IDEA.
  User: tiansu
  Date: 2017/7/25
  Time: 19:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>角色数据权限分配</title>
    <%@ include file="/WEB-INF/views/include/taglib.jsp"%>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treeview.jsp" %>
    <script type="text/javascript" src="/resource/plugins/jQuery/jquery-form.js"></script>
    <script type="text/javascript" src="/resource/plugins/layer/layer.js"></script>
    <script type="text/javascript">
        $(document).ready(function(){
            var setting = {
                check:{
                    enable:true,
                    chkboxType : { "Y" : "", "N" : "" }
                    //Y 属性定义 checkbox 被勾选后的情况；
                    //N 属性定义 checkbox 取消勾选后的情况；
                    //"p" 表示操作会影响父级节点；
                    //"s" 表示操作会影响子级节点。
                },
                view:{
                    selectedMulti:false
                },
                data:{
                    simpleData:{
                        enable:true,
                        idKey:"id",
                        pIdKey:"pId",
                        rootPId:'0'
                    }
                },
                callback:{beforeClick:function(id, node){
                    tree.checkNode(node, !node.checked, true, true);
                    return false;
                }
                }
            };
            $.ajaxSettings.async = false;   //同步加载
            var deptTree;
            // 初始化树结构
            function refreshTree(){
                $.getJSON("${ctx}/eam/dept/treeData",function(data){
                    deptTree = $.fn.zTree.init($("#deptTree"), setting, data);
                    deptTree.expandAll(true);
                });
            }
            refreshTree();
            refreshDeptTree();

            var dataScope = '${role.dataScope}';
            if(dataScope != null && dataScope != "null" && dataScope != '') {
                if('${role.dataScope.data_scope}' != null && '${role.dataScope.data_scope}' != '') {
                    $("#dataScope").val('${role.dataScope.data_scope}').trigger("change");    //设置Select的Value值为${role.dataScope.data_scope}的项选中

                    if( $("#dataScope option:selected").val() == '9'){
                        $("#roleAuth").show();
                        if('${role.dataScope.custom_detail}' != null && '${role.dataScope.custom_detail}' != ''){
                            var check_detail = '${role.dataScope.custom_detail}'.split(",");
                            for(var i = 0; i < check_detail.length; i++){
                                var node = deptTree.getNodeByParam("id", check_detail[i]);
                                deptTree.checkNode(node, true, true); //显示选中节点
                            }
                        }
                    }
                }
            }
        });

        function selectChange(){
            console.log($("#dataScope option:selected").val());
            refreshDeptTree();
        }
        function refreshDeptTree(){
            if($("#dataScope option:selected").val() == '9'){
                $("#roleAuth").show();
            }else{
                $("#roleAuth").hide();
            }
        }

        function submitdata(){
            var obj = {};
            obj.roleCode = $("#rolecode").val();
            obj.data_scope = $("#dataScope option:selected").val();
            var zTree = $.fn.zTree.getZTreeObj("deptTree");
            var nodes = zTree.getCheckedNodes(true);
            var custom_detail = "";
            for (var i = 0, l = nodes.length; i< l; i++) {
                custom_detail += nodes[i].id + ",";
            }

            if(obj.data_scope == '9') {  //自定义角色的数据权限范围
                obj.custom_detail = custom_detail.substring(0, custom_detail.length - 1);
            } else {  //选中默认的数据范围权限
                obj.custom_detail = null;
            }

            $.ajax({
                url: "addDataScope",
                type: "POST",
//                contentType : "application/x-www-form-urlencoded",
                //contentType:"application/json;charse=UTF-8",
                async:false,
                dataType:"json",
                data: obj,
                success: function(data){
                    if(data){
                        alert("数据范围分配成功！");
                        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                        parent.layer.close(index);
                    } else {
                        alert("数据范围分配失败！");
                    }
                }
            });
        }

        //重置功能，让下拉框默认选择全部，则机构树隐藏
        function resetData(){
            $("#dataScope").val('1').trigger("change");
            $("#roleAuth").hide();
        }

    </script>
</head>
<body>
    <form:form id="inputForm" modelAttribute="role" class="form-horizontal" onsubmit="return false;" >
        <%--<form:hidden path="id"/>--%>
        <div class="control-group">
            <label class="control-label">角色编号:</label>
            <div class="controls">
                <input id="rolecode" name="rolecode" type="hidden" value="${role.rolecode}">
                <form:input path="rolecode" htmlEscape="false" maxlength="50" class="required"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">角色名称:</label>
            <div class="controls">
                <input id="rolename" name="rolename" type="hidden" value="${role.rolename}">
                <form:input path="rolename" htmlEscape="false" maxlength="50" class="required"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">数据范围:</label>
            <div class="controls">
                <%--<form:select path="datascope.dataScope" class="input-medium">--%>
                    <%--<form:options items="${fns:getDictList('data_scope')}" itemLabel="typenote" itemValue="typevalue" htmlEscape="false"/>--%>
                <%--</form:select>--%>
                    <select id="dataScope" name="dataScope" style="width:220px" onChange="selectChange();">
                        <option value="1">所有数据</option>
                        <option value="2">所在公司及以下数据</option>
                        <option value="3">所在公司数据</option>
                        <option value="4">所在部门及以下数据</option>
                        <option value="5">所在部门数据</option>
                        <option value="8">仅本人数据</option>
                        <option value="9">按明细设置</option>
                    </select>
                    <span class="help-inline">特殊情况下，设置为“按明细设置”，可进行跨机构授权</span>
            </div>
        </div>
        <div id="roleAuth" class="control-group">
            <label class="control-label">角色授权:</label>
            <div class="controls">
                <div id="deptTree" class="ztree" style="margin-left:100px;margin-top:3px;float:left;"></div>
            </div>
        </div>
        <div class="form-actions">
            <input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" onclick="submitdata()"/>&nbsp;
            <input id="btnCancel" class="btn resetbtn" type="button" value="重 置" onclick="resetData()"/>
        </div>
    </form:form>
</body>
</html>
