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

			//获取编码
            common.ajaxForCode({type:"INSPECTION_AREA"},false,"text",function(data){
                if (data !="" ||data !=null) {
                    $("input[name='area_code']").val(data);
                    $("input[name='area_code']").attr("readonly","readonly");
                }
            });



            //状态：   有效  1       无效  0
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "common"},function(data){
                var statusHtml="";
                $.each(data, function (i, item) {
                    statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                var area_statusBox= $("#area_statusBox");
                area_statusBox .html(statusHtml);
                area_statusBox.append($("<input></input>").attr("type", "hidden").attr("name", "area_status"));//设置隐藏域
                area_statusBox.val(1);//设置默认值
                area_statusBox.trigger('change.select2');
            });
            //状态：   有效  1       无效  0
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "area_qrcode_status"},function(data){
                var statusHtml="";
                $.each(data, function (i, item) {
                    statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                var area_qrcode_statusBox= $("#area_qrcode_statusBox");
                area_qrcode_statusBox .html(statusHtml);
                area_qrcode_statusBox.append($("<input></input>").attr("type", "hidden").attr("name", "area_qrcode_status"));//设置隐藏域
                area_qrcode_statusBox.val(0);//设置默认值
                area_qrcode_statusBox.trigger('change.select2');
            });


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
				//表单提交
				common.callAjax('post',false,common.interfaceUrl.inspectionAreaSave,"text",{param :JSON.stringify(param)} ,function(data) {
					if(data=="success"){
						layer.msg('新增成功！',{icon: 1,time: 1000},function () {
							parent.RefreshPage();//刷新父页面
							parent.layer.closeAll();
						});
					}else if (data == "timeout") {
						layer.msg("登录超时或无权限！",{time: 1000,icon:7});
					} else {
						layer.msg("新增失败！",{time: 1000,icon:2});
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
	<input id="btnSubmit" type="button" value="提 交"/>
</div>
</body>
</html>