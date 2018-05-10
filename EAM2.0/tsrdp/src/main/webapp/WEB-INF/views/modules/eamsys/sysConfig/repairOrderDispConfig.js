
define(["text!modules/eamsys/sysConfig/repairOrderDispConfig.html", "text!modules/eamsys/sysConfig/repairOrderDispConfig.css"], function (htmlTemp, cssTemp) {
    var ro_disp_type;
    var module = {
        init: function () {
            $(".content-wrapper").html(htmlTemp);
            $(".css-attr").html(cssTemp);
            this.render();
        },
        render: function () {
            this.editBtn();
            this.initSetting();
        },
        editBtn:function() {
            //按钮的功能
            this.initStyle();
        },
        initSetting:function () {
            //加载时判断当前选项
            common.callAjax('post',false,ctx + "/repairOrderDisp/getRepairOrderConfig","json",null,function(data){
                if(data.data==undefined || data.data[0]=='' || data.data[0]=='1'){
                    $("#spana").trigger("click");
                }else if(data.data[0]=='2'){
                    $("#spanb").trigger("click");
                }else if(data.data[0]=='3'){
                    $("#spanc").trigger("click");
                    $("#max_orders").val(data.data[1]);
                    $("#max_timeout").val(data.data[2]);
                }
            });
        },
        initStyle:function () {
            var ccc = 1;
            $("#content_right_span13").click(function() {
                if(ccc%2==1){
                    $("#content_right_span2").show();
                    $("#content_xx").hide();
                    $("#content_yy").show();
                }
                if(ccc%2==0){
                    $("#content_right_span2").hide();
                    $('#content_xx').show();
                    $('#content_yy').hide();
                }
                ccc++;
            });

            var ddd = 1;
            $("#content_right_sapn5").click(function() {
                if(ddd%2==1){
                    $("#content_right_span4").show();
                    $("#content_xxx").hide();
                    $("#content_yyy").show();
                }
                if(ddd%2==0){
                    $("#content_right_span4").hide();
                    $('#content_xxx').show();
                    $('#content_yyy').hide();
                }
                ddd++;
            });

            var aaa = 1;
            $("#spana").click(function() {
                ro_disp_type=1;
                $('#spana img').attr('src', '/resource/img/circle_maintain.png');
                $('#spana img').css({opacity:1});
                $("#spanb img ").attr('src', '/resource/img/circle_scrap.png');
                $("#spanb img ").css({opacity:0.2});
                $("#spanc img ").attr('src', '/resource/img/circle_scrap.png');
                $("#spanc img ").css({opacity:0.2});

            });
            $("#spanb").click(function() {
                ro_disp_type=2;
                $('#spanb img').attr('src', '/resource/img/circle_maintain.png');
                $('#spanb img').css({opacity:1});
                $("#spana img ").attr('src', '/resource/img/circle_scrap.png');
                $("#spana img ").css({opacity:0.2});
                $("#spanc img ").attr('src', '/resource/img/circle_scrap.png');
                $("#spanc img ").css({opacity:0.2});
            });
            $("#spanc").click(function() {
                ro_disp_type=3;
                $('#spanc img').attr('src', '/resource/img/circle_maintain.png');
                $('#spanc img').css({opacity:1});
                $("#spana img ").attr('src', '/resource/img/circle_scrap.png');
                $("#spana img ").css({opacity:0.2});
                $("#spanb img ").attr('src', '/resource/img/circle_scrap.png');
                $("#spanb img ").css({opacity:0.2});
            });

            $(".bottom_two").click(function(){
                ro_disp_type=1;
                $('#spana img').attr('src', '/resource/img/circle_maintain.png');
                $('#spana img').css({opacity:1});
                $("#spanb img ").attr('src', '/resource/img/circle_scrap.png');
                $("#spanb img ").css({opacity:0.2});
                $("#spanc img ").attr('src', '/resource/img/circle_scrap.png');
                $("#spanc img ").css({opacity:0.2});

            });
            $(".bottom_first").on("click",function(){
                var max_orders = null;
                var max_timeout = null;
                if(ro_disp_type==3){
                    max_orders = $("#max_orders").val();
                    max_timeout =$("#max_timeout").val();
                }

                var obj = new Object();
                obj.disp_type = ro_disp_type;
                obj.max_orders = max_orders;
                obj.max_timeout = max_timeout;
                common.callAjax('post',false,ctx + "/repairOrderDisp/saveRepairOrderConfig","json",obj,function(data){
                    layer.msg(data.msg,{time: 1000,icon:1});
                });
            });

        }


    }
    return module;
});












