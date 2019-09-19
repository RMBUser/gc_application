-- 因为desc是mysql的关键字，会引起冲突
ALTER TABLE `t_gc_db_garbage_info`
CHANGE COLUMN `DESC` `GARBAGE_DESC`  varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '垃圾描述' AFTER `GARBAGE_NAME`;
-- 因为explain是mysql的关键字，会引起冲突
ALTER TABLE `t_gc_db_category`
CHANGE COLUMN `EXPLAIN` `CATEGORY_EXPLAIN`  varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类解释' AFTER `CATEGORY_TYPE`;

