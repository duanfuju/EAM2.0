<%@ page import="org.activiti.engine.form.FormType" %>
<%@ page import="org.activiti.engine.form.FormProperty" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<html>
<head>
	<title>标准库审批</title>
	<style type="text/css">
		#pinfo th {background: #f7f7f9;}
		#flowstatus tr {
			border: 1px solid rgba(34, 34, 34, 0.17);
		}
		#flowstatus td,#flowstatus th{
			width: 1%;
			height: 40px;
		}
		#flowstatus .blue{
			background-color: rgba(21, 199, 189, 0.19);
		}


        input[id^='vid'],input[id^='img'] {
			display: inline-block;

			vertical-align: middle;
			-webkit-border-radius: 2px;
			-moz-border-radius: 2px;
			border-radius: 2px;
		}

        input[id^='vid'],input[id^='img'] {
			position:relative;
			z-index:1;
			background-color: #ffffff;
			border: 1px solid #cccccc;
			-webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
			-moz-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
			box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
			color: #7B7B7B;
		}


	</style>
	<meta name="decorator" content="default"/>
	<script type="text/javascript" src="/js/bootstrap.min.js"></script>

	<script type="text/javascript">
        var executionId = '${task.executionId}';
        var businessKey = '${businessKey}';
		var taskid = '${task.id}';
        var activityList ='${activityList}';

        $(function () {
            /**渲染流程详情内容-start*/
            activityList=JSON.parse(activityList)
            showData(activityList);
            /**渲染流程详情内容-end*/

            businessKey = businessKey.substring(businessKey.indexOf(":")+1);

            //ajax提交
            $("#btnFeedBack").on('click', function() {

                var form = liger.get('serviceForm');
                if (!form.valid()) {
                    form.showInvalid();
                }

                //复杂表单用ajax提交
                var param=new Object();//对象存储object



                var task_appearance=$("input[name='task_appearance']").val();
                var task_result=$('#task_resultBox').val();
				//保养内容编码赋值
				var task_maintain_code = "";
                $("input[name^='task_maintain_code']").each(function(){
                    task_maintain_code+=($(this).val()+",");
                    console.log($(this).val());
				})

                //保养内容赋值
                var task_maintain_content = "";
                $("input[name^='task_maintain_content']").each(function(){
                    task_maintain_content+=($(this).val()+",");
                    console.log($(this).val());
                })

                //保养备注赋值
                var task_maintain_remark = "";
                $("input[name^='task_maintain_remark']").each(function(){
                    task_maintain_remark+=($(this).val()+",");
                    console.log($(this).val());
                })

                //是否已反馈
                var task_fk_flag = "";
                $("select[id*='task_fk_flag']").each(function(){
                    task_fk_flag+=($(this).val()+",");
                    console.log($(this).val());
                })


                var tool_data = manager1.getData();
                var attachment_data = manager2.getData();
                var manhaur_data = manager3.getData();
                var other_data = manager4.getData();

                console.log(tool_data);
                console.log(attachment_data);
                console.log(manhaur_data);
                console.log(other_data);





                /**保养内容*/
                param.task_maintain_code = task_maintain_code;
                param.task_maintain_content = task_maintain_content;
                param.task_maintain_remark = task_maintain_remark;
                param.task_fk_flag = task_fk_flag;

                /**保养内容*/
                param.id = businessKey;
                param.task_appearance = task_appearance;
                param.task_result = task_result;
                param.toolList = tool_data;
                param.attachmentList = attachment_data;
                param.empList = manhaur_data;
                param.otherList = other_data;



                /**获取上传的图片*/
                var fileimg="";
                $('.fileimg').each(function(){//上传文件
                    if($(this).find('input').val()!="" || $(this).find('input').val()!=null){
                        fileimg+=","+$(this).find('input').val();
                    }
                });
                if(fileimg.indexOf(",")!=-1){
                    param.task_fk_photo=fileimg.substring(1);
                }

                /**获取上传的视频*/
                var filevid="";
                $('.filevid').each(function(){//上传文件
                    if($(this).find('input').val()!="" || $(this).find('input').val()!=null){
                        filevid+=","+$(this).find('input').val();
                    }
                });
                if(filevid.indexOf(",")!=-1){
                    param.task_fk_video=filevid.substring(1);
                }

                common.callAjax('post',false,'${ctx}/eam/maintainTask/feedBack',"text",{param:JSON.stringify(param),taskid:taskid},function(data){
                    if(data=="success"){
                        layer.msg('新增成功！',{icon:1,time: 1000}, function(index){
                            parent.$("#mytable").DataTable().ajax.reload();
                            parent.layer.closeAll();
                        });

                    }else if (data == "noauth") {
                        layer.msg("登录超时或无权限！",{time: 1000,icon:2});

                    }else{
                        layer.msg("提交失败！",{time: 1000,icon:2});

                    }
                })
            });

            /**附件上传-图片*/
            var FieldCount=0;
            var imgList = [];
            $('#uploadmoreimg').on('click',function(){
                var maxInputfile=4;
                if($(".fileimg").length<maxInputfile){
                    FieldCount++;
                    $('#imgarea').append('<div class="fileimg"><input type="text" name="mytext[]" id="img_'+FieldCount+'" readonly /><a href="#" class="uploadclass">上传</a>&nbsp;<a href="#" class="removeclass">删除</a></div>');
                }else{
                    layer.msg("最多可上传4张图片",{time: 1000,icon:7});
                }

            })
            $('#imgarea').on('click','.uploadclass',function(){
                var id=$(this).prev().attr('id');
                var finder = new CKFinder();
                finder.selectActionFunction = function(fileUrl){
                    imgList.push(fileUrl);
                    var nImgList = imgList.sort();
                    for(var i=0;i<nImgList.length;i++){
                        if(nImgList[i] == nImgList[i+1]){
                            imgList.splice(i,1);
                            layer.msg("无法重复上传",{time: 1000,icon:7});
                            return;
                        }
                    }
                    $("#"+id).val(fileUrl);
                } //当选中图片时执行的函数
                finder.popup();//调用窗口
            })
            $('#imgarea').on('click','.removeclass',function(){
                if($(".fileimg").length>1){
                    var thisimg = $(this).prev().prev().val();
                    for(var i=0;i<imgList.length;i++){
                        if(imgList[i] == thisimg){
                            imgList.splice(i,1);
                        }
                    }
                    $(this).parent('div').remove();
                }
            })

            /**附件上传-视频*/
            var FieldCount=0;
            var vidList = []
            $('#uploadmorevid').on('click',function(){
                var maxInputfile=4;
                if($(".filevid").length<maxInputfile){
                    FieldCount++;
                    $('#vidarea').append('<div class="filevid"><input type="text" name="mytext[]" id="vid_'+FieldCount+'" readonly /><a href="#" class="uploadclass">上传</a>&nbsp;<a href="#" class="removeclass">删除</a></div>');
                }else{
                    layer.msg("最多可上传4个视频",{time: 1000,icon:7});
                }

            })
            $('#vidarea').on('click','.uploadclass',function(){
                var id=$(this).prev().attr('id');
                var finder = new CKFinder();
                finder.selectActionFunction = function(fileUrl){
                    vidList.push(fileUrl);
                    var nVidList = vidList.sort();
                    for(var i=0;i<nVidList.length;i++){
                        if(nVidList[i] == nVidList[i+1]){
                            vidList.splice(i,1);
                            layer.msg("无法重复上传",{time: 1000,icon:7});
                            return;
                        }
                    }
                    $("#"+id).val(fileUrl);
                } //当选中图片时执行的函数
                finder.popup();//调用窗口
            })
            $('#vidarea').on('click','.removeclass',function(){
                if($(".filevid").length>1){
                    var thisVid = $(this).prev().prev().val();
                    for(var i=0;i<vidList.length;i++){
                        if(vidList[i] == thisVid){
                            vidList.splice(i,1);
                        }
                    }
                    $(this).parent('div').remove();
                }
            })



            $.fn.outerHTML = function () {

                // IE, Chrome & Safari will comply with the non-standard outerHTML, all others (FF) will have a fall-back for cloning
                return (!this.length) ? this : (this[0].outerHTML ||
                (function (el) {
                    var div = document.createElement('div');
                    div.appendChild(el.cloneNode(true));
                    var contents = div.innerHTML;
                    div = null;
                    return contents;
                })(this[0]));

            };

            if ($('#processDiagram').length == 1) {
                showActivities();
            }
            function showActivities() {
                $.getJSON(ctx + '/eam/act/process/trace/data/' + executionId, function (infos) {
                    var positionHtml = "";

                    var diagramPositon = $('#processDiagram').position();
                    var varsArray = new Array();
                    $.each(infos, function (i, v) {
                        var $positionDiv = $('<div/>', {
                            'class': 'activity-attr'
                        }).css({
                            position: 'absolute',
                            left: (v.x + 2),
                            top: (v.y + 35),
                            width: (v.width - 2),
                            height: (v.height - 2),
                            backgroundColor: 'black',
                            opacity: 0
                        });

                        // 节点边框
                        var $border = $('<div/>', {
                            'class': 'activity-attr-border'
                        }).css({
                            position: 'absolute',
                            left: (v.x + 2),
                            top: (v.y + 35),
                            width: (v.width - 4),
                            height: (v.height - 3)
                        });

                        if (v.currentActiviti) {
                            $border.css({
                                border: '3px solid red'
                            }).addClass('ui-corner-all-12');
                        }
                        positionHtml += $positionDiv.outerHTML() + $border.outerHTML();
                        varsArray[varsArray.length] = v.vars;
                    });

                    $(positionHtml).appendTo('#flowchart').find('.activity-attr-border');

                    // 鼠标移动到活动上提示
                    $('.activity-attr-border').each(function (i, v) {
                        var tipContent = "<table class='table table-bordered'>";
                        $.each(varsArray[i], function(varKey, varValue) {
                            if (varValue) {
                                tipContent += "<tr><td>" + varKey + "</td><td>" + varValue + "</td></tr>";
                            }
                        });
                        tipContent += "</table>";
                        $(this).data('vars', varsArray[i]).data('toggle', 'tooltip').data('placement', 'bottom').data('title', '活动属性').attr('title', tipContent);
                    }).tooltip();
                });
            }

            var formField = new Array();
			var formData = null;
            /**表单字段暂时写死，后期再通过接口查询*/
            //保养内容
            //var maintainContent = null;
            $.ajax({
                type : "post",
                async : false, //同步执行
                url : ctx+"/eam/maintainTask/findMaintainProject?id="+businessKey,
                dataType : "json",
                success : function(data)
                {
                    formData=data;
					var flag=1;
                    $.each(data, function(index,val){
                        formField.push(
                            {display:"保养内容编码",editable:false,name:"task_maintain_code"+flag,type:"text",group:"保养内容"+flag},
							{display:"保养内容",editable:false,name:"task_maintain_content"+flag,type:"text",group:"保养内容"+flag},
                            {display:"是否已保养",editable:true,name:"task_fk_flag"+flag,type:"select",comboboxName:"task_fk_flag"+flag+"Box",group:"保养内容"+flag,
                                options:{
                                    data: [
                                        { text: '是', id: '1' },
                                        { text: '否', id: '0' }
                                    ]
                                }
                            },
                            {display:"保养项备注",editable:false,name:"task_maintain_remark"+flag,type:"text",group:"保养内容"+flag}
						)
                        flag++;
                        //console.log(val);
                    })
                }
            });

            var task_check_standard = {display:"保养验收标准",editable:false,name:"task_check_standard",type:"text",group:"保养结果"};
            var task_check_remark = {display:"备注",editable:false,name:"task_check_remark",type:"text",group:"保养结果"};
            var task_result = {display:"反馈结果",editable:true,name:"task_result",type:"select",comboboxName:"task_resultBox",group:"保养结果",
                options:{
					data: [
						{ text: '正常', id: '0' },
						{ text: '报修', id: '1' },
                        { text: '异常', id: '2' }
					]
				}
            };
            var task_appearance = {display:"现象",editable:true,name:"task_appearance",type:"text"};
            //工单实际明细
            var detail = {display:"保养实际明细",editable:true,name:"task_fk_detail",type:"select",comboboxName:"task_fk_detailBox",
                options:{
                    data: [
                        { text: '工器具', id: '0' },
                        { text: '备件材料', id: '1' },
                        { text: '人员工时', id: '2' },
                        { text: '其他费用', id: '3' }
                    ]
                }
            };

            formField.push(task_check_standard);
            formField.push(task_check_remark);
            formField.push(task_result);
            formField.push(task_appearance);
            formField.push(detail);







            /*formField.push(result);
            formField.push(reason);
            formField.push(detail);*/



            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields: formField
            };

            //console.log(formField)
            $("#serviceForm").ligerForm(formConfig);

            //编辑页面字段赋值
            $.ajax({
                type : "post",
                async : false, //同步执行
                url : ctx+"/eam/maintainTask/getFeedBackDatas?id="+businessKey,
                dataType : "json", //传递数据形式为text
                success : function(data)
                {
                    console.log(data)
                    var editForm  = liger.get("serviceForm");
                    editForm.setData(data);

                    var flag=1;
                    debugger
                    $.each(formData, function(index,val){
                        var maintain_code = "task_maintain_code" + flag;
                        var maintain_content = "task_maintain_content" + flag;
                        var dt = {};
                        dt[maintain_code] = val.maintain_code;
                        dt[maintain_content] = val.maintain_content;
                        editForm.setData(dt);
                        flag++;
                    })
                }
            });


            //值类型下拉切换
            $("#task_fk_detailBox").on('change',function () {

                //alert($("#subject_valuetypeBox").val());
                if ($("#toolStdTable").children().length <1){
                    $(f_initGrid1);
                    $(f_initGrid2);
                    $(f_initGrid3);
                    $(f_initGrid4);
                }

                if($("#task_fk_detailBox").val() =="0"){
                    $("#toolStdTable").show().siblings().hide();
                }else if($("#task_fk_detailBox").val() =="1"){
                    $("#attachmentStdTable").show().siblings().hide();
                }else if($("#task_fk_detailBox").val() =="2"){
                    $("#manhaurStdTable").show().siblings().hide();
                }else{
                    $("#otherStdTable").show().siblings().hide();
				}

            });



            //给表单赋值
            //var data1,data2,data3,data4,data5,data6;
            /*$.ajax({
			 type:'POST',
			 url:ctx+"/opestandard/standardLibrary/getLibByPIid",
			 async:false,
			 dataType:'json',
			 data:{pIid:$("#pIid").val()},
			 success:function(data){
			 var libid=data.id;
                 common.callAjax('POST',true,ctx+"/opestandard/standardLibrary/editObj",'json',{id:libid},function(data){
                     var editForm  = liger.get("serviceForm");
                     editForm.setData(data);
                 })
			 if(libid !=null && libid !=""){
			 $('#library_code').val(data.library_code);
			 $('#library_name').val(data.library_name);
			 //给检修列表赋值
			 common.callAjax('POST',false,ctx+"/opestandard/standardLibrary/quFault",'json',{id:libid},function(data){
			 data1={Rows:data};
			 })
			 common.callAjax('POST',false,ctx+"/opestandard/standardLibrary/quMaintain",'json',{id:libid},function(data){
			 data2={Rows:data};
			 })
			 common.callAjax('POST',false,ctx+"/opestandard/standardLibrary/quPatrol",'json',{id:libid},function(data){
			 data3={Rows:data};
			 })
			 common.callAjax('POST',false,ctx+"/opestandard/standardLibrary/quFailure",'json',{id:libid},function(data){
			 data4={Rows:data};
			 })
			 common.callAjax('POST',false,ctx+"/opestandard/standardLibrary/quOpe",'json',{id:libid},function(data){
			 data5={Rows:data};
			 })
			 common.callAjax('POST',false,ctx+"/opestandard/standardLibrary/quSafe",'json',{id:libid},function(data){
			 data6={Rows:data};
			 })

			 }
			 }
			 })*/

            var data1 = {
                Rows: [
                    {
                        "task_id":businessKey,
                        "tool_id":null,
                        "tool_num":null,
                        "tool_model":null,
                        "tool_remark":null
                    }
                ]
            };

            //$(f_initGrid1);
            //工器具
            var manager1, g1;
            function f_initGrid1()
            {
                g1 =  manager1 = $("#toolStdTable").ligerGrid({
                    columns: [
                        { display: '工器具', name: 'tool_id',
                            editor: {
                                type: 'selectGrid',
                                data:[{id:0,name:"螺丝"},{id:1,name:"剪刀"},{id:2,name:"夹子"},{id:3,name:"钻头"}],
                                valueField: 'id',
                                textField: 'name',
                                onSelected: function (value)
                                {
                                    //alert(value)
                                }

                            },
                            render: function (item) {

                                var text = "";
                                $.each([{id:0,name:"螺丝"},{id:1,name:"剪刀"},{id:2,name:"夹子"},{id:3,name:"钻头"}], function (i, data) {
                                    if (data.id == item.tool_id) {
                                        text = data.name;
                                    }
                                })
                                return text;
                            }
                        },
                        { display: '数量', name: 'tool_num',
                            editor: { type: 'text' }
                        },
                        { display: '规格型号', name: 'tool_model',
                            editor: { type: 'text' }
                        },
                        { display: '备注', name: 'tool_remark',
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
                    data:data1,
                    width: '88%'
                });
            }
            $('#toolStdTable').on('click','.dele',function(){
                if (manager1.getData().length ==1){
                    alert("只剩一项时无法删除");
                }else {
                    manager1.deleteRow($(this).data("id"));
                }
            });
            $('#toolStdTable').on('click','.add',function(){
                //表格参数key必须填，哪怕value是null，否则getData()取不到值

                manager1.addRow({
                    "task_id":businessKey,
                    "tool_id":null,
                    "tool_num":null,
                    "tool_model":null,
                    "tool_remark":null
                });
            });



            //备品备件
            var data2 = {
                Rows: [
                    {
                        "task_id":businessKey,
                        "attachment_id":null,
                        "attachment_num":null,
                        "attachment_remark":null
                    }
                ]
            };
            var manager2, g2;
            function f_initGrid2()
            {
                g2 =  manager2 = $("#attachmentStdTable").ligerGrid({
                    columns: [
                        { display: '备品备件', name: 'attachment_id',
                            editor: {
                                type: 'selectGrid',
                                data:[{id:0,name:"备件A"},{id:1,name:"备件B"},{id:2,name:"备件C"},{id:3,name:"备件D"}],
                                valueField: 'id',
                                textField: 'name',
                                onSelected: function (value)
                                {
                                    //alert(value)
                                }

                            },
                            render: function (item) {

                                var text = "";
                                $.each([{id:0,name:"备件A"},{id:1,name:"备件B"},{id:2,name:"备件C"},{id:3,name:"备件D"}], function (i, data) {
                                    if (data.id == item.part_id) {
                                        text = data.name;
                                    }
                                })
                                return text;
                            }
                        },
                        { display: '数量', name: 'attachment_num',
                            editor: { type: 'text' }
                        },

                        { display: '备注', name: 'attachment_remark',
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
            $('#attachmentStdTable').on('click','.dele',function(){
                if (manager2.getData().length ==1){
                    alert("只剩一项时无法删除");
                }else {
                    manager2.deleteRow($(this).data("id"));
                }
            });
            $('#attachmentStdTable').on('click','.add',function(){
                //表格参数key必须填，哪怕value是null，否则getData()取不到值

                manager2.addRow({
                    "task_id":businessKey,
                    "attachment_id":null,
                    "attachment_num":null,
                    "attachment_remark":null
                });
            });

            //人员工时
            var data3 = {
                Rows: [
                    {
                        "task_id":businessKey,
                        "emp_id":null,
                        "emp_hour":null,
                        "emp_charge":null,
                        "emp_remark":null
                    }
                ]
            };

            var empdata = null;
            $.ajax({
                type : "post",
                async : false, //同步执行
                url : ctx+"/sys/eamuser/getUsersSelect",
                dataType : "json", //传递数据形式为text
                success : function(data)
                {
                    //console.log(data)
                    empdata=data;
                }
            });

            var manager3, g3;
            function f_initGrid3()
            {
                g3 =  manager3 = $("#manhaurStdTable").ligerGrid({
                    columns: [
                        /*{ display: '测量通道', name: 'subject_way',
                            editor: {
                                type: 'combobox',
                                comboboxName: "subject_wayBox",
                                //data:[{id:0,name:"超表类"},{id:1,name:"温度"},{id:2,name:"震动"},{id:3,name:"漏气"},{id:4,name:"水位"},{id:5,name:"停机类"}],
                                valueField: 'id',
                                textField: 'name',
                                tree: {
                                    url: ctx + "/material/materialType/materialTypeTree",
                                    checkbox: false,
                                    idFieldName: 'id',
                                    parentIDFieldName: 'pid',
                                    parentIcon: null,
                                    childIcon: null,
                                    ajaxType: 'post',
                                    onClick: function (note) {
                                        console.log(note.data.text);
                                        if ($("#maingrid").children().length <1){
                                            $(f_initGrid);
                                        }
                                        if(note.data.text =="设备A"){
                                            $("#maingrid").hide();
                                            $("#maingrid1").show();
                                        }else {
                                            $("#maingrid").show();
                                            $("#maingrid1").hide();
                                        }
                                    }
                                },
                                onSelected: function (value)
                                {
                                    alert(1)
                                }

                            },
                            render: function (item) {

                                var text = "";
                                $.each([{id:0,name:"超表类"},{id:1,name:"温度"},{id:2,name:"震动"},{id:3,name:"漏气"},{id:4,name:"水位"},{id:5,name:"停机类"}], function (i, data) {
                                    if (data.id == item.id) {
                                        text = data.name;
                                    }
                                })
                                return text;
                            }
                        },*/


                        { display: '人员', name: 'emp_id',
                            editor: {
                                type: 'selectGrid',
                                data:empdata,
                                valueField: 'id',
                                textField: 'name',
                                onSelected: function (value)
                                {
                                    //alert(value)
                                }

                            },
                            render: function (item) {

                                var text = "";
                                $.each(empdata, function (i, data) {
                                    if (data.id == item.emp_id) {
                                        text = data.name;
                                    }
                                })
                                return text;
                            }
                        },
                        { display: '实际工时', name: 'emp_hour',
                            editor: { type: 'text' }
                        },
                        { display: '工时单价', name: 'emp_charge',
                            editor: { type: 'text' }
                        },
                        { display: '岗位技能', name: 'emp_skill',
                            editor: { type: 'text' }
                        },
                        { display: '备注', name: 'emp_remark',
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
            $('#manhaurStdTable').on('click','.dele',function(){
                if (manager3.getData().length ==1){
                    alert("只剩一项时无法删除");
                }else {
                    manager3.deleteRow($(this).data("id"));
                }
            });
            $('#manhaurStdTable').on('click','.add',function(){
                //表格参数key必须填，哪怕value是null，否则getData()取不到值

                manager3.addRow({
                    "task_id":businessKey,
                    "emp_id":null,
                    "emp_hour":null,
                    "emp_charge":null,
                    "emp_remark":null
                });
            });


            //其他费用
            var data4 = {
                Rows: [
                    {
                        "task_id":businessKey,
                        "charge_name":null,
                        "charge_price":null,
                        "charge_remark":null
                    }
                ]
            };
            var manager4, g4;
            function f_initGrid4()
            {
                g4 =  manager4 = $("#otherStdTable").ligerGrid({
                    columns: [
                        { display: '其他费用事项', name: 'charge_name',
                            editor: { type: 'text' }
                        },

                        { display: '金额', name: 'charge_price',
                            editor: { type: 'text' }
                        },
                        {
                            display: '备注', name: 'charge_remark',
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
            $('#otherStdTable').on('click','.dele',function(){
                if (manager4.getData().length ==1){
                    alert("只剩一项时无法删除");
                }else {
                    manager4.deleteRow($(this).data("id"));
                }
            });
            $('#otherStdTable').on('click','.add',function(){
                //表格参数key必须填，哪怕value是null，否则getData()取不到值

                manager4.addRow({
                    "task_id":businessKey,
                    "charge_name":null,
                    "charge_price":null,
                    "charge_remark":null
                });
            });


            function showData(data) {
                //debugger;
                var dom = '';
                $.each(data,function(index,e){
                    dom += '<tr class="blue"><td>'+ (index+1) +'</td><td>' + e.node +
                        '</td><td>操作者总计:' + e.operatorNum +
                        '</td><td>已提交:' + e.submit +
                        '</td><td></td><td></td></tr>' +
                        '<tr class="operator"><td></td><td>操作人</td><td>操作状态</td><td>接收时间</td><td>操作时间</td><td>操作损耗</td></tr>';
                    $.each(e.detail,function(index,i){
                        var a = "<tr><td></td><td>" + i.operator +
                            "</td><td>" + i.status +
                            "</td><td>" + i.accepttime +
                            "</td><td>" + i.submittime +
                            "</td><td>" + i.duration + "</td></tr>";
                        dom += a;
                    })
                })
                $('table').append(dom);
            }

        });



	</script>
</head>
<body>
<div>
	<ul class="nav nav-tabs">
		<li role="presentation" class="active"><a href="#taskContent" role="tab" data-toggle="tab" aria-controls="home" aria-expanded="true">任务详情</a></li>
		<li role="presentation" ><a href="#flowchart" role="tab" data-toggle="tab" aria-controls="profile" aria-expanded="false">流程图</a></li>
		<li role="presentation" ><a href="#flowstatus" role="tab" data-toggle="tab" aria-controls="profile" aria-expanded="false">流程状态</a></li>
	</ul>
	<div class="tab-content">
		<div id="taskContent" role="tabpanel" class="tab-pane active">
			<form  class="form-horizontal" action="" method="post" id="serviceForm">


			</form>
			<div class="editDiv">
				<div class="l-clear"></div>
				<div class="subeditDiv" id="toolStdTable" ></div>
				<div class="subeditDiv" id="attachmentStdTable" ></div>
				<div class="subeditDiv" id="manhaurStdTable" ></div>
				<div	 class="subeditDiv" id="otherStdTable" ></div>
			</div>
			<%--附件区域-图片--%>
			<div class="path">
				<div id="imgarea">
					<div class="fileimg"><label>上传图片</label><br><input type="text" name="mytext[]" id="img" readonly/><a href="#" class="uploadclass">上传</a>&nbsp;<a href="#" id="uploadmoreimg">新增</a></div>
				</div>
			</div>

			<%--附件区域-视频--%>
			<div class="path">
				<div id="vidarea">
					<div class="filevid"><label>上传视频</label><br><input type="text" name="mytext[]" id="vid" readonly/><a href="#" class="uploadclass">上传</a>&nbsp;<a href="#" id="uploadmorevid">新增</a></div>
				</div>
			</div>


			<div class="control-group">
				<div class="controls form-actions">
					<button type="" class="btn btn-primary" id="btnFeedBack"><i class="icon-ok"></i>完成任务</button>
				</div>
			</div>



		</div>
		<%--流程图tab页--%>
		<div id="flowchart" role="tabpanel" class="tab-pane">
			<img id="processDiagram" src="${ctx }/eam/act/process/read-resource?pdid=${processDefinition.id}&resourceName=${processDefinition.diagramResourceName}" />
		</div>
		<%--流程详情tab页--%>
		<div id="flowstatus" role="tabpanel" class="tab-pane">
			<%--<div class="menu">
				<div><li>5</li>总人次</div>
				<div><li>4</li>已提交</div>
				<div><li>3</li>未提交</div>
				<div><li>2</li>已查看</div>
				<div><li>1</li>未查看</div>
			</div>--%>
			<div>
				<table>
					<tr>
						<th>序号</th>
						<th>节点</th>
						<th>操作情况统计</th>
						<th></th>
						<th></th>
						<th></th>
					</tr>

				</table>
			</div>
		</div>
	</div>
</div>
</body>
</html>
