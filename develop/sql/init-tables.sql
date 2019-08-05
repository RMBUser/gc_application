/* 初始化t_gc_db_category表*/
INSERT INTO `t_gc_db_category` (`CATEGORY_TYPE`) VALUES ('上海可回收垃圾');
INSERT INTO `t_gc_db_category` (`CATEGORY_TYPE`) VALUES ('上海有害垃圾');
INSERT INTO `t_gc_db_category` (`CATEGORY_TYPE`) VALUES ('上海湿垃圾');
INSERT INTO `t_gc_db_category` (`CATEGORY_TYPE`) VALUES ('上海干垃圾');

/* 初始化t_gc_db_garbage_info表*/
INSERT INTO t_gc_db_garbage_info(GARBAGE_NAME) SELECT g.garbage FROM garbage g;

/* 初始化t_gc_bu_garbage_classification表*/
INSERT INTO t_gc_bu_garbage_classification(GARBAGE_ID,CATEGORY_ID) (SELECT t.GARBAGE_ID,g.type FROM garbage g JOIN t_gc_db_garbage_info t ON BINARY g.garbage = BINARY t.GARBAGE_NAME);

/* 我们用3代表湿垃圾，4代表干垃圾*/
UPDATE t_gc_bu_garbage_classification gc SET gc.CATEGORY_ID = 3 WHERE gc.CATEGORY_ID = 4;
UPDATE t_gc_bu_garbage_classification gc SET gc.CATEGORY_ID = 4 WHERE gc.CATEGORY_ID = 8;