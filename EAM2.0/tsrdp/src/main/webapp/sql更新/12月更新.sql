----------巡检子项字段名称修改 12/06-----------dfj
update eam_menu_field set display_name='空间信息' where menu_id='1091' and field_name ='device_location';

----------空间信息字段名称修改 12/05-----------dfj
select * from eam_menu_field where field_name='area_device_location_names';
update eam_menu_field set display_name='空间信息' where field_name='area_device_location_names';







----------s设备信息 12/01-----------zww
update eam_menu_field set field_type='ztree' where menu_id='1062' and field_name='dev_location';
update eam_menu_field set field_type='ztree' where menu_id='1062' and field_name='cat_name';
update eam_menu_field set field_type='ztree' where menu_id='1062' and field_name='cat_id';
update eam_menu_field set field_type='ztree' where menu_id='1061' and field_name='loc_pid';
update eam_menu_field set field_type='ztree' where menu_id='1081' and field_name='project_bm';

--报修工单字段必填调整 171201-wangjl
UPDATE eam_menu_pages SET content=
'
[{"displayName":"ID","editable":"true","fieldName":"id_key","gridWidth":100,"marginType":"hidden","nullable":"true","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"工单编码","editable":"false","fieldName":"order_code","gridWidth":100,"marginType":"text","nullable":"false","showInForm":"true","showInGrid":"true","showInSearch":"true","validateType":""},{"displayName":"填单人员","editable":"false","fieldName":"creator","gridWidth":100,"marginType":"labelText","nullable":"true","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"报修电话","editable":"true","fieldName":"notifier_tel","gridWidth":100,"marginType":"text","nullable":"false","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"报修人员","editable":"true","fieldName":"notifier","gridWidth":100,"marginType":"text","nullable":"false","showInForm":"true","showInGrid":"true","showInSearch":"true","validateType":""},{"displayName":"报修部门","editable":"true","fieldName":"notifier_dept","gridWidth":100,"marginType":"combobox","nullable":"true","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"人员工号","editable":"true","fieldName":"notifier_no","gridWidth":100,"marginType":"text","nullable":"true","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"报修位置","editable":"true","fieldName":"notifier_loc","gridWidth":100,"marginType":"combobox","nullable":"false","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"报修设备","editable":"true","fieldName":"order_device","gridWidth":100,"marginType":"combobox","nullable":"false","showInForm":"true","showInGrid":"true","showInSearch":"true","validateType":""},{"displayName":"故障现象","editable":"true","fieldName":"notifier_appearance","gridWidth":100,"marginType":"text","nullable":"true","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"报修来源","editable":"false","fieldName":"notifier_source","gridWidth":100,"marginType":"text","nullable":"true","showInForm":"true","showInGrid":"true","showInSearch":"false","validateType":""},{"displayName":"处理方式","editable":"true","fieldName":"order_level","gridWidth":100,"marginType":"select","nullable":"true","showInForm":"true","showInGrid":"true","showInSearch":"false","validateType":""},{"displayName":"期望解决时间","editable":"true","fieldName":"order_expect_time","gridWidth":100,"marginType":"dateTime","nullable":"false","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"具体地点","editable":"true","fieldName":"detail_location","gridWidth":100,"marginType":"text","nullable":"true","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"备注","editable":"true","fieldName":"notifier_remark","gridWidth":100,"marginType":"text","nullable":"true","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"故障来源","editable":"true","fieldName":"order_source","gridWidth":100,"marginType":"combobox","nullable":"true","showInForm":"false","showInGrid":"true","showInSearch":"true","validateType":""},{"displayName":"工单状态","editable":"true","fieldName":"order_status","gridWidth":100,"marginType":"combobox","nullable":"false","showInForm":"false","showInGrid":"true","showInSearch":"true","validateType":""},{"displayName":"计划开始时间","editable":"true","fieldName":"order_plan_start_time","gridWidth":100,"marginType":"dateTime","nullable":"true","showInForm":"false","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"计划结束时间","editable":"true","fieldName":"order_plan_end_time","gridWidth":100,"marginType":"dateTime","nullable":"true","showInForm":"false","showInGrid":"false","showInSearch":"false","validateType":""}]
'
WHERE menu_id=1069;

update ioms_menu set link = 'modules/faultOrder/faultStatistic' where menuno = 1070;
--end 报修工单字段必填调整 171201-wangjl

-- 物料类别、物料信息模块下拉树改ztree-caoh-1201
UPDATE eam_menu_pages set content=
'[
    {
        "displayName": "物料类别编码",
        "editable": "true",
        "fieldName": "type_code",
        "gridWidth": 100,
        "marginType": "text",
        "nullable": "false",
        "showInForm": "true",
        "showInGrid": "true",
        "showInSearch": "true",
        "validateType": ""
    },
    {
        "displayName": "物料类别名称",
        "editable": "true",
        "fieldName": "type_name",
        "gridWidth": 100,
        "marginType": "text",
        "nullable": "false",
        "showInForm": "true",
        "showInGrid": "true",
        "showInSearch": "true",
        "validateType": ""
    },
    {
        "displayName": "ID",
        "editable": "true",
        "fieldName": "id_key",
        "gridWidth": 100,
        "marginType": "hidden",
        "nullable": "true",
        "showInForm": "true",
        "showInGrid": "false",
        "showInSearch": "false",
        "validateType": ""
    },
    {
        "displayName": "类别ID",
        "editable": "true",
        "fieldName": "type_id",
        "gridWidth": 100,
        "marginType": "hidden",
        "nullable": "true",
        "showInForm": "true",
        "showInGrid": "false",
        "showInSearch": "false"
    },
    {
        "displayName": "父级类别",
        "editable": "true",
        "fieldName": "type_pid",
        "gridWidth": 100,
        "marginType": "ztree",
        "nullable": "true",
        "showInForm": "true",
        "showInGrid": "false",
        "showInSearch": "false",
        "validateType": ""
    },
    {
        "displayName": "上层类别编码",
        "editable": "true",
        "fieldName": "type_pcode",
        "gridWidth": 100,
        "marginType": "text",
        "nullable": "false",
        "showInForm": "true",
        "showInGrid": "true",
        "showInSearch": "false"
    },
    {
        "displayName": "上层类别名称",
        "editable": "true",
        "fieldName": "type_pname",
        "gridWidth": 100,
        "marginType": "text",
        "nullable": "true",
        "showInForm": "false",
        "showInGrid": "true",
        "showInSearch": "false"
    },
    {
        "displayName": "类别描述",
        "editable": "true",
        "fieldName": "type_description",
        "gridWidth": 100,
        "marginType": "text",
        "nullable": "true",
        "showInForm": "true",
        "showInGrid": "true",
        "showInSearch": "false",
        "validateType": ""
    },
    {
        "displayName": "状态",
        "editable": "true",
        "fieldName": "type_status",
        "gridWidth": 100,
        "marginType": "select",
        "nullable": "false",
        "showInForm": "true",
        "showInGrid": "true",
        "showInSearch": "false",
        "validateType": ""
    }
]'
WHERE menu_id='1103';

UPDATE eam_menu_pages set content=
'[
    {
        "displayName": "ID",
        "editable": "true",
        "fieldName": "id_key",
        "gridWidth": 100,
        "marginType": "hidden",
        "nullable": "true",
        "showInForm": "true",
        "showInGrid": "false",
        "showInSearch": "false"
    },
    {
        "displayName": "物料编码",
        "editable": "true",
        "fieldName": "material_code",
        "gridWidth": 100,
        "marginType": "text",
        "nullable": "false",
        "showInForm": "true",
        "showInGrid": "true",
        "showInSearch": "true"
    },
    {
        "displayName": "物料名称",
        "editable": "true",
        "fieldName": "material_name",
        "gridWidth": 100,
        "marginType": "text",
        "nullable": "false",
        "showInForm": "true",
        "showInGrid": "true",
        "showInSearch": "true"
    },
    {
        "displayName": "物料类别",
        "editable": "true",
        "fieldName": "material_type",
        "gridWidth": 100,
        "marginType": "ztree",
        "nullable": "false",
        "showInForm": "true",
        "showInGrid": "true",
        "showInSearch": "false"
    },
    {
        "displayName": "计量单位",
        "editable": "true",
        "fieldName": "material_unit",
        "gridWidth": 100,
        "marginType": "text",
        "nullable": "false",
        "showInForm": "true",
        "showInGrid": "true",
        "showInSearch": "false"
    },
    {
        "displayName": "库存数量",
        "editable": "true",
        "fieldName": "material_qty",
        "gridWidth": 100,
        "marginType": "text",
        "nullable": "false",
        "showInForm": "true",
        "showInGrid": "true",
        "showInSearch": "false"
    },
    {
        "displayName": "采购方式",
        "editable": "true",
        "fieldName": "material_purchasing",
        "gridWidth": 100,
        "marginType": "select",
        "nullable": "false",
        "showInForm": "true",
        "showInGrid": "true",
        "showInSearch": "true"
    },
    {
        "displayName": "标准成本",
        "editable": "true",
        "fieldName": "material_cost",
        "gridWidth": 100,
        "marginType": "text",
        "nullable": "false",
        "showInForm": "true",
        "showInGrid": "true",
        "showInSearch": "false"
    },
    {
        "displayName": "销售价格",
        "editable": "true",
        "fieldName": "material_price",
        "gridWidth": 100,
        "marginType": "text",
        "nullable": "true",
        "showInForm": "true",
        "showInGrid": "true",
        "showInSearch": "false"
    },
    {
        "displayName": "品牌",
        "editable": "true",
        "fieldName": "material_brand",
        "gridWidth": 100,
        "marginType": "text",
        "nullable": "true",
        "showInForm": "true",
        "showInGrid": "true",
        "showInSearch": "false"
    },
    {
        "displayName": "供应商",
        "editable": "true",
        "fieldName": "material_supplier",
        "gridWidth": 100,
        "marginType": "select",
        "nullable": "true",
        "showInForm": "true",
        "showInGrid": "true",
        "showInSearch": "false"
    },
    {
        "displayName": "备注",
        "editable": "true",
        "fieldName": "material_remark",
        "gridWidth": 100,
        "marginType": "text",
        "nullable": "true",
        "showInForm": "true",
        "showInGrid": "true",
        "showInSearch": "false"
    },
    {
        "displayName": "状态",
        "editable": "true",
        "fieldName": "material_status",
        "gridWidth": 100,
        "marginType": "select",
        "nullable": "false",
        "showInForm": "true",
        "showInGrid": "true",
        "showInSearch": "false"
    },
    {
        "displayName": "重要程度",
        "editable": "true",
        "fieldName": "material_level",
        "gridWidth": 100,
        "marginType": "select",
        "nullable": "false",
        "showInForm": "true",
        "showInGrid": "true",
        "showInSearch": "true"
    },
    {
        "displayName": "规格型号",
        "editable": "true",
        "fieldName": "material_model",
        "gridWidth": 100,
        "marginType": "text",
        "nullable": "true",
        "showInForm": "true",
        "showInGrid": "true",
        "showInSearch": "false"
    },
    {
        "displayName": "员工信息",
        "editable": "true",
        "fieldName": "project_empid",
        "gridWidth": 100,
        "marginType": "select",
        "nullable": "true",
        "showInForm": "true",
        "showInGrid": "true",
        "showInSearch": "false",
        "validateType": ""
    }
]'
WHERE menu_id='1104';


