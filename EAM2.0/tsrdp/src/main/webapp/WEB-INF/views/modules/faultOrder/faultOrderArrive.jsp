
<script type="text/javascript">
    ligerform = $("#form1").ligerForm({
        space : 40,
        labelWidth : 120 ,
        inputWidth : 200,
        fields: [
            {group:"操作信息"}
        ]
    });

    $(function () {
        try{
            //按钮的功能
            $("#uploadbtn").on("click", function () {
                new BrowseServer($("#xFilePath"),$("#logo"));
            });

        }catch (e){
            console.error(e);
        }

    });

    //初始化ckeditor
    function BrowseServer(pathElement,picElement) {
        var finder = new CKFinder();
        //finder.basePath = '../'; //此路径为CKFinder的安装路径，默认为 (default = "/ckfinder/").

        //文件选中时执行
        finder.selectActionFunction = function(fileUrl){
            pathElement.val(fileUrl);
            picElement.attr("src",""+fileUrl+"");
        }
        finder.popup();//调用窗口
    }

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
<div id="baseForm" class="liger-form"></div>
<div id="form1" class="liger-form">
    <div class="Ling1"><img id="logo" class="logo" src="" style="width:320px;margin: 5px"></div>
    <div class="Kang1" style="white-space:nowrap">
        <span style="color: red">*</span><span>图片</span>
        <input type="text" id="xFilePath" name="xFilePath"/>
        <input type="button" id="uploadbtn" value="上 传"  />
    </div>

</div>
