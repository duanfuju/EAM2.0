<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<html>
<head>
    <title>排班类型管理</title>
    <meta name="decorator" content="default"/>

   <%-- <link href="/resource/form.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="/resource/plugins/jQuery/jquery-form.js"></script>
    <script type="text/javascript" src="/resource/plugins/layer/layer.js"></script>
    <script type="text/javascript" src="/resource/common.js"></script>


    <link type="text/css" rel="stylesheet" href="/resource/plugins/jQueryUI/jquery-ui.min.css">
    <script type="text/javascript" src="/resource/plugins/jQueryUI/jquery-ui.min.js"></script>
    <script src="/resource/plugins/ligerUI/jquery-validation/jquery.validate.min.js"></script>
    <script src="/resource/plugins/ligerUI/jquery-validation/jquery.metadata.js"></script>
    <script src="/resource/plugins/ligerUI/jquery-validation/messages_cn.js"></script>--%>
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
//        ligerform.editors;liger.get(index.name);ligerform.getEditor(index.name);ligerform.editors[eIndex].control;
        //可编辑性设置
        parent.formConfig.forEach(function (index) {
            if (index.editable == "false") {
                ligerform.setEnabled(index.name, false);
            }
            if (true) {
                ligerform.setVisible(index.name, true);
            }
        });


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
            Rows: [
                {
                    "schedual_begin": "00:00:00",
                    "schedual_end": "00:00:00"
                }
            ],
            Total: 0
        };
        g = manager = $("#maingrid").ligerGrid({
            columns: [
                {
                    display: '起始时间', name: 'schedual_begin',
                    type:'time', editor: {type: 'time'}
                },
                {
                    display: '结束时间', name: 'schedual_end',
                    type:'time', editor: {type: 'time'}
                },
                {
                    display: '操作', isSort: false,
                    render: function (rowdata, rowindex, value) {
                        var h = "";
                        h += "<a class='add'>添加</a> ";
                        h += "<a  class='del' data-id='" + rowindex + "'>删除</a> ";
                        return h;
                    }
                }

            ],
            onSelectRow: function (rowdata, rowindex) {
                //$("#txtrowindex").val(rowindex);
            },
            enabledEdit: true, isScroll: false, checkbox: true, rownumbers: true, onAfterEdit: f_onAfterEdit,
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
            var type_statusBox= $("#type_statusBox");
            type_statusBox .html(statusHtml);
            type_statusBox.append($("<input></input>").attr("type", "hidden").attr("name", "type_status"));//设置隐藏域
            type_statusBox.val(1);//设置默认值
            type_statusBox.trigger('change.select2');
        });

        //获取默认编码
        console.log('${schedualType}');
        ligerform.getEditor('type_code').setValue('${schedualType.type_code}');

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
                var checkValue = true;
                var param = ligerform.getData();
                param.type_status = $("#type_statusBox").val();
                param.schedual_time_list = new Array();
                $.each(manager.getData(),function (index,element) {
                    var timeObj = new Object();
                    timeObj.schedual_begin = element.schedual_begin;
                    timeObj.schedual_end = element.schedual_end;
                    debugger;
                    if(DateUtil.compareTimeOfDate(element.schedual_end,element.schedual_begin) <= 0){
                        checkValue = false;
                        layer.alert('排班结束时间必须大于排班起始时间!');
                    }
                    param.schedual_time_list.push(timeObj);
                });
                if(!checkValue){
                    return ;
                }
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


    }

    function f_onAfterEdit(e) {
        manager.submitEdit(e.record);
//        manager.updateCell('Count', e.record.Age * e.record.Sex, e.record);
//        manager.updateCell('Counts', e.record.Number * e.record.Count, e.record);
    }

        


</script>

<div id="form1" class="liger-form">
<%--    <div class="fields">
        <input data-type="text" data-label="编码" data-name="type_code" />
        <input data-type="text" data-label="名称" data-name="type_name" data-newline="true"/>
        &lt;%&ndash;<input data-type="text" data-label="排班时间" id="start_time" data-name="schedule_begin" />
        <input data-type="text" data-label="至" id="end_time" data-name="schedule_end" data-newline="true"/>&ndash;%&gt;
        <input data-type="text" data-label="描述" data-name="type_desc" data-newline="true"/>
        <input data-type="text" data-label="备注" data-name="type_remark" data-newline="true"/>
        <div data-type="select" data-label="状态" data-name="type_status">
        </div>
    </div>--%>

</div>
<div id="maingrid" style="margin-left:50px"></div>

<div class="form-actions">
    <input id="btnSubmit" type="button" value="提 交"/>
</div>
</body>
</html>
