/**
 * Created by lenovo on 2017/6/26.
 */
//jsp运行在服务器端，可以使用taglib标签，js则不行，在此命名全局变量

var ctx = '/a';


var _nodeList;//树节点查询数据

localStorage.setItem("ctx", ctx); //设置键的值

(function () {

    var common = {
        interfaceUrl: {
            // menuList: "/resource/data/menuList.json",
            roleList: ctx+"/eam/role/listData",
            roleForm:ctx+"/eam/role/roleForm",
            menuList: ctx+"/sys/eamuser/getMenuTreeByLoginUser",
            subSystemList: ctx+"/sys/eamsubsystem/findSubSystems",
            // getUserEmployeeInfo: "/resource/data/employeeInfo.json",
            getUserEmployeeInfo:ctx+"/sys/eamuser/getLoginUser",
            tableData: "/resource/data/tableData.json",
            //物料test模块列表数据
            materialData: ctx+"/material/testMaterial/listData",
            //物料类别模块列表数据
            materialTypeData: ctx+"/material/materialType/listData",
            //角色字段权限配置页面：
            roleMenuFieldForm:ctx+"/eam/role/roleMenuFieldForm",
			//数据范围权限配置接口
			addDataScope:ctx+"/eam/role/addDataScope",
            //根据菜单id获取按钮
            getButtonByRole: ctx + "/eam/button/getButtonByRole",
            //根据菜单id查询表格、表单、查询区域的字段权限
            getfields: ctx + "/eam/role/getfields",
            //文件上传 dfj
            fileUp:ctx+"/eam/excels/fileUp",
            importExcel:ctx+"/eam/excels/importExcel",
            devCategoryList: ctx+"/eam/devCategory/listData",

            //客户信息 dfj
            customerAddUI:ctx+"/employee/eamCustomer/addUI",//跳转新增页面
            customerInsert:ctx+"/employee/eamCustomer/insert",//新增
            customerDelete:ctx+"/employee/eamCustomer/delete",//删除
            customerEditUI:ctx+"/employee/eamCustomer/editUI",//跳转修改页面
            customerUpdate:ctx+"/employee/eamCustomer/update",//修改
            customerDetailUI:ctx+"/employee/eamCustomer/detailUI",//跳转详情页面
            customerDataTablePageMap:ctx+"/employee/eamCustomer/dataTablePageMap",//获取列表数据
            customerEditObj:ctx+"/employee/eamCustomer/editObj",//根据id获取单条数据
            //空间信息 dfj
            devLoctionAddUI:ctx+"/device/devLoction/addUI",//跳转新增页面
            devLoctionInsert:ctx+"/device/devLoction/insert",//新增
            devLoctionDelete:ctx+"/device/devLoction/delete",//删除
            devLoctionEditUI:ctx+"/device/devLoction/editUI",//跳转修改页面
            devLoctionUpdate:ctx+"/device/devLoction/update",//修改
            devLoctionDetailUI:ctx+"/device/devLoction/detailUI",//跳转详情页面
            devLoctionQrCodePrintLocUI:ctx+"/device/devLoction/qrCodePrintLocUI",//跳转二维码批量打印页面
            devLoctionDataTablePageMap:ctx+"/device/devLoction/dataTablePageMap",//获取列表数据
            getDevLocationTree:ctx+"/device/devLoction/getDevLocationTree",//获取ztree树数据
            getDeptTreeData:ctx+"/eam/dept/treeData",//获取部门树数据
            getDeptUserTreeData:ctx+"/eam/dept/deptUserTreeData",//获取部门人员树数据
            getDevTree:ctx+"/eam/device/treeData",//获取ztree树数据
            devLoctionEditObj:ctx+"/device/devLoction/editObj",//根据id获取单条数据
            //人员信息 dfj
            userExtAddUI:ctx+"/employee/eamUserExt/addUI",//跳转新增页面
            userExtInsert:ctx+"/employee/eamUserExt/insert",//新增
            userExtDelete:ctx+"/employee/eamUserExt/delete",//删除
            userExtEditUI:ctx+"/employee/eamUserExt/editUI",//跳转修改页面
            userExtUpdate:ctx+"/employee/eamUserExt/update",//修改
            userExtDetailUI:ctx+"/employee/eamUserExt/detailUI",//跳转详情页面
            userExtDataTablePageMap:ctx+"/employee/eamUserExt/dataTablePageMap",//获取列表数据
            userExtEditObj:ctx+"/employee/eamUserExt/editObj",//根据id获取单条数据
            userExtGetAllUser:ctx+"/employee/eamUserExt/getAllUser",//获取所有的用户
            userExtFindByLoginName:ctx+"/employee/eamUserExt/findByLoginName",//获取根据用户名称获取信息

            //物料信息模块列表数据
            materialInfoData: ctx+"/material/materialInfo/listData",
			deviceList: ctx + "/eam/device/listData",          //设备信息列表
            bizDictList: ctx + "/eam/bizDict/listData",     //业务字典列表
            getDictByDictTypeCode:ctx + "/eam/bizDict/getValues",//根据dict_type_code获取指定的业务字典信息
            operationworkData: ctx + "/eam/operationwork/listData",    //标准工作列表
            toolsInfoData: ctx + "/eam/operationwork/tools",    //下拉工器具列表数据
            workProcedure: ctx + "/eam/operationwork/getProcedure",   //获取某个标准工作下的工序列表数据
            workSafety: ctx + "/eam/operationwork/getSafety",     //获取某个标准工作下的安全措施列表数据
            workTools: ctx + "/eam/operationwork/getTools",       //获取某个标准工作下的工器具列表数据
            workSpareparts: ctx + "/eam/operationwork/getSpareparts",       //获取某个标准工作下的备品备件列表数据
            workPersonHours: ctx + "/eam/operationwork/getPersonhours",     //获取某个标准工作下的人员工时列表数据
            workOtherexpenses: ctx + "/eam/operationwork/getOtherexpenses",               //获取某个标准工作下的其他费用列表数据
            inspectionTaskData: ctx + "/eam/inspectionTask/listData",       // 巡检任务列表数据

            //巡检项   dfj
            inspectionSubjectDataTablePageMap:ctx + "/inspection/inspectionSubject/dataTablePageMap",//巡检项列表
            inspectionSubjectAddUI:ctx+"/inspection/inspectionSubject/addUI",//跳转新增页面
            inspectionSubjectSave:ctx+"/inspection/inspectionSubject/save",//新增和修改
            inspectionSubjectDelete:ctx+"/inspection/inspectionSubject/delete",//删除
            inspectionSubjectEditUI:ctx+"/inspection/inspectionSubject/editUI",//跳转修改页面
            inspectionSubjectDetailUI:ctx+"/inspection/inspectionSubject/detailUI",//跳转详情页面
            inspectionSubjectEditObj:ctx+"/inspection/inspectionSubject/editObj",//根据id获取单条数据
            inspectionSubjectGetDeviceTreeDataForInspectionSubject: ctx + "/eam/operationwork/getDeviceTreeDataForInspectionSubject",//获取设备
            inspectionSubjectDeviceSelectUI :ctx+"/inspection/inspectionSubject/DeviceSelectUI",//获取设备的弹出路径
            //巡检区域
            inspectionAreaDataTablePageMap:ctx + "/inspection/inspectionArea/dataTablePageMap",//巡检项列表
            inspectionAreaAddUI:ctx+"/inspection/inspectionArea/addUI",//跳转新增页面
            inspectionAreaSave:ctx+"/inspection/inspectionArea/save",//新增和修改
            inspectionAreaDelete:ctx+"/inspection/inspectionArea/delete",//删除
            inspectionAreaEditUI:ctx+"/inspection/inspectionArea/editUI",//跳转修改页面
            inspectionAreaDetailUI:ctx+"/inspection/inspectionArea/detailUI",//跳转详情页面
            inspectionAreaEditObj:ctx+"/inspection/inspectionArea/editObj",//根据id获取单条数据
            inspectionAreaSubjectSelectUI :ctx+"/inspection/inspectionArea/inspectionSubjectSelectUI",//获取巡检项的弹出路径
            inspectionSubjectSelectUI:ctx + "/inspection/inspectionSubject/inspectionSubjectSelectUI",//选择巡检项列表
            inspectionAreaInfoByTaskPstid : ctx + "/eam/inspectionTask/getAreaInfoByTaskPstid",    //根据流程id获取任务下的巡检区域
            inspectionSubjectInfoByAreaId : ctx + "/eam/inspectionTask/getSubjectInfoByAreaId",    // 根据区域id获取巡检项信息
            inspectionSaveSubjectFeedBack : ctx + "/eam/inspectionTask/saveSubjectFeedBack",       // 保存巡检项反馈信息
            maintSetGetDeviceTreeDataForMaintSet: ctx + "/eam/operationwork/getDeviceTreeDataForMaintSet",//获取设备
            maintSetDeviceSelectUI :ctx+"/inspection/inspectionSubject/DeviceSelectUI",//获取设备的弹出路径

            //保养年计划   dfj
            maintainProjectDataTablePageMap:ctx + "/maintain/maintainproject/dataTablePageMap",//保养列表页面
            maintainProjectApprovalUI:ctx + "/maintain/maintainproject/approvalUI",//生成审批页面
            maintainProjectApproval:ctx + "/maintain/maintainproject/approval",//审批
            maintainProjectCreateProjectData:ctx+"/maintain/maintainproject/createProjectData",//跳转详情页面
            maintainProjectEditObj:ctx+"/maintain/maintainproject/editObj",//根据id获取单条数据
            maintainProjectFindByPstid:ctx+"/maintain/maintainproject/findByPstid",//根据流程实例id获取单条数据
            maintainProjectEditUI:ctx+"/maintain/maintainproject/editUI",//跳转修改页面
            maintainProjectDetailUI:ctx+"/maintain/maintainproject/detailUI",//跳转详情页面
            maintainProjectDelete:ctx + "/maintain/maintainproject/delete",//删除
            maintainProjectSave:ctx + "/maintain/maintainproject/save",//修改保存

            //业务字典
            businessDictAddUI:ctx+"/eam/bizDict/addUI",//跳转新增页面
            businessDictList: ctx+"/eam/bizDict/listData",
        },
        dataTableOption: {},
        moduleConfig: {
            module1: [""]
        },
        //加载模块
        loadModule: function (module,menuno) {
            if (module) {
                try{
                    require([module],function(m){
                        m && m.init(menuno);
                    });
                }catch (e){
                    console.error(JSON.stringify(e));
                }
                this.initSetting();
            }
        },
        /**
         * 封装了自动添加序列列，复选框列和自定义操作列的表格
         * @param eid 表格元素的id号
         * @param option 配置项
         * 新增配置项：1.hascheckbox: false,//是否在表格的第一列添加复选框，默认false 不添加；
         * 2.hasIndex: false,//是否在表格第一列（若有复选框，则第二列）增加序号列，默认不添加；
         * 3.opBtns: [],//表格最后一列的自定义操作按钮，配置：title：鼠标悬浮的提示，icon:显示的图标，callBack：单击时回调函数（参数为该行原始数据对象）
         * @returns {jQuery} 表格对象，
         * 增加API：1.getCheckedRow：返回表格复选框勾选的原始数据对象
         */
        renderTable: function (eid, option) {
            //默认配置
            var defaultOption = {
                "hascheckbox": false,//是否在表格的第一列添加复选框，默认false 不添加；
                "hasIndex": false,//是否在表格第一列（若有复选框，则第二列）增加序号列，默认不添加；
                "opBtns": [],//表格最后一列的自定义操作按钮，配置：title：鼠标悬浮的提示，icon:显示的图标，callBack：单击时回调函数
                "paging": true,
                "searching": false,
                "autoWidth": false,
                "serverSide": true,
                "pagingType": "full_numbers",
                "aLengthMenu": [10, 15, 30, 100],
                "info": false,
                "dom": 't<"col-sm-6"l>p<"clear">',
                'language': {
                    'emptyTable': '没有数据',
                    'loadingRecords': '加载中...',
                    'processing': '查询中...',
                    'search': '检索:',
                    'lengthMenu': '每页 _MENU_ 条',
                    'zeroRecords': '没有数据',
                    'paginate': {
                        'first': '首页',
                        'last': '末页',
                        'next': '下一页',
                        'previous': '上一页'
                    },
                    'info': '第 _PAGE_ 页 / 总 _PAGES_ 页',
                    'infoEmpty': '没有数据',
                    'infoFiltered': '(过滤总条数 _MAX_ 条)'
                }
            };

            //modify by wangjl at 20170829 : 后台数据查询为空时，前台返回的json中没有数据为null的列，
            // 会报Requested unknown parameter 错误，在此统一处理，如果数据为空，则返回空字符串
            if(option.columns){
                option.columns.forEach(function (index) {
                    index.sDefaultContent  = '';
                });
            }
            //重写dataTable默认错误提示方式，来替换 DataTables 弹出的错误提示
            $.fn.dataTable.ext.errMode = function(settings, techNote, message){
                console.error('表格数据获取失败:'+settings.jqXHR.responseText);
                layer.msg("表格数据获取失败！",{time: 1000,icon:2});
            };


            var o = $.extend({}, defaultOption, option);
            /**增加序号列*/
            if (o.hasIndex) {
                $('#' + eid + ' > thead > tr').prepend('<th>序号</th>');
                o.columns = o.columns || [];
                o.columns.unshift({
                    title: '<th>序号</th>',
                    "width": "10px",
                    "orderable": false,
                    render: function (data, type, row, pos) {
                        return pos.row + 1;
                    },
                });
            }
            /**增加复选框*/
            if (o.hascheckbox) {
                $('#' + eid + ' > thead > tr').prepend('<th  class=""><divclass=""><input name="checkbox" type="checkbox"/> </div> </th >');
                $('#' + eid).on("change", " > thead > tr input[name='checkbox']", function () {
                    $('#' + eid + ' > tbody > tr input[name="checkbox"]').prop("checked", $(this).prop("checked"));
                });
                o.columns = o.columns || [];
                o.columns.unshift({
                    title: '<th class=""><divclass=""><input name="checkbox" type="checkbox"/> </div> </th >',
                    "data": o.checkId,
                    "width": "10px",
                    "orderable": false,
                    render: function (data, type, row, pos) {
                        return " <div class=''> <input name='checkbox' type='checkbox'  row='" + pos.row + "'/></label>";
                    },
                });
            }

            /**增加操作按钮*/
            if (o.opBtns && o.opBtns.length > 0) {
                $('#' + eid + ' > thead > tr').append('<th>操作</th>');
                var callbacks = {};
                $('#' + eid).off("click");
                $('#' + eid).on("click", ">tbody i[name='ji-optBtn'],>tbody span[name='ji-optBtn']", function () {
                    callbacks[$(this).attr("callback")].call(tab, tab.row($(this).attr("row")).data());
                });
                var i = 0, len = o.opBtns.length, obj, btnHtml = "";
                for (; i < len; i++) {
                    obj = o.opBtns[i];
                    callbacks["call-optBtn" + i] = obj.callBack;
                    if(obj.icon){
                        btnHtml += "<i name='ji-optBtn' row=ji-row callback='call-optBtn" + i + "' style='cursor: pointer;' class='fa " + obj.icon +
                            " "+obj.class+"' title='" + obj.title + "'></i><span style='display: inline-block;width: 5px;'></span>";
                    }else if(obj.label){
                        btnHtml += "<span name='ji-optBtn' row=ji-row callback='call-optBtn" + i + "' style='cursor: pointer;' class='" +
                            obj.class+"' title='" + obj.title + "'>"+obj.label+"</span><span style='display: inline-block;width: 5px;'></span>";
                    }

                }
                o.columns = o.columns || [];
                o.columns.push({
                    "title": "操作",
                    "data": "id",
                    "orderable": false,
                    render: function (data, type, row, pos) {
                        return btnHtml.replace(/ji-row/g, pos.row);
                    },
                });
            }
            var tab = $("#" + eid).DataTable(o);
            /**获取多选框选中的项*/
            tab.getCheckedRow = function () {
                var result = [];
                $('#' + eid + ' > tbody > tr > td input[name="checkbox"]:checked').each(function (index, item) {
                    result.push(tab.row($(item).attr("row")).data());
                });
                return result;
            };
            return tab;
        },
        initSetting: function(){
            this.initAjaxSetting();
        },

        initAjaxSetting: function () {
            var tipLayer;
            //页面首个ajax开始时
            // $(document).ajaxStart(function(){
            //     tipLayer = layer.load(1, {
            //         shade: [0.1,'#fff'] //0.1透明度的白色背景
            //     });
            // });
            //页面所有ajax结束时
            $(document). ajaxStop(function(){
                layer.closeAll('loading');
            });
            //设置AJAX的全局默认选项
            $.ajaxSetup( {
                //设置ajax请求结束后的执行动作
                complete :
                    function(XMLHttpRequest, textStatus) {
                        try {
                            // 通过XMLHttpRequest取得响应头，sessionstatus
                            var sessionstatus = XMLHttpRequest.getResponseHeader("sessionstatus");
                            if (sessionstatus == "TIMEOUT") {
                                var win = window;
                                while (win != win.top){
                                    win = win.top;
                                }
                                alert("登录超时或该用户已在其他地方登录，请重新登录");
                                win.location.href= XMLHttpRequest.getResponseHeader("CONTEXTPATH");
                            }
                        } catch (e) {
                            console.error(e);
                        }
                    },
                error: function(jqXHR, textStatus, errorMsg){ // 出错时默认的处理函数
                    // jqXHR 是经过jQuery封装的XMLHttpRequest对象
                    // textStatus 可能为： null、"timeout"、"error"、"abort"或"parsererror"
                    // errorMsg 可能为： "Not Found"、"Internal Server Error"等
                    layer.close(tipLayer);
                    // 提示形如：发送AJAX请求到"/index.html"时出错[404]：Not Found
                    console.log( '发送AJAX请求到"' + this.url + '"时出错[' + jqXHR.status + ']：' + errorMsg );
                }
            } );
        },



        //定义统一的ajax调用方法，统一封装异常捕捉，防止个人写ajax时未捕捉js异常导致js错误引发整个页面的卡死;
        callAjax:function (f_type,f_async,f_url,f_dataType,f_param,f_suc_callback) {
            $.ajax({
                type: f_type,
                async: f_async, //false表示同步
                url: f_url,
                data:f_param,
                dataType: f_dataType,
                success: function (data) {
                    try {
                        // debugger;
                        if(f_suc_callback){
                            f_suc_callback.call(this,data);
                        }
                    } catch (e) {
                        console.error(console.error(JSON.stringify(e)));
                    }
                },
                error:function (data) {
                    var result = data.responseText;
                    // layer.msg('操作失败！',{time: 1000,icon:2});
                    debugger;
                    console.error(result);
                }
            });
        },
    //公用获取编码
    ajaxForCode:function(param,async,dataType,callback){
            $.ajax({
                type:'post',
                url:ctx + '/sequence/getcode',
                async:async,
                dataType:dataType,
                data:param,
                success:function(data){
                    try{
                        if(callback){
                            callback.call(this,data);
                        }
                    }catch(e){
                        console.error(e);
                    }
                }
            })
    },
//公用获取行内按钮
        ajaxForButton:function(mid,dataType,editUi,deleteUi,detailUi,area,closeUi,approve_status,approve_value){
        var btnInline=[];
            $.ajax({
                type:'post',
                url:ctx + "/eam/button/getButtonByRole",
                async:false,
                dataType:dataType,
                data:{id:mid},
                success:function(data){
                    try{
                        var btnHtml="";
                        for(var i=0;i<data.length;i++){
                            var btn = data[i];
                            if(btn!==null && btn!=""){
                                if(btn.buttonno=="edit"){
                                    var obj = {};
                                    obj.icon=btn.icon;
                                    obj.title=btn.buttonname;
                                    obj.callBack=function (data) {
                                        editId=data.id_key;
                                        if(approve_status !=null && approve_status!=""){
                                            if(data.approve_status !=approve_value){
                                                layer.msg('只允许编辑待审批的数据！',{time: 1000,icon:7}, function (index) {
                                                });
                                                return;
                                            }
                                        }
                                        layer.open({
                                            type: 2,
                                            title: '修改',
                                            skin: 'layui-layer-rim', //加上边框
                                            area: area, //宽高
                                            closeBtn: 1, //显示关闭按钮
                                            content: editUi
                                        });
                                    };
                                    btnInline.push(obj);
                                }else if(btn.buttonno=="delete"){
                                    var obj = {};
                                    obj.icon=btn.icon;
                                    obj.title=btn.buttonname;
                                    obj.callBack=function (data) {
                                        layer.confirm('确认删掉这条数据吗？',{icon: 3, title:'提示'},function() {
                                            common.callAjax('post', false, deleteUi, "text", {id:data.id_key}, function (d) {
                                                if (d == "success") {
                                                    layer.msg('删除成功！',{icon: 1,time: 1000}, function (index) {
                                                        $('#mytable').dataTable().fnDraw(false);
                                                        layer.close(index);
                                                    });
                                                }else if(d=="nodele"){
                                                    layer.msg('有效或被引用的数据禁止删除！',{time: 1000,icon:7}, function (index) {
                                                        layer.close(index);

                                                    })
                                                }else if(d="nodelestatus"){
                                                    layer.msg('只能删除待审批的数据！',{time: 1000,icon:7}, function (index) {
                                                        layer.close(index);

                                                    })
                                                }else {
                                                    layer.msg("删除失败！",{time: 1000,icon:2});
                                                }
                                            });
                                        })
                                    };
                                    btnInline.push(obj);
                                }else if(btn.buttonno=="detail"){
                                    var obj = {};
                                    obj.icon=btn.icon;
                                    obj.title=btn.buttonname;
                                    obj.callBack=function (data) {
                                        editId=data.id_key;
                                        layer.open({
                                            type: 2,
                                            title: '详情',
                                            skin: 'layui-layer-rim', //加上边框
                                            area: area, //宽高
                                            closeBtn: 1, //显示关闭按钮
                                            content: detailUi
                                        });
                                    };
                                    btnInline.push(obj);
                                }else if(btn.buttonno=="closed"){
                                    var obj = {};
                                    obj.icon=btn.icon;
                                    obj.title=btn.buttonname;
                                    obj.callBack=function (data) {
                                        if(data.approve_status !="1" ||data.isClosed!="0"){
                                            layer.msg("此数据禁止作废！",{time: 1000,icon:7});
                                            return;
                                        }
                                        layer.confirm('确认申请作废这条数据吗？',{icon: 3, title:'提示'},function() {
                                            common.callAjax('post', false, closeUi, "text", {id:data.id_key}, function (d) {
                                                if (d == "success") {
                                                    layer.msg('申请成功！',{icon:1,time: 1000}, function (index) {
                                                        $('#mytable').dataTable().fnDraw(false);
                                                        layer.close(index);
                                                    });
                                                } else {
                                                    layer.msg("申请失败！",{time: 1000,icon:2});
                                                }
                                            });
                                        })
                                    };
                                    btnInline.push(obj);
                                }
                                //行外按钮渲染
                                if(btn.buttonno=='add'||btn.buttonno=='export'||btn.buttonno=='exportall'||btn.buttonno=='download'||btn.buttonno=='import') {
                                    $("<input>", {
                                        type: 'button',
                                        val: btn.buttonname,
                                        id: btn.buttonno,
                                        onclick: btn.onclickevent
                                    }).appendTo($(".btnArea"));
                                }
                            }
                        }

                    }catch(e){
                        console.error(e);
                    }
                }
            });
            console.log(btnInline);
            return btnInline;
        },

        //excel模板下载
        loadExcelModel:function(name){
                common.downloadFileByForm("/uploadServlet.do",name);
            },
        // 模拟表单提交同步方式下载文件
        // 能够弹出保存文件对话框
        downloadFileByForm:function (url,fileName,showname) {
                var form = $("<form></form>").attr("action", url).attr("method", "post");
                form.append($("<input></input>").attr("type", "hidden").attr("name", "name").attr("value", fileName));
                form.append($("<input></input>").attr("type", "hidden").attr("name", "showname").attr("value", showname));
                form.appendTo('body').submit().remove();
            },
        //导入excel
        setExcel: function (type){
                //文件下载名称
                var name="";
                //显示的名称
                var showname="";
                var formId = 'jUploadForm' + 'fileField';  //file为input的id
                var test1 = jQuery('#'+formId);
                //console.log("1:"+test1.prop("outerHTML"));//打印输出
                if(test1.length>0){
                    test1.remove();
                }
                $.ajaxFileUpload({
                    /*java.io.IOException: Corrupt form data: premature ending*/
                    /*   url:common.interfaceUrl.fileUp,        上面的信息是无法使用spring mvc的原因，网上说的是拦截的问题*/
                    url:"/fileupload.do",
                    secureuri:false,
                    fileElementId:'fileField',
                    dataType:'html',
                    success:function(d,s) {
                        try {
                            var json = eval('(' + d + ')');
                            //设置下载名称
                            name=json.uid+".txt";
                            showname=json.fileName+"错误信息.txt";
                            $.ajax({
                                /*  url: "../importServlet.do?a=customer",*/
                                url: common.interfaceUrl.importExcel,
                                type: 'POST',
                                data: {'name': json.uid + json.type, 'fileName': json.fileName, a: type},
                                async: false,
                                dataType: "json",
                                success: function (data) {//通过验证后也可能失败
                                    var msg=data.msg;
                                    if ("导入成功"==msg){
                                        layer.msg(msg,{icon:1,time: 1000});
                                        $(".searchbtn").click();
                                        $(".queryBtn").click();
                                        return;
                                    }
                                    try {
                                        layer.open({
                                            type: 1,
                                            title: '错误信息',
                                            skin: 'layui-layer-rim', //加上边框
                                            area: common.getArea(), //宽高
                                            content: msg,
                                            btn: ['关闭','下载'], //只是为了演示
                                            btn1: function(){
                                                layer.closeAll();
                                            },
                                            btn2: function(){
                                               common.downloadFileByForm("/loadTxtServlet.do",name,showname);
                                            }
                                        });
                                    } catch (e) {
                                        console.error(e);
                                    }
                                },
                                error: function (data) {//未通过验证返回

                                    try {
                                        var msg = data.responseText;
                                        layer.open({
                                            type: 1,
                                            title: '错误信息',
                                            skin: 'layui-layer-rim', //加上边框
                                            area: common.getArea(), //宽高
                                            content: msg,
                                            btn: ['关闭','下载'], //只是为了演示
                                            btn1: function(){
                                                layer.closeAll();
                                            },
                                            btn2: function(){
                                                common.downloadFileByForm("/loadTxtServlet.do",name,showname);
                                            }
                                        });
                                    } catch (e) {
                                        console.error(e);
                                    }
                                }
                            });
                        } catch (e) {
                            console.error(e);
                        }
                    }
                });
                $("#fileField").val('');
            },

    //树节点查询操作

        getTreeByName:function (treeId,text) {
            var zTree = $.fn.zTree.getZTreeObj(treeId);
            var value = $.trim(text);
            //收缩所有节点并且取消上次的搜索后的状态

            zTree.cancelSelectedNode();
            this.updateNodes(false,false,treeId,_nodeList);
            //zTree.expandAll(false);
            if(value.length>0){
                //根据节点数据的属性搜索，获取条件模糊匹配的节点数据 JSON对象集合
                 _nodeList = zTree.getNodesByParamFuzzy("name", value);
                if(_nodeList.length!=0){
                   this.updateNodes(true,true,treeId,_nodeList);
                }
            }
        },
        //更新高亮显示
        updateNodes: function (highlight,expand,treeId,nodeList) {
            var zTree = $.fn.zTree.getZTreeObj(treeId);
            $.each(nodeList,function(index,node){
                node.highlight = highlight;
                zTree.updateNode(node);
                if(expand){
                    zTree.expandNode(node.getParentNode(),expand);
                    if (index==0) {//搜索时默认显示第一个搜索结果
                        zTree.selectNode(node);
                    }
                }

            });
        },
        getAreaXS:function () {
            if ($(window).width() > 1123 && $(window).height()>672) {
                return ['950px', '50%'];
            }else {
                return ['950px', '55%'];
            }
        },
        //数量较少
        getAreaS:function () {
            if ($(window).width() > 1123 && $(window).height()>672) {
                return ['950px', '70%'];
            }else {
                return ['950px', '75%'];
            }
        },
        //数量很多
        getAreaM:function () {
                return ['960px', '90%'];
        },
        //数量少&带有二维码
        getAreaL:function () {
            if ($(window).width() > 1123 && $(window).height()>672) {
                return ['1200px', '70%'];
            }else {
                return ['1200px', '75%'];
            }
        },
        //数量很多&带有二维码
        getAreaXL:function () {
            if ($(window).width() > 1123 && $(window).height()>672) {
                return ['1200px', '90%'];
            }else {
                return ['1200px', '90%'];
            }
        },
        //弹框中的弹框
        getAreaInner:function () {
            if ($(window).width() > 1123 && $(window).height()>672) {
                return ['90%', '90%'];
            }else {
                return ['95%', '90%'];
            }
        },
        getArea:function () {
            if ($(window).width() > 1123 && $(window).height()>672) {
                return ['1200px', '90%'];
            }else {
                return ['1200px', '90%'];
            }
        },
    };
    Date.prototype.format = function(fmt) {
        var o = {
            "M+" : this.getMonth()+1,                 //月份
            "d+" : this.getDate(),                    //日
            "h+" : this.getHours(),                   //小时
            "m+" : this.getMinutes(),                 //分
            "s+" : this.getSeconds(),                 //秒
            "q+" : Math.floor((this.getMonth()+3)/3), //季度
            "S"  : this.getMilliseconds()             //毫秒
        };
        if(/(y+)/.test(fmt)) {
            fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
        }
        for(var k in o) {
            if(new RegExp("("+ k +")").test(fmt)){
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
            }
        }
        return fmt;
    };
    window.common = common;
})();
(function() {
    function WatchService() {

    }

    WatchService.property = {
        count: 0,
        SocketCreated: true,
        f:null,
        // wsImpl: window.WebSocket || window.MozWebSocket,
        wsurl: 'ws://127.0.0.1:8181', // 192.168.20.26:8181为后台服务端的地址
        loginWatch: function() {
            //console.log(that.wsurl);
            var $socket=$("#socket");
            var self = this;

            that.wsurl='ws://'+$socket.attr('data-host');
            var token=$socket.attr('data-token');
            if ("WebSocket" in window) {
                window.ws = new window.WebSocket(that.wsurl);
            } else if ("MozWebSocket" in window) {
                window.ws = new window.MozWebSocket(that.wsurl);
            }
            self.SocketCreated = false;

            // 显示数据
            ws.onmessage = function(evt) {
                var json = JSON.parse(evt.data);
                self.logout(json.Message,self);
            };
            ws.onopen = function() {
                var pageId = window.location.href.toLowerCase();
                ws.send(token);
            };
            ws.onerror = function(e) {
                console.log("链接通道中断！");
            };
            ws.onclose = function(e) {
                //console.log()
                self.count = self.count + 1;
                if (self.count < 3) {
                    // self.loginWatch(self.wsurl);
                    // console.log(self.count);
                }
            };

        },
        logout: function(msg) {
            $tiansu.common.info("show",
                {
                    content: msg,
                    timeout: 8000
                });
            setTimeout(function() {
                window.location.href = ctx+'/a/eam/logout';
            }, 8000);
        },
        close: function() {
            if (!this.SocketCreated) {
                window.ws.close();
                this.SocketCreated = true;
            }

        },
        getCookie: function(cname) {
            var name = cname + "=";
            var ca = document.cookie.split(';');
            for (var i = 0; i < ca.length; i++) {
                var c = ca[i];
                while (c.charAt(0) == ' ') c = c.substring(1);
                if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
            }
            return "";
        }

    };
    var that;
    window._ = that = WatchService.property;
})();
