<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/eamsys/eamTaglib.jsp" %>
<html>
<head>
    <title>物料信息管理</title>
    <meta name="decorator" content="default"/>
    <%--<link href="/resource/plugins/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css"/>--%>
    <%--<link href="/resource/form.css" rel="stylesheet" type="text/css"/>--%>
    <%--<script type="text/javascript" src="/resource/plugins/jQuery/jquery-form.js"></script>--%>
    <%--<script type="text/javascript" src="/resource/plugins/layer/layer.js"></script>--%>
    <%--&lt;%&ndash;<script src="/resource/plugins/ligerUI/js/core/base.js" type="text/javascript"></script>&ndash;%&gt;--%>

    <%--<script src="/resource/plugins/ligerUI/js/ligerui.all.js"></script>--%>

    <%--&lt;%&ndash;表单校验&ndash;%&gt;--%>
    <%--<script src="/resource/plugins/ligerUI/jquery-validation/jquery.validate.min.js"></script>--%>
    <%--<script src="/resource/plugins/ligerUI/jquery-validation/jquery.metadata.js"></script>--%>
    <%--<script src="/resource/plugins/ligerUI/jquery-validation/messages_cn.js"></script>--%>

    <%--&lt;%&ndash;时间控件&ndash;%&gt;--%>
    <%--<script src="/resource/plugins/jQueryUI/jquery-ui.js"></script>--%>
    <%--<link href="/resource/plugins/jQuery-Timepicker-Addon/jquery-ui-timepicker-addon.css" type="text/css"/>--%>
    <%--<script src="/resource/plugins/jQuery-Timepicker-Addon/jquery-ui-timepicker-addon.js"--%>
            <%--type="text/javascript"></script>--%>

    <%--<script type="text/javascript" src="/resource/common.js"></script>--%>
    <%--<script src="/resource/plugins/datatables/jquery.dataTables.min.js" type="text/javascript"></script>--%>
    <%--<script src="/resource/plugins/datatables/dataTables.bootstrap.min.js" type="text/javascript"></script>--%>


    <%--<link rel="stylesheet" type="text/css" href="/resource/plugins/datatables/dataTables.bootstrap.css">--%>
    <%--<link rel="stylesheet" type="text/css" href="/resource/plugins/bootstrap/css/bootstrap.min.css">--%>
    <%--<link rel="stylesheet" type="text/css" href="/resource/plugins/font-awesome/css/font-awesome.min.css">--%>

    <%--<script src="/resource/plugins/select2/select2.full.js" type="text/javascript"></script>--%>
    <%--<link rel="stylesheet" type="text/css" href="/resource/plugins/select2/select2.min.css">--%>
    <script type="text/javascript">
        // var groupicon = "/resource/plugins/ligerUI/skins/icons/communication.gif";
        $(function () {
            /**
             * 查询表格、表单、查询区域的字段权限
             */
            var formField = null;
            $.ajax({
                type: "post",
                async: false, //同步执行
                url: ctx + "/material/testMaterial/getfields?menuno=1102",
                dataType: "json",
                success: function (data) {
                    //表单赋值
                    formField = data.formfield;
                }
            });

            //创建表单结构
            formField.forEach(function (index) {
                if (index.type == "combobox") {
                    index.option = {
                        isMultiSelect: true,
                        valueField: 'text',
                        tree: {
                            url: "/resource/data/tree.json",
                            idFieldName: 'text',
                            ajaxType: 'get'
                        }

                    }
                    index.newline = false;

                }

            });

            //delete parent.formConfig[1]["group"];

            var formConfig = {
                space: 50, labelWidth: 80, inputWidth: 200,
                validate: true,
                fields: formField

            };


            var formConfigg = {
                space: 50, labelWidth: 80, inputWidth: 200,
                validate: true,
                fields: [{display: "日期 ", name: "AddTime", newline: false, type: "date"},
                    {
                        display: "折扣", name: "QuantityPerUnit", comboboxName: "QuantityPerUnit",
                        options: {
                            isMultiSelect: true,
                            valueField: 'text',
                            tree: {
                                url: "/resource/data/tree.json",
                                idFieldName: 'text',
                                ajaxType: 'get'
                            }
                        }, newline: false, type: "combobox", group: ""
                    },
                    {display: "单价", name: "UnitPrice", comboboxName: "UnitPrice", newline: false, type: "combobox"}]
            };

            var CustomersData =
                {
                    Rows: [{
                        "CustomerID": "ALFKI",
                        "CompanyName": "Alfreds Futterkiste",
                        "ContactName": "Maria Anders",
                        "ContactTitle": "Sales Representative",
                        "Address": "Obere Str. 57",
                        "City": "Berlin",
                        "Region": null,
                        "PostalCode": "12209",
                        "Country": "Germany",
                        "Phone": "030-0074321",
                        "Fax": "030-0076545"
                    }, {
                        "CustomerID": "ANATR",
                        "CompanyName": "Ana Trujillo Emparedados y helados",
                        "ContactName": "Ana Trujillo",
                        "ContactTitle": "Owner",
                        "Address": "Avda. de la Constitución 2222",
                        "City": "México D.F.",
                        "Region": null,
                        "PostalCode": "05021",
                        "Country": "Mexico",
                        "Phone": "(5) 555-4729",
                        "Fax": "(5) 555-3745"
                    }, {
                        "CustomerID": "ANTON",
                        "CompanyName": "Antonio Moreno Taquería",
                        "ContactName": "Antonio Moreno",
                        "ContactTitle": "Owner",
                        "Address": "Mataderos  2312",
                        "City": "México D.F.",
                        "Region": null,
                        "PostalCode": "05023",
                        "Country": "Mexico",
                        "Phone": "(5) 555-3932",
                        "Fax": null
                    }, {
                        "CustomerID": "AROUT",
                        "CompanyName": "Around the Horn",
                        "ContactName": "Thomas Hardy",
                        "ContactTitle": "Sales Representative",
                        "Address": "120 Hanover Sq.",
                        "City": "London",
                        "Region": null,
                        "PostalCode": "WA1 1DP",
                        "Country": "UK",
                        "Phone": "(171) 555-7788",
                        "Fax": "(171) 555-6750"
                    }, {
                        "CustomerID": "BERGS",
                        "CompanyName": "Berglunds snabbköp",
                        "ContactName": "Christina Berglund",
                        "ContactTitle": "Order Administrator",
                        "Address": "Berguvsvägen  8",
                        "City": "Luleå",
                        "Region": null,
                        "PostalCode": "S-958 22",
                        "Country": "Sweden",
                        "Phone": "0921-12 34 65",
                        "Fax": "0921-12 34 67"
                    }, {
                        "CustomerID": "BLAUS",
                        "CompanyName": "Blauer See Delikatessen",
                        "ContactName": "Hanna Moos",
                        "ContactTitle": "Sales Representative",
                        "Address": "Forsterstr. 57",
                        "City": "Mannheim",
                        "Region": null,
                        "PostalCode": "68306",
                        "Country": "Germany",
                        "Phone": "0621-08460",
                        "Fax": "0621-08924"
                    }, {
                        "CustomerID": "BLONP",
                        "CompanyName": "Blondel père et fils",
                        "ContactName": "Frédérique Citeaux",
                        "ContactTitle": "Marketing Manager",
                        "Address": "24, place Kléber",
                        "City": "Strasbourg",
                        "Region": null,
                        "PostalCode": "67000",
                        "Country": "France",
                        "Phone": "88.60.15.31",
                        "Fax": "88.60.15.32"
                    }, {
                        "CustomerID": "BOLID",
                        "CompanyName": "Bólido Comidas preparadas",
                        "ContactName": "Martín Sommer",
                        "ContactTitle": "Owner",
                        "Address": "C/ Araquil, 67",
                        "City": "Madrid",
                        "Region": null,
                        "PostalCode": "28023",
                        "Country": "Spain",
                        "Phone": "(91) 555 22 82",
                        "Fax": "(91) 555 91 99"
                    }, {
                        "CustomerID": "BONAP",
                        "CompanyName": "Bon app'",
                        "ContactName": "Laurence Lebihan",
                        "ContactTitle": "Owner",
                        "Address": "12, rue des Bouchers",
                        "City": "Marseille",
                        "Region": null,
                        "PostalCode": "13008",
                        "Country": "France",
                        "Phone": "91.24.45.40",
                        "Fax": "91.24.45.41"
                    }, {
                        "CustomerID": "BOTTM",
                        "CompanyName": "Bottom-Dollar Markets",
                        "ContactName": "Elizabeth Lincoln",
                        "ContactTitle": "Accounting Manager",
                        "Address": "23 Tsawassen Blvd.",
                        "City": "Tsawwassen",
                        "Region": "BC",
                        "PostalCode": "T2F 8M4",
                        "Country": "Canada",
                        "Phone": "(604) 555-4729",
                        "Fax": "(604) 555-3745"
                    }, {
                        "CustomerID": "BSBEV",
                        "CompanyName": "B's Beverages",
                        "ContactName": "Victoria Ashworth",
                        "ContactTitle": "Sales Representative",
                        "Address": "Fauntleroy Circus",
                        "City": "London",
                        "Region": null,
                        "PostalCode": "EC2 5NT",
                        "Country": "UK",
                        "Phone": "(171) 555-1212",
                        "Fax": null
                    }, {
                        "CustomerID": "CACTU",
                        "CompanyName": "Cactus Comidas para llevar",
                        "ContactName": "Patricio Simpson",
                        "ContactTitle": "Sales Agent",
                        "Address": "Cerrito 333",
                        "City": "Buenos Aires",
                        "Region": null,
                        "PostalCode": "1010",
                        "Country": "Argentina",
                        "Phone": "(1) 135-5555",
                        "Fax": "(1) 135-4892"
                    }, {
                        "CustomerID": "CENTC",
                        "CompanyName": "Centro comercial Moctezuma",
                        "ContactName": "Francisco Chang",
                        "ContactTitle": "Marketing Manager",
                        "Address": "Sierras de Granada 9993",
                        "City": "México D.F.",
                        "Region": null,
                        "PostalCode": "05022",
                        "Country": "Mexico",
                        "Phone": "(5) 555-3392",
                        "Fax": "(5) 555-7293"
                    }, {
                        "CustomerID": "CHOPS",
                        "CompanyName": "Chop-suey Chinese",
                        "ContactName": "Yang Wang",
                        "ContactTitle": "Owner",
                        "Address": "Hauptstr. 29",
                        "City": "Bern",
                        "Region": null,
                        "PostalCode": "3012",
                        "Country": "Switzerland",
                        "Phone": "0452-076545",
                        "Fax": null
                    }, {
                        "CustomerID": "COMMI",
                        "CompanyName": "Comércio Mineiro",
                        "ContactName": "Pedro Afonso",
                        "ContactTitle": "Sales Associate",
                        "Address": "Av. dos Lusíadas, 23",
                        "City": "São Paulo",
                        "Region": "SP",
                        "PostalCode": "05432-043",
                        "Country": "Brazil",
                        "Phone": "(11) 555-7647",
                        "Fax": null
                    }, {
                        "CustomerID": "CONSH",
                        "CompanyName": "Consolidated Holdings",
                        "ContactName": "Elizabeth Brown",
                        "ContactTitle": "Sales Representative",
                        "Address": "Berkeley Gardens\r\n12  Brewery ",
                        "City": "London",
                        "Region": null,
                        "PostalCode": "WX1 6LT",
                        "Country": "UK",
                        "Phone": "(171) 555-2282",
                        "Fax": "(171) 555-9199"
                    }, {
                        "CustomerID": "DRACD",
                        "CompanyName": "Drachenblut Delikatessen",
                        "ContactName": "Sven Ottlieb",
                        "ContactTitle": "Order Administrator",
                        "Address": "Walserweg 21",
                        "City": "Aachen",
                        "Region": null,
                        "PostalCode": "52066",
                        "Country": "Germany",
                        "Phone": "0241-039123",
                        "Fax": "0241-059428"
                    }, {
                        "CustomerID": "DUMON",
                        "CompanyName": "Du monde entier",
                        "ContactName": "Janine Labrune",
                        "ContactTitle": "Owner",
                        "Address": "67, rue des Cinquante Otages",
                        "City": "Nantes",
                        "Region": null,
                        "PostalCode": "44000",
                        "Country": "France",
                        "Phone": "40.67.88.88",
                        "Fax": "40.67.89.89"
                    }, {
                        "CustomerID": "EASTC",
                        "CompanyName": "Eastern Connection",
                        "ContactName": "Ann Devon",
                        "ContactTitle": "Sales Agent",
                        "Address": "35 King George",
                        "City": "London",
                        "Region": null,
                        "PostalCode": "WX3 6FW",
                        "Country": "UK",
                        "Phone": "(171) 555-0297",
                        "Fax": "(171) 555-3373"
                    }, {
                        "CustomerID": "ERNSH",
                        "CompanyName": "Ernst Handel",
                        "ContactName": "Roland Mendel",
                        "ContactTitle": "Sales Manager",
                        "Address": "Kirchgasse 6",
                        "City": "Graz",
                        "Region": null,
                        "PostalCode": "8010",
                        "Country": "Austria",
                        "Phone": "7675-3425",
                        "Fax": "7675-3426"
                    }, {
                        "CustomerID": "FAMIA",
                        "CompanyName": "Familia Arquibaldo",
                        "ContactName": "Aria Cruz",
                        "ContactTitle": "Marketing Assistant",
                        "Address": "Rua Orós, 92",
                        "City": "São Paulo",
                        "Region": "SP",
                        "PostalCode": "05442-030",
                        "Country": "Brazil",
                        "Phone": "(11) 555-9857",
                        "Fax": null
                    }, {
                        "CustomerID": "FISSA",
                        "CompanyName": "FISSA Fabrica Inter. Salchichas S.A.",
                        "ContactName": "Diego Roel",
                        "ContactTitle": "Accounting Manager",
                        "Address": "C/ Moralzarzal, 86",
                        "City": "Madrid",
                        "Region": null,
                        "PostalCode": "28034",
                        "Country": "Spain",
                        "Phone": "(91) 555 94 44",
                        "Fax": "(91) 555 55 93"
                    }, {
                        "CustomerID": "FOLIG",
                        "CompanyName": "Folies gourmandes",
                        "ContactName": "Martine Rancé",
                        "ContactTitle": "Assistant Sales Agent",
                        "Address": "184, chaussée de Tournai",
                        "City": "Lille",
                        "Region": null,
                        "PostalCode": "59000",
                        "Country": "France",
                        "Phone": "20.16.10.16",
                        "Fax": "20.16.10.17"
                    }, {
                        "CustomerID": "FOLKO",
                        "CompanyName": "Folk och fä HB",
                        "ContactName": "Maria Larsson",
                        "ContactTitle": "Owner",
                        "Address": "Åkergatan 24",
                        "City": "Bräcke",
                        "Region": null,
                        "PostalCode": "S-844 67",
                        "Country": "Sweden",
                        "Phone": "0695-34 67 21",
                        "Fax": null
                    }, {
                        "CustomerID": "FRANK",
                        "CompanyName": "Frankenversand",
                        "ContactName": "Peter Franken",
                        "ContactTitle": "Marketing Manager",
                        "Address": "Berliner Platz 43",
                        "City": "München",
                        "Region": null,
                        "PostalCode": "80805",
                        "Country": "Germany",
                        "Phone": "089-0877310",
                        "Fax": "089-0877451"
                    }, {
                        "CustomerID": "FRANR",
                        "CompanyName": "France restauration",
                        "ContactName": "Carine Schmitt",
                        "ContactTitle": "Marketing Manager",
                        "Address": "54, rue Royale",
                        "City": "Nantes",
                        "Region": null,
                        "PostalCode": "44000",
                        "Country": "France",
                        "Phone": "40.32.21.21",
                        "Fax": "40.32.21.20"
                    }, {
                        "CustomerID": "FRANS",
                        "CompanyName": "Franchi S.p.A.",
                        "ContactName": "Paolo Accorti",
                        "ContactTitle": "Sales Representative",
                        "Address": "Via Monte Bianco 34",
                        "City": "Torino",
                        "Region": null,
                        "PostalCode": "10100",
                        "Country": "Italy",
                        "Phone": "011-4988260",
                        "Fax": "011-4988261"
                    }, {
                        "CustomerID": "FURIB",
                        "CompanyName": "Furia Bacalhau e Frutos do Mar",
                        "ContactName": "Lino Rodriguez ",
                        "ContactTitle": "Sales Manager",
                        "Address": "Jardim das rosas n. 32",
                        "City": "Lisboa",
                        "Region": null,
                        "PostalCode": "1675",
                        "Country": "Portugal",
                        "Phone": "(1) 354-2534",
                        "Fax": "(1) 354-2535"
                    }, {
                        "CustomerID": "GALED",
                        "CompanyName": "Galería del gastrónomo",
                        "ContactName": "Eduardo Saavedra",
                        "ContactTitle": "Marketing Manager",
                        "Address": "Rambla de Cataluña, 23",
                        "City": "Barcelona",
                        "Region": null,
                        "PostalCode": "08022",
                        "Country": "Spain",
                        "Phone": "(93) 203 4560",
                        "Fax": "(93) 203 4561"
                    }, {
                        "CustomerID": "GODOS",
                        "CompanyName": "Godos Cocina Típica",
                        "ContactName": "José Pedro Freyre",
                        "ContactTitle": "Sales Manager",
                        "Address": "C/ Romero, 33",
                        "City": "Sevilla",
                        "Region": null,
                        "PostalCode": "41101",
                        "Country": "Spain",
                        "Phone": "(95) 555 82 82",
                        "Fax": null
                    }], Total: 91
                };

            var table = {
                display: "顾客", name: "CustomerID", textField: "CustomerName",
                newline: false, type: "combobox", editor: {
                    selectBoxWidth: 600,
                    selectBoxHeight: 300,
                    textField: 'CustomerID',
                    valeuField: 'CustomerID',
                    condition: {
                        prefixID: 'condition_',
                        fields: [
                            {
                                label: '顾客', name: 'CustomerID', type: 'combobox', editor: {
                                selectBoxWidth: 600,
                                selectBoxHeight: 300,
                                textField: 'CustomerID',
                                valeuField: 'CustomerID',
                                data: CustomersData
                            }
                            }
                        ]
                    },
                    grid: {
                        columns: [
                            {display: '主键', name: 'CustomerID', align: 'left', width: 140, minWidth: 33},
                            {display: '公司名', name: 'CompanyName', minWidth: 120},
                            {display: '联系名', name: 'ContactName', minWidth: 140},
                            {display: '城市', name: 'City'}
                        ], data: CustomersData, isScroll: false, sortName: 'CustomerID',
                        width: 680
                    }
                }
            }

            var formConfigTime = {
                space: 50, labelWidth: 120, inputWidth: 200,
                validate: true,
                fields: [{display: "二维码", name: "qrcode", type: "qrcode",group: "基础信息",
                    option:{
                        data:{qrcode: '1.png',title:"南京天溯",equipname: 'aaaaaaa',equipcode:'bbbbbbbbbbbbb',logo:'2.png'}
                    }
                },{
                    display: "发布日期日期",
                    name: "firstPublishDate",
                    type: "date",
                    format: "yyyy-mm-dd HH:mm:ss",
                },
                    {display: "修改时间", name: "modif_date", type: "time"},

                    {display: "日期和事件", name: "datetime", type: "dateTime"},
                    {display: "标题", name: "title", type: "text"},
                    {display: "创建人", name: "createUserName"},
                    {display: "有效状态", name: "materialstatus", comboboxName: "materialstatusBox", type: "select",
                        option:{
                            data:[{text1: '有效', id1: '1'},{text1: '无效', id1: '0'}],
                            id:'id1',
                            text:'text1'
                        }
                    },
                    {display: "下拉表", name: "table", comboboxName: "tableBox", type: "combobox"},
                    {display: "标签", name: "label", type: "label"},

                    {
                        display: "标准库", name: "standard", comboboxName: "standardBox", type: "combobox", group: "库存",
                        options: {
                            isMultiSelect: true,
                            valueField: 'id',
                            textField: 'name',
                            treeLeafOnly: false,
                            tree: {
                                url: ctx + "/material/materialType/materialTypeTree",
                                checkbox: false,
                                idFieldName: 'id',
                                parentIDFieldName: 'pid',
                                parentIcon: null,
                                childIcon: null,
                                ajaxType: 'post',
                                onClick: function (note) {
                                    console.log(note.data.text);
                                    if ($("#maingrid").children().length <1){
                                        $(f_initGrid);
                                    }
                                    if(note.data.text =="设备A"){
                                        $("#maingrid").hide();
                                        $("#maingrid1").show();
                                    }else {
                                        $("#maingrid").show();
                                        $("#maingrid1").hide();
                                    }
                                }
                            }
                        }
                    },
                    {display: "表格切换", name: "tableTab", comboboxName: "tableTabBox", group: "表格切换", type: "select",
                        option:{
                            data:[{text: '表格1', id: '1'},{text: '表格2', id: '0'}]
                        }
                    }
                ]
//				buttons: [{ text: "添加", width: 60, click: null }]
            };

            //debugger;

            $("#inputForm").ligerForm(formConfigTime);

            var dataGrid = [
                {id: 1, name: '李三', sex: '男'},
                {id: 2, name: '李四', sex: '女'},
                {id: 3, name: '赵武', sex: '女'},
                {id: 4, name: '陈留', sex: '女'}
            ];
            //物料类型下拉初始化
            $("input[name='tableBox']").ligerComboBox({
                width: 200,
                columns: [
                    {header: 'ID', name: 'id', width: 20},
                    {header: '名字', name: 'name'},
                    {header: '性别', name: 'sex'}
                ],
                data: dataGrid,
                textField: 'name'
            });
//            $("input[name='modif_date']").timepicker({
//                hourGrid: 4,
//                minuteGrid: 10
//            });
            //debugger;
            var data = [
                {
                    "isNewRecord": true,
                    "id_key": "8D71F307-51DD-4D69-B5BD-B6ED59E3B15A",
                    "supplier_code": "GYS201708230002",
                    "supplier_name": "机械供应商",
                    "supplier_type": "1",
                    "supplier_level": "1",
                    "supplier_credit": "1",
                    "supplier_address": "",
                    "supplier_linkman": "李四",
                    "supplier_phone": "18658530416",
                    "supplier_fax": "",
                    "supplier_bus_license": "",
                    "supplier_org_code": "",
                    "supplier_taxcode": "",
                    "supplier_sucCode": "",
                    "supplier_deposit_bank": "",
                    "supplier_account": "",
                    "supplier_bank_address": "",
                    "supplier_remark": "",
                    "supplier_status": "1"
                },
                {
                    "isNewRecord": true,
                    "id_key": "C6F035E6-F54E-4E9B-A17B-993DD9B761B7",
                    "supplier_code": "GYS201708230001",
                    "supplier_name": "电器供应商",
                    "supplier_type": "1",
                    "supplier_level": "0",
                    "supplier_credit": "1",
                    "supplier_address": "",
                    "supplier_linkman": "张三11",
                    "supplier_phone": "18961855835",
                    "supplier_fax": "",
                    "supplier_bus_license": "",
                    "supplier_org_code": "",
                    "supplier_taxcode": "",
                    "supplier_sucCode": "",
                    "supplier_deposit_bank": "",
                    "supplier_account": "",
                    "supplier_bank_address": "",
                    "supplier_remark": "",
                    "supplier_status": "1"
                },
                {
                    "isNewRecord": true,
                    "id_key": "D88B5A75-05D4-4DCC-A76B-86A0D1E104D5",
                    "supplier_code": "GYS201708240001",
                    "supplier_name": "钢铁供应商",
                    "supplier_type": "2",
                    "supplier_level": "0",
                    "supplier_credit": "1",
                    "supplier_address": "",
                    "supplier_linkman": "李四",
                    "supplier_phone": "18961855968",
                    "supplier_fax": "",
                    "supplier_busdate_start": 1503504000000,
                    "supplier_busdate_end": 1503590400000,
                    "supplier_bus_license": "",
                    "supplier_org_code": "",
                    "supplier_taxcode": "",
                    "supplier_sucCode": "",
                    "supplier_deposit_bank": "",
                    "supplier_account": "",
                    "supplier_bank_address": "",
                    "supplier_remark": "",
                    "supplier_status": "1"
                }
            ]
            /* //测试select类型下拉初始化
             $("input[name='materialstatusBox']").ligerComboBox({
             width : 200,
             data: data,
             textField: 'supplier_name',
             valueField:'supplier_code',
             onSelected: function (value)
             {
             console.log(data)
             }
             });*/

            //物料类型下拉初始化
//            $("input[name='tableTabBox']").ligerComboBox({
//                width: 200,
//                data: [
//                    {name: '表格1', id: '1'},
//                    {name: '表格2', id: '0'},
//
//                ],
//                valueFieldID: 'type_status',
//                textField: 'name',
//                onSelected: function (value) {
//                    console.log(value);
//                    if(value =="1"){
//                        $("#mytable1_wrapper").show();
//                        $("#mytable2_wrapper").hide();
//                    }else {
//                        $("#mytable1_wrapper").hide();
//                        $("#mytable2_wrapper").show();
//                    }
//                }
//            });
//            var tableData=[{text: '表格1', id: '1'},{text: '表格2', id: '0'}];
//            var optionHtml = "<option value='AL'>请选择</option>";
//            $.each(tableData, function (i, item) {
//                optionHtml += "<option value=\"" + item.id + "\">" + item.text + "</option>";
//            });
//            $("#tableTabBox").html(optionHtml);
            //下拉框选中事件
            $("#tableTabBox").on('change',function () {
                //alert($("#tableTabBox").val());
                if($("#tableTabBox").val() =="1"){
                    $("#mytable1_wrapper").show();
                    $("#mytable2_wrapper").hide();
                }else {
                    $("#mytable1_wrapper").hide();
                    $("#mytable2_wrapper").show();
                }
            })


            /*$("input[name='type_pidBox']").ligerComboBox({
             //data:dataTree,
             isMultiSelect:true,
             valueField: 'text',
             tree: {
             url:"/resource/data/tree.json",
             idFieldName :'text',
             ajaxType: 'get'
             }
             });

             $("input[name='type_pidBox']").eq(0).parent().hide();*/


            $("#btnSubmit").on("click", function () {

                var data = manager.getData();
                var data1 = manager1.getData();
                console.log(data);
                console.log(data1);
                return;

                var form = liger.get('inputForm');
                if (!form.valid()) {
                    form.showInvalid();
                } else {
                    //表单提交

                    $("#inputForm").ajaxSubmit({
                        type: 'post',
                        dataType: "text",
                        success: function (data) {
                            if (data == "success") {
                                layer.msg('新增成功！',{icon: 1,time: 1000},function (index) {
                                    parent.$("#mytable").DataTable().ajax.reload();
                                    parent.layer.closeAll();
                                });
                            } else if (data == "timeout") {
                                layer.msg("登录超时或无权限！",{time: 1000,icon:7});
                            } else {
                                layer.msg("新增失败！",{time: 1000,icon:2});
                            }
                        }
                    });

                }

            })

            $("#standard").closest('.l-fieldcontainer').parent().after($("#standardEdit"));

            $('#standardEdit').on('click','.btnDel',function(){
                if($(this).parent().siblings().length  == 0)
                {
                    alert("只能一项时无法删除");
                }else {
                    $(this).parent().remove();
                }
            })

            $('#standardEdit').on('click','.btnAdd',function(){
                $(this).parent().after($(this).parent().clone());
            })

//            $("input[name='materialstatusBox']").ligerComboBox({
//                width: 200,
//                data: [
//                    {name: '有效', id: '1'},
//                    {name: '无效', id: '0'}
//                ],
//                valueFieldID: 'type_status',
//                textField: 'name',
//                onSelected: function (value) {
//                    console.log(value)
//                }
//            });

//            $("input[name='materialstatusBox']").select2({
//                allowClear: true,
//                above: false,
//                data: [{text: '有效', id: '1'},
//                        {text: '无效', id: '0'}],
//                placeholder:'请选择',
//                minimumResultsForSearch: 1
//            })
            //select2渲染的方法
//            var statusData=[{text: '有效', id: '1'},{text: '无效', id: '0'}];
//            var optionHtml = "<option value=''>请选择</option>";
//            $.each(statusData, function (i, item) {
//                optionHtml += "<option value=\"" + item.id + "\">" + item.text + "</option>";
//            });
//            $("#materialstatusBox").html(optionHtml);

            //下拉框选中事件
//            $("#materialstatusBox").on('change',function () {
//                //alert($("#materialstatusBox").val());
//            })


            var tab1 = common.renderTable("mytable1", {
                "order": [[3, "desc"]],
                "hascheckbox": true,
                "hasIndex":true,
                "ajax": {
                    "url": common.interfaceUrl.tableData,
                    "dataSrc": function (json) {
                        return json.data.data;
                    }
                },
                "columns": [
                    {"data": "materialCode"},
                    {"data": "materialName"},
                    {"data": "materialNumber"},
                    {"data": "materialUnit"},
                    {"data": "materialCost"},
                    {"data": "materialType"}
                ],
                "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                    //新增省略号
                    if (aData.materialCode.length > 5) {
                        $('td:eq(2)', nRow).html( '<a title="'+aData.materialCode +'" data-code="' + aData.materialCode + '">'+aData.materialCode.substr(0, 5)+'...</a>');
                    }else{
                        $('td:eq(2)', nRow).html( '<a title="'+aData.materialCode +'" data-code="' + aData.materialCode + '">'+aData.materialCode+'</a>');
                    }
                }
            });

            $('#mytable1').on('click','a',function(){
                alert($(this).data("code"));
            })

            //不使用ajax请求数据
            var tabData2 = [
                {
                    "rowno": 1,
                    "id": 37884,
                    "materialCode": "WL20170223005",
                    "materialName": "机械键盘",
                    "materialNumber": 100,
                    "materialUnit": "台",
                    "materialCost": 598.5,
                    "materialPurchaseMethods": "自购",
                    "materialStatus": "有效",
                    "materialLevel": "A类物资",
                    "materialType": "机械设备",
                    "materialSupplier": null
                },
                {
                    "rowno": 2,
                    "id": 37883,
                    "materialCode": "WL20170223004",
                    "materialName": "打印机220v",
                    "materialNumber": 100,
                    "materialUnit": "台",
                    "materialCost": 332.5,
                    "materialPurchaseMethods": "自购",
                    "materialStatus": "有效",
                    "materialLevel": "A类物资",
                    "materialType": "机械设备",
                    "materialSupplier": null
                }]
            var tab2 = common.renderTable("mytable2", {
                "order": [[3, "desc"]],
                "hascheckbox": true,
                "hasIndex":true,
                "serverSide": false,
                "data": tabData2,
                "columns": [
                    {"data": "materialCode"},
                    {"data": "materialName"},
                    {"data": "materialNumber"},
                    {"data": "materialUnit"},
                    {"data": "materialCost"},
                    {"data": "materialType"}
                ]
            });
//            var tab2 = $('#mytable2').DataTable({
//                "hascheckbox": true,
//                "hasIndex":true,
//                "data": tabData2,
//                "columns": [
//                    {"data": "materialCode"},
//                    {"data": "materialName"},
//                    {"data": "materialNumber"},
//                    {"data": "materialUnit"},
//                    {"data": "materialCost"},
//                    {"data": "materialType"}
//                ]
//            });
            $("#mytable2_wrapper").hide();


//            var editor = new $.fn.dataTable.Editor( {
//                "ajax": {
//                    "url": common.interfaceUrl.tableData,
//                    "dataSrc": function (json) {
//                        //return json.data.data;
//                        return [{"fault_standard_code":"001"}];
//                    }
//                },
//                table: "#repaireStdFromCTable",
//                fields: [ {
//                    name: "fault_standard_code"
//                }, {
//                    name: "fault_standard_description"
//                }, {
//                    name: "fault_standard_explain"
//                }, {
//                    name: "fault_standard_remark"
//                }
//                ]
//            } );

            var tab = common.renderTable("repaireStdFromCTable", {
                "order": [[3, "desc"]],
                "hascheckbox": true,
                "hasIndex":false,
                "paging": false,
                "ajax": {
                    "url": common.interfaceUrl.tableData,
                    "dataSrc": function (json) {
                        //return json.data.data;
                        return [{"fault_standard_code":"001",
                                "fault_standard_description":"描述1",
                                "fault_standard_explain":"说明1",
                                "fault_standard_remark":"备注1",
                                "fault_standard_select":"选择1"}];
                    }
                },
                "columns": [
                    {"data": "fault_standard_code"},
                    {"data": "fault_standard_description"},
                    {"data": "fault_standard_explain"},
                    {"data": "fault_standard_remark"},
                    {"data": "fault_standard_select"}
                ],
                "columnDefs": [
                    {
                        "render": function ( data, type, row ) {
                            return "<select><option value='1'>选择1</option><option value='2'>选择2</option></select>";
                        },
                        "targets": [5]
                    },
                    {
                        "render": function ( data, type, row ) {
                            return "<a class='add'>添加</a><a class='del' style='padding-left: 6px'>删除</a>";
                        },
                        "targets": [6]
                    }
                ]
            });

            //$(".valid").eq(0).val();

            //$("#repaireStdFromCTable tbody td:not(':first,:last')").attr("contenteditable",true);
            $('#repaireStdFromCTable').on( 'click', 'tbody td:not(":first,:last")', function (e) {
                $(this).attr("contenteditable",true);
            } );

            $('#repaireStdFromCTable').on('click','.add',function(){
//                var tr = $(this).closest('tr');
//                tr.parent().append(tr.clone());
//                var tr = $(this).closest('tr');
//                var code = $('#repaireStdFromCTable').DataTable().row(tr).data().fault_standard_code;
//                $('#repaireStdFromCTable').DataTable().row.add(tr.clone()).draw();

                var row1 = $(this).closest('tr');
                var row2 = row1.clone();
                $(row2.children()[0]).html(null);
//                var rowNode = $('#repaireStdFromCTable').DataTable()
//                    .row.add(row2)
//                    .node();
                var rowNode = $('#repaireStdFromCTable').DataTable()
                    .row.add({"fault_standard_code":"002"})
                    .node();
                row1.after(rowNode);
            })

            $('#repaireStdFromCTable').on('click','.del',function(){
                var tr = $(this).closest('tr');
                if(tr.siblings().length  == 0)
                {
                    alert("只剩一项时无法删除");
                }else {
                    tr.remove();
                }
            })

            var EmployeeData = {
                Rows: [
                    {
                        "__status": null,
                        "ID": 1,
                        "DepartmentID": 3,
                        "RealName": "fewf",
                        "DepartmentName": "销售部",
                        "Sex": 1,
                        "Age": 2433,
                        "IncomeDay": "2011-5-2",
                        "Phone": "159244332",
                        "Address": "变为不为恶"
                    },
                    {
                        "__status": null,
                        "ID": 2,
                        "DepartmentID": 1,
                        "RealName": "李三34",
                        "DepartmentName": "主席",
                        "Sex": 2,
                        "Age": 23,
                        "IncomeDay": "2012-1-2",
                        "Phone": "2323232323",
                        "Address": "3434"
                    },
                    {
                        "__status": null,
                        "ID": 3,
                        "DepartmentID": 3,
                        "RealName": "吴彬3",
                        "DepartmentName": "销售部",
                        "Sex": 2,
                        "Age": 26,
                        "IncomeDay": "2011-5-2",
                        "Phone": "159244332",
                        "Address": "1311飞屋服务费3434343433434"
                    },
                    {
                        "__status": null,
                        "ID": 5,
                        "DepartmentID": 2,
                        "RealName": "吴久",
                        "DepartmentName": "研发中心",
                        "Sex": 2,
                        "Age": 29,
                        "IncomeDay": "2012-5-2",
                        "Phone": "159244332",
                        "Address": "3444444"
                    },
                    {
                        "__status": null,
                        "ID": 6,
                        "DepartmentID": 3,
                        "RealName": "陈民",
                        "DepartmentName": "销售部",
                        "Sex": 2,
                        "Age": 21,
                        "IncomeDay": "2014-1-1",
                        "Phone": null,
                        "Address": "3435333"
                    },
                    {
                        "__status": null,
                        "ID": 7,
                        "DepartmentID": 3,
                        "RealName": "陈留",
                        "DepartmentName": "销售部",
                        "Sex": 1,
                        "Age": 32,
                        "IncomeDay": "2013-5-6",
                        "Phone": null,
                        "Address": "3333444444444444"
                    },
                    {
                        "__status": null,
                        "ID": 8,
                        "DepartmentID": 1,
                        "RealName": "谢银223",
                        "DepartmentName": "主席",
                        "Sex": 1,
                        "Age": 13,
                        "IncomeDay": "2011-1-2",
                        "Phone": null,
                        "Address": "34344555555555"
                    },
                    {
                        "__status": null,
                        "ID": 10,
                        "DepartmentID": 2,
                        "RealName": "陈元为2",
                        "DepartmentName": "研发中心",
                        "Sex": 2,
                        "Age": 16,
                        "IncomeDay": "2011-6-2",
                        "Phone": null,
                        "Address": "34343434343434"
                    },
                    {
                        "__status": null,
                        "ID": 11,
                        "DepartmentID": 1,
                        "RealName": "大为",
                        "DepartmentName": "主席",
                        "Sex": 1,
                        "Age": 19,
                        "IncomeDay": "2014-5-2",
                        "Phone": null,
                        "Address": "3434444444"
                    },
                    {
                        "__status": null,
                        "ID": 21,
                        "DepartmentID": 4,
                        "RealName": "444",
                        "DepartmentName": "市场部",
                        "Sex": 2,
                        "Age": 20,
                        "IncomeDay": "2014-5-2",
                        "Phone": null,
                        "Address": "444"
                    },
                    {
                        "__status": null,
                        "ID": 22,
                        "DepartmentID": 1,
                        "RealName": "22",
                        "DepartmentName": "主席",
                        "Sex": 1,
                        "Age": 20,
                        "IncomeDay": "2012-5-2",
                        "Phone": null,
                        "Address": "22"
                    },
                    {
                        "__status": null,
                        "ID": 23,
                        "DepartmentID": 2,
                        "RealName": "22",
                        "DepartmentName": "研发中心",
                        "Sex": 1,
                        "Age": 20,
                        "IncomeDay": "2014-5-1",
                        "Phone": null,
                        "Address": "2224444444"
                    },
                    {
                        "__status": null,
                        "ID": 26,
                        "DepartmentID": 4,
                        "RealName": "232323",
                        "DepartmentName": "市场部",
                        "Sex": 2,
                        "Age": 0,
                        "IncomeDay": "2014-5-2",
                        "Phone": null,
                        "Address": "222222222222222"
                    }
                ],
                Total: 13
            };
            //$(f_initGrid);
            var manager, g;
            var manager1, g1;

            function f_initGrid() {
                g = manager = $("#maingrid").ligerGrid({
                    columns: [
//                    { display: '主键', name: 'ID', type: 'int',frozen:true },
                        {
                            display: '名字', name: 'RealName',
                            editor: {type: 'text'}
                        },
                        {
                            display: '性别', name: 'Sex', type: 'int',
                            editor: {
                                type: 'selectGrid', data: [{Sex: 1, text: '男'}, {Sex: 2, text: '女'}],
                                valueField: 'Sex', textField: 'text'
                            },
                            render: function (item) {
//                            if (parseInt(item.Sex) == 1) return '男';
//                            return '女';
                                var text = "";
                                $.each([{Sex: 1, text: '男'}, {Sex: 2, text: '女'}], function (i, data) {
                                    if (data.Sex == item.Sex) {
                                        text = data.text;
                                    }
                                })
                                return text;
                            }
                        },
                        {display: '年龄', name: 'Age', type: 'int', editor: {type: 'int'}},
                        {display: '数量', name: 'Number', type: 'int', editor: {type: 'int'}},
                        { display: '时间', name: 'Time', type: 'time', editor: { type: 'time'} },
                        {
                            display: '地址', name: 'Address',
                            editor: {type: 'text'}
                        },
                        {
                            display: '操作', isSort: false,
                            render: function (rowdata, rowindex, value) {
                                var h = "";
                                h += "<a class='add'>添加</a> ";
                                h += "<a  class='del' data-id='"+ rowindex +"'>删除</a> ";
                                return h;
                            }
                        },
                        {
                            display: '开关', isSort: false,
                            render: function (rowdata, rowindex, value) {
                                var h = '';
                                h += '<input type="checkbox" id="checkbox_c2'+rowindex+'" class="chk_3" style="display:none" checked /><label for="checkbox_c2'+rowindex+'" class="lab_3"></label>'
                                return h;
                            }
                        },
                        {
                            display: '统计', name: 'Count',isSort: false,
                            render: function (rowdata, rowindex, value) {
                                var h = rowdata.Age * rowdata.Sex;
                                return h;
                            }
                        },
                        {
                            display: '统计联动', name: 'Counts',isSort: false,
                            render: function (rowdata, rowindex, value) {
                                var h = rowdata.Number * rowdata.Count;
                                return h;
                            }
                        }
                    ],
                    onSelectRow: function (rowdata, rowindex) {
                        //$("#txtrowindex").val(rowindex);
                    },
                    enabledEdit: true, isScroll: false, checkbox: true, rownumbers: true,onAfterEdit: f_onAfterEdit,
                    data: EmployeeData,
                    width: '88%'
                });


                g1 = manager1 = $("#maingrid1").ligerGrid({
                    columns: [
//                    { display: '主键', name: 'ID', type: 'int',frozen:true },
                        {
                            display: '名字1', name: 'RealName',
                            editor: {type: 'text'}
                        },
                        {
                            display: '性别', name: 'Sex', type: 'int',
                            editor: {
                                type: 'selectGrid', data: [{Sex: 1, text: '男'}, {Sex: 2, text: '女'}],
                                valueField: 'Sex', textField: 'text'
                            },
                            render: function (item) {
//                            if (parseInt(item.Sex) == 1) return '男';
//                            return '女';
                                var text = "";
                                $.each([{Sex: 1, text: '男'}, {Sex: 2, text: '女'}], function (i, data) {
                                    if (data.Sex == item.Sex) {
                                        text = data.text;
                                    }
                                })
                                return text;
                            }
                        },
                        {display: '年龄', name: 'Age', type: 'int', editor: {type: 'int'}},
                        {
                            display: '地址', name: 'Address',
                            editor: {type: 'text'}
                        },
                        {
                            display: '操作', isSort: false,
                            render: function (rowdata, rowindex, value) {
                                var h = "";
                                h += "<a class='add'>添加</a> ";
                                h += "<a  class='del' data-id='"+ rowindex +"'>删除</a> ";
                                return h;
                            }
                        },
                        {
                            display: '统计', isSort: false,
                            render: function (rowdata, rowindex, value) {
                                var h = rowdata.Age * rowdata.Sex;
                                return h;
                            }
                        }
                    ],
                    onSelectRow: function (rowdata, rowindex) {
                        //$("#txtrowindex").val(rowindex);
                    },
                    enabledEdit: true, isScroll: false, checkbox: true, rownumbers: true,
                    data: EmployeeData,
                    width: '88%'
                });

                $('#maingrid').on('click','.lab_3',function(){
                    $(this).toggleClass("thisAct");
                    if($(this).hasClass("thisAct")){
                        alert("关闭");
                    }else{
                        alert("打开");
                    }
                })
            }

            function f_onAfterEdit(e) {
                manager.updateCell('Count', e.record.Age * e.record.Sex, e.record);
                manager.updateCell('Counts', e.record.Number * e.record.Count, e.record);
            }

            $('#maingrid').on('click','.add',function(){
                //var row = manager.getSelectedRow();
                //var data = manager.getData();
                //表格参数key必须填，哪怕value是null，否则getData()取不到值
                manager.addRow({
                    "RealName": "lisi",
                    "Sex": 2,
                    "Age": 23,
                    "Address": "",
                    "Count": ""
                });
            })

            $('#maingrid').on('click','.del',function(){
                if (manager.getData().length == 1) {
                    alert("只剩一项时无法删除");
                } else {
                    console.log(manager.rows[$(this).data("id")]);
                    manager.deleteRow($(this).data("id"));
                }
            })

            $('#maingrid1').on('click','.add',function(){
                //var row = manager1.getSelectedRow();
                //var data = manager1.getData();
                //表格参数key必须填，哪怕value是null，否则getData()取不到值
                manager1.addRow({
                    "RealName": "lisi",
                    "Sex": 2,
                    "Age": 23,
                    "Address": "",
                    "Count": ""
                });
            })

            $('#maingrid1').on('click','.del',function(){
                if (manager1.getData().length == 1) {
                    alert("只剩一项时无法删除");
                } else {
                    manager1.deleteRow($(this).data("id"));
                }
            })

//            function deleteRow(rowid) {
//                if (manager.getData().length == 1) {
//                    alert("只剩一项时无法删除");
//                } else {
//                    manager.deleteRow(rowid);
//                }
//            }
//
//            function addNewRow() {
//                //var row = manager.getSelectedRow();
//                //var data = manager.getData();
//                //表格参数key必须填，哪怕value是null，否则getData()取不到值
//                manager.addRow({
//                    "RealName": "lisi",
//                    "Sex": 2,
//                    "Age": 23,
//                    "Address": ""
//                });
//            }

//            setTimeout(function (){
//                console.log(manager.getData());
//                console.log(manager1.getData());
//            },20000)
        });
    </script>
    <script type="text/javascript">
        $(function () {
            var TreeDeptData = {
                Rows: [
                    {
                        id: '01', name: "企划部", remark: "1989-01-12",
                        children: [
                            {
                                id: '0101', name: "企划分部一", remark: "企划分部一"
                            },
                            {
                                id: '0102', name: "企划分部二", remark: "企划分部二", children: [
                                {
                                    id: '010201', name: "企划分部二 A组", remark: "企划分部二 A组", children: [
                                    {id: '01020101', name: "企划分部二 A组1分队", remark: "企划分部二 A组1分队"},
                                    {id: '01020102', name: "企划分部二 A组2分队", remark: "企划分部二 A组2分队"}
                                ]
                                },
                                {id: '010202', name: "企划分部二 B组", remark: "企划分部二 B组"}
                            ]
                            },
                            {id: '0103', name: "企划分部三", remark: "企划分部三"}
                        ]
                    },
                    {id: '02', name: "研发部", remark: "研发部"},
                    {id: '03', name: "产品部", remark: "产品部"}
                ]
            };
            var a = $("#treeGrid").ligerGrid({
                    columns: [
                        {display: '部门名', name: 'name', id: 'id1', align: 'left'},
                        {display: '部门标示', name: 'id', type: 'int', align: 'left'},

                        {display: '部门描述', name: 'remark', align: 'left'},
                        {
                            display: '操作', isSort: false,
                            render: function (rowdata, rowindex, value) {
                                var h = "";
                                h += "<a class='add'>添加</a> ";
                                h += "<a  class='del' data-id='"+ rowindex +"'>删除</a> ";
                                return h;
                            }
                        }
                    ], width: '90%',allowHideColumn: false, rownumbers: true, colDraggable: true, rowDraggable: true,checkbox:true,
                    data: TreeDeptData, alternatingRow: false, isChecked: f_isChecked,tree: {columnId: 'id1'}
                }
            );
            $("#searchBtn").on("click", function ()
            {
                debugger;
                a.collapseAll();
                a.expandAll();
                a.options.data = TreeDeptData;
                a.loadData(getWhere());
            })

            //返回筛选行的方法
            function getWhere() {
                if (!a) return null;

                var clause = function (rowdata, rowindex)
                {
                    var value = false;
                    var key = $("#searchContent").val();
                    var subclause = function(rowdata){
                        if (rowdata.name.indexOf(key) > -1)
                        {
                            value = true;
                        }else if(rowdata.children){
                            $.each(rowdata.children, function (i, item) {
                                subclause(item);
                            })
                        }
                    }
                    subclause(rowdata);
                    return value;
                };
                return clause;
            }

            //选中复选框方法
            function getSelect() {
                var test = ["企划分部二","企划分部三"];
                $(".l-grid-tree-content").each(function ( ) {
                    for (var index in test)
                    {
                        if(test[index]==$(this).text()){
                            a.select($(this).closest("tr")[0]);
                        }
                    }
                });
//                $.each($(".l-grid-tree-content"),function () {
//                    for (var index in test)
//                    {
//                        if(index==$(this).text()){
//                            a.select($(this).closest("tr"));
//                        }
//                    }
//                })
//                a.select(4);
            }

            //复选框初始化
            function f_isChecked(rowdata)
            {
                var result = false;
                var test = ["企划分部二","企划分部三"];
                for (var index in test)
                {
                    if (rowdata.name == test[index])
                        result =  true;
                }
                return result;
            }
        })
    </script>
