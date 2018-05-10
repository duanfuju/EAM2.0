--获取设备类别树的节点等级、路径过程

--drop PROCEDURE dbo.setDevCatagoryLevel
CREATE procedure dbo.setDevCatagoryLevel
AS
BEGIN  
DECLARE @level INT
DECLARE @leaf char(1)
SET @level = 0
SET @leaf = '0'
update a
set a.cat_level = b.level,a.cat_seq = b.scort, a.isleaf = b.leaf
from eam_dev_category as a inner join 
(SELECT cat_id,@level level,@leaf leaf,concat(',',cat_id,',') scort FROM eam_dev_category 
 WHERE cat_pid is null or cat_pid = '') as b on a.cat_id = b.cat_id
 where a.isdelete = '0'
 
 WHILE @@Rowcount > 0 
begin 
  SET @level = @level + 1
update a
set a.cat_level = b.level,a.cat_seq = concat(b.cat_seq,a.cat_id,','), a.isleaf = b.leaf
from eam_dev_category as a inner join 
(SELECT cat_id,@level level,@leaf leaf,cat_seq FROM eam_dev_category 
 WHERE cat_level = @level - 1) as b on a.cat_pid = b.cat_id 
  where a.isdelete = '0'
 END
update a
set a.isleaf = '1' from eam_dev_category as a 
where not exists (select 1 from eam_dev_category b where a.cat_id = b.cat_pid) 
END


--获取空间信息树的节点等级、路径过程

--drop PROCEDURE dbo.setDevLocationLevel
CREATE procedure dbo.setDevLocationLevel
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


--更新物料类别树的节点等级、路径、是否叶子节点过程
--drop procedure dbo.setMaterialTypeLevel
CREATE procedure dbo.setMaterialTypeLevel
AS
BEGIN  
DECLARE @level INT
DECLARE @leaf char(1)
SET @level = 0
SET @leaf = '0'
update a
set a.type_level = b.level,a.type_seq = b.scort, a.isleaf = b.leaf
from eam_material_type as a inner join 
(SELECT type_id,@level level,@leaf leaf,concat(',',type_id,',') scort FROM eam_material_type 
 WHERE type_pid is null or type_pid = '') as b on a.type_id = b.type_id
  where a.isdelete = '0'
 
 WHILE @@Rowcount > 0 
begin 
  SET @level = @level + 1
update a
set a.type_level = b.level,a.type_seq = concat(b.type_seq,a.type_id,','), a.isleaf = b.leaf
from eam_material_type as a inner join 
(SELECT type_id,@level level,@leaf leaf,type_seq FROM eam_material_type 
 WHERE type_level = @level - 1) as b on a.type_pid = b.type_id 
  where a.isdelete = '0'
 END
update a
set a.isleaf = '1' from eam_material_type as a 
where not exists (select 1 from eam_material_type b where a.type_id = b.type_pid) 
END