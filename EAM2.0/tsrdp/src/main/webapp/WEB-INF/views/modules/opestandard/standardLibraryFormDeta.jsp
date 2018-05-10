<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>标准库管理</title>
	<meta name="decorator" content="default"/>
	<style>
		.editDiv .l-panel-bar{
			display: none ;
		}
	</style>
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

            //设置所有字段只读属性
            var formFields = cloneObject(parent.formConfig);
            debugger;
            $.each(formFields, function (index, val) {
                val.readonly = true;

//                if(val.name == 'library_typeBox'){//标准库类型
//                    val.options = {
//                        isMultiSelect : true,
//                        valueField : '',
//                        treeLeafOnly : false,
//                        tree : {
//                            url :"",

//                            checkbox : true,
//                            parentIcon: null,
//                            childIcon: null,
//                            nodeWidth:200,
//                            ajaxType : 'post',
//                            textFieldName:' ',//文本字段名，默认值text
//                            autoCheckboxEven:false,//复选框联动
//                            onClick : function (note) {
//
//                            }
//                        }
//                    }
//                }


            });

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
                    index.newline=false;

                }

            });

            //delete parent.formConfig[1]["group"];
            var formField = cloneObject(parent.formConfig);
            var formFieldnew=[];
            var typeDate=[{id:0,name:"设备运维标准",pid:""},{id:1,name:"设备运行标准",pid:""},{id:2,name:"设备安全标准",pid:""},{id:3,name:"故障检修标准",pid:"0"},{id:4,name:"保养标准",pid:"0"},{id:5,name:"巡检标准",pid:"0"},{id:6,name:"缺陷故障库",pid:"0"}];
