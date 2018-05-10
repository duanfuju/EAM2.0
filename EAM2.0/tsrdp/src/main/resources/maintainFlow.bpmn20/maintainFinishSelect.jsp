<script type="text/javascript">
    var requestReason;   // 请求原因
    var result = '5';           //操作方式
    $(function () {
        // 给申请原因赋值
        // 挂起原因
        $('#hangUpReason').bind('input propertychange','textarea',function(){
            requestReason = $("#hangUpReason").val();
            $("input[name='requestReason']").val(requestReason);
        });

        $("input:radio[name='radio']").change(function (){
            debugger;
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
                $(".hangup-area").hide();
            } else if(value == '4') {
                $(".hangup-area").show();
            }
        });
        $("#result").val(result);
    });
</script>


<div class="control-select">
    <div class="controls" style="margin-left: 60px;">
        <span>操作选择&nbsp;&nbsp;</span>
        <input type="radio" name="radio" value="1" checked>反馈
        <input type="radio" name="radio" value="4">挂起
    </div>
    <div class="hangup-area" style="margin-left: 500px;display:none">
        <span>挂起原因&nbsp;&nbsp;</span>
        <textarea id="hangUpReason" style="height: 100px;width: 600px; resize:none"></textarea>
    </div>
    <div>
        <input type="hidden" value="" id="requestReason" name="requestReason">
        <input type="hidden" value="" id="result" name="result">
    </div>
</div>