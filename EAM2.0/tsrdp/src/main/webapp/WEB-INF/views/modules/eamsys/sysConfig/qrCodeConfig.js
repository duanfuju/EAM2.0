define(["text!modules/eamsys/sysConfig/qrCodeConfig.html", "text!modules/eamsys/sysConfig/qrCodeConfig.css"], function (htmlTemp, cssTemp) {
    var module = {
        init:function(){
            $(".content-wrapper").html(htmlTemp);
            $(".css-attr").html(cssTemp);

            this.render();
        },
        render: function () {
            this.editBtn();
            this.initEwmPic();
        },
        editBtn:function() {
            //按钮的功能
            $("#uploadbtn1").on("click", function () {
               new BrowseServer($("#xFilePath1"),$("#logo1"));
            });
            $("#uploadbtn2").on("click", function () {
                var finder = new CKFinder();
                finder.selectActionFunction = function(fileUrl){
                    $("#xFilePath2").val(fileUrl);
                    createEwmPic(fileUrl);//'201706288083'
                } //当选中图片时执行的函数
                finder.popup();//调用窗口
            });
            $("#xmmc").keyup(function(){
                var xmmc = $("#xmmc").val();
                if(xmmc == undefined || xmmc == ''){
                    $(".xm_name").css("display","none");
                    $("#xmmc_display").html('');
                    return;
                }
                $(".xm_name").css("display","block");
                $("#xmmc_display").html(xmmc);
            });

            $(".submitBtn").on("click",function(){
                var obj = new Object();
                obj.top_logo = $("#xFilePath1").val();
                obj.title = $("#xmmc").val();
                obj.bot_logo = $("#xFilePath2").val();
                // if(obj.title.length>12){
                //     alert("项目名称过长！");
                //     return;
                // }
                //保存配置项
                common.callAjax('post',false,ctx + "/QRCode/saveQRCodeConfig","json",obj,function(data){
                    layer.msg(data.msg,{time:1000,icon:1});
                });
            });
            $(".reserBtn").on("click",function(){
                $("#xFilePath1").val(" ");
                $("#xFilePath2").val(" ");
                $("#xmmc").val(" ");
            });
        },

        initEwmPic: function () {
            try{
                var obj = new Object();
                common.callAjax('post',false,ctx + "/QRCode/getQRCodeConfig","json",obj,function(data){
                    var result = data;
                    if(result.result!=null){
                        $('#logo1').attr( 'src', result.result[0]);
                        $("#xFilePath1").val(result.result[0]);
                        var title = result.result[1];
                        var picUrl2 = result.result[2];
                        $("#xFilePath2").val(picUrl2);
                        createEwmPic(picUrl2);
                        $("#xmmc").val(title);
                        $("#xmmc").keyup();
                    }
                });


            }catch (e){
                console.error(e);
            }
        }
    }
    return module;
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

//生成二维码,返回二维码图片地址
function createEwmPic(picUrl){
    var xhr = new XMLHttpRequest();
    xhr.open("get", ctx + "/QRCode/createQRCode?demo=true&&picUrl="+picUrl, true);
    xhr.responseType = "blob";
    xhr.onload = function() {
        if (this.status == 200) {
            var blob = this.response;
            $("#logo2").attr("src",window.URL.createObjectURL(blob));
        }else if(this.status == 500){
            layer.msg("系统错误");
        }
    }
    xhr.send();
}



function queryConfigByName(){
    var obj = new Object();
    var result = null;
    common.callAjax('post',false,ctx + "/QRCode/getQRCodeConfig","json",obj,function(data){
        result = data;
    });
    return result;
}