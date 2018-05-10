/*! AdminLTE app.js
 * ================
 * Main JS application file for AdminLTE v2. This file
 * should be included in all pages. It controls some layout
 * options and implements exclusive AdminLTE plugins.
 *
 * @Author  Almsaeed Studio
 * @Support <http://www.almsaeedstudio.com>
 * @Email   <abdullah@almsaeedstudio.com>
 * @version 2.3.8
 * @license MIT <http://opensource.org/licenses/MIT>
 */

//Make sure jQuery has been loaded before app.js
require.config({
    // baseUrl:"/front/demo-eam/",
    baseUrl:"/resource/",
    paths:{
        "plugins":"dest/plugins/plugins.min",
        "text":"plugins/require/text",
        "jquery":"plugins/jQuery/jquery-2.2.3.min",
        "bootstrap":"plugins/bootstrap/js/bootstrap.min",
        "dataTables":"plugins/datatables/jquery.dataTables.min",
        "dataTables.bootstrap":"plugins/datatables/dataTables.bootstrap.min",
        "tree":"plugins/ztree/js/jquery.ztree.all.min",
        "common":"common",
        "layer":"plugins/layer/layer",
        "ajaxfileupload":"plugins/ajaxfileupload",
        "common2":"plugins/common2",
        "jquery-form":"plugins/jQuery/jquery-form",
        "liger-all":"plugins/ligerUI/js/ligerui.all",
        "WebPageGuide":"plugins/WebPageGuide/WebPageGuide",
        "fusioncharts":"plugins/fusioncharts-suite-xt/fusioncharts",
        "fusioncharts.theme":"plugins/fusioncharts-suite-xt/themes/fusioncharts.theme.fint",
        "WebUploader":"plugins/webupload/webuploader",
        "fullcalendar":"plugins/fullcalendar/fullcalendar",
        "moment":"plugins/daterangepicker/moment",
        "jqueryUI":"plugins/jQueryUI/jquery-ui.min",
        "jqueryUI2":"plugins/jQueryUI/jquery-ui",
        "timepicker":"plugins/jQuery-Timepicker-Addon/jquery-ui-timepicker-addon",
        "select2":"plugins/select2/select2.full",
        "jeDate":"plugins/jedate/jedate",
        "timerCount":"plugins/timerCount/jquery.timerCount"
    },
    shim:{
        "plugins":["text"],
        "tree":["jquery"],
        "dataTables":["jquery"],
        "bootstrap":["jquery"],
        "timerCount":["jquery"],
        "dataTables.bootstrap":["dataTables","bootstrap"],
        "common":["jquery","layer","timerCount","dataTables.bootstrap","tree","jquery-form","liger-all","WebPageGuide","fusioncharts","WebUploader","fullcalendar","timepicker","select2"],
        "layer":["jquery"],
        "ajaxfileupload":["jquery"],
        "common2":["jquery"],
        "liger-all":["jquery"],
        "WebPageGuide":["jquery"],
        "fusioncharts":["jquery"],
        "fusioncharts.theme":["jquery"],
        "WebUploader":["jquery"],
        "fullcalendar":["jquery","moment","jqueryUI"],
        "timepicker":["jquery","jqueryUI2"],
        "select2":["jquery"]
    }
});