//标准类型下拉树
            $.each(formField, function (index, val) {
                val.readonly = true;
                if(val.name=='id_key'){formFieldnew.push(val);}
                if(val.name=='library_code'){formFieldnew.push(val);}
                if(val.name=='library_name'){formFieldnew.push(val);}
                if(val.name=='device_id'){formFieldnew.push(val);}
                if(val.name=='library_status'){formFieldnew.push(val);}
            });
            formFieldnew.push({
                display:"标准库类型",name:"library_type",comboboxName:"library_typeBox","type": "combobox",
                options:{
                    isMultiSelect : true,
                    valueField : 'id',
                    textField: 'name',
                    treeLeafOnly : false,
                    tree : {
                        //url :  ,
                        data:typeDate, //存在数据累加
                        checkbox : false,
                        parentIcon: null,
                        childIcon: null,
                        idFieldName : 'id',
                        parentIDFieldName : 'pid',
                        textFieldName:'name',//文本字段名，默认值text
                        nodeWidth:200,
                        ajaxType : 'post',
                        onClick : function (note) {
                            if ($("#maintainStdTable").children().length <1){
                                $(f_initGrid2);
                                $(f_initGrid3);
                                $(f_initGrid4);
                                $(f_initGrid5);
                                $(f_initGrid6);
                            }
                            if(note.data.id =="3" || note.data.id =="0"){
                                $("#faultStdTable").show().siblings().hide();
                            }else if(note.data.id =="4"){
                                $("#maintainStdTable").show().siblings().hide();
                            }else if(note.data.id =="5"){
                                $("#patrolStdTable").show().siblings().hide();
                            }else if(note.data.id =="6"){
                                $("#failureStdTable").show().siblings().hide();
                            }else if(note.data.id =="1"){
                                $("#operationStdTable").show().siblings().hide();
                            }else if(note.data.id =="2") {
                                $("#safeStdTable").show().siblings().hide();
                            }
                        }
                    }

                }
            });
            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields: formFieldnew

            };

            //debugger;
            console.log(parent.formConfig)

            console.log(parent.editId);
            $("#inputForm").ligerForm(formConfig);
            //状态初始化
            $('input[name="library_typeBox"]').val(typeDate[3].name);
            //状态：   有效  1       无效  0
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "common"},function(data){
                var statusHtml="";
                $.each(data, function (i, item) {
                    statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                var library_statusBox= $("#library_statusBox").html(statusHtml);
                library_statusBox.trigger('change.select2');
            });
//给表单赋值
            common.callAjax('POST',true,ctx+"/opestandard/standardLibrary/editObj",'json',{id:parent.editId},function(data){
                var editForm  = liger.get("inputForm");
                editForm.setData(data);
                $("input[name='device_id']").val(data.devicename);
                $("#deviceId").val(data.deviceids);
                $('#library_statusBox').val(data.library_status).trigger('change.select2');
                var attach_ids=data.attach_id.split(",");
                $.each(attach_ids,function(index,item){
                    if(index==0){
                        $("#field").val(item);
                    }else{
                        $('#filearea').append('<div class="file"><a href="#" class="uploadclass">上传</a>&nbsp;<a href="#" class="removeclass">删除</a><input type="text" name="mytext[]" id="field_'+index+'" value="'+item+'" style="margin-bottom: 0;" readonly /></div>');
                    }
                });
            })
            var data1,data2,data3,data4,data5,data6;
            //给检修列表赋值
            common.callAjax('POST',false,ctx+"/opestandard/standardLibrary/quFault",'json',{id:parent.editId},function(data){
                data1={Rows:data};
            })
            common.callAjax('POST',false,ctx+"/opestandard/standardLibrary/quMaintain",'json',{id:parent.editId},function(data){
                data2={Rows:data};
            })
            common.callAjax('POST',false,ctx+"/opestandard/standardLibrary/quPatrol",'json',{id:parent.editId},function(data){
                data3={Rows:data};
            })
            common.callAjax('POST',false,ctx+"/opestandard/standardLibrary/quFailure",'json',{id:parent.editId},function(data){
                data4={Rows:data};
            })
            common.callAjax('POST',false,ctx+"/opestandard/standardLibrary/quOpe",'json',{id:parent.editId},function(data){
                data5={Rows:data};
            })
            common.callAjax('POST',false,ctx+"/opestandard/standardLibrary/quSafe",'json',{id:parent.editId},function(data){
                data6={Rows:data};
            })
            $(f_initGrid1);
            var manager1, g1;
            function f_initGrid1()
            {
                g1 =  manager1 = $("#faultStdTable").ligerGrid({
                    columns: [
                        { display: '检修标准编码', name: 'fault_standard_code',
                             type: 'text'
                        },
                        { display: '检修标准描述', name: 'fault_standard_desc',
                             type: 'text'
                        },
                        { display: '检修标准说明', name: 'fault_standard_explain',
                             type: 'text'
                        },
                        { display: '备注', name: 'fault_standard_remark',
                          type: 'text'
                        }

                    ],
                    onSelectRow: function (rowdata, rowindex)
                    {
                    },
                    enabledEdit: true, isScroll: false, checkbox:true,rownumbers:true,
                    data:data1,
                    width: '88%'
                });
            }

            //保养标准
            var manager2, g2;
            function f_initGrid2()
            {
                g2 =  manager2 = $("#maintainStdTable").ligerGrid({
                    columns: [
                        { display: '保养标准编码', name: 'maintain_standard_code',
                          type: 'text'
                        },
                        { display: '保养标准描述', name: 'maintain_standard_desc',
                            type: 'text'
                        },
                        { display: '保养标准说明', name: 'maintain_standard_explain',
                            type: 'text'
                        },
                        { display: '备注', name: 'maintain_standard_remark',
                            type: 'text'
                        }
                    ],
                    onSelectRow: function (rowdata, rowindex)
                    {
                    },
                    enabledEdit: true, isScroll: false, checkbox:true,rownumbers:true,
                    data:data2,
                    width: '88%'
                });
            }

            //巡检标准

            var manager3, g3;
            function f_initGrid3()
            {
                g3 =  manager3 = $("#patrolStdTable").ligerGrid({
                    columns: [
                        { display: '巡检标准编码', name: 'patrol_standard_code',
                             type: 'text'
                        },
                        { display: '巡检标准描述', name: 'patrol_standard_desc',
                             type: 'text'
                        },
                        { display: '巡检标准说明', name: 'patrol_standard_explain',
                            type: 'text'
                        },
                        { display: '备注', name: 'patrol_standard_remark',
                            type: 'text'
                        }

                    ],
                    onSelectRow: function (rowdata, rowindex)
                    {
                    },
                    enabledEdit: true, isScroll: false, checkbox:true,rownumbers:true,
                    data:data3,
                    width: '88%'
                });
            }

            //缺陷故障
            var manager4, g4;
            function f_initGrid4()
            {
                g4 =  manager4 = $("#failureStdTable").ligerGrid({
                    columns: [
                        { display: '故障现象编码', name: 'failure_phenomena_code',
                            type: 'text'
                        },
                        { display: '优先级', name: 'failure_phenomena_priority',type:'text',
                             type: 'selectGrid', data: [{ priority: 0, text: '高' }, { priority: 1, text: '中'},{ priority: 2, text: '低'}],
                                valueField: 'priority',textField: 'text',
                            render: function (item) {
                                var text = "";
                                $.each([{ priority: 0, text: '高' }, { priority: 1, text: '中'},{ priority: 2, text: '低'}], function (i, data) {
                                    if (data.priority == item.failure_phenomena_priority) {
                                        text = data.text;
                                    }
                                })
                                return text;
                            }
                        },
                        { display: '故障现象描述', name: 'failure_phenomena_desc',
                             type: 'text'
                        },
                        { display: '故障原因编码', name: 'failure_cause_code',
                             type: 'text'
                        },
                        { display: '严重程度', name: 'failure_cause_serverity',
                            type: 'selectGrid', data: [{ serverity: 0, text: '非常严重' }, { serverity: 1, text: '严重'},{ serverity: 2, text: '一般'}],
                                valueField: 'serverity',textField: 'text',
                            render: function (item) {
                                var text = "";
                                $.each([{ serverity: 0, text: '非常严重' }, { serverity: 1, text: '严重'},{ serverity: 2, text: '一般'}], function (i, data) {
                                    if (data.serverity == item.failure_phenomena_priority) {
                                        text = data.text;
                                    }
                                })
                                return text;
                            }
                        },
                        { display: '故障原因描述', name: 'failure_cause_desc',
                             type: 'text'
                        },
                        {
                            display: '故障措施编码', name: 'failure_measures_code',
                            type: 'text'
                        },
                        {
                            display: '故障措施描述', name: 'failure_measures_desc',
                             type: 'text'
                        }
                    ],
                    onSelectRow: function (rowdata, rowindex)
                    {
                    },
                    enabledEdit: true, isScroll: false, checkbox:true,rownumbers:true,
                    data:data4,
                    width: '88%'
                });
            }

            //运行标准

            var manager5, g5;
            function f_initGrid5()
            {
                g5 =  manager5 = $("#operationStdTable").ligerGrid({
                    columns: [
                        { display: '运行标准编码', name: 'operation_standard_code',
                             type: 'text'
                        },
                        { display: '运行标准描述', name: 'operation_standard_desc',
                             type: 'text'
                        },
                        { display: '运行标准说明', name: 'operation_standard_explain',
                             type: 'text'
                        },
                        { display: '备注', name: 'operation_standard_remark',
                            type: 'text'
                        }

                    ],
                    onSelectRow: function (rowdata, rowindex)
                    {
                    },
                    enabledEdit: true, isScroll: false, checkbox:true,rownumbers:true,
                    data:data5,
                    width: '88%'
                });
            }

            //安全标准
            var manager6, g6;
            function f_initGrid6()
            {
                g6 =  manager6 = $("#safeStdTable").ligerGrid({
                    columns: [
                        { display: '安全标准编码', name: 'safety_standard_code',
                            type: 'text'
                        },
                        { display: '安全标准描述', name: 'safety_standard_desc',
                           type: 'text'
                        },
                        { display: '安全标准说明', name: 'safety_standard_explain',
                            type: 'text'
                        },
                        { display: '备注', name: 'safety_standard_remark',
                            type: 'text'
                        }

                    ],
                    onSelectRow: function (rowdata, rowindex)
                    {
                    },
                    enabledEdit: true, isScroll: false, checkbox:true,rownumbers:true,
                    data:data6,
                    width: '88%'
                });
            }


            // 标准选择多个设备的弹出框
            $("input[name='device_id']").on("click",function () {
                layer.open({
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getArea(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: "/a/opestandard/standardLibrary/DeviceSelectUI"
                });
            });

            var FieldCount=30;//为了不让id重复
            $('#uploadmorefile').on('click',function(){
                var maxInputfile=4;
                if($(".file").length<maxInputfile){
                    FieldCount++;
                    $('#filearea').append('<div class="file"><a href="#" class="uploadclass">上传</a>&nbsp;<a href="#" class="removeclass">删除</a><input type="text" name="mytext[]" id="field_'+FieldCount+'" style="margin-bottom: 0;" readonly /></div>');
                }

            })
            $('#filearea').on('click','.uploadclass',function(){
                var id=$(this).closest('.file').find('input').attr('id');
                var finder = new CKFinder();
                finder.selectActionFunction = function(fileUrl){
                    $("#"+id).val(fileUrl);

                } //当选中图片时执行的函数
                finder.popup();//调用窗口
            })
            $('#filearea').on('click','.removeclass',function(){
                if($(".file").length>1){
                    $(this).parent('div').remove();
                }
            })
            $("#closeBtn").on("click",function () {
                parent.layer.closeAll();

            });


            //设置选择下拉框只读
            $("select").attr("disabled","disabled");
            //设置设备名称只读 找到input当前元素,设置只读.
            $("input[ligeruiid='device_id']").attr("disabled","disabled");



        })
	</script>
</head>
<body>
<form id="inputForm" action="${ctx}/opestandard/standardLibrary/update" method="post" class="form-horizontal">


</form>
<div class="editDiv">
	<div class="l-clear"></div>
	<div class="subeditDiv" id="faultStdTable" ></div>
	<div class="subeditDiv" id="maintainStdTable" ></div>
	<div class="subeditDiv" id="patrolStdTable" ></div>
	<div class="subeditDiv" id="failureStdTable" ></div>
	<div class="subeditDiv" id="operationStdTable" ></div>
	<div class="subeditDiv" id="safeStdTable"></div>
</div>
<div class="path">
	<div id="filearea">
		<div class="file">
			<div><b>附件</b></div>
			<a href="#" class="uploadclass">上传</a>&nbsp;<a href="#" id="uploadmorefile">新增</a>
			<input type="text" name="mytext[]" id="field" style="margin-bottom: 0;" readonly/>
		</div>
	</div>
</div>
<div class="form-actions">
	<input id="deviceId" type="hidden"/>
	<input id="closeBtn"  type="button"  value="关 闭"/>
</div>
</body>
</html>
