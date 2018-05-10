(function ($) {
            var WebPageGuide = function (options) {
                this.settings = {
                	showCloseButton:true,
                    source: null
                }
                this.closeButton='<a href="javascript:void(0);" class="WPGclose" title="关闭新手帮助">×</a>';
                this.stepTemplate='<div class="WPGstep" step="" >'
                +'<b class="WPGjt" style=""></b>'
                +'<p><span class="h1 WPGstepNum"></span><span class="h2 WPGstepTitle"></span>'
                +'<br><span class="WPGstepContent"></span><a href="###" class="WPGnext">下一步</a></p></div>';

                this.settings = $.extend(this.settings, options);
                this.stepList=[];
                if(this.settings.showCloseButton)
                    this.mask='<div class="WPGhelp">'+this.closeButton+'</div>';
                else
                    this.mask='<div class="WPGhelp"></div>';
                $("body").append(this.mask);
                $(".WPGhelp").css({height:document.body.offsetHeight});
                $(".WPGclose").click(function(){
						$('.WPGhelp').remove();
                    });
                return this;
            }

            var num;
            WebPageGuide.prototype = {
                newGuidStep: function (source,title,content) {
                    var item = {};
                    num = this.stepList.length;
                    item.source=source;
                    item.title = title;
                    item.content = content;
                    item.container=$(this.stepTemplate);
                    item.container.find(".WPGstepTitle").html(item.title);
                    item.container.find(".WPGstepContent").html(item.content);
                    item.container.attr("step",num);
                    item.container.attr("id","step"+num);
                    item.container.find(".WPGstepNum").html(num+1);
                    //绑定下一步事件
                    item.container.find(".WPGnext").click(function(){
                    	var obj = $(this).parents('.WPGstep');
						var step = obj.attr('step');
						obj.hide();
						if(parseInt(step)==num)//最后一个按钮时候删除添加的标签
						{
							$('.WPGhelp').remove();
						}
						else
                        {
							$('#step'+(parseInt(step)+1)).show();
                            var scroll_offset = $('#step'+(parseInt(step)+1)).offset();  //得到pos这个div层的offset，包含两个值，top和left
                            $("body,html").animate({
                                scrollTop: scroll_offset.top-window.screen.availHeight/2  //让body的scrollTop等于pos的top，就实现了滚动
                            }, 400);
                        }
                    });
                 
                    this.stepList.push(item);
                    //先添加到页面中，否则无法获取container的宽高
                    $(".WPGhelp").append(item.container);
                     var target=$(source);
                    var corner = item.container.find(".WPGjt");
                    var tleft = target.offset().left;
                    var ttop = target.offset().top;
                    var twidth = target.width();
                    var theight = target.height();
                    var cheight=item.container.height();
                    var cwidth = item.container.width();
                    var cpaddingHeight = parseInt(item.container.css("padding-bottom"))+parseInt(item.container.css("padding-top"));
                    var cpaddingWidth = parseInt(item.container.css("padding-left"))+parseInt(item.container.css("padding-right"));
                    var cnBorder=20;
                    //根据target的位置设置提示框的位置
                    if(tleft<(document.body.offsetWidth/2))
                    {
                        if(ttop<(document.body.offsetHeight/4))
                        {
                            item.container.css({
                                top:ttop+theight+cnBorder,
                                left:tleft+twidth/2
                            });
                            corner.addClass("WPGjt_topleft");
                        }
                        else if(ttop>(document.body.offsetHeight*3/4))
                        {
                            item.container.css({
                                top:ttop-cheight-cpaddingHeight-cnBorder,
                                left:tleft+twidth/2
                            });
                            corner.addClass("WPGjt_bottomleft");
                        }
                        else
                        {
                            item.container.css({
                                top:ttop+(theight-cheight-cpaddingHeight)/2,
                                left:tleft+twidth+cnBorder
                            });
                            corner.addClass("WPGjt_left");
                        }
                    }
                    else
                    {
                        if(ttop<(document.body.offsetHeight/4))
                        {
                            item.container.css({
                                top:ttop+theight+cnBorder,
                                left:tleft-cwidth/2
                            });
                            corner.addClass("WPGjt_topright");
                        }
                        else if(ttop>(document.body.offsetHeight*3/4))
                        {
                            item.container.css({
                                top:ttop-cheight-cpaddingHeight-cnBorder,
                                left:tleft-cwidth/2
                            });
                            corner.addClass("WPGjt_bottomright");
                        }
                        else
                        {
                            item.container.css({
                                top:ttop+(theight-cheight-cpaddingHeight)/2,
                                left:tleft-cwidth-cpaddingWidth-cnBorder
                            });
                            corner.addClass("WPGjt_right");
                        }
                    }
                    return item;
                },
                startGuide:function(){
                    //建议不要使用display:none，否则在添加时候无法获取目标宽高，位置会有偏差
                	$(".WPGhelp").css("visibility","visible");
                    //最后一个按钮内容为完成
                	this.stepList[this.stepList.length-1].container.find(".WPGnext").html("完成");
                	this.stepList[0].container.show();
                }
            }
            $.extend({
            	WebPageGuide:function(options){
            		return new WebPageGuide(options); 
            	}
            });
        })(jQuery);