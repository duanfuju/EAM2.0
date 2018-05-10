------liwenlong  12-07 增加供应商类型的"模版下载" 和 "导入"两个按钮
INSERT INTO ioms_button VALUES (NEWID(), '1098', '1098_download', '模版下载',  'eam',  '2');
INSERT INTO ioms_button VALUES (NEWID(), '1098', '1098_import', '导入',  'eam',  '3');

INSERT INTO ioms_rolebutton VALUES (NEWID(), 'administrators', '1098_download', 'eam',  '1098');
INSERT INTO ioms_rolebutton VALUES (NEWID(), 'SYSADMIN', '1098_download', 'eam',  '1098');