require(['common','jeDate','ajaxfileupload','common2',"WebUploader"], function () {
    if (typeof jQuery === "undefined") {
        throw new Error("AdminLTE requires jQuery");
    }
    layer.config({
        path: '/resource/plugins/layer/'//layer.js所在的目录，可以是绝对目录，也可以是相对目录
    });

    /* AdminLTE
     *
     * @type Object
     * @description $.AdminLTE is the main object for the template's app.
     *              It's used for implementing functions and options related
     *              to the template. Keeping everything wrapped in an object
     *              prevents conflict with other plugins and is a better
     *              way to organize our code.
     */
    $.AdminLTE = {};

    /* --------------------
     * - AdminLTE Options -
     * --------------------
     * Modify these options to suit your implementation
     */
    $.AdminLTE.options = {
        //Add slimscroll to navbar menus
        //This requires you to load the slimscroll plugin
        //in every page before app.js
        navbarMenuSlimscroll: true,
        navbarMenuSlimscrollWidth: "3px", //The width of the scroll bar
        navbarMenuHeight: "200px", //The height of the inner menu
        //General animation speed for JS animated elements such as box collapse/expand and
        //sidebar treeview slide up/down. This options accepts an integer as milliseconds,
        //'fast', 'normal', or 'slow'
        animationSpeed: 500,
        //Sidebar push menu toggle button selector
        sidebarToggleSelector: "[data-toggle='offcanvas']",
        //Activate sidebar push menu
        sidebarPushMenu: true,
        //Activate sidebar slimscroll if the fixed layout is set (requires SlimScroll Plugin)
        sidebarSlimScroll: true,
        //Enable sidebar expand on hover effect for sidebar mini
        //This option is forced to true if both the fixed layout and sidebar mini
        //are used together
        sidebarExpandOnHover: false,
        //BoxRefresh Plugin
        enableBoxRefresh: true,
        //Bootstrap.js tooltip
        enableBSToppltip: true,
        BSTooltipSelector: "[data-toggle='tooltip']",
        //Enable Fast Click. Fastclick.js creates a more
        //native touch experience with touch devices. If you
        //choose to enable the plugin, make sure you load the script
        //before AdminLTE's app.js
        enableFastclick: false,
        //Control Sidebar Tree views
        enableControlTreeView: true,
        //Control Sidebar Options
        enableControlSidebar: true,
        controlSidebarOptions: {
            //Which button should trigger the open/close event
            toggleBtnSelector: "[data-toggle='control-sidebar']",
            //The sidebar selector
            selector: ".control-sidebar",
            //Enable slide over content
            slide: true
        },
        //Box Widget Plugin. Enable this plugin
        //to allow boxes to be collapsed and/or removed
        enableBoxWidget: true,
        //Box Widget plugin options
        boxWidgetOptions: {
            boxWidgetIcons: {
                //Collapse icon
                collapse: 'fa-minus',
                //Open icon
                open: 'fa-plus',
                //Remove icon
                remove: 'fa-times'
            },
            boxWidgetSelectors: {
                //Remove button selector
                remove: '[data-widget="remove"]',
                //Collapse button selector
                collapse: '[data-widget="collapse"]'
            }
        },
        //Direct Chat plugin options
        directChat: {
            //Enable direct chat by default
            enable: true,
            //The button to open and close the chat contacts pane
            contactToggleSelector: '[data-widget="chat-pane-toggle"]'
        },
        //Define the set of colors to use globally around the website
        colors: {
            lightBlue: "#3c8dbc",
            red: "#f56954",
            green: "#00a65a",
            aqua: "#00c0ef",
            yellow: "#f39c12",
            blue: "#0073b7",
            navy: "#001F3F",
            teal: "#39CCCC",
            olive: "#3D9970",
            lime: "#01FF70",
            orange: "#FF851B",
            fuchsia: "#F012BE",
            purple: "#8E24AA",
            maroon: "#D81B60",
            black: "#222222",
            gray: "#d2d6de"
        },
        //The standard screen sizes that bootstrap uses.
        //If you change these in the variables.less file, change
        //them here too.
        screenSizes: {
            xs: 480,
            sm: 768,
            md: 992,
            lg: 1200
        },

    };

    /* ------------------
     * - Implementation -
     * ------------------
     * The next block of code implements AdminLTE's
     * functions and plugins as specified by the
     * options above.
     */
    $(function () {
        "use strict";

        //Fix for IE page transitions
        $("body").removeClass("hold-transition");

        //Extend options if external options exist
        if (typeof AdminLTEOptions !== "undefined") {
            $.extend(true,
                $.AdminLTE.options,
                AdminLTEOptions);
        }

        //Easy access to options
        var o = $.AdminLTE.options;

        //Set up the object
        _init();
        _refreshTimeDiv();
        //获取所有子平台信息
        $.ajax({
            type: 'GET',
            url: common.interfaceUrl.subSystemList,
            dataType: "json",
            success: function (d, s) {
                $.AdminLTE.initTopMenu(d);
            },
            error: function (e) {
                console.log(e.responseText);
            }
        });

        //获取登录用户信息
        $.ajax({
            type: 'GET',
            url: common.interfaceUrl.getUserEmployeeInfo,
            dataType: "json",
            success: function (data, status) {
                if (data != null && data!= undefined ) {
                    $(".span_word").html(data.realname);
                }
            },
            error: function (e) {
                console.log(e.responseText);
            }
        });

        // //新增按钮
        // $("#register").on("click", function () {
        //     var ssohost=$("#register").attr("data-host");
        //     $.ajax({
        //         url:ctx+"/eam/register/getJsonString",
        //         type:"post",
        //         dataType : "text",
        //         async:false,
        //         success:function(data){
        //             $.ajax({
        //                 url:ssohost+"/v1.00/api/register",
        //                 type:"post",
        //                 dataType : "json",
        //                 data:{info:data},
        //                 async:false,
        //                 success:function(da){
        //                     if(!da.success){
        //                         alert("注册失败");
        //                     }
        //                     if(da.success){
        //                         alert("注册成功");}
        //                 }
        //             })
        //
        //
        //
        //         }
        //     })
        //
        // });

        /**
         * 查询当前用户拥有的菜单数据
         */
        $.ajax({
            type: 'GET',
            url: common.interfaceUrl.menuList,
            dataType: "json",
            success: function (d, s) {
                $.AdminLTE.initLeftMenu(d);

                var p = 0,t = 0,g = $('.main-sidebar').offset().top,
                s = $(window).height() - $('.main-sidebar').height() - $('.main-sidebar').offset().top;
                $('#menu-bar').scroll(function(){
                    p = $(this).scrollTop();
                    var d = $('.main-sidebar').height() + g - $(window).height();
                    if(p >= t){
                        if(p > d){
                            $('.main-sidebar').css({position:'absolute',top: p-d });
                            t = p;
                            return false;
                        }else{
                            $('.main-sidebar').css({position:'absolute',top:0});
                            t = p;
                            return false;
                        }
                    }else {
                        if(p-d>=0){
                            $('.main-sidebar').css({position:'absolute',top:p-d});
                            t = p;
                            return false;
                        }else{
                            $('.main-sidebar').css({position:'absolute',top:0});
                            t = p;
                            return false;
                        }

                    }
                })
            }
        });

        //Activate the layout maker
        $.AdminLTE.layout.activate();

        //Enable sidebar tree view controls
        if (o.enableControlTreeView) {
            $.AdminLTE.tree('.sidebar');
        }

        //Enable control sidebar
        if (o.enableControlSidebar) {
            $.AdminLTE.controlSidebar.activate();
        }

        //Add slimscroll to navbar dropdown
        if (o.navbarMenuSlimscroll && typeof $.fn.slimscroll != 'undefined') {
            $(".navbar .menu").slimscroll({
                height: o.navbarMenuHeight,
                alwaysVisible: false,
                size: o.navbarMenuSlimscrollWidth
            }).css("width", "100%");
        }

        //Activate sidebar push menu
        if (o.sidebarPushMenu) {
            $.AdminLTE.pushMenu.activate(o.sidebarToggleSelector);
        }

        //Activate Bootstrap tooltip
        if (o.enableBSToppltip) {
            $('body').tooltip({
                selector: o.BSTooltipSelector,
                container: 'body'
            });
        }

        //Activate box widget
        if (o.enableBoxWidget) {
            $.AdminLTE.boxWidget.activate();
        }

        //Activate fast click
        if (o.enableFastclick && typeof FastClick != 'undefined') {
            FastClick.attach(document.body);
        }

        //Activate direct chat widget
        if (o.directChat.enable) {
            $(document).on('click', o.directChat.contactToggleSelector, function () {
                var box = $(this).parents('.direct-chat').first();
                box.toggleClass('direct-chat-contacts-open');
            });
        }

        /*
         * INITIALIZE BUTTON TOGGLE
         * ------------------------
         */
        $('.btn-group[data-toggle="btn-toggle"]').each(function () {
            var group = $(this);
            $(this).find(".btn").on('click', function (e) {
                group.find(".btn.active").removeClass("active");
                $(this).addClass("active");
                e.preventDefault();
            });

        });


        $.fn.serializeJson=function(){
            var serializeObj={};
            var array=this.serializeArray();
            var str=this.serialize();
            $(array).each(function(){
                if(serializeObj[this.name]){
                    if($.isArray(serializeObj[this.name])){
                        serializeObj[this.name].push(this.value);
                    }else{
                        serializeObj[this.name]=[serializeObj[this.name],this.value];
                    }
                }else{
                    serializeObj[this.name]=this.value;
                }
            });
            return serializeObj;
        };
        
    });

    /* ----------------------------------
     * - Initialize the AdminLTE Object -
     * ----------------------------------
     * All AdminLTE functions are implemented below.
     */
    function _init() {
        'use strict';
        $.AdminLTE.initTopMenu=function(systemInfo){
            var liHtml = '';

            $.each(systemInfo, function (i, obj) {
                var systemname=obj.subsystemname;
                var url=obj.subcallbackurl;
                if(systemname=='设备管理'){
                    liHtml +='<li><a href="'+url+'"  class="eam_breadCrumb_menu_hide">'+systemname+'</a></li>';
                }else{
                liHtml +='<li><a href="'+url+'" class="breadCrumb_menu_hide">'+systemname+'</a></li>';
                }
            });
            $(".top-menu").append(liHtml);

        };
        $.AdminLTE.initLeftMenu = function (menu) {
            var liHtml = "";
            $("#navbar>.ul1").empty();
            $.each(menu, function (i, obj) {
                if (obj.children && obj.children.length > 0) {
                    //liHtml += '<li class="treeview"><a href="#" data-link="' + obj.href + '"><i class="fa fa-' + obj.icon + '"></i><span class="titleLab">' + obj.text + '</span>';
                    var url=obj.icon==undefined?'basicmanage':obj.icon;
                    liHtml += '<li class="treeview"><a href="#" data-id="' + obj.id + '" data-link="' + obj.href + '"><img src="/resource/img/' + url + '.png"></img><span class="titleLab">' + obj.text + '</span>';
                    if (obj.tip) {
                        liHtml += '<span class="pull-right-container"> <i class="fa fa-angle-left pull-right"></i> <small class="label pull-right bg-red">' + obj.tip + '</small></span>';
                    } else {
                        liHtml += '<span class="pull-right-container"><i class="fa fa-angle-left pull-right"></i></span>';
                    }
                    liHtml += " </a><ul class='treeview-menu'>";
                    $.each(obj.children, function (j, subObj) {
                        if (subObj.children && subObj.children.length > 0) {
                            liHtml += "<li class='treeview'><a href='#' data-id='" + subObj.id + "' data-link='" + subObj.href + "'><img class='subObj' src='/resource/img/right_fullArrow.png'><span class='titleLab'>" + subObj.text + "</span>";
                            if (subObj.tip) {
                                //liHtml += '<span class="pull-right-container"><i class="fa fa-angle-left pull-right"></i>  <small class="label pull-right bg-red">' + subObj.tip + '</small></span>';
                            } else {
                                //liHtml += '<span class="pull-right-container"><i class="fa fa-angle-left pull-right"></i></span>';
                            }
                            liHtml += "</a><ul class='treeview-menu'>";
                            $.each(subObj.children, function (k, ksubObj) {
                                liHtml += "<li><a href='#' data-id='" + ksubObj.id + "' data-link='" + ksubObj.href + "'><i class='fa'></i><span class='titleLab'>" + ksubObj.text + "</span>";
                                if (ksubObj.tip) {
                                    liHtml += '<span class="pull-right-container"> <small class="label pull-right bg-red">' + ksubObj.tip + '</small> </span>';
                                }
                                liHtml += "</a></li>";
                            });
                            liHtml += "</ul></li>";
                        } else {
                            liHtml += "<li><a href='#' data-id='" + subObj.id + "' data-link='" + subObj.href + "'><i class='fa'></i><span class='titleLab'>" + subObj.text + "</span>";
                            if (subObj.tip) {
                                liHtml += '<span class="pull-right-container"> <small class="label pull-right bg-red">' + subObj.tip + '</small></span>';
                            }
                            liHtml += "</a></li>";
                        }

                    });
                    liHtml += "</ul></li>";
                } else {
                    liHtml += "<li><a href='#' data-id='" + obj.id + "' data-link='" + obj.href + "'><i class='fa fa-" + obj.icon + "'></i><span class='titleLab'>" + obj.text + "</span>";
                    if (obj.tip) {
                        liHtml += '<span class="pull-right-container"> <small class="label pull-right bg-red">' + obj.tip + '</small> </span>';
                    }
                    liHtml += "</a></li>";
                }

            });
            $(".sidebar-menu").append(liHtml);
        };
        /* Layout
         * ======
         * Fixes the layout height in case min-height fails.
         *
         * @type Object
         * @usage $.AdminLTE.layout.activate()
         *        $.AdminLTE.layout.fix()
         *        $.AdminLTE.layout.fixSidebar()
         */
        $.AdminLTE.layout = {
            activate: function () {
                var _this = this;
                _this.fix();
                _this.fixSidebar();
                $('body, html, .wrapper').css('height', '100%');
                $(window, ".wrapper").resize(function () {
                    _this.fix();
                    _this.fixSidebar();
                });
            },
            fix: function () {
                // Remove overflow from .wrapper if layout-boxed exists
                $(".layout-boxed > .wrapper").css('overflow', 'hidden');
                //Get window height and the wrapper height
                var footer_height = $('.main-footer').outerHeight() || 0;
                var neg = $('.main-header').outerHeight() + footer_height;
                var window_height = $(window).height();
                var sidebar_height = $(".sidebar").height() || 0;
                //Set the min-height of the content and sidebar based on the
                //the height of the document.
                if ($("body").hasClass("fixed")) {
                    $(".content-wrapper, .right-side").css('min-height', window_height - footer_height);
                } else {
                    var postSetWidth;
                    if (window_height >= sidebar_height) {
                        $(".content-wrapper, .right-side").css('min-height', window_height - neg);
                        postSetWidth = window_height - neg;
                    } else {
                        $(".content-wrapper, .right-side").css('min-height', sidebar_height);
                        postSetWidth = sidebar_height;
                    }

                    //Fix for the control sidebar height
                    var controlSidebar = $($.AdminLTE.options.controlSidebarOptions.selector);
                    if (typeof controlSidebar !== "undefined") {
                        if (controlSidebar.height() > postSetWidth)
                            $(".content-wrapper, .right-side").css('min-height', controlSidebar.height());
                    }

                }
            },
            fixSidebar: function () {
                //Make sure the body tag has the .fixed class
                if (!$("body").hasClass("fixed")) {
                    if (typeof $.fn.slimScroll != 'undefined') {
                        $(".sidebar").slimScroll({destroy: true}).height("auto");
                    }
                    return;
                } else if (typeof $.fn.slimScroll == 'undefined' && window.console) {
                    window.console.error("Error: the fixed layout requires the slimscroll plugin!");
                }
                //Enable slimscroll for fixed layout
                if ($.AdminLTE.options.sidebarSlimScroll) {
                    if (typeof $.fn.slimScroll != 'undefined') {
                        //Destroy if it exists
                        $(".sidebar").slimScroll({destroy: true}).height("auto");
                        //Add slimscroll
                        $(".sidebar").slimScroll({
                            height: ($(window).height() - $(".main-header").height()) + "px",
                            color: "rgba(0,0,0,0.2)",
                            size: "3px"
                        });
                    }
                }
            }
        };

        /* PushMenu()
         * ==========
         * Adds the push menu functionality to the sidebar.
         *
         * @type Function
         * @usage: $.AdminLTE.pushMenu("[data-toggle='offcanvas']")
         */
        $.AdminLTE.pushMenu = {
            activate: function (toggleBtn) {
                //Get the screen sizes
                var screenSizes = $.AdminLTE.options.screenSizes;

                //Enable sidebar toggle
                $(document).on('click', toggleBtn, function (e) {
                    e.preventDefault();

                    //Enable sidebar push menu
                    if ($(window).width() > (screenSizes.sm - 1)) {
                        if ($("body").hasClass('sidebar-collapse')) {
                            $("body").removeClass('sidebar-collapse').trigger('expanded.pushMenu');
                        } else {
                            $("body").addClass('sidebar-collapse').trigger('collapsed.pushMenu');
                        }
                    }
                    //Handle sidebar push menu for small screens
                    else {
                        if ($("body").hasClass('sidebar-open')) {
                            $("body").removeClass('sidebar-open').removeClass('sidebar-collapse').trigger('collapsed.pushMenu');
                        } else {
                            $("body").addClass('sidebar-open').trigger('expanded.pushMenu');
                        }
                    }
                });

                $(".content-wrapper").click(function () {
                    //Enable hide menu when clicking on the content-wrapper on small screens
                    if ($(window).width() <= (screenSizes.sm - 1) && $("body").hasClass("sidebar-open")) {
                        $("body").removeClass('sidebar-open');
                    }
                });

                //Enable expand on hover for sidebar mini
                if ($.AdminLTE.options.sidebarExpandOnHover
                    || ($('body').hasClass('fixed')
                    && $('body').hasClass('sidebar-mini'))) {
                    this.expandOnHover();
                }
            },
            expandOnHover: function () {
                var _this = this;
                var screenWidth = $.AdminLTE.options.screenSizes.sm - 1;
                //Expand sidebar on hover
                $('.main-sidebar').hover(function () {
                    if ($('body').hasClass('sidebar-mini')
                        && $("body").hasClass('sidebar-collapse')
                        && $(window).width() > screenWidth) {
                        _this.expand();
                    }
                }, function () {
                    if ($('body').hasClass('sidebar-mini')
                        && $('body').hasClass('sidebar-expanded-on-hover')
                        && $(window).width() > screenWidth) {
                        _this.collapse();
                    }
                });
            },
            expand: function () {
                $("body").removeClass('sidebar-collapse').addClass('sidebar-expanded-on-hover');
            },
            collapse: function () {
                if ($('body').hasClass('sidebar-expanded-on-hover')) {
                    $('body').removeClass('sidebar-expanded-on-hover').addClass('sidebar-collapse');
                }
            }
        };

        /* Tree()
         * ======
         * Converts the sidebar into a multilevel
         * tree view menu.
         *
         * @type Function
         * @Usage: $.AdminLTE.tree('.sidebar')
         */
        $.AdminLTE.tree = function (menu) {
            var _this = this;
            var animationSpeed = $.AdminLTE.options.animationSpeed;
            $(document).off('click', menu + ' li a')
                .on('click', menu + ' li a', function (e) {
                    //Get the clicked link and the next element
                    var $this = $(this);
                    var checkElement = $this.next();

                    //Check if the next element is a menu and is visible
                    if ((checkElement.is('.treeview-menu')) && (checkElement.is(':visible')) && (!$('body').hasClass('sidebar-collapse'))) {
                        //Close the menu
                        checkElement.slideUp(animationSpeed, function () {
                            checkElement.removeClass('menu-open');
                            //Fix the layout in case the sidebar stretches over the height of the window
                            //_this.layout.fix();
                        });
                        var parent = checkElement.parent("li");
                        parent.removeClass("active");
                        var ul = parent.find('ul:visible').slideUp(animationSpeed);
                        ul.removeClass('menu-open');
                        parent.find('li.active').removeClass('active');
                    }
                    //If the menu is not visible
                    else if ((checkElement.is('.treeview-menu')) && (!checkElement.is(':visible'))) {
                        //Get the parent menu
                        var parent = $this.parents('ul').first();
                        //Close all open menus within the parent
                        var ul = parent.find('ul:visible').slideUp(animationSpeed);
                        //Remove the menu-open class from the parent
                        ul.removeClass('menu-open');
                        //Get the parent li
                        var parent_li = $this.parent("li");

                        //Open the target menu and add the menu-open class
                        checkElement.slideDown(animationSpeed, function () {
                            //Add the class active to the parent li
                            checkElement.addClass('menu-open');
                            parent.find('li.active').removeClass('active');
                            parent_li.addClass('active');
                            //Fix the layout in case the sidebar stretches over the height of the window
                            _this.layout.fix();
                        });
                    } else if ((!checkElement.is('.treeview-menu')) && $('body').hasClass('sidebar-collapse')) {
                        var parent = $(".sidebar-menu");
                        var ul = parent.find('ul').css("display", "none")
                        ul.removeClass('menu-open');
                        ul.parent('li.active').removeClass('active');


                        var parents_ul = $this.parents(".sidebar-menu ul");
                        parents_ul.addClass('menu-open');
                        parents_ul.css("display", "block");
                        var parent_li = parents_ul.parent("li");
                        parent_li.addClass("active");
                    }
                    //if this isn't a link, prevent the page from being redirected
                    if (checkElement.is('.treeview-menu')) {
                        e.preventDefault();
                    } else {
                        $(".main-sidebar").find(".sidebar-menu .treeview-menu>li:not(.treeview)").removeClass("selected");
                        $this.parent().addClass("selected");
                        var linkHtml = $this.attr("data-link");
                        if (linkHtml) {
                            var index = layer.load(1, {
                                shade: [0.1,'#fff'],
                                time: 2000//0.1透明度的白色背景
                              });
                            if(linkHtml.indexOf('.html') > 0){
                                var htmlPage = '<iframe id="pa" src= "'+linkHtml+'"  width="100%" ' +
                                    'frameborder = "0" scrolling= "auto" ></iframe>';
                                $('.content-wrapper').html(htmlPage);
                                debugger;
                                var frameHeight = document.body.scrollHeight+'px';
                                $('#pa').css('height',frameHeight);
                                // common.initSetting();
                                return;
                                /*$('.content-wrapper').load(linkHtml, function() {
                                    common.initSetting();
                                    return;
                                });*/

                            }
                            //$(".content-wrapper").load($this.attr("data-link"))
                            common.loadModule($this.attr("data-link"),$this.attr("data-id"));
                            var p = $(".mianbaoxie .urlRecord");
                            p.children("a.dataLink").remove();
                            var pars = $this.parents("li.treeview");
                            for(var i=pars.length-1,item;i>=0;i--){
                                item = $(pars.get(i));
                                p.append("<a class='dataLink'>&gt;"+item.children("a").children(".titleLab").html()+"</a>")
                            }
                            p.append("<a class='dataLink'>&gt;"+$this.children("span.titleLab").html()+"</a>")
                        }
                    }
                });
                
        };

        /* ControlSidebar
         * ==============
         * Adds functionality to the right sidebar
         *
         * @type Object
         * @usage $.AdminLTE.controlSidebar.activate(options)
         */
        $.AdminLTE.controlSidebar = {
            //instantiate the object
            activate: function () {
                //Get the object
                var _this = this;
                //Update options
                var o = $.AdminLTE.options.controlSidebarOptions;
                //Get the sidebar
                var sidebar = $(o.selector);
                //The toggle button
                var btn = $(o.toggleBtnSelector);

                //Listen to the click event
                btn.on('click', function (e) {
                    e.preventDefault();
                    //If the sidebar is not open
                    if (!sidebar.hasClass('control-sidebar-open')
                        && !$('body').hasClass('control-sidebar-open')) {
                        //Open the sidebar
                        _this.open(sidebar, o.slide);
                    } else {
                        _this.close(sidebar, o.slide);
                    }
                });

                //If the body has a boxed layout, fix the sidebar bg position
                var bg = $(".control-sidebar-bg");
                _this._fix(bg);

                //If the body has a fixed layout, make the control sidebar fixed
                if ($('body').hasClass('fixed')) {
                    _this._fixForFixed(sidebar);
                } else {
                    //If the content height is less than the sidebar's height, force max height
                    if ($('.content-wrapper, .right-side').height() < sidebar.height()) {
                        _this._fixForContent(sidebar);
                    }
                }
            },
            //Open the control sidebar
            open: function (sidebar, slide) {
                //Slide over content
                if (slide) {
                    sidebar.addClass('control-sidebar-open');
                } else {
                    //Push the content by adding the open class to the body instead
                    //of the sidebar itself
                    $('body').addClass('control-sidebar-open');
                }
            },
            //Close the control sidebar
            close: function (sidebar, slide) {
                if (slide) {
                    sidebar.removeClass('control-sidebar-open');
                } else {
                    $('body').removeClass('control-sidebar-open');
                }
            },
            _fix: function (sidebar) {
                var _this = this;
                if ($("body").hasClass('layout-boxed')) {
                    sidebar.css('position', 'absolute');
                    sidebar.height($(".wrapper").height());
                    if (_this.hasBindedResize) {
                        return;
                    }
                    $(window).resize(function () {
                        _this._fix(sidebar);
                    });
                    _this.hasBindedResize = true;
                } else {
                    sidebar.css({
                        'position': 'fixed',
                        'height': 'auto'
                    });
                }
            },
            _fixForFixed: function (sidebar) {
                sidebar.css({
                    'position': 'fixed',
                    'max-height': '100%',
                    'overflow': 'auto',
                    'padding-bottom': '50px'
                });
            },
            _fixForContent: function (sidebar) {
                $(".content-wrapper, .right-side").css('min-height', sidebar.height());
            }
        };

        /* BoxWidget
         * =========
         * BoxWidget is a plugin to handle collapsing and
         * removing boxes from the screen.
         *
         * @type Object
         * @usage $.AdminLTE.boxWidget.activate()
         *        Set all your options in the main $.AdminLTE.options object
         */
        $.AdminLTE.boxWidget = {
            selectors: $.AdminLTE.options.boxWidgetOptions.boxWidgetSelectors,
            icons: $.AdminLTE.options.boxWidgetOptions.boxWidgetIcons,
            animationSpeed: $.AdminLTE.options.animationSpeed,
            activate: function (_box) {
                var _this = this;
                if (!_box) {
                    _box = document; // activate all boxes per default
                }
                //Listen for collapse event triggers
                $(_box).on('click', _this.selectors.collapse, function (e) {
                    e.preventDefault();
                    _this.collapse($(this));
                });

                //Listen for remove event triggers
                $(_box).on('click', _this.selectors.remove, function (e) {
                    e.preventDefault();
                    _this.remove($(this));
                });
            },
            collapse: function (element) {
                var _this = this;
                //Find the box parent
                var box = element.parents(".box").first();
                //Find the body and the footer
                var box_content = box.find("> .box-body, > .box-footer, > form  >.box-body, > form > .box-footer");
                if (!box.hasClass("collapsed-box")) {
                    //Convert minus into plus
                    element.children(":first")
                        .removeClass(_this.icons.collapse)
                        .addClass(_this.icons.open);
                    //Hide the content
                    box_content.slideUp(_this.animationSpeed, function () {
                        box.addClass("collapsed-box");
                    });
                } else {
                    //Convert plus into minus
                    element.children(":first")
                        .removeClass(_this.icons.open)
                        .addClass(_this.icons.collapse);
                    //Show the content
                    box_content.slideDown(_this.animationSpeed, function () {
                        box.removeClass("collapsed-box");
                    });
                }
            },
            remove: function (element) {
                //Find the box parent
                var box = element.parents(".box").first();
                // box.slideUp(this.animationSpeed);
                if($('.indexOverview').length>0){
                    box.closest('.col-md-6').slideUp(this.animationSpeed);
                    // box.closest('.col-md-6').hide();
                     var addHtml ='';
                     switch (box.data('id')){
                         case 'performance':{addHtml = '<li class="li-performance">绩效总览</li>';break;} //绩效总览
                         case 'repair':{addHtml = '<li class="li-repair">报修总览</li>';break;} //报修总览
                         case 'workorder':{addHtml = '<li class="li-workorder">工单总览</li>';break;} //工单总览
                         case 'maintain':{addHtml = '<li class="li-maintain">保养总览</li>';break;} //保养总览
                         case 'inspection':{addHtml = '<li class="li-inspection">巡检总览</li>';break;} //巡检总览
                         case 'maintenance':{addHtml = '<li class="li-maintenance">运维总览</li>';break;} //运维总览
                         case 'device':{addHtml = '<li class="li-device">设备总览</li>';break;} //设备总览
                     }
                    $('.moduleSlc ul').append(addHtml);
                }
            }
        };
    }

    function _refreshTimeDiv(){
        setTimeout(_refreshTimeDiv,60*1000);
        var datetime = new Date();
        var year = datetime.getFullYear();
        var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
        var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
        var hour = datetime.getHours()< 10 ? "0" + datetime.getHours() : datetime.getHours();
        var minute = datetime.getMinutes()< 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();
        var time = hour+':'+minute+ " "+month+"/"+date;;
        $(".timeCode").html(time);
    }

    /* ------------------
     * - Custom Plugins -
     * ------------------
     * All custom plugins are defined below.
     */

    /*
     * BOX REFRESH BUTTON
     * ------------------
     * This is a custom plugin to use with the component BOX. It allows you to add
     * a refresh button to the box. It converts the box's state to a loading state.
     *
     * @type plugin
     * @usage $("#box-widget").boxRefresh( options );
     */
    (function ($) {

        "use strict";

        $.fn.boxRefresh = function (options) {

            // Render options
            var settings = $.extend({
                //Refresh button selector
                trigger: ".refresh-btn",
                //File source to be loaded (e.g: ajax/src.php)
                source: "",
                //Callbacks
                onLoadStart: function (box) {
                    return box;
                }, //Right after the button has been clicked
                onLoadDone: function (box) {
                    return box;
                } //When the source has been loaded

            }, options);

            //The overlay
            var overlay = $('<div class="overlay"><div class="fa fa-refresh fa-spin"></div></div>');

            return this.each(function () {
                //if a source is specified
                if (settings.source === "") {
                    if (window.console) {
                        window.console.log("Please specify a source first - boxRefresh()");
                    }
                    return;
                }
                //the box
                var box = $(this);
                //the button
                var rBtn = box.find(settings.trigger).first();

                //On trigger click
                rBtn.on('click', function (e) {
                    e.preventDefault();
                    //Add loading overlay
                    start(box);

                    //Perform ajax call
                    box.find(".box-body").load(settings.source, function () {
                        done(box);
                    });
                });
            });

            function start(box) {
                //Add overlay and loading img
                box.append(overlay);

                settings.onLoadStart.call(box);
            }

            function done(box) {
                //Remove overlay and loading img
                box.find(overlay).remove();

                settings.onLoadDone.call(box);
            }

        };

    })(jQuery);

    /*
     * EXPLICIT BOX CONTROLS
     * -----------------------
     * This is a custom plugin to use with the component BOX. It allows you to activate
     * a box inserted in the DOM after the app.js was loaded, toggle and remove box.
     *
     * @type plugin
     * @usage $("#box-widget").activateBox();
     * @usage $("#box-widget").toggleBox();
     * @usage $("#box-widget").removeBox();
     */
    (function ($) {

        'use strict';

        $.fn.activateBox = function () {
            $.AdminLTE.boxWidget.activate(this);
        };

        $.fn.toggleBox = function () {
            var button = $($.AdminLTE.boxWidget.selectors.collapse, this);
            $.AdminLTE.boxWidget.collapse(button);
        };

        $.fn.removeBox = function () {
            var button = $($.AdminLTE.boxWidget.selectors.remove, this);
            $.AdminLTE.boxWidget.remove(button);
        };

    })(jQuery);

    /*
     * TODO LIST CUSTOM PLUGIN
     * -----------------------
     * This plugin depends on iCheck plugin for checkbox and radio inputs
     *
     * @type plugin
     * @usage $("#todo-widget").todolist( options );
     */
    (function ($) {

        'use strict';

        $.fn.todolist = function (options) {
            // Render options
            var settings = $.extend({
                //When the user checks the input
                onCheck: function (ele) {
                    return ele;
                },
                //When the user unchecks the input
                onUncheck: function (ele) {
                    return ele;
                }
            }, options);

            return this.each(function () {

                if (typeof $.fn.iCheck != 'undefined') {
                    $('input', this).on('ifChecked', function () {
                        var ele = $(this).parents("li").first();
                        ele.toggleClass("done");
                        settings.onCheck.call(ele);
                    });

                    $('input', this).on('ifUnchecked', function () {
                        var ele = $(this).parents("li").first();
                        ele.toggleClass("done");
                        settings.onUncheck.call(ele);
                    });
                } else {
                    $('input', this).on('change', function () {
                        var ele = $(this).parents("li").first();
                        ele.toggleClass("done");
                        if ($('input', ele).is(":checked")) {
                            settings.onCheck.call(ele);
                        } else {
                            settings.onUncheck.call(ele);
                        }
                    });
                }
            });
        };
    }(jQuery));
});

