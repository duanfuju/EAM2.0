<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>巡检区域</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
        var selectedSubjectObj = [];
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
            var formField = cloneObject(parent.formConfig);
            //初始化下拉树数据
            $.each(formField, function (index, val) {
                if(val.name == 'area_status') {
                    val.options = {
                        data : parent._areaStatus
                    }
                }else if(val.name == 'area_qrcode_status'){
                    val.options = {
                        data : parent._areaQrcodeStatus
                    }
                }
            });
            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                validate: true,
                fields: formField

            };
            $("#inputForm").ligerForm(formConfig);



            // 选择多个设备的弹出框
            $("input[name='area_subject_names']").on("click",function () {
                layer.open({
                    type: 2,
                    title:'选择巡检项',
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getAreaInner(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: common.interfaceUrl.inspectionAreaSubjectSelectUI
                });
            });


            //编辑页面字段赋值
            common.callAjax('post',false,common.interfaceUrl.inspectionAreaEditObj,"json",{"id":parent.editId},function(d){
                var data=d.obj;
                var subjects=eval(d.subjects);
                var editForm  = liger.get("inputForm");
                editForm.setData(data);
                //状态下拉设置默认值
                $("#area_statusBox").val(data.area_status).trigger('change.select2');
                $("#area_statusBox").append($("<input></input>").attr("type", "hidden").attr("name", "area_status"));
                $("#area_qrcode_statusBox").val(data.area_qrcode_status).trigger('change.select2');
                $("#area_qrcode_statusBox").append($("<input></input>").attr("type", "hidden").attr("name", "area_qrcode_status"));
                if(subjects){
                    selectedSubjectObj=subjects;
				}
            });
            $("input[name='area_code']").attr("readonly","readonly");
            $("#btnSubmit").on("click",function () {
                var form = liger.get('inputForm');
                if (!form.valid()) {
                    form.showInvalid();
                }else{
                    //保存数据object
                    var param={};
                    param=form.getData();
                    param.area_status=$("#area_statusBox").val();
                    param.area_qrcode_status=$("#area_qrcode_statusBox").val();
                    console.log(param);
                    //表单提交
                    common.callAjax('post',false,common.interfaceUrl.inspectionAreaSave,"text",{param :JSON.stringify(param)} ,function(data) {
                        if(data=="success"){
                            layer.msg('修改成功！',{icon: 1,time: 1000}, function(index){
                                parent.RefreshPage();//刷新父页面
                                parent.layer.closeAll();
                            });
                        }else{
                            layer.msg("修改失败！",{time: 1000,icon:2});
                        }
                    });
                }
            })
        });
	</script>
</head>
<body>
<form id="inputForm" action="${ctx}/inspection/inspectionArea/save"  method="post" class="form-horizontal">
</form>
<div class="form-actions">
	<input id="btnSubmit" type="button" value="保 存"/>
</div>
</body>
</html>