-- 业务字典表结构
create table dbo.eam_business_dict(
	id varchar(50) primary key NOT NULL,
	dict_pid varchar(50),
	dict_seq varchar(8000),
	dict_level int,
	ifleaf char(1),
	dict_value varchar(50),
	dict_name varchar(100),
	dict_desc varchar(200),
	dict_status varchar(10)
);
create unique index eam_business_dict_index on eam_dict(id);

--新增字段设置
INSERT INTO eam_menu_field (id, menu_id, field_name, display_name, field_type, is_basic, sort_order, validate_type) VALUES (newid(), '1510', 'dict_pname', '上级字典枚举名称', 'text', '1', '7', null);
INSERT INTO eam_menu_field (id, menu_id, field_name, display_name, field_type, is_basic, sort_order, validate_type) VALUES (newid(), '1510', 'dict_status', '状态', 'text', '1', '17', Null);
INSERT INTO eam_menu_field (id, menu_id, field_name, display_name, field_type, is_basic, sort_order, validate_type) VALUES (newid(), '1510', 'dict_name', '业务字典值名称', 'text', '1', '13', Null);
INSERT INTO eam_menu_field (id, menu_id, field_name, display_name, field_type, is_basic, sort_order, validate_type) VALUES (newid(), '1510', 'dict_pdesc', '上级字典值描述', 'text', '1', '9', Null);
INSERT INTO eam_menu_field (id, menu_id, field_name, display_name, field_type, is_basic, sort_order, validate_type) VALUES (newid(), '1510', 'dict_desc', '业务字典值描述', 'text', '1', '15', Null);
INSERT INTO eam_menu_field (id, menu_id, field_name, display_name, field_type, is_basic, sort_order, validate_type) VALUES (newid(), '1510', 'id_key', 'ID', 'hidden', '1', '1', Null);
INSERT INTO eam_menu_field (id, menu_id, field_name, display_name, field_type, is_basic, sort_order, validate_type) VALUES (newid(), '1510', 'dict_pid', '父级字典类型编码', 'ztree', '1', '3', Null);
INSERT INTO eam_menu_field (id, menu_id, field_name, display_name, field_type, is_basic, sort_order, validate_type) VALUES (newid(), '1510', 'dict_pvalue', '上级字典枚举值', 'text', '1', '5', Null);
INSERT INTO eam_menu_field (id, menu_id, field_name, display_name, field_type, is_basic, sort_order, validate_type) VALUES (newid(), '1510', 'dict_value', '业务字典枚举值', 'text', '1', '11', Null);


