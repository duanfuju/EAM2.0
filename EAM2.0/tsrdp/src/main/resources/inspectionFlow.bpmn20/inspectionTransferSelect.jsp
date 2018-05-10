<script type="text/javascript">
    var requestReason;   // 请求原因
    var switch_person = "";   //待转单人
    var result = '1';           //操作方式
    $(function () {
        common.callAjax('post',false,common.interfaceUrl.userExtGetAllUser,"json",null,function(data){
            personSelect = data;
            var optionstring = "<option value='请选择'>请选择</option>";
            for (var i in data) {
                var jsonObj = data[i];

                optionstring += "<option value=\"" + jsonObj.loginname + "\" >" + jsonObj.b_realname + "</option>";
            }
            $("#person").html(optionstring);
        });

        $("select#person").change(function(){
            switch_person = $('#person option:selected') .val();
            $("input[name='switch_person']").val(switch_person);
        });

        // 给申请原因赋值
        // 撤销原因
        $('#cancleReason').bind('input propertychange','textarea',function(){
            requestReason = $("#cancleReason").val();
            $("input[name='requestReason']").val(requestReason);
        });

        // 转单原因
        $('#transferReason').bind('input propertychange','textarea',function(){
            requestReason = $("#transferReason").val();
            $("input[name='requestReason']").val(requestReason);
        });

        $("input:radio[name='radio']").change(function (){
            var value;
            var radio = document.getElementsByName("radio");
            for(var i = 0; i < radio.length; i++) {
                if(radio[i].checked == true){
                    value = radio[i].value;
                    break;
                }
            }
            result = value;
            $("#result").val(result);
            if(value == '1'){
                $(".transfer-area").hide();
                $(".cancle-area").hide();
            } else if(value == '2') {
                $(".transfer-area").show();
                $(".cancle-area").hide();
            } else {
                $(".transfer-area").hide();
                $(".cancle-area").show();
            }
        });
        $("#result").val(result);
    });
</script>


<div class="control-select">
    <div class="controls" style="margin-left: 60px;">
        <span>操作选择&nbsp;&nbsp;</span>
        <input type="radio" name="radio" value="1" checked>接单
        <input type="radio" name="radio" value="2">转单
        <input type="radio" name="radio" value="3">撤销
    </div>
    <div class="cancle-area" style="margin-left: 500px;display:none">
        <span>撤销原因&nbsp;&nbsp;</span>
        <textarea id="cancleReason" style="height: 100px;width: 600px; resize:none"></textarea>
    </div>
    <div class="transfer-area" style="margin-left: 500px;display: none">
        <span>转单原因&nbsp;&nbsp;</span>
        <textarea id="transferReason" style="height: 100px;width: 600px; resize:none"></textarea>
        <div style="margin-top:10px;">转单给&nbsp;&nbsp;&nbsp;&nbsp;
            <select style="margin-left:5px;" id="person"></select>
        </div>

    </div>
    <div>
        <input type="hidden" value="" id="requestReason" name="requestReason">
        <input type="hidden" value="" id="result" name="result">
        <input type="hidden" value="" id="switch_person" name="switch_person">
    </div>
</div>