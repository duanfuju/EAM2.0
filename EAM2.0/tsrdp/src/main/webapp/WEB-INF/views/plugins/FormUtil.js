var FormUtil = function(){

	//初始化表单select数据集
    this.initSelectBox = function(url,idStr,idBoxObj,param,initVal,val_key,show_key){
        var resultData;
        common.callAjax('post',false,url,"json",param,function(data){
            resultData = data;
            var order_receiverHtml="<option value=\"" + 0 + "\">请选择</option>";
            $.each(data, function (i, item) {
                order_receiverHtml += "<option value=\"" + eval("item."+val_key) + "\">" + eval("item."+show_key) + "</option>";
            });
            idBoxObj .html(order_receiverHtml);
            idBoxObj.append($("<input></input>").attr("type", "hidden").attr("name", idStr));//设置隐藏域
            initVal=initVal!=null?initVal:0;
            idBoxObj.val(initVal).trigger('change.select2');;

        });
        return resultData;
    }





    return this;
}();