/**
 * Created by tiansu on 2017/7/25.
 */
define(["text!modules/eamsys/sysConfig/roleConfig.html", "text!modules/module1/module1.css",""], function (htmlTemp, cssTemp) {
    var module = {
        init:function(){
            $(".content-wrapper").html(htmlTemp);
            $(".css-attr").html(cssTemp);
            var tab = common.renderTable("mytable", {
                "order": [[1, "desc"]],
                "hascheckbox": true,
                "hasIndex":true,
                "opBtns": [{
                    "icon": "fa-comment-o", "title": "分配数据权限", "callBack": function (data) {
                        console.log(data);
                        layer.open({
                            type: 2,
                            skin: 'layui-layer-rim', //加上边框
                            area: ['800px', '500px'], //宽高
                            closeBtn: 1, //不显示关闭按钮
                            content: common.interfaceUrl.roleForm + "?id=" + data.rolecode + "&&name=" + data.rolename
                        });
                    }
                },
                    {
                        "icon": "fa-comments-o", "title": "分配字段权限", "callBack": function (data) {
                        console.log(data);
                        layer.open({
                            type: 2,
                            skin: 'layui-layer-rim', //加上边框
                            area: ['1000px', '600px'], //宽高
                            closeBtn: 1, //不显示关闭按钮
                            content: common.interfaceUrl.roleMenuFieldForm + "?id=" + data.rolecode + "&&name=" + data.rolename
                        });
                    }
                    }],
                "ajax": {
                    "url": common.interfaceUrl.roleList,
                    "dataSrc": function (json) {
                        return json.data;
                    }
                },
                "columns": [
                    {"data": "rolecode", title:"角色编号"},
                    {"data": "rolename", title:"角色名称"},
                    {"data": "rolenote", title:"角色描述"}

                ],
                "columnDefs": [
                    {
                        "render": function (data, type, row) {
                            if(row.rolenote == undefined){
                                debugger;
                                return "";
                            }else{
                                return data;
                            }
                        },
                        "targets": [3]
                    }]
            });
        }
    }
    return module;
});


