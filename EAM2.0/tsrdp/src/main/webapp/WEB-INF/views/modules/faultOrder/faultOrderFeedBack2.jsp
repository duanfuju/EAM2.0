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
	</style>
	<meta name="decorator" content="default"/>
	<script type="text/javascript" src="/js/bootstrap.min.js"></script>

	<script type="text/javascript">
        var executionId = '${task.executionId}';
        var pstid = '${pstid}';
        pstid = '171024160549229SfM20';
        var bsligerform;
        var empdata = null;
        var manager1,manager2,manager3,manager4;
        //所有的巡检项(内部包含对应的巡检区域)
        var allRouteArea = new Array();
        $(function () {
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

            showInspXhDetail();
            function showInspXhDetail() {
                //工单实际明细
                var detail = {display:"工单计划明细",editable:true,name:"order_detail",type:"select",comboboxName:"order_detailBox",group: "",
                    options:{
                        data: [
                            { text: '工器具', id: '0' },
                            { text: '备件材料', id: '1' },
                            { text: '人员工时', id: '2' },
                            { text: '其他费用', id: '3' }
                        ]
                    }
                };

                var formField = new Array();
                formField.push(detail);
                var formConfig = {
                    space : 50, labelWidth : 120 , inputWidth : 200,
                    validate: true,
                    fields: formField
                };
				$("#serviceForm").ligerForm(formConfig);
            }

            var routeArea = [
                    {
                        code : 111,
                        instp : [
                            {
                                no:1,
                                device:'久旱逢甘霖',
                                device_loc:'Beijing',
                                instp_name:'A,BC,D,E,F'
                            },
                            {
                                no:2,
                                device:'他乡遇故知',
                                device_loc:'Beijing',
                                instp_name:'A,BC,D,E,F'
                            },
                            {
                                no:3,
                                device:'洞房花烛夜',
                                device_loc:'Beijing',
                                instp_name:'A,BC,D,E,F'
                            }
                        ]
                    },
                    {
                        code : 222,
                        instp : [
                            {
                                no:1,
                                device:'金榜提名时',
                                device_loc:'Beijing',
                                instp_name:'A,BC,D,E,F'
                            }
                        ]
                    },
                    {
                        code : 333,
                        instp : [
                            {
                                no:1,
                                device:'锄禾日当午',
                                device_loc:'Beijing',
                                instp_name:'A,BC,D,E,F'
                            }
                        ]
                    },
                ];

            common.callAjax('post',false,ctx + "/eam/inspectionTask/getSubjectInfosByPstid?pstid="+pstid,"json",null,function(data){
                debugger;
                if(data.length>0){
                    $.each(data, function (i, elem) {
                        var areaInfo = new Object();
                        areaInfo.area_code = elem.area_code;
                        areaInfo.area_name = elem.area_name;
                        var devList = new Array();
                        if(elem.devList !=undefined && elem.devList.length>0){
                            $.each(elem.devList, function (i, elem2) {
                                var inspInfo = new Object();
                                inspInfo.no = parseInt(i+1);
                                inspInfo.dev_code = elem2.dev_code;
                                inspInfo.device = elem2.dev_code;
                                inspInfo.device_loc = elem2.loc_name;
                                inspInfo.instp_name = elem2.subjectNames;
                                inspInfo.subjects = elem2.subjects;
                                devList.push(inspInfo);
                            });
						}

                        areaInfo.devList = devList;
                        allRouteArea.push(areaInfo);
                    });
				}




            });


            //巡检区域，巡检项的数组
//			var routeInsArray = 1;

            $.each(allRouteArea, function (i, item) {
                var tmpTableDiv = 'tdiv' + item.area_code;
                var tmpPanelDiv = 'pdiv' + item.area_code;
                var tmpFormDiv = 'fdiv' + item.area_code;
                var mainDivHtml = $("#mainDiv").html() + "<div id='"+tmpPanelDiv+"' style='margin:10px'><div id='"+tmpTableDiv+"' style='height: auto'></div></div>";
                $("#mainDiv").html(mainDivHtml);

                initTablePanel(item);

                function initTablePanel(item) {
					var insp_area_code = item.area_code;
                    var routeTable =$("#" + tmpTableDiv + "").ligerGrid({
                        columns: [
                            { display: '序号', name: 'no',
                                editor: { type: 'text' }
                            },
                            { display: '设备', name: 'device',
                                editor: { type: 'text' }
                            },
                            { display: '设备位置', name: 'device_loc',
                                editor: { type: 'text' }
                            },
                            { display: '巡检项', name: 'instp_name',
                                editor: { type: 'text' }
                            },
                            {
                                display: '操作', isSort: false,
                                render: function (rowdata, rowindex, value) {
                                    var h = "";
                                    h += "<a class='feedback' data='"+JSON.stringify(rowdata) +"'+ area_code='"+insp_area_code+"' instp='" + rowdata.instp_name + "' subdiv='"+tmpFormDiv+"'>反馈</a> ";
//                                    h += "<a  class='del' data='" + rowdata + "'>作废</a> ";
                                    return h;
                                }
                            }

                        ],

                        enabledEdit: false, isScroll: true, checkbox:false,rownumbers:true,
                        data:{
                            Rows: item.devList
                        },
                        width: '88%'
                    });

                    var ligerPanel = $("#" + tmpPanelDiv + "").ligerPanel({
                        title: '巡检区域编码 '+item.area_code,
                        width: '86%',
                        content : $("#" + tmpTableDiv + "").html()+ "<div id='"+tmpFormDiv+"' style='300px'></div>"

                    });
                    //默认全部收缩
                    ligerPanel.collapse();
                    $(".l-panel-content").css("height","auto");

                    //重写ligerui收缩方法，否则只会收缩最后一个
                    $(".l-panel-header").click(function(){
                        var clickPanelId = $(this).parent().attr("id");
                        var displ = $("#" + clickPanelId + " .l-panel-content").css('display');
                        if('none' == displ){
                            $("#" + clickPanelId + " .l-panel-content").css('display','block');
                        }else if('block' == displ ){
                            $("#" + clickPanelId + " .l-panel-content").css('display','none');
                        }
//                    liger.get(clickPanelId).toggle();
                    });


                    $("#mainDiv  .feedback").click(function () {
                        //查询巡检项信息
                        var dev_subjects = eval('(' + $(this).attr("data") + ')');
						// 巡检路线编码
						var area_code = $(this).attr("area_code");
						var formDiv = $(this).attr("subdiv");
                        if(dev_subjects.subjects != undefined && dev_subjects.subjects.length > 0){
                            initFormPanel(formDiv,area_code,dev_subjects.subjects);
						}


                    });

                    function initFormPanel(formDiv,area_code,dev_subjects) {
                        var formFields = new Array();
                        $.each(dev_subjects, function (i, item) {
							var dyn_key = area_code+"-"+item.id+"-";
                            var group = {group:"巡检项"+item.subject_content};
                            var insp_code = {display:"巡检项编码", name:dyn_key+"insp_code",type:"labelText"};
                            var insp_name = {display:"巡检项", name:dyn_key+"insp_name",type:"labelText"};
                            var check_value = {display:"检查值", name:dyn_key+"check_value",type:"text"};
                            var unit = {display:"单位", name:dyn_key+"unit",type:"labelText"};
                            var refer_value = {display:"参考值", name:dyn_key+"refer_value",type:"labelText"};
                            var check_time = {display:"测量时间", name:dyn_key+"check_time",type:"labelText"};
                            var feed_result = {display:"反馈结果",editable:true,name:dyn_key+"feed_result",type:"select",comboboxName:dyn_key+"feed_resultBox",
                                options:{
                                    data: [
                                        { text: '正常', value: '1' },
                                        { text: '报修', value: '2' },
                                        { text: '异常', value: '3' }
                                    ]
                                }
                            };
                            var feed_reason = {display:"原因", name:dyn_key+"feed_reason",type:"text"};
                            /*var feed_reason = {display:"原因",editable:true,name:dyn_key+"feed_reason",type:"select",comboboxName:dyn_key+"feed_reasonBox",
                                options:{
                                    data: [
                                        { text: '正常', value: '1' },
                                        { text: '报修', value: '2' },
                                        { text: '异常', value: '3' }
                                    ]
                                }
                            };*/
                            var feed_remark = {display:"备注", name:dyn_key+"feed_remark",type:"text"};
//                            <img id="logo" class="logo" src="" style="width:320px;margin: 5px">
                            var feed_pic = {display:"上传图片", name:dyn_key+"feed_pic",type:"text"};
                            var feed_video = {display:"上传视频", name:dyn_key+"feed_video",type:"text"};

                            formFields.push(group);
                            formFields.push(insp_code);
                            formFields.push(insp_name);
                            formFields.push(check_value);
                            formFields.push(unit);
                            formFields.push(refer_value);
                            formFields.push(check_time);
                            formFields.push(feed_result);
                            formFields.push(feed_reason);
                            formFields.push(feed_remark);
                            formFields.push(feed_pic);
                            formFields.push(feed_video);

                        });

                        var formConfig = {
                            space : 50, labelWidth : 120 , inputWidth : 200,
                            validate: true,
                            fields: formFields,
                        };
                        bsligerform = $("#" + formDiv + "").ligerForm(formConfig);

                        //渲染出整个表单后，再对字段做event渲染
                        $.each(dev_subjects, function (i, item) {
                            var dyn_key = area_code+"-"+item.id+"-";
                            // 选择多个设备的弹出框
                            var picname = dyn_key+'feed_pic';
                            $("input[name='"+picname+"']").on("click",function () {
                                new BrowseServer($(this));
                            });

                            var videoname = dyn_key+'feed_video';
                            $("input[name='"+videoname+"']").on("click",function () {
                                new BrowseServer($(this));
                            });

                            $("#"+dyn_key+"feed_resultBox").on('change',function () {
                                var resultValue = $("#"+dyn_key+"feed_resultBox").val();
                                alert(resultValue);
                            });

                            //赋初始值
                            bsligerform.getEditor(dyn_key+'insp_code').setValue(item.id);
                            bsligerform.getEditor(dyn_key+'insp_name').setValue(item.subject_content);
                            bsligerform.getEditor(dyn_key+'unit').setValue(item.subject_unit);
                            //参考值分字符型和数值型，数值型取subject_ck_value，字符型取subject_value1,2,3
                            var ckz  = '';
                            if(item.hasOwnProperty('subject_sx_value')){
                                ckz = item.subject_sx_value;
                                if(item.hasOwnProperty('subject_xx_value')){
                                    ckz = ckz+'-'+item.subject_xx_value;
								}
							}else if(item.hasOwnProperty('subject_value1')){
                                ckz = item.subject_value1 + ','+item.subject_value2 + ','+item.subject_value3;
							}
                            bsligerform.getEditor(dyn_key+'refer_value').setValue(ckz);
                            bsligerform.getEditor(dyn_key+'check_time').setValue(DateUtil.dateToStr('yyyy-MM-dd hh:mm:ss',new Date()));

                            <%--$("#order_levelBox").val('${faultOrder.order_level}').trigger('change.select2');--%>
                            <%--$('input[name="notifier_dept"]').val('${faultOrder.notifier_dept.deptno}');--%>
                            <%--$('input[name="notifier_loc"]').val('${faultOrder.notifier_loc.id}');--%>
                            <%--$('input[name="order_device"]').val('${faultOrder.order_device.id}');--%>
                        });




                        //初始化ckeditor
                        function BrowseServer(pathElement) {
                            var finder = new CKFinder();
                            finder.selectMultiple = true;//可以多选
//                            finder.SelectFunction = SetFileField;//选中之后回调函数
//                            finder.SelectFunctionData = pathElement.attr("id");//传入的控件值
							
                            //文件选中时执行
                            finder.selectActionFunction = function(fileUrl){
                                pathElement.val(fileUrl);
//                                picElement.attr("src",""+fileUrl+"");
                            }
                            finder.popup();//调用窗口
                        }




                    }
                }

            });


            initFormEvent();



        });

        function initFormEvent() {
            //表单提交
            $("#btnSubmit").on('click', function() {
                var form = liger.get('inputForm');
                debugger;
                //复杂表单用ajax提交


				var routeAreas = allRouteArea;
				//遍历巡检区域
				var validate_res = true;
				var validate_reason='';
                $.each(routeAreas, function (i, area) {
					var devList = area.devList;
                    if( devList!= undefined && devList.length >0){
                        //遍历设备
                        $.each(devList, function (i, devs) {
							var deviceSubjects = devs.subjects;
							if(deviceSubjects!= undefined && deviceSubjects.length >0){
                                //遍历设备下巡检项反馈信息
                                $.each(deviceSubjects, function (i, subject) {
                                    var dyn_key = subject.area_code+"-"+subject.id+"-";
                                    subject.check_value = $("input[name='"+dyn_key+"check_value']").val();
                                    subject.feed_result = $("#" + dyn_key + "feed_resultBox").val();
                                    if(subject.feed_result == undefined){
                                        validate_res = false;
                                        validate_reason = '巡检区域['+subject.area_code+']下巡检项['+subject.subject_content+']未反馈，请检查';
									}

                                    subject.feed_reason = $("input[name='"+dyn_key+"feed_reason']").val();
                                    subject.feed_remark = $("input[name='"+dyn_key+"feed_remark']").val();
                                    subject.feed_pic = $("input[name='"+dyn_key+"feed_pic']").val();
                                    subject.feed_video = $("input[name='"+dyn_key+"feed_video']").val();
                                });
							}
                        });
					}



                });

				if(!validate_res){
				    layer.msg(validate_reason,{time: 1000,icon:7});
				    return;
				}




                var cost=new Object();//对象存储object
				if(manager1 != undefined){
                    cost.tool_data = manager1.getData();
                    cost.attachment_data = manager2.getData();
                    cost.manhaur_data = manager3.getData();
                    cost.other_data = manager4.getData();
				}



                var submitData = new Object();
                submitData.costInfo = cost;
                submitData.routeAreas = routeAreas;

                console.log("表单数据:");
                console.log(JSON.stringify(submitData));
				layer.msg(JSON.stringify(submitData));
            });


            //值类型下拉切换
            $("#order_detailBox").on('change',function () {
                empdata = FormUtil.initSelectBox(ctx+"/sys/eamuser/getUsersSelect","order_receiver",$("#order_receiverBox"),null,null,"id","name");
                //alert($("#subject_valuetypeBox").val());
                if ($("#toolStdTable").children().length <1){
                    $(f_initGrid1);
                    $(f_initGrid2);
                    $(f_initGrid3);
                    $(f_initGrid4);
                }

                if($("#order_detailBox").val() =="0"){
                    $("#toolStdTable").show().siblings().hide();
                }else if($("#order_detailBox").val() =="1"){
                    $("#attachmentStdTable").show().siblings().hide();
                }else if($("#order_detailBox").val() =="2"){
                    $("#manhaurStdTable").show().siblings().hide();
                }else if($("#order_detailBox").val() =="3"){
                    $("#otherStdTable").show().siblings().hide();
                }else{
                    $("#toolStdTable").hide();
                    $("#attachmentStdTable").hide();
                    $("#manhaurStdTable").hide();
                    $("#otherStdTable").hide();
				}
            });

            //工器具
            function f_initGrid1(){
                manager1 = $("#toolStdTable").ligerGrid({
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
                    onSelectRow: function (rowdata, rowindex)
                    {
                    },
                    enabledEdit: true, isScroll: false, checkbox:true,rownumbers:true,
                    data:{
                        Rows: [
                            {
                                "tool_id":null,
                                "tool_num":null,
                                "tool_model":null,
                                "tool_remark":null
                            }
                        ]
                    },
                    width: '88%'
                });

                $('#toolStdTable').on('click', '.add', function () {
                    //表格参数key必须填，哪怕value是null，否则getData()取不到值
                    manager1.addRow({});
                });
                $('#toolStdTable').on('click','.del',function(){
                    if (manager1.getData().length == 1) {
                        layer.msg("首行不能删除",{time: 1000,icon:7});
                    } else {
                        manager1.deleteRow($(this).data("id"));
                    }
                })

            }

            //备品备件
            function f_initGrid2(){
                manager2 = $("#attachmentStdTable").ligerGrid({
                    columns: [
                        { display: '备品备件', name: 'part_id',
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
                        { display: '数量', name: 'part_num',
                            editor: { type: 'text' }
                        },

                        { display: '备注', name: 'part_remark',
                            editor: { type: 'text' }
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
                    onSelectRow: function (rowdata, rowindex)
                    {
                    },
                    enabledEdit: true, isScroll: false, checkbox:true,rownumbers:true,
                    data:{
                        Rows: [
                            {
                                "part_id":null,
                                "part_num":null,
                                "part_remark":null
                            }
                        ]
                    },
                    width: '88%'
                });

                $('#attachmentStdTable').on('click', '.add', function () {
                    manager2.addRow({});
                });
                $('#attachmentStdTable').on('click','.del',function(){
                    if (manager2.getData().length == 1) {
                        layer.msg("首行不能删除",{time: 1000,icon:7});
                    } else {
                        manager2.deleteRow($(this).data("id"));
                    }
                })
            }

            //人员工时
            function f_initGrid3(){
                manager3 = $("#manhaurStdTable").ligerGrid({
                    columns: [
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
                        { display: '实际工时', name: 'hour',
                            editor: { type: 'text' }
                        },
                        { display: '工时单价', name: 'charge',
                            editor: { type: 'text' }
                        },
                        { display: '备注', name: 'remark',
                            editor: { type: 'text' }
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
                    onSelectRow: function (rowdata, rowindex)
                    {
                    },
                    enabledEdit: true, isScroll: false, checkbox:true,rownumbers:true,
                    data:{
                        Rows: [
                            {
                                "emp_id":null,
                                "hour":null,
                                "charge":null,
                                "remark":null
                            }
                        ]
                    },
                    width: '88%'
                });
                $('#manhaurStdTable').on('click', '.add', function () {
                    //表格参数key必须填，哪怕value是null，否则getData()取不到值
                    manager3.addRow({});
                });
                $('#manhaurStdTable').on('click','.del',function(){
                    if (manager3.getData().length == 1) {
                        layer.msg("首行不能删除",{time: 1000,icon:7});
                    } else {
                        manager3.deleteRow($(this).data("id"));
                    }
                })
            }

            //其他费用
            function f_initGrid4(){
                manager4 = $("#otherStdTable").ligerGrid({
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
                    onSelectRow: function (rowdata, rowindex)
                    {
                    },
                    enabledEdit: true, isScroll: false, checkbox:true,rownumbers:true,
                    data:{
                        Rows: [
                            {
                                "charge_name":null,
                                "charge_price":null,
                                "charge_remark":null
                            }
                        ]
                    },
                    width: '88%'
                });
                $('#otherStdTable').on('click', '.add', function () {
                    //表格参数key必须填，哪怕value是null，否则getData()取不到值
                    manager4.addRow({});
                });
                $('#otherStdTable').on('click','.del',function(){
                    if (manager4.getData().length == 1) {
                        layer.msg("首行不能删除",{time: 1000,icon:7});
                    } else {
                        manager4.deleteRow($(this).data("id"));
                    }
                })
            }
        }


	</script>
</head>
<body>
<div>
	<ul class="nav nav-tabs">
		<li role="presentation" class="active"><a href="#taskContent" role="tab" data-toggle="tab" aria-controls="home" aria-expanded="true">任务详情</a></li>
		<li role="presentation" ><a href="#flowchart" role="tab" data-toggle="tab" aria-controls="profile" aria-expanded="false">流程图</a></li>
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
				<div class="subeditDiv" id="otherStdTable" ></div>
			</div>
			<div id="mainDiv" style="margin:20px"></div>

			<%-- 按钮区域 --%>
			<div class="control-group">
				<div class="controls form-actions">
					<button type="" class="btn btn-primary" id="btnSubmit"><i class="icon-ok"></i>完成任务</button>
				</div>
			</div>

		</div>
		<div id="flowchart" role="tabpanel" class="tab-pane">
			<img id="processDiagram" src="${ctx }/eam/act/process/read-resource?pdid=${processDefinition.id}&resourceName=${processDefinition.diagramResourceName}" />
		</div>

	</div>
</div>
</body>
</html>
