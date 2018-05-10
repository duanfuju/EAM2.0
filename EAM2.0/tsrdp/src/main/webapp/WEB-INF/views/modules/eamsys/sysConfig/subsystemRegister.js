/**
 * Created by suven on 2017/11/1.
 */



define(["text!modules/eamsys/sysConfig/subsystemRegister.html", "text!modules/eamsys/sysConfig/subsystemRegister.css","liger-all"], function (htmlTemp, cssTemp) {

    var module = {
        init:function(){
            $(".content-wrapper").html(htmlTemp);
            $(".css-attr").html(cssTemp);

            this.render();
        },
        render: function () {
            this.editBtn();
        },
        editBtn:function() {


            $(".submitBtn").on("click",function(){
                var ssohost=$("#ssohost").val();
                var ssohost_port=$("#ssohost_port").val();
                console.log(ssohost);
                $.ajax({
                    url:ctx+"/eam/register/getJsonString",
                    type:"post",
                    dataType : "text",
                    async:false,
                    success:function(data){
                        debugger;
                        $.ajax({
                            url:"http://"+ssohost+":"+ssohost_port+"/onesite/v1.00/api/register",
                            type:"post",
                            dataType : "json",
                            data:{info:data},
                            async:false,
                            success:function(da){
                                if(!da.success){
                                    alert("注册失败");
                                }
                                if(da.success){
                                    alert("注册成功");}
                            }
                        })



                    }
                })
            });
            $(".reserBtn").on("click",function(){
                $("#ssohost").val(" ");

            });
        }
    }
    return module;
});

