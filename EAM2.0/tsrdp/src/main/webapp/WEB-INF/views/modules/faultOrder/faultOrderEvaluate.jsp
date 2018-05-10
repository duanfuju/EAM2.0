<style>
    .demo .target-demo {
        display: inline-block; vertical-align: middle
    }
    .demo div.hint {
        border-radius: 5px; color: #333;margin-top:12px;
        display: inline-block; height: 27px; font-size: 1.0em;
        text-align: center; width: 135px; vertical-align: middle
    }

</style>
<div id="baseForm" class="liger-form"></div>
<div>
    <div class="demo" style=" margin:10px;">
        工作态度：<div id="function-demo1" class="target-demo"></div><div id="function-hint1" class="hint"></div><br>
        个人表现：<div id="function-demo2" class="target-demo"></div><div id="function-hint2" class="hint"></div><br>
        仪容仪表：<div id="function-demo3" class="target-demo"></div><div id="function-hint3" class="hint"></div><br>
        身高体重：<div id="function-demo4" class="target-demo"></div><div id="function-hint4" class="hint"></div><br>
        分值：<div id="avgScoreShow" class="hint"></div><br>
        <input type="hidden" id="avgScore" name="avgScore">
        <div class="target-demo">
            评价说明：<textarea name="evaluate" rows = "8" style="width: 300px;border: 1px solid #ccc;"></textarea>
        </div><br>
    </div>
</div>
<script type="text/javascript">
    $(function() {
        $.fn.raty.defaults.path = 'lib/img';
        initRaty($('#function-demo1'),'#function-hint1');
        initRaty($('#function-demo2'),'#function-hint2');
        initRaty($('#function-demo3'),'#function-hint3');
        initRaty($('#function-demo4'),'#function-hint4');

        function initRaty(ratyElem,targetElem) {
            ratyElem.raty({
                number: 5,//多少个星星设置
                score: 0,//初始值是设置
                hints: ['很差', '较差', '一般', '良好', '很好'],
                targetType: 'number',//类型选择，number是数字值，hint，是设置的数组值
                path: '/resource/img/star',
                cancelOff: 'cancel-off-big.png',
                cancelOn: 'cancel-on-big.png',
                size: 24,
                starHalf: 'star-half-big.png',
                starOff: 'star-off-big.png',
                starOn: 'star-on-big.png',
                target: targetElem,
                cancel: false,
                targetKeep: true,
                precision: false,//是否包含小数
                click: function (score, evt) {
                    var s1 = $('#function-hint1').text()==''?'0':$('#function-hint1').text();
                    var s2 = $('#function-hint2').text()==''?'0':$('#function-hint2').text();
                    var s3 = $('#function-hint3').text()==''?'0':$('#function-hint3').text();
                    var s4 = $('#function-hint4').text()==''?'0':$('#function-hint4').text();
                    var avgScore = (parseInt(s1)+parseInt(s2)+parseInt(s3)+parseInt(s4))/4;

                    $('#avgScoreShow').text(avgScore.toFixed(1));
                    $('#avgScore').val(avgScore.toFixed(1));
//                    alert('ID: ' + $(this).attr('id') + "\nscore: " + score + "\nevent: " + evt.type);
                }
            });

        }

    });


</script>
<script>
    $(function () {
        try{
            //创建表单结构
            var baseligerForm = $("#baseForm").ligerForm({
                space : 40,
                labelWidth : 120 ,
                inputWidth : 200,
                fields: [
                    {display:"工单编码",editable:true,name:"order_code_display",type:"labelText",group: "基本信息"},
                    {display:"报修位置",editable:true,name:"order_location_display",type:"labelText",group: "基本信息"},
                    {display:"报修设备",editable:true,name:"order_device_display",type:"labelText",group: "基本信息"},
                    {display:"报修人员",editable:true,name:"order_notify_display",type:"labelText",group: "基本信息"},
                    {display:"报修时间",editable:true,name:"order_notify_time_display",type:"labelText",group: "基本信息"},
                    {display:"故障现象",editable:true,name:"appearance_display",type:"labelText",group: "基本信息"},
                    {display:"",editable:true,name:"operation_title",type:"labelText",group:"操作信息"}
                ]
            });
            debugger;
            var pa = new Object();
            pa.pstid = executionId;
            //查询工单基本信息
            common.callAjax('post',false,ctx + "/faultOrder/getOrderDetail","json",pa,function(data){
                debugger;
                if(data != null){
                    baseligerForm.getEditor('order_code_display').setValue(data.order_code);
                    baseligerForm.getEditor('appearance_display').setValue(data.notifier_appearance);
                    baseligerForm.getEditor('order_device_display').setValue(data.order_device.dev_name);
                    baseligerForm.getEditor('order_location_display').setValue(data.notifier_loc.loc_name);
                    baseligerForm.getEditor('order_notify_display').setValue(data.notifier);
                    baseligerForm.getEditor('order_notify_time_display').setValue(data.createDate);
                }
            });



        }catch (e){
            console.error(e);
        }

    });
</script>


</script>