define(["text!modules/eamsys/sysConfig/statisticsLevelConfig.html", "text!modules/eamsys/sysConfig/statisticsLevelConfig.css"], function (htmlTemp, cssTemp) {
    var dev_stat_level;
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
                obj.config_key = 'STATISTICS_LEVEL';
                obj.config_value = dev_stat_level;
                common.callAjax('post',false,ctx + "/sysConfig/updateConfigByName","json",obj,function(data){
                    layer.msg(data.msg,{time: 1000,icon:1});
                });
            });
            //重置
            $('.bottom_two').on('click',function(){
                dev_stat_level=1;
                $("#spana img").attr('src', '/resource/img/circle_maintain.png');
                $("#spana img").css({opacity:1});
                $("#spanb img ").attr('src', '/resource/img/circle_scrap.png');
                $("#spanb img ").css({opacity:0.2});
                $("#spanc img ").attr('src', '/resource/img/circle_scrap.png');
                $("#spanc img ").css({opacity:0.2});
            });
        },
            initSetting:function () {
                //加载时判断当前选项
                var obj = new Object();
                obj.config_key = 'STATISTICS_LEVEL';
                common.callAjax('post',false,ctx + "/sysConfig/getConfigByName","json",obj,function(data){
                    if(data.data==undefined || data.data=='' || data.data=='1'){
                        $("#spana").trigger("click");
                    }else {
                        $("#spanb").trigger("click");
                    }
                });
            },
        initStyle:function () {
            $("#spana").bind("click",function(){
                dev_stat_level=1;
                $("#spana img").attr('src', '/resource/img/circle_maintain.png');
                $("#spana img").css({opacity:1});
                $("#spanb img ").attr('src', '/resource/img/circle_scrap.png');
                $("#spanb img ").css({opacity:0.2});
                $("#spanc img ").attr('src', '/resource/img/circle_scrap.png');
                $("#spanc img ").css({opacity:0.2});
            });

            $("#spanb").bind("click",function(){
                dev_stat_level=2;
                $("#spanb img").attr('src', '/resource/img/circle_maintain.png');
                $("#spanb img").css({opacity:1});
                $("#spana img ").attr('src', '/resource/img/circle_scrap.png');
                $("#spana img ").css({opacity:0.2});
                $("#spanc img ").attr('src', '/resource/img/circle_scrap.png');
                $("#spanc img ").css({opacity:0.2});
            });
        }

    }
    return module;
});











