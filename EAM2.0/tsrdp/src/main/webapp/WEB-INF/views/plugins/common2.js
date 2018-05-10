/**
 * Created by Shangod on 2016/9/13.
 */
/*
 * Created by Administrator on 2016/7/13.
 */
function Common() {
    this.init();
}

Common.prototype = {
    init: function () {
        this.render();
    },

    render: function () {
        //this.leftMenu();
        this.generalFunc();
        this.usergo();
        //点击header部分的列表对应的左侧菜单框是否弹出
        this.headerListgo();
        this.logo();
        this.leftMenu2();
        this.carousel();
        this.energyCollection();
        this.switchUl();
        //this.switchLi();
        this.editFavourite();
        this.addToUl();
        this.tableCheck();
        this.requireCheck();
        this.formBtn();
        this.phoneCheck();
        this.placeHolder();


        //进行表格跳转框输入值的验证
    },
  //获取指定时间格式，格式为 格式“yyyy-MM-dd HH:MM:SS”
    getFormatDate: function (_date) {
        var date = new Date(_date);
        var seperator1 = "-";
        var seperator2 = ":";
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        var hour=date.getHours();
        var min=date.getMinutes();
        var second=date.getSeconds();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        if (hour >= 0 && hour <= 9) {
        	hour = "0" + hour;
        }
        if (min >= 0 && min <= 9) {
        	min = "0" + min;
        }
        if (second >= 0 && second <= 9) {
        	second = "0" + second;
        }
        
        var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
                + " " + hour + seperator2 + min
                + seperator2 + second;
        return currentdate;
    },
    //增加平台通用功能
    generalFunc: function () {
        var that = this;
        //that.generalBase.autoSlide();
        that.generalBase.autoSlide();
        that.generalBase.scrollStyle();
    },
    generalBase: {
        //自动弹出的功能
        autoSlide: function () {
            $(document).on("click", function () {
                $(".autoSlide").hide(500);
            });
            $("#content").on("click", ".autoSlide,.showSlide", function (e) {
                e.stopPropagation();
            });
        },
        //定义平台的滚动条样式，使用时需要在滚动的元素上加class：scrollStyle，然后设置其css即可
        scrollStyle: function () {
            (function(){
                $('.scrollStyle',document).jScrollPane({
                    autoReinitialise: true,
                    mouseWheelSpeed: 20,
                    stickToBottom: true
                });
            })();

        }

    },

    //用户工具栏：点击管理员右边的箭头，控制下方登录框是否显示
    usergo: function () {
        var number = 1;
        $(".userId").on('click', function () {
            number = number * (-1);
            if (number > 0) {
                $(this).parent().removeClass('userDiv');
                $('.admin2').hide();
                $(this).removeClass('userMore');
            } else {
                $(this).parent().addClass('userDiv');
                $('.admin2').show();
                $(this).addClass('userMore');

            }

        })
    },

    //点击header部分的列表对应的左侧菜单框是否弹出
    headerListgo: function () {
        /*$('#header .breadCrumb_menu_hide').on('click',function(){
         $('.breadCrumb .breadCrumb_menu').hide(200);
         $("#navbar").animate({left: '-170px'});
         $('.breadCrumb_view span').html("首页");
         });
         $('#header .breadCrumb_menu_show').on('click',function(){
         $('.breadCrumb_view span').html("总览");
         $('.breadCrumb .breadCrumb_menu').show(200);
         var isActive = $('.breadCrumb_menu').hasClass('active');
         if(isActive==false){
         $('.breadCrumb_menu').addClass('active');
         $("#navbar").data("navbarFlag",1);
         }
         $("#navbar").animate({left: '0px'});
         //$('.navbar_hide').on('click',function(){
         //    $("#navbar").animate({left: '-170px'});
         //})
         });*/

    },


    //logo变化部分：面包屑按钮左侧菜单栏弹出
    logo: function () {
        //点击面包屑按钮左侧菜单栏弹出
        $("#navbar").data("navbarFlag", 0);
        $('#navbar_hide').on('click', function () {
            if ($("#navbar").data("navbarFlag") === 1) {
                $("#navbar").animate({left: '-180px'});
                //面包屑按钮所在的div背景颜色有变化
                $('.breadCrumb_menu').removeClass('active');
                $("#navbar").data("navbarFlag", 0);
            } else {
                $("#navbar").animate({left: '0px'});
                $('.breadCrumb_menu').addClass('active');
                $("#navbar").data("navbarFlag", 1);
            }
        });
        $(document).on("click", function () {
            $("#navbar").animate({left: '-180px'});
            $('.breadCrumb_menu').removeClass('active');
            $("#navbar").data("navbarFlag", 0);
        });
        $("#navbar,#navbar_hide,#header").on("click", function (e) {
            e.stopPropagation();
        });
    },


    //左侧菜单设置
    leftMenu2: function () {
        var that = this;
        //点击一级li
        $("#navbar .ul1>.l1").click(function () {

            $(this).siblings('li').find('.ul_childer').hide();
            $(this).toggleClass('active');
            var isActive = $(this).hasClass("active");
            //一级菜单箭头有变化

            if (isActive) {
                //相邻li去掉激活状态
                $(this).siblings('.l1').removeClass('active');
                //相邻li右边的箭头返回原状态，不旋转
                $(this).siblings('.l1').find(".iconfa.m8").removeClass('rotate');
                $(this).find(".iconfa.m8").addClass("rotate");
                $(this).find('.ul_childer').show();
                //点击当前一级的li所在区域背景颜色有变化，同时鼠标滑过a时背景颜色有变化
                $(this).css('background', '#14C7BD');
                $(this).siblings('.l1').css('background', '');
                $('#navbar .ul1>.l1 a').mouseover(function () {
                    $(this).css('background', '#4BDBD3');
                    $(this).siblings('a').css('background', '');
                });
                $('#navbar .ul1>.l1 a').mouseout(function () {
                    $(this).css('background', '');
                });

            } else {
                $(this).find(".iconfa.m8").removeClass("rotate");
                $(this).find('.ul_childer').hide();
                $(this).css('background', "");

            }

        });
        //为了实现点击父菜单可以收缩子菜单的功能
        $("#navbar .ul1 .l1,#navbar .ul1 .l2").on("click", function () {
            $(this).siblings('li').removeClass("mine").find("div[class^='ul_childer']").hide();
            $(this).toggleClass("mine");
            if (!$(this).hasClass("mine")) {
                $(this).children("div[class^='ul_childer']").hide();
            }
        });

        //点击二级li
        /*        $('#navbar .ul_childer .ul2>.l2').click(function(e){

         $('.content').load($(this).find("a").attr("data-link"),function(){
         /!*if($(this).attr("data-link")=="../pages/welcome.html"){
         var common = new Common();
         common.initPie();
         }*!/
         alert($(this).find("a").attr("data-link"));

         });
         e.stopPropagation();
         $(this).parent().removeClass("active");
         $(this).siblings('li').find('.ul_childer2').hide();
         $(this).toggleClass("active");

         var isActive = $(this).hasClass("active");
         if(isActive){
         $(this).siblings('.l2').removeClass('active');
         $(this).find(".iconfa2").addClass("openMore");
         $(this).find('.ul_childer2').show();

         }else{
         $(this).find(".iconfa2").removeClass("openMore");
         $(this).find('.ul_childer2').hide();
         }

         });*/

        /*
         * 给左侧导航菜单设置点击后样式切换，方便查看功能
         * */
        $("#navbar li.l1 li").each(function () {
            $(this).on("click", function () {
                $("#navbar li.l1 li").removeClass("userClicked");
                $(this).addClass("userClicked");
            });
        });

        $('#navbar li').each(function () {
            $(this).on("click", function (e) {
                if ($(this).children().length == 1) {
                    var htmlLink = $(this).attr("data-link");
                    //var jsLink="../js/"+String(htmlLink).replace(/\.[^.\/]+$/, "")+".js";
                    //var tttt="MaterialType";
                    /*$('#content').load(htmlLink,function(){
                     $LAB.script(jsLink).wait(function(){
                     var control;
                     var model=String(htmlLink).replace(/\.[^.\/]+$/, "").substr(13);
                     switch( model ){
                     case "The Speedster":
                     control = new Speedster();
                     break;
                     case "materialType":
                     control = new MaterialType();
                     break;
                     case "materialInfo":
                     control = new MaterialInfo();
                     break;
                     }
                     //control.init();
                     });
                     //$LAB.script(jsLink).wait();

                     });*/
                    $('#content').load(htmlLink, function () {
                        that.initDate();
                        //that.tablePop();
                        //统一修改二维码等信息；
                        var config = new Config();
                        config.init();
                        
                        that.generalBase.scrollStyle();
                    });

                    /*
                     * 每点击一次，改变一次sessionStorage中的值
                     * */
                    sessionStorage.setItem("currentLink", htmlLink);

                }

                e.stopPropagation();
                $(this).parent().removeClass("active");
                //$(this).siblings('li').find('.ul_childer2').hide();
                $(this).toggleClass("active");

                var isActive = $(this).hasClass("active");
                if (isActive) {
                    $(this).siblings('.l2').removeClass('active');
                    $(this).find(".iconfa2").addClass("openMore");
                    $(this).find('.ul_childer2').show();

                } else {
                    $(this).find(".iconfa2").removeClass("openMore");
                    //$(this).find('.ul_childer2').hide();
                }
            })
        });
        that.initDate();
        that.tablePop();
        that.generalBase.scrollStyle();


    },


    carousel: function () {
        var $container = $('.energy_knowledge'),
            $ul = $('ul', $container),
            $lis = $('li', $ul);

        if ($lis.length * $lis.outerWidth(true) <= $container.width()) {
            return;
        }
        $ul.append($('li', $ul).clone());
        $lis = $('li', $ul);
        $ul.width($lis.length * $lis.outerWidth(true));
        var scroll = function (speed) {
            var left = parseInt($ul.css('margin-left'));

            if (left <= -$ul.width() / 2) {
                $ul.css('left', 0);
            } else if (left >= 0) {
                $ul.css('left', -$ul.width() / 2);
            }
        };
    },
    energyCollection: function () {
        $('.favoritesBtn').click(function () {
            $('.collDiv').slideDown();
        })
        $('.collCancel').click(function () {
            $('.collDiv').slideUp();
            if ($(this).hasClass("press")) {
                $(this).removeClass("press");
            }
            console.log($(this).html() + $(this).hasClass("press"))
            $('.favorites').removeClass('press');
            console.log("common")
        })
    },
    switchUl: function () {
        $('.alarm_tab>ul.alarmList>li').click(function () {
            $(this).addClass('hit').siblings().removeClass('hit');
            $('.panes div.pane:eq(' + $(this).index() + ')').show().siblings().hide();
            $(window).trigger('resize');
        })
    },


    //收藏
    editFavourite: function () {
        var comFavouriteItems = $(".favoriteItem");
        var btns = $("<a class='edit_favouriteBtn' href='javascript:void(0)'>编辑收藏</a>");

        function completedNode() {
            $.each(comFavouriteItems, function () {
                var Str = $("<p>" + $(this).children("input").val() + "</p>");
                $(this).html(Str);
            })
            $(".button_li").html(btns);
        }

        /*选中收藏节点样式*/
        $(document).delegate('.favoriteItem p', 'click', function () {
            if (!$(this).hasClass("region")) {
                $(this).addClass("region")
            } else {
                $(this).removeClass("region")
            }
        });
        //编辑收藏
        $(document).delegate('.edit_favouriteBtn', 'click', function () {
            var favouriteItems = $(".favoriteItem");
            $.each(favouriteItems, function () {
                var Str = $("<input type='text' value='" + $(this).children("p").text() + "'><span>" + $(this).children("p").text() + "</span><img class='remove_btn' src='../img/remove.png'>");
                $(this).html(Str);
            });
            var btnStr = $("<a class='complete_btn' href='javascript:void(0)'>完成</a><a class='cancel_btn' href='javascript:void(0)'>取消</a>")
            $(".button_li").html(btnStr);
        });

        /*完成收藏*/
        $(document).delegate('.complete_btn', 'click', function () {
            completedNode();
            comFavouriteItems = $(".favoriteItem");
        });
        /*取消操作*/
        $(document).delegate('.cancel_btn', 'click', function () {
            var iteItems = $(".favoriteItem");
            $.each(iteItems, function () {
                var Str = $("<p>" + $(this).children("span").text() + "</p>");
                $(this).html(Str);
            })
            $(".button_li").html(btns);
        });
        //删除收藏节点
        $(document).delegate('.favoriteItem .remove_btn', 'click', function () {
            $(this).parent(".favoriteItem").remove();
            comFavouriteItems = $(".favoriteItem");
            if (comFavouriteItems.size() == 0) {
                $(".collection").remove();
            }
        });
        /*添加收藏节点*/
        $(document).delegate('.okButton', 'click', function () {
            if (comFavouriteItems.size() == 0) {
                $(".top_operContainer .top_searchContainer").before("<ul class='clearfix collection'><li><img src='../img/collection.png'></li><li class='button_li'><a class='edit_favouriteBtn' href='javascript:void(0)'>编辑收藏</a> </li></ul>")
            }
            var nodeName = "收藏名称";
            var inputVal = $(".collDivIput .collInput").val();
            if (inputVal !== "") {
                nodeName = inputVal;
            }
            if ($(".collection .favoriteItem").size() < 5) {
                if ($(".button_li a").size() == 2) {
                    completedNode();
                    $(this).parents(".top_searchContainer").siblings(".collection").children(".button_li").before("<li class='favoriteItem'><p>" + nodeName + "</p></li>");
                } else {
                    $(this).parents(".top_searchContainer").siblings(".collection").children(".button_li").before("<li class='favoriteItem'><p>" + nodeName + "</p></li>");
                }
            }
            comFavouriteItems = $(".favoriteItem");
        })
    },
    addToUl: function () {
        $('#addUl').on('click', function () {
            $('.add_li').show();
            var $val = $("input").filter(".valueAdd");
            console.debug($val.length);
            var $input;
            if ($val.length > 1) {
                for (var i = 0; i < $val.length; i++) {
                    $input = $($val[i - 1]).val() + '至' + $($val[i]).val();
                }
            } else {
                $input = $($val).val();
            }
            //var $input = $(this).prev().prev().val();
            var $str;
            $str = '<li><label>' + $input + '</label><img src="../img/remove.png" class="imgLi"></li>';
            var $span = '<span class="clearSpan">清空</span>';
            if ($('.addUl').find('li').length > 9) {
                return false;
            } else {
                $('.addUl').append($str);
                $(this).prev().prev().val('');
                if ($('.clearDiv').find('span').length > 0) {
                    return false;
                } else {
                    $('.addUl').after($span);
                }

            }
        });
        $(document).delegate('.imgLi', 'click', function () {
            $(this).parent().remove();
            if (parseInt($('.addUl').find('li').length) <= 0) {
                $('.add_li').hide();
            }
        });
        $(document).delegate('.clearSpan', 'click', function () {
            console.debug('addNone')
            $(this).prev().find('li').remove();
            $('.addNone').hide();
            $('#addUl').prev().prev().val('');
        });


    },
    tableCheck: function () {
        $(document).delegate("#redirect", "keyup", function () {
            var result = /^\d*$/.test($(this).val());
            if (!result) {
                $(this).val("");
            }
        });

    },
    /*******以下是扩展20161108以后*******/
    //    定义页面统一的表单提交和清空按钮功能
    formBtn: function () {
        //清空按钮的效果
        $("#content").on("click", "*[id^='resetBtn']", function () {
            $(this).parents("form").find("input,textarea").val("");
        });
        //提交，确定按钮的功能
        $("#content").on("click", "*[id^='submitBtn']", function () {
            var n = 0;
            $(this).parents("form").find(".requireCheck").each(function () {
                $(this).val() == "" && n++;
            });
            if (n == 0) {
                $(this).parents("form").submit();
            }
        });
    },
    //对表单的必填性进行验证，必填性input表单需要有class=requireCheck
    requireCheck: function () {
        //判断是否填写，并进行提示
        $("#content").on("blur", ".requireCheck", function () {
            $(this).next() && $(this).next(".redRequire").remove();
            if ($(this).val() == "") {
                $(this).removeClass("notCheck");
                $('<span class="redRequire" style="color:#f00;color: #f00;position: static;width: auto;margin-left: 16px;">必填</span>').insertAfter($(this));
            }
        });
        $("#content").on("click", "#submitBtn", function (event) {
            $(this).parents("form").unbind("submit");
        });

    },
    //电话验证
    phoneCheck: function () {
        $("#content").on("blur", ".phoneCheck", function () {
            //电话验证支持座机和手机验证
            var reg = /^((\d{3,4}-)?\d{7,8}|(?:1[13584][0-9]{9}))$/;
            $(this).next() && $(this).next().remove();
            if (!reg.exec($(this).val())) {
                $(this).removeClass("notCheck");
                $('<span style="color:#f00;color: #f00;position: static;width: auto;margin-left: 16px;">格式错误</span>').insertAfter($(this));
            }
        });
    },
    //使所有的浏览器都默认支持placeholder属性
    placeHolder: function () {
        $(function () {

            //判断浏览器是否支持placeholder属性
            supportPlaceholder = 'placeholder'in document.createElement('input'),

                placeholder = function (input) {

                    var text = input.attr('placeholder'),
                        defaultValue = input.defaultValue;

                    if (!defaultValue) {

                        input.val(text).addClass("phcolor");
                    }

                    input.focus(function () {

                        if (input.val() == text) {

                            $(this).val("");
                        }
                    });


                    input.blur(function () {

                        if (input.val() == "") {

                            $(this).val(text).addClass("phcolor");
                        }
                    });

                    //输入的字符不为灰色
                    input.keydown(function () {

                        $(this).removeClass("phcolor");
                    });
                };

            //当浏览器不支持placeholder属性时，调用placeholder函数
            if (!supportPlaceholder) {

                $('input').each(function () {

                    text = $(this).attr("placeholder");

                    if ($(this).attr("type") == "text") {

                        placeholder($(this));
                    }
                });
            }

        });


    },

    //初始化时间
    initDate: function () {
        var date = new Date();
        var orginM = date.getMonth() + 1;
        var today = date.getFullYear() + "-" + (orginM < 10 ? ("0" + orginM) : orginM) + "-" + (date.getDate() < 10 ? ("0" + date.getDate()) : date.getDate());
        var month = date.getFullYear() + "-" + (orginM < 10 ? ("0" + orginM) : orginM);
        var year = date.getFullYear();
        $(".yearInit").val(year);     //年的初始化
        $(".monthInit").val(month);    //月的初始化
        $(".dateInit").val(today);     //日期的初始化
        $(".dateStartInit").val(month + "-01");      //当月第一天的初始化

        $(".yearInit").attr("data-date", year);     //年的初始化
        $(".monthInit").attr("data-date", month);    //月的初始化
        $(".dateInit").attr("data-date", today);     //日期的初始化
        $(".dateStartInit").attr("data-date", month + "-01");      //当月第一天的初始化
    },

    //省略内容弹窗
    tablePop: function () {
        /*
         * 悬浮溢出文本的弹窗效果
         * */
        $(document).on("mouseover", ".dataTable td", function (e) {
            var xPos = $(this).offset().left - 20;
            var yPos = $(this).offset().top + 30;
            if (this.clientWidth < this.scrollWidth) {
                console.log("hello");
                var strHTML = "<div id=\"detailPopBox\" style=\"top:" + (yPos) + "px;left:" + (xPos) + "px;\">" + $(this).html() + "</div>";
                $(document.body).append(strHTML);
            }
        }).on("mouseout", ".dataTable td", function () {
            $(document.body).find("#detailPopBox").remove();
        });

        $(document).on("mouseover", ".tablePop", function (e) {
            var xPos = $(this).offset().left + 10;
            var yPos = $(this).offset().top + 30;
            if (this.clientWidth < this.scrollWidth) {
                var strHTML = "<div id=\"detailPopBox\" style=\"top:" + (yPos) + "px;left:" + (xPos) + "px;\">" + $(this).html() + "</div>";
                $(document.body).append(strHTML);
            }
        }).on("mouseout", ".tablePop", function () {
            $(document.body).find("#detailPopBox").remove();
        });
    }

    /*tableCheck:function(){
     $(document).delegate("#redirect","keyup",function(){
     var result=/^\d*$/.test($(this).val());
     if(!result){
     $(this).val("");
     }
     });
     /!* $("#content").on("keyup","#main #redirect",function(){
     var result=/^\d*$/.test($(this).val());
     if(!result){
     $(this).val("");
     }
     });*!/
     },
     /!*******以下是扩展20161108以后*******!/
     //    定义页面统一的表单提交和清空按钮功能
     formBtn:function(){
     //清空按钮的效果
     $("#content").on("click","*[id^='resetBtn']",function(){
     $(this).parents("form").find("input,textarea").val("");
     });
     //提交，确定按钮的功能
     $("#content").on("click","*[id^='submitBtn']",function(){
     var n=0;
     $(this).parents("form").find(".requireCheck").each(function(){
     $(this).val()==""&&n++;
     });
     if(n==0){
     $(this).parents("form").submit();
     }
     });
     },
     //对表单的必填性进行验证，必填性input表单需要有class=requireCheck
     requireCheck:function(){
     //判断是否填写，并进行提示
     $("#content").on("blur",".requireCheck",function(){
     $(this).next()&&$(this).next(".redRequire").remove();
     if($(this).val()==""){
     $(this).removeClass("notCheck");
     $('<span class="redRequire" style="color:#f00;color: #f00;position: static;width: auto;margin-left: 16px;">必填</span>').insertAfter($(this));
     }
     });
     $("#content").on("click","#submitBtn",function(event){
     $(this).parents("form").unbind("submit");
     });

     },
     //电话验证
     phoneCheck:function(){
     $("#content").on("blur",".phoneCheck",function(){
     //电话验证支持座机和手机验证
     var reg = /^((\d{3,4}-)?\d{7,8}|(?:1[13584][0-9]{9}))$/;
     $(this).next()&&$(this).next().remove();
     if(!reg.exec($(this).val())){
     $(this).removeClass("notCheck");
     $('<span style="color:#f00;color: #f00;position: static;width: auto;margin-left: 16px;">格式错误</span>').insertAfter($(this));
     }
     });
     }*/
};