-- 新增业务字段菜单页的按钮设置
INSERT INTO ioms_button (sysno, menuno, buttonno, buttonname, subsystemno, orderid) VALUES (newid(), '1510', '1510_closed', '作废', 'eam', '7');
INSERT INTO ioms_button (sysno, menuno, buttonno, buttonname, subsystemno, orderid) VALUES (newid(), '1510', '1510_detail', '查看', 'eam', '1');
INSERT INTO ioms_button (sysno, menuno, buttonno, buttonname, subsystemno, orderid) VALUES (newid(), '1510', '1510_add', '增加', 'eam', null);
INSERT INTO ioms_button (sysno, menuno, buttonno, buttonname, subsystemno, orderid) VALUES (newid(), '1510', '1510_edit', '编辑', 'eam', '3');

--分配权限
INSERT INTO ioms_rolebutton (sysno, rolecode, buttonno, subsystemno, menuno) VALUES (newid(), 'sysadmin', '1510_closed', 'eam', '1510');
INSERT INTO ioms_rolebutton (sysno, rolecode, buttonno, subsystemno, menuno) VALUES (newid(), 'sysadmin', '1510_detail', 'eam', '1510');
INSERT INTO ioms_rolebutton (sysno, rolecode, buttonno, subsystemno, menuno) VALUES (newid(), 'sysadmin', '1510_add', 'eam', '1510');
INSERT INTO ioms_rolebutton (sysno, rolecode, buttonno, subsystemno, menuno) VALUES (newid(), 'sysadmin', '1510_edit', 'eam', '1510');

