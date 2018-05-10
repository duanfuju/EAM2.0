<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>巡检项</title>
	<style>
		.editDiv .l-panel-bar{
			display: none ;
		}
	</style>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
        $(function () {
            var _charGrid=null;//字符型表格
            var _numGrid=null;//数值型表格
            var _devices="";//设备的ids
            var _subject_valuetype="1";//设置显示的值类型
            var _editData=null;
            window.getDevices = function () {//子页面获取父页面的设备的ids的数据
                return _devices;
            };
            window.setDevices = function (devices){//子页面设置父页面的设备的ids的数据
                _devices=devices;
            };
            var _subjectDefault=[//设置默认结果
                { text: '结果一', id: '0' },
                { text: '结果二', id: '1' },
                { text: '结果三', id: '2' },
            ];

            //字符型		初始化数据
            var _charData = [{
                "subject_name":null,
                "subject_content":null,
                "subject_standard":null,
                "subject_way":null,
                "subject_value1":null,
                "subject_value2":null,
                "subject_value3":null,
                "subject_default":null
            }];
            //数值型		初始化数据
            var _numData = [{
                "subject_name":null,
                "subject_content":null,
                "subject_standard":null,
                "subject_way":null,
                "subject_unit":0,
                "subject_decimal":0,
                "subject_sx_value":null,
                "subject_xx_value":null,
                "subject_ck_value":null
            }];
            //深克隆
            function cloneObject(obj) {
                //var o = obj instanceof Array ? [] : {};
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

            var formField = cloneObject(parent.formConfig);

            //初始化下拉树数据
            var formFieldDisplay=[];
            $.each(formField, function(index,val){
                if(val.name=="subject_status"){
                    val.options={
                        data:parent._subjectStatus
                    }
                }else if(val.name=="subject_valuetype"){
                    val.options={
                        data:parent._subjectValuetype
                    }

                }else if(val.name=="subject_way"){
                    val.options={
                        data:parent._subjectWay
                    }
                }
                if(val.name=="dev_id"
                    ||val.name=="subject_valuetype"
                    ||val.name=="subject_status"){
                    formFieldDisplay.push(val)
                }
                val.readonly = true;
            });

            //创建表单结构
            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields: formFieldDisplay
            };

            $("#inputForm").ligerForm(formConfig);

            //编辑页面字段赋值
            common.callAjax('post',false,common.interfaceUrl.inspectionSubjectEditObj,"json",{"id":parent.editId},function(data){
                _editData=data;
                _subject_valuetype=data.subject_valuetype;
                if(data.subject_valuetype=="1"){//设置数据值的数据
                    _charData=[];
                    _charData.push(data);
                    $(f_initGrid1);
                }else  if(data.subject_valuetype=="0"){
                    _numData=[];
                    _numData.push(data);
                    $(f_initGrid2);
                }
            });



            function f_initGrid1()
            {
                _charGrid = $("#charStdTable").ligerGrid({
                    columns: [
                        { display: '巡检项', name: 'subject_name',
                            editor: { type: 'text' }
                        },
                        { display: '内容和方法', name: 'subject_content',
                            editor: { type: 'text' }
                        },
                        { display: '判定标准', name: 'subject_standard',
                            editor: { type: 'text' }
                        },
                        { display: '测量通道', name: 'subject_way',type:'text',
                            editor: { type: 'selectGrid', data: parent._subjectWay,
                                valueField: 'id',textField: 'text'},
                            render: function (item)
                            {
                                var name;
                                if(item != null && item.subject_way!= null && item.subject_way != ""){
                                    $.each(parent._subjectWay, function (i, data) {
                                        if(data.id == item.subject_way){
                                            name = data.text;
                                        }
                                    });
                                }
                                return name;
                            }
                        },
                        { display: '结果一', name: 'subject_value1',
                            editor: { type: 'text' }
                        },
                        { display: '结果二', name: 'subject_value2',
                            editor: { type: 'text' }
                        },
                        { display: '结果三', name: 'subject_value3',
                            editor: { type: 'text' }
                        },
                        {
                            display: '默认结果', name: 'subject_default',type:'text',
                            editor: { type: 'selectGrid', data:_subjectDefault,
                                valueField: 'id',textField: 'text'},
                            render: function (item)
                            {
                                var name;
                                if(item != null && item.subject_default != null && item.subject_default != ""){
                                    $.each(_subjectDefault, function (i, data) {
                                        if(data.id == item.subject_default){
                                            name = data.text;
                                        }
                                    });
                                }
                                return name;
                            }
                        }

                    ],
                    onSelectRow: function (rowdata, rowindex)
                    {
                    },
                    enabledEdit: false, isScroll: false, checkbox:false,rownumbers:true,
                    data:{Rows: _charData},
                    width: '88%'
                });
            }

            $('#charStdTable').on('click','.dele',function(){
                if (_charGrid.getData().length ==1){
                    alert("只剩一项时无法删除");
                }else {
                    _charGrid.deleteRow($(this).data("id"));
                }
            });
            $('#charStdTable').on('click','.add',function(){
                _charGrid.addRow({
                    "subject_name":null,
                    "subject_content":null,
                    "subject_standard":null,
                    "subject_way":null,
                    "subject_value1":null,
                    "subject_value2":null,
                    "subject_value3":null,
                    "subject_default":null
                });
            });
            function f_initGrid2(){
                _numGrid=$("#numberStdTable").ligerGrid({
                    columns: [
                        { display: '巡检项', name: 'subject_name',
                            editor: { type: 'text' }
                        },
                        { display: '内容和方法', name: 'subject_content',
                            editor: { type: 'text' }
                        },
                        { display: '判定标准', name: 'subject_standard',
                            editor: { type: 'text' }
                        },
                        { display: '测量通道', name: 'subject_way',type:'text',
                            editor: { type: 'selectGrid', data: parent._subjectWay,
                                valueField: 'id',textField: 'text'},
                            render: function (item)
                            {
                                var name;
                                if(item != null && item.subject_way != null && item.subject_way != ""){
                                    $.each(parent._subjectWay, function (i, data) {
                                        if(data.id == item.subject_way){
                                            name = data.text;
                                        }
                                    });
                                }
                                return name;
                            }
                        },
                        { display: '单位', name: 'subject_unit',type:'text',
                            editor: { type: 'selectGrid', data: parent._unit,
                                valueField: 'id',textField: 'text'},
                            render: function (item)
                            {
                                var name;
                                if(item != null && item.subject_unit != null && item.subject_unit != ""){
                                    $.each(parent._unit, function (i, data) {
                                        if(data.id == item.subject_unit){
                                            name = data.text;
                                        }
                                    });
                                }
                                return name;
                            }
                        },
                        { 	display: '小数位', name: 'subject_decimal',type:'text',
                            editor: {
                                type: 'selectGrid',
                                data:[{id:0,text:"0"},{id:1,text:"1"},{id:2,text:"2"}],
                                valueField: 'id',
                                textField: 'text'
                            },
                            render: function (item) {

                                var name = "";
                                $.each([{id:0,text:"0"},{id:1,text:"1"},{id:2,text:"2"}], function (i, data) {
                                    if (data.id == item.subject_decimal) {
                                        name = data.text;
                                    }
                                });
                                return name;
                            }
                        },
                        { display: '上限值', name: 'subject_sx_value',
                            editor: { type: 'text' }
                        },
                        { display: '下限值', name: 'subject_xx_value',
                            editor: { type: 'text' }
                        },
                        { display: '参考值', name: 'subject_ck_value',
                            editor: { type: 'text' }
                        }

                    ],
                    onSelectRow: function (rowdata, rowindex)
                    {
                    },
                    enabledEdit: false, isScroll: false, checkbox:false,rownumbers:true,
                    data:{Rows: _numData},
                    width: '88%'
                });
            }


            $('#numberStdTable').on('click','.dele',function(){
                if (_numGrid.getData().length ==1){
                    alert("只剩一项时无法删除");
                }else {
                    _numGrid.deleteRow($(this).data("id"));
                }
            });
            $('#numberStdTable').on('click','.add',function(){
                _numGrid.addRow({
                    "subject_name":null,
                    "subject_content":null,
                    "subject_standard":null,
                    "subject_way":null,
                    "subject_unit":0,
                    "subject_decimal":0,
                    "subject_sx_value":null,
                    "subject_xx_value":null,
                    "subject_ck_value":null
                });
            });

            //赋值
            var editForm  = liger.get("inputForm");
            editForm.setData(_editData);
            $("input[name='dev_id']").val(_editData.dev_name);//将设备名称设置到页面上的dev_id的input上
            _devices=_editData.dev_id;

            //值类型下拉切换
            $("#subject_valuetypeBox").on('change',function () {
                //alert($("#subject_valuetypeBox").val())
                debugger;
                if(_subject_valuetype=="1"){
                    $(f_initGrid2);
                    _subject_valuetype="-1";
                }else if(_subject_valuetype=="0"){
                    $(f_initGrid2);
                    $(f_initGrid1);
                    _subject_valuetype="-1";
                }
                if($("#subject_valuetypeBox").val() =="1"){
                    $("#charStdTable").show().siblings().hide();
                }else if($("#subject_valuetypeBox").val() =="0"){
                    $("#numberStdTable").show().siblings().hide();
                }
            });
            //设置只读
            $("select").attr("disabled","disabled");

            $("#closeBtn").on("click",function () {
                parent.layer.closeAll();
            });
        });


	</script>
</head>
<body>
<form id="inputForm" action="" method="post" class="form-horizontal">
</form>
<div class="editDiv">
	<div class="subeditDiv" id="numberStdTable" ></div>
	<div class="subeditDiv" id="charStdTable" ></div>
</div>
<div class="form-actions">
	<input id="closeBtn"  type="button"  value="关 闭"/>
</div>
</body>
</html>