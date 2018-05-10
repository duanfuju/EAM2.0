<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<html>
<head>
    <title>排班类型管理</title>
    <meta name="decorator" content="default"/>
</head>
<body>


<script type="text/javascript">
    var ligerform;
    var manager, g;

    $(function () {
        try{
            ligerform = liger.get('form1');
            initForm();
            initData();
            initActEvent();
        }catch (e){
            console.error(e);
        }

    });

    function initForm() {

        //创建表单结构
        var formConfig = {
            space : 50, labelWidth : 90 , inputWidth : 200,
            validate: true,
            fields: parent.formConfig
        };

        $("#form1").ligerForm(formConfig);

        f_initGrid();
        /* $("input[name='schedual_begin']").timepicker({
         hourGrid: 4,
         minuteGrid: 10
         });
         $("input[name='schedual_end']").timepicker({
         hourGrid: 4,
         minuteGrid: 10
         });*/

    }

    function f_initGrid() {
        var initData = {
            Rows:[
                <c:forEach items="${schedualType.schedual_time_list}" var="t">
                    {
                        schedual_begin:"${t.schedual_begin}",
                        schedual_end:"${t.schedual_end}",
                    },
                </c:forEach>],
            Total: ${schedualType.schedual_time_list.size()}
        };
       /* initData ={
            Rows:[],
            Total: 0
        };*/
        var columns = [
            {
                display: '起始时间', name: 'schedual_begin',
                type:'time', editor: {type: 'time'}
            },
            {
                display: '结束时间', name: 'schedual_end',type:'time',  editor: {type: 'time'}
            }];
        var ocl = {
            display: '操作', isSort: false,
            render: function (rowdata, rowindex, value) {
                var h = "";
                h += "<a class='add'>添加</a> ";
                h += "<a  class='del' data-id='" + rowindex + "'>删除</a> ";
                return h;
            }
        };
        var enabledEdit = false;
        if('view' != '${ptype}'){
            enabledEdit = true;
            columns.push(ocl);
        }

        g = manager = $("#maingrid").ligerGrid({
            columns: columns,
            onSelectRow: function (rowdata, rowindex) {
                //$("#txtrowindex").val(rowindex);
            },
            enabledEdit: enabledEdit,
            isScroll: false, checkbox: true, rownumbers: true, onAfterEdit: f_onAfterEdit, onSubmit:f_onSubmit,
            data: initData,
            width: '88%'
        });
    }

    function initData() {
        //状态下拉初始化
        common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "common"},function(data){
            //状态：   有效  1       无效  0
            var statusHtml="";
            $.each(data, function (i, item) {
                statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
            });
            $("#type_statusBox").html(statusHtml).append($("<input></input>").attr("type", "hidden").attr("name", "type_status")).val(1).trigger('change.select2');

//            var type_statusBox= $("#type_statusBox");
//            type_statusBox .html(statusHtml);
//            type_statusBox.append($("<input></input>").attr("type", "hidden").attr("name", "type_status"));//设置隐藏域
//            type_statusBox.val(1);//设置默认值
//            type_statusBox.trigger('change.select2');
        });

        if('view' == '${ptype}'){
            //可编辑性设置
            parent.formConfig.forEach(function (index) {
                ligerform.setEnabled(index.name, false);
                if (true) {
                    ligerform.setVisible(index.name, true);
                }
            });
            $("#type_statusBox").attr("disabled",true);
        }
        //获取默认编码
        console.log('${schedualType}');
        ligerform.getEditor('type_code').setValue('${schedualType.type_code}');
        ligerform.getEditor('type_name').setValue('${schedualType.type_name}');
        ligerform.getEditor('type_desc').setValue('${schedualType.type_desc}');
        ligerform.getEditor('type_remark').setValue('${schedualType.type_remark}');
        $("#type_statusBox").val('${schedualType.type_status}').trigger('change.select2');

    }

    function initActEvent() {
        $('#maingrid').on('click', '.add', function () {
            //var row = manager.getSelectedRow();
            //var data = manager.getData();
            //表格参数key必须填，哪怕value是null，否则getData()取不到值
            manager.addRow({
                "schedual_begin": "00:00:00",
                "schedual_end": "00:00:00"
            });
        });
        $('#maingrid').on('click','.del',function(){
            if (manager.getData().length == 1) {
                alert("排班时间不能为空");
            } else {
                manager.deleteRow($(this).data("id"));
            }
        })
        //表单提交
        $("#btnSubmit").on("click", function () {
            debugger;
            if (!ligerform.valid()) {
                ligerform.showInvalid();
            } else {
                //表单提交
                var param = ligerform.getData();
                param.id = '${schedualType.id}';
                param.type_status = $("#type_statusBox").val();
                param.schedual_time_list = new Array();
//                manager.submitEdit
                $.each(manager.getData(),function (index,element) {
                    var timeObj = new Object();
                    timeObj.schedual_begin = element.schedual_begin;
                    timeObj.schedual_end = element.schedual_end;
                    param.schedual_time_list.push(timeObj);
                });
                console.log(param);
                var saveData = JSON.stringify(param);
                //定义了contentType，不能调用common.callAjax;
                $.ajax({
                    type: 'post',
                    async: false, //同步执行
                    url: ctx + "/schedualType/saveOrUpdateType",
                    data:saveData,
                    dataType: "json",
                    contentType: "application/json; charset=utf-8",
                    success: function (data) {
                        try {
                            if(data.flag){
                                layer.msg(data.msg,{icon:1,time: 1000});
                                parent.$("#mytable").DataTable().ajax.reload();
                                parent.layer.closeAll();
                            }
                        } catch (e) {
                            console.error(e);
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        layer.msg('保存失败',{time: 1000,icon:2});
                    }
                });

                common.callAjax('post', false, ctx + "/schedualType/saveOrUpdateType", "json", saveData, function (data) {
                    if (data.flag) {
                        layer.msg(data.msg,{icon:1,time: 1000}, function (index) {
                            parent.$("#mytable").DataTable().ajax.reload();
                            parent.layer.closeAll();
                        });
                    } else {
                        layer.msg(data.msg,{time: 1000,icon:2});
                    }
                });
            }
        });

        $("#closeBtn").on("click",function () {
            parent.layer.closeAll();
        });

    }

    function f_onAfterEdit(e) {
        manager.submitEdit(e.record);
//        manager.updateCell('Count', e.record.Age * e.record.Sex, e.record);
//        manager.updateCell('Counts', e.record.Number * e.record.Count, e.record);
    }
    function f_onSubmit() {
        debugger;
//        manager.updateCell('Count', e.record.Age * e.record.Sex, e.record);
//        manager.updateCell('Counts', e.record.Number * e.record.Count, e.record);
    }



</script>

<div id="form1" class="liger-form">
</div>
<div id="maingrid" style="margin-left:50px"></div>
<div class="form-actions">
    <c:choose>
        <c:when test="${ptype != 'view'}">
            <input id="btnSubmit" type="button" value="保 存"/>
        </c:when>
        <c:otherwise>
            <input id="closeBtn"  type="button"  value="关 闭"/>
        </c:otherwise>
    </c:choose>

</div>
</body>
</html>
