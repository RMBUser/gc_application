-- ��Ϊdesc��mysql�Ĺؼ��֣��������ͻ
ALTER TABLE `t_gc_db_garbage_info`
CHANGE COLUMN `DESC` `GARBAGE_DESC`  varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '��������' AFTER `GARBAGE_NAME`;
-- ��Ϊexplain��mysql�Ĺؼ��֣��������ͻ
ALTER TABLE `t_gc_db_category`
CHANGE COLUMN `EXPLAIN` `CATEGORY_EXPLAIN`  varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '�������' AFTER `CATEGORY_TYPE`;