/*$(function() {
 var common = new Common();

 });*/

//通用的功能类
var eam = {
    //弹出函数
    upload: function (total, that) {
        //计算当前弹窗位置函数
        function nowPos(that) {
            var xPosthis = $(that).offset().left;
            var yPosthis = $(that).offset().top;

            var xPosMain = $('#main').offset().left;
            var yPosMain = $('#main').offset().top;

            var xPos = xPosthis - xPosMain;
            var yPos = yPosthis - yPosMain + 26;
            var posObj = {
                xPos: xPos,
                yPos: yPos
            };
            return posObj;
        }


        var posObj = nowPos(that);
        console.log(posObj);
        var popHTML = '<div id="uploadPop"><div class="uploadPopContain" style="left: ' + posObj.xPos + 'px;top: ' + posObj.yPos + 'px;"><!-- 右上角叉号--> <span class="canceUpload"></span><!-- 选择导入对象模块--><!-- 正在导入模块--> <div class="uploading"> 正在导入' + total + '条数据 <div class="spinner"> <div class="spinner-container container1"> <div class="circle1"></div> <div class="circle2"></div> <div class="circle3"></div> <div class="circle4"></div> </div> <div class="spinner-container container2"> <div class="circle1"></div> <div class="circle2"></div> <div class="circle3"></div> <div class="circle4"></div> </div> <div class="spinner-container container3"> <div class="circle1"></div> <div class="circle2"></div> <div class="circle3"></div> <div class="circle4"></div> </div> </div> </div><!-- 导入成功模块--><div class="uploadSuccess hide"><div class="uploadStatus">导入成功</div><a class="uploadSubmit btn btn-default">确定</a></div></div></div>';
        $('#main').append(popHTML);

        /*后台代码在下面写*/
        //1.阻止导入的进程
        $('#main').on('click', ".canceUpload", function () {
            $("#uploadPop").remove();
            //    后台在此添加组织导入进程的代码
        });
        //2.导入成功后的提示
        //调用successCal（）；
    },
    uploadClose: function (uploadInfo) {
        //导入成功的回调函数
        //将导入信息弹窗关闭，并且一秒后关闭整个弹窗
        $('#uploadPop .uploadPopContain .uploading').addClass('hide');
        $('#uploadPop .uploadPopContain .uploadSuccess').removeClass('hide');
        $('#uploadPop .uploadPopContain .uploadStatus').html(uploadInfo);
        $("#uploadPop .uploadPopContain .uploadSubmit").on("click", function () {
            $("#uploadPop").remove();
        });
    }
};

//$(function(){
//    $("#navbar .nav_ul .l1").on("click",function(){
//        $(this).find(".iconfa.m8").toggleClass("rotate");
//    });
//});