</head>
<body>
<form id="inputForm" action="${ctx}/material/testMaterial/insert" method="post" class="form-horizontal">

</form>
<div id="standardEdit">
    <%--故障检修标准--%>
    <div id="repaireStdFromC" style="display: none;">
        <div>
            <ul class="fl">
                <li class="l-fieldcontainer l-fieldcontainer-first">
                    <ul>
                        <li style="width:120px;text-align:right;">检修编码</li>
                        <li style="width:200px;text-align:right;">
                            <div class="l-text" style="width: 198px;">
                                <input type="text"  name="fault_standard_code" class="l-text-field" style="width: 194px;">
                                <div class="l-text-l"></div>
                                <div class="l-text-r"></div>
                            </div>
                        </li>
                        <li style="width:50px;"></li>
                    </ul>
                </li>
            </ul>
            <ul class="fl">
                <li class="l-fieldcontainer l-fieldcontainer-first">
                    <ul>
                        <li style="width:120px;text-align:right;">检修描述</li>
                        <li style="width:200px;text-align:right;">
                            <div class="l-text" style="width: 198px;">
                                <input type="text"  name="fault_standard_description" class="l-text-field" style="width: 194px;">
                                <div class="l-text-l"></div>
                                <div class="l-text-r"></div>
                            </div>
                        </li>
                        <li style="width:50px;"></li>
                    </ul>
                </li>
            </ul>
            <ul class="fl">
                <li class="l-fieldcontainer l-fieldcontainer-first">
                    <ul>
                        <li style="width:120px;text-align:right;">检修说明</li>
                        <li style="width:200px;text-align:right;">
                            <div class="l-text" style="width: 198px;">
                                <input type="text"  name="fault_standard_explain" class="l-text-field" style="width: 194px;">
                                <div class="l-text-l"></div>
                                <div class="l-text-r"></div>
                            </div>
                        </li>
                        <li style="width:50px;"></li>
                    </ul>
                </li>
            </ul>
            <ul class="fl">
            <li class="l-fieldcontainer l-fieldcontainer-first">
                <ul>
                    <li style="width:120px;text-align:right;">备注</li>
                    <li style="width:200px;text-align:right;">
                        <div class="l-text" style="width: 198px;">
                            <input type="text"  name="fault_standard_remark" class="l-text-field" style="width: 194px;">
                            <div class="l-text-l"></div>
                            <div class="l-text-r"></div>
                        </div>
                    </li>
                    <li style="width:50px;"></li>
                </ul>
            </li>
            </ul>

            <input class="btnDel" type="button" value="删除"/>
            <input class="btnAdd" type="button" value="新增"/>
        </div>
    </div>
    <table id="repaireStdFromCTable" class="table table-bordered table-hover mytable" style="display: none">
        <thead>
            <tr>
                <th>检修编码</th>
                <th>检修描述</th>
                <th>检修说明</th>
                <th>备注</th>
                <th>下拉框</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
        </tbody>
    </table>
    <div class="editDiv">
        <%--<input type="button" class="l-button" style="" value="删除行" onclick="deleteRow()"/>--%>
        <%--<input type="button" class="l-button" style="" value="添加行" onclick="addNewRow()"/>--%>
        <%--<div class="l-clear"></div>--%>
        <div class="subeditDiv" id="maingrid"></div>
        <div class="subeditDiv" id="maingrid1"></div>
        <%--<input type="button" class="l-button" value="获取选中的值(选择行)" onclick="getSelected()"></input>--%>
        <%--<input type="button" class="l-button" value="获取当前的值" onclick="getData()"></input>--%>
    </div>
    <%--保养标准--%>
    <div id="maintenceStdFromC" style="display: none;">
        <div>
            <ul class="fl">
                <li class="l-fieldcontainer l-fieldcontainer-first">
                    <ul>
                        <li style="width:120px;text-align:right;">保养编码</li>
                        <li style="width:200px;text-align:right;">
                            <div class="l-text" style="width: 198px;">
                                <input type="text"  name="maintain_standard_code" class="l-text-field"  style="width: 194px;">
                                <div class="l-text-l"></div>
                                <div class="l-text-r"></div>
                            </div>
                        </li>
                        <li style="width:50px;"></li>
                    </ul>
                </li>
            </ul>
            <ul class="fl">
                <li class="l-fieldcontainer l-fieldcontainer-first">
                    <ul>
                        <li style="width:120px;text-align:right;">保养描述</li>
                        <li style="width:200px;text-align:right;">
                            <div class="l-text" style="width: 198px;">
                                <input type="text"  name="maintain_standard_description" class="l-text-field"  style="width: 194px;">
                                <div class="l-text-l"></div>
                                <div class="l-text-r"></div>
                            </div>
                        </li>
                        <li style="width:50px;"></li>
                    </ul>
                </li>
            </ul>
            <ul class="fl">
                <li class="l-fieldcontainer l-fieldcontainer-first">
                    <ul>
                        <li style="width:120px;text-align:right;">保养说明</li>
                        <li style="width:200px;text-align:right;">
                            <div class="l-text" style="width: 198px;">
                                <input type="text"  name="maintain_standard_explain" class="l-text-field"  style="width: 194px;">
                                <div class="l-text-l"></div>
                                <div class="l-text-r"></div>
                            </div>
                        </li>
                        <li style="width:50px;"></li>
                    </ul>
                </li>
            </ul>
            <ul class="fl">
            <li class="l-fieldcontainer l-fieldcontainer-first">
                <ul>
                    <li style="width:120px;text-align:right;">备注</li>
                    <li style="width:200px;text-align:right;">
                        <div class="l-text" style="width: 198px;">
                            <input type="text"  name="maintain_standard_remark" class="l-text-field" style="width: 194px;">
                            <div class="l-text-l"></div>
                            <div class="l-text-r"></div>
                        </div>
                    </li>
                    <li style="width:50px;"></li>
                </ul>
            </li>
            </ul>
            <input class="btnDel" type="button" value="删除"/>
            <input class="btnAdd" type="button" value="新增"/>
        </div>
    </div>
</div>
<table id="mytable1" class="table table-bordered table-hover mytable">
    <thead>
    <tr>
        <th>物料编码1</th>
        <th>物料名称1</th>
        <th>库存数量1</th>
        <th>单位1</th>
        <th>标准成本1</th>
        <th>物料类别1</th>
        <%--<th>物料编码11</th>--%>
        <%--<th>物料名称11</th>--%>
        <%--<th>库存数量11</th>--%>
        <%--<th>单位11</th>--%>
        <%--<th>标准成本11</th>--%>
        <%--<th>物料类别11</th>--%>
    </tr>
    </thead>
</table>
<table id="mytable2" class="table table-bordered table-hover mytable">
    <thead>
    <tr>
        <th>物料编码2</th>
        <th>物料名称2</th>
        <th>库存数量2</th>
        <th>单位2</th>
        <th>标准成本2</th>
        <th>物料类别2</th>
    </tr>
    </thead>
</table>
<div class="editDiv">
    <input type="text" id="searchContent"/>
    <input type="button" id="searchBtn" value="查询"/>
    <div class="subeditDiv" id="treeGrid"></div>
</div>
<div class="form-actions">
    <input id="btnSubmit" type="button" value="保 存"/>
</div>
<div>


</div>
</body>
</html>
