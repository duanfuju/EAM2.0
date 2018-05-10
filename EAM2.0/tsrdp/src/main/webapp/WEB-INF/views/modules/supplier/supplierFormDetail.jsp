<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>供应商管理</title>
    <meta name="decorator" content="default"/>

    <script type="text/javascript">
        // var groupicon = "/resource/plugins/ligerUI/skins/icons/communication.gif";

        $(function () {
            debugger;
            //深克隆
            function cloneObject(obj) {
                //var o = obj instanceof Array ? [] : {};
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
            var formFields = cloneObject(parent.formConfig);
            debugger;
            $.each(formFields, function (index, val) {
                debugger;
                val.readonly = true;
            });
            //创建表单结构
            var formConfig = {
                space: 50, labelWidth: 120, inputWidth: 200,
                //validate: true,
                fields: formFields
            };

            $("#inputForm").ligerForm(formConfig);

            //状态初始化

            common.callAjax('post', false, common.interfaceUrl.getDictByDictTypeCode, "json", {"dict_type_code": "supplier_level"}, function (data) {
                var statusHtml = "";
                $.each(data, function (i, item) {
                    statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                $("#supplier_levelBox").html(statusHtml).append($("<input></input>").attr("type", "hidden").attr("name", "supplier_level")).val(1).trigger('change.select2');
            });
            common.callAjax('post', false, common.interfaceUrl.getDictByDictTypeCode, "json", {"dict_type_code": "common"}, function (data) {
                var statusHtml = "";
                $.each(data, function (i, item) {
                    statusHtml += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                $("#supplier_statusBox").html(statusHtml).append($("<input></input>").attr("type", "hidden").attr("name", "supplier_status")).val(1).trigger('change.select2');
            });
            // 信用等级：	 优秀AAA      良好AA        较好A     一般BBB       BB        差B

            common.callAjax('post', false, common.interfaceUrl.getDictByDictTypeCode, "json", {"dict_type_code": "credit_level"}, function (data) {
                var html = "";
                $.each(data, function (i, item) {
                    html += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                $("#supplier_creditBox").html(html).append($("<input></input>").attr("type", "hidden").attr("name", "supplier_credit")).val('AAA').trigger('change.select2');
            });
            //初始化供应商类型
            $.ajax({
                url: ctx + '/supplier/supplierType/getsuppliertype',
                type: 'post',
                async: false,
                dataType: 'json',
                success: function (data) {
                    var statusHtml = "<option>请选择</option>";
                    $.each(data, function (i, item) {
                        statusHtml += "<option value=\"" + item.id + "\">" + item.text + "</option>";
                    });
                    $("#supplier_typeBox").html(statusHtml).append($("<input></input>").attr("type", "hidden").attr("name", "supplier_type")).val(1).trigger('change.select2');
                }
            });


            //详情页面字段赋值
            $.ajax({
                type: "post",
                async: false, //同步执行
                url: ctx + "/supplier/supplier/editObj?id=" + parent.editId,
                dataType: "json", //传递数据形式为json
                success: function (data) {
                    data.supplier_busdate_start = DateUtil.dateToStr('yyyy-MM-dd', new Date(data.supplier_busdate_start));
                    data.supplier_busdate_end = DateUtil.dateToStr('yyyy-MM-dd', new Date(data.supplier_busdate_end));
                    console.log(data)
                    var editForm = liger.get("inputForm");
                    editForm.setData(data);
                    $("#supplier_creditBox").val(data.supplier_credit).trigger('change.select2');
                    $("#supplier_statusBox").val(data.supplier_status).trigger('change.select2');
                    $("#supplier_levelBox").val(data.supplier_level).trigger('change.select2');
                    $("#supplier_typeBox").val(data.supplier_type).trigger('change.select2');

                }
            });

            //设置选择下拉框只读
            $("select").attr("disabled","disabled");
            //设置开启时间只读 找到input当前元素,设置只读.
            $("input[ligeruiid='supplier_busdate_start']").attr("disabled","disabled");
             // 设置结束时间,找到input的当前元素,设置只读
            $("input[ligeruiid='supplier_busdate_end']").attr("disabled","disabled");

               // 设置背景颜色,input和父级元素的大小不一样.暂时没有设置.
            // $("input[type='text']").css("background","#E0E0E0");


            $("#closeBtn").on("click", function () {
                parent.layer.closeAll();

            });

        });


    </script>
</head>
<body>
<form id="inputForm" action="" method="post" class="form-horizontal">


</form>
<div class="form-actions">

    <input id="closeBtn" type="button" value="关 闭"/>
</div>
</body>
</html>