--更新业务字典菜单路径
update ioms_menu set link = 'modules/eamsys/sysConfig/businessDict' where menuno = '1503'

-- 新增设备信息中的空间信息名称字段
INSERT INTO eam_menu_field (id, menu_id, field_name, display_name, field_type, is_basic, sort_order, validate_type) VALUES (newid(), '1062', 'loc_name', '空间名称', 'text', '1', '14', Null);

--新增巡检任务编码
alter table eam_inspection_task add task_code varchar(50);
insert into eam_sequence(id, seq_type, seq_const, seq_timestamp, serial_no, seq_length,seq_incre, seq_clean, seq_currenttime) values
(newid(), 'INSPECTION_TASK', 'XJBM', 'yyyyMMdd', 1, 4, 1, '0', GETDATE())

--修改物料信息中字段类型-caoh-1205
ALTER table eam_material alter column material_cost varchar(50);
ALTER table eam_material alter column material_price varchar(50);

-- 修改设备类别表字段设置
update eam_menu_field set display_name = '上级设备类别' where menu_id = '1060' and field_name = 'cat_pid'

--zww供应商
insert into ioms_rolebutton VALUES(NEWID(),'sysadmin','1052_supplierdev','eam','1052');
insert into ioms_button VALUES(NEWID(),'1052','1052_supplierdev','供应设备详情','eam','2');
insert into eam_button_ext(id,buttonno,icon) VALUES(NEWID(),'supplierdev','fa-list');

------zww保养月计划
insert into eam_menu_field values (NEWID(),'1084','project_producedate','保养月份','text','1','13',null);


------liwenlong  12-07 增加供应商类型的"模版下载" 和 "导入"两个按钮
INSERT INTO ioms_button VALUES (NEWID(), '1098', '1098_download', '模版下载',  'eam',  '2');
INSERT INTO ioms_button VALUES (NEWID(), '1098', '1098_import', '导入',  'eam',  '3');

INSERT INTO ioms_rolebutton VALUES (NEWID(), 'administrators', '1098_download', 'eam',  '1098');
INSERT INTO ioms_rolebutton VALUES (NEWID(), 'SYSADMIN', '1098_download', 'eam',  '1098');



