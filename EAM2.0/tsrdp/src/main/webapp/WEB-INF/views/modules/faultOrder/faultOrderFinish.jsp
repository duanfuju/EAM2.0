


<script type="text/javascript">
    var ligerform;

    $(function () {
        try{
            //创建表单结构
            ligerform = $("#form1").ligerForm({
                space : 50,
                labelWidth : 120 ,
                inputWidth : 200,
                fields: [
                    {display: "操作", name: "result", newline: true, type: "radiolist",group:"操作信息"},
                    {
                        display: "原因",
                        name: "reason",
                        newline: true,
                        type: "textarea",
                        width: 200
//                        validate: {required: true}
                    }
                ]
            });

            var dataGrid = [{ id: 0, name: '反馈' },{ id: 1, name: '挂起' }];
            $("#result").ligerRadioList({
                data: dataGrid,
                textField: 'name',
                value:'0'
            });

            //接单时不需要填写原因
            ligerform.setVisible('reason', false);
            $("input:radio[name='result']").change(function (){
                var val = liger.get("result").getValue();
                if('0' == val){
                    ligerform.setVisible('result', false);
                }else{
                    ligerform.setVisible('result', true);
                }
            });

        }catch (e){
            console.error(e);
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
<div id="baseForm" class="liger-form"></div>
<div id="form1" class="liger-form">


</div>