<%@ page import="org.activiti.engine.form.FormType" %>
<%@ page import="org.activiti.engine.form.FormProperty" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<html>
<head>
    <title>巡检任务反馈</title>
    <style type="text/css">
        #pinfo th {background: #f7f7f9;}
    </style>
    <meta name="decorator" content="default"/>
    <script type="text/javascript" src="/js/bootstrap.min.js"></script>

    <script type="text/javascript">
        var executionId = '${task.executionId}';
        var pstid = '${task.executionId}';
//        pstid = '171024160549229SfM20';
        var bsligerform;
        var manager1,manager2,manager3,manager4;
        var inspectiontask_id = null;   //巡检任务id

        // 初始化工器具和备品备件的下拉框列表数据
        var materialSelect = null;
        // 人员下拉数据
        var personSelect = null;
        //所有的巡检项(内部包含对应的巡检区域)
        var allRouteArea = new Array();
        // 任务id
        var taskid = '${task.id}';

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

            common.callAjax('post',false,common.interfaceUrl.toolsInfoData,"json",null,function(data){
                materialSelect = data;
            });
            common.callAjax('post',false,common.interfaceUrl.userExtGetAllUser,"json",null,function(data){
                personSelect = data;
            });

            common.callAjax('post',false,ctx + "/eam/inspectionTask/getSubjectInfosByPstid","json",{"pstid":pstid},function(data){
                debugger;
                inspectiontask_id = data[0].inspectiontask_id;
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
                            var group = {group:"巡检项"+item.id+item.subject_content};
                            var id = {display:"巡检项编码", name:dyn_key+"id",type:"labelText"};
                            var subject_name = {display:"巡检项", name:dyn_key+"subject_name",type:"labelText"};
                            var check_value = {display:"检查值", name:dyn_key+"check_value",type:"text"};
                            var subject_unit = {display:"单位", name:dyn_key+"subject_unit",type:"labelText"};
                            var refer_value = {display:"参考值", name:dyn_key+"refer_value",type:"labelText"};
                            var check_time = {display:"测量时间", name:dyn_key+"check_time",type:"labelText"};
                            var check_result = {display:"反馈结果",editable:true,name:dyn_key+"check_result",type:"select",comboboxName:dyn_key+"check_resultBox",
                                options:{
                                    data: [
                                        { text: '正常', id: '0' },
                                        { text: '报修', id: '1' },
                                        { text: '异常', id: '2' }
                                    ]
                                }
                            };
                            var appearance = {display:"现象", name:dyn_key+"appearance",type:"text"};
                            var remark = {display:"备注", name:dyn_key+"remark",type:"text"};
                            var check_picture = {display:"上传图片", name:dyn_key+"check_picture",type:"text"};
                            var check_video = {display:"上传视频", name:dyn_key+"check_video",type:"text"};

                            formFields.push(group);
                            formFields.push(id);
                            formFields.push(subject_name);
                            formFields.push(check_value);
                            formFields.push(subject_unit);
                            formFields.push(refer_value);
                            formFields.push(check_time);
                            formFields.push(check_result);
                            formFields.push(appearance);
                            formFields.push(remark);
                            formFields.push(check_picture);
                            formFields.push(check_video);

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
                            var picname = dyn_key+'check_picture';
                            $("input[name='"+picname+"']").on("click",function () {
                                new BrowseServer($(this));
                            });

                            var videoname = dyn_key+'check_video';
                            $("input[name='"+videoname+"']").on("click",function () {
                                new BrowseServer($(this));
                            });

                            $("#"+dyn_key+"feed_resultBox").on('change',function () {
                                var resultValue = $("#"+dyn_key+"check_resultBox").val();
                                alert(resultValue);
                            });

                            //赋初始值
                            bsligerform.getEditor(dyn_key+'subject_name').setValue(item.subject_content);
                            bsligerform.getEditor(dyn_key+'subject_unit').setValue(item.subject_unit);
                            bsligerform.getEditor(dyn_key+'id').setValue(item.id);

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
                        });

                        //初始化ckeditor
                        function BrowseServer(pathElement) {
                            var finder = new CKFinder();
                            finder.selectMultiple = true;//可以多选
                            //文件选中时执行
                            finder.selectActionFunction = function(fileUrl){
                                pathElement.val(fileUrl);
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

                var inspectiontaskFeedbackList = new Array();  // 反馈信息数组，存储反馈信息
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
                                    var inspectiontaskFeedback = {};
                                    var dyn_key = subject.area_code+"-"+subject.id+"-";
                                    inspectiontaskFeedback.subject_id = subject.id;  // 巡检项id
                                    inspectiontaskFeedback.inspectiontask_id = inspectiontask_id;
                                    inspectiontaskFeedback.check_value = $("input[name='"+dyn_key+"check_value']").val();
                                    inspectiontaskFeedback.check_result = $("#" + dyn_key + "check_resultBox").val();
//                                    if(subject.check_result == undefined){
//                                        subject.issubmit = '0';    // 未提交，则保存
//                                        validate_res = false;
//                                        validate_reason = '巡检区域['+subject.area_code+']下巡检项['+subject.subject_content+']未反馈，请检查';
//                                    } else {
//                                        subject.issubmit = '1';   // 点击提交按钮，则给issubmit赋值未1 已提交
//                                    }
                                    inspectiontaskFeedback.check_result = '0';   // 默认反馈结果都是正常
                                    inspectiontaskFeedback.issubmit = '1';   // 点击提交按钮，则给issubmit赋值未1 已提交
                                    inspectiontaskFeedback.isclose = '0';    // 未关闭，则要反馈信息
                                    inspectiontaskFeedback.check_time = DateUtil.dateToStr('yyyy-MM-dd hh:mm:ss',new Date());    // 反馈时间
                                    inspectiontaskFeedback.appearance = $("input[name='"+dyn_key+"appearance']").val();
                                    inspectiontaskFeedback.remark = $("input[name='"+dyn_key+"remark']").val();
                                    inspectiontaskFeedback.check_picture = $("input[name='"+dyn_key+"check_picture']").val();
                                    inspectiontaskFeedback.check_video = $("input[name='"+dyn_key+"check_video']").val();
                                    inspectiontaskFeedbackList.push(inspectiontaskFeedback);
                                });
                            }
                        });
                    }
                });

                if(!validate_res){
                    layer.alert(validate_reason);
                    return;
                }

                var submitData=new Object();//对象存储object
                if(manager1 != undefined){
                    submitData.toolsList = manager1.getData();
                    submitData.sparepartsList = manager2.getData();
                    submitData.personList = manager3.getData();
                    submitData.othersList = manager4.getData();
                }

                submitData.inspectiontask_id = inspectiontask_id;
                submitData.inspectiontaskFeedbackList = inspectiontaskFeedbackList;

                console.log("表单数据:");
                console.log(JSON.stringify(submitData));

                common.callAjax('post',false,'${ctx}/eam/inspectionTask/feedBack',"text",{param:JSON.stringify(submitData),taskid:taskid},function(data){
                    if(data=="success"){
                        layer.msg('提交成功！',{icon:1,time: 1000}, function(index){
                            parent.$("#mytable").DataTable().ajax.reload();
                            parent.layer.closeAll();
                        });
                    }else if (data == "noauth") {
                        layer.msg('登录超时或无权限！',{time: 1000,icon:7}, function(index){
                            layer.close(index);
                        });

                    }else{
                        layer.msg('提交失败！',{time: 1000,icon:2}, function(index){
                            layer.close(index);
                        });
                    }
                })


            });

            //值类型下拉切换
            $("#order_detailBox").on('change',function () {
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

                        { display: '工器具',  name: 'material_id',type:'text',
                            editor: { type: 'selectGrid', data: materialSelect,
                                valueField: 'material_id',textField: 'material_name'},
                            render: function (item)
                            {
                                var material_name;
                                if(item != null && item.material_id != null && item.material_id != ""){
                                    $.each(materialSelect, function (i, data) {
                                        if(data.material_id == item.material_id){
                                            material_name = data.material_name;
                                        }
                                    });
                                }
                                return material_name;
                            }
                        },
                        { display: '数量', name: 'tools_num', type: 'int', editor: { type: 'int'} },
                        { display: '计量单位', name: 'material_unit',
                            render: function (rowdata, rowindex, value) {
                                var material_unit;
                                $.each(materialSelect, function (i, data) {
                                    if(data.material_id == rowdata.material_id){
                                        material_unit = data.material_unit;
                                    }
                                });
                                return material_unit;
                            }
                        },
                        { display: '规格型号', name: 'material_model',
                            render: function (rowdata, rowindex, value) {
                                var material_model;
                                $.each(materialSelect, function (i, data) {
                                    if(data.material_id == rowdata.material_id){
                                        material_model = data.material_model;
                                    }
                                });
                                return material_model;
                            }
                        },

                        { display: '备注', name: 'tools_remark',
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
                        Rows: [{
                            "material_id": null,
                            "tools_num": null,
                            "material_unit": null,
                            "material_model": null,
                            "tools_remark": null
                        }]
                    },
                    width: '88%'
                });

                $('#toolStdTable').on('click', '.add', function () {
                    //表格参数key必须填，哪怕value是null，否则getData()取不到值
                    manager1.addRow({});
                });
                $('#toolStdTable').on('click','.del',function(){
                    if (manager1.getData().length == 1) {
                        layer.alert("首行不能删除");
                    } else {
                        manager1.deleteRow($(this).data("id"));
                    }
                })
            }

            //备品备件
            function f_initGrid2(){
                manager2 = $("#attachmentStdTable").ligerGrid({
                    columns: [
                        { display: '备品备件',  name: 'material_id',type:'text',
                            editor: { type: 'selectGrid', data: materialSelect,
                                valueField: 'material_id',textField: 'material_name'},
                            render: function (item)
                            {
                                var material_name;
                                if(item != null && item.material_id != null && item.material_id != ""){
                                    $.each(materialSelect, function (i, data) {
                                        if(data.material_id == item.material_id){
                                            material_name = data.material_name;
                                        }
                                    });
                                }
                                return material_name;
                            }
                        },
                        { display: '数量', name: 'spareparts_num', type: 'int', editor: { type: 'int'} },
                        { display: '计量单位', name: 'material_unit',
                            render: function (rowdata, rowindex, value) {
                                var material_unit;
                                $.each(materialSelect, function (i, data) {
                                    if(data.material_id == rowdata.material_id){
                                        material_unit = data.material_unit;
                                    }
                                });
                                return material_unit;
                            }
                        },
                        { display: '单价', name: 'material_price',
                            render: function (rowdata, rowindex, value) {
                                var material_price;
                                $.each(materialSelect, function (i, data) {
                                    if(data.material_id == rowdata.material_id){
                                        material_price = data.material_price;
                                    }
                                });
                                return material_price;
                            }
                        },
                        { display: '小计',name:'spareparts_total', isSort: false,
                            render: function (rowdata, rowindex, value)
                            {
                                var total = parseInt(rowdata.spareparts_num) * parseInt(rowdata.material_price);
                                return total;
                            }
                        },
                        { display: '备注', name: 'spareparts_remark', type: 'text', editor: { type: 'text'} },
                        { display: '操作', isSort: false,
                            render: function (rowdata, rowindex, value)
                            {
                                var h = "";
                                h += "<a  class='add'>添加</a> ";
                                h += "<a  class='del' data-id='" + rowindex + "'>删除</a> ";
                                return h;
                            }
                        },

                    ],
                    onSelectRow: function (rowdata, rowindex)
                    {
                    },
                    enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                    data:{Rows: [{
                        "material_id": null,
                        "spareparts_num": null,
                        "material_unit": null,
                        "material_price": null,
                        "spareparts_total": null,
                        "spareparts_remark":null
                    }]},
                    width: '88%'
                });

                $('#attachmentStdTable').on('click', '.add', function () {
                    manager2.addRow({});
                });
                $('#attachmentStdTable').on('click','.del',function(){
                    if (manager2.getData().length == 1) {
                        layer.alert("首行不能删除");
                    } else {
                        manager2.deleteRow($(this).data("id"));
                    }
                })
            }

            //人员工时
            function f_initGrid3(){
                manager3 = $("#manhaurStdTable").ligerGrid({
                    columns: [
                        { display: '人员',  name: 'loginname',type:'text',
                            editor: { type: 'selectGrid', data: personSelect,
                                valueField: 'loginname',textField: 'b_realname'},
                            render: function (item)
                            {
                                var realname;
                                if(item != null && item.loginname != null && item.loginname != ""){
                                    $.each(personSelect, function (i, data) {
                                        if(data.loginname == item.loginname){
                                            realname = data.b_realname;
                                        }
                                    });
                                }
                                return realname;
                            }
                        },
                        { display: '额定工时', name: 'person_hours', type: 'float', editor: { type: 'float'} },
                        { display: '额定工时单价', name: 'person_hourprice', type: 'float', editor: { type: 'float'} },
                        { display: '小计', name: 'person_hourtotal',
                            render: function (rowdata, rowindex, value) {
                                var total = parseFloat(rowdata.person_hours) * parseFloat(rowdata.person_hourprice);
                                return total;
                            }
                        },
                        { display: '岗位技能', name: 'person_postskill',editor: { type: 'text' },editor: { type: 'text' }},
                        { display: '备注', name: 'person_remark',editor: { type: 'text' },editor: { type: 'text' }},
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
                                "loginname": null,
                                "person_hours": null,
                                "person_hourprice": null,
                                "person_hourtotal": null,
                                "person_postskill": null,
                                "person_remark": null
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
                        layer.alert("首行不能删除");
                    } else {
                        manager3.deleteRow($(this).data("id"));
                    }
                })
            }

            //其他费用
            function f_initGrid4(){
                manager4 = $("#otherStdTable").ligerGrid({
                    columns: [
                        { display: '其他费用事项', name: 'otherexpenses', type: 'text', editor: { type: 'text'} },
                        { display: '金额', name: 'otherexpenses_amount', type: 'float', editor: { type: 'float'} },
                        { display: '备注', name: 'otherexpenses_remark',type: 'text',editor: { type: 'text' }},
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
                                "otherexpenses": null,
                                "otherexpenses_amount" : null,
                                "otherexpenses_remark" : null
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
                        layer.alert("首行不能删除");
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
