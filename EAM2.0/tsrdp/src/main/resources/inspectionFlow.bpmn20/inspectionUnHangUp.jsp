<script type="text/javascript">
    var requestReason;   // 请求原因
    var result = '6';           //操作方式
    $(function () {
        $("#result").val(result);
    });
</script>


<div class="control-select">
    <div class="controls" style="margin-left: 60px;">
        <span>操作选择&nbsp;&nbsp;</span>
        <input type="radio" name="result" value="6" checked>解挂
    </div>
    <div>
        <input type="hidden" value="" id="result" name="result">
    </div>
</div>