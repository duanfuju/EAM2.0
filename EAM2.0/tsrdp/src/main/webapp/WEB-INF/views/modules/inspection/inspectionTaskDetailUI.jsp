<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp" %>
<html>
<head>
    <title>巡检任务详情</title>
    <link rel="stylesheet" type="text/css" href="/resource/plugins/select2/select2.css"/>
    <link rel="stylesheet" type="text/css" href="/resource/plugins/select2/select2.min.css">
    <link rel="stylesheet" type="text/css" href="/resource/plugins/webupload/style.css"/>
    <link href="/resource/form.css" rel="stylesheet" type="text/css" />
    <link href="/resource/plugins/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="/resource/plugins/webupload/webuploader.css"/>
    <link rel="stylesheet" type="text/css" href="/resource/plugins/bootstrap-switch/css/bootstrap3/bootstrap-switch.css"/>
    <link rel="stylesheet" type="text/css" href="/resource/modules/inspection/inspectionfinishSelect.css"/>
    <link rel="stylesheet" type="text/css" href="/resource/plugins/jQuery-Timepicker-Addon/jquery-ui-timepicker-addon.css"/>

    <style>
        .editDiv .l-panel-bar{
            display: none ;
        }
    </style>
    <script src="/resource/plugins/jQuery/jquery-2.2.3.min.js"></script>
    <script src="/resource/plugins/jQueryUI/jquery-ui.min.js"></script>
    <script src="/resource/plugins/select2/select2.js"></script>
    <script src="/resource/plugins/ligerUI/js/ligerui.all.js"></script>
    <script src="/resource/common.js"></script>
    <script src="/resource/plugins/bootstrap-switch/js/bootstrap-switch.min.js"></script>
    <script type="text/javascript" src="/static/ckfinder/ckfinder.js"></script>
    <script type="text/javascript" src="/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/resource/plugins/jQuery-Timepicker-Addon/jquery-ui-timepicker-addon.js"></script>

    <script type="text/javascript">
        var pstid;  // 巡检任务工作流id
        var sbjCount = 0;
        var allLength = 0;
        var dat = null;  //所有巡检项数据
        var nowDate = null;
        var nowTime = null;
        var nowSubCode = null;
        $(function() {
            //深克隆
            function cloneObject(obj) {
                var o = obj.length > 0 ? [] : {};
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
            console.log(parent.formConfig)

            var formField = cloneObject(parent.formConfig);

            //将详情页面的基本信息改为不可编辑
            $.each(formField, function(index,val) {
                val.readonly = true;
            });
            var length = formField.length;
            formField[length] = {
                editable: "true",
                display: "巡检明细",
                name: "inspectionTaskDetailTab",
                comboboxName: "inspectionTaskDetailTabBox",
                type: "select"
            };
            //创建表单结构
            var formConfig = {
                space: 50, labelWidth: 120, inputWidth: 203,
                validate: true,
                fields: formField
            };
            $("#inputForm").ligerForm(formConfig);

            /**巡检任务的基本信息，给表单赋值**/
            common.callAjax('post',false,ctx + "/eam/inspectionTask/getInspectionTaskById","json",{"inspectionTask_id" : parent.editId},function(data){
                //shufq:bug  #17892【巡检任务】巡检任务中，实际 使用工时为0，实际使用工时需要自动计算，按确认接单时间到反馈完成时间计算，参照1.0.1计算方法
                var editForm  = liger.get("inputForm");
                if(data.task_time_finish) {
                    var time = data.task_time_finish - data.task_time_begin;
                    var hour = Math.floor(time / 3600 / 1000);
                    var min = Math.floor(time / 1000 % 3600 / 60);
                    data.task_totalhour = hour + '小时' + min + '分钟';
                }
                editForm.setData(data);
                pstid = data.pstid;
            });

            // 工序表格数据
            var procedureTable = null;
            // 安全措施表格数据
            var safetyTable = null;
            // 工器具表格数据
            var toolsTable = null;
            // 备品备件表格数据
            var sparepartsTable = null;
            // 人员工时表格数据
            var personhoursTable = null;
            // 其他费用表格数据
            var othersTable = null;
            // 物料下拉数据
            var materialSelect = new Object();
            // 人员工时下拉数据
            var personSelect = new Object();

            common.callAjax('post',false,common.interfaceUrl.toolsInfoData,"json",{"inspectiontask_id":parent.editId},function(data){
                if(data == null || data.length == 0){
                    materialSelect = [{
                        "material_code": null,
                        "material_id" : null,
                        "material_model" : null,
                        "material_name" : null,
                        "material_price" : null,
                        "material_unit" : null
                    }];
                } else {
                    materialSelect = data;
                }
            });
            /**人员工时下拉框数据**/
            common.callAjax('post',false,common.interfaceUrl.userExtGetAllUser,"json",{"inspectiontask_id":parent.editId},function(data){
                personSelect = data;
            });

            /** 巡检任务下的巡检工器具明细信息**/
            common.callAjax('post',false,ctx + "/eam/inspectionTask/getDetailList","json",{"inspectiontask_id" : parent.editId},function(data){
                procedureTable = data.procedure;
                safetyTable = data.safety;
                toolsTable = data.tools;
                sparepartsTable = data.spareparts;
                personhoursTable = data.personList;
                othersTable = data.othersList;
            });



            // procedure    工序列表
            var procedure = $("#procedure").ligerGrid({
                columns: [
                    { display: '工序编码', name: 'procedure_code', type: 'text'},
                    { display: '描述', name: 'procedure_desc', type: 'text'},
                    { display: '质检标准', name: 'procedure_standard',type: 'text'},
                    { display: '备注', name: 'procedure_remark'}
                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                data:{Rows: procedureTable},
                width: '88%'
            });

            // safety    安全措施列表
            var safety = $("#safety").ligerGrid({
                columns: [
                    { display: '安全措施编码', name: 'safety_code', type: 'text'},
                    { display: '描述', name: 'safety_desc', type: 'text'},
                    { display: '质检标准', name: 'safety_standard',type: 'text'},
                    { display: '备注', name: 'safety_remark'}
                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                data:{Rows: safetyTable},
                width: '88%'
            });
            // 安全措施表格默认隐藏
            $("#safety").hide();

            // tools   工器具列表
            var tools = $("#tools").ligerGrid({
                columns: [
                    { display: '工器具',  name: 'material_id',type:'text',
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
                    { display: '数量', name: 'tools_num', type: 'text'},
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
                    { display: '备注', name: 'tools_remark'}
                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                data:{Rows: toolsTable},
                width: '88%'
            });
            // 其他费用表格默认隐藏
            $("#tools").hide();

            // spareparts   备品备件列表
            var spareparts = $("#spareparts").ligerGrid({
                columns: [
                    { display: '备品备件',  name: 'material_id',type:'text',
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
                    { display: '数量', name: 'spareparts_num', type: 'text'},
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
                    { display: '小计',name:'spareparts_total', isSort: false},
                    { display: '备注', name: 'spareparts_remark', type: 'text', editor: { type: 'text'} }
                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                data:{Rows: sparepartsTable},
                width: '88%'
            });
            // 备品备件表格默认隐藏
            $("#spareparts").hide();

            // 人员工时列表
            var person = $("#person").ligerGrid({
                columns: [
                    { display: '人员',  name: 'loginname',type:'text',
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
                    { display: '额定工时', name: 'person_hours', type: 'text'},
                    { display: '额定工时单价', name: 'person_hourprice', type: 'text'},
                    { display: '小计', name: 'person_hourtotal'},
                    { display: '岗位技能', name: 'person_postskill'},
                    { display: '备注', name: 'person_remark'}
                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                data:{Rows: personhoursTable},
                width: '88%'
            });
            // 人员工时表格默认隐藏
            $("#person").hide();

            // otherexpenses    其他费用列表
            var otherexpenses = $("#otherexpenses").ligerGrid({
                columns: [
                    { display: '其他费用事项', name: 'otherexpenses', type: 'text'},
                    { display: '金额', name: 'otherexpenses_amount', type: 'text'},
                    { display: '备注', name: 'otherexpenses_remark',type: 'text'}
                ],
                onSelectRow: function (rowdata, rowindex)
                {
                },
                enabledEdit: true, isScroll: false, checkbox:false,rownumbers:true,
                data:{Rows: othersTable},
                width: '88%'
            });
            // 其他费用表格默认隐藏
            $("#otherexpenses").hide();

            //标准工作类型下拉初始化
            var typeHtml = "";
            $.each(parent.operationworkTypeSelect, function (i, item) {
                typeHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
            });
            var inspectionTaskDetailTabBox = $("#operationwork_typeBox");
            inspectionTaskDetailTabBox .html(typeHtml);
            inspectionTaskDetailTabBox.trigger('change.select2');
            inspectionTaskDetailTabBox.prop("disabled", true);

            // 下拉表格
            var tableData = [{text: '工序', id: '0'}, {text: '安全措施', id: '1'}, {text: '工器具', id: '2'},
                {text: '备品备件', id: '3'}, {text: '人员工时', id: '4'}, {text: '其他费用', id: '5'}];
            var optionHtml = "";
            $.each(tableData, function (i, item) {
                optionHtml += "<option value=\"" + item.id + "\">" + item.text + "</option>";
            });
            $("#inspectionTaskDetailTabBox").html(optionHtml);
            $("#inspectionTaskDetailTabBox").val("0").trigger("change");
            //下拉框选中事件
            $("#inspectionTaskDetailTabBox").on('change',function () {
                if($("#inspectionTaskDetailTabBox").val() == "0"){
                    $("#procedure").show();
                    $("#safety").hide();
                    $("#tools").hide();
                    $("#spareparts").hide();
                    $("#person").hide();
                    $("#otherexpenses").hide();
                } else if($("#inspectionTaskDetailTabBox").val() == "1"){
                    $("#procedure").hide();
                    $("#safety").show();
                    $("#tools").hide();
                    $("#spareparts").hide();
                    $("#person").hide();
                    $("#otherexpenses").hide();
                } else if($("#inspectionTaskDetailTabBox").val() == "2"){
                    $("#procedure").hide();
                    $("#safety").hide();
                    $("#tools").show();
                    $("#spareparts").hide();
                    $("#person").hide();
                    $("#otherexpenses").hide();
                } else if($("#inspectionTaskDetailTabBox").val() == "3"){
                    $("#procedure").hide();
                    $("#safety").hide();
                    $("#tools").hide();
                    $("#spareparts").show();
                    $("#person").hide();
                    $("#otherexpenses").hide();
                } else if($("#inspectionTaskDetailTabBox").val() == "4"){
                    $("#procedure").hide();
                    $("#safety").hide();
                    $("#tools").hide();
                    $("#spareparts").hide();
                    $("#person").show();
                    $("#otherexpenses").hide();
                } else if($("#inspectionTaskDetailTabBox").val() == "5"){
                    $("#procedure").hide();
                    $("#safety").hide();
                    $("#tools").hide();
                    $("#spareparts").hide();
                    $("#person").hide();
                    $("#otherexpenses").show();
                }
            });

            /*---加载巡检区域详情start---*/
            var areas = null;

            common.callAjax('post',false,common.interfaceUrl.inspectionAreaInfoByTaskPstid,"json",{pstid: pstid},function(data){
                areas = data;
                if(data && data.length>0){
                    inspectiontask_id = data[0].inspectiontask_id;
                }
                for (var i = 0; i < areas.length; i++) {
                    var areaName = '<li data-areaId="' + areas[i].id + '"><span class="serialNum"></span>' + areas[i].area_code + areas[i].area_name + '<span class="toggleIcon toggleIconClick"></span></li>';
                    $("#patrol-result-content .patrol-area-list").append(areaName);
                }
            });
            /*---加载巡检区域详情end---*/
            Date.prototype.Format = function (fmt) {
                var o = {
                    "M+": this.getMonth() + 1,
                    "d+": this.getDate(),
                    "h+": this.getHours(),
                    "m+": this.getMinutes(),
                    "s+": this.getSeconds(),
                    "q+": Math.floor((this.getMonth() + 3) / 3),
                    "S": this.getMilliseconds()
                };
                if (/(y+)/.test(fmt))
                    fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
                for (var k in o)
                    if (new RegExp("(" + k + ")").test(fmt))
                        fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
                return fmt;
            };

            //巡检区域的序号生成
            $("#patrol-result-content .patrol-area-list .serialNum").each(function (i) {
                var str = ("00" + (i + 1)).slice(-2);
                $(this).html(str);
            });
            //隐藏显示“现象录入”
            $("#patrol-result-content").on("click", "span", function () {
                var index = $(this).index();
                var $pheno = $(this).closest("tr").next(".phenomenon-content");
                if (index == 0) {
                    $pheno.find("input.phenomenon").val("");
                    $pheno.addClass("hide");
                } else {
                    $pheno.removeClass("hide");
                }
            });

            //点击箭头按钮生成巡检区域表格数据
            $("#patrol-result-content").on("click", ".toggleIcon", function () {
                //获取当前区域id
                var areaId = $(this).parent().attr("data-areaId");
                var $nextLi = $(this).closest("li").next();
                var everInit = $nextLi.hasClass("patrol-area-table");
                var hasToggle = $(this).hasClass("toggleIconClick");
                if (hasToggle) {

                    if (!everInit) {
                        $(this).closest("li").siblings(".patrol-area-table").addClass("hide");
                        /*
                         * 数据说明：status状态判断该条数据是否标记红色，异常或者报修需要标记红色
                         * */

                        common.callAjax('post',false,common.interfaceUrl.inspectionSubjectInfoByAreaId,"json",{area_id: areaId,inspectiontask_id:inspectiontask_id},function(data){
                            dat = data;
                            allLength +=data.length;
                            if (dat && dat.length > 0) {
                                for (var i = 0; i < dat.length; i++) {
                                    var subjects = dat[i].subjects;
                                    var patrolCode = [];
                                    if (subjects && subjects.length > 0) {
                                        for (var j = 0; j < subjects.length; j++) {
                                            patrolCode.push(subjects[j].id);
                                            dat[i].patrolCode = patrolCode.join("_")
                                        }
                                    }
                                }
                            }
                            //巡检项总数赋值
                            sbjCount += dat.length;
                        });

                        var jsonString = JSON.stringify(dat);
                        var json = JSON.parse(jsonString);

                        var waitingData = {
                            /*数据要求：传来的patrolCode唯一，否则会造成文件上传显示错误*/
                            "retCode": 0,
                            "data": json
                        };
                        orderCompleteCTable(waitingData.data, this);
                        $("#patrol-result-content .toggleIcon").addClass("toggleIconClick");
                        $(this).removeClass("toggleIconClick");
                    } else {
                        $(this).closest("li").next().removeClass("hide");
                    }
                } else {
                    $(this).toggleClass("toggleIconClick");
                    $nextLi.toggleClass("hide");
                }
            });

            var that = this;

            //点击反馈按钮生成录入区域，并初始化
            $("#patrol-result-content").on("click", ".patrol-feedback-icon", function () {
                $(this).addClass("thisEdit").closest("tr").siblings().find(".patrol-feedback-icon").removeClass("thisEdit");
                //获取当前巡检项的数据
                var thisSubDat = new Object();
                if (dat && dat.length) {
                    var thisIndex = $(this).closest("tr").children("td:first").text() - 1;
                    thisSubDat = dat[thisIndex];
                }

                var tib = $(this).parent("td").prev().prev().prev().text();
                $(this).closest('table').parent().next().children().hide();

                if ($(this).closest('tr').hasClass('nowTr editingTr')) {
                    var containBox = $(this).closest('table').parent().next().find('div.contain-box');
                    $.each(containBox, function (index, contain) {
                        var ensureName = $(contain).find('table.ensureTable').find('td:nth-child(2)').text();
                        if (tib == ensureName) {
                            $(contain).removeClass("hide").show();
                        }
                    });
                    return;
                }

                //获取当前并生成随机数，用于命名图片和视频--caoh
                nowDate = new Date().Format('yyyy-MM-dd hh:mm:ss').replace(/-/g, "").replace(" ", "").replace(/:/g, "");
                var num = parseInt(1000 * Math.random());
                nowDate += num;


                var thatIcon = this;
                var nowTr = $(this).closest("tr");
                nowTr.addClass("nowTr editingTr");
                //nowTr.addClass("nowTr editingTr").siblings("tr").removeClass("nowTr editingTr");
                var domId = nowTr.find("td:nth-child(2)").html();

                //获取当前正在编辑的subjectCode--caoh
                nowSubCode = domId;

                createLoadArea(domId, thatIcon, that, nowDate, thisSubDat);
                $(".switch input").bootstrapSwitch({
                    size: "mini",
                    onSwitchChange: function (event, state) {
                        $(this).closest("td").find("span.patrol-feedback-icon").click();
                        var switchall = $(this).closest("table").attr("class");
                        var thisText = $(this).closest("td").prev().prev().prev().text();
                        var thisTables = $(this).closest("table").parent().next().find(".contain-box");
                        if (state == true) {
                            $(this).closest("table").next(".hideEle").hide();
                            if (switchall == "std-workDet-tbl") {
                                $.each(thisTables, function (index, thisTable) {
                                    var thatText = $(thisTable).find('table.ensureTable').find('td:nth-child(2)').text();
                                    if (thatText == thisText) {
                                        $(thisTable).find('table.unsureTable').find('tr:first').find('td.switch').find('div.bootstrap-switch-off').find('input').click();
                                    }
                                });
                            }
                        } else {
                            $(this).closest("table").next(".hideEle").show();
                            if (switchall == "std-workDet-tbl") {
                                $.each(thisTables, function (index, thisTable) {
                                    var thatText = $(thisTable).find('table.ensureTable').find('td:nth-child(2)').text();
                                    if (thatText == thisText) {
                                        $(thisTable).children('table.unsureTable').find('tr:first').find('td.switch').find('div.bootstrap-switch-on').find('input').click();
                                    }
                                });
                            }
                        }
                    }
                });

                if (thisSubDat && thisSubDat.subjects) {
                    var objs = thisSubDat.subjects;
                    for (var i = 0; i < objs.length; i++) {
                        var subid = objs[i].id;
                        $("#checkVal" + subid).val(objs[i].subject_ck_value);
                        $("#excDescription" + subid).val(objs[i].remark);
                        $("#checkResult" + subid).find("span").eq(objs[i].check_result).trigger("click");
                        if (objs[i].check_result == '1' || objs[i].check_result == '2') {
                            $("#phenomenon" + subid).val(objs[i].appearance);
                        }
                    }
                }
            });

            //点击编辑按钮显示具体内容
            $("#patrol-result-content").on("click", ".patrol-edit-icon", function () {
                var tib = $(this).parent("td").prev().prev().prev().text();
                var containBox = $(this).closest('table').parent().next().find('div.contain-box');
                $.each(containBox, function (index, contain) {
                    $(contain).addClass("hide");
                    var ensureName = $(contain).find('table.ensureTable').find('td:nth-child(2)').text();
                    if (tib == ensureName) {
                        $(contain).removeClass("hide").show();
                    }
                });
                var nowTr = $(this).closest("tr");
                var domId = nowTr.find("td:nth-child(2)").html();
                $(this).closest(".patrol-area-table").find("#popTable" + domId).removeClass("hide");
                $(this).next(".switch-mini").removeClass("hide");
            });

            //取消按钮，清除所有的unsureTable

            $("#patrol-result-content").on("click", ".cancelBtn", function () {
                $(".contain-box").addClass("hide");
            });

            //录入区域的生成函数
            function createLoadArea(domId, thatIcon, that, nowDate, thisSubDat){
                var domIds = domId.split("_");
                var devname = $(thatIcon).parent().prev().prev().prev().text();
                var devloca = $(thatIcon).parent().prev().prev().text();
                var region = 0;

                if (domIds.length > 0) {

                    var divs = '<div class="contain-box showTable" style="border: 1px solid #15c7bd;">';
                    var tableTitle = '<table width=900 class="userFormTable ensureTable"><tr><td width=9%><span class="tdlable devicenames">设备名称：</span></td><td width=30%>' + devname + '</td><td width=10%>设备位置：</td><td width=28%><span class="eqPos">' + devloca + '</span></td><td width=12%></td></tr></table>';
                    divs = $(divs).append(tableTitle);
                    $.each(domIds, function (index, item) {
                        var thisSubject = thisSubDat.subjects[index];
                        var tableHTML = "";
                        var imgHtml = "";
                        var vIdHtml = "";

                        //shufq:bug17621图片赋值
                        var upload='<div style="overflow:hidden"><div  class="upload">' +
                            '<span class="txt">上传图片</span>' +
                            '<ul class="imgArea">'+
                            '</ul>' +
                            '</div>';



                        upload += '<div  class="upload">' +
                            '<span class="txt">上传视频</span>' +
                            '<ul class="videoArea">' +
                            '</ul>' +
                            '</div></div>';
//                        var upload = '<tr><td style="display:block;margin-right:60px"><div class="uploadFile"><div id="imgarea'+item+'"><div class="fileimg"><label>上传图片：</label><img id="img'+item+'" class="img'+item+'" src="" style="width:200px;height:200px;"><input  type="hidden" name="mytext[]" id="img'+item+'" readonly/></div></div></div></td>' +
//                            '<td><div class="uploadFile"><div id="vidarea'+item+'"><div class="filevid"><label>上传视频：</label><img id="vid'+item+'" class="vid'+item+'" src="" style="width:200px;height:200px;"><input type="hidden" name="mytext[]" id="vid'+item+'" readonly/></div></div></div></td></tr>';


//                        var upload = '<tr><td style="display:block;margin-right:60px"><div class="uploadFile"><div id="imgarea'+item+'"><div class="fileimg"><label>上传图片：</label><input  type="hidden" style="width:200px;height: 30px;" name="mytext[]" id="img'+item+'" readonly/></div></div></div></td>' +
//                            '<td><div class="uploadFile"><div id="vidarea'+item+'"><div class="filevid"><label>上传视频：</label><input type="hidden" style="width:200px;height: 30px;" name="mytext[]" id="vid'+item+'" readonly/></div></div></div></td></tr>';
                        //内容是否可编辑
                        var hidEle = '<div style="position:absolute;z-index:10;background-color:#f5f2ec;opacity:0.5" class="getwid' + index + ' hideEle" hidden></div>';

                        tableHTML += '<table style="position:relative; border-collapse:collapse;"  class="userFormTable patrol-area-popTable unsureTable" id="popTable' + item + '"><tbody>';

                        tableHTML += '<tr style="position: relative;"><td class="tdlable" width=11%>巡检项编码：</td><td width=32%><span' +
                            ' class="patrolList subnamme" id="subnamme' + item + '">' + item + '</span></td>' +
                            ' <td class="tdlable" width=11%>巡检项：</td><td style="word-wrap: break-word;word-break: break-all;" width=32%><span class="patrolList" id="subname' + item + '">温度检查</span></td>' +
                            '</tr>';

                        tableHTML += '<tr ><td class="tdlable"><span class="requireStar">*</span>检查值：</td><td class="checkValue"><select id="checkVal' + item + '" name="eqName" class="popInp requireCheck requireCheckNor" disabled="disabled"></select></td><td class="tdlable">单位：</td><td><span class="unit" id="checkUnit' + item + '">无</span></td></tr><tr style="position: relative;"><td class="tdlable">参考值：</td><td><span class="referValue" id="referValue' + item + '">无</span></td><td class="tdlable"><span>测量时间：</span></td><td><span class="nowTime" id="checkTime' + item + '"></span></td></tr>';
                        tableHTML += '<tr><td class="tdlable"><span class="requireStar">*</span>反馈结果：</td><td><div class="multisSpan"><div style="margin:0;padding:0;" class="radio clearfix revResult" id="checkResult' + item + '"><span class="rightSpan region useRight">正常</span><span class="rightSpan unusual">报修</span><span class="unusual">异常</span></div></div></td><td class="tdlable repair" hidden><span class="requireStar">*</span>现象：</td><td class="repair" hidden><select id="moreExc' + item + '" name="eqName" class="wideInp requireCheck"></select></td></tr>';
                        tableHTML += '<tr><td class="tdlable remarks">备注：</td><td colspan="4"><textarea readonly name="excDescription"' +
                            ' placeholder="备注描述" id="excDescription' + item + '" maxlength="150"' +
                            ' style="width: 75%;padding: 5px 0 0;"></textarea><span class="s_area2" style="position: relative; top:' +
                            ' 18px;left:15px;color: #CCCCCC; display:none">0/150</span></td></tr>';
                        tableHTML += '<tr><td class="tdlable"></td><td><div style="width: 275%;border: 1px dashed #CCCCCC;border:' +
                            ' box;border-bottom: 0px;margin-bottom: 0px;margin-left: -60px;"></div></td></tr></tbody></table>';

                        divs = $(divs).append(tableHTML);
                        divs = $(divs).append(hidEle);


                        var tablebtm = '<ul class="btnlist_list clearfix"><li></li><li style="margin-top:10px;margin-left:-98px;"><div class="btnlist"><a' +
                            ' class="ml15 cancelBtn" id="cancelBtn1' + domId + '">取消</a></div></li><li colspan="2"></li></ul></div>';
                        if (domIds.length == index + 1) {
                            $(divs).append(upload);
//                            divs = $(divs).append(tablebtm);
                        }
                        $(thatIcon).closest(".patrol-area-table").find(".patrol-area-table-feedback").append(divs);

                        //初始化赋值
                        if(thisSubject.check_result == 1){
                            $("#checkResult"+item).closest('tr').find('.repair').show();
                            $("#checkResult"+item).find(".rightSpan.useRight").removeClass("region");
                            $("#checkResult"+item).find(".rightSpan.unusual").addClass("region");

                        }else if(thisSubject.check_result == 2){
                            $("#checkResult"+item).find(".rightSpan.useRight").removeClass("region");
                            $("#checkResult"+item).closest('tr').find('.repair').show();
                            $("#checkResult"+item).find(".unusual:last").addClass("region");
                        }else{
                            $("#checkResult"+item).find(".rightSpan.useRight").addClass("region");
                        }

                        //检查值
                        var checkVals = [];
                        if (thisSubject && thisSubject.subject_unit == undefined) {
                            thisSubject.subject_value1 && checkVals.push(thisSubject.subject_value1);
                            thisSubject.subject_value2 && checkVals.push(thisSubject.subject_value2);
                            thisSubject.subject_value3 && checkVals.push(thisSubject.subject_value3);
                            $("#checkVal" + thisSubject.id).select2({
                                data: checkVals,
                                width: 200,
                                allowClear: false,
                                above: false,
                                placeholder:  thisSubject.check_value ||'请选择',
                                disabled: false,
                                minimumResultsForSearch: -1,
                                tags: true,
                                theme: "ems"
                            }).on('change', function () {
                                nowTime = new Date().Format("yyyy-MM-dd hh:mm:ss");
                                $("#checkTime" + item).html(nowTime);
                                $("#checkTime" + item).show();
                            });
                            $("#checkVal" + thisSubject.id).attr('disabled','disabled');
                        } else if (thisSubject && thisSubject.subject_unit != undefined) {
                            // thisSubject.check_value && checkVals.push(thisSubject.check_value);
                            thisSubject.subject_ck_value && checkVals.push(thisSubject.subject_ck_value);
                            thisSubject.subject_sx_value && checkVals.push(thisSubject.subject_sx_value);
                            thisSubject.subject_xx_value && checkVals.push(thisSubject.subject_xx_value);
                            checkVals.sort(function (a, b) {
                                return a - b;
                            })
                            $("#referValue" + thisSubject.id).html(checkVals[0] + "~" + checkVals[checkVals.length-1]);  //参考值
                            $("#checkVal" + thisSubject.id).select2({
                                data: [thisSubject.check_value],
                                width: 200,
                                allowClear: false,
                                above: false,
                                placeholder:  thisSubject.check_value ||'请输入检查值',
                                disabled: false,
                                minimumResultsForSearch: 0,
                                tags: true,
                                theme: "ems"
                            }).on('change', function () {
                                var thisCheckVal = $("#checkVal" + item).val();
                                var thisReferVal = $("#referValue" + item).html();
                                if (thisReferVal != "") {
                                    referValList = thisReferVal.split("~").sort(function (a, b) {
                                        return a - b
                                    });
                                    if (!Number(thisCheckVal) || Number(thisCheckVal) < Number(referValList[0]) || Number(thisCheckVal) > Number(referValList[1])) {
                                        layer.msg("输入的值不在参考值范围内,请确认",{time: 1000,icon:7});
                                    }
                                }
                                nowTime = new Date().Format("yyyy-MM-dd hh:mm:ss");
                                $("#checkTime" + item).html(nowTime);
                                $("#checkTime" + item).show();
                            });
                            $("#checkVal" + thisSubject.id).attr('disabled','disabled');
                        }

                        if (thisSubject) {
                            $("#subname" + thisSubject.id).html(thisSubject.subject_content);   //巡检项
                            $("#checkUnit" + thisSubject.id).html(thisSubject.unit_name);  //单位
//                            $("#img" + thisSubject.id).val(thisSubject.check_picture);//图片
//                            $("#vid" + thisSubject.id).val(thisSubject.check_video);//视频
                           //shufq:bug17621图片视频赋值
                            if(thisSubject.check_picture) {
                                //遍历图片并赋值
                                var imgAll = thisSubject.check_picture;
                                var imgId = imgAll.substring(1,imgAll.length-1).split(",");
                                for (var j = 0; j < imgId.length; j++) {
                                    imgHtml += '<li><img src='+imgId[j]+'/></li>'
                                }
                                $(".imgArea").append(imgHtml);
                               //遍历视频并赋值
                                var vIdAll = thisSubject.check_video;
                                var vId = vIdAll.substring(1,vIdAll.length-1).split(",");
//                                var vId = thisSubject.check_video.split(";")
                                for (var j = 1; j < vId.length; j++) {
                                    vIdHtml += '<li><video src='+vId[j]+' controls="controls"/></li>'
                                }
                                $(".videoArea").append(vIdHtml);
                            }
                            $("#checkTime" + thisSubject.id).html(thisSubject.check_time);    // 测量时间
                            $("#checkTime" + thisSubject.id).html(thisSubject.check_time);    // 测量时间
                        }

                        var standardFailureList = [];
                        if(thisSubDat && thisSubDat.standardFailureList.length>0){
                            for(var i=0;i<thisSubDat.standardFailureList.length;i++){
                                thisSubDat.standardFailureList[i].failure_phenomena_desc!=null && standardFailureList.push(thisSubDat.standardFailureList[i].failure_phenomena_desc);
                            }
                        }
                        //故障现象
                        $("#moreExc" + item).select2({
                            data: standardFailureList,
                            width: 200,
                            allowClear: false,
                            above: false,
                            placeholder: thisSubject.appearance || '请选择或者输入故障现象',
                            disabled: false,
                            minimumResultsForSearch: 0,
                            tags: true,
                            theme: "ems"
                        });

                        $(".getwid" + index).width("100%");
                        $(".getwid" + index).height(217);
                        $(".getwid" + index).css("margin-top", -217);

                    });
                }
            }

            // 已完成页面的巡检区域编码表格生成
            function orderCompleteCTable(data, that){
                var liHTML = '<li class="patrol-area-table"><div class="table-scroll"><table class="std-workDet-tbl"><thead><tr><th>序号</th><th>设备名称</th><th>设备位置</th><th>巡检项</th><th>操作</th></tr></thead><tbody>';

                $.each(data, function (index, dataList) {
                    var patrolCode = dataList.patrolCode || "";
                    var dev_name = dataList.dev_name || "";
                    var loc_name = dataList.loc_name || "";
                    var subjectNames = dataList.subjectNames || "";
                    liHTML += '<tr class="' + status + '"><td>' + (1 + index) + '</td>';
                    liHTML += '<td style="display:none">' + patrolCode + '</td>' + '<td>' + dev_name + '</td>' + '<td>' + loc_name + '</td>' + '<td>' + subjectNames + '</td>';
                    liHTML += '<td><span class="patrol-feedback-icon"></span><span class="patrol-edit-icon' +
                        ' hide"></span></td></tr>';
                });
                liHTML += "</tbody></table></div>" + '<div class="patrol-area-table-feedback" style="margin-top: 10px;"></div>';
                $(that).closest("li").after(liHTML);
            }
            $("#closeBtn").on("click", function () {
                parent.layer.closeAll();
            });

        });
    </script>
</head>
<body>
<div class="patrol-area-detail" id="patrol-result-content">
    <form id="inputForm" action="" method="post" class="form-horizontal">

    </form>
    <div class="editDiv">
    <div class="subeditDiv" id="procedure"></div>
    <div class="subeditDiv" id="safety"></div>
    <div class="subeditDiv" id="tools"></div>
    <div class="subeditDiv" id="spareparts"></div>
    <div class="subeditDiv" id="person"></div>
    <div class="subeditDiv" id="otherexpenses"></div>
    </div>
    <div class="switchArea patrolResult" style="margin-top:30px;">
        <div class="tdUl">
            <ul class="patrol-area-list">

            </ul>
        </div>
    </div>
    <div class="form-actions">
    <input id="closeBtn"  type="button"  value="关 闭"/>
    </div>
</div>
</body>
<style>
    .patrol-area-detail .l-text-field{
        -webkit-box-shadow: none;
        box-shadow: none;
        border: none;
        border-right: 1px solid #b5b5b5;
        padding-left: 0;
    }
</style>
</html>
