<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp"%>
    <%@ include file="/WEB-INF/views/include/head.jsp" %>
    <script type="text/javascript" src="/resource/plugins/jQuery/jquery-2.2.3.min.js"></script>
    <script src="/resource/plugins/jQueryUI/jquery-ui.js"></script>
    <script src="/resource/plugins/ztree/js/jquery.ztree.all.min.js"></script>
    <script src="/static/jquery-ztree/3.5.12/js/jquery.ztree.core-3.5.js"></script>
    <script src="/static/jquery-ztree/3.5.12/js/jquery.ztree.exhide-3.5.js"></script>
    <link rel="stylesheet" type="text/css" href="/resource/plugins/ztree/css/zTreeStyle/zTreeStyle.css">
    <style>
        .ztree li span.button.switch {
            width:13px !important;
            height:18px
        }
        .treeContainer .ztreeInputCont{
            padding: 10px;
        }
        .treeContainer .querykeyword{
            margin-top: 10px;
        }
        .treeContainer .zTreeLeft{
            padding-left: 10px;
        }


    </style>


</head>
<body>
<div id="main">
    <section class="content module1">
        <div class="treeContainer">
            <div class="ztreeInputCont">
                关键字：<input type="text" id="keyword" class="querykeyword" placeholder="请输入搜索关键字"/>
                <input type="button" id="search_bt" class='searchbtn' value="搜索" onclick="seachTree()"/>
            </div>
            <div class="zTreeLeft">
                <ul id="seltree" class="ztree"></ul>
            </div>
        </div>
    </section>
</div>
</body>
<script>
    var hiddenNodes=[];	//用于存储被隐藏的结点
    var zTreeObj;
    $(function () {
        initTree();
        initData();
        initActEvent();
    });

    function initTree() {
        /**ztree数据初始化**/
        var isMult = ${isMult};
        var zNodes = ${treeData};
        var setting = {
            view: {
                dblClickExpand: false,
                showLine: true,
                selectedMulti: false
            },
            data: {
                simpleData: {
                    enable:true,
                    idKey: "id",
                    pIdKey: "pid",
                    rootPId: "0"
                }
            },
            check: {
                enable: isMult,
                chkStyle: "checkbox",
                chkboxType: { "Y": "s", "N": "ps" }
            },
            /*check: {
                enable: true,
                chkStyle: "radio",  //单选框
                radioType: "all"   //对所有节点设置单选
            },*/
            callback: {
                onClick:function(event, treeId, treeNode, clickFlag){
                    //console.log(treeId);
                    console.log(treeNode.id)
                }
            }
        };

        zTreeObj = $.fn.zTree.init($("#seltree"), setting, zNodes);
        zTreeObj.expandAll(true);
        /*zTreeObj = $.fn.zTree.getZTreeObj("seltree");
        //默认展开二级节点
        var nodes = zTreeObj.getNodes();
        for(var i=0;i<nodes.length;i++) {
            if (nodes[i].pid == 0) {
                zTreeObj.expandNode(node[i],true, false, true,true);
                debugger;
            }
        }*/
    }
    function initData() {

    }


    function initActEvent() {
        $("#search_bt").click(filter);
        $("#keyword").keyup(filter);

    }
    //过滤ztree显示数据
    function filter(){
        //显示上次搜索后背隐藏的结点
        zTreeObj.showNodes(hiddenNodes);

        //查找不符合条件的叶子节点
        function filterFunc(node){
            var _keywords=$("#keyword").val();
            if(node.isParent|| node.name.indexOf(_keywords)!=-1) return false;
            return true;
        };

        //获取不符合条件的叶子结点
        hiddenNodes=zTreeObj.getNodesByFilter(filterFunc);

        //隐藏不符合条件的叶子结点
        zTreeObj.hideNodes(hiddenNodes);
    }

</script>
