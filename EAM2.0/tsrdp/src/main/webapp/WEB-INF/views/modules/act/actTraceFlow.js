/**
 * 流程跟踪Javascript实现
 */
$(function () {

    //表单提交
    $("#btnSubmit").on('click', function() {

        $("#inputForm").on("submit", function () {

            $(this).ajaxSubmit({
                type: 'post',
                success: function (data) {
                    if (data == "success") {
                        layer.msg('提交成功！',{icon:1,time: 1000}, function (index) {
                            parent.$("#mytable").DataTable().ajax.reload();
                            parent.layer.closeAll();
                        });
                    } else if (data == "noauth") {
                        layer.msg("无审批权限，请先签收任务！",{time: 1000,icon:7});
                    } else {
                        layer.msg("提交失败！",{time: 1000,icon:2});
                    }
                }
            });
            return false;
        });
    });

    /**
     * 获取元素的outerHTML
     */
    $.fn.outerHTML = function () {

        // IE, Chrome & Safari will comply with the non-standard outerHTML, all others (FF) will have a fall-back for cloning
        return (!this.length) ? this : (this[0].outerHTML ||
            (function (el) {
                var div = document.createElement('div');
                div.appendChild(el.cloneNode(true));
                var contents = div.innerHTML;
                div = null;
                return contents;
            })(this[0]));

    };

    if ($('#processDiagram').length == 1) {
        showActivities();
    }
    //渲染下拉框
    var statusData=[{text: '同意', id: 'true'},{text: '拒绝', id: 'false'}];
    var optionHtml = "";
    $.each(statusData, function (i, item) {
       optionHtml += "<option value=\"" + item.id + "\">" + item.text + "</option>";
    });
    $("#deptLeaderApproved").html(optionHtml);
    $("#deptLeaderApproved").select2({
        allowClear: true,
        above: false,
        minimumResultsForSearch: 1
    })


/*    // 解决坐标错误问题
    $('#changeToAutoDiagram').click(function() {
        $('.activity-attr,.activity-attr-border').remove();
        $('#processDiagram').attr('src', ctx + '/a/eam/act/process/trace/data/auto/' + processInstanceId);
    });*/

});

function showActivities() {
    $.getJSON(ctx + '/eam/act/process/trace/data/' + executionId, function (infos) {
        var positionHtml = "";

        var diagramPositon = $('#processDiagram').position();
        var varsArray = new Array();
        $.each(infos, function (i, v) {
            var $positionDiv = $('<div/>', {
                'class': 'activity-attr'
            }).css({
                    position: 'absolute',
                    left: (v.x + 2),
                    top: (v.y + 35),
                    width: (v.width - 2),
                    height: (v.height - 2),
                    backgroundColor: 'black',
                    opacity: 0
                });

            // 节点边框
            var $border = $('<div/>', {
                'class': 'activity-attr-border'
            }).css({
                    position: 'absolute',
                    left: (v.x + 2),
                    top: (v.y + 35),
                    width: (v.width - 4),
                    height: (v.height - 3)
                });

            if (v.currentActiviti) {
                $border.css({
                    border: '3px solid red'
                }).addClass('ui-corner-all-12');
            }
            positionHtml += $positionDiv.outerHTML() + $border.outerHTML();
            varsArray[varsArray.length] = v.vars;
        });

        $(positionHtml).appendTo('#flowchart').find('.activity-attr-border');

        // 鼠标移动到活动上提示
        $('.activity-attr-border').each(function (i, v) {
            var tipContent = "<table class='table table-bordered'>";
            $.each(varsArray[i], function(varKey, varValue) {
                if (varValue) {
                    tipContent += "<tr><td>" + varKey + "</td><td>" + varValue + "</td></tr>";
                }
            });
            tipContent += "</table>";
            $(this).data('vars', varsArray[i]).data('toggle', 'tooltip').data('placement', 'bottom').data('title', '活动属性').attr('title', tipContent);
        }).tooltip();
    });
}