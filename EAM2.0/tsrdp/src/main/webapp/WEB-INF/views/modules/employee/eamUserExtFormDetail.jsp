<%--
	@creator duanfuju
	* @createtime 2017/9/18 9:39
	* @description: 
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>客户信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

        $(function () {
            //深克隆
            function cloneObject(obj) {
                //var o = obj instanceof Array ? [] : {};
                var o = obj.length >0 ? [] : {};
                for (var i in obj) {
                    if (obj.hasOwnProperty(i)) {
                        if (typeof obj[i] === "object" && obj[i] != null) {
                            o[i] = cloneObject(obj[i]);
                        } else {
                            o[i] = obj[i];
                        }
                    }
                }
                return o;
            }

            //设置所有字段只读属性
			var formField = cloneObject(parent.formConfig);

            $.each(formField, function(index,val){
                //设置只读
                val.readonly = true;



                if(val.name == 'own_area'){//归属区域
                    val.options = {
                        isMultiSelect : true,
                        valueField : 'id',
                        treeLeafOnly : false,
                        tree : {
                            url :  common.interfaceUrl.getDevLocationTree,
                            checkbox : true,
                            parentIcon: null,
                            childIcon: null,
                            idFieldName : 'id',
                            parentIDFieldName : 'pId',
                            nodeWidth:200,
                            ajaxType : 'post',
                            textFieldName:'name',//文本字段名，默认值text
                            autoCheckboxEven:false,//复选框联动
                            onClick : function (note) {

                            }
                        }
                    }
                }else if(val.name == 'duty_area'){//责任区域
                    val.options = {
                        isMultiSelect : true,
                        valueField : 'id',
                        treeLeafOnly : false,
                        tree : {
                            url :  common.interfaceUrl.getDevLocationTree,
                            checkbox : true,
                            parentIcon: null,
                            childIcon: null,
                            idFieldName : 'id',
                            parentIDFieldName : 'pId',
                            nodeWidth:200,
                            ajaxType : 'post',
                            textFieldName:'name',//文本字段名，默认值text
                            autoCheckboxEven:false,//复选框联动
                            onClick : function (note) {

                            }
                        }
                    }
                }else if(val.name == 'major'){//专业
                    val.options = {
                        isMultiSelect : true,
                        valueField : 'dict_value',
                        treeLeafOnly : false,
                        tree : {
                            url :  common.interfaceUrl.getDictByDictTypeCode +"?dict_type_code=dev_major",
                            checkbox : true,
                            parentIcon: null,
                            childIcon: null,
                            nodeWidth:200,
                            ajaxType : 'post',
                            textFieldName:'dict_name',//文本字段名，默认值text
                            autoCheckboxEven:false,//复选框联动
                            onClick : function (note) {

                            }
                        }
                    }
                }
            });


            //创建表单结构
            var formConfig = {
                space : 50, labelWidth : 120 , inputWidth : 200,
                //validate: true,
                fields :formField
            };

            $("#inputForm").ligerForm(formConfig);
            //下拉初始化
            common.callAjax('post',false,common.interfaceUrl.getDictByDictTypeCode,"json",{"dict_type_code" : "user_status"},function(data){
                var html="";
                $.each(data, function (i, item) {
                    html += "<option value=\"" + item.dict_value + "\">" + item.dict_name + "</option>";
                });
                var statusBox= $("#statusBox");
                statusBox .html(html);
                statusBox.append($("<input></input>").attr("type", "hidden").attr("name", "status"));//设置隐藏域
                statusBox.val(0);//设置默认值
                statusBox.trigger('change.select2');
            });
            common.callAjax('post',false,common.interfaceUrl.userExtGetAllUser,"json",null,function(data){
                var html="";
                $.each(data, function (i, item) {
                    html += "<option value=\"" + item.id_key + "\">" + item.b_realname + "</option>";
                });
                var higher_authorBox= $("#higher_authorBox");
                higher_authorBox .html(html);
                higher_authorBox.append($("<input></input>").attr("type", "hidden").attr("name", "higher_author"));//设置隐藏域
            });


            //编辑页面字段赋值
            common.callAjax('post',false,common.interfaceUrl.userExtEditObj,"json",{"id":parent.editId},function(data){
                var editForm  = liger.get("inputForm");
                editForm.setData(data);
                //状态下拉设置默认值
                var statusBox=  $("#statusBox");
                statusBox.val(data.status);//设置默认值
                statusBox.trigger('change.select2');
                //上级主管下拉设置默认值
                var higher_authorBox=  $("#higher_authorBox");
                higher_authorBox.val(data.higher_author);//设置默认值
                higher_authorBox.trigger('change.select2');
            });


            //设置只读
            $("select").attr("disabled","disabled");

           // 将时间设置为只读
            $("input[ligeruiid='birth_date']").attr("disabled","disabled");
            $("input[ligeruiid='hiredate']").attr("disabled","disabled");
            $("input[ligeruiid='expirydate']").attr("disabled","disabled");



            $("#closeBtn").on("click",function () {
                parent.layer.closeAll();
            });

        });


	</script>
</head>
<body>
<form id="inputForm" action="" method="post" class="form-horizontal">


</form>
<div class="form-actions">

	<input id="closeBtn"  type="button"  value="关 闭"/>
</div>
</body>
</html>
