<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>标准工单管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript" src="/static/ckfinder/ckfinder.js"></script>

	<script type="text/javascript">
        $(function () {
            //深克隆
            function cloneObject(obj) {
                var o = obj.length >0 ? [] : {};
                for (var i in obj) {
                    if (obj.hasOwnProperty(i)) {
                        if (typeof obj[i] === "object" && obj[i] != null) {
                            o[i] = cloneObject(obj[i]);
                        } else {
                            o[i] = obj[i];
                        }
                    }
                }
                return o;
            }


            //创建表单结构
            parent.formConfig.forEach(function(index){
                if(index.type=="combobox"){
                    index.option= {
                        isMultiSelect: true,
                        valueField: 'text',
                        tree: {
                            url: "/resource/data/tree.json",
                            idFieldName: 'text',
                            ajaxType: 'get'
                        }

                    }
                    index.newline=true;

                }

            });

            //delete parent.formConfig[1]["group"];
            var formField = cloneObject(parent.formConfig);
            $.each(formField, function (index, val) {
                val.readonly = true;
                if(val.name=="order_work"){//标准工作,支持多选
                    val.options = {
                        isMultiSelect : true,
                        valueField : 'id',
                        treeLeafOnly : false,
                        tree : {
                            url :'${ctx}/opestandard/standardOrder/getOrderwork',
                            checkbox : true,
                            parentIcon: null,
                            childIcon: null,
                            nodeWidth:200,
                            ajaxType : 'post',
                            textFieldName:'operationwork_content',
                            autoCheckboxEven:false,//复选框联动
                            onClick : function (note) {

                            }
                        }
                    }
                }
            })
            console.log(parent.editId);
            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields: formField

            };
            $("#inputForm").ligerForm(formConfig);
            //状态初始化
            //状态：   有效  1       无效  0
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "common"},function(data){
                var statusHtml="";
                $.each(data, function (i, item) {
                    statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                var order_statusBox= $("#order_statusBox");
                order_statusBox .html(statusHtml).append($("<input></input>").attr("type", "hidden").attr("name", "order_status"));
            });
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "priority"},function(data){
                var statusHtml="";
                $.each(data, function (i, item) {
                    statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                var order_priorityBox= $("#order_priorityBox");
                order_priorityBox .html(statusHtml).append($("<input></input>").attr("type", "hidden").attr("name", "order_priority"));
            });
//给表单赋值
		common.callAjax('POST',true,ctx+"/opestandard/standardOrder/editObj",'json',{id:parent.editId},function(data){
			var editForm  = liger.get("inputForm");
			editForm.setData(data);
        $('#order_statusBox').val(data.order_status).trigger('change.select2');
        $('#order_priorityBox').val(data.order_priority).trigger('change.select2');
		});

            $("ul li a:contains('点击展开')").on("click",function(){
                if($('#deviceTable').length>0){
                    $('#deviceTable').toggle();
                    if($('#deviceTable').css('display')=="block")
                        common.callAjax('POST', false, ctx + "/opestandard/standardOrder/getDevinfo", 'json', {"workid": $('#order_work').val()}, function (data) {
                            var data = {Rows: data};
                            $(f_initGrid);
                            var manager, g;

                            function f_initGrid() {
                                g = manager = $("#deviceTable").ligerGrid({
                                    columns: [
                                        {
                                            display: '设备编码', name: 'dev_code',
                                            editor: {type: 'text'}
                                        },
                                        {
                                            display: '设备名称', name: 'dev_name',
                                            editor: {type: 'text'}
                                        },
                                        {
                                            display: '设备位置', name: 'loc_name',
                                            editor: {type: 'text'}
                                        },
                                        {
                                            display: '设备类别', name: 'cat_name',
                                            editor: {type: 'text'}
                                        }
                                    ],
                                    onSelectRow: function (rowdata, rowindex) {
                                    },
                                    enabledEdit: true, isScroll: false, checkbox: true, rownumbers: true,
                                    data: data,
                                    width: '88%'
                                });
                            }
                        })
                }else {
                    if ($('#order_work').val() != "") {
                        $(this).closest('.l-fieldcontainer').closest('ul').after('<div class="subeditDiv" id="deviceTable" ></div>');

                        common.callAjax('POST', false, ctx + "/opestandard/standardOrder/getDevinfo", 'json', {"workid": $('#order_work').val()}, function (data) {
                            var data = {Rows: data};
                            $(f_initGrid);
                            var manager, g;

                            function f_initGrid() {
                                g = manager = $("#deviceTable").ligerGrid({
                                    columns: [
                                        {
                                            display: '设备编码', name: 'dev_code',
                                            editor: {type: 'text'}
                                        },
                                        {
                                            display: '设备名称', name: 'dev_name',
                                            editor: {type: 'text'}
                                        },
                                        {
                                            display: '设备位置', name: 'loc_name',
                                            editor: {type: 'text'}
                                        },
                                        {
                                            display: '设备类别', name: 'cat_name',
                                            editor: {type: 'text'}
                                        }
                                    ],
                                    onSelectRow: function (rowdata, rowindex) {
                                    },
                                    enabledEdit: true, isScroll: false, checkbox: true, rownumbers: true,
                                    data: data,
                                    width: '88%'
                                });
                            }
                        })
                    }
                }
            });

            $("#btnSubmit").on("click",function () {
                //给隐藏框赋值
                $("input[name='order_priority']").val($("#order_priorityBox").val());
                $("input[name='order_status']").val($("#order_statusBox").val());
                var form = liger.get('inputForm');
                if (!form.valid()) {
                    form.showInvalid();
                }else{
//表单提交
                    $("#inputForm").ajaxSubmit({
                        type: 'post',
                        dataType:"text",
                        success:function( data ){
                            if(data=="success"){
                                layer.msg('编辑成功！',{icon:1,time: 1000}, function(index){
                                    parent.$("#mytable").DataTable().ajax.reload();
                                    parent.layer.closeAll();
                                });
                            }else{
                                layer.msg("编辑失败！",{time: 1000,icon:2});
                            }
                        }
                    });

                }
            });

            //设置选择下拉框只读
            $("select").attr("disabled","disabled");


            $("#closeBtn").on("click", function () {
                parent.layer.closeAll();
            });
		})
	</script>
</head>
<body>
<form id="inputForm" action="${ctx}/opestandard/standardOrder/update" method="post" class="form-horizontal">


</form>
<div class="form-actions">
	<input id="closeBtn"  type="button"  value="关 闭"/>
</div>
</body>
</html>
