
-- 系统配置表
insert into eam_sys_config(id,config_key,config_value,config_desc) VALUES (newid(),'QRCODE_TOP_LOGO','','二维码配置顶层图片地址');
insert into eam_sys_config(id,config_key,config_value,config_desc) VALUES (newid(),'QRCODE_TITLE','','二维码标题（项目名称）');
insert into eam_sys_config(id,config_key,config_value,config_desc) VALUES (newid(),'QRCODE_BOT_LOGO','','二维码中间图片地址');

insert into eam_sys_config(id,config_key,config_value,config_desc) VALUES (newid(),'REPAIR_ORDER_DISP_TYPE','','维修工单派单方式');
insert into eam_sys_config(id,config_key,config_value,config_desc) VALUES (newid(),'REPARI_ORDEDR_QRAB_ORDERS','','维修工单运维人员最多处理工单数');
insert into eam_sys_config(id,config_key,config_value,config_desc) VALUES (newid(),'REPARI_ORDEDR_QRAB_TIMEOUT','','维修工单抢单时限');

insert into eam_sys_config(id,config_key,config_value,config_desc) VALUES (newid(),'MAINT_ORDER_DISP_TYPE','','保养工单派单方式');
insert into eam_sys_config(id,config_key,config_value,config_desc) VALUES (newid(),'INSPECT_ORDER_DISP_TYPE','','巡检工单派单方式');

insert into eam_sys_config(id,config_key,config_value,config_desc) VALUES (newid(),'STATISTICS_LEVEL','','统计级别配置');



