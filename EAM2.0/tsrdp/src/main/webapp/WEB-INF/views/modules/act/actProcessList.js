define(["text!modules/act/actProcessList.html", "text!modules/act/actProcessList.css", ""], function (htmlTemp, cssTemp) {
    var module = {
        init: function () {
            var $ = parent.$;
            $(".content-wrapper").html(htmlTemp).css('background', '#ffffff');
            $(".css-attr").html(cssTemp);

            var datas = null;
            /*$.ajax({
                type : "post",
                async : false, //同步执行
                url : ctx+"/eam/act/process/list",
                dataType : "json", //传递数据形式为text
                success : function(data)
                {
                    //console.log(data)
                    datas=data;
                }
            });*/

           /* datas = [{
                    title: "巡检",
                    content: [{
                            name: "巡检巡检1巡检11",
                            processDefineId: "test_audit:1:1ef9e1a46a304d25b7ce54a57439d39f",
                            processDefineKey: "test_audit1"
                        },
                        {
                            name: "巡检巡检2巡检22",
                            processDefineId: "test_audit:1:1ef9e1a46a304d25b7ce54a57439d39f",
                            processDefineKey: "test_audit2"
                        },
                        {
                            name: "巡检巡检3巡检33",
                            processDefineId: "test_audit:1:1ef9e1a46a304d25b7ce54a57439d39f",
                            processDefineKey: "test_audit3"
                        },
                        {
                            name: "巡检巡检3巡检33",
                            processDefineId: "test_audit:1:1ef9e1a46a304d25b7ce54a57439d39f",
                            processDefineKey: "test_audit3"
                        }
                    ]
                },
                {
                    title: "巡检",
                    content: [{
                            name: "巡检巡检1巡检12",
                            processDefineId: "test_audit:1:1ef9e1a46a304d25b7ce54a57439d39f",
                            processDefineKey: "test_audit1"
                        },
                        {
                            name: "巡检巡检2巡检22",
                            processDefineId: "test_audit:1:1ef9e1a46a304d25b7ce54a57439d39f",
                            processDefineKey: "test_audit2"
                        }
                    ]
                },
                {
                    title: "巡检",
                    content: [{
                            name: "巡检巡检1巡检11",
                            processDefineId: "test_audit:1:1ef9e1a46a304d25b7ce54a57439d39f",
                            processDefineKey: "test_audit1"
                        },
                        {
                            name: "巡检巡检2巡检22",
                            processDefineId: "test_audit:1:1ef9e1a46a304d25b7ce54a57439d39f",
                            processDefineKey: "test_audit2"
                        },
                        {
                            name: "巡检巡检3巡检33",
                            processDefineId: "test_audit:1:1ef9e1a46a304d25b7ce54a57439d39f",
                            processDefineKey: "test_audit3"
                        }
                    ]
                },
                {
                    title: "巡检",
                    content: [{
                            name: "巡检巡检1巡检11",
                            processDefineId: "test_audit:1:1ef9e1a46a304d25b7ce54a57439d39f",
                            processDefineKey: "test_audit1"
                        },
                        {
                            name: "巡检巡检2巡检22",
                            processDefineId: "test_audit:1:1ef9e1a46a304d25b7ce54a57439d39f",
                            processDefineKey: "test_audit2"
                        },
                        {
                            name: "巡检巡检3巡检33",
                            processDefineId: "test_audit:1:1ef9e1a46a304d25b7ce54a57439d39f",
                            processDefineKey: "test_audit3"
                        }
                    ]
                }, 
                {
                    title: "巡检",
                    content: [{
                            name: "巡检巡检1巡检11",
                            processDefineId: "test_audit:1:1ef9e1a46a304d25b7ce54a57439d39f",
                            processDefineKey: "test_audit1"
                        },
                        {
                            name: "巡检巡检2巡检22",
                            processDefineId: "test_audit:1:1ef9e1a46a304d25b7ce54a57439d39f",
                            processDefineKey: "test_audit2"
                        },
                        {
                            name: "巡检巡检3巡检33",
                            processDefineId: "test_audit:1:1ef9e1a46a304d25b7ce54a57439d39f",
                            processDefineKey: "test_audit3"
                        }
                    ]
                },
                {
                    title: "巡检",
                    content: [{
                            name: "巡检巡检1巡检11",
                            processDefineId: "test_audit:1:1ef9e1a46a304d25b7ce54a57439d39f",
                            processDefineKey: "test_audit1"
                        },
                        {
                            name: "巡检巡检2巡检22",
                            processDefineId: "test_audit:1:1ef9e1a46a304d25b7ce54a57439d39f",
                            processDefineKey: "test_audit2"
                        },
                        {
                            name: "巡检巡检3巡检33",
                            processDefineId: "test_audit:1:1ef9e1a46a304d25b7ce54a57439d39f",
                            processDefineKey: "test_audit3"
                        }
                    ]
                }, 
                {
                    title: "巡检",
                    content: [{
                            name: "巡检巡检1巡检11",
                            processDefineId: "test_audit:1:1ef9e1a46a304d25b7ce54a57439d39f",
                            processDefineKey: "test_audit1"
                        },
                        {
                            name: "巡检巡检2巡检22",
                            processDefineId: "test_audit:1:1ef9e1a46a304d25b7ce54a57439d39f",
                            processDefineKey: "test_audit2"
                        },
                        {
                            name: "巡检巡检3巡检33",
                            processDefineId: "test_audit:1:1ef9e1a46a304d25b7ce54a57439d39f",
                            processDefineKey: "test_audit3"
                        }
                    ]
                }
            ];*/

            var option = {
                type: "post",
                async: true,
                url: ctx+"/eam/act/process/list",
                dataType: "json",
                success: function (datas) {
                    renderData(datas);
                },
                error: function () {
                    layer.msg('登录超时或无权限！', {
                        time: 1000,
                        icon: 7
                    });
                }
            }
            $.ajax(option);

            //动态加载页面函数
            function renderData(datas) {
                var content = "";
                var clearfix = 1;
                for (var a in datas) {

                    //单个块的数据
                    var single = datas[a];
                    //单个块的内容
                    content += '<div class="col-box" style="width:30%; margin-right:25px;"><div class="titleitem"><div class="titleContent"><label>' + single.title + '</label><font color="#989898" style="font-weight: 400;margin-left:5px;">(' + single.content.length + '）</font></div class=""></div>';

                    $.each(single.content, function (index, data) {
                        content += '<div class="centerItem"><div class="fontItem"><a class="contentView clickMe" data-processDefineId="' + data.processDefineId + '" data-processDefineKey="'+data.processDefineKey+'">' + data.name + '</a></div></div>';
                    })
                    content += '</div>'
                    clearfix++;
                }
                $('.boxes').append(content);
                waterFall();

            }
            renderData(datas);   //临时调用，接口实现后删除

            //瀑布流布局函数
            function waterFall() {
                var boxes = $('.col-box'),
                    heightArr = [];
                $.each(boxes, function (index, box) {
                    var boxHeight = $(box).outerHeight() + parseInt($(box).css('margin-bottom'));
                    if (index < 3) {
                        heightArr.push(boxHeight);
                    } else {
                        //求出最矮的盒子高度
                        var minBoxHeight = Math.min.apply(this, heightArr);
                        //求出最矮盒子对应的索引
                        var minBoxIndex = getMinBoxIndex(minBoxHeight, heightArr);
                        //盒子瀑布流定位  顶部间距就是最矮盒子的高度
                        box.style.position = 'absolute';
                        box.style.top = minBoxHeight + 'px';
                        var boxWidth = box.offsetWidth + parseInt($(box).css('margin-right'));
                        box.style.left = minBoxIndex * boxWidth + 'px';
                        //关键:更新数组最矮高度,使下一个图片在高度数组中总是找最矮高度的图片下面拼接
                        heightArr[minBoxIndex] += boxHeight;
                    }
                })
            }
            //求出最矮盒子对应的索引函数
            function getMinBoxIndex(val, arr) {
                for (var i in arr) {
                    if (val == arr[i]) {
                        return i;
                    }
                }
            }
            
            $('.boxes').on('click','a.clickMe',function(){
                var processDefineId = $(this).attr('data-processDefineId');
                var processDefineKey = $(this).attr('data-processDefineKey');
                //debugger;
                if(processDefineKey.indexOf("inspectionFlow")>=0
                    ||processDefineKey.indexOf("maintainFlow")>=0
                    ||processDefineKey.indexOf("inspectionRoute_closed")>=0
                    ||processDefineKey.indexOf("maintAnnual_approve")>=0
                    ||processDefineKey.indexOf("maintMon_approve")>=0
                    ||processDefineKey.indexOf("schedualOrderFlow")>=0
                ){
                    layer.msg("被动流程不可新建！",{time: 1000,icon:2});
                    return;
                }
                var name = $(this).text();
                //alert(11);
                //弹窗
                layer.open({
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: common.getArea(), //宽高
                    closeBtn: 1, //显示关闭按钮
                    content: [ctx+"/eam/act/process/startForm?processDefinitionId="+processDefineId]
                });
            })
        }
    }
    return module;
});