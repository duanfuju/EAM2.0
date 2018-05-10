--报修工单字段必填调整 171128-wangjl
UPDATE eam_menu_pages SET content=
'
[{"displayName":"ID","editable":"true","fieldName":"id_key","gridWidth":100,"marginType":"hidden","nullable":"true","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"工单编码","editable":"false","fieldName":"order_code","gridWidth":100,"marginType":"text","nullable":"false","showInForm":"true","showInGrid":"true","showInSearch":"true","validateType":""},{"displayName":"填单人员","editable":"false","fieldName":"creator","gridWidth":100,"marginType":"labelText","nullable":"true","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"报修电话","editable":"true","fieldName":"notifier_tel","gridWidth":100,"marginType":"text","nullable":"false","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"报修人员","editable":"true","fieldName":"notifier","gridWidth":100,"marginType":"text","nullable":"false","showInForm":"true","showInGrid":"true","showInSearch":"true","validateType":""},{"displayName":"报修部门","editable":"true","fieldName":"notifier_dept","gridWidth":100,"marginType":"combobox","nullable":"true","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"人员工号","editable":"true","fieldName":"notifier_no","gridWidth":100,"marginType":"text","nullable":"true","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"报修位置","editable":"true","fieldName":"notifier_loc","gridWidth":100,"marginType":"combobox","nullable":"false","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"报修设备","editable":"true","fieldName":"order_device","gridWidth":100,"marginType":"combobox","nullable":"false","showInForm":"true","showInGrid":"true","showInSearch":"true","validateType":""},{"displayName":"故障现象","editable":"true","fieldName":"notifier_appearance","gridWidth":100,"marginType":"text","nullable":"true","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"报修来源","editable":"false","fieldName":"notifier_source","gridWidth":100,"marginType":"text","nullable":"true","showInForm":"true","showInGrid":"true","showInSearch":"false","validateType":""},{"displayName":"处理方式","editable":"true","fieldName":"order_level","gridWidth":100,"marginType":"select","nullable":"true","showInForm":"true","showInGrid":"true","showInSearch":"false","validateType":""},{"displayName":"期望解决时间","editable":"true","fieldName":"order_expect_time","gridWidth":100,"marginType":"dateTime","nullable":"false","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"具体地点","editable":"true","fieldName":"detail_location","gridWidth":100,"marginType":"text","nullable":"true","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"备注","editable":"true","fieldName":"notifier_remark","gridWidth":100,"marginType":"text","nullable":"true","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"故障来源","editable":"true","fieldName":"order_source","gridWidth":100,"marginType":"combobox","nullable":"true","showInForm":"false","showInGrid":"true","showInSearch":"true","validateType":""},{"displayName":"工单状态","editable":"true","fieldName":"order_status","gridWidth":100,"marginType":"combobox","nullable":"false","showInForm":"false","showInGrid":"true","showInSearch":"true","validateType":""},{"displayName":"计划开始时间","editable":"true","fieldName":"order_plan_start_time","gridWidth":100,"marginType":"dateTime","nullable":"true","showInForm":"false","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"计划结束时间","editable":"true","fieldName":"order_plan_end_time","gridWidth":100,"marginType":"dateTime","nullable":"true","showInForm":"false","showInGrid":"false","showInSearch":"false","validateType":""}]
'
WHERE menu_id=1069;

--我的发起流程页面请求地址 171128-caoh
UPDATE ioms_menu SET link='modules/act/actMyRequestList' WHERE menuno=1703;


--物料信息、类别模块字段必填调整 171121-caoh
UPDATE eam_menu_pages SET content=
'
[{"displayName":"物料类别编码","editable":"true","fieldName":"type_code","gridWidth":100,"marginType":"text","nullable":"false","showInForm":"true","showInGrid":"true","showInSearch":"true","validateType":""},{"displayName":"物料类别名称","editable":"true","fieldName":"type_name","gridWidth":100,"marginType":"text","nullable":"false","showInForm":"true","showInGrid":"true","showInSearch":"true","validateType":""},{"displayName":"ID","editable":"true","fieldName":"id_key","gridWidth":100,"marginType":"hidden","nullable":"true","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"类别ID","editable":"true","fieldName":"type_id","gridWidth":100,"marginType":"hidden","nullable":"true","showInForm":"true","showInGrid":"false","showInSearch":"false"},{"displayName":"父级类别","editable":"true","fieldName":"type_pid","gridWidth":100,"marginType":"combobox","nullable":"true","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"上层类别编码","editable":"true","fieldName":"type_pcode","gridWidth":100,"marginType":"text","nullable":"false","showInForm":"true","showInGrid":"true","showInSearch":"false"},{"displayName":"上层类别名称","editable":"true","fieldName":"type_pname","gridWidth":100,"marginType":"text","nullable":"true","showInForm":"false","showInGrid":"true","showInSearch":"false"},{"displayName":"类别描述","editable":"true","fieldName":"type_description","gridWidth":100,"marginType":"text","nullable":"true","showInForm":"true","showInGrid":"true","showInSearch":"false","validateType":""},{"displayName":"状态","editable":"true","fieldName":"type_status","gridWidth":100,"marginType":"select","nullable":"false","showInForm":"true","showInGrid":"true","showInSearch":"false","validateType":""}]
'

WHERE menu_id=1103;


UPDATE eam_menu_pages SET content=
'
[{"displayName":"ID","editable":"true","fieldName":"id_key","gridWidth":100,"marginType":"hidden","nullable":"true","showInForm":"true","showInGrid":"false","showInSearch":"false"},{"displayName":"物料编码","editable":"true","fieldName":"material_code","gridWidth":100,"marginType":"text","nullable":"false","showInForm":"true","showInGrid":"true","showInSearch":"true"},{"displayName":"物料名称","editable":"true","fieldName":"material_name","gridWidth":100,"marginType":"text","nullable":"false","showInForm":"true","showInGrid":"true","showInSearch":"true"},{"displayName":"物料类别","editable":"true","fieldName":"material_type","gridWidth":100,"marginType":"combobox","nullable":"false","showInForm":"true","showInGrid":"true","showInSearch":"false"},{"displayName":"计量单位","editable":"true","fieldName":"material_unit","gridWidth":100,"marginType":"text","nullable":"false","showInForm":"true","showInGrid":"true","showInSearch":"false"},{"displayName":"库存数量","editable":"true","fieldName":"material_qty","gridWidth":100,"marginType":"text","nullable":"false","showInForm":"true","showInGrid":"true","showInSearch":"false"},{"displayName":"采购方式","editable":"true","fieldName":"material_purchasing","gridWidth":100,"marginType":"select","nullable":"false","showInForm":"true","showInGrid":"true","showInSearch":"true"},{"displayName":"标准成本(元)","editable":"true","fieldName":"material_cost","gridWidth":100,"marginType":"text","nullable":"false","showInForm":"true","showInGrid":"true","showInSearch":"false"},{"displayName":"销售价格(元)","editable":"true","fieldName":"material_price","gridWidth":100,"marginType":"text","nullable":"true","showInForm":"true","showInGrid":"true","showInSearch":"false"},{"displayName":"品牌","editable":"true","fieldName":"material_brand","gridWidth":100,"marginType":"text","nullable":"true","showInForm":"true","showInGrid":"true","showInSearch":"false"},{"displayName":"供应商","editable":"true","fieldName":"material_supplier","gridWidth":100,"marginType":"select","nullable":"true","showInForm":"true","showInGrid":"true","showInSearch":"false"},{"displayName":"备注","editable":"true","fieldName":"material_remark","gridWidth":100,"marginType":"text","nullable":"true","showInForm":"true","showInGrid":"true","showInSearch":"false"},{"displayName":"状态","editable":"true","fieldName":"material_status","gridWidth":100,"marginType":"select","nullable":"false","showInForm":"true","showInGrid":"true","showInSearch":"false"},{"displayName":"重要程度","editable":"true","fieldName":"material_level","gridWidth":100,"marginType":"select","nullable":"false","showInForm":"true","showInGrid":"true","showInSearch":"true"},{"displayName":"规格型号","editable":"true","fieldName":"material_model","gridWidth":100,"marginType":"text","nullable":"true","showInForm":"true","showInGrid":"true","showInSearch":"false"}]
'

WHERE menu_id=1104;




--物料信息模块添加数据字典 171121-caoh
INSERT INTO eam_dict VALUES (NEWID(), 'material_purchasing', '采购方式', '物料采购方式', '0', '自购', '');
INSERT INTO eam_dict VALUES (NEWID(), 'material_purchasing', '采购方式', '物料采购方式', '1', '统购', '');

INSERT INTO eam_dict VALUES (NEWID(), 'material_level', '重要程度', '物料重要程度', '0', 'A类	', '');
INSERT INTO eam_dict VALUES (NEWID(), 'material_level', '重要程度', '物料重要程度', '1', 'B类	', '');
INSERT INTO eam_dict VALUES (NEWID(), 'material_level', '重要程度', '物料重要程度', '2', 'C类	', '');


-- add by wangjinlong at 20171120 for : app报修需要增加工单图片视频两个字段；
ALTER table eam_fault_order add notifier_photo varchar(1000) COMMENT '报修上传图片';
ALTER table eam_fault_order add notifier_video varchar(1000) COMMENT '报修上传视频';

-- wangjinlong end

--物料类别、信息编码自动获取 171120-caoh
INSERT INTO eam_sequence VALUES (newid(), 'MATERIAL_TYPE', 'WLLB', 'yyyyMMdd', '1', '4', '1', NULL, NULL, '0', '2017-11-20');
INSERT INTO eam_sequence VALUES (newid(), 'MATERIAL_INFO', 'WLXX', 'yyyyMMdd', '1', '4', '1', NULL, NULL, '0', '2017-11-21');


-- add by wujh at 20171125 for: 物料类型新增是否叶子节点以及节点路径两个字段；
alter table eam_material_type add type_seq varchar(8000);
alter table eam_material_type add isleaf char(1);
alter table eam_material_type add type_level varchar(10);
---end wujh 20171125
-- add by suven at 20171127 for: 缺陷保修详情按钮；
INSERT INTO ioms_button(sysno,menuno, buttonno, buttonname, subsystemno, orderid) VALUES (NEWID(), '1069', '1069_detail', '详情', 'eam', '6');
INSERT INTO ioms_rolebutton(sysno, rolecode, buttonno, subsystemno,menuno) VALUES (NEWID(), 'SYSADMIN', '1069_detail', 'eam', '1069');
---end suven 20171127


----保养月计划-11/29---------------zww
alter TABLE eam_maintain_project_sub ADD project_producedate varchar(50);
alter TABLE eam_maintain_project_sub ADD projectyear_id VARCHAR(100);


----- #17649 【系统统一问题】列表中按钮顺序需要调换为详情、编辑、删除 --20171130------shufq
UPDATE ioms_button SET orderid = 10 WHERE buttonname ='查看详情'
UPDATE ioms_button SET orderid = 10 WHERE buttonname ='详情'
UPDATE ioms_button SET orderid = 11 WHERE buttonname ='编辑'
UPDATE ioms_button SET orderid = 12 WHERE buttonname ='删除'
---- end  shufq 20171130
----------保养年计划 11/30-----------zww
alter TABLE eam_maintain_project ADD project_produceyear varchar(50);
alter TABLE eam_maintain_project ADD projectset_id VARCHAR(100);
alter TABLE eam_maintain_project_sub ADD projectset_id VARCHAR(100);


