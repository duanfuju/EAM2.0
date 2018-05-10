<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>角色管理</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treeview.jsp" %>
    <link href="/resource/plugins/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="/resource/plugins/jQuery/jquery-form.js"></script>
    <script type="text/javascript" src="/resource/plugins/layer/layer.js"></script>
    <%--<script src="/resource/plugins/ligerUI/js/core/base.js" type="text/javascript"></script>--%>
    <script src="/resource/plugins/ligerUI/js/ligerui.all.js"></script>

    <script type="text/javascript">
        var manager;
        var formParam = new Object();
        $(document).ready(function(){

            $("#name").focus();
//            $("#inputForm").validate({
//                submitHandler: function(form){
//                    $("#content").val(getData(false));
//                    $("#menuno").val(formParam.menuno);
//                    form.submit();
//                    alert("操作成功");
//                    parent.layer.close();
//                }
//            });
            f_initGrid();
            $("#gridDiv").hide();
            var setting = {
//                check:{enable:true,nocheckInherit:true},
                view:{selectedMulti:false},
                data:{simpleData:{enable:true}},
                callback:{beforeClick:function(id, node){
                    $("#selectMenuName").html("已选择:  "+node.name);
                    if(node.isParent){
                        $("#gridDiv").hide();
                    }else{
                        formParam.rolecode = '${fieldControl.rolecode}';
                        formParam.menuno = node.id;
                        manager.reload();
                        $("#gridDiv").show();
                    }

                    tree.checkNode(node, !node.checked, true, true);
                    return false;
                }}};

            // 用户-菜单
            var zNodes=[
                    <c:forEach items="${menuList}" var="menu">{id:"${menu.menuno}", pId:"${not empty menu.parentId ? menu.parentId:0}", name:"${not empty menu.parentId ? menu.menuname:'权限列表'}"},
                </c:forEach>];
            // 初始化树结构
            var tree = $.fn.zTree.init($("#menuTree"), setting, zNodes);
            // 不选择父节点
            tree.setting.check.chkboxType = { "Y" : "ps", "N" : "s" };
            // 默认选择节点

            // 默认不展开全部节点
            tree.expandAll(false);


        });




        function f_initGrid()
        {

            var isShow = [{ Show: 'true', text: '是' }, { Show: 'false', text: '否'}];
            var marginType = [{ type: 'hidden', text: '隐藏' },{ type: 'select', text: '下拉框' }, { type: 'text', text: '单行文本框'}
                , { type: 'textarea', text: '多行文本框'}, { type: 'date', text: '日期'}
                , { type: 'datetime', text: '日期时间'}, { type: 'checkbox', text: '复选框'}
                , { type: 'radiobox', text: '单选框'}, { type: 'special', text: '自定义'}];
            var validateType = [{ type: '', text: '无' },{ type: 'vint', text: '整数' },{ type: 'vfloat', text: '浮点数' }, { type: 'vtel', text: '手机号'}
                , { type: 'vmail', text: '邮箱'}];
            manager = $("#maingrid").ligerGrid({
                //toolbar: {},
                //title : 'gg',
                url:'${ctx}/eam/fieldControl/getMenuFieldJson',
                parms:formParam,
                usePager:false,
                async:true,//默认异步
                data:{Rows:[{}],Total:1},
                enabledSort:false,
                enabledEdit: true,isScroll:false,checkbox:true,rownumbers:true,allowAdjustColWidth:false,
                width: '90%',//630px;
                columns: [
                    { display: '字段名', name: 'fieldName', width:150, editor: { type: 'text' } },
                    { display: '显示名', name: 'displayName', width:155,
                        editor: { type: 'text' }
                    },
                    { display: '列表显示', width:60, name: 'showInGrid',type:'string',
                        editor: { type: 'selectGrid', data: isShow, valueField: 'Show' },
                        render: function (item)
                        {
                            if (item.showInGrid == 'true') return '是';
                            return '否';
                        }
                    },
                    { display: '列表宽度', name: 'gridWidth', width:60, type: 'int', editor: { type: 'text'} },
                    { display: '控件类型', width:90, name: 'marginType',
                        editor: { type: 'selectGrid', data: marginType, valueField: 'type' },
                        render: function (item)
                        {
                            for (var i = 0; i < marginType.length; i++)
                            {
                                if (marginType[i]['type'] == item.marginType)
                                    return marginType[i]['text']
                            }
                            return item.marginType;
                        }
                    },
                    { display: '搜索显示', width:60, name: 'showInSearch',type:'string',
                        editor: { type: 'selectGrid', data: isShow, valueField: 'Show' },
                        render: function (item)
                        {
                            if (item.showInSearch == 'true') return '是';
                            return '否';
                        }
                    },
                    { display: '表单显示', width:60, name: 'showInForm',type:'string',
                        editor: { type: 'selectGrid', data: isShow, valueField: 'Show' },
                        render: function (item)
                        {
                            if (item.showInForm == 'true') return '是';
                            return '否';
                        }
                    },
                    { display: '能否为空', width:60, name: 'nullable',type:'string',
                        editor: { type: 'selectGrid', data: isShow, valueField: 'Show' },
                        render: function (item)
                        {
                            if (item.nullable == 'true') return '是';
                            return '否';
                        }
                    },
                    { display: '能否编辑', width:60, name: 'editable',type:'string',
                        editor: { type: 'selectGrid', data: isShow, valueField: 'Show' },
                        render: function (item)
                        {
                            if (item.editable == 'true') return '是';
                            return '否';
                        }
                    },
                    { display: '校验类型', width:60, name: 'validateType',type:'string',
                        editor: { type: 'selectGrid', data: validateType, valueField: 'type' },
                        render: function (item)
                        {
                            for (var i = 0; i < validateType.length; i++)
                            {
                                if (validateType[i]['type'] == item.validateType)
                                    return validateType[i]['text'];
                            }
                            return '无';
                        }
                    }
                ],

                onSelectRow: function (rowdata, rowindex)
                {
                    $("#txtrowindex").val(rowindex);
                },
                onBeforeEdit: function (e){
                    //编辑前事件
                    if(e.column.name == 'fieldName' || e.column.name == 'displayName'
                        || e.column.name == 'marginType' || e.column.name == 'validateType' )
                        return false;
                    return true;
                },


            });



        }

        function deleteRow()
        {
            manager.deleteSelectedRow();
        }

        function addNewRow(isBefore)
        {
            var row = manager.getSelectedRow();

            //参数1:rowdata(非必填)
            //参数2:插入的位置 Row Data
            //参数3:之前或者之后(非必填)
            manager.addRow({
                fieldName: 'field',
                displayName: '字段名',
                showInGrid:'true',
                gridWidth:40,
                marginType : 'select',
                showInSearch : 'true',
                showInForm: 'true',
                nullable : 'false',
                editable:'false'
            }, row,isBefore);
        }
        function getSelected()
        {
            var row = manager.getSelectedRow();
            if (!row) { layer.msg('请选择行',{time: 1000,icon:7}); return; }
            layer.msg(JSON.stringify(row));
            return row;
        }
        function getData(alt)
        {
            var data = manager.getData();
            if(alt){
                layer.msg(JSON.stringify(data));
            }
            return JSON.stringify(data);

        }

        function saveConfig() {
            var formData = new Object();
            formData.menuno = formParam.menuno;
            formData.content = getData(false);
            formData.rolecode = formParam.rolecode;

            $.ajax({
                type: 'POST',
                url: "${ctx}/eam/fieldControl/saveOrUpdate",
                data: formData,
                dataType: "text",
                success: function (data, status) {
                    if (data=='success') {
//                        alert('保存成功');
//                        var index = parent.layer.getFrameIndex(window.name);
//                        parent.layer.close(index);
                        //shufq:bug#17808【角色设置】角色设置模块提示框还没有统一
                        layer.msg("保存成功",{icon:1,time: 1000},function (index) {
                            var index = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(index);
                        });
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    console.log(errorThrown.message);
//                    console.log(e.responseText);
                }
            });



        }
    </script>
</head>



<body>
<ul class="nav nav-tabs">
    <%--    <li><a href="${ctx}/sys/role/">角色列表</a></li>--%>
    <li class="active">权限分配</li>
</ul><br/>
<form:form id="inputForm" modelAttribute="fieldControl" class="form-horizontal">
<form:hidden path="id"/>
<form:hidden path="rolecode"/>
<form:hidden path="menuno"/>
<form:hidden path="content"/>

<%--<sys:message content="${message}"/>--%>

<div class="control-group">
    <label class="control-label">角色名称:</label>
    <div class="controls">
        <span>${fieldControl.rolename}</span>

    </div>
</div>

<div class="control-group">
    <label class="control-label">菜单选择:</label>
    <div class="controls">
        <label id="selectMenuName">已选择:</label>
        <div class="l-clear"></div>
        <div id="menuTree" class="ztree" style="margin-top:3px;float:left;"></div>
        <br><br>
        <div class="l-clear"></div>
        <div id = "gridDiv">
            <%--<a class="l-button" style="width:120px;float:left; margin-left:10px;" onclick="deleteRow()">删除选择的行</a>
            <a class="l-button" style="width:100px;float:left; margin-left:10px;" onclick="addNewRow(true)">添加前行</a>
            <a class="l-button" style="width:100px;float:left; margin-left:10px;" onclick="addNewRow(false)">添加后行</a>--%>
            <a class="l-button" style="width:120px;float:left; margin-left:10px;" onclick="getSelected()">选中行值</a>
            <a class="l-button" style="width:120px;float:left; margin-left:10px;" onclick="getData(true)">列表值</a>

            <br>
            <div id="maingrid" style="margin-top:20px"></div> <br>

        </div>
    </div>

    <div class="form-actions">
        <input id="btnSubmit" class="btn btn-primary" type="button" onclick="saveConfig()" value="保 存"/>
            <%--<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>--%>
    </div>
    </form:form>
</body>
</html>