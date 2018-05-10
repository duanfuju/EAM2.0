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
                _subject_valuetype=data.subject_valuetype;
                if(_subject_valuetype=="1"){//设置数据值的数据
                    _charData=[];
                    _charData.push(data);

                }else  if(_subject_valuetype=="0"){
                    _numData=[];
                    _numData.push(data);

                }
                //赋值
                var editForm  = liger.get("inputForm");
                editForm.setData(data);
                $("input[name='dev_id']").val(data.dev_name);//将设备名称设置到页面上的dev_id的input上
                _devices=data.dev_id;
            });


            // 选择多个设备的弹出框
            $("input[name='dev_id']").on("click",function () {
                layer.open({
                    type: 2,
                    title:'选择设备',
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getAreaInner(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: common.interfaceUrl.inspectionSubjectDeviceSelectUI
                });
            });

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
                    enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                    data:{Rows: _charData},
                    width: '88%'
                });
            $("#charStdTable").hide();
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
                    enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,onAfterEdit: f_onAfterEdit,
                    data:{Rows: _numData},
                    width: '88%'
                });
			//数值型小数位判断
			function f_onAfterEdit(e){
			    var smallNum=Number(e.record.subject_decimal);
			    var subject_sx_value=Number(e.record.subject_sx_value);
                var subject_xx_value=Number(e.record.subject_xx_value);
                var subject_ck_value=Number(e.record.subject_ck_value);
                if(subject_sx_value&&subject_ck_value||subject_xx_value&&subject_ck_value){
                    layer.msg("上限值和下限值不能与参考值同时存在",{time: 2000,icon:2});
                    _numGrid.updateCell('subject_xx_value','', e.record);
                    _numGrid.updateCell('subject_sx_value','', e.record);
                    _numGrid.updateCell('subject_ck_value','', e.record);
                }else if(subject_ck_value==0){
                    _numGrid.updateCell('subject_sx_value',subject_sx_value.toFixed(smallNum), e.record);
                    _numGrid.updateCell('subject_xx_value',subject_xx_value.toFixed(smallNum), e.record);
				}else if(subject_ck_value){
                    _numGrid.updateCell('subject_xx_value','', e.record);
                    _numGrid.updateCell('subject_sx_value','', e.record);
                    _numGrid.updateCell('subject_ck_value',subject_ck_value.toFixed(smallNum), e.record);
				}else{
                    _numGrid.updateCell('subject_xx_value',subject_xx_value.toFixed(smallNum), e.record);
                    _numGrid.updateCell('subject_ck_value',subject_ck_value.toFixed(smallNum), e.record);
				}
            }
            $("#numberStdTable").hide();
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

            if(_subject_valuetype=="1"){
                $("#charStdTable").show();
                $("#numberStdTable").hide();
            }else  if(_subject_valuetype=="0"){
                $("#charStdTable").hide();
                $("#numberStdTable").show();
            }
            //值类型下拉切换
            $("#subject_valuetypeBox").on('change',function () {
                if($("#subject_valuetypeBox").val() =="1"){
                    $("#charStdTable").show().siblings().hide();
                }else if($("#subject_valuetypeBox").val() =="0"){
                    $("#numberStdTable").show().siblings().hide();
                }
            });
            //表单提交
            $("#btnSubmit").on("click",function () {
                var form = liger.get('inputForm');
                if (!form.valid()) {
                    form.showInvalid();
                }else{
                    //保存数据object
                    var param={};
                    param.id_key=parent.editId;
                    param.dev_id=_devices;
                    param.charSubjects=_charGrid.getData();//字符型
                    param.numberSubjects=_numGrid.getData();//数值型
                    param.subject_status=$('#subject_statusBox').val();//状态值
                    console.log(param);
                    console.log(JSON.stringify(param));
                    common.callAjax('post',false,common.interfaceUrl.inspectionSubjectSave,"text",{param :JSON.stringify(param)} ,function(data) {
                        if(data=="success"){
                            layer.msg('修改成功！',{icon: 1,time: 1000}, function(index){
                                parent.$("#mytable").DataTable().ajax.reload();
                                parent.layer.closeAll();
                            });
                        }else{
                            layer.msg("修改失败！",{time: 1000,icon:2});
                        }
                    });
                }
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
	<input id="btnSubmit" type="button" value="保 存"/>
</div>
</body>
</html>