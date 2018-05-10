<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp" %>
<html>
<head>
    <title>二维码示例</title>
    <link href="/resource/plugins/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
    <link href="/resource/form.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="/resource/plugins/jQuery/jquery-2.2.3.min.js"></script>
    <script type="text/javascript" src="/resource/plugins/jQuery/jquery-form.js"></script>
    <script type="text/javascript" src="/resource/plugins/layer/layer.js"></script>
    <script src="/resource/plugins/ligerUI/js/ligerui.all.js"></script>

    <%--表单校验--%>
    <script src="/resource/plugins/ligerUI/jquery-validation/jquery.validate.min.js"></script>
    <script src="/resource/plugins/ligerUI/jquery-validation/jquery.metadata.js"></script>
    <script src="/resource/plugins/ligerUI/jquery-validation/messages_cn.js"></script>
    <script src="/resource/common.js"></script>
    <script src="/resource/jquery.jqprint-0.3.js"></script>

    <script type="text/javascript">
        var obj = parent.objArray;   // 获取父页面中已选择要打印二维码的数据信息
        var oldHtml="";
        $(function(){

            $(".ewmcode_display").html(obj[0].dev_code);   // 给二维码下的设备编码赋值
            $(".ewmmc_display").html(obj[0].dev_name);   // 给二维码下的设备名称赋值

            // 二维码信息获取并显示（上图标 项目文字  设备二维码图片）
            common.callAjax('post',true,ctx + "/QRCode/getQRCodeConfig","json",{"device_id" : obj[0].id_key},function(data){
                // 给下拉框设置获取到的值
                $("#QR-code-area-logo").attr("src",data.result[0]);
                $(".QR-code-area-txt").html(data.result[1]);
                createEwmPic(obj[0].id_key,"ewm_display0");
            });

            //生成二维码,返回二维码图片地址
            function createEwmPic(id_key,ewm_id){
                var xhr = new XMLHttpRequest();
                xhr.open("get", ctx + "/QRCode/createQRCode?qrdata=TYPE:DEVICE;ID:" + id_key, true);
                xhr.responseType = "blob";
                xhr.onload = function() {
                    if (this.status == 200) {
                        var blob = this.response;
                        $("#"+ewm_id).attr("src", window.URL.createObjectURL(blob));
                    }else if(this.status == 500){
                        layer.msg("系统错误",{time: 1000,icon:2});
                    }
                };
                xhr.send();
            }

            $("#canclePrintBtn").on("click", function () {
                parent.layer.closeAll();
            });

            var logo1,text,logo2;
            // 二维码信息获取并显示（上图标 项目文字  设备二维码图片）
            common.callAjax('post',true,ctx + "/QRCode/getQRCodeConfig","json",{"device_id" : parent.editId},function(data){
                // 给下拉框设置获取到的值
                logo1 = data.result[0];
                text = data.result[1];
                logo2 = data.result[2];
                var $strs=$('<div"></div>');
                $('#printContent').html($strs);
                for (var i = 0; i < obj.length; i++) {
                    var html="";
                    var objf = obj[i];
                    var style = 'margin:0;';
                    if (i != 0) {
                        style += 'padding-top: 15px;';
                    }
                    var names = '<p style="width:250px;"><b>设备名称：' + objf.dev_name + '</b></p><p style="width:250px;margin-top:-5px;"><b>设备编码：' + objf.dev_code + '</b></p>';
                    var id="ewm_display"+objf.id_key;
                    html= '<div style="background:#fff;width:265px;' + style + '">' +
                        '<div style="text-align: center;width:250;height:40px;"><img id="QR_logo1" class="QR_logo1" src="' + logo1 + '" style="width:230px;height:40px;" /></div>' +
                        '<div style="text-align: center;width:250;height:20px;padding:2px;">' + text + '</div>'  +
                        '<div style="text-align: center;width:250px;height:230px;"><img id="'+id+'" src="' + logo2 + '" alt="" style="width:230px !important;height:230px !important;" /></div>' +
                        '<div style="width:250px;height:75px;text-align:left;color:#000;font-size:12px;margin-top:-10px;overflow:hidden;">' + names + '</div></div>';
                    $strs.append(html);
                    createEwmPic(objf.id_key,id);
                }
            });

            $('#printBtn').on('click', function () {
                $('.closeBtn').click();
                $("#printContent").css("display","block");
                if(oldHtml==""){
                    oldHtml= $("#printContent").html();
                }else {
                    $("#printContent").html(oldHtml);
                }
                $('#printContent').jqprint({
                    debug: false,
                    importCSS: false,
                    printContainer: true,
                    operaSupport: false
                });
                $('#printContent').empty();
                setTimeout(function(){
                    var index = parent.layer.getFrameIndex(window.name);
                    parent.layer.close(index);
                },1000);
            });
            
        });
    </script>
</head>
<body>
<div class="QR-code-print" >

    <div>
        <!-- 图片 -->
        <div class="QR-code-area-logo">
            <img id="QR-code-area-logo" style="width:248px; height:30px;" src=""></img>
        </div>
        <!-- 文字 -->
        <div class="QR-code-area-txt">南京天溯</div>
        <!-- 二维码 -->
        <div class="QR-code" style="text-align:center;"><img id="ewm_display0" src="" alt="" style="width:248px;height:248px;"></div>
        <!--设备名称和设备编码文字 -->
        <div class="eqpInfo-txt pt10"><p class="QR-code-area-txtP1">设备名称:<span class="ewmmc_display">asdsadas</span></p>
            <p class="QR-code-area-txtP2">设备编码:<span class="ewmcode_display">12312312321313454323</span></p></div>
    </div><!-- 底部阴影条 -->
</div>
<div class="form-actions" style="margin-top: 30px">
    <input id="printBtn" type="button" value="开始打印"/>
    <input id="canclePrintBtn" type="button" value="取 消"/>
</div>
<div style="margin-top: 250px;"></div>
<div id="printContent" style="display: none"></div>
</body>
</html>