---- 故障报修脚本控件改为ztree  wjl
UPDATE eam_menu_pages SET content=
'[{"displayName":"ID","editable":"true","fieldName":"id_key","gridWidth":100,"marginType":"hidden","nullable":"true","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"工单编码","editable":"false","fieldName":"order_code","gridWidth":100,"marginType":"text","nullable":"false","showInForm":"true","showInGrid":"true","showInSearch":"true","validateType":""},{"displayName":"填单人员","editable":"false","fieldName":"creator","gridWidth":100,"marginType":"labelText","nullable":"true","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"报修电话","editable":"true","fieldName":"notifier_tel","gridWidth":100,"marginType":"text","nullable":"false","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"报修人员","editable":"true","fieldName":"notifier","gridWidth":100,"marginType":"text","nullable":"false","showInForm":"true","showInGrid":"true","showInSearch":"true","validateType":""},{"displayName":"报修部门","editable":"true","fieldName":"notifier_dept","gridWidth":100,"marginType":"ztree","nullable":"true","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"人员工号","editable":"true","fieldName":"notifier_no","gridWidth":100,"marginType":"text","nullable":"true","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"报修位置","editable":"true","fieldName":"notifier_loc","gridWidth":100,"marginType":"ztree","nullable":"false","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"报修设备","editable":"true","fieldName":"order_device","gridWidth":100,"marginType":"ztree","nullable":"false","showInForm":"true","showInGrid":"true","showInSearch":"true","validateType":""},{"displayName":"故障现象","editable":"true","fieldName":"notifier_appearance","gridWidth":100,"marginType":"text","nullable":"true","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"报修来源","editable":"false","fieldName":"notifier_source","gridWidth":100,"marginType":"text","nullable":"true","showInForm":"true","showInGrid":"true","showInSearch":"false","validateType":""},{"displayName":"处理方式","editable":"true","fieldName":"order_level","gridWidth":100,"marginType":"select","nullable":"true","showInForm":"true","showInGrid":"true","showInSearch":"false","validateType":""},{"displayName":"期望解决时间","editable":"true","fieldName":"order_expect_time","gridWidth":100,"marginType":"dateTime","nullable":"false","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"具体地点","editable":"true","fieldName":"detail_location","gridWidth":100,"marginType":"text","nullable":"true","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"备注","editable":"true","fieldName":"notifier_remark","gridWidth":100,"marginType":"text","nullable":"true","showInForm":"true","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"故障来源","editable":"true","fieldName":"order_source","gridWidth":100,"marginType":"combobox","nullable":"true","showInForm":"false","showInGrid":"true","showInSearch":"true","validateType":""},{"displayName":"工单状态","editable":"true","fieldName":"order_status","gridWidth":100,"marginType":"combobox","nullable":"false","showInForm":"false","showInGrid":"true","showInSearch":"true","validateType":""},{"displayName":"计划开始时间","editable":"true","fieldName":"order_plan_start_time","gridWidth":100,"marginType":"dateTime","nullable":"true","showInForm":"false","showInGrid":"false","showInSearch":"false","validateType":""},{"displayName":"计划结束时间","editable":"true","fieldName":"order_plan_end_time","gridWidth":100,"marginType":"dateTime","nullable":"true","showInForm":"false","showInGrid":"false","showInSearch":"false","validateType":""}]'
WHERE menu_id=1069;

---- 更新设备信息页面显示字段名称  wujh  2017/12/07
update eam_menu_field set display_name = '空间信息' where menu_id = '1062' and field_name = 'loc_name';


-- wangjl 1208修改空间信息树层级修改的存储过程，适配导入需要
-- ----------------------------
-- Procedure structure for [dbo].[setDevLocationLevel]
-- ----------------------------
DROP PROCEDURE [dbo].[setDevLocationLevel]
GO
CREATE procedure [dbo].[setDevLocationLevel]
AS
BEGIN
DECLARE @level INT
DECLARE @leaf char(1)
SET @level = 0
SET @leaf = '0'
update a
set a.loc_level = b.level,a.loc_seq = b.scort, a.isleaf = b.leaf
from eam_dev_location as a inner join
(SELECT id,@level level,@leaf leaf,concat(',',id,',') scort FROM eam_dev_location
 WHERE loc_pid is null or loc_pid = '') as b on a.id = b.id
  where a.isdelete = '0'

 WHILE @@Rowcount > 0
begin
  SET @level = @level + 1
update a
set a.loc_level = b.level,a.loc_seq = concat(b.loc_seq,a.id,','), a.isleaf = b.leaf
from eam_dev_location as a inner join
(SELECT id,@level level,@leaf leaf,loc_seq FROM eam_dev_location
 WHERE loc_level = @level - 1) as b on a.loc_pid = b.id
 where a.isdelete = '0'
 END
update a
set a.isleaf = '1' from eam_dev_location as a
where not exists (select 1 from eam_dev_location b where a.id = b.loc_pid)
END


GO
-- --------------------------------------------
-- wangjl 1208修改空间信息loc_id字段大小
alter table eam_dev_location alter column loc_id varchar(50);
