define(["text!modules/eamsys/sysConfig/inspectOrderDispConfig.html", "text!modules/eamsys/sysConfig/inspectOrderDispConfig.css"], function (htmlTemp, cssTemp) {
    var disp_type;
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
            $(".bottom_first").on("click",function(){
                var obj = new Object();
                obj.config_key = 'INSPECT_ORDER_DISP_TYPE';
                obj.config_value = disp_type;
                common.callAjax('post',false,ctx + "/sysConfig/updateConfigByName","json",obj,function(data){
                    layer.msg(data.msg ,{time: 1000, icon:1});
                });
            });

        },
            initSetting:function () {
                //加载时判断当前选项
                var obj = new Object();
                obj.config_key = 'INSPECT_ORDER_DISP_TYPE';
                common.callAjax('post',false,ctx + "/sysConfig/getConfigByName","json",obj,function(data){
                    if(data.data==undefined || data.data=='' || data.data=='1'){
                        $("#spana").trigger("click");
                    }else {
                        $("#spanb").trigger("click");
                    }
                });
            },
        initStyle:function () {
            var ccc = 1;
            $("#content_right_sapn3").click(function() {
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
            $("#spana").click(function() {
                disp_type=1;
                $('#spana img').attr('src', '/resource/img/circle_maintain.png');
                $('#spana img').css({opacity:1});
                $("#spanb img ").attr('src', '/resource/img/circle_scrap.png');
                $("#spanb img ").css({opacity:0.2});
                $("#spanc img ").attr('src', '/resource/img/circle_scrap.png');
                $("#spanc img ").css({opacity:0.2});

            });
            $("#spanb").click(function() {
                disp_type=2;
                $('#spanb img').attr('src', '/resource/img/circle_maintain.png');
                $('#spanb img').css({opacity:1});
                $("#spana img ").attr('src', '/resource/img/circle_scrap.png');
                $("#spana img ").css({opacity:0.2});
                $("#spanc img ").attr('src', '/resource/img/circle_scrap.png');
                $("#spanc img ").css({opacity:0.2});
            });
            $(".bottom_two").click(function(){
                disp_type=1;
                $('#spana img').attr('src', '/resource/img/circle_maintain.png');
                $('#spana img').css({opacity:1});
                $("#spanb img ").attr('src', '/resource/img/circle_scrap.png');
                $("#spanb img ").css({opacity:0.2});
                $("#spanc img ").attr('src', '/resource/img/circle_scrap.png');
                $("#spanc img ").css({opacity:0.2});

            })
        }
    }
    return module;
});











