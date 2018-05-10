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
                //设置只读
                val.readonly = true;
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

            //设置只读
            $("select").attr("disabled","disabled");

            //设置巡检项名称只读 找到input当前元素,设置只读.
            $("input[ligeruiid='area_subject_names']").attr("disabled","disabled");

            $("#closeBtn").on("click",function () {
                parent.layer.closeAll();
            });
        });


	</script>
</head>
<body>
<form id="inputForm" action="" method="post" class="form-horizontal">


</form>
<div class="form-actions">

	<input id="closeBtn"  type="button"  value="关 闭"/>
</div>
</body>
</html>