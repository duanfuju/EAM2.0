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
            })
            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields: formFieldnew

            };

            //debugger;
            console.log(parent.formConfig)

            console.log(parent.editId);
            $("#inputForm").ligerForm(formConfig);
            liger.get('inputForm').setEnabled('library_code' , false);
            //标准类型初始化
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
                            editor: { type: 'text' }
                        },
                        { display: '检修标准说明', name: 'fault_standard_explain',
                            editor: { type: 'text' }
                        },
                        { display: '备注', name: 'fault_standard_remark',
                            editor: { type: 'text' }
                        },
                        { display: '操作', isSort: false,
                            render: function (rowdata, rowindex, value)
                            {
                                var h = "";
                                h += "<a class='add' >添加</a> ";
                                h += "<a class='dele' data-id='"+ rowindex +"'>删除</a> ";
                                return h;
                            }
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

            $('#faultStdTable').on('click','.dele',function(){
                if (manager1.getData().length ==1){
                    alert("只剩一项时无法删除");
                }else {
                    manager1.deleteRow($(this).data("id"));
                }
            })
            $('#faultStdTable').on('click','.add',function(){
                var val=manager1.getData()[manager1.getData().length-1].fault_standard_code;
                val='JXBZ'+(Array(3).join(0)+(parseInt(val.substring(4,val.length))+1)).slice(-3);
                manager1.addRow({
                    "fault_standard_code":val,
                    "fault_standard_desc":null,
                    "fault_standard_explain":null,
                    "fault_standard_remark":null
                });
            })



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
                            editor: { type: 'text' }
                        },
                        { display: '保养标准说明', name: 'maintain_standard_explain',
                            editor: { type: 'text' }
                        },
                        { display: '备注', name: 'maintain_standard_remark',
                            editor: { type: 'text' }
                        },
                        { display: '操作', isSort: false,
                            render: function (rowdata, rowindex, value)
                            {
                                var h = "";
                                h += "<a class='add'>添加</a> ";
                                h += "<a class='dele' data-id='"+ rowindex +"'>删除</a> ";
                                return h;
                            }
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

            $('#maintainStdTable').on('click','.dele',function(){
                if (manager2.getData().length ==1){
                    alert("只剩一项时无法删除");
                }else {
                    manager2.deleteRow($(this).data("id"));
                }
            })
            $('#maintainStdTable').on('click','.add',function(){
                //var row = manager.getSelectedRow();
                //var data = manager.getData();
                //表格参数key必须填，哪怕value是null，否则getData()取不到值
                var val=manager2.getData()[manager2.getData().length-1].maintain_standard_code;
                val='BYBZ'+(Array(3).join(0)+(parseInt(val.substring(4,val.length))+1)).slice(-3);
                manager2.addRow({
                    "maintain_standard_code":val,
                    "maintain_standard_desc":null,
                    "maintain_standard_explain":null,
                    "maintain_standard_remark":null
                });
            })




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
                            editor: { type: 'text' }
                        },
                        { display: '巡检标准说明', name: 'patrol_standard_explain',
                            editor: { type: 'text' }
                        },
                        { display: '备注', name: 'patrol_standard_remark',
                            editor: { type: 'text' }
                        },
                        { display: '操作', isSort: false,
                            render: function (rowdata, rowindex, value)
                            {
                                var h = "";
                                h += "<a class='add'>添加</a> ";
                                h += "<a class='dele' data-id='"+ rowindex +"'>删除</a> ";
                                return h;
                            }
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

            $('#patrolStdTable').on('click','.dele',function(){
                if (manager3.getData().length ==1){
                    alert("只剩一项时无法删除");
                }else {
                    manager3.deleteRow($(this).data("id"));
                }
            })
            $('#patrolStdTable').on('click','.add',function(){
                //var row = manager.getSelectedRow();
                //var data = manager.getData();
                //表格参数key必须填，哪怕value是null，否则getData()取不到值
                var val=manager3.getData()[manager3.getData().length-1].patrol_standard_code;
                val='XJBZ'+(Array(3).join(0)+(parseInt(val.substring(4,val.length))+1)).slice(-3);
                manager3.addRow({
                    "patrol_standard_code":val,
                    "patrol_standard_desc":null,
                    "patrol_standard_explain":null,
                    "patrol_standard_remark":null
                });
            })

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
                            editor: { type: 'selectGrid', data: [{ priority: 0, text: '高' }, { priority: 1, text: '中'},{ priority: 2, text: '低'}],
                                valueField: 'priority',textField: 'text'},
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
                            editor: { type: 'text' }
                        },
                        { display: '故障原因编码', name: 'failure_cause_code',
                            type: 'text'
                        },
                        { display: '严重程度', name: 'failure_cause_serverity',type:'text',
                            editor: { type: 'selectGrid', data: [{ serverity: 0, text: '非常严重' }, { serverity: 1, text: '严重'},{ serverity: 2, text: '一般'}],
                                valueField: 'serverity',textField: 'text'},
                            render: function (item) {
                                var text = "";
                                $.each([{ serverity: 0, text: '非常严重' }, { serverity: 1, text: '严重'},{ serverity: 2, text: '一般'}], function (i, data) {
                                    if (data.serverity == item.failure_cause_serverity) {
                                        text = data.text;
                                    }
                                })
                                return text;
                            }
                        },
                        { display: '故障原因描述', name: 'failure_cause_desc',
                            editor: { type: 'text' }
                        },
                        {
                            display: '故障措施编码', name: 'failure_measures_code',
                             type: 'text'
                        },
                        {
                            display: '故障措施描述', name: 'failure_measures_desc',
                            editor: { type: 'text' }
                        },
                        { display: '操作', isSort: false,
                            render: function (rowdata, rowindex, value)
                            {
                                var h = "";
                                h += "<a class='add'>添加</a> ";
                                h += "<a class='dele' data-id='"+ rowindex +"'>删除</a> ";
                                return h;
                            }
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

            $('#failureStdTable').on('click','.dele',function(){
                if (manager4.getData().length ==1){
                    alert("只剩一项时无法删除");
                }else {
                    manager4.deleteRow($(this).data("id"));
                }
            })
            $('#failureStdTable').on('click','.add',function(){
                //var row = manager.getSelectedRow();
                //var data = manager.getData();
                //表格参数key必须填，哪怕value是null，否则getData()取不到值
                var val=manager4.getData()[manager4.getData().length-1].failure_phenomena_code;
                var val1='GZXX'+(Array(3).join(0)+(parseInt(val.substring(4,val.length))+1)).slice(-3);
                val =manager4.getData()[manager4.getData().length-1].failure_cause_code;
                var val2='GZYY'+(Array(3).join(0)+(parseInt(val.substring(4,val.length))+1)).slice(-3);
                val=manager4.getData()[manager4.getData().length-1].failure_measures_code;
                var val3='GZCS'+(Array(3).join(0)+(parseInt(val.substring(4,val.length))+1)).slice(-3);
                manager4.addRow({
                    "failure_phenomena_code":val1,
                    "failure_phenomena_priority":null,
                    "failure_phenomena_desc":null,
                    "failure_cause_code":val2,
                    "failure_cause_serverity":null,
                    "failure_cause_desc":null,
                    "failure_measures_code":val3,
                    "failure_measures_desc":null
                });
            })


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
                            editor: { type: 'text' }
                        },
                        { display: '运行标准说明', name: 'operation_standard_explain',
                            editor: { type: 'text' }
                        },
                        { display: '备注', name: 'operation_standard_remark',
                            editor: { type: 'text' }
                        },
                        { display: '操作', isSort: false,
                            render: function (rowdata, rowindex, value)
                            {
                                var h = "";
                                h += "<a class='add'>添加</a> ";
                                h += "<a class='dele' data-id='"+ rowindex +"'>删除</a> ";
                                return h;
                            }
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

            $('#operationStdTable').on('click','.dele',function(){
                if (manager5.getData().length ==1){
                    alert("只剩一项时无法删除");
                }else {
                    manager5.deleteRow($(this).data("id"));
                }
            })
            $('#operationStdTable').on('click','.add',function(){
                //var row = manager.getSelectedRow();
                //var data = manager.getData();
                //表格参数key必须填，哪怕value是null，否则getData()取不到值
                var val=manager5.getData()[manager5.getData().length-1].operation_standard_code;
                val='YXBZ'+(Array(3).join(0)+(parseInt(val.substring(4,val.length))+1)).slice(-3);
                manager5.addRow({
                    "operation_standard_code":val,
                    "operation_standard_desc":null,
                    "operation_standard_explain":null,
                    "operation_standard_remark":null
                });
            })

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
                            editor: { type: 'text' }
                        },
                        { display: '安全标准说明', name: 'safety_standard_explain',
                            editor: { type: 'text' }
                        },
                        { display: '备注', name: 'safety_standard_remark',
                            editor: { type: 'text' }
                        },
                        { display: '操作', isSort: false,
                            render: function (rowdata, rowindex, value)
                            {
                                var h = "";
                                h += "<a class='add'>添加</a> ";
                                h += "<a class='dele' data-id='"+ rowindex +"'>删除</a> ";
                                return h;
                            }
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

            $('#safeStdTable').on('click','.dele',function(){
                if (manager6.getData().length ==1){
                    alert("只剩一项时无法删除");
                }else {
                    manager6.deleteRow($(this).data("id"));
                }
            })
            $('#safeStdTable').on('click','.add',function(){
                //var row = manager.getSelectedRow();
                //var data = manager.getData();
                //表格参数key必须填，哪怕value是null，否则getData()取不到值
                var val=manager6.getData()[manager6.getData().length-1].safety_standard_code;
                val='AQBZ'+(Array(3).join(0)+(parseInt(val.substring(4,val.length))+1)).slice(-3);
                manager6.addRow({
                    "safety_standard_code":val,
                    "safety_standard_desc":null,
                    "safety_standard_explain":null,
                    "safety_standard_remark":null
                });
            })

			// 标准选择多个设备的弹出框
            $("input[name='device_id']").on("click",function () {
                layer.open({
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getAreaInner(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: "/a/opestandard/standardLibrary/DeviceSelectUI"
                });
            });
			$("#btnSubmit").on("click",function () {
                var form = liger.get('inputForm');
                if (!form.valid()) {
                    form.showInvalid();
                }
                if ($("#maintainStdTable").children().length <1){
                    $(f_initGrid2);
                    $(f_initGrid3);
                    $(f_initGrid4);
                    $(f_initGrid5);
                    $(f_initGrid6);
                    $("#faultStdTable").show().siblings().hide();
                }
//复杂表单用ajax提交修改
				var library_code=$("input[name='library_code']").val();
				var library_name=$("input[name='library_name']").val();
                var library_id=$("input[name='id_key']").val();

                var fault=manager1.getData();//检修标准
                var maintain=manager2.getData();//保养标准
                var patrol=manager3.getData();//巡检标注
                var failure=manager4.getData();//缺陷故障
                var operation=manager5.getData();//运行标准
                var safe=manager6.getData();//安全标准
                debugger;
                var param=new Object();
                param.id_key=library_id;
                param.library_code=library_code;
                param.library_name=library_name;
                param.library_status=$('#library_statusBox').val();
                var array=$('#deviceId').val().split(',');
                var devArray=new Array();
                for(var i=0;i<array.length;i++){
                    devArray.push({device_id:array[i]});
                }
                var filestr="";
                $('.file').each(function(){//上传文件
                    if($(this).find('input').val()!="" || $(this).find('input').val()!=null){
                        filestr+=","+$(this).find('input').val();
                    }
                });
                if(filestr.indexOf(",")!=-1){
                    param.attach_id=filestr.substring(1);
                }
                param.standardDevices=devArray;
                param.standardFaults=fault;
                param.standardMaintains=maintain;
                param.standardPatrols=patrol;
                param.standardFailures=failure;
                param.standardOpes=operation;
                param.standardSafetys=safe;
				console.log(param);
                common.callAjax('post',false,'${ctx}/opestandard/standardLibrary/update',"text",{param:JSON.stringify(param)},function(data){
                    if(data=="success"){
                        layer.msg('修改成功！',{icon: 1,time: 1000}, function(index){
                            parent.$("#mytable").DataTable().ajax.reload();
                            parent.layer.closeAll();
                        });
                    }else{
                        layer.msg("修改失败！",{time: 1000,icon:2});
                }
            })
			})
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
	<input id="btnSubmit" type="button" value="保 存"/>
</div>
</body>
</html>
