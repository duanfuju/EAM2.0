<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>标准工单管理</title>
	<meta name="decorator" content="default"/>
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
                if(val.name=="order_work"){//标准工作,支持多选
                    val.options = {
                        isMultiSelect : true,
                        valueField : 'id',
                        textField: 'operationwork_content',
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
								if($('#order_work').val()!="" && $('#order_work').val()!=null){
                                    common.callAjax('post',false,'${ctx}/opestandard/standardOrder/getMajorByWork',"json",{"workid" :$('#order_work').val()},function(data){
										var major="";
                                        $.each(data,function(index,item){
                                            major+=","+item.dict_name;
                                    		})
										$('input[name="order_major"]').val(major.substring(1))
                                    })
                            }
                        }
                    }
				}
				}
			})
            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields: formField

            };
            console.log(formConfig);
            $(".inputForm").ligerForm(formConfig);

         $('input[name="order_major"]').val('请选择标准工作');

//获取编码
            common.ajaxForCode({type:"STAND_ORDER"},false,"text",function(data){
                if (data !="" ||data !=null) {
					$("input[name='order_code']").val(data);
                }
            })

            //状态初始化
            //状态：   有效  1       无效  0
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "common"},function(data){
                var statusHtml="";
                $.each(data, function (i, item) {
                    statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                var order_statusBox= $("#order_statusBox");
                order_statusBox .html(statusHtml).append($("<input></input>").attr("type", "hidden").attr("name", "order_status"));
                order_statusBox.val(1);//设置默认值
                order_statusBox.trigger('change.select2');
            });
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "priority"},function(data){
                var statusHtml="";
                $.each(data, function (i, item) {
                    statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                var order_priorityBox= $("#order_priorityBox");
                order_priorityBox .html(statusHtml).append($("<input></input>").attr("type", "hidden").attr("name", "order_priority"));
                order_priorityBox.val(1);//设置默认值
                order_priorityBox.trigger('change.select2');
            });
            $("#inputForm a").on("click",function(){
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
                                layer.msg('新增成功！',{icon: 1,time: 1000},function (index) {
                                    parent.$("#mytable").DataTable().ajax.reload();
                                    parent.layer.closeAll();
                                });
                            }else if(data=="repeat"){
                                layer.msg("编码重复！",{time: 1000,icon:7});
                            }else if (data == "timeout") {
                                layer.msg("登录超时或无权限！",{time: 1000,icon:7});
                            }else{
                                layer.msg("新增失败！",{time: 1000,icon:2});
                            }
                        }
                    });

				}
			})



		})

	</script>
</head>
<body>
<form id="inputForm" class="inputForm" action="${ctx}/opestandard/standardOrder/insert" method="post" class="form-horizontal">

</form>
<div class="form-actions">
	<input id="btnSubmit" type="button" value="提 交"/>
</div>
</body>
</html